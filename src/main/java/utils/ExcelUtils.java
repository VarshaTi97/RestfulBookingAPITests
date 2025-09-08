package utils;

import constants.ExcelConstants;
import exceptions.ExcelExceptions;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojoModels.BookingDates;
import pojoModels.BookingDetails;

import java.io.*;
import java.util.*;

public class ExcelUtils {
    private Workbook workbook;
    private Sheet sheet;
    private FileOutputStream fos;
    private FileInputStream fis;

    /*
     * Loads the Excel file
     * @param path of the Excel file
     */
    public void loadExcel(String filePath) {
        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
        }catch (IOException e){
            throw new ExcelExceptions.FileLoadingException("Failed to load excel file: "+ filePath);
        }
    }

    /*
     * Load the specific sheet
     * @param name of the sheet in Excel file
     */
    public void loadSheet(String sheetName){
        sheet = workbook.getSheet(sheetName);
        if(sheet == null){
            throw new ExcelExceptions.SheetNotFoundException("Sheet "+ sheetName + "does not exists in the excel file.");
        }
    }

    /*
     * Reads data from the Excel sheet
     * @return List of user data in the form of maps
     */
    public List<Map<String, Object>> readBookingData(String sheetName){
        loadSheet(sheetName);
        List<Map<String, Object>> data = new ArrayList<>();
        Row headerRow = sheet.getRow(0);
        int totalColumns = headerRow.getLastCellNum();
        for(int i=1; i<= sheet.getLastRowNum(); i++){
            Row row =  sheet.getRow(i);
            Map<String, Object> rowData = new HashMap<>();
            for(int j=0;j<totalColumns;j++){
                String columnHeader = headerRow.getCell(j).getStringCellValue();
                Cell cell  = row.getCell(j);
                Object value = (cell == null) ? "": getCellValue(cell);
                rowData.put(columnHeader, value);
            }
            data.add(rowData);
        }
        return data;
    }

    public Object getCellValue(Cell cell){
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                // Check if the numeric value is an integer (i.e., no decimal part)
                if (numericValue == (int) numericValue) {
                    return (int) numericValue;  // Convert to integer if it has no decimal
                } else {
                    return numericValue;  // Return as double if it has decimals
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }

    public List<BookingDetails> getBookingDetailsFromExcel(String sheetName){
        List<Map<String, Object>> rows = readBookingData(sheetName);
        List<BookingDetails> bookings = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            BookingDetails booking = new BookingDetails(
                    row.get("firstname").toString(),
                    row.get("lastname").toString(),
                    Integer.parseInt(row.get("totalprice").toString()),
                    Boolean.parseBoolean(row.get("depositpaid").toString()),
                    new BookingDates(
                            row.get("checkin").toString(),
                            row.get("checkout").toString()
                    ),
                    row.get("additionalneeds").toString()
            );
            bookings.add(booking);
        }

        return bookings;
    }


}