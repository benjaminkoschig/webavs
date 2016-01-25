/**
 * 
 */
package globaz.amal.process.reprise;

import globaz.amal.process.AMALabstractProcess;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ch.globaz.amal.business.models.reprise.ContribuableReprise;
import ch.globaz.amal.business.models.reprise.ContribuableRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleContribuableReprise;
import ch.globaz.amal.business.models.reprise.SimpleFamilleReprise;
import ch.globaz.amal.business.models.reprise.SimpleFamilleRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleInnerMFReprise;
import ch.globaz.amal.business.models.reprise.SimpleInnerMFRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;

/**
 * Process de reprise tiers (nouvelle persistence)
 * 
 * @author DHI
 * 
 */
public class AMRepriseTiersProcess extends AMALabstractProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Gets the stack trace of exception into a string
     * 
     * @param e
     * @return
     */
    public static String stack2string(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String returnString = sw.toString();
            returnString = returnString.replaceAll("(\r\n|\r|\n|\n\r)", " <--> ");
            return returnString;
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }

    private Boolean askToStop = true;
    private Boolean checkDataBeforeReprise = false;
    private AMGestionTiers gestionTiers = new AMGestionTiers();
    private int iErrorAdresse = 0;
    private int iErrorTiers = 0;
    private int iNewAdresse = 0;
    private int iNewTiers = 0;
    private Boolean isSimulation = true;
    private int iTotalAdresse = 0;
    private int iTotalTiers = 0;
    private int iUpdatedAdresse = 0;

    private int iUpdatedTiers = 0;

    /**
     * Default constructor
     */
    public AMRepriseTiersProcess() {
        super();
    }

    /**
     * Lecture de tous les enregistrements contribuable reprise, permet de valider l'utilisation des données avec la
     * nouvelle persistence
     * 
     * @return true si erreur de lecture (données non compatibles avec nouvelle persistence) false sinon
     */
    private Boolean checkContribuableData() {
        Boolean hasError = false;
        // Récupération des contribuables de reprise
        // ----------------------------------------------------------------------------
        ContribuableRepriseSearch contribuableSearch = new ContribuableRepriseSearch();
        try {
            contribuableSearch = AmalServiceLocator.getContribuableRepriseService().search(contribuableSearch);
            int iOffset = 0;

            while (contribuableSearch.isHasMoreElements() && (hasError == false)) {
                for (int iContribuable = 0; iContribuable < contribuableSearch.getSize(); iContribuable++) {
                    ContribuableReprise contribuable = (ContribuableReprise) contribuableSearch.getSearchResults()[iContribuable];
                    String csToDisplay = "";
                    csToDisplay += "Contribuable : " + contribuable.getId() + " - ";
                    csToDisplay += contribuable.getSimpleContribuableReprise().getIdTiers() + " - ";
                    csToDisplay += contribuable.getSimpleContribuableInfoReprise().getNomDeFamille() + " ";
                    csToDisplay += contribuable.getSimpleContribuableInfoReprise().getPrenom();
                    csToDisplay += "                                       ";
                    JadeLogger.info(this, csToDisplay);
                }
                iOffset += contribuableSearch.getSize();
                contribuableSearch.setOffset(iOffset);
                contribuableSearch = AmalServiceLocator.getContribuableRepriseService().search(contribuableSearch);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger
                    .error(this, "ERREUR DANS LES DONNEES DE REPRISE RP_MA_CONTRI ET RP_MA_MACTBINF " + ex.toString());
            hasError = true;
        }

        return hasError;
    }

    /**
     * Lecture de tous les enregistrements famille reprise, permet de valider l'utilisation des données avec la nouvelle
     * persistence
     * 
     * @return true si erreur de lecture (données non compatibles avec nouvelle persistence) false sinon
     */
    private Boolean checkFamilleData() {
        Boolean hasError = false;

        // Récupération des familles de reprises
        // ----------------------------------------------------------------------------
        SimpleFamilleRepriseSearch familleRepriseSearch = new SimpleFamilleRepriseSearch();
        try {
            familleRepriseSearch = AmalServiceLocator.getContribuableRepriseService().search(familleRepriseSearch);
            int iOffset = 0;

            while (familleRepriseSearch.isHasMoreElements() && (hasError == false)) {
                for (int iFamille = 0; iFamille < familleRepriseSearch.getSize(); iFamille++) {
                    SimpleFamilleReprise famille = (SimpleFamilleReprise) familleRepriseSearch.getSearchResults()[iFamille];
                    String csToDisplay = "";
                    csToDisplay += "Famille : " + famille.getId() + " - ";
                    csToDisplay += famille.getIdTiers() + " - ";
                    csToDisplay += famille.getNomPrenom();
                    csToDisplay += "                                       ";
                    JadeLogger.info(this, csToDisplay);
                }
                iOffset += familleRepriseSearch.getSize();
                familleRepriseSearch.setOffset(iOffset);
                familleRepriseSearch = AmalServiceLocator.getContribuableRepriseService().search(familleRepriseSearch);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, "ERREUR DANS LES DONNEES DE REPRISES RP_AMAL_MAFAMILL " + ex.toString());
            hasError = true;
        }

        return hasError;
    }

    /**
     * Lecture de tous les enregistrements inner_mf reprise, permet de valider l'utilisation des données avec la
     * nouvelle persistence
     * 
     * @return true si erreur de lecture (données non compatibles avec nouvelle persistence) false sinon
     */
    private Boolean checkInnerMFData() {
        Boolean hasError = false;

        // Récupération des informations de la table RP_INNER_MF
        // ----------------------------------------------------------------------------
        SimpleInnerMFRepriseSearch innerSearch = new SimpleInnerMFRepriseSearch();
        try {
            innerSearch = AmalServiceLocator.getContribuableRepriseService().search(innerSearch);
            int iOffset = 0;
            while (innerSearch.isHasMoreElements() && (hasError == false)) {
                for (int iInner = 0; iInner < innerSearch.getSize(); iInner++) {
                    SimpleInnerMFReprise innerMF = (SimpleInnerMFReprise) innerSearch.getSearchResults()[iInner];
                    String csToDisplay = "";
                    csToDisplay += "Inner MF : " + innerMF.getId() + " - ";
                    csToDisplay += innerMF.getIdInnerMF();
                    JadeLogger.info(this, csToDisplay);
                }
                iOffset += innerSearch.getSize();
                innerSearch.setOffset(iOffset);
                innerSearch = AmalServiceLocator.getContribuableRepriseService().search(innerSearch);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, "ERREUR DANS LES DONNEES DE REPRISES RP_INNER_MF " + ex.toString());
            hasError = true;
        }

        return hasError;
    }

    /**
     * Contrôle des données de reprise
     * 
     * @return true si erreur, false sinon
     */
    private Boolean checkRepriseData() {
        Boolean hasError = false;
        hasError = checkInnerMFData();
        if (hasError) {
            return true;
        }
        hasError = checkContribuableData();
        if (hasError) {
            return true;
        }
        hasError = checkFamilleData();
        return hasError;
    }

    /**
     * @return the askToStop
     */
    public Boolean getAskToStop() {
        return askToStop;
    }

    /**
     * @return the checkDataBeforeReprise
     */
    public Boolean getCheckDataBeforeReprise() {
        return checkDataBeforeReprise;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Process de reprise des tiers";
    }

    /**
     * @return the gestionTiers
     */
    public AMGestionTiers getGestionTiers() {
        return gestionTiers;
    }

    /**
     * @return the isSimulation
     */
    public Boolean getIsSimulation() {
        return isSimulation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return toString();
    }

    public void incrementCountAdresseError() {
        synchronized (this) {
            iErrorAdresse++;
        }
    }

    public void incrementCountAdresseNew() {
        synchronized (this) {
            iNewAdresse++;
        }
    }

    public void incrementCountAdresseTotal() {
        synchronized (this) {
            iTotalAdresse++;
        }
    }

    public void incrementCountAdresseUpdated() {
        synchronized (this) {
            iUpdatedAdresse++;
        }
    }

    public void incrementCountTiersError() {
        synchronized (this) {
            iErrorTiers++;
        }
    }

    public void incrementCountTiersNew() {
        synchronized (this) {
            iNewTiers++;
        }
    }

    public void incrementCountTiersTotal() {
        synchronized (this) {
            iTotalTiers++;
        }
    }

    public void incrementCountTiersUpdated() {
        synchronized (this) {
            iUpdatedTiers++;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.amal.process.AMALabstractProcess#process()
     */
    @Override
    protected void process() {
        PrintStream savedOut = System.out;
        PrintStream savedError = System.err;
        // Redirection des messages de la console vers les fichiers
        JadeLogger.info(this, "                                                           ");
        JadeLogger.info(this, "Redirection de la console vers fichiers particuliers.");
        JadeLogger.info(this, "                                                           ");
        try {
            PrintStream streamOut = new PrintStream(new FileOutputStream("d:/temp/Lamal_Reprise_stdout.log"));
            System.setOut(streamOut);
            PrintStream streamErr = new PrintStream(new FileOutputStream("d:/temp/Lamal_Reprise_stderr.log"));
            System.setErr(streamErr);
        } catch (Exception ex) {
            JadeLogger.error(this, "Erreur lors de la redirection des messages : " + ex.toString());
        }

        JadeLogger.info(this, "> Start process                                              ");
        JadeLogger.info(this, "                                                             ");
        Boolean hasError = false;
        Boolean needTreatment = false;
        if (checkDataBeforeReprise) {
            hasError = checkRepriseData();
        }
        if (hasError) {
            // Mise à jour du status à stopped
            try {
                updateInnerMF("0", "1");
            } catch (Exception ex) {
                JadeLogger.error(this, "Error setting inner mf 0 1 : " + ex.toString());
            }
            // Redirection des messages de la console vers les fichiers
            try {
                System.setOut(savedOut);
                System.setErr(savedError);
                JadeLogger.info(this, "Console réactivée. ");
            } catch (Exception ex) {
                JadeLogger.error(this, "Erreur lors de la redirection des messages : " + ex.toString());
            }
            return;
        }
        // On execute la reprise
        // ----------------------------------------------------------
        iErrorAdresse = 0;
        iErrorTiers = 0;
        iTotalTiers = 0;
        iNewAdresse = 0;
        iNewTiers = 0;
        iTotalAdresse = 0;
        iUpdatedAdresse = 0;
        iUpdatedTiers = 0;

        // Parcours des membres familles
        // ----------------------------------------------------------
        SimpleFamilleRepriseSearch familleRepriseSearch = new SimpleFamilleRepriseSearch();
        // 1 par 1 ?
        familleRepriseSearch.setDefinedSearchSize(4000);
        // A Marca Clementine
        // familleRepriseSearch.setForIdContribuable("53989");
        // A Marca Boris
        // familleRepriseSearch.setForIdContribuable("51826");
        try {
            // mise à jour du compteur
            int iTotalCount = AmalServiceLocator.getContribuableRepriseService().count(familleRepriseSearch);
            updateInnerMF("-1", "" + iTotalCount);
            updateInnerMF("-2", "0");

            // 1ère recherche et boucle pour tout faire
            familleRepriseSearch = AmalServiceLocator.getContribuableRepriseService().search(familleRepriseSearch);
            if (familleRepriseSearch.getSize() > 0) {
                needTreatment = true;
            } else {
                needTreatment = false;
            }

            int iOffset = 0;
            while (needTreatment) {
                // ----------------------------------------------------------------
                // Dispatch to worker and thread pools
                // ----------------------------------------------------------------
                int iNbWorkers = familleRepriseSearch.getSize();
                int iThreadPoolSize = iNbWorkers / 4;
                if (iThreadPoolSize < 1) {
                    iThreadPoolSize = 1;
                } else if (iThreadPoolSize > 20) {
                    iThreadPoolSize = 15;
                }
                // iThreadPoolSize = 1;
                ExecutorService tpes = Executors.newFixedThreadPool(iThreadPoolSize);
                AMRepriseTiersWorker[] workers = new AMRepriseTiersWorker[iNbWorkers];
                for (int iFamille = 0; iFamille < iNbWorkers; iFamille++) {
                    SimpleFamilleReprise currentFamille = (SimpleFamilleReprise) familleRepriseSearch
                            .getSearchResults()[iFamille];
                    // Création du worker
                    workers[iFamille] = new AMRepriseTiersWorker(iFamille, this, currentFamille);
                    // Set session
                    workers[iFamille].setSession(getSession());
                    // Exécution
                    tpes.execute(workers[iFamille]);
                }
                tpes.shutdown();
                while (!tpes.isTerminated()) {
                    JadeLogger.info(null, "Executor service not terminated. Wait 10s");
                    try {
                        Thread.sleep(10000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        break;
                    }
                }
                tpes = null;
                workers = null;
                JadeLogger.info(null, "**********************************************************");
                JadeLogger.info(null, "MAXIMUM : " + (Runtime.getRuntime().maxMemory() / 1048576) + " Mb");
                JadeLogger.info(null, "TOTAL : " + (Runtime.getRuntime().totalMemory() / 1048576) + " Mb");
                JadeLogger.info(null, "FREE : " + (Runtime.getRuntime().freeMemory() / 1048576) + " Mb");
                JadeLogger.info(null, "**********************************************************");
                // ----------------------------------------------------------------
                // Workers done the job, next please
                // ----------------------------------------------------------------
                iOffset += familleRepriseSearch.getSize();
                // Mise à jour du compteur en DB
                updateInnerMF("-2", "" + iOffset);
                // Set le nouvel offset
                familleRepriseSearch.setOffset(iOffset);
                if (familleRepriseSearch.isHasMoreElements()) {
                    // Check si une demande de stop est en cours
                    SimpleInnerMFReprise statusInnerMF = AmalServiceLocator.getContribuableRepriseService().readInner(
                            "0");
                    if (statusInnerMF.getIdTiers().equals("-1")) {
                        JadeLogger.info(this, "STOPPED REQUESTED ");
                        needTreatment = false;
                    } else {
                        // poursuivons le traitement
                        needTreatment = true;
                        familleRepriseSearch = AmalServiceLocator.getContribuableRepriseService().search(
                                familleRepriseSearch);
                    }
                } else {
                    needTreatment = false;
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Erreur dans la reprise des membres de famille : " + ex.toString());
            hasError = true;
        }

        JadeLogger.info(this, "Nombre tiers NEW : " + iNewTiers);
        JadeLogger.info(this, "Nombre tiers UPDATED : " + iUpdatedTiers);
        JadeLogger.info(this, "Nombre tiers ERROR : " + iErrorTiers);
        JadeLogger.info(this, "Nombre tiers TOTAL : " + iTotalTiers);
        JadeLogger.info(this, "Nombre adresses NEW : " + iNewAdresse);
        JadeLogger.info(this, "Nombre adresses UPDATED : " + iUpdatedAdresse);
        JadeLogger.info(this, "Nombre adresses ERROR : " + iErrorAdresse);
        JadeLogger.info(this, "Nombre adresses TOTAL : " + iTotalAdresse);
        JadeLogger.info(this, "                                                             ");
        JadeLogger.info(this, "> End   process                                              ");
        JadeLogger.info(this, "                                                             ");

        // Redirection des messages de la console vers les fichiers
        try {
            System.setOut(savedOut);
            System.setErr(savedError);
            JadeLogger.info(this, "Console réactivée. ");
        } catch (Exception ex) {
            JadeLogger.error(this, "Erreur lors de la redirection des messages : " + ex.toString());
        }

        // Mise à jour du status à stopped
        try {
            updateInnerMF("0", "1");
        } catch (Exception ex) {
            JadeLogger.error(this, "Error setting inner mf 0 1 : " + ex.toString());
        }
        // Send completion mail
        try {
            ArrayList<String> emailAdresses = new ArrayList<String>();
            emailAdresses.add(getSession().getUserEMail());
            sendCompletionMail(emailAdresses);
        } catch (Exception e) {
            JadeLogger.error(this, "Error sending completion mail : " + e.toString());
        }
        // relâche des fichiers
        AMRepriseTiersStatusFileHelper.getInstance(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_TIERS).closeFile();
        AMRepriseTiersStatusFileHelper.getInstance(AMRepriseTiersStatusFileHelper.STATUS_FILE_ERROR_ADRESSES)
                .closeFile();
        AMRepriseTiersStatusFileHelper.getInstance(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_ADRESSES).closeFile();
        AMRepriseTiersStatusFileHelper.getInstance(AMRepriseTiersStatusFileHelper.STATUS_FILE_NEW_TIERS).closeFile();
        AMRepriseTiersStatusFileHelper.getInstance(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_ADRESSES)
                .closeFile();
        AMRepriseTiersStatusFileHelper.getInstance(AMRepriseTiersStatusFileHelper.STATUS_FILE_UPDATED_TIERS)
                .closeFile();

    }

    /**
     * @param askToStop
     *            the askToStop to set
     */
    public void setAskToStop(Boolean askToStop) {
        this.askToStop = askToStop;
    }

    /**
     * @param checkDataBeforeReprise
     *            the checkDataBeforeReprise to set
     */
    public void setCheckDataBeforeReprise(Boolean checkDataBeforeReprise) {
        this.checkDataBeforeReprise = checkDataBeforeReprise;
    }

    /**
     * @param gestionTiers
     *            the gestionTiers to set
     */
    public void setGestionTiers(AMGestionTiers gestionTiers) {
        this.gestionTiers = gestionTiers;
    }

    /**
     * @param isSimulation
     *            the isSimulation to set
     */
    public void setIsSimulation(Boolean isSimulation) {
        this.isSimulation = isSimulation;
    }

    /**
     * Mise à jour de la table rp_inner_mf
     * 
     * @param idFamille
     * @param idTiers
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws Exception
     */
    public void updateInnerMF(String idFamille, String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {

        JadeLogger.info(null, "Synchronized : " + idFamille);
        synchronized (this) {
            try {
                JadeLogger.info(null, "Done : " + idFamille);
                if (idFamille.equals("0") && idTiers.equals("0")) {
                    JadeLogger.error(this, "ID FAMILLE ET ID TIERS à 0");
                    return;
                }
                SimpleInnerMFRepriseSearch innerMFSearch = new SimpleInnerMFRepriseSearch();
                innerMFSearch.setForIdInnerMF(idFamille);
                innerMFSearch = AmalServiceLocator.getContribuableRepriseService().search(innerMFSearch);
                if (innerMFSearch.getSize() < 1) {
                    SimpleInnerMFReprise innerMF = new SimpleInnerMFReprise();
                    innerMF.setId(idFamille);
                    innerMF.setIdTiers(idTiers);
                    AmalServiceLocator.getContribuableRepriseService().create(innerMF);
                } else {
                    SimpleInnerMFReprise innerMFFound = (SimpleInnerMFReprise) innerMFSearch.getSearchResults()[0];
                    if (idTiers.equals("0") && !idFamille.contains("-") && !idFamille.equals("0")) {
                        innerMFFound = AmalServiceLocator.getContribuableRepriseService().deleteSimpleInnerMF(
                                innerMFFound);
                    } else {
                        innerMFFound.setIdTiers(idTiers);
                        innerMFFound = AmalServiceLocator.getContribuableRepriseService().updateSimpleInnerMF(
                                innerMFFound);
                    }
                }
                JadeThread.commitSession();
            } catch (Exception ex) {
                JadeLogger.error(this, "Error updating RP_INNER_MF" + idFamille + " " + idTiers);
                ex.printStackTrace();
            } finally {
                JadeThread.logClear();
            }
        }
    }

    /**
     * 
     * @param contribuable
     */
    public SimpleContribuableReprise updateSimpleContribuableReprise(SimpleContribuableReprise contribuable) {
        synchronized (this) {
            try {
                contribuable = AmalServiceLocator.getContribuableRepriseService()
                        .updateSimpleContribuable(contribuable);
                JadeThread.commitSession();
            } catch (Exception ex) {
                JadeLogger.error(this,
                        "Error updating RP_AMAL_MACONTRI" + contribuable.getId() + " " + contribuable.getIdTiers());
                ex.printStackTrace();
            } finally {
                JadeThread.logClear();
            }
            return contribuable;
        }

    }

    /**
     * 
     * @param famille
     * @return
     */
    public SimpleFamilleReprise updateSimpleFamilleReprise(SimpleFamilleReprise famille) {

        synchronized (this) {
            try {
                famille = AmalServiceLocator.getContribuableRepriseService().updateSimpleFamille(famille);
                JadeThread.commitSession();
            } catch (Exception ex) {
                JadeLogger.error(this,
                        "Error updating RP_AMAL_MAFAMILL" + famille.getIdFamille() + " " + famille.getIdTiers());
                ex.printStackTrace();
            } finally {
                JadeThread.logClear();
            }
            return famille;
        }
    }

    /**
     * Ecriture des fichiers de status
     * 
     * @param statusFileType
     * @param message
     */
    public void writeStatusFile(String statusFileType, String message) {
        synchronized (this) {
            try {
                AMRepriseTiersStatusFileHelper.getInstance(statusFileType).writeStatus(statusFileType, message);
            } catch (Exception ex) {
                JadeLogger.error(this, "Error writing status File : " + statusFileType + "_" + message + ex.toString());
            }
        }
    }
}
