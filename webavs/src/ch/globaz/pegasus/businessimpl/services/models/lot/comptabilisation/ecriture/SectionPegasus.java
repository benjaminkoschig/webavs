package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.osiris.api.APISection;

public enum SectionPegasus {

    // COMPENSATION(
    // APIReferenceRubrique.COMPENSATION_RENTES),
    DECISION_PC(APISection.ID_TYPE_SECTION_DECISION_PC, APISection.ID_CATEGORIE_SECTION_DECISION_PC),
    RESTIUTION(APISection.ID_TYPE_SECTION_RESTITUTION, APISection.ID_CATEGORIE_SECTION_RESTITUTIONS),

    BLOCAGE(APISection.ID_TYPE_SECTION_BLOCAGE, APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES);

    private String categorie;
    private String type;

    SectionPegasus(String type, String categorie) {
        this.type = type;
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getType() {
        return type;
    }

}
