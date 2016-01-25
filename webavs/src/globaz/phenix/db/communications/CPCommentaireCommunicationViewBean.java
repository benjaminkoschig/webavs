package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.globall.api.BISession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiersViewBean;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPCommentaireCommunicationViewBean extends CPCommentaireCommunication implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private CPCommunicationFiscaleRetourViewBean communicationFiscaleRetour = null;
    private java.lang.String idTiers;
    private java.lang.String localite;
    private java.lang.String nom;
    private java.lang.String numAffilie;
    private java.lang.String numAvs;

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // --- Chargement entête
        // _chargerEntete();
    }

    public void _chargerEntete() throws Exception {
        // --- Initialisation des zones
        // -------- Recherche des données de la communication en retour --------
        CPCommunicationFiscaleRetourViewBean commRetour = new CPCommunicationFiscaleRetourViewBean();
        commRetour.setSession(getSession());
        commRetour.setIdRetour(getIdCommunicationRetour());
        commRetour.retrieve();
        // -------- Recherche des données de l'affilié --------
        if (commRetour != null && !commRetour.isNew()) {
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(commRetour.getIdTiers());
            setIdTiers(commRetour.getIdTiers());
            persAvs.retrieve();
            if (persAvs != null && !persAvs.isNew()) {
                setNom(persAvs.getNom());
                setNumAvs(persAvs.getNumAvsActuel() + " - " + persAvs.getDateNaissance() + " - "
                        + CodeSystem.getCodeUtilisateur(getSession(), persAvs.getSexe()) + " - " + persAvs.getIdPays());
                TIAdresseDataSource ds = new TIAdresseDataSource();
                ds.setSession(getSession());
                ds.load(AFAffiliationUtil.getAdresseExploitation(commRetour.getAffiliation()));
                String localiteLong = ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " - "
                        + ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                setLocalite(localiteLong);
            }
            AFAffiliation affi = new AFAffiliation();
            affi.setSession(getSession());
            affi.setAffiliationId(commRetour.getIdAffiliation());
            affi.retrieve();
            if (affi != null && !affi.isNew()) {
                setNumAffilie(affi.getAffilieNumero());
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Renvoie la communicationFiscaleRetour ou null en cas d'exception
     * 
     * @param idRetour
     * @param session
     * @return
     */
    public CPCommunicationFiscaleRetourViewBean getCommunicationFiscaleRetour(HttpSession session) {
        if (communicationFiscaleRetour == null) {
            try {
                FWController controller = (FWController) session.getAttribute("objController");
                BISession bSession = controller.getSession();
                communicationFiscaleRetour = new CPCommunicationFiscaleRetourViewBean();
                communicationFiscaleRetour.setISession(bSession);
                communicationFiscaleRetour.setIdRetour(getIdCommunicationRetour());
                communicationFiscaleRetour.retrieve();
            } catch (Exception e) {
                communicationFiscaleRetour = null;
            }
        }
        return communicationFiscaleRetour;

    }

    public String getIdJournalRetour(HttpSession session) {
        CPCommunicationFiscaleRetourViewBean communication = getCommunicationFiscaleRetour(session);
        String idJournalRetour = null;
        if (communication != null) {
            idJournalRetour = communication.getIdJournalRetour();
        }
        return idJournalRetour;
    }

    /**
     * Returns the idTiers.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 15:18:53) retourne le n° avs + le nom du
     * conjoint
     */
    public String getLibelleCommentaire() {
        String libelleCommentaire = "";
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdCommentaire())) {
                libelleCommentaire = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getIdCommentaire());
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return libelleCommentaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:31:28)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLocalite() {
        return localite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:20:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNom() {
        return nom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:20:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:20:51)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumAvs() {
        return numAvs;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Sets the idTiers.
     * 
     * @param idTiers
     *            The idTiers to set
     */
    public void setIdTiers(java.lang.String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:31:28)
     * 
     * @param newLoaclite
     *            java.lang.String
     */
    public void setLocalite(java.lang.String newLocalite) {
        localite = newLocalite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:20:10)
     * 
     * @param newNom
     *            java.lang.String
     */
    public void setNom(java.lang.String newNom) {
        nom = newNom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:20:33)
     * 
     * @param newNumAffilie
     *            java.lang.String
     */
    public void setNumAffilie(java.lang.String newNumAffilie) {
        numAffilie = newNumAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:20:51)
     * 
     * @param newNumAvs
     *            java.lang.String
     */
    public void setNumAvs(java.lang.String newNumAvs) {
        numAvs = newNumAvs;
    }

}
