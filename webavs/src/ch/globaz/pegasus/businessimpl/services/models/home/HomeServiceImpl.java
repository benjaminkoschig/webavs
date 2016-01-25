package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCHomes;
import ch.globaz.pegasus.business.exceptions.models.home.ChambreMedicaliseeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.MembreFamilleHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.ChambreMedicaliseeSearch;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.HomeSearch;
import ch.globaz.pegasus.business.models.home.MembreFamilleHome;
import ch.globaz.pegasus.business.models.home.MembreFamilleHomeSearch;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.business.services.models.home.HomeService;
import ch.globaz.pegasus.businessimpl.checkers.home.HomeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class HomeServiceImpl extends PegasusAbstractServiceImpl implements HomeService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#count(ch.
     * globaz.pegasus.business.models.home.HomeSearch)
     */
    @Override
    public int count(HomeSearch search) throws HomeException, JadePersistenceException {
        if (search == null) {
            throw new HomeException("Unable to count homes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public int countChambreMedicalisee(String idTiers, String dateDonneeFinanciere, String idVersionDroit)
            throws ChambreMedicaliseeException, JadePersistenceException, HomeException {

        int countInt = 0;

        ChambreMedicaliseeSearch chambreMedicaliseeSearch = new ChambreMedicaliseeSearch();
        chambreMedicaliseeSearch.setForIdTiers(idTiers);
        chambreMedicaliseeSearch.setForDateDonneeFinanciere(dateDonneeFinanciere);
        chambreMedicaliseeSearch.setForIdVersionPcDroit(idVersionDroit);
        chambreMedicaliseeSearch.setForCsMedicalise(IPCHomes.CS_MEDICALISE);
        chambreMedicaliseeSearch.setForCsTypeDonneeFinanciere(IPCDroits.CS_TAXE_JOURN_HOME);

        try {
            countInt = PegasusImplServiceLocator.getChambreMedicaliseeService().count(chambreMedicaliseeSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }

        return countInt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#create(ch
     * .globaz.pegasus.business.models.home.Home)
     */
    @Override
    public Home create(Home home) throws JadePersistenceException, HomeException {
        if (home == null) {
            throw new HomeException("Unable to create home, the model passed is null!");
        }

        try {
            HomeChecker.checkForCreate(home);
            // on Vas regarder si les home est hors canton est si c'est le cas on le set
            home.getSimpleHome().setIsHorsCanton(isHorsCanton(home));
            home.setSimpleHome(PegasusImplServiceLocator.getSimpleHomeService().create(home.getSimpleHome()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }

        return home;
    }

    @Override
    public PeriodeServiceEtat createPeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException {
        try {
            periodeServiceEtat = PegasusImplServiceLocator.getPeriodeServiceEtatService().create(periodeServiceEtat);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return periodeServiceEtat;
    }

    @Override
    public PeriodeServiceEtat createPeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException {

        try {
            PegasusImplServiceLocator.getSimpleHomeService().update(simpleHome);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        periodeServiceEtat.setSimpleHome(simpleHome);
        periodeServiceEtat = this.createPeriode(periodeServiceEtat);

        return periodeServiceEtat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#createPrixChambre
     * (ch.globaz.pegasus.business.models.home.Home, ch.globaz.pegasus.business.models.home.PrixChambre)
     */
    @Override
    public PrixChambre createPrixChambre(Home home, PrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException, HomeException {
        update(home);
        prixChambre.getTypeChambre().getSimpleTypeChambre().setIdHome(home.getId());

        return this.createPrixChambre(prixChambre);
    }

    @Override
    public PrixChambre createPrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException {

        try {
            prixChambre = PegasusImplServiceLocator.getPrixChambreService().create(prixChambre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return prixChambre;
    }

    @Override
    public TypeChambre createTypeChambre(Home home, TypeChambre typeChambre) throws TypeChambreException,
            HomeException, JadePersistenceException {
        update(home);

        typeChambre.setHome(home);

        return this.createTypeChambre(typeChambre);
    }

    @Override
    public TypeChambre createTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException {
        try {
            typeChambre = PegasusImplServiceLocator.getTypeChambreService().create(typeChambre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return typeChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#delete(ch
     * .globaz.pegasus.business.models.home.Home)
     */
    @Override
    public Home delete(Home home) throws HomeException, JadePersistenceException, TypeChambreException,
            PeriodeServiceEtatException, JadeNoBusinessLogSessionError {
        if (home == null) {
            throw new HomeException("Unable to delete home, the given model is null!");
        }
        try {

            HomeChecker.checkForDelete(home);

            home.setSimpleHome(PegasusImplServiceLocator.getSimpleHomeService().delete(home.getSimpleHome()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }

        return home;
    }

    @Override
    public PeriodeServiceEtat deletePeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException {
        try {
            periodeServiceEtat = PegasusImplServiceLocator.getPeriodeServiceEtatService().delete(periodeServiceEtat);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return periodeServiceEtat;
    }

    @Override
    public PeriodeServiceEtat deletePeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException {

        try {
            PegasusImplServiceLocator.getSimpleHomeService().update(simpleHome);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        periodeServiceEtat.setSimpleHome(simpleHome);
        periodeServiceEtat = this.deletePeriode(periodeServiceEtat);

        return periodeServiceEtat;
    }

    @Override
    public PrixChambre deletePrixChambre(Home home, PrixChambre prixChambre) throws HomeException,
            PrixChambreException, JadePersistenceException {

        // tente de mettre à jour le home
        update(home);

        // supprime le prix de chambre
        return this.deletePrixChambre(prixChambre);
    }

    @Override
    public PrixChambre deletePrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException {
        try {
            prixChambre = PegasusImplServiceLocator.getPrixChambreService().delete(prixChambre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return prixChambre;
    }

    @Override
    public TypeChambre deleteTypeChambre(Home home, TypeChambre typeChambre) throws HomeException,
            JadePersistenceException, TypeChambreException {
        update(home);

        return this.deleteTypeChambre(typeChambre);
    }

    @Override
    public TypeChambre deleteTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException {
        try {
            typeChambre = PegasusImplServiceLocator.getTypeChambreService().delete(typeChambre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return typeChambre;
    }

    @Override
    public PrixChambreSearch findPrixChambre(Home home, PrixChambreSearch prixChambreSearch)
            throws PrixChambreException, JadePersistenceException, HomeException {

        if (prixChambreSearch == null) {
            throw new HomeException("Unable to search prixChambre, the search model passed is null!");
        }

        if (home == null) {
            throw new HomeException("Unable to search prixChambre, the Home passed is null!");
        }

        prixChambreSearch.setForIdHome(home.getId());

        return this.findPrixChambre(prixChambreSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#findPrixChambre
     * (ch.globaz.pegasus.business.models.home.PrixChambreSearch)
     */
    @Override
    public PrixChambreSearch findPrixChambre(PrixChambreSearch prixChambreSearch) throws PrixChambreException,
            JadePersistenceException, HomeException {

        if (prixChambreSearch == null) {
            throw new HomeException("Unable to search prixChambre, the search model passed is null!");
        }

        String idHome = prixChambreSearch.getForIdHome();
        if (JadeStringUtil.isEmpty(idHome)) {
            throw new HomeException("Unable to search prixChambre, the idHome passed is null");
        }

        try {
            prixChambreSearch = PegasusImplServiceLocator.getPrixChambreService().search(prixChambreSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }

        return prixChambreSearch;
    }

    public boolean isHorsCanton(Home home) throws HomeException {
        try {
            String canton = TIBusinessServiceLocator
                    .getAdresseService()
                    .getAdresseTiers(home.getSimpleHome().getIdTiersHome(), Boolean.TRUE, JACalendar.todayJJsMMsAAAA(),
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER,
                            null).getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON_COURT);
            return !EPCProperties.CANTON_CAISSE.getValue().equalsIgnoreCase(canton);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service tier not available", e);
        } catch (JadePersistenceException e) {
            throw new HomeException("Percistence exception", e);
        } catch (JadeApplicationException e) {
            throw new HomeException("Application exception", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#read (java.lang.String)
     */
    @Override
    public Home read(String idHome) throws JadePersistenceException, HomeException {
        if (JadeStringUtil.isEmpty(idHome)) {
            throw new HomeException("Unable to read home, the id passed is null!");
        }
        Home home = new Home();
        home.setId(idHome);
        return (Home) JadePersistenceManager.read(home);
    }

    @Override
    public PeriodeServiceEtat readPeriode(String id) throws PeriodeServiceEtatException, JadePersistenceException,
            HomeException {
        try {
            return PegasusImplServiceLocator.getPeriodeServiceEtatService().read(id);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
    }

    @Override
    public PrixChambre readPrixChambre(String idPrixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException {
        try {
            return PegasusImplServiceLocator.getPrixChambreService().read(idPrixChambre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
    }

    @Override
    public TypeChambre readTypeChambre(String id) throws TypeChambreException, JadePersistenceException, HomeException {
        try {
            return PegasusImplServiceLocator.getTypeChambreService().read(id);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
    }

    @Override
    public MembreFamilleHome retrieveTypeHome(String idTiers, String idVersionDroit, String date)
            throws MembreFamilleHomeException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idTiers) || JadeStringUtil.isBlankOrZero(idVersionDroit)
                || JadeStringUtil.isBlankOrZero(date)) {
            throw new MembreFamilleHomeException(
                    "Unable to search membreFamilleHome, the idTiers or the idVersionDroit or the date is null!");
        } else {

            MembreFamilleHomeSearch membreFamilleHomeSearch = new MembreFamilleHomeSearch();

            membreFamilleHomeSearch.setForIdTiers(idTiers);
            membreFamilleHomeSearch.setForIdVersionDroit(idVersionDroit);
            membreFamilleHomeSearch.setForDate(date);

            membreFamilleHomeSearch = (MembreFamilleHomeSearch) JadePersistenceManager.search(membreFamilleHomeSearch);

            if (membreFamilleHomeSearch.getSize() == 1) {
                return (MembreFamilleHome) membreFamilleHomeSearch.getSearchResults()[0];
            } else {
                if (membreFamilleHomeSearch.getSize() > 1) {
                    throw new MembreFamilleHomeException("More than one home found whith these parameters! idTiers : "
                            + idTiers + " / idVersionDroit : " + idVersionDroit + " / date : " + date);
                } else {
                    return null;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#search
     * (ch.globaz.pegasus.business.models.home.HomeSearch)
     */
    @Override
    public HomeSearch search(HomeSearch homeSearch) throws JadePersistenceException, HomeException {
        if (homeSearch == null) {
            throw new HomeException("Unable to search home, the search model passed is null!");
        }
        return (HomeSearch) JadePersistenceManager.search(homeSearch);
    }

    @Override
    public PeriodeServiceEtatSearch searchPeriode(PeriodeServiceEtatSearch periodeSearch)
            throws PeriodeServiceEtatException, JadePersistenceException, HomeException {
        try {
            periodeSearch = PegasusImplServiceLocator.getPeriodeServiceEtatService().search(periodeSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return periodeSearch;
    }

    @Override
    public TypeChambreSearch searchTypeChambre(TypeChambreSearch typeChambreSearch) throws HomeException,
            TypeChambreException, JadePersistenceException {
        try {
            return PegasusImplServiceLocator.getTypeChambreService().search(typeChambreSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.HomeService#update(ch
     * .globaz.pegasus.business.models.home.Home)
     */
    @Override
    public Home update(Home home) throws JadePersistenceException, HomeException {
        if (home == null) {
            throw new HomeException("Unable to update home, the given model is null!");
        }

        if (home.isNew()) {
            throw new HomeException("Unable to create prix chambre, the model home passed is new!");
        }

        try {
            // on Vas regarder si les home est hors canton est si c'est le cas on le set
            home.getSimpleHome().setIsHorsCanton(isHorsCanton(home));

            HomeChecker.checkForUpdate(home);

            // la mise a jour ne se fait que sur le simpleHome
            PegasusImplServiceLocator.getSimpleHomeService().update(home.getSimpleHome());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }

        return home;
    }

    @Override
    public PeriodeServiceEtat updatePeriode(PeriodeServiceEtat periodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException, HomeException {
        try {
            periodeServiceEtat = PegasusImplServiceLocator.getPeriodeServiceEtatService().update(periodeServiceEtat);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return periodeServiceEtat;
    }

    @Override
    public PeriodeServiceEtat updatePeriode(SimpleHome simpleHome, PeriodeServiceEtat periodeServiceEtat)
            throws HomeException, JadePersistenceException, PeriodeServiceEtatException {
        try {
            PegasusImplServiceLocator.getSimpleHomeService().update(simpleHome);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        periodeServiceEtat.setSimpleHome(simpleHome);
        periodeServiceEtat = this.updatePeriode(periodeServiceEtat);
        return periodeServiceEtat;
    }

    @Override
    public PrixChambre updatePrixChambre(Home home, PrixChambre prixChambre) throws HomeException,
            PrixChambreException, JadePersistenceException {
        update(home);

        prixChambre.getTypeChambre().getSimpleTypeChambre().setIdHome(home.getId());

        return this.updatePrixChambre(prixChambre);
    }

    @Override
    public PrixChambre updatePrixChambre(PrixChambre prixChambre) throws HomeException, PrixChambreException,
            JadePersistenceException {
        try {
            prixChambre = PegasusImplServiceLocator.getPrixChambreService().update(prixChambre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return prixChambre;
    }

    @Override
    public TypeChambre updateTypeChambre(Home home, TypeChambre typeChambre) throws TypeChambreException,
            HomeException, JadePersistenceException {
        update(home);

        typeChambre.setHome(home);

        return this.updateTypeChambre(typeChambre);
    }

    @Override
    public TypeChambre updateTypeChambre(TypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException, HomeException {
        try {
            typeChambre = PegasusImplServiceLocator.getTypeChambreService().update(typeChambre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Service not available - " + e.getMessage());
        }
        return typeChambre;
    }

}
