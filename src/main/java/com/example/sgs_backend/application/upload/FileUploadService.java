package com.example.sgs_backend.application.upload;

import com.example.sgs_backend.application.upload.dto.UploadResponse;
import com.example.sgs_backend.infrastructure.upload.LocalFileStorageAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final LocalFileStorageAdapter storage;
    
    public UploadResponse uploadProductImage(MultipartFile file) {
        return storage.uploadFile(file, "products");
    }
    
    public UploadResponse uploadExpenseReceipt(MultipartFile file) {
        return storage.uploadFile(file, "receipts");
    }
}
