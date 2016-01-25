package ch.globaz.pegasus.business.models.dettecomptatcompense;

import globaz.jade.persistence.model.JadeComplexModel;

public class DetteCompenseCompteAnnexe extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String idCompteAnnexe;
    public String idTiers;
    public SimpleDetteComptatCompense simpleDetteComptatCompense;

    public DetteCompenseCompteAnnexe() {
        simpleDetteComptatCompense = new SimpleDetteComptatCompense();
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public SimpleDetteComptatCompense getSimpleDetteComptatCompense() {
        return simpleDetteComptatCompense;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setSimpleDetteComptatCompense(SimpleDetteComptatCompense simpleDetteComptatCompense) {
        this.simpleDetteComptatCompense = simpleDetteComptatCompense;
    }

    @Override
    public void setSpy(String spy) {
    }

}
