package com.example.springsocial.repository;

import com.example.springsocial.model.WebSites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebSitesRepository extends JpaRepository<WebSites, Long> {

    Optional<WebSites> findById(Long id);
}
