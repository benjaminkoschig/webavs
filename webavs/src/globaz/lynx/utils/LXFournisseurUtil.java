package globaz.lynx.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.fournisseur.LXFournisseurManager;

public class LXFournisseurUtil {

    /**
     * Permet la r�cup�ration d'un objet fournisseur. Si probleme, retourne null
     * 
     * @param session
     * @param idFournisseur
     * @return
     */
    public static LXFournisseur getFournisseur(BSession session, String idFournisseur) {
        LXFournisseur fournisseur = new LXFournisseur();
        fournisseur.setSession(session);

        fournisseur.setIdFournisseur(idFournisseur);

        try {
            fournisseur.retrieve();
        } catch (Exception e) {
            return null;
        }

        if (fournisseur.hasErrors() || fournisseur.isNew()) {
            return null;
        }

        return fournisseur;
    }

    /**
     * Return l'id externe et le nom complet d'un fournisseur. Utilis� pour les �crans.
     * 
     * @param session
     * @param idSociete
     * @return
     */
    public static String getIdEtLibelleNomComplet(BSession session, String idFournisseur) {
        LXFournisseur fournisseur = new LXFournisseur();
        fournisseur.setSession(session);

        fournisseur.setIdFournisseur(idFournisseur);

        try {
            fournisseur.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (fournisseur.hasErrors() || fournisseur.isNew()) {
            return "";
        }

        return fournisseur.getIdExterne() + " - " + fournisseur.getNomComplet();
    }

    /**
     * Return le libell� du complement d'un fournisseur. Utilis� pour les �crans.
     * 
     * @param session
     * @param idSociete
     * @return
     */
    public static String getLibelleComplement(BSession session, String idFournisseur) {
        LXFournisseur fournisseur = new LXFournisseur();
        fournisseur.setSession(session);

        fournisseur.setIdFournisseur(idFournisseur);

        try {
            fournisseur.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (fournisseur.hasErrors() || fournisseur.isNew()) {
            return "";
        }

        return fournisseur.getComplement();
    }

    /**
     * Return le libell� du nom d'un fournisseur. Utilis� pour les �crans.
     * 
     * @param session
     * @param idSociete
     * @return
     */
    public static String getLibelleNom(BSession session, String idFournisseur) {
        LXFournisseur fournisseur = new LXFournisseur();
        fournisseur.setSession(session);

        fournisseur.setIdFournisseur(idFournisseur);

        try {
            fournisseur.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (fournisseur.hasErrors() || fournisseur.isNew()) {
            return "";
        }

        return fournisseur.getNom();
    }

    /**
     * Return le libell� du complement d'un fournisseur. Utilis� pour les �crans.
     * 
     * @param session
     * @param idSociete
     * @return
     */
    public static String getLibelleNomComplet(BSession session, String idFournisseur) {
        LXFournisseur fournisseur = new LXFournisseur();
        fournisseur.setSession(session);

        fournisseur.setIdFournisseur(idFournisseur);

        try {
            fournisseur.retrieve();
        } catch (Exception e) {
            return "";
        }

        if (fournisseur.hasErrors() || fournisseur.isNew()) {
            return "";
        }

        return fournisseur.getNomComplet();
    }

    /**
     * Permet de savoir si l'Id Externe pass� en param�tre est pr�sent en base ou non
     * 
     * @param session
     *            La session
     * @param transaction
     * @param idExterne
     *            Un id externe
     * @return True si pr�sent, false sinon
     * @throws Exception
     *             Si exception
     */
    public static boolean isIdExterneExist(BSession session, BTransaction transaction, String idExterne)
            throws Exception {
        LXFournisseurManager manager = new LXFournisseurManager();
        manager.setSession(session);
        manager.setForIdExterne(idExterne);
        manager.find(transaction);

        return !manager.isEmpty();
    }

    /**
     * L'id externe est-elle utilis�e une seule fois ?
     * 
     * @param session
     * @param transaction
     * @param idExterne
     * @return
     * @throws Exception
     */
    public static boolean isIdExterneUnique(BSession session, BTransaction transaction, String idExterne)
            throws Exception {
        LXFournisseurManager manager = new LXFournisseurManager();
        manager.setSession(session);
        manager.setForIdExterne(idExterne);
        manager.find(transaction);

        return (!manager.isEmpty() && manager.size() == 1);
    }

    /**
     * Constructeur
     */
    protected LXFournisseurUtil() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
