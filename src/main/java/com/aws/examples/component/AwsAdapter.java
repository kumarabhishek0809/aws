package com.aws.examples.component;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Component
@Log4j2
public class AwsAdapter {
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3Client;

    public URL storeObjectToS3(MultipartFile multipartFile,String fileName,String contentType){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(multipartFile.getSize());
        try {
            amazonS3Client.putObject(bucketName,fileName,multipartFile.getInputStream(),objectMetadata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return amazonS3Client.getUrl(bucketName, fileName);
    }

    public S3Object fetchObject(String awsFileName){
        S3Object s3Object = null;
        try{
            s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, awsFileName));
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return s3Object;
    }

    public void deleteObject(String key){
        amazonS3Client.deleteObject(bucketName,key);
    }

    public void createBucket(){
        String bucketName = "bucket-test";
        amazonS3Client.createBucket(bucketName, "ap-south-1");
    }
}
