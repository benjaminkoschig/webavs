package ch.globaz.vulpecula.business.models.travailleur;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService.motifsModification;

public class PersonneEtendueMetierComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TiersSimpleModel tiers = new TiersSimpleModel();
    private PersonneSimpleModel personne = new PersonneSimpleModel();
    private PersonneEtendueSimpleModel personneEtendue = new PersonneEtendueSimpleModel();

    private motifsModification motifModifAvs;
    private String dateModifAvs;
    private motifsModification motifModifContribuable;
    private String dateModifContribuable;
    private motifsModification motifModifDesignation1;
    private String dateModifDesignation1;
    private motifsModification motifModifDesignation2;
    private String dateModifDesignation2;
    private motifsModification motifModifDesignation3;
    private String dateModifDesignation3;
    private motifsModification motifModifDesignation4;
    private String dateModifDesignation4;
    private motifsModification motifModifTitre;
    private String dateModifTitre;
    private motifsModification motifModifPays;
    private String dateModifPays;
    private String oldDateDeces;

    public TiersSimpleModel getTiers() {
        return tiers;
    }

    public PersonneSimpleModel getPersonne() {
        return personne;
    }

    public PersonneEtendueSimpleModel getPersonneEtendue() {
        return personneEtendue;
    }

    @Override
    public String getId() {
        return getPersonneEtendue().getId();
    }

    @Override
    public void setId(String id) {
        getPersonneEtendue().setId(id);
    }

    @Override
    public String getSpy() {
        return getPersonneEtendue().getSpy();
    }

    @Override
    public void setSpy(String spy) {
        getPersonneEtendue().setSpy(spy);
    }

    /**
     * @param motifModifAvs
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifAvs(motifsModification motifModifAvs) {
        this.motifModifAvs = motifModifAvs;
    }

    public motifsModification getMotifModifAvs() {
        return motifModifAvs;
    }

    public void setDateModifAvs(String dateModifAvs) {
        this.dateModifAvs = dateModifAvs;
    }

    public String getDateModifAvs() {
        return dateModifAvs;
    }

    /**
     * @param motifModifContribuable
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifContribuable(motifsModification motifModifContribuable) {
        this.motifModifContribuable = motifModifContribuable;
    }

    public motifsModification getMotifModifContribuable() {
        return motifModifContribuable;
    }

    public void setDateModifContribuable(String dateModifContribuable) {
        this.dateModifContribuable = dateModifContribuable;
    }

    public String getDateModifContribuable() {
        return dateModifContribuable;
    }

    /**
     * @param motifModifDesignation1
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifDesignation1(motifsModification motifModifDesignation1) {
        this.motifModifDesignation1 = motifModifDesignation1;
    }

    public motifsModification getMotifModifDesignation1() {
        return motifModifDesignation1;
    }

    public void setDateModifDesignation1(String dateModifDesignation1) {
        this.dateModifDesignation1 = dateModifDesignation1;
    }

    public String getDateModifDesignation1() {
        return dateModifDesignation1;
    }

    public motifsModification getMotifModifDesignation2() {
        return motifModifDesignation2;
    }

    /**
     * @param motifModifDesignation2
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifDesignation2(motifsModification motifModifDesignation2) {
        this.motifModifDesignation2 = motifModifDesignation2;
    }

    public String getDateModifDesignation2() {
        return dateModifDesignation2;
    }

    public void setDateModifDesignation2(String dateModifDesignation2) {
        this.dateModifDesignation2 = dateModifDesignation2;
    }

    public motifsModification getMotifModifDesignation3() {
        return motifModifDesignation3;
    }

    /**
     * @param motifModifDesignation3
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifDesignation3(motifsModification motifModifDesignation3) {
        this.motifModifDesignation3 = motifModifDesignation3;
    }

    public String getDateModifDesignation3() {
        return dateModifDesignation3;
    }

    public void setDateModifDesignation3(String dateModifDesignation3) {
        this.dateModifDesignation3 = dateModifDesignation3;
    }

    public motifsModification getMotifModifDesignation4() {
        return motifModifDesignation4;
    }

    /**
     * @param motifModifDesignation4
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifDesignation4(motifsModification motifModifDesignation4) {
        this.motifModifDesignation4 = motifModifDesignation4;
    }

    public String getDateModifDesignation4() {
        return dateModifDesignation4;
    }

    public void setDateModifDesignation4(String dateModifDesignation4) {
        this.dateModifDesignation4 = dateModifDesignation4;
    }

    public motifsModification getMotifModifTitre() {
        return motifModifTitre;
    }

    /**
     * @param motifModifTitre
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifTitre(motifsModification motifModifTitre) {
        this.motifModifTitre = motifModifTitre;
    }

    public String getDateModifTitre() {
        return dateModifTitre;
    }

    public void setDateModifTitre(String dateModifTitre) {
        this.dateModifTitre = dateModifTitre;
    }

    /**
     * 
     * @param motifModifPays
     *            - Enum de PersonneEtendueService.motifsModification
     */
    public void setMotifModifPays(motifsModification motifModifPays) {
        this.motifModifPays = motifModifPays;
    }

    public motifsModification getMotifModifPays() {
        return motifModifPays;
    }

    public void setDateModifPays(String dateModifPays) {
        this.dateModifPays = dateModifPays;
    }

    public String getDateModifPays() {
        return dateModifPays;
    }

    public void setOldDateDeces(String oldDateDeces) {
        this.oldDateDeces = oldDateDeces;
    }

    public String getOldDateDeces() {
        return oldDateDeces;
    }
}
