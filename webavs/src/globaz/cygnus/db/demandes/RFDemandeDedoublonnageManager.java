/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.db.demandes;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import java.util.Set;

/**
 * @author jje
 */
public class RFDemandeDedoublonnageManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final String IS_NULL = " is null ";

    private static final String ZERO = " 0 ";
    private String forCodeSousTypeDeSoinList = "";

    private String forCodeTypeDeSoinList = "";

    // private String forCsEtatDemande = "";
    // private String forCsStatutDemande = "";

    private String forDateFacture = "";
    private Set<String> forIdsDemandeToIgnore = null;
    private String forIdTiers = "";

    // private String forMontantAPayer = "";
    private String forMontantFacture = "";
    private String forNoFacture = "";

    private transient String fromClause = null;
    private String fromDateDebutTraitement = "";

    private String fromDateFinTraitement = "";
    private boolean hasMontantfacture = false;
    private boolean hasNumeroFacture = false;
    private boolean hasPeriodeTraitement = false;
    private boolean isPeriodeTraitementCumulative = true;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeDedoublonnageManager
     */
    public RFDemandeDedoublonnageManager() {
        super();
        wantCallMethodBeforeFind(false);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFDemandeDedoublonnage.createFromClause(_getCollection()));

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

        if ((forIdsDemandeToIgnore != null) && (forIdsDemandeToIgnore.size() > 0)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" NOT IN (");

            int inc = 0;
            for (String id : forIdsDemandeToIgnore) {
                if (!JadeStringUtil.isEmpty(id)) {
                    inc++;
                    if (forIdsDemandeToIgnore.size() != inc) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCodeTypeDeSoinList)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeTypeDeSoinList));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCodeSousTypeDeSoinList)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeSousTypeDeSoinList));
        }

        if (!JadeStringUtil.isIntegerEmpty(forMontantFacture)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_MONTANT_FACTURE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forMontantFacture));

        } else {

            if (hasMontantfacture) {

                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(RFDemande.FIELDNAME_MONTANT_FACTURE);
                sqlWhere.append(" = ");
                sqlWhere.append(RFDemandeDedoublonnageManager.ZERO);

            }
        }

        if (!JadeStringUtil.isBlank(forNoFacture)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_NUMERO_FACTURE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forNoFacture));
        } else {

            if (hasNumeroFacture) {

                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(" ( ");
                sqlWhere.append(RFDemande.FIELDNAME_NUMERO_FACTURE);
                sqlWhere.append(" = ");
                sqlWhere.append("''");
                sqlWhere.append(" OR ");
                sqlWhere.append(RFDemande.FIELDNAME_NUMERO_FACTURE);
                sqlWhere.append(RFDemandeDedoublonnageManager.IS_NULL);
                sqlWhere.append(" ) ");
            }

        }

        if (!JadeStringUtil.isBlank(forDateFacture)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            if (!JadeStringUtil.isBlankOrZero(fromDateDebutTraitement) || hasPeriodeTraitement) {
                sqlWhere.append(" ( ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateFacture));
        }

        if (!JadeStringUtil.isBlankOrZero(fromDateDebutTraitement)) {

            if (sqlWhere.length() > 0) {
                if (isPeriodeTraitementCumulative) {
                    sqlWhere.append(" AND ");
                } else {
                    sqlWhere.append(" OR ");
                }
            }

            sqlWhere.append(" ( " + RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebutTraitement));

            sqlWhere.append(" AND ");

            if (!JadeStringUtil.isBlankOrZero(fromDateFinTraitement)) {
                sqlWhere.append(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT + "="
                        + this._dbWriteDateAMJ(statement.getTransaction(), fromDateFinTraitement) + " ) ");
            } else {
                sqlWhere.append(" ( " + RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT + " = "
                        + RFDemandeDedoublonnageManager.ZERO + " OR " + RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT
                        + RFDemandeDedoublonnageManager.IS_NULL + " )) ");
            }

            if (!JadeStringUtil.isBlankOrZero(forDateFacture)) {
                sqlWhere.append(" ) ");
            }
        } else {

            if (hasPeriodeTraitement) {

                if (sqlWhere.length() > 0) {
                    if (isPeriodeTraitementCumulative) {
                        sqlWhere.append(" AND ");
                    } else {
                        sqlWhere.append(" OR ");
                    }
                }

                sqlWhere.append(" (( ");
                sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
                sqlWhere.append(" = ");
                sqlWhere.append(RFDemandeDedoublonnageManager.ZERO);
                sqlWhere.append(" OR ");
                sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
                sqlWhere.append(RFDemandeDedoublonnageManager.IS_NULL);
                sqlWhere.append(")");
                sqlWhere.append(" AND ");
                sqlWhere.append("(");
                sqlWhere.append(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT);
                sqlWhere.append(" = ");
                sqlWhere.append(RFDemandeDedoublonnageManager.ZERO);
                sqlWhere.append(" OR ");
                sqlWhere.append(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT);
                sqlWhere.append(RFDemandeDedoublonnageManager.IS_NULL);
                sqlWhere.append(")) ");
                if (!JadeStringUtil.isBlankOrZero(forDateFacture)) {
                    sqlWhere.append(" ) ");
                }

            }
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeDedoublonnage();
    }

    public String getForCodeSousTypeDeSoinList() {
        return forCodeSousTypeDeSoinList;
    }

    public String getForCodeTypeDeSoinList() {
        return forCodeTypeDeSoinList;
    }

    public String getForDateFacture() {
        return forDateFacture;
    }

    /*
     * public String getForCsEtatDemande() { return forCsEtatDemande; }
     * 
     * public void setForCsEtatDemande(String forCsEtatDemande) { this.forCsEtatDemande = forCsEtatDemande; }
     * 
     * public String getForCsStatutDemande() { return forCsStatutDemande; }
     * 
     * public void setForCsStatutDemande(String forCsStatutDemande) { this.forCsStatutDemande = forCsStatutDemande; }
     */

    public Set<String> getForIdsDemandeToIgnore() {
        return forIdsDemandeToIgnore;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForMontantFacture() {
        return forMontantFacture;
    }

    public String getForNoFacture() {
        return forNoFacture;
    }

    public String getFromDateDebutTraitement() {
        return fromDateDebutTraitement;
    }

    public String getFromDateFinTraitement() {
        return fromDateFinTraitement;
    }

    public boolean hasMontantfacture() {
        return hasMontantfacture;
    }

    public boolean hasNumeroFacture() {
        return hasNumeroFacture;
    }

    public boolean hasPeriodeTraitement() {
        return hasPeriodeTraitement;
    }

    public boolean isPeriodeTraitementCumulative() {
        return isPeriodeTraitementCumulative;
    }

    public void setForCodeSousTypeDeSoinList(String forCodeSousTypeDeSoinList) {
        this.forCodeSousTypeDeSoinList = forCodeSousTypeDeSoinList;
    }

    public void setForCodeTypeDeSoinList(String forCodeTypeDeSoinList) {
        this.forCodeTypeDeSoinList = forCodeTypeDeSoinList;
    }

    public void setForDateFacture(String forDateFacture) {
        this.forDateFacture = forDateFacture;
    }

    public void setForIdsDemandeToIgnore(Set<String> forIdsDemandeToIgnore) {
        this.forIdsDemandeToIgnore = forIdsDemandeToIgnore;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForMontantFacture(String forMontantFacture) {
        this.forMontantFacture = forMontantFacture;
    }

    public void setForNoFacture(String forNoFacture) {
        this.forNoFacture = forNoFacture;
    }

    public void setFromDateDebutTraitement(String fromDateDebutTraitement) {
        this.fromDateDebutTraitement = fromDateDebutTraitement;
    }

    public void setFromDateFinTraitement(String fromDateFinTraitement) {
        this.fromDateFinTraitement = fromDateFinTraitement;
    }

    public void setHasMontantfacture(boolean hasMontantfacture) {
        this.hasMontantfacture = hasMontantfacture;
    }

    public void setHasNumeroFacture(boolean hasNumeroFacture) {
        this.hasNumeroFacture = hasNumeroFacture;
    }

    public void setHasPeriodeTraitement(boolean hasPeriodeTraitement) {
        this.hasPeriodeTraitement = hasPeriodeTraitement;
    }

    public void setPeriodeTraitementCumulative(boolean isPeriodeTraitementCumulative) {
        this.isPeriodeTraitementCumulative = isPeriodeTraitementCumulative;
    }

}
