/*
 * Créé le 17 janvier 2012
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
 * @author MBO
 */
public class RFDemandesJointSousTypeParNbCasManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_NB_CAS = "NOMBRE";

    // ~ Instance fields
    private String forDateDebutStat = null;
    private String forDateFinStat = null;
    private String forSousTypeDeSoin = null;
    private transient String fromClause = null;
    private String gestionnaire = null;
    private boolean isImportation = false;

    public RFDemandesJointSousTypeParNbCasManager() {
        super();
    }

    // ------------------------------------------------------------------------------------------------

    /**
     * Recupération du field
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append("COUNT(" + RFDemande.FIELDNAME_ID_DEMANDE + ") "
                + RFDemandesJointSousTypeParNbCasManager.ALIAS_NB_CAS);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(RFDemandeJointSousTypeParNbCas.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getGroupBy(BStatement statement) {
        StringBuffer sqlGroup = new StringBuffer();

        sqlGroup.append(" GROUP BY ").append(RFDemande.FIELDNAME_ID_DEMANDE);

        return sqlGroup.toString();
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if ((null != forSousTypeDeSoin) && (forSousTypeDeSoin.length() > 0)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            // Clause ajoutant le gestionnaire ci celui est renseigné
            if (!JadeStringUtil.isEmpty(gestionnaire)) {
                sqlWhere.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
                sqlWhere.append("=");
                sqlWhere.append("'");
                sqlWhere.append(gestionnaire);
                sqlWhere.append("'");

                sqlWhere.append(" AND ");
            }

            // Condition par état d'importation
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

            // Condition par sous type de soins
            sqlWhere.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append("=");
            sqlWhere.append(forSousTypeDeSoin);

            sqlWhere.append(" AND ");

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

            // Clause pour que la date de validation soit entre la date de début et la date de fin de la période
            // souhaitée
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" BETWEEN ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutStat));
            sqlWhere.append(" AND ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinStat));

            // sqlWhere.append(this._getGroupBy(statement));

        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeJointSousTypeParNbCas();
    }

    public String getForDateDebutStat() {
        return forDateDebutStat;
    }

    public String getForDateFinStat() {
        return forDateFinStat;
    }

    public String getForSousTypeDeSoin() {
        return forSousTypeDeSoin;
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

    public void setForDateDebutStat(String forDateDebutStat) {
        this.forDateDebutStat = forDateDebutStat;
    }

    public void setForDateFinStat(String forDateFinStat) {
        this.forDateFinStat = forDateFinStat;
    }

    public void setForSousTypeDeSoin(String forSousTypeDeSoin) {
        this.forSousTypeDeSoin = forSousTypeDeSoin;
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
