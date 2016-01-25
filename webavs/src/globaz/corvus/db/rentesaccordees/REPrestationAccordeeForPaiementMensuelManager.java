package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.adressepaiement.ITIAvoirPaiementDefTable;

/**
 * @author BSC
 */
public class REPrestationAccordeeForPaiementMensuelManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean exclureMontantsNuls = false;
    private String forCsEtatIn = "";
    private String forEnCoursAtMois = "";
    private String forIdDomaineApplication = "";
    private String forIdTiersAdressePmt = "";
    private boolean forTestAdressePmtOrDomaine = false;
    private boolean forTestCompteAnnexe = false;
    private boolean limitReturnedFields = false;

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableInfoCompta = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        if (!JadeStringUtil.isIntegerEmpty(forEnCoursAtMois)) {

            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            // si pas de date de fin ou si date de fin > date donnée
            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sql.append("<=");
            sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois));

            sql.append(" AND ( ");
            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append("=0");
            sql.append(" OR ");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append(" IS NULL ");

            sql.append(" OR ");
            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append(" >= ");
            sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois));

            sql.append(")");
        }

        if (!JadeStringUtil.isEmpty(forCsEtatIn)) {

            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append(" IN (");
            sql.append(forCsEtatIn);
            sql.append(")");
        }

        if (!JadeStringUtil.isEmpty(getForIdDomaineApplication())) {

            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdDomaineApplication()));
        }

        if (!JadeStringUtil.isEmpty(getForIdTiersAdressePmt())) {

            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiersAdressePmt()));
        }

        // L'un ET l'autre
        if (isForTestAdressePmtOrDomaine() && isForTestCompteAnnexe()) {

            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append("((");

            sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
            sql.append("=0");

            sql.append(" OR ((");
            sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                    .append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
            sql.append(" IS NULL");
            sql.append(" OR ");
            sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                    .append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
            sql.append("=0");
            sql.append(") AND (");
            sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                    .append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
            sql.append(" IS NULL");
            sql.append(" OR ");
            sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                    .append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
            sql.append("=0");
            sql.append("))");

            sql.append(") OR (");

            sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
            sql.append("=0");
            sql.append(" OR ");
            sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
            sql.append(" IS NULL");

            sql.append("))");
        } else {
            // L'un OU l'autre
            if (isForTestAdressePmtOrDomaine()) {

                if (sql.length() != 0) {
                    sql.append(" AND ");
                }

                sql.append(tableInfoCompta).append(".")
                        .append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
                sql.append("=0");

                sql.append(" OR ((");
                sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES)
                        .append(".").append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
                sql.append(" IS NULL");
                sql.append(" OR ");
                sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES)
                        .append(".").append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
                sql.append("=0");
                sql.append(") AND (");
                sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD)
                        .append(".").append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
                sql.append(" IS NULL");
                sql.append(" OR ");
                sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD)
                        .append(".").append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT);
                sql.append("=0");

                sql.append("))");
            }

            if (isForTestCompteAnnexe()) {

                if (sql.length() != 0) {
                    sql.append(" AND ");
                }

                sql.append("(");

                sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
                sql.append("=0");
                sql.append(" OR ");
                sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
                sql.append(" IS NULL");

                sql.append(")");
            }
        }

        if (isExclureMontantsNuls()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            sql.append("<>0.0");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationAccordeeForPaiementMensuel();
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForEnCoursAtMois() {
        return forEnCoursAtMois;
    }

    public String getForIdDomaineApplication() {
        return forIdDomaineApplication;
    }

    public String getForIdTiersAdressePmt() {
        return forIdTiersAdressePmt;
    }

    @Override
    public String getOrderByDefaut() {
        return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
    }

    public boolean isExclureMontantsNuls() {
        return exclureMontantsNuls;
    }

    public boolean isForTestAdressePmtOrDomaine() {
        return forTestAdressePmtOrDomaine;
    }

    public boolean isForTestCompteAnnexe() {
        return forTestCompteAnnexe;
    }

    public boolean isLimitReturnedFields() {
        return limitReturnedFields;
    }

    public void setExclureMontantsNuls(boolean exclureMontantsNuls) {
        this.exclureMontantsNuls = exclureMontantsNuls;
    }

    public void setForCsEtatIn(String string) {
        forCsEtatIn = string;
    }

    public void setForEnCoursAtMois(String string) {
        forEnCoursAtMois = string;
    }

    public void setForIdDomaineApplication(String newForIdDomaineApplication) {
        forIdDomaineApplication = newForIdDomaineApplication;
    }

    public void setForIdTiersAdressePmt(String newForIdTiersAdressePmt) {
        forIdTiersAdressePmt = newForIdTiersAdressePmt;
    }

    public void setForTestAdressePmtOrDomaine(boolean newTestAdressePmtOrDomaine) {
        forTestAdressePmtOrDomaine = newTestAdressePmtOrDomaine;
    }

    public void setForTestCompteAnnexe(boolean forTestCompteAnnexe) {
        this.forTestCompteAnnexe = forTestCompteAnnexe;
    }

    public void setLimitReturnedFields(boolean limitReturnedFields) {
        this.limitReturnedFields = limitReturnedFields;
    }
}
