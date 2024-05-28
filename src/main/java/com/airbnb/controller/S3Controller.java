package com.airbnb.controller;

import com.airbnb.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("s3bucket")
@CrossOrigin("*")
public class S3Controller {
    @Autowired
    S3Service s3Service;
    @PostMapping(path ="/upload/file/{bucketName}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String>uploadFile(@RequestParam MultipartFile file,@PathVariable String bucketName){
        return new ResponseEntity<>(s3Service.uploadFile(file,bucketName), HttpStatus.OK);
    }
}
