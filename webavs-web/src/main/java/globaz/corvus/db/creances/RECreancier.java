/*
 * Créé le 18 juil. 07
 */

package globaz.corvus.db.creances;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * @author BSC
 * 
 */
public class RECreancier extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_CS_ETAT = "YSTETA";

    public static final String FIELDNAME_CS_TYPE = "YSTTYP";
    public static final String FIELDNAME_DATE_DEBUT_PER_REV = "YSDDPR";
    public static final String FIELDNAME_DATE_FIN_PER_REV = "YSDFPR";
    public static final String FIELDNAME_ID_AFFILIE_ADR_PMT = "YSIAAP";
    public static final String FIELDNAME_ID_CREANCIER = "YSICRE";
    public static final String FIELDNAME_ID_DEMANDE_RENTE = "YSIDRE";
    public static final String FIELDNAME_ID_DOMAINE_APPLICATIF = "YSIDOA";
    // pas utilise
    // public static final String FIELDNAME_ID_RETENUE = "YSIRET";
    public static final String FIELDNAME_ID_TIERS = "YSITIE";
    public static final String FIELDNAME_ID_TIERS_ADR_PMT = "YSITAP";
    public static final String FIELDNAME_ID_TIERS_REGROUPEMENT = "YSITRG";
    public static final String FIELDNAME_IS_BLOQUE = "YSBBLO";
    public static final String FIELDNAME_MONTANT_REVANDIQUE = "YSMREV";
    public static final String FIELDNAME_REVENU_ANNUEL_DETERMINANT = "YSRAND";
    public static final String FIELDNAME_TAUX_IMPOSITION = "YSRTIM";
    public static final String FIELDNAME_REF_PAIEMENT = "YSLRPA";
    public static final String TABLE_NAME_CREANCIER = "RECREAN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtat = "";
    private String csType = "";
    private String dateDebutPerRev = "";
    private String dateFinPerRev = "";
    private String idAffilieAdressePmt = "";
    private String idCreancier = "";
    private String idDemandeRente = "";
    private String idDomaineApplicatif = "";
    // pas utilise
    // private String idRetenue = "";
    private String idTiers = "";
    private String idTiersAdressePmt = "";
    private String idTiersRegroupement = "";
    private Boolean isBloque = Boolean.FALSE;
    private String montantRevandique = "";
    private String revenuAnnuelDeterminant = "";
    private String tauxImposition = "";
    private String refPaiement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCreancier(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_CREANCIER;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idCreancier = statement.dbReadNumeric(FIELDNAME_ID_CREANCIER);
        idDemandeRente = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE);
        idTiersAdressePmt = statement.dbReadNumeric(FIELDNAME_ID_TIERS_ADR_PMT);
        idAffilieAdressePmt = statement.dbReadNumeric(FIELDNAME_ID_AFFILIE_ADR_PMT);
        idDomaineApplicatif = statement.dbReadNumeric(FIELDNAME_ID_DOMAINE_APPLICATIF);
        montantRevandique = statement.dbReadNumeric(FIELDNAME_MONTANT_REVANDIQUE);
        revenuAnnuelDeterminant = statement.dbReadNumeric(FIELDNAME_REVENU_ANNUEL_DETERMINANT);
        tauxImposition = statement.dbReadNumeric(FIELDNAME_TAUX_IMPOSITION);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
        csType = statement.dbReadNumeric(FIELDNAME_CS_TYPE);
        dateDebutPerRev = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT_PER_REV);
        dateFinPerRev = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN_PER_REV);
        refPaiement = statement.dbReadString(FIELDNAME_REF_PAIEMENT);
        idTiersRegroupement = statement.dbReadNumeric(FIELDNAME_ID_TIERS_REGROUPEMENT);
        isBloque = statement.dbReadBoolean(FIELDNAME_IS_BLOQUE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_CREANCIER,
                _dbWriteNumeric(statement.getTransaction(), idCreancier, "idCreancier"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_CREANCIER,
                _dbWriteNumeric(statement.getTransaction(), idCreancier, "idCreancier"));
        statement.writeField(FIELDNAME_ID_DEMANDE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
        statement.writeField(FIELDNAME_ID_TIERS_ADR_PMT,
                _dbWriteNumeric(statement.getTransaction(), idTiersAdressePmt, "idTiersAdressePmt"));
        statement.writeField(FIELDNAME_ID_AFFILIE_ADR_PMT,
                _dbWriteNumeric(statement.getTransaction(), idAffilieAdressePmt, "idAffilieAdressePmt"));
        statement.writeField(FIELDNAME_ID_DOMAINE_APPLICATIF,
                _dbWriteNumeric(statement.getTransaction(), idDomaineApplicatif, "idDomaineApplicatif"));
        statement.writeField(FIELDNAME_MONTANT_REVANDIQUE,
                _dbWriteNumeric(statement.getTransaction(), montantRevandique, "montantRevandique"));
        statement.writeField(FIELDNAME_REVENU_ANNUEL_DETERMINANT,
                _dbWriteNumeric(statement.getTransaction(), revenuAnnuelDeterminant, "revenuAnnuelDeterminant"));
        statement.writeField(FIELDNAME_TAUX_IMPOSITION,
                _dbWriteNumeric(statement.getTransaction(), tauxImposition, "tauxImposition"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(FIELDNAME_CS_TYPE, _dbWriteNumeric(statement.getTransaction(), csType, "csType"));
        statement.writeField(FIELDNAME_DATE_DEBUT_PER_REV,
                _dbWriteDateAMJ(statement.getTransaction(), dateDebutPerRev, "dateDebutPerRev"));
        statement.writeField(FIELDNAME_DATE_FIN_PER_REV,
                _dbWriteDateAMJ(statement.getTransaction(), dateFinPerRev, "dateFinPerRev"));
        statement.writeField(FIELDNAME_REF_PAIEMENT,
                _dbWriteString(statement.getTransaction(), refPaiement, "refPaiement"));
        statement.writeField(FIELDNAME_ID_TIERS_REGROUPEMENT,
                _dbWriteNumeric(statement.getTransaction(), idTiersRegroupement, "idTiersRegroupement"));
        statement.writeField(FIELDNAME_IS_BLOQUE,
                _dbWriteBoolean(statement.getTransaction(), isBloque, BConstants.DB_TYPE_BOOLEAN_CHAR, "isBloque"));
    }

    /**
     * @return
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return
     */
    public String getCsType() {
        return csType;
    }

    /**
     * @return
     */
    public String getDateDebutPerRev() {
        return dateDebutPerRev;
    }

    /**
     * @return
     */
    public String getDateFinPerRev() {
        return dateFinPerRev;
    }

    public String getIdAffilieAdressePmt() {
        return idAffilieAdressePmt;
    }

    /**
     * @return
     */
    public String getIdCreancier() {
        return idCreancier;
    }

    /**
     * @return
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    /**
     * @return
     */
    public String getIdTiersRegroupement() {
        return idTiersRegroupement;
    }

    /**
     * @return
     */
    public Boolean getIsBloque() {
        return isBloque;
    }

    /**
     * @return
     */
    public String getMontantRevandique() {
        return montantRevandique;
    }

    /**
     * @return
     */
    public String getTauxImposition() {
        return tauxImposition;
    }

    /**
     * @return
     */
    public String getRevenuAnnuelDeterminant() {
        return revenuAnnuelDeterminant;
    }

    /**
     * @return
     */
    public String getRefPaiement() {
        return refPaiement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * setter pour l'attribut adresse paiement.
     * 
     * @param adressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setAdressePaiement(TIAdressePaiementData adressePaiement) {
        if (adressePaiement != null) {
            idTiersAdressePmt = adressePaiement.getIdTiers();
            idDomaineApplicatif = adressePaiement.getIdApplication();
        } else {
            idTiersAdressePmt = "";
            idDomaineApplicatif = "";
        }
    }

    /**
     * @param string
     */
    public void setCsEtat(String string) {
        csEtat = string;
    }

    /**
     * @param string
     */
    public void setCsType(String string) {
        csType = string;
    }

    /**
     * @param string
     */
    public void setDateDebutPerRev(String string) {
        dateDebutPerRev = string;
    }

    /**
     * @param string
     */
    public void setDateFinPerRev(String string) {
        dateFinPerRev = string;
    }

    public void setIdAffilieAdressePmt(String idAffilieAdressePmt) {
        this.idAffilieAdressePmt = idAffilieAdressePmt;
    }

    /**
     * @param string
     */
    public void setIdCreancier(String string) {
        idCreancier = string;
    }

    /**
     * @param string
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIdDomaineApplicatif(String string) {
        idDomaineApplicatif = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setIdTiersAdressePmt(String string) {
        idTiersAdressePmt = string;
    }

    /**
     * @param string
     */
    public void setIdTiersRegroupement(String string) {
        idTiersRegroupement = string;
    }

    /**
     * @param boolean1
     */
    public void setIsBloque(Boolean boolean1) {
        isBloque = boolean1;
    }

    /**
     * @param string
     */
    public void setMontantRevandique(String string) {
        montantRevandique = string;
    }

    /**
     * @param revenuAnnuelDeterminant
     */
    public void setRevenuAnnuelDeterminant(String revenuAnnuelDeterminant) {
        this.revenuAnnuelDeterminant = revenuAnnuelDeterminant;
    }

    /**
     * @param tauxImposition
     */
    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    /**
     * @param string
     */
    public void setRefPaiement(String string) {
        refPaiement = string;
    }

}
