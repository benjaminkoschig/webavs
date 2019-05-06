/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * @author jje
 */
public class RFDemandeManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forDateDebutBetweenDateDebutDem = "";

    private String forDateFinBetweenDateDebutDem = "";
    private String forDifferentsIdsDemandes = null;
    private String[] forEtatsDemande = null;
    private boolean forHasDemandeParent = Boolean.FALSE;
    private boolean forHasNotDemandeParent = Boolean.FALSE;

    private String forIdDecision = "";
    private String forIdProcess = "";

    private String forIdQdAssure = "";
    private String forIdQdPrincipale = "";
    private String[] forIdsDecision = null;
    private List<String> forIdsDemande = null;
    private transient String fromClause = null;
    private String forIdDossier = "";

    /**
     * Crée une nouvelle instance de la classe RFDemandeManager.
     */
    public RFDemandeManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer();

            from.append(_getCollection());
            from.append(RFDemande.TABLE_NAME);

            fromClause = from.toString();
        }

        return fromClause;
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        // String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forIdQdPrincipale)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_QD_PRINCIPALE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdQdPrincipale));
        }

        if (forDifferentsIdsDemandes != null) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" NOT IN ");
            sqlWhere.append("(");
            sqlWhere.append(forDifferentsIdsDemandes);
            sqlWhere.append(")");
            sqlWhere.append(")");
        }

        if (forHasNotDemandeParent) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE_PARENT);
            sqlWhere.append(" = ");
            sqlWhere.append("0");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE_PARENT);
            sqlWhere.append(" IS NULL");
            sqlWhere.append(")");
        }

        if (forHasDemandeParent) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE_PARENT);
            sqlWhere.append(" <> ");
            sqlWhere.append("0");
            sqlWhere.append(")");

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdQdAssure)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_QD_ASSURE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdQdAssure));
        }

        if ((null != forEtatsDemande) && (forEtatsDemande.length > 0)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_CS_ETAT);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String etat : forEtatsDemande) {
                inc++;
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), etat));
                if (inc < forEtatsDemande.length) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(") ");

        }

        if ((null != forIdsDecision) && (forIdsDecision.length > 0)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_DECISION);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String id : forIdsDecision) {
                inc++;
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), id));
                if (inc < forIdsDecision.length) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(") ");

        }

        if ((null != forIdsDemande) && (forIdsDemande.size() > 0)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String id : forIdsDemande) {
                inc++;
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), id));
                if (inc < forIdsDemande.size()) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(") ");

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebutBetweenDateDebutDem)
                && !JadeStringUtil.isIntegerEmpty(forDateFinBetweenDateDebutDem)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenDateDebutDem));
            sqlWhere.append(" OR ");
            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" <> 0 AND ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenDateDebutDem));
            sqlWhere.append(" )) ");

            sqlWhere.append(" AND ");

            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenDateDebutDem));
            sqlWhere.append(" OR ");
            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" <> 0 AND ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenDateDebutDem));
            sqlWhere.append(" )) ");

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdProcess)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdProcess));
        }

        if (!JadeStringUtil.isEmpty(forIdDossier)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_DOSSIER
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        return sqlWhere.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Définition de l'entité (RFDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemande();
    }

    public String getForDateDebutBetweenDateDebutDem() {
        return forDateDebutBetweenDateDebutDem;
    }

    public String getForDateFinBetweenDateDebutDem() {
        return forDateFinBetweenDateDebutDem;
    }

    public String getForDifferentsIdsDemandes() {
        return forDifferentsIdsDemandes;
    }

    public String[] getForEtatsDemande() {
        return forEtatsDemande;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdProcess() {
        return forIdProcess;
    }

    public String getForIdQdAssure() {
        return forIdQdAssure;
    }

    public String getForIdQdPrincipale() {
        return forIdQdPrincipale;
    }

    public String[] getForIdsDecision() {
        return forIdsDecision;
    }

    public List<String> getForIdsDemande() {
        return forIdsDemande;
    }

    public boolean isForHasDemandeParent() {
        return forHasDemandeParent;
    }

    public boolean isForHasNotDemandeParent() {
        return forHasNotDemandeParent;
    }

    public void setForDateDebutBetweenDateDebutDem(String forDateDebutBetweenDateDebutDem) {
        this.forDateDebutBetweenDateDebutDem = forDateDebutBetweenDateDebutDem;
    }

    public void setForDateFinBetweenDateDebutDem(String forDateFinBetweenDateDebutDem) {
        this.forDateFinBetweenDateDebutDem = forDateFinBetweenDateDebutDem;
    }

    public void setForDifferentsIdsDemandes(String forDifferentsIdsDemandes) {
        this.forDifferentsIdsDemandes = forDifferentsIdsDemandes;
    }

    public void setForEtatsDemande(String[] forEtatsDemande) {
        this.forEtatsDemande = forEtatsDemande;
    }

    public void setForHasDemandeParent(boolean forHasDemandeParent) {
        this.forHasDemandeParent = forHasDemandeParent;
    }

    public void setForHasNotDemandeParent(boolean forHasNotDemandeParent) {
        this.forHasNotDemandeParent = forHasNotDemandeParent;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdProcess(String forIdProcess) {
        this.forIdProcess = forIdProcess;
    }

    public void setForIdQdAssure(String forIdQdAssure) {
        this.forIdQdAssure = forIdQdAssure;
    }

    public void setForIdQdPrincipale(String forIdQdPrincipale) {
        this.forIdQdPrincipale = forIdQdPrincipale;
    }

    public void setForIdsDecision(String[] forIdsDecision) {
        this.forIdsDecision = forIdsDecision;
    }

    public void setForIdsDemande(List<String> forIdsDemande) {
        this.forIdsDemande = forIdsDemande;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

}
