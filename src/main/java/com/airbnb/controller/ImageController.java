package com.airbnb.controller;

import com.airbnb.entity.Images;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.ImagesRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private ImagesRepository imagesRepository;
    private PropertyRepository propertyRepository;
    private S3Service s3Service;

    public ImageController(ImagesRepository imagesRepository, PropertyRepository propertyRepository, S3Service s3Service) {
        this.imagesRepository = imagesRepository;
        this.propertyRepository = propertyRepository;
        this.s3Service = s3Service;
    }
    @PostMapping(path = "/upload/file/{bucketName}/property/{propertyId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?>uplodeFile(@RequestParam MultipartFile file,
                                            @PathVariable String bucketName,
                                            @PathVariable long propertyId,
                                            @AuthenticationPrincipal PropertyUser user){
        String imageUrl= s3Service.uploadFile(file,bucketName);
        Property property =propertyRepository.findById(propertyId).get();

        Images img =new Images();
        img.setImageUrl(imageUrl);
        img.setProperty(property);
        img.setPropertyUser(user);
        Images savedImage = imagesRepository.save(img);

        return new ResponseEntity<>(savedImage, HttpStatus.OK);
    }

}
