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
public class RFDemandeJointSousTypeParMontantSashManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Instance fields
    private String forDateDebutStat = null;
    private String forDateFinStat = null;
    private String forSousTypeDeSoin = null;
    private transient String fromClause = null;
    private String gestionnaire = null;
    private boolean isImportation = false;

    /**
     * Crée une nouvelle instance de la classe RFDossierManager.
     */
    public RFDemandeJointSousTypeParMontantSashManager() {
        super();
    }

    /**
     * Recupération du field
     */
    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append(RFDemande.FIELDNAME_MONTANT_A_PAYER);
        fields.append(",");
        fields.append(RFDemande.FIELDNAME_IS_FORCER_PAIEMENT);
        fields.append(",");
        fields.append(RFDemande.FIELDNAME_ID_DEMANDE);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(
                    RFDemandeJointSousTypeParMontantSash.createFromClause(_getCollection()));
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

        if ((null != forSousTypeDeSoin) && (forSousTypeDeSoin.length() > 0)) {
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

            // Clause ajoutant les décisions en état VALIDER
            sqlWhere.append(RFDecision.FIELDNAME_ETAT_DECISION);
            sqlWhere.append("=");
            sqlWhere.append(IRFDecisions.VALIDE);

            sqlWhere.append(" AND ");

            // Clause par sous type de soins
            sqlWhere.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append("=");
            sqlWhere.append(forSousTypeDeSoin);

            sqlWhere.append(" AND ");

            // Clause ajoutant la date de validation de la décision comprise dans la période donnée
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" BETWEEN ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutStat));
            sqlWhere.append(" AND ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinStat));

            sqlWhere.append(" AND ");

            // Clause ajoutant la date de validation de la décision comprise dans la période donnée
            sqlWhere.append(RFDemande.FIELDNAME_CS_ETAT);
            sqlWhere.append(" <> ");
            sqlWhere.append(IRFDemande.ANNULE);

        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeJointSousTypeParMontantSash();
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

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
