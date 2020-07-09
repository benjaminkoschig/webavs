package globaz.apg.enums;

import globaz.apg.api.droits.IAPDroitLAPG;

import java.util.Arrays;

public enum APGenreServiceAPG {

    CoursMoniteursJeunesTireurs("50", IAPDroitLAPG.CS_COURS_MONITEURS_JEUNES_TIREURS),
    Maternite("90", IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE),
    JeunesseEtSportFormationCadres("30", IAPDroitLAPG.CS_FORMATION_DE_CADRE_JEUNESSE_SPORTS),
    MilitaireEcoleDeRecrue("11", IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE),
    MilitairePaiementGallons("12", IAPDroitLAPG.CS_SERVICE_AVANCEMENT),
    MilitaireRecrutement("13", IAPDroitLAPG.CS_RECRUTEMENT),
    MilitaireServiceNormal("10", IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL),
    MilitaireSousOfficierEnServiceLong("14", IAPDroitLAPG.CS_SOF_EN_SERVICE_LONG),
    InterruptionAvantEcoleSousOfficier("15", IAPDroitLAPG.CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF),
    InterruptionPendantServiceAvancement("16", IAPDroitLAPG.CS_SERVICE_INTERRUPTION_PENDANT_SERVICE_AVANCEMENT),
    ProtectionCivileCadreSpecialiste("22", IAPDroitLAPG.CS_PROTECTION_CIVILE_CADRE_SPECIALISTE),
    ProtectionCivileCommandant("23", IAPDroitLAPG.CS_PROTECTION_CIVILE_COMMANDANT),
    ProtectionCivileFormationDeBase("21", IAPDroitLAPG.CS_FORMATION_DE_BASE),
    ProtectionCivileServiceNormale("20", IAPDroitLAPG.CS_PROTECTION_CIVILE_SERVICE_NORMAL),
    ServiceCivilNormal("40", IAPDroitLAPG.CS_SERVICE_CIVIL_SERVICE_NORMAL),
    ServiceCivilTauxRecrue("41", IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES),
    Demenagement("501", IAPDroitLAPG.CS_DEMENAGEMENT_CIAB),
    Naissance("502", IAPDroitLAPG.CS_NAISSANCE_CIAB),
    MariageLPart("503", IAPDroitLAPG.CS_MARIAGE_LPART_CIAB),
    Deces("504", IAPDroitLAPG.CS_DECES_CIAB),
    InspectionRecrutementLiberation("505", IAPDroitLAPG.CS_JOURNEES_DIVERSES_CIAB),
    CongeJeunesse("506", IAPDroitLAPG.CS_CONGE_JEUNESSE_CIAB),
    ServiceEtranger("507", IAPDroitLAPG.CS_SERVICE_ETRANGER_CIAB),
    DecesDemiJour("508", IAPDroitLAPG.CS_DECES_DEMI_JOUR_CIAB),
    GardeParentale("400", IAPDroitLAPG.CS_GARDE_PARENTALE),
    Quarantaine("401", IAPDroitLAPG.CS_QUARANTAINE),
    IndependantPandemie("402", IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE),
    IndependantPerteGains("403", IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS),
    GardeParentaleHandicap("404", IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP),
    IndependantManifAnnulee("405", IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE),
    SalairieEvenementiel("406", IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL);


    public static APGenreServiceAPG resoudreGenreParCodeSystem(final String codeSystem) {
        for (final APGenreServiceAPG genre : APGenreServiceAPG.values()) {
            if (genre.codeSysteme.equals(codeSystem)) {
                return genre;
            }
        }
        return null;
    }

    public static APGenreServiceAPG resoudreGenreParCodeAnnonce(final String codeAnnonce) {
        for (final APGenreServiceAPG genre : APGenreServiceAPG.values()) {
            if (genre.codePourAnnonce.equals(codeAnnonce)) {
                return genre;
            }
        }
        return null;
    }

    public static boolean isValidGenreService(String genreService) {
        if (genreService == null) {
            return false;
        }
        if(isValidGenreServicePandemie(genreService)){
            return false;
        }
        for (APGenreServiceAPG gs : APGenreServiceAPG.values()) {
            if (gs.getCodePourAnnonce().equals(genreService)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidGenreServicePandemie(String genreService) {
        if (genreService == null) {
            return false;
        }
        for (APGenreServiceAPG gs : Arrays.asList(APGenreServiceAPG.GardeParentale, APGenreServiceAPG.Quarantaine,  APGenreServiceAPG.IndependantPandemie,
                APGenreServiceAPG.IndependantPerteGains, APGenreServiceAPG.GardeParentaleHandicap, APGenreServiceAPG.IndependantManifAnnulee)) {
            if (gs.getCodePourAnnonce().equals(genreService)) {
                return true;
            }
        }
        return false;
    }

    private String codePourAnnonce;

    private String codeSysteme;

    private APGenreServiceAPG(String codePourAnnonce, String codeSysteme) {
        this.codePourAnnonce = codePourAnnonce;
        this.codeSysteme = codeSysteme;
    }

    public String getCodePourAnnonce() {
        return codePourAnnonce;
    }

    public String getCodeSysteme() {
        return codeSysteme;
    }
}
