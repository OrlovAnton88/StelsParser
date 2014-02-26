import csv.CSVEngine;
import excel.FileReader;
import excel.PriceReaderException;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 22.02.14
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class ParserEngine {
    private static Logger LOGGER = Logger.getLogger(FileReader.class);

    public static void main(String[] args){
         try{
//        reader.getModels();
             CSVEngine.getInstance().writeFile();

         } catch (PriceReaderException ex){
             LOGGER.error(ex);

         }

         catch (Exception ex){
             LOGGER.error(ex);
         }
    }
}
