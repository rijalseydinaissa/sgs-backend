package com.example.sgs_backend.infrastructure.upload;
import com.example.sgs_backend.application.upload.dto.UploadResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.UUID;

@Component
public class LocalFileStorageAdapter {
    private static final String BASE_PATH = "/var/sgs/uploads";
    
    public UploadResponse uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(BASE_PATH, folder, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return new UploadResponse(fileName, "/uploads/" + folder + "/" + fileName, file.getSize());
        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }
}
