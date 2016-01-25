package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.TauxAssuranceSimpleModel;

/**
 * Représente une assurance et ces taux du module d'affiliation (NAOS).
 * 
 * @since WebBMS 0.4.1
 */
public class AssuranceTauxComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -8805297182190028711L;

    private AssuranceSimpleModel assurance;
    private TauxAssuranceSimpleModel taux;

    public AssuranceTauxComplexModel() {
        assurance = new AssuranceSimpleModel();
        taux = new TauxAssuranceSimpleModel();
    }

    @Override
    public String getId() {
        return taux.getId();
    }

    @Override
    public String getSpy() {
        return taux.getSpy();
    }

    @Override
    public void setId(String id) {
        taux.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        taux.setSpy(spy);
    }

    /**
     * @return the assurance
     */
    public AssuranceSimpleModel getAssuranceSimpleModel() {
        return assurance;
    }

    /**
     * @return the taux
     */
    public TauxAssuranceSimpleModel getTauxAssuranceSimpleModel() {
        return taux;
    }

    /**
     * @param assurance the assurance to set
     */
    public void setAssuranceSimpleModel(AssuranceSimpleModel assurance) {
        this.assurance = assurance;
    }

    /**
     * @param taux the taux to set
     */
    public void setTauxAssuranceSimpleModel(TauxAssuranceSimpleModel taux) {
        this.taux = taux;
    }

}
