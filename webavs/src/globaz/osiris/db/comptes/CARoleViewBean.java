package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CARoleViewBean extends CARole implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * équivalent à createOptionsTags(session, selectedId, true).
     * 
     * @see #createOptionsTags(BSession, String, boolean)
     */
    public static final String createOptionsTags(BSession session, String selectedId) throws Exception {
        return CARoleViewBean.createOptionsTags(session, selectedId, true);
    }

    /**
     * Crée les balises options du menu déroulant 'rôles' en fonction des rôles pour lesquels l'utilisateur actuellement
     * connecté a les droits.
     * 
     * @param session
     *            la session permettant de retrouver l'utilisateur et ses droits
     * @param selectedId
     *            le rôle à présélectionner.
     * @param tous
     *            si vrai, rajoute une option 'Tous' en début de liste.
     * @return une chaîne jamais nulle, jamais vide, constituée de balises 'option'
     */
    public static final String createOptionsTags(BSession session, String selectedId, boolean tous) throws Exception {
        return CARoleViewBean.createOptionsTagsExcludeRole(session, selectedId, tous, null);
    }

    public static final String createOptionsTagsExcludeRole(BSession session, String selectedId, boolean tous,
            List<String> roleToExclude) throws Exception {
        SortedMap<Integer, CARole> roles = CARole.rolesPourUtilisateurCourant(session);
        StringBuffer options = new StringBuffer();

        if (roles.isEmpty()) {
            if (tous) {
                options.append("<option value=\"-1\">");
                options.append(session.getLabel("TOUS"));
                options.append("</option>");
            }
        } else {
            StringBuffer tousRoles = new StringBuffer();

            for (Iterator<CARole> rolesIter = roles.values().iterator(); rolesIter.hasNext();) {
                CARole role = rolesIter.next();
                if ((roleToExclude == null) || (!roleToExclude.isEmpty() && !roleToExclude.contains(role.getIdRole()))) {

                    // ce role
                    options.append("<option value=\"");
                    options.append(role.getIdRole());
                    options.append("\"");

                    if (role.getIdRole().equals(selectedId)) {
                        options.append(" selected");
                    }

                    options.append(">");
                    options.append(role.getDescription());
                    options.append("</options>");

                    // tous les roles
                    if (tousRoles.length() > 0) {
                        tousRoles.append(",");
                    }

                    tousRoles.append(role.getIdRole());
                }
            }

            if (tous) {
                options.insert(0, "<option value=\"" + tousRoles + "\">" + session.getLabel("TOUS") + "</option>");
            }
        }

        return options.toString();
    }

    public final static String getTousOptions(BSession session) throws Exception {
        SortedMap<Integer, CARole> roles = CARole.rolesPourUtilisateurCourant(session);
        StringBuffer tousRoles = new StringBuffer();

        for (Iterator<CARole> rolesIter = roles.values().iterator(); rolesIter.hasNext();) {
            CARole role = rolesIter.next();

            if (tousRoles.length() > 0) {
                tousRoles.append(",");
            }
            tousRoles.append(role.getIdRole());
        }

        return tousRoles.toString();
    }

    /**
     * Constructor for CARoleViewBean.
     */
    public CARoleViewBean() {
        super();
    }
}
