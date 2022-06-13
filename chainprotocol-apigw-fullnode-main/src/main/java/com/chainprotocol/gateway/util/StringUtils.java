package com.chainprotocol.gateway.util;

import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public final class StringUtils {

    private StringUtils() {
    }

    public static String rightPad(String str, int len, char padChar) {
        return Strings.padEnd(str, len, padChar);
    }

    public static String leftPad(String str, int len, char padChar) {
        return Strings.padStart(str, len, padChar);
    }

    public static String rightJustify(String temp, int length, char justifyChar) {
        int j = temp.length();
        for (int i = j; i < length; i++) {
            temp = justifyChar + temp;
        }
        return temp;
    }

    public static String leftJustify(String temp, int length, char justifyChar) {
        int j = temp.length();
        for (int i = j; i < length; i++) {
            temp = temp + justifyChar;
        }
        return temp;
    }

    public static void characterFill(StringBuffer stringBuffer, int length, char fillChar) {
        for (int i = 0; i < length; i++) {
            stringBuffer.append(fillChar);
        }
    }

    public static String getMasked(int length) {
        return getMasked(length, "*");
    }

    public static String getMasked(String str) {
        return getMasked(str, "*");
    }

    public static String getMasked(String str, String symbol) {
        return str == null ? "" : getMasked(str.length(), symbol);
    }

    public static String getMasked(int length, String symbol) {
        return Strings.repeat(symbol, length);
    }

    public static String performMasking(String value, boolean isAll, int left, int right, String maskChar) {
        if (!isBlank(value)) {
            if (isAll || left + right >= value.length()) {
                return getMasked(value.length(), maskChar);
            } else {
                return value.substring(0, left)
                        + getMasked(value.length() - left - right, maskChar)
                        + value.substring(value.length() - right);
            }
        }
        return value;
    }

    public static String maskPAN(String pan) {
        return performMasking(pan, false, 6, 4, "*");
    }

    public static Map<String, String> searchSection(String sectionName, String str) {
        int index = str.indexOf("[" + sectionName + "]");
        Map<String, String> sectionMap = null;
        if (index != -1) {
            sectionMap = new HashMap<String, String>();

            int endIndex = str.indexOf('[', index + 2);

            if (endIndex == -1) {
                endIndex = str.length();
            }

            BufferedReader sectionReader = new BufferedReader(new StringReader(str.substring(index, endIndex)));
            try {
                // skip section name
                sectionReader.readLine();
                String line;
                while ((line = sectionReader.readLine()) != null) {
                    // ignore comments, search for a key:value
                    if (!line.startsWith("//") && (index = line.indexOf(':')) != -1) {
                        String value = line.substring(index + 1).trim();
                        if (!value.isEmpty()) {
                            sectionMap.put(line.substring(0, index).trim(), value);
                        }
                    }
                }
            } catch (IOException e) {
                // ignore, because it is a memory reader.
            }
        }
        return sectionMap;
    }


    public static boolean isBlank(String str) {
        return (str == null || str.trim().equals(""));
    }

    @Deprecated
    public static boolean isBlankOrNull(String str) {
//        return (str == null || str.trim().equals("") || str.equals("null"));
        return isBlank(str);
    }

    public static String trimValue(String value, int length) {
        String formatValue = "";
        if (!isBlank(value)) {
            formatValue = value.length() > length ? value.substring(0, length) : value;
        }
        return formatValue;
    }

    public static String getString(Object str) {
        return str != null ? String.valueOf(str).trim() : "";
    }

    public static String trimValue(Object str) {
        return str != null ? String.valueOf(str).trim() : null;
    }

    public static String formatAmount(Double amount) {
        DecimalFormat df = new DecimalFormat("##0.00");
        return amount != null ? df.format(amount) : "0";
    }

    public static Map<String, String> convertResultStringToMap(String result) {
        Map<String, String> map = null;
        try {
            if (!StringUtils.isBlank(result)) {
                if ((result.startsWith("{")) && (result.endsWith("}"))) {
                    result = result.substring(1, result.length() - 1);
                }
                map = parseQString(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String> parseQString(String str) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();

        String key = null;
        boolean isKey = true;
        boolean isOpen = false;
        char openName = '\000';
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                char curChar = str.charAt(i);
                if (isKey) {
                    if (curChar == '=') {
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else {
                    if (isOpen) {
                        if (curChar == openName) {
                            isOpen = false;
                        }
                    } else {
                        if (curChar == '{') {
                            isOpen = true;
                            openName = '}';
                        }
                        if (curChar == '[') {
                            isOpen = true;
                            openName = ']';
                        }
                    }
                    if ((curChar == '&') && (!isOpen)) {
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }
            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map)
            throws UnsupportedEncodingException {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, temp.toString());
        }
    }

    public static String formatMapValues(Map<String, String> map) {
        String orderKeyValue = "";
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (map.get(key) != null) {
                orderKeyValue += map.get(key);
            }
        }
        return orderKeyValue;
    }

    public static Map<String, String> convertXmlToMap(String xmlStr) {
        HashMap<String, String> values = new HashMap<String, String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            Node user = doc.getFirstChild();
            NodeList childs = user.getChildNodes();
            Node child;
            for (int i = 0; i < childs.getLength(); i++) {
                child = childs.item(i);
                values.put(child.getNodeName(), child.getNodeValue());
            }
            return values;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String formatMapKeyValues(Map<String, String> map) {
        StringBuilder strbuiler = new StringBuilder();
        int i = 0;
        Set<String> keys = map.keySet();
        for (String key : keys) {
            i++;
            if (map.get(key) != null) {
                strbuiler.append(key);
                strbuiler.append("=");
                strbuiler.append(map.get(key));
                if (i != keys.size())
                    strbuiler.append("&");
            }
        }
        return strbuiler.toString();
    }

    public static Map<String, String> getKeyMaptoLowerCase(Map<String, String> map) {
        Map<String, String> lowerCaseMap = new HashMap<>(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            lowerCaseMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return lowerCaseMap;
    }

    public static String maskCardNumber(String pan) {
        return performMasking(pan, false, 6, 4, "*");
    }

    public static List<String> splitToList(String str) {
        return splitToList(str, ",");
    }

    public static List<String> splitToList(String str, String regex) {
        if (StringUtils.isBlankOrNull(str)) {
            return new ArrayList<>();
        }
        return Arrays.asList(str.split(regex));
    }

    public static List<Long> splitToLongList(String str) {
        return splitToLongList(str, ",");
    }

    public static List<Long> splitToLongList(String str, String regex) {
        if (StringUtils.isBlankOrNull(str)) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(regex)).map(Long::parseLong).collect(Collectors.toList());
    }

    public static String defaultString(String str) {
        return org.apache.commons.lang3.StringUtils.defaultString(str);
    }

    public static String defaultString(String str, String defaultStr) {
        return org.apache.commons.lang3.StringUtils.defaultString(str, defaultStr);
    }

    public static String removeNonPrintable(String str) {
        return replaceNonPrintable(str, true, null);
    }
    
    public static Boolean getBoolean(String str) {
    	if(isBlank(str)) {
    		return null;
    	}
    	return str.equalsIgnoreCase("true") ? true : false;
    }

    private static String replaceNonPrintable(String str, Character replaceChar) {
        return replaceNonPrintable(str, false, replaceChar);
    }

    private static String replaceNonPrintable(String str, boolean noReplace, Character replaceChar) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("(^\\h*)|(\\h*$)", "");

        if (str.length() == 0) {
            return str;
        }

        // From this thread: https://stackoverflow.com/a/18603020
        StringBuilder newString = new StringBuilder(str.length());
        for (int offset = 0; offset < str.length();) {
            int codePoint = str.codePointAt(offset);
            offset += Character.charCount(codePoint);

            // Replace invisible control characters and unused code points
            switch (Character.getType(codePoint)) {
                case Character.CONTROL:     // \p{Cc}
                case Character.FORMAT:      // \p{Cf}
                case Character.PRIVATE_USE: // \p{Co}
                case Character.SURROGATE:   // \p{Cs}
                case Character.UNASSIGNED:  // \p{Cn}
                    if (!noReplace) {
                        newString.append(replaceChar);
                    }
                    break;
                default:
                    if (codePoint <= 0) {
                        if (!noReplace) {
                            newString.append(replaceChar);
                        }
                    } else {
                        newString.append(Character.toChars(codePoint));
                    }
                    break;
            }
        }
        return newString.toString();
    }

}