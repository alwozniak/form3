package com.alwozniak.form3.repository;

import com.alwozniak.form3.domain.ChargesInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChargesInformationRepository extends JpaRepository<ChargesInformation, UUID> {
}
