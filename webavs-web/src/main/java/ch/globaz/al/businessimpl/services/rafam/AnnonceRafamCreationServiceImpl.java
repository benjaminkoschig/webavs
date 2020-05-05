package ch.globaz.al.businessimpl.services.rafam;

import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.AllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.BeneficiaryType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildAllowanceType;
import ch.eahv_iv.xmlns.eahv_iv_fao_empl._0.ChildType;
import ch.globaz.al.business.constantes.*;
import ch.globaz.al.business.constantes.enumerations.*;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.droit.DroitBusinessService;
import ch.globaz.al.business.services.rafam.AnnonceRafamCreationService;
import ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService;
import ch.globaz.al.businessimpl.checker.model.prestation.EntetePrestationModelChecker;
import ch.globaz.al.businessimpl.checker.model.rafam.AnnonceRafamModelChecker;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafamDelegue;
import ch.globaz.al.businessimpl.rafam.handlers.AnnonceHandlerAbstract;
import ch.globaz.al.businessimpl.rafam.handlers.AnnonceHandlerFactory;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALRafamUtils;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;

/**
 * Implémentation du service de création d'annonces RAFAM
 * 
 * @author jts
 * 
 */
public class AnnonceRafamCreationServiceImpl extends ALAbstractBusinessServiceImpl implements
        AnnonceRafamCreationService {

    public static final String IMPOSSIBLE_DE_TRAITER_L_ANNONCE_N = "Impossible de traiter l'annonce n°";

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#create68cForAnnonce(ch.globaz.al.business.models
     * .rafam.AnnonceRafamModel)
     */
    @Override
    public AnnonceRafamModel create68cForAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException {

        if ((annonce == null) || annonce.isNew()) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#create68cForAnnonce : dossier is null or new");
        }

        // si l'annonce n'est pas l'annonce d'origine, on recharge. Permet d'éviter des erreurs si le NSS à changé
        if (!annonce.getRecordNumber().equals(annonce.getIdAnnonce())) {
            annonce = ALServiceLocator.getAnnonceRafamModelService().read(annonce.getRecordNumber());
        }

        if (RafamEtatAnnonce.A_TRANSMETTRE.equals(RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getEtat()))) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(), "al.rafam.annulation.annonce.a_transmettre");
            return null;
        } else {

            ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSentForRecordNumber(
                    annonce.getRecordNumber());

            return ALServiceLocator.getAnnonceRafamModelService().create(
                    ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68c(annonce), annonce);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#creerAnnonceModificationsTiers(java.lang.String)
     */
    @Override
    public void creerAnnonceModificationsTiers(String idTiers) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeNumericUtil.isEmptyOrZero(idTiers)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#creerAnnonceModificationsTiers : idTiers is empty or zero");
        }

        /*
         * Gestion des cas "Enfants"
         */
        DroitComplexSearchModel searchDroits = new DroitComplexSearchModel();
        searchDroits.setForIdTiersEnfant(idTiers);
        searchDroits.setWhereKey("droitsForIdTiers");
        searchDroits.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchDroits = (DroitComplexSearchModel) JadePersistenceManager.search(searchDroits);

        for (int i = 0; i < searchDroits.getSize(); i++) {
            this.creerAnnoncesSelonPrecedent(RafamEvDeclencheur.MODIF_ENFANT, null, (DroitComplexModel) searchDroits.getSearchResults()[i]);
        }

        /*
         * Gestion des cas "Allocataires"
         */
        DossierComplexSearchModel searchDossiers = new DossierComplexSearchModel();
        searchDossiers.setForIdTiersAllocataire(idTiers);
        searchDossiers.setWhereKey("dossiersForIdTiers");
        searchDossiers.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchDossiers = (DossierComplexSearchModel) JadePersistenceManager.search(searchDossiers);

        for (int i = 0; i < searchDossiers.getSize(); i++) {
            this.creerAnnonces(RafamEvDeclencheur.MODIF_ALLOCATAIRE,
                    (DossierComplexModel) searchDossiers.getSearchResults()[i]);
        }
    }

    @Override
    public void creerAnnonceModificationAffilie(String numeroAffilie) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isBlank(numeroAffilie)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamBusinessServiceImpl#creerAnnonceModificationAffilie : numeroAffilie is null or empty");
        }

        DossierComplexSearchModel searchDossiers = new DossierComplexSearchModel();
        searchDossiers.setForNumeroAffilie(numeroAffilie);
        searchDossiers.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchDossiers = (DossierComplexSearchModel) JadePersistenceManager.search(searchDossiers);

        for (int i = 0; i < searchDossiers.getSize(); i++) {
            this.creerAnnonces(RafamEvDeclencheur.MODIF_DOSSIER,
                    (DossierComplexModel) searchDossiers.getSearchResults()[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#creerAnnonces(ch.globaz.al.business.constantes
     * .enumerations.RafamEvDeclencheur, ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public void creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {

        if (evDecl == null) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : evDecl is null");
        }

        if ((dossier == null) || dossier.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : dossier is null or new");
        }

        DroitComplexSearchModel search = new DroitComplexSearchModel();
        search.setForIdDossier(dossier.getId());

        List<String> types = new ArrayList<String>();
        types.add(ALCSDroit.TYPE_ENF);
        types.add(ALCSDroit.TYPE_FORM);
        search.setInTypeDroit(types);

        search = ALServiceLocator.getDroitComplexModelService().search(search);

        for (int i = 0; i < search.getSize(); i++) {
            this.creerAnnoncesSelonPrecedent(evDecl, dossier, (DroitComplexModel) search.getSearchResults()[i]);
        }
    }

    public void creerAnnonces(RafamEvDeclencheur evDecl, DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {
        this.creerAnnonces(evDecl, RafamEtatAnnonce.A_TRANSMETTRE, dossier, droit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#creerAnnonces(ch.globaz.al.business.constantes
     * .enumerations.RafamEvDeclencheur, ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public void creerAnnonces(RafamEvDeclencheur evDecl, DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {

        if (evDecl == null) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : evDecl is null");
        }

        if ((droit == null) || droit.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : droit is null or new");
        }

        this.creerAnnonces(evDecl,
                ALServiceLocator.getDossierComplexModelService().read(droit.getDroitModel().getIdDossier()), droit);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#creerAnnonces(ch.globaz.al.business.constantes
     * .enumerations.RafamEvDeclencheur, ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public void creerAnnoncesSelonPrecedent(RafamEvDeclencheur evDecl, DossierComplexModel dossier, DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {

        if(dossier == null) {
            dossier = ALServiceLocator.getDossierComplexModelService().read(droit.getDroitModel().getIdDossier());
        }

        if(ALCSDossier.STATUT_IS.equals(dossier.getDossierModel().getStatut())){
            return;
        }

        genererAnnonceSelonPrecedant(evDecl, dossier, droit);

    }


    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#creerAnnonces(ch.globaz.al.business.constantes
     * .enumerations.RafamEvDeclencheur, ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public void creerAnnoncesNaissanceOnly(RafamEvDeclencheur evDecl, DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {

        if (evDecl == null) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : evDecl is null");
        }

        if ((droit == null) || droit.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : droit is null or new");
        }

        this.creerAnnonces(evDecl, RafamEtatAnnonce.A_TRANSMETTRE, ALServiceLocator.getDossierComplexModelService().read(droit.getDroitModel().getIdDossier()), droit, false, true, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#creerAnnonces(ch.globaz.al.business.constantes
     * .enumerations.RafamEvDeclencheur, ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public void creerAnnoncesNotNaissance(RafamEvDeclencheur evDecl, DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {

        if (evDecl == null) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : evDecl is null");
        }

        if ((droit == null) || droit.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : droit is null or new");
        }

        this.creerAnnonces(evDecl, RafamEtatAnnonce.A_TRANSMETTRE, ALServiceLocator.getDossierComplexModelService().read(droit.getDroitModel().getIdDossier()), droit, false, false, true);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#creerAnnonces(ch.globaz.al.business.constantes
     * .enumerations.RafamEvDeclencheur, ch.globaz.al.business.models.dossier.DossierComplexModel,
     * ch.globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public void creerAnnonces(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DossierComplexModel dossier,
                              DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException {
        creerAnnonces(evDecl, etat, dossier, droit, true, false, false);
    }

    @Override
    public void creerAnnoncesWithoutDelete(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DossierComplexModel dossier,
                                           DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException {
        creerAnnonces(evDecl, etat, dossier, droit, false, false, false);
    }

    private void creerAnnonces(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DossierComplexModel dossier,
                               DroitComplexModel droit, boolean deletePrevious, boolean primeNaisanceOnly, boolean montantOnly) throws JadeApplicationException, JadePersistenceException {

        if (evDecl == null) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : evDecl is null");
        }

        if ((dossier == null) || dossier.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : dossier is null or new");
        }

        if ((droit == null) || droit.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : droit is null or new");
        }

        if (isAnnoncesRequired(dossier, droit)) {

            if(deletePrevious) {
                if(ALCSDossier.STATUT_IS.equals(dossier.getDossierModel().getStatut())) {
                    String dateDebut = droit.getDroitModel().getDebutDroit();
                    if(!JadeStringUtil.isEmpty(dateDebut) && dateDebut.length() > 6) {
                        String annee = dateDebut.substring(6);
                        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteForEtatYear(droit.getId(), etat, annee);
                    } else {
                        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteForEtat(droit.getId(), etat);
                    }
                } else {
                    ALImplServiceLocator.getAnnonceRafamBusinessService().deleteForEtat(droit.getId(), etat);
                }
            }

            List<RafamFamilyAllowanceType> types;
            if(RafamEvDeclencheur.ANNULATION.equals(evDecl)) {
                types = getTypesAllocationForAnnulation(dossier.getDossierModel(), droit);
            } else {
                types = getTypesAllocation(dossier.getDossierModel(), droit);
            }


            for (RafamFamilyAllowanceType type : types) {
                // S190502_010 : traiter uniquement les primes naissances
                if((primeNaisanceOnly && !isPrimeNaissance(type))
                    || (montantOnly && isPrimeNaissance(type))) {
                    continue;
                }

                // S190502_010 : ne pas annuler les primes naissances si déjà versées lors de l'annulation d'un montant mensuel à 0
                if(RafamEvDeclencheur.ANNULATION.equals(evDecl)
                    && isPrimeNaissance(type)
                    && !ALCSDroit.NAISSANCE_TYPE_AUCUNE.equals(droit.getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance())
                    && droit.getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee()) {
                    continue;
                }
                if(isPrimeNaissance(type) &&
                        droit.getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee()){
                    continue;

                }
                if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                    ContextAnnonceRafam context = ContextAnnonceRafam.getContext(evDecl, etat, dossier, droit, type);
                    AnnonceHandlerAbstract handler = (AnnonceHandlerFactory.getHandler(context));
                    handler.creerAnnonce();
                }
            }
        } else {
            if(deletePrevious) {
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSent(droit.getId());
            }
            ALImplServiceLocator.getAnnonceRafamBusinessService().deleteRefuseesForDroit(droit.getId());
        }
    }

    private boolean isPrimeNaissance(RafamFamilyAllowanceType type) {
        return RafamFamilyAllowanceType.ADOPTION.equals(type)
                || RafamFamilyAllowanceType.NAISSANCE.equals(type)
                || RafamFamilyAllowanceType.DIFFERENCE_ADOPTION.equals(type)
                || RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE.equals(type);
    }

    @Override
    public void creerAnnonces(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {

        if (evDecl == null) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : evDecl is null");
        }

        if ((droit == null) || droit.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#creerAnnonces : droit is null or new");
        }

        this.creerAnnonces(evDecl, etat,
                ALServiceLocator.getDossierComplexModelService().read(droit.getDroitModel().getIdDossier()), droit);
    }

    @Override
    public void creerAnnoncesDelegue(String idEmployeur, ChildAllowanceType currentAnnonces) throws Exception {

        StringBuffer errors = new StringBuffer();

        for (BeneficiaryType beneficiary : currentAnnonces.getBeneficiary()) {
            for (ChildType child : beneficiary.getChild()) {
                for (AllowanceType allowance : child.getAllowance()) {
                    try {

                        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSentForRecordNumber(
                                idEmployeur.concat(allowance.getAllowanceRefNumber().substring(2)));

                        if ("01".equals(allowance.getAllowanceCompleteStorno())) {
                            (AnnonceHandlerFactory.getHandler(ContextAnnonceRafamDelegue.getContext(
                                    RafamEvDeclencheur.ANNULATION, RafamEtatAnnonce.ENREGISTRE, idEmployeur,
                                    currentAnnonces, beneficiary, child, allowance))).creerAnnonce();
                        } else {
                            (AnnonceHandlerFactory.getHandler(ContextAnnonceRafamDelegue.getContext(
                                    RafamEvDeclencheur.MODIF_DROIT, RafamEtatAnnonce.ENREGISTRE, idEmployeur,
                                    currentAnnonces, beneficiary, child, allowance))).creerAnnonce();
                        }

                        if (JadeThread.logMessages() != null) {
                            errors.append(
                                    IMPOSSIBLE_DE_TRAITER_L_ANNONCE_N + allowance.getAllowanceRefNumber()
                                            + ",erreur:+" + JadeThread.logMessages()[0].getMessageId()).append(")\n");
                            JadeLogger.error(this, allowance.getAllowanceRefNumber() + " non transmis à la CAF");
                            JadeThread.rollbackSession();
                            JadeThread.logClear();
                        } else {
                            JadeThread.commitSession();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        JadeThread.rollbackSession();

                        JadeLogger.error(this, IMPOSSIBLE_DE_TRAITER_L_ANNONCE_N + allowance.getAllowanceRefNumber()
                                + ":" + e.getMessage());

                        errors.append(IMPOSSIBLE_DE_TRAITER_L_ANNONCE_N + allowance.getAllowanceRefNumber())
                                .append(e.getMessage()).append(")\n");

                        JadeThread.logClear();

                    }

                }

            }
        }
        if (errors.length() > 0) {

            ALRafamUtils.sendMail(new StringBuffer("Erreurs lors du chargement des annonces de l'employeur n° "
                    + idEmployeur), errors, new String[0]);

        }

    }

    @Override
    public void creerAnnoncesADI(List<AdiEnfantMoisComplexModel> listAdi)
            throws JadeApplicationException, JadePersistenceException {
        DroitComplexModel droit = listAdi.get(0).getDroitComplexModel();
        TreeMap<Periode, Boolean> periodes = createPeriodes(listAdi);
        if(EntetePrestationModelChecker.checkAnnnonce(periodes.firstEntry().getKey().getDateDebut().substring(3,7), droit.getId())) {
            JadeThread.logWarn(EntetePrestationModelChecker.class.getName(),
                    "al.protocoles.paiementDirect.annonce.envoyee.compta.error");
            throw new ALAnnonceRafamException("AnnonceRafamCreationServiceImpl#creerAnnoncesADI : Annonces RAFam liées au décompte ADI en état « Transmise »");
        } else {
            createRafamFromPeriode(periodes, droit);
        }
    }

    private TreeMap<Periode, Boolean> createPeriodes(List<AdiEnfantMoisComplexModel> listAdi) {

        Collections.sort(listAdi, new Comparator<AdiEnfantMoisComplexModel>() {
            @Override
            public int compare(AdiEnfantMoisComplexModel a1, AdiEnfantMoisComplexModel a2) {
                Date a2Mois = JadeDateUtil.getGlobazDate(JadeDateUtil.getFirstDateOfMonth(a2.getAdiEnfantMoisModel().getMoisPeriode()));
                Date a1Mois = JadeDateUtil.getGlobazDate(JadeDateUtil.getFirstDateOfMonth(a1.getAdiEnfantMoisModel().getMoisPeriode()));
                return a1Mois.compareTo(a2Mois);
            }
        });
        TreeMap<Periode, Boolean> periodes = new TreeMap<>();

        AdiEnfantMoisModel firstAdi = listAdi.get(0).getAdiEnfantMoisModel();
        listAdi.remove(listAdi.get(0));

        String startPeriode = firstAdi.getMoisPeriode();
        String lastMonthPeriode = startPeriode;
        boolean isZeroPeriode = JadeStringUtil.isBlankOrZero(firstAdi.getMontantAdi());

        for(AdiEnfantMoisComplexModel adiComplexe: listAdi) {
            AdiEnfantMoisModel adi =  adiComplexe.getAdiEnfantMoisModel();
            boolean isMontantZero = JadeStringUtil.isBlankOrZero(adi.getMontantAdi());
            if((isMontantZero && !isZeroPeriode)
                    || (!isMontantZero && isZeroPeriode)) {
                // nouvelle période à créer
                periodes.put(new Periode(startPeriode, lastMonthPeriode), isZeroPeriode);
                isZeroPeriode = !isZeroPeriode;
                startPeriode = adi.getMoisPeriode();
            }
            lastMonthPeriode = adi.getMoisPeriode();
        }

        // dernière période
        periodes.put(new Periode(startPeriode, lastMonthPeriode), isZeroPeriode);

        return periodes;
    }

    private void createRafamFromPeriode(TreeMap<Periode, Boolean> periodes, DroitComplexModel droitComplexModel)
            throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamSearchModel annonceSearch = ALImplServiceLocator.getAnnoncesRafamSearchService().loadAnnoncesForDroit(droitComplexModel.getId());
        List<AnnonceRafamModel> annonces = new ArrayList<>();
        for(JadeAbstractModel abstractModel : annonceSearch.getSearchResults()) {
            annonces.add((AnnonceRafamModel) abstractModel);
        }

        annonces = getLastAnnonceFromRecordNumber(annonces);

        for (Map.Entry<Periode, Boolean> entry : periodes.entrySet()) {
            annonces = genererAnnonceSelonAnnoncePrecedante(droitComplexModel, annonces, entry.getKey(), entry.getValue());
        }
    }



    private List<AnnonceRafamModel> getLastAnnonceFromRecordNumber(List<AnnonceRafamModel> annonces) throws JadeApplicationException {
        Map<String, AnnonceRafamModel> mapAnnonces = new HashMap<>();
        for(AnnonceRafamModel annonce : annonces){
            if(is68(annonce)
                && !isPrimeNaissance(RafamFamilyAllowanceType.getFamilyAllowanceType(annonce.getGenrePrestation()))
                && (mapAnnonces.get(annonce.getRecordNumber()) == null
                || Integer.valueOf(mapAnnonces.get(annonce.getRecordNumber()).getId()) < Integer.valueOf(annonce.getId()))) {
                mapAnnonces.put(annonce.getRecordNumber(), annonce);
            }
        }
        return new ArrayList(mapAnnonces.values());
    }

    private boolean is68(AnnonceRafamModel annonce) {
        return RafamTypeAnnonce._68A_CREATION.getCode().equals(annonce.getTypeAnnonce())
                || RafamTypeAnnonce._68B_MUTATION.getCode().equals(annonce.getTypeAnnonce())
                || RafamTypeAnnonce._68C_ANNULATION.getCode().equals(annonce.getTypeAnnonce());
    }

    private List<AnnonceRafamModel> genererAnnonceSelonAnnoncePrecedante(
            DroitComplexModel droit,
            List<AnnonceRafamModel> annonces,
            Periode periode,
            boolean isZero ) throws JadeApplicationException, JadePersistenceException {

        Date debutPeriode = JadeDateUtil.getGlobazDate(JadeDateUtil.getFirstDateOfMonth(periode.getDateDebut()));
        Date finPeriode = JadeDateUtil.getGlobazDate(JadeDateUtil.getLastDateOfMonth(periode.getDateFin()));
        String debutDroitBackup = droit.getDroitModel().getDebutDroit();
        String finDroitBackup = droit.getDroitModel().getFinDroitForcee();
        Date debutDroit = JadeDateUtil.getGlobazDate(debutDroitBackup);
        Date finDroit = JadeDateUtil.getGlobazDate(finDroitBackup);
        Date debDate = debutPeriode;
        Date finDate = finPeriode;

        if(debutDroit.after(debutPeriode)) {
            debDate = debutDroit;
        } else {
            droit.getDroitModel().setDebutDroit(JadeDateUtil.getGlobazFormattedDate(debutPeriode));
        }
        if(finDroit.before(finPeriode)) {
            finDate = finDroit;
        } else {
            droit.getDroitModel().setFinDroitForcee(JadeDateUtil.getGlobazFormattedDate(finPeriode));
        }

        List<AnnonceRafamModel> annonceDejaAnnulee = new ArrayList<>();

        for(AnnonceRafamModel annonce : annonces){
            if(!RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(annonce.getTypeAnnonce()))
                && isEtatActive(RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getEtat()))
                    && !debDate.after(JadeDateUtil.getGlobazDate(annonce.getEcheanceDroit()))
                    && !finDate.before(JadeDateUtil.getGlobazDate(annonce.getDebutDroit()))) {
                ALServiceLocator.getAnnonceRafamCreationService().create68cForAnnonce(annonce);
                annonceDejaAnnulee.add(annonce);
            }
        }

        annonces.removeAll(annonceDejaAnnulee);

        if (!isZero) {
            DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(droit.getDroitModel().getIdDossier());
            ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.CREATION, RafamEtatAnnonce.A_TRANSMETTRE, dossier, droit);
        }

        // restauration des bonnes dates pour les prochains traitements
        droit.getDroitModel().setDebutDroit(debutDroitBackup);
        droit.getDroitModel().setFinDroitForcee(finDroitBackup);

        return annonces;
    }

    private boolean isEtatActive(RafamEtatAnnonce etat) {
        return RafamEtatAnnonce.RECU.equals(etat)
                || RafamEtatAnnonce.SUSPENDU.equals(etat)
                || RafamEtatAnnonce.TRANSMIS.equals(etat)
                || RafamEtatAnnonce.VALIDE.equals(etat)
                || RafamEtatAnnonce.ARCHIVE.equals(etat);
    }

    /**
     * Recherche les types d'annonces qu'il est possible d'avoir en fonction d'un dossier et d'un droit
     * 
     * @return Liste des types d'annonces identifiés
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    public List<RafamFamilyAllowanceType> getTypesAllocation(DossierModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {
        return getTypesAllocation(dossier, droit, false);
    }

    public List<RafamFamilyAllowanceType> getTypesAllocationForAnnulation(DossierModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {
        return getTypesAllocation(dossier, droit, true);
    }

    private List<RafamFamilyAllowanceType> getTypesAllocation(DossierModel dossier, DroitComplexModel droit, boolean forAnnulation)
            throws JadeApplicationException, JadePersistenceException {

        AnnoncesRafamSearchService annonceSearchService = ALImplServiceLocator.getAnnoncesRafamSearchService();

        if ((dossier == null) || dossier.isNew()) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamIdentificationServiceImpl#getTypesAllocation : dossier is null or is new");
        }

        if ((droit == null) || droit.isNew()) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamIdentificationServiceImpl#getTypesAllocation : droit is null or is new");
        }

        DroitBusinessService dbs = ALServiceLocator.getDroitBusinessService();
        List<RafamFamilyAllowanceType> list = new ArrayList<>();

        if (!dbs.isMontantForceZero(droit.getDroitModel()) || forAnnulation) {

            // ADC (code 30)
            if (ALCSDossier.STATUT_CS.equals(dossier.getStatut())
                    || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.ADC)) {
                list.add(RafamFamilyAllowanceType.ADC);
            }

            // ADI (code 31)
            if (ALCSDossier.STATUT_IS.equals(dossier.getStatut())
                    || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.ADI)) {
                list.add(RafamFamilyAllowanceType.ADI);
            }
        }

        // Différentielle naissance (code 03)
        if ((ALCSDossier.STATUT_CS.equals(dossier.getStatut())
                && ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit()) && ALCSDroit.NAISSANCE_TYPE_NAIS
                    .equals(droit.getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance()))
                || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE)) {

            list.add(RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE);
        }

        // Différentielle accueil (code 04)
        if ((ALCSDossier.STATUT_CS.equals(dossier.getStatut())
                && ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit()) && ALCSDroit.NAISSANCE_TYPE_ACCE
                    .equals(droit.getEnfantComplexModel().getEnfantModel().getTypeAllocationNaissance()))
                || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.DIFFERENCE_ADOPTION)) {

            list.add(RafamFamilyAllowanceType.DIFFERENCE_ADOPTION);
        }

        // ///////////////////////////////////////////////////////////////////////////////////////////////////
        // Cas non Différentiel
        // ///////////////////////////////////////////////////////////////////////////////////////////////////
        if ((!ALCSDossier.STATUT_CS.equals(dossier.getStatut()) && !ALCSDossier.STATUT_IS.equals(dossier.getStatut()))) {

            // Naissance (code 01)
            if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                    && ALCSDroit.NAISSANCE_TYPE_NAIS.equals(droit.getEnfantComplexModel().getEnfantModel()
                            .getTypeAllocationNaissance()) && !ALImplServiceLocator.getDossierBusinessService()
                    .isAgricole(dossier.getActiviteAllocataire()))
                    || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.NAISSANCE)) {

                list.add(RafamFamilyAllowanceType.NAISSANCE);
            }

            // accueil (code 02)
            if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                    && ALCSDroit.NAISSANCE_TYPE_ACCE.equals(droit.getEnfantComplexModel().getEnfantModel()
                            .getTypeAllocationNaissance()) && !ALImplServiceLocator.getDossierBusinessService()
                    .isAgricole(dossier.getActiviteAllocataire()))
                    || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.ADOPTION)) {

                list.add(RafamFamilyAllowanceType.ADOPTION);
            }

            if (!dbs.isMontantForceZero(droit.getDroitModel()) || forAnnulation) {

                // Enfant (code 10)
                if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                        && droit.getEnfantComplexModel().getEnfantModel().getCapableExercer() && !droit.getDroitModel()
                        .getSupplementFnb())
                        || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.ENFANT)) {

                    list.add(RafamFamilyAllowanceType.ENFANT);
                }

                // Enfant incapable d'exercer (code 12)
                if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                        && !droit.getEnfantComplexModel().getEnfantModel().getCapableExercer() && !droit
                        .getDroitModel().getSupplementFnb())
                        || annonceSearchService.hasAnnonceOfType(droit.getId(),
                                RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE)) {

                    list.add(RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE);
                    list.add(RafamFamilyAllowanceType.ENFANT);
                }

                // Formation et formation anticipée (codes 20, 22)
                if ((ALCSDroit.TYPE_FORM.equals(droit.getDroitModel().getTypeDroit()) && !droit.getDroitModel()
                        .getSupplementFnb())
                        || annonceSearchService.hasAnnonceOfType(droit.getId(), RafamFamilyAllowanceType.FORMATION)
                        || annonceSearchService.hasAnnonceOfType(droit.getId(),
                                RafamFamilyAllowanceType.FORMATION_ANTICIPEE)) {

                    list.add(RafamFamilyAllowanceType.FORMATION);
                    list.add(RafamFamilyAllowanceType.FORMATION_ANTICIPEE);
                }

                // Enfant (code 11) (FNB)
                if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                        && droit.getEnfantComplexModel().getEnfantModel().getCapableExercer() && droit.getDroitModel()
                        .getSupplementFnb())
                        || annonceSearchService.hasAnnonceOfType(droit.getId(),
                                RafamFamilyAllowanceType.ENFANT_AVEC_SUPPLEMENT)) {

                    list.add(RafamFamilyAllowanceType.ENFANT_AVEC_SUPPLEMENT);
                }

                // Enfant incapable d'exercer (FNB) (code 13)
                if ((ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit())
                        && !droit.getEnfantComplexModel().getEnfantModel().getCapableExercer() && droit.getDroitModel()
                        .getSupplementFnb())
                        || annonceSearchService.hasAnnonceOfType(droit.getId(),
                                RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE_AVEC_SUPPLEMENT)) {

                    list.add(RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE_AVEC_SUPPLEMENT);
                }

                // Formation et formation anticipée (FNB) (codes 21, 23)
                if ((ALCSDroit.TYPE_FORM.equals(droit.getDroitModel().getTypeDroit()) && droit.getDroitModel()
                        .getSupplementFnb())
                        || annonceSearchService.hasAnnonceOfType(droit.getId(),
                                RafamFamilyAllowanceType.FORMATION_AVEC_SUPPLEMENT)
                        || annonceSearchService.hasAnnonceOfType(droit.getId(),
                                RafamFamilyAllowanceType.FORMATION_ANTICIPEE_AVEC_SUPPLEMENT)) {

                    list.add(RafamFamilyAllowanceType.FORMATION_AVEC_SUPPLEMENT);
                    list.add(RafamFamilyAllowanceType.FORMATION_ANTICIPEE_AVEC_SUPPLEMENT);
                }
            }
        }

        return list;
    }

    /**
     * Indique si une annonce doit être générée en fonction de l'état (général et pas la valeur du champ état) du
     * dossier et du droit
     * 
     * @param dossier
     * @param droit
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private boolean isAnnoncesRequired(DossierComplexModel dossier, DroitComplexModel droit)
            throws JadeApplicationException, JadePersistenceException {

        // Le traitement est effectué si :
        // - le droit a l'état actif
        // - le dossier à un état qui engendre une annonce Rafam
        // - le droit débute après le 31.12.1979
        // - le droit est de type enfant ou formation
        // - le droit se termine après le 31.12.2010

        HashSet<String> collectionEtatOkRafam = (HashSet<String>) ALServiceLocator.getDossierBusinessService()
                .listerEtatsOkRafam();

        if ((ALCSDroit.ETAT_A.equals(droit.getDroitModel().getEtatDroit()) && collectionEtatOkRafam.contains(dossier
                .getDossierModel().getEtatDossier()))
                && (ALCSDroit.TYPE_ENF.equals(droit.getDroitModel().getTypeDroit()) || ALCSDroit.TYPE_FORM.equals(droit
                        .getDroitModel().getTypeDroit()))
                && !JadeDateUtil.isDateBefore(droit.getDroitModel().getDebutDroit(), ALConstRafam.DATE_DEBUT_MINIMUM)
                && JadeDateUtil.isDateBefore(ALConstRafam.DATE_FIN_MINIMUM, droit.getDroitModel().getFinDroitForcee())) {

            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#suspendreAnnonce(ch.globaz.al.business.models
     * .rafam.AnnonceRafamModel)
     */
    @Override
    public AnnonceRafamModel suspendreAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException {
        if ((annonce == null) || annonce.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#validerAnnonce : annonce is null or new");
        }

        return ALImplServiceLocator.getAnnonceRafamBusinessService().setEtat(annonce, RafamEtatAnnonce.SUSPENDU);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#transmettreAnnoncesTemporaires(java.lang.String)
     */
    @Override
    public void transmettreAnnoncesTemporaires(String idDroit) throws JadePersistenceException,
            JadeApplicationException {

        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteForEtat(idDroit, RafamEtatAnnonce.A_TRANSMETTRE);

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        ArrayList<String> etats = new ArrayList<>();
        etats.add(RafamEtatAnnonce.ENREGISTRE.getCS());
        search.setInEtatAnnonce(etats);
        search.setForIdDroit(idDroit);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        for (int i = 0; i < search.getSize(); i++) {
            AnnonceRafamModel currentAnnonce = (AnnonceRafamModel) (search.getSearchResults()[i]);
            ALImplServiceLocator.getAnnonceRafamBusinessService().setEtat(currentAnnonce,
                    RafamEtatAnnonce.A_TRANSMETTRE);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.rafam.AnnonceRafamCreationService#validerAnnonce(ch.globaz.al.business.models.
     * rafam.AnnonceRafamModel)
     */
    @Override
    public AnnonceRafamModel validerAnnonce(AnnonceRafamModel annonce) throws JadeApplicationException,
            JadePersistenceException {

        if ((annonce == null) || annonce.isNew()) {
            throw new ALAnnonceRafamException("AnnonceRafamBusinessServiceImpl#validerAnnonce : annonce is null or new");
        }

        return ALImplServiceLocator.getAnnonceRafamBusinessService().setEtat(annonce, RafamEtatAnnonce.VALIDE);
    }


    private List<String> getMontantCalcule(DossierComplexModel dossierComplexModel, DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException {

        List<String> montants = new ArrayList<>();
        String dateDebut =  droit.getDroitModel().getDebutDroit();
        String dateFin =  droit.getDroitModel().getFinDroitForcee();
        Date dateDuJour = new Date();
        String dateCalcul = JadeDateUtil.getGlobazFormattedDate(dateDuJour);
        if(!JadeStringUtil.isEmpty(dateDebut)
            && JadeDateUtil.getGlobazDate(dateDebut).after(dateDuJour)) {
            dateCalcul = dateDebut;
        } else if(!JadeStringUtil.isEmpty(dateFin)
            && JadeDateUtil.getGlobazDate(dateFin).before(dateDuJour)) {
            dateCalcul = dateFin;
        }
        List<CalculBusinessModel> droitsList = new ArrayList<>();
        List<CalculBusinessModel> droitsNaissance = new ArrayList<>();

        // Chargement des droits calculés
        try {
            List<CalculBusinessModel> droitsListTemp = ALServiceLocator.getCalculBusinessService().getCalcul(dossierComplexModel, dateCalcul);
            for(CalculBusinessModel calcul : droitsListTemp) {
                if(calcul.getDroit() != null && calcul.getDroit().getId().equals(droit.getId())
                        && !ALCSTarif.CATEGORIE_SUP_HORLO.equals(calcul.getTarif())) {
                    droitsList.add(calcul);
                    if(ALCSDroit.TYPE_NAIS.equals(calcul.getType())
                            || ALCSDroit.TYPE_ACCE.equals(calcul.getType())) {
                        droitsNaissance.add(calcul);
                    }
                }
            }
        } catch (JadeApplicationException e) {
            boolean calculError = true;
        }

        String montantTotal = "0";
        String montantNaissance = "0";

        // Montant total calculé
        if (droitsList.size() > 0) {
            montantTotal = (ALServiceLocator.getCalculBusinessService().getTotal(dossierComplexModel,
                    droitsList, dossierComplexModel.getDossierModel().getUniteCalcul(), "1", false, dateCalcul)).get(
                    ALConstCalcul.TOTAL_EFFECTIF).toString();
        }

        if(droitsNaissance.size() > 0) {
            montantNaissance = (ALServiceLocator.getCalculBusinessService().getTotal(dossierComplexModel,
                    droitsNaissance, dossierComplexModel.getDossierModel().getUniteCalcul(), "1", true, dateCalcul)).get(
                    ALConstCalcul.TOTAL_EFFECTIF).toString();
        }

        montants.add(montantTotal);
        montants.add(montantNaissance);

        return montants;
    }

    private void genererAnnonceSelonPrecedant(RafamEvDeclencheur evDecl,
                                              DossierComplexModel dossier,
                                              DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException {

        List<String> montants = getMontantCalcule(dossier, droit);

        List<RafamFamilyAllowanceType> types = ALServiceLocator.getAnnonceRafamCreationService().getTypesAllocationForAnnulation(dossier.getDossierModel(), droit);

        if(!types.isEmpty()) {
            AnnonceRafamModel lastAnnonce = ALImplServiceLocator.getAnnoncesRafamSearchService().getLastActive(droit.getId(), types.get(0));

            Montant montAfter = new Montant(montants.get(0));
            Montant montNaissaceAfter = new Montant(montants.get(1));

            if (montAfter.isZero()) {
                if(lastAnnonce.getTypeAnnonce()!= null) {
                    if (RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(lastAnnonce.getTypeAnnonce()))) {
                        ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSent(droit.getId());
                    } else { //RafamTypeAction.CREATION + RafamTypeAction.MODIFICATION
                        ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.ANNULATION, droit);
                    }
                }
                // Test si annonce naissance
                if (hasAllocationNaissance(droit) && !montNaissaceAfter.isZero()) {
                    ALServiceLocator.getAnnonceRafamCreationService().creerAnnoncesNaissanceOnly(RafamEvDeclencheur.CREATION, droit);
                }
            } else {
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSent(droit.getId());
                if (lastAnnonce.getTypeAnnonce()!= null && RafamTypeAnnonce._68C_ANNULATION.equals(RafamTypeAnnonce.getRafamTypeAnnonce(lastAnnonce.getTypeAnnonce()))) {
                    evDecl = RafamEvDeclencheur.CREATION;
                }
                if (hasAllocationNaissance(droit) && montNaissaceAfter.isZero()) {
                    ALServiceLocator.getAnnonceRafamCreationService().creerAnnoncesNotNaissance(evDecl, droit);
                } else {
                    ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(evDecl, droit);
                }
            }
        }
    }

    private boolean hasAllocationNaissance(DroitComplexModel droit) {
        return !droit.getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee();
    }

    public void supprimeAnnonceSiModificationStatut(DossierComplexModel dossier) throws JadeApplicationException, JadePersistenceException {
        if(!ALCSDossier.STATUT_IS.equals(dossier.getDossierModel().getStatut())) {
            return;
        }
        DossierComplexModel oldDossier = ALServiceLocator.getDossierComplexModelService().read(dossier.getId());

        if(!ALCSDossier.STATUT_IS.equals(oldDossier.getDossierModel().getStatut())) {
            DroitComplexSearchModel search = new DroitComplexSearchModel();
            search.setForIdDossier(dossier.getId());

            search = ALServiceLocator.getDroitComplexModelService().search(search);

            for (int i = 0; i < search.getSize(); i++) {
                ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSent(((DroitComplexModel) search.getSearchResults()[i]).getId());
            }
        }
    }
}
