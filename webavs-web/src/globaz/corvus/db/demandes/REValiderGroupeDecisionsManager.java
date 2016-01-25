package globaz.corvus.db.demandes;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.db.tiers.ITIHistoriqueAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * <p>
 * Manager pour l'écran de validation de décisions.
 * </p>
 * <p>
 * Recherche les décisions d'une demande. Seul les demandes dont les décisions sont toutes pré-validées sont affichées
 * (à une exception : si une demande comporte une décision validée de type courant, ceci afin de ne pas filtrer les
 * éventuelles décisions rétro pré-validées de cette demande).
 * </p>
 * 
 * @author HPE
 * @author PBA
 */
public class REValiderGroupeDecisionsManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String WHERE_NSS_DEBUT = "(TIHAVSP.HVNAVS like ";
    public static final String WHERE_NSS_FIN = ")";

    private String forCsSexe = "";
    private String forDateNaissance = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";
    private String likePreparerPar = "";

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(PRDemande.FIELDNAME_IDTIERS).append(", ");
        sql.append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE).append(", ");
        sql.append(REDemandeRente.FIELDNAME_DATE_RECEPTION).append(", ");
        sql.append(REDemandeRente.FIELDNAME_DATE_DEBUT).append(", ");
        sql.append(REDemandeRente.FIELDNAME_DATE_FIN).append(", ");
        sql.append(REDemandeRente.FIELDNAME_DATE_DEPOT).append(", ");
        sql.append(REDemandeRente.FIELDNAME_DATE_TRAITEMENT).append(", ");
        sql.append(REDemandeRente.FIELDNAME_CS_ETAT).append(", ");
        sql.append(REDemandeRente.FIELDNAME_ID_GESTIONNAIRE).append(", ");
        sql.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append(", ");
        sql.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE).append(", ");
        sql.append(REDemandeRente.FIELDNAME_CS_TYPE_CALCUL).append(", ");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_NUM_AVS).append(", ");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_NOM).append(", ");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_PRENOM).append(", ");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE).append(", ");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_DATEDECES).append(", ");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_SEXE).append(", ");
        sql.append(REDemandeRenteJointDemande.FIELDNAME_NATIONALITE).append(", ");

        sql.append(" CASE WHEN ( ");
        sql.append(REDemandeRente.FIELDNAME_DATE_FIN);
        sql.append(" = 0 OR ");
        sql.append(REDemandeRente.FIELDNAME_DATE_FIN);
        sql.append(" IS NULL) THEN 99999999 ELSE ");
        sql.append(REDemandeRente.FIELDNAME_DATE_FIN);
        sql.append(" END AS DATE_FIN ");

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder from = new StringBuilder();

        from.append(REDemandeRenteJointDemande.createFromClause(_getCollection()));

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            String tableHistoriqueAvs = _getCollection() + ITIHistoriqueAvsDefTable.TABLE_NAME;
            String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

            from.append(" LEFT JOIN ");
            from.append(tableHistoriqueAvs);
            from.append(" ON (");
            from.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);
            from.append("=");
            from.append(tableHistoriqueAvs).append(".").append(ITIHistoriqueAvsDefTable.ID_TIERS);
            from.append(")");
        }

        return from.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();
        // FIXME 2014.11 MMO, LGA : en l'état le schéma ne sera pas ajouté par la méthode PRNSSUtil.getWhereNSS. Méthode
        // pas terrible
        if ((!JadeStringUtil.isBlank(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            sql.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS(), true));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1_MAJ);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2_MAJ);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE);
            sql.append("=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (sql.length() != 0) {
            sql.append(" AND ");
        }

        // on ne prend que les demandes comportant une ou des décisions pré-validés
        sql.append(" EXISTS (");
        sql.append(genererSqlInclusionDecisionsPrevalidees(statement));
        sql.append(")");

        sql.append(" AND ");

        // on exclue les demandes ayant des décisions dans un état différent que pré-validé (à l'exception d'une
        // décision courant validée pour ne pas filtrer les décisions rétro pré-validée de cette demande)
        sql.append(" NOT EXISTS (");
        sql.append(genererSqlExlusionDecisionsValideesEtEnAttentes());
        sql.append(")");

        return sql.toString();
    }

    @Override
    protected REDemandeRenteJointDemande _newEntity() throws Exception {
        return new REDemandeRenteJointDemande();
    }

    private String genererSqlExlusionDecisionsValideesEtEnAttentes() {

        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;
        String tableDemande = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM ");
        sql.append(tableDecision);
        sql.append(" WHERE ");
        // liaison avec la requête principale
        sql.append(tableDemande).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        sql.append("=");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);
        // exclusion des décisions dans un état non voulu
        sql.append(" AND (");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ETAT).append("=")
                .append(IREDecision.CS_ETAT_ATTENTE);
        sql.append(" OR (");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ETAT).append("=")
                .append(IREDecision.CS_ETAT_VALIDE);
        sql.append(" AND ");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_TYPE_DECISION).append("=")
                .append(IREDecision.CS_TYPE_DECISION_RETRO);
        sql.append("))");

        return sql.toString();
    }

    private String genererSqlInclusionDecisionsPrevalidees(BStatement statement) {

        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;
        String tableDemande = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM ");
        sql.append(tableDecision);
        sql.append(" WHERE ");
        // liaison avec la requête principale
        sql.append(tableDemande).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        sql.append("=");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);
        // on ne prend que les décisions pré-validée
        sql.append(" AND ");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ETAT);
        sql.append("=");
        sql.append(this._dbWriteNumeric(statement.getTransaction(), IREDecision.CS_ETAT_PREVALIDE));

        return sql.toString();
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public String getLikePreparerPar() {
        return likePreparerPar;
    }

    @Override
    public String getOrderByDefaut() {
        return REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH + ","
                + REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false ou vide)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getLikeNumeroAVS())) {
            return "";
        }

        if (getLikeNumeroAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public void setForCsSexe(String string) {
        forCsSexe = string;
    }

    public void setForDateNaissance(String string) {
        forDateNaissance = string;
    }

    public void setLikeNom(String string) {
        likeNom = string;
    }

    public void setLikeNumeroAVS(String string) {
        likeNumeroAVS = string;
    }

    public void setLikeNumeroAVSNNSS(String string) {
        likeNumeroAVSNNSS = string;
    }

    public void setLikePrenom(String string) {
        likePrenom = string;
    }

    public void setLikePreparerPar(String likePreparerPar) {
        this.likePreparerPar = likePreparerPar;
    }
}
