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
import globaz.apg.module.calcul.*;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
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
        APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, session.getCurrentThreadTransaction(),
                idDroit);
        checkAndGetNssIntegrite(fCalcul, droit);
        final List<APBaseCalcul> basesCalcul = getBasesCalcul(session, droit);
        Collection<APPrestationWrapper> pw = createWrappers(session, createPrestationAcor(session, fCalcul, droit, basesCalcul), droit, basesCalcul);
        updateWrappersTauxParticipation(session, fCalcul, pw);
        final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
        calculateur.reprendreDepuisACOR(session, pw, droit, getFraisDeGarde(droit), basesCalcul);
    }

    private List<APPrestationAcor> createPrestationAcor(BSession session, FCalcul fCalcul, APDroitLAPG droit,  List<APBaseCalcul> basesCalcul) throws Exception {
        List<APPrestationAcor> prestations = new ArrayList<>();
        AssureApgType assure = fCalcul.getAssure().stream().filter(a -> a.getFonction() == FonctionApgType.REQUERANT).collect(Collectors.toList()).get(0);
        String genreService = session.getCode(droit.getGenreService());
        for (VersementMoisComptableApgType versementMoisComptable:
             fCalcul.getVersementMoisComptable()) {

            APPrestationAcor prestation = new APPrestationAcor();
            if(fCalcul.getFraisGarde() != null) {
                prestation.setFraisDegarde(new FWCurrency(fCalcul.getFraisGarde().getMontantGardeOctroye()));
            }else{
                getFraisDeGarde(droit);
            }
            prestation.setMoisComptable(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(versementMoisComptable.getMoisComptable()))));
            prestation.setIdAssure(assure.getId());
            prestation.setGenre(genreService);
            getInformationFromPeriodeServiceApg(session, fCalcul, genreService, versementMoisComptable, prestation);
            getInformationFromMontantJournalierApg(fCalcul, prestation);

            APBaseCalcul baseCalcul = getInformationFromBaseCalcul(session, basesCalcul, prestation);
            List<APRepartitionPaiementAcor> repartitionPaiement = createRepartitionPaiementsDecomptes(session, baseCalcul, fCalcul, versementMoisComptable);
            for (APRepartitionPaiementAcor repartitionPaiementAcor:
                    repartitionPaiement) {
                prestation.getRepartitionPaiements().add(repartitionPaiementAcor);
            }
            prestations.add(prestation);
        }
        return prestations;
    }

    private APBaseCalcul getInformationFromBaseCalcul(BSession session, List<APBaseCalcul> basesCalcul, APPrestationAcor prestation) throws PRACORException {
        APBaseCalcul baseCalcul = findBaseCalcul(session, basesCalcul, Dates.toJADate(prestation.getDateDebut()),
                Dates.toJADate(prestation.getDateFin()));
        if(baseCalcul != null) {
            prestation.setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
            prestation.setIdTauxImposition(baseCalcul.getIdTauxImposition());
            prestation.setTauxImposition(baseCalcul.getTauxImposition());
        }
        return baseCalcul;
    }

    private void getInformationFromMontantJournalierApg(FCalcul fCalcul, APPrestationAcor prestation) {
        PeriodeMontantJournApgType periodeMontantJournApgType = fCalcul.getPeriodeMontantJourn().get(0);
        if(periodeMontantJournApgType != null){
            prestation.setAllocationJournalier(new FWCurrency(periodeMontantJournApgType.getAllocJournBaseEnfants()));
            // TODO : Récupérer les jours d'hospitalisation soit depuis FCalcul soit depuis le droit.
            prestation.setNombreJoursSupplementaires(0);
            prestation.setRevenuDeterminantMoyen(new FWCurrency(periodeMontantJournApgType.getRjm()));
        }
    }

    private void getInformationFromPeriodeServiceApg(BSession session, FCalcul fCalcul, String genreService, VersementMoisComptableApgType versementMoisComptable, APPrestationAcor prestation) throws Exception {
        Optional<PeriodeServiceApgType> periodeServiceApgTypeOptional = fCalcul.getCarteApg().getPeriodeService().stream().filter(p -> {
            try {
                return checkPeriodesDansLeMemeMois(p.getDebut(), versementMoisComptable.getMoisComptable());
            } catch (JAException e) {
                throw new PRAcorTechnicalException(e);
            }
        }).findFirst();
        if(periodeServiceApgTypeOptional.isPresent()){
            PeriodeServiceApgType periodeServiceApgType = periodeServiceApgTypeOptional.get();
            if(periodeServiceApgType.getPartAssure() != null) {
                prestation.setVersementAssure(new FWCurrency(periodeServiceApgType.getPartAssure()));
            }
            IAPReferenceDataPrestation ref = getReferenceData(session,periodeServiceApgType, genreService);
            prestation.setRevision(ref.getNoRevision());
            prestation.setFraisDegardeMax(ref.getMontantMaxFraisGarde());
            prestation.setDateDebut(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getDebut()))));
            prestation.setDateFin(Dates.toDate(JADate.newDateFromAMJ(String.valueOf(periodeServiceApgType.getFin()))));
            prestation.setNombreJoursSoldes(periodeServiceApgType.getNbJours());
        }
    }

    private APRepartitionPaiementAcor createRepartitionPaiement(BSession session, APBaseCalcul baseCalcul, VersementBeneficiaireApgType beneficiare, PeriodeDecompteApgType periodeDecompte, FCalcul fCalcul) throws JAException {
        Optional<EmployeurApgType> employeurOptional = fCalcul.getEmployeur().stream().filter(e -> e.getIdIntEmpl().equals(beneficiare.getIdBeneficiaire())).findFirst();
        if(employeurOptional.isPresent()){
            EmployeurApgType employeur = employeurOptional.get();
            APRepartitionPaiementAcor repartitionPaiementAcor = new APRepartitionPaiementAcor(session, employeur.getNoAffilie(), employeur.getNom(), "");
            repartitionPaiementAcor.setMontantNet(new FWCurrency(periodeDecompte.getMontantPeriode()));
            repartitionPaiementAcor.setSalaireJournalier(new FWCurrency(periodeDecompte.getMontantJourn()));
            APBaseCalculSituationProfessionnel bcSitPro;
            try {
                bcSitPro = findBaseCalculSitPro(baseCalcul, repartitionPaiementAcor.getIdTiers(),
                        repartitionPaiementAcor.getIdAffilie(), employeur.getNom());
            } catch (PRACORException e) {

                // Nouvelle tentative de recherche par #affilie pour les cas affiliés avec 2 affiliations sous
                // le même #.
                bcSitPro = findBaseCalculSitProParNoAffilie(baseCalcul, repartitionPaiementAcor.getIdTiers(),
                        repartitionPaiementAcor.getIdAffilie(), employeur.getNom(),
                        null);
            }
            repartitionPaiementAcor.setVersementEmployeur(bcSitPro.isPaiementEmployeur());
            repartitionPaiementAcor.setIndependant(bcSitPro.isIndependant());
            repartitionPaiementAcor.setTravailleurSansEmployeur(bcSitPro.isTravailleurSansEmployeur());
            repartitionPaiementAcor.setCollaborateurAgricole(bcSitPro.isCollaborateurAgricole());
            repartitionPaiementAcor.setTravailleurAgricole(bcSitPro.isTravailleurAgricole());
            repartitionPaiementAcor.setSoumisCotisation(bcSitPro.isSoumisCotisation());
            repartitionPaiementAcor.setIdSituationProfessionnelle(bcSitPro.getIdSituationProfessionnelle());
            return repartitionPaiementAcor;
        }
        return null;
    }

    private IAPReferenceDataPrestation getReferenceData(BSession session, PeriodeServiceApgType periode, String genreService) throws Exception {
        IAPReferenceDataPrestation ref;

        if (session.getCode(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE).equals(genreService)) {
            ref = APReferenceDataParser.loadReferenceData(session, "MATERNITE", JADate.newDateFromAMJ(String.valueOf(periode.getDebut())),
                    JADate.newDateFromAMJ(String.valueOf(periode.getFin())), JADate.newDateFromAMJ(String.valueOf(periode.getFin())));
        } else {
            ref = APReferenceDataParser.loadReferenceData(session, "APG", JADate.newDateFromAMJ(String.valueOf(periode.getDebut())),
                    JADate.newDateFromAMJ(String.valueOf(periode.getFin())), JADate.newDateFromAMJ(String.valueOf(periode.getFin())));
        }

        return ref;
    }

    private List<APRepartitionPaiementAcor> createRepartitionPaiementsDecomptes(BSession session, APBaseCalcul baseCalcul, FCalcul fCalcul, VersementMoisComptableApgType moisComptable) throws JAException {
        List<APRepartitionPaiementAcor> repartitions = new ArrayList<>();

        for (VersementMoisComptableApgType versement:
             fCalcul.getVersementMoisComptable()) {
            for (VersementApgType versementApg:
                 versement.getVersement()) {
                VersementBeneficiaireApgType versementBeneficiaire = versementApg.getVersementsFederal().getVersementEmployeur();
                for (DecompteApgType decompteApgType :
                        versementBeneficiaire.getDecompte()) {
                    for (PeriodeDecompteApgType periodeDecompte :
                            decompteApgType.getPeriodeDecompte()) {
                        if (checkPeriodesDansLeMemeMois(moisComptable.getMoisComptable(), periodeDecompte.getDebut())) {
                            APRepartitionPaiementAcor repartition = createRepartitionPaiement(session, baseCalcul, versementBeneficiaire, periodeDecompte, fCalcul);
                            if(repartition != null){
                                repartitions.add(repartition);
                            }
                        }
                    }
                }
            }
        }
        return repartitions;
    }

    private List<APPrestationWrapper> createWrappers(BSession session, List<APPrestationAcor> prestations, APDroitLAPG droit, List basesCalcul) throws PRACORException {
        List<APPrestationWrapper> wrappers = new ArrayList<>();
        for (APPrestationAcor prestation:
             prestations) {
            APPrestationWrapper prestationWrapper = new APPrestationWrapper();
            prestationWrapper.setFraisGarde(prestation.getFraisDegarde());
            prestationWrapper.setIdDroit(droit.getIdDroit());
            APPeriodeWrapper periodeWrapper = new APPeriodeWrapper();
            periodeWrapper.setDateDebut(Dates.toJADate(prestation.getDateDebut()));
            periodeWrapper.setDateFin(Dates.toJADate(prestation.getDateFin()));
            prestationWrapper.setPeriodeBaseCalcul(periodeWrapper);
            APResultatCalcul rc = new APResultatCalcul();
            rc.setDateDebut(periodeWrapper.getDateDebut());
            rc.setDateFin(periodeWrapper.getDateDebut());
            rc.setSoumisImpotSource(prestation.isSoumisImpotSource());
            rc.setIdTauxImposition(prestation.getIdTauxImposition());
            rc.setTauxImposition(prestation.getTauxImposition());
            rc.setTypeAllocation(prestation.getGenre());
            rc.setVersementAssure(prestation.getVersementAssure());
            rc.setRevision(prestation.getRevision());
            rc.setAllocationJournaliereExploitation(prestation.getAllocationExploitation());
            rc.setAllocationJournaliereMaxFraisGarde(prestation.getFraisDegardeMax());
            rc.setMontantJournalier(prestation.getAllocationJournalier());
            rc.setBasicDailyAmount(prestation.getAllocationJournalier());
            rc.setNombreJoursSoldes(prestation.getNombreJoursSoldes());
            rc.setNombreJoursSupplementaires(prestation.getNombreJoursSupplementaires());
            rc.setRevenuDeterminantMoyen(prestation.getRevenuDeterminantMoyen());
            prestationWrapper.setPrestationBase(rc);
            setResultatCalculSituationProfessionnelWithAPPrestationAcor(prestation, rc);
            setResultatCalculSituationProfessionnelWithBaseCalcul(session, basesCalcul, prestation, rc);
            prestationWrapper.setPrestationBase(rc);
            wrappers.add(prestationWrapper);
        }
        return wrappers;
    }

    private void setResultatCalculSituationProfessionnelWithBaseCalcul(BSession session, List basesCalcul, APPrestationAcor prestation, APResultatCalcul rc) throws PRACORException {
        APBaseCalcul baseCalcul = findBaseCalcul(session, basesCalcul, Dates.toJADate(prestation.getDateDebut()),
                Dates.toJADate(prestation.getDateFin()));
        if(baseCalcul != null) {
            for (Object o : baseCalcul.getBasesCalculSituationProfessionnel()) {
                APBaseCalculSituationProfessionnel bsp = (APBaseCalculSituationProfessionnel) o;

                List<APResultatCalculSituationProfessionnel> lrcsp = rc.getResultatsCalculsSitProfessionnelle();

                boolean found = false;
                for (APResultatCalculSituationProfessionnel rcsp : lrcsp) {
                    String nomRCSP = rcsp.getNom();
                    String nomBSP = bsp.getNom();
                    try {

                        if (nomBSP.startsWith(" ")) {
                            while (nomBSP.startsWith(" ")) {
                                nomBSP = nomBSP.substring(1, nomBSP.length() - 1);
                            }
                        }

                        if (nomRCSP.startsWith(" ")) {
                            while (nomRCSP.startsWith(" ")) {
                                nomRCSP = nomRCSP.substring(1, nomRCSP.length() - 1);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        nomRCSP = rcsp.getNom();
                        nomBSP = bsp.getNom();
                    }

                    /*
                     * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '['
                     * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caractères valide,
                     * donc les chiffres, le '+' dit que çà doit correspondre au moins une fois) et qui est
                     * suivit d'un ']' Si je n'est pas été assez claire :
                     * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
                     */
                    nomRCSP = nomRCSP.replaceFirst("^\\[[0-9]+\\]", "");
                    if (rcsp.getIdTiers().equals(bsp.getIdTiers())
                            && (rcsp.getIdAffilie().equals(bsp.getIdAffilie()) || ((rcsp.getNoAffilie() != null) && rcsp
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

    private void setResultatCalculSituationProfessionnelWithAPPrestationAcor(APPrestationAcor prestation, APResultatCalcul rc) {
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
                        String nomEmployeur = employeur.getNom().replaceFirst("^\\[[0-9]+\\]", "");
                        String nomSitProEmployeur = sitPro.getNom().replaceFirst("^\\[[0-9]+\\]", "");
                        String idAffilie = "";
                        String idTiers = "";
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
                            if ((sitPro.getSalaireJournalierNonArrondi() == null)
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

    private boolean checkPeriodesDansLeMemeMois(int dateDebutPeriode1, int dateDebutPeriode2) throws JAException {
        LocalDate datePeriode1 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateDebutPeriode1)));
        LocalDate datePeriode2 = Dates.toDate(JADate.newDateFromAMJ(String.valueOf(dateDebutPeriode2)));
        return datePeriode1.getMonth().equals(datePeriode2.getMonth());
    }

    private FWCurrency getFraisDeGarde(final APDroitLAPG droit) throws Exception {
        APDroitAPG droitAPG = entityService.load(APDroitAPG.class, droit.getIdDroit());
        return new FWCurrency(droitAPG.loadSituationFamilliale().getFraisGarde());
    }

    private List<APBaseCalcul> getBasesCalcul(final BSession session, final APDroitLAPG droit) throws Exception {
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

    private static APBaseCalcul findBaseCalcul(BSession session, List<APBaseCalcul> basesCalcul, JADate dateDebutPeriodeAcor, JADate dateFinPeriodeAcor)
            throws PRACORException {
        APBaseCalcul retValue = null;

        for (APBaseCalcul baseCalcul : basesCalcul) {
            retValue = baseCalcul;

            try {
                if ((BSessionUtil.compareDateFirstLowerOrEqual(session, retValue.getDateDebut().toString(),
                        dateDebutPeriodeAcor.toString()))
                        && (BSessionUtil.compareDateFirstGreaterOrEqual(session, retValue.getDateFin().toString(),
                        dateFinPeriodeAcor.toString()))) {
                    break; // sortir de la boucle
                }
            } catch (Exception e) {
                throw new PRACORException("comparaison de dates impossibles", e);
            }

            retValue = null; // on a pas trouve donc on va retourner null et non
            // pas la derniere base de calcul
        }

        if(retValue==null && basesCalcul.stream().anyMatch(APBaseCalcul::isExtension)){
            for (APBaseCalcul baseCalcul : basesCalcul) {
                retValue = baseCalcul;

                try {
                    if (BSessionUtil.compareDateFirstLowerOrEqual(session, dateDebutPeriodeAcor.toString(), retValue.getDateDebut().toString())
                            && (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateFinPeriodeAcor.toString(), retValue.getDateFin().toString()))) {
                        break; // sortir de la boucle
                    }
                } catch (Exception e) {
                    throw new PRACORException("comparaison de dates impossibles", e);
                }

                retValue = null; // on a pas trouve donc on va retourner null et non
                // pas la derniere base de calcul

            }
        }

        return retValue;
    }

    private static APBaseCalculSituationProfessionnel findBaseCalculSitPro(APBaseCalcul basesCalcul,
                                                                           String idTiers, String idAffilie, String nomAffilie)
            throws PRACORException {
        if (basesCalcul == null) {
            throw new PRACORException("La base de calcul est null et la situation professionelle ne peut être trouvée !!!");
        }

        for (Object o : basesCalcul.getBasesCalculSituationProfessionnel()) {
            APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) o;

            String nomSP = bcSitPro.getNom();
            String nomAff = nomAffilie;
            try {

                if (nomSP.startsWith(" ")) {
                    while (nomSP.startsWith(" ")) {
                        nomSP = nomSP.substring(1, nomSP.length() - 1);
                    }
                }

                if (nomAff.startsWith(" ")) {
                    while (nomAff.startsWith(" ")) {
                        nomAff = nomAff.substring(1, nomAff.length() - 1);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                nomSP = bcSitPro.getNom();
                nomAff = nomAffilie;
            }

            /*
             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '[' suivi d'un minimum
             * de un ou de plusieurs chiffres ([0-9] signifie les caractères valide, donc les chiffres, le '+' dit que
             * çà doit correspondre au moins une fois) et qui est suivit d'un ']' Si je n'est pas été assez claire :
             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
             */
            nomAff = nomAff.replaceFirst("^\\[[0-9]+\\]", "");
            if (idTiers.equals(bcSitPro.getIdTiers()) && idAffilie.equals(bcSitPro.getIdAffilie())
                    && ((nomSP.contains(nomAff)) || nomAff.contains(nomSP))) {
                return bcSitPro;
            }

        }

        throw new PRACORException("La situation professionelle ne peut être trouvée !!!");
    }

    private static APBaseCalculSituationProfessionnel findBaseCalculSitProParNoAffilie(APBaseCalcul basesCalcul, String idTiers, String noAffilie,
                                                                                       String nomAffilie, List<String> idsSP) throws PRACORException {
        if (basesCalcul == null) {
            throw new PRACORException("La base de calcul est null et la situation professionelle ne peut être trouvée par no affilié !!!");
        }

        for (Iterator iter = basesCalcul.getBasesCalculSituationProfessionnel().iterator(); iter.hasNext();) {
            APBaseCalculSituationProfessionnel bcSitPro = (APBaseCalculSituationProfessionnel) iter.next();

            String nomSP = bcSitPro.getNom();
            String nomAff = nomAffilie;
            try {

                if (nomSP.startsWith(" ")) {
                    while (nomSP.startsWith(" ")) {
                        nomSP = nomSP.substring(1, nomSP.length() - 1);
                    }
                }

                if (nomAff.startsWith(" ")) {
                    while (nomAff.startsWith(" ")) {
                        nomAff = nomAff.substring(1, nomAff.length() - 1);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                nomSP = bcSitPro.getNom();
                nomAff = nomAffilie;
            }

            /*
             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '[' suivi d'un minimum
             * de un ou de plusieurs chiffres ([0-9] signifie les caractères valide, donc les chiffres, le '+' dit que
             * çà doit correspondre au moins une fois) et qui est suivit d'un ']' Si je n'est pas été assez claire :
             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
             */
            nomAff = nomAff.replaceFirst("^\\[[0-9]+\\]", "");
            if (idTiers.equals(bcSitPro.getIdTiers()) && noAffilie.equals(bcSitPro.getNoAffilie())
                    && (nomSP.contains(nomAff) || nomAff.contains(nomSP))
                    && !idsSP.contains(bcSitPro.getIdSituationProfessionnelle())) {

                // ACOR peut rajouter des (1) a la fin des noms si plusieurs fois le même nom dans la liste des
                // employeurs...
                return bcSitPro;
            }
        }

        throw new PRACORException("la situation professionelle ne peut être trouvée par no affilié !!!");
    }
}
