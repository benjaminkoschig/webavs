package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.util.APGSeodorErreurListEntities;
import globaz.apg.util.APGSeodorServiceCallUtil;
import globaz.prestation.beans.PRPeriode;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : une ann�e
 * civile1 Si le champ � serviceType � = 20, le champ � referenceNumber � = cas 3 (cours de r�p�tition) et le champ �
 * numberOfDays � > 7 jours -> erreur </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule2000 extends Rule {

    public Rule2000(String errorCode) {
        super(errorCode, true);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {

        APGSeodorErreurListEntities messagesErrors = new APGSeodorErreurListEntities();
        List<PRPeriode> periodes = new ArrayList<>();
        PRPeriode periode = new PRPeriode(champsAnnonce.getStartOfPeriod(), champsAnnonce.getEndOfPeriod());
        periodes.add(periode);
        messagesErrors = APGSeodorServiceCallUtil.callWSSeodor(champsAnnonce.getServiceType(), champsAnnonce.getInsurant(), periodes
                , champsAnnonce.getNumberOfDays(), getSession());
        if (StringUtils.isNotEmpty(messagesErrors.getMessageErreur())) {
            if (messagesErrors.getApgSeodorErreurEntityList().size() == 0) {
                throw new APWebserviceException(messagesErrors.getMessageErreur());
            }
            return false;
        }
        return true;
    }
}
