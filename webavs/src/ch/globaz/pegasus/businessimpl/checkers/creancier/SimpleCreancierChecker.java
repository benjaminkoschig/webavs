package ch.globaz.pegasus.businessimpl.checkers.creancier;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.constantes.IPCCreancier;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.SimpleCreancier;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class SimpleCreancierChecker {
    public static void checkForCreate(SimpleCreancier simpleCreancier) throws CreancierException,
            JadePersistenceException, JadeNoBusinessLogSessionError, JadeApplicationServiceNotAvailableException {
        SimpleCreancierChecker.checkMandatory(simpleCreancier);
        SimpleCreancierChecker.checkIntegrity(simpleCreancier);

    }

    /**
     * @param simpleCreancier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws CreancierException
     */
    public static void checkForDelete(SimpleCreancier simpleCreancier) throws CreancierException,
            JadePersistenceException {
        try {
            if (SimpleCreancierChecker.hasCreanceAccordee(simpleCreancier)) {
                JadeThread.logError(simpleCreancier.getClass().getName(), "pegasus.simplecreancier.existe.integrity");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Unable to check for delete", e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new CreancierException("Unable to check for delete", e);
        }
    }

    /**
     * @param simpleCreancier
     * @throws CreancierException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForUpdate(SimpleCreancier simpleCreancier) throws CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleCreancierChecker.checkMandatory(simpleCreancier);
        SimpleCreancierChecker.checkIntegrity(simpleCreancier);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleCreancier
     * @throws CreancierException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private static void checkIntegrity(SimpleCreancier simpleCreancier) throws CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // montant is not null
        if (!simpleCreancier.isNew()) {
            BigDecimal sum = new BigDecimal(0);
            SimpleCreanceAccordeeSearch search = new SimpleCreanceAccordeeSearch();
            search.setForIdCreancier(simpleCreancier.getIdCreancier());
            search = PegasusImplServiceLocator.getSimpleCreanceAccordeeService().search(search);
            for (JadeAbstractModel model : search.getSearchResults()) {
                sum = sum.add(new FWCurrency(((SimpleCreanceAccordee) model).getMontant()).getBigDecimalValue());
            }
            if (sum.compareTo(new FWCurrency(simpleCreancier.getMontant()).getBigDecimalValue()) == 1) {
                JadeThread.logError(simpleCreancier.getClass().getName(), "pegasus.simplecreancier.montant.tropgrand");
            }
        }
        try {
            if (!TIBusinessServiceLocator.getAdresseService().hasAdressePaiement(
                    simpleCreancier.getIdTiersAdressePaiement(), simpleCreancier.getIdDomaineApplicatif(),
                    JACalendar.todayJJsMMsAAAA())
                    && !TIBusinessServiceLocator.getAdresseService().hasAdressePaiement(
                            simpleCreancier.getIdTiersAdressePaiement(),
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT, JACalendar.todayJJsMMsAAAA())) {

                PersonneEtendueComplexModel tier = TIBusinessServiceLocator.getPersonneEtendueService().read(
                        simpleCreancier.getIdTiersAdressePaiement());

                String[] params = { tier.getPersonneEtendue().getNumAvsActuel() + " "
                        + tier.getTiers().getDesignation1() + " " + tier.getTiers().getDesignation2() };

                JadeThread.logError(simpleCreancier.getClass().getName(),
                        "pegasus.simplecreancier.tierAdressePaiement.notFound", params);
            }
        } catch (JadeApplicationException e) {
            throw new CreancierException("Unable to get date now", e);
        }
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * @param simpleCreancier
     */
    private static void checkMandatory(SimpleCreancier simpleCreancier) {

        // montant is not null
        if (JadeStringUtil.isEmpty(simpleCreancier.getMontant())) {
            JadeThread.logError(simpleCreancier.getClass().getName(), "pegasus.simplecreancier.montant.mandatory");
        }
        // cs etat is not null
        if (JadeStringUtil.isEmpty(simpleCreancier.getCsEtat())) {
            JadeThread.logError(simpleCreancier.getClass().getName(), "pegasus.simplecreancier.etat.mandatory");
        }
        // cs type créacne is not null
        if (JadeStringUtil.isEmpty(simpleCreancier.getCsTypeCreance())) {
            JadeThread.logError(simpleCreancier.getClass().getName(), "pegasus.simplecreancier.typecreance.mandatory");
        }
        // id demande is not null
        if (JadeStringUtil.isEmpty(simpleCreancier.getIdDemande())) {
            JadeThread.logError(simpleCreancier.getClass().getName(), "pegasus.simplecreancier.demande.mandatory");
        }

        // Obligatoir si ce n'est pas les impots
        if (!IPCCreancier.CS_TYPE_CREANCE_IMPOT.equals(simpleCreancier.getCsTypeCreance())) {
            // id domaine applicatif is not null
            if (JadeStringUtil.isEmpty(simpleCreancier.getIdDomaineApplicatif())) {
                JadeThread.logError(simpleCreancier.getClass().getName(),
                        "pegasus.simplecreancier.domaineApplicatif.mandatory");
            }
            // id tiers adresse paiement is not null
            if (JadeStringUtil.isEmpty(simpleCreancier.getIdTiersAdressePaiement())) {
                JadeThread.logError(simpleCreancier.getClass().getName(),
                        "pegasus.simplecreancier.tiersadressepaiment.mandatory");
            }
        }
        /*
         * // id tiers is not null if (JadeStringUtil.isEmpty(simpleCreancier.getIdTiers())) {
         * JadeThread.logError(simpleCreancier.getClass().getName(), "pegasus.simplecreancier.tiers.mandatory"); }
         */
    }

    private static boolean hasCreanceAccordee(SimpleCreancier simpleCreancier) throws CreancierException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        return PegasusServiceLocator.getCreancierService().hasCreanceAccordee(simpleCreancier.getIdCreancier());
    }
}
