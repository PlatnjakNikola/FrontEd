package com.example.springsocial.controller;

import com.example.springsocial.model.WebSites;
import com.example.springsocial.service.WebSitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/websites")
public class WebSitesController {
    @Autowired
    private WebSitesService webSitesService;

    @GetMapping("")
    public List<WebSites> listStudents() {
        return webSitesService.listAll();
    }
}
