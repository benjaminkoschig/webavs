package globaz.pavo.print.list;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (21.07.2003 15:58:33)
 * 
 * @author: Administrator
 */
public class CIJournalRecapSource implements net.sf.jasperreports.engine.JRDataSource {
    private int _index = -1;
    private java.util.ArrayList data;

    /**
     * Commentaire relatif au constructeur CIJournalRecap.
     */
    public CIJournalRecapSource() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.03.2003 14:40:10)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
	 *
	 */
    @Override
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        // retourne chaque champ
        // Titres
        if (jrField.getName().equals("COL_1")) {
            return getRecapGenre();
        }
        // Nombre
        if (jrField.getName().equals("COL_2")) {
            return getNombre();
        }
        // Total revenus
        if (jrField.getName().equals("COL_3")) {
            return getTotalRevenus();
        }
        // Total revenus inscrits
        if (jrField.getName().equals("COL_4")) {

        }
        return null;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.07.2003 17:02:02)
     * 
     * @return java.lang.Integer
     */
    private Integer getNombre() {
        return (Integer) ((java.util.ArrayList) data.get(_index)).get(1);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.07.2003 17:03:08)
     * 
     * @return java.lang.String
     */
    private String getRecapGenre() {
        return (String) ((java.util.ArrayList) data.get(_index)).get(0);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.07.2003 17:06:20)
     * 
     * @return java.lang.Double
     */
    private Double getTotalRevenus() {
        return (Double) ((java.util.ArrayList) data.get(_index)).get(2);
    }

    /**
	 *
	 */
    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        _index++;
        if (_index < data.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.07.2003 09:01:57)
     */
    public void setSource(java.util.ArrayList tableau) {
        data = tableau;
    }
}
