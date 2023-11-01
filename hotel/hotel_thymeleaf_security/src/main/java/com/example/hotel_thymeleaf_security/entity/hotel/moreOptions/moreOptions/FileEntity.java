package com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions;

import com.example.hotel_thymeleaf_security.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity extends BaseEntity {
    private String fileName;
    private String downloadUrl;
    private String fileType;
    private long size;
}
