/*
 * Créé le 3 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.db.access;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.hermes.utils.StringUtils;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author dostes Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HECheckRights {
    /***/
    public final static String PERMISSIONS_MOTIFS = "securityRoles";

    /**
     * @param args
     */
    public static void main(String[] args) {
        BSession session;
        try {
            session = new BSession("HERMES");
            session.connect("globazf", "ssiiadm");
            HECheckRights rights = new HECheckRights("97", session);
            try {
                rights.checkRole();

            } catch (Exception e1) {

                e1.printStackTrace();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        System.exit(1);
    }

    /***/
    private String motif;

    /***/
    private BSession session;

    /**
	 * 
	 *
	 */
    public HECheckRights() {
        motif = "";
    }

    /**
     * @param string
     * @param session
     */
    public HECheckRights(String motif, BSession session) {

        setSession(session);
        setMotif(motif);
    }

    /**
     * @param transaction
     */
    public void checkRole() throws Exception {
        // boolean isAllowed = false;
        // On va chercher les rôles dans le fichier properties
        String roles = getSession().getApplication().getProperty(PERMISSIONS_MOTIFS);
        ArrayList arcList = extractCollection(roles);

        // on balaye les groupes de l'utilisateur pour voir si y'en a un qui est
        // dans les
        // rôles gérés par les properties
        String[] userRolesList = JadeAdminServiceLocatorProvider.getLocator().getRoleService()
                .findAllIdRoleForIdUser(session.getUserId());

        for (int i = 0; i < userRolesList.length; i++) {
            if (arcList.contains(userRolesList[i])) {
                // je regarde si ce rôle a le droit de faire ce motif
                ArrayList motifsRole = extractCollection(getSession().getApplication().getProperty(userRolesList[i]));
                if (motifsRole.contains(motif)) {
                    // L'utilisateur a le droit !
                    return;
                }
            }
        }
        // l'utilisateur a pas le droit...
        throw new Exception(FWMessageFormat.format(getSession().getLabel("HERMES_DROIT_MOTIF"), getSession()
                .getUserId(), motif));
    }

    /**
     * @param roles
     * @return
     */
    private ArrayList extractCollection(String myString) {
        // remove brackets just in case
        myString = StringUtils.removeChar(myString, '[');
        myString = StringUtils.removeChar(myString, ']');
        ArrayList returnList = new ArrayList();

        StringTokenizer rolesSt = new StringTokenizer(myString, ",");
        while (rolesSt.hasMoreTokens()) {
            String token = rolesSt.nextToken();
            returnList.add(token.trim());
        }

        return returnList;
    }

    /**
     * @return
     */
    public String getMotif() {
        return motif;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    /**
     * @param string
     */
    public void setMotif(String motif) {
        this.motif = motif;
    }

    /**
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }

}
