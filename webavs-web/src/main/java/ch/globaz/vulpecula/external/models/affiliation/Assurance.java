package ch.globaz.vulpecula.external.models.affiliation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;

/**
 * Représente une assurance pouvant être lié au sens du module affiliation
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 * 
 */
public class Assurance {
    public static final List<TypeAssurance> AF;

    static {
        AF = new ArrayList<TypeAssurance>();
        AF.add(TypeAssurance.COTISATION_FFPP_MASSE);
        AF.add(TypeAssurance.COTISATION_FFPP_CAPITATION);
        AF.add(TypeAssurance.COTISATION_AF);
    }

    public static final List<TypeAssurance> NOT_CAISSES_SOCIALES;

    static {
        NOT_CAISSES_SOCIALES = new ArrayList<TypeAssurance>();
        NOT_CAISSES_SOCIALES.add(TypeAssurance.COTISATION_AVS_AI);
        NOT_CAISSES_SOCIALES.add(TypeAssurance.ASSURANCE_CHOMAGE);
        NOT_CAISSES_SOCIALES.add(TypeAssurance.COTISATION_AC2);
        NOT_CAISSES_SOCIALES.add(TypeAssurance.FRAIS_ADMINISTRATION);
    }

    public static final List<TypeAssurance> NOT_CAISSES_SOCIALES_AND_AF;

    static {
        NOT_CAISSES_SOCIALES_AND_AF = new ArrayList<TypeAssurance>();
        NOT_CAISSES_SOCIALES_AND_AF.add(TypeAssurance.COTISATION_AVS_AI);
        NOT_CAISSES_SOCIALES_AND_AF.add(TypeAssurance.ASSURANCE_CHOMAGE);
        NOT_CAISSES_SOCIALES_AND_AF.add(TypeAssurance.COTISATION_AC2);
        NOT_CAISSES_SOCIALES_AND_AF.add(TypeAssurance.FRAIS_ADMINISTRATION);
        NOT_CAISSES_SOCIALES_AND_AF.addAll(AF);
    }

    private String id;
    private String libelleFr;
    private String libelleAl;
    private String libelleIt;
    private String libelleCourtFr;
    private String libelleCourtAl;
    private String libelleCourtIt;
    private String rubriqueId;
    private String assuranceCanton;
    private String assuranceGenre;
    private TypeAssurance typeAssurance;
    private String assurance13;
    private String tauxParCaisse;
    private String surDocAcompte;

    /**
     * Retourne l'id de l'assurance
     * 
     * @return String représentant l'id
     */
    public String getId() {
        return id;
    }

    /**
     * Mise à jour de l'id de l'assurance
     * 
     * @param id Nouvel id de l'assurance
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Retourne la désignation de l'assurance en français
     * 
     * @return la désignation en français
     */
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Mise à jour de la désignation de l'assurance en français
     * 
     * @param libelleFr Nouvelle désignation de l'assurance
     */
    public void setLibelleFr(final String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * Retourne la désignation de l'assurance en allemand
     * 
     * @return la désignation en allemand
     */
    public String getLibelleAl() {
        return libelleAl;
    }

    /**
     * Mise à jour de la désignation de l'assurance en allemand
     * 
     * @param libelleAl Nouvelle désignation de l'assurance
     */
    public void setLibelleAl(final String libelleAl) {
        this.libelleAl = libelleAl;
    }

    /**
     * Retourne la désignation de l'assurance en italien
     * 
     * @return la désignation en italien
     */
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Mise à jour de la désignation de l'assurance en italien
     * 
     * @param libelleIt Nouvelle désignation de l'assurance
     */
    public void setLibelleIt(final String libelleIt) {
        this.libelleIt = libelleIt;
    }

    /**
     * Retourne la désignation abrégée en français
     * 
     * @return désignation abrégée en français
     */
    public String getLibelleCourtFr() {
        return libelleCourtFr;
    }

    /**
     * Mise à jour de la désignation abrégée en français
     * 
     * @param libelleCourtFr libelle abrégé
     */
    public void setLibelleCourtFr(final String libelleCourtFr) {
        this.libelleCourtFr = libelleCourtFr;
    }

    /**
     * Retourne la désignation abrégée en allemand
     * 
     * @return désignation en allemand
     */
    public String getLibelleCourtAl() {
        return libelleCourtAl;
    }

    /**
     * Mise à jour de la désignation abrégée en allemand
     * 
     * @param libelleCourtAl Nouvelle désignation en allemand
     */
    public void setLibelleCourtAl(final String libelleCourtAl) {
        this.libelleCourtAl = libelleCourtAl;
    }

    /**
     * Retourne la désignation abrégée en allemand
     * 
     * @return désignation en italien
     */
    public String getLibelleCourtIt() {
        return libelleCourtIt;
    }

    /**
     * Mise à jour de la désignation abrégée en italien
     * 
     * @param libelleCourtIt désignation abrégée en italien
     */
    public void setLibelleCourtIt(final String libelleCourtIt) {
        this.libelleCourtIt = libelleCourtIt;
    }

    // FIXME: ??
    public String getRubriqueId() {
        return rubriqueId;
    }

    // FIXME: ??
    public void setRubriqueId(final String rubriqueId) {
        this.rubriqueId = rubriqueId;
    }

    // FIXME: ??
    public String getAssuranceCanton() {
        return assuranceCanton;
    }

    // FIXME: ??
    public void setAssuranceCanton(final String assuranceCanton) {
        this.assuranceCanton = assuranceCanton;
    }

    // FIXME: ??
    public String getAssuranceGenre() {
        return assuranceGenre;
    }

    // FIXME: ??
    public void setAssuranceGenre(final String assuranceGenre) {
        this.assuranceGenre = assuranceGenre;
    }

    // FIXME: ??
    public TypeAssurance getTypeAssurance() {
        return typeAssurance;
    }

    // FIXME: ??
    public void setTypeAssurance(final TypeAssurance typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    // FIXME: ??
    public String getAssurance13() {
        return assurance13;
    }

    // FIXME: ??
    public void setAssurance13(final String assurance13) {
        this.assurance13 = assurance13;
    }

    // FIXME: ??
    public String getTauxParCaisse() {
        return tauxParCaisse;
    }

    // FIXME: ??
    public void setTauxParCaisse(final String tauxParCaisse) {
        this.tauxParCaisse = tauxParCaisse;
    }

    // FIXME: ??
    public String getSurDocAcompte() {
        return surDocAcompte;
    }

    // FIXME: ??
    public void setSurDocAcompte(final String surDocAcompte) {
        this.surDocAcompte = surDocAcompte;
    }

    /**
     * Retourne si le type d'assurance est de type AVS.
     * 
     * @return true si type d'assurance est de type AVS
     */
    public boolean isTypeAVS() {
        return TypeAssurance.COTISATION_AVS_AI.equals(typeAssurance);
    }

    /**
     * Retourne si le type d'assurance est de type AC.
     * 
     * @return true si type d'assurance est de type AC
     */
    public boolean isTypeAC() {
        return TypeAssurance.ASSURANCE_CHOMAGE.equals(typeAssurance);
    }

    /**
     * Retourne si le type d'assurance est de type AF.
     * 
     * @return true si type d'assurance est de type AF
     */
    public boolean isTypeAF() {
        return TypeAssurance.COTISATION_AF.equals(typeAssurance);
    }

    /**
     * Retourne si le type d'assurance est de type AC2.
     * 
     * @return true si type d'assurance est de type AC2
     */
    public boolean isTypeAC2() {
        return TypeAssurance.COTISATION_AC2.equals(typeAssurance);
    }

    /**
     * Retourne si le type d'assurance est de type LPP.
     * 
     * @return true si type assurance est de type LPP
     */
    public boolean isTypeLPP() {
        return TypeAssurance.COTISATION_LPP.equals(typeAssurance);
    }

    public String getLibelle(Locale locale) {
        if (Locale.GERMAN.equals(locale)) {
            return libelleAl;
        } else if (Locale.ITALIAN.equals(locale)) {
            return libelleIt;
        } else {
            return libelleFr;
        }
    }

    /**
     * Retourne le libellé court dans la langue locale passée en paramètre
     * 
     * @param locale Langue dans laquelle on veut que le libellé soit retourné
     * 
     * @return libellé court dans la langue souhaitée
     */
    public String getLibelleCourt(Locale locale) {
        if (Locale.GERMAN.equals(locale)) {
            return libelleCourtAl;
        } else if (Locale.ITALIAN.equals(locale)) {
            return libelleCourtIt;
        } else {
            return libelleCourtFr;
        }
    }

    @Override
    public String toString() {
        return "Assurance [id=" + id + ", libelleFr=" + libelleFr + ", libelleAl=" + libelleAl + ", libelleIt="
                + libelleIt + ", libelleCourtFr=" + libelleCourtFr + ", libelleCourtAl=" + libelleCourtAl
                + ", libelleCourtIt=" + libelleCourtIt + ", rubriqueId=" + rubriqueId + ", assuranceCanton="
                + assuranceCanton + ", assuranceGenre=" + assuranceGenre + ", typeAssurance=" + typeAssurance
                + ", assurance13=" + assurance13 + ", tauxParCaisse=" + tauxParCaisse + ", surDocAcompte="
                + surDocAcompte + "]";
    }
}
