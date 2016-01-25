package globaz.apg.enums;

import globaz.apg.api.droits.IAPDroitLAPG;

public enum APGenreServiceAPG {

    CoursMoniteursJeunesTireurs("50", IAPDroitLAPG.CS_COURS_MONITEURS_JEUNES_TIREURS),
    JeunesseEtSportFormationCadres("30", IAPDroitLAPG.CS_FORMATION_DE_CADRE_JEUNESSE_SPORTS),
    MilitaireEcoleDeRecrue("11", IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE),
    MilitairePaiementGallons("12", IAPDroitLAPG.CS_SERVICE_AVANCEMENT),
    MilitaireRecrutement("13", IAPDroitLAPG.CS_RECRUTEMENT),
    MilitaireServiceNormal("10", IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL),
    MilitaireSousOfficierEnServiceLong("14", IAPDroitLAPG.CS_SOF_EN_SERVICE_LONG),
    ProtectionCivileCadreSpecialiste("22", IAPDroitLAPG.CS_PROTECTION_CIVILE_CADRE_SPECIALISTE),
    ProtectionCivileCommandant("23", IAPDroitLAPG.CS_PROTECTION_CIVILE_COMMANDANT),
    ProtectionCivileFormationDeBase("21", IAPDroitLAPG.CS_FORMATION_DE_BASE),
    ProtectionCivileServiceNormale("20", IAPDroitLAPG.CS_PROTECTION_CIVILE_SERVICE_NORMAL),
    ServiceCivilNormal("40", IAPDroitLAPG.CS_SERVICE_CIVIL_SERVICE_NORMAL),
    ServiceCivilTauxRecrue("41", IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES);

    public static boolean isValidGenreService(String genreService) {
        if (genreService == null) {
            return false;
        }
        for (APGenreServiceAPG gs : APGenreServiceAPG.values()) {
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
