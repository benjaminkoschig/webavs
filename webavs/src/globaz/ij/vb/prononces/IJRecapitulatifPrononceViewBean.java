/*
 * Créé le 16 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSessionUtil;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prononces.IJRecapitulatifPrononce;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRUserUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRecapitulatifPrononceViewBean extends IJRecapitulatifPrononce implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Le libelle de csGenreReadaptation
     * 
     * @return
     */
    public String getCsGenreReadaptationLibelle() {
        return getSession().getCodeLibelle(getCsGenreReadaptation());
    }

    /**
     * @return
     */
    public String getCsTypeHebergementLibelle() {
        return getSession().getCodeLibelle(getCsTypeHebergement());
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
     * getter pour l'attribut libelle etat
     * 
     * @return la valeur courante de l'attribut libelle etat
     */
    public String getLibelleEtat() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    /**
     * getter pour l'attribut libelle etat civil
     * 
     * @return la valeur courante de l'attribut libelle etat civil
     */
    public String getLibelleEtatCivil() {
        return getSession().getCodeLibelle(getCsEtatCivil());
    }

    /**
     * getter pour l'attribut libelle genre readaptation
     * 
     * @return la valeur courante de l'attribut libelle genre readaptation
     */
    public String getLibelleGenreReadaptation() {
        return getSession().getCodeLibelle(getCsGenre());
    }

    /**
     * getter pour l'attribut libelle mode calcul
     * 
     * @return la valeur courante de l'attribut libelle mode calcul
     */
    public String getLibelleModeCalcul() {
        return getSession().getCodeLibelle(getCsModeCalcul());
    }

    /**
     * getter pour l'attribut libelle periodicite manque AGagner
     * 
     * @return la valeur courante de l'attribut libelle periodicite manque AGagner
     */
    public String getLibellePeriodiciteManqueAGagner() {
        return getSession().getCodeLibelle(getCsPeriodiciteManqueAGagner());
    }

    /**
     * getter pour l'attribut libelle periodicite readaptation
     * 
     * @return la valeur courante de l'attribut libelle periodicite readaptation
     */
    public String getLibellePeriodiciteReadaptation() {
        return getSession().getCodeLibelle(getCsPeriodiciteRevenuReadaptation());
    }

    /**
     * getter pour l'attribut libelle statut
     * 
     * @return la valeur courante de l'attribut libelle statut
     */
    public String getLibelleStatut() {
        return getSession().getCodeLibelle(getCsStatutProfessionnel());
    }

    /**
     * getter pour l'attribut nom prenom
     * 
     * @return la valeur courante de l'attribut prenom nom
     */
    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean is4emeRevision() throws Exception {
        if (isGrandeIJ()) {
            String date4emeRevision = getSession().getApplication().getProperty(
                    IJApplication.PROPERTY_DATE_DEBUT_4EME_REVISION);

            return BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutPrononce(), date4emeRevision);
        } else {
            return false;
        }
    }

    /**
     * getter pour l'attribut id role administrateur.
     * 
     * @return la valeur courante de l'attribut id role administrateur
     */
    public boolean isAdministrateur() {
        try {
            IJApplication application = (IJApplication) GlobazSystem
                    .getApplication(IJApplication.DEFAULT_APPLICATION_IJ);

            return PRUserUtils.isUtilisateurARole(getSession(), application.getIdRoleAdministrateur());
        } catch (Exception e) {
            return false;
        }
    }

}
