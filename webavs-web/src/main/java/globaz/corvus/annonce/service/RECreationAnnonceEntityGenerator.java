package globaz.corvus.annonce.service;

import globaz.corvus.annonce.REAnnoncesAPersister;
import globaz.corvus.annonce.domain.REAnnonce1A;
import globaz.corvus.annonce.domain.REAnnonce2A;
import globaz.corvus.annonce.domain.REAnnonce3A;
import globaz.corvus.annonce.domain.REAnnonce3B;
import globaz.corvus.annonce.domain.REEnteteAnnonce;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle10EmeRevision;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle9EmeRevision;
import globaz.corvus.annonce.formatter.REAbstractCreationAnnonceFormatter;
import globaz.corvus.annonce.formatter.RECreationAnnonce10EmeRevisionFormatter;
import globaz.corvus.annonce.formatter.RECreationAnnonce9EmeRevisionFormatter;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel3A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel3B;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;

public class RECreationAnnonceEntityGenerator {

    private static final String CODE_ENREGISTREMENT_01 = "01";
    private static final String CODE_ENREGISTREMENT_02 = "02";

    public REAnnoncesAPersister create(REAnnoncePonctuelle10EmeRevision annonceACreer) {

        REAnnoncesAugmentationModification10Eme annonce01 = new REAnnoncesAugmentationModification10Eme();
        REAnnoncesAugmentationModification10Eme annonce02 = new REAnnoncesAugmentationModification10Eme();
        REAnnoncesAPersister annoncesAPersister = new REAnnoncesAPersister(annonce01, annonce02);

        RECreationAnnonce10EmeRevisionFormatter formatter = new RECreationAnnonce10EmeRevisionFormatter();
        formatREAnnoncesAbstractLevel3A(annonceACreer, annoncesAPersister, formatter);

        // Formatage des valeurs REANN44
        String nouveauNoAssureAyantDroit = formatter.formatNouveauNoAssureAyantDroit(annonceACreer
                .getNouveauNoAssureAyantDroit());

        // Set des valeurs
        annonce01.setNouveauNoAssureAyantDroit(nouveauNoAssureAyantDroit);

        return annoncesAPersister;
    }

    public REAnnoncesAPersister create(REAnnoncePonctuelle9EmeRevision annonceACreer) {

        REAnnoncesAugmentationModification9Eme annonce01 = new REAnnoncesAugmentationModification9Eme();
        REAnnoncesAugmentationModification9Eme annonce02 = new REAnnoncesAugmentationModification9Eme();
        REAnnoncesAPersister annoncesAPersister = new REAnnoncesAPersister(annonce01, annonce02);

        RECreationAnnonce9EmeRevisionFormatter formatter = new RECreationAnnonce9EmeRevisionFormatter();
        formatREAnnoncesAbstractLevel3B(annonceACreer, annoncesAPersister, formatter);

        // Formatage des valeurs REANN41
        String montantRenteOrdinaireRemplace = formatter.formatMontantRenteOrdinaireRemplace(annonceACreer
                .getMontantRenteOrdinaireRemplace());
        String nouveauNoAssureAyantDroit = formatter.formatNouveauNoAssureAyantDroit(annonceACreer
                .getNouveauNoAssureAyantDroit());

        // Set des valeurs
        annonce01.setNouveauNoAssureAyantDroit(nouveauNoAssureAyantDroit);
        annonce01.setMensualiteRenteOrdRemp(montantRenteOrdinaireRemplace);

        return annoncesAPersister;
    }

    private void formatREAnnoncesAbstractLevel3B(final REAnnonce3B donneesAnnonce,
            final REAnnoncesAPersister annoncesAPersister, RECreationAnnonce9EmeRevisionFormatter formatter) {
        formatREAnnoncesAbstractLevel2A(donneesAnnonce, annoncesAPersister, formatter);

        // Formatage des valeurs
        String revenuPrisEnCompte = formatter.formatRevenuPrisEnCompte(donneesAnnonce.getRevenuPrisEnCompte());
        String isLimiteRevenu = formatter.formatIsLimiteRevenu(donneesAnnonce.getIsLimiteRevenu());
        String isMinimumGaranti = formatter.formatIsMinimumGaranti(donneesAnnonce.getIsMinimumGaranti());
        String revenuAnnuelMoyenSansBTE = formatter.formatRevenuAnnuelMoyenSansBTE(donneesAnnonce
                .getRevenuAnnuelMoyenSansBTE());

        // FIXME
        // String bteMoyenPrisEnCompte = formatter
        // .formatBteMoyennePrisEnCompte(donneesAnnonce.getBteMoyennePrisEnCompte());

        // Set des valeurs

        REAnnoncesAbstractLevel3B annonce02 = (REAnnoncesAbstractLevel3B) annoncesAPersister.getAnnonce02();

        annonce02.setRevenuPrisEnCompte(revenuPrisEnCompte);
        annonce02.setIsLimiteRevenu(isLimiteRevenu);
        annonce02.setIsMinimumGaranti(isMinimumGaranti);
        annonce02.setRevenuAnnuelMoyenSansBTE(revenuAnnuelMoyenSansBTE);
        annonce02.setBteMoyennePrisEnCompte(formatter.formatBteMoyennePrisEnCompte(donneesAnnonce
                .getBteMoyennePrisEnCompte()));
    }

    /**
     * @param errors
     * @param annonce
     */
    private void formatREAnnoncesAbstractLevel3A(final REAnnonce3A donneesAnnonce,
            final REAnnoncesAPersister annoncesAPersister, RECreationAnnonce10EmeRevisionFormatter formatter) {
        formatREAnnoncesAbstractLevel2A(donneesAnnonce, annoncesAPersister, formatter);

        // Formatage des valeurs
        String codeRevenuSplitte = formatter.formatCodeRevenuSplitte(donneesAnnonce.getCodeRevenuSplitte());
        String dateDebutAnticipation = formatter.formatDateDebutAnticipation(donneesAnnonce.getDateDebutAnticipation());
        String isSurvivant = formatter.formatIsSurvivant(donneesAnnonce.isSurvivant());

        String nombreAnneeAnticipation = formatter.formatNombreAnneeAnticipation(donneesAnnonce
                .getNbreAnneeAnticipation());

        String nombreAnneeBonifTrans = formatter.formatNombreAnneeBonifTrans(
                donneesAnnonce.getNbreAnneeBonifTrans_nombreAnnee(),
                donneesAnnonce.getNbreAnneeBonifTrans_isDemiAnnee());

        String nombreAnneeBTA = formatter.formatNombreAnneeBTA(donneesAnnonce.getBTA_valeurEntiere(),
                donneesAnnonce.getBTA_valeurDecimal());

        String reductionAnticipation = formatter.formatReductionAnticipation(donneesAnnonce.getReductionAnticipation());

        // Set des valeurs
        REAnnoncesAbstractLevel3A annonce02 = (REAnnoncesAbstractLevel3A) annoncesAPersister.getAnnonce02();

        annonce02.setCodeRevenuSplitte(codeRevenuSplitte);
        annonce02.setDateDebutAnticipation(dateDebutAnticipation);
        annonce02.setIsSurvivant(isSurvivant);
        annonce02.setNbreAnneeAnticipation(nombreAnneeAnticipation);
        annonce02.setNbreAnneeBonifTrans(nombreAnneeBonifTrans);
        annonce02.setNbreAnneeBTA(nombreAnneeBTA);
        annonce02.setReductionAnticipation(reductionAnticipation);
    }

    /**
     * @param errors
     * @param annonce
     */
    private void formatREAnnoncesAbstractLevel2A(final REAnnonce2A donneesAnnonce,
            final REAnnoncesAPersister annoncesAPersister, REAbstractCreationAnnonceFormatter formatter) {

        formatREAnnoncesAbstractLevel1A(donneesAnnonce, annoncesAPersister, formatter);

        // Formatage des valeurs
        String ageDebutInvalidite = formatter.formatAgeDebutInvalidite(donneesAnnonce.getAgeDebutInvalidite());
        String anneeCotClasseAge = formatter.formatAnneeCotClasseAge(donneesAnnonce.getAnneeCotClasseAge());
        String anneeNiveau = formatter.formatAnneeNiveau(donneesAnnonce.getAnneeNiveau());
        String ccs1 = formatter.formatCodeCasSpecial(donneesAnnonce.getCasSpecial1());
        String ccs2 = formatter.formatCodeCasSpecial(donneesAnnonce.getCasSpecial2());
        String ccs3 = formatter.formatCodeCasSpecial(donneesAnnonce.getCasSpecial3());
        String ccs4 = formatter.formatCodeCasSpecial(donneesAnnonce.getCasSpecial4());
        String ccs5 = formatter.formatCodeCasSpecial(donneesAnnonce.getCasSpecial5());
        String codeInfirmite = formatter.formatCodeInfirmite(donneesAnnonce.getCodeInfirmite(),
                donneesAnnonce.getCodeAtteinteFonctionnelle());
        String dateRevocationAjournement = formatter.formatDateRevocationAjournement(donneesAnnonce
                .getDateRevocationAjournement());
        String degreInvalidite = formatter.formatDegreInvalidite(donneesAnnonce.getDegreInvalidite());
        String dureeAjournement = formatter.formatDureeAjournement(donneesAnnonce.getDureeAjournementValeurEntiere(),
                donneesAnnonce.getDureeAjournementValeurDecimal());

        String dureeCoEchelleRenteAv73 = formatter.formatDureeCoEchelleRenteAv73(
                donneesAnnonce.getDureeCoEchelleRenteAv73_nombreAnnee(),
                donneesAnnonce.getDureeCoEchelleRenteAv73_nombreMois());

        String dureeCoEchelleRenteDes73 = formatter.formatDureeCoEchelleRenteDes73(
                donneesAnnonce.getDureeCoEchelleRenteDes73_nombreAnnee(),
                donneesAnnonce.getDureeCoEchelleRenteDes73_nombreMois());

        String dureeCotManquante48_72 = formatter.formatDureeCotManquante48_72(donneesAnnonce
                .getDureeCotManquante48_72());

        String dureeCotManquante73_78 = formatter.formatDureeCotManquante73_78(donneesAnnonce
                .getDureeCotManquante73_78());

        String dureeCotPourDetRAM = formatter.formatDureeCotPourDetRAM(
                donneesAnnonce.getDureeCotPourDetRAM_nombreAnnee(), donneesAnnonce.getDureeCotPourDetRAM_nombreMois());
        String echelleRente = formatter.formatEchelleRente(donneesAnnonce.getEchelleRente());
        String genreDroitAPI = formatter.formatGenreDroitAPI(donneesAnnonce.getGenreDroitAPI());
        String nombreAnneeBTE = formatter.formatNombreAnneeBTE(donneesAnnonce.getNombreAnneeBTE_valeurEntiere(),
                donneesAnnonce.getNombreAnneeBTE_valeurDecimal());
        String officeAICompetent = formatter.formatOfficeAICompetent(donneesAnnonce.getOfficeAICompetent());
        String ramDeterminant = formatter.formatRamDeterminant(donneesAnnonce.getRamDeterminant());
        String reduction = formatter.formatReduction(donneesAnnonce.getReduction());
        String supplementAjournement = formatter.formatSupplementAjournement(donneesAnnonce.getSupplementAjournement());
        String survenanceEvenAssure = formatter.formatSurvenanceEvenAssure(donneesAnnonce.getSurvenanceEvenAssure());

        // Set des valeurs
        REAnnoncesAbstractLevel2A annonce02 = (REAnnoncesAbstractLevel2A) annoncesAPersister.getAnnonce02();

        annonce02.setAgeDebutInvalidite(ageDebutInvalidite);
        annonce02.setAnneeCotClasseAge(anneeCotClasseAge);
        annonce02.setAnneeNiveau(anneeNiveau);
        annonce02.setCasSpecial1(ccs1);
        annonce02.setCasSpecial2(ccs2);
        annonce02.setCasSpecial3(ccs3);
        annonce02.setCasSpecial4(ccs4);
        annonce02.setCasSpecial5(ccs5);
        annonce02.setCodeInfirmite(codeInfirmite);
        annonce02.setDateRevocationAjournement(dateRevocationAjournement);
        annonce02.setDegreInvalidite(degreInvalidite);
        annonce02.setDureeAjournement(dureeAjournement);
        annonce02.setDureeCoEchelleRenteAv73(dureeCoEchelleRenteAv73);
        annonce02.setDureeCoEchelleRenteDes73(dureeCoEchelleRenteDes73);
        annonce02.setDureeCotManquante48_72(dureeCotManquante48_72);
        annonce02.setDureeCotManquante73_78(dureeCotManquante73_78);
        annonce02.setDureeCotPourDetRAM(dureeCotPourDetRAM);
        annonce02.setEchelleRente(echelleRente);
        annonce02.setGenreDroitAPI(genreDroitAPI);
        annonce02.setNombreAnneeBTE(nombreAnneeBTE);
        annonce02.setOfficeAICompetent(officeAICompetent);
        annonce02.setRamDeterminant(ramDeterminant);
        annonce02.setReduction(reduction);
        annonce02.setSupplementAjournement(supplementAjournement);
        annonce02.setSurvenanceEvenAssure(survenanceEvenAssure);
    }

    /**
     * Appel formatREAnnonceHeader(annonce)
     * Format l'ensemble des champs appartenant à REAnnoncesAbstractLevel1A
     * 
     * @param errors
     * 
     * @param annonce L'annonce à formatter
     */
    private void formatREAnnoncesAbstractLevel1A(final REAnnonce1A donneesAnnonce,
            final REAnnoncesAPersister annoncesAPersister, REAbstractCreationAnnonceFormatter formatter) {
        formatREAnnonceHeader(donneesAnnonce, annoncesAPersister, formatter);

        // Formatage des valeurs
        String cantonEtatDomicile = formatter.formatCantonEtatDomicile(donneesAnnonce.getCantonEtatDomicile());
        String codeMutation = formatter.formatCodeMutation(donneesAnnonce.getCodeMutation());
        String debutDroit = formatter.formatDebutDroit(donneesAnnonce.getDebutDroit());
        String etatCivil = formatter.formatEtatCivil(donneesAnnonce.getEtatCivil());
        String finDroit = formatter.formatFinDroit(donneesAnnonce.getFinDroit());
        String genrePrestation = formatter.formatGenrePrestation(donneesAnnonce.getGenrePrestation());
        String idRenteAccordee = formatter.formatIdRenteAccordee(donneesAnnonce.getIdRenteAccordee());
        String idTiers = formatter.formatIdTiers(donneesAnnonce.getIdTiers());
        String isRefugie = formatter.formatIsRefugie(donneesAnnonce.isRefugie());

        String mensualitePrestationsFrancs = formatter.formatMensualitePrestationsFrancs(donneesAnnonce
                .getMensualitePrestationsFrancs());
        String moisRapport = formatter.formatMoisRapport(donneesAnnonce.getMoisRapport());
        String noAssAyantDroit = formatter.formatNoAssAyantDroit(donneesAnnonce.getNoAssAyantDroit());
        String numeroAnnonce = formatter.formatNumeroAnnonce(donneesAnnonce.getNumeroAnnonce());
        String premierNoAssComplementaire = formatter.formatPremierNoAssComplementaire(donneesAnnonce
                .getPremierNoAssComplementaire());
        String referenceCaisseInterne = formatter.formatReferenceCaisseInterne(
                donneesAnnonce.getPrefixPourReferenceInterneCaisseProvider(),
                donneesAnnonce.getUtilisateurPourReferenceCaisseInterne());
        String secondNoAssComplementaire = formatter.formatSecondNoAssComplementaire(donneesAnnonce
                .getSecondNoAssComplementaire());

        // Set des valeurs
        REAnnoncesAbstractLevel1A annonce01 = (REAnnoncesAbstractLevel1A) annoncesAPersister.getAnnonce01();
        REAnnoncesAbstractLevel1A annonce02 = (REAnnoncesAbstractLevel1A) annoncesAPersister.getAnnonce02();

        annonce01.setCantonEtatDomicile(cantonEtatDomicile);
        annonce01.setCodeMutation(codeMutation);
        annonce01.setDebutDroit(debutDroit);
        annonce01.setEtatCivil(etatCivil);
        annonce01.setFinDroit(finDroit);
        annonce01.setGenrePrestation(genrePrestation);
        annonce01.setIdTiers(idTiers);
        annonce01.setIsRefugie(isRefugie);
        annonce01.setMensualitePrestationsFrancs(mensualitePrestationsFrancs);
        annonce01.setNoAssAyantDroit(noAssAyantDroit);
        annonce01.setPremierNoAssComplementaire(premierNoAssComplementaire);
        annonce01.setSecondNoAssComplementaire(secondNoAssComplementaire);
        annonce01.setReferenceCaisseInterne(referenceCaisseInterne);
        annonce01.setNumeroAnnonce(numeroAnnonce);
        annonce01.setMoisRapport(moisRapport);

        annonce02.setIdTiers(idTiers);

    }

    /**
     * @param errors
     * @param annonce
     */
    private void formatREAnnonceHeader(final REEnteteAnnonce donneesAnnonce, REAnnoncesAPersister annoncesAPersister,
            REAbstractCreationAnnonceFormatter formatter) {
        String codeApplication = formatter.formatCodeApplication(donneesAnnonce.getCodeApplicationProvider()
                .getCodeApplication());
        // Formatage des valeurs
        String etat = donneesAnnonce.getEtat().getCodeSystemEtatAnnonceAsString();
        String numeroAgence = formatter.formatNumeroAgence(donneesAnnonce.getNumeroAgence());
        String numeroCaisse = formatter.formatNumeroCaisse(donneesAnnonce.getNumeroCaisse());

        // Set des valeurs
        REAnnonceHeader annonce01 = annoncesAPersister.getAnnonce01();
        REAnnonceHeader annonce02 = annoncesAPersister.getAnnonce02();

        annonce01.setCodeApplication(codeApplication);
        annonce01.setCodeEnregistrement01(CODE_ENREGISTREMENT_01);
        annonce01.setEtat(etat);
        annonce01.setNumeroCaisse(numeroCaisse);
        annonce01.setNumeroAgence(numeroAgence);

        annonce02.setCodeApplication(codeApplication);
        annonce02.setCodeEnregistrement01(CODE_ENREGISTREMENT_02);
        annonce02.setEtat(etat);
    }

}
