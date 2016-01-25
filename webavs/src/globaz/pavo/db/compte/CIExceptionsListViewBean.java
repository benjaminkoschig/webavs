package globaz.pavo.db.compte;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BStatement;

/**
 * ListViewBean pour gérer les exceptions
 */
public class CIExceptionsListViewBean extends CIExceptionsManager implements FWListViewBeanInterface {

    private static final long serialVersionUID = -439818329559604690L;

    public CIExceptionsListViewBean() {
        super();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        // inforom 451 modification par ordre de nom dans l'écran des exceptions
        return "KALNOM,KANAVS";
    }
}
