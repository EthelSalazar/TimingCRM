package com.timing.crm.api.Services;

import com.timing.crm.api.View.ErrorLead;
import com.timing.crm.api.View.FileUploadResponse;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileUploadService {

  FileUploadResponse fileUploadProcessing(MultipartFile file, UUID userId, Integer repId);
  List<ErrorLead> getFileUploadErrorDetails(Integer fileUploadId);

}
