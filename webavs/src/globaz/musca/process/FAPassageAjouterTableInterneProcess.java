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
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch Process de facturation ALFAGEST, appelé par le module de facturation (ALFacturation)
 */
public class FAPassageAjouterTableInterneProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // constantes
    private static final String CAISSE_PROF = "509028";
    private static final String TYPE_FACTU_INTERNE = "I";
    private FAFacturationExt entity = null;
    private String idModuleFacturation = "";
    private FAFacturationExtManager manager = null;
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
            manager.setForTypeFactu(FAPassageAjouterTableInterneProcess.TYPE_FACTU_INTERNE);
            BStatement statement = manager.cursorOpen(getTransaction());
            FAEnteteFacture enteteFacture = null;
            while (((entity = (FAFacturationExt) manager.cursorReadNext(statement)) != null) && !isAborted()) {
                enteteFacture = getEnteteFacture(entity.getNumAffilie(), numPassage, entity.getIdModeRecouvrement());

                if (!isModeRecouvrementEgal(enteteFacture, entity.getIdModeRecouvrement())) {
                    throw new Exception(getSession().getLabel("ERREUR_IMPORTATION_FAFAEXT_IDRECOUVREMENT") + "(FAIDFE="
                            + entity.getIdFacturationExt() + " IDENTETEFACTURE=" + enteteFacture.getIdEntete() + ")");
                }

                // on rajoute l'affact dans l'en-tête de facture
                createLigneFacture(entity, enteteFacture);
            }

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
    private void createLigneFacture(FAFacturationExt entity, FAEnteteFacture enteteFacture) throws Exception {
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
        aFact.setAnneeCotisation(entity.getAnneeCotisation());
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
        adminManager.setForGenreAdministration(FAPassageAjouterTableInterneProcess.CAISSE_PROF);
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
                    "Erreur lors de la création de l'Afact (extID=" + entity.getIdFacturationExt() + "/"
                            + entity.getNumAffilie() + "): " + enteteFacture.getIdExterneRole() + " "
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

    private FAEnteteFacture getEnteteFacture(String numAffilie, String numPassage, String theIdModeRecouvrement) {
        FAEnteteFactureManager entete = new FAEnteteFactureManager();
        try {
            if (JadeStringUtil.isBlank(entity.getNumPeriode()) || JadeStringUtil.isNull(entity.getNumPeriode())) {
                getMemoryLog().logMessage(
                        "Le numéro de décompte ne peut pas être vide dans la table Facturation Externe",
                        FWViewBeanInterface.ERROR, this.getClass().getName());
            } else {
                entete.setSession(getSession());
                entete.setForIdPassage(numPassage);
                entete.setForIdExterneRole(numAffilie);
                entete.setForIdExterneFacture(entity.getNumPeriode());
                entete.find();
                if (entete.size() > 0) {
                    return (FAEnteteFacture) entete.getFirstEntity();
                } else if (entete.size() == 0) {
                    FAEnteteFacture enteteFac = new FAEnteteFacture();
                    enteteFac.setSession(getSession());
                    enteteFac.setIdPassage(numPassage);
                    enteteFac.setIdExterneRole(numAffilie);
                    enteteFac.setIdTiers(getIdTiers(numAffilie));
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
                    if (!JadeStringUtil.isBlankOrZero(theIdModeRecouvrement)) {
                        enteteFac.setIdModeRecouvrement(theIdModeRecouvrement);
                    }
                    // DGI init plan
                    enteteFac.initDefaultPlanValue(enteteFac.getIdRole());
                    enteteFac.add(getTransaction());
                    return enteteFac;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de la création du décompte: " + numAffilie + " " + entity.getNumPeriode() + "\n"
                            + e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        return null;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * @param string
     * @return
     */
    private String getIdRubrique(String numRubr) {
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

    // /**
    // * @param numAffilie
    // * @return
    // */
    // private String getIdAdresse(String numAffilie) {
    // TIAvoirAdresseManager adresse = new TIAvoirAdresseManager();
    // try {
    // adresse.setSession(this.getSession());
    // adresse.setForIdTiers(this.getIdTiers(numAffilie));
    // adresse.find();
    // if (adresse.size() > 0) {
    // return ((TIAvoirAdresse) adresse.getFirstEntity()).getIdAdresse();
    // }
    // } catch (Exception e) {
    // this.getMemoryLog().logMessage(
    // "Erreur lors de la récupération de l'id d'adresse " + numAffilie + "\n" + e.getMessage(),
    // FWViewBeanInterface.ERROR, this.getClass().getName());
    // }
    // return "";
    // }

    // /**
    // * @param numAffilie
    // * @return
    // */
    // private String getIdAdressePaiement(String numAffilie) {
    // TIAvoirPaiementManager adresse = new TIAvoirPaiementManager();
    // try {
    // adresse.setSession(this.getSession());
    // adresse.setForIdTiers(this.getIdTiers(numAffilie));
    // adresse.find();
    // if (adresse.size() > 0) {
    // return ((TIAvoirPaiement) adresse.getFirstEntity()).getIdAdressePaiement();
    // }
    // } catch (Exception e) {
    // this.getMemoryLog().logMessage(
    // "Erreur lors de la récupération de l'adresse de paiement: " + numAffilie + "\n" + e.getMessage(),
    // FWViewBeanInterface.ERROR, this.getClass().getName());
    // }
    // return "";
    // }

    /**
     * @param string
     * @return
     */
    private String getIdSousType(String numPeriode) {
        String numId = new String();
        numId = numPeriode.substring(4, 6);
        return "2270" + numId;
    }

    /**
     * @param numAffilie
     * @return
     */
    private String getIdTiers(String numAffilie) {
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

    private boolean isModeRecouvrementEgal(FAEnteteFacture theEnteteFacture, String theIdModeRecouvrement) {
        return (theIdModeRecouvrement.equalsIgnoreCase(theEnteteFacture.getIdModeRecouvrement()))
                || (JadeStringUtil.isBlankOrZero(theIdModeRecouvrement) && FAEnteteFacture.CS_MODE_AUTOMATIQUE
                        .equalsIgnoreCase(theEnteteFacture.getIdModeRecouvrement()));
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
