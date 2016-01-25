package ch.globaz.common.listoutput.converterImplemented;

import java.text.MessageFormat;
import java.util.Locale;
import ch.globaz.simpleoutputlist.converter.Converter;

public class PhoneConverter implements Converter<String, String> {

    @Override
    public String getValue(String tel, Locale local) {

        if (tel != null) {
            tel = tel.trim().replaceAll("\\s", "").replaceAll("\\.", "");
            if (tel.length() == 10) {
                MessageFormat text = new java.text.MessageFormat("{0} {1} {2} {3}");
                String[] telArr = { tel.substring(0, 3), tel.substring(3, 6), tel.substring(6, 8), tel.substring(8, 10) };
                return text.format(telArr);
            } else if (tel.length() == 14) {
                MessageFormat text = new java.text.MessageFormat("{0} {1} {2} {3} {4}");

                String[] telArr = { tel.substring(0, 4), tel.substring(4, 7), tel.substring(7, 10),
                        tel.substring(10, 12), tel.substring(12, 14) };
                return text.format(telArr);
            } else if (tel.length() == 12) {
                MessageFormat text = new java.text.MessageFormat("{0} {1} {2} {3} {4}");
                String[] telArr = { tel.substring(0, 2), tel.substring(2, 5), tel.substring(5, 8),
                        tel.substring(8, 10), tel.substring(10, 12) };
                return text.format(telArr);
            } else if (tel.length() == 11) {
                MessageFormat text = new java.text.MessageFormat("{0} {1} {2} {3} {4}");
                String[] telArr = { tel.substring(0, 2), tel.substring(2, 4), tel.substring(4, 7), tel.substring(7, 9),
                        tel.substring(9, 11) };
                return text.format(telArr);
            } else if (tel.length() == 13) {
                if (tel.startsWith("00417")) {
                    MessageFormat text = new java.text.MessageFormat("{0} {1} {2} {3} {4}");
                    String[] telArr = { tel.substring(0, 4), tel.substring(4, 7), tel.substring(7, 9),
                            tel.substring(9, 11), tel.substring(11, 13) };
                    return text.format(telArr);
                } else {
                    MessageFormat text = new java.text.MessageFormat("{0} {1} {2} {3} {4}");
                    String[] telArr = { tel.substring(0, 4), tel.substring(4, 6), tel.substring(6, 9),
                            tel.substring(9, 11), tel.substring(11, 13) };
                    return text.format(telArr);
                }
            }
        }
        return tel;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
