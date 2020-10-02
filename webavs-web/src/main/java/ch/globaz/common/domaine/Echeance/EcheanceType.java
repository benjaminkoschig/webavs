package ch.globaz.common.domaine.Echeance;

public enum EcheanceType implements CodeSysteme<EcheanceType> {

    DEMANDE("52205001"),
    RENTE_AVS_AI("52205002"),
    IJAI("52205003"),
    AUTRE_RENTE("52205004"),
    ASSURANCE_VIE("52205005"),
    PRET_ENVERS_TIERS("52205006"),
    ALLOCATION_FAMILIALE("52205007"),
    AUTRE_REVENU("52205008"),
    COTISATION_PSAL("52205009"),
    PENSION_ALIMENTAIRE("52205010"),
    REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE("52205011"),
    REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE("52205012"),
    REVENU_HYPOTHETIQUE("52205013"),
    /**
     * BIEN_IMMOBILIER_HABITATION_PRINCIPALE
     */
    BIEN_IMMOBILIER_HABITATION_PRINCIPALE("52205014"),
    FRAIS_GARDE("52205015");

    private String id;

    EcheanceType(String id) {
        this.id = id;
    }

    @Override
    public String getCodeSysteme() {
        return id;
    }

}
