package globaz.naos.db.affiliation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public class AFAffiliationManager extends BManager implements Serializable {
    // détermine les types d'affiliation pour le domaine paritaire
    private final static String[] AFFILIATION_TYPES_PARITAIRES = new String[] { CodeSystem.TYPE_AFFILI_EMPLOY,
            CodeSystem.TYPE_AFFILI_INDEP_EMPLOY, CodeSystem.TYPE_AFFILI_EMPLOY_D_F, CodeSystem.TYPE_AFFILI_LTN };
    // détermine les type d'affiliation pour le domaine personnel
    private final static String[] AFFILIATION_TYPES_PERSONNELLES = new String[] { CodeSystem.TYPE_AFFILI_INDEP,
            CodeSystem.TYPE_AFFILI_NON_ACTIF, CodeSystem.TYPE_AFFILI_SELON_ART_1A, CodeSystem.TYPE_AFFILI_INDEP_EMPLOY,
            CodeSystem.TYPE_AFFILI_TSE, CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE };
    public final static java.lang.String ANNUEL30JUIN = "ann";

    public static int ORDER_AFFILIDATIONID = 2;
    //
    public static int ORDER_AFFILIENUMERO = 1;
    //
    public final static java.lang.String PARITAIRE = "par";
    public final static java.lang.String PERSONNEL = "per";
    private java.lang.String forAffiliationId;
    private java.lang.String forAffilieNumero;
    private String forAffilieNumeroIn = "";
    private java.lang.String forAncienNumero;
    private String forAnnee;

    private String forBranchBrancheEconomique;
    private String forCodeNoga = new String();

    private String forDateDebut;
    private java.lang.String forDateDebutAffLowerOrEqualTo;
    private java.lang.String forDateDebutGreaterOrEqualTo;
    private java.lang.String forDateDebutLowerOrEqualTo;
    private java.lang.String forDateFinGreater;

    private String forDateFin;
    private java.lang.String forDeclarationSalaire;
    private java.lang.String forEntreDate;
    private Boolean forEnvoiAutoAnnSal;
    private java.lang.String forIdTiers;
    private Boolean forIsNotReleveParitaire;
    //
    private Boolean forIsTraitement = null;
    private String forMotifFin = new String();
    private String forNotTypeAffiliation;
    private java.lang.String forPeriodicite;
    private java.lang.String[] forPeriodiciteIn = new String[] {};
    private java.lang.String forTillDateDebut;

    private java.lang.String forTillDateFin;
    private String[] forTypeAffiliation;

    private java.lang.String forTypeFacturation;
    private java.lang.String forTypeNNSS;
    private java.lang.String fromAffilieNumero;

    private String fromDateCreation;
    private java.lang.String fromDateFin;
    private java.lang.String fromDateFinNonVide;

    private String fromDebutAffiliation;

    private java.lang.String fromId;
    private java.lang.String fromNom;
    private java.lang.String likeAffilieNumero;
    private java.lang.String likeAncienNumero;
    //
    private String notInCodeFacturation = "";
    private java.lang.String order = new String();
    private java.lang.String toAffilieNumero;
    private String toDateCreation;

    private java.lang.String toDateFin;

    // modif NNSS
    private java.lang.String toDateNNSS;
    private boolean forActif = false;
    private boolean forNumeroIdeNotEmpty = false;

    private String forNumeroIde;

    public String getForNumeroIde() {
        return forNumeroIde;
    }

    public void setForNumeroIde(String forNumeroIde) {
        this.forNumeroIde = forNumeroIde;
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (!JadeStringUtil.isEmpty(getFromNom())) {
            return _getCollection() + "AFAFFIP INNER JOIN " + _getCollection() + "TITIERP ON " + _getCollection()
                    + "TITIERP.HTITIE=" + _getCollection() + "AFAFFIP.HTITIE";
        } else {
            return _getCollection() + "AFAFFIP";
        }
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getForNumeroIde())) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String numeroIde = forNumeroIde;
            numeroIde = numeroIde.replaceAll("[^\\d]", "");
            numeroIde = "CHE" + numeroIde;

            sqlWhere += AFAffiliation.FIELDNAME_NUMERO_IDE + " = "
                    + this._dbWriteString(statement.getTransaction(), numeroIde);
        }

        if (!JadeStringUtil.isEmpty(getFromDateCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADCRE >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation());
        }

        if (!JadeStringUtil.isEmpty(getFromDebutAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADDEB >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDebutAffiliation());
        }

        if (!JadeStringUtil.isEmpty(getToDateCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADCRE <= " + this._dbWriteDateAMJ(statement.getTransaction(), getToDateCreation());
        }

        if (!JadeStringUtil.isEmpty(getForDeclarationSalaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MATDEC=" + this._dbWriteNumeric(statement.getTransaction(), getForDeclarationSalaire());
        }

        if (!JadeStringUtil.isEmpty(getForMotifFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MATMOT = " + this._dbWriteNumeric(statement.getTransaction(), getForMotifFin());
        }
        if (getNotInCodeFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MATCFA not in (" + getNotInCodeFacturation() + ")";
        }
        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForEntreDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADDEB <= " + this._dbWriteDateAMJ(statement.getTransaction(), getForEntreDate())
                    + " AND (MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getForEntreDate())
                    + " OR MADFIN=0)";
        }

        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADDEB <= " + this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + getForAnnee())
                    + " AND (MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + getForAnnee())
                    + " OR MADFIN=0)";
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MALNAF," + _getCollection() + "AFAFFIP.MADDEB";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }
        if (!JadeStringUtil.isEmpty(getFromId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MAIAFF>=" + this._dbWriteNumeric(statement.getTransaction(), getFromId());
        }

        if (!JadeStringUtil.isEmpty(getForPeriodicite())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MATPER=" + this._dbWriteNumeric(statement.getTransaction(), getForPeriodicite());
        }
        if (getForPeriodiciteIn().length != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MATPER in(";
            for (int i = 0; i < getForPeriodiciteIn().length; i++) {
                sqlWhere += this._dbWriteNumeric(statement.getTransaction(), getForPeriodiciteIn()[i]);
                if ((i + 1) < getForPeriodiciteIn().length) {
                    sqlWhere += ",";
                }
            }
            sqlWhere += " ) ";
        }
        if (!JadeStringUtil.isEmpty(getForAffilieNumero())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MADDEB";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF=" + this._dbWriteString(statement.getTransaction(), getForAffilieNumero());
        }

        if (!JadeStringUtil.isEmpty(getForAffilieNumeroIn())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MADDEB";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF in (" + getForAffilieNumeroIn() + ") ";
        }

        if (!JadeStringUtil.isEmpty(getForAncienNumero())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MADDEB";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAA=" + this._dbWriteString(statement.getTransaction(), getForAncienNumero());
        }
        if (!JadeStringUtil.isEmpty(getLikeAncienNumero())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MALNAA";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAA like '" + getLikeAncienNumero() + "%'";
        }
        if (!JadeStringUtil.isEmpty(getFromNom())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "TITIERP.HTLDU1";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "TITIERP.HTLDU1 like "
                    + this._dbWriteString(statement.getTransaction(), getFromNom().toUpperCase() + "%");
        }
        if (!JadeStringUtil.isEmpty(getForTypeFacturation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForTypeFacturation().equals(AFAffiliationManager.PARITAIRE)) {
                sqlWhere += "MATTAF in(804002,804005,804010,804012)";
            } else {
                sqlWhere += "MATTAF in(804001,804004) AND MATPER not in(802005)";
            }
        }

        if (!JadeStringUtil.isEmpty(getForTypeNNSS())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // if (getForTypeFacturation().equals("true"))
            sqlWhere += "MATTAF in(804001,804004,804005,804008)";
        }

        if (!JadeStringUtil.isEmpty(getLikeAffilieNumero())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MALNAF";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF like '" + getLikeAffilieNumero() + "%'";
        }

        if (!JadeStringUtil.isEmpty(getToDateNNSS())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (MADFIN = 0 OR ( MADFIN <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getToDateNNSS());
            // sqlWhere += " AND MADFIN >= 20070101 )";
            String tDate = this._dbWriteDateAMJ(statement.getTransaction(), getToDateNNSS());
            sqlWhere += " AND MADFIN >= " + tDate.substring(0, 4) + "0101 ))";
            // System.out.print(sqlWhere);
        }

        if (!JadeStringUtil.isEmpty(getFromAffilieNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF >= '" + getFromAffilieNumero() + "'";
        }
        if (!JadeStringUtil.isEmpty(getToAffilieNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF <= '" + getToAffilieNumero() + "'";
        }
        // traitement du numéro d'affilié, fonctionne comme un between
        if (!JadeStringUtil.isEmpty(getForTillDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADDEB < " + this._dbWriteDateAMJ(statement.getTransaction(), getForTillDateDebut());
        }
        // traitement du numéro d'affilié, fonctionne comme un between
        if (!JadeStringUtil.isEmpty(getForTillDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADFIN = " + this._dbWriteDateAMJ(statement.getTransaction(), getForTillDateFin());
        }
        // Positionnement depuis la date de fin
        if (!JadeStringUtil.isEmpty(getFromDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN>=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFin())
                    + " OR MADFIN=0)";
        }
        // Positionnement depuis la date de fin
        if (!JadeStringUtil.isEmpty(getForDateFinGreater())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN>" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFinGreater())
                    + " OR MADFIN=0)";
        }
        // traitement du ISTRAITEMENT
        if (getForIsTraitement() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MABTRA = "
                    + this._dbWriteBoolean(statement.getTransaction(), getForIsTraitement(), "is traitement");
        }
        if ((getForIsNotReleveParitaire() != null) && getForIsNotReleveParitaire().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MABREP <> "
                    + this._dbWriteBoolean(statement.getTransaction(), getForIsNotReleveParitaire(),
                            BConstants.DB_TYPE_BOOLEAN_NUMERIC, "forIsReleveParitaire");
        }
        if (forEnvoiAutoAnnSal != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MABEAA = "
                    + this._dbWriteBoolean(statement.getTransaction(), forEnvoiAutoAnnSal, "forEnvoiAutoAnnSal");
        }
        if (!JadeStringUtil.isEmpty(getForDateDebutLowerOrEqualTo())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADTEN<=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebutLowerOrEqualTo());
        }
        if (!JadeStringUtil.isEmpty(getForDateDebutAffLowerOrEqualTo())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADDEB<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebutAffLowerOrEqualTo());
        }

        if (!JadeStringUtil.isEmpty(getForDateDebutGreaterOrEqualTo())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADTEN>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebutGreaterOrEqualTo());
        }
        if (!JadeStringUtil.isEmpty(getToDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADFIN < " + this._dbWriteDateAMJ(statement.getTransaction(), getToDateFin());
        }
        if (!JadeStringUtil.isEmpty(getFromDateFinNonVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFinNonVide());
        }

        if (isForNumeroIdeNotEmpty()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " MALFED <> '' ";
        }

        if ((forTypeAffiliation != null) && (forTypeAffiliation.length > 0)) {
            StringBuffer whereClause = new StringBuffer();

            if (sqlWhere.length() > 0) {
                whereClause.append(" AND ");
            } else {
                whereClause.append(" ");
            }

            whereClause.append("(");

            for (int idType = 0; idType < forTypeAffiliation.length; ++idType) {
                if (idType > 0) {
                    whereClause.append(" OR ");
                }

                whereClause.append("MATTAF=");
                whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forTypeAffiliation[idType]));
            }

            whereClause.append(")");

            sqlWhere += whereClause.toString();
        }
        // Recherche par date de début
        if (!JadeStringUtil.isEmpty(getForDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADDEB=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
        }
        // Recherche par date de fin
        if (!JadeStringUtil.isEmpty(getForDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADFIN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin());
        }
        // Recherche par branche économique
        if (!JadeStringUtil.isEmpty(getForBranchBrancheEconomique())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MATBRA=" + this._dbWriteNumeric(statement.getTransaction(), getForBranchBrancheEconomique());
        }
        // Recherche par code noga
        if (!JadeStringUtil.isEmpty(getForCodeNoga())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MATCDN=" + this._dbWriteNumeric(statement.getTransaction(), getForCodeNoga());
        }
        // Recherche par type d'affiliation différent de
        if (!JadeStringUtil.isEmpty(getForNotTypeAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MATTAF<>" + this._dbWriteNumeric(statement.getTransaction(), getForNotTypeAffiliation());
        }
        if (getForActif()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADFIN=0";
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliation();
    }

    public void forEnvoiAutoAnnSal(Boolean boolean1) {
        forEnvoiAutoAnnSal = boolean1;
    }

    public void forIsTraitement(boolean newForIsTraitement) {
        forIsTraitement = new Boolean(newForIsTraitement);
    }

    public void forIsTraitement(Boolean newForIsTraitement) {
        forIsTraitement = newForIsTraitement;
    }

    public void forIsTraitement(String newForIsTraitement) {
        forIsTraitement = new Boolean(newForIsTraitement);
    }

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    public java.lang.String getForAffilieNumero() {
        return forAffilieNumero;
    }

    // ***********************************************
    // Getter
    // ***********************************************

    public String getForAffilieNumeroIn() {
        return forAffilieNumeroIn;
    }

    public java.lang.String getForAncienNumero() {
        return forAncienNumero;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForBranchBrancheEconomique() {
        return forBranchBrancheEconomique;
    }

    public String getForCodeNoga() {
        return forCodeNoga;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public java.lang.String getForDateDebutAffLowerOrEqualTo() {
        return forDateDebutAffLowerOrEqualTo;
    }

    public java.lang.String getForDateDebutGreaterOrEqualTo() {
        return forDateDebutGreaterOrEqualTo;
    }

    public java.lang.String getForDateDebutLowerOrEqualTo() {
        return forDateDebutLowerOrEqualTo;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public java.lang.String getForDeclarationSalaire() {
        return forDeclarationSalaire;
    }

    public java.lang.String getForEntreDate() {
        return forEntreDate;
    }

    public Boolean getForEnvoiAutoAnnSal() {
        return forEnvoiAutoAnnSal;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public Boolean getForIsNotReleveParitaire() {
        return forIsNotReleveParitaire;
    }

    public Boolean getForIsTraitement() {
        return forIsTraitement;
    }

    public String getForMotifFin() {
        return forMotifFin;
    }

    public String getForNotTypeAffiliation() {
        return forNotTypeAffiliation;
    }

    public java.lang.String getForPeriodicite() {
        return forPeriodicite;
    }

    public java.lang.String[] getForPeriodiciteIn() {
        return forPeriodiciteIn;
    }

    public java.lang.String getForTillDateDebut() {
        return forTillDateDebut;
    }

    public java.lang.String getForTillDateFin() {
        return forTillDateFin;
    }

    public String[] getForTypeAffiliation() {
        return forTypeAffiliation;
    }

    public java.lang.String getForTypeFacturation() {
        return forTypeFacturation;
    }

    public java.lang.String getForTypeNNSS() {
        return forTypeNNSS;
    }

    public String getFromAffilieNumero() {
        return fromAffilieNumero;
    }

    public boolean getForActif() {
        return forActif;
    }

    // ***********************************************
    // Setter
    // ***********************************************
    public void setForActif(boolean valeur) {
        forActif = valeur;
    }

    public String getFromDateCreation() {
        return fromDateCreation;
    }

    public java.lang.String getFromDateFin() {
        return fromDateFin;
    }

    public java.lang.String getFromDateFinNonVide() {
        return fromDateFinNonVide;
    }

    public String getFromDebutAffiliation() {
        return fromDebutAffiliation;
    }

    public java.lang.String getFromId() {
        return fromId;
    }

    public java.lang.String getFromNom() {
        return fromNom;
    }

    public java.lang.String getLikeAffilieNumero() {
        return likeAffilieNumero;
    }

    public java.lang.String getLikeAncienNumero() {
        return likeAncienNumero;
    }

    public String getNotInCodeFacturation() {
        return notInCodeFacturation;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public String getToAffilieNumero() {
        return toAffilieNumero;
    }

    public String getToDateCreation() {
        return toDateCreation;
    }

    public java.lang.String getToDateFin() {
        return toDateFin;
    }

    public java.lang.String getToDateNNSS() {
        return toDateNNSS;
    }

    public boolean isForNumeroIdeNotEmpty() {
        return forNumeroIdeNotEmpty;
    }

    public void setForAffiliationId(java.lang.String newForAffiliationId) {
        forAffiliationId = newForAffiliationId;
    }

    public void setForAffilieNumero(java.lang.String newForAffilieNumero) {
        forAffilieNumero = newForAffilieNumero;
    }

    public void setForAffilieNumeroIn(String forAffilieNumeroIn) {
        this.forAffilieNumeroIn = forAffilieNumeroIn;
    }

    public void setForAncienNumero(java.lang.String newForAncienNumero) {
        forAncienNumero = newForAncienNumero;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForBranchBrancheEconomique(String forBranchBrancheEconomique) {
        this.forBranchBrancheEconomique = forBranchBrancheEconomique;
    }

    public void setForCodeNoga(String forCodeNoga) {
        this.forCodeNoga = forCodeNoga;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateDebutAffLowerOrEqualTo(String newForDateDebutAffLowerOrEqualTo) {
        forDateDebutAffLowerOrEqualTo = newForDateDebutAffLowerOrEqualTo;
    }

    public void setForDateDebutGreaterOrEqualTo(String newforDateDebutGreaterOrEqualTo) {
        forDateDebutGreaterOrEqualTo = newforDateDebutGreaterOrEqualTo;
    }

    public void setForDateDebutLowerOrEqualTo(java.lang.String newForDateDebutLowerOrEqualTo) {
        forDateDebutLowerOrEqualTo = newForDateDebutLowerOrEqualTo;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDeclarationSalaire(java.lang.String newForDeclarationSalaire) {
        forDeclarationSalaire = newForDeclarationSalaire;
    }

    public void setForEntreDate(java.lang.String newForEntreDate) {
        forEntreDate = newForEntreDate;
    }

    public void setForIdTiers(java.lang.String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForIsNotReleveParitaire(Boolean forIsNotReleveParitaire) {
        this.forIsNotReleveParitaire = forIsNotReleveParitaire;
    }

    /**
     * Liste de types d'affiliations séparés par des virgules
     * 
     * @param listType
     */
    public void setForListTypeAffiliation(String listTypeAffiliation) {
        String[] typeAffiliations = listTypeAffiliation.split(",");
        setForTypeAffiliation(typeAffiliations);
    }

    public void setForMotifFin(String s) {
        forMotifFin = s;
    }

    public void setForNotTypeAffiliation(String forNotTypeAffiliation) {
        this.forNotTypeAffiliation = forNotTypeAffiliation;
    }

    public void setForNumeroIdeNotEmpty(boolean forNumeroIdeNotEmpty) {
        this.forNumeroIdeNotEmpty = forNumeroIdeNotEmpty;
    }

    public void setForPeriodicite(java.lang.String newForPeriodicite) {
        forPeriodicite = newForPeriodicite;
    }

    public void setForPeriodiciteIn(java.lang.String[] newForPeriodiciteIn) {
        forPeriodiciteIn = newForPeriodiciteIn;
    }

    public void setForTillDateDebut(java.lang.String newForTillDateDebut) {
        forTillDateDebut = newForTillDateDebut;
    }

    public void setForTillDateFin(java.lang.String newForTillDateFin) {
        forTillDateFin = newForTillDateFin;
    }

    public void setForTypeAffiliation(String[] strings) {
        forTypeAffiliation = strings;
    }

    public void setForTypeFacturation(java.lang.String newForTypeFacturation) {
        forTypeFacturation = newForTypeFacturation;
    }

    public void setForTypeNNSS(java.lang.String forTypeNNSS) {
        this.forTypeNNSS = forTypeNNSS;
    }

    /**
     * permet de filtrer sur les types d'affiliations paritaires (804002,804005,804010,804012)
     */
    public void setForTypesAffParitaires() {
        setForTypeAffiliation(AFAffiliationManager.AFFILIATION_TYPES_PARITAIRES);
    }

    /**
     * permet de filtrer sur les type d'affiliations personnelles (804001,804005,804011,804008,804004,804006)
     */
    public void setForTypesAffPersonelles() {
        setForTypeAffiliation(AFAffiliationManager.AFFILIATION_TYPES_PERSONNELLES);
    }

    public void setFromAffilieNumero(String newFromAffilieNumero) {
        fromAffilieNumero = newFromAffilieNumero;
    }

    public void setFromDateCreation(String newFromDateCreation) {
        fromDateCreation = newFromDateCreation;
    }

    public void setFromDateFin(java.lang.String string) {
        fromDateFin = string;
    }

    public void setFromDateFinNonVide(java.lang.String string) {
        fromDateFinNonVide = string;
    }

    public void setFromDebutAffiliation(String fromDebutAffiliation) {
        this.fromDebutAffiliation = fromDebutAffiliation;
    }

    public void setFromId(java.lang.String newFromId) {
        fromId = newFromId;
    }

    public void setFromNom(java.lang.String newFromNom) {
        fromNom = newFromNom;
    }

    public void setLikeAffilieNumero(java.lang.String newLikeAffilieNumero) {
        likeAffilieNumero = newLikeAffilieNumero;
    }

    public void setLikeAncienNumero(java.lang.String likeAncienNumero) {
        this.likeAncienNumero = likeAncienNumero;
    }

    public void setNotInCodeFacturation(String notInCodeFacturation) {
        this.notInCodeFacturation = notInCodeFacturation;
    }

    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    public void setOrderBy(int orderBy) {
        if (orderBy == AFAffiliationManager.ORDER_AFFILIENUMERO) {
            order = "MALNAF";
        } else if (orderBy == AFAffiliationManager.ORDER_AFFILIDATIONID) {
            order = "MAIAFF";
        }
    }

    public void setToAffilieNumero(String newToAffilieNumero) {
        toAffilieNumero = newToAffilieNumero;
    }

    public void setToDateCreation(String newToDateCreation) {
        toDateCreation = newToDateCreation;
    }

    public void setToDateFin(java.lang.String string) {
        toDateFin = string;
    }

    public void setToDateNNSS(java.lang.String toDateNNSS) {
        this.toDateNNSS = toDateNNSS;
    }

    public java.lang.String getForDateFinGreater() {
        return forDateFinGreater;
    }

    public void setForDateFinGreater(java.lang.String forDateFinGreater) {
        this.forDateFinGreater = forDateFinGreater;
    }

}
