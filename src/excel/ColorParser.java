package excel;

import entities.Bicycle;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import util.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ColorParser {

    private static Logger LOGGER = Logger.getLogger(ColorParser.class);
    private static ColorParser instance=null;
    Map<String, String> colorsMap;

    private ColorParser() throws PriceReaderException {
        colorsMap = getColors();
    }


    public void addColors(Collection<Bicycle> bicycles) throws PriceReaderException {
        java.util.Iterator<Bicycle> bicycleIterator = bicycles.iterator();
        while (bicycleIterator.hasNext()) {
            Bicycle bicycle = bicycleIterator.next();
//            LOGGER.debug("Adding colors to: " + bicycle.toString());
            String code = bicycle.getProductCode();
//            LOGGER.debug("Product code is [" + code+ ']');
            String colors = colorsMap.get(code);
//            LOGGER.debug("Colors are ["+ colors +']');
            if (colors != null) {
                bicycle.setColors(colors);
            }
        }

    }


    private Map<String, String> getColors() throws PriceReaderException {
        Map<String, String> modelColorMap = new HashMap<String, String>();
        HSSFSheet sheet = getSheet();
        java.util.Iterator<Row> rowIterator = sheet.iterator();
        //skip header
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell modelCodeCell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
            Cell colorCell = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
            if (Cell.CELL_TYPE_STRING == modelCodeCell.getCellType() && Cell.CELL_TYPE_STRING == colorCell.getCellType()) {
                String modelCode = modelCodeCell.getStringCellValue();
                if(modelCode == null || modelCode.length() ==0){
                    LOGGER.error("Product code is null or empty. Skip row");
                    continue;
                }
                modelCode.trim();
                String colors = colorCell.getStringCellValue();
                if(colors !=null && colors.length() !=0)
                modelColorMap.put(modelCode, colors);

            }  else{
                LOGGER.error("No color for ["+modelCodeCell+']');
            }
        }
        return modelColorMap;
    }

    private HSSFSheet getSheet() throws PriceReaderException {
        HSSFSheet sheet;
        try {
            InputStream inputStream = new FileInputStream(Constants.COLOR_FILE_FILENAME);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);       // first sheet
        } catch (FileNotFoundException e) {
            throw new PriceReaderException("File not found: " + e);
        } catch (IOException e) {
            throw new PriceReaderException("Wrong file format: " + e);
        }
        return sheet;
    }


    public static ColorParser getInstance() throws PriceReaderException {
        if (instance == null) {
            instance = new ColorParser();
        }
        return instance;
    }


}
