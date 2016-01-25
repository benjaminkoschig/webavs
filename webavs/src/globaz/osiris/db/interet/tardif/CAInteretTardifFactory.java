package globaz.osiris.db.interet.tardif;

import globaz.osiris.api.APISection;

public abstract class CAInteretTardifFactory {

    private static int getIdTypeSectionAsInt(String idTypeSection) {
        return Integer.parseInt(idTypeSection);
    }

    public static CAInteretTardif getInteretTardif(String categorieSection) throws Exception {
        int type = Integer.parseInt(categorieSection);

        if ((type >= CAInteretTardifFactory.getIdTypeSectionAsInt(APISection.ID_CATEGORIE_SECTION_DECOMPTE_JANVIER))
                && (type <= CAInteretTardifFactory
                        .getIdTypeSectionAsInt(APISection.ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE))) {
            // Trimestriel et annuel 1-12
            return new CAInteretTardifPeriodique();
        } else if ((type >= CAInteretTardifFactory
                .getIdTypeSectionAsInt(APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE))
                && (type <= CAInteretTardifFactory
                        .getIdTypeSectionAsInt(APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE_3TRIMESTRE))) {
            // Annuel et trimestriel 40-47
            return new CAInteretTardifPeriodique();
        } else if ((type >= CAInteretTardifFactory
                .getIdTypeSectionAsInt(APISection.ID_CATEGORIE_SECTION_DECISION_1SEMESTRE))
                && (type <= CAInteretTardifFactory
                        .getIdTypeSectionAsInt(APISection.ID_CATEGORIE_SECTION_DECISION_2SEMESTRE))) {
            // 61-62
            return new CAInteretTardifPeriodique();
        } else if (categorieSection.equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS)
                || categorieSection.equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT)) {
            // Décompte 20 et 22
            return new CAInteretTardifAutreCotArrieree();
        } else if (categorieSection.equals(APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL)
                || categorieSection.equals(APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE)
                || categorieSection.equals(APISection.ID_CATEGORIE_SECTION_LTN)
                || categorieSection.equals(APISection.ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE)) {
            // Décompte 13, 14, 33, 38
            return new CAInteretTardifAutreCotArrieree();
        } else if (categorieSection.equals(APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR)
                || categorieSection.equals(APISection.ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE)
                || categorieSection.equals(APISection.ID_CATEGORIE_SECTION_DECOMPTE_SALAIRES_DIFFERES)) {
            // Décompte 17, 18, 36
            return new CAInteretTardifAutreCotArrieree();
        } else if (categorieSection.equals(APISection.ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE)) {
            // Décompte 30
            return new CAInteretTardifAutreCotArrieree();
        } else {
            return null;
        }
    }
}
