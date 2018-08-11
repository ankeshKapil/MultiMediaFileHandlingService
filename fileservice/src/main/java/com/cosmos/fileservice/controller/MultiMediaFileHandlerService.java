package com.cosmos.fileservice.controller;

import com.cosmos.fileservice.service.FileStorageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@Api(value="File Operations")

public class MultiMediaFileHandlerService {

    private static Logger logger = LoggerFactory.getLogger(MultiMediaFileHandlerService.class);

    @Autowired
    private FileStorageService fileStorageService;
    
    @ApiOperation(value="This API lets you upload the multimedia file to server and return the processed file after executing supplied command on it")
    @PostMapping("/api/processfile")
    public ResponseEntity<Resource> fileHandler(@RequestHeader("Authorization") String authorization, @RequestPart("file") MultipartFile file,@RequestPart("commands") String commands,HttpServletRequest request){

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            logger.info("Content Type " + contentType);
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "Content-Type: audio/mpeg";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
