package globaz.apg.calculateur.maternite.acm2;

import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;

public class APPrestationACM2 {

    private APPrestation prestation;
    private APRepartitionPaiements repartitionPaiements;

    /**
     * @param prestation
     * @param repartitionPaiements
     */
    public APPrestationACM2(APPrestation prestation, APRepartitionPaiements repartitionPaiements) {
        super();
        this.prestation = prestation;
        this.repartitionPaiements = repartitionPaiements;
    }

    public APPrestation getPrestation() {
        return prestation;
    }

    public APRepartitionPaiements getRepartitionPaiements() {
        return repartitionPaiements;
    }

}
