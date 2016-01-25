/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.process;

import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFPreparerDecisionDemandeLinkedQdPrincipaleManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient String fromClause = null;
    private transient String idGestionnaire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFPreparerDecisionDemandeLinkedQdPrincipaleManager.
     */
    public RFPreparerDecisionDemandeLinkedQdPrincipaleManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFPreparerDecisionDemandeLinkedQdPrincipale.createFromClause(_getCollection()));

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
        String schema = _getCollection();
        String point = ".";
        String equal = " = ";
        String notEqual = " <> ";
        String from = " FROM ";
        String where = " WHERE ";
        String and = " AND ";
        String or = " OR ";
        String in = " IN ";
        // String notIn = " NOT IN ";
        String aliasName = "b";

        sqlWhere.append(RFDemande.FIELDNAME_CS_ETAT);
        sqlWhere.append(" = ");
        sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFDemande.ENREGISTRE));

        if (!JadeStringUtil.isEmpty(idGestionnaire)) {
            sqlWhere.append(and);
            sqlWhere.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), idGestionnaire));
        }

        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_ID_DOSSIER);
        sqlWhere.append(in + " ( ");

        // Génération de la sous-requête retournant tous les ids dossier étant
        // lié à un dossier
        sqlWhere.append(" SELECT ");
        sqlWhere.append(RFDossier.FIELDNAME_ID_DOSSIER);
        sqlWhere.append(from);

        // Génération de la clause from de la sous requête
        sqlWhere.append(getFromClauseQdJointPeriodeValJointDossier(schema));
        sqlWhere.append(where);
        sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
        sqlWhere.append(notEqual);
        sqlWhere.append(IRFQd.CS_ETAT_QD_CLOTURE);

        // condition sur l'année de la Qd
        sqlWhere.append(and);
        sqlWhere.append(" (( ");
        sqlWhere.append(RFQd.FIELDNAME_ANNEE_QD);
        sqlWhere.append(equal);
        sqlWhere.append("integer(substr(char(" + RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT + "),1,4))");
        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(notEqual);
        sqlWhere.append(" 0 ");
        sqlWhere.append(" ) ");

        sqlWhere.append(or);
        sqlWhere.append(" ( ");

        sqlWhere.append(RFQd.FIELDNAME_ANNEE_QD);
        sqlWhere.append(equal);
        sqlWhere.append("integer(substr(char(" + RFDemande.FIELDNAME_DATE_FACTURE + "),1,4))");
        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(equal);
        sqlWhere.append(" 0 ");
        sqlWhere.append(" )) ");

        // Condition
        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_ID_DOSSIER);
        sqlWhere.append(equal);
        sqlWhere.append(RFDossier.FIELDNAME_ID_DOSSIER);

        sqlWhere.append(and);
        sqlWhere.append(aliasName);
        sqlWhere.append(point);
        sqlWhere.append(RFAssQdDossier.FIELDNAME_ID_QD);
        sqlWhere.append(equal);
        sqlWhere.append(RFQd.FIELDNAME_ID_QD);

        // Gestion des périodes de validités
        sqlWhere.append(and);
        sqlWhere.append(" (( " + RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
        sqlWhere.append(" <= ");
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(notEqual);
        sqlWhere.append(" 0 )");

        sqlWhere.append(or);

        sqlWhere.append(" ( " + RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
        sqlWhere.append(" <= ");
        sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(equal);
        sqlWhere.append(" 0 ))");

        sqlWhere.append(and);

        // ( (( FCDFIN >= EDDDTR AND EDDDTR <> 0 ) OR ( FCDFIN >= EDDFAC AND
        // EDDDTR = 0 )) OR FCDFIN = 0 )
        sqlWhere.append(" ( (( " + RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
        sqlWhere.append(" >= ");
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(notEqual);
        sqlWhere.append(" 0 )");

        sqlWhere.append(or);

        sqlWhere.append(" ( " + RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
        sqlWhere.append(" >= ");
        sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
        sqlWhere.append(and);
        sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        sqlWhere.append(equal);
        sqlWhere.append(" 0 ))");

        sqlWhere.append(or);

        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
        sqlWhere.append(equal);
        sqlWhere.append(" 0 )");

        // Selection de la dernière période de validité
        sqlWhere.append(and);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(in);
        sqlWhere.append(" ( SELECT ");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(from);
        sqlWhere.append(schema);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        sqlWhere.append(" as a");
        sqlWhere.append(" where ");
        sqlWhere.append("a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append(equal);
        sqlWhere.append(schema);
        sqlWhere.append(RFQdPrincipale.TABLE_NAME);
        sqlWhere.append(point);
        sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append(and);
        sqlWhere.append("a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION);
        sqlWhere.append(notEqual);
        sqlWhere.append(IRFQd.CS_SUPPRESSION);
        sqlWhere.append(" AND NOT EXISTS ( SELECT * FROM ");
        sqlWhere.append(schema);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        sqlWhere.append(where);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION);
        sqlWhere.append(equal);
        sqlWhere.append("a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION);
        sqlWhere.append(and);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(" > a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(and);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append(equal);
        sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append("))");

        sqlWhere.append(" ) ");

        return sqlWhere.toString();

    }

    /**
     * Définition de l'entité (RFQdJointDossierJointTiersJointDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPreparerDecisionDemandeLinkedQdPrincipale();
    }

    private String getFromClauseQdJointPeriodeValJointDossier(String schema) {

        StringBuffer sqlFromClause = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        sqlFromClause.append(schema);
        sqlFromClause.append(RFQd.TABLE_NAME);

        // jointure entre la table des Qds et la table des QdPrinicpales
        sqlFromClause.append(innerJoin);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFQdPrincipale.TABLE_NAME);
        sqlFromClause.append(on);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFQd.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFQd.FIELDNAME_ID_QD);
        sqlFromClause.append(egal);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFQdPrincipale.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);

        // jointure entre la table des QdPrincipales et la table des périodes de
        // validités
        sqlFromClause.append(innerJoin);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        sqlFromClause.append(on);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFQdPrincipale.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlFromClause.append(egal);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);

        // jointure entre la table des QdBase et la table Association Qd,dossier
        sqlFromClause.append(innerJoin);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFAssQdDossier.TABLE_NAME);
        sqlFromClause.append(on);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFQd.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFQd.FIELDNAME_ID_QD);
        sqlFromClause.append(egal);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFAssQdDossier.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFAssQdDossier.FIELDNAME_ID_QD);

        // jointure entre la table Association Qd, dossier et la table des
        // dossier
        sqlFromClause.append(innerJoin);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFDossier.TABLE_NAME);
        sqlFromClause.append(on);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFAssQdDossier.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);
        sqlFromClause.append(egal);
        sqlFromClause.append(schema);
        sqlFromClause.append(RFDossier.TABLE_NAME);
        sqlFromClause.append(point);
        sqlFromClause.append(RFDossier.FIELDNAME_ID_DOSSIER);

        return sqlFromClause.toString();
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

}