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
     * Utilis� dans le cas ou l'on veut recr�er une annonce li�e � une prestation suite � une reprise de donn�es Permet
     * d'ins�rer le BPID voulut pour l'annonce qui sera cr��
     */
    private String pidAnnonce;

    /**
     * M�thode qui retourne le d�tail du requ�rant format� pour les d�tails
     *
     * @return le d�tail du requ�rant format�
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
     * M�thode qui retourne le d�tail du requ�rant format� pour les listes
     *
     * @return le d�tail du requ�rant format�
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
     * M�thode qui retourne l'attribut idTiers du Tiers
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

