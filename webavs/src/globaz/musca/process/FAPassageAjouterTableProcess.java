package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAFacturationExt;
import globaz.musca.db.facturation.FAFacturationExtManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiementManager;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch Process de facturation ALFAGEST, appelé par le module de facturation (ALFacturation)
 */
public class FAPassageAjouterTableProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // constantes
    protected static final String CAISSE_PROF = "509028";
    protected static final String TYPE_FACTU_CONT_EMP = "C";
    protected static final String TYPE_FACTU_DECL = "D";
    protected static final String TYPE_FACTU_EXTERNE = "E";
    protected FAFacturationExt entity = null;
    protected String idModuleFacturation = "";
    protected FAFacturationExtManager manager = null;
    // passage
    private String numPassage = null;

    /**
     * Nettoyage après erreur ou exécution.
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Exécution du process
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        // gestion des exceptions...
        try {
            manager = new FAFacturationExtManager();
            manager.setSession(getSession());
            manager.setForTypeFactu(FAPassageAjouterTableProcess.TYPE_FACTU_EXTERNE);
            BStatement statement = manager.cursorOpen(getTransaction());
            FAEnteteFacture enteteFacture = null;
            while (((entity = (FAFacturationExt) manager.cursorReadNext(statement)) != null) && !isAborted()) {
                enteteFacture = getEnteteFacture(numPassage);
                // on rajoute l'affact dans l'en-tête de facture
                createLigneFacture(enteteFacture);
            }
            manager.cursorClose(statement);
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        }

        // tout s'est bien passé
        return true;
    }

    /**
     * Exécute le processus de facturation en appelant la méthode protected
     */
    public boolean _executeProcessFacturation() throws Exception {
        return _executeProcess();
    }

    /**
     * Crée une ligne de facture
     */
    protected void createLigneFacture(FAEnteteFacture enteteFacture) throws Exception {
        // On reset le buffer d'erreur de la transaction
        getTransaction().clearErrorBuffer();

        FAAfact aFact = new FAAfact();

        aFact.setISession(getSession());
        aFact.setIdEnteteFacture(enteteFacture.getIdEntete());
        aFact.setIdPassage(numPassage);
        aFact.setIdModuleFacturation(getIdModuleFacturation());

        aFact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        aFact.setNonImprimable(Boolean.FALSE);
        aFact.setNonComptabilisable(Boolean.FALSE);
        aFact.setAQuittancer(entity.getEnSuspens());
        // La rubrique utilisée pour une prestation est de type standard
        // donc il ne faut pas y mettre d'année
        // if (!this.entity.getTypeFactu().equals(FAPassageAjouterTableProcess.TYPE_FACTU_DECL)) {
        aFact.setAnneeCotisation(entity.getAnneeCotisation());
        // }
        aFact.setIdRubrique(getIdRubrique(entity.getNumRubrique()));
        aFact.setIdExterneRubrique(entity.getNumRubrique());
        aFact.setDebutPeriode(entity.getPeriodeDe());
        aFact.setFinPeriode(entity.getPeriodeA());
        aFact.setMontantFacture(entity.getCotisation());
        aFact.setLibelle(entity.getLibelle());
        aFact.setMasseFacture(entity.getMasse());
        aFact.setTauxFacture(entity.getTaux());
        aFact.setAffichtaux(entity.getAfficheTaux());
        aFact.setDateValeur(entity.getDateValeur());
        aFact.setNumCaisse(entity.getNumCaisse());
        TIAdministrationManager adminManager = new TIAdministrationManager();
        adminManager.setSession(getSession());
        adminManager.setForCodeAdministration(entity.getNumCaisse());
        adminManager.setForGenreAdministration(FAPassageAjouterTableProcess.CAISSE_PROF);
        adminManager.find();
        if (adminManager.size() > 0) {
            aFact.setNumCaisse(((TIAdministrationViewBean) adminManager.getFirstEntity()).getIdTiersAdministration());
        }
        entity.setNumPassage(numPassage);
        entity.update(getTransaction());

        try {
            aFact.add(getTransaction());

            // erreur dans la transaction?
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la création de l'Afact: " + enteteFacture.getIdExterneRole() + " "
                            + entity.getNumRubrique() + "\n" + e.getMessage(), FWViewBeanInterface.ERROR,
                    this.getClass().getName());
        }
    }

    /**
     * ?
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    protected FAEnteteFacture getEnteteFacture(String numPassage) {
        FAEnteteFactureManager entete = new FAEnteteFactureManager();
        try {
            if (JadeStringUtil.isBlank(entity.getNumPeriode()) || JadeStringUtil.isNull(entity.getNumPeriode())) {
                getMemoryLog().logMessage(
                        "Le numéro de décompte ne peut pas être vide dans la table Facturation Externe",
                        FWViewBeanInterface.ERROR, this.getClass().getName());
            } else {
                entete.setSession(getSession());
                entete.setForIdPassage(numPassage);
                entete.setForIdExterneRole(entity.getNumAffilie());
                entete.setForIdExterneFacture(entity.getNumPeriode());
                entete.find();
                if (entete.size() > 0) {
                    return (FAEnteteFacture) entete.getFirstEntity();
                } else if (entete.size() == 0) {
                    FAEnteteFacture enteteFac = new FAEnteteFacture();
                    enteteFac.setSession(getSession());
                    enteteFac.setIdPassage(numPassage);
                    enteteFac.setIdExterneRole(entity.getNumAffilie());
                    enteteFac.setIdTiers(getIdTiers(entity.getNumAffilie()));
                    enteteFac.setIdRole(entity.getRole());
                    // S120411_004 - Domaine Cotisation
                    // enteteFac.setIdAdresse(this.getIdAdresse(numAffilie));
                    enteteFac.setIdTypeFacture("1");
                    enteteFac.setIdSousType(getIdSousType(entity.getNumPeriode()));
                    enteteFac.setIdExterneFacture(entity.getNumPeriode());
                    // enteteFac.setIdAdressePaiement(getIdAdressePaiement(numAffilie));
                    enteteFac.setNonImprimable(new Boolean(false));
                    enteteFac.setIdSoumisInteretsMoratoires("229001");
                    enteteFac.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_AUTOMATIQUE);
                    // DGI init plan
                    enteteFac.initDefaultPlanValue(enteteFac.getIdRole());
                    enteteFac.add(getTransaction());
                    return enteteFac;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la création du décompte: " + entity.getNumAffilie() + " " + entity.getNumPeriode()
                            + "\n" + e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return null;
    }

    /**
     * @param numAffilie
     * @return
     */
    protected String getIdAdresse(String numAffilie) {
        TIAvoirAdresseManager adresse = new TIAvoirAdresseManager();
        try {
            adresse.setSession(getSession());
            adresse.setForIdTiers(getIdTiers(numAffilie));
            adresse.find();
            if (adresse.size() > 0) {
                return ((TIAvoirAdresse) adresse.getFirstEntity()).getIdAdresse();
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la récupération de l'id d'adresse " + numAffilie + "\n" + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return "";
    }

    /**
     * @param numAffilie
     * @return
     */
    protected String getIdAdressePaiement(String numAffilie) {
        TIAvoirPaiementManager adresse = new TIAvoirPaiementManager();
        try {
            adresse.setSession(getSession());
            adresse.setForIdTiers(getIdTiers(numAffilie));
            adresse.find();
            if (adresse.size() > 0) {
                return ((TIAvoirPaiement) adresse.getFirstEntity()).getIdAdressePaiement();
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la récupération de l'adresse de paiement: " + numAffilie + "\n" + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return "";
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * @param string
     * @return
     */
    protected String getIdRubrique(String numRubr) {
        CARubriqueManager rubrique = new CARubriqueManager();
        try {
            rubrique.setSession((BSession) ((FAApplication) getSession().getApplication())
                    .getSessionOsiris(getSession()));
            rubrique.setForIdExterne(numRubr);
            rubrique.find();
            if (rubrique.size() > 0) {
                return ((CARubrique) rubrique.getFirstEntity()).getIdRubrique();
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la récupération de la rubrique: " + numRubr + "\n" + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return null;
    }

    /**
     * @param string
     * @return
     */
    protected String getIdSousType(String numPeriode) {
        String numId = new String();
        numId = numPeriode.substring(4, 6);
        return "2270" + numId;
    }

    /**
     * @param numAffilie
     * @return
     */
    protected String getIdTiers(String numAffilie) {
        AFAffiliationManager affiliation = new AFAffiliationManager();
        try {
            affiliation.setSession(getSession());
            affiliation.setForAffilieNumero(numAffilie);
            affiliation.find();
            if (affiliation.size() > 0) {
                return ((AFAffiliation) affiliation.getFirstEntity()).getIdTiers();
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la récupération de l'id tiers : " + numAffilie + "\n" + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return null;
    }

    /**
     * @return
     */
    public String getNumPassage() {
        return numPassage;
    }

    /**
     * Renvoie la Job Queue à utiliser pour soumettre le process (constantes dans <code>GlobazJobQueue</code>).
     * 
     * @return la Job Queue à utiliser pour soumettre le process
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    /**
     * @param string
     */
    public void setNumPassage(String string) {
        numPassage = string;
    }
}
