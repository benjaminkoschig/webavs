package globaz.corvus.vb.echeances;

import globaz.jade.client.util.JadeStringUtil;
import globaz.lyra.vb.process.LYProcessAjaxViewBean;
import java.util.HashSet;
import java.util.Set;

public class REDiminutionRenteEnfantAjaxViewBean extends LYProcessAjaxViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idRentesADiminuer;

    public REDiminutionRenteEnfantAjaxViewBean() {
        super();

        idRentesADiminuer = "";
    }

    public Set<String> getIdRentesADiminuer() {
        Set<String> ids = new HashSet<String>();
        for (String unId : idRentesADiminuer.split(",")) {
            if (!JadeStringUtil.isBlankOrZero(unId)) {
                ids.add(unId);
            }
        }
        return ids;
    }

    public void setIdRentesADiminuer(String idRentesADiminuer) {
        this.idRentesADiminuer = idRentesADiminuer;
    }
}
