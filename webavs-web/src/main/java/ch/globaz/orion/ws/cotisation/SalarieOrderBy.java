package ch.globaz.orion.ws.cotisation;


public enum SalarieOrderBy {
    ORDER_BY_NOM_PRENOM("KALNOM"),
    ORDER_BY_NSS("KANAVS"),
    ORDER_BY_DATE_NAISSANCE("KADNAI");

    private String text;

    private SalarieOrderBy(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
