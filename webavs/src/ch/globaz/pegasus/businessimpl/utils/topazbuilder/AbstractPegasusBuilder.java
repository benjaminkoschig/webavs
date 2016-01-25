package ch.globaz.pegasus.businessimpl.utils.topazbuilder;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.user.bean.JadeUser;

public abstract class AbstractPegasusBuilder {

    public static final String CR = "\n";
    public static final String CR_FROM_BABEL = "{br}";
    public static final String CRLF = "\r\n";
    public static final String NEW_LINE = "\n";

    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    /**
     * Retourne le nom formatté de l'utilisateur connecté
     * 
     * @return chaine de caratère au format [prénom] [nom]
     */
    protected String getUserNomFormatte() {
        JadeUser userConnected = BSessionUtil.getSessionFromThreadContext().getUserInfo();

        return userConnected.getFirstname() + " " + userConnected.getLastname();
    }

}
