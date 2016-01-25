package ch.globaz.pegasus.business.vo.blocage;

import ch.globaz.pegasus.business.vo.adresse.AdressePaiement;

public class Creancier extends LigneDeblocage {
    private AdressePaiement adressePaiement;
    private String refPaiement = null;
    private String designationTiers1 = null;
    private String designationTiers2 = null;

    public String getDesignationTiers1() {
        return designationTiers1;
    }

    public void setDesignationTiers1(String designationTiers1) {
        this.designationTiers1 = designationTiers1;
    }

    public String getDesignationTiers2() {
        return designationTiers2;
    }

    public void setDesignationTiers2(String designationTiers2) {
        this.designationTiers2 = designationTiers2;
    }

    public AdressePaiement getAdressePaiement() {
        return adressePaiement;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setAdressePaiement(AdressePaiement adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }
}
