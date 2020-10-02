package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import ch.globaz.pegasus.business.constantes.ConstantesCalcul;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesFraisGarde;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesFraisGardeSearch;
import ch.globaz.pegasus.business.models.calcul.CalculMoisSuivantSearch;
import ch.globaz.pegasus.business.models.droit.*;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGarde;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetique;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;

public class RevenusFraisGardeChecker extends PegasusAbstractChecker {

    public static void checkSupositionFraisGarde(SimpleDonneeFinanciereHeader donneeFinanciereHeader,String csTypeRevenuDepenseIgnore) throws JadeApplicationServiceNotAvailableException {

        SimpleVersionDroitSearch vdSearch = new SimpleVersionDroitSearch();
        vdSearch.setForIdVersionDroit(donneeFinanciereHeader.getIdVersionDroit());
        if (JadeDateUtil.isDateBefore(donneeFinanciereHeader.getDateFin(), donneeFinanciereHeader.getDateDebut())) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.dates.consistency");
        }
        try {
            vdSearch = PegasusImplServiceLocator.getSimpleVersionDroitService().search(vdSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new JadeApplicationServiceNotAvailableException("Service not available - " + e.getMessage());
        } catch (DroitException e) {
            throw new JadeApplicationServiceNotAvailableException("Unable to check donneeFinanciereHeader", e);
        } catch (JadePersistenceException e) {
            throw new JadeApplicationServiceNotAvailableException("Unable to check donneeFinanciereHeader", e);
        }

        SimpleVersionDroit vrDroit = (SimpleVersionDroit) vdSearch.getSearchResults()[0];
        List<String> listIdMembres = new ArrayList<String>();
        listIdMembres.add(donneeFinanciereHeader.getIdDroitMembreFamille());
        try {
            CalculDonneesFraisGardeSearch calculDonneesFraisGardeSearch = new CalculDonneesFraisGardeSearch();
            calculDonneesFraisGardeSearch.setForIdDroit(vrDroit.getIdDroit());
            calculDonneesFraisGardeSearch.setForNotCsTypeDonneeFinanciere(csTypeRevenuDepenseIgnore);
            calculDonneesFraisGardeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            calculDonneesFraisGardeSearch.setWhereKey("checkFraisGardeExists");
            if(PegasusImplServiceLocator.getCalculDonneesFraisGardeService().count(calculDonneesFraisGardeSearch)>0){
                JadeThread.logWarn(donneeFinanciereHeader.getClass().getName(),
                        "pegasus.simpleRevenuHypothetique.fraisdegarde.existsAutres");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new JadeApplicationServiceNotAvailableException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new JadeApplicationServiceNotAvailableException("Service not available - " + e.getMessage());
        } catch (CalculException e) {
            throw new JadeApplicationServiceNotAvailableException("Service not available - " + e.getMessage());
        }


    }

    public static void checkCanHaveFraisGarde(FraisGarde fraisGarde) throws JadeApplicationServiceNotAvailableException {
        String dateDebutPeriode = fraisGarde.getSimpleDonneeFinanciereHeader().getDateDebut();

        MembreFamilleEtenduSearch vdSearch = new MembreFamilleEtenduSearch();
        vdSearch.setForIdDroitMembreFamille(fraisGarde.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
        try {
            vdSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(vdSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new JadeApplicationServiceNotAvailableException("Service not available - " + e.getMessage());
        } catch (DroitException e) {
            throw new JadeApplicationServiceNotAvailableException("Unable to check donneeFinanciereHeader", e);
        } catch (JadePersistenceException e) {
            throw new JadeApplicationServiceNotAvailableException("Unable to check donneeFinanciereHeader", e);
        }
        MembreFamilleEtendu membreFamille = (MembreFamilleEtendu) vdSearch.getSearchResults()[0];
        String dateNaissance = membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getPersonne().getDateNaissance();
        String[] param = {dateDebutPeriode};
        if (JadeDateUtil.getNbYearsBetween(dateNaissance, JadeDateUtil.getLastDateOfMonth(dateDebutPeriode)) >=11) {
            JadeThread.logError(fraisGarde.getSimpleDonneeFinanciereHeader().getClass().getName(),
                    "pegasus.simpleRevenuHypothetique.fraisdegarde.enfant11.mandatory",param);
        }


    }
}
