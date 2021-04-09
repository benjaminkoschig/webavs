package globaz.apg.menu;

import globaz.apg.vb.droits.APTypePresationDemandeResolver;
import globaz.prestation.api.IPRDemande;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Le but de cette class est d'avoir la logique des menus répétitif pour la gestion des différentes allocation.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class MenuPrestation {

    private static final String AP_OPTIONSEMPTY = "ap-optionsempty";
    private static final String AP_OPTIONLOT = "ap-optionlot";
    private static final Map<String, MenuPrestation> MENU_MAP = generateMap();
    private static final  Map<String, String> MAIN_TITLE_MAP = generateMapForTitle();

    private final String menuIdPrincipal;
    private final String menuIdOptionsLot;
    private final String csTypePrestation;

    public static MenuPrestation of(final HttpSession httpSession) {
        return of(APTypePresationDemandeResolver.resolveTypePrestation(httpSession));
    }

    public static MenuPrestation of(final String csTypePrestation) {
        return MENU_MAP.get(csTypePrestation);
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
        map.put(IPRDemande.CS_TYPE_APG, new MenuPrestation("ap-menuprincipalapg", AP_OPTIONLOT, IPRDemande.CS_TYPE_APG));
        map.put(IPRDemande.CS_TYPE_MATERNITE, new MenuPrestation("ap-menuprincipalamat", AP_OPTIONLOT, IPRDemande.CS_TYPE_MATERNITE));
        map.put(IPRDemande.CS_TYPE_PANDEMIE, new MenuPrestation("ap-menuprincipalpan", "ap-optionlotpan", IPRDemande.CS_TYPE_PANDEMIE));
        map.put(IPRDemande.CS_TYPE_PATERNITE, new MenuPrestation("ap-menuprincipalapat", "ap-menuprincipalapat", IPRDemande.CS_TYPE_PATERNITE));
        map.put(IPRDemande.CS_TYPE_PROCHE_AIDANT, new MenuPrestation("ap-menuprincipalprai", AP_OPTIONLOT, IPRDemande.CS_TYPE_PROCHE_AIDANT));
        return map;
    }
}
