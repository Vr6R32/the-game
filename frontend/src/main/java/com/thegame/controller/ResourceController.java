package com.thegame.controller;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/static")
public class ResourceController {

//    @GetMapping(value = "avatar/{imageUrl}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public Resource getImage(@PathVariable String imageUrl) {
//        return new FileSystemResource("files/static/" + imageUrl);
//    }

    @GetMapping(value = "avatar/default", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImage() {
        return new FileSystemResource("app/files/static/avatar.jpg");
    }
}
