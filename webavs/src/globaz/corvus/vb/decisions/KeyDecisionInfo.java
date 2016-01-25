package globaz.corvus.vb.decisions;

import globaz.jade.client.util.JadeStringUtil;

/*
 * Inner Class
 * 
 * Clé de regroupement des RA
 */

/**
 * 
 * @deprecated, plus utilisé.
 */
public class KeyDecisionInfo implements Comparable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     */
    public String fraction = "";

    /**
     */
    public String genrePrestation = "";

    /**
     */
    public String perdiodeDu = "";

    /**
     */
    public String periodeAu = "";

    // Rétro ou Courant
    public String typeMontant = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe KeyDecisionInfo.
     * 
     * @param typeMontant
     * @param genrePrestation
     * @param fraction
     * @param perdiodeDu
     * @param periodeAu
     * @throws Exception
     */
    public KeyDecisionInfo(String typeMontant, String genrePrestation, String fraction, String perdiodeDu,
            String periodeAu) throws Exception {

        if (JadeStringUtil.isBlankOrZero(typeMontant)) {
            throw new Exception("'typeMontant' cannot be empty");
        }

        if (JadeStringUtil.isBlankOrZero(genrePrestation)) {
            throw new Exception("'genrePrestation' cannot be empty");
        }

        if (JadeStringUtil.isBlankOrZero(perdiodeDu)) {
            throw new Exception("'perdiodeDu' cannot be empty");
        }

        this.typeMontant = typeMontant;
        this.genrePrestation = genrePrestation;
        this.fraction = fraction;
        this.perdiodeDu = perdiodeDu;
        this.periodeAu = periodeAu;

        if (JadeStringUtil.isBlankOrZero(this.periodeAu)) {
            this.periodeAu = "000000";
        }

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
        KeyDecisionInfo keyGroupRA = (KeyDecisionInfo) o;

        if (typeMontant.compareTo(keyGroupRA.typeMontant) != 0) {
            return typeMontant.compareTo(keyGroupRA.typeMontant);
        } else if (genrePrestation.compareTo(keyGroupRA.genrePrestation) != 0) {
            return genrePrestation.compareTo(keyGroupRA.genrePrestation);
        } else if (fraction.compareTo(keyGroupRA.fraction) != 0) {
            return fraction.compareTo(keyGroupRA.fraction);
        } else if (perdiodeDu.compareTo(keyGroupRA.perdiodeDu) != 0) {
            return perdiodeDu.compareTo(keyGroupRA.perdiodeDu);
        } else if (periodeAu.compareTo(keyGroupRA.periodeAu) != 0) {
            return periodeAu.compareTo(keyGroupRA.periodeAu);

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
        if (!(obj instanceof KeyDecisionInfo)) {
            return false;
        }

        KeyDecisionInfo keyGroupRA = (KeyDecisionInfo) obj;

        return ((keyGroupRA.typeMontant.equals(typeMontant)) && (keyGroupRA.genrePrestation.equals(genrePrestation))
                && (keyGroupRA.fraction.equals(fraction)) && (keyGroupRA.perdiodeDu.equals(perdiodeDu)) && (keyGroupRA.periodeAu
                    .equals(periodeAu)));
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
        return (typeMontant + genrePrestation + fraction + perdiodeDu + periodeAu).hashCode();
    }
}
