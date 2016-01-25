/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.typeDeSoins.RFPotAssure;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import java.util.Set;

/**
 * @author jje
 */
public class RFQdAssureJointDossierJointTiersManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnneeQd = "";

    private String forCodeSousTypeDeSoin = "";
    private String forCodeTypeDeSoin = "";
    private boolean forCsEtatNotCloture = false;
    private String forCsEtatQd = "";
    private String forCsGenreQd = "";
    private String forCsTypeRelation = "";
    private String forDateBetweenPeriode = "";
    private String forDateDebutBetweenPeriode = "";

    private String forDateFinBetweenPeriode = "";

    private String forIdPotAssure = "";
    private Set<String> forIdsQd = null;

    private Set<String> forIdsTiers = null;

    private String forIdTiers = "";

    private transient String fromClause = null;

    private String idQdToIgnore = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdAssureJointDossierJointTiersManager.
     */
    public RFQdAssureJointDossierJointTiersManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append(RFQdAssure.FIELDNAME_ID_QD_ASSURE).append(",");
        fields.append(RFQdAssure.FIELDNAME_ID_SOUS_TYPE_DE_SOIN).append(",");
        fields.append(RFQdAssure.FIELDNAME_ID_POT_SOUS_TYPE_DE_SOIN).append(",");
        fields.append(RFQdAssure.FIELDNAME_DATE_DEBUT).append(",");
        fields.append(RFQdAssure.FIELDNAME_DATE_FIN).append(",");

        fields.append(RFQd.FIELDNAME_ID_QD).append(",");
        fields.append(RFQd.FIELDNAME_DATE_CREATION).append(",");
        fields.append(RFQd.FIELDNAME_LIMITE_ANNUELLE).append(",");
        fields.append(RFQd.FIELDNAME_CS_GENRE_QD).append(",");
        fields.append(RFQd.FIELDNAME_IS_PLAFONNEE).append(",");
        fields.append(RFQd.FIELDNAME_CS_ETAT_QD).append(",");
        fields.append(RFQd.FIELDNAME_CS_SOURCE).append(",");
        fields.append(RFQd.FIELDNAME_ID_GESTIONNAIRE).append(",");
        fields.append(RFQd.FIELDNAME_MONTANT_CHARGE_RFM).append(",");
        fields.append(RFQd.FIELDNAME_ANNEE_QD).append(",");
        fields.append(RFQd.FIELDNAME_MONTANT_INITIAL_CHARGE_RFM).append(",");

        fields.append(RFPotAssure.FIELDNAME_ID_POT_ASSURE).append(",");
        fields.append(PRDemande.FIELDNAME_IDTIERS).append(",");
        fields.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION).append(",");
        fields.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER).append(",");
        fields.append(RFSousTypeDeSoin.FIELDNAME_CODE).append(",");
        fields.append(RFTypeDeSoin.FIELDNAME_CODE);

        return fields.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFQdAssureJointDossierJointTiers.createFromClause(_getCollection()));

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
        // String schema = _getCollection();

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

        /*
         * if(!JadeStringUtil.isBlankOrZero(forIdQdPrincipaleFk)){
         * 
         * if (sqlWhere.length() != 0) { sqlWhere.append(" AND "); }
         * 
         * sqlWhere.append(RFQdAssure.FIELDNAME_ID_QD_PRINCIPALE); sqlWhere.append(" = ");
         * sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdQdPrincipaleFk)); }
         */

        if (!JadeStringUtil.isBlankOrZero(forIdPotAssure)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPotAssure.FIELDNAME_ID_POT_ASSURE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdPotAssure));
        }

        if (forCsEtatNotCloture) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
            sqlWhere.append(" <> ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFQd.CS_ETAT_QD_CLOTURE));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsGenreQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_GENRE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCsGenreQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsEtatQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsTypeRelation)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeRelation));
        }

        if (!JadeStringUtil.isBlankOrZero(forAnneeQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_ANNEE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forAnneeQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forCodeSousTypeDeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeSousTypeDeSoin));
        }

        if (!JadeStringUtil.isBlankOrZero(forCodeTypeDeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeTypeDeSoin));
        }

        if (!JadeStringUtil.isBlankOrZero(forDateBetweenPeriode)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateBetweenPeriode));
            sqlWhere.append(" AND ( ");
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateBetweenPeriode));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 )");
        }

        if (!JadeStringUtil.isBlankOrZero(forDateDebutBetweenPeriode)
                && !JadeStringUtil.isBlankOrZero(forDateFinBetweenPeriode)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" (( ");

            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenPeriode));
            sqlWhere.append(" AND (");
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenPeriode));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ) ) ");

            sqlWhere.append(" OR ");

            sqlWhere.append(" ( ");
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFinBetweenPeriode)) {
                sqlWhere.append(forDateFinBetweenPeriode);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenPeriode));
            }
            sqlWhere.append(" AND (");
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            if (RFUtils.MAX_DATE_VALUE.equals(forDateFinBetweenPeriode)) {
                sqlWhere.append(forDateFinBetweenPeriode);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenPeriode));
            }
            sqlWhere.append(" OR ");
            sqlWhere.append(RFQdAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ))) ");
        }

        if (!JadeStringUtil.isBlankOrZero(idQdToIgnore)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQdAssure.FIELDNAME_ID_QD_ASSURE);
            sqlWhere.append(" <> ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), idQdToIgnore));
        }

        if ((forIdsQd != null) && (forIdsQd.size() > 0)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_ID_QD);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String id : forIdsQd) {
                if (!JadeStringUtil.isEmpty(id)) {
                    inc++;
                    if (forIdsQd.size() != inc) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }

        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFQdJointDossierJointTiersJointDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdAssureJointDossierJointTiers();
    }

    public String getForAnneeQd() {
        return forAnneeQd;
    }

    public String getForCodeSousTypeDeSoin() {
        return forCodeSousTypeDeSoin;
    }

    public String getForCodeTypeDeSoin() {
        return forCodeTypeDeSoin;
    }

    public String getForCsEtatQd() {
        return forCsEtatQd;
    }

    public String getForCsGenreQd() {
        return forCsGenreQd;
    }

    public String getForCsTypeRelation() {
        return forCsTypeRelation;
    }

    public String getForDateBetweenPeriode() {
        return forDateBetweenPeriode;
    }

    public String getForDateDebutBetweenPeriode() {
        return forDateDebutBetweenPeriode;
    }

    public String getForDateFinBetweenPeriode() {
        return forDateFinBetweenPeriode;
    }

    public String getForIdPotAssure() {
        return forIdPotAssure;
    }

    public Set<String> getForIdsQd() {
        return forIdsQd;
    }

    public Set<String> getForIdsTiers() {
        return forIdsTiers;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getIdQdToIgnore() {
        return idQdToIgnore;
    }

    public boolean isForCsEtatNotCloture() {
        return forCsEtatNotCloture;
    }

    public void setForAnneeQd(String forAnneeQd) {
        this.forAnneeQd = forAnneeQd;
    }

    public void setForCodeSousTypeDeSoin(String forCodeSousTypeDeSoin) {
        this.forCodeSousTypeDeSoin = forCodeSousTypeDeSoin;
    }

    public void setForCodeTypeDeSoin(String forCodeTypeDeSoin) {
        this.forCodeTypeDeSoin = forCodeTypeDeSoin;
    }

    public void setForCsEtatNotCloture(boolean forCsEtatNotCloture) {
        this.forCsEtatNotCloture = forCsEtatNotCloture;
    }

    public void setForCsEtatQd(String forCsEtatQd) {
        this.forCsEtatQd = forCsEtatQd;
    }

    public void setForCsGenreQd(String forCsGenreQd) {
        this.forCsGenreQd = forCsGenreQd;
    }

    public void setForCsTypeRelation(String forCsTypeRelation) {
        this.forCsTypeRelation = forCsTypeRelation;
    }

    public void setForDateBetweenPeriode(String forDateBetweenPeriode) {
        this.forDateBetweenPeriode = forDateBetweenPeriode;
    }

    public void setForDateDebutBetweenPeriode(String forDateDebutBetweenPeriode) {
        this.forDateDebutBetweenPeriode = forDateDebutBetweenPeriode;
    }

    public void setForDateFinBetweenPeriode(String forDateFinBetweenPeriode) {
        this.forDateFinBetweenPeriode = forDateFinBetweenPeriode;
    }

    public void setForIdPotAssure(String forIdPotAssure) {
        this.forIdPotAssure = forIdPotAssure;
    }

    public void setForIdsQd(Set<String> forIdsQd) {
        this.forIdsQd = forIdsQd;
    }

    public void setForIdsTiers(Set<String> forIdsTiers) {
        this.forIdsTiers = forIdsTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setIdQdToIgnore(String idQdToIgnore) {
        this.idQdToIgnore = idQdToIgnore;
    }

}