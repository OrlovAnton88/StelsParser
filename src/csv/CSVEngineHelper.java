package csv;

import entities.Bicycle;
import excel.PriceReaderException;
import util.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aorlov on 2/26/14.
 */
public class CSVEngineHelper {


    public static String[] getHeader() throws PriceReaderException {
        StringBuffer result = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(Constants.HEADER_FILENAME));

            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (FileNotFoundException ex) {
            throw new PriceReaderException("Couldn't find file with columns for header" + ex);
        } catch (IOException ex) {
            throw new PriceReaderException("Error during reading file with columns for header" + ex);
        }

        return result.toString().split("\\t");
    }



    private static String getFirstChar(String name) {
        return name.substring(0, 1).toLowerCase();
    }


    private static boolean isAdrenalinModel(String name) {
        if (name.toLowerCase().contains("adrenalin") && !name.toLowerCase().contains("disc")) {
            return true;
        }
        return false;
    }

    private static boolean isTalismanModel(String name) {
        if (name.contains("Talisman")) {
            return true;
        }
        return false;
    }

    private static boolean isChallengerModel(String name) {
        if (name.toLowerCase().contains("challenger")) {
            return true;
        }
        return false;
    }

    private static boolean isAggressorModel(String name) {
        if (name.toLowerCase().contains("aggressor")) {
            return true;
        }
        return false;
    }

    private static boolean isMustangModel(String name) {
        if (name.toLowerCase().contains("mustang")) {
            return true;
        }
        return false;
    }

    private static boolean isCrossModel(String name) {
        if (name.toLowerCase().contains("cross")) {
            return true;
        }
        return false;
    }

    private static boolean isEnergyModel(String name) {
        if (name.toLowerCase().contains("energy")) {
            return true;
        }
        return false;
    }

    private static boolean isBMXModel(String name) {
        if (name.toLowerCase().contains("bmx")) {
            return true;
        }
        return false;
    }


    public static ArrayList<String> setSpecialKidsModel() {
        ArrayList<String> specialKidsModels = new ArrayList<String>();
        specialKidsModels.add("Joy");
        specialKidsModels.add("Jet");
        specialKidsModels.add("Magic");
        specialKidsModels.add("Flash");
        specialKidsModels.add("Arrow");
        specialKidsModels.add("Magic");
        specialKidsModels.add("Arrow");
        specialKidsModels.add("Fortune");
//        specialKidsModels.add("Talisman (black)");
//        specialKidsModels.add("Talisman (chrome)");
        specialKidsModels.add("Dolphin");
        specialKidsModels.add("Echo");


        return specialKidsModels;
    }
}
