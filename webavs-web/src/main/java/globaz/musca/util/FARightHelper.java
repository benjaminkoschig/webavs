/*
 * Cr�� le 8 nov. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.musca.util;

import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPlanFacturation;
import globaz.musca.db.facturation.FAPlanFacturationManager;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author mmu Permet la gestion des droits des utilisateurs sur certains �l�ments de musca (Passage, Plan de
 *         facturation...) Si un module est ajout�, il
 */
public class FARightHelper {

    public static final String FA_PLAN_FACTURATION_RIGHTS = "FA_PLAN_FACTURATION_RIGHTS";
    public static final String FA_SECURITY_ELEMENT_PLAN_FACTURATION_PREFIX = "musca.facturation.planFacturation.idPlan.";

    /**
     * Renvoie un tableau d'id de tous les plans de facturation auxquels un utilisateur � le droit d'acc�der
     * 
     * @param session
     *            : la BSession de l'utilisateur
     * @param right
     *            : de droit que l'on souhaite avoir sur le plan (FWSecureConstants.READ, ...)
     * @return un tableau vide en cas d'exception
     */
    public static String[] getIdPlanFacturationRights(BSession session, String right) {
        // les droits sont stockes dans la BSession de l'utilisateur
        String[] rights = (String[]) session.getAttribute(FARightHelper.FA_PLAN_FACTURATION_RIGHTS);
        try {
            if (rights == null) {
                Vector<String> listIds = new Vector<String>();
                FAPlanFacturationManager planMgr = new FAPlanFacturationManager();
                planMgr.setSession(session);
                planMgr.find();
                for (Iterator<?> itPlan = planMgr.iterator(); itPlan.hasNext();) {
                    FAPlanFacturation plan = (FAPlanFacturation) itPlan.next();
                    String element = FARightHelper.FA_SECURITY_ELEMENT_PLAN_FACTURATION_PREFIX
                            + plan.getIdPlanFacturation();
                    // Si le droit est accord�, on ajoute l'id du plan � la
                    // liste
                    if (session.hasRight(element, right)) {
                        listIds.add(plan.getIdPlanFacturation());
                    }

                }

                // Met les donn�es du vecteur dans un tableau de String
                int nbPlan = listIds.size();
                rights = new String[nbPlan];
                int i = 0;
                for (Iterator<String> iter = listIds.iterator(); iter.hasNext();) {
                    rights[i++] = iter.next();
                }
                session.setAttribute(FARightHelper.FA_PLAN_FACTURATION_RIGHTS, rights);
            }
        } catch (Exception e) {
            JadeLogger
                    .warn("FARightHelper.getIdPlanFacturationRights: impossible de retourner les plans de facturation autoris�s. ",
                            e);
            rights = new String[] {};
        }
        return rights;
    }

    /**
     * Renvoie une liste d'id de tous les plans de facturation auxquels un utilisateur � le droit d'acc�der. Les ids
     * sont s�par�s par une virgule de fa�on � �tre utilis�s dans un sql where statement "... in (liste_ids)"
     * 
     * @param session
     *            : la BSession de l'utilisateur
     * @param right
     *            : de droit que l'on souhaite avoir sur le plan (FWSecureConstants.READ, ...)
     */
    public static String getIdPlanFacturationRightsInString(BSession session, String right) {
        StringBuffer result = new StringBuffer();
        String[] rights = FARightHelper.getIdPlanFacturationRights(session, right);
        for (int i = 0; i < rights.length; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append(rights[i]);
        }
        // Si acc�s � aucun plan
        if (result.length() == 0) {
            return "'0'";
        }
        return result.toString();
    }

    /**
     * Cette classe ne peut-�tre instanci�e
     */
    private FARightHelper() {
        super();
    }

}
