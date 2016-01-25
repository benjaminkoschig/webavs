package globaz.corvus.vb.adaptation;

import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;

/**
 * 
 * @author HPE
 * 
 */
public class RERentesAdapteesJointRATiersViewBean extends RERentesAdapteesJointRATiers implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAnneeDefault() throws JAException {
        JADate date = new JADate(REPmtMensuel.getDateProchainPmt(getSession()));
        return String.valueOf(date.getYear());
    }

}
