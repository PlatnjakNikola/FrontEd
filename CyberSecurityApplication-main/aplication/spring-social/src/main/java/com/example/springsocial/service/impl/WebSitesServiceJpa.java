package com.example.springsocial.service.impl;

import com.example.springsocial.model.WebSites;
import com.example.springsocial.repository.WebSitesRepository;
import com.example.springsocial.service.WebSitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSitesServiceJpa implements WebSitesService {

    @Autowired
    private WebSitesRepository webSitesRepository;
    @Override
    public List<WebSites> listAll() {
        return webSitesRepository.findAll();
    }
}
