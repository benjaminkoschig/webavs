package globaz.apg.vb.droits;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APRecapitulatifDroitPai;
import globaz.apg.servlet.IAPActions;
import globaz.globall.api.GlobazSystem;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.api.PRTypeDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRUserUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import lombok.Getter;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Getter
public class APRecapitulatifDroitPaiViewBean extends APRecapitulatifDroitPai implements APRecapitulatifDroitViewBean {

    private static final long serialVersionUID = 1L;

    private boolean afficherBoutonSimulerPmtBPID;
    /**
     * Utilisé dans le cas ou l'on veut recréer une annonce liée à une prestation suite à une reprise de données Permet
     * d'insérer le BPID voulut pour l'annonce qui sera créé
     */
    private String pidAnnonce;

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     *
     * @return le détail du requérant formaté
     *
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVS());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                                      .getCode(
                                              getSession().getSystemCode(
                                                      "CIPAYORI",
                                                      tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode(
                                "CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVS(), getNomPrenom(),
                                                         tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                                                         getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     *
     * @return le détail du requérant formaté
     */
    public String getDetailRequerantListe() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVS());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                                      .getCode(
                                              getSession().getSystemCode(
                                                      "CIPAYORI",
                                                      tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode(
                                "CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNomPrenom(),
                                                        tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                                                        getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne l'attribut idTiers du Tiers
     *
     * @return la valeur courante de l'attribut idTiers du Tiers
     */
    public String getTiers() {
        return getIdTiers();
    }


    public String resolveTitle(HttpSession httpSession) {
        Map<String, String> map = new HashMap<>();
        map.put(IPRDemande.CS_TYPE_PATERNITE, "JSP_TITRE_RECAPITULATIF_PAT");
        map.put(IPRDemande.CS_TYPE_PROCHE_AIDANT, "JSP_TITRE_RECAPITULATIF_PROCHE_AIDANT");
        String typePrestation = APTypePresationDemandeResolver.resolveTypePrestation(httpSession);
        return map.get(typePrestation);
    }

    public String action(HttpSession httpSession) {
        PRTypeDemande typePrestation = APTypePresationDemandeResolver.resolveEnumTypePrestation(httpSession);

        String action = IAPActions.ACTION_SAISIE_CARTE_APAT;
        if (typePrestation.isProcheAidant()) {
            action = IAPActions.ACTION_SAISIE_CARTE_PAI;
        }
        return action;
    }

    /**
     * getter pour l'attribut id role administrateur.
     *
     * @return la valeur courante de l'attribut id role administrateur
     */
    public boolean isAdministrateur() {
        try {
            APApplication application = (APApplication) GlobazSystem
                    .getApplication(APApplication.DEFAULT_APPLICATION_APG);

            return PRUserUtils.isUtilisateurARole(getSession(), application.getIdRoleAdministrateur());
        } catch (Exception e) {
            return false;
        }
    }

    public void setAfficherBoutonSimulerPmtBPID(boolean afficherBoutonSimulerPmtBPID) {
        this.afficherBoutonSimulerPmtBPID = afficherBoutonSimulerPmtBPID;
    }

    public void setPidAnnonce(String pidAnnonce) {
        this.pidAnnonce = pidAnnonce;
    }
}

