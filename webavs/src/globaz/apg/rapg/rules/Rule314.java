package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br>Si le champ « serviceType » = 10, 11, 12, 13, 14, 20, 21, 22, 23, 30, 40, 41 ou 50 et la date de
 * naissance issue du NAVS13
 * indique un âge > 65 ans (homme) -> erreur.</br>
 * indique un âge > 64 ans (femme) -> erreur.</br>
 * 
 * @author lga
 */
public class Rule314 extends Rule {

    /**
     * @param errorCode
     */
    public Rule314(String errorCode) {
        super(errorCode, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String serviceType = champsAnnonce.getServiceType();
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String insurantBirthDate = champsAnnonce.getInsurantBirthDate();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
            validNotEmpty(startOfPeriod, "startOfPeriod");
        }

        List<String> services = new ArrayList<String>();
        services.add("10");
        services.add("11");
        services.add("12");
        services.add("13");
        services.add("14");
        services.add("20");
        services.add("21");
        services.add("22");
        services.add("23");
        services.add("30");
        services.add("40");
        services.add("41");
        services.add("50");

        if (services.contains(serviceType)) {
            validNotEmpty(insurantBirthDate, "insurantBirthDate");

            // définir l'age de la retraite en fonction du sexe
            int ageRetraite = 65;
            if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(champsAnnonce.getInsurantSexe())) {
                ageRetraite = 64;
            }

            if (JadeDateUtil.getNbYearsBetween(insurantBirthDate, startOfPeriod) > ageRetraite) {
                return false;
            }
        }

        return true;
    }

}
