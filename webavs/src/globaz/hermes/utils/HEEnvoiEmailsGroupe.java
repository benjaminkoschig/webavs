package globaz.hermes.utils;

import globaz.globall.db.BSession;
import globaz.hermes.application.HEApplication;
import globaz.hermes.zas.HEReprise;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEEnvoiEmailsGroupe {
    public final static String CHAR_SEP = ",";
    public final static String DEFAULT_USER_EMAIL_KEY = "EMail";
    // responsable arc
    public final static String responsable_ARC = "zas.user.responsable.role";
    public final static String resultat_ARC = "zas.user.result.role";
    public final static String USER_EMAIL_KEY = "emailKey";
    private BSession _session = null;
    private List listeResponsables = new ArrayList();

    /**
     * Constructor for HEEnvoiEmailsGroupe.
     */
    public HEEnvoiEmailsGroupe(BSession session, String propRoleName) {
        _session = session;
        try {
            String roleName = _session.getApplication().getProperty(propRoleName);
            // est-ce que la propriété responsableARC à une valeur
            if (JadeStringUtil.isEmpty(roleName)) {
                return;
            }
            listeResponsables = HEReprise.loadMails(roleName, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEmail(String email) {
        listeResponsables.add(email);
    }

    public String getEmailKey() throws Exception {
        return _session.getApplication().getProperty(USER_EMAIL_KEY, DEFAULT_USER_EMAIL_KEY);
    }

    public List getEmailListe() {
        return listeResponsables;
    }

    public void sendMail(String subject, String body) throws Exception {
        // HEApplication heApp = (HEApplication) _session.getApplication();
        if (listeResponsables.size() > 0) {
            JadeSmtpClient.getInstance().sendMail(JadeConversionUtil.toStringArray(listeResponsables), subject, body,
                    null);
        } else {
            JadeSmtpClient.getInstance().sendMail(
                    _session.getApplication().getProperty(HEApplication.PROPERTY_DEFAULT_EMAIL), subject, body, null);
        }

    }

    public int size() {
        return listeResponsables.size();
    }
}
