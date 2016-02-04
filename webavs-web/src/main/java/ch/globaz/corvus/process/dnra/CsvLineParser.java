package ch.globaz.corvus.process.dnra;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import ch.globaz.common.domaine.Date;

class CsvLineParser {
    private final List<String> elements;
    private int currentElement;

    public CsvLineParser(String line) {
        elements = Arrays.asList(line.split(";"));
    }

    public Date nextElementToDate(String forField) {
        return toDate(nextElementTrim(), forField);
    }

    public String nextElementTrim() {
        String element = nextElement();
        if (element != null) {
            element = element.trim();
            if (element.isEmpty()) {
                return null;
            }
        }
        return element;
    }

    public String nextElement() {
        if (currentElement < elements.size()) {
            String tocken = elements.get(currentElement);
            currentElement++;
            if (!tocken.isEmpty()) {
                return tocken;
            }
        }
        return null;
    }

    public String nextElementToNssFormate() {
        return formatNss(nextElementTrim());
    }

    private String formatNss(String nss) {
        if (nss != null && nss.length() == 13) {
            MessageFormat text = new java.text.MessageFormat("{0}.{1}.{2}.{3}");
            String[] telArr = { nss.substring(0, 3), nss.substring(3, 7), nss.substring(7, 11), nss.substring(11, 13) };
            return text.format(telArr);
        }
        return nss;
    }

    private static Date toDate(String date, String forField) {
        Date dateDate = null;
        if (date != null) {
            date = date.trim();
            if (!date.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                try {
                    dateDate = new Date(dateFormat.parse(date));
                } catch (ParseException e) {
                    throw new RuntimeException("Unable to pars date for forField : " + forField, e);
                }
            }
        }
        return dateDate;
    }

}
