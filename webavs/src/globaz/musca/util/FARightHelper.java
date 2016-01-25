/*
 * Créé le 8 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
 * @author mmu Permet la gestion des droits des utilisateurs sur certains éléments de musca (Passage, Plan de
 *         facturation...) Si un module est ajouté, il
 */
public class FARightHelper {

    public static final String FA_PLAN_FACTURATION_RIGHTS = "FA_PLAN_FACTURATION_RIGHTS";
    public static final String FA_SECURITY_ELEMENT_PLAN_FACTURATION_PREFIX = "musca.facturation.planFacturation.idPlan.";

    /**
     * Renvoie un tableau d'id de tous les plans de facturation auxquels un utilisateur à le droit d'accéder
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
                    // Si le droit est accordé, on ajoute l'id du plan à la
                    // liste
                    if (session.hasRight(element, right)) {
                        listIds.add(plan.getIdPlanFacturation());
                    }

                }

                // Met les données du vecteur dans un tableau de String
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
                    .warn("FARightHelper.getIdPlanFacturationRights: impossible de retourner les plans de facturation autorisés. ",
                            e);
            rights = new String[] {};
        }
        return rights;
    }

    /**
     * Renvoie une liste d'id de tous les plans de facturation auxquels un utilisateur à le droit d'accéder. Les ids
     * sont séparés par une virgule de façon à être utilisés dans un sql where statement "... in (liste_ids)"
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
        // Si accès à aucun plan
        if (result.length() == 0) {
            return "'0'";
        }
        return result.toString();
    }

    /**
     * Cette classe ne peut-être instanciée
     */
    private FARightHelper() {
        super();
    }

}
