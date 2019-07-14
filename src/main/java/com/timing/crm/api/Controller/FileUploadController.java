package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.AuthenticationService;
import com.timing.crm.api.Services.FileUploadService;
import com.timing.crm.api.View.ErrorLead;
import com.timing.crm.api.View.FileUploadResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static com.timing.crm.api.Utils.Constants.ERROR_FILE_SIZE;
import static com.timing.crm.api.Utils.Constants.JSON;

@RestController
@ControllerAdvice
@RequestMapping(value = "/v1")
public class FileUploadController extends ResponseEntityExceptionHandler {

    private final Logger fileUploadLogger = LoggerFactory.getLogger("FileUploadController");

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST, produces = JSON)
    public FileUploadResponse handleFileUpload(
            @Valid @RequestBody MultipartFile file,
            @RequestHeader(value = "token") UUID token,
            @RequestParam(value = "repUserId") Integer repUserId) throws MultipartException {

        if (!authenticationService.isAuthenticated(token)){
            fileUploadLogger.error("handleFileUpload() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }
        fileUploadLogger.info("Starting handleFileUpload");
        FileUploadResponse fileUploadResponse;
        fileUploadResponse = fileUploadService.fileUploadProcessing(file, token, repUserId);
        return fileUploadResponse;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMultipartException(HttpServletRequest httpServletRequest, Exception exception) {
        if (exception.getMessage().equalsIgnoreCase("Current request is not a multipart request")) {
            fileUploadLogger.error("Current request is not a Multipart request, file parameter is empty");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            fileUploadLogger.error(ERROR_FILE_SIZE);
            // Returning HTTP 413 Entity too large
            return new ResponseEntity(HttpStatus.PAYLOAD_TOO_LARGE);
        }
    }

    @RequestMapping(value = "/fileuploadErrors", method = RequestMethod.GET)
    @ApiOperation(value = "Returns records in error given a file upload id", notes = "Given a valid file upload Id "
            + "and returns its details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful get error details")}
    )
    public List<ErrorLead> getErrorDetails (
            @RequestHeader(value = "token") UUID token,
            @RequestParam(value = "fileUploadId") Integer fileUploadId) {

        if (!authenticationService.isAuthenticated(token)){
            fileUploadLogger.error("getErrorDetails() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }
        fileUploadLogger.info("Starting get file upload error details with fileUploadId: {}", fileUploadId);
        List<ErrorLead> errorLeadList;
        errorLeadList = fileUploadService.getFileUploadErrorDetails(fileUploadId);
        fileUploadLogger.info("Completing getting file upload error details with fileUploadId: {} ", fileUploadId);
        return errorLeadList;
    }

}
