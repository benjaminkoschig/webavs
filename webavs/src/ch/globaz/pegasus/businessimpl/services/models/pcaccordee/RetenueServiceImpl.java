package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenueSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.pcaccordee.RetenueService;

public class RetenueServiceImpl implements RetenueService {

    private void checkCsRoleFamille(PcaRetenue retenue, PCAccordee pca) throws JadeNoBusinessLogSessionError {
        if (isDom2R(pca) && JadeStringUtil.isBlankOrZero(retenue.getCsRoleFamillePC())) {
            JadeThread.logError(this.getClass().getName(), "pegasus.retenu.csRoleMembreFamille.mandatory");
        }
    }

    private void checkEtatDroit(SimpleVersionDroit simpleVersionDroit) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (!(IPCDroits.CS_VALIDE.equals(simpleVersionDroit.getCsEtatDroit()) || IPCDroits.CS_CALCULE
                .equals(simpleVersionDroit.getCsEtatDroit()))) {
            JadeThread.logError(this.getClass().getName(), "pegasus.retenu.etatDroit.calculeValide");
        }
    }

    private boolean checkIsDeletable(PcaRetenue retenue) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {
        String dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        if (JadeDateUtil.isDateMonthYearBefore(retenue.getSimpleRetenue().getDateDebutRetenue(), dateProchainPaiement)) {
            String[] params = new String[1];
            params[0] = JadeDateUtil.addMonths("01." + dateProchainPaiement, -1).substring(3);
            JadeThread.logError(this.getClass().getName(), "pegasus.retenu.delete.unalble", params);
            return false;
        } else {
            return true;
        }
    }

    private void checkMontant(PcaRetenue retenue, PCAccordee pca, BigDecimal newMontant)
            throws JadePersistenceException, SimpleRetenuePayementException {
        BigDecimal sommeRetenue = sumMontantRetenu(retenue);
        sommeRetenue = sommeRetenue.add(newMontant);
        if (new Float(pca.getSimplePrestationsAccordees().getMontantPrestation()) < sommeRetenue.floatValue()) {
            throw new SimpleRetenuePayementException(
                    "SimpleRetenuePayementException: le montant total des retenue sont trop important par rapport a la PCA");
        }
    }

    @Override
    public void create(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException {
        prepareRentenuToSave(retenue, true);
        createRetenu(retenue);
        updatePcaRetenuToTrue(retenue);
    }

    private void createRetenu(PcaRetenue retenue) throws SimpleRetenuePayementException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        retenue.setSimpleRetenue(CorvusServiceLocator.getSimpleRetenuePayementService().create(
                retenue.getSimpleRetenue()));
    }

    @Override
    public void createWithOutCheck(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException {
        prepareRentenuToSave(retenue, false);
        createRetenu(retenue);
        updatePcaRetenuToTrue(retenue);
    }

    @Override
    public void delete(PcaRetenue retenue) throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        if (checkIsDeletable(retenue)) {
            String dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            PcaRetenueSearch retenueSearch = searchByIdPca(retenue);
            if (!hasRetenueActive(retenueSearch, retenue, dateProchainPaiement)) {
                updatePcaRetenuToFalse(retenue);
            }
            CorvusServiceLocator.getSimpleRetenuePayementService().delete(retenue.getSimpleRetenue());
        }
    }

    private boolean isConjoint(PcaRetenue retenue) {
        return IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(retenue.getCsRoleFamillePC());
    }

    private boolean isDom2R(PCAccordee pca) {
        return !JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPrestationAccordeeConjoint());
    }

    private boolean isRequerant(PcaRetenue retenue) {
        return IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(retenue.getCsRoleFamillePC())
                || JadeStringUtil.isBlank(retenue.getCsRoleFamillePC());
    }

    private void prepareRentenuToSave(PcaRetenue retenue, boolean withCheck) throws JadePersistenceException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError,
            SimpleRetenuePayementException {
        PCAccordee pca = PegasusServiceLocator.getPCAccordeeService().readDetail(retenue.getIdPCAccordee());
        setIdPrestationAccordee(retenue, pca);
        FWCurrency montant = new FWCurrency(retenue.getSimpleRetenue().getMontantRetenuMensuel());
        FWCurrency montantTotal = new FWCurrency(retenue.getSimpleRetenue().getMontantTotalARetenir());
        retenue.getSimpleRetenue().setMontantRetenuMensuel(montant.toString());
        retenue.getSimpleRetenue().setMontantTotalARetenir(montantTotal.toString());
        if (withCheck) {
            checkCsRoleFamille(retenue, pca);
            checkMontant(retenue, pca, montant.getBigDecimalValue());
            checkEtatDroit(pca.getSimpleVersionDroit());
        }
    }

    @Override
    public PcaRetenue read(String id) throws JadePersistenceException {
        PcaRetenue retenue = new PcaRetenue();
        retenue.setId(id);
        retenue = (PcaRetenue) JadePersistenceManager.read(retenue);
        return retenue;
    }

    @Override
    public PcaRetenueSearch search(PcaRetenueSearch search) throws JadePersistenceException {
        if (search == null) {
            throw new IllegalArgumentException("Unable to search, the search is null!");
        }
        return (PcaRetenueSearch) JadePersistenceManager.search(search);
    }

    private PcaRetenueSearch searchByIdPca(PcaRetenue retenue) throws JadePersistenceException {
        PcaRetenueSearch search = new PcaRetenueSearch();
        search.setForIdPca(retenue.getIdPCAccordee());
        search = search(search);
        return search;
    }

    private void setIdPrestationAccordee(PcaRetenue retenue, PCAccordee pca) throws SimpleRetenuePayementException {
        if (isRequerant(retenue)) {
            retenue.getSimpleRetenue()
                    .setIdRenteAccordee(pca.getSimplePrestationsAccordees().getIdPrestationAccordee());
        } else if (isConjoint(retenue)) {
            retenue.getSimpleRetenue().setIdRenteAccordee(
                    pca.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee());
        } else {
            throw new SimpleRetenuePayementException("Unable to add the retenu this csRoleMembreFamille: "
                    + retenue.getCsRoleFamillePC() + "is not threat by the systeme");
        }
    }

    private BigDecimal sumMontantRetenu(PcaRetenue retenue) throws JadePersistenceException {
        PcaRetenueSearch search = searchByIdPca(retenue);

        BigDecimal sommeRetenue = new BigDecimal(0);
        for (JadeAbstractModel model : search.getSearchResults()) {
            PcaRetenue donnee = (PcaRetenue) model;
            if (donnee.getSimpleRetenue().getIdRenteAccordee().equals(retenue.getSimpleRetenue().getIdRenteAccordee())) {
                if (!donnee.getSimpleRetenue().getIdRetenue().equals(retenue.getSimpleRetenue().getIdRetenue())) {
                    sommeRetenue = sommeRetenue
                            .add(new BigDecimal(donnee.getSimpleRetenue().getMontantRetenuMensuel()));
                }
            }
        }
        return sommeRetenue;
    }

    @Override
    public void update(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException {
        prepareRentenuToSave(retenue, true);
        String dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        PcaRetenueSearch retenueSearch = searchByIdPca(retenue);

        if (isPossibleToRomveROnThePca(retenue, dateProchainPaiement, retenueSearch)) {
            updatePcaRetenuToFalse(retenue);
        } else {
            updatePcaRetenuToTrue(retenue);
        }

        retenue.setSimpleRetenue(CorvusServiceLocator.getSimpleRetenuePayementService().update(
                retenue.getSimpleRetenue()));
    }

    private boolean isPossibleToRomveROnThePca(PcaRetenue retenue, String dateProchainPaiement,
            PcaRetenueSearch retenueSearch) {
        return !hasRetenueActive(retenueSearch, retenue, dateProchainPaiement)
                && JadeDateUtil.isDateMonthYearBefore(retenue.getSimpleRetenue().getDateFinRetenue(),
                        dateProchainPaiement)
                && !dateProchainPaiement.equals(retenue.getSimpleRetenue().getDateFinRetenue());
    }

    private boolean hasRetenueActive(PcaRetenueSearch retenueSearch, PcaRetenue retenue, String dateProchainPaiement) {
        for (JadeAbstractModel model : retenueSearch.getSearchResults()) {
            PcaRetenue donnee = (PcaRetenue) model;
            if (!retenue.getId().equals(donnee.getId())) {
                if (JadeStringUtil.isBlankOrZero(donnee.getSimpleRetenue().getDateFinRetenue())
                        && JadeDateUtil.isDateMonthYearBefore(retenue.getSimpleRetenue().getDateFinRetenue(),
                                dateProchainPaiement)) {
                    return true;
                } else if (donnee.getSimpleRetenue().getDateFinRetenue().equals(dateProchainPaiement)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updatePcaRetenu(PcaRetenue retenue, boolean isRentenue) throws JadePersistenceException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException, JadeApplicationException {
        PCAccordeeSearch searchPC = new PCAccordeeSearch();
        searchPC.setForIdPCAccordee(retenue.getIdPCAccordee());
        searchPC = PegasusServiceLocator.getPCAccordeeService().search(searchPC);
        if (searchPC.getSearchResults().length == 1) {
            PCAccordee pcAccordee = ((PCAccordee) searchPC.getSearchResults()[0]);
            pcAccordee.getSimplePrestationsAccordees().setIsRetenues(isRentenue);
            pcAccordee.getSimplePrestationsAccordeesConjoint().setIsRetenues(isRentenue);
            PegasusServiceLocator.getPCAccordeeService().update(pcAccordee);
        }
    }

    private void updatePcaRetenuToFalse(PcaRetenue retenue) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        updatePcaRetenu(retenue, false);
    }

    private void updatePcaRetenuToTrue(PcaRetenue retenue) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        updatePcaRetenu(retenue, true);
    }

}
