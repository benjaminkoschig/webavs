package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CPSortieMontantViewBean extends CPSortieMontant implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private java.lang.String idAffiliation = "";
    private java.lang.String idTiers = "";
    private java.lang.String nom = "";
    private java.lang.String numAffilie = "";

    public CPSortieMontantViewBean() {
        super();
    }

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // --- Chargement entête
        _chargerEntete();
    }

    public void _chargerEntete() throws Exception {
        // --- Initialisation des zones
        try {
            // -------- Recherche des données de la sortie --------
            CPSortie sortie = new CPSortie();
            sortie.setSession(getSession());
            sortie.setIdSortie(getIdSortie());
            sortie.retrieve();
            setIdAffiliation(sortie.getIdAffiliation());
            // -------- Recherche des données de l'affilié --------
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(sortie.getIdTiers());
            setIdTiers(sortie.getIdTiers());
            persAvs.retrieve();
            setNom(persAvs.getNomPrenom());
            // -------- Recherche des données de l'affilié --------
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(sortie.getIdAffiliation());
            affiliation.retrieve();
            setNumAffilie(affiliation.getAffilieNumero());
        } catch (Exception e) {
            e.printStackTrace();
            _addError(getSession().getCurrentThreadTransaction(), e.getMessage());
        }
    }

    /**
     * @return
     */
    public String getAction() {
        return action;
    }

    public String getAnnee() {
        String annee = "";
        try {
            CPSortie sortie = new CPSortie();
            sortie.setSession(getSession());
            sortie.setIdSortie(getIdSortie());
            sortie.retrieve();
            if (sortie != null && !sortie.isNew()) {
                annee = sortie.getAnnee();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return annee;
    }

    /**
     * @return
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * @return
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public java.lang.String getNom() {
        return nom;
    }

    /**
     * @return
     */
    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @param string
     */
    public void setAction(String string) {
        action = string;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(java.lang.String string) {
        idAffiliation = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(java.lang.String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setNom(java.lang.String string) {
        nom = string;
    }

    /**
     * @param string
     */
    public void setNumAffilie(java.lang.String string) {
        numAffilie = string;
    }

}