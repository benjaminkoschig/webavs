package globaz.libra.db.journalisations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.IJOCommonComplementJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonGroupeJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.libra.db.dossiers.LIDossiers;

public class LIEcheancesJointDossiersManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String documents = new String();
    private String forCsType = new String();
    private String forDateDebut = new String();
    private String forDateFin = new String();
    private String forIdDomaine = new String();
    private String forIdDossier = new String();
    private String forIdExtern = null;
    private String forIdGroupe = new String();
    private String forIdUtilisateur = new String();
    private String orderBy = new String();

    /**
     * Crée une nouvelle instance de la classe LIEcheancesJointDossiersManager.
     */
    public LIEcheancesJointDossiersManager() {
        super();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
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

        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }

        sqlWhere.append(IJOCommonJournalisationDefTable.CSTYPEJOURNAL);
        sqlWhere.append(" = ");
        sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), JOConstantes.CS_JO_RAPPEL));

        if (!JadeStringUtil.isIntegerEmpty(forIdDossier)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdExtern)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_EXTERNE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdExtern));
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebut)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonGroupeJournalDefTable.DATE_RAPPEL);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateFin)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonGroupeJournalDefTable.DATE_RAPPEL);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdUtilisateur)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            if (forIdUtilisateur.equals("idFX")) {
                sqlWhere.append(IJOCommonJournalisationDefTable.IDUTILISATEUR);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteString(statement.getTransaction(), getSession().getUserId()));
            } else {
                sqlWhere.append(IJOCommonJournalisationDefTable.IDUTILISATEUR);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdUtilisateur));
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdGroupe)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_GROUPE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdGroupe));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDomaine)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(LIDossiers.FIELDNAME_ID_DOMAINE);

            if (forIdDomaine.startsWith("IN")) {
                sqlWhere.append(" " + forIdDomaine);
            } else {
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDomaine));
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(forCsType)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsType));
        } else {
            if (!JadeStringUtil.isBlankOrZero(documents) && documents.equals("on")) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME);
                sqlWhere.append(" IN (");
                sqlWhere.append(" " + JOConstantes.CS_JO_AVS_FMT_ENVOI_MULTIPLE + ", "
                        + JOConstantes.CS_JO_AVS_FMT_ENVOI_SIMPLE);
                sqlWhere.append(") ");
            }
        }

        return sqlWhere.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Définition de l'entité (LIJournalisationsJointDossiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIJournalisationsJointDossiers();
    }

    public String getDocuments() {
        return documents;
    }

    public String getForCsType() {
        return forCsType;
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdDomaine() {
        return forIdDomaine;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdExtern() {
        return forIdExtern;
    }

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    public String getForIdUtilisateur() {
        return forIdUtilisateur;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdDomaine(String forIdDomaine) {
        this.forIdDomaine = forIdDomaine;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdExtern(String forIdExtern) {
        this.forIdExtern = forIdExtern;
    }

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }

    public void setForIdUtilisateur(String forIdUtilisateur) {
        this.forIdUtilisateur = forIdUtilisateur;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
