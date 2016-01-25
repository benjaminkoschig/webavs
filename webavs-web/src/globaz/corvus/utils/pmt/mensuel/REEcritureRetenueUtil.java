package globaz.corvus.utils.pmt.mensuel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SCR
 */
public class REEcritureRetenueUtil extends REEcritureUtil {

    private List<RERetenueInfoUtil> retenues = null;

    public void addRetenue(RERetenueInfoUtil element) {
        if (retenues == null) {
            retenues = new ArrayList<RERetenueInfoUtil>();
        }
        retenues.add(element);
    }

    public void addRetenues(List<RERetenueInfoUtil> lst) {
        if (retenues == null) {
            retenues = new ArrayList<RERetenueInfoUtil>();
        }

        retenues.addAll(lst);
    }

    public List<RERetenueInfoUtil> getRetenues() {
        return retenues;
    }
}
