/**
 * 
 */
package ch.globaz.vulpecula.external.models.pyxis;

/**
 * Représentation métier du Pays au sens du module Pyxis
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
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
     * Mise à jour de l'id du pays
     * 
     * @param id String représentant l'id du pays
     */
    public void setId(String id) {
        this.id = id;
    }

    /***
     * Retourne l'id de la monnaie utilisée dans le pays
     * 
     * @return id de la monnaie
     */
    public String getIdMonnaie() {
        return idMonnaie;
    }

    /***
     * Mise à jour de l'id de la monnaie utilisée dans le pays
     * 
     * @param idMonnaie nouvelle id de la monnaie
     */
    public void setIdMonnaie(String idMonnaie) {
        this.idMonnaie = idMonnaie;
    }

    /**
     * Retourne le code ISO du pays
     * 
     * @return String représentant le code ISO du pays (Suisse -> CH)
     */
    public String getCodeIso() {
        return codeIso;
    }

    /**
     * Mise à jour du code ISO utilisé dans le pays
     * 
     * @param codeIso Nouveau code ISO pour le pays
     */
    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    /**
     * Retourne le code utilisé par la centrale, soit un numéro de 3 caractères
     * 
     * @return String représentant le nombre utilisé par la centrale
     */
    public String getCodeCentrale() {
        return codeCentrale;
    }

    /**
     * Mise à jour du code utilisé par la centrale
     * 
     * @param codeCentrale String représentant un nombre de 3 caractères
     */
    public void setCodeCentrale(String codeCentrale) {
        this.codeCentrale = codeCentrale;
    }

    /**
     * Retourne l'indicatif du pays
     * 
     * @return String représentant l'indicatif du pays (Suisse -> 41)
     */
    public String getIndicatif() {
        return indicatif;
    }

    /**
     * Mise à jour de l'indicatif du pays
     * 
     * @param indicatif String représentant l'indicatif du pays
     */
    public void setIndicatif(String indicatif) {
        this.indicatif = indicatif;
    }

    // FIXME: AGE: A compléter
    public String getCodeFormatNpa() {
        return codeFormatNpa;
    }

    // FIXME: AGE: A compléter
    public void setCodeFormatNpa(String codeFormatNpa) {
        this.codeFormatNpa = codeFormatNpa;
    }

    // FIXME: AGE: A compléter
    public String getCodeFormatTel() {
        return codeFormatTel;
    }

    // FIXME: AGE: A compléter
    public void setCodeFormatTel(String codeFormatTel) {
        this.codeFormatTel = codeFormatTel;
    }

    /**
     * Retourne le libellle du pays en français
     * 
     * @return String représentant le libelle du pays en français (Suisse -> Suisse)
     */
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Mise à jour du libelle du pays en français
     * 
     * @param libelleFr Nouveau libelle pour le pays
     */
    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * Retourne le libelle du pays en allemand
     * 
     * @return String représentant le libelle du pays en allemand (Suisse -> Schweiz)
     */
    public String getLibelleAl() {
        return libelleAl;
    }

    /**
     * Mise à jour du libelle du pays en allemand
     * 
     * @param libelleAl Nouveau libelle pour le pays
     */
    public void setLibelleAl(String libelleAl) {
        this.libelleAl = libelleAl;
    }

    /**
     * Retourne le libelle du pays en italien
     * 
     * @return String représentant le libelle du pays en italien (Suisse -> Svizzera)
     */
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Mise à jour du libelle du pays en italien
     * 
     * @param libelleIt Nouveau libelle pour le pays
     */
    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    /**
     * Contrôle si le pays est actif ou non
     * 
     * @return true si le pays est actif
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Mise à jour de l'activité du pays
     * 
     * @param actif true pour activer le pays, false pour le désactiver
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
