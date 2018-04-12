package ch.globaz.orion.db;


public enum EBDemandeModifAcompteManagerOrderBy {
    NUM_AFFILIE("ORDER_BY_NUM_AFFILIE"),
    NOM("ORDER_BY_NOM"),
    DATE_RECEPTION("ORDER_BY_DATE_RECEPTION");

    private EBDemandeModifAcompteManagerOrderBy(String label) {
        this.label = label;
    }

    public static EBDemandeModifAcompteManagerOrderBy fromLabel(String label) {
        if (label == null || label.isEmpty()) {
            return null;
        }

        for (EBDemandeModifAcompteManagerOrderBy enumOrder : EBDemandeModifAcompteManagerOrderBy.values()) {
            String val = enumOrder.label;
            if (val.equals(label)) {
                return enumOrder;
            }
        }
        throw new IllegalArgumentException("La valeur (" + label + ") ne correspond à aucune valeur connu");
    }

    private String label;

    public String getLabel() {
        return label;
    }
}
