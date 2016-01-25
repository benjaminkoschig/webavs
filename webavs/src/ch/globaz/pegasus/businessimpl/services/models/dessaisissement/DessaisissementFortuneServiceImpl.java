package ch.globaz.pegasus.businessimpl.services.models.dessaisissement;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.api.ITIPersonne;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.hera.business.models.famille.MembreFamille;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCParametre;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContrePresation;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContreprestationContainer;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortune;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.parametre.ConversionRente;
import ch.globaz.pegasus.business.models.parametre.ConversionRenteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementFortuneService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class DessaisissementFortuneServiceImpl extends PegasusAbstractServiceImpl implements
        DessaisissementFortuneService {

    @Override
    public CalculContrePresation calculContrePresation(CalculContreprestationContainer calculCPContainer)
            throws ConversionRenteException, DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        CalculContrePresation calculContrePresation = new CalculContrePresation();

        Map<MembreFamille, FWCurrency> facteurs;

        facteurs = getFacteursCapitalisations(calculCPContainer, calculContrePresation);

        if (!facteurs.isEmpty()) {
            calculContrePresation.setFacteurCapitalisation(facteurs);
            MembreFamille keyPlusfavorable = null;
            for (MembreFamille key : facteurs.keySet()) {
                if (keyPlusfavorable == null) {
                    keyPlusfavorable = key;
                } else if (facteurs.get(keyPlusfavorable).compareTo(facteurs.get(key)) < 0) {
                    keyPlusfavorable = key;
                }
            }
            calculContrePresation.setKeyFacteurCapitalisationPlusFavorable(keyPlusfavorable);
            calculContrePresation.setMontantNetDuBien(new BigDecimal(calculCPContainer.getMontantBrut())
                    .subtract(new BigDecimal((calculCPContainer.getDeductionMontantDessaisi()))));

            calculContrePresation.setRendementNet(new BigDecimal(calculCPContainer.getRendementFortune())
                    .subtract(new BigDecimal(calculCPContainer.getCharges())));
            BigDecimal facteurFavorable = facteurs.get(keyPlusfavorable).getBigDecimalValue();

            /*
             * if (IPCParametre.CS_VAL.equals(calculContrePresation.getTypeValeur())) { BigDecimal div =
             * BigDecimal.valueOf(1000).divide(facteurFavorable, MathContext.DECIMAL128);
             * calculContrePresation.setRendementNetAvecFacteur(calculContrePresation.getRendementNet().multiply(div));
             * } else if (IPCParametre.CS_1000_DIVISE_VAL.equals(calculContrePresation.getTypeValeur())) {
             * calculContrePresation.setRendementNetAvecFacteur(calculContrePresation.getRendementNet().multiply(
             * facteurFavorable)); }
             */

            calculContrePresation.setRendementNetAvecFacteur(calculContrePresation.getRendementNet().multiply(
                    facteurFavorable));

            calculContrePresation.setDessaisissement(calculContrePresation.getMontantNetDuBien().subtract(
                    calculContrePresation.getRendementNetAvecFacteur()));

            BigDecimal arr = new BigDecimal(calculContrePresation.getDessaisissement().doubleValue()).setScale(0,
                    BigDecimal.ROUND_HALF_DOWN);

            calculContrePresation.setTotalArrondi(arr);
        }

        return calculContrePresation;
    }

    @Override
    public CalculContrePresation calculContrePresation(DessaisissementFortune dessaisissementFortune)
            throws ConversionRenteException, DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        CalculContreprestationContainer container = new CalculContreprestationContainer();
        SimpleDessaisissementFortune simpleDessaisissementFortune = dessaisissementFortune
                .getSimpleDessaisissementFortune();
        container.setMontantBrut(simpleDessaisissementFortune.getMontantBrut());
        container.setCharges(simpleDessaisissementFortune.getCharges());
        container.setDeductionMontantDessaisi(simpleDessaisissementFortune.getDeductionMontantDessaisi());
        container.setRendementFortune(simpleDessaisissementFortune.getRendementFortune());
        container.setIdDroit(dessaisissementFortune.getMembreFamilleEtendu().getDroitMembreFamille()
                .getSimpleDroitMembreFamille().getIdDroit());

        container.setDateValeur(simpleDessaisissementFortune.getDateValeur());
        container.setDateDebut(dessaisissementFortune.getSimpleDonneeFinanciereHeader().getDateDebut());
        container.setDateFin(dessaisissementFortune.getSimpleDonneeFinanciereHeader().getDateFin());

        return this.calculContrePresation(container);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService #
     * count(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementFortuneSearch)
     */
    @Override
    public int count(DessaisissementFortuneSearch search) throws DessaisissementFortuneException,
            JadePersistenceException {
        if (search == null) {
            throw new DessaisissementFortuneException(
                    "Unable to count dessaisissementFortune, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementFortune)
     */
    @Override
    public DessaisissementFortune create(DessaisissementFortune dessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException, DonneeFinanciereException {
        if (dessaisissementFortune == null) {
            throw new DessaisissementFortuneException(
                    "Unable to create dessaisissementFortune, the model passed is null!");
        }

        try {
            dessaisissementFortune.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            dessaisissementFortune.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleDessaisissementFortuneService().create(
                    dessaisissementFortune.getSimpleDessaisissementFortune());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementFortuneException("Service not available - " + e.getMessage());
        }

        return dessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementFortune)
     */
    @Override
    public DessaisissementFortune delete(DessaisissementFortune dessaisissementFortune)
            throws DessaisissementFortuneException, JadePersistenceException {
        if (dessaisissementFortune == null) {
            throw new DessaisissementFortuneException(
                    "Unable to delete dessaisissement fortune, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleDessaisissementFortuneService().delete(
                    dessaisissementFortune.getSimpleDessaisissementFortune());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementFortuneException("Service not available - " + e.getMessage());
        }

        return dessaisissementFortune;
    }

    public Integer getAge(String dateNaissance) {
        String now = JACalendar.todayJJsMMsAAAA();
        Integer age = JadeDateUtil.getNbYearsBetween(dateNaissance, now);
        return age;
    }

    /* Ceci permet de recher dans les table AFC */
    private ConversionRente getConversionRenteByDessaisissementFortune(CalculContreprestationContainer calculCPContainer)
            throws ConversionRenteException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ConversionRenteSearch conversionRenteSearch = new ConversionRenteSearch();
        ConversionRente conversionRente = null;
        String age = getAge(calculCPContainer.getDateNaissance()).toString();
        // si la donnation a été faite dans les 6 premiers mois on soustrai de 1
        // l'age
        try {
            if ((JACalendar.getMonth(calculCPContainer.getDateValeur()) < 6) && (Integer.valueOf(age) > 0)) {
                age = ((Integer) (Integer.valueOf(age) - 1)).toString();

            }
        } catch (NumberFormatException e) {
            throw new ConversionRenteException("Unable to find month width date value", e);
        } catch (JAException e) {
            throw new ConversionRenteException("Unable to find month width date value", e);
        }

        conversionRenteSearch.setForAge(age);

        conversionRenteSearch.setForDateDebut("01." + calculCPContainer.getDateDebut());
        conversionRenteSearch.setForDateFin("01." + calculCPContainer.getDateDebut());
        conversionRenteSearch.setWhereKey("withValueInPeriode");
        // conversionRenteSearch.setWhereKey(ConversionRenteSearch.WITH_DATE_VALABLE);
        PegasusServiceLocator.getConversionRenteService().search(conversionRenteSearch);

        if (conversionRenteSearch.getSize() == 1) {
            conversionRente = (ConversionRente) conversionRenteSearch.getSearchResults()[0];
        } else {

            JadeThread.logError(calculCPContainer.getClass().getName(),
                    "pegasus.dessaisissementFortune.facteurTableAFC.notFound");

            /*
             * throw new ConversionRenteException( "Unable to find the conversionRente");
             */

        }
        return conversionRente;

    }

    private Map<MembreFamille, FWCurrency> getFacteursCapitalisations(
            CalculContreprestationContainer calculCPContainer, CalculContrePresation calculContrePresation)
            throws ConversionRenteException, DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        String csSex = null;
        Map<MembreFamille, FWCurrency> facteurs = new HashMap<MembreFamille, FWCurrency>();

        ConversionRente tableAfc = null;
        // BigDecimal facteur = null;
        String facteur = null;
        DroitMembreFamilleSearch search = new DroitMembreFamilleSearch();
        search.setForIdDroit(calculCPContainer.getIdDroit());

        List<String> csRole = new ArrayList<String>();
        csRole.add(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
        csRole.add(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        search.setForCsRoletMembreFamilleIn(csRole);
        search = PegasusServiceLocator.getDroitService().searchDroitMembreFamille(search);

        for (JadeAbstractModel absDonnee : search.getSearchResults()) {
            DroitMembreFamille donnee = (DroitMembreFamille) absDonnee;
            csSex = donnee.getMembreFamille().getCsSexe();

            calculCPContainer.setDateNaissance(donnee.getMembreFamille().getDateNaissance());
            tableAfc = getConversionRenteByDessaisissementFortune(calculCPContainer);
            calculContrePresation.setTypeValeur(tableAfc.getSimpleConversionRente().getTypeDeValeur());
            if (tableAfc != null) {
                if (ITIPersonne.CS_FEMME.equals(csSex)) {
                    facteur = tableAfc.getSimpleConversionRente().getRenteFemme();
                } else if (ITIPersonne.CS_HOMME.equals(csSex)) {
                    facteur = tableAfc.getSimpleConversionRente().getRenteHomme();
                }
            }

            if (IPCParametre.CS_VAL.equals(calculContrePresation.getTypeValeur())) {
                BigDecimal div = BigDecimal.valueOf(1000).divide(new FWCurrency(facteur).getBigDecimalValue(),
                        MathContext.DECIMAL128);
                facteurs.put(donnee.getMembreFamille(), new FWCurrency(div.doubleValue()));

            } else if (IPCParametre.CS_1000_DIVISE_VAL.equals(calculContrePresation.getTypeValeur())) {
                facteurs.put(donnee.getMembreFamille(), new FWCurrency(facteur));
            }

        }

        return facteurs;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService
     * #read(java.lang.String)
     */
    @Override
    public DessaisissementFortune read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException {
        if (JadeStringUtil.isEmpty(idDessaisissementFortune)) {
            throw new DessaisissementFortuneException("Unable to read dessaisissementFortune, the id passed is null!");
        }
        DessaisissementFortune dessaisissementFortune = new DessaisissementFortune();
        dessaisissementFortune.setId(idDessaisissementFortune);
        return (DessaisissementFortune) JadePersistenceManager.read(dessaisissementFortune);
    }

    /**
     * Chargement d'une DessaisissementFortune via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws DessaisissementFortuneSearch
     *             Exception
     * @throws JadePersistenceException
     */
    @Override
    public DessaisissementFortune readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws DessaisissementFortuneException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new DessaisissementFortuneException(
                    "Unable to find DessaisissementFortune the idDonneeFinanciereHeader passed si null!");
        }

        DessaisissementFortuneSearch search = new DessaisissementFortuneSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (DessaisissementFortuneSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new DessaisissementFortuneException("More than one DessaisissementFortune find, one was exepcted!");
        }

        return (DessaisissementFortune) search.getSearchResults()[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService #
     * search(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementFortuneSearch )
     */
    @Override
    public DessaisissementFortuneSearch search(DessaisissementFortuneSearch dessaisissementFortuneSearch)
            throws JadePersistenceException, DessaisissementFortuneException {
        if (dessaisissementFortuneSearch == null) {
            throw new DessaisissementFortuneException(
                    "Unable to search dessaisissementFortune, the search model passed is null!");
        }
        return (DessaisissementFortuneSearch) JadePersistenceManager.search(dessaisissementFortuneSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementFortune)
     */
    @Override
    public DessaisissementFortune update(DessaisissementFortune dessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException, DonneeFinanciereException {
        if (dessaisissementFortune == null) {
            throw new DessaisissementFortuneException("Unable to update numeraire, the model passed is null!");
        }

        try {
            dessaisissementFortune.setSimpleDessaisissementFortune(PegasusImplServiceLocator
                    .getSimpleDessaisissementFortuneService().update(
                            dessaisissementFortune.getSimpleDessaisissementFortune()));
            dessaisissementFortune.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            dessaisissementFortune.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementFortuneException("Service not available - " + e.getMessage());
        }

        return dessaisissementFortune;
    }
}
