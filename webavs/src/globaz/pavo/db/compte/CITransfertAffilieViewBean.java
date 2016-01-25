package globaz.pavo.db.compte;

import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CITransfertAffilieViewBean extends CIAbstractPersistentViewBean {
    public String dateFusion = "";
    public String employeurDst = "";
    public String employeurSrc = "";
    public boolean imprimerAttestations = false;
    public String infoAffilieDst = "";
    public String infoAffilieSrc = "";

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateFusion() {
        return dateFusion;
    }

    public String getEmployeurDst() {
        return employeurDst;
    }

    public String getEmployeurSrc() {
        return employeurSrc;
    }

    public String getInfoAffilieDst() {
        return infoAffilieDst;
    }

    public boolean isImprimerAttestations() {
        return imprimerAttestations;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setDateFusion(String dateFusion) {
        this.dateFusion = dateFusion;
    }

    public void setEmployeurDst(String employeurDst) {
        this.employeurDst = employeurDst;
    }

    public void setEmployeurSrc(String employeurSrc) {
        this.employeurSrc = employeurSrc;
    }

    public void setImprimerAttestations(boolean imprimerAttestations) {
        this.imprimerAttestations = imprimerAttestations;
    }

    public void setInfoAffilieDst(String infoAffilieDst) {
        this.infoAffilieDst = infoAffilieDst;
    }

    /**
     * Récupérer les informations de l'affilié source dans le widget
     * 
     * @return informations de l'affilié source
     */
    public String getInfoAffilieSrc() {
        return infoAffilieSrc;
    }

    /**
     * Setter les finroatmions de l'affilié source
     * 
     * @param infoAffilieSrc
     */
    public void setInfoAffilieSrc(String infoAffilieSrc) {
        this.infoAffilieSrc = infoAffilieSrc;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
