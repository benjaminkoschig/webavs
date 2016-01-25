package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;

/*
 * Créé le 03 févr. 05 RRI Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */

/**
 * Génération de la facturation. Date de création : (23.02.2005 11:54:17)
 * 
 * @author rri
 */
public class FAPassageModuleFacturationProcess extends FAGenericProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idModuleFact;

    /**
     * Commentaire relatif au constructeur
     */
    public FAPassageModuleFacturationProcess() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @auteur: BTC
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {

            // On récupère le passage grâce a idPassage
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve(getTransaction());

            if ((passage != null) && !passage.isNew()) {
                setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_REUSSIE") + passage.getLibelle()
                        + " (" + getIdPassage() + ")");
            } else {
                setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_ECHOUEE") + passage.getLibelle()
                        + " (" + getIdPassage() + ")");
            }

            // Vérifie si le passage peut être généré, ne l'est pas si vérouillé
            // ou annulé
            if (passage.isEstVerrouille().booleanValue() || passage.getStatus().equals(FAPassage.CS_ETAT_ANNULE)) {
                this.logInfo4Process(false, "OBJEMAIL_FA_ISVERROUILLE_INFO");
                throw new Exception("Facturation refusée car le passage est verrouillé ou annulé.");
            }

            // Début du traitement
            try {
                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Initialisation *********************************************** **************************
                 */
                _initializePassage(passage);

                /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
                 * Ajouts des modules systèmes **********************************
                 * ***************************************
                 */

                // Cherche le module de facturation selon son Id
                FAModuleFacturationManager moduleFacturationManager = new FAModuleFacturationManager();
                moduleFacturationManager.setSession(getSession());
                if (idModuleFact != null) {
                    moduleFacturationManager.setForIdModuleFacturation(idModuleFact);
                } else {
                    moduleFacturationManager.setForIdModuleFacturation("");
                }
                moduleFacturationManager.find();

                FAModuleFacturation moduleFact = (FAModuleFacturation) moduleFacturationManager.getEntity(0);
                if (moduleFact.getIdTypeModule().equals(FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE)
                        || moduleFact.getIdTypeModule().equals(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES)
                        || moduleFact.getIdTypeModule().equals(FAModuleFacturation.CS_MODULE_LETTRE_TAXE_CO2)) {
                    passage.setModuleEnCours(moduleFact.getIdTypeModule());
                }
                // Prend le nom de la classe qu'utilise le module et appelle la
                // méthode dActionImprimer
                String nomClasse = moduleFact.getNomClasse();
                try {
                    if ((!JadeStringUtil.isEmpty(nomClasse) || !JadeStringUtil.isBlank(nomClasse))
                            && !getActionModulePassage().equals(FAModulePassage.CS_ACTION_COMPTABILISE)) {
                        doActionImprimer(moduleFact, nomClasse);
                    } else if ((!JadeStringUtil.isEmpty(nomClasse) || !JadeStringUtil.isBlank(nomClasse))
                            && getActionModulePassage().equals(FAModulePassage.CS_ACTION_COMPTABILISE)) {
                        doActionComptabiliser(moduleFact, nomClasse);
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, this.getClass().getName());
                }

            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            } finally {
                // Vérification du log - logger toutes les erreurs des modules
                this.logInfo4Process(getTransaction().getErrors().toString());
                // Fin de la procédure
                _finalizePassage(passage);
            }

        } catch (Exception e) {
            this.logInfo4Process(false, "OBJEMAIL_FA_ERROR");
            return false;
        }
        return !isAborted();
    }

    public boolean doActionComptabiliser(FAModuleFacturation modulePassage, String nomClasse) {
        try {
            // Instancier une classe anonyme avec son nom
            this.logInfo4Process(false, false, "OBJEMAIL_FA_IMPRIMERIMPL_INFO");
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
            interface_modulefacturation.comptabiliser(passage, this);
        } catch (ClassNotFoundException e) {
            getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FA_MODULENOMCLASSE_WARNING") + ": " + nomClasse,
                    FWMessage.AVERTISSEMENT, "");
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de l'impression: " + e.getMessage(), FWViewBeanInterface.ERROR,
                    this.getClass().getName());
        }
        return true;
    }

    /**
     * Method doActionImprimer.
     * 
     * @param modulePassage
     */
    public boolean doActionImprimer(FAModuleFacturation modulePassage, String nomClasse) {
        try {
            // Instancier une classe anonyme avec son nom
            this.logInfo4Process(false, false, "OBJEMAIL_FA_IMPRIMERIMPL_INFO");
            Class<?> cl = Class.forName(nomClasse);
            IntModuleFacturation interface_modulefacturation = (IntModuleFacturation) cl.newInstance();
            interface_modulefacturation.imprimer(passage, this);
        } catch (ClassNotFoundException e) {
            getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FA_MODULENOMCLASSE_WARNING") + ": " + nomClasse,
                    FWMessage.AVERTISSEMENT, "");
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de l'impression: " + e.getMessage(), FWViewBeanInterface.ERROR,
                    this.getClass().getName());
        }
        return true;
    }

    public String getIdModuleFact() {
        return idModuleFact;
    }

    /**
     * @return
     */
    public String getLibelleModuleFact() {
        String idModule = getIdModuleFact();
        String libelle = "";
        FAModuleFacturation module = new FAModuleFacturation();
        module.setSession(getSession());
        module.setIdModuleFacturation(idModule);
        try {
            module.retrieve();
            libelle = module.getLibelle();
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la récupération du libellé du module: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        }

        return libelle;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdModuleFact(String idModule) {
        idModuleFact = idModule;
    }

}
