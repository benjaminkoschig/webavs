package globaz.corvus.utils;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

public class REPmtMensuel {

    public static final String AUTHORISER_VALIDATION_DES_DECISIONS = "OK";
    public static final String INTERDIRE_VALIDATION_DES_DECISIONS = "KO";
    public static final String PARAM_VAL_DEC_APP_NAME = "RECorvus";
    public static final String PARAM_VAL_DEC_ID = "VALI_DECIS";

    /**
     * LGA Si la date du dernier paiement n'a pas pu être retrouvée, c'est la date contenue dans cette constante qui
     * sera renvoyée. En l'occurence le 01.1970. Cette constante va disparaître dans un avenir proche !!!
     */
    @Deprecated
    public static final String DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT = "01.1970";

    /**
     * @param session
     * @return La date du dernier pmt mensuel effectué, au format mm.aaaa
     */
    public static String getDateDernierPmt(BSession session) {

        try {
            RELotManager mgr = new RELotManager();
            mgr.setSession(session);
            mgr.setForCsEtatIn(IRELot.CS_ETAT_LOT_VALIDE + ", " + IRELot.CS_ETAT_LOT_PARTIEL);
            mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);

            mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgr.setOrderBy(RELot.FIELDNAME_DATE_ENVOI + " desc ");
            mgr.find(1);

            if (mgr.size() == 0) {
                return REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT;
            } else {
                RELot lot = (RELot) mgr.getEntity(0);
                if (JadeStringUtil
                        .isBlankOrZero(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(lot.getDateEnvoiLot()))) {
                    return REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT;
                } else {
                    return (PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(lot.getDateEnvoiLot()));
                }
            }
        } catch (Exception e) {
            return REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT;
        }

    }

    /**
     * @param
     * @return La date du dernier pmt mensuel avec un mois fixe, au format mm.aaaa
     */
    public static String getDateDernierPmtMoisFixe(String moisFixe, String dernierPaiement) {
        return moisFixe + "." + PRDateFormater.convertDate_MMxAAAA_to_AAAA(dernierPaiement);
    }

    /**
     * @param session la session
     * @param moisFixe le mois fixé
     * @return La date dernier pmt mensuel avec un mois fixe moins 1 années, au format mm.aaaa
     */
    public static String getDateDernierPmtMoisFixeMoinsUneAnnee(BSession session, String moisFixe) {
        String dernierPaiement = REPmtMensuel.getDateDernierPmt(session);

        JACalendar cal = new JACalendarGregorian();
        String dernierPaiementMoinsUneAnnee;

        try {
            dernierPaiementMoinsUneAnnee = cal.addYears("01." + dernierPaiement, -1);
            return moisFixe + "." + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dernierPaiementMoinsUneAnnee);
        } catch (JAException e) {
            return "01.1970";
        }
    }

    /**
     * @param session la session
     * @return La date dernier pmt mensuel plus 1 mois, au format mm.aaaa
     */
    public static String getDateDernierPmtPlusUnMois(BSession session) {
        String dernierPaiement = REPmtMensuel.getDateDernierPmt(session);

        JACalendar cal = new JACalendarGregorian();
        String dernierPaiementMoinsUnMois;

        try {
            dernierPaiementMoinsUnMois = cal.addMonths("01." + dernierPaiement, 1);
            return PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dernierPaiementMoinsUnMois);
        } catch (JAException e) {
            return "01.1970";
        }
    }

    /**
     * @param session la session
     * @return La date dernier pmt mensuel moins 1 mois, au format mm.aaaa
     */
    public static String getDateDernierPmtMoinsUnMois(BSession session) {
        String dernierPaiement = REPmtMensuel.getDateDernierPmt(session);

        JACalendar cal = new JACalendarGregorian();
        String dernierPaiementMoinsUnMois;

        try {
            dernierPaiementMoinsUnMois = cal.addMonths("01." + dernierPaiement, -1);
            return PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dernierPaiementMoinsUnMois);
        } catch (JAException e) {
            return "01.1970";
        }
    }

    /**
     * @param session
     * @return La date du prochain pmt mensuel, au format mm.aaaa
     */
    public static String getDateProchainPmt(BSession session) {

        String dernierPaiement = REPmtMensuel.getDateDernierPmt(session);

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

        try {
            FWFindParameterManager mgr = new FWFindParameterManager();
            mgr.setSession(session);
            mgr.setIdCodeSysteme("11111111");
            mgr.setIdApplParametre(REPmtMensuel.PARAM_VAL_DEC_APP_NAME);
            mgr.setIdCleDiffere(REPmtMensuel.PARAM_VAL_DEC_ID);
            mgr.find();

            if (mgr.isEmpty()) {
                return true;
            } else {
                FWFindParameter e = (FWFindParameter) mgr.getFirstEntity();
                String val = e.getValeurAlphaParametre();
                if (REPmtMensuel.AUTHORISER_VALIDATION_DES_DECISIONS.equals(val)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return true;
        }

    }
}
