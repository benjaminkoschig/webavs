package ch.globaz.hera.business.models.famille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class MembreFamille extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneEtendueComplexModel personneEtendue = null;
    private SimpleMembreFamille simpleMembreFamille = null;

    public MembreFamille() {
        super();
        simpleMembreFamille = new SimpleMembreFamille();
        personneEtendue = new PersonneEtendueComplexModel();
    }

    /**
     * Si le membre de famille a un idTiers, on donne la valeur de Pyxis sinon on donne la valeur d'Hera
     * 
     * @return
     */
    public String getCsCanton() {
        return JadeStringUtil.isIntegerEmpty(simpleMembreFamille.getIdTiers()) ? simpleMembreFamille.getCsCanton()
                : personneEtendue.getPersonne().getCanton();
    }

    /**
     * Si le membre de famille a un idTiers, on donne la valeur de Pyxis sinon on donne la valeur d'Hera
     * 
     * @return
     */
    public String getCsNationalite() {
        return JadeStringUtil.isIntegerEmpty(simpleMembreFamille.getIdTiers()) ? simpleMembreFamille.getCsNationalite()
                : personneEtendue.getTiers().getIdPays();
    }

    /**
     * Si le membre de famille a un idTiers, on donne la valeur de Pyxis sinon on donne la valeur d'Hera
     * 
     * @return
     */
    public String getCsSexe() {
        return JadeStringUtil.isIntegerEmpty(simpleMembreFamille.getIdTiers()) ? simpleMembreFamille.getCsSexe()
                : personneEtendue.getPersonne().getSexe();
    }

    /**
     * Si le membre de famille a un idTiers, on donne la valeur de Pyxis sinon on donne la valeur d'Hera
     * 
     * @return
     */
    public String getDateDeces() {
        return JadeStringUtil.isIntegerEmpty(simpleMembreFamille.getIdTiers()) ? simpleMembreFamille.getDateDeces()
                : personneEtendue.getPersonne().getDateDeces();
    }

    /**
     * Si le membre de famille a un idTiers, on donne la valeur de Pyxis sinon on donne la valeur d'Hera
     * 
     * @return
     */
    public String getDateNaissance() {
        return JadeStringUtil.isIntegerEmpty(simpleMembreFamille.getIdTiers()) ? simpleMembreFamille.getDateNaissance()
                : personneEtendue.getPersonne().getDateNaissance();
    }

    @Override
    public String getId() {
        return simpleMembreFamille.getId();
    }

    /**
     * Si le membre de famille a un idTiers, on donne la valeur de Pyxis sinon on donne la valeur d'Hera
     * 
     * @return
     */
    public String getNom() {
        return JadeStringUtil.isIntegerEmpty(simpleMembreFamille.getIdTiers()) ? simpleMembreFamille.getNom()
                : personneEtendue.getTiers().getDesignation1();
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    /**
     * Si le membre de famille a un idTiers, on donne la valeur de Pyxis sinon on donne la valeur d'Hera
     * 
     * @return
     */
    public String getPrenom() {
        return JadeStringUtil.isIntegerEmpty(simpleMembreFamille.getIdTiers()) ? simpleMembreFamille.getPrenom()
                : personneEtendue.getTiers().getDesignation2();
    }

    /**
     * @return the simpleMembreFamille
     */
    public SimpleMembreFamille getSimpleMembreFamille() {
        return simpleMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleMembreFamille.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleMembreFamille.setId(id);
    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    /**
     * @param simpleMembreFamille
     *            the simpleMembreFamille to set
     */
    public void setSimpleMembreFamille(SimpleMembreFamille simpleMembreFamille) {
        this.simpleMembreFamille = simpleMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleMembreFamille.setSpy(spy);
    }

}
