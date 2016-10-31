/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import java.util.Set;

/**
 * @author jje
 */
public class RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeQd = "";
    private String forAnneeQdPlusGrandOuEgale = "";

    private String forCsEtatQd = "";
    private String forCsGenreQd = "";
    private String forCsTypeBeneficiaireQd = "";
    private String forCsGenrePc = "";

    public String getForCsGenrePc() {
        return forCsGenrePc;
    }

    public void setForCsGenrePc(String forCsGenrePc) {
        this.forCsGenrePc = forCsGenrePc;
    }

    public String getForCsTypeBeneficiaireQd() {
        return forCsTypeBeneficiaireQd;
    }

    public void setForCsTypeBeneficiaireQd(String forCsTypeBeneficiaireQd) {
        this.forCsTypeBeneficiaireQd = forCsTypeBeneficiaireQd;
    }

    private String forCsGenre = "";
    private String forDateDebutBetweenPeriode = "";
    private String forDateFinBetweenPeriode = "";
    private String forIdDossier = "";
    private String forIdPeriodeValToIgnore = "";
    private String forIdQd = "";
    private Set<String> forIdsTiers = null;
    private String forIdTiers = "";
    private String forNotCsEtatQd = "";
    private String forTypeRelation = "";
    private transient String fromClause = null;

    private boolean isComprisDansCalcul = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager.
     */
    public RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        // String schema = this._getCollection();

        StringBuffer fields = new StringBuffer();

        fields.append(RFQdPrincipale.FIELDNAME_CS_DEGRE_API).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_CS_GENRE_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_BENEFICIAIRE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_DATE_DEBUT_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_DATE_FIN_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_EXCEDENT_RECETTE_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_AUGMENTATION_QD).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_SOLDE_EXCEDENT_PRE_DEC).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_POT_DROIT_PC).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_IS_LAPRAMS).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_IS_RI).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_CONJOINT).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_REQUERANT).append(",");

        fields.append(RFQd.FIELDNAME_ANNEE_QD).append(",");
        fields.append(RFQd.FIELDNAME_CS_ETAT_QD).append(",");
        fields.append(RFQd.FIELDNAME_CS_GENRE_QD).append(",");
        fields.append(RFQd.FIELDNAME_CS_SOURCE).append(",");
        fields.append(RFQd.FIELDNAME_CS_TYPE_QD).append(",");
        fields.append(RFQd.FIELDNAME_DATE_CREATION).append(",");
        fields.append(RFQd.FIELDNAME_ID_GESTIONNAIRE).append(",");
        fields.append(RFQd.FIELDNAME_ID_QD).append(",");
        fields.append(RFQd.FIELDNAME_IS_PLAFONNEE).append(",");
        fields.append(RFQd.FIELDNAME_LIMITE_ANNUELLE).append(",");
        fields.append(RFQd.FIELDNAME_MONTANT_CHARGE_RFM).append(",");
        fields.append(RFQd.FIELDNAME_MONTANT_INITIAL_CHARGE_RFM).append(",");

        fields.append(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL).append(",");
        fields.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION).append(",");

        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_CONCERNE).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_MODIFICATION).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_GESTIONNAIRE).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VAL_MODIFIE_PAR).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_REMARQUE).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION).append(",");

        fields.append(PRDemande.FIELDNAME_IDTIERS).append(",");

        fields.append(RFDossier.FIELDNAME_ID_DOSSIER).append(",");

        fields.append(RFQdPrincipale.FIELDNAME_CS_GENRE_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_BENEFICIAIRE);

        return fields.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return RFQd.FIELDNAME_ID_QD;
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

        sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
        sqlWhere.append(" <> ");
        sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFQd.CS_ETAT_QD_CLOTURE));

        if (isComprisDansCalcul) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), isComprisDansCalcul,
                    BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if (!JadeStringUtil.isBlankOrZero(forAnneeQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_ANNEE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forAnneeQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forAnneeQdPlusGrandOuEgale)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_ANNEE_QD);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forAnneeQdPlusGrandOuEgale));
        }

        if (!JadeStringUtil.isBlankOrZero(forIdTiers)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if ((forIdsTiers != null) && (forIdsTiers.size() > 0)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String id : forIdsTiers) {
                if (!JadeStringUtil.isEmpty(id)) {
                    inc++;
                    if (forIdsTiers.size() != inc) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }

        }

        if (!JadeStringUtil.isBlankOrZero(forCsEtatQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsGenrePc)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQdPrincipale.FIELDNAME_CS_GENRE_PC_ACCORDEE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsGenrePc));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsTypeBeneficiaireQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQdPrincipale.FIELDNAME_CS_TYPE_BENEFICIAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeBeneficiaireQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forNotCsEtatQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
            sqlWhere.append(" <> ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forNotCsEtatQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forTypeRelation)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forTypeRelation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDossier)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDossier.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdQd)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFQd.FIELDNAME_ID_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdQd));
        }

        /************************************************************/

        if (!JadeStringUtil.isBlankOrZero(forDateDebutBetweenPeriode)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            if (!JadeStringUtil.isBlankOrZero(forDateFinBetweenPeriode)) {
                sqlWhere.append(" (( ");
            } else {
                sqlWhere.append(" ( ");
            }
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenPeriode));
            sqlWhere.append(" AND (");
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenPeriode));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ) ) ");
        }

        if (!JadeStringUtil.isBlankOrZero(forDateFinBetweenPeriode)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" OR ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFinBetweenPeriode)) {
                sqlWhere.append(forDateFinBetweenPeriode);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenPeriode));
            }
            sqlWhere.append(" AND (");
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFinBetweenPeriode)) {
                sqlWhere.append(forDateFinBetweenPeriode);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenPeriode));
            }
            sqlWhere.append(" OR ");
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ))");

            sqlWhere.append(" OR ");
            sqlWhere.append(" ( ");

            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" >= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateDebutBetweenPeriode)) {
                sqlWhere.append(forDateDebutBetweenPeriode);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenPeriode));
            }
            sqlWhere.append(" AND (");
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
            sqlWhere.append(" <= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFinBetweenPeriode)) {
                sqlWhere.append(forDateFinBetweenPeriode);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenPeriode));
            }
            sqlWhere.append(" AND ");
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
            sqlWhere.append(" <> 0 ))");
            sqlWhere.append(" ) ");
        }

        /*
         * if (!JadeStringUtil.isBlankOrZero(this.forDateDebutBetweenPeriode)) { if (sqlWhere.length() > 0) {
         * sqlWhere.append(" AND "); } if (!JadeStringUtil.isBlankOrZero(this.forDateFinBetweenPeriode)) {
         * sqlWhere.append(" (( "); } else { sqlWhere.append(" ( "); }
         * sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT); sqlWhere.append(" <= ");
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.forDateDebutBetweenPeriode));
         * sqlWhere.append(" AND ("); sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
         * sqlWhere.append(" >= "); sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(),
         * this.forDateDebutBetweenPeriode)); sqlWhere.append(" OR ");
         * sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN); sqlWhere.append(" = 0 ) ) "); }
         * 
         * if (!JadeStringUtil.isBlankOrZero(this.forDateFinBetweenPeriode)) { if (sqlWhere.length() > 0) {
         * sqlWhere.append(" OR "); }
         * 
         * sqlWhere.append(" ( "); sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
         * sqlWhere.append(" <= "); if (RFUtils.MAX_DATE_VALUE.equals(this.forDateFinBetweenPeriode)) {
         * sqlWhere.append(this.forDateFinBetweenPeriode); } else {
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.forDateFinBetweenPeriode)); }
         * sqlWhere.append(" AND ("); sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
         * sqlWhere.append(" >= "); if (RFUtils.MAX_DATE_VALUE.equals(this.forDateFinBetweenPeriode)) {
         * sqlWhere.append(this.forDateFinBetweenPeriode); } else {
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.forDateFinBetweenPeriode)); }
         * sqlWhere.append(" OR "); sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
         * sqlWhere.append(" = 0 ))");
         * 
         * sqlWhere.append(" OR "); sqlWhere.append(" ( ");
         * 
         * sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT); sqlWhere.append(" >= "); if
         * (RFUtils.MAX_DATE_VALUE.equals(this.forDateDebutBetweenPeriode)) {
         * sqlWhere.append(this.forDateDebutBetweenPeriode); } else {
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.forDateDebutBetweenPeriode)); }
         * sqlWhere.append(" AND ("); sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
         * sqlWhere.append(" <= "); if (RFUtils.MAX_DATE_VALUE.equals(this.forDateFinBetweenPeriode)) {
         * sqlWhere.append(this.forDateFinBetweenPeriode); } else {
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.forDateFinBetweenPeriode)); }
         * sqlWhere.append(" OR "); sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
         * sqlWhere.append(" = 0 ))"); sqlWhere.append(" ) "); }
         */

        if (!JadeStringUtil.isIntegerEmpty(forIdPeriodeValToIgnore)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
            sqlWhere.append(" <> ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdPeriodeValToIgnore));
        }

        // Sélection de la dernière version des périodes de validité
        if (sqlWhere.length() > 0) {
            sqlWhere.append(" AND ");
        }

        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(" in ");
        sqlWhere.append("(Select ");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(" from ");
        sqlWhere.append(schema);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        sqlWhere.append(" as a");
        sqlWhere.append(" where ");
        sqlWhere.append("a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append(" = ");
        sqlWhere.append(schema);
        sqlWhere.append(RFQdPrincipale.TABLE_NAME);
        sqlWhere.append(".");
        sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append(" and ");
        sqlWhere.append("a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION);
        sqlWhere.append(" <> ");
        sqlWhere.append(IRFQd.CS_SUPPRESSION);
        sqlWhere.append(" and not exists (select * from ");
        sqlWhere.append(schema);
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        sqlWhere.append(" where ");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION);
        sqlWhere.append(" = ");
        sqlWhere.append("a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION);
        sqlWhere.append(" and ");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(" > a.");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        sqlWhere.append(" and ");
        sqlWhere.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append(" = ");
        sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        sqlWhere.append("))");

        return sqlWhere.toString();

    }

    /**
     * Définition de l'entité (RFQdJointDossierJointTiersJointDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande();
    }

    public String getForAnneeQd() {
        return forAnneeQd;
    }

    public String getForAnneeQdPlusGrandOuEgale() {
        return forAnneeQdPlusGrandOuEgale;
    }

    public String getForCsEtatQd() {
        return forCsEtatQd;
    }

    public String getForCsGenreQd() {
        return forCsGenreQd;
    }

    public String getForCsTypePC() {
        return forCsGenre;
    }

    public String getForDateDebutBetweenPeriode() {
        return forDateDebutBetweenPeriode;
    }

    public String getForDateFinBetweenPeriode() {
        return forDateFinBetweenPeriode;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdPeriodeValToIgnore() {
        return forIdPeriodeValToIgnore;
    }

    public String getForIdQd() {
        return forIdQd;
    }

    public Set<String> getForIdsTiers() {
        return forIdsTiers;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNotCsEtatQd() {
        return forNotCsEtatQd;
    }

    public String getForTypeRelation() {
        return forTypeRelation;
    }

    public boolean isComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    public void setComprisDansCalcul(boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

    public void setForAnneeQd(String forAnneeQd) {
        this.forAnneeQd = forAnneeQd;
    }

    public void setForAnneeQdPlusGrandOuEgale(String forAnneeQdPlusGrandOuEgale) {
        this.forAnneeQdPlusGrandOuEgale = forAnneeQdPlusGrandOuEgale;
    }

    public void setForCsEtatQd(String forCsEtatQd) {
        this.forCsEtatQd = forCsEtatQd;
    }

    public void setForCsGenreQd(String forCsGenreQd) {
        this.forCsGenreQd = forCsGenreQd;
    }

    public void setForCsTypePC(String forCsTypePC) {
        forCsGenre = forCsTypePC;
    }

    public void setForDateDebutBetweenPeriode(String forDateDebutBetweenPeriode) {
        this.forDateDebutBetweenPeriode = forDateDebutBetweenPeriode;
    }

    public void setForDateFinBetweenPeriode(String forDateFinBetweenPeriode) {
        this.forDateFinBetweenPeriode = forDateFinBetweenPeriode;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdPeriodeValToIgnore(String forIdPeriodeValToIgnore) {
        this.forIdPeriodeValToIgnore = forIdPeriodeValToIgnore;
    }

    public void setForIdQd(String forIdQd) {
        this.forIdQd = forIdQd;
    }

    public void setForIdsTiers(Set<String> forIdsTiers) {
        this.forIdsTiers = forIdsTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNotCsEtatQd(String forNotCsEtatQd) {
        this.forNotCsEtatQd = forNotCsEtatQd;
    }

    public void setForTypeRelation(String forTypeRelation) {
        this.forTypeRelation = forTypeRelation;
    }

}