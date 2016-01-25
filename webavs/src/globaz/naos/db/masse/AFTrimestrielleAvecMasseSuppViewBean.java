package globaz.naos.db.masse;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * ViewBean pour la liste des affiliés paritaires actifs avec masse AVS > 200'000.- et périodicité trimestrielle.
 * 
 * @author SEL <br>
 *         Date : 19 févr. 08
 */
public class AFTrimestrielleAvecMasseSuppViewBean extends AFAbstractViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String email;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public AFTrimestrielleAvecMasseSuppViewBean() throws Exception {
    }

    /**
     * @param session
     */
    public AFTrimestrielleAvecMasseSuppViewBean(BSession session) {
    }

    /**
     * getter pour l'attribut email
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
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
