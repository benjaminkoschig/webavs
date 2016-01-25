package globaz.corvus.annonce.domain;

import globaz.corvus.annonce.RECodeApplicationProvider;
import globaz.corvus.annonce.REEtatAnnonce;

/**
 * L'en-tête d'annonce est la classe mère de toutes annonce de rentes
 * 
 * @author lga
 * 
 */
public abstract class REEnteteAnnonce {

    private REEtatAnnonce etat = REEtatAnnonce.OUVERT;
    private Integer numeroAgence;
    private Integer numeroCaisse;

    /**
     * Retourne un objet capable de fournir le code application
     * 
     * @return un objet capable de fournir le code application
     */
    public abstract RECodeApplicationProvider getCodeApplicationProvider();

    /**
     * Retourne l'état de l'annonce. Par défaut, une annonce est en état ouvert lors de sa création
     * 
     * @return l'état de l'annonce
     */
    public REEtatAnnonce getEtat() {
        return etat;
    }

    /**
     * Retourne le numéro de la caisse (ex : 150 pour la CCJU)
     * 
     * @param numeroCaisse le numéro de la caisse
     */
    public Integer getNumeroCaisse() {
        return numeroCaisse;
    }

    /**
     * Définit le numéro de la caisse (ex : 150 pour la CCJU)
     * 
     * @param numeroCaisse le numéro de la caisse
     */
    public void setNumeroCaisse(Integer numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }

    /**
     * Retourne le numéro de l'agence (ex : 0 pour la CCJU)
     * 
     * @return le numéro de l'agence
     */
    public Integer getNumeroAgence() {
        return numeroAgence;
    }

    /**
     * Définit le numéro de l'agence (ex : 0 pour la CCJU)
     * 
     * @return le numéro de l'agence
     */
    public void setNumeroAgence(Integer numeroAgence) {
        this.numeroAgence = numeroAgence;
    }

}
