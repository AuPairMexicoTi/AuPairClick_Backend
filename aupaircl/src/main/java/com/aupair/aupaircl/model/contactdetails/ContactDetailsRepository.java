package com.aupair.aupaircl.model.contactdetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactDetailsRepository extends JpaRepository<ContactDetails, UUID> {
}
