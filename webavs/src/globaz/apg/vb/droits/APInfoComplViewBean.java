/*
 * Créé le 11 juil. 06
 */
package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.prestation.vb.PRInfoComplViewBean;

/**
 * @author hpe
 * 
 */
public class APInfoComplViewBean extends PRInfoComplViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_CAISSE = new Object[] { new String[] { "idTiersCaisse", "idTiers" },
            new String[] { "nomCaisse", "nom" } };

    private String dateFinPrononce = "";
    private String dateTransferPrononce = "";
    private String destination = "";
    private String genreService = "";
    private String idPrononce = "";
    private boolean isBaseIndAfterEnd = false;
    private String noAVS = "";
    private String nomCaisse = "";
    private boolean retourDepuisPyxis = false;

    public APInfoComplViewBean() {
        super();
        setTypeInfoCompl(IAPDroitLAPG.CS_TRANSFER_DOSSIER);
    }

    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * @return
     */
    public String getDateFinPrononce() {
        return dateFinPrononce;
    }

    /**
     * @return
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVS());

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

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVS(), tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getGenreService() {
        return genreService;
    }

    /**
     * @return
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    /**
     * getter pour l'attribut methodes selecteur employeur
     * 
     * @return la valeur courante de l'attribut methodes selecteur employeur
     */
    public Object[] getMethodesSelecteurCaisse() {
        return METHODES_SEL_CAISSE;
    }

    /**
     * @return
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * @return
     */
    public String getNomCaisse() {
        return nomCaisse;
    }

    /**
     * @return
     */
    public boolean isBaseIndAfterEnd() {
        return isBaseIndAfterEnd;
    }

    /**
     * @return
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * @param b
     */
    public void setBaseIndAfterEnd(boolean b) {
        isBaseIndAfterEnd = b;
    }

    /**
     * @param string
     */
    public void setDateFinPrononce(String string) {
        dateFinPrononce = string;
    }

    /**
     * @param string
     */
    public void setDestination(String string) {
        destination = string;
    }

    /**
     * @param string
     */
    public void setGenreService(String string) {
        genreService = string;
    }

    /**
     * @param string
     */
    public void setIdPrononce(String string) {
        idPrononce = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.infos.PRInfoCompl#setIdTiersCaisse(java.lang.String)
     */
    @Override
    public void setIdTiersCaisse(String string) {
        super.setIdTiersCaisse(string);
        retourDepuisPyxis = true;
    }

    /**
     * @param string
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * @param string
     */
    public void setNomCaisse(String string) {
        nomCaisse = string;
    }

    /**
     * @param b
     */
    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

}
