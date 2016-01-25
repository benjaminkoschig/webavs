package ch.globaz.vulpecula.external.models.pyxis;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.external.repositories.tiers.AdresseRepository;

/**
 * Repr�sentation m�tier d'un Tiers selon le module Pyxis
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public class Tiers implements DomainEntity {
    protected String idTiers;
    protected Pays pays;
    protected String typeTiers;
    protected String titreTiers;
    protected String designation1;
    protected String designation2;
    protected String designation3;
    protected String designation4;
    protected String langue;
    protected String designationUpper1;
    protected String designationUpper2;
    protected String designationCourt;
    protected String idTiersExterne;
    protected String politesseSpecFr;
    protected String politesseSpecDe;
    protected String politesseSpecIt;
    // FIXME: Work around afin de disposer d'une adresse unique et non pas d'un
    // tableau d'adresse
    protected Adresse adressePrincipale;
    protected String spy;

    public Tiers() {

    }

    public Tiers(final Tiers tiers) {
        idTiers = tiers.getIdTiers();
        pays = tiers.getPays();
        typeTiers = tiers.getTypeTiers();
        titreTiers = tiers.getTitreTiers();
        designation1 = tiers.getDesignation1();
        designation2 = tiers.getDesignation2();
        designation3 = tiers.getDesignation3();
        designation4 = tiers.getDesignation4();
        langue = tiers.getLangue();
        designationUpper1 = tiers.getDesignationUpper1();
        designationUpper2 = tiers.getDesignationUpper2();
        designationCourt = tiers.getDesignationCourt();
        idTiersExterne = tiers.getIdTiersExterne();
        politesseSpecFr = tiers.getPolitesseSpecFr();
        politesseSpecDe = tiers.getPolitesseSpecIt();
        politesseSpecIt = tiers.getPolitesseSpecDe();
        adressePrincipale = tiers.getAdressePrincipale();
        spy = tiers.getSpy();
    }

    /**
     * Retourne l'id du tiers
     * 
     * @return String repr�sentant l'id du tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Retourne le Pays du tiers
     * 
     * @return Objet m�tier repr�sentant le pays du tiers
     */
    public Pays getPays() {
        return pays;
    }

    /**
     * Mise � jour du pays du tiers
     * 
     * @param pays
     *            Nouveau pays du tiers
     */
    public void setPays(final Pays pays) {
        this.pays = pays;
    }

    /**
     * Retourne le type de tiers sous forme de code syst�me
     * 
     * @return Code syst�me repr�sentant le type de tiers (ex: Tiers, Banque,
     *         Administration...)
     */
    public String getTypeTiers() {
        return typeTiers;
    }

    /**
     * Mise � jour du type de tiers sous forme de code syst�me
     * 
     * @param typeTiers
     *            Code syst�me repr�sentant le type de tiers
     */
    public void setTypeTiers(final String typeTiers) {
        this.typeTiers = typeTiers;
    }

    /**
     * Retourne le titre du tiers sous forme de code syst�me
     * 
     * @return Code syst�me repr�sentant le type de tiers (ex: Mademoiselle,
     *         Mesdammes, Maitre, Monsieur Le Docteur..)
     */
    public String getTitreTiers() {
        return titreTiers;
    }

    /**
     * Mise � jour du code syst�me repr�sentant le titre du tiers
     * 
     * @param titreTiers
     *            Code syst�me repr�sentant le nouveau type du tiers
     */
    public void setTitreTiers(final String titreTiers) {
        this.titreTiers = titreTiers;
    }

    /**
     * Retourne la partie 1 de la designation
     * 
     * @return String repr�sentant la partie 1 de la d�signation
     */
    public String getDesignation1() {
        return designation1;
    }

    /**
     * Mise � jour de la partie 1 de la designation
     * 
     * @param designation1
     *            String repr�sentant la nouvelle partie 1 de la d�signation
     */
    public void setDesignation1(final String designation1) {
        this.designation1 = designation1;
    }

    /**
     * Retourne la partie 2 de la designation
     * 
     * @return String repr�sentant la partie 2 de la d�signation
     */
    public String getDesignation2() {
        return designation2;
    }

    /**
     * Mise � jour de la partie 2 de la designation
     * 
     * @param designation2
     *            String repr�sentant la nouvelle partie 2 de la d�signation
     */
    public void setDesignation2(final String designation2) {
        this.designation2 = designation2;
    }

    /**
     * Retourne la partie 3 de la designation
     * 
     * @return String repr�sentant la partie 3 de la d�signation
     */
    public String getDesignation3() {
        return designation3;
    }

    /**
     * Mise � jour de la partie 3 de la designation
     * 
     * @param designation3
     *            String repr�sentant la nouvelle partie 3 de la d�signation
     */
    public void setDesignation3(final String designation3) {
        this.designation3 = designation3;
    }

    /**
     * Retourne la partie 4 de la designation
     * 
     * @return String repr�sentant la partie 4 de la d�signation
     */
    public String getDesignation4() {
        return designation4;
    }

    /**
     * Mise � jour de la partie 4 de la designation
     * 
     * @param designation4
     *            String repr�sentant la nouvelle partie 4 de la d�signation
     */
    public void setDesignation4(final String designation4) {
        this.designation4 = designation4;
    }

    /**
     * Retourne le code syst�me repr�sentant la langue
     * 
     * @return Code syst�me repr�sentant la langue (Allemand, Anglais,
     *         Italien..)
     */
    public String getLangue() {
        return langue;
    }

    /**
     * Mise � jour du code syst�me repr�sentant la langue
     * 
     * @param langue
     *            Nouveau code syst�me repr�sentant la langue
     */
    public void setLangue(final String langue) {
        this.langue = langue;
    }

    /**
     * Retourne la premi�re partie de la d�signation en majuscule
     * 
     * @return String repr�sentant la premi�re partie de la d�signation
     */
    public String getDesignationUpper1() {
        return designationUpper1;
    }

    /**
     * Mise � jour de la premi�re partie de la d�signation en majuscule
     * 
     * @param designationUpper1
     *            String repr�sentant la nouvelle premi�re partie de la
     *            d�signation
     */
    public void setDesignationUpper1(final String designationUpper1) {
        this.designationUpper1 = designationUpper1;
    }

    /**
     * Retourne la seconde partie de la d�signation en majuscule
     * 
     * @return String repr�sentant la seconde partie de la d�signation
     */
    public String getDesignationUpper2() {
        return designationUpper2;
    }

    /**
     * Mise � jour de la seconde partie de la d�signation en majuscule
     * 
     * @param designationUpper2
     *            repr�sentant la nouvelle seconde partie de la d�signation
     */
    public void setDesignationUpper2(final String designationUpper2) {
        this.designationUpper2 = designationUpper2;
    }

    /**
     * Retourne la d�signation abr�g�
     * 
     * @return String repr�sentant la d�signation abr�g�
     */
    public String getDesignationCourt() {
        return designationCourt;
    }

    /**
     * Mise � jour de la d�signation abr�g�
     * 
     * @param designationCourt
     *            String repr�sentant la nouvelle d�signation abr�g�
     */
    public void setDesignationCourt(final String designationCourt) {
        this.designationCourt = designationCourt;
    }

    // FIXME: Qu'est-ce que l'id tiers externe ?
    public String getIdTiersExterne() {
        return idTiersExterne;
    }

    // FIXME: Qu'est-ce que l'id tiers externe ?
    public void setIdTiersExterne(final String idTiersExterne) {
        this.idTiersExterne = idTiersExterne;
    }

    /**
     * Retourne la politesse en application sur le tiers.
     * La d�termination est effectu� comme suit :
     * 
     * <ul>
     * <li>1. Si le tiers dispose d'une politesse sp�cifique, on retourne la politesse sp�cifique
     * <li>2. Si le tiers ne dispose pas d'une poltisse sp�cifique, on retourne le code syst�me du titre
     */
    public String getPolitesse(CodeLangue codeLangue) {
        String politesseSpec = getPolitesseSpec(codeLangue);
        if (politesseSpec != null && !politesseSpec.isEmpty()) {
            return politesseSpec;
        } else {
            return titreTiers;
        }
    }

    /**
     * Retourne la politesse sp�cifique selon la langue concern�e.
     * 
     * @param codeLangue Le code langue dans laquelle la traduction est souhait�
     * @return String repr�sentant la politesse
     */
    public String getPolitesseSpec(CodeLangue codeLangue) {
        switch (codeLangue) {
            case FR:
                return politesseSpecFr;
            case DE:
                return politesseSpecDe;
            case IT:
                return politesseSpecIt;
            default:
                return politesseSpecFr;
        }
    }

    /**
     * Retourne la politesse sp�cifi� en fran�ais
     * 
     * @return String repr�sentant la politesse en fran�ais
     */
    public String getPolitesseSpecFr() {
        return politesseSpecFr;
    }

    /**
     * Mise � jour de la politesse en fran�ais
     * 
     * @param politesseSpecFr
     *            String repr�sentant la politesse en fran�ais
     */
    public void setPolitesseSpecFr(final String politesseSpecFr) {
        this.politesseSpecFr = politesseSpecFr;
    }

    /**
     * Retourne la politesse sp�cifi� en allemand
     * 
     * @return String repr�sentant la politesse en allemand
     */
    public String getPolitesseSpecDe() {
        return politesseSpecDe;
    }

    /**
     * Mise � jour de la politesse en allemand
     * 
     * @param politesseSpecDe
     *            String repr�sentant la politesse en allemand
     */
    public void setPolitesseSpecDe(final String politesseSpecDe) {
        this.politesseSpecDe = politesseSpecDe;
    }

    /**
     * Retourne la politesse sp�cifi� en italien
     * 
     * @return String repr�sentant la politesse en italien
     */
    public String getPolitesseSpecIt() {
        return politesseSpecIt;
    }

    /**
     * Mise � jour de la politesse en italien
     * 
     * @param politesseSpecIt
     *            String repr�sentant la politesse en italien
     */
    public void setPolitesseSpecIt(final String politesseSpecIt) {
        this.politesseSpecIt = politesseSpecIt;
    }

    /**
     * Retourne l'adresse principale du tiers ATTENTION : La r�cup�ration de
     * l'adresse est un service tr�s lourd en base de donn�es. De ce fait, il
     * faut utiliser le repository {@link AdresseRepository#findAdressePrioriteCourrierByIdTiers(String)} afin de r�cup�rer
     * l'adresse.
     * 
     * @return {@link Adresse} du tiers
     */
    public Adresse getAdressePrincipale() {
        return adressePrincipale;
    }

    /**
     * Retourne l'adresse principale format�e pour les ent�tes de lettres, soit :
     * <ul>
     * <li>Attention
     * <li>Rue
     * <li>NPA - Localite
     * 
     * 
     * @return String repr�sentant l'adresse principale formatt�e
     */
    public String getAdressePrincipaleFormatee() {
        if (adressePrincipale == null) {
            return null;
        }
        return adressePrincipale.getAdresseFormatte();
    }

    /**
     * Mise � jour de l'adresse du tiers
     * 
     * @param adressePrincipale
     *            Nouvelle adresse principale du tiers
     */
    public void setAdressePrincipale(final Adresse adressePrincipale) {
        this.adressePrincipale = adressePrincipale;
    }

    @Override
    public String getId() {
        return idTiers;
    }

    @Override
    public void setId(final String id) {
        idTiers = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(final String spy) {
        this.spy = spy;
    }
}
