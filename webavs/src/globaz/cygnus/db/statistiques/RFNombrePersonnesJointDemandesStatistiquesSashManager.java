/*
 * Créé le 26 janvier 2012
 */
package globaz.cygnus.db.statistiques;

import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.api.demandes.IRFDemande;
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
public class RFNombrePersonnesJointDemandesStatistiquesSashManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_NOMBRE_PERSONNE = "PERSONNE";

    private String forDateDebutPeriodeStat = null;
    private String forDateFinPeriodeStat = null;
    private transient String fromClause = null;
    private String gestionnaire = null;
    private boolean isImportation = false;

    public RFNombrePersonnesJointDemandesStatistiquesSashManager() {
        super();
    }

    /**
     * Recupération du field
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        // fields.append("COUNT(" + RFDemande.FIELDNAME_ID_DEMANDE + ") "
        // + RFNombrePersonnesJointDemandesStatistiquesSashManager.ALIAS_NOMBRE_PERSONNE);

        fields.append("DISTINCT " + RFDemande.FIELDNAME_ID_DOSSIER + " "
                + RFNombrePersonnesJointDemandesStatistiquesSashManager.ALIAS_NOMBRE_PERSONNE);

        return fields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(
                    RFNombrePersonnesJointDemandesStatistiquesSash.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    // @Override
    // protected String _getGroupBy(BStatement statement) {
    // StringBuffer sqlGroup = new StringBuffer();
    //
    // sqlGroup.append(" GROUP BY ").append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);
    //
    // return sqlGroup.toString();
    // }

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

            // Clause ajoutant la recherche par gestionnaire si celui est renseigné
            if (!JadeStringUtil.isEmpty(gestionnaire)) {
                sqlWhere.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
                sqlWhere.append("=");
                sqlWhere.append("'");
                sqlWhere.append(gestionnaire);
                sqlWhere.append("'");

                sqlWhere.append(" AND ");

            }

            // Clause ajoutant le type de source Gestionnaire ou Importé
            if (isImportation == true) {
                sqlWhere.append(RFDemande.FIELDNAME_CS_SOURCE);
                sqlWhere.append("=");
                sqlWhere.append(IRFDemande.SYSTEME);

                sqlWhere.append(" AND ");

            } else {
                sqlWhere.append(RFDemande.FIELDNAME_CS_SOURCE);
                sqlWhere.append("=");
                sqlWhere.append(IRFDemande.GESTIONNAIRE);

                sqlWhere.append(" AND ");
            }

            // Condition pour obtenir les demandes qui ne sont pas dans le statut : REFUSEES
            sqlWhere.append(RFDemande.FIELDNAME_CS_STATUT);
            sqlWhere.append(" <> ");
            sqlWhere.append(IRFDemande.REFUSE);

            sqlWhere.append(" AND ");

            // Clause ajoutant les décisions en état VALIDER
            sqlWhere.append(RFDecision.FIELDNAME_ETAT_DECISION);
            sqlWhere.append("=");
            sqlWhere.append(IRFDecisions.VALIDE);

            sqlWhere.append(" AND ");

            // Clause ajoutant la date de validation de la décision comprise dans la période donnée
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" BETWEEN ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutPeriodeStat));
            sqlWhere.append(" AND ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinPeriodeStat));

            // sqlWhere.append(this._getGroupBy(statement));

        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFNombrePersonnesJointDemandesStatistiquesSash();
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

    public boolean isImportation() {
        return isImportation;
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

    public void setImportation(boolean isImportation) {
        this.isImportation = isImportation;
    }
}
