package globaz.draco.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;

/**
 * � la rue
 * 
 * Date de cr�ation : (18.11.2002 18:19:41)
 * 
 * @author: Administrator
 */
public class DSActionDecompteImpotLtn extends DSActionCustomFind {
    public DSActionDecompteImpotLtn(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("customFind".equals(getAction().getActionPart())) {
            _actionCustomFind(session, request, response, mainDispatcher);
        }
    }
}