package com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Entity(name = "files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileEntity extends BaseEntity {
    private String fileName;
    private String downloadUrl;
    private String fileType;
    private String fileUrl;
    private double size;
}
