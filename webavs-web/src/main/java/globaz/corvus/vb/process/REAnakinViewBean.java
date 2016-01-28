/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.process;

import globaz.corvus.application.REApplication;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserValue;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Vector;

/**
 * @author scr
 * 
 */

public class REAnakinViewBean extends PRAbstractViewBeanSupport {

    public static final String PARAM_KEY_ANAKIN_VALIDATOR = "KEY-ANAKIN_VALIDATOR";
    public static final String PARAM_VAL_ANAKIN_VALIDATOR_ACTIF = "ANAKIN_VALIDATOR_ACTIF";

    public static final String PARAM_VAL_ANAKIN_VALIDATOR_INACTIF = "ANAKIN_VALIDATOR_INACTIF";
    public static final String TRAITEMENT_ACTIVER_ANAKIN_VALIDATOR = "ACTIVER";
    public static final String TRAITEMENT_DESACTIVER_ANAKIN_VALIDATOR = "DESACTIVER";

    public static boolean isAnakinValidatorActif(BSession session) {
        FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
        valUtili.setSession(session);
        Vector<String> v = valUtili.retrieveValeur(REApplication.DEFAULT_APPLICATION_CORVUS,
                REAnakinViewBean.PARAM_KEY_ANAKIN_VALIDATOR);

        try {
            if (v != null && !v.isEmpty()) {
                String value = v.get(0);
                if (REAnakinViewBean.PARAM_VAL_ANAKIN_VALIDATOR_ACTIF.equals(value)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String traitement = "";

    public String getTraitement() {
        return traitement;
    }

    public void setTraitement(String traitement) {
        this.traitement = traitement;
    }

    @Override
    public boolean validate() {
        return false;
    }

}
