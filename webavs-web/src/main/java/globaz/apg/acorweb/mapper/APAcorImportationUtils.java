package globaz.apg.acorweb.mapper;

import acor.apg.xsd.apg.out.*;
import ch.globaz.common.util.Dates;
import ch.globaz.eavs.utils.StringUtils;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.APReferenceDataParser;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.acor.PRAcorTechnicalException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.util.CommonNSSFormater;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public static VersementBeneficiaireApgType findVersementBeneficiaireFederalType(VersementApgType versementApg) {
        VersementBeneficiaireApgType versementBeneficiaire = null;
        if(Objects.nonNull(versementApg.getVersementsFederal())){
            versementBeneficiaire = versementApg.getVersementsFederal().getVersementEmployeur();
            if(Objects.isNull(versementBeneficiaire)){
                versementBeneficiaire = versementApg.getVersementsFederal().getVersementAssure();
            }
        }
        return versementBeneficiaire;
    }

    public static VersementBeneficiaireApgType findVersementBeneficiaireGenevoisType(VersementApgType versementApg) {
        VersementBeneficiaireApgType versementBeneficiaire = null;
        if(Objects.nonNull(versementApg.getVersementsGenevois())){
            versementBeneficiaire = versementApg.getVersementsGenevois().getVersementEmployeur();
            if(Objects.isNull(versementBeneficiaire)){
                versementBeneficiaire = versementApg.getVersementsGenevois().getVersementAssure();
            }
        }
        return versementBeneficiaire;
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
                                                                    String idTiers, String idAffilie, String nomAffilie)
            throws PRACORException {
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

    public static boolean comparePeriod2IsInsidePeriod1(Integer startDatePeriod1, Integer endDatePeriode1,
                                                        Integer startDatePeriod2, Integer endDatePeriode2) {
        JADate startDatePeriod1JADate;
        try {
            startDatePeriod1JADate = JADate.newDateFromAMJ(String.valueOf(startDatePeriod1));
        } catch (JAException e) {
            throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de début d'une période de service APG type.", e);
        }
        JADate endDatePeriode1JADate;
        try {
            endDatePeriode1JADate = JADate.newDateFromAMJ(String.valueOf(endDatePeriode1));
        } catch (JAException e) {
            throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de fin d'une période de service APG type.", e);
        }
        JADate startDatePeriod2JADate;
        try {
            startDatePeriod2JADate = JADate.newDateFromAMJ(String.valueOf(startDatePeriod2));
        } catch (JAException e) {
            throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de début d'une période de service APG type.", e);
        }
        JADate endDatePeriode2JADate;
        try {
            endDatePeriode2JADate = JADate.newDateFromAMJ(String.valueOf(endDatePeriode2));
        } catch (JAException e) {
            throw new PRAcorTechnicalException("Erreur lors de la récupération de la date de fin d'une période de service APG type.", e);
        }
        LocalDate localDateDebutPeriod1 = Dates.toDate(startDatePeriod1JADate);
        LocalDate localDateFinPeriode1 = Dates.toDate(endDatePeriode1JADate);
        LocalDate localDateDebutPeriod2 = Dates.toDate(startDatePeriod2JADate);
        LocalDate localDateFinPeriod2 = Dates.toDate(endDatePeriode2JADate);
        return (localDateDebutPeriod1.isBefore(localDateDebutPeriod2) ||
                localDateDebutPeriod1.isEqual(localDateDebutPeriod2)) &&
                (localDateFinPeriode1.isAfter(localDateFinPeriod2) ||
                        localDateFinPeriode1.isEqual(localDateFinPeriod2));
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
}
