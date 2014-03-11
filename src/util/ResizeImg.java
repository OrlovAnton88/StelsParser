package util;

import entities.Bicycle;
import excel.PriceReaderException;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 06.03.14
 * Time: 0:31
 * To change this template use File | Settings | File Templates.
 */
public class ResizeImg {

    private static final int IMG_WIDTH = 150;
    private static final int IMG_WIDTH350 = 350;

    private static ResizeImg instance = null;

    private ResizeImg() {

    }

    public void resizeImages(Collection<Bicycle> bicycles) throws PriceReaderException {
        LOGGER.debug("Starting resizing images");
        Collection<String> missingImages = new ArrayList();
        Iterator<Bicycle> iterator = bicycles.iterator();
        while (iterator.hasNext()) {
            Bicycle bicycle = iterator.next();
//            LOGGER.debug("Processing " + bicycle.toString());
            doAction(bicycle.getImageName());
            doAction(bicycle.getImageName2());
            doAction(bicycle.getImageName3());
        }
    }

    private void doAction(String imageName) throws PriceReaderException {
        if (imageName != null && ifFileExists("D:\\GitHub\\StelsParser\\res\\images_to_add\\", imageName)) {
            try {
                BufferedImage originalImage = ImageIO.read(new File("D:\\GitHub\\StelsParser\\res\\images_to_add\\" + imageName));

                BufferedImage image150 = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, IMG_WIDTH);
                ImageIO.write(image150, "jpg", new File("D:\\GitHub\\StelsParser\\res\\images_to_add\\1_small\\" + imageName));

                BufferedImage image350 = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, IMG_WIDTH350);
                ImageIO.write(image350, "jpg", new File("D:\\GitHub\\StelsParser\\res\\images_to_add\\1_medium\\" + imageName));
            } catch (IOException ex) {
                throw new PriceReaderException("Resizing images" + ex.getMessage());
            } catch (Exception ex){
                throw  new PriceReaderException("Something wrong with image[" +imageName+']');
            }

        }

    }


    private boolean ifFileExists(String path, String filename) {
        File file = new File(path + filename);
        return file.exists();
    }

    private static Logger LOGGER = Logger.getLogger(ImageFileChecker.class);


    public static ResizeImg getInstance() {
        if (instance == null) {
            instance = new ResizeImg();
        }
        return instance;
    }

}
