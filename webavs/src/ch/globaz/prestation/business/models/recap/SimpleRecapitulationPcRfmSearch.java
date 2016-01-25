package ch.globaz.prestation.business.models.recap;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * <p>
 * Modèle de recherche pour les récapitulatifs de paiement mensuel dans les PC/RFM.
 * </p>
 * <p>
 * Permet de recherche un récapitulatif par son ID, le mois du paiement mensuel ou par le code de récapitulatif.
 * </p>
 * 
 * @author PBA
 */
public class SimpleRecapitulationPcRfmSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCode;
    private String forId;
    private String forMois;

    public SimpleRecapitulationPcRfmSearch() {
        super();

        forCode = "";
        forId = "";
        forMois = "";
    }

    public String getForCode() {
        return forCode;
    }

    public String getForId() {
        return forId;
    }

    public String getForMois() {
        return forMois;
    }

    public void setForCode(String forCode) {
        this.forCode = forCode;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public void setForMois(String forMois) {
        this.forMois = forMois;
    }

    @Override
    public Class<?> whichModelClass() {
        return SimpleRecapitulationPcRfm.class;
    }
}
