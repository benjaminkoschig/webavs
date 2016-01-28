/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.statOfas;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * Permet de controler les valeurs entrées par l'utilisateur
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFStatOFASControleViewBean extends AFAbstractViewBean {
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annee;
    private String email;

    public AFStatOFASControleViewBean() throws java.lang.Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEmail(String string) {
        email = string;
    }

}
