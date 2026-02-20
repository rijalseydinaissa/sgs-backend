package com.example.sgs_backend.api.upload;

import com.example.sgs_backend.api.common.ApiResponse;
import com.example.sgs_backend.application.upload.FileUploadService;
import com.example.sgs_backend.application.upload.dto.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService uploadService;
    
    @PostMapping("/product-image")
    public ResponseEntity<ApiResponse<UploadResponse>> uploadProductImage(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success(uploadService.uploadProductImage(file)));
    }
    
    @PostMapping("/expense-receipt")
    public ResponseEntity<ApiResponse<UploadResponse>> uploadExpenseReceipt(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success(uploadService.uploadExpenseReceipt(file)));
    }
}
