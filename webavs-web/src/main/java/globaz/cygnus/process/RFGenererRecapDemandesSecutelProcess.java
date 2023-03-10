package globaz.cygnus.process;

import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.exceptions.JadeProcessException;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.cygnus.topaz.recapitulationImportationSecutel.RFGenererRecapImportationSecutelServiceOO;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.print.server.JadePrintDocumentContainer;

import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * @author mbo / 01.07.2013
 * 
 */
public class RFGenererRecapDemandesSecutelProcess extends AbstractJadeJob {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private DocumentData docData = new DocumentData();
    private String emailAdress = "";
    private String idGestionnaire = "";
    private boolean isMiseEnGed = Boolean.FALSE;
    private FWMemoryLog memoryLog = new FWMemoryLog();
    private String numAf = "";
    private Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitementMap = null;
    BSession session = null;

    private JadeProcessStep step = null;

    /**
     * Constructeur
     *
     * @throws Exception
     */
    public RFGenererRecapDemandesSecutelProcess(String emailAdress, boolean isMiseEnGed,
                                                Map<String, ArrayList<RFImportDemandesCmsData>> regroupementDemandesParCodeTraitementMap,
                                                FWMemoryLog memoryLog, BSession session, JadeProcessStep step, String numAf) {
        super();
        this.emailAdress = emailAdress;
        idGestionnaire = session.getUserId();
        this.isMiseEnGed = isMiseEnGed;
        this.regroupementDemandesParCodeTraitementMap = regroupementDemandesParCodeTraitementMap;
        this.memoryLog = memoryLog;
        this.session = session;
        this.step = step;
        this.numAf = numAf;
        checkParams();
    }

    /**
     * Methode qui permet de controler si les param?tres re?us sont NULL
     */
    private void checkParams() {

        if (emailAdress == null) {
            throw new IllegalArgumentException("RFGenererRecapDemandesSecutelProcess - The 'emailAdress' can't be null");
        }
        if (idGestionnaire == null) {
            throw new IllegalArgumentException(
                    "RFGenererRecapDemandesSecutelProcess - The 'idGestionnaire' can't be null");
        }
        if (regroupementDemandesParCodeTraitementMap == null) {
            throw new IllegalArgumentException(
                    "RFGenererRecapDemandesSecutelProcess - The 'regroupementDemandesParTypes' can't be null");
        }
        if (memoryLog == null) {
            throw new IllegalArgumentException("RFGenererRecapDemandesSecutelProcess - The 'memoryLog' can't be null");
        }
        if (session == null) {
            throw new IllegalArgumentException("RFGenererRecapDemandesSecutelProcess - The 'session' can't be null");
        }
        if (step == null) {
            throw new IllegalArgumentException("RFGenererRecapDemandesSecutelProcess - The 'step' can't be null");
        }
        if (numAf == null) {
            throw new IllegalArgumentException("RFGenererRecapDemandesSecutelProcess - The 'idCms' can't be null");
        }

    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * Methode d'initialisation de la transaction
     * 
     * @throws Exception
     */
    private void initTransaction() throws Exception {
        BITransaction transaction = new BTransaction(getSession());
        transaction.openTransaction();
        setTransaction((BTransaction) transaction);
    }

    public boolean isMiseEnGed() {
        return isMiseEnGed;
    }

    /**
     * Methode qui va cr?er le document en appelant une instance du serviceOO
     * 
     * @return container
     * @throws Exception
     */
    private JadePrintDocumentContainer preparerDocumentOO() throws Exception {

        RFGenererRecapImportationSecutelServiceOO documentOO = new RFGenererRecapImportationSecutelServiceOO();
        return documentOO.preparerContainerRecapSecutel(regroupementDemandesParCodeTraitementMap, memoryLog,
                isMiseEnGed, session, step, emailAdress, numAf);
    }

    @Override
    public void run() {
        try {
            // Initialistation de la transaction
            initTransaction();

            // Construction du document
            this.createDocuments(preparerDocumentOO());

        } catch (Exception e) {
            new JadeProcessException("RFGenererRecapDemandesSecutelProcess_run() : " + e.getMessage());
        }
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setMiseEnGed(boolean isMiseEnGed) {
        this.isMiseEnGed = isMiseEnGed;
    }

}
