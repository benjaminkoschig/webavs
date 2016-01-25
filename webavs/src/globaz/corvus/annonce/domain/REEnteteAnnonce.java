package globaz.corvus.annonce.domain;

import globaz.corvus.annonce.RECodeApplicationProvider;
import globaz.corvus.annonce.REEtatAnnonce;

/**
 * L'en-t�te d'annonce est la classe m�re de toutes annonce de rentes
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
     * Retourne l'�tat de l'annonce. Par d�faut, une annonce est en �tat ouvert lors de sa cr�ation
     * 
     * @return l'�tat de l'annonce
     */
    public REEtatAnnonce getEtat() {
        return etat;
    }

    /**
     * Retourne le num�ro de la caisse (ex : 150 pour la CCJU)
     * 
     * @param numeroCaisse le num�ro de la caisse
     */
    public Integer getNumeroCaisse() {
        return numeroCaisse;
    }

    /**
     * D�finit le num�ro de la caisse (ex : 150 pour la CCJU)
     * 
     * @param numeroCaisse le num�ro de la caisse
     */
    public void setNumeroCaisse(Integer numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }

    /**
     * Retourne le num�ro de l'agence (ex : 0 pour la CCJU)
     * 
     * @return le num�ro de l'agence
     */
    public Integer getNumeroAgence() {
        return numeroAgence;
    }

    /**
     * D�finit le num�ro de l'agence (ex : 0 pour la CCJU)
     * 
     * @return le num�ro de l'agence
     */
    public void setNumeroAgence(Integer numeroAgence) {
        this.numeroAgence = numeroAgence;
    }

}
