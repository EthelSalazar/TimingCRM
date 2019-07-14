package com.timing.crm.api.Services;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.Repository.LeadRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.View.ErrorLead;
import com.timing.crm.api.View.FileUploadResponse;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.UserAndCompany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.timing.crm.api.Utils.Constants.INVALID_FILE_CONTENT;
import static com.timing.crm.api.Utils.Constants.STATUS_FAIL;
import static com.timing.crm.api.Utils.Constants.STATUS_OK;
import static com.timing.crm.api.Utils.Constants.STATUS_PARTIAL;
import static com.timing.crm.api.Utils.Constants.STATUS_RECEIVED;

@Service
public class FileUploadServiceImpl implements FileUploadService {

  private final Logger fileUploadLogger = LoggerFactory.getLogger("FileUploadServiceImpl");

  @Autowired
  FileUploadValidationService fileUploadValidationService;

  @Autowired
  LeadRepository leadRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public FileUploadResponse fileUploadProcessing(MultipartFile file, UUID token, Integer repId) {

    fileUploadLogger.info("Starting file upload process with employeeId token: {}", token);
    FileUploadResponse fileUploadResponse = new FileUploadResponse();
    List<Lead> leadList;
    List<ErrorLead> leadDuplicateList;
    List<ErrorLead> leadInErrorList;

    if (file==null) {
      throw new BadRequestException("Multipart file is null");
    }

    try {
      Integer employeeId;
      Integer companyId;
      UserAndCompany userAndCompany;

      //Getting employeeId and companyId based on the security token
      userAndCompany = userRepository.getUserByToken(token);
      employeeId = userAndCompany.getUserId();
      companyId = userAndCompany.getCompanyId();

      fileUploadLogger.info("Starting fileUploadProcessing method with employeeId: {} and repId: {}", employeeId,
              repId);
      fileUploadLogger.info("Multipart file: {} has a valid size: {} Bytes", file.getOriginalFilename(), file.getSize());
      long startFileUploadProcessing = Calendar.getInstance().getTime().getTime();

      // Create the file upload log with status 0
      Integer fileUploadId;
      fileUploadId = leadRepository.createFileUpload(file.getOriginalFilename());
      leadRepository.updateFileUploadStatus(fileUploadId, STATUS_RECEIVED);


      // Step 1. Validation / Processing at file level (format and content)
      fileUploadLogger.info("Step 1: Starting file format validation and processing file content data");
      CompletableFuture<FileUploadResponse> fileFormatValidation = fileUploadValidationService.validateFileFormat(file);
      CompletableFuture<FileUploadResponse> fileContentValidation =
              fileUploadValidationService.processingFileContent(file, repId, companyId, employeeId, fileUploadId);
      CompletableFuture.allOf(fileFormatValidation, fileContentValidation);
      fileUploadLogger.info("Step 1: Completing file format validation and processing file content data");

      // Checks for file format error and stop
      if (fileFormatValidation.get().getMessage()!=null) {
        leadRepository.updateFileUploadStatus(fileUploadId, STATUS_FAIL);
        fileUploadLogger.error("Throwing exception: File format error with fileUploadId: {}", fileUploadId);
        throw new BadRequestException(fileUploadResponse.getMessage());

      }
      // Checks for file content critical error and stop
      if (fileContentValidation.get().getMessage()!=null && fileContentValidation.get().getMessage().equalsIgnoreCase
              (INVALID_FILE_CONTENT)) {
        leadRepository.updateFileUploadStatus(fileUploadId, STATUS_FAIL);
        fileUploadLogger.error("Throwing exception: Invalid file content with fileUploadId: {}", fileUploadId);
        throw new BadRequestException(fileUploadResponse.getMessage());

      }

      // Extracting list of valid leads
      leadList = fileContentValidation.get().getLeads();
      // Extracting list of leads in error
      leadInErrorList = fileContentValidation.get().getLeadsInError();

      // Step 2. Inserting error details in DB (invalid and duplicate records)
      fileUploadLogger.info("Step 2: Starting Inserting error details into DB");
      leadRepository.insertErrorDetails(leadInErrorList, fileUploadId);
      fileUploadLogger.info("Step 2: Completing Inserting error details into DB");


      // Step 4. Preparing response
      fileUploadLogger.info("Step 3: Preparing response");
      if (leadList.size() == 0 && (leadInErrorList.size() >= 0)) {
        leadRepository.updateFileUploadStatus(fileUploadId, STATUS_FAIL);
        fileUploadResponse.setMessage("Failed file upload");
      } else if (leadList.size()>0 && leadInErrorList.size()==0) {
        leadRepository.updateFileUploadStatus(fileUploadId, STATUS_OK);
        fileUploadResponse.setMessage("Successfully file upload");
      } else if (leadList.size()>0 && leadInErrorList.size()>0) {
        fileUploadResponse.setMessage("Partially file upload");
        leadRepository.updateFileUploadStatus(fileUploadId, STATUS_PARTIAL);
      }

      fileUploadResponse.setTotalLinesSuccessful(leadList.size());
      fileUploadResponse.setTotalLinesError(leadInErrorList.size());
      fileUploadResponse.setTotalLines(fileUploadResponse.getTotalLinesSuccessful()+fileUploadResponse.getTotalLinesError());
      fileUploadResponse.setFileName(file.getOriginalFilename());
      fileUploadResponse.setFileUploadId(fileUploadId);
      leadRepository.updateFileUploadTotals(fileUploadId, fileUploadResponse.getTotalLines(), fileUploadResponse
              .getTotalLinesSuccessful(), fileUploadResponse.getTotalLinesError());
      fileUploadLogger.info("Step 3: Completing preparing response");

      long endFileUploadProcessing = Calendar.getInstance().getTime().getTime();
      fileUploadLogger.info("Completing fileUploadProcessing method in {} msecs for file: {}", endFileUploadProcessing
              - startFileUploadProcessing, file.getOriginalFilename());

    } catch (InterruptedException e) {
        fileUploadResponse.setMessage("Error while uploading file, please review the file and try again");
        fileUploadLogger.error(e.toString());
    } catch (ExecutionException e) {
        fileUploadResponse.setMessage("Error while uploading file, please review the file and try again");
        fileUploadLogger.error(e.toString());
    }

    return fileUploadResponse;
  }

  @Override
  public List<ErrorLead> getFileUploadErrorDetails(Integer fileUploadId) {
    List<ErrorLead> errorLeadList;
    errorLeadList = leadRepository.getErrorLeadsByFileId(fileUploadId);
    return errorLeadList;
  }

}
