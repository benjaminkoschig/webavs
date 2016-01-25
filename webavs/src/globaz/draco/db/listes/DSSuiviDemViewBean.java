/*
 * Cr�� le 25 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.draco.db.listes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * Permet de controler les valeurs entr�es par l'utilisateur
 * 
 * @author sda
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class DSSuiviDemViewBean extends AFAbstractViewBean {
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annee;
    private String email;
    private String typeDeclaration;

    public DSSuiviDemViewBean() throws java.lang.Exception {
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

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEmail(String string) {
        email = string;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

}
