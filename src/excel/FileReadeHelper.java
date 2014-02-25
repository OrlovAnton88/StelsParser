package excel;

import entities.Bicycle;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 25.02.14
 * Time: 23:20
 * To change this template use File | Settings | File Templates.
 */
public class FileReadeHelper {

    private static final String FORK = "вилка";
    private static final String RIMS = "обода";
    private static final String FENDERS = "крылья";
    private static final String BREAKS = "тормоз";
    private static final String SADDLE = "седло";
    private static final String FROND_DERAILLEUR = "FD";
    private static final String REAR_DERAILLEUR = "RD";


    private static Logger LOGGER = Logger.getLogger(FileReadeHelper.class);

    public static void parseDescription(String cellValue, Bicycle bicycle) throws PriceReaderException {
        String[] characteristics = cellValue.split(",");
//        LOGGER.debug("characteristics.length [" + characteristics.length + ']');
        //todo: default should be 1
        String speedsStr = characteristics[0];
        String frameStr = characteristics[1];

        bicycle.setFrontFork(setParameter(characteristics, FORK));
        bicycle.setRims(setParameter(characteristics, RIMS));
        bicycle.setFenders(setParameter(characteristics, FENDERS));
        bicycle.setBreaks(setParameter(characteristics, BREAKS));
        bicycle.setSaddle(setParameter(characteristics, SADDLE));

        bicycle.setFrontDerailleur(setDerailleur(characteristics, FROND_DERAILLEUR));
        bicycle.setRearDerailleur(setDerailleur(characteristics, REAR_DERAILLEUR));
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
        for (int i = 1; i < str.length; i++) {
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
        return null;
    }

}
