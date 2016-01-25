package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class SFMembreFamilleJointPeriodeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsDomaineApplication = "";
    private String forIdMembreFamille = "";
    private String forIdTiers = "";
    private String forTypePeriode = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer from = new StringBuffer(SFMembreFamilleJointPeriode.createFromClause(_getCollection()));

        return from.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.FIELD_IDTIERS + " = " + forIdTiers + ")";

        }

        if (!JadeStringUtil.isEmpty(getForTypePeriode())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFPeriode.FIELD_TYPE + " = " + forTypePeriode + ")";

        }

        if (!JadeStringUtil.isEmpty(getForCsDomaineApplication())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.FIELD_DOMAINE_APPLICATION + " = " + forCsDomaineApplication + ")";

        }

        if (!JadeStringUtil.isEmpty(getForIdMembreFamille())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.FIELD_IDMEMBREFAMILLE + " = " + forIdMembreFamille + ")";

        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFMembreFamilleJointPeriode();
    }

    public String getForCsDomaineApplication() {
        return forCsDomaineApplication;
    }

    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForTypePeriode() {
        return forTypePeriode;
    }

    public void setForCsDomaineApplication(String forCsDomaineApplication) {
        this.forCsDomaineApplication = forCsDomaineApplication;
    }

    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForTypePeriode(String forTypePeriode) {
        this.forTypePeriode = forTypePeriode;
    }

}
