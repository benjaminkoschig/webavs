package ch.globaz.al.api.facturation;

import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.context.exception.BJadeMultipleJdbcConnectionInSameThreadException;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.prestations.ALCompensationPrestationException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Implémentation de l'interface permettant l'utilisation des services AF de compensation sur facture depuis le module
 * de facturation de Web@AVS
 * 
 * @author jts
 * 
 */
public class ALFacturationInterfaceImpl implements ALFacturationInterface {

    @Override
    public String getNumProcessusAFLie(String idPassage, BTransaction transaction) throws JadeApplicationException,
            JadePersistenceException, ArrayIndexOutOfBoundsException {
        ProcessusPeriodiqueSearchModel searchProcessusPassage = new ProcessusPeriodiqueSearchModel();
        searchProcessusPassage.setForIdPassageFactu(idPassage);
        // TODO: logger
        // logger.getErrorsLogger(this.numeroPassage, this.numeroPassage).addMessage(
        // new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ALFacturationProcess.class
        // .getName(), "plusieurs processus de facturation AF liés à ce passage"));
        // this.getTransaction().rollback();
        // hasError = true;

        ProcessusPeriodiqueModel processusAFLie = null;
        String numProcessusAF = "0";
        try {
            BJadeThreadActivator.startUsingContext(transaction);
            processusAFLie = (ProcessusPeriodiqueModel) ALServiceLocator.getProcessusPeriodiqueModelService()
                    .search(searchProcessusPassage).getSearchResults()[0];
            numProcessusAF = processusAFLie.getIdProcessusPeriodique();

        } catch (ArrayIndexOutOfBoundsException e1) {
            throw new ALCompensationPrestationException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "FACTURATION_AF_PROCESSUS_NON_COMPLETE"), e1);
        } catch (Exception e2) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getNumProcessusAFLie : ", e2);
        } finally {
            BJadeThreadActivator.stopUsingContext(transaction);
        }

        return numProcessusAF;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.api.facturation.ALFacturationInterface#getPeriode(globaz .globall.db.BTransaction)
     */
    @Override
    public String getPeriode(BTransaction transaction) throws JadeApplicationException {

        if (transaction == null) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getPeriode : transaction is null");
        }

        try {
            BJadeThreadActivator.startUsingContext(transaction);

            PeriodeAFModel periode = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeEnCours(
                    ALCSPrestation.BONI_INDIRECT, true);

            return periode.getDatePeriode();
        } catch (BJadeMultipleJdbcConnectionInSameThreadException e) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#getPeriode (BJadeMultipleJdbcConnectionInSameThreadException)", e);
        } catch (SQLException e) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getPeriode (SQLException) ", e);
        } catch (JadePersistenceException e) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#getPeriode : unable to get periode", e);
        } finally {
            BJadeThreadActivator.stopUsingContext(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.api.facturation.ALFacturationInterface#getProtocoles(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String, ch.globaz.al.business.loggers.ProtocoleLogger,
     * globaz.globall.db.BTransaction)
     */
    @Override
    public DocumentDataContainer getProtocoles(String idPassage, String dateFacturation, String periode,
            String typeCoti, String email, ProtocoleLogger logger, BTransaction transaction)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(idPassage)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#getProtocoles : idPassage is null, zero or empty");
        }

        if (!JadeDateUtil.isGlobazDate(dateFacturation)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#getProtocoles : dateFacturation is not a valid date");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#getProtocoles : dateFacturation is not a valid date");
        }

        if (JadeStringUtil.isBlank(email)) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getProtocoles"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.param.undefined", new String[] { "email" }));
        }

        if (logger == null) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getProtocoles : logger is null");
        }

        if (transaction == null) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#getProtocoles : transaction is null");
        }

        try {
            BJadeThreadActivator.startUsingContext(transaction);

            if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                    && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                    && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
                throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getProtocoles : " + typeCoti
                        + " is not valid");
            }

            return ALServiceLocator.getCompensationFactureProtocolesService().genererProtocolesDefinitif(idPassage,
                    dateFacturation, periode, typeCoti, email, logger);
        } catch (Exception e) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getProtocoles : " + e.getMessage(),
                    e);
        } finally {
            BJadeThreadActivator.stopUsingContext(transaction);
        }
    }

    /**
     * Retourne l'objet JadePublishDocumentInfo nécessaire à la publication de l'éventuel protocole CSV
     * 
     * @param idPassage
     * @param periode
     * @param typeCoti
     * @return
     * @throws ALCompensationPrestationException
     */
    // FIXME: a quoi sert la map params ?? / 14.02.2014
    public JadePublishDocumentInfo getPubInfoProtocoleCSV(String idPassage, String periode, String typeCoti,
            BTransaction transaction) throws ALCompensationPrestationException {

        try {
            BJadeThreadActivator.startUsingContext(transaction);
            // paramètres

            StringBuffer sbTitreMail = new StringBuffer();

            HashMap<String, String> params = new HashMap<String, String>();
            params.put(ALConstProtocoles.INFO_PASSAGE, idPassage);
            if (ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)) {
                params.put(ALConstProtocoles.INFO_PROCESSUS,
                        JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PAR));
                sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PAR))
                        .append(", ");
            } else if (ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)) {
                params.put(ALConstProtocoles.INFO_PROCESSUS,
                        JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS));
                sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS))
                        .append(", ");
            } else {
                params.put(ALConstProtocoles.INFO_PROCESSUS,
                        JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION));
                sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_PROCESSUS_COMPENSATION))
                        .append(", ");
            }

            sbTitreMail.append(JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_TRAITEMENT_COMPENSATION)).append(
                    " ");
            params.put(ALConstProtocoles.INFO_TRAITEMENT,
                    JadeCodesSystemsUtil.getCodeLibelle(ALCSProcessus.NAME_TRAITEMENT_COMPENSATION));
            params.put(ALConstProtocoles.INFO_PERIODE, periode);

            JadePublishDocumentInfo pubInfoCSV = new JadePublishDocumentInfo();
            pubInfoCSV.setOwnerEmail(JadeThread.currentUserEmail());
            pubInfoCSV.setOwnerId(JadeThread.currentUserId());

            pubInfoCSV.setDocumentTitle(sbTitreMail.toString()
                    + JadeThread.getMessage("al.protocole.compensation.publication.titreListeAllocataires"));
            pubInfoCSV.setDocumentSubject(sbTitreMail.toString()
                    + JadeThread.getMessage("al.protocole.compensation.publication.titreListeAllocataires"));
            pubInfoCSV.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));

            return pubInfoCSV;
        } catch (Exception e) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getPubInfoProtocoleCSV : "
                    + e.getMessage(), e);
        } finally {
            BJadeThreadActivator.stopUsingContext(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.api.facturation.ALFacturationInterface#getRecaps(java.lang .String, java.lang.String,
     * globaz.globall.db.BTransaction)
     */
    @Override
    public Collection<CompensationBusinessModel> getRecaps(String idProcessus, String periodeA, String typeCoti,
            BTransaction transaction) throws JadeApplicationException,
            BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadePersistenceException {

        if (!JadeNumericUtil.isNumeric(idProcessus)) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getRecaps : " + idProcessus
                    + " is not a valid num processus");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getRecaps : " + periodeA
                    + " is not a valid period (MM.YYYY)");
        }

        if (transaction == null) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getRecaps : transaction is null");
        }

        Collection<CompensationBusinessModel> recaps = null;

        try {
            BJadeThreadActivator.startUsingContext(transaction);

            if (!ALConstPrestations.TYPE_INDIRECT_GROUPE.equals(typeCoti)
                    && !ALConstPrestations.TYPE_COT_PAR.equals(typeCoti)
                    && !ALConstPrestations.TYPE_COT_PERS.equals(typeCoti)) {
                throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getRecaps : " + typeCoti
                        + " is not valid");
            }
            if (JadeNumericUtil.isIntegerPositif(idProcessus)) {
                recaps = ALServiceLocator.getCompensationFactureBusinessService().loadRecapsPrepareesByNumProcessus(
                        idProcessus, typeCoti);
            } else {
                recaps = ALServiceLocator.getCompensationFactureBusinessService().loadRecapsPreparees(periodeA,
                        typeCoti);
            }

        } catch (Exception e) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#getRecaps : " + e.getMessage(), e);
        } finally {
            BJadeThreadActivator.stopUsingContext(transaction);
        }

        return recaps;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.api.facturation.ALFacturationInterface#rollbackRecaps(java .lang.String,
     * globaz.globall.db.BTransaction)
     */
    @Override
    public void rollbackRecaps(String idPassage, BTransaction transaction)
            throws BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadeApplicationException,
            JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(idPassage)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#rollbackRecaps : idPassage is null, zero or empty");
        }

        if (transaction == null) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#rollbackRecaps : transaction is null");
        }

        try {
            BJadeThreadActivator.startUsingContext(transaction);

            ALServiceLocator.getCompensationFactureBusinessService().restoreEtatPrestations(idPassage);

        } catch (Exception e) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#rollbackRecaps : ", e);
        } finally {
            BJadeThreadActivator.stopUsingContext(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.api.facturation.ALFacturationInterface#updateRecap(java. lang.String, java.lang.String,
     * java.lang.String, globaz.globall.db.BTransaction)
     */
    @Override
    public void updateRecap(String idRecap, String idPassage, String date, BTransaction transaction)
            throws BJadeMultipleJdbcConnectionInSameThreadException, SQLException, JadeApplicationException,
            JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(idRecap)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#updateRecap : idRecap is null, zero or empty");
        }

        if (!JadeNumericUtil.isIntegerPositif(idPassage)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#updateRecap : idPassage is null, zero or empty");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#updateRecap : date is not a valid date");
        }

        if (transaction == null) {
            throw new ALCompensationPrestationException("ALFacturationInterfaceImpl#updateRecap : transaction is null");
        }

        try {
            BJadeThreadActivator.startUsingContext(transaction);

            ALServiceLocator.getCompensationFactureBusinessService().updatePrestationsCompensees(idRecap, date,
                    idPassage);

        } catch (Exception e) {
            throw new ALCompensationPrestationException(
                    "ALFacturationInterfaceImpl#updateRecap : unable to update recap", e);
        } finally {
            BJadeThreadActivator.stopUsingContext(transaction);
        }
    }
}
