package ch.globaz.common.domaine;

import java.text.SimpleDateFormat;

public class DateRente extends Date {

    private static final String REGEX_0000YEAR = "[0]{4}[0-9]{4}";
    private static final String REGEX_00MMYEAR = "[0]{2}[0-9]{6}";
    private static final String REGEX_00_00_YEAR = "[0]{2}.[0]{2}.[0-9]{4}";
    private static final String REGEX_MM_00_YEAR = "[0]{2}.[0-9]{2}.[0-9]{4}";
    private static final String REGEX_YEAR0000 = "[0-9]{4}[0]{4}";
    private static final String REGEX_YEARMM00 = "[0-9]{6}[0]{2}";
    private static final String dd_MM = "01.01.";

    private Boolean sansdd;
    private Boolean sansMMdd;

    public DateRente() {
        super();
        sansMMdd = false;
        sansdd = false;
    }

    public DateRente(final String date, String pattern) {
        super(date, pattern);
    }

    @Override
    protected String changeDate(final String date) {
        sansMMdd = true;

        if (isNull(date)) {
            throw new IllegalArgumentException("La date ne peut être null");
        }

        if (date.matches(REGEX_0000YEAR) && DATE_PATTERN_ddMMyyyy.equals(pattern)) {
            return "0101" + date.substring(4);
        }
        if (date.matches(REGEX_YEAR0000) && DATE_PATTERN.equals(pattern)) {
            return date.subSequence(0, 4) + "0101";
        }
        if (date.matches(REGEX_00_00_YEAR)) {
            return dd_MM + date.subSequence(6, 10);
        }
        sansdd = true;
        sansMMdd = false;
        if (date.matches(REGEX_00MMYEAR) && DATE_PATTERN_ddMMyyyy.equals(pattern)) {
            return "01" + date.substring(2);
        }
        if (date.matches(REGEX_YEARMM00) && DATE_PATTERN.equals(pattern)) {
            return date.subSequence(0, 6) + "01";
        }
        if (date.matches(REGEX_MM_00_YEAR)) {
            return "01." + date.subSequence(4, 10);
        }
        sansdd = false;
        return date;
    }

    public Boolean isSansMMDD() {
        return sansMMdd;
    }

    /**
     * Retour la date au format standard Swiss
     * 
     * @return String au format dd.MM.yyyy
     */
    @Override
    public String getSwissValue() {
        if (sansMMdd) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String year = dateFormat.format(date);
            return "00.00." + year;
        }
        if (sansdd) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM.yyyy");
            String year = dateFormat.format(date);
            return "00." + year;
        }
        return super.getSwissValue();
    }

}