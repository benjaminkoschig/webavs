package db;

import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationViewBean;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

public class LASuiviCaisseViewBean extends AFSuiviCaisseAffiliationViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String numCaisse = "";

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        numCaisse = statement.dbReadString("HBCADM");
    }

    public String getGenreCaisseLibelle() {
        return getSession().getCodeLibelle(getGenreCaisse());
    }

    public String getIdTiers() {
        try {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(getAffiliationId());
            aff.retrieve();
            if (aff.isNew()) {
                return "";
            } else {
                return aff.getIdTiers();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getNumCaisse() {
        return numCaisse;
    }

    public String getNumCaisseLibelle() {
        TIAdministrationViewBean tiersAdministration = new TIAdministrationViewBean();
        tiersAdministration.setSession(getSession());
        tiersAdministration.setIdTiersAdministration(getIdTiersCaisse());
        try {
            tiersAdministration.retrieve();
            return tiersAdministration.getCodeAdministration();
        } catch (Exception e) {
            return "";
        }
    }

    public void setNumCaisse(String numCaisse) {
        this.numCaisse = numCaisse;
    }
}
