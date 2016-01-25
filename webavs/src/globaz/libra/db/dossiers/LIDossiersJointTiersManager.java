/*
 * Créé le 24 juillet 2009
 */
package globaz.libra.db.dossiers;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.IJOCommonGroupeJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceProvenanceDefTable;
import globaz.journalisation.db.journalisation.access.IJOGroupeJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOJournalisationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceProvenanceDefTable;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * 
 * @author HPE
 * 
 */
public class LIDossiersJointTiersManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAffichage = new String();

    private String forCsEtat = new String();
    private String forDateNaissance = new String();

    private String forIdDomaine = new String();
    private String forIdExterne = new String();
    private String forIdGroupe = new String();

    private String forIdTiers = new String();

    private String forIdUtilisateur = new String();

    private transient String fromClause = null;
    private String likeNom = new String();
    private String likeNumeroAVS = new String();
    private String likeNumeroAVSNNSS = new String();

    private String likePrenom = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public LIDossiersJointTiersManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(LIDossiersJointTiers.createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                        + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                        + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS
                        + ")");
            }

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return LIDossiers.FIELDNAME_IS_URGENT;
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

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS()));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema
                    + LIDossiersJointTiers.TABLE_TIERS
                    + "."
                    + LIDossiersJointTiers.FIELDNAME_NOM_FOR_SEARCH
                    + " LIKE "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likeNom)
                            + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema
                    + LIDossiersJointTiers.TABLE_TIERS
                    + "."
                    + LIDossiersJointTiers.FIELDNAME_PRENOM_FOR_SEARCH
                    + " LIKE "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likePrenom)
                            + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdExterne)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_EXTERNE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdExterne));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_TIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_CS_ETAT);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forCsEtat));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdUtilisateur)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            if (forIdUtilisateur.equals("idFX")) {
                sqlWhere.append(LIUtilisateurs.FIELDNAME_ID_UTILISATEUR_EXTERNE);
                sqlWhere.append(" = ");
                sqlWhere.append(_dbWriteString(statement.getTransaction(), getSession().getUserId()));
            } else {
                sqlWhere.append(LIDossiers.FIELDNAME_ID_GESTIONNAIRE);
                sqlWhere.append(" = ");
                sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdUtilisateur));
            }

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdGroupe)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIDossiers.FIELDNAME_ID_GROUPE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdGroupe));
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
                sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDomaine));
            }

        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + LIDossiersJointTiers.TABLE_PERSONNE + "."
                    + LIDossiersJointTiers.FIELDNAME_DATENAISSANCE + "="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));

        }

        if (forAffichage.equals(ILIConstantesExternes.CS_AFF_SUSPENS)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            String innerJoin = " INNER JOIN ";
            String leftJoin = " LEFT JOIN ";
            String on = " ON ";
            String and = " AND ";
            String point = ".";
            String egal = "=";

            sqlWhere.append("(SELECT COUNT(*) FROM ").append(_getCollection()).append(LIDossiers.TABLE_NAME)
                    .append(" AS jointDos ");

            // Jointure entre table des ReferenceProvnance et des dossiers
            sqlWhere.append(leftJoin).append(_getCollection()).append(IJOReferenceProvenanceDefTable.TABLE_NAME)
                    .append(" ON ");
            sqlWhere.append(_getCollection()).append(IJOReferenceProvenanceDefTable.TABLE_NAME);
            sqlWhere.append(point);
            sqlWhere.append(IJOCommonReferenceProvenanceDefTable.IDCLEREFERENCEPROVENANCE);
            sqlWhere.append(egal);
            sqlWhere.append("replace(ltrim(replace(digits(");
            sqlWhere.append("jointDos");
            sqlWhere.append(point);
            sqlWhere.append(LIDossiers.FIELDNAME_ID_DOSSIER);
            sqlWhere.append("), '0', ' ')), ' ', '0')");
            sqlWhere.append(and);
            sqlWhere.append(_getCollection());
            sqlWhere.append(IJOReferenceProvenanceDefTable.TABLE_NAME);
            sqlWhere.append(point);
            sqlWhere.append(IJOCommonReferenceProvenanceDefTable.TYPEREFERENCEPROVENANCE);
            sqlWhere.append(egal);
            sqlWhere.append("'" + ILIConstantesExternes.REF_PRO_DOSSIER + "'");

            // Jointure entre table des journalisations et des
            // referenceProvenance
            sqlWhere.append(innerJoin);
            sqlWhere.append(_getCollection());
            sqlWhere.append(IJOJournalisationDefTable.TABLE_NAME);
            sqlWhere.append(on);
            sqlWhere.append(_getCollection());
            sqlWhere.append(IJOJournalisationDefTable.TABLE_NAME);
            sqlWhere.append(point);
            sqlWhere.append(IJOCommonJournalisationDefTable.IDJOURNALISATION);
            sqlWhere.append(egal);
            sqlWhere.append(_getCollection());
            sqlWhere.append(IJOReferenceProvenanceDefTable.TABLE_NAME);
            sqlWhere.append(point);
            sqlWhere.append(IJOCommonReferenceProvenanceDefTable.IDJOURNALISATION);

            // Jointure entre table des journalisations et des groupeJournal
            sqlWhere.append(innerJoin);
            sqlWhere.append(_getCollection());
            sqlWhere.append(IJOGroupeJournalDefTable.TABLE_NAME);
            sqlWhere.append(on);
            sqlWhere.append(_getCollection());
            sqlWhere.append(IJOJournalisationDefTable.TABLE_NAME);
            sqlWhere.append(point);
            sqlWhere.append(IJOCommonJournalisationDefTable.IDGROUPEJOURNAL);
            sqlWhere.append(egal);
            sqlWhere.append(_getCollection());
            sqlWhere.append(IJOGroupeJournalDefTable.TABLE_NAME);
            sqlWhere.append(point);
            sqlWhere.append(IJOCommonGroupeJournalDefTable.IDGROUPEJOURNAL);

            sqlWhere.append(" WHERE ");

            sqlWhere.append(IJOCommonGroupeJournalDefTable.DATE_RAPPEL);
            sqlWhere.append(" <= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), JACalendar.todayJJsMMsAAAA()));

            sqlWhere.append(" AND ");
            sqlWhere.append("dossier.").append(LIDossiers.FIELDNAME_ID_DOSSIER);
            sqlWhere.append("=jointDos.").append(LIDossiers.FIELDNAME_ID_DOSSIER);

            sqlWhere.append(" AND ");
            sqlWhere.append(IJOCommonJournalisationDefTable.CSTYPEJOURNAL);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), JOConstantes.CS_JO_RAPPEL));

            sqlWhere.append(") <>0");

        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LIDossiersJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIDossiersJointTiers();
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getForAffichage() {
        return forAffichage;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdDomaine() {
        return forIdDomaine;
    }

    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdUtilisateur() {
        return forIdUtilisateur;
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

    public void setForAffichage(String forAffichage) {
        this.forAffichage = forAffichage;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdDomaine(String forIdDomaine) {
        this.forIdDomaine = forIdDomaine;
    }

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdUtilisateur(String forIdUtilisateur) {
        this.forIdUtilisateur = forIdUtilisateur;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

}