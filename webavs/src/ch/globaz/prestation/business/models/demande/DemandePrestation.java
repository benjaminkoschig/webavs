package ch.globaz.prestation.business.models.demande;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class DemandePrestation extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDemandePrestation demandePrestation = null;
    private PersonneEtendueComplexModel personneEtendue = null;

    public DemandePrestation() {
        super();
        demandePrestation = new SimpleDemandePrestation();
        personneEtendue = new PersonneEtendueComplexModel();
    }

    /**
     * @return the demandePrestation
     */
    public SimpleDemandePrestation getDemandePrestation() {
        return demandePrestation;
    }

    @Override
    public String getId() {
        return demandePrestation.getId();
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    @Override
    public String getSpy() {
        return demandePrestation.getSpy();
    }

    /**
     * @param demandePrestation
     *            the demandePrestation to set
     */
    public void setDemandePrestation(SimpleDemandePrestation demandePrestation) {
        this.demandePrestation = demandePrestation;
    }

    @Override
    public void setId(String id) {
        demandePrestation.setId(id);
    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    @Override
    public void setSpy(String spy) {
        demandePrestation.setSpy(spy);
    }

}
