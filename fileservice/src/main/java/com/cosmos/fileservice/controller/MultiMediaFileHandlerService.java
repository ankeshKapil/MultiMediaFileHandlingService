package com.cosmos.fileservice.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.cosmos.fileservice.domain.Command;
import com.cosmos.fileservice.service.FileStorageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@Api(value="File Operations")

public class MultiMediaFileHandlerService {

    private static Logger logger = LoggerFactory.getLogger(MultiMediaFileHandlerService.class);

    @Autowired
    private FileStorageService fileStorageService;
    
    @ApiOperation(value="This API lets you upload the multimedia file to server and return the processed file after executing supplied command on it")
    @PostMapping("/api/processfile")
    public ResponseEntity<Resource> fileHandler(@RequestHeader("Authorization") String authorization, @RequestPart("file") MultipartFile file,@RequestPart("commands") String command,HttpServletRequest request,
    		HttpServletResponse response) throws IOException{

        String fileName = fileStorageService.storeFile(file);

        String modifiedFilepath=fileStorageService.executeCommand(command, fileName);
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(modifiedFilepath)
//                .toUriString();

        Resource resource = fileStorageService.loadFileAsResource(modifiedFilepath);
        String contentType = null;
        contentType = request.getServletContext().getMimeType(modifiedFilepath);
		logger.info("Content Type " + contentType);

        // Fallback to the default content type if type could not be determined
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+modifiedFilepath)
              .body(resource);
        
        
    }
}
