/*
 * Cr�� le 9 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.orion.vb.acompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractViewBean;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un viewBean leger pour stocker les options de lancement du process de generation des suivi des annonces des salaires.
 * </p>
 * 
 * @author vre
 */
public class EBPrevisionAcompteViewBean extends EBAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String email;

    /**
     * Cr�e une nouvelle instance de la classe AFAnnonceSalairesViewBean.
     */
    public EBPrevisionAcompteViewBean() {
        super();
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            try {
                email = getISession().getUserEMail();
            } catch (Exception e) {
                setMessage("Impossible de trouver l'adresse e-mail de l'utilisateur");
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return email;
    }

    /**
     * setter pour l'attribut email
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String string) {
        email = string;
    }
}
