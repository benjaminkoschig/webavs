package globaz.prestation.tools.sql;

public class PRCustomSQLStringBuffer {

    protected String _egal = "=";
    protected String _like = " LIKE ";
    protected StringBuffer sb = new StringBuffer();

    public PRCustomSQLStringBuffer() {
        super();
    }

    /**
     * @return
     * @see java.lang.StringBuffer#length(java.lang.StringBuffer)
     */
    public int length() {
        return sb.length();
    }

    /**
     * @param egal
     *            the _egal to set
     */
    public void setEgal(String egal) {
        _egal = egal;
    }

    /**
     * @param like
     *            the _like to set
     */
    public void setLike(String like) {
        _like = like;
    }

    /**
     * @param start
     * @return
     * @see java.lang.StringBuffer#substring(int)
     */
    public String substring(int start) {
        return sb.substring(start);
    }

    /**
     * @param start
     * @param end
     * @return
     * @see java.lang.StringBuffer#substring(int, int)
     */
    public String substring(int start, int end) {
        return sb.substring(start, end);
    }

    /**
     * @return
     * @see java.lang.StringBuffer#toString()
     */
    @Override
    public String toString() {
        return sb.toString();
    }

    /**
     * Retourne l'objet sous forme de StringBuffer
     * 
     * @return l'objet sous sa forme StringBuffer
     */
    public StringBuffer toStringBuffer() {
        return sb;
    }

}