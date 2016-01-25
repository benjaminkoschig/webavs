package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import java.math.BigDecimal;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.AllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.AllocationNoelDemandeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.AllocationNoelSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.pcaccordee.AllocationDeNoelService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.process.allocationsNoel.AdressePaiementPrimeNoelService;
import ch.globaz.pegasus.businessimpl.utils.OldPersistence;
import ch.globaz.pegasus.process.allocationsNoel.step1.PCProcessAllocationsNoelEntityHandler;
import ch.globaz.pyxis.business.model.AdressePaiementSimpleModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AllocationDeNoelServiceImpl extends PegasusAbstractServiceImpl implements AllocationDeNoelService {

    /**
     * Ajout d'une adresse de paiement
     * 
     * @param accordeePopulation
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     */
    private static AdressePaiementSimpleModel addAdressePaiement(PCAccordeePopulation accordeePopulation)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, JadeApplicationException {

        String idTiers = accordeePopulation.getPersonneEtendue().getTiers().getIdTiers();
        AdresseTiersDetail adresseTiersDetail = AllocationDeNoelServiceImpl.findAdresse(idTiers,
                PCProcessAllocationsNoelEntityHandler.CS_TYPE_COURRIER);

        if (AllocationDeNoelServiceImpl.isAdresseFounded(adresseTiersDetail)) {
            // On test que l'adresse trouvé ne soit pas une case postale et si c'est le cas on recherche l'adresse de
            // domicile
            if (!JadeStringUtil.isBlank(adresseTiersDetail.getFields().get(
                    TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE))) {
                adresseTiersDetail = AllocationDeNoelServiceImpl.findAdresse(idTiers,
                        PCProcessAllocationsNoelEntityHandler.CS_TYPE_DOMICILE);
            }
        }

        if (AllocationDeNoelServiceImpl.isAdresseFounded(adresseTiersDetail)) {
            AdressePaiementPrimeNoelService s = new AdressePaiementPrimeNoelService();
            AdressePaiementSimpleModel adressePaiementSimpleModel = s.create(
                    adresseTiersDetail.getFields().get(TIAbstractAdresseDataSource.ADRESSE_ID_ADRESSE),
                    accordeePopulation.getPersonneEtendue().getTiers().getIdTiers());
            return adressePaiementSimpleModel;
        } else {
            throw new AllocationDeNoelException("Aucune adresse de courrier trouvé");
        }
    }

    private static AdressePaiementSimpleModel createAdressForPaiementPostalIfNotExiste(
            final PCAccordeePopulation accordeePopulation) throws AllocationDeNoelException {

        OldPersistence<AdressePaiementSimpleModel> oldPercistence = new OldPersistence<AdressePaiementSimpleModel>() {

            @Override
            public AdressePaiementSimpleModel action() throws Exception {
                boolean hasAdresseAllocationNoel = TIBusinessServiceLocator.getAdresseService().hasAdressePaiement(
                        accordeePopulation.getPersonneEtendue().getTiers().getIdTiers(),
                        IPRConstantesExternes.TIERS_CS_DOMAINE_ALLOCATION_DE_NOEL, JACalendar.todayJJsMMsAAAA());

                if (!hasAdresseAllocationNoel) {
                    try {
                        AdressePaiementSimpleModel adressePaiementSimpleModel = AllocationDeNoelServiceImpl
                                .addAdressePaiement(accordeePopulation);

                        JadeThread.logInfo("", "pegasus.process.allocationsNoel.newAdressePaiementPostal",
                                new String[] { adressePaiementSimpleModel.getIdAdressePmtUnique() });
                        return adressePaiementSimpleModel;
                    } catch (Exception e) {
                        JadeThread.logError("", "Impossible de créer une adresse de paiement pour:"
                                + accordeePopulation.getPersonneEtendue().getTiers().getDesignation1() + " "
                                + accordeePopulation.getPersonneEtendue().getTiers().getDesignation2() + " "
                                + accordeePopulation.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()
                                + " Exception:" + e.toString());
                    }
                }
                return null;
            }
        };
        try {
            return oldPercistence.execute();
        } catch (Exception e) {
            throw new AllocationDeNoelException("", e);
        }

    }

    private static AdresseTiersDetail findAdresse(String idTiers, String csTypeAdresse)
            throws JadePersistenceException, JadeApplicationException, JadeApplicationServiceNotAvailableException {
        AdresseTiersDetail adresseTiersDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers,
                Boolean.TRUE, JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                csTypeAdresse, null);
        if (adresseTiersDetail == null) {
            adresseTiersDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers, Boolean.TRUE,
                    JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT,
                    csTypeAdresse, null);

        }
        return adresseTiersDetail;
    }

    private static boolean hasPaiementByPaimentPostal(PCAccordeePopulation accordeePopulation) {
        return !(accordeePopulation.isHasRepresantLegal() || AllocationDeNoelServiceImpl
                .isPcaTypeHome(accordeePopulation));
    }

    private static boolean isAdresseFounded(AdresseTiersDetail adresseTiersDetail) {
        boolean isAdresseFounded = ((adresseTiersDetail != null) && adresseTiersDetail.getFields() != null && !JadeStringUtil
                .isBlankOrZero(adresseTiersDetail.getFields().get(TIAbstractAdresseDataSource.ADRESSE_ID_ADRESSE)));
        return isAdresseFounded;
    }

    private static boolean isPcaTypeHome(PCAccordeePopulation accordeePopulation) {
        return accordeePopulation.getSimplePCAccordee().getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_HOME);
    }

    private int count(AllocationNoelDemandeSearch search) throws JadePersistenceException {
        if (search == null) {
            throw new IllegalArgumentException("Unable to count, the search is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public AllocationNoel create(PCAccordeePopulation pca, float montantAllocation, String anneeAllocation,
            String idAdressePaiementPostaleCreer, Boolean isCoupleSeparer) throws JadePersistenceException,
            JadeApplicationException {

        AdressePaiementSimpleModel adressePaiementSimpleModel = null;

        // recherche du nbres de membres de famille
        int nbreMembres = PegasusServiceLocator.getPCAccordeeService().countMembreFamilleForPlanRetenu(
                pca.getSimplePlanDeCalcul().getId());

        if (isCoupleSeparer) {
            if (pca.getSimplePCAccordee().getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                nbreMembres = 1;
            } else {
                nbreMembres = nbreMembres - 1;
            }
        }

        if (nbreMembres == 0) {
            nbreMembres = 1;
            JadeThread.logWarn("",
                    "Le nombre de personnes pour calculer l'allocation de noël a été forcé à 1, idPlancal:"
                            + pca.getSimplePlanDeCalcul().getId() + ", isCoupleSeparer:" + isCoupleSeparer + ")");
        }

        // ********** montant allocation, tableau req, conj *************/
        BigDecimal[] montantsAllocation = PegasusImplServiceLocator.getSimpleAllocationDeNoelService()
                .computeAndGetMontantAllocation(nbreMembres, pca, montantAllocation);

        // ********* Gestion conjoint ******************/
        Boolean isPcConjoint = !isDom2R(pca);
        // Si conjoint on set l'id prestaccordee conjoint, sinon null
        String idPrestVersesConjoint = null;

        if (isPcConjoint) {
            idPrestVersesConjoint = pca.getSimplePCAccordee().getIdPrestationAccordeeConjoint();
        }

        boolean hasPaiementPostal = AllocationDeNoelServiceImpl.hasPaiementByPaimentPostal(pca);
        // cas général (ni avec représentant légal, ni en home)

        if (hasPaiementPostal) {
            adressePaiementSimpleModel = AllocationDeNoelServiceImpl.createAdressForPaiementPostalIfNotExiste(pca);
            if (adressePaiementSimpleModel != null) {
                if (JadeStringUtil.isBlankOrZero(idAdressePaiementPostaleCreer)) {
                    idAdressePaiementPostaleCreer = adressePaiementSimpleModel.getId();
                } else {
                    throw new AllocationDeNoelException("Tow adressePaiement created (idAdressePaiement1:"
                            + idAdressePaiementPostaleCreer + ", idAdressePaiement"
                            + adressePaiementSimpleModel.getId() + ")");
                }
            }
        }

        // ************** generation et persistance
        return generateAndPersistAllocationsCompletes(pca, montantsAllocation, hasPaiementPostal, anneeAllocation,
                idPrestVersesConjoint, nbreMembres, idAdressePaiementPostaleCreer);

    }

    private SimpleInformationsComptabilite createInfoCompta(String idTiersAdressePaiement, String idCompteAnnexe)
            throws RentesAccordeesException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleInformationsComptabilite simpleInformationsComptabilite = new SimpleInformationsComptabilite();
        simpleInformationsComptabilite.setIdTiersAdressePmt(idTiersAdressePaiement);
        simpleInformationsComptabilite.setIdCompteAnnexe(idCompteAnnexe);
        simpleInformationsComptabilite = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(
                simpleInformationsComptabilite);
        return simpleInformationsComptabilite;
    }

    private SimplePrestationsAccordees createPresationAccordee(String idTiersBeneficiaire, String montants,
            String codePrestAllocNoel, String date, SimpleInformationsComptabilite simpleInformationsComptabilite,
            Boolean isPCABloque) throws AllocationDeNoelException, JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {

        SimplePrestationsAccordees spa = new SimplePrestationsAccordees();
        spa.setIdTiersBeneficiaire(idTiersBeneficiaire);
        spa.setMontantPrestation(montants);
        spa.setCodePrestation(codePrestAllocNoel);
        spa.setDateDebutDroit(date);
        spa.setDateFinDroit(date);
        spa.setCsGenre(IREPrestationAccordee.CS_GENRE_PC);
        spa.setIdInfoCompta(simpleInformationsComptabilite.getIdInfoCompta());
        spa.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        spa.setIsRetenues(false);
        spa.setIsPrestationBloquee(isPCABloque);
        spa.setIsAttenteMajBlocage(false);
        spa.setIsAttenteMajRetenue(false);
        spa = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(spa);
        return spa;
    }

    @Override
    public AllocationNoel readAllocationNoelByIdPca(String idPca) throws PCAccordeeException,
            AllocationDeNoelException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        AllocationNoelSearch search = new AllocationNoelSearch();
        search.setForIdPCAccordee(idPca);
        search = PegasusServiceLocator.getAllocationDeNoelService().search(search);
        if (search.getSize() > 0) {
            if (search.getSize() > 1) {
                throw new PCAccordeeException("Too many allocationNoel founded with this idPca:" + idPca);
            }
            return (AllocationNoel) search.getSearchResults()[0];
        }
        return new AllocationNoel();
    }

    @Override
    public AllocationNoelSearch search(AllocationNoelSearch search) throws JadePersistenceException {

        if (null == search) {
            throw new IllegalArgumentException("Unable to search, the search is null!");
        }

        return (AllocationNoelSearch) JadePersistenceManager.search(search);
    }

    private AllocationNoel generateAndPersistAllocationsCompletes(PCAccordeePopulation pca,
            BigDecimal[] montantsAllocation, boolean hasPaiementPostal, String anneeAllocation,
            String idPrestVersesConjoint, int nbreMembres, String idAdressePaiementPostalCreer)
            throws JadePersistenceException, JadeApplicationException {
        String date = PCProcessAllocationsNoelEntityHandler.MOIS_ALLOCATION_NOEL + "." + JACalendar.today().getYear();

        // définition si pcAccordee bloque
        Boolean isPCABloque = pca.getSimplePrestationsAccordees().getIsPrestationBloquee();

        AllocationNoel allocationNoel = new AllocationNoel();

        allocationNoel.setSimplePCAccordee(pca.getSimplePCAccordee());

        String codePrestAllocNoel = getCodePrestationAllocationNoel(pca.getSimplePrestationsAccordees()
                .getCodePrestation(), hasPaiementPostal);

        // ***** Pour le requérant
        SimpleInformationsComptabilite simpleInformationsComptabilite = createInfoCompta(pca
                .getSimpleInformationsComptabilite().getIdTiersAdressePmt(), pca.getSimpleInformationsComptabilite()
                .getIdCompteAnnexe());

        SimplePrestationsAccordees spa = createPresationAccordee(pca.getSimplePrestationsAccordees()
                .getIdTiersBeneficiaire(), montantsAllocation[0].toString(), codePrestAllocNoel, date,
                simpleInformationsComptabilite, isPCABloque);

        allocationNoel.setSimplePrestationsAccordees(spa);
        SimplePrestationsAccordees spaConjoint = new SimplePrestationsAccordees();

        // ***** Pour le conjoint (DOM2R)
        if (isDom2R(pca)) {
            SimplePrestationsAccordees prestationsAccordeesConjointPc = PegasusImplServiceLocator
                    .getSimplePrestatioAccordeeService().read(
                            pca.getSimplePCAccordee().getIdPrestationAccordeeConjoint());

            SimpleInformationsComptabilite informationsComptabiliteConjoint = CorvusServiceLocator
                    .getSimpleInformationsComptabiliteService().read(prestationsAccordeesConjointPc.getIdInfoCompta());

            // on vérifie que la prestation existe bien en BD on ne peut pas utiliser isNew car il est basé sur les spy
            // et la table des prestation n'a pas de spy!!!!
            if (!JadeStringUtil.isBlankOrZero(prestationsAccordeesConjointPc.getIdTiersBeneficiaire())) {
                // Non, ce n'est pas une erreur mais une volonté. on paie sur le compte annexe du tiers requérant
                SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint = createInfoCompta(
                        informationsComptabiliteConjoint.getIdTiersAdressePmt(), pca
                                .getSimpleInformationsComptabilite().getIdCompteAnnexe());

                // ************** PrestationVerses
                spaConjoint = createPresationAccordee(prestationsAccordeesConjointPc.getIdTiersBeneficiaire(),
                        montantsAllocation[1].toString(), codePrestAllocNoel, date,
                        simpleInformationsComptabiliteConjoint, isPCABloque);

                allocationNoel.setSimplePrestationsAccordeesConjoint(spaConjoint);
            } else {
                throw new AllocationDeNoelException("Unabeled to read the prestation with this idPrestation: "
                        + pca.getSimplePCAccordee().getIdPrestationAccordeeConjoint()
                        + " , this PCA is detected has a DOM2!");
            }
        }

        // creation de l'allocation de Noël
        SimpleAllocationNoel allocationDeNoel = new SimpleAllocationNoel();
        allocationDeNoel.setIdDemande(pca.getSimpleDroit().getIdDemandePC());
        allocationDeNoel.setIdPCAccordee(pca.getSimplePCAccordee().getId());
        allocationDeNoel.setIdPrestationAccordee(spa.getIdPrestationAccordee());
        allocationDeNoel.setIdPrestationAccordeeConjoint(spaConjoint.getIdPrestationAccordee());
        allocationDeNoel.setMontantAllocation(montantsAllocation[2].toString());
        allocationDeNoel.setAnneeAllocation(anneeAllocation);
        allocationDeNoel.setNbrePersonnes(String.valueOf(nbreMembres));
        allocationDeNoel.setHasPaiementPostal(hasPaiementPostal);
        allocationDeNoel.setIdAdressePaiementPostaleCreer(idAdressePaiementPostalCreer);

        allocationNoel.setSimpleAllocationNoel(PegasusImplServiceLocator.getSimpleAllocationDeNoelService().create(
                allocationDeNoel));

        return allocationNoel;
    }

    private String getCodePrestationAllocationNoel(String codePrestation, boolean isRepresentantLegalOuDansHome)
            throws AllocationDeNoelException {
        if (JadeStringUtil.isEmpty(codePrestation)) {
            throw new AllocationDeNoelException(
                    "Impossible de retrouver le code prestation allocation de Noel car le code prestation de la rente principale est null");
        }

        if (PRCodePrestationPC.isCodePrestationAVS(codePrestation)) {
            if (isRepresentantLegalOuDansHome) {
                return PRCodePrestationPC._119.getCodePrestationAsString();
            } else {
                return PRCodePrestationPC._118.getCodePrestationAsString();
            }
        } else if (PRCodePrestationPC.isCodePrestationAI(codePrestation)) {
            if (isRepresentantLegalOuDansHome) {
                return PRCodePrestationPC._159.getCodePrestationAsString();
            } else {
                return PRCodePrestationPC._158.getCodePrestationAsString();
            }
        } else {
            throw new AllocationDeNoelException(
                    "Impossible de retrouver le type de prestation (AVS, AI) pour le code prestation : "
                            + codePrestation);
        }
    }

    @Override
    public boolean hasAlreadyRecivedAllocationNoelForTheYear(int year, String idDemande)
            throws JadePersistenceException {
        if (year == 0) {
            throw new IllegalArgumentException("Unable to hasAlreadyRecivedAllocationNoelForTheYear, the year is null!");
        }

        if (idDemande == null) {
            throw new IllegalArgumentException(
                    "Unable to hasAlreadyRecivedAllocationNoelForTheYear, the idDemande is null!");
        }

        AllocationNoelDemandeSearch search = new AllocationNoelDemandeSearch();
        search.setForAnnee(String.valueOf(year));
        search.setForIdDemande(idDemande);
        int nb = count(search);

        return nb != 0;
    }

    private boolean isDom2R(PCAccordeePopulation pca) {
        return !JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPrestationAccordeeConjoint());
    }
}
