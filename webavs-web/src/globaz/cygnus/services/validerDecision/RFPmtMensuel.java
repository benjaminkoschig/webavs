package globaz.cygnus.services.validerDecision;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserValue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.util.Vector;

/**
 * @author fha
 */
public class RFPmtMensuel {
    public static final String AUTHORISER_VALIDATION_DES_DECISIONS = "AUTHORISER_VALIDATION_DECISION";
    public static final String INTERDIRE_VALIDATION_DES_DECISIONS = "INTERDIRE_VALIDATION_DECISION";
    public static final String PARAM_VAL_DEC_APP_NAME = "RECorvus";
    public static final String PARAM_VAL_DEC_ID = "validationDecision";

    /**
     * 
     * @param session
     * @return La date du dernier pmt mensuel effectué, au format mm.aaaa
     */
    public static String getDateDernierPmt(BSession session) {

        try {
            RELotManager mgr = new RELotManager();
            mgr.setSession(session);
            mgr.setForCsEtatIn(IRELot.CS_ETAT_LOT_VALIDE + ", " + IRELot.CS_ETAT_LOT_PARTIEL);

            mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgr.setOrderBy(RELot.FIELDNAME_DATE_ENVOI + " desc ");
            mgr.changeManagerSize(0);
            mgr.find();

            if (mgr.size() == 0) {
                return "01.1970";
            } else {
                RELot lot = (RELot) mgr.getEntity(0);
                if (JadeStringUtil
                        .isBlankOrZero(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(lot.getDateEnvoiLot()))) {
                    return "01.1970";
                } else {
                    return (PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(lot.getDateEnvoiLot()));
                }
            }
        } catch (Exception e) {
            return "01.1970";
        }

    }

    /**
     * 
     * @param session
     * @return La date du prochain pmt mensuel, au format mm.aaaa
     */
    public static String getDateProchainPmt(BSession session) {

        String dernierPaiement = RFPmtMensuel.getDateDernierPmt(session);

        JACalendar cal = new JACalendarGregorian();
        String prochainPaiement;
        try {
            prochainPaiement = cal.addMonths("01." + dernierPaiement, 1);
            return PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(prochainPaiement);
        } catch (JAException e) {
            return "01.1970";
        }
    }

    public static boolean isValidationDecisionAuthorise(BSession session) {
        FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
        valUtili.setSession(session);
        Vector v = valUtili.retrieveValeur(RFPmtMensuel.PARAM_VAL_DEC_APP_NAME, RFPmtMensuel.PARAM_VAL_DEC_ID);

        if ((v != null) && (v.size() > 0)) {
            String value = (String) v.get(0);
            if (RFPmtMensuel.INTERDIRE_VALIDATION_DES_DECISIONS.equals(value)) {
                return false;
            } else {
                return true;
            }

        }
        // Valeur par défaut
        else {
            return true;
        }

    }
}
