package util;

import entities.Bicycle;
import excel.FileReader;
import excel.PriceReaderException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class YMLGenerator {

    private static Logger LOGGER = Logger.getLogger(YMLGenerator.class);

    private static YMLGenerator instance = null;

    private YMLGenerator() throws PriceReaderException {


    }

    public void generateYMLFile(Collection<Bicycle> bicycles) throws PriceReaderException {
        Map<String, Bicycle> bicycleMap = convertoToMap(bicycles);
        Map<String, Integer> categoriesMap = new HashMap<String, Integer>();
        Set<String> categoryNamesSet = categoriesMap.keySet();
        setURLanCategory(bicycleMap, categoriesMap);
//        urlMap = getModelsURL();

        Iterator<Bicycle> iterator = bicycles.iterator();
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
//            DOMImplementation domImpl = doc.getImplementation();
//            DocumentType doctype = domImpl.createDocumentType("yml_catalog", "SYSTEM","shops.dtd");
//            doc.appendChild(doctype);

//            <yml_catalog date="2012-03-18 23:31">
            Element yml_catalog = doc.createElement("yml_catalog");
            Attr dateAttr = doc.createAttribute("date");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = sdf.format(new Date());
            dateAttr.setValue(date);
            yml_catalog.setAttributeNode(dateAttr);
            doc.appendChild(yml_catalog);

            Element shop = doc.createElement("shop");
            yml_catalog.appendChild(shop);


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
            //                  <category id="15">Типы велосипедов</category>
            Element rootCategory = doc.createElement("category");

            Attr rootCategoryId = doc.createAttribute("id");
            rootCategoryId.setValue("1");
            rootCategory.setAttributeNode(rootCategoryId);

            rootCategory.appendChild(doc.createTextNode("Типы велосипедов"));


            Iterator<String> categoryIterator = categoryNamesSet.iterator();
            while (categoryIterator.hasNext()) {
                String categoryName = categoryIterator.next();

                Element category = doc.createElement("category");

                Attr categoryId = doc.createAttribute("id");
                categoryId.setValue(categoriesMap.get(categoryName).toString());
                category.setAttributeNode(categoryId);

                Attr parentId = doc.createAttribute("parentId");
                parentId.setValue("1");
                category.setAttributeNode(parentId);

                category.appendChild(doc.createTextNode(categoryName));

                categories.appendChild(category);
            }
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

                //type="vendor.model"

                Attr type = doc.createAttribute("type");
                type.setValue("vendor.model");
                offer.setAttributeNode(type);

//                available="false"
                Attr available = doc.createAttribute("available");
                available.setValue("false");
                offer.setAttributeNode(available);


                Element url = doc.createElement("url");
//                String urlString = urlMap.get("Stels " + bicycle.getModel() + " 2014");
                String urlString = bicycle.getUrl();
                LOGGER.debug(bicycle.getModel() + " URL: [" + urlString + ']');
                if(urlString == null){
                    LOGGER.error("No url for model [" + bicycle.getModel()+"] Skipping thi model");
                    continue;
                }
                url.appendChild(doc.createTextNode(urlString));
                offer.appendChild(url);

                Element price = doc.createElement("price");
                price.appendChild(doc.createTextNode(String.valueOf(bicycle.getPrice())));
                offer.appendChild(price);

                Element currency = doc.createElement("currencyId");
                currency.appendChild(doc.createTextNode("RUR"));
                offer.appendChild(currency);

                //       <categoryId>5</categoryId>

                Element categoryId = doc.createElement("categoryId");
                categoryId.appendChild(doc.createTextNode(bicycle.getCategoryId().toString()));
                offer.appendChild(categoryId);

                // <delivery>true</delivery>
                //  <local_delivery_cost>300</local_delivery_cost>
                Element delivery = doc.createElement("delivery");
                delivery.appendChild(doc.createTextNode("false"));
                offer.appendChild(delivery);

//                   <typePrefix>Принтер</typePrefix>
                Element typePrefix = doc.createElement("typePrefix");
                typePrefix.appendChild(doc.createTextNode("Велосипед"));
                offer.appendChild(typePrefix);

                //  <vendor>НP</vendor>
                Element vendor = doc.createElement("vendor");
                vendor.appendChild(doc.createTextNode("Stels"));
                offer.appendChild(vendor);
//
//                Element picture = doc.createElement("picture");
//                picture.appendChild(doc.createTextNode("N/A"));
//                offer.appendChild(picture);

                Element name = doc.createElement("model");
                name.appendChild(doc.createTextNode("Stels " + bicycle.getModel() + ' ' + Constants.YEAR));
                offer.appendChild(name);

                Element shortDescription = doc.createElement("description");
                shortDescription.appendChild(doc.createTextNode(bicycle.getShortDescription()));
                offer.appendChild(shortDescription);

                //    <manufacturer_warranty>true</manufacturer_warranty>
                Element manufacturer_warranty = doc.createElement("manufacturer_warranty");
                manufacturer_warranty.appendChild(doc.createTextNode("true"));
                offer.appendChild(manufacturer_warranty);

                //   <country_of_origin>Россия</country_of_origin>
                Element country_of_origin = doc.createElement("country_of_origin");
                country_of_origin.appendChild(doc.createTextNode("Россия"));
                offer.appendChild(country_of_origin);

                offers.appendChild(offer);
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(
                    OutputKeys.DOCTYPE_SYSTEM, "shops.dtd");
            transformer.setOutputProperty("encoding", "windows-1251");
            doc.setXmlStandalone(true);
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

    /**
     * Genetarate id usin hash code of model code string
     *
     * @param bicycle
     * @return
     */
    private String getId(Bicycle bicycle) {
        int hashCode = bicycle.getModel().hashCode();

        return String.valueOf(Math.abs(hashCode));
    }


    public void setURLanCategory(Map<String, Bicycle> bicycleMap, Map<String, Integer> categories) throws PriceReaderException {

        org.jsoup.nodes.Document doc = getPage("http://www.velo-line.ru/price.html");
        Elements table = doc.select("table.gre");
        String categotyName = "";
        Integer categotyId = 2;
        boolean startRecord = false;
        Elements links = table.select("a[href]");
        for (org.jsoup.nodes.Element link : links) {
            if (startRecord == false) {
                if (link.text().indexOf("Типы велосипедов") >= 0) {
                    startRecord = true;
                }
            } else {
                String url = link.attr("abs:href");
                if (url.contains("category")) {
                    categotyId++;
                    categotyName = link.text().trim();
                    categories.put(categotyName, categotyId);
                    continue;
                }
                String model = link.text().trim();
                if (model.indexOf("2014") >= 0) {
                    Bicycle bicycle = bicycleMap.get(model);
                    if(bicycle == null){
                        LOGGER.debug('[' + model + "] not found if map. Skipping this model");
                        continue;
                    }
                    bicycle.setUrl(url);
                    bicycle.setCategoryId(categotyId);
//                    LOGGER.debug(bicycle.getModel() + "categoryID [" + categotyId + "] url ["+ url + ']');

                }
            }
        }
    }


    public org.jsoup.nodes.Document getPage(String address) throws PriceReaderException {
        org.jsoup.nodes.Document doc = null;
        try {
            URL url = new URL(address);
            doc = Jsoup.parse(url, 7000);
        } catch (IOException ex) {
            throw new PriceReaderException(" Can't get page: " + ex);
        }
        return doc;
    }

    private Map convertoToMap(Collection<Bicycle> bicycles) {
        Map<String, Bicycle> toReturn = new HashMap();
        Iterator<Bicycle> iterator = bicycles.iterator();
        while (iterator.hasNext()) {
            Bicycle bicycle = iterator.next();
            toReturn.put("Stels " + bicycle.getModel() + " 2014", bicycle);
        }
        return toReturn;
    }


    public static YMLGenerator getInstance() throws PriceReaderException {
        if (instance == null) {
            instance = new YMLGenerator();
        }
        return instance;
    }


}
