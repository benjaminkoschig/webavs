/*
 * Cr�� le 31 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APRecapitulatifDroitPat;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRUserUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APRecapitulatifDroitPatViewBean extends APRecapitulatifDroitPat implements FWViewBeanInterface {

    /**
     * 
     */
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
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
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
     * getter pour l'attribut prenom nom
     * 
     * @return la valeur courante de l'attribut prenom nom
     */

    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    /**
     * M�thode qui retourne l'attribut idTiers du Tiers
     * 
     * @return la valeur courante de l'attribut idTiers du Tiers
     */
    public String getTiers() {
        return getIdTiers();
    }

    public final String getPidAnnonce() {
        return pidAnnonce;
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

    public final boolean isAfficherBoutonSimulerPmtBPID() {
        return afficherBoutonSimulerPmtBPID;
    }

    public final void setAfficherBoutonSimulerPmtBPID(boolean afficherBoutonSimulerPmtBPID) {
        this.afficherBoutonSimulerPmtBPID = afficherBoutonSimulerPmtBPID;
    }

    public final void setPidAnnonce(String pidAnnonce) {
        this.pidAnnonce = pidAnnonce;
    }
}
