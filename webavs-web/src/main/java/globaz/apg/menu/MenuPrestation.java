package globaz.apg.menu;

import globaz.apg.vb.droits.APTypePresationDemandeResolver;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.api.PRTypeDemande;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Le but de cette class est d'avoir la logique des menus répétitif pour la gestion des différentes allocation.
 */
@Getter
@ToString
@Builder
public class MenuPrestation {

    private static final String AP_OPTIONSEMPTY = "ap-optionsempty";
    private static final String AP_OPTIONLOT = "ap-optionlot";
    private static final Map<String, MenuPrestation> MENU_MAP = generateMap();
    private static final Map<String, String> MAIN_TITLE_MAP = generateMapForTitle();

    private final String menuIdPrincipal;
    private final String menuIdOptionDroit;
    private final String menuIdOptionsLot;
    private final String csTypePrestation;

    public static MenuPrestation of(final HttpSession httpSession) {
        return of(APTypePresationDemandeResolver.resolveTypePrestation(httpSession));
    }

    public static MenuPrestation of(final String csTypePrestation) {
        return MENU_MAP.get(csTypePrestation);
    }

    public static MenuPrestation of(final PRTypeDemande typeDemande) {
        return MENU_MAP.get(typeDemande.getCsType());
    }

    public String getMenuIdOptionsEmpty() {
        return AP_OPTIONSEMPTY;
    }

    public String getTitre() {
        return MAIN_TITLE_MAP.getOrDefault(this.csTypePrestation, "GESTION_APG");
    }

    private static Map<String, String> generateMapForTitle() {
        Map<String, String> map = new HashMap<>();
        map.put(IPRDemande.CS_TYPE_APG, "GESTION_APG");
        map.put(IPRDemande.CS_TYPE_MATERNITE, "GESTION_APG");
        map.put(IPRDemande.CS_TYPE_PANDEMIE, "GESTION_APG");
        map.put(IPRDemande.CS_TYPE_PATERNITE, "GESTION_APG");
        map.put(IPRDemande.CS_TYPE_PROCHE_AIDANT, "GESTION_PROCHE_AIDANT");
        return map;
    }

    private static Map<String, MenuPrestation> generateMap() {

        Map<String, MenuPrestation> map = new HashMap<>();

        map.put(IPRDemande.CS_TYPE_APG, MenuPrestation.builder()
                                                      .menuIdPrincipal("ap-menuprincipalapg")
                                                      .menuIdOptionDroit("ap-optionmenudroitapg")
                                                      .menuIdOptionsLot(AP_OPTIONLOT)
                                                      .csTypePrestation(IPRDemande.CS_TYPE_APG).build());

        map.put(IPRDemande.CS_TYPE_MATERNITE, MenuPrestation.builder()
                                                            .menuIdPrincipal("ap-menuprincipalamat")
                                                            .menuIdOptionDroit("ap-optionmenudroitamat")
                                                            .menuIdOptionsLot(AP_OPTIONLOT)
                                                            .csTypePrestation(IPRDemande.CS_TYPE_MATERNITE).build());

        map.put(IPRDemande.CS_TYPE_PANDEMIE, MenuPrestation.builder()
                                                           .menuIdPrincipal("ap-menuprincipalpan")
                                                           .menuIdOptionDroit("ap-optionmenudroitpan")
                                                           .menuIdOptionsLot("ap-optionlotpan")
                                                           .csTypePrestation(IPRDemande.CS_TYPE_PANDEMIE).build());

        map.put(IPRDemande.CS_TYPE_PATERNITE, MenuPrestation.builder()
                                                            .menuIdPrincipal("ap-menuprincipalapat")
                                                            .menuIdOptionDroit("ap-menuprincipalapat")
                                                            .menuIdOptionsLot("ap-optionlotpat")
                                                            .csTypePrestation(IPRDemande.CS_TYPE_PATERNITE).build());

        map.put(IPRDemande.CS_TYPE_PROCHE_AIDANT, MenuPrestation.builder()
                                                                .menuIdPrincipal("ap-menuprincipalprai")
                                                                .menuIdOptionDroit("ap-optionmenudroitprai")
                                                                .menuIdOptionsLot(AP_OPTIONLOT)
                                                                .csTypePrestation(IPRDemande.CS_TYPE_PROCHE_AIDANT).build());
        return map;
    }
}
