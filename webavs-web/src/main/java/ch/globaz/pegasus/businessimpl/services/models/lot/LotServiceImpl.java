package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.properties.REProperties;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.pegasus.process.lot.PCValidationDecisionsComptabiliserLotProcess;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.lot.ComptabiliserLotSearch;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.lot.LotService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationData;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationHandler;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.PegasusJournalConteneur;

public class LotServiceImpl extends PegasusAbstractServiceImpl implements LotService {
    @Override
    public ComptabilisationData comptabiliserLot(String idLot, String idOrganeExecution, String numeroOG,
            String libelleJournal, String dateValeur, String dateEcheance) throws JadePersistenceException,
            ComptabiliserLotException, JAException {
        if (idLot == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLot the idLot is null!");
        }

        if (dateValeur == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLot the dateValeur is null!");
        }

        if (JadeStringUtil.isEmpty(dateEcheance)) {
            throw new ComptabiliserLotException("La date Echeance est obligatoire !");
        }

        if (JadeStringUtil.isEmpty(idOrganeExecution)) {
            throw new ComptabiliserLotException("L'idOrganeExecution est obligatoire !");
        }

        if (!isIso20022(idOrganeExecution)) {
            if (JadeStringUtil.isEmpty(numeroOG)) {
                throw new ComptabiliserLotException("le numero OG est obligatoire !");
            }
        }

        try {

            long currentTime = System.currentTimeMillis();

            JadeThread.logInfo(this.getClass().getName(),
                    "-----------------------Start process comptabilisation-----------------------");

            ComptabilisationData comptabilisationData = ComptabilisationHandler.execute(idLot, idOrganeExecution,
                    numeroOG, libelleJournal, dateValeur, dateEcheance);

            JadeThread.logInfo(this.getClass().getName(), "-----------------------Stop process comptabilisation ("
                    + (System.currentTimeMillis() - currentTime) + ")-----------------------");

            return comptabilisationData;

        } catch (JadeApplicationException e) {
            throw new ComptabiliserLotException("Unable to access create the journal and operation", e);
        }
    }

    private boolean isIso20022(String idOrganeExecution) throws ComptabiliserLotException {
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setForIdOrganeExecution(idOrganeExecution);
        try {
            mgr.find();
            if (mgr.size() != 1) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ComptabiliserLotException("L'idOrganeExecution est inconnu !");
        }

        return ((CAOrganeExecution) mgr.getEntity(0)).getIdTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022);
    }

    @Override
    public SimpleLot createLot(String csTypeLot, String description)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException {

        try {
            SimpleLot simpleLot = new SimpleLot();
            simpleLot.setCsTypeLot(csTypeLot);
            simpleLot.setCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
            simpleLot.setDateCreation(JACalendar.todayJJsMMsAAAA());
            simpleLot.setCsProprietaire(IRELot.CS_LOT_OWNER_PC);

            simpleLot.setDescription(description);
            return CorvusServiceLocator.getLotService().create(simpleLot);
        } catch (LotException e) {
            throw new DecisionException("Unable to create the lot", e);
        }
    }

    private SimpleLot createLot(String csTypeLot) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {

        String monthYear = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_MMsYYYY);

        SimpleLotSearch simpleLotSearch = new SimpleLotSearch();

        simpleLotSearch.setForCsProprietaire(IRELot.CS_LOT_OWNER_PC);
        simpleLotSearch.setForCsType(csTypeLot);
        simpleLotSearch.setForDateDebut("01." + monthYear);

        try {
            JADate date = new JADate(monthYear);
            JACalendarGregorian jaCalendarGregorian = new JACalendarGregorian();
            simpleLotSearch.setForDateFin(jaCalendarGregorian.daysInMonth(date.getMonth(), date.getYear()) + "."
                    + monthYear);
        } catch (JAException e1) {
            throw new DecisionException("Unable to create the lot, probleme with the date", e1);
        }

        int nbLot = 0;
        try {
            nbLot = CorvusServiceLocator.getLotService().count(simpleLotSearch);
        } catch (LotException e) {
            throw new DecisionException("Unable to create the lot", e);
        }

        String libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csTypeLot);
        String description = (" PC - " + libelle + "s " + monthYear + " / lot " + String.valueOf(nbLot + 1));

        return createLot(csTypeLot, description);
    }

    /**
     * Suppression d'un lot de type blocahe, owner PC, si il ne contient pas de prestation
     * 
     */
    @Override
    public void deleteLotDeblocageIfEmpty() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PrestationException, LotException {

        SimpleLot lot = (SimpleLot) findLotOuvert(IRELot.CS_TYP_LOT_DEBLOCAGE_RA).getSearchResults()[0];

        if (lot == null) {
            throw new LotException("The lot destined to be deleted is null! [LotServiceImpl.deleteLotDeblocageIfEmpty]");
        }

        SimplePrestationSearch searchPrest = new SimplePrestationSearch();
        searchPrest.setForIdLot(lot.getIdLot());

        int nombreDePrestation = PegasusImplServiceLocator.getSimplePrestationService().count(searchPrest);

        if (nombreDePrestation == 0) {
            CorvusServiceLocator.getLotService().delete(lot);
        }

    }

    @Override
    public SimpleLot findCurrentDeblocageLotOrCreate() throws JadeApplicationServiceNotAvailableException,
            DecisionException, JadePersistenceException {
        SimpleLot result = findLotOrCreate(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
        return result;
    }

    @Override
    public SimpleLot findCurrentDecisionLotOrCreate() throws JadeApplicationServiceNotAvailableException,
            DecisionException, JadePersistenceException {
        SimpleLot result = findLotOrCreate(IRELot.CS_TYP_LOT_DECISION);
        return result;
    }

    private SimpleLot findLotOrCreate(String csTypeLot) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleLot result;
        SimpleLotSearch simpleLotSearch = findLotOuvert(csTypeLot);
        if (simpleLotSearch.getSize() == 0) {
            result = createLot(csTypeLot);
        } else {
            result = (SimpleLot) simpleLotSearch.getSearchResults()[0];
        }
        return result;
    }

    private SimpleLotSearch findLotOuvert(String csTypeLot) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {
        SimpleLotSearch search = new SimpleLotSearch();
        search.setForCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
        search.setForCsProprietaire(IRELot.CS_LOT_OWNER_PC);
        search.setForCsType(csTypeLot);
        try {
            search = CorvusServiceLocator.getLotService().search(search);
        } catch (LotException e) {
            throw new DecisionException("Unable to search lot", e);
        }
        return search;
    }

    @Override
    public PegasusJournalConteneur generateJournalByIdPrestation(String idPrestation) throws JadePersistenceException,
            JadeApplicationException, JAException {
        if (idPrestation == null) {
            throw new IllegalArgumentException("Unable to generateJournalByIdPrestation, the idPrestation is null!");
        }
        return ComptabilisationHandler.generateEcritureForOnePresation(idPrestation).getJournalConteneur();
    }

    @Override
    public ComptabiliserLotSearch searchComptabiliserLot(ComptabiliserLotSearch search)
            throws ComptabiliserLotException {
        if (search == null) {
            throw new ComptabiliserLotException("Unable to searchComptabliserLot search, the model passed is null!");
        }
        try {
            return (ComptabiliserLotSearch) JadePersistenceManager.search(search);
        } catch (JadePersistenceException e) {
            throw new ComptabiliserLotException("Unable to search comptabliliserLot", e);
        }
    }

    @Override
    public String toString() {
        return "LotServiceImpl [getClass()=" + this.getClass() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }

    /**
     * Permet une comptabilisation du lot<br>
     * 
     * Sinon on fait rien.
     * 
     * @throws PropertiesException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ComptabiliserLotException
     * @throws DecisionException
     * @throws IllegalArgumentException
     */
    @Override
    public void comptabiliserAndResolveDateComptableEcheance(String idLot, String mailProcessCompta)
            throws PropertiesException, ComptabiliserLotException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        Checkers.checkNotNull(idLot, "idLot");

        String idOrganeExecution = REProperties.ORGANE_EXECUTION_PAIEMENT.getValue();

        try {
            Date dateDernierPaiement = new Date(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
            String dateEcheancePaiement = resolveDateToUse(dateDernierPaiement, new Date());
            String dateComptable = dateEcheancePaiement;
            // lancement process de comptabilisation
            try {
                PCValidationDecisionsComptabiliserLotProcess comptabiliserLotProcess = new PCValidationDecisionsComptabiliserLotProcess();
                comptabiliserLotProcess.setSession(BSessionUtil.getSessionFromThreadContext());
                comptabiliserLotProcess.setMailAdress(mailProcessCompta);
                comptabiliserLotProcess.setIdLot(idLot);
                comptabiliserLotProcess.setIdOrganeExecution(idOrganeExecution);
                comptabiliserLotProcess.setNumeroOG("1");
                comptabiliserLotProcess.setLibelleJournal(null);
                comptabiliserLotProcess.setDateValeur(dateComptable);
                comptabiliserLotProcess.setDateEcheance(dateEcheancePaiement);
                try {
                    BProcessLauncher.startJob(comptabiliserLotProcess);
                } catch (Exception e) {
                    throw new JadeApplicationServiceNotAvailableException("cannot start comptabiliserLotProcess", e);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unabled to comptabilise lot decision restitution", e);
            }

        } catch (PmtMensuelException e1) {
            throw new RuntimeException(e1);
        }

    }

    String resolveDateToUse(Date dateDernierPaiement, Date toDay) {
        Date dateComptable = toDay;

        if (!dateDernierPaiement.equalsByMonthYear(dateComptable)) {
            dateComptable = dateComptable.addMonth(1).getFirstDayOfMonth();
            if (dateComptable.isSunday()) {
                dateComptable = dateComptable.getFirstMonday();
            }
        }

        String dateEcheancePaiement = dateComptable.getSwissValue();
        return dateEcheancePaiement;
    }

}
