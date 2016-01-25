package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelle;
import ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelleSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.FortuneUsuelleService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;

public class FortuneUsuelleServiceImpl extends PegasusAbstractServiceImpl implements FortuneUsuelleService {

    private AssuranceVie copyAssuranceVie(FortuneUsuelle fortuneUsuelle, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AssuranceVie assuranceVie = new AssuranceVie();
        try {
            assuranceVie.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            assuranceVie.setSimpleAssuranceVie(fortuneUsuelle.getAssuranceVie().getSimpleAssuranceVie());
            assuranceVie.setTiersCompagnie(fortuneUsuelle.getAssuranceVie().getTiersCompagnie());
            assuranceVie = createAssuranceVie(newDroit.getSimpleVersionDroit(), droitMembreFamille, assuranceVie);
            return assuranceVie;
        } catch (AssuranceVieException e) {
            throw new DonneeFinanciereException("Unable to copy the assuranceVie", e);
        }
    }

    private AutresDettesProuvees copyAutresDettesProuvees(FortuneUsuelle fortuneUsuelle, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AutresDettesProuvees autresDettesProuvees = new AutresDettesProuvees();
        try {
            autresDettesProuvees.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            autresDettesProuvees.setSimpleAutresDettesProuvees(fortuneUsuelle.getSimpleAutresDettesProuvees());
            autresDettesProuvees = createAutresDettesProuvees(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    autresDettesProuvees);
            return autresDettesProuvees;
        } catch (AutresDettesProuveesException e) {
            throw new DonneeFinanciereException("Unable to copy the autresDettesProuvees", e);
        }
    }

    private BienImmobilierHabitationNonPrincipale copyBienImmobilierHabitationNonPrincipale(
            FortuneUsuelle fortuneUsuelle, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        BienImmobilierHabitationNonPrincipale principale = new BienImmobilierHabitationNonPrincipale();
        try {
            principale.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            principale.setSimpleBienImmobilierHabitationNonPrincipale(fortuneUsuelle
                    .getBienImmobilierHabitationNonPrincipale().getSimpleBienImmobilierHabitationNonPrincipale());
            principale.setLocalite(fortuneUsuelle.getBienImmobilierHabitationNonPrincipale().getLocalite());
            principale.setTiersCompagnie(fortuneUsuelle.getBienImmobilierHabitationNonPrincipale().getTiersCompagnie());

            principale = createBienImmobilierHabitationNonPrincipale(newDroit.getSimpleVersionDroit(),
                    droitMembreFamille, principale);
            return principale;
        } catch (BienImmobilierHabitationNonPrincipaleException e) {
            throw new DonneeFinanciereException("Unable to copy the contratEntretienViager", e);
        }
    }

    private BienImmobilierNonHabitable copyBienImmobilierNonHabitable(FortuneUsuelle fortuneUsuelle, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        BienImmobilierNonHabitable habitable = new BienImmobilierNonHabitable();
        try {
            habitable.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            habitable.setSimpleBienImmobilierNonHabitable(fortuneUsuelle.getBienImmobilierNonHabitable()
                    .getSimpleBienImmobilierNonHabitable());
            habitable.setLocalite(fortuneUsuelle.getBienImmobilierNonHabitable().getLocalite());
            habitable.setTiersCompagnie(fortuneUsuelle.getBienImmobilierNonHabitable().getTiersCompagnie());
            habitable = createBienImmobilierNonHabitable(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    habitable);
            return habitable;
        } catch (BienImmobilierNonHabitableException e) {
            throw new DonneeFinanciereException("Unable to copy the contratEntretienViager", e);
        }
    }

    private BienImmobilierServantHabitationPrincipale copyBienImmobilierServantHabitationPrincipale(
            FortuneUsuelle fortuneUsuelle, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        BienImmobilierServantHabitationPrincipale principale = new BienImmobilierServantHabitationPrincipale();
        try {
            principale.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            principale.setSimpleBienImmobilierServantHabitationPrincipale(fortuneUsuelle
                    .getBienImmobilierServantHabitationPrincipale()
                    .getSimpleBienImmobilierServantHabitationPrincipale());
            principale.setLocalite(fortuneUsuelle.getBienImmobilierNonHabitable().getLocalite());
            principale.setTiersCompagnie(fortuneUsuelle.getBienImmobilierServantHabitationPrincipale()
                    .getTiersCompagnie());
            principale = createBienImmobilierServantHabitationPrincipale(newDroit.getSimpleVersionDroit(),
                    droitMembreFamille, principale);
            return principale;
        } catch (BienImmobilierServantHabitationPrincipaleException e) {
            throw new DonneeFinanciereException("Unable to copy the bienImmobilierServantHabitationPrincipale", e);
        }
    }

    private CapitalLPP copyCapitalLPP(FortuneUsuelle fortuneUsuelle, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        CapitalLPP capitalLPP = new CapitalLPP();
        try {
            capitalLPP.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            capitalLPP.setSimpleCapitalLPP(fortuneUsuelle.getCapitalLPP().getSimpleCapitalLPP());
            capitalLPP.setCaisse(fortuneUsuelle.getCapitalLPP().getCaisse());
            capitalLPP = createCapitalLPP(newDroit.getSimpleVersionDroit(), droitMembreFamille, capitalLPP);
            return capitalLPP;
        } catch (CapitalLPPException e) {
            throw new DonneeFinanciereException("Unable to copy the capitalLPP", e);
        }
    }

    private CompteBancaireCCP copyCompteBancaireCCP(FortuneUsuelle fortuneUsuelle, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        CompteBancaireCCP compteBancaireCCP = new CompteBancaireCCP();
        try {
            compteBancaireCCP.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            compteBancaireCCP.setSimpleCompteBancaireCCP(fortuneUsuelle.getSimpleCompteBancaireCCP());
            compteBancaireCCP = createCompteBancaireCCP(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    compteBancaireCCP);
            return compteBancaireCCP;
        } catch (CompteBancaireCCPException e) {
            throw new DonneeFinanciereException("Unable to copy the capitalLPP", e);
        }
    }

    @Override
    public void copyFortuneUsuelle(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException {
        try {
            List<String> lisCsType = new ArrayList<String>();

            lisCsType.add(IPCDroits.CS_COMPTE_BANCAIRE_POSTAL);
            lisCsType.add(IPCDroits.CS_ASSURANCE_VIE);
            lisCsType.add(IPCDroits.CS_AUTRES_DETTES_PROUVEES);
            lisCsType.add(IPCDroits.CS_BIENS_IMMOBILIERS_HABITATION_NON_PRINCIPALE);
            lisCsType.add(IPCDroits.CS_BIENS_IMMOBILIERS_NON_HABITABLE);
            lisCsType.add(IPCDroits.CS_BIENS_IMMOBILIERS_SERVANT_HABITATION_PRINCIPALE);
            lisCsType.add(IPCDroits.CS_CAPITAL_LPP);
            lisCsType.add(IPCDroits.CS_TITRES);

            FortuneUsuelleSearch searchModel = new FortuneUsuelleSearch();
            searchModel.setWhereKey(FortuneUsuelleSearch.FOR_ALL_VALABLE_LE);
            searchModel.setInCsTypeDonneeFinancierer(lisCsType);
            searchModel.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }

            searchModel = PegasusServiceLocator.getDroitService().searchFortuneUsuelle(searchModel);

            FortuneUsuelle fortuneUsuelle = null;
            String csType = null;
            DroitMembreFamille droitMembreFamille = null;
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                try {

                    fortuneUsuelle = (FortuneUsuelle) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, fortuneUsuelle.getMembreFamilleEtendu().getDroitMembreFamille()
                                    .getSimpleDroitMembreFamille().getIdMembreFamilleSF());

                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        csType = fortuneUsuelle.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();

                        fortuneUsuelle.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils
                                .copySimpleDonneFianciere(fortuneUsuelle.getSimpleDonneeFinanciereHeader()));

                        if ((fortuneUsuelle.getAssuranceVie() != null) && IPCDroits.CS_ASSURANCE_VIE.equals(csType)) {
                            copyAssuranceVie(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                        if ((fortuneUsuelle.getBienImmobilierHabitationNonPrincipale() != null)
                                && IPCDroits.CS_BIENS_IMMOBILIERS_HABITATION_NON_PRINCIPALE.equals(csType)) {
                            copyBienImmobilierHabitationNonPrincipale(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                        if (((fortuneUsuelle.getBienImmobilierNonHabitable()) != null)
                                && IPCDroits.CS_BIENS_IMMOBILIERS_NON_HABITABLE.equals(csType)) {
                            copyBienImmobilierNonHabitable(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                        if ((fortuneUsuelle.getBienImmobilierServantHabitationPrincipale() != null)
                                && IPCDroits.CS_BIENS_IMMOBILIERS_SERVANT_HABITATION_PRINCIPALE.equals(csType)) {
                            copyBienImmobilierServantHabitationPrincipale(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                        if ((fortuneUsuelle.getCapitalLPP() != null) && IPCDroits.CS_CAPITAL_LPP.equals(csType)) {
                            copyCapitalLPP(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                        if ((fortuneUsuelle.getSimpleAutresDettesProuvees() != null)
                                && IPCDroits.CS_AUTRES_DETTES_PROUVEES.equals(csType)) {
                            copyAutresDettesProuvees(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                        if ((fortuneUsuelle.getSimpleCompteBancaireCCP() != null)
                                && IPCDroits.CS_COMPTE_BANCAIRE_POSTAL.equals(csType)) {
                            copyCompteBancaireCCP(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                        if ((fortuneUsuelle.getSimpleTitre() != null) && IPCDroits.CS_TITRES.equals(csType)) {
                            copyTitre(fortuneUsuelle, newDroit, droitMembreFamille);
                        }
                    }

                } catch (JadeCloneModelException e) {
                    throw new DonneeFinanciereException("Unable to clone (habitat) for the copy ", e);
                }
            }
        } catch (DroitException e) {
            throw new DonneeFinanciereException("Unable to search the renteIjApi for the copy", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new DonneeFinanciereException(
                    "Unable to copy the donneeFinanciere (habitat) probleme with the persistence" + e);
        }
    }

    private Titre copyTitre(FortuneUsuelle fortuneUsuelle, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Titre titre = new Titre();
        try {
            titre.setSimpleDonneeFinanciereHeader(fortuneUsuelle.getSimpleDonneeFinanciereHeader());
            titre.setSimpleTitre(fortuneUsuelle.getSimpleTitre());
            titre = createTitre(newDroit.getSimpleVersionDroit(), droitMembreFamille, titre);
            return titre;
        } catch (TitreException e) {
            throw new DonneeFinanciereException("Unable to copy the capitalLPP", e);
        }
    }

    @Override
    public AssuranceVie createAssuranceVie(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AssuranceVie assuranceVie) throws AssuranceVieException,
            JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AssuranceVieException("Unable to create assuranceVie, the droitMembreFamille is null or new");
        }
        if (assuranceVie == null) {
            throw new AssuranceVieException("Unable to create assuranceVie, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new AssuranceVieException("Unable to create assuranceVie, the simpleVersionDroit is null");
        }

        assuranceVie.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_ASSURANCE_VIE);

        try {
            assuranceVie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            assuranceVie.getSimpleDonneeFinanciereHeader()));

            assuranceVie = PegasusImplServiceLocator.getAssuranceVieService().create(assuranceVie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceVieException("Service not available - " + e.getMessage());
        }

        return assuranceVie;
    }

    @Override
    public AutresDettesProuvees createAutresDettesProuvees(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AutresDettesProuvees autresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AutresDettesProuveesException(
                    "Unable to create autresDettesProuvees, the droitMembreFamille is null or new");
        }
        if (autresDettesProuvees == null) {
            throw new AutresDettesProuveesException("Unable to create autresDettesProuvees, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new AutresDettesProuveesException(
                    "Unable to create autresDettesProuvees, the simpleVersionDroit is null");
        }

        autresDettesProuvees.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_AUTRES_DETTES_PROUVEES);

        try {
            autresDettesProuvees.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, autresDettesProuvees.getSimpleDonneeFinanciereHeader()));

            autresDettesProuvees = PegasusImplServiceLocator.getAutresDettesProuveesService().create(
                    autresDettesProuvees);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresDettesProuveesException("Service not available - " + e.getMessage());
        }

        return autresDettesProuvees;
    }

    @Override
    public BienImmobilierHabitationNonPrincipale createBienImmobilierHabitationNonPrincipale(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to create bienImmobilierHabitationNonPrincipale, the droitMembreFamille is null or new");
        }
        if (bienImmobilierHabitationNonPrincipale == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to create bienImmobilierHabitationNonPrincipale, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to create bienImmobilierHabitationNonPrincipale, the simpleVersionDroit is null");
        }

        bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_BIENS_IMMOBILIERS_HABITATION_NON_PRINCIPALE);

        try {
            bienImmobilierHabitationNonPrincipale.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader()));

            bienImmobilierHabitationNonPrincipale = PegasusImplServiceLocator
                    .getBienImmobilierHabitationNonPrincipaleService().create(bienImmobilierHabitationNonPrincipale);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierHabitationNonPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierHabitationNonPrincipale;
    }

    @Override
    public BienImmobilierNonHabitable createBienImmobilierNonHabitable(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to create bienImmobilierNonHabitable, the droitMembreFamille is null or new");
        }
        if (bienImmobilierNonHabitable == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to create bienImmobilierNonHabitable, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to create bienImmobilierNonHabitable, the simpleVersionDroit is null");
        }

        bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_BIENS_IMMOBILIERS_NON_HABITABLE);

        try {
            bienImmobilierNonHabitable.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader()));

            bienImmobilierNonHabitable = PegasusImplServiceLocator.getBienImmobilierNonHabitableService().create(
                    bienImmobilierNonHabitable);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierNonHabitableException("Service not available - " + e.getMessage());
        }

        return bienImmobilierNonHabitable;
    }

    @Override
    public BienImmobilierServantHabitationPrincipale createBienImmobilierServantHabitationPrincipale(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException,
            DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to create bienImmobilierServantHabitationPrincipale, the droitMembreFamille is null or new");
        }
        if (bienImmobilierServantHabitationPrincipale == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to create bienImmobilierServantHabitationPrincipale, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to create bienImmobilierServantHabitationPrincipale, the simpleVersionDroit is null");
        }

        bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_BIENS_IMMOBILIERS_SERVANT_HABITATION_PRINCIPALE);

        try {
            bienImmobilierServantHabitationPrincipale.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille,
                            bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader()));

            bienImmobilierServantHabitationPrincipale = PegasusImplServiceLocator
                    .getBienImmobilierServantHabitationPrincipaleService().create(
                            bienImmobilierServantHabitationPrincipale);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierServantHabitationPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierServantHabitationPrincipale;
    }

    @Override
    public CapitalLPP createCapitalLPP(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            CapitalLPP capitalLPP) throws CapitalLPPException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new CapitalLPPException("Unable to create capitalLPP, the droitMembreFamille is null or new");
        }
        if (capitalLPP == null) {
            throw new CapitalLPPException("Unable to create capitalLPP, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new CapitalLPPException("Unable to create capitalLPP, the simpleVersionDroit is null");
        }

        capitalLPP.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_CAPITAL_LPP);

        try {
            capitalLPP.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            capitalLPP.getSimpleDonneeFinanciereHeader()));

            capitalLPP = PegasusImplServiceLocator.getCapitalLPPService().create(capitalLPP);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CapitalLPPException("Service not available - " + e.getMessage());
        }

        return capitalLPP;
    }

    @Override
    public CompteBancaireCCP createCompteBancaireCCP(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, CompteBancaireCCP compteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new CompteBancaireCCPException(
                    "Unable to create compteBancaireCCP, the droitMembreFamille is null or new");
        }
        if (compteBancaireCCP == null) {
            throw new CompteBancaireCCPException("Unable to create compteBancaireCCP, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new CompteBancaireCCPException("Unable to create compteBancaireCCP, the simpleVersionDroit is null");
        }

        compteBancaireCCP.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_COMPTE_BANCAIRE_POSTAL);

        try {
            compteBancaireCCP.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, compteBancaireCCP.getSimpleDonneeFinanciereHeader()));

            compteBancaireCCP = PegasusImplServiceLocator.getCompteBancaireCCPService().create(compteBancaireCCP);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CompteBancaireCCPException("Service not available - " + e.getMessage());
        }

        return compteBancaireCCP;
    }

    @Override
    public Titre createTitre(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, Titre titre)
            throws TitreException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new TitreException("Unable to create titre, the droitMembreFamille is null or new");
        }
        if (titre == null) {
            throw new TitreException("Unable to create titre, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new TitreException("Unable to create titre, the droit is null or new");
        }

        titre.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_TITRES);

        try {
            titre.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            titre.getSimpleDonneeFinanciereHeader()));

            titre = PegasusImplServiceLocator.getTitreService().create(titre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TitreException("Service not available - " + e.getMessage());
        }

        return titre;
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_COMPTE_BANCAIRE_POSTAL + "-")) {
            PegasusImplServiceLocator.getSimpleCompteBancaireCCPService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_ASSURANCE_VIE + "-")) {
            PegasusImplServiceLocator.getSimpleAssuranceVieService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_AUTRES_DETTES_PROUVEES + "-")) {
            PegasusImplServiceLocator.getSimpleAutresDettesProuveesService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_BIENS_IMMOBILIERS_HABITATION_NON_PRINCIPALE + "-")) {
            PegasusImplServiceLocator.getSimpleBienImmobilierHabitationNonPrincipaleService().deleteParListeIdDoFinH(
                    idsDonneFinanciere);
        }

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_BIENS_IMMOBILIERS_NON_HABITABLE + "-")) {
            PegasusImplServiceLocator.getSimpleBienImmobilierNonHabitableService().deleteParListeIdDoFinH(
                    idsDonneFinanciere);
        }

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_BIENS_IMMOBILIERS_SERVANT_HABITATION_PRINCIPALE + "-")) {
            PegasusImplServiceLocator.getSimpleBienImmobilierServantHabitationPrincipaleService()
                    .deleteParListeIdDoFinH(idsDonneFinanciere);
        }

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_CAPITAL_LPP + "-")) {
            PegasusImplServiceLocator.getSimpleCapitalLPPService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }

        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_TITRES + "-")) {
            PegasusImplServiceLocator.getSimpleTitreService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }

    }

}
