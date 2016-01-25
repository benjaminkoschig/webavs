/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.Compteurs;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.droit.DonneeFinanciereHeaderService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author SCE
 * 
 *         8 juil. 2010
 */
public class DonneeFinanciereHeaderServiceImpl extends PegasusAbstractServiceImpl implements
        DonneeFinanciereHeaderService {
    /*
     * public void copyDonneeFinanciere(Droit newDroit) throws DonneeFinanciereException,
     * JadeApplicationServiceNotAvailableException { DroitMembreFamilleSearch droitMembreFamilleSearch = new
     * DroitMembreFamilleSearch(); droitMembreFamilleSearch.setForIdDroit(newDroit.getSimpleDroit().getIdDroit()); try {
     * 
     * droitMembreFamilleSearch = PegasusImplServiceLocator.getDroitMembreFamilleService().search(
     * droitMembreFamilleSearch); } catch (DroitException e) { throw new
     * DonneeFinanciereException("Unable to find the droitMembreFamille", e); } catch
     * (JadeApplicationServiceNotAvailableException e) { throw new DonneeFinanciereException("Service not available - "
     * + e.getMessage()); } catch (JadePersistenceException e) { throw new
     * DonneeFinanciereException("Unable to find the droitMembreFamille", e); }
     * 
     * if (newDroit.getSimpleVersionDroit().getNoVersion().equals("1")) { Droit oldDroit = this.findOldDroit(newDroit);
     * if (oldDroit != null) { PegasusImplServiceLocator.getRenteIjApiService().copyRenteIjApi(newDroit, oldDroit,
     * droitMembreFamilleSearch);
     * PegasusImplServiceLocator.getFortuneParticuliereService().copyFortuneParticuliere(newDroit, oldDroit,
     * droitMembreFamilleSearch); PegasusImplServiceLocator.getHabitatService().copyFortuneParticuliere(newDroit,
     * oldDroit, droitMembreFamilleSearch);
     * PegasusImplServiceLocator.getDessaisissementService().copyDessaisissement(newDroit, oldDroit,
     * droitMembreFamilleSearch); PegasusImplServiceLocator.getRevenusDepensesService().copyRevenusDepense(newDroit,
     * oldDroit, droitMembreFamilleSearch);
     * PegasusImplServiceLocator.getFortuneUsuelleService().copyFortuneUsuelle(newDroit, oldDroit,
     * droitMembreFamilleSearch); } } }
     */

    @Override
    public void copyDonneeFinanciere(Droit newDroit, Droit oldDroit,
            DroitMembreFamilleSearch newdroitMembreFamilleSearch) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException {
        if (newDroit.getSimpleVersionDroit().getNoVersion().equals("1")) {
            if (oldDroit != null) {
                PegasusImplServiceLocator.getRenteIjApiService().copyRenteIjApi(newDroit, oldDroit,
                        newdroitMembreFamilleSearch);
                PegasusImplServiceLocator.getFortuneParticuliereService().copyFortuneParticuliere(newDroit, oldDroit,
                        newdroitMembreFamilleSearch);
                PegasusImplServiceLocator.getHabitatService().copyHabitat(newDroit, oldDroit,
                        newdroitMembreFamilleSearch);
                PegasusImplServiceLocator.getDessaisissementService().copyDessaisissement(newDroit, oldDroit,
                        newdroitMembreFamilleSearch);
                PegasusImplServiceLocator.getRevenusDepensesService().copyRevenusDepense(newDroit, oldDroit,
                        newdroitMembreFamilleSearch);
                PegasusImplServiceLocator.getFortuneUsuelleService().copyFortuneUsuelle(newDroit, oldDroit,
                        newdroitMembreFamilleSearch);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. DonneeFinanciereHeaderService
     * #count(ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch)
     */
    @Override
    public int count(DonneeFinanciereHeaderSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count, the search model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. DonneeFinanciereHeaderService
     * #delete(ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader)
     */
    @Override
    public DonneeFinanciereHeader delete(DonneeFinanciereHeader donneeFinanciereHeader) throws DroitException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteDonneFinancierByIdVersionDroit(String idVersionDroit) throws JadePersistenceException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException {
        // create list of id's
        StringBuffer typeDonneFinianciere = new StringBuffer();
        String typeDFinanciere = null;

        // parcours de chaque donnée financiere pour la supprimer
        SimpleDonneeFinanciereHeaderSearch search = new SimpleDonneeFinanciereHeaderSearch();
        search.setForIdVersionDroit(idVersionDroit);
        search = PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService().search(search);

        List<String> idsDonneFinanciere = new ArrayList<String>();

        for (JadeAbstractModel searchDFH : search.getSearchResults()) {
            SimpleDonneeFinanciereHeader dfh = (SimpleDonneeFinanciereHeader) searchDFH;
            idsDonneFinanciere.add(dfh.getIdDonneeFinanciereHeader());
            typeDonneFinianciere.append("-" + dfh.getCsTypeDonneeFinanciere() + "-");
        }
        typeDFinanciere = typeDonneFinianciere.toString();

        // supprime les donnees financieres correspondant à ces id
        try {
            PegasusImplServiceLocator.getFortuneUsuelleService().deleteParListeIdDoFinH(idsDonneFinanciere,
                    typeDFinanciere);

            PegasusImplServiceLocator.getRenteIjApiService()
                    .deleteParListeIdDoFinH(idsDonneFinanciere, typeDFinanciere);

            PegasusImplServiceLocator.getFortuneUsuelleService().deleteParListeIdDoFinH(idsDonneFinanciere,
                    typeDFinanciere);

            PegasusImplServiceLocator.getHabitatService().deleteParListeIdDoFinH(idsDonneFinanciere, typeDFinanciere);
            PegasusImplServiceLocator.getDessaisissementService().deleteParListeIdDoFinH(idsDonneFinanciere,
                    typeDFinanciere);
            PegasusImplServiceLocator.getFortuneParticuliereService().deleteParListeIdDoFinH(idsDonneFinanciere,
                    typeDFinanciere);
            try {
                PegasusImplServiceLocator.getRevenusDepensesService().deleteParListeIdDoFinH(idsDonneFinanciere,
                        typeDFinanciere);
            } catch (RevenuActiviteLucrativeDependanteException e) {
                throw new DonneeFinanciereException("Could not delete revenuActiviteLucrativeDependante", e);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Could not find service", e);
        }
        search = PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService().delete(search);

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. DonneeFinanciereHeaderService#read(java.lang.String)
     */
    @Override
    public DonneeFinanciereHeader read(String idDonneeFinanciereHeader) throws JadePersistenceException, DroitException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DonneeFinanciereHeaderSearch search(DonneeFinanciereHeaderSearch search) throws DonneeFinanciereException,
            JadePersistenceException {
        if (search == null) {
            throw new DonneeFinanciereException("Unable to search search, the model passed is null!");
        }
        return (DonneeFinanciereHeaderSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleDonneeFinanciereHeader setDonneeFinanciereHeaderForCreation(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader)
            throws JadePersistenceException {
        simpleDonneeFinanciereHeader.setIdVersionDroit(simpleVersionDroit.getId());
        simpleDonneeFinanciereHeader.setIsSupprime(Boolean.FALSE);
        simpleDonneeFinanciereHeader.setIdEntity(JadePersistenceManager
                .incIndentifiant(Compteurs.DONNEE_FINANCIERE_ID_ENTITY));
        simpleDonneeFinanciereHeader.setIdEntityGroup(JadePersistenceManager
                .incIndentifiant(Compteurs.DONNEE_FINANCIERE_ID_ENTITY_GROUP));
        simpleDonneeFinanciereHeader.setIdDroitMembreFamille(droitMembreFamille.getId());

        return simpleDonneeFinanciereHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.droit.DonneeFinanciereHeaderService#toggleTookInCalculating(java.lang
     * .String)
     */
    @Override
    public SimpleDonneeFinanciereHeader toggleTookInCalculating(String idDonneeFinanciereHeader, String idVersionDroit)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DroitException {
        SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = PegasusImplServiceLocator
                .getSimpleDonneeFinanciereHeaderService().read(idDonneeFinanciereHeader);

        // SimpleVersionDroit droitVersion =
        // PegasusImplServiceLocator.getSimpleVersionDroitService().read(idVersionDroit);

        Droit droit = PegasusServiceLocator.getDroitService().readDroitFromVersion(idVersionDroit);

        // VersionDroit droitCourant =
        // PegasusServiceLocator.getDroitService().readCurrentVersionDroit(droit.getIdDroit());

        if (droit.getSimpleVersionDroit().getCsEtatDroit().equals(IPCDroits.CS_ENREGISTRE)
                || droit.getSimpleVersionDroit().getCsEtatDroit().equals(IPCDroits.CS_CALCULE)
                || droit.getSimpleVersionDroit().getCsEtatDroit().equals(IPCDroits.CS_AU_CALCUL)) {

            try {
                PegasusServiceLocator.getPCAccordeeService().deleteByIdVersionDroit(droit);
            } catch (JadeApplicationException e) {
                throw new DroitException("Unabled to delete the PCA", e);
            }

            if (simpleDonneeFinanciereHeader.getIsCopieFromPreviousVersion()) {
                simpleDonneeFinanciereHeader.setIsCopieFromPreviousVersion(false);
            } else {
                simpleDonneeFinanciereHeader.setIsCopieFromPreviousVersion(true);
            }
            PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService().update(simpleDonneeFinanciereHeader);

            return simpleDonneeFinanciereHeader;
        } else {
            throw new DroitException("The droit is not is a valid state");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. DonneeFinanciereHeaderService
     * #update(ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader)
     */
    @Override
    public DonneeFinanciereHeader update(DonneeFinanciereHeader donneeFinanciereHeader)
            throws JadePersistenceException, DroitException {
        // TODO Auto-generated method stub
        return null;
    }

}
