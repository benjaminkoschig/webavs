package ch.globaz.pegasus.business.constantes;

/**
 * Enumération servant à gérer les id des menus et nodes, du fichier PegasusMenu.xml
 * 
 * @author SCE
 * 
 */
public enum EPCMenu {

    ALLOCATION_NOEL_NODE_ID("NODE_ALLOCATION_NOEL"),
    LISTE_REPARTITION_COMMUNE_POLITIQUE("NODE_LISTE_REPARTITION_COMMUNE_POLITIQUE"),
    PEGASUS_MENU_PRINCIPAL("pegasus-menuprincipal"),
    PROCESS_NODE_ID("MENU_PROCESS");

    private String property;

    EPCMenu(String prop) {
        property = prop;
    }

    public String getProperty() {
        return property;
    }
}
