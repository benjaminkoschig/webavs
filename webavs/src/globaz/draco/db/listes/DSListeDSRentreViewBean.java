/*
 * Cr�� le 25 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.draco.db.listes;

import globaz.draco.vb.DSAbstractPersistentViewBean;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * Permet de controler les valeurs entr�es par l'utilisateur
 * 
 * @author sda
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class DSListeDSRentreViewBean extends DSAbstractPersistentViewBean implements FWViewBeanInterface {

    private String dateReference;
    private String eMail;

    public DSListeDSRentreViewBean() throws java.lang.Exception {
    }

    public String getDateReference() {
        return dateReference;
    }

    public String getEMail() {
        return eMail;
    }

    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    public void setEMail(String mail) {
        eMail = mail;
    }

}
