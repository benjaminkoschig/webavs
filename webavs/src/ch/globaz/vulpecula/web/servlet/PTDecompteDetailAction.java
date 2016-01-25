package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.decomptedetail.PTDecomptedetailViewBean;
import globaz.vulpecula.vb.decomptedetail.PTLigneDecompteAjaxViewBean;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Hex;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

/**
 * @author Arnaud Geiser (AGE) | Créé le 28 mars 2014
 * 
 */
public class PTDecompteDetailAction extends PTDefaultServletAction {
    private static final String PARAMETER_IDDECOMPTE = "idDecompte";

    private static final String MODE_EDITION = "edition";

    public PTDecompteDetailAction(final FWServlet aServlet) {
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
        if (viewBean instanceof globaz.vulpecula.vb.decomptedetail.PTDecomptedetailViewBean) {
            PTDecomptedetailViewBean vb = (PTDecomptedetailViewBean) viewBean;
            FWViewBeanInterface oldViewbean = (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
            if (oldViewbean instanceof PTDecomptedetailViewBean) {
                PTDecomptedetailViewBean vbOld = (PTDecomptedetailViewBean) session.getAttribute(FWServlet.VIEWBEAN);
                // Si on a demandé un contrôle, on reprend la différence contrôlé de l'ancien sur le nouveau viewBean
                if (vbOld.isControler()) {
                    vb.setDifferenceControle(vbOld.getDifferenceControle());
                }
            }

            String mode = request.getParameter("mode");
            boolean controler = Boolean.valueOf(request.getParameter("controler"));
            vb.setControler(controler);
            if (MODE_EDITION.equals(mode)) {
                vb.setEdition(true);
            }
            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(vb.getDecompte().getId());
            if (!VulpeculaServiceLocator.getUsersService().hasRightAccesSecurity(
                    decompte.getEmployeur().getAccesSecurite())) {
                String destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + ".decompte.decompte.afficher&protected=true";
                try {
                    servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
                    ((FWUrlsStack) session.getAttribute(FWServlet.URL_STACK)).pop();
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (viewBean instanceof PTLigneDecompteAjaxViewBean) {
            PTLigneDecompteAjaxViewBean vb = (PTLigneDecompteAjaxViewBean) viewBean;

            String idDecompte = request.getParameter(PARAMETER_IDDECOMPTE);
            String idPosteTravail = request.getParameter(PTConstants.ID_POSTE_TRAVAIL);
            String nomTravailleur = request.getParameter(PTConstants.NOM_TRAVAILLEUR);

            vb.setIdDecompte(idDecompte);
            vb.setIdPosteTravail(idPosteTravail);
            vb.setNomTravailleur(nomTravailleur);
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTDecomptedetailViewBean) {
            PTDecomptedetailViewBean vb = (PTDecomptedetailViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.decomptedetail.decomptedetail.afficher&selectedId=" + vb.getId();
        }
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    private FWAJAXViewBeanInterface deserializeViewBean(final String hexaSerializedViewBean)
            throws ClassNotFoundException {
        FWAJAXViewBeanInterface viewBean;
        byte[] serializedViewBean;
        try {
            serializedViewBean = Hex.decodeHex(hexaSerializedViewBean.toCharArray());
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serializedViewBean));
            viewBean = (FWAJAXViewBeanInterface) in.readObject();
            in.close();
            return viewBean;
        } catch (org.apache.commons.codec.DecoderException e) {
            throw new ClassNotFoundException("Could not retrieve serialized viewBean : " + e.getMessage());
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not retrieve serialized viewBean : " + e.getMessage());
        }
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTDecomptedetailViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=vulpecula.decompte.decompte.afficher";
        }
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    @Override
    protected void actionSupprimerAJAX(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);
        FWAJAXViewBeanInterface viewBean = null;
        String destination = "";
        try {

            String hexaSerializedViewBean = request.getParameter("viewBean");

            if (hexaSerializedViewBean == null || hexaSerializedViewBean.trim().equalsIgnoreCase("null")
                    || hexaSerializedViewBean.trim().length() == 0) {

                FWAction privateAction = FWAction.newInstance(action);

                viewBean = (FWAJAXViewBeanInterface) FWViewBeanActionFactory.newInstance(privateAction,
                        mainDispatcher.getPrefix());
            } else {
                viewBean = deserializeViewBean(hexaSerializedViewBean);
            }

            if (FWAJAXFindInterface.class.isAssignableFrom(viewBean.getClass())) {
                ((FWAJAXFindInterface) viewBean).initList();
            }
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (FWAJAXViewBeanInterface) beforeSupprimer(session, request, response, viewBean);

            viewBean.setGetListe(true);

            viewBean = (FWAJAXViewBeanInterface) mainDispatcher.dispatch(viewBean, newAction);

            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.hasList()) {
                destination = getAJAXListerSuccessDestination(session, request, viewBean);
            } else {
                destination = getAJAXAfficherSuccessDestination(session, request);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            request.setAttribute("exception", e);
        } finally {
            executeFindForAajaxList(request, viewBean);
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }
}
