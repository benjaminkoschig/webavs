package globaz.naos.db.listeDeces;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

public class AFListeDecesViewBean extends AFAbstractViewBean {

    String dateDeces = "";
    private String email;

    public String getDateDeces() {
        return dateDeces;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
