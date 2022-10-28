package globaz.apg.acorweb.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import acor.apg.xsd.apg.out.*;
import ch.globaz.common.util.Dates;
import ch.globaz.eavs.utils.StringUtils;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.APResultatCalculSituationProfessionnel;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorTechnicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static globaz.apg.acorweb.mapper.APAcorImportationUtils.*;

@Slf4j
public class APPrestationAcor {
    @Getter
    @Setter
    private String idAssure;
    @Getter
    @Setter
    LocalDate moisComptable;
    @Getter
    @Setter
    private LocalDate dateDebut;
    @Getter
    @Setter
    private LocalDate dateFin;
    @Getter
    @Setter
    BigDecimal tauxJour;
    @Getter
    @Setter
    BigDecimal tauxJourBase;
    @Getter
    @Setter
    FWCurrency montantBrut;
    @Getter
    @Setter
    FWCurrency allocationExploitation;
    @Getter
    @Setter
    FWCurrency allocationJournalier;
    @Getter
    @Setter
    FWCurrency allocationJournalierPlaffonee;
    @Getter
    @Setter
    FWCurrency fraisGarde;
    @Getter
    @Setter
    FWCurrency fraisGardeMax;
    @Getter
    @Setter
    FWCurrency versementAssure;
    @Getter
    @Setter
    Integer nombreJoursSoldes;
    @Getter
    @Setter
    Integer nombreJoursSupplementaires;
    @Getter
    @Setter
    FWCurrency revenuDeterminantMoyen;
    @Getter
    @Setter
    String genre;
    @Getter
    @Setter
    String revision;
    @Getter
    @Setter
    boolean soumisImpotSource;
    @Getter
    @Setter
    String idTauxImposition;
    @Getter
    @Setter
    String tauxImposition;

    @Getter
    List<APRepartitionPaiementAcor> repartitionPaiements;

    public APPrestationAcor(){
        repartitionPaiements = new ArrayList<>();
    }

    public void mapInformationFromBaseCalcul(APBaseCalcul baseCalcul) {
        if(Objects.nonNull(baseCalcul)) {
            setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
            setIdTauxImposition(baseCalcul.getIdTauxImposition());
            setTauxImposition(baseCalcul.getTauxImposition());
        }
    }

    public void mapInformationFromMontantJournalierApg(APPeriodeWrapper periode, FCalcul fCalcul, String genreService) {
        Optional<PeriodeMontantJournApgType> periodeMontantJournApgTypeOpt = fCalcul.getPeriodeMontantJourn()
                .stream().filter(p -> isInPeriode(p.getDebut(), p.getFin(), periode)).findFirst();
        if(periodeMontantJournApgTypeOpt.isPresent()){
            PeriodeMontantJournApgType periodeMontantJournApgType = periodeMontantJournApgTypeOpt.get();
            setAllocationJournalier(new FWCurrency(periodeMontantJournApgType.getAllocJourn()));
            setAllocationExploitation(new FWCurrency(periodeMontantJournApgType.getAllocJournExploitation()));
            setRevenuDeterminantMoyen(new FWCurrency(periodeMontantJournApgType.getRjm()));
            if(!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
                setNombreJoursSoldes(periodeMontantJournApgType.getNbJours());
            }
        }else {
            setAllocationJournalier(new FWCurrency(0));
            setAllocationExploitation(new FWCurrency(0));
            setRevenuDeterminantMoyen(new FWCurrency(0));
        }
        setNombreJoursSupplementaires(0);
    }

    public void mapInformationFromPeriodeServiceApg(BSession session, String genreService, APPeriodeWrapper periode, FCalcul fCalcul) {
        IAPReferenceDataPrestation ref = retrieveReferenceData(session, periode, genreService);
        setRevision(ref.getNoRevision());
        setFraisGardeMax(ref.getMontantMaxFraisGarde());
        setDateDebut(Dates.toDate(periode.getDateDebut()));
        setDateFin(Dates.toDate(periode.getDateFin()));
        if(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            Optional<PeriodeServiceApgType> periodeServiceApgType = fCalcul.getCarteApg()
                    .getPeriodeService()
                    .stream().filter(p -> isInPeriode(p.getDebut(), p.getFin(), periode)).findFirst();
            periodeServiceApgType.ifPresent(serviceApgType -> setNombreJoursSoldes(serviceApgType.getNbJours()));
        }
    }

    private boolean isSamePeriode(Integer debut, Integer fin, APPeriodeWrapper periodeWrapper){
        try {
            JADate periodeApgDebut = JADate.newDateFromAMJ(String.valueOf(debut));
            if(periodeApgDebut.equals(periodeWrapper.getDateDebut())){
                JADate periodeApgFin = JADate.newDateFromAMJ(String.valueOf(fin));
                if(periodeApgFin.equals(periodeWrapper.getDateFin())){
                    return true;
                }
            }
            return false;
        } catch (JAException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isInPeriode(Integer debut, Integer fin, APPeriodeWrapper periodeWrapper) {
        try {
            LocalDate periodeApgDebut = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(debut)));
            if (periodeApgDebut.isBefore(Dates.toDate(periodeWrapper.getDateDebut())) ||
                periodeApgDebut.isEqual(Dates.toDate(periodeWrapper.getDateDebut()))) {
                LocalDate periodeApgFin = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(fin)));
                if (periodeApgFin.isAfter(Dates.toDate(periodeWrapper.getDateFin())) ||
                    periodeApgFin.isEqual(Dates.toDate(periodeWrapper.getDateFin()))) {
                    return true;
                }
            }
            return false;
        } catch (JAException e) {
            throw new RuntimeException(e);
        }
    }

    public static APRepartitionPaiementAcor createRepartitionPaiement(BSession session,
                                                                APBaseCalcul baseCalcul,
                                                                VersementBeneficiaireApgType beneficiare,
                                                                PeriodeDecompteApgType periodeDecompte, FCalcul fCalcul) throws PRACORException {
        Optional<EmployeurApgType> employeurOptional = fCalcul.getEmployeur().stream()
                                                              .filter(e -> e.getIdIntEmpl().equals(beneficiare.getIdBeneficiaire()))
                                                              .findFirst();
        if (employeurOptional.isPresent()) {
            EmployeurApgType employeur = employeurOptional.get();
            APRepartitionPaiementAcor repartitionPaiementAcor = new APRepartitionPaiementAcor(session,
                                                                                              employeur.getNoAffilie(),
                                                                                              employeur.getNom(),
                                                                                    "");
            repartitionPaiementAcor.setMontantNet(new FWCurrency(periodeDecompte.getMontantPeriode()));
            repartitionPaiementAcor.setSalaireJournalier(new FWCurrency(periodeDecompte.getMontantJourn()));
            APBaseCalculSituationProfessionnel bcSitPro = null;

            bcSitPro = findBaseCalculSitPro(baseCalcul, repartitionPaiementAcor.getIdTiers(),
                    repartitionPaiementAcor.getIdAffilie(), employeur.getNom());
            if (Objects.isNull(bcSitPro)) {
                // Nouvelle tentative de recherche par #affilie pour les cas affiliés avec 2 affiliations sous
                // le même #.
                bcSitPro = findBaseCalculSitProParNoAffilie(baseCalcul, repartitionPaiementAcor.getIdTiers(),
                        repartitionPaiementAcor.getIdAffilie(), employeur.getNom());
            }
            if (Objects.nonNull(bcSitPro)) {
                repartitionPaiementAcor.mapSituationProfessionnel(bcSitPro);
            }
            return repartitionPaiementAcor;
        }
        if(Double.compare(periodeDecompte.getTauxAllocJourn(), 0) != 0) {
            APBaseCalculSituationProfessionnel bcSitPro = findBaseCalculSitProVersementAssure(baseCalcul);
            if (Objects.isNull(bcSitPro)) {
                bcSitPro = findBaseCalculSitProIndependant(session, baseCalcul, beneficiare.getIdBeneficiaire());
            }
            if (Objects.nonNull(bcSitPro)) {
                APRepartitionPaiementAcor repartitionPaiementAcor = new APRepartitionPaiementAcor(beneficiare.getIdBeneficiaire());
                repartitionPaiementAcor.setMontantNet(new FWCurrency(periodeDecompte.getMontantPeriode()));
                repartitionPaiementAcor.setSalaireJournalier(new FWCurrency(periodeDecompte.getMontantJourn()));
                repartitionPaiementAcor.setNomEmployeur(bcSitPro.getNom());
                repartitionPaiementAcor.setNumeroAffilieEmployeur(bcSitPro.getNoAffilie());
                repartitionPaiementAcor.updateIdsEmployeur(session, bcSitPro.getNoAffilie(), bcSitPro.getNom());
                repartitionPaiementAcor.mapSituationProfessionnel(bcSitPro);
                return repartitionPaiementAcor;
            }
        }
        APRepartitionPaiementAcor repartitionPaiementAcor = new APRepartitionPaiementAcor(beneficiare.getIdBeneficiaire());
        repartitionPaiementAcor.setMontantNet(new FWCurrency(periodeDecompte.getMontantPeriode()));
        repartitionPaiementAcor.setSalaireJournalier(new FWCurrency(periodeDecompte.getMontantJourn()));
        repartitionPaiementAcor.setVersementEmployeur(false);
        repartitionPaiementAcor.setIndependant(false);
        repartitionPaiementAcor.setTravailleurSansEmployeur(false);
        repartitionPaiementAcor.setCollaborateurAgricole(false);
        repartitionPaiementAcor.setTravailleurAgricole(false);
        repartitionPaiementAcor.setSoumisCotisation(true);
        return repartitionPaiementAcor;
    }

    public void createAndMapResultatCalculSituationProfessionnelleWithRepartitionPaiement(APResultatCalcul rc) {
        for (APRepartitionPaiementAcor repartitionPaiement:
                repartitionPaiements) {
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

}
