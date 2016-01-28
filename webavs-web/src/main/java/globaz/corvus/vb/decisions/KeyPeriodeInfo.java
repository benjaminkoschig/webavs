package globaz.corvus.vb.decisions;

/*
 * Inner Class
 * 
 * Clé de regroupement des RA
 */

/**
 * 
 * Author : scr
 */
public class KeyPeriodeInfo implements Comparable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     */
    public String dateDebut = "";

    /**
     */
    public String dateFin = "";

    public String remarque = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public KeyPeriodeInfo() throws Exception {

    }

    /**
     * Crée une nouvelle instance de la classe KeyDecisionInfo.
     * 
     * @param dateDebut
     *            Format : AAAAMMJJ
     * @param dateFin
     *            Format : AAAAMMJJ
     * @throws Exception
     */
    public KeyPeriodeInfo(String dateDebut, String dateFin) throws Exception {

        // if (JadeStringUtil.isBlankOrZero(dateDebut)) {
        // throw new Exception("'dateDebut' cannot be empty");
        // }

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * 
     * @param o
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public int compareTo(Object o) {
        KeyPeriodeInfo keyGroupRA = (KeyPeriodeInfo) o;

        if (dateDebut.compareTo(keyGroupRA.dateDebut) != 0) {
            return dateDebut.compareTo(keyGroupRA.dateDebut);
        } else if (dateFin.compareTo(keyGroupRA.dateFin) != 0) {
            return dateFin.compareTo(keyGroupRA.dateFin);
        } else {
            return 0;
        }
    }

    /**
     * @param obj
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyPeriodeInfo)) {
            return false;
        }

        KeyPeriodeInfo keyGroupRA = (KeyPeriodeInfo) obj;

        return ((keyGroupRA.dateDebut.equals(dateDebut)) && (keyGroupRA.dateFin.equals(dateFin)));
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public int hashCode() {
        return (dateDebut + dateFin).hashCode();
    }
}
