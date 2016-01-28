package globaz.campus.process.chargementLot;

import globaz.commons.nss.NSUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

public class GERecordField {
    // les différents formats
    /** format AVS */
    public final static int FORMAT_AVS = 1;
    /** format Date */
    public final static int FORMAT_DATE_DDsMMsYYYY = 2;
    // l'index de début du champ (zero based)
    private int beginIndex;
    // l'index de fin du champ (zero based)
    private int endIndex;
    // le nom du champ (ex : Code Application)
    private String fieldName = "";
    // la valeur du champ (ex : 95)
    private String fieldValue = "";
    // le format du champ (cf java.txt.Format)
    private int format = -1;
    // l'index du champ (zero based), par exemple l'index de code application est 0
    private int index;
    // la longeur maximum du champ
    private int maxLength;

    /**
     * Constructor for RecordField.
     */
    protected GERecordField(int _beginIndex, int _endIndex) {
        super();
        beginIndex = _beginIndex;
        endIndex = _endIndex;
        maxLength = (endIndex - beginIndex) + 1;
    }

    /**
     * Method format. return string formatted
     * 
     * @return String
     */
    public String format() {
        if (format == -1) {
            return fieldValue.trim();
        } else {
            switch (format) {
                case FORMAT_AVS:
                    String numAvs = fieldValue.trim();
                    if (JadeStringUtil.isBlankOrZero(numAvs)) {
                        return "";
                    } else {
                        return NSUtil.formatAVSUnknown(fieldValue.trim());
                    }
                case FORMAT_DATE_DDsMMsYYYY:
                    String date = fieldValue.trim();
                    String dayAndMonth = date.substring(0, 4);
                    String year = date.substring(4, 6);
                    if (30 <= Integer.parseInt(year)) {
                        year = "19" + year;
                    } else {
                        year = "20" + year;
                    }
                    date = dayAndMonth + year;
                    return JACalendar.format(date);
                default:
                    return fieldValue.trim();
            }
        }
    }

    /**
     * Returns the beginIndex (zero based).
     * 
     * @return int
     */
    public int getBeginIndex() {
        return beginIndex;
    }

    /**
     * Returns the endIndex (zero based).
     * 
     * @return int
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Returns the fieldName.
     * 
     * @return String
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns the fieldValue.
     * 
     * @return String
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * Returns the format.
     * 
     * @return int
     */
    public int getFormat() {
        return format;
    }

    /**
     * Returns the index.
     * 
     * @return int
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the maxLength.
     * 
     * @return int
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the beginIndex.
     * 
     * @param beginIndex
     *            The beginIndex to set (included)
     */
    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    /**
     * Sets the endIndex.
     * 
     * @param endIndex
     *            The endIndex to set (excluded)
     */
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * Sets the fieldName.
     * 
     * @param fieldName
     *            The fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Sets the fieldValue.
     * 
     * @param fieldValue
     *            The fieldValue to set
     */
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    /**
     * Sets the format. Le format à utiliser pour un joli affichage
     * 
     * @param format
     *            The format to set
     */
    public void setFormat(int format) {
        this.format = format;
    }

    /**
     * Sets the index.
     * 
     * @param index
     *            The index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the maxLength.
     * 
     * @param maxLength
     *            The maxLength to set
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return fieldValue;
    }

}
