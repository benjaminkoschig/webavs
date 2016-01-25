package ch.globaz.amal.businessimpl.utils.parametres;

import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class ParametresAnnuelsProvider {
    private final static List<String> champsParametresAnnuels = Arrays.asList(new String[] {
            IAMParametresAnnuels.CS_IPS_LIMITE_COTI_AC, IAMParametresAnnuels.CS_IPS_LIMITE_COTI_AC_SUPPL,
            IAMParametresAnnuels.CS_IPS_LIMITE_DEDUC_FRAIS_OBTENTION,
            IAMParametresAnnuels.CS_IPS_LIMITE_DEDUCTION_ASSU, IAMParametresAnnuels.CS_IPS_LIMITE_PRIME_AANP,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_COTI_AC, IAMParametresAnnuels.CS_IPS_TX_CALCUL_COTI_AC_SUPPL,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_COTI_AVS_AI_APG,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_ASSU_ENFA,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_ASSU_JEUNE,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_DOUBLE_GAIN,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_FRAIS_OBTENTION,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_CELIB,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ENFANT_GREATER_2,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ENFANT_LESS_EQUAL_2,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_SUPPL_ENFANT,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_PRIME_AANP, IAMParametresAnnuels.CS_IPS_TX_CALCUL_PRIME_LPP,
            IAMParametresAnnuels.CS_IPS_TX_CALCUL_REVENU, IAMParametresAnnuels.CS_LIMITE_REVENU_POUR_SUBSIDE,
            IAMParametresAnnuels.CS_MONTANT_AVEC_ENFANT_CHARGE, IAMParametresAnnuels.CS_MONTANT_MAX_REVENU_MODESTE,
            IAMParametresAnnuels.CS_MONTANT_NOMBRE_ENFANT_PLUS_GRAND_EGAL_3,
            IAMParametresAnnuels.CS_MONTANT_NOMBRE_ENFANT_PLUS_PETIT_3,
            IAMParametresAnnuels.CS_MONTANT_SANS_ENFANT_CHARGE,
            IAMParametresAnnuels.CS_MONTANT_SUBSIDE_FAMILLE_1_PERSONNE,
            IAMParametresAnnuels.CS_MONTANT_SUBSIDE_FAMILLE_2_PERSONNES, IAMParametresAnnuels.CS_REVENU_MAX_SUBSIDE,
            IAMParametresAnnuels.CS_REVENU_MIN_SUBSIDE, IAMParametresAnnuels.CS_TAUX_CALCUL_FORTUNE_IMPOSABLE });
    public static ContainerParametres containerParametres = null;

    public static List<String> getChampsparametresannuels() {
        return ParametresAnnuelsProvider.champsParametresAnnuels;
    }

    public static void initParamAnnuels() {
        try {
            ParametresAnnuelsProvider.containerParametres = new ContainerParametres();
            SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
            simpleParametreAnnuelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService().search(
                    simpleParametreAnnuelSearch);
            ParametresAnnuelsProvider.containerParametres.setParametresAnnuelsProvider(new ParametresAnnuelsProvider(
                    simpleParametreAnnuelSearch));
        } catch (Exception e) {
            JadeLogger.error("ParametresAnnuelsProvider().initParametresAnnuels()",
                    "Error loading parametre annuels --> " + e.getMessage());
        }
    }

    /**
     * Reset le container des parametres annuels
     */
    public static void resetParametersCache() {
        ParametresAnnuelsProvider.containerParametres = null;
    }

    private Map<String, ParametresAnnuels> listeParametresAnnuels = new HashMap<String, ParametresAnnuels>();

    public ParametresAnnuelsProvider(SimpleParametreAnnuelSearch simpleParametreAnnuelSearch) {
        load(simpleParametreAnnuelSearch);
    }

    public Map<String, ParametresAnnuels> getListeParametresAnnuels() {
        return listeParametresAnnuels;
    }

    public void load(JadeAbstractSearchModel parametresAnnuelSearch) {

        Map<String, ParametresAnnuels> listeParametresAnnuels = new HashMap<String, ParametresAnnuels>();

        for (globaz.jade.persistence.model.JadeAbstractModel absDonnee : parametresAnnuelSearch.getSearchResults()) {
            SimpleParametreAnnuel simpleParametreAnnuel = (SimpleParametreAnnuel) absDonnee;
            String csTypeParametreAnnuel = simpleParametreAnnuel.getCodeTypeParametre();
            if (!listeParametresAnnuels.containsKey(csTypeParametreAnnuel)) {
                ParametresAnnuels params = new ParametresAnnuels();
                params.setCsTypeParametreAnnuel(csTypeParametreAnnuel);
                params.getParametresAnnuels().put(simpleParametreAnnuel.getAnneeParametre(),
                        simpleParametreAnnuel.getValeurParametre());
                listeParametresAnnuels.put(csTypeParametreAnnuel, params);
            } else {
                ParametresAnnuels params = listeParametresAnnuels.get(csTypeParametreAnnuel);
                params.getParametresAnnuels().put(simpleParametreAnnuel.getAnneeParametre(),
                        simpleParametreAnnuel.getValeurParametre());
            }
        }

        setListeParametresAnnuels(listeParametresAnnuels);
    }

    public void setListeParametresAnnuels(Map<String, ParametresAnnuels> listeParametresAnnuels) {
        this.listeParametresAnnuels = listeParametresAnnuels;
    }
}
