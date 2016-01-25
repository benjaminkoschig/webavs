package ch.globaz.al.facturation.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionInfo;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.client.JadePrintServerFacade;
import globaz.jade.print.message.JadePrintTaskDefinition;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.util.FAUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.osiris.api.APISection;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import ch.globaz.al.api.facturation.ALFacturationInterfaceImpl;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

public class ALFacturationProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Date comptable
     */
    private String dateComptable = null;
    private String idModuleFacturation = "";

    /**
     * id du type de facturation
     */
    private String idTypeFacturation = null;

    /**
     * Numéro de passage
     */
    private String numeroPassage = null;
    /**
     * période comptable du journal incluant le module AF
     */
    private String periodeComptable = null;

    /**
     * définit le type de cotisation à prendre en compte - par défaut 0 => les 2
     */
    private String typeCoti = null;

    @Override
    protected void _executeCleanUp() {
        // DO NOTHING
    }

    @Override
    public boolean _executeProcess() throws Exception {

        boolean hasError = false;
        String errorMessage = null;
        ALFacturationInterfaceImpl factuAPI = new ALFacturationInterfaceImpl();
        ProtocoleLogger logger = new ProtocoleLogger();
        String numProcessusAF = "0";
        String periode = "";
        if (FAPassage.CS_TYPE_PERIODIQUE.equals(idTypeFacturation)) {
            periode = periodeComptable;
        } else {
            periode = dateComptable.substring(3);

            numProcessusAF = factuAPI.getNumProcessusAFLie(numeroPassage, getTransaction());
        }

        FAEnteteFacture enteteFacture = null;
        BigDecimal totalAF = new BigDecimal(0);

        try {

            Collection<CompensationBusinessModel> recaps = factuAPI.getRecaps(numProcessusAF, periode, typeCoti,
                    getTransaction());
            Collection<CompensationBusinessModel> recapsProtocoles = new ArrayList<CompensationBusinessModel>();
            int count = 0;

            Iterator<CompensationBusinessModel> it = recaps.iterator();
            while (it.hasNext()) {

                try {

                    CompensationBusinessModel recap = it.next();

                    IAFAffiliation affiliationAF = getAffiliationFacturation(recap.getNumeroAffilie());

                    if (affiliationAF == null) {
                        logger.getErrorsLogger(recap.getIdRecap(), recap.getNumeroAffilie()).addMessage(
                                new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ALFacturationProcess.class
                                        .getName(), "impossible de récupérer l'affiliation pour "
                                        + recap.getNumeroAffilie()));

                        getTransaction().rollback();
                        logger.addFatalError(new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                                .getName(), "impossible de récupérer l'affiliation pour " + recap.getNumeroAffilie()));
                        errorMessage = "impossible de récupérer l'affiliation pour un ou plusieurs affilié, veuillez consulter le protocole d'erreurs pour plus d'informations";
                        hasError = true;
                    } else {

                        enteteFacture = FAUtil.getEnteteFacture(
                                numeroPassage,
                                affiliationAF.getIdTiers(),
                                CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                                        getSession().getApplication()), affiliationAF.getAffilieNumero(),
                                APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION, recap.getNumeroFacture(), getSession(),
                                getTransaction());
                        createLigneFacture(recap, enteteFacture, affiliationAF);
                        factuAPI.updateRecap(recap.getIdRecap(), numeroPassage, dateComptable, getTransaction());
                        getTransaction().commit();

                        totalAF = totalAF.add(recap.getMontant());
                        recapsProtocoles.add(recap);
                    }
                } catch (Exception e) {
                    getTransaction().rollback();
                    errorMessage = "Une erreur s'est produite pendant le traitement d'une ou plusieurs récaps, veuillez consulter le protocole d'erreurs pour plus d'informations";
                    hasError = true;
                    logger.addFatalError(new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                            .getName(), e.getMessage()));
                }
                count++;
            }

            if (recaps.size() > 0) {

                try {
                    BigDecimal sommeMUSCA = retrieveSommeMUSCA(numeroPassage);
                    if (totalAF.compareTo(sommeMUSCA) != 0) {
                        logger.getErrorsLogger(String.valueOf(count), "Total").addMessage(
                                new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ALFacturationProcess.class
                                        .getName(), "Le montant total AF <--> Facturation ne correspond pas (AF : "
                                        + totalAF.toPlainString() + ", MUSCA : " + sommeMUSCA.toPlainString() + ")"));
                        errorMessage = "Le montant total AF <--> Facturation ne correspond pas (AF : "
                                + totalAF.toPlainString() + ", MUSCA : " + sommeMUSCA.toPlainString() + ")";
                        hasError = true;
                    }
                } catch (Exception e) {
                    logger.addFatalError(new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, this.getClass()
                            .getName(), e.getMessage()));
                    errorMessage = e.getMessage();
                    hasError = true;
                }

            } else {
                // TODO : gérer les cas où aucune prestation n'a été récupérée (envoi d'un e-mail)
            }

            publishProtocoles(factuAPI, logger, periode);
            // this.removeWarnings();

            if (getTransaction().hasErrors() || hasError) {
                getTransaction().rollback();
                getTransaction().addErrors(errorMessage);
                getMemoryLog().logMessage(errorMessage, FWViewBeanInterface.ERROR, this.getClass().getName());
                return false;
            } else {
                getTransaction().commit();
                return true;
            }

        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
            return false;
        }
    }

    private void createLigneFacture(CompensationBusinessModel recap, FAEnteteFacture enteteFacture,
            IAFAffiliation affiliationAF) throws Exception {

        // On reset le buffer d'erreur de la transaction
        getTransaction().clearErrorBuffer();

        FAAfact aFact = new FAAfact();

        aFact.setISession(getSession());
        aFact.setIdEnteteFacture(enteteFacture.getIdEntete());
        aFact.setIdPassage(numeroPassage);
        aFact.setIdModuleFacturation(getIdModuleFacturation());

        aFact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        aFact.setNonImprimable(Boolean.FALSE);
        aFact.setNonComptabilisable(Boolean.FALSE);
        aFact.setAQuittancer(Boolean.FALSE);
        aFact.setAnneeCotisation("");
        aFact.setIdExterneRubrique(recap.getNumeroCompte());
        // TODO:379
        // aFact.setReferenceRubrique

        aFact.setDebutPeriode(JACalendar.format(recap.getPeriodeRecapDe(), JACalendar.FORMAT_DDMMYYYY));
        JACalendarGregorian calendar = new JACalendarGregorian();
        aFact.setFinPeriode(calendar.lastInMonth(JACalendar.format(recap.getPeriodeRecapA(), JACalendar.FORMAT_DDMMYYYY)));
        aFact.setMontantFacture((recap.getMontant().negate()).toString());
        aFact.add(getTransaction());

        // erreur dans la transaction?
        if (getTransaction().hasErrors()) {
            throw new Exception("Erreur à l'enregistrement de l'AFact: " + getTransaction().getErrors().toString());
        }
    }

    /**
     * @param numeroAffilie
     *            Numéro de l'affilié
     * @return affiliation récupérée
     * @throws Exception
     */
    private IAFAffiliation getAffiliationFacturation(String numeroAffilie) throws Exception {
        Hashtable params = new Hashtable();
        params.put(IAFAffiliation.FIND_FOR_NOAFFILIE, numeroAffilie);

        IAFAffiliation affiliation = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
        IAFAffiliation affiliations[] = affiliation.findAffiliationAF(params);

        if ((affiliations != null) && (affiliations.length > 0)) {
            affiliation = affiliations[0];
            affiliation.setISession(getSession());
        } else {
            affiliation = null;
        }

        IAFAffiliation newAff = affiliation.getAffiliationFacturationAF(JACalendar.today().toString());
        // TODO: si succursale et que maison mère n'est pas active car pas de masse...mettre erreur
        if (newAff == null) {
            return affiliation;
        }

        return newAff;
    }

    public String getDateComptable() {
        return dateComptable;
    }

    @Override
    protected String getEMailObject() {
        // DO NOTHING
        return null;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getNumeroPassage() {
        return numeroPassage;
    }

    public String getPeriodeComptable() {
        return periodeComptable;
    }

    public String getTypeCoti() {
        return typeCoti;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // DO NOTHING
        return null;
    }

    private void publishProtocoles(ALFacturationInterfaceImpl factuAPI, ProtocoleLogger logger, String periode)
            throws Exception {
        // préparation du protocole
        DocumentDataContainer aContainer = null;
        try {
            aContainer = factuAPI.getProtocoles(numeroPassage, dateComptable, periode, typeCoti, getEMailAddress(),
                    logger, getTransaction());

            JadePrintDocumentContainer containerCopy;

            if (aContainer != null) {
                containerCopy = (JadePrintDocumentContainer) aContainer.getContainer().clone();
                JadePrintTaskDefinition printTask = new JadePrintTaskDefinition();
                printTask.setRunnableProperty("DOC_ALL", containerCopy);
                BSession session = getSession();
                BSessionInfo sessionInfo = new BSessionInfo();
                sessionInfo.setApplication(session.getApplicationId());
                sessionInfo.setUserId(session.getUserId());
                sessionInfo.setLanguageId(session.getIdLangue());
                sessionInfo.setLanguageISO(session.getIdLangueISO());
                printTask.setRunnableProperty("BSessionInfo", sessionInfo);
                JadePrintServerFacade.addPrintTask(printTask);
            }

        } catch (Exception e1) {
            JadeLogger.error(this, e1);
            JadeSmtpClient.getInstance().sendMail(
                    getEMailAddress(),
                    "Facturation AF : erreur pendant la génération du protocole PDF",
                    "Une erreur s'est produite pendant la génération du protocole de la compensation des prestations AF : "
                            + e1.getMessage(), null);
        }

        /*
         * création fichiers CSV si défini
         */
        if ((aContainer != null) && (aContainer.getDocumentCSV().size() > 0)) {
            try {
                String fileNameBase = new String("protocole_csv_%d.csv");
                int counter = 0;
                for (String csv : aContainer.getDocumentCSV()) {

                    if (!JadeStringUtil.isBlank(csv)) {
                        String fileName = Jade.getInstance().getPersistenceDir()
                                + String.format(fileNameBase, counter++);
                        FileOutputStream fichier = new FileOutputStream(fileName);

                        fichier.write(csv.getBytes());
                        fichier.flush();
                        fichier.close();

                        JadePublishDocumentInfo publishDocInfo = factuAPI.getPubInfoProtocoleCSV(numeroPassage,
                                periode, typeCoti, getTransaction());
                        publishDocInfo.setOwnerEmail(getEMailAddress());

                        JadePublishDocument docInfoCSV = new JadePublishDocument(fileName, publishDocInfo);
                        JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur lors de la génération du protocole CSV", e));
                JadeSmtpClient.getInstance().sendMail(
                        getSession().getUserEMail(),
                        "Facturation AF : erreur pendant la génération du protocole CSV",
                        "Une erreur s'est produite pendant la génération du protocole de la compensation des prestations AF : "
                                + e.getMessage(), null);
            }

        }
    }

    /**
     * Retire les messages de niveau WARN ou inférieur.
     * 
     * Une fois que les avertissements ont été traité il n'est pas utile de les conservés et peuvent perturber la suite
     * du traitement (la facturation considère les avertissements comme des erreur).
     */
    private void removeWarnings() {

        JadeBusinessMessage[] logMessages = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
        JadeThread.logClear();

        if (logMessages != null) {
            for (int i = 0; i < logMessages.length; i++) {

                if (logMessages[i].getLevel() >= JadeBusinessMessageLevels.ERROR) {
                    JadeThread.logError(logMessages[i].getSource(), logMessages[i].getMessageId(),
                            logMessages[i].getParameters());
                }
            }
        }
    }

    /**
     * Recheche la somme total des prestations passées à MUSCA
     * 
     * @return La somme des prestations
     */
    private BigDecimal retrieveSommeMUSCA(String numPassage) throws Exception {
        // montant de facturation MUSCA
        String montantTotal = null;
        String idModuleFact = null;
        // reset du buffer de transaction
        getTransaction().clearErrorBuffer();

        FAModulePassageManager factPassage = new FAModulePassageManager();
        FAAfactManager factManager = new FAAfactManager();

        // Recherche du numéro de module AF

        factPassage.setForIdTypeModule(getIdModuleFacturation());
        factPassage.setSession(getSession());
        factPassage.find(getTransaction(), 1);
        if (getTransaction().hasErrors()) {
            throw new Exception("Impossible de retrouver l'ID du module de facturation AF : "
                    + getTransaction().getErrors().toString());
        }

        try {
            montantTotal = factManager.getSumPassage(numPassage, getIdModuleFacturation(), getTransaction());
        } catch (Exception e) {
            montantTotal = null;
        }

        if (getTransaction().hasErrors() || (montantTotal == null)) {
            throw new Exception("Impossible de charger la facturation pour le passage ( " + numPassage + ", module"
                    + idModuleFact + ")" + getTransaction().getErrors().toString());
        }

        return new BigDecimal(montantTotal);
    }

    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public void setIdTypeFacturation(String idTypeFacturation) {
        this.idTypeFacturation = idTypeFacturation;
    }

    public void setNumeroPassage(String numeroPassage) {
        this.numeroPassage = numeroPassage;
    }

    public void setPeriodeComptable(String periodeComptable) {
        this.periodeComptable = periodeComptable;
    }

    public void setTypeCoti(String typeCoti) {
        this.typeCoti = typeCoti;
    }
}