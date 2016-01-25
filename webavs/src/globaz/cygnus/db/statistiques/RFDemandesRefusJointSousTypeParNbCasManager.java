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
public class RFDemandesRefusJointSousTypeParNbCasManager extends BManager {

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

    public RFDemandesRefusJointSousTypeParNbCasManager() {
        super();
    }

    /**
     * Recupération du field
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append("COUNT(" + RFDemande.FIELDNAME_ID_DEMANDE + ") "
                + RFDemandesRefusJointSousTypeParNbCasManager.ALIAS_NB_CAS);

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

            // Clause ajoutant le gestionnaire si celui-ci est renseigné dans l'écran
            if (!JadeStringUtil.isEmpty(gestionnaire)) {
                sqlWhere.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
                sqlWhere.append("=");
                sqlWhere.append("'");
                sqlWhere.append(gestionnaire);
                sqlWhere.append("'");

                sqlWhere.append(" AND ");
            }

            // Clause par sous type de soins
            sqlWhere.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append("=");
            sqlWhere.append(forSousTypeDeSoin);

            sqlWhere.append(" AND ");

            // Clause par dates de validation entre la date de début et la date de fin de la période donnée
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" BETWEEN ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutStat));
            sqlWhere.append(" AND ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinStat));

            sqlWhere.append(" AND ");

            // Condition pour obtenir les demandes qui ont le statut : REFUSEES
            sqlWhere.append(RFDemande.FIELDNAME_CS_STATUT);
            sqlWhere.append("=");
            sqlWhere.append(IRFDemande.REFUSE);

            sqlWhere.append(" AND ");

            // Condition pour obtenir les decisions qui sont dans l'état : VALIDEES
            sqlWhere.append(RFDecision.FIELDNAME_ETAT_DECISION);
            sqlWhere.append("=");
            sqlWhere.append(IRFDecisions.VALIDE);

            sqlWhere.append(_getGroupBy(statement));

        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeRefusJointSousTypeParNbCas();
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

}
