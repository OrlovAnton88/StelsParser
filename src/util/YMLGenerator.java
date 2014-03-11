package util;

import entities.Bicycle;
import excel.PriceReaderException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Mouse
 * Date: 11.03.14
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */
public class YMLGenerator {

    private static YMLGenerator instance = null;

    private YMLGenerator() {
    }

    public void generateYMLFile(Collection<Bicycle> bicycles) throws PriceReaderException{

        Iterator<Bicycle> iterator = bicycles.iterator();
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element shop = doc.createElement("shop");
            doc.appendChild(shop);

            Element shopname = doc.createElement("name");
            shopname.appendChild(doc.createTextNode("VELO-LINE"));
            shop.appendChild(shopname);

            Element company = doc.createElement("company");
            company.appendChild(doc.createTextNode("VELO-LINE"));
            shop.appendChild(company);

            Element shopULR = doc.createElement("url");
            shopULR.appendChild(doc.createTextNode("http://www.velo-line.ru/"));
            shop.appendChild(shopULR);

            Element currencies = doc.createElement("currencies");
            shop.appendChild(currencies);

            Element currencyRUR = doc.createElement("currency");
            Attr currRUR = doc.createAttribute("id");
            currRUR.setValue("RUR");
            currencyRUR.setAttributeNode(currRUR);

            Attr rateRUR = doc.createAttribute("rate");
            rateRUR.setValue("1");
            currencyRUR.setAttributeNode(rateRUR);

            currencies.appendChild(currencyRUR);


            Element categories = doc.createElement("categories");
            shop.appendChild(categories);


            Element offers = doc.createElement("offers");
            shop.appendChild(offers);

            while (iterator.hasNext()) {
                Bicycle bicycle = iterator.next();



                //todo: available = falase
                Element offer = doc.createElement("offer");
                shop.appendChild(offers);

                // set attribute to staff element
                Attr attr = doc.createAttribute("id");
                attr.setValue(getId(bicycle));
                offer.setAttributeNode(attr);

                Element url = doc.createElement("url");
                url.appendChild(doc.createTextNode("N/A"));
                offer.appendChild(url);

                Element price = doc.createElement("price");
                price.appendChild(doc.createTextNode(String.valueOf(bicycle.getPrice())));
                offer.appendChild(price);

                Element currency = doc.createElement("currencyId");
                currency.appendChild(doc.createTextNode("RUR"));
                offer.appendChild(currency);

                Element picture = doc.createElement("picture");
                picture.appendChild(doc.createTextNode("N/A"));
                offer.appendChild(picture);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode("Stels " + bicycle.getModel() + ' ' + Constants.YEAR));
                offer.appendChild(name);

                Element shortDescription = doc.createElement("description");
                shortDescription.appendChild(doc.createTextNode("N/A"));
                offer.appendChild(shortDescription);

               // <delivery>true</delivery>
              //  <local_delivery_cost>300</local_delivery_cost>


                //   <typePrefix>Принтер</typePrefix>

                //  <vendor>НP</vendor>

                //    <manufacturer_warranty>true</manufacturer_warranty>

                //   <country_of_origin>Япония</country_of_origin>


                offers.appendChild(offer);
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("D:\\GitHub\\StelsParser\\res\\file.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);


        } catch (ParserConfigurationException ex) {
           throw new PriceReaderException("YML generation error(ParserConfigurationException)" + ex);
        } catch (TransformerException ex) {
            throw new PriceReaderException("YML generation error(TransformerException)" + ex);
        }

    }

    private String getId(Bicycle bicycle){
        int hashCode = bicycle.getModel().hashCode();

        return String.valueOf(Math.abs(hashCode));
    }

    public static YMLGenerator getInstance() {
        if (instance == null) {
            instance = new YMLGenerator();
        }
        return instance;
    }

}
