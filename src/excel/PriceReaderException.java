package excel;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 22.02.14
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public class PriceReaderException extends Exception {
    public PriceReaderException() {}

    //Constructor that accepts a message
    public PriceReaderException(String message)
    {
        super(message);
    }
}
