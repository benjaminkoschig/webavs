/*
 * Cr�� le 15 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.ccju.affiliation;

import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;

/**
 * G�n�rateur de num�ro d'affiliation
 * 
 * @author dgi
 */
public class CCJUNoAffGenerator implements INumberGenerator {

    @Override
    public String generateBeforeAdd(AFAffiliation affiliation) throws Exception {
        String no = "";
        if (affiliation != null) {
            no = affiliation.getAffilieNumero();
            // doit �tre non null et de longueur de 3
            if (no != null) {
                if (no.length() == 4 && no.charAt(3) == '.') {
                    // saisie avec le point, le supprimer
                    no = no.substring(0, no.length() - 1);
                }
                if (no.length() == 3) {
                    // recherche du prochain num�ro valable pour le pr�fix saisi
                    // par l'utilisateur
                    int cnt = 1000;
                    AFAffiliationManager mgr = new AFAffiliationManager();
                    mgr.setSession(affiliation.getSession());
                    mgr.setLikeAffilieNumero(no);
                    mgr.setOrder("MALNAF DESC");
                    mgr.find(affiliation.getSession().getCurrentThreadTransaction());
                    if (mgr.size() != 0) {
                        // encore aucun no pr�sent
                        cnt = Integer.parseInt(((AFAffiliation) mgr.getFirstEntity()).getAffilieNumero().substring(4));
                    }
                    return no + "." + JadeStringUtil.rightJustify(String.valueOf(cnt + 1), 4, '0');
                } else {
                    IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                            AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
                    no = affilieFormater.format(no);
                }
            } else {
                no = "";
            }
        }
        return no;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.affiliation.INumberGenerator#generate(globaz.naos.db.affiliation .AFAffiliation)
     */
    @Override
    public String generateBeforeDisplay(AFAffiliation affiliation) throws Exception {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.naos.affiliation.INumberGenerator#isEditable(globaz.naos.db. affiliation.AFAffiliation)
     */
    @Override
    public boolean isEditable(AFAffiliation affiliation) throws Exception {
        // TODO Raccord de m�thode auto-g�n�r�
        return true;
    }

}
