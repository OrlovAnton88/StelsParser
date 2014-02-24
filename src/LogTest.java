import org.apache.log4j.Logger;

public class LogTest {
    private static Logger log = Logger.getLogger(LogTest.class);

    public static void main(String[] args) {


//        BasicConfigurator.configure();
//        Logger.getLogger("org.apache").setLevel(Level.INFO);

        log.trace("Trace");
        log.debug("Debug");
        log.info("Info");
        log.warn("Warn");
        log.error("Error");
        log.fatal("Fatal");

//        String classpath = System.getProperty("java.class.path");
//        System.out.println(classpath);


    }

}
