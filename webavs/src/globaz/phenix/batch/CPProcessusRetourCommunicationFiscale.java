package globaz.phenix.batch;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPJournalRetourListViewBean;
import globaz.phenix.process.communications.CPProcessReceptionCommunication;
import globaz.phenix.process.communications.CPProcessReceptionGenererDecision;
import globaz.phenix.process.communications.CPProcessValidationSelectionJournalRetourViewBean;
import globaz.phenix.process.documentsItext.CPProcessImprimerDecisionAgence;
import java.io.File;

public class CPProcessusRetourCommunicationFiscale extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class FichierReception {
        public String chemin = "";
        public String csCanton = "";
        public String name = "";
    }

    private String erreurMessage = "";

    private String nomFichier = "";

    private String repertoire = "";

    /**
     * Constructor for CPCalculCIProcess.
     */
    public CPProcessusRetourCommunicationFiscale() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CPCalculCIProcess.
     */
    public CPProcessusRetourCommunicationFiscale(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CPCalculCIProcess.
     */
    public CPProcessusRetourCommunicationFiscale(globaz.globall.db.BSession session) {
        super(session);
    }

    public CPProcessusRetourCommunicationFiscale(String _repertoire) {
        super();
        repertoire = _repertoire;
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        boolean result = true;
        while (scanRepertoireReception() > 0) {
            IFAPassage passage = null;
            FichierReception fichier = null;
            try {
                System.out.println("CPProcessusRetourCommunicationFiscale : 1");
                // On va rechercher le premier fichier que l'on trouve
                fichier = getFichier();
            } catch (Exception e) {
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0168"),
                        globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(e.toString());
                result = false;
            }

            // On effectue la réception des communications fiscales
            try {
                System.out.println("CPProcessusRetourCommunicationFiscale : 2");
                if (result) {
                    if (fichier != null) {
                        if (!receptionFichier(fichier)) {
                            result = false;
                        }
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0169") + " "
                                + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(e.toString());
                result = false;
            }
            // On génère les décisions
            try {
                System.out.println("CPProcessusRetourCommunicationFiscale : 3");
                if (result) {
                    if (!generationDecisions(fichier)) {
                        result = false;
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0170") + " "
                                + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(e.toString());
                result = false;
            }
            // On valide les décisions
            try {
                System.out.println("CPProcessusRetourCommunicationFiscale : 4");
                if (result) {
                    passage = validationDecisions(fichier);
                    if (passage == null) {
                        result = false;
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0171") + " "
                                + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(e.toString());
                result = false;
            }
            try {
                System.out.println("CPProcessusRetourCommunicationFiscale : 5");
                // On imprime les décisions du passage
                if (passage != null) {
                    if (result) {
                        impressionDecisions(passage, fichier);
                    }
                }
                System.out.println("CPProcessusRetourCommunicationFiscale : 6");
                publishDocuments();
                waitForDocumentsPublication();
            } catch (Exception e) {
                result = false;
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0172") + " "
                                + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(e.toString());
            }
            // On copie le fichier dans le répertoire de réception et on
            // l'efface
            try {
                System.out.println("CPProcessusRetourCommunicationFiscale : 7");
                deplacementFichierReceptionne(fichier);
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0173") + " "
                                + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(e.toString());
                result = false;
            }
            // On envoi les erreurs
            if (getMemoryLog().hasMessages()) {
                System.out.println("CPProcessusRetourCommunicationFiscale : 8");
                try {
                    JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getSubject(),
                            getMemoryLog().getMessagesInString() + "\n" + getErreurMessage(), null);
                    wait(1000);
                } catch (Exception e) {
                }
            }
            if (!result) {
                break;
            }
        }
        return result;
    }

    @Override
    protected void _validate() throws Exception {
        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    private void deplacementFichierReceptionne(FichierReception fichier) {
        // si le fichier existe on le renomme !
        String repertoireR = "";
        File file_source = new File(fichier.chemin);
        repertoireR = getRepertoireReceptionne();

        File file_dest = new File(repertoireR + fichier.name);
        int i = 1;
        while (file_dest.exists()) {
            file_dest = new File(repertoireR + fichier.name + i);
            i++;
        }
        // On copie le fichier et on efface l'ancien
        file_source.renameTo(file_dest);
    }

    private boolean generationDecisions(FichierReception fichier) throws Exception {
        // Récupération du dernier journal traité
        CPJournalRetourListViewBean jrnManager = new CPJournalRetourListViewBean();
        jrnManager.setSession(getSession());
        jrnManager.setOrderBy("NUM");
        jrnManager.changeManagerSize(0);
        jrnManager.find();
        if (jrnManager.size() > 0) {
            CPJournalRetour journal = (CPJournalRetour) jrnManager.getFirstEntity();
            // Génération des décisions
            CPProcessReceptionGenererDecision processGen = new CPProcessReceptionGenererDecision();
            processGen.setSession(getSession());
            processGen.setEMailAddress(getEMailAddress());
            processGen.setIdJournal(journal.getIdJournalRetour());
            String[] typeGeneration = getTypeGeneration();
            for (int i = 0; i < typeGeneration.length; i++) {
                if (!JadeStringUtil.isEmpty(typeGeneration[i])) {
                    if (processGen.getForStatus().length() > 0) {
                        processGen.setForStatus(processGen.getForStatus() + "," + typeGeneration[i]);
                    } else {
                        processGen.setForStatus(typeGeneration[i]);
                    }
                }
            }
            processGen.executeProcess();
            if (processGen.getMemoryLog().hasMessages()) {
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0170") + " "
                                + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(processGen.getMemoryLog().getMessagesInString());
            }
            return !processGen.getMemoryLog().hasErrors();
        }
        return true;
    }

    private String getCantonDossier(String cantonParDefaut) {
        try {
            FWParametersSystemCodeManager mgr = new FWParametersSystemCodeManager();
            mgr.setSession(getSession());
            mgr.setForCodeUtilisateur(cantonParDefaut);
            mgr.setForIdLangue("F");
            mgr.setForIdTypeCode("10500005"); // famille des cantons
            mgr.find();
            FWParametersSystemCode code = (FWParametersSystemCode) mgr.getFirstEntity();
            return code.getId();
        } catch (Exception e) {
            return "";
        }
    }

    private String getCantonParDefaut() {
        try {
            return ((CPApplication) getSession().getApplication()).getCantonCaisse();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected String getEMailObject() {
        if (!getMemoryLog().hasMessages()) {
            return getSession().getLabel("CP_JOB_0154");
        } else {
            return getSession().getLabel("CP_JOB_0155");
        }
    }

    public String getErreurMessage() {
        return erreurMessage;
    }

    private FichierReception getFichier() throws Exception {
        try {
            FichierReception fichierReception = new FichierReception();
            repertoire = getRepertoire(repertoire);
            File f = new File(repertoire);
            File[] files = f.listFiles();

            for (int j = 0; j < files.length; j++) {
                File fichier = files[j];
                if (!fichier.isDirectory()) {
                    fichierReception.chemin = fichier.getPath();
                    fichierReception.csCanton = getCantonDossier(getCantonParDefaut());
                    fichierReception.name = fichier.getName();
                    return fichierReception;
                } else {
                    // On va ouvrir le repertoire
                    File f2 = new File(repertoire + fichier.getName());
                    File[] files2 = f2.listFiles();
                    for (int i = 0; i < files2.length; i++) {
                        File fichier2 = files2[i];
                        if (!fichier2.isDirectory()) {
                            fichierReception.chemin = fichier2.getPath();
                            fichierReception.csCanton = getCantonDossier(fichier.getName());
                            fichierReception.name = fichier2.getName();
                            return fichierReception;
                        }
                    }
                }
            }
            return fichierReception;
        } catch (Exception e) {
            throw e;
        }

    }

    public String getNomFichier() {
        return nomFichier;
    }

    private String getRepertoire(String repertoire) {
        if (JadeStringUtil.isEmpty(repertoire)) {
            try {
                repertoire = ((CPApplication) getSession().getApplication()).getRepertoireCommunicationsFiscales();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return repertoire;
    }

    private String getRepertoireReceptionne() {
        String repertoireR = "";
        try {
            repertoireR = ((CPApplication) getSession().getApplication())
                    .getRepertoireCommunicationsFiscalesReceptionnees();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repertoireR;
    }

    private String[] getTypeGeneration() {
        try {
            String[] typesGeneration = new String[10];
            String types = ((CPApplication) getSession().getApplication()).getTypesGeneration();
            int i = 0;
            while (types.indexOf(',') != -1) {
                typesGeneration[i++] = types.substring(0, types.indexOf(','));
                types = types.substring(types.indexOf(',') + 1, types.length());
            }
            typesGeneration[i] = types;
            return typesGeneration;
        } catch (Exception e) {
            return new String[0];
        }
    }

    private String[] getTypeValidation() {
        try {
            String[] typesValidation = new String[10];
            String types = ((CPApplication) getSession().getApplication()).getTypesValidation();
            int i = 0;
            while (types.indexOf(',') != -1) {
                typesValidation[i++] = types.substring(0, types.indexOf(','));
                types = types.substring(types.indexOf(',') + 1, types.length());
            }
            typesValidation[i] = types;
            return typesValidation;
        } catch (Exception e) {
            return new String[0];
        }
    }

    private void impressionDecisions(IFAPassage passage, FichierReception fichier) throws Exception {
        if (((CPApplication) getSession().getApplication()).isImpressionComFiscAuto()) {
            CPProcessImprimerDecisionAgence processImpression = new CPProcessImprimerDecisionAgence();
            processImpression.setSession(getSession());
            processImpression.setIdPassage(passage.getIdPassage());
            processImpression.setDateImpression(passage.getDateFacturation());
            processImpression.setEMailAddress(getEMailAddress());
            processImpression.setParent(this);
            processImpression.setAffichageEcran(Boolean.FALSE);
            processImpression.executeProcess();
            if (processImpression.getMemoryLog().hasErrors()) {
                getMemoryLog().logMessage(
                        getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0169") + " "
                                + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
                setErreurMessage(processImpression.getMemoryLog().getMessagesInString());
            }
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private boolean receptionFichier(FichierReception fichier) throws Exception {
        // Réception et chargement du fichier
        globaz.jade.fs.JadeFsFacade.copyFile("jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema()
                + "/" + fichier.name, fichier.chemin);
        CPProcessReceptionCommunication processReception = new CPProcessReceptionCommunication();
        processReception.setSession(getSession());
        processReception.setEMailAddress(getEMailAddress());
        processReception.setTypeReception("1");
        // Nom properties + nom fichier + date du jour
        String nom = ((CPApplication) getSession().getApplication()).getLibelleJournal();
        processReception.setLibelleJournal(nom + " " + fichier.name + " : " + JACalendar.todayJJsMMsAAAA());
        processReception.setInputFileName(fichier.name);
        processReception.setCsCanton(fichier.csCanton);
        processReception.executeProcess();
        if (processReception.getMemoryLog().hasMessages()) {
            getMemoryLog().logMessage(
                    getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0169") + " " + fichier.name
                            + "\n", globaz.framework.util.FWMessage.ERREUR, getClass().getName());
            setErreurMessage(processReception.getMemoryLog().getMessagesInString());
        }
        return !processReception.getMemoryLog().hasErrors();
    }

    private int scanRepertoireReception() {
        int nbreCommunications = 0;
        String repertoire = "";
        repertoire = getRepertoire(repertoire);
        File f = new File(repertoire);
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++) {
            File fichier = files[i];
            if (!fichier.isDirectory()) {
                nbreCommunications++;
            } else {
                // On va ouvrir le repertoire
                File f2 = new File(repertoire + fichier.getName());
                File[] files2 = f2.listFiles();
                for (int j = 0; j < files2.length; j++) {
                    File fichier2 = files2[j];
                    if (!fichier2.isDirectory()) {
                        nbreCommunications++;
                    }
                }
            }
        }
        return nbreCommunications;
    }

    public void setErreurMessage(String erreurMessage) {
        this.erreurMessage = erreurMessage;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    private IFAPassage validationDecisions(FichierReception fichier) throws Exception {
        // Recherche du prochain passage de décision cot. pers.
        // Récupération du dernier journal traité
        CPJournalRetourListViewBean jrnManager = new CPJournalRetourListViewBean();
        jrnManager.setSession(getSession());
        jrnManager.setOrderBy("NUM");
        jrnManager.changeManagerSize(0);
        jrnManager.find();
        if (jrnManager.size() > 0) {
            CPJournalRetour journal = (CPJournalRetour) jrnManager.getFirstEntity();
            IFAPassage passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                    FAModuleFacturation.CS_MODULE_COT_PERS);
            if (passage != null) {
                CPProcessValidationSelectionJournalRetourViewBean processValidation = new CPProcessValidationSelectionJournalRetourViewBean();
                processValidation.setSession(getSession());
                processValidation.setEMailAddress(getEMailAddress());
                processValidation.setIdJournalFacturation(passage.getIdPassage());
                processValidation.setForJournal(journal.getIdJournalRetour());
                String[] typeValidation = getTypeValidation();
                for (int i = 0; i < typeValidation.length; i++) {
                    if (!JadeStringUtil.isEmpty(typeValidation[i])) {
                        if (!JadeStringUtil.isEmpty(processValidation.getForStatus())) {
                            processValidation.setForStatus(processValidation.getForStatus() + "," + typeValidation[i]);
                        } else {
                            processValidation.setForStatus(typeValidation[i]);
                        }
                    }
                }
                processValidation.executeProcess();
                if (processValidation.getMemoryLog().hasMessages()) {
                    getMemoryLog()
                            .logMessage(
                                    getSession().getLabel("CP_MSG_0167") + getSession().getLabel("CP_MSG_0171") + " "
                                            + fichier.name + "\n", globaz.framework.util.FWMessage.ERREUR,
                                    getClass().getName());
                    setErreurMessage(processValidation.getMemoryLog().getMessagesInString());
                }
                return passage;
            } else {
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0153"), globaz.framework.util.FWMessage.ERREUR,
                        getClass().getName());
                return null;
            }
        } else {
            getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0153"), globaz.framework.util.FWMessage.ERREUR,
                    getClass().getName());
            return null;
        }
    }

}
