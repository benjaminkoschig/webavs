package globaz.apg.acorweb.service;

import acor.apg.xsd.apg.out.*;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Dates;
import ch.globaz.eavs.utils.StringUtils;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acorweb.mapper.APPrestationAcor;
import globaz.apg.acorweb.mapper.APRepartitionPaiementAcor;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.helpers.prestation.APPrestationHelper;
import globaz.apg.module.calcul.*;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.apg.vb.prestation.APPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.acor.PRAcorTechnicalException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCalcul;
import globaz.pyxis.util.CommonNSSFormater;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class APImportationCalculAcor {

    private final EntityService entityService;
    public APImportationCalculAcor() {
        entityService = EntityService.of(BSessionUtil.getSessionFromThreadContext());
    }

    public void importCalculAcor(String idDroit, FCalcul fCalcul) throws Exception {
        LOG.info("Importation des données calculées.");
        BSession session = entityService.getSession();
        APPrestationViewBean pViewBean = new APPrestationViewBean();
        pViewBean.setISession(session);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, session.getCurrentThreadTransaction(),
                    idDroit);
            checkAndGetNssIntegrite(fCalcul, droit);
            final List<APBaseCalcul> basesCalcul = retrieveBasesCalcul(session, droit);
            Collection<APPrestationWrapper> pw = createWrappers(createPrestationsAcor(session, fCalcul, droit, basesCalcul), droit, basesCalcul);
            updateWrappersTauxParticipation(session, fCalcul, pw);
            final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
            calculateur.reprendreDepuisACOR(session, pw, droit, retrieveFraisDeGarde(droit), basesCalcul);
            // Calcul des prestations standard, LAMat et ACM_ALFA
            pViewBean = calculateur.calculPrestationAMAT_ACM(session, transaction, droit);

            APPrestationHelper.calculerComplement(session, transaction, droit);


            // Calcul des prestations MATCIAB1 si besoin
            APPrestationHelper.calculerComplementMATCIAB1(session, transaction, droit);
            // Calcul des ACM NE si la propriété TYPE_DE_PRESTATION_ACM vaut ACM_NE
            APPrestationHelper.calculerPrestationsAcmNe(session, transaction, droit);

            // Calcul des prestations ACM 2 si besoin
            APPrestationHelper.calculerPrestationsAcm2(session, transaction, droit);

            // Suppression des prestations standards si l'utilisateur a coché "Exclure l'amat fédéral" dans la situation
            // professionnelle du droit
            calculateur.deletePrestationsStandardsWhenAmatIsExcluded(session, transaction, droit);

            if (!hasErrors(session, transaction)) {
                transaction.commit();
            }
        } catch (final Exception exception) {
            if (transaction != null) {
                transaction.rollback();
                // on s'assure que les prestations standards sont supprimées
                APPrestationHelper.supprimerPrestationsDuDroit(session, pViewBean.getIdDroit());
            }
            pViewBean.setMsgType(FWViewBeanInterface.ERROR);
            pViewBean.setMessage("Unexpected exception throw on commit/rollback of transaction during ACOR v4.0 importation  : " + exception);

        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    private List<APPrestationAcor> createPrestationsAcor(BSession session, FCalcul fCalcul, APDroitLAPG droit, List<APBaseCalcul> basesCalcul) throws Exception {
        List<APPrestationAcor> prestations = new ArrayList<>();
        for (VersementMoisComptableApgType versementMoisComptable:
             fCalcul.getVersementMoisComptable()) {
            Optional<PeriodeServiceApgType> periodeServiceApgTypeOptional = fCalcul.getCarteApg().getPeriodeService().stream().filter(p -> {
                try {
                    return checkPeriodesDansLeMemeMois(p.getDebut(), p.getFin(), versementMoisComptable.getMoisComptable());
                } catch (JAException e) {
                    throw new PRAcorTechnicalException(e);
                }
            }).findFirst();
            if(periodeServiceApgTypeOptional.isPresent()) {
                PeriodeServiceApgType periodeServiceApgType = periodeServiceApgTypeOptional.get();
                JADate dateDebutPeriode = JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getDebut()));
                JADate dateFinPeriode = JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getFin()));
                APBaseCalcul baseCalcul = findBaseCalcul(basesCalcul, dateDebutPeriode, dateFinPeriode);
                if(Objects.nonNull(baseCalcul)) {
                    prestations.add(mapPrestation(session, fCalcul, droit, versementMoisComptable, periodeServiceApgType, baseCalcul));
                }
            }
        }
        return prestations;
    }

    private APPrestationAcor mapPrestation(BSession session, FCalcul fCalcul, APDroitLAPG droit, VersementMoisComptableApgType versementMoisComptable, PeriodeServiceApgType periodeServiceApgType, APBaseCalcul baseCalcul) throws Exception {
        APPrestationAcor prestation = new APPrestationAcor();
        AssureApgType assure = fCalcul.getAssure().stream().filter(a -> a.getFonction() == FonctionApgType.REQUERANT).collect(Collectors.toList()).get(0);
        String genreService = session.getCode(droit.getGenreService());
        if (Objects.nonNull(fCalcul.getFraisGarde())) {
            prestation.setFraisGarde(new FWCurrency(fCalcul.getFraisGarde().getMontantGardeOctroye()));
        } else {
            retrieveFraisDeGarde(droit);
        }
        prestation.setMoisComptable(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(versementMoisComptable.getMoisComptable()))));
        prestation.setIdAssure(assure.getId());
        prestation.setGenre(genreService);
        mapInformationFromPeriodeServiceApg(session, genreService, periodeServiceApgType, prestation);
        mapInformationFromMontantJournalierApg(droit, fCalcul, prestation);

        mapInformationFromBaseCalcul(baseCalcul, prestation);
        List<APRepartitionPaiementAcor> repartitionPaiement = createRepartitionPaiementsDecomptes(session, baseCalcul, fCalcul, versementMoisComptable);
        for (APRepartitionPaiementAcor repartitionPaiementAcor :
                repartitionPaiement) {
            prestation.getRepartitionPaiements().add(repartitionPaiementAcor);
        }
        return prestation;
    }

    private void mapInformationFromBaseCalcul(APBaseCalcul baseCalcul, APPrestationAcor prestation) {
        if(Objects.nonNull(baseCalcul)) {
            prestation.setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
            prestation.setIdTauxImposition(baseCalcul.getIdTauxImposition());
            prestation.setTauxImposition(baseCalcul.getTauxImposition());
        }
    }

    private void mapInformationFromMontantJournalierApg(APDroitLAPG droit, FCalcul fCalcul, APPrestationAcor prestation) {
        PeriodeMontantJournApgType periodeMontantJournApgType = fCalcul.getPeriodeMontantJourn().get(0);
        if(Objects.nonNull(periodeMontantJournApgType)){
            prestation.setAllocationJournalier(new FWCurrency(periodeMontantJournApgType.getAllocJourn()));
            prestation.setAllocationExploitation(new FWCurrency(periodeMontantJournApgType.getAllocJournExploitation()));
            String nbJoursHospitalisation = droit.getJoursSupplementaires();
            if(StringUtils.isInteger(nbJoursHospitalisation)) {
                prestation.setNombreJoursSupplementaires(Integer.parseInt(nbJoursHospitalisation));
            }
            prestation.setRevenuDeterminantMoyen(new FWCurrency(periodeMontantJournApgType.getRjm()));
        }
    }

    private void mapInformationFromPeriodeServiceApg(BSession session, String genreService, PeriodeServiceApgType periodeServiceApgType, APPrestationAcor prestation) {

        if (Objects.nonNull(periodeServiceApgType.getPartAssure())) {
            prestation.setVersementAssure(new FWCurrency(periodeServiceApgType.getPartAssure()));
        }
        IAPReferenceDataPrestation ref = retrieveReferenceData(session, periodeServiceApgType, genreService);
        prestation.setRevision(ref.getNoRevision());
        prestation.setFraisGardeMax(ref.getMontantMaxFraisGarde());
        try {
            prestation.setDateDebut(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getDebut()))));
            prestation.setDateFin(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getFin()))));
        } catch (JAException e) {
            throw new PRAcorTechnicalException("Formattage des dates de période de service APG incorrect !!!", e);
        }
        prestation.setNombreJoursSoldes(periodeServiceApgType.getNbJours());

    }

    private APRepartitionPaiementAcor createRepartitionPaiement(BSession session, APBaseCalcul baseCalcul, VersementBeneficiaireApgType beneficiare, PeriodeDecompteApgType periodeDecompte, FCalcul fCalcul) throws Exception {
        Optional<EmployeurApgType> employeurOptional = fCalcul.getEmployeur().stream().filter(e -> e.getIdIntEmpl().equals(beneficiare.getIdBeneficiaire())).findFirst();
        if (employeurOptional.isPresent()) {
            EmployeurApgType employeur = employeurOptional.get();
            APRepartitionPaiementAcor repartitionPaiementAcor = new APRepartitionPaiementAcor(session, employeur.getNoAffilie(), employeur.getNom(), "");
            repartitionPaiementAcor.setMontantNet(new FWCurrency(periodeDecompte.getMontantPeriode()));
            repartitionPaiementAcor.setSalaireJournalier(new FWCurrency(periodeDecompte.getMontantJourn()));
            APBaseCalculSituationProfessionnel bcSitPro = null;
            try {
                bcSitPro = findBaseCalculSitPro(baseCalcul, repartitionPaiementAcor.getIdTiers(),
                        repartitionPaiementAcor.getIdAffilie(), employeur.getNom());
            } catch (PRACORException e) {

                try {
                    // Nouvelle tentative de recherche par #affilie pour les cas affiliés avec 2 affiliations sous
                    // le même #.
                    bcSitPro = findBaseCalculSitProParNoAffilie(baseCalcul, repartitionPaiementAcor.getIdTiers(),
                            repartitionPaiementAcor.getIdAffilie(), employeur.getNom());
                } catch (PRACORException e2) {
                    LOG.warn("La situation professionnelle de la base de caclul n'a pas été trouvé. \nLa période correspondante est peut être créé pour des jours d'hosipitalisation.");
                }
            }
            if (Objects.nonNull(bcSitPro)) {
                mapSituationProfessionnel(repartitionPaiementAcor, bcSitPro);
            }
            return repartitionPaiementAcor;
        }
        if(periodeDecompte.getTauxAllocJourn() != 0) {
            APBaseCalculSituationProfessionnel bcSitPro = findBaseCalculSitProVersementAssure(baseCalcul);
            if (Objects.isNull(bcSitPro)) {
                bcSitPro = findBaseCalculSitProIndependant(baseCalcul, beneficiare.getIdBeneficiaire());
            }
            if (Objects.nonNull(bcSitPro)) {
                APRepartitionPaiementAcor repartitionPaiementAcor = new APRepartitionPaiementAcor(beneficiare.getIdBeneficiaire());
                repartitionPaiementAcor.setMontantNet(new FWCurrency(periodeDecompte.getMontantPeriode()));
                repartitionPaiementAcor.setSalaireJournalier(new FWCurrency(periodeDecompte.getMontantJourn()));
                repartitionPaiementAcor.setVersementEmployeur(bcSitPro.isPaiementEmployeur());
                repartitionPaiementAcor.setNomEmployeur(bcSitPro.getNom());
                repartitionPaiementAcor.setNumeroAffilieEmployeur(bcSitPro.getNoAffilie());
                repartitionPaiementAcor.updateIdsEmployeur(session, bcSitPro.getNoAffilie(), bcSitPro.getNom());
                mapSituationProfessionnel(repartitionPaiementAcor, bcSitPro);
                return repartitionPaiementAcor;
            }

        }
        return null;
    }

    private void mapSituationProfessionnel(APRepartitionPaiementAcor repartitionPaiementAcor, APBaseCalculSituationProfessionnel bcSitPro) {
        repartitionPaiementAcor.setVersementEmployeur(bcSitPro.isPaiementEmployeur());
        repartitionPaiementAcor.setIndependant(bcSitPro.isIndependant());
        repartitionPaiementAcor.setTravailleurSansEmployeur(bcSitPro.isTravailleurSansEmployeur());
        repartitionPaiementAcor.setCollaborateurAgricole(bcSitPro.isCollaborateurAgricole());
        repartitionPaiementAcor.setTravailleurAgricole(bcSitPro.isTravailleurAgricole());
        repartitionPaiementAcor.setSoumisCotisation(bcSitPro.isSoumisCotisation());
        repartitionPaiementAcor.setIdSituationProfessionnelle(bcSitPro.getIdSituationProfessionnelle());
    }

    private IAPReferenceDataPrestation retrieveReferenceData(BSession session, PeriodeServiceApgType periode, String genreService) {
        IAPReferenceDataPrestation ref;

        try {
            if (session.getCode(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE).equals(genreService)) {
                ref = APReferenceDataParser.loadReferenceData(session, "MATERNITE", JADate.newDateFromAMJ(String.valueOf(periode.getDebut())),
                        JADate.newDateFromAMJ(String.valueOf(periode.getFin())), JADate.newDateFromAMJ(String.valueOf(periode.getFin())));
            } else {
                ref = APReferenceDataParser.loadReferenceData(session, "APG", JADate.newDateFromAMJ(String.valueOf(periode.getDebut())),
                        JADate.newDateFromAMJ(String.valueOf(periode.getFin())), JADate.newDateFromAMJ(String.valueOf(periode.getFin())));
            }
            return ref;
        }catch(Exception e){
            throw new PRAcorTechnicalException("Erreur lors de la récupération des référence data.", e);
        }
    }

    private List<APRepartitionPaiementAcor> createRepartitionPaiementsDecomptes(BSession session, APBaseCalcul baseCalcul, FCalcul fCalcul, VersementMoisComptableApgType moisComptable) throws Exception {
        List<APRepartitionPaiementAcor> repartitions = new ArrayList<>();
        for (VersementMoisComptableApgType versement:
             fCalcul.getVersementMoisComptable()) {
            for (VersementApgType versementApg:
                 versement.getVersement()) {
                VersementBeneficiaireApgType versementBeneficiaire = findVersementBeneficiaireFederalType(versementApg);
                getRepartitionsPaiementParBenficiaire(session, baseCalcul, fCalcul, moisComptable, repartitions, versementBeneficiaire);
                versementBeneficiaire = findVersementBeneficiaireGenevoisType(versementApg);
                getRepartitionsPaiementParBenficiaire(session, baseCalcul, fCalcul, moisComptable, repartitions, versementBeneficiaire);

            }
        }
        return repartitions;
    }

    private void getRepartitionsPaiementParBenficiaire(BSession session, APBaseCalcul baseCalcul, FCalcul fCalcul, VersementMoisComptableApgType moisComptable, List<APRepartitionPaiementAcor> repartitions, VersementBeneficiaireApgType versementBeneficiaire) throws Exception {
        if(Objects.nonNull(versementBeneficiaire)) {
            for (DecompteApgType decompteApgType :
                    versementBeneficiaire.getDecompte()) {
                for (PeriodeDecompteApgType periodeDecompte :
                        decompteApgType.getPeriodeDecompte()) {
                    if (checkPeriodesDansLeMemeMois(periodeDecompte.getDebut(), periodeDecompte.getFin(), moisComptable.getMoisComptable())) {
                        APRepartitionPaiementAcor repartition = createRepartitionPaiement(session, baseCalcul, versementBeneficiaire, periodeDecompte, fCalcul);
                        if (Objects.nonNull(repartition)) {
                            repartitions.add(repartition);
                        }
                    }
                }
            }
        }
    }

    private VersementBeneficiaireApgType findVersementBeneficiaireFederalType(VersementApgType versementApg) {
        VersementBeneficiaireApgType versementBeneficiaire = null;
        if(Objects.nonNull(versementApg.getVersementsFederal())){
            versementBeneficiaire = versementApg.getVersementsFederal().getVersementEmployeur();
            if(Objects.isNull(versementBeneficiaire)){
                versementBeneficiaire = versementApg.getVersementsFederal().getVersementAssure();
            }
        }
        return versementBeneficiaire;
    }

    private VersementBeneficiaireApgType findVersementBeneficiaireGenevoisType(VersementApgType versementApg) {
        VersementBeneficiaireApgType versementBeneficiaire = null;
        if(Objects.nonNull(versementApg.getVersementsGenevois())){
            versementBeneficiaire = versementApg.getVersementsGenevois().getVersementEmployeur();
            if(Objects.isNull(versementBeneficiaire)){
                versementBeneficiaire = versementApg.getVersementsGenevois().getVersementAssure();
            }
        }
        return versementBeneficiaire;
    }

    private List<APPrestationWrapper> createWrappers(List<APPrestationAcor> prestations, APDroitLAPG droit, List<APBaseCalcul> basesCalcul) {
        List<APPrestationWrapper> wrappers = new ArrayList<>();
        for (APPrestationAcor prestation:
             prestations) {
            APPrestationWrapper prestationWrapper = new APPrestationWrapper();
            prestationWrapper.setFraisGarde(prestation.getFraisGarde());
            prestationWrapper.setIdDroit(droit.getIdDroit());
            APPeriodeWrapper periodeWrapper = new APPeriodeWrapper();
            periodeWrapper.setDateDebut(Dates.toJADate(prestation.getDateDebut()));
            periodeWrapper.setDateFin(Dates.toJADate(prestation.getDateFin()));
            prestationWrapper.setPeriodeBaseCalcul(periodeWrapper);
            APResultatCalcul rc = new APResultatCalcul();
            rc.setDateDebut(periodeWrapper.getDateDebut());
            rc.setDateFin(periodeWrapper.getDateFin());
            rc.setSoumisImpotSource(prestation.isSoumisImpotSource());
            rc.setIdTauxImposition(prestation.getIdTauxImposition());
            rc.setTauxImposition(prestation.getTauxImposition());
            rc.setTypeAllocation(prestation.getGenre());
            rc.setVersementAssure(prestation.getVersementAssure());
            rc.setRevision(prestation.getRevision());
            rc.setAllocationJournaliereExploitation(prestation.getAllocationExploitation());
            rc.setAllocationJournaliereMaxFraisGarde(prestation.getFraisGardeMax());
            rc.setMontantJournalier(prestation.getAllocationJournalier());
            rc.setBasicDailyAmount(prestation.getAllocationJournalier());
            rc.setNombreJoursSoldes(prestation.getNombreJoursSoldes());
            rc.setNombreJoursSupplementaires(prestation.getNombreJoursSupplementaires());
            rc.setRevenuDeterminantMoyen(prestation.getRevenuDeterminantMoyen());
            prestationWrapper.setPrestationBase(rc);
            mapResultatCalculSituationProfessionnelWithAPPrestationAcor(prestation, rc);
            mapResultatCalculSituationProfessionnelWithBaseCalcul(basesCalcul, prestation, rc);
            prestationWrapper.setPrestationBase(rc);
            wrappers.add(prestationWrapper);
        }
        return wrappers;
    }

    private void mapResultatCalculSituationProfessionnelWithBaseCalcul(List<APBaseCalcul> basesCalcul, APPrestationAcor prestation, APResultatCalcul rc) {
        APBaseCalcul baseCalcul = findBaseCalcul(basesCalcul, Dates.toJADate(prestation.getDateDebut()),
                Dates.toJADate(prestation.getDateFin()));
        if(Objects.nonNull(baseCalcul)) {
            for (Object o : baseCalcul.getBasesCalculSituationProfessionnel()) {
                APBaseCalculSituationProfessionnel bsp = (APBaseCalculSituationProfessionnel) o;

                List<APResultatCalculSituationProfessionnel> lrcsp = rc.getResultatsCalculsSitProfessionnelle();

                boolean found = false;
                for (APResultatCalculSituationProfessionnel rcsp : lrcsp) {
                    String nomRCSP = rcsp.getNom();
                    String nomBSP = bsp.getNom();
                    nomBSP = nomBSP.trim();
                    nomRCSP = nomRCSP.trim();

                    /*
                     * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '['
                     * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caractères valide,
                     * donc les chiffres, le '+' dit que çà doit correspondre au moins une fois) et qui est
                     * suivit d'un ']' Si je n'est pas été assez claire :
                     * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
                     */
                    nomRCSP = mapNameWithoutEmployerType(nomRCSP);
                    if (rcsp.getIdTiers().equals(bsp.getIdTiers())
                            && (rcsp.getIdAffilie().equals(bsp.getIdAffilie()) || ((Objects.nonNull(rcsp.getNoAffilie())) && rcsp
                            .getNoAffilie().equals(bsp.getNoAffilie())))
                            && (nomBSP.contains(nomRCSP) || nomRCSP.contains(nomBSP))) {
                        found = true;
                        break;
                    }
                }
                // La base de calcul n'existe pas dans la liste des
                // résultats du calcul retournée par ACOR,
                // on va donc la créer et la rajouter.
                if (!found) {
                    APResultatCalculSituationProfessionnel newRcSitPro = new APResultatCalculSituationProfessionnel();
                    // completer la sitpro
                    newRcSitPro.setIdAffilie(bsp.getIdAffilie());
                    newRcSitPro.setIdTiers(bsp.getIdTiers());
                    newRcSitPro.setNom(bsp.getNom());

                    // info Inconnue
                    newRcSitPro.setMontant(new FWCurrency(0));
                    newRcSitPro.setSalaireJournalierNonArrondi(new FWCurrency(0));

                    // Devrait toujours être à false, car ACOR retourne
                    // toujours l
                    newRcSitPro.setVersementEmployeur(bsp.isPaiementEmployeur());
                    newRcSitPro.setIndependant(bsp.isIndependant());
                    newRcSitPro.setTravailleurSansEmployeur(bsp.isTravailleurSansEmployeur());
                    newRcSitPro.setCollaborateurAgricole(bsp.isCollaborateurAgricole());
                    newRcSitPro.setTravailleurAgricole(bsp.isTravailleurAgricole());
                    newRcSitPro.setSoumisCotisation(bsp.isSoumisCotisation());
                    newRcSitPro.setIdSituationProfessionnelle(bsp.getIdSituationProfessionnelle());

                    rc.addResultatCalculSitProfessionnelle(newRcSitPro);
                }
            }
        }
    }

    private void mapResultatCalculSituationProfessionnelWithAPPrestationAcor(APPrestationAcor prestation, APResultatCalcul rc) {
        for (APRepartitionPaiementAcor repartitionPaiement:
             prestation.getRepartitionPaiements()) {
            APResultatCalculSituationProfessionnel rcSitPro = new APResultatCalculSituationProfessionnel();
            rcSitPro.setIdAffilie(repartitionPaiement.getIdAffilie());
            rcSitPro.setIdTiers(repartitionPaiement.getIdTiers());
            rcSitPro.setNoAffilie(repartitionPaiement.getNumeroAffilieEmployeur());
            rcSitPro.setNom(repartitionPaiement.getNomEmployeur());
            rcSitPro.setMontant(repartitionPaiement.getMontantNet());
            rcSitPro.setSalaireJournalierNonArrondi(repartitionPaiement.getSalaireJournalier());
            rcSitPro.setVersementEmployeur(repartitionPaiement.isVersementEmployeur());
            rcSitPro.setIndependant(repartitionPaiement.isIndependant());
            rcSitPro.setCollaborateurAgricole(repartitionPaiement.isCollaborateurAgricole());
            rcSitPro.setTravailleurSansEmployeur(repartitionPaiement.isTravailleurSansEmployeur());
            rcSitPro.setTravailleurAgricole(repartitionPaiement.isTravailleurAgricole());
            rcSitPro.setSoumisCotisation(repartitionPaiement.isSoumisCotisation());
            rcSitPro.setIdSituationProfessionnelle(repartitionPaiement.getIdSituationProfessionnelle());
            rc.addResultatCalculSitProfessionnelle(rcSitPro);
        }
    }

    private void updateWrappersTauxParticipation(BSession session, FCalcul fCalcul, Collection<APPrestationWrapper> wrappers) throws PRACORException {
        for (EmployeurApgType employeur:
            fCalcul.getEmployeur()) {
            Optional<PeriodeRepartitionEmployeurApgType> periodeRepartitionEmployeurOptional = fCalcul.getPeriodeMontantJourn().get(0).getPeriodeRepartition().get(0).getEmployeur().stream().filter(p -> p.getIdEmpl().equals(employeur.getIdIntEmpl())).findFirst();
            if(periodeRepartitionEmployeurOptional.isPresent()) {
                PeriodeRepartitionEmployeurApgType periodeRepartitionEmployeur = periodeRepartitionEmployeurOptional.get();
                for (APPrestationWrapper wrapper : wrappers) {
                    for (APResultatCalculSituationProfessionnel sitPro : wrapper.getPrestationBase().getResultatsCalculsSitProfessionnelle()) {
                        /*
                         * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '['
                         * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caractères valide,
                         * donc les chiffres, le '+' dit que çà doit correspondre au moins une fois) et qui est
                         * suivit d'un ']' Si je n'est pas été assez claire :
                         * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
                         */
                        String nomEmployeur = mapNameWithoutEmployerType(employeur.getNom());
                        String nomSitProEmployeur = mapNameWithoutEmployerType(sitPro.getNom());
                        String idAffilie;
                        String idTiers;
                        if (PRAbstractEmployeur.isNumeroBidon(employeur.getNoAffilie())) {
                            idAffilie = "0"; // sauve dans la base puis recharge, donc 0
                            idTiers = PRAbstractEmployeur.extractIdTiers(employeur.getNoAffilie());
                        } else {
                            try {
                                IPRAffilie affilie = APRepartitionPaiementAcor.getIprAffilie(session, employeur.getNoAffilie(), employeur.getNom());

                                idAffilie = affilie.getIdAffilie();
                                idTiers = affilie.getIdTiers();
                            } catch (Exception e) {
                                throw new PRACORException("Impossible de trouver l'affilie", e);
                            }
                        }
                        if (idAffilie.equals(sitPro.getIdAffilie())
                                && idTiers.equals(sitPro.getIdTiers())
                                && nomEmployeur.equals(nomSitProEmployeur)) {
                            FWCurrency taux = new FWCurrency(periodeRepartitionEmployeur.getTauxRjmArr());
                            sitPro.setTauxProRata(taux);

                            // Il s'agit d'une situation profesionnelle
                            // créer entièrement à partir de la base de
                            // calcul.
                            // newRcSitPro on va donc y rajouter le montant
                            // et salaire journalier en le recalculant à
                            // partir
                            // du montant total de la prestation au prorata.

                            // Ceci est nécessaire pour le calcul du montant
                            // des cotisations, afin de determiner
                            // si la part salariale est supérieure à la part
                            // de l'indépendant, le cas échéant.
                            if ((Objects.isNull(sitPro.getSalaireJournalierNonArrondi()))
                                    || JadeStringUtil.isBlankOrZero(sitPro.getSalaireJournalierNonArrondi()
                                    .toString())) {
                                BigDecimal montant = (BigDecimal.valueOf(fCalcul.getCarteApg().getAllocTotaleCarteApg()))
                                        .multiply(taux.getBigDecimalValue());

                                double salaireJ = PRCalcul.quotient(montant.toString(),
                                        String.valueOf(fCalcul.getCarteApg().getSommeJoursService()));
                                sitPro.setMontant(new FWCurrency(montant.toString()));
                                sitPro.setSalaireJournalierNonArrondi(new FWCurrency(salaireJ));
                            }

                        }
                    }
                }
            }
        }
    }

    private boolean checkPeriodesDansLeMemeMois(int dateDebutPeriode1, int dateFinPeriode1, int dateDebutPeriode2) throws JAException {
        LocalDate dateLocalDebutPeriode1 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateDebutPeriode1)));
        LocalDate dateLocalFinPeriode1 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateFinPeriode1)));
        LocalDate datePeriode2 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateDebutPeriode2)));
        return dateLocalDebutPeriode1.getMonth().equals(datePeriode2.getMonth()) || dateLocalFinPeriode1.getMonth().equals(datePeriode2.getMonth());
    }

    private FWCurrency retrieveFraisDeGarde(final APDroitLAPG droit) throws Exception {
        APDroitAPG droitAPG = entityService.load(APDroitAPG.class, droit.getIdDroit());
        return new FWCurrency(droitAPG.loadSituationFamilliale().getFraisGarde());
    }

    private List<APBaseCalcul> retrieveBasesCalcul(final BSession session, final APDroitLAPG droit) throws Exception {
        return APBasesCalculBuilder.of(session, droit).createBasesCalcul();
    }

    private void checkAndGetNssIntegrite(FCalcul fCalcul, APDroitLAPG droit) throws Exception {

        // changer de parser si NNSS ou NAVS
        PRDemande demande = new PRDemande();
        demande.setSession(entityService.getSession());
        demande.setIdDemande(droit.getIdDemande());
        demande.retrieve();

        if (demande.isNew()) {
            throw new PRACORException("Demande prestation non trouvée !!");
        } else {

            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(entityService.getSession(), demande.getIdTiers());
            if (null == tiers) {
                throw new PRACORException("Tiers de la demande prestation non trouvé !!");
            }

            String nss = "";
            // Récupère le NSS du FCalcul reçu d'ACOR
            for (AssureApgType assure :
                    fCalcul.getAssure()) {
                if (FonctionApgType.REQUERANT.equals(assure.getFonction())) {
                    nss = assure.getId();
                    break;
                }
            }
            if (StringUtils.isBlank(nss)) {
                throw new PRAcorDomaineException("Réponse invalide : Impossible de retrouver le NSS du requérant. ");
            }
            if (nss.equals(tiers.getNSS())) {
                throw new PRAcorDomaineException(entityService.getSession().getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
            }
        }
    }

    private APBaseCalcul findBaseCalcul(List<APBaseCalcul> basesCalcul, JADate dateDebutPeriodeAcor, JADate dateFinPeriodeAcor) {
        APBaseCalcul retValue = null;
        boolean found = false;
        for (APBaseCalcul baseCalcul : basesCalcul) {
            retValue = baseCalcul;
            if (comparePeriod2IsInsidePeriod1(retValue.getDateDebut(), retValue.getDateFin(), dateDebutPeriodeAcor, dateFinPeriodeAcor)) {
                found = true;
                break;
            }
        }

        if(!found && basesCalcul.stream().anyMatch(APBaseCalcul::isExtension)){
            for (APBaseCalcul baseCalcul : basesCalcul) {
                retValue = baseCalcul;

                if (comparePeriod2IsInsidePeriod1(dateDebutPeriodeAcor, dateFinPeriodeAcor, retValue.getDateDebut(), retValue.getDateFin())) {
                    found = true;
                    break;
                }
            }
        }

        if(!found){
            for (APBaseCalcul baseCalcul : basesCalcul){
                retValue = baseCalcul;
                LocalDate localDateDebutPeriod1 = Dates.toDate(dateDebutPeriodeAcor);
                LocalDate localDateDebutPeriod2 = Dates.toDate(retValue.getDateDebut());
                LocalDate localDateFinPeriod2 = Dates.toDate(retValue.getDateFin());
                if(localDateDebutPeriod1.isBefore(localDateFinPeriod2) && localDateDebutPeriod1.isAfter(localDateDebutPeriod2)){
                    found = true;
                    break;
                }
            }
        }

        return found ? retValue : null;
    }

    private boolean comparePeriod2IsInsidePeriod1(JADate startDatePeriod1, JADate endDatePeriode1, JADate startDatePeriod2, JADate endDatePeriode2) {
        LocalDate localDateDebutPeriod1 = Dates.toDate(startDatePeriod1);
        LocalDate localDateFinPeriode1 = Dates.toDate(endDatePeriode1);
        LocalDate localDateDebutPeriod2 = Dates.toDate(startDatePeriod2);
        LocalDate localDateFinPeriod2 = Dates.toDate(endDatePeriode2);
        return (localDateDebutPeriod1.isBefore(localDateDebutPeriod2) ||
                localDateDebutPeriod1.isEqual(localDateDebutPeriod2)) &&
                (localDateFinPeriode1.isAfter(localDateFinPeriod2) ||
                        localDateFinPeriode1.isEqual(localDateFinPeriod2));
    }

    private APBaseCalculSituationProfessionnel findBaseCalculSitPro(APBaseCalcul basesCalcul,
                                                                    String idTiers, String idAffilie, String nomAffilie)
            throws PRACORException {
        if (Objects.isNull(basesCalcul)) {
            throw new PRACORException("La base de calcul est null et la situation professionelle ne peut être trouvée !!!");
        }

        for (Object o : basesCalcul.getBasesCalculSituationProfessionnel()) {
            APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) o;

            if(Objects.nonNull(bcSitPro.getNom())) {
                String nomSP = bcSitPro.getNom().trim();
                String nomAff = nomAffilie.trim();
                /*
                 * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '[' suivi d'un minimum
                 * de un ou de plusieurs chiffres ([0-9] signifie les caractères valide, donc les chiffres, le '+' dit que
                 * çà doit correspondre au moins une fois) et qui est suivit d'un ']' Si je n'est pas été assez claire :
                 * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
                 */
                nomAff = mapNameWithoutEmployerType(nomAff);
                if (Objects.equals(idTiers, bcSitPro.getIdTiers()) && Objects.equals(idAffilie, bcSitPro.getIdAffilie())
                        && ((nomSP.contains(nomAff)) || nomAff.contains(nomSP))) {
                    return bcSitPro;
                }
            }
        }

        throw new PRACORException("La situation professionelle ne peut être trouvée !!!");
    }

    private APBaseCalculSituationProfessionnel findBaseCalculSitProParNoAffilie(APBaseCalcul basesCalcul, String idTiers, String noAffilie,
                                                                                       String nomAffilie) throws PRACORException {
        if (Objects.isNull(basesCalcul)) {
            throw new PRACORException("La base de calcul est null et la situation professionelle ne peut être trouvée par no affilié !!!");
        }

        for (Object o : basesCalcul.getBasesCalculSituationProfessionnel()) {
            APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) o;

            String nomSP = bcSitPro.getNom().trim();
            String nomAff = nomAffilie.trim();


            nomAff = mapNameWithoutEmployerType(nomAff);
            if (idTiers.equals(bcSitPro.getIdTiers()) && noAffilie.equals(bcSitPro.getNoAffilie())
                    && (nomSP.contains(nomAff) || nomAff.contains(nomSP))) {

                // ACOR peut rajouter des (1) a la fin des noms si plusieurs fois le même nom dans la liste des
                // employeurs...
                return bcSitPro;
            }
        }

        throw new PRACORException("la situation professionelle ne peut être trouvée par no affilié !!!");
    }
    private APBaseCalculSituationProfessionnel findBaseCalculSitProVersementAssure(APBaseCalcul basesCalcul){
        APBaseCalculSituationProfessionnel bcSitPro;
        for (Object o1 : basesCalcul.getBasesCalculSituationProfessionnel()) {
            bcSitPro = (APBaseCalculSituationProfessionnel) o1;
            if(!bcSitPro.isPaiementEmployeur()){
                return bcSitPro;
            }
        }
        return null;
    }

    private APBaseCalculSituationProfessionnel findBaseCalculSitProIndependant(APBaseCalcul basesCalcul, String idBeneficiaire) throws Exception {
        APBaseCalculSituationProfessionnel bcSitPro;
        CommonNSSFormater formater = new CommonNSSFormater();
        PRTiersWrapper tiers = PRTiersHelper.getTiers(entityService.getSession(), formater.format(idBeneficiaire));
        if (Objects.nonNull(tiers)) {
            for (Object o1 : basesCalcul.getBasesCalculSituationProfessionnel()) {
                bcSitPro = (APBaseCalculSituationProfessionnel) o1;
                if (bcSitPro.getIdTiers().equals(tiers.getIdTiers()) && bcSitPro.isIndependant()) {
                    return bcSitPro;
                }
            }
        }
        return null;
    }


    /**
     * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '[' suivi d'un minimum
     * de un ou de plusieurs chiffres ([0-9] signifie les caractères valide, donc les chiffres, le '+' dit que
     * çà doit correspondre au moins une fois) et qui est suivit d'un ']' Si je n'est pas été assez claire :
     * <a href="http://en.wikipedia.org/wiki/Regular_expression">...</a> saura répondre (RCO) BZ 8422
     **/
    private String mapNameWithoutEmployerType(String name){
        return name.replaceFirst("^\\[\\d+\\]", "");
    }

    private boolean hasErrors(final BSession session, final BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }
}
