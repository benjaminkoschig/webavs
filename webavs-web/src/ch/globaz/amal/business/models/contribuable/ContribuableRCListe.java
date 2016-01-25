/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author CBU
 * 
 */
public class ContribuableRCListe extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDeces = null;
    private String dateNaissance = null;
    private String designation1 = null;
    private String designation2 = null;
    private String designationUpper1 = null;
    private String designationUpper2 = null;
    private SimpleFamille famille = null;
    private String familleDateNaissance = null;
    private String familleNoAVS = null;
    private String familleNomPrenom = null;
    private String familleNomPrenomUpper = null;
    private String familleSexe = null;
    private String idContribuable = null;
    private String idContribuableFamille = null;
    private String idFamille = null;
    private String idTier = null;
    private Boolean isContribuable = null;
    private Boolean isContribuableActif = null;
    private String numAvsActuel = null;
    private String numContribuable = null;
    private String numPersonnel = null;
    private PersonneEtendueComplexModel personneEtendue = null;
    private String sexe = null;
    private SimpleContribuable simpleContribuable = null;

    /**
	 * 
	 */
    public ContribuableRCListe() {
        super();
        simpleContribuable = new SimpleContribuable();
        personneEtendue = new PersonneEtendueComplexModel();
        famille = new SimpleFamille();
    }

    public ContribuableRCListe(PersonneEtendueComplexModel personneEtendue) {
        super();
        this.personneEtendue = personneEtendue;
        simpleContribuable = new SimpleContribuable();
    }

    public ContribuableRCListe(SimpleContribuable simpleContribuable) {
        super();
        this.simpleContribuable = simpleContribuable;
        personneEtendue = new PersonneEtendueComplexModel();
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getDesignationUpper1() {
        return designationUpper1;
    }

    public String getDesignationUpper2() {
        return designationUpper2;
    }

    public SimpleFamille getFamille() {
        return famille;
    }

    /**
     * @return the familleDateNaissance
     */
    public String getFamilleDateNaissance() {
        return familleDateNaissance;
    }

    /**
     * @return the familleNoAVS
     */
    public String getFamilleNoAVS() {
        return familleNoAVS;
    }

    /**
     * @return the familleNomPrenom
     */
    public String getFamilleNomPrenom() {
        return familleNomPrenom;
    }

    /**
     * @return the familleNomPrenomUpper
     */
    public String getFamilleNomPrenomUpper() {
        return familleNomPrenomUpper;
    }

    /**
     * @return the familleSexe
     */
    public String getFamilleSexe() {
        return familleSexe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdContribuable();
    }

    public String getIdContribuable() {
        return idContribuable;
    }

    public String getIdContribuableFamille() {
        return idContribuableFamille;
    }

    public String getIdFamille() {
        return idFamille;
    }

    public String getIdTier() {
        return idTier;
    }

    public Boolean getIsContribuable() {
        return isContribuable;
    }

    public Boolean getIsContribuableActif() {
        return isContribuableActif;
    }

    public String getNumAvsActuel() {
        return numAvsActuel;
    }

    public String getNumContribuable() {
        return numContribuable;
    }

    public String getNumPersonnel() {
        return numPersonnel;
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public String getSexe() {
        return sexe;
    }

    /**
     * @return the simpleContribuable
     */
    public SimpleContribuable getSimpleContribuable() {
        return simpleContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return null;
        // return this.contribuable.getSpy();
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    public void setDesignationUpper1(String designationUpper1) {
        this.designationUpper1 = designationUpper1;
    }

    public void setDesignationUpper2(String designationUpper2) {
        this.designationUpper2 = designationUpper2;
    }

    public void setFamille(SimpleFamille famille) {
        this.famille = famille;
    }

    /**
     * @param familleDateNaissance
     *            the familleDateNaissance to set
     */
    public void setFamilleDateNaissance(String familleDateNaissance) {
        this.familleDateNaissance = familleDateNaissance;
    }

    /**
     * @param familleNoAVS
     *            the familleNoAVS to set
     */
    public void setFamilleNoAVS(String familleNoAVS) {
        this.familleNoAVS = familleNoAVS;
    }

    /**
     * @param familleNomPrenom
     *            the familleNomPrenom to set
     */
    public void setFamilleNomPrenom(String familleNomPrenom) {
        this.familleNomPrenom = familleNomPrenom;
    }

    /**
     * @param familleNomPrenomUpper
     *            the familleNomPrenomUpper to set
     */
    public void setFamilleNomPrenomUpper(String familleNomPrenomUpper) {
        this.familleNomPrenomUpper = familleNomPrenomUpper;
    }

    /**
     * @param familleSexe
     *            the familleSexe to set
     */
    public void setFamilleSexe(String familleSexe) {
        this.familleSexe = familleSexe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        setIdContribuable(id);
        // this.contribuable.setId(id);
    }

    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    public void setIdContribuableFamille(String idContribuableFamille) {
        this.idContribuableFamille = idContribuableFamille;
    }

    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

    public void setIdTier(String idTier) {
        this.idTier = idTier;
    }

    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    public void setIsContribuableActif(Boolean isContribuableActif) {
        this.isContribuableActif = isContribuableActif;
    }

    public void setNumAvsActuel(String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    public void setNumContribuable(String numContribuable) {
        this.numContribuable = numContribuable;
    }

    public void setNumPersonnel(String numPersonnel) {
        this.numPersonnel = numPersonnel;
    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * @param contribuable
     *            the simpleContribuable to set
     */
    public void setSimpleContribuable(SimpleContribuable simpleContribuable) {
        this.simpleContribuable = simpleContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
    }

}
