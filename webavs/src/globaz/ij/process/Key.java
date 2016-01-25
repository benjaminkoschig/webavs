/*
 * Créé le 31 oct. 05
 */
package globaz.ij.process;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe servant de clef pour regrouper les répartitions de paiements dans une map suivant l'idTiers, l'idAffilie,
 * l'idAdressePaiement et l'idDepartement
 * </p>
 * 
 * <p>
 * à utiliser pour la génération des compensations et des écritures comptables (Les regroupements effectués dans ces
 * deux process doivent être les mêmes)
 * </p>
 * 
 * @author dvh
 */

public final class Key implements Comparable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     */
    public String idAffilie = "";

    /**
     * non pris en compte pour le regroupement, sert à retrouver l'idAdressePaiement
     */
    public String idDomaineAdressePaiement = "";

    /**
     */
    public String idExtra1 = "";

    /**
     */
    public String idExtra2 = "";

    /**
     */
    public String idTiers = "";

    /**
     * non pris en compte pour le regroupement, sert à retrouver l'idAdressePaiement
     */
    public String idTiersAdressePaiement = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe Key.
     * 
     * @param idTiers
     *            DOCUMENT ME!
     * @param idAffilie
     *            DOCUMENT ME!
     * @param idExtra1
     *            DOCUMENT ME!
     * @param idExtra2
     *            DOCUMENT ME!
     */
    public Key(String idTiers, String idAffilie, String idExtra1, String idExtra2) {
        this.idTiers = idTiers;
        this.idAffilie = idAffilie;
        this.idExtra1 = idExtra1;
        this.idExtra2 = idExtra2;
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
        Key key = (Key) o;

        if (idTiers.compareTo(key.idTiers) != 0) {
            return idTiers.compareTo(key.idTiers);
        } else if (idAffilie.compareTo(key.idAffilie) != 0) {
            return idAffilie.compareTo(key.idAffilie);
        } else if (idExtra1.compareTo(key.idExtra1) != 0) {
            return idExtra1.compareTo(key.idExtra1);
        } else if (idExtra2.compareTo(key.idExtra2) != 0) {
            return idExtra2.compareTo(key.idExtra2);
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
        if (!(obj instanceof Key)) {
            return false;
        }

        Key key = (Key) obj;

        return ((key.idTiers.equals(idTiers)) && (key.idAffilie.equals(idAffilie)) && (key.idExtra1.equals(idExtra1)) && (key.idExtra2
                .equals(idExtra2)));
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
        return (idTiers + idAffilie + idExtra1 + idExtra2).hashCode();
    }
}
