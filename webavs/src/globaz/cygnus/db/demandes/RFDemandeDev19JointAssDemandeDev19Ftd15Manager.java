package globaz.cygnus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Set;

/**
 * author jje
 */
public class RFDemandeDev19JointAssDemandeDev19Ftd15Manager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private String forIdDemandeFtd15 = "";
    private String forIdDevis = "";
    private String forIdDossier = "";
    private Set<String> forIdsDemande15ToIgnore = null;
    private transient String fromClause = null;
    private String fromDateDebutFacture = "";
    private String fromDateFinFacture = "";

    public RFDemandeDev19JointAssDemandeDev19Ftd15Manager() {
        super();
        wantCallMethodBeforeFind(false);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFDemandeDev19JointAssDemandeDev19Ftd15.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_DEV19;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        // String schema = _getCollection();

        if ((forIdsDemande15ToIgnore != null) && (forIdsDemande15ToIgnore.size() > 0)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_FTD15);
            sqlWhere.append(" NOT IN (");

            int inc = 0;
            for (String id : forIdsDemande15ToIgnore) {
                if (!JadeStringUtil.isEmpty(id)) {
                    inc++;
                    if (forIdsDemande15ToIgnore.size() != inc) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDossier)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDemandeFtd15)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_FTD15);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDemandeFtd15));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDevis)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_DEV19);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDevis));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_CS_ETAT);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtat));
        }

        if (!JadeStringUtil.isIntegerEmpty(fromDateDebutFacture)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebutFacture));
        }

        if (!JadeStringUtil.isIntegerEmpty(fromDateFinFacture)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), fromDateFinFacture));
        }

        /*
         * if (this.isMontantAssocieDevisNonNull) { if (sqlWhere.length() > 0) { sqlWhere.append(" AND ("); }
         * 
         * sqlWhere.append(RFAssDemandeDev19Ftd15.FIELDNAME_MONTANT_ASSOCIE_AU_DEVIS); sqlWhere.append(" IS NOT NULL ");
         * sqlWhere.append(" OR "); sqlWhere.append(RFAssDemandeDev19Ftd15.FIELDNAME_MONTANT_ASSOCIE_AU_DEVIS);
         * sqlWhere.append(" <> 0 )"); }
         */

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeDev19JointAssDemandeDev19Ftd15();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForIdDemandeFtd15() {
        return forIdDemandeFtd15;
    }

    public String getForIdDevis() {
        return forIdDevis;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public Set<String> getForIdsDemande15ToIgnore() {
        return forIdsDemande15ToIgnore;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getFromDateDebutFacture() {
        return fromDateDebutFacture;
    }

    public String getFromDateFinFacture() {
        return fromDateFinFacture;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForIdDemandeFtd15(String forIdDemandeFtd15) {
        this.forIdDemandeFtd15 = forIdDemandeFtd15;
    }

    public void setForIdDevis(String forIdDevis) {
        this.forIdDevis = forIdDevis;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdsDemande15ToIgnore(Set<String> forIdsDemande15ToIgnore) {
        this.forIdsDemande15ToIgnore = forIdsDemande15ToIgnore;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setFromDateDebutFacture(String fromDateDebutFacture) {
        this.fromDateDebutFacture = fromDateDebutFacture;
    }

    public void setFromDateFinFacture(String fromDateFinFacture) {
        this.fromDateFinFacture = fromDateFinFacture;
    }

}