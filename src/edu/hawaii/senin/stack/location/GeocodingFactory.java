package edu.hawaii.senin.stack.location;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Implements universal methods fo update.
 * 
 * @author psenin
 * 
 */
public class GeocodingFactory {

  // URL prefix to the geocoder
  private static final String GEOCODER_REQUEST_PREFIX_FOR_XML = "http://maps.google.com/maps/api/geocode/xml";
  private static final Object SPACE = " ";
  private static final Object COMMA = ",";

  /**
   * This takes a LUCENE normalized location line and transforms it into Google-style location line.
   * 
   * @param address
   * @return
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws XPathExpressionException
   */
  public static String getLocationForString(String address) throws IOException, SAXException,
      ParserConfigurationException, XPathExpressionException {

    // query address
    // String address = "1600 Amphitheatre Parkway, Mountain View, CA";

    // prepare a URL to the geocoder
    URL url = new URL(GEOCODER_REQUEST_PREFIX_FOR_XML + "?address="
        + URLEncoder.encode(address, "UTF-8") + "&sensor=false");

    // prepare an HTTP connection to the geocoder
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    Document geocoderResultDocument = null;
    try {
      // open the connection and get results as InputSource.
      conn.connect();
      InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

      // read result and parse into XML Document
      geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(geocoderResultInputSource);
    }
    finally {
      conn.disconnect();
    }

    // prepare XPath
    XPath xpath = XPathFactory.newInstance().newXPath();

    // extract the result
    NodeList resultNodeList = null;
    StringBuffer locationSB = new StringBuffer();

    resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/status", geocoderResultDocument,
        XPathConstants.NODESET);

    if (!(resultNodeList.item(0).getTextContent().toUpperCase().contains("OK"))) {
      return "N/A, status: " + resultNodeList.item(0).getTextContent();
    }

    // a) obtain the formatted_address field for every result
    resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result/formatted_address",
        geocoderResultDocument, XPathConstants.NODESET);
    for (int i = 0; i < resultNodeList.getLength(); ++i) {
      locationSB.append(resultNodeList.item(i).getTextContent()).append(SPACE);
    }

    if (locationSB.charAt(locationSB.length() - 1) == ' ') {
      locationSB.append("| ");
    }

    // b) extract the locality for the first result
    resultNodeList = (NodeList) xpath.evaluate(
        "/GeocodeResponse/result[1]/address_component[type/text()='locality']/long_name",
        geocoderResultDocument, XPathConstants.NODESET);
    for (int i = 0; i < resultNodeList.getLength(); ++i) {
      System.out.println(resultNodeList.item(i).getTextContent());
    }

    if (locationSB.charAt(locationSB.length() - 1) == ' ') {
      locationSB.append("| ");
    }

    // c) extract the coordinates of the first result
    resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result[1]/geometry/location/*",
        geocoderResultDocument, XPathConstants.NODESET);
    float lat = Float.NaN;
    float lng = Float.NaN;
    for (int i = 0; i < resultNodeList.getLength(); ++i) {
      Node node = resultNodeList.item(i);
      if ("lat".equals(node.getNodeName()))
        lat = Float.parseFloat(node.getTextContent());
      if ("lng".equals(node.getNodeName()))
        lng = Float.parseFloat(node.getTextContent());
    }
    locationSB.append("lat/lng=").append(lat).append(COMMA).append(lng).append(SPACE);

    if (locationSB.charAt(locationSB.length() - 1) == ' ') {
      locationSB.append("| ");
    }

    // c) extract the coordinates of the first result
    resultNodeList = (NodeList) xpath
        .evaluate(
            "/GeocodeResponse/result[1]/address_component[type/text() = 'administrative_area_level_1']/country[short_name/text() = 'US']/*",
            geocoderResultDocument, XPathConstants.NODESET);

    lat = Float.NaN;
    lng = Float.NaN;
    for (int i = 0; i < resultNodeList.getLength(); ++i) {
      Node node = resultNodeList.item(i);
      if ("lat".equals(node.getNodeName()))
        lat = Float.parseFloat(node.getTextContent());
      if ("lng".equals(node.getNodeName()))
        lng = Float.parseFloat(node.getTextContent());
    }

    locationSB.append("lat/lng=").append(lat).append(COMMA).append(lng).append(SPACE);

    return locationSB.toString();

  }

  public static Map<String, String> getTZforURL(String urlString) throws IOException, SAXException,
      ParserConfigurationException, XPathExpressionException {
    // <geonames>
    // <timezone tzversion="tzdata2012d">
    // <countryCode>PH</countryCode>
    // <countryName>Philippines</countryName>
    // <lat>12.879721</lat>
    // <lng>121.77402</lng>
    // <timezoneId>Asia/Manila</timezoneId>
    // <dstOffset>8.0</dstOffset>
    // <gmtOffset>8.0</gmtOffset>
    // <rawOffset>8.0</rawOffset>
    // <time>2012-10-22 01:46</time>
    // <sunrise>2012-10-22 05:44</sunrise>
    // <sunset>2012-10-22 17:30</sunset>
    // </timezone>
    // </geonames>
    // prepare an HTTP connection to the geocoder

    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    Document geocoderResultDocument = null;
    try {
      // open the connection and get results as InputSource.
      conn.connect();
      InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

      // read result and parse into XML Document
      geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(geocoderResultInputSource);
    }
    finally {
      conn.disconnect();
    }

    HashMap<String, String> res = new HashMap<String, String>();

    // prepare XPath
    XPath xpath = XPathFactory.newInstance().newXPath();

    // extract the result
    NodeList resultNodeList = null;

    resultNodeList = (NodeList) xpath.evaluate("/geonames/timezone/timezoneId",
        geocoderResultDocument, XPathConstants.NODESET);

    if (resultNodeList.getLength() > 0) {
      String temezoneId = resultNodeList.item(0).getTextContent();
      res.put("timezoneId", temezoneId);
    }
    else {
      return null;
    }

    resultNodeList = (NodeList) xpath.evaluate("/geonames/timezone/gmtOffset",
        geocoderResultDocument, XPathConstants.NODESET);

    if (resultNodeList.getLength() > 0) {
      String gmtOffset = resultNodeList.item(0).getTextContent();
      res.put("gmtOffset", gmtOffset);
    }
    else {
      return null;
    }

    return res;

  }

  public static Map<String, String> getTZforURL2(String urlString) throws IOException,
      SAXException, ParserConfigurationException, XPathExpressionException {
    // <response message="ok" code="0">
    // <data>
    // <result>
    // <TimeZone TimeZoneId="America/Chicago" InDstNow="true"
    // WindowsStandardName="Central Standard Time" ShortName="CDT" CurrentOffsetMs="-18000000"
    // MinDistanceKm="0.0" IsInside="true" AskGeoId="3157"/>
    // </result>
    // </data>
    // </response>

    // prepare an HTTP connection to the geocoder

    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    Document geocoderResultDocument = null;
    try {
      // open the connection and get results as InputSource.
      conn.connect();
      InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

      // read result and parse into XML Document
      geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(geocoderResultInputSource);
    }
    finally {
      conn.disconnect();
    }

    HashMap<String, String> res = new HashMap<String, String>();

    // prepare XPath
    XPath xpath = XPathFactory.newInstance().newXPath();

    String response = (String) xpath.evaluate("/response/@message", geocoderResultDocument,
        XPathConstants.STRING);
    if (!("OK".equalsIgnoreCase(response.toUpperCase()))) {
      System.err.println("Response is not OK");
      System.exit(-10);
    }

    // extract the result
    String timezoneId = (String) xpath.evaluate("/response/data/result/TimeZone/@TimeZoneId",
        geocoderResultDocument, XPathConstants.STRING);
    res.put("timezoneId", timezoneId);
    res.put("gmtOffset", null);

    return res;

  }

  public static String tokenizeNormalize(String location) throws IOException {

    StringReader reader = new StringReader(location);

    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
    TokenStream tokenStream = analyzer.tokenStream(null, reader);

    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

    StringBuffer sb = new StringBuffer();

    while (tokenStream.incrementToken()) {
      String term = charTermAttribute.toString();
      if (null != term && term.length() > 0) {
        sb.append(term).append(SPACE);
      }
    }

    if (sb.length() > 0) {
      sb.delete(sb.length() - 1, sb.length());
    }

    return sb.toString();

  }

}
