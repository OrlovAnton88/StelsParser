import excel.FileReader;
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
        FileReader reader = new FileReader();
         try{
        reader.getModels();
         }catch (Exception ex){
             LOGGER.error(ex);
         }
    }
}
