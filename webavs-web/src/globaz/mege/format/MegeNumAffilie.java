package globaz.mege.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;

public class MegeNumAffilie implements IFormatData {

    private static String ERROR_MSG = "Format incorrect";
    public static int NUMBER_LENGTH = 10;
    public static char NUMBER_SEPARATOR_CHAR = '.';
    public static int NUMBER_SEPARATOR_POS = 7;
    public static char TYPE_SEPARATOR_CHAR = '-';
    public static int TYPE_SEPARATOR_POS = 9;

    public static void main(String[] args) {

        MegeNumAffilie num = new MegeNumAffilie();

        String[] toTest = {

        "0000354001" // nok

        };

        for (int i = 0; i < toTest.length; i++) {
            try {
                num.format(toTest[i]);
                System.out.println("valide : " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            } catch (Exception e) {
                System.out.println(e.getMessage() + " - " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            }
        }

    }

    /**
     * complete le numero d'affilie par le bon nombre de head 0
     * 
     * @param noAffilie
     *            a completer par des head 0
     * @return noAffilie complete par le bon nombre de head 0
     */
    public String addHeadZero(String noAffilie) {

        int noAffilieLength = noAffilie.length();

        if (noAffilieLength < MegeNumAffilie.NUMBER_SEPARATOR_POS) {
            noAffilie = "0000000".substring(noAffilieLength) + noAffilie;
        }

        return noAffilie;
    }

    @Override
    public String check(Object value) throws Exception {
        if (JadeStringUtil.isEmpty((String) value)) {
            throw new Exception(MegeNumAffilie.ERROR_MSG);
        }
        String valueStr = (String) value;
        int sep = JadeStringUtil.indexOf(valueStr, MegeNumAffilie.NUMBER_SEPARATOR_CHAR, 0);
        if ((sep != -1) && (sep != MegeNumAffilie.NUMBER_SEPARATOR_POS)) {
            throw new Exception(MegeNumAffilie.ERROR_MSG);
        }
        valueStr = unformat(valueStr);
        if (valueStr.length() < MegeNumAffilie.NUMBER_LENGTH) {
            throw new Exception(MegeNumAffilie.ERROR_MSG);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.format.IFormatData#format(java.lang.String)
     */
    @Override
    public String format(String value) {
        if (value == null) {
            return null;
        }

        value = unformat(value);
        value = addHeadZero(value);

        StringBuffer result = new StringBuffer();
        if (value.length() > MegeNumAffilie.NUMBER_SEPARATOR_POS) {
            result.append(value.substring(0, MegeNumAffilie.NUMBER_SEPARATOR_POS));
            result.append(MegeNumAffilie.NUMBER_SEPARATOR_CHAR);
            // result.append(value.substring(NUMBER_SEPARATOR_POS,TYPE_SEPARATOR_POS));
            if (value.length() > MegeNumAffilie.TYPE_SEPARATOR_POS) {
                result.append(value.substring(MegeNumAffilie.NUMBER_SEPARATOR_POS, MegeNumAffilie.TYPE_SEPARATOR_POS));
                result.append(MegeNumAffilie.TYPE_SEPARATOR_CHAR);
                result.append(value.substring(MegeNumAffilie.TYPE_SEPARATOR_POS));
            } else if (value.contains("?")) {
                result.append(value.substring(MegeNumAffilie.NUMBER_SEPARATOR_POS, value.length()));
            }
        } else {
            return value;
        }
        return result.toString();
    }

    @Override
    public String unformat(String value) {
        return JadeStringUtil.removeChar(JadeStringUtil.removeChar(value, MegeNumAffilie.NUMBER_SEPARATOR_CHAR),
                MegeNumAffilie.TYPE_SEPARATOR_CHAR);
    }

}
