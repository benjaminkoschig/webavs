package ch.globaz.pegasus.business.constantes;

public enum EPCLienParentePA {

    LIEN_AUTRES("64033011"),
    LIEN_BEAU_PERE("64033007"),
    LIEN_BELLE_MERE("64033008"),
    LIEN_EPOUSE("64033004"),
    LIEN_EX_EPOUSE("64033003"),
    LIEN_EX_MARI("64033001"),
    LIEN_FILLE("64033010"),
    LIEN_FILS("64033009"),
    LIEN_MARI("64033002"),
    LIEN_MERE("64033006"),
    LIEN_PERE("64033005");

    private String lien = null;

    private EPCLienParentePA(String lienParente) {
        lien = lienParente;
    }

    public String getCsLienParente() {
        return lien;
    }

}
