/*
 * Créé le 22 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.administrateurs;

import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.administrateurs.COAdministrateur;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;

/**
 * @author dvh
 */
public class COAdministrateurViewBean extends COAdministrateur implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresse = "";
    private boolean adresseInitialisee = false;
    private String forIdExterneLike = null;
    private String nomTiers = "";

    /**
	 *
	 */
    public COAdministrateurViewBean() {
        super();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getAdresse() {

        if (!adresseInitialisee) {
            initAdresse();
        }
        return adresse;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getForIdExterneLike() {
        return forIdExterneLike;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNomTiers() {
        if (!adresseInitialisee) {
            initAdresse();
        }
        return nomTiers;
    }

    private void initAdresse() {

        // On récupère l'adresse contentieux. et si elle n'existe pas, l'adresse
        // de courrier
        if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {

            try {
                BSession tiSession = (BSession) GlobazSystem.getApplication(COApplication.DEFAULT_APPLICATION_PYXIS)
                        .newSession();
                getSession().connectSession(tiSession);

                ITITiers tiers = (ITITiers) tiSession.getAPIFor(ITITiers.class);
                tiers.setIdTiers(getIdTiers());
                tiers.retrieve(null);

                adresse = tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_CONTENTIEUX, JACalendar.todayJJsMMsAAAA(), true);
                nomTiers = tiers.getDesignation1() + " " + tiers.getDesignation2();

                // TIPersonneAvsAdresseListViewBean
                // personneAvsAdresseListViewBean =
                // new TIPersonneAvsAdresseListViewBean();
                // personneAvsAdresseListViewBean.setSession(tiSession);
                // personneAvsAdresseListViewBean.setForIdApplication(
                // globaz.pyxis.db.divers.TIApplication.CS_CONTENTIEUX);
                // personneAvsAdresseListViewBean.setForTypeAdresse(
                // CS_ADRESSE_COURRIER);
                // personneAvsAdresseListViewBean.setForDateEntreDebutEtFin(
                // JACalendar.todayJJsMMsAAAA());
                // personneAvsAdresseListViewBean.setForIdTiers(getIdTiers());
                // personneAvsAdresseListViewBean.find();
                //
                // if (!personneAvsAdresseListViewBean.isEmpty()) {
                // TIPersonneAvsAdresseViewBean personneAvsAdresseViewBean =
                // (TIPersonneAvsAdresseViewBean)
                // personneAvsAdresseListViewBean.getEntity(0);
                // setAdresse1(personneAvsAdresseViewBean.getRue() + " " +
                // personneAvsAdresseViewBean.getNumero());
                // setAdresse2(personneAvsAdresseViewBean
                // .getDesignation2_adr());
                // setNpa(personneAvsAdresseViewBean.getNpa());
                // setLocalite(personneAvsAdresseViewBean.getLocalite());
                // }
                //
                // //recupération du nom du tiers
                // TITiers tiers = new TITiers();
                // tiers.setSession(tiSession);
                // tiers.setIdTiers(getIdTiers());
                // tiers.retrieve();
                // nomTiers = tiers.getPrenomNom();

            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.WARNING);
            }
        }

        adresseInitialisee = true;
    }

    /**
     * teste si le numero du compte annexe auxiliaire correspond bien à celui qu'on recherche
     */
    public boolean isCompteAnnexeOk() {

        if ((getIdExterneRole() != null) && (forIdExterneLike != null)
                && (forIdExterneLike.length() <= getIdExterneRole().length())) {
            return getIdExterneRole().substring(0, forIdExterneLike.length()).equals(forIdExterneLike);
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setAdresse(String string) {
        adresse = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param b
     *            DOCUMENT ME!
     */
    public void setAdresseInitialisee(boolean b) {
        adresseInitialisee = b;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setForIdExterneLike(String string) {
        forIdExterneLike = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setNomTiers(String string) {
        nomTiers = string;
    }

}
