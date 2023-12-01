package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.villa.VillaRentEntity;
import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.FileEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository.FileUploadRepository;
import com.example.hotel_thymeleaf_security.repository.villa.VillaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final VillaRepository villaRepository;

    private final Path fileLocation;
    @Value("${file.upload-dir}")
    private String fileUploadDir;
    private final FileUploadRepository fileUploadRepository;
    @Autowired
    public FileServiceImpl(VillageService villageService, VillaRepository villaRepository, @Value("${file.upload-dir}") String fileUploadDir, FileUploadRepository fileUploadRepository) {
        this.villaRepository = villaRepository;
        this.fileLocation = Paths.get(fileUploadDir)
                .toAbsolutePath().normalize();
        this.fileUploadRepository = fileUploadRepository;
    }

    private String generateUniqueFileName(String fileExtension) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"));
        return timestamp + "." + fileExtension;
    }

    public Path downloadFile(String fileName) {
        return fileLocation.resolve(fileName);
    }

    @Override
    public FileEntity create(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = originalFileName.contains(".") ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1) : "";

        String uniqueFileName = generateUniqueFileName(fileExtension);
        Path targetLocation = fileLocation.resolve(uniqueFileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the file: " + originalFileName, e);
        }

        String downloadUrl = "/download/" + uniqueFileName;
        String fileUrl = fileLocation.resolve(uniqueFileName).toString();
        double fileSizeInKB = (double) file.getSize() / 1024;

        return fileUploadRepository.save(FileEntity.builder()
                .fileName(uniqueFileName)
                .fileType(fileExtension)
                .downloadUrl(downloadUrl)
                .fileUrl(fileUrl)
                .size(fileSizeInKB)
                .build());
    }

    @Override
    public FileEntity getById(UUID id) {
        return fileUploadRepository.findById(id).orElseThrow(()-> new DataNotFoundException("File not found by id"));
    }

    @Override
    public FileEntity update(MultipartFile multipartFile, UUID id) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        fileUploadRepository.deleteById(id);
    }

    @Override
    public Path getGeneralPhoto(UUID hotelId) {
        VillaRentEntity rentEntity = villaRepository.findById(hotelId).orElseThrow(()-> new DataNotFoundException("Hotel not found"));
        return Path.of(rentEntity.getImages().get(0).getFileUrl());
    }
}
