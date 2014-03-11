package excel;

import entities.Bicycle;
import org.apache.log4j.Logger;
import util.Constants;

import java.awt.font.NumericShaper;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 25.02.14
 * Time: 23:20
 * To change this template use File | Settings | File Templates.
 */
public class FileReadeHelper {

    private static final String FORK = "вилка";
    private static final String FRAME = "paмa";
    private static final String FRAME2 = "рама";
    private static final String RIMS = "обода";
    private static final String FENDERS = "крылья";
    private static final String BREAKS = "тормоз";
    private static final String SADDLE = "седло";
    private static final String FROND_DERAILLEUR = "FD";
    private static final String REAR_DERAILLEUR = "RD";


    private static Logger LOGGER = Logger.getLogger(FileReadeHelper.class);

    public static void parseDescription(String cellValue, Bicycle bicycle) throws PriceReaderException {
        cellValue = roadBikesLadyGentFix(cellValue);
        String[] characteristics = cellValue.split(",");

        bicycle.setSpeedsNum(getNumberOfSpeeds(characteristics));
        String frame = null;
        if ((frame = setParameter(characteristics, FRAME)) == null) {
            frame = setParameter(characteristics, FRAME2);
        }
        if (frame != null) {
            frame = frame.replaceAll("рама", "").trim();
            frame = frame.replaceAll("paмa", "").trim();
        }
        bicycle.setFrame(frame);
        bicycle.setFrontFork(setParameter(characteristics, FORK));
        bicycle.setRims(setParameter(characteristics, RIMS));
        bicycle.setFenders(setParameter(characteristics, FENDERS));
        bicycle.setBreaks(setParameter(characteristics, BREAKS));
        bicycle.setSaddle(setParameter(characteristics, SADDLE));

        bicycle.setFrontDerailleur(setDerailleur(characteristics, FROND_DERAILLEUR));
        bicycle.setRearDerailleur(setDerailleur(characteristics, REAR_DERAILLEUR));
    }

    private static String roadBikesLadyGentFix(String str) {
        int start = 0;
        int end = 0;
        if ((start = str.indexOf('(')) > 0 && (end = str.indexOf(")")) > 0 && str.substring(start, end).indexOf(',') > 0) {
            String begining = str.substring(0, start);
            String ending = str.substring(end, str.length());
            String middle = str.substring(start, end);
            if (middle.contains("Lady")) {
                middle = middle.replace(",", " ");
                return begining + middle + ending;
            } else {
                return str;
            }

        } else {
            return str;
        }
    }

    public static void parsePrice(double cellValue, Bicycle bicycle) throws PriceReaderException {
        int price = (int) Math.round(cellValue);
        bicycle.setPrice(price);


    }


    private static String setParameter(String str[], String parameter) {
        for (int i = 1; i < str.length; i++) {
            if (str[i].contains(parameter)) {
//                LOGGER.debug(parameter + " [" + str[i].trim() + ']');
                return str[i].trim();
            }
        }
        return null;
    }


    private static String setDerailleur(String[] str, String parameter) {
        for (int i = 0; i < str.length; i++) {
            if (str[i].contains(parameter)) {
                String derailleurs[] = str[i].split("/");

                for (int x = 0; x < derailleurs.length; x++) {
                    if (derailleurs[x].contains(parameter)) {
                        LOGGER.debug(parameter + " [" + derailleurs[x].trim() + ']');
                        return derailleurs[x].trim();
                    }
                }
            }
        }
        return "";
    }

    public static int getNumberOfSpeeds(String[] characteristics) throws PriceReaderException {
        String speedsStr = characteristics[0].trim();
        Pattern p = Pattern.compile(Constants.PATTERN_SPEEDS_STRING);
        Matcher matcher = p.matcher(speedsStr);
        if (matcher.find()) {
            String str = matcher.group();
            int delimeter = str.indexOf("-");
            str = str.substring(0, delimeter);
            int speeds = Integer.valueOf(str);
            return speeds;
        } else {
            throw new PriceReaderException("Couldn't parse number of speeds. String[" + speedsStr + ']');
        }
    }

    public static String reduceSpaces(String stringIn) {
        return stringIn.replaceAll("\\s+", " ");
    }

    public static void processCrossModel(String model, Bicycle bicicle) {
        bicicle.setModel(model);
        bicicle.setWheelsSize("28");
    }

    public static void process175Model(String model, Bicycle bicycle) {
        String wheelSize = model.substring(0, 4);
        String modelName = model.substring(5, model.length()).trim();
        bicycle.setWheelsSize(wheelSize);
        bicycle.setModel(modelName);
    }

    public static boolean isCrossModel(String model) {
        if (model.contains("Cross")) {
            return true;
        }
        return false;
    }

    public static boolean is275Model(String model) {
        if (model.contains("27,5")) {
            return true;
        }
        return false;
    }


    /**
     * it's assumed if model has new design, then model with old desing is 2013 model
     *
     * @param bicycles
     */
    public static void removeOldModels(Collection<Bicycle> bicycles) {
        Collection modelNamesToRemove = new ArrayList();
        for (Bicycle bicycle : bicycles) {
            if (bicycle.getModel().contains("(новый дизайн)")) {
                modelNamesToRemove.add(bicycle.getModel().replace("(новый дизайн)", "").toLowerCase().trim());
            }
        }
        Iterator<Bicycle> iterator = bicycles.iterator();
        while (iterator.hasNext()) {
            Bicycle bicycle = iterator.next();
            if (modelNamesToRemove.contains(bicycle.getModel().toLowerCase())) {
                iterator.remove();
                LOGGER.debug("Model [" + bicycle.getModel() + "] removed");
            }
        }

    }

    public static void generateProdCode(Bicycle bicycle) throws PriceReaderException {
        String model = bicycle.getModel();
        if (model == null) {
            throw new PriceReaderException("model name is null");
        }
        StringBuffer result = new StringBuffer("s");
        model = model.toLowerCase();
        model = model.replace("(новая модель)", "");
        model = model.replace("(новый дизайн)", "");
        model = model.replace("cross", "");
        model = model.replace("pilot", "p");
        model = model.replace("miss", "m");
        model = model.replace("navigator", "n");
        if (model.indexOf('(') > 0) {
            model = model.replaceAll("\\(|\\)", "_").trim();
        }
        model = model.replaceAll("\\s", "").trim();
        result.append(model);
        result.append(bicycle.getWheelsSize());
        try {
            result.append('_');
            result.append(Constants.YEAR);
        } catch (StringIndexOutOfBoundsException ex) {
            throw new PriceReaderException("Error generating product code for model [" + model + ']' + ex);
        }
        bicycle.setProductCode(result.toString());
    }


    public static void addLadyModelsToRoadBikes(Collection<Bicycle> bicycles) throws PriceReaderException {
        Collection<Bicycle> newBikes = new ArrayList<Bicycle>();
        java.util.Iterator<Bicycle> iterator = bicycles.iterator();
        while (iterator.hasNext()) {
            Bicycle bicycle = iterator.next();
            String frame = bicycle.getFrame();
            if (frame != null && frame.indexOf("(Gent") > 0 && frame.indexOf("Lady") > 0) {
                Bicycle newBicycle = null;
                try {
                    newBicycle = (Bicycle) bicycle.clone();
                    newBicycle.setModel(bicycle.getModel() + " Lady");
                } catch (CloneNotSupportedException ex) {
                    LOGGER.error(ex);
                }
                FileReadeHelper.generateProdCode(newBicycle);
                newBikes.add(newBicycle);
            }

        }
        bicycles.addAll(newBikes);
    }

    public static void addWheelSizeToModelWithIdenticalNames(Collection<Bicycle> bicycles) {
        Iterator<Bicycle> iterator = bicycles.iterator();
        Set<String> uniqueModes = new HashSet<String>();
        Set<String> sameModelName = new HashSet<String>();
        while (iterator.hasNext()) {
            Bicycle bicycle = iterator.next();
            String modelName = bicycle.getModel();
            boolean unique = uniqueModes.add(modelName);
            if (!unique) {
                sameModelName.add(modelName);
            }
        }
        Iterator<Bicycle> secondIterator = bicycles.iterator();
        while (secondIterator.hasNext()) {
            Bicycle bicycle = secondIterator.next();
            String modelName = bicycle.getModel();
            if (sameModelName.contains(modelName)) {
                bicycle.setModel(modelName + " " + bicycle.getWheelsSize());
            }
        }
    }

}
