package globaz.lynx.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceManager;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean;

public class LXSocieteDebitriceUtil {

    /**
     * Return l'adresse d'une société. Utilisé pour les écrans.
     * 
     * @param session
     *            une session
     * @param idSociete
     *            Un id de société
     * @return une adresse
     */
    public static String getAdresse(BSession session, String idSociete) {
        LXSocieteDebitriceViewBean societe = new LXSocieteDebitriceViewBean();
        societe.setSession(session);

        societe.setIdSociete(idSociete);

        try {
            societe.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (societe.hasErrors() || societe.isNew()) {
            return "";
        }

        return societe.getAdresse();
    }

    /**
     * Return le libellé d'une société. Utilisé pour les écrans.
     * 
     * @param session
     * @param idSociete
     * @return
     */
    public static String getLibelle(BSession session, String idSociete) {
        LXSocieteDebitrice societe = new LXSocieteDebitrice();
        societe.setSession(session);

        societe.setIdSociete(idSociete);

        try {
            societe.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (societe.hasErrors() || societe.isNew()) {
            return "";
        }

        return societe.getIdExterne() + " - " + societe.getNom();
    }

    /**
     * Return la socitété si il y en à qu'une de paramétrée, sinon null
     * 
     * @param session
     * @return
     */
    public static LXSocieteDebitrice getOnlyOneSociete(BSession session) {
        try {
            LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
            manager.setSession(session);

            manager.find();

            if (!manager.isEmpty() && manager.size() == 1) {
                return (LXSocieteDebitrice) manager.getFirstEntity();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Permet la récupération d'un objet societe débitrice. Si probleme, retourne null
     * 
     * @param session
     * @param idFournisseur
     * @return
     */
    public static LXSocieteDebitrice getSocieteDebitrice(BSession session, String idSocieteDebitrice) {
        LXSocieteDebitrice societeDeb = new LXSocieteDebitrice();
        societeDeb.setSession(session);

        societeDeb.setIdSociete(idSocieteDebitrice);

        try {
            societeDeb.retrieve();
        } catch (Exception e) {
            return null;
        }

        if (societeDeb.hasErrors() || societeDeb.isNew()) {
            return null;
        }

        return societeDeb;
    }

    /**
     * Y-a-t'il plusieurs sociétés paramétrés ?
     * 
     * @param session
     * @return
     */
    public static boolean hasSeveralSociete(BSession session) {
        try {
            LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
            manager.setSession(session);

            manager.find();

            return (!manager.isEmpty() && manager.size() > 1);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Permet de savoir si l'Id Externe passé en paramètre est présent en base ou non
     * 
     * @param session
     *            La session
     * @param transaction
     * @param idExterne
     *            Un id externe
     * @return True si présent, false sinon
     * @throws Exception
     *             Si exception
     */
    public static boolean isIdExterneExist(BSession session, BTransaction transaction, String idExterne)
            throws Exception {
        LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
        manager.setSession(session);
        manager.setForIdExterne(idExterne);
        manager.find(transaction);

        return !manager.isEmpty();
    }

    /**
     * L'id externe est-elle utilisée une seule fois ?
     * 
     * @param session
     * @param transaction
     * @param idExterne
     * @return
     * @throws Exception
     */
    public static boolean isIdExterneUnique(BSession session, BTransaction transaction, String idExterne)
            throws Exception {
        LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
        manager.setSession(session);
        manager.setForIdExterne(idExterne);
        manager.find(transaction);

        return (!manager.isEmpty() && manager.size() == 1);
    }

    /**
     * Return l'adresse d'une société. Utilisé pour les écrans.
     * 
     * @param session
     *            une session
     * @param idSociete
     *            Un id de société
     * @return une adresse
     */
    public static boolean isLectureOptique(BSession session, String idSociete) {
        LXSocieteDebitrice societe = new LXSocieteDebitrice();
        societe.setSession(session);

        societe.setIdSociete(idSociete);

        try {
            societe.retrieve();
        } catch (Exception e) {
            return false;
        }

        if (societe.hasErrors() || societe.isNew()) {
            return false;
        }

        return societe.isLectureOptique().booleanValue();
    }

    /**
     * Constructeur
     */
    protected LXSocieteDebitriceUtil() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
