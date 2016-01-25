package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.pojo.APReferenceNumber;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Le champ :</br> -
 * referenceNumber doit être présent lorsque le champ « serviceType » est 20, 21, 22, 23 ;</br> - controlNumber doit
 * être présent lorsque le champ « serviceType » est 40, 41 ;</br> sauf en cas d'établissement d'un duplicata (copie
 * d'un formulai-re APG), lequel doit être annoncé avec ce breakRule. (breakRule 505 -> duplicata) </br><strong>Champs
 * concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule505 extends Rule {

    /**
     * @param errorCode
     */
    public Rule505(String errorCode) {
        super(errorCode, true);
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
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        String referenceNumber = champsAnnonce.getReferenceNumber();
        String controlNumber = champsAnnonce.getControlNumber();

        List<String> services = new ArrayList<String>();
        services.add("20");
        services.add("21");
        services.add("22");
        services.add("23");
        if (services.contains(serviceType) && !champsAnnonce.getBreakRules().contains("505")) {
            if (!JadeStringUtil.isEmpty(referenceNumber)) {
                APReferenceNumber refNumber = new APReferenceNumber();
                // Finalement on valide aussi le format du referenceNumber. Si invalide --> exception
                try {
                    refNumber.setReferenceNumber(referenceNumber);
                } catch (Exception exception) {
                    throw new APRuleExecutionException(exception);
                }
            } else {
                return false;
            }
        }

        services.clear();
        services.add("40");
        services.add("41");
        if (services.contains(serviceType) && !champsAnnonce.getBreakRules().contains("505")) {
            if (JadeStringUtil.isEmpty(controlNumber)) {
                return false;
            }
        }

        return true;
    }

}
