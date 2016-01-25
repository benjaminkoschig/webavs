package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPRemarqueDecisionViewBean extends CPRemarqueDecision implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private java.lang.String descriptionDecision;
    public java.lang.String idAffiliation = "";
    private java.lang.String idTiers = "";
    private java.lang.String localite;
    private java.lang.String nom;
    private java.lang.String numAffilie;

    private java.lang.String numAvs;
    private java.lang.String periodicite;

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // --- Chargement entête
        _chargerEntete();
    }

    public void _chargerEntete() throws Exception {
        // --- Initialisation des zones
        try {
            // -------- Recherche des données de la décision --------
            CPDecision decision = new CPDecision();
            decision.setSession(getSession());
            decision.setIdDecision(getIdDecision());
            decision.retrieve();
            if (!decision.isNew() && decision != null) {
                setIdTiers(decision.getIdTiers());
                setDescriptionDecision(decision.getDescriptionDecision());
                setNom(decision.loadTiers().getNom());
                setNumAvs(decision.loadTiers().getNumAvsActuel() + " - " + decision.loadTiers().getDateNaissance()
                        + " - " + CodeSystem.getCodeUtilisateur(getSession(), decision.loadTiers().getSexe()) + " - "
                        + decision.loadTiers().getIdPays());
                TIAdresseDataSource ds = new TIAdresseDataSource();
                ds.setSession(getSession());
                ds.load(AFAffiliationUtil.getAdresseExploitation(decision.loadAffiliation()));
                String localiteLong = ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " - "
                        + ds.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                setLocalite(localiteLong);
                setNumAffilie(decision.loadAffiliation().getAffilieNumero());
                setPeriodicite(decision.loadAffiliation().getPeriodicite());
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
    }

    /**
     * Effectue des traitements après une lecture dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    // protected void _init() {
    // // Emplacement = Rmarque par défaut
    // if (JadeStringUtil.isEmpty(getIdRemarqueDecision())) {
    // setEmplacement(CPRemarqueType.CS_REMARQUE);
    // }
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:22:14)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionDecision() {
        return descriptionDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.05.2003 14:45:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 16:29:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Affiche le libbellé de la périodicité
     * 
     * @return String
     */
    public String getLibellePeriodicite() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getPeriodicite());
        } catch (Exception e) {
            return "";
        }
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
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:21:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPeriodicite() {
        return periodicite;
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
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:22:14)
     * 
     * @param newLibelleDecision
     *            java.lang.String
     */
    public void setDescriptionDecision(java.lang.String newDescriptionDecision) {
        descriptionDecision = newDescriptionDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.05.2003 14:45:46)
     * 
     * @param newIdAffiliation
     *            java.lang.String
     */
    public void setIdAffiliation(java.lang.String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 16:29:46)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
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

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:21:41)
     * 
     * @param newPeriodicite
     *            java.lang.String
     */
    public void setPeriodicite(java.lang.String newPeriodicite) {
        periodicite = newPeriodicite;
    }
}
