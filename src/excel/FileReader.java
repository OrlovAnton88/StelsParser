package excel;

import entities.Bicycle;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 22.02.14
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class FileReader {

    private static Logger LOGGER = Logger.getLogger(FileReader.class);

    private static final int rowToStart = 4;

    private static final int modelNameColumnNum = 0;
    private static final int descriptionColumnNum = 1;
    private static final int priceColumnNum = 5;


    public Collection<Bicycle> getModels() throws PriceReaderException {
        HSSFSheet sheet = getSheet();
        int numerOfRows = sheet.getPhysicalNumberOfRows();
        Collection<Bicycle> bicycles = new ArrayList<Bicycle>();
        for (int i = rowToStart; i < numerOfRows; i++) {
            Bicycle bicycle = new Bicycle();
            Row row = sheet.getRow(i);
            if(row == null){
                break;
            }
            Cell dirtyModelName = row.getCell(modelNameColumnNum);
            Cell description = row.getCell(descriptionColumnNum);
            Cell price = row.getCell(priceColumnNum);
            if (Cell.CELL_TYPE_STRING == dirtyModelName.getCellType()) {
                parseModelName(dirtyModelName.getStringCellValue(), bicycle);
            } else {
                throw new PriceReaderException("Name cell is not of string type");
            }
            if (Cell.CELL_TYPE_STRING == description.getCellType()) {
                FileReadeHelper.parseDescription(description.getStringCellValue(), bicycle);
            } else {
                throw new PriceReaderException("Description cell is not of string type");
            }
            LOGGER.debug(bicycle.toString());
          bicycles.add(bicycle);
        }

        return null;


    }

    private void parseModelName(String cellValue, Bicycle bicycle) {
        cellValue = cellValue.replace("\r\n", " ").replace("\n", " ").trim();
        int wheelSize = 0;
        String modelName="";
        Pattern p = Pattern.compile(Constants.PATTERN_STANDART_MODEL_FORMAT);
        Matcher matchModel = p.matcher(cellValue);

        if (matchModel.find()) {
            Pattern wheelPattern = Pattern.compile(Constants.PATTERN_WHEEL_SIZE);
            Matcher wheelMatcher = wheelPattern.matcher(cellValue);
            if (wheelMatcher.find()) {
                String extract = wheelMatcher.group().trim();
                String wheelSizeString = extract.substring(0, extract.length() - 1);
                wheelSize = Integer.valueOf(wheelSizeString);
            }
            modelName = cellValue.substring(3, cellValue.length());
            modelName = Constants.STELS + Constants.SPACE_CHAR + modelName;
//            LOGGER.debug('[' + cellValue + "] ModelName [" + modelName + "] Wheel size [" + wheelSize + ']');

        } else {
            LOGGER.info("Non-Standart model string format: " + cellValue);
        }

        bicycle.setModel(modelName);
        bicycle.setWheelsSize(wheelSize);
    }

    private HSSFSheet getSheet() throws PriceReaderException {
        HSSFSheet sheet;
        try {
            InputStream inputStream = new FileInputStream(Constants.PRICE_FILE_NAME);
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
