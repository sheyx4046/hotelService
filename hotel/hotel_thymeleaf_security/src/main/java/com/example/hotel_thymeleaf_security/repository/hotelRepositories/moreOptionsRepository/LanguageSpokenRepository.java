package com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository;


import com.example.hotel_thymeleaf_security.entity.hotel.moreOptions.moreOptions.LanguageSpoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LanguageSpokenRepository extends JpaRepository<LanguageSpoken, UUID> {
    LanguageSpoken findLanguageSpokenByLanguage(String language);
}
