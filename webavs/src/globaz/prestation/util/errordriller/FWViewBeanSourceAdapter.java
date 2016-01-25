package globaz.prestation.util.errordriller;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.prestation.util.errordriller.ErrorDriller.DrilledError;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class FWViewBeanSourceAdapter implements ErrorSource {
    private final FWViewBeanInterface managedViewBean;

    FWViewBeanSourceAdapter(FWViewBeanInterface aViewBean) {
        managedViewBean = aViewBean;
    }

    /**
     * Cherche dans ce FWViewBeanInterface si on a des erreurs ou pas.
     */
    @Override
    public List<? extends DrilledError> drill() {
        List<DrilledError> errors = new ArrayList<ErrorDriller.DrilledError>();
        if (FWViewBeanInterface.ERROR.equals(managedViewBean.getMsgType())) {
            errors.add(new DrilledError(managedViewBean.getMessage(), managedViewBean.getClass().getName() + " "
                    + managedViewBean.toString(), new Date()));
        }
        return errors;
    }

}
