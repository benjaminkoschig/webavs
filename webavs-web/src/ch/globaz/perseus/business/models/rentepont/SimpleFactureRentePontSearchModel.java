package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleFactureRentePontSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateFacture;
    private String forIdQD;
    private String forMontant;
    private String forSousTypeSoin;

    public String getForDateFacture() {
        return forDateFacture;
    }

    public String getForIdQD() {
        return forIdQD;
    }

    public String getForMontant() {
        return forMontant;
    }

    public String getForSousTypeSoin() {
        return forSousTypeSoin;
    }

    public void setForDateFacture(String forDateFacture) {
        this.forDateFacture = forDateFacture;
    }

    public void setForIdQD(String forIdQD) {
        this.forIdQD = forIdQD;
    }

    public void setForMontant(String forMontant) {
        this.forMontant = forMontant;
    }

    public void setForSousTypeSoin(String forSousTypeSoin) {
        this.forSousTypeSoin = forSousTypeSoin;
    }

    @Override
    public Class whichModelClass() {
        return SimpleFactureRentePont.class;
    }
}
