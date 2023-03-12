package com.dhl.assetmanager.service;

import com.dhl.assetmanager.entity.Asset;
import com.dhl.assetmanager.exception.TechnicalException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportGenerationService {

    private static final List<String> COLUMN_HEADERS = List.of("Serial number", "Model", "Business unit", "Service Line",
            "Country", "Location", "Floor/Room/Desk", "State", "Create CI", "CREST Code", "Stockroom", "Loan Due Date",
            "Depreciation", "Depreciation effective date", "Acquisition Method", "Acquisition Date", "End of Lease",
            "Configuration Item", "Substate", "Keyboard Layout", "Phone Number", "Name", "Asset tag", "MAC Address",
            "Secondary MAC Address", "Assigned to", "Comments", "Description", "Reserved for", "Price",
            "Reselling price", "Assigned", "Managed by", "Owned by", "Company", "Installed", "Invoice number",
            "Order Number", "RMA", "Supplier", "BCA Number", "SAP Number", "AM Cost Center", "GL account",
            "Cost Center", "Cost Center Ownership", "Subsidized", "Subsidized Until", "Resale price",
            "Scheduled retirement", "Retired date", "Disposal reason", "Disposal Company", "Disposal Certificate",
            "Salvage value", "Residual date", "Depreciated amount", "Lease Number", "Warranty End Date", "Support group",
            "Supported by", "Multiple Users", "Alternate Users");

    private static final String POLAND = "Poland";
    private static final String EXPRESS = "Express";

    private static final int SERIAL_NUMBER_CELL = 0;
    private static final int MODEL_CELL = 1;
    private static final int BUSINESS_UNIT_CELL = 2;
    private static final int SERVICE_LINE_CELL = 3;
    private static final int COUNTRY_CELL = 4;
    private static final int LOCATION_CELL = 5;
    private static final int CREST_CODE_CELL = 9;
    private static final int STOCKROOM_CELL = 10;
    private static final int ASSIGNED_TO_CELL = 25;

    public byte[] generateReportBytes(List<Asset> assets) {
        XSSFWorkbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        var sheet = workbook.createSheet("Asset Data");

        createHeaders(sheet);
        fillRows(sheet, assets);

        return getBytes(workbook);
    }

    private void createHeaders(XSSFSheet sheet) {
        var headersRow = sheet.createRow(0);

        int cellNumber = 0;
        for (String header : COLUMN_HEADERS) {
            var cell = headersRow.createCell(cellNumber);
            cell.setCellValue(header);
            cellNumber++;
        }
    }

    private void fillRows(XSSFSheet sheet, List<Asset> assets) {
        int rowNumber = 1;
        for (Asset asset : assets) {
            var row = sheet.createRow(rowNumber);
            fillCells(row, asset);
            rowNumber++;
        }
    }

    private void fillCells(XSSFRow row, Asset asset) {
        row.createCell(SERIAL_NUMBER_CELL)
                .setCellValue(asset.getSerialNumber());
        row.createCell(MODEL_CELL)
                .setCellValue(asset.getModel().getMessage());
        row.createCell(BUSINESS_UNIT_CELL)
                .setCellValue(EXPRESS);
        row.createCell(SERVICE_LINE_CELL)
                .setCellValue(EXPRESS);
        row.createCell(COUNTRY_CELL)
                .setCellValue(POLAND);
        row.createCell(LOCATION_CELL)
                .setCellValue(asset.getLocation() != null ? asset.getLocation().getName() : null);
        row.createCell(CREST_CODE_CELL)
                .setCellValue(asset.getCrestCode());
        row.createCell(STOCKROOM_CELL)
                .setCellValue(asset.getStockroom() != null ? asset.getStockroom().getName() : null);
        row.createCell(ASSIGNED_TO_CELL)
                .setCellValue(asset.getCurrentUser() != null ? asset.getCurrentUser().getEmail() : null);
    }

    private byte[] getBytes(XSSFWorkbook workbook) {
        try {
            var byteOutputStream = new ByteArrayOutputStream();
            workbook.write(byteOutputStream);
            return byteOutputStream.toByteArray();
        } catch (IOException exception) {
            throw new TechnicalException();
        }
    }

}
