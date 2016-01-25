package globaz.lynx.utils;

import globaz.globall.db.BSession;
import globaz.lynx.db.ordregroupe.LXOrdreGroupe;

public class LXOrdreGroupeUtil {

    /**
     * Return l'etat d'un ordre group�. Utilis� pour les �crans.
     * 
     * @param session
     * @param idJournal
     * @return
     */
    public static String getEtatOrdreGroupe(BSession session, String idOrdreGroupe) {
        LXOrdreGroupe ordreGroupe = new LXOrdreGroupe();
        ordreGroupe.setSession(session);

        ordreGroupe.setIdOrdreGroupe(idOrdreGroupe);

        try {
            ordreGroupe.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (ordreGroupe.hasErrors() || ordreGroupe.isNew()) {
            return "";
        }

        return ordreGroupe.getCsEtat();
    }

    /**
     * Return le libell� d'un ordre groupe. Utilis� pour les �crans.
     * 
     * @param session
     * @param idOrdreGroupe
     * @return
     */
    public static String getLibelleOrdreGroupe(BSession session, String idOrdreGroupe) {
        LXOrdreGroupe ordreGroupe = new LXOrdreGroupe();
        ordreGroupe.setSession(session);

        ordreGroupe.setIdOrdreGroupe(idOrdreGroupe);

        try {
            ordreGroupe.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (ordreGroupe.hasErrors() || ordreGroupe.isNew()) {
            return "";
        }

        return ordreGroupe.getIdOrdreGroupe() + " - " + ordreGroupe.getLibelle();
    }

    /**
     * Return vrai si l'ordre group� est ouvert. Utilis� pour les �crans.
     * 
     * @param session
     * @param idJournal
     * @return
     */
    public static boolean isOuvert(BSession session, String idOrdreGroupe) {
        LXOrdreGroupe ordreGroupe = new LXOrdreGroupe();
        ordreGroupe.setSession(session);

        ordreGroupe.setIdOrdreGroupe(idOrdreGroupe);

        try {
            ordreGroupe.retrieve();
        } catch (Exception e) {
            return false;
        }

        if (ordreGroupe.hasErrors() || ordreGroupe.isNew()) {
            return false;
        }

        return ordreGroupe.isOuvert();
    }

    /**
     * Constructeur
     */
    protected LXOrdreGroupeUtil() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
