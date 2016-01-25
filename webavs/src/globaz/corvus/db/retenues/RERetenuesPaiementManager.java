package globaz.corvus.db.retenues;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

public class RERetenuesPaiementManager extends PRAbstractManager implements BIGenericManager<RERetenuesPaiement> {

    private static final long serialVersionUID = 1L;

    private String forDate = "";
    private boolean seulementEnCours = false;
    private String forIdRenteAccordee = "";
    private String forIdTiersBeneficiaire = "";
    private String montantRenteAccordee = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdRenteAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RERetenuesPaiement.FIELDNAME_ID_RENTE_ACCORDEE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdRenteAccordee);
        }

        if (isSeulementEnCours()) {

            // AAAAMM actuel
            String dateActuelle = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel
                    .getDateProchainPmt(getSession()));

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            /*
             * SELECT * FROM CICIWEB.RERETEN WHERE YQIRAC=21933 AND ( (YQDDER <= 201009 AND (YQDDFR = 0 OR YQDDFR IS
             * NULL OR YQDDFR >= 201009) AND YQMMDR<YQMTAR)
             * 
             * 
             * OR
             * 
             * 
             * (YQDDER <= 201009 AND (YQDDFR = 0 OR YQDDFR IS NULL OR YQDDFR >= 201009) AND YQTTYP = 52830002) )
             * ORDER BY YQIRET
             */

            sqlWhere += "( " +

            "(" + RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE + " <= " + dateActuelle + " AND " + "("
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE + " = 0" + " OR "
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE + " IS NULL " + " OR "
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE + " >= " + dateActuelle + ") " +

                    " AND " +

                    RERetenuesPaiement.FIELDNAME_MONTANT_DEJA_RETENU + " < "
                    + RERetenuesPaiement.FIELDNAME_MONTANT_TOTAL_A_RETENIR + ") " +

                    " OR " +

                    "(" + RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE + " <= " + dateActuelle + " AND " + "("
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE + " = 0" + " OR "
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE + " IS NULL " + " OR "
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE + " >= " + dateActuelle + ") " +

                    " AND " +

                    RERetenuesPaiement.FIELDNAME_TYPE_RETENU + " = " + IRERetenues.CS_TYPE_IMPOT_SOURCE + ") " + ")";
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDate())) {
            if (sqlWhere.length() > 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE;
            sqlWhere += "<=";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), getForDate());
            sqlWhere += " AND ";

            sqlWhere += " ( ";
            sqlWhere += RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE;
            sqlWhere += ">=";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), getForDate());
            sqlWhere += " OR ";
            sqlWhere += RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE;
            sqlWhere += "=";
            sqlWhere += _dbWriteNumeric(statement.getTransaction(), "0");
            sqlWhere += " )";
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERetenuesPaiement();
    }

    @Override
    public String getOrderByDefaut() {
        return RERetenuesPaiement.FIELDNAME_ID_RETENUE;
    }

    public final String getForDate() {
        return forDate;
    }

    public final void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public boolean isSeulementEnCours() {
        return seulementEnCours;
    }

    public void setSeulementEnCours(boolean seulementEnCours) {
        this.seulementEnCours = seulementEnCours;
    }

    public final String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public final void setForIdRenteAccordee(String forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }

    public final String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public final void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public final String getMontantRenteAccordee() {
        return montantRenteAccordee;
    }

    public final void setMontantRenteAccordee(String montantRenteAccordee) {
        this.montantRenteAccordee = montantRenteAccordee;
    }

    @Override
    public List<RERetenuesPaiement> getContainerAsList() {
        List<RERetenuesPaiement> list = new ArrayList<RERetenuesPaiement>();

        for (int i = 0; i < size(); i++) {
            list.add((RERetenuesPaiement) get(i));
        }

        return list;
    }
}
