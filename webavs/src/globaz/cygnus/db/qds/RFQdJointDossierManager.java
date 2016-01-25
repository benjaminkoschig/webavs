/*
 * Créé le 20 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import java.util.Set;

/**
 * @author jje
 */
public class RFQdJointDossierManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forAnneeQd = "";
    private String forCsEtatQd = "";
    private String forCsGenreQd = "";
    private String forCsTypeRelation = "";
    private String forIdDossier = "";
    private String forIdPot = "";
    private Set<String> forIdsQd = null;
    private Set<String> forIdsTiers = null;
    private String forIdTiers = "";
    private String forNotAnneeQd = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdPrincipaleJointDossierManager
     */
    public RFQdJointDossierManager() {
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
            StringBuffer from = new StringBuffer(RFQdJointDossier.createFromClause(_getCollection()));

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

        if (!JadeStringUtil.isBlankOrZero(forIdTiers)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if (!JadeStringUtil.isBlankOrZero(forIdPot)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_POT_DROIT_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdPot));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsEtatQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsGenreQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_GENRE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsGenreQd));
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

        if (!JadeStringUtil.isBlankOrZero(forNotAnneeQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_ANNEE_QD);
            sqlWhere.append(" <> ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forNotAnneeQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forIdDossier)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
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
     * Définition de l'entité (RFQdPrincipaleJointDossier)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdJointDossier();
    }

    public String getForAnneeQd() {
        return forAnneeQd;
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

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdPot() {
        return forIdPot;
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

    public String getForNotAnneeQd() {
        return forNotAnneeQd;
    }

    public void setForAnneeQd(String forAnneeQd) {
        this.forAnneeQd = forAnneeQd;
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

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdPot(String forIdPot) {
        this.forIdPot = forIdPot;
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

    public void setForNotAnneeQd(String forNotAnneeQd) {
        this.forNotAnneeQd = forNotAnneeQd;
    }

}
