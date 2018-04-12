package globaz.orion.vb.adi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractViewBean;

public class EBRecapDemandesTransmisesViewBean extends EBAbstractViewBean {
    private String email = "";

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
