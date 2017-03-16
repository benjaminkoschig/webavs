package globaz.corvus.db.lignedeblocage;

import ch.globaz.common.domaine.AdressePaiement;

public class RELigneDeblocageCreancier extends RELigneDeblocage {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdressePaiement adressePaiement;
    private String refPaiement = null;
    private String designationTiers1 = null;
    private String designationTiers2 = null;

    public AdressePaiement getAdressePaiement() {
        return adressePaiement;
    }

    public void setAdressePaiement(AdressePaiement adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    @Override
    public String getRefPaiement() {
        return refPaiement;
    }

    @Override
    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

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

}
