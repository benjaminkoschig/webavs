package ch.globaz.corvus.process.dnra;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.DateRente;

class CsvLineParser {
    private final List<String> elements;
    private int currentElement;

    public CsvLineParser(String line) {
        elements = Arrays.asList(line.split(";"));
    }

    public DateRente nextElementToDateRente(String forField) {
        return toDateRente(nextElementTrim(), forField);
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

    static DateRente toDateRente(String date, String forField) {
        DateRente dateDate = null;
        if (date != null) {
            date = date.trim();
            if (!date.isEmpty()) {
                dateDate = new DateRente(date, Date.DATE_PATTERN_ddMMyyyy);
            }
        }
        return dateDate;
    }

}
