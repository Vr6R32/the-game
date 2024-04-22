package com.thegame.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/static")
public class ResourceController {

    @Value("${static.files.path}")
    private String filesPath;

    @GetMapping(value = "avatar/default", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImage() {
        return new FileSystemResource(filesPath);
    }
}
