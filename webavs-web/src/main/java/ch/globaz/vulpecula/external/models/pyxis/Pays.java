/**
 * 
 */
package ch.globaz.vulpecula.external.models.pyxis;

/**
 * Repr�sentation m�tier du Pays au sens du module Pyxis
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public class Pays {
    private String id;
    private String idMonnaie;
    private String codeIso;
    private String codeCentrale;
    private String indicatif;
    private String codeFormatNpa;
    private String codeFormatTel;
    private String libelleFr;
    private String libelleAl;
    private String libelleIt;
    private boolean actif;

    /***
     * Retourne l'id du pays
     * 
     * @return id du pays
     */
    public String getId() {
        return id;
    }

    /**
     * Mise � jour de l'id du pays
     * 
     * @param id String repr�sentant l'id du pays
     */
    public void setId(String id) {
        this.id = id;
    }

    /***
     * Retourne l'id de la monnaie utilis�e dans le pays
     * 
     * @return id de la monnaie
     */
    public String getIdMonnaie() {
        return idMonnaie;
    }

    /***
     * Mise � jour de l'id de la monnaie utilis�e dans le pays
     * 
     * @param idMonnaie nouvelle id de la monnaie
     */
    public void setIdMonnaie(String idMonnaie) {
        this.idMonnaie = idMonnaie;
    }

    /**
     * Retourne le code ISO du pays
     * 
     * @return String repr�sentant le code ISO du pays (Suisse -> CH)
     */
    public String getCodeIso() {
        return codeIso;
    }

    /**
     * Mise � jour du code ISO utilis� dans le pays
     * 
     * @param codeIso Nouveau code ISO pour le pays
     */
    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    /**
     * Retourne le code utilis� par la centrale, soit un num�ro de 3 caract�res
     * 
     * @return String repr�sentant le nombre utilis� par la centrale
     */
    public String getCodeCentrale() {
        return codeCentrale;
    }

    /**
     * Mise � jour du code utilis� par la centrale
     * 
     * @param codeCentrale String repr�sentant un nombre de 3 caract�res
     */
    public void setCodeCentrale(String codeCentrale) {
        this.codeCentrale = codeCentrale;
    }

    /**
     * Retourne l'indicatif du pays
     * 
     * @return String repr�sentant l'indicatif du pays (Suisse -> 41)
     */
    public String getIndicatif() {
        return indicatif;
    }

    /**
     * Mise � jour de l'indicatif du pays
     * 
     * @param indicatif String repr�sentant l'indicatif du pays
     */
    public void setIndicatif(String indicatif) {
        this.indicatif = indicatif;
    }

    // FIXME: AGE: A compl�ter
    public String getCodeFormatNpa() {
        return codeFormatNpa;
    }

    // FIXME: AGE: A compl�ter
    public void setCodeFormatNpa(String codeFormatNpa) {
        this.codeFormatNpa = codeFormatNpa;
    }

    // FIXME: AGE: A compl�ter
    public String getCodeFormatTel() {
        return codeFormatTel;
    }

    // FIXME: AGE: A compl�ter
    public void setCodeFormatTel(String codeFormatTel) {
        this.codeFormatTel = codeFormatTel;
    }

    /**
     * Retourne le libellle du pays en fran�ais
     * 
     * @return String repr�sentant le libelle du pays en fran�ais (Suisse -> Suisse)
     */
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Mise � jour du libelle du pays en fran�ais
     * 
     * @param libelleFr Nouveau libelle pour le pays
     */
    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * Retourne le libelle du pays en allemand
     * 
     * @return String repr�sentant le libelle du pays en allemand (Suisse -> Schweiz)
     */
    public String getLibelleAl() {
        return libelleAl;
    }

    /**
     * Mise � jour du libelle du pays en allemand
     * 
     * @param libelleAl Nouveau libelle pour le pays
     */
    public void setLibelleAl(String libelleAl) {
        this.libelleAl = libelleAl;
    }

    /**
     * Retourne le libelle du pays en italien
     * 
     * @return String repr�sentant le libelle du pays en italien (Suisse -> Svizzera)
     */
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Mise � jour du libelle du pays en italien
     * 
     * @param libelleIt Nouveau libelle pour le pays
     */
    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    /**
     * Contr�le si le pays est actif ou non
     * 
     * @return true si le pays est actif
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Mise � jour de l'activit� du pays
     * 
     * @param actif true pour activer le pays, false pour le d�sactiver
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
