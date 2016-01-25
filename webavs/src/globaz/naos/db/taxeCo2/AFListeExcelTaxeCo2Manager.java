package globaz.naos.db.taxeCo2;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;

public class AFListeExcelTaxeCo2Manager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnnee = new String();
    private Boolean isRadie = new Boolean(false);
    protected java.lang.String order = "AFAFFIP.MALNAF ASC";
    private java.lang.String tauxDefaut = new String();

    /** Permet de filtrer les plans selon les droits accordés à l'utilisateur */
    private Boolean wantFilterByPlanFacturation = new Boolean(false);

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return AFListeExcelTaxeCo2.TABLE_FIELDS;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "AFTACOP AS AFTACOP" + " LEFT JOIN " + _getCollection()
                + "AFAFFIP AS AFAFFIP ON (AFTACOP.MAIAFF=AFAFFIP.MAIAFF)" + " LEFT JOIN " + _getCollection()
                + "AFSUAFP AS AFSUAFP ON (AFAFFIP.MAIAFF=AFSUAFP.MAIAFF and MYTGEN=830001)" + " LEFT JOIN "
                + _getCollection() + "TIADMIP AS TIADMIP ON AFSUAFP.HTITIE=TIADMIP.HTITIE";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MWDANN=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }

        if (getIsRadie().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADFIN>0";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new AFListeExcelTaxeCo2();
    }

    public java.lang.String getForAnnee() {
        return forAnnee;
    }

    public Boolean getIsRadie() {
        return isRadie;
    }

    public java.lang.String getTauxDefaut(String forAnnee) throws Exception {
        AFAssuranceManager manaAssurance = new AFAssuranceManager();
        manaAssurance.setSession(getSession());
        manaAssurance.setForTypeAssurance(CodeSystem.TYPE_ASS_TAXE_CO2);
        manaAssurance.find();
        if (manaAssurance.size() >= 1) {
            AFAssurance assurance = (AFAssurance) manaAssurance.getFirstEntity();
            AFTauxAssurance taux = assurance.getTaux("01.01." + forAnnee);
            tauxDefaut = taux.getTauxSansFraction();
        }
        return tauxDefaut;
    }

    public void setForAnnee(java.lang.String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setIsRadie(Boolean isRadie) {
        this.isRadie = isRadie;
    }

    public void setTauxDefaut(java.lang.String tauxDefaut) {
        this.tauxDefaut = tauxDefaut;
    }
}
