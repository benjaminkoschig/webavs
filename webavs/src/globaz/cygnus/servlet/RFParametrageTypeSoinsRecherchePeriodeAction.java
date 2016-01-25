/*
 * créé le 24 mars 2010
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.typeDeSoins.RFParametrageTypeSoinsRecherchePeriodeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFParametrageTypeSoinsRecherchePeriodeAction extends RFDefaultAction {

    public RFParametrageTypeSoinsRecherchePeriodeAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        ((RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean).setIsUpdate(false);
        return super._getDestAjouterEchec(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestAjouterSucces(session, request, response, viewBean) + "&_valid=new";
    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean).setIsUpdate(true);
        return super._getDestModifierEchec(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean).setIsUpdate(false);
        return super._getDestSupprimerEchec(session, request, response, viewBean);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFParametrageTypeSoinsRecherchePeriodeViewBean outputViewBean = new RFParametrageTypeSoinsRecherchePeriodeViewBean();
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, outputViewBean);
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(outputViewBean, e.getMessage());
        }

        mainDispatcher.dispatch(outputViewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", outputViewBean);
        request.setAttribute(FWServlet.VIEWBEAN, outputViewBean);

        /*
         * redirection vers la destination
         */
        String _destination = "";

        if (outputViewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        } else {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {

            RFParametrageTypeSoinsRecherchePeriodeViewBean oldViewBean = (RFParametrageTypeSoinsRecherchePeriodeViewBean) this
                    .loadViewBean(session);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            ((RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean).setCodeSousTypeDeSoin(oldViewBean
                    .getCodeSousTypeDeSoin());
            ((RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean).setCodeSousTypeDeSoinList(oldViewBean
                    .getCodeSousTypeDeSoin());

            ((RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean).setCodeTypeDeSoin(oldViewBean
                    .getCodeTypeDeSoin());
            ((RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean).setCodeTypeDeSoinList(oldViewBean
                    .getCodeTypeDeSoin());

            return viewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

}
