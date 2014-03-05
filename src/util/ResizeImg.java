package util;

import entities.Bicycle;
import excel.PriceReaderException;
import org.apache.log4j.Logger;

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
            String productCode = iterator.next().getProductCode();
            if (ifFileExists("D:\\GitHub\\StelsParser\\res\\img\\", productCode)) {
                try {
                    BufferedImage originalImage = ImageIO.read(new File("D:\\GitHub\\StelsParser\\res\\img\\" + productCode + ".jpg"));
                    int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                    BufferedImage resizeImageJpg = resizeImageWithHint(originalImage, type);
                    ImageIO.write(resizeImageJpg, "jpg", new File("D:\\GitHub\\StelsParser\\res\\img_medium\\" +productCode+ ".jpg"));
                } catch (IOException ex) {
                    throw new PriceReaderException("Resizing images"+ex.getMessage());
                }

            }
        }
    }

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {
        int original_width = originalImage.getWidth();
        int original_height = originalImage.getHeight();

        int ratio = original_width/IMG_WIDTH350;
        int newHeight = original_height/ratio;




        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH350, newHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH350, newHeight, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }


    private boolean ifFileExists(String path, String filename) {
        File file = new File(path + filename + ".jpg");
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
