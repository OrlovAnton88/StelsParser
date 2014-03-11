package csv;


import com.csvreader.CsvWriter;
import entities.Bicycle;
import excel.FileReader;
import excel.PriceReaderException;
import org.apache.log4j.Logger;
import util.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVEngine {
    private static Logger LOGGER = Logger.getLogger(CSVEngine.class);


    private static CSVEngine instance;

    private Collection<Bicycle> bicycles;
    private Collection<String> specialModels;

    private CSVEngine() throws PriceReaderException {
        bicycles = FileReader.getInstance().getModels();
        specialModels = CSVEngineHelper.setSpecialKidsModel();

    }

    public void writeFile() throws PriceReaderException {

        CsvWriter csvWriter = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Constants.OUTPUT_FILENAME);
            csvWriter = new CsvWriter(fileOutputStream, ';', Charset.forName("CP1251"));
            String[] line = CSVEngineHelper.getHeader();
            csvWriter.writeRecord(line);
            for (Bicycle model : bicycles) {
                line = convertModelsIntoArray(model);
                csvWriter.writeRecord(line);
            }

        } catch (IOException ex)

        {

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
        result[5] = shortDesc(model);
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


    public String shortDesc(Bicycle model) {
        String strMod = model.getModel();
        StringBuffer result = new StringBuffer();
        try {
            Pattern p = Pattern.compile("\\d\\d\\d");
            //  get a matcher object
            Matcher m = p.matcher(strMod);
            String tmp = "";
            if (m.find()) {
                tmp = m.group();
            }
            int modNum = Integer.parseInt(tmp);
            if (modNum >= 500 && strMod.contains("Navigator")) {
                result.append("Горный велосипед ");
            } else if (modNum >= 200 && modNum < 390 && strMod.contains("Navigator")) {
                if (strMod.contains("Lady")) {
                    result.append("Женский дорожный велосипед ");
                } else {
                    result.append("Дорожный велосипед ");
                }
            } else if (strMod.contains("Cross") || (strMod.contains("Navigator") && modNum == 170)) {
                result.append("Гибридный велосипед ");
            } else if (strMod.contains("Miss")) {
                result.append("Женский горный велосипед ");
            } else if (strMod.contains("Pilot") && (modNum > 300 && modNum < 830)) {
                result.append("Складной велосипед ");
            } else {
                result.append("Велосипед ");
            }
        } catch (NumberFormatException ex) {
            LOGGER.error("Can't define bicycle type: " + ex.getMessage());
            result.append("Велосипед ");

        }
        String wheels = model.getWheelsSize();
        if (wheels.equals("24")) {
            result.append("с колесами " + wheels + " дюйма.");
        } else {
            result.append("с колесами " + wheels + " дюймов.");
        }

        if (strMod.toLowerCase().contains("disc")) {
            int size = result.length();
            result.delete(size - 1, size);
            result.append(" и дисковыми тормозами. ");

        }
        String frameMaterial = model.getFrame();
        if (frameMaterial != null) {
            if (frameMaterial.toLowerCase().contains("ста")) {
                result.append(" Рама: сталь.");
            } else if (frameMaterial.toLowerCase().contains("AL")) {
                result.append(" Рама: алюминий.");
            }
        }
        try {
            int speeds = model.getSpeedsNum();
            result.append(" " + speeds);
            if (speeds == 21 || speeds == 1) {
                result.append(" скорость. ");
            } else if (speeds == 18 || speeds == 5 || speeds == 7 || speeds == 6 || speeds == 12) {
                result.append(" скоростей. ");

            } else {
                result.append(" скорости. ");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Cant parse speeds number: " + ex.getMessage());
        }

        return result.toString();
    }


    public boolean isSpecialKidsModel(String model) {
        if (specialModels.contains(model)) {
            return true;
        }
        return false;
    }


    public static CSVEngine getInstance() throws PriceReaderException {
        if (instance == null) {
            instance = new CSVEngine();
        }
        return instance;
    }
}
