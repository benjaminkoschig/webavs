package globaz.campus.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;

public class GEDefaultServletAction extends FWDefaultServletAction {
    protected static final String SELECTED_ID = "selectedId";
    protected static final String VIEWBEAN = "viewBean";

    /**
     * @param servlet
     */
    public GEDefaultServletAction(FWServlet servlet) {
        super(servlet);
    }
}
