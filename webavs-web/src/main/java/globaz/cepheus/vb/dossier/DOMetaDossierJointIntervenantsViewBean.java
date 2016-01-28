/*
 * Créé le 20 oct. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.vb.dossier;

import globaz.cepheus.db.dossier.DOMetaDossierJointIntervenants;
import globaz.cepheus.db.intervenant.DOIntervenant;
import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOMetaDossierJointIntervenantsViewBean extends DOMetaDossierJointIntervenants implements
        FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** L'id d'un tiers */
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    /** Le nom d'un tiers */
    public static final String FIELDNAME_NOM = "HTLDE1";
    /** Le numero AVS */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    /** Le pernom d'un tiers */
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    private static final Object[] METHODES_SEL_INTERVENANT = new Object[] {
            new String[] { "idTiersIntervenant", "idTiers" }, new String[] { "nomIntervenantRetourPyxis", "nom" } };
    /** Table des personnes AVS */
    public static final String TABLE_AVS = "TIPAVSP";

    /** Table des tiers */
    public static final String TABLE_TIERS = "TITIERP";
    private String csTypeDemande = "";
    private String detailIntervenant = "";

    private String idTiersMetaDossier = "";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private String noAvsIntervenant = "";
    private String noAvsTiersMetaDossier = "";
    // pour l'affichage dans la liste
    private String nomIntervenant = "";
    // pour memoriser les parametres de recherche
    private String nomTiersMetaDossier = "";

    private String prenomIntervenant = "";
    private String prenomTiersMetaDossier = "";

    private boolean retourFromPyxis = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        noAvsIntervenant = NSUtil.formatAVSUnknown(statement.dbReadString(FIELDNAME_NUM_AVS));
        nomIntervenant = statement.dbReadString(FIELDNAME_NOM);
        prenomIntervenant = statement.dbReadString(FIELDNAME_PRENOM);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    public String createFromClause(String schema) {

        StringBuffer fromClause = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClause.append(super.createFromClause(schema));

        // jointure entre table des demandes et table des tiers
        fromClause.append(innerJoin);
        fromClause.append(schema);
        fromClause.append(TABLE_TIERS);
        fromClause.append(on);
        fromClause.append(schema);
        fromClause.append(DOIntervenant.TABLE_INTRVENANTS);
        fromClause.append(point);
        fromClause.append(DOIntervenant.FIELDNAME_ID_TIERS);
        fromClause.append(egal);
        fromClause.append(schema);
        fromClause.append(TABLE_TIERS);
        fromClause.append(point);
        fromClause.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des numeros AVS
        fromClause.append(innerJoin);
        fromClause.append(schema);
        fromClause.append(TABLE_AVS);
        fromClause.append(on);
        fromClause.append(schema);
        fromClause.append(DOIntervenant.TABLE_INTRVENANTS);
        fromClause.append(point);
        fromClause.append(DOIntervenant.FIELDNAME_ID_TIERS);
        fromClause.append(egal);
        fromClause.append(schema);
        fromClause.append(TABLE_AVS);
        fromClause.append(point);
        fromClause.append(FIELDNAME_ID_TIERS_TI);

        return fromClause.toString();
    }

    /**
	 *
	 */
    public String getCsDescriptionLibelle() {
        return getSession().getCodeLibelle(getCsDescription());
    }

    /**
     * @return
     */
    public String getCsTypeDemande() {
        return csTypeDemande;
    }

    /**
     * @return
     */
    public String getDetailIntervenant() {
        return detailIntervenant;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerant() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAvsIntervenant());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantListe(getNoAvsIntervenant(), getNomPrenomIntervenant(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getIdTiersMetaDossier() {
        return idTiersMetaDossier;
    }

    /**
     * getter pour l'attribut methodes selecteur intervenant
     * 
     * @return la valeur courante de l'attribut methodes selecteur intervenant
     */
    public Object[] getMethodesSelecteurIntervenant() {
        return METHODES_SEL_INTERVENANT;
    }

    /**
     * @return
     */
    public String getNoAvsIntervenant() {
        return noAvsIntervenant;
    }

    /**
     * @return
     */
    public String getNoAvsTiersMetaDossier() {
        return noAvsTiersMetaDossier;
    }

    /**
     * @return
     */
    public String getNomIntervenant() {
        return nomIntervenant;
    }

    public String getNomPrenomIntervenant() {
        return nomIntervenant + " " + prenomIntervenant;
    }

    /**
     * @return
     */
    public String getNomTiersMetaDossier() {
        return nomTiersMetaDossier;
    }

    /**
     * @return
     */
    public String getPrenomIntervenant() {
        return prenomIntervenant;
    }

    /**
     * @return
     */
    public String getPrenomTiersMetaDossier() {
        return prenomTiersMetaDossier;
    }

    /**
     * @return
     */
    public boolean isRetourFromPyxis() {
        return retourFromPyxis;
    }

    /**
     * @param string
     */
    public void setCsTypeDemande(String string) {
        csTypeDemande = string;
    }

    /**
     * @param string
     */
    public void setDetailIntervenant(String string) {
        detailIntervenant = string;
    }

    /**
     * @param string
     */
    public void setIdTiersMetaDossier(String string) {
        idTiersMetaDossier = string;
    }

    /**
     * @param string
     */
    public void setNoAvsIntervenant(String string) {
        noAvsIntervenant = string;
    }

    /**
     * @param string
     */
    public void setNoAvsTiersMetaDossier(String string) {
        noAvsTiersMetaDossier = string;
    }

    /**
     * @param string
     */
    public void setNomIntervenant(String string) {
        nomIntervenant = string;
    }

    /**
     * @return
     */
    public void setNomIntervenantRetourPyxis(String string) {
        setNomIntervenant(string);
        setRetourFromPyxis(true);
    }

    /**
     * @param string
     */
    public void setNomTiersMetaDossier(String string) {
        nomTiersMetaDossier = string;
    }

    /**
     * @param string
     */
    public void setPrenomIntervenant(String string) {
        prenomIntervenant = string;
    }

    /**
     * @param string
     */
    public void setPrenomTiersMetaDossier(String string) {
        prenomTiersMetaDossier = string;
    }

    /**
     * @param b
     */
    public void setRetourFromPyxis(boolean b) {
        retourFromPyxis = b;
    }

}
