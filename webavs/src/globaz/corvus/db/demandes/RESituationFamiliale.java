/*
 * Créé le 5 juil. 07
 */

package globaz.corvus.db.demandes;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.INSSViewBean;
import globaz.prestation.interfaces.util.nss.PRUtil;
import java.util.Vector;

/**
 * @author HPE
 * 
 */

public class RESituationFamiliale extends BEntity implements INSSViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_IDTIERS = 1;

    public static final int ALTERNATE_KEY_NOMPRENOMDATENAI = 2;

    /** DOCUMENT ME! */
    public static final String FIELD_AVS_IDTIERS = "HTITIE";

    /** DOCUMENT ME! */
    public static final String FIELD_AVS_NOAVS = "HXNAVS";

    /** DOCUMENT ME! */
    public static final String FIELD_CANTON = "WGTCDO";

    public static final String FIELD_CS_DOMAINE = "WGTDOA";

    /** DOCUMENT ME! */
    public static final String FIELD_DATEDECES = "WGDDEC";

    /** DOCUMENT ME! */
    public static final String FIELD_DATENAISSANCE = "WGDNAI";

    /** DOCUMENT ME! */
    public static final String FIELD_IDMEMBREFAMILLE = "WGIMEF";

    /** DOCUMENT ME! */
    public static final String FIELD_IDTIERS = "WGITIE";

    /** DOCUMENT ME! */
    public static final String FIELD_NATIONALITE = "WGTNAT";

    /** DOCUMENT ME! */
    public static final String FIELD_NOM = "WGLNOM";

    /** DOCUMENT ME! */
    public static final String FIELD_NOM_FOR_SEARCH = "WGLNOU";

    /** DOCUMENT ME! */
    public static final String FIELD_PAYS = "WGTPDO";

    /** DOCUMENT ME! */
    public static final String FIELD_PERS_CANTON = "HPTCAN";

    /** DOCUMENT ME! */
    public static final String FIELD_PERS_DATEDEC = "HPDDEC";

    /** DOCUMENT ME! */
    public static final String FIELD_PERS_DATENAI = "HPDNAI";

    /** DOCUMENT ME! */
    public static final String FIELD_PERS_IDTIERS = "HTITIE";

    /** DOCUMENT ME! */
    public static final String FIELD_PERS_SEX = "HPTSEX";

    /** DOCUMENT ME! */
    public static final String FIELD_PRENOM = "WGLPRE";

    /** DOCUMENT ME! */
    public static final String FIELD_PRENOM_FOR_SEARCH = "WGLPRU";

    /** DOCUMENT ME! */
    public static final String FIELD_SEX = "WGTSEX";

    /** DOCUMENT ME! */
    public static final String FIELD_TI_IDTIERS = "HTITIE";

    /** DOCUMENT ME! */
    public static final String FIELD_TI_NOM = "HTLDE1";

    /** DOCUMENT ME! */
    public static final String FIELD_TI_NOM_FOR_SEARCH = "HTLDU1";

    /** DOCUMENT ME! */
    public static final String FIELD_TI_PAYS = "HNIPAY";

    /** DOCUMENT ME! */
    public static final String FIELD_TI_PRENOM = "HTLDE2";

    /** DOCUMENT ME! */
    public static final String FIELD_TI_PRENOM_FOR_SEARCH = "HTLDU2";

    /** DOCUMENT ME! */
    public static final String TABLE_AVS = "TIPAVSP";

    /** DOCUMENT ME! */
    public static final String TABLE_AVS_HIST = "TIHAVSP";

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "SFMBRFAM";

    /** DOCUMENT ME! */
    public static final String TABLE_PERS = "TIPERSP";

    /** DOCUMENT ME! */
    public static final String TABLE_RELATION_FAMILIALE = "SFREFARE";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    /** DOCUMENT ME! */
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Effectue une jointure sur les tiers pour être synchro avec le nom, prenom, numAvs des tiers
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {
        return RESituationFamiliale.createFromClause(schema, RESituationFamiliale.TABLE_NAME,
                RESituationFamiliale.TABLE_TIERS, RESituationFamiliale.TABLE_PERS, RESituationFamiliale.TABLE_AVS);
    }

    public static String createFromClause(String schema, String aliasMembre, String aliasTiTiers, String aliasTiPers,
            String aliasTiAvs) {
        String createFrom = schema + RESituationFamiliale.TABLE_NAME + " AS " + aliasMembre + " LEFT JOIN " + schema
                + RESituationFamiliale.TABLE_TIERS + " AS " + aliasTiTiers + " ON (" + aliasMembre + "."
                + RESituationFamiliale.FIELD_IDTIERS + " = " + aliasTiTiers + "."
                + RESituationFamiliale.FIELD_TI_IDTIERS + ")" + " LEFT JOIN " + schema
                + RESituationFamiliale.TABLE_PERS + " AS " + aliasTiPers + " ON (" + aliasMembre + "."
                + RESituationFamiliale.FIELD_IDTIERS + " = " + aliasTiPers + "."
                + RESituationFamiliale.FIELD_PERS_IDTIERS + ")"

                + " LEFT JOIN " + schema + RESituationFamiliale.TABLE_AVS + " AS " + aliasTiAvs + " ON (" + aliasMembre
                + "." + RESituationFamiliale.FIELD_IDTIERS + " = " + aliasTiAvs + "."
                + RESituationFamiliale.FIELD_AVS_IDTIERS + ")";

        return createFrom;
    }

    private String csCantonDomicile = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    // checkAVS vérifie aussi la présence du n°AVS
    private String dateNaissance = "";
    private String idAssure = "";
    private String idMembreFamille = "";
    private String idTiers = "";
    private String nom = "";
    private String nss = ""; // initialisé pour pas que le checkAVS braille :
    private String pays = "";
    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String provenance = "";

    private String relationAuRequerant = "";

    /**
     * DOCUMENT ME!
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdMembreFamille(this._incCounter(transaction, "0"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return RESituationFamiliale.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMembreFamille = statement.dbReadNumeric(RESituationFamiliale.FIELD_IDMEMBREFAMILLE);
        idTiers = statement.dbReadNumeric(RESituationFamiliale.FIELD_IDTIERS);

        if (!isLoadedFromManager() && !JadeStringUtil.isIntegerEmpty(idTiers)) {
            // load from Tiers
            PRTiersWrapper tw = PRTiersHelper.getTiersAdresseDomicileParId(getSession(), getIdTiers(),
                    JACalendar.todayJJsMMsAAAA());
            nom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM);
            prenom = tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
            dateDeces = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);
            csSexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);
            csCantonDomicile = tw.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);
            if (JadeStringUtil.isIntegerEmpty(csCantonDomicile)) {
                csCantonDomicile = statement.dbReadNumeric(RESituationFamiliale.FIELD_CANTON);
            }
            // Nationalite
            pays = tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE);
            csNationalite = tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS);

        } else {

            if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                nom = statement.dbReadString(RESituationFamiliale.FIELD_NOM);
                prenom = statement.dbReadString(RESituationFamiliale.FIELD_PRENOM);
                // nSS = statement.dbReadString(FIELD_NOAVS);
                dateNaissance = statement.dbReadDateAMJ(RESituationFamiliale.FIELD_DATENAISSANCE);
                dateDeces = statement.dbReadDateAMJ(RESituationFamiliale.FIELD_DATEDECES);
                csSexe = statement.dbReadNumeric(RESituationFamiliale.FIELD_SEX);
                csCantonDomicile = statement.dbReadNumeric(RESituationFamiliale.FIELD_CANTON);
                pays = statement.dbReadNumeric(RESituationFamiliale.FIELD_PAYS);
                csNationalite = statement.dbReadNumeric(RESituationFamiliale.FIELD_NATIONALITE);
            } else { // on va rechercher dans les tables tiers
                nom = statement.dbReadString(RESituationFamiliale.FIELD_TI_NOM);
                prenom = statement.dbReadString(RESituationFamiliale.FIELD_TI_PRENOM);
                nss = statement.dbReadString(RESituationFamiliale.FIELD_AVS_NOAVS);
                dateNaissance = statement.dbReadDateAMJ(RESituationFamiliale.FIELD_PERS_DATENAI);
                dateDeces = statement.dbReadDateAMJ(RESituationFamiliale.FIELD_PERS_DATEDEC);
                csSexe = statement.dbReadNumeric(RESituationFamiliale.FIELD_PERS_SEX);
                csCantonDomicile = statement.dbReadNumeric(RESituationFamiliale.FIELD_PERS_CANTON);
                if (JadeStringUtil.isIntegerEmpty(csCantonDomicile)) {
                    csCantonDomicile = statement.dbReadNumeric(RESituationFamiliale.FIELD_CANTON);
                }
                pays = statement.dbReadNumeric(RESituationFamiliale.FIELD_TI_PAYS);
                // csNationalite = statement.dbReadNumeric(FIELD_PERS_SEX);
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RESituationFamiliale.FIELD_IDMEMBREFAMILLE,
                this._dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    /**
     * DOCUMENT ME!
     * 
     * @return Le Cs du canton de domicile ou le Cs du canton de la caisse (si vide)
     */
    @Override
    public String getCsCantonDomicile() {
        if (JadeStringUtil.isEmpty(csCantonDomicile)) {
            try {
                csCantonDomicile = CaisseHelperFactory.getInstance().getCsDefaultCantonCaisse(
                        getSession().getApplication());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return csCantonDomicile;
    }

    /**
     * Getter sans effet
     */
    @Override
    public String getCsEtatCivil() {
        return null;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    @Override
    public String getCsNationalite() {
        return csNationalite;
    }

    /**
     * getter pour l'attribut sex
     * 
     * @return la valeur courante de l'attribut sex
     */
    @Override
    public String getCsSexe() {
        return csSexe;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    @Override
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * getter pour l'attribut date naissance
     * 
     * @return la valeur courante de l'attribut date naissance
     */
    @Override
    public String getDateNaissance() {
        return dateNaissance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getIdAssure()
     */
    @Override
    public String getIdAssure() {
        return idAssure;
    }

    /**
     * getter pour l'attribut id membre famille
     * 
     * @return la valeur courante de l'attribut id membre famille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        if (JadeStringUtil.isEmpty(idTiers) && PRUtil.PROVENANCE_TIERS.equals(provenance)) {
            idTiers = idAssure;
        }
        return idTiers;
    }

    public String getLibellePays() {
        return getSession().getCodeLibelle(
                getSession().getSystemCode("CIPAYORI", JadeStringUtil.isEmpty(csNationalite) ? pays : csNationalite));
    }

    /**
     * getter pour l'attribut libelleSexe
     * 
     */
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(csSexe);
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    @Override
    public String getNom() {
        return nom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getNss()
     */
    @Override
    public String getNss() {
        return nss;
    }

    /**
     * getter pour l'attribut no avs
     * 
     * @return la valeur courante de l'attribut no avs
     */
    public String getNSS() {
        return nss;
    }

    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getPays() {
        return pays;
    }

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    @Override
    public String getPrenom() {
        return prenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getProvenance()
     */
    @Override
    public String getProvenance() {
        return provenance;
    }

    /**
     * ATTENTION: La relation n'est jamais calculée, mais retourne uniquement la valeur settée
     */
    public String getRelationAuRequerant() {
        return relationAuRequerant;
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public String getVisibleNoAvs() {
        return JAStringFormatter.unFormatAVS(JadeStringUtil.isEmpty(nss) ? "00000000000" : nss, "");
    }

    /**
     * @return
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }

        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    @Override
    public void setCsCantonDomicile(String string) {
        csCantonDomicile = string;
    }

    /**
     * Setter sans effet
     */
    @Override
    public void setCsEtatCivil(String s) {
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    @Override
    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    /**
     * setter pour l'attribut sex
     * 
     * @param sex
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsSexe(String sex) {
        csSexe = sex;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    @Override
    public void setDateDeces(String string) {
        dateDeces = string;
    }

    /**
     * setter pour l'attribut date naissance
     * 
     * @param dateNaissance
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setIdAssure(java.lang .String)
     */
    @Override
    public void setIdAssure(String string) {
        idAssure = string;
        if (PRUtil.PROVENANCE_TIERS.equals(getProvenance())) {
            idTiers = string;
        }
    }

    /**
     * setter pour l'attribut id membre famille
     * 
     * @param idMembreFamille
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param nom
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setNss(java.lang.String )
     */
    @Override
    public void setNss(String string) {
        nss = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setPays(String string) {
        pays = string;
    }

    /**
     * setter pour l'attribut prenom
     * 
     * @param prenom
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setProvenance(java .lang.String)
     */
    @Override
    public void setProvenance(String string) {
        provenance = string;
    }

    public String setRelationAuRequerant(String rel) {
        return relationAuRequerant = rel;
    }

}