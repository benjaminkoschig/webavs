package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.formater.TILocaliteLongFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPCommunicationFiscaleViewBean extends CPCommunicationFiscale implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private java.lang.String annee = "";
    private java.lang.String codeAdministration = "";
    // private java.lang.String periodicite = "";
    private java.lang.String descriptionAdministration = "";
    // private java.lang.String descriptionDecision = "";
    private java.lang.String localite = "";

    private java.lang.String nom = "";
    private java.lang.String numAffilie = "";
    private java.lang.String numAvs = "";
    private java.lang.String numContri = "";
    // private java.lang.String idDecision;
    private java.lang.String numIfd = "";
    private globaz.pyxis.db.tiers.TITiersViewBean tiers = null;

    // private CPDecision decision = null;

    @Override
    public void _afterRetrieve(BTransaction transaction) throws Exception {
        // --- Chargement entête
        _chargerEntete(transaction);
        // Recherche de la description de la caisse
        TIAdministrationViewBean admin = new TIAdministrationViewBean();
        admin.setSession(transaction.getSession());
        admin.setIdTiersAdministration(getIdCaisse());
        admin.retrieve();
        if (!admin.isNew()) {
            setCodeAdministration(admin.getCodeAdministration());
            setDescriptionAdministration(admin.getDescriptionAdministration(transaction.getSession()));
        } else {
            setCodeAdministration("");
            setDescriptionAdministration("");
        }
        // Recherche période fiscale
        CPPeriodeFiscale periode = new CPPeriodeFiscale();
        periode.setSession(transaction.getSession());
        periode.setIdIfd(getIdIfd());
        periode.retrieve();
        if (!periode.isNew()) {
            setNumIfd(periode.getNumIfd());
        } else {
            setNumIfd("");
        }
    }

    public void _chargerEntete(BTransaction transaction) throws Exception {
        // --- Initialisation des zones
        // setDescriptionDecision("");
        setNom("");
        setNumAffilie("");
        setNumContri("");
        setLocalite("");
        // -------- Recherche des données de l'affilié --------
        TITiersViewBean persAvs = new TITiersViewBean();
        persAvs.setSession(getSession());
        persAvs.setIdTiers(getIdTiers());
        persAvs.retrieve();
        setNom(persAvs.getNom());
        setNumAvs(persAvs.getNumAvsActuel() + " - " + persAvs.getDateNaissance() + " - "
                + CodeSystem.getCodeUtilisateur(getSession(), persAvs.getSexe()) + " - " + persAvs.getIdPays());
        setLocalite(persAvs.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12." + getAnnee(),
                new TILocaliteLongFormater()));
        AFAffiliation affi = new AFAffiliation();
        affi.setSession(getSession());
        affi.setAffiliationId(getIdAffiliation());
        affi.retrieve();
        setNumAffilie(affi.getAffilieNumero());
        TIHistoriqueContribuable hist = new TIHistoriqueContribuable();
        hist.setSession(getSession());
        try {
            setNumContri(hist.findPrevKnownNumContribuable(getIdTiers(), getAnnee()));
        } catch (Exception e) {
            setNumContri("");
        }
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        // Test si l'idAdministration correspond bien au code saisi
        // sinon cela implique qu'une saisie a été effectué et qu'il faut aller
        // rechercher
        // le nouvel idTiersAdministration selon ce code saisi.
        // En création, l'idTiersAdministration peut être à blanc donc ce test
        // n'est pas à faire
        if (!JadeStringUtil.isEmpty(getIdCaisse())) {
            // Recherche du code administration selon idTiersAdministration
            TIAdministrationViewBean admin = new TIAdministrationViewBean();
            admin.setIdTiersAdministration(getIdCaisse());
            try {
                admin.retrieve(statement.getTransaction());
                if (!admin.getCodeAdministration().equalsIgnoreCase(codeAdministration)) {
                    // Recherche de l'idTiersAdministration correspondant au
                    // code saisi
                    TIAdministrationManager adminManager = new TIAdministrationManager();
                    adminManager.setSession(getSession());
                    adminManager.setForCodeAdministration(codeAdministration);
                    adminManager.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
                    try {
                        adminManager.find(statement.getTransaction());
                        if (adminManager.size() == 1) {
                            admin = (TIAdministrationViewBean) adminManager.getEntity(0);
                            setIdCaisse(admin.getIdTiersAdministration());
                        } else if (adminManager.size() > 1) {
                            _addError(statement.getTransaction(), getSession().getLabel("COMMFISCAL_ERROR_MANY_ADMIN"));
                        } else {
                            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0014"));
                        }

                    } catch (Exception e) {
                        _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0015"));
                    }
                } else {
                    setIdCaisse(getIdCaisse());
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0016"));
            }
        } else {
            // Recherche de l'idTiersAdministration correspondant au code saisi
            TIAdministrationManager adminManager = new TIAdministrationManager();
            adminManager.setSession(getSession());
            adminManager.setForCodeAdministration(codeAdministration);
            adminManager.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
            try {
                adminManager.find(statement.getTransaction());
                if (adminManager.size() == 1) {
                    TIAdministrationViewBean admin = (TIAdministrationViewBean) adminManager.getEntity(0);
                    setIdCaisse(admin.getIdTiersAdministration());
                } else if (adminManager.size() > 1) {
                    _addError(statement.getTransaction(), getSession().getLabel("COMMFISCAL_ERROR_MANY_ADMIN"));
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0014"));
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0015"));
            }
        }
        super._validate(statement);
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
     * @return
     */
    public java.lang.String getAnnee() {
        try {
            CPPeriodeFiscale periode = new CPPeriodeFiscale();
            periode.setSession(getSession());
            periode.setIdIfd(getIdIfd());
            periode.retrieve();
            annee = periode.getAnneeDecisionDebut();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return annee;
    }

    public String getCantonTiers() throws Exception {
        if (getTiers() != null) {
            return getTiers().getCantonDomicile();
        } else {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.03.2003 13:26:16)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCodeAdministration() {
        return codeAdministration;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.03.2003 13:26:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionAdministration() {
        return descriptionAdministration;
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
    @Override
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
     * @return
     */
    public java.lang.String getNumContri() {
        return numContri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.03.2003 14:19:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumIfd() {
        return numIfd;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:07)
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    public globaz.pyxis.db.tiers.TITiersViewBean getTiers() {
        // enregistrement déjà chargé ?
        if (tiers == null) {
            // liste pas encore chargée, on la charge
            tiers = new globaz.pyxis.db.tiers.TITiersViewBean();
            tiers.setSession(getSession());
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            try {
                tiers.setIdTiers(getIdTiers());
                tiers.retrieve();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return tiers;
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
     * @param string
     */
    public void setAnnee(java.lang.String string) {
        annee = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.03.2003 13:26:16)
     * 
     * @param newCodeAdministration
     *            java.lang.String
     */
    public void setCodeAdministration(java.lang.String newCodeAdministration) {
        codeAdministration = newCodeAdministration;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.03.2003 13:26:00)
     * 
     * @param newDescriptionAdministration
     *            java.lang.String
     */
    public void setDescriptionAdministration(java.lang.String newDescriptionAdministration) {
        descriptionAdministration = newDescriptionAdministration;
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
    @Override
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
     * @param string
     */
    public void setNumContri(java.lang.String string) {
        numContri = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.03.2003 14:19:23)
     * 
     * @param newNumIfd
     *            java.lang.String
     */
    public void setNumIfd(java.lang.String newNumIfd) {
        numIfd = newNumIfd;
    }

}
