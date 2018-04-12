package ch.globaz.orion.ws.comptabilite;

import globaz.osiris.db.comptes.CASection;

public enum FactureOrderBy {
    ORDER_BY_NO_FACTURE(CASection.FIELD_IDEXTERNE),
    ORDER_BY_DATE(CASection.FIELD_DATESECTION);

    private String text;

    private FactureOrderBy(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
