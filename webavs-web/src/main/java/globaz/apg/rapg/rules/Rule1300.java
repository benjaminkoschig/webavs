package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> Lorsqu'une prestation de
 * paternit� est vers�e � une femme, une information doit apparaitre dans l'�cran des breakrules pour sensibiliser
 * le gestionnaire</br><strong>Champs concern�(s) :</strong></br> serviceType</br>insurant/personIdentificationType/vn
 * @author lga
 */
public class Rule1300 extends Rule {
    private String genreService = "";

    /**
     * @param errorCode
     */
    public Rule1300(String errorCode) {
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
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }
        if (!JadeStringUtil.isEmpty(serviceType)
                && "91".equals(serviceType)
                && !"516001".equals(champsAnnonce.getInsurantSexe())) {
            return false;
        }
        return true;
    }
}
