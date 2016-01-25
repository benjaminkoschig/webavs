package globaz.pegasus.process.decision;

import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.lot.ComptabiliserProcessMailHandler;
import globaz.pegasus.process.lot.ComptabiliserProcessMailHandler.PROCESS_TYPE;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.decision.DecisionBuilder;
import ch.globaz.pegasus.businessimpl.services.models.decision.DACPublishHandler;
import ch.globaz.pegasus.businessimpl.services.models.decision.ged.DACGedHandler;

/**
 * Processus de mise en GED des décisions aprèsCalul PC A pour vocation à être lancé en <b>ligne de commande</b>. Le
 * processus applicatif, est lui, lancé, avec le processus de comptabilisation des décisions.<br/>
 * Utilisation en ligne de commande:<br/>
 * classname=globaz.pegasus.process.decision.PCImprimerDecisionProcessForLot</br> user=[user]<br/>
 * password=[pass]<br/>
 * application="PEGASUS"<br/>
 * idLot=[identifian du lot, ou identifiant de plusieurs lots séparés par des ,]
 * 
 * @see globaz.pegasus.process.lot.PCComptabiliserProcess
 * @author sce
 * 
 */
public class PCImprimerDecisionProcessForLot extends AbstractJadeJob {

    private static final long serialVersionUID = 8731586547381408153L;
    /* Liste d'identifiant effectivement à traiter */
    List<String> identifiantDeLotsATraiter = new ArrayList<String>();

    /* paramètre passé en ligne de commande, format possible: un entier, ou x,x,x, */
    private String idLot = null;

    public void addError(Exception e, String[] param) throws JadeNoBusinessLogSessionError {

        JadeThread.logError("", (e.getMessage() != null) ? e.getMessage() : e.toString(), param);
        String cause = "";
        Throwable currentException = null;
        JadeBusinessMessage message = null;
        currentException = e;
        while (currentException.getCause() != null) {
            message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, getName(), currentException.getCause()
                    .toString());
            getLogSession().addMessage(message);
            currentException = currentException.getCause();
        }
        message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, getName(), e.toString() + " " + cause);
        getLogSession().addMessage(message);
    }

    /**
     * Génération des documents présents dans le container. Le but est de créer une publication, donc une tâche
     * d'impression par décisions présente dans le handler
     * 
     * @param publisherHandler
     *            le handler contenant les documents
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private void createDecisionsByLot(DACPublishHandler publisherHandler) throws JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {
        for (JadePrintDocumentContainer containerGed : publisherHandler.getContainersGed()) {
            this.createDocuments(containerGed);
        }
    }

    private void generateDecisionsForLot(String idLot) throws Exception {

        String exceptionMsg = null;
        String dateDoc = JadeDateUtil.getGlobazFormattedDate(new Date());
        String mail = getSession().getUserEMail();

        try {

            ArrayList<String> listeIdDecisionToPrint = getIdsDecisionToPrintForlot(idLot);
            String loggedUser = getSession().getUserId();

            DACGedHandler globalLotGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLot, loggedUser,
                    getSession(), listeIdDecisionToPrint);

            DecisionBuilder decBuilder = null;
            ArrayList<String> idsDecisions;

            for (String idDecs : listeIdDecisionToPrint) {
                decBuilder = PegasusServiceLocator.getDecisionBuilderProvderService().getBuilderFor(
                        DecisionTypes.DECISION_APRES_CALCUL);

                idsDecisions = new ArrayList<String>();
                idsDecisions.add(idDecs);

                DACGedHandler gedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLot, loggedUser,
                        getSession(), idsDecisions);
                DACPublishHandler publisherHandlerForLot = DACPublishHandler.getInstanceForGedPrintOnly(idsDecisions,
                        mail, dateDoc, loggedUser, gedHandler);
                decBuilder.build(publisherHandlerForLot);
                // Création des décisions
                createDecisionsByLot(publisherHandlerForLot);

            }

            sendProcessMail(PROCESS_TYPE.MISE_EN_GED, globalLotGedHandler, mail);

        } catch (Exception e) {
            addError(e, null);
            JadeLogger.error(this, e.getMessage());
            exceptionMsg = e.getMessage();
        } finally {
            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                JadeSmtpClient.getInstance().sendMail(mail, getSession().getLabel("IMPRIMER_DECISION_ERREUR"),
                        exceptionMsg, null);
            }
        }

    }

    @Override
    public String getDescription() {
        return "Processus de mise en GED des décisions aprèsCalcul pour un lot donné";
    }

    public String getIdLot() {
        return idLot;
    }

    private ArrayList<String> getIdsDecisionToPrintForlot(String idLot) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PrestationException {

        // liste des prestations
        ArrayList<String> listeIdsPrestations = new ArrayList<String>();
        PrestationSearch prestSearch = new PrestationSearch();
        prestSearch.setForIdLot(idLot);
        prestSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        // iteration sur les résultats
        for (JadeAbstractModel model : PegasusServiceLocator.getPrestationService().search(prestSearch)
                .getSearchResults()) {
            listeIdsPrestations.add(((Prestation) model).getId());
        }

        // liste des idDecisions
        ArrayList<String> idsDecisionAc = new ArrayList<String>();

        if (listeIdsPrestations.size() > 0) {

            DecisionApresCalculSearch dacSearch = new DecisionApresCalculSearch();
            dacSearch.setForIdPrestationsIn(listeIdsPrestations);
            dacSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            dacSearch.setWhereKey(DecisionApresCalculSearch.FOR_MISE_EN_GED_COMPTA_WHERE_KEY);

            for (JadeAbstractModel model : PegasusServiceLocator.getDecisionApresCalculService().search(dacSearch)
                    .getSearchResults()) {
                idsDecisionAc.add(((DecisionApresCalcul) model).getSimpleDecisionApresCalcul()
                        .getIdDecisionApresCalcul());
            }
        }

        return idsDecisionAc;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    private final JadeContext initContext(BSession session) throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        return ctxtImpl;
    }

    private void initParameters() {

        String[] identifiantsDeLot = idLot.split(",");

        for (String id : identifiantsDeLot) {
            if ((null != id) && JadeStringUtil.isDigit(id)) {
                identifiantDeLotsATraiter.add(id);
            }
        }

    }

    @Override
    public void run() {

        try {
            initParameters();
            validateParameter();
            JadeThreadActivator.startUsingJdbcContext(this, initContext(getSession()));
            for (String idLot : identifiantDeLotsATraiter) {
                generateDecisionsForLot(idLot);
            }
        } catch (Exception e) {
            JadeLogger.error(this, "The process cannot be terminated due to an unexpected error: ");
            JadeLogger.error(this, e);
            try {
                JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(),
                        getSession().getLabel("IMPRIMER_DECISION_ERREUR"), e.getMessage(), null);
            } catch (Exception ex) {
                JadeLogger.error(this, "Problem happening when sending error messages...." + ex.getMessage());
            }
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }

    }

    /**
     * Envoi du mail suite au process
     * 
     */
    private final void sendProcessMail(PROCESS_TYPE process, DACGedHandler gedHandler, String mailGest)
            throws Exception {
        List<String> mails = new ArrayList<String>();
        if (mailGest == null) {
            mails.add(getSession().getUserEMail());
        } else {
            mails.add(mailGest);
        }

        ComptabiliserProcessMailHandler handler = new ComptabiliserProcessMailHandler(process, gedHandler.getLot(),
                getSession(), getLogSession(), gedHandler);

        handler.sendMail(mails);
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * Validation des paramètres d'entrée<br/>
     * Consiste en la validation de l'identifiant du lot concerné
     * 
     * @throws DecisionException
     *             l'exception levé si l'id est null ou n'est pas un entier
     */
    private void validateParameter() throws DecisionException {

        if (identifiantDeLotsATraiter.size() == 0) {
            throw new DecisionException("The id(s) passed must is(are) not valid integer id(s)");
        }

    }

}
