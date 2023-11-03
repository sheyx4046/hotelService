package com.example.hotel_thymeleaf_security.repository.hotelRepositories.moreOptionsRepository;

import com.example.hotel_thymeleaf_security.entity.village.moreOptions.moreOptions.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
