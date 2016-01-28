package ch.globaz.pegasus.businessimpl.services.demande;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.user.bean.JadeUser;

public abstract class AbstractDemandeBuilder {

    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    /**
     * Retourne le nom formatt� de l'utilisateur connect�
     * 
     * @return chaine de carat�re au format [pr�nom] [nom]
     */
    protected String getUserNomFormatte() {
        JadeUser userConnected = BSessionUtil.getSessionFromThreadContext().getUserInfo();

        return userConnected.getFirstname() + " " + userConnected.getLastname();
    }
}
