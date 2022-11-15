package globaz.apg.acorweb.mapper;

import acor.apg.xsd.apg.out.*;
import ch.globaz.common.util.Dates;
import ch.globaz.eavs.utils.StringUtils;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.module.calcul.*;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
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

@Slf4j
public final class APAcorImportationUtils {

    private APAcorImportationUtils(){

    }

    public static List<APBaseCalcul> retrieveBasesCalcul(final BSession session, final APDroitLAPG droit) throws Exception {
        return APBasesCalculBuilder.of(session, droit).createBasesCalcul();
    }

    public static void checkAndGetNssIntegrite(BSession session, FCalcul fCalcul, APDroitLAPG droit) throws Exception {

        // changer de parser si NNSS ou NAVS
        PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setIdDemande(droit.getIdDemande());
        demande.retrieve();

        if (demande.isNew()) {
            throw new PRACORException("Demande prestation non trouvée !!");
        } else {

            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, demande.getIdTiers());
            if (null == tiers) {
                throw new PRACORException("Tiers de la demande prestation non trouvé !!");
            }

            String nss = "";
            // Récupère le NSS du FCalcul reçu d'ACOR
            for (AssureApgType assure :
                    fCalcul.getAssure()) {
                if (FonctionApgType.REQUERANT == assure.getFonction()) {
                    nss = assure.getId();
                    break;
                }
            }
            if (StringUtils.isBlank(nss)) {
                throw new PRAcorDomaineException("Réponse invalide : Impossible de retrouver le NSS du requérant. ");
            }
            if (nss.equals(tiers.getNSS())) {
                throw new PRAcorDomaineException(session.getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
            }
        }
    }

    public static APBaseCalcul findBaseCalcul(Collection<APBaseCalcul> basesCalcul, JADate dateDebutPeriodeAcor, JADate dateFinPeriodeAcor) {
        APBaseCalcul retValue = basesCalcul.stream().filter(b ->
                comparePeriod2IsInsidePeriod1(b.getDateDebut(),
                                              b.getDateFin(),
                                              dateDebutPeriodeAcor,
                                              dateFinPeriodeAcor))
                                .findFirst()
                                .orElse(null);

        if(Objects.isNull(retValue) && basesCalcul.stream().anyMatch(APBaseCalcul::isExtension)){
            retValue = basesCalcul.stream().filter(b ->
                            comparePeriod2IsInsidePeriod1(dateDebutPeriodeAcor,
                                                          dateFinPeriodeAcor,
                                                          b.getDateDebut(),
                                                          b.getDateFin()))
                    .findFirst()
                    .orElse(null);
        }

        if(Objects.isNull(retValue)){
            retValue = basesCalcul.stream()
                           .filter(b -> Dates.toDate(dateDebutPeriodeAcor).isBefore(Dates.toDate(b.getDateFin())) &&
                                        Dates.toDate(dateDebutPeriodeAcor).isAfter(Dates.toDate(b.getDateDebut())))
                           .findFirst()
                           .orElse(null);

        }

        return retValue;
    }

    public static APBaseCalculSituationProfessionnel findBaseCalculSitProVersementAssure(APBaseCalcul basesCalcul){
        APBaseCalculSituationProfessionnel bcSitPro;
        for (Object o1 : basesCalcul.getBasesCalculSituationProfessionnel()) {
            bcSitPro = (APBaseCalculSituationProfessionnel) o1;
            if(!bcSitPro.isPaiementEmployeur() && !bcSitPro.isIndependant()){
                return bcSitPro;
            }
        }
        return null;
    }

    public static APBaseCalculSituationProfessionnel findBaseCalculSitProIndependant(BSession session,
                                                                                     APBaseCalcul basesCalcul,
                                                                                     String idBeneficiaire) {
        CommonNSSFormater formater = new CommonNSSFormater();
        try {
            PRTiersWrapper tiers = PRTiersHelper.getTiers(session, formater.format(idBeneficiaire));
            if (Objects.nonNull(tiers)) {
                Optional opt = basesCalcul.getBasesCalculSituationProfessionnel()
                        .stream()
                        .filter(bc ->
                                ((APBaseCalculSituationProfessionnel)bc).getIdTiers().equals(tiers.getIdTiers()) &&
                                ((APBaseCalculSituationProfessionnel)bc).isIndependant())
                        .findFirst();
                return (APBaseCalculSituationProfessionnel)opt.orElse(null);
            }
        }catch(Exception exception){
            LOG.warn("Le tiers avec l'id " + idBeneficiaire + " défini pour retrouver l'instance de "
                                            + APBaseCalculSituationProfessionnel.class + "n'existe pas.");
        }
        return null;
    }

    public static APBaseCalculSituationProfessionnel findBaseCalculSitProParNoAffilie(APBaseCalcul basesCalcul, String idTiers, String noAffilie,
                                                                                String nomAffilie) {
        if (Objects.isNull(basesCalcul)) {
            LOG.warn("La base de calcul est null et la situation professionelle ne peut être trouvée par no affilié !!!");
            return null;
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

        LOG.warn("la situation professionelle ne peut être trouvée par no affilié !!!");
        return null;
    }

    public static APBaseCalculSituationProfessionnel findBaseCalculSitPro(APBaseCalcul basesCalcul,
                                                                    String idTiers, String idAffilie, String nomAffilie) {
        if (Objects.isNull(basesCalcul)) {
            LOG.warn("La base de calcul est null et la situation professionelle ne peut être trouvée !!!");
            return null;
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
        LOG.warn("La situation professionelle ne peut être trouvée !!!");
        return null;
    }

    /**
     * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '[' suivi d'un minimum
     * de un ou de plusieurs chiffres ([0-9] signifie les caractères valide, donc les chiffres, le '+' dit que
     * çà doit correspondre au moins une fois) et qui est suivit d'un ']' Si je n'est pas été assez claire :
     * <a href="http://en.wikipedia.org/wiki/Regular_expression">...</a> saura répondre (RCO) BZ 8422
     **/
    public static String mapNameWithoutEmployerType(String name){
        return name.replaceFirst("^\\[\\d+\\]", "");
    }

    public static boolean comparePeriod2IsInsidePeriod1(JADate startDatePeriod1, JADate endDatePeriode1,
                                                        JADate startDatePeriod2, JADate endDatePeriode2) {
        LocalDate localDateDebutPeriod1 = Dates.toDate(startDatePeriod1);
        LocalDate localDateFinPeriode1 = Dates.toDate(endDatePeriode1);
        LocalDate localDateDebutPeriod2 = Dates.toDate(startDatePeriod2);
        LocalDate localDateFinPeriod2 = Dates.toDate(endDatePeriode2);
        return (localDateDebutPeriod1.isBefore(localDateDebutPeriod2) ||
                localDateDebutPeriod1.isEqual(localDateDebutPeriod2)) &&
                (localDateFinPeriode1.isAfter(localDateFinPeriod2) ||
                        localDateFinPeriode1.isEqual(localDateFinPeriod2));
    }

    public static boolean  checkPeriodesDansLeMemeMois(int dateDebutPeriode1, int dateFinPeriode1, int dateDebutPeriode2) throws JAException {
        LocalDate dateLocalDebutPeriode1 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateDebutPeriode1)));
        LocalDate dateLocalFinPeriode1 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateFinPeriode1)));
        LocalDate datePeriode2 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateDebutPeriode2)));
        return dateLocalDebutPeriode1.getMonth() == datePeriode2.getMonth() || dateLocalFinPeriode1.getMonth() == datePeriode2.getMonth();
    }

    public static IAPReferenceDataPrestation retrieveReferenceData(BSession session, APPeriodeWrapper periode, String genreService) {
        IAPReferenceDataPrestation ref;

        try {
            if (session.getCode(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE).equals(genreService)) {
                ref = APReferenceDataParser.loadReferenceData(session, "MATERNITE", periode.getDateDebut(),
                        periode.getDateFin(), periode.getDateFin());
            } else {
                ref = APReferenceDataParser.loadReferenceData(session, "APG", periode.getDateDebut(),
                        periode.getDateFin(), periode.getDateFin());
            }
            return ref;
        }catch(Exception e){
            throw new PRAcorTechnicalException("Erreur lors de la récupération des référence data.", e);
        }
    }

    public static boolean hasErrors(final BSession session, final BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }

    public static APPeriodeWrapper createAndMapPeriodeWrapper(APPrestationAcor prestation, APPrestationWrapper prestationWrapper) {
        APPeriodeWrapper periodeWrapper = new APPeriodeWrapper();
        periodeWrapper.setDateDebut(Dates.toJADate(prestation.getDateDebut()));
        periodeWrapper.setDateFin(Dates.toJADate(prestation.getDateFin()));
        prestationWrapper.setPeriodeBaseCalcul(periodeWrapper);
        return periodeWrapper;
    }

    public static APResultatCalcul createAndMapAPResultatCalul(List<APBaseCalcul> basesCalcul,
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
        return rc;
    }

    public static List<APPeriodeWrapper> getPeriodes(FCalcul fCalcul, String genreService) {
        List<APPeriodeWrapper> periodes = new ArrayList<>();
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            for (PeriodeServiceApgType periode :
                    fCalcul.getCarteApg().getPeriodeService()) {
                APPeriodeWrapper periodeAcor = getPeriodeWrapper(periode.getDebut(), periode.getFin());
                periodes.add(periodeAcor);
            }
        } else {
            for (VersementMoisComptableApgType moisComptableApgType :
                    fCalcul.getVersementMoisComptable()) {
                for (VersementApgType versementApgType :
                        moisComptableApgType.getVersement()) {
                    getPeriodesFromVersementApgType(periodes, versementApgType.getVersementsGenevois());
                    getPeriodesFromVersementApgType(periodes, versementApgType.getVersementsFederal());
                }
            }
        }
        return periodes;
    }

    public static void getPeriodesFromVersementApgType(List<APPeriodeWrapper> periods, VersementsInstanceAdminApgType versement) {
        if(Objects.nonNull(versement)) {
            getPeriodeDepuisBenificiare(versement.getVersementAssure(), periods);
            getPeriodeDepuisBenificiare(versement.getVersementEmployeur(), periods);
        }
    }

    public static void getPeriodeDepuisBenificiare(VersementBeneficiaireApgType versementBeneficiaireApgType, List<APPeriodeWrapper> periodes) {
        if(Objects.nonNull(versementBeneficiaireApgType)) {
            periodes.addAll(getVersementPeriode(versementBeneficiaireApgType, periodes));
        }
    }

    public static List<APPeriodeWrapper> getVersementPeriode(VersementBeneficiaireApgType versementBeneficiaireApgType,
                                                              List<APPeriodeWrapper> periodesExistantes){
        List<APPeriodeWrapper> periodesPourAjout = new ArrayList<>();
        for (DecompteApgType decompteApgType :
                versementBeneficiaireApgType.getDecompte()) {
            for (PeriodeDecompteApgType periodeDecompteApgType :
                    decompteApgType.getPeriodeDecompte()) {
                APPeriodeWrapper periodeAcor = getPeriodeWrapper(periodeDecompteApgType.getDebut(), periodeDecompteApgType.getFin());
                if(periodesExistantes.stream().noneMatch(p ->
                        periodeAcor.getDateDebut().equals(p.getDateDebut()) &&
                                periodeAcor.getDateFin().equals(p.getDateFin())
                )){
                    periodesPourAjout.add(periodeAcor);
                }
            }
        }
        return periodesPourAjout;
    }

    public static APPeriodeWrapper getPeriodeWrapper(Integer debut, Integer fin) {
        APPeriodeWrapper periodeWrapper = new APPeriodeWrapper();
        try {
            periodeWrapper.setDateDebut(JADate.newDateFromAMJ(String.valueOf(debut)));
        } catch (JAException e) {
            throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de début d'une période de service APG type.", e);
        }
        try {
            periodeWrapper.setDateFin(JADate.newDateFromAMJ(String.valueOf(fin)));
        } catch (JAException e) {
            throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de fin d'une période de service APG type.", e);
        }
        return periodeWrapper;
    }

    public static void updateWrappersTauxParticipation(BSession session,
                                                        FCalcul fCalcul,
                                                        Collection<APPrestationWrapper> wrappers) throws PRACORException {
        for (EmployeurApgType employeur:
                fCalcul.getEmployeur()) {
            for ( PeriodeMontantJournApgType periodeMontantJournApgType:
                    fCalcul.getPeriodeMontantJourn()) {
                for (PeriodeRepartitionApgType periodeRepartitionApgType :
                        periodeMontantJournApgType.getPeriodeRepartition()) {
                    Optional<PeriodeRepartitionEmployeurApgType> periodeRepartitionEmployeurOptional =
                            periodeRepartitionApgType
                                    .getEmployeur().stream().filter(p -> p.getIdEmpl().equals(employeur.getIdIntEmpl())).findFirst();
                    if (periodeRepartitionEmployeurOptional.isPresent()) {
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
        }
    }
}
