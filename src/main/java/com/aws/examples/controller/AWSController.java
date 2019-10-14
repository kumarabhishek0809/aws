package com.aws.examples.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.aws.examples.component.AwsAdapter;
import com.aws.examples.dto.CommonDto;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RestController
public class AWSController {

    @Autowired
    private AwsAdapter awsAdapter;

    @PostMapping
    public ResponseEntity<CommonDto> upload(@ModelAttribute MultipartFile file) {
        URL url = awsAdapter.storeObjectToS3(file, file.getOriginalFilename(), file.getContentType());
        return new ResponseEntity(new CommonDto("url", url.toString()),HttpStatus.OK);
    }

    @GetMapping
    public void getMedicalRecords(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        S3Object s3Object = awsAdapter.fetchObject(fileName);
        InputStream stream = s3Object.getObjectContent();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        IOUtils.copy(stream, response.getOutputStream());
    }
}
