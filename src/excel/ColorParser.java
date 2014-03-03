package excel;

import entities.Bicycle;
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


    public void addColors(Collection<Bicycle> bicycles) throws PriceReaderException{
         Map<String, String> colorsMap = getColors();
        java.util.Iterator <Bicycle> bicycleIterator = bicycles.iterator();
        while (bicycleIterator.hasNext()){
            Bicycle bicycle = bicycleIterator.next();
            String code = null;
// bicycle.getCode;
              String colors = colorsMap.get(code);
            if(colors!=null){
                bicycle.setColors(colors);
            }

        }

    }


    private Map<String, String> getColors() throws PriceReaderException{
        Map  <String, String> modelColorMap = new HashMap<String, String>();
        HSSFSheet sheet = getSheet();
        java.util.Iterator <Row> rowIterator = sheet.iterator();
        //skip header
        rowIterator.next();
        while (rowIterator.hasNext()){
            Row row =  rowIterator.next();
            Cell modelCodeCell = row.getCell(0);
            Cell colorCell = row.getCell(1);
            if(Cell.CELL_TYPE_STRING == modelCodeCell.getCellType() && Cell.CELL_TYPE_STRING == colorCell.getCellType()){
                String modelCode = modelCodeCell.getStringCellValue().trim();
                String colors = colorCell.getStringCellValue();
                modelColorMap.put(modelCode, colors);
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
}
