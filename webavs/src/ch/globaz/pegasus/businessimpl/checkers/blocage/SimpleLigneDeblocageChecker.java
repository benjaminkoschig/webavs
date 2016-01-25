package ch.globaz.pegasus.businessimpl.checkers.blocage;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.model.SoldeCompteCourant;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.osiris.exception.OsirisException;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDeblocage;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocageSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class SimpleLigneDeblocageChecker {

    private static void checkAmountOfDette(SimpleLigneDeblocage simpleDeblocage) throws BlocageException,
            JadeNoBusinessLogSessionError {
        SimpleLigneDeblocage detteDebloqueConjoint = null;
        try {
            detteDebloqueConjoint = PegasusImplServiceLocator.getDeblocageDetteService()
                    .readDetteDeblocageConjointEnregistrer(simpleDeblocage.getIdSectionDetteEnCompta(),
                            simpleDeblocage.getIdPca());
        } catch (JadeApplicationServiceNotAvailableException e1) {
            throw new BlocageException("Unable call the service to check the dette conjoint", e1);
        } catch (JadePersistenceException e1) {
            throw new BlocageException("Unable to search the dette deblocage conjoint", e1);
        }

        try {

            SectionSimpleModel section = CABusinessServiceLocator.getSectionService().readSection(
                    simpleDeblocage.getIdSectionDetteEnCompta());

            BigDecimal montantDebloque = SimpleLigneDeblocageChecker.transformMontant(simpleDeblocage);

            // Float montantDebloque = Float.valueOf(simpleDeblocage.getMontant());

            if (detteDebloqueConjoint != null) {
                montantDebloque = montantDebloque.add(SimpleLigneDeblocageChecker
                        .transformMontant(detteDebloqueConjoint));
            }

            if ((new BigDecimal(section.getSolde())).compareTo(montantDebloque) == -1) {
                String[] p = new String[3];
                p[0] = simpleDeblocage.getMontant();
                p[1] = section.getSolde();
                if (detteDebloqueConjoint == null) {

                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.montant.tropGrandPourLaDette", p);
                } else {
                    p[2] = detteDebloqueConjoint.getMontant();
                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.montant.tropGrandPourLaDetteAvecLeConjoint", p);
                }
            }
        } catch (OsirisException e) {
            throw new BlocageException("Unable to check the section", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BlocageException("Unable call the service to check the section", e);
        }
    }

    public static void checkForCreate(SimpleLigneDeblocage simpleDeblocage) throws JadeApplicationException,
            JadePersistenceException {
        SimpleLigneDeblocageChecker.checkMandatory(simpleDeblocage);
        SimpleLigneDeblocageChecker.checkIntegrity(simpleDeblocage);

    }

    public static void checkForDelete(SimpleLigneDeblocage simpleDeblocage) {
        // TODO Auto-generated method stub
    }

    public static void checkForUpdate(SimpleLigneDeblocage simpleDeblocage) throws JadeApplicationException,
            JadePersistenceException {
        SimpleLigneDeblocageChecker.checkMandatory(simpleDeblocage);
        SimpleLigneDeblocageChecker.checkIntegrity(simpleDeblocage);
    }

    private static void checkIntegrity(SimpleLigneDeblocage simpleDeblocage) throws JadeApplicationException,
            JadePersistenceException {

        SimpleLigneDeblocageChecker.checkSumMontantDebloque(simpleDeblocage);

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            if (EPCTypeDeblocage.CS_CREANCIER.getCsCode().equals(simpleDeblocage.getCsTypeDeblocage())) {
                if (JadeStringUtil.isEmpty(simpleDeblocage.getIdTiersCreancier())) {
                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.idTiersCreancier.mandatory");
                }
                if (JadeStringUtil.isEmpty(simpleDeblocage.getIdApplicationAdressePaiement())) {
                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.idApplicationAdressePaiement.mandatory");
                }
                if (JadeStringUtil.isEmpty(simpleDeblocage.getIdTiersAdressePaiement())) {
                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.idTiersAdressePaiement.mandatory");
                }
            } else if (EPCTypeDeblocage.CS_DETTE_EN_COMPTA.getCsCode().equals(simpleDeblocage.getCsTypeDeblocage())) {
                if (JadeStringUtil.isEmpty(simpleDeblocage.getIdSectionDetteEnCompta())) {
                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.idSection.mandatory");
                }
                if (JadeStringUtil.isEmpty(simpleDeblocage.getIdRoleDetteEnCompta())) {
                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.idRoleCa.mandatory");
                }
                SimpleLigneDeblocageChecker.checkAmountOfDette(simpleDeblocage);
            } else if (EPCTypeDeblocage.CS_VERSEMENT_BENEFICIAIRE.getCsCode().equals(
                    simpleDeblocage.getCsTypeDeblocage())) {
                if (JadeStringUtil.isEmpty(simpleDeblocage.getIdTiersAdressePaiement())) {
                    JadeThread.logError(simpleDeblocage.getClass().getName(),
                            "pegasus.simpleDeblocage.idTiersAdressePaiement.mandatory");
                }
            } else {
                throw new BlocageException("The type " + simpleDeblocage.getCsTypeDeblocage()
                        + " is not konw by the system");
            }
        }
    }

    private static void checkMandatory(SimpleLigneDeblocage simpleDeblocage) {

        // montant is not null
        if (JadeStringUtil.isEmpty(simpleDeblocage.getMontant())) {
            JadeThread.logError(simpleDeblocage.getClass().getName(), "pegasus.simpleDeblocage.montant.mandatory");
        }

        // idPca is not null
        if (JadeStringUtil.isEmpty(simpleDeblocage.getIdPca())) {
            JadeThread.logError(simpleDeblocage.getClass().getName(), "pegasus.simpleDeblocage.idPca.mandatory");
        }

        // csTypeBlocage is not null
        if (JadeStringUtil.isEmpty(simpleDeblocage.getCsTypeDeblocage())) {
            JadeThread
                    .logError(simpleDeblocage.getClass().getName(), "pegasus.simpleDeblocage.csTypeBlocage.mandatory");
        }

    }

    private static void checkSumMontantDebloque(SimpleLigneDeblocage simpleDeblocage) throws JadeApplicationException,
            JadePersistenceException {

        BigDecimal sum = SimpleLigneDeblocageChecker.sumMontantaADebloque(simpleDeblocage);

        PcaBloque pcaBloque = PegasusImplServiceLocator.getPcaBloqueService().readPcaBloque(simpleDeblocage.getIdPca());

        SoldeCompteCourant compteCourant = PegasusServiceLocator.getBlocageService().determineLeCompteCouranAUtiliser(
                pcaBloque);

        BigDecimal montantAutiliser = new BigDecimal(Math.min(Float.valueOf(compteCourant.getMontant()),
                Float.valueOf(pcaBloque.getMontantBloque()))).abs();

        if (montantAutiliser.compareTo(sum) == -1) {
            String[] p = new String[3];
            p[0] = simpleDeblocage.getMontant();
            p[1] = new FWCurrency(montantAutiliser.floatValue()).toStringFormat();
            p[2] = new FWCurrency(sum.subtract(montantAutiliser).toString()).toStringFormat();
            JadeThread.logError(simpleDeblocage.getClass().getName(),
                    "pegasus.simpleDeblocage.montantTotalDeblocqueDepasse.integrity", p);
        }

    }

    private static BigDecimal sumMontantaADebloque(SimpleLigneDeblocage simpleDeblocage) throws BlocageException {
        SimpleLigneDeblocageSearch search = new SimpleLigneDeblocageSearch();
        search.setForIdPca(simpleDeblocage.getIdPca());
        search.setForCsEtat(IPCDeblocage.CS_ETAT_ENREGISTRE);
        BigDecimal sum = new BigDecimal(0);
        try {
            search = PegasusServiceLocator.getSimpleDeblocageService().search(search);
            for (JadeAbstractModel model : search.getSearchResults()) {
                SimpleLigneDeblocage deblocage = (SimpleLigneDeblocage) model;
                if (!deblocage.getId().equals(simpleDeblocage.getId())) {
                    sum = sum.add(new BigDecimal(deblocage.getMontant()));
                }
            }
            sum = sum.add(SimpleLigneDeblocageChecker.transformMontant(simpleDeblocage));
            return sum;
        } catch (JadeApplicationServiceNotAvailableException e1) {
            throw new BlocageException("Unable call the service to count the amount deblocage", e1);
        } catch (JadePersistenceException e1) {
            throw new BlocageException("Unable to search for compute the amount", e1);
        }
    }

    private static BigDecimal transformMontant(SimpleLigneDeblocage simpleDeblocage) {
        return new BigDecimal(JadePersistenceUtil.deQuote(simpleDeblocage.getMontant()));
    }

}
