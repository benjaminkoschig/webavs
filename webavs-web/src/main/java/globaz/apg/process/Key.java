/*
 * Cr�� le 31 oct. 05
 */
package globaz.apg.process;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe servant de clef pour regrouper les r�partitions de paiements dans une map suivant l'idTiers, l'idAffilie,
 * l'idAdressePaiement et l'idDepartement
 * </p>
 * 
 * <p>
 * � utiliser pour la g�n�ration des compensations et des �critures comptables (Les regroupements effectu�s dans ces
 * deux process doivent �tre les m�mes)
 * </p>
 * 
 * @author dvh
 */

public final class Key implements Comparable {

    public String genrePrestation = "";
    public String idAffilie = "";
    public String idExtra1 = "";
    public String idExtra2 = "";
    public String idTiers = "";
    public Boolean isIndependant = false;
    public Boolean isEmployeur = false;
    public String idAdressePaiement = "";
    public String referencePaiement = "";
    public Boolean isPorteEnCompte = false;

    /**
     * non pris en compte pour le regroupement, sert � retrouver l'idAdressePaiement
     */
    public String idDomaineAdressePaiement = "";

    /**
     * non pris en compte pour le regroupement, sert � retrouver l'idAdressePaiement
     */
    public String idTiersAdressePaiement = "";

    public Key(String idTiers, String idAffilie, String idExtra1, String idExtra2, String genrePrestation,
            boolean isEmployeur, boolean isIndependant, String idAdressePaiement, String referencePaiement, boolean isPorteEnCompte) {
        this.idTiers = idTiers;
        this.idAffilie = idAffilie;
        this.idExtra1 = idExtra1;
        this.idExtra2 = idExtra2;
        this.genrePrestation = genrePrestation;
        this.isEmployeur = isEmployeur;
        this.isIndependant = isIndependant;
        this.idAdressePaiement = idAdressePaiement;
        this.referencePaiement = referencePaiement;
        this.isPorteEnCompte = isPorteEnCompte;
    }

//    @Override
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
        } else if (genrePrestation.compareTo(key.genrePrestation) != 0) {
            return genrePrestation.compareTo(key.genrePrestation);
        } else if (isEmployeur.compareTo(key.isEmployeur) != 0) {
            return isEmployeur.compareTo(key.isEmployeur);
        } else if (isIndependant.compareTo(key.isIndependant) != 0) {
            return isIndependant.compareTo(key.isIndependant);
        } else if (idAdressePaiement.compareTo(key.idAdressePaiement) != 0) {
            return idAdressePaiement.compareTo(key.idAdressePaiement);
        } else if (referencePaiement.compareTo(key.referencePaiement) != 0) {
            return referencePaiement.compareTo(key.referencePaiement);
        } else if (isPorteEnCompte.compareTo(key.isPorteEnCompte) != 0) {
            return isPorteEnCompte.compareTo(key.isPorteEnCompte);
        } else {
            return 0;
        }
    }



    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Key)) {
            return false;
        }

        Key key = (Key) obj;

        return ((key.idTiers.equals(idTiers)) && (key.idAffilie.equals(idAffilie)) && (key.idExtra1.equals(idExtra1))
                && (key.idExtra2.equals(idExtra2)) && key.genrePrestation.equals(genrePrestation))
                && key.isEmployeur.equals(isEmployeur)
                && key.isIndependant.equals(isIndependant)
                && key.idAdressePaiement.equals(idAdressePaiement)
                && key.referencePaiement.equals(referencePaiement)
                && key.isPorteEnCompte.equals(isPorteEnCompte);
    }



    @Override
    public int hashCode() {
        return (idTiers + idAffilie + idExtra1 + idExtra2 + genrePrestation + isEmployeur + isIndependant
                + idAdressePaiement + referencePaiement + isPorteEnCompte).hashCode();
    }
}
