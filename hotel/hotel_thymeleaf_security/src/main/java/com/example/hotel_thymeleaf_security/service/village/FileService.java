package com.example.hotel_thymeleaf_security.service.village;

import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.FileEntity;
import com.example.hotel_thymeleaf_security.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

public interface FileService extends BaseService<FileEntity, MultipartFile> {
}
