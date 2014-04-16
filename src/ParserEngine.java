import csv.CSVEngine;
import entities.Bicycle;
import excel.ColorParser;
import excel.FileReadeHelper;
import excel.FileReader;
import excel.PriceReaderException;
import org.apache.log4j.Logger;
import util.ImageFileChecker;
import util.YMLGenerator;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 22.02.14
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class ParserEngine {
    private static Logger LOGGER = Logger.getLogger(FileReader.class);

    public static void main(String[] args) {
        try {
            LOGGER.debug("Starting...");
            Collection<Bicycle> bicycles = FileReader.getInstance().getModels();
            LOGGER.debug("Removing old models...");
            FileReadeHelper.removeOldModels(bicycles);
            FileReadeHelper.addLadyModelsToRoadBikes(bicycles);
            FileReadeHelper.addWheelSizeToModelWithIdenticalNames(bicycles);
            FileReadeHelper.generateShortDescription(bicycles);
            FileReadeHelper.addWheelSizeToSomeKidsModels(bicycles);
            ColorParser.getInstance().addColors(bicycles);

            ImageFileChecker.getInstance().checkImages(bicycles);
            ImageFileChecker.getInstance().addAdditionalImages(bicycles);
//       ResizeImg.getInstance().resizeImages(bicycles);
            CSVEngine.getInstance().writeFullFile(bicycles);
            CSVEngine.getInstance().writeCodeAndPriceFile(bicycles);
            YMLGenerator.getInstance().generateYMLFile(bicycles);

            LOGGER.debug("Finish");


        } catch (PriceReaderException ex) {
            LOGGER.error(ex);

        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }
}
