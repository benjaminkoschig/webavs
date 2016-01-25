/*
 * Créé le 13 mai 05
 * 
 * Description :
 */
package globaz.prestation.interfaces.ci;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.api.ICICompteIndividuel;
import java.util.Hashtable;

/**
 * Descpription Helper class sur les CI
 * 
 * @author scr
 */
public class PRCompteIndividuelHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut ci like no AVS
     * 
     * @param session
     *            DOCUMENT ME!
     * @param likeNoAVS
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut ci like no AVS
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final PRCiWrapper[] getCiLikeNoAVS(BISession session, String likeNoAVS) throws Exception {
        // fail fast
        if (JadeStringUtil.isBlank(likeNoAVS)) {
            return null;
        }

        ICICompteIndividuel ci = (ICICompteIndividuel) session.getAPIFor(ICICompteIndividuel.class);
        Hashtable criteres = new Hashtable();
        criteres.put(ICICompteIndividuel.FIND_FOR_NUM_AVS_LIKE, likeNoAVS);
        ci.setISession(session);

        Object[] obj = ci.find(criteres);

        if ((obj == null) || (obj.length == 0)) {
            return null;
        } else {
            PRCiWrapper[] result = new PRCiWrapper[obj.length];

            for (int i = 0; i < obj.length; i++) {
                result[i] = new PRCiWrapper((GlobazValueObject) obj[i], PRCiWrapper.CI);
            }

            return result;
        }
    }

    /**
     * Description Retourne une entete de CI à partir du numéro AVS
     * 
     * @param session
     * @param noAvs
     *            Le noAVS du tiers à rechercher
     * 
     * @return ITIPersonneAvsAdresse Le tiers, si trouvé. Ne retourne jamais null
     * 
     * @throws Exception
     *             si noAVS inexistant ou non unique
     */
    public static final ICICompteIndividuel getCompteIndividuel(BSession session, String noAvs) throws Exception {
        ICICompteIndividuel ci = (ICICompteIndividuel) session.getAPIFor(ICICompteIndividuel.class);

        noAvs = JadeStringUtil.change(noAvs, ".", ""); // on ne veut pas des
        // points dans le CI

        if (session.getCurrentThreadTransaction() != null) {
            ci.load(noAvs, session.getCurrentThreadTransaction());
        } else {
            // HACK: s'il n'y a pas de transaction existante, en créer une pour
            // rechercher dans les CI
            BITransaction transaction = session.newTransaction();

            try {
                ci.load(noAvs, transaction);
            } finally {
                transaction.closeTransaction();
            }
        }

        return ci;
    }
}
