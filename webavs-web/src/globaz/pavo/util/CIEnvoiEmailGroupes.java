package globaz.pavo.util;

import globaz.globall.db.BSession;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe permettant de regrouper des emails par utilisateur avant de les envoyés. Date de création : (29.04.2003
 * 10:09:08)
 * 
 * @author: David Girardin
 */
public class CIEnvoiEmailGroupes {
    private CIApplication application;
    private HashMap groups;
    private BSession session;

    /**
     * Constructeur.
     */
    public CIEnvoiEmailGroupes(CIApplication ciapplication) {
        super();
        application = ciapplication;
        groups = new HashMap();
    }

    /**
     * Ajout d'un email. Date de création : (29.04.2003 10:12:52)
     * 
     * @param to
     *            le destinataire
     * @param subject
     *            le sujet du mail
     * @param message
     *            le message du mail
     */
    public void addEmail(String to, String subject, String message) {
        String postSubject = "";
        if (to == null) {
            // envoi à l'administrateur
            to = application.getEmailAdmin();
            // sujet erreur
            postSubject = " " + getSession().getLabel("MSG_ANNONCE_EMAIL_ERREUR");
        }
        String[] content = (String[]) groups.get(to);
        if (content == null) {
            // destinataire pas encore présent
            groups.put(to, new String[] { subject + postSubject, message + "\n" });
        } else {
            // destinataire existe. Changer de sujet et ajouter le nouveau
            // messager
            content[0] = getSession().getLabel("MSG_ANNONCE_EMAIL_SUJET") + postSubject;
            content[1] += "----------\n" + message + "\n";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 07:56:22)
     * 
     * @return globaz.globall.db.BSession
     */
    public globaz.globall.db.BSession getSession() {
        return session;
    }

    /**
     * Envoie les emails. Date de création : (29.04.2003 10:18:12)
     */
    public void send() throws Exception {
        Iterator it = groups.keySet().iterator();
        while (it.hasNext()) {
            String to = (String) it.next();
            String[] content = (String[]) groups.get(to);
            JadeSmtpClient.getInstance().sendMail(to, content[0], content[1], null);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 07:56:22)
     * 
     * @param newSession
     *            globaz.globall.db.BSession
     */
    public void setSession(globaz.globall.db.BSession newSession) {
        session = newSession;
    }
}
