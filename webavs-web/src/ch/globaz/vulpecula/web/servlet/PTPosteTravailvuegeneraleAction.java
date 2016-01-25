package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.postetravailvueglobale.PTEmployeurvueglobaleViewBean;
import globaz.vulpecula.vb.postetravailvueglobale.PTTravailleurvueglobaleViewBean;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * @author Arnaud Geiser (AGE) | Créé le 28 janv. 2014
 * 
 */
public class PTPosteTravailvuegeneraleAction extends FWDefaultServletAction {
    private static final String TAB = "tab";

    public PTPosteTravailvuegeneraleAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                selectedId = request.getParameter("id");
            }

            /*
             * Creation dynamique de notre viewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            /*
             * pour pouvoir faire un setId remarque : si il y a d'autre set a faire (si plusieurs id par ex) il faut le
             * faire dans le beforeAfficher(...)
             */
            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            /*
             * initialisation du viewBean
             */
            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        if (!response.isCommitted()) {
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        }

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTTravailleurvueglobaleViewBean) {
            PTTravailleurvueglobaleViewBean vb = (PTTravailleurvueglobaleViewBean) viewBean;
            String tab = request.getParameter(TAB);
            vb.setTab(tab);

            List<PosteTravail> listePoste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                    .findByIdTravailleurWithDependencies(vb.getTravailleur().getId());
            for (PosteTravail posteTravail : listePoste) {
                if (!VulpeculaServiceLocator.getUsersService().hasRightAccesSecurity(
                        posteTravail.getEmployeur().getAccesSecurite())) {
                    String destination = "/" + getAction().getApplicationPart() + "?userAction="
                            + getAction().getApplicationPart() + ".postetravail.travailleur.afficher&protected=true";
                    try {
                        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
                        ((FWUrlsStack) session.getAttribute(FWServlet.URL_STACK)).pop();
                    } catch (ServletException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (viewBean instanceof PTEmployeurvueglobaleViewBean) {
            PTEmployeurvueglobaleViewBean vb = (PTEmployeurvueglobaleViewBean) viewBean;
            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                    vb.getEmployeur().getId());
            String tab = request.getParameter(TAB);
            vb.setTab(tab);
            boolean hasRight = VulpeculaServiceLocator.getUsersService().hasRightAccesSecurity(
                    employeur.getAccesSecurite());
            if (!hasRight) {
                String destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + ".postetravail.employeur.afficher&protected=true";
                try {
                    servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
                    ((FWUrlsStack) session.getAttribute(FWServlet.URL_STACK)).pop();
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected void actionCustom(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = "";
        destination = getRelativeURLwithoutClassPart(request, session) + "employeurvueglobale_de.jsp";
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
