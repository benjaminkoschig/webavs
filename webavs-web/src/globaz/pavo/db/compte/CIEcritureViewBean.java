package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.translation.CodeSystem;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (12.11.2002 17:04:09)
 * 
 * @author: Administrator
 */
public class CIEcritureViewBean extends CIEcriture implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CIEcritureViewBean.
     */
    public CIEcritureViewBean() {
        super();
    }

    /**
     * Retourne le GRE formatté sur deux chiffres si le code particulier est à mandat normal.
     * 
     * @return Le GRE formatté sur deux chiffres si le code particulier est à mandat normal.
     */
    public String getGreFormat(HttpSession session) {
        String greCourant = new String();
        // L'extourne n'a pas de code utilisateur 0...
        if (JadeStringUtil.isIntegerEmpty(getExtourne())) {
            greCourant = "0";
        } else {
            greCourant += CodeSystem.getCodeUtilisateur(getExtourne(), session);
        }
        greCourant += CodeSystem.getCodeUtilisateur(getGenreEcriture(), session);
        if (!CIEcriture.CS_MANDAT_NORMAL.equals(getParticulier())) {
            greCourant += CodeSystem.getCodeUtilisateur(getParticulier(), session);
        }

        return greCourant;
    }

    public CIJournal getJournal(BSession session) throws Exception {
        CIJournal journal = new CIJournal();
        journal.setSession(session);
        journal.setIdJournal(getIdJournal());
        journal.retrieve();
        if (journal.isNew() == false) {
            return journal;
        } else {
            return null;
        }
    }

}
