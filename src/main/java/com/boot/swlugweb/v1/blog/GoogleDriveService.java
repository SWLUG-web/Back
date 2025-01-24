package com.boot.swlugweb.v1.blog;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GoogleDriveService {
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

//    public static Drive getDriveService() throws GeneralSecurityException, IOException {
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }

    private final Drive driveService;

    public GoogleDriveService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.driveService = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                getCredentials(HTTP_TRANSPORT)
        ).setApplicationName(APPLICATION_NAME).build();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

//    public String uploadFile(MultipartFile imageFile) throws IOException {
//        Path tempFilePath = saveTempFile(imageFile);
//        File fileMetadata = new File();
//        fileMetadata.setName(imageFile.getOriginalFilename());
//
//        FileContent mediaContent = new FileContent(imageFile.getContentType(), tempFilePath.toFile());
//        File uploadedFile = driveService.files().create(fileMetadata, mediaContent).execute();
//
//        Files.delete(tempFilePath); // Remove temporary file
//        return uploadedFile.getId(); // Return Google Drive file ID
//    }

    public String uploadFile(MultipartFile imageFile) throws IOException {
        // MultipartFile을 임시 파일로 저장
        Path tempFilePath = saveTempFile(imageFile);

        // Google Drive File 클래스 사용하여 메타데이터 생성
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(imageFile.getOriginalFilename()); // setName 메서드 사용 가능

        // 파일 내용 설정
        FileContent mediaContent = new FileContent(imageFile.getContentType(), tempFilePath.toFile());

        // 파일을 Google Drive에 업로드
        com.google.api.services.drive.model.File uploadedFile = driveService.files()
                .create(fileMetadata, mediaContent)
                .setFields("id, webContentLink") // 필요한 필드만 가져오기
                .execute();

        // 임시 파일 삭제
        Files.delete(tempFilePath);

        // 업로드된 파일의 ID 또는 URL 반환
        return uploadedFile.getWebContentLink();
    }


    private Path saveTempFile(MultipartFile multipartFile) throws IOException {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path tempFile = tempDir.resolve(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile.toFile())) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

//        public String uploadFile(MultipartFile file) throws IOException, GeneralSecurityException {
//            Drive driveService = GoogleDriveServiceHelper.getDriveService(); // Google Drive API 서비스 인스턴스
//            File fileMetadata = new File();
//            fileMetadata.setName(file.getOriginalFilename());
//
//            FileContent mediaContent = new FileContent(file.getContentType(), convertMultipartFileToFile(file));
//            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
//                    .setFields("id, webContentLink")
//                    .execute();
//
//            return uploadedFile.getWebContentLink(); // Google Drive의 공개 URL 반환
//        }


//    public static void uploadFile(Drive service) throws IOException{
//        System.out.println("\n\n 파일 업로드 시작..");
//        Path currentWorkingDir = Paths.get("").toAbsolutePath();
//        File fileMetaData = new File();
//        fileMetaData.setName("test1.jpg"); //업로드 파일 이름
//        java.io.File f = new java.io.File(currentWorkingDir + "/files/test1.jpg");
//        FileContent fileContent = new FileContent("image/jpeg", f);
//        service.files().create(fileMetaData, fileContent).execute();
//    }

//    public String uploadFile(MultipartFile file) throws IOException {
//        UUID uuid = UUID.randomUUID();
//        String fileName = uuid.toString() + "_" + file.getOriginalFilename();
//        java.io.File tempFile = java.io.File.createTempFile(uuid.toString(), file.getOriginalFilename());
//        file.transferTo(tempFile);
//
//        File fileMetaData = new File();
//        fileMetaData.setName(fileName);
//
//        FileContent fileContent = new FileContent(file.getContentType(), tempFile);
//        File uploadedFile = driveService.files().create(fileMetaData, fileContent).setFields("id").execute();
//
//        tempFile.delete(); // 임시 파일 삭제
//
//        return "https://drive.google.com/uc?id=" + uploadedFile.getId();
//    }

    public void deleteFile(String fileId) throws IOException, GeneralSecurityException {
            Drive driveService = GoogleDriveServiceHelper.getDriveService();
            driveService.files().delete(fileId).execute();
        }

        private java.io.File convertMultipartFileToFile(MultipartFile file) throws IOException {
            java.io.File convFile = new java.io.File(file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
            }
            return convFile;
        }
    }

