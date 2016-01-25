package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.pojo.APReferenceNumber;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ «serviceType» = 20,
 * 21, 22, 23 (protection civile), le champ « referenceNumber » doit être contrôlé conformément aux directives 318.705
 * :</br> Si le champ « referenceNumber » < 100000</br> Format CCIIF avec CC : canton = 1 à 25 et 50 II : centre
 * d'instruction = 11 ou 99 ; sont autorisés en plus selon les cantons : </br> ZH 01 12, 13 </br> BE 02 12, 13, 14. 15,
 * 16 .......(tous les autres cantons)</br> GE 25 12</br> Confédération 26 00 (remplace 11) </br> F : filiale = 1 ou 2
 * </br> Si le champ « referenceNumber » > 100000</br> Format CCNNNNF NNNN : NPA = présent dans le fichier FXXPTT (181)
 * </br></br> Si le champ « serviceType » = 20 et le champ « referenceNumber » != cas 1, cas 3, cas 4 et cas 5 ->
 * erreur</br> Si le champ « serviceType » = 21 et le champ «referenceNumber » != cas 1 -> erreur</br> Si le champ «
 * serviceType » = 22 et le champ « referenceNumber » != cas 2, cas 3, cas 4 et cas 5 -> erreur</br> Si le champ «
 * serviceType » = 23 et le champ « referenceNumber » != cas 2, cas 3, cas 4 et cas 5 -> erreur </br> <strong>Champs
 * concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule304 extends Rule {

    /**
     * @param errorCode
     */
    public Rule304(String errorCode) {
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
        String referenceNumber = champsAnnonce.getReferenceNumber();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> serviceCivils = new ArrayList<String>();
        serviceCivils.add("20");
        serviceCivils.add("21");
        serviceCivils.add("22");
        serviceCivils.add("23");

        // Si le genre de service est 20, 21, 22, 23
        if (!JadeStringUtil.isEmpty(serviceType) && serviceCivils.contains(serviceType)) {

            // Et que ce n'est pas un duplicata
            if (!champsAnnonce.getBreakRules().contains("505")) {

                APReferenceNumber refNumber = new APReferenceNumber();
                try {
                    refNumber.setReferenceNumber(referenceNumber);
                } catch (Exception exception) {
                    throw new APRuleExecutionException("Reference number seems invalid", exception);
                }
                if ("20".equals(serviceType)) {
                    if ((refNumber.getCas() == 1) || (refNumber.getCas() == 3) || (refNumber.getCas() == 4)
                            || (refNumber.getCas() == 5)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if ("21".equals(serviceType)) {
                    if (refNumber.getCas() == 1) {
                        return true;
                    } else {
                        return false;
                    }
                } else if ("22".equals(serviceType) || "23".equals(serviceType)) {
                    if ((refNumber.getCas() == 2) || (refNumber.getCas() == 3) || (refNumber.getCas() == 4)
                            || (refNumber.getCas() == 5)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
