package ch.globaz.al.businessimpl.services.generation.prestations;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationPrestationsContextException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.generation.factures.NumeroFactureService;
import ch.globaz.al.business.services.generation.prestations.GenerationDossierService;
import ch.globaz.al.businessimpl.generation.prestations.GenPrestationADI;
import ch.globaz.al.businessimpl.generation.prestations.GenPrestationFactory;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextTucana;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.naos.business.data.AssuranceInfo;

/**
 * Implémentation du service de génération de prestations
 * 
 * @author jts
 */
public class GenerationDossierServiceImpl extends ALAbstractBusinessServiceImpl implements GenerationDossierService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.generation.prestations.GenerationDossierService#generationDossier(ch.globaz.al
     * .business.models.dossier.DossierComplexModel, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void generationDossier(DossierComplexModel dossier, String idDroit, String debutPeriode, String finPeriode,
            String debutRecap, String finRecap, String montant, Bonification bonification, String nbUnites,
            String numFacture, String numProcessus) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossier : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutPeriode)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossier : " + debutPeriode
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(finPeriode)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossier : " + finPeriode
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutRecap)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossier : " + debutRecap
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutRecap)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossier : " + debutRecap
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeStringUtil.isEmpty(montant) && !JadeNumericUtil.isNumeric(montant)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossier : " + montant
                    + " is not a numeric value");
        }

        if (JadeStringUtil.isEmpty(nbUnites)
                || (!JadeNumericUtil.isIntegerPositif(nbUnites) && !JadeNumericUtil.isZeroValue(nbUnites))) {
            throw new ALGenerationPrestationsContextException("GenerationDossierServiceImpl#generationDossier : "
                    + nbUnites + " is not an unsigned integer");
        }

        if (JadeStringUtil.isEmpty(nbUnites)
                || (!JadeNumericUtil.isIntegerPositif(nbUnites) && !JadeNumericUtil.isZeroValue(nbUnites))) {
            throw new ALGenerationPrestationsContextException("GenerationDossierServiceImpl#generationDossier : "
                    + nbUnites + " is not an unsigned integer");
        }

        ProtocoleLogger logger = new ProtocoleLogger();

        ContextTucana.initContext("01." + debutPeriode);
        ContextAffilie contextAffilie = ContextAffilie.getContextAffilie(debutRecap, finRecap,
                ALCSPrestation.GENERATION_TYPE_GEN_DOSSIER, dossier.getDossierModel().getNumeroAffilie(), null, "0",
                logger, JadeThread.currentContext().getContext());

        if (!JadeStringUtil.isEmpty(numFacture)) {
            contextAffilie.setNumFactureForcee(numFacture);

        }

        if (!JadeStringUtil.isEmpty(numProcessus)) {
            contextAffilie.setNumProcessus(numProcessus);
        }

        contextAffilie
                .initContextDossier(
                        dossier,
                        debutPeriode,
                        finPeriode,
                        montant,
                        bonification,
                        nbUnites,
                        (ALCSDossier.STATUT_IS.equals(dossier.getDossierModel().getStatut()) ? ALConstPrestations.TypeGeneration.ADI_TEMPORAIRE
                                : ALConstPrestations.TypeGeneration.STANDARD));

        contextAffilie.getContextDossier().setIdDroit(idDroit);

        AssuranceInfo info = contextAffilie.getContextDossier().getAssuranceInfo();

        NumeroFactureService s = ALServiceLocator.getNumeroFactureService();
        if (!JadeStringUtil.isEmpty(numFacture)
                && !s.isAvailable(
                        numFacture,
                        dossier.getDossierModel().getNumeroAffilie(),
                        (JadeNumericUtil.isNumericPositif(dossier.getDossierModel().getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_DIRECT
                                : ALCSPrestation.BONI_INDIRECT))) {
            JadeThread.logError(GenerationAffilieServiceImpl.class.getName(),
                    "al.generation.facture.numero.num.notAvailable");
        } else if (!JadeStringUtil.isEmpty(numFacture)
                && !s.checkNumFacture(
                        numFacture,
                        finRecap,
                        info.getPeriodicitieAffiliation(),
                        JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel().getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT
                                : ALCSPrestation.BONI_DIRECT)) {
            JadeThread.logError(GenerationAffilieServiceImpl.class.getName(), "al.generation.facture.numero.nonValide");
        } else {
            GenPrestationFactory.getGenPrestation(contextAffilie).execute();
            contextAffilie.releaseDossier();
            logErrors(logger);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.generation.prestations.GenerationDossierService#generationDossierADI(ch.globaz
     * .al.business.models.dossier.DossierComplexModel, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.util.HashMap)
     */
    @Override
    public void generationDossierADI(DossierComplexModel dossier, String debutPeriode, String finPeriode,
            String debutRecap, String finRecap, HashMap<String, ArrayList<CalculBusinessModel>> details,
            String numFacture, String numProcessus) throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossierADI : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutPeriode)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossierADI : " + debutPeriode
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(finPeriode)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossierADI : " + finPeriode
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutRecap)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossierADI : " + debutRecap
                    + " is not a valid period (MM.YYYY)");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(debutRecap)) {
            throw new ALGenerationException("GenerationDossierServiceImpl#generationDossierADI : " + debutRecap
                    + " is not a valid period (MM.YYYY)");
        }

        ProtocoleLogger logger = new ProtocoleLogger();
        ContextTucana.initContext("01." + debutPeriode);
        ContextAffilie contextAffilie = ContextAffilie.getContextAffilie(debutRecap, finRecap,
                ALCSPrestation.GENERATION_TYPE_GEN_DOSSIER, dossier.getDossierModel().getNumeroAffilie(), null, "0",
                logger, JadeThread.currentContext().getContext());

        if (!JadeStringUtil.isEmpty(numFacture)) {
            contextAffilie.setNumFactureForcee(numFacture);

        }

        if (!JadeStringUtil.isEmpty(numProcessus)) {
            contextAffilie.setNumProcessus(numProcessus);
        }

        contextAffilie.initContextDossier(dossier, debutPeriode, finPeriode, "0", Bonification.AUTO, "1",
                ALConstPrestations.TypeGeneration.ADI_DEFINITIF);
        // Contrôle cohérence entre numéro facture et période récap et périodicité affilié
        // et contrôle numéro de facture disponible
        AssuranceInfo info = contextAffilie.getContextDossier().getAssuranceInfo();
        NumeroFactureService s = ALServiceLocator.getNumeroFactureService();
        if (!JadeStringUtil.isEmpty(numFacture)
                && !s.isAvailable(
                        numFacture,
                        dossier.getDossierModel().getNumeroAffilie(),
                        (JadeNumericUtil.isNumericPositif(dossier.getDossierModel().getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_DIRECT
                                : ALCSPrestation.BONI_INDIRECT))) {
            JadeThread.logError(GenerationAffilieServiceImpl.class.getName(),
                    "al.generation.facture.numero.num.notAvailable");
        } else if (!JadeStringUtil.isEmpty(numFacture)
                && !s.checkNumFacture(
                        numFacture,
                        finRecap,
                        info.getPeriodicitieAffiliation(),
                        JadeNumericUtil.isEmptyOrZero(dossier.getDossierModel().getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT
                                : ALCSPrestation.BONI_DIRECT)) {
            JadeThread.logError(GenerationAffilieServiceImpl.class.getName(), "al.generation.facture.numero.nonValide");
        } else {
            GenPrestationADI genPrest = (GenPrestationADI) GenPrestationFactory.getGenPrestation(contextAffilie);
            genPrest.setDetailsList(details);
            genPrest.execute();
            contextAffilie.releaseDossier();

            logErrors(logger);
        }

    }

    /**
     * Loggue les erreurs contenues dans le logger passé en paramètre dans le context d'exécution
     * 
     * @param logger
     *            logger contenant les messages à logguer
     */
    private void logErrors(ProtocoleLogger logger) {
        if (logger == null) {
            return;
        }

        // log des erreurs
        for (ProtocoleLogger log : logger.getErrorsContainer().values()) {
            for (JadeBusinessMessage message : log.getMessages()) {
                JadeThread.logError(message.getSource(), message.getMessageId(), message.getParameters());
            }
        }

        // log des avertissements
        for (ProtocoleLogger log : logger.getWarningsContainer().values()) {
            for (JadeBusinessMessage message : log.getMessages()) {
                JadeThread.logWarn(message.getSource(), message.getMessageId(), message.getParameters());
            }
        }

        // log des infos
        for (ProtocoleLogger log : logger.getInfosContainer().values()) {
            for (JadeBusinessMessage message : log.getMessages()) {
                JadeThread.logInfo(message.getSource(), message.getMessageId(), message.getParameters());
            }
        }

        // erreurs système
        for (JadeBusinessMessage message : logger.getFatalErrorsContainer()) {
            JadeThread.logError(GenerationDossierServiceImpl.class.getName(), message.getMessageId());
        }
    }
}