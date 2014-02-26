package csv;

import excel.PriceReaderException;
import util.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
}
