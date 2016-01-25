/*
 * Créé le 26 janvier 2012
 */
package globaz.cygnus.db.statistiques;

import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author mbo
 * 
 */
public class RFNombrePersonnesTotalJointDemandesStatistiquesNbCasManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_NOMBRE_PERSONNE = "PERSONNE";

    private String forDateDebutPeriodeStat = null;
    private String forDateFinPeriodeStat = null;
    private transient String fromClause = null;
    private String gestionnaire = null;

    public RFNombrePersonnesTotalJointDemandesStatistiquesNbCasManager() {
        super();
    }

    /**
     * Recupération du field
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append("DISTINCT " + RFDemande.FIELDNAME_ID_DOSSIER + " "
                + RFNombrePersonnesTotalJointDemandesStatistiquesNbCasManager.ALIAS_NOMBRE_PERSONNE);

        return fields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(
                    RFNombrePersonnesTotalJointDemandesStatistiquesNbCas.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forDateDebutPeriodeStat)
                && !JadeStringUtil.isIntegerEmpty(forDateFinPeriodeStat)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            // Clause par gestionnaire si sélectionné
            if (!JadeStringUtil.isEmpty(gestionnaire)) {
                sqlWhere.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
                sqlWhere.append("=");
                sqlWhere.append("'");
                sqlWhere.append(gestionnaire);
                sqlWhere.append("'");

                sqlWhere.append(" AND ");
            }

            // Condition de la date de validation de la décision entre la date de début et la date de fin de la période
            // donnée
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" BETWEEN ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutPeriodeStat));
            sqlWhere.append(" AND ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinPeriodeStat));

            sqlWhere.append(" AND ");

            // Condition d'absence d'idQd dans dans la demande
            sqlWhere.append("(");
            sqlWhere.append(RFDemande.FIELDNAME_ID_QD_PRINCIPALE);
            sqlWhere.append("=");
            sqlWhere.append("0");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFDemande.FIELDNAME_ID_QD_PRINCIPALE);
            sqlWhere.append(" IS NULL");
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            // Condition de la décision en état VALIDEE
            sqlWhere.append(RFDecision.FIELDNAME_ETAT_DECISION);
            sqlWhere.append("=");
            sqlWhere.append(IRFDecisions.VALIDE);

        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFNombrePersonnesTotalJointDemandesStatistiquesNbCas();
    }

    public String getForDateDebutPeriodeStat() {
        return forDateDebutPeriodeStat;
    }

    public String getForDateFinPeriodeStat() {
        return forDateFinPeriodeStat;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    public void setForDateDebutPeriodeStat(String forDateDebutPeriodeStat) {
        this.forDateDebutPeriodeStat = forDateDebutPeriodeStat;
    }

    public void setForDateFinPeriodeStat(String forDateFinPeriodeStat) {
        this.forDateFinPeriodeStat = forDateFinPeriodeStat;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

}
