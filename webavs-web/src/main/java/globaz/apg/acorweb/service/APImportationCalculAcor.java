package globaz.apg.acorweb.service;

import acor.apg.xsd.apg.out.*;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Dates;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acorweb.mapper.APAcorImportationUtils;
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
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorTechnicalException;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.tools.PRCalcul;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        try{
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
            if (!APAcorImportationUtils.hasErrors(session, transaction)) {
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
            throw new PRAcorTechnicalException(exception.getMessage(), exception);
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

        calculateur.reprendreDepuisACOR(session, pw, droit, retrieveFraisDeGarde(droit), basesCalcul);

        // Calcul des prestations standard, LAMat et ACM_ALFA
        calculateur.calculPrestationAMAT_ACM(session, transaction, droit);

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
    }

    private List<APPrestationAcor> createAndMapPrestationsAcor(BSession session,
                                                               APDroitLAPG droit,
                                                               List<APBaseCalcul> basesCalcul) {
        List<APPrestationAcor> prestations = new ArrayList<>();
        List<APPeriodeWrapper> periodes = getPeriodes(fCalcul, droit.getGenreService());
        for (APPeriodeWrapper periode :
                periodes) {
            APBaseCalcul baseCalcul = findBaseCalcul(basesCalcul, periode.getDateDebut(), periode.getDateFin());
            for (VersementMoisComptableApgType moisComptableApgType :
                    fCalcul.getVersementMoisComptable()) {
                try {
                    if (APAcorImportationUtils.checkPeriodesDansLeMemeMois(periode.getDateDebut().toInt(),
                            periode.getDateFin().toInt(),
                            moisComptableApgType.getMoisComptable())) {

                        APPrestationAcor prestationAcor = createAndMapPrestation(droit, moisComptableApgType, periode, baseCalcul);
                        for (VersementApgType versementApgType :
                                moisComptableApgType.getVersement()) {
                            List<APRepartitionPaiementAcor> repartitions = new ArrayList<>();
                            createAndMapRepartitionsByVersementApg(session, periode, repartitions, baseCalcul, versementApgType);
                            prestationAcor.getRepartitionPaiements().addAll(repartitions);
                        }
                        prestations.add(prestationAcor);
                    }
                } catch (Exception e) {
                    throw new PRAcorTechnicalException("Erreur lors de la création de la prestation.", e);
                }
            }
        }
        return prestations;
    }

    private void createAndMapRepartitionsByVersementApg(BSession session,
                                                        APPeriodeWrapper periode,
                                                        List<APRepartitionPaiementAcor> repartitions,
                                                        APBaseCalcul baseCalcul,
                                                        VersementApgType versementApgType) throws PRACORException {
        VersementsInstanceAdminApgType versementGenevois = versementApgType.getVersementsGenevois();
        if(Objects.nonNull(versementGenevois)) {
            VersementBeneficiaireApgType versementAssure = versementGenevois.getVersementAssure();
            createAndMapRepartitionByBeneficiaire(session, periode, repartitions, baseCalcul, versementAssure);
            VersementBeneficiaireApgType versementEmployeur = versementGenevois.getVersementEmployeur();
            createAndMapRepartitionByBeneficiaire(session, periode, repartitions, baseCalcul, versementEmployeur);
        }
        VersementsInstanceAdminApgType versementFederal = versementApgType.getVersementsFederal();
        if(Objects.nonNull(versementFederal)) {
            VersementBeneficiaireApgType versementEmployeur = versementFederal.getVersementEmployeur();
            createAndMapRepartitionByBeneficiaire(session, periode, repartitions, baseCalcul, versementEmployeur);
            VersementBeneficiaireApgType versementAssure = versementFederal.getVersementAssure();
            createAndMapRepartitionByBeneficiaire(session, periode, repartitions, baseCalcul, versementAssure);
        }
    }

    private void createAndMapRepartitionByBeneficiaire(BSession session,
                                                       APPeriodeWrapper periode,
                                                       List<APRepartitionPaiementAcor> repartitions,
                                                       APBaseCalcul baseCalcul,
                                                       VersementBeneficiaireApgType versementAssure) throws PRACORException {
        if(Objects.nonNull(versementAssure)) {
            List<APRepartitionPaiementAcor> repartitionPaiementAcors = createAndMapRepartitionByBeneficiaire(session,
                                                                                                             baseCalcul,
                                                                                                             periode,
                                                                                                             versementAssure);
            if(!repartitionPaiementAcors.isEmpty()){
                repartitions.addAll(repartitionPaiementAcors);
            }
        }
    }

    private List<APRepartitionPaiementAcor> createAndMapRepartitionByBeneficiaire(BSession session,
                                                                                  APBaseCalcul baseCalcul,
                                                                                  APPeriodeWrapper periode,
                                                                                  VersementBeneficiaireApgType versementBeneficiaireApgType)
                                                                                  throws PRACORException {
        List<APRepartitionPaiementAcor> repartitionPaiementAcors = new ArrayList<>();
        for (DecompteApgType decompteApgType :
                versementBeneficiaireApgType.getDecompte()) {
            for (PeriodeDecompteApgType periodeDecompteApgType :
                    decompteApgType.getPeriodeDecompte()) {
                APPeriodeWrapper periodeApg = getPeriodeWrapper(periodeDecompteApgType.getDebut(), periodeDecompteApgType.getFin());
                LocalDate periodeApgDebut = Dates.toDate(periodeApg.getDateDebut());
                LocalDate periodeApgFin = Dates.toDate(periodeApg.getDateFin());
                LocalDate periodeDebut = Dates.toDate(periode.getDateDebut());
                LocalDate periodeFin = Dates.toDate(periode.getDateFin());
                boolean isInPeriod = periodeDebut.isBefore(periodeApgDebut) || periodeDebut.isEqual(periodeApgDebut);
                isInPeriod = isInPeriod && (periodeFin.isAfter(periodeApgFin) || periodeFin.isEqual(periodeApgFin));
                if (isInPeriod) {

                repartitionPaiementAcors.add(APPrestationAcor.createRepartitionPaiement(session,
                        baseCalcul,
                        versementBeneficiaireApgType,
                        periodeDecompteApgType,
                        fCalcul));
                }
            }
        }
        return repartitionPaiementAcors;
    }

    private APPrestationAcor createAndMapPrestation(APDroitLAPG droit,
                                                    VersementMoisComptableApgType versementMoisComptable,
                                                    APPeriodeWrapper periode,
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
            prestation.setFraisGarde(retrieveFraisDeGarde(droit));
        }
        prestation.setMoisComptable(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(versementMoisComptable.getMoisComptable()))));
        prestation.setIdAssure(assure.getId());
        prestation.setGenre(genreService);
        prestation.mapInformationFromPeriodeServiceApg(session, droit.getGenreService(), periode, fCalcul);
        prestation.mapInformationFromMontantJournalierApg(periode, fCalcul, droit.getGenreService());
        prestation.mapInformationFromBaseCalcul(baseCalcul);
        return prestation;
    }

    private FWCurrency retrieveFraisDeGarde(final APDroitLAPG droit) throws Exception {
        APDroitAPG droitAPG = entityService.load(APDroitAPG.class, droit.getIdDroit());
        return new FWCurrency(droitAPG.loadSituationFamilliale().getFraisGarde());
    }
}
