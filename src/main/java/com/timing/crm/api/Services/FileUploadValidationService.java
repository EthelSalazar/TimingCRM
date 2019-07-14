package com.timing.crm.api.Services;

import com.timing.crm.api.Helper.ValidatorHelper;
import com.timing.crm.api.Repository.LeadRepository;
import com.timing.crm.api.View.ErrorLead;
import com.timing.crm.api.View.FileUploadResponse;
import com.timing.crm.api.View.Lead;

import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.timing.crm.api.Utils.Constants.INVALID_FILE_CONTENT;
import static com.timing.crm.api.Utils.Constants.XLS;
import static com.timing.crm.api.Utils.Constants.XLSX;

@Service
public class FileUploadValidationService {

    private final Logger fileUploadLogger = LoggerFactory.getLogger("FileUploadValidationService");

    @Autowired
    LeadRepository leadRepository;

    @Async("fileUploadExecutor")
    public CompletableFuture<FileUploadResponse> validateFileFormat(MultipartFile file) throws InterruptedException {

        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        fileUploadLogger.info("Starting file format sub-process");
        String fileName = (file != null) ? file.getOriginalFilename() : null;
        String fileExtension;
        if (fileName != null && fileName.length() > 0 && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            if (!fileExtension.equalsIgnoreCase(XLSX) && !fileExtension.equalsIgnoreCase(XLS)) {
                fileUploadResponse.setTotalLines(0);
                fileUploadResponse.setMessage("Invalid file format extension");
                fileUploadLogger.error("Invalid File Format Extension: {}", fileExtension);
            }
        }
        fileUploadLogger.info("Completing file format sub-process");
        return CompletableFuture.completedFuture(fileUploadResponse);
    }

    @Async("fileUploadExecutor")
    public CompletableFuture<FileUploadResponse> processingFileContent(MultipartFile file, Integer repId,
            Integer companyId, Integer employeeId, Integer fileUploadId) throws
            InterruptedException {

        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        List<Lead> leadList = new ArrayList<>();
        Lead lead;
        List<ErrorLead> leadInErrorList = new ArrayList<>();
        ErrorLead errorLead;
        fileUploadLogger.info("Starting data validation / processing sub-process");

        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            // first sheet in the file
            Sheet inputSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = inputSheet.iterator();
            Row currentRow;
            // Skipping the first line of the file (header)
            iterator.next();
            List<Cell> cellList;
            String cellValue = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Boolean inError;
            while (iterator.hasNext()) {
                inError = false;
                lead = new Lead();
                currentRow = iterator.next();
                if (!checkIfRowIsEmpty(currentRow)) {
                    Iterator<Cell> cellIterator = currentRow.iterator();

                    cellList = new ArrayList<>();
                    cellIterator.forEachRemaining(cellList::add);

                    for (int i = 0; i < cellList.size(); i++) {

                        try {

                            // Checks for Fecha Data (optional)
                            if (i == 0) {
                                try {
                                    cellValue = sdf.format(cellList.get(i).getDateCellValue());
                                    lead.setDateData(cellValue);
                                } catch (Exception e) {
                                    fileUploadLogger.error("Invalid Date in line: {}, {}. Saving value as is.",
                                            currentRow.getRowNum(), cellList.get(i).getStringCellValue());
                                    // Saving input date as String
                                    lead.setDateData(cellList.get(i).getStringCellValue());
                                }
                                // Skipping column Nro.
                            } else if (i == 1) {
                                // Do nothing

                                // Checks for Rating (optional)
                            } else if (i == 2) {
                                try {
                                    cellValue = "";
                                    cellValue = cellList.get(i).getStringCellValue();
                                    if (!ValidatorHelper.isValidRating(cellValue)) {
                                        fileUploadLogger.error("Invalid Rating in line: {}, {}. Saving value as is.", currentRow
                                                .getRowNum(), cellList.get(i).getStringCellValue());
                                    }
                                }catch (Exception e) {
                                    fileUploadLogger.error("Invalid Rating in line: {}. Saving value as is.", currentRow.getRowNum());
                                }
                                lead.setRating(cellValue);
                                // Checks for Name (mandatory)
                            } else if (i == 3) {
                                if (cellList.get(i).getCellTypeEnum() != CellType.STRING) {
                                    fileUploadLogger.error("Invalid Name in line: {}, {}", currentRow.getRowNum(),
                                            cellList.get(i).getStringCellValue());
                                    fileUploadResponse.setMessage("Invalid Name in line: " + currentRow.getRowNum());
                                    errorLead = getErrorDetails(currentRow.getRowNum(), "Invalid Name", cellList.get
                                            (i).getStringCellValue());
                                    leadInErrorList.add(errorLead);
                                    inError = true;
                                    break;
                                }
                                lead.setName(cellList.get(i).getStringCellValue());
                                // Checks for Phone (mandatory)
                            } else if (i == 6) {
                                if (cellList.get(i).getCellTypeEnum() != CellType.STRING
                                        && cellList.get(i).getCellTypeEnum() != CellType.NUMERIC) {
                                    fileUploadLogger.error("Invalid Phone in line: {}, {}", currentRow.getRowNum(),
                                            cellList.get(i).getNumericCellValue());
                                    fileUploadResponse.setMessage("Invalid Phone in line: " + currentRow.getRowNum());
                                    errorLead = getErrorDetails(currentRow.getRowNum(), "Invalid Phone number",
                                            cellList.get(i).getStringCellValue());
                                    leadInErrorList.add(errorLead);
                                    inError = true;
                                    break;
                                }
                                if (cellList.get(i).getCellTypeEnum() == CellType.NUMERIC) {
                                    lead.setPhone(NumberToTextConverter.toText(cellList.get(i).getNumericCellValue()));
                                } else {
                                    lead.setPhone(cellList.get(i).getStringCellValue());
                                }
                                // Checks for Zip code (optional)
                            } else if (i == 7) {
                                try {
                                    cellValue = NumberToTextConverter.toText(cellList.get(i).getNumericCellValue());
                                    if ((cellValue.equalsIgnoreCase("0"))) cellValue = "";
                                } catch (Exception e) {
                                    fileUploadLogger.error("Invalid Zip Code in line: {}, {}. Saving values as is.", currentRow.getRowNum(),
                                            cellList.get(i).getStringCellValue());
                                    cellValue = cellList.get(i).getStringCellValue();
                                }
                                lead.setZip(cellValue);

                                // Checks for approach type / tipo de prospeccion (optional)
                            } else if (i == 9) {
                                cellValue = cellList.get(i).getStringCellValue();
                                try {
                                    if (!cellValue.isEmpty()) {
                                        cellValue = String.valueOf(ValidatorHelper.getApproachCode(cellList.get(i)
                                                .getStringCellValue().toUpperCase()));
                                    } else {
                                        cellValue = null;
                                    }
                                } catch (Exception e) {
                                    fileUploadLogger.error("Invalid Approach Type value in line: {}, {}", currentRow.getRowNum(), cellList.get(i).getStringCellValue());
                                    fileUploadResponse.setMessage("Invalid Approach Type value in line: " + currentRow.getRowNum());
                                    errorLead = getErrorDetails(currentRow.getRowNum(), "Invalid Approach Type value",
                                            cellList.get(i).getStringCellValue());
                                    leadInErrorList.add(errorLead);
                                    inError = true;
                                    break;

                                }
                                lead.setApproachType(cellValue);

                                // prospect category / categoria de prospecto (optional)
                            } else if (i == 10) {
                                cellValue = cellList.get(i).getStringCellValue();
                                try {
                                    if (!cellValue.isEmpty()) {
                                        cellValue = String.valueOf(ValidatorHelper.getApproachCode(cellList.get(i).getStringCellValue().toUpperCase()));
                                    } else {
                                        cellValue = null;
                                    }
                                } catch (Exception e) {
                                    fileUploadLogger.error("Invalid Prospect Category value in line: {}, {}", currentRow.getRowNum(), cellList.get(i).getStringCellValue());
                                    fileUploadResponse.setMessage("Invalid Prospect Category value in line: " + currentRow.getRowNum());
                                    errorLead = getErrorDetails(currentRow.getRowNum(), "Invalid Prospect Category value", cellList.get(i).getStringCellValue());
                                    leadInErrorList.add(errorLead);
                                    inError = true;
                                    break;
                                }
                                lead.setProspectCategory(cellValue);

                                // Checks for remains columns
                            } else {
                                if (cellList.get(i).getCellTypeEnum() != CellType.STRING
                                        && cellList.get(i).getCellTypeEnum() != CellType.BLANK) {
                                    fileUploadLogger.error("Invalid data in line: {}, {}", currentRow.getRowNum(),
                                            cellList.get(i).getStringCellValue());
                                    fileUploadResponse.setMessage("Invalid file content, wrong data in line: " + 
                                            currentRow.getRowNum());
                                    errorLead = getErrorDetails(currentRow.getRowNum(), "Invalid content",
                                            cellList.get(i).getStringCellValue());
                                    leadInErrorList.add(errorLead);
                                    inError = true;
                                    break;
                                }
                                lead = leadMapper(lead, cellList.get(i).getStringCellValue(), i);
                            }
                        } catch (IllegalStateException e) {
                            errorLead = getErrorDetails(currentRow.getRowNum(), "Invalid content", cellList.get(i).getStringCellValue());
                            leadInErrorList.add(errorLead);
                            fileUploadLogger.error(e.getMessage());
                            inError = true;
                            break;
                        }

                    }

                    if (!inError) {
                        // Inserting lead and return any duplication detail if exists
                        errorLead = leadRepository.insertLeadDetails(lead, repId, companyId, employeeId, fileUploadId,
                                currentRow.getRowNum());
                        // Check for duplication and add into the error list
                        if (errorLead.getLineNumber() != null) {
                            leadInErrorList.add(errorLead);
                        } else {
                            leadList.add(lead);
                        }
                    }
                }
            }
        // Critical error, stop processing
        } catch (IOException e) {
            fileUploadResponse.setTotalLinesSuccessful(0);
            fileUploadResponse.setTotalLinesError(0);
            fileUploadResponse.setTotalLines(0);
            fileUploadResponse.setMessage(INVALID_FILE_CONTENT);
            fileUploadLogger.error(e.getMessage());
        } catch (Exception e) {
            fileUploadLogger.error(e.getMessage());
            e.printStackTrace();
        }

        // Duplicates are in the leadList also
        fileUploadResponse.setTotalLinesSuccessful(leadList.size());
        fileUploadResponse.setTotalLinesError(leadInErrorList.size());
        fileUploadResponse.setTotalLines(fileUploadResponse.getTotalLinesSuccessful()+fileUploadResponse.getTotalLinesError());
        fileUploadResponse.setLeads(leadList);
        fileUploadResponse.setLeadsInError(leadInErrorList);
        fileUploadLogger.info("Completing data validation sub-process");
        return CompletableFuture.completedFuture(fileUploadResponse);
    }

    private ErrorLead getErrorDetails(int rowNum, String message, String value) {
        ErrorLead errorLead = new ErrorLead();
        errorLead.setLineValue(value);
        errorLead.setErrorMessage(message);
        errorLead.setLineNumber(rowNum);
        return errorLead;
    }

    private Lead leadMapper(Lead lead, String token, int i) {
        switch (i) {
            case 4: lead.setMaritalStatus(token);
                    break;
            case 5: lead.setSpouseName(token);
                    break;
            case 8: lead.setCity(token);
                    break;
            case 9: lead.setApproachTypeDescription(token);
                    break;
            case 10: lead.setProspectCategoryDescription(token);
                    break;
            case 11: lead.setApproachPlace(token);
                    break;
            case 12: lead.setApproachNotes(token);
                    break;
            case 13: lead.setApproachNotes414(token);
                    break;
        }
        return lead;
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && !StringUtils.isEmpty(cell.toString())) {
                return false;
            }
        }
        return true;
    }
}
