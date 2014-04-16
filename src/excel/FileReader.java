package excel;

import entities.Bicycle;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import util.Constants;
import util.ImageFileChecker;
import util.ResizeImg;
import util.YMLGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileReader {

    private static FileReader instance = null;

    private FileReader() {

    }

    private static Logger LOGGER = Logger.getLogger(FileReader.class);

    private static final int rowToStart = 4;

    private static final int modelNameColumnNum = 0;
    private static final int descriptionColumnNum = 1;
    private static final int priceColumnNum = 2;


    public Collection<Bicycle> getModels() throws PriceReaderException {
        HSSFSheet sheet = getSheet();
        int numerOfRows = sheet.getPhysicalNumberOfRows();
        Collection<Bicycle> bicycles = new ArrayList<Bicycle>();
        for (int i = rowToStart; i < numerOfRows; i++) {
            Bicycle bicycle = new Bicycle();
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            Cell dirtyModelName = row.getCell(modelNameColumnNum);
            Cell description = row.getCell(descriptionColumnNum);
            Cell price = row.getCell(priceColumnNum);
            if (Cell.CELL_TYPE_STRING == dirtyModelName.getCellType()) {
                String cellValue = dirtyModelName.getStringCellValue();
                if (cellValue.contains("2013")) {
                    continue;
                }
                parseModelName(cellValue, bicycle);
            } else {
                throw new PriceReaderException("Name cell is not of string type");
            }
            if (Cell.CELL_TYPE_STRING == description.getCellType()) {
                FileReadeHelper.parseDescription(description.getStringCellValue(), bicycle);
            } else {
                throw new PriceReaderException("Description cell is not of string type");
            }
            if (Cell.CELL_TYPE_NUMERIC == price.getCellType()) {
                FileReadeHelper.parsePrice(price.getNumericCellValue(), bicycle);
            } else {
                LOGGER.error("Cell cell is not of numeric type. Cell type [" + price.getCellType() + ']');
                bicycle.setPrice(0);
            }
            LOGGER.debug("Bicycle generated: " + bicycle.toString());
            FileReadeHelper.generateProdCode(bicycle);
            bicycles.add(bicycle);

        }
        return bicycles;


    }

    private void parseModelName(String cellValue, Bicycle bicycle) {
        cellValue = cellValue.replace("\r\n", " ").replace("\n", " ").trim();
        //remove Stels sfrom model name

        cellValue = cellValue.replace("STELS", "").trim();
        String wheelSize = "0";
        String modelName = "";
        Pattern p = Pattern.compile(Constants.PATTERN_STANDART_MODEL_FORMAT);
        Matcher matchModel = p.matcher(cellValue);

        if (matchModel.find()) {
            Pattern wheelPattern = Pattern.compile(Constants.PATTERN_WHEEL_SIZE);
            Matcher wheelMatcher = wheelPattern.matcher(cellValue);
            if (wheelMatcher.find()) {
                String extract = wheelMatcher.group().trim();
                wheelSize = extract.substring(0, extract.length() - 1);
            }
            modelName = cellValue.substring(3, cellValue.length()).trim();
            modelName = FileReadeHelper.reduceSpaces(modelName);
                bicycle.setModel(modelName);

            bicycle.setWheelsSize(wheelSize);
        } else {
            LOGGER.error("Non-Standart model string format: " + cellValue);
            LOGGER.info("Trying alternative way");
            String model = FileReadeHelper.reduceSpaces(cellValue);
            if (FileReadeHelper.isCrossModel(model)) {
                FileReadeHelper.processCrossModel(model, bicycle);
            } else if (FileReadeHelper.is275Model(model)) {
                FileReadeHelper.process175Model(model, bicycle);
            }
        }
        bicycle.setTrademark(Constants.STELS);


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

    public static FileReader getInstance() {
        if (instance == null) {
            instance = new FileReader();
        }
        return instance;
    }
}
