package globaz.helios.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;

/**
 * @author dda
 * 
 */
public class CGDefaultServletAction extends FWDefaultServletAction {

    protected static final String SELECTED_ID = "selectedId";
    protected static final String VIEWBEAN = "viewBean";

    /**
     * @param servlet
     */
    public CGDefaultServletAction(FWServlet servlet) {
        super(servlet);
    }

}
