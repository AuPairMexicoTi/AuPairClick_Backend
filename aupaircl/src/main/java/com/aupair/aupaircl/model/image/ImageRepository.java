package com.aupair.aupaircl.model.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
List<Image> findByProfile_User_EmailAndProfile_IsApproved(String email, boolean isApproved);
}
