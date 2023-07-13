package com.goodjob.article.domain.s3.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.file.entity.File;
import com.goodjob.article.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final FileService fileService;

    public void uploadFile(Article article, Map<String, MultipartFile> fileMap) throws IOException {
        List<String> imgUrlList = new ArrayList<>();

        for(String inputName : fileMap.keySet()) {
            MultipartFile multipartFile = fileMap.get(inputName);

            // 파일이 선택되지 않은 경우 처리
            if (multipartFile.isEmpty()) {
                continue; // 다음 파일로 넘어감
            }

            String[] inputNameBits = inputName.split("__");
            Long fileNo = Long.parseLong(inputNameBits[1]);

            String fileName = createFileName(multipartFile.getOriginalFilename());

            //파일 형식 구하기
            String ext = fileName.split("\\.")[1];
            String contentType = "";

            //content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
            switch (ext) {
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                case "png":
                    contentType = "image/png";
                    break;
                case "gif":
                    contentType = "image/gif";
                    break;
            }

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);

                amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (AmazonServiceException e) {
                e.printStackTrace();
            } catch (SdkClientException e) {
                e.printStackTrace();
            }

            //object 정보 가져오기
            ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);
            List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

            String url = amazonS3.getUrl(bucket, fileName).toString();

            imgUrlList.add(url);

            fileService.save(article, url, fileNo, fileName);

        }
    }

    // 이미지파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".gif");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        fileValidate.add(".GIF");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new RuntimeException("지원하지 않는 파일 형식입니다");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void deleteFiles(Article article, Map<String, String> params) {
        List<String> deleteFilesArgs = params.keySet()
                .stream()
                .filter(key -> key.startsWith("delete___"))
                .map(key -> key.replace("delete___", ""))
                .collect(Collectors.toList());

        deleteFiles(article, deleteFilesArgs);
    }

    public void deleteFiles(Article article, List<String> params) {
        params
                .stream()
                .forEach(key -> {
                    String[] keyBits = key.split("__");

                    int fileNo = Integer.parseInt(keyBits[1]);

                    Optional<File> fileOp = fileService.findByArticleAndFileNo(article, fileNo);

                    if (fileOp.isPresent()) {
                        fileService.delete(fileOp.get());
                        deleteS3(fileOp.get().getFileName());
                    }
                });
    }

    // DeleteObject를 통해 S3 파일 삭제
    public void deleteS3(String fileName){
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }
}
