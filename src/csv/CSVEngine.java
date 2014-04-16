package csv;


import com.csvreader.CsvWriter;
import entities.Bicycle;
import excel.FileReader;
import excel.PriceReaderException;
import org.apache.log4j.Logger;
import util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVEngine {
    private static Logger LOGGER = Logger.getLogger(CSVEngine.class);


    private static CSVEngine instance;

//    private Collection<Bicycle> bicycles;

    private CSVEngine() throws PriceReaderException {
//        bicycles = FileReader.getInstance().getModels();

    }

    public void writeFullFile(Collection<Bicycle> bicycles) throws PriceReaderException {

        CsvWriter csvWriter = null;
        try {
            File file = new File(Constants.OUTPUT_FILENAME);
            if (file.exists()) {
                if (!file.delete()) {
                    LOGGER.error("Unable to delete file[" + file.getAbsolutePath() + ']');
                    LOGGER.error("Creating temporary file");
                    file = new File(Constants.RES_FOLDER + "out" + new Date().getTime() + ".csv");
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            csvWriter = new CsvWriter(fileOutputStream, ';', Charset.forName("CP1251"));
            String[] line = CSVEngineHelper.getHeader();
            csvWriter.writeRecord(line);
            for (Bicycle model : bicycles) {
                line = convertModelsIntoArray(model);
                csvWriter.writeRecord(line);
            }
        } catch (FileNotFoundException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);

        } catch (IOException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);
        } finally

        {
            csvWriter.close();
        }

    }


    public void writeCodeAndPriceFile(Collection<Bicycle> bicycles) throws PriceReaderException {

        CsvWriter csvWriter = null;
        try {
            File file = new File(Constants.OUTPUT_FILENAME_CODE_AND_PRICE);
            if (file.exists()) {
                if (!file.delete()) {
                    LOGGER.error("Unable to delete file[" + file.getAbsolutePath() + ']');
                    LOGGER.error("Creating temporary file");
                    file = new File(Constants.RES_FOLDER + "out_code_and_price" + new Date().getTime() + ".csv");
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            csvWriter = new CsvWriter(fileOutputStream, ';', Charset.forName("CP1251"));
            String[] line = {"Код", "Цена"};
            csvWriter.writeRecord(line);
            for (Bicycle bicycle : bicycles) {
                line = new String[]{bicycle.getProductCode(), String.valueOf(bicycle.getPrice())};
                csvWriter.writeRecord(line);

            }
        } catch (FileNotFoundException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);

        } catch (IOException ex) {
            throw new PriceReaderException("Error writing .csv file. " + ex);
        } finally

        {
            csvWriter.close();
        }

    }

    private String[] convertModelsIntoArray(Bicycle model) {
        String prodCode = model.getProductCode();
        String[] result = new String[52];
        for (int i = 0; i < 25; i++) {
            result[i] = "";
        }
        result[0] = "-1";
        result[1] = prodCode;
        result[2] = "Stels " + model.getModel() + " " + Constants.YEAR;
        result[3] = result[2];
        result[5] = "";
        result[5] = model.getShortDescription();
        result[6] = String.valueOf(model.getPrice());
        result[8] = "-1";
        result[9] = "0";
        result[11] = "Stels " + model.getModel() + " " + Constants.YEAR;
        result[12] = "0";
        result[13] = "0";
        result[14] = "1";
        result[16] = "7";
//        result[18] = prodCode + ".jpg," + prodCode + ".jpg," + prodCode + ".jpg";
        if (model.getImageName() != null) {
            result[18] = model.getImageName() + ',' + model.getImageName() + ',' + model.getImageName();
        }
        if (model.getImageName2() != null) {
            result[19] = model.getImageName2() + ',' + model.getImageName2() + ',' + model.getImageName2();
        }
        if (model.getImageName3() != null) {
            result[20] = model.getImageName3() + ',' + model.getImageName3() + ',' + model.getImageName3();
        }
        result[25] = "Stels";
        result[26] = model.getWheelsSize();
        String frameSize = String.valueOf(model.getFrameSize());
        if (frameSize.equals("0")) {
            frameSize = "";
        }
        result[27] = frameSize;
        result[28] = model.getFrame();
        result[29] = String.valueOf(model.getSpeedsNum());
        result[30] = model.getColors();
        result[32] = model.getFrontFork();
        result[34] = model.getSteeringTube();
        result[35] = model.getBottomBracket();
        result[36] = model.getCrankset();
        result[37] = model.getFrontHub();
        result[38] = model.getRearHub();
        result[40] = model.getShifters();
        result[41] = model.getFreeWheel();
        result[42] = model.getFrontDerailleur();
        result[43] = model.getRearDerailleur();
        result[44] = model.getBreaks();
        result[45] = model.getRims();
        result[46] = model.getTyres();
        result[47] = model.getFenders();
        result[48] = model.getPedals();
        result[49] = model.getSaddle();
        result[50] = "";
        result[51] = model.getRack();

        return result;
    }


    public static CSVEngine getInstance() throws PriceReaderException {
        if (instance == null) {
            instance = new CSVEngine();
        }
        return instance;
    }
}
