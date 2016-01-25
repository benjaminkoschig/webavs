package globaz.orion.vb.partnerWeb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractViewBean;

public class EBListeDesContactsViewBean extends EBAbstractViewBean {
    private String email;

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesViewBean.
     */
    public EBListeDesContactsViewBean() {
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
