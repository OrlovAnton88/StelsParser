package util;

import entities.Bicycle;
import excel.PriceReaderException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 05.03.14
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class ImageFileChecker {

    private static ImageFileChecker instance = null;

    private ImageFileChecker() {

    }

    private static Logger LOGGER = Logger.getLogger(ImageFileChecker.class);

    public void checkImages(Collection<Bicycle> bicycles) throws PriceReaderException {
        LOGGER.debug("Starting checking images");
        Collection<String> missingImages = new ArrayList();
        Iterator<Bicycle> iterator = bicycles.iterator();
        while (iterator.hasNext()) {
            String productCode = iterator.next().getProductCode();
            if (!ifFileExists(Constants.IMAGES_FOLDER, productCode)) {
                missingImages.add(productCode);
            }
        }
        if (!missingImages.isEmpty()) {
            File file = createFile(Constants.RES_FOLDER, "missing_images.txt");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                Writer writer = new BufferedWriter(outputStreamWriter);
                for (String prodCode : missingImages) {
                    writer.write(prodCode);
                    writer.write(System.getProperty("line.separator"));
                }
                writer.close();
            } catch (FileNotFoundException ex) {
                throw new PriceReaderException(ex.getMessage());
            } catch (IOException ex) {
                throw new PriceReaderException(ex.getMessage());
            }
        }
        LOGGER.debug("Images check completed." + missingImages.size() + " images are missing");
    }


    private boolean ifFileExists(String path, String filename) {
        File file = new File(path + filename + ".jpg");
        return file.exists();
    }


    public File createFile(String path, String filename) throws PriceReaderException {
        try {
            File myFile = new File(path + filename);
            if(myFile.exists()) {
                myFile.delete();
            }
            if (myFile.createNewFile()) {
                return myFile;
            } else {
                throw new PriceReaderException("Fail to create file [" + path + filename + ']');
            }
        } catch (IOException ex) {
            throw new PriceReaderException("Cannot create file [" + path + filename + ']' + ex);
        }
    }


    public static ImageFileChecker getInstance() {
        if (instance == null) {
            instance = new ImageFileChecker();
        }
        return instance;
    }

}
