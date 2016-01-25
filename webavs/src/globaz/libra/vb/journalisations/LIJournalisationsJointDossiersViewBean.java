package globaz.libra.vb.journalisations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.libra.db.journalisations.LIJournalisationsJointDossiers;
import globaz.libra.interfaces.ILIConstantes;
import globaz.libra.vb.dossiers.LIDossiersViewBean;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class LIJournalisationsJointDossiersViewBean extends LIJournalisationsJointDossiers implements
        FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_DOSSIER = new Object[] { new String[] { "idDossierSelector", "idDossier" } };

    private static final Object[] METHODES_SEL_TIERS = new Object[] { new String[] { "idTiersDepuisPyxis", "idTiers" } };

    private static final Object[] METHODES_SEL_USER = new Object[] { new String[] { "idUserSelector",
            "idUtilisateurExterne" } };

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public static final String getLibelleCourtSexe(String csSexe, BSession session) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public static final String getLibellePays(String csNationalite, BSession session) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", csNationalite));
        }

    }

    private LIDossiersViewBean dossier = new LIDossiersViewBean();

    private String idDossierSelector = "";

    private String idTiersDepuisPyxis = "";
    private String idUser = new String();

    private String idUserSelector = "";

    private transient Vector orderBy = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    Map sacUser = new HashMap();

    private PRTiersWrapper tiers;

    /**
     * Méthode qui retourne le détail du tiers formaté
     * 
     * @return le détail du tiers formaté
     * @throws Exception
     */
    public String getDetailTiers() throws Exception {

        return PRNSSUtil.formatDetailRequerantListeCourt(getNoAVS(), getNom() + " " + getPrenom(), getDateNaissance());

    }

    public LIDossiersViewBean getDossier() throws Exception {
        return dossier;
    }

    public String getIdDossierSelector() {
        return idDossierSelector;
    }

    public String getIdTiersDepuisPyxis() {
        return idTiersDepuisPyxis;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdUserSelector() {
        return idUserSelector;
    }

    /**
     * getter pour l'attribut methodes selection des dossiers.
     * 
     * @return la valeur courante de l'attribut methodes selection des dossiers
     */
    public Object[] getMethodesSelecteurDossier() {
        return METHODES_SEL_DOSSIER;
    }

    /**
     * getter pour l'attribut methodes selection copie.
     * 
     * @return la valeur courante de l'attribut methodes selection copie
     */
    public Object[] getMethodesSelecteurTiers() {
        return METHODES_SEL_TIERS;
    }

    /**
     * getter pour l'attribut methodes selection des utilisateurs.
     * 
     * @return la valeur courante de l'attribut methodes selection des utilisateurs
     */
    public Object[] getMethodesSelecteurUser() {
        return METHODES_SEL_USER;
    }

    public Vector getOrderByData() {
        orderBy = new Vector(4);
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_DATE, "Date" });
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_DOMAINE, "Domaine" });
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_TYPE, "Type" });
        orderBy.add(new String[] { ILIConstantes.ECHEANCES_TRI_UTILISATEUR, "Utilisateur" });
        return orderBy;
    }

    public Map getSacUser() {
        return sacUser;
    }

    public PRTiersWrapper getTiers() {
        return tiers;
    }

    public void loadDossier() throws Exception {
        dossier.setSession(getSession());
        dossier.setIdDossier(getIdDossier());
        dossier.retrieve();
    }

    public void loadTiers() throws Exception {
        tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiers());
    }

    public void setIdDossierSelector(String idDossierSelector) {
        this.idDossierSelector = idDossierSelector;
    }

    public void setIdTiersDepuisPyxis(String idTiersDepuisPyxis) {
        this.idTiersDepuisPyxis = idTiersDepuisPyxis;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdUserSelector(String idUserSelector) {
        this.idUserSelector = idUserSelector;
    }

    public void setSacUser(Map sacUser) {
        this.sacUser = sacUser;
    }

}
