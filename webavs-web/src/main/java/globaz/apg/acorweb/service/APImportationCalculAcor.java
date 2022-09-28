package globaz.apg.acorweb.service;

import acor.apg.xsd.apg.out.*;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Dates;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acorweb.mapper.APPrestationAcor;
import globaz.apg.acorweb.mapper.APRepartitionPaiementAcor;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.helpers.prestation.APPrestationHelper;
import globaz.apg.module.calcul.*;
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
import globaz.prestation.acor.PRAcorTechnicalException;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.tools.PRCalcul;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static globaz.apg.acorweb.mapper.APAcorImportationUtils.*;

@Slf4j
public class APImportationCalculAcor {
    private final FCalcul fCalcul;
    private final EntityService entityService;
    public APImportationCalculAcor(FCalcul fCalcul) {
        this.fCalcul = fCalcul;
        entityService = EntityService.of(BSessionUtil.getSessionFromThreadContext());
    }

    public void importCalculAcor(String idDroit) throws Exception {
        LOG.info("Importation des données calculées.");
        BSession session = entityService.getSession();
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, session.getCurrentThreadTransaction(),
                    idDroit);
            checkAndGetNssIntegrite(session, fCalcul, droit);
            final List<APBaseCalcul> basesCalcul = retrieveBasesCalcul(session, droit);
            Collection<APPrestationWrapper> pw = createAPPrestationWrappers(session, droit, basesCalcul);
            calculerPrestationsComplementaires(session, transaction, droit, pw, basesCalcul);

            if (!hasErrors(session, transaction)) {
                transaction.commit();
            }
        } catch (final Exception exception) {
            APPrestationViewBean pViewBean = new APPrestationViewBean();
            pViewBean.setISession(session);
            if (transaction != null) {
                transaction.rollback();
                // on s'assure que les prestations standards sont supprimées
                APPrestationHelper.supprimerPrestationsDuDroit(session, pViewBean.getIdDroit());
            }
            pViewBean.setMsgType(FWViewBeanInterface.ERROR);
            pViewBean.setMessage("Unexpected exception throw on commit/rollback of transaction during ACOR v4.0 importation  : " + exception);
            throw exception;
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    private List<APPrestationWrapper> createAPPrestationWrappers(BSession session,
                                                                 APDroitLAPG droit,
                                                                 List<APBaseCalcul> basesCalcul) throws Exception {
        List<APPrestationWrapper> wrappers = new ArrayList<>();
        List<APPrestationAcor> prestationsAcor = createAndMapPrestationsAcor(session, droit, basesCalcul);
        for (APPrestationAcor prestation:
                prestationsAcor) {
            APPrestationWrapper prestationWrapper = new APPrestationWrapper();
            prestationWrapper.setFraisGarde(prestation.getFraisGarde());
            prestationWrapper.setIdDroit(droit.getIdDroit());
            APPeriodeWrapper periodeWrapper = createAndMapPeriodeWrapper(prestation, prestationWrapper);
            prestationWrapper.setPrestationBase(createAndMapAPResultatCalul(basesCalcul, prestation, prestationWrapper, periodeWrapper));
            wrappers.add(prestationWrapper);
        }
        updateWrappersTauxParticipation(session, fCalcul, wrappers);
        return wrappers;
    }

    private void calculerPrestationsComplementaires(BSession session,
                                                    BTransaction transaction,
                                                    APDroitLAPG droit,
                                                    Collection<APPrestationWrapper> pw,
                                                    List<APBaseCalcul> basesCalcul) throws Exception {
        final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();

        APPrestationHelper.calculerComplement(session, transaction, droit);
        calculateur.reprendreDepuisACOR(session, pw, droit, retrieveFraisDeGarde(droit), basesCalcul);

        // Calcul des prestations standard, LAMat et ACM_ALFA
        calculateur.calculPrestationAMAT_ACM(session, transaction, droit);

        // Calcul des prestations MATCIAB1 si besoin
        APPrestationHelper.calculerComplementMATCIAB1(session, transaction, droit);
        // Calcul des ACM NE si la propriété TYPE_DE_PRESTATION_ACM vaut ACM_NE
        APPrestationHelper.calculerPrestationsAcmNe(session, transaction, droit);

        // Calcul des prestations ACM 2 si besoin
        APPrestationHelper.calculerPrestationsAcm2(session, transaction, droit);

        // Suppression des prestations standards si l'utilisateur a coché "Exclure l'amat fédéral" dans la situation
        // professionnelle du droit
        calculateur.deletePrestationsStandardsWhenAmatIsExcluded(session, transaction, droit);
    }

    private static APPeriodeWrapper createAndMapPeriodeWrapper(APPrestationAcor prestation, APPrestationWrapper prestationWrapper) {
        APPeriodeWrapper periodeWrapper = new APPeriodeWrapper();
        periodeWrapper.setDateDebut(Dates.toJADate(prestation.getDateDebut()));
        periodeWrapper.setDateFin(Dates.toJADate(prestation.getDateFin()));
        prestationWrapper.setPeriodeBaseCalcul(periodeWrapper);
        return periodeWrapper;
    }

    private static APResultatCalcul createAndMapAPResultatCalul(List<APBaseCalcul> basesCalcul,
                                                                APPrestationAcor prestation,
                                                                APPrestationWrapper prestationWrapper,
                                                                APPeriodeWrapper periodeWrapper) {
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
        prestation.createAndMapResultatCalculSituationProfessionnelleWithRepartitionPaiement(rc);
        mapResultatCalculSituationProfessionnelWithBaseCalcul(basesCalcul, prestation, rc);
        return rc;
    }

    private List<APPrestationAcor> createAndMapPrestationsAcor(BSession session,
                                                               APDroitLAPG droit,
                                                               List<APBaseCalcul> basesCalcul) throws PRACORException {
        List<APPrestationAcor> prestations = new ArrayList<>();
        for (PeriodeServiceApgType periodeServiceApgType:
                fCalcul.getCarteApg().getPeriodeService()) {
            List<APRepartitionPaiementAcor> repartitions = new ArrayList<>();
            JADate dateDebutPeriode;
            try {
                dateDebutPeriode = JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getDebut()));
            } catch (JAException e) {
                throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de début d'une période de service APG type.", e);
            }
            JADate dateFinPeriode;
            try {
                dateFinPeriode = JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getFin()));
            } catch (JAException e) {
                throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de fin d'une période de service APG type.", e);
            }
            APBaseCalcul baseCalcul = findBaseCalcul(basesCalcul, dateDebutPeriode, dateFinPeriode);
            for (VersementMoisComptableApgType moisComptableApgType :
                    fCalcul.getVersementMoisComptable()) {
                for (VersementApgType versementApgType :
                        moisComptableApgType.getVersement()) {
                    createAndMapRepartitionsByVersementApg(session, periodeServiceApgType, repartitions, baseCalcul, versementApgType);
                }
                if (!repartitions.isEmpty()) {
                    try {
                        APPrestationAcor prestationAcor = createAndMapPrestation( droit,
                                                                                  moisComptableApgType,
                                                                                  periodeServiceApgType,
                                                                                  baseCalcul);
                        for (APRepartitionPaiementAcor repartition :
                                repartitions) {
                            prestationAcor.getRepartitionPaiements().add(repartition);
                        }
                        prestations.add(prestationAcor);
                    } catch (Exception e) {
                        throw new PRAcorTechnicalException("Erreur lors de la création de la prestation.", e);
                    }
                }
            }
        }
        return prestations;
    }

    private void createAndMapRepartitionsByVersementApg(BSession session,
                                                        PeriodeServiceApgType periodeServiceApgType,
                                                        List<APRepartitionPaiementAcor> repartitions,
                                                        APBaseCalcul baseCalcul,
                                                        VersementApgType versementApgType) throws PRACORException {
        VersementsInstanceAdminApgType versementGenevois = versementApgType.getVersementsGenevois();
        if(Objects.nonNull(versementGenevois)) {
            VersementBeneficiaireApgType versementAssure = versementGenevois.getVersementAssure();
            createAndMapRepartitionByBeneficiaire(session, fCalcul, periodeServiceApgType, repartitions, baseCalcul, versementAssure);
            VersementBeneficiaireApgType versementEmployeur = versementGenevois.getVersementEmployeur();
            createAndMapRepartitionByBeneficiaire(session, fCalcul, periodeServiceApgType, repartitions, baseCalcul, versementEmployeur);
        }
        VersementsInstanceAdminApgType versementFederal = versementApgType.getVersementsFederal();
        if(Objects.nonNull(versementFederal)) {
            VersementBeneficiaireApgType versementEmployeur = versementFederal.getVersementEmployeur();
            createAndMapRepartitionByBeneficiaire(session, fCalcul, periodeServiceApgType, repartitions, baseCalcul, versementEmployeur);
            VersementBeneficiaireApgType versementAssure = versementFederal.getVersementAssure();
            createAndMapRepartitionByBeneficiaire(session, fCalcul, periodeServiceApgType, repartitions, baseCalcul, versementAssure);
        }
    }

    private void createAndMapRepartitionByBeneficiaire(BSession session, FCalcul fCalcul,
                                                       PeriodeServiceApgType periodeServiceApgType,
                                                       List<APRepartitionPaiementAcor> repartitions,
                                                       APBaseCalcul baseCalcul,
                                                       VersementBeneficiaireApgType versementAssure) throws PRACORException {
        if(Objects.nonNull(versementAssure)) {
            APRepartitionPaiementAcor repartitionPaiementAcor = createAndMapRepartitionByBeneficiaire(session,
                                                                                                               periodeServiceApgType,
                                                                                                               baseCalcul,
                                                                                                               versementAssure);
            if(Objects.nonNull(repartitionPaiementAcor)){
                repartitions.add(repartitionPaiementAcor);
            }
        }
    }

    private APRepartitionPaiementAcor createAndMapRepartitionByBeneficiaire(BSession session,
                                                                            PeriodeServiceApgType periodeServiceApgType,
                                                                            APBaseCalcul baseCalcul,
                                                                            VersementBeneficiaireApgType versementBeneficiaireApgType)
                                                                                throws PRACORException {
        for (DecompteApgType decompteApgType :
                versementBeneficiaireApgType.getDecompte()) {
            for (PeriodeDecompteApgType periodeDecompteApgType :
                    decompteApgType.getPeriodeDecompte()) {
                if (periodeDecompteApgType.getDebut().equals(periodeServiceApgType.getDebut()) &&
                        periodeDecompteApgType.getFin().equals(periodeServiceApgType.getFin())) {
                    return APPrestationAcor.createRepartitionPaiement(session,
                            baseCalcul,
                            versementBeneficiaireApgType,
                            periodeDecompteApgType,
                            fCalcul);
                }
            }
        }
        return null;
    }

    private APPrestationAcor createAndMapPrestation(APDroitLAPG droit,
                                                    VersementMoisComptableApgType versementMoisComptable,
                                                    PeriodeServiceApgType periodeServiceApgType,
                                                    APBaseCalcul baseCalcul) throws Exception {
        BSession session = entityService.getSession();
        APPrestationAcor prestation = new APPrestationAcor();
        AssureApgType assure = fCalcul.getAssure().stream()
                .filter(a -> a.getFonction() == FonctionApgType.REQUERANT)
                .collect(Collectors.toList()).get(0);
        String genreService = session.getCode(droit.getGenreService());
        if (Objects.nonNull(fCalcul.getFraisGarde())) {
            prestation.setFraisGarde(new FWCurrency(fCalcul.getFraisGarde().getMontantGardeOctroye()));
        } else {
            retrieveFraisDeGarde(droit);
        }
        prestation.setMoisComptable(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(versementMoisComptable.getMoisComptable()))));
        prestation.setIdAssure(assure.getId());
        prestation.setGenre(genreService);
        prestation.mapInformationFromPeriodeServiceApg(session, genreService, periodeServiceApgType);
        prestation.mapInformationFromMontantJournalierApg(droit, fCalcul);
        prestation.mapInformationFromBaseCalcul(baseCalcul);
        return prestation;
    }

    private static void mapResultatCalculSituationProfessionnelWithBaseCalcul(List<APBaseCalcul> basesCalcul,
                                                                       APPrestationAcor prestationAcor,
                                                                       APResultatCalcul rc) {
        APBaseCalcul baseCalcul = findBaseCalcul(basesCalcul, Dates.toJADate(prestationAcor.getDateDebut()),
                Dates.toJADate(prestationAcor.getDateFin()));
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
                    if (isSameAffilie(rcsp, bsp) && isBaseCalculAndSitProAreSame(nomRCSP, nomBSP)) {
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

    private static boolean isBaseCalculAndSitProAreSame(String nomRCSP, String nomBSP) {
        return nomBSP.contains(nomRCSP) || nomRCSP.contains(nomBSP);
    }

    private static boolean isSameAffilie(APResultatCalculSituationProfessionnel rcsp, APBaseCalculSituationProfessionnel bsp) {
        return (rcsp.getIdTiers().equals(bsp.getIdTiers()) &&
                rcsp.getIdAffilie().equals(bsp.getIdAffilie())) ||
               rcsp.getNoAffilie().equals(bsp.getNoAffilie());
    }

    private static void updateWrappersTauxParticipation(BSession session,
                                                        FCalcul fCalcul,
                                                        Collection<APPrestationWrapper> wrappers) throws PRACORException {
        for (EmployeurApgType employeur:
            fCalcul.getEmployeur()) {
            Optional<PeriodeRepartitionEmployeurApgType> periodeRepartitionEmployeurOptional =
                    fCalcul.getPeriodeMontantJourn().get(0)
                           .getPeriodeRepartition().get(0)
                           .getEmployeur().stream().filter(p -> p.getIdEmpl().equals(employeur.getIdIntEmpl())).findFirst();
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
                        String nomSitProEmployeur = mapNameWithoutEmployerType(sitPro.getNom());
                        String nomEmployeur = mapNameWithoutEmployerType(employeur.getNom());
                        if (idAffilie.equals(sitPro.getIdAffilie())
                                && idTiers.equals(sitPro.getIdTiers())
                                && nomEmployeur.equals(nomSitProEmployeur)) {
                            FWCurrency taux = new FWCurrency(periodeRepartitionEmployeur.getTauxRjmArr(), 4);
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

    private FWCurrency retrieveFraisDeGarde(final APDroitLAPG droit) throws Exception {
        APDroitAPG droitAPG = entityService.load(APDroitAPG.class, droit.getIdDroit());
        return new FWCurrency(droitAPG.loadSituationFamilliale().getFraisGarde());
    }

    private static boolean hasErrors(final BSession session, final BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }
}
