/*
 * Créé le 24 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.droits.APPereMatListViewBean;
import globaz.apg.vb.droits.APPereMatViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APPereMatAction extends APAbstractDroitDTOAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPereMatAction.
     * 
     * @param servlet
     */
    public APPereMatAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWAction.ACTION_CHERCHER);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return _getDestAjouterSucces(session, request, response, viewBean);
    }

    protected void actionAfficherPere(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = ERROR_PAGE;
        String sAction = request.getParameter("userAction");
        FWAction action = FWAction.newInstance(sAction);

        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");

            /*
             * Creation dynamique de notre viewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());

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
            if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            } else {
                initViewBean(viewBean, loadDTO(session), session);
                // Charge le pere s'il existe déjà
                APPereMatViewBean pere = (APPereMatViewBean) viewBean;
                APPereMatListViewBean mgr = new APPereMatListViewBean();

                mgr.setSession((BSession) mainDispatcher.getSession());
                mgr.setForIdDroitMaternite(pere.getIdDroitMaternite());
                mgr.find();

                if (!mgr.isEmpty()) {
                    pere.copyDataFromEntity((APPereMatViewBean) mgr.get(0));
                }
            }
            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

        //
        // FWViewBeanInterface viewBean = null;
        // String sAction = request.getParameter ("userAction");
        // FWAction action = FWAction.newInstance(sAction);
        // action.changeActionPart(FWAction.ACTION_AFFICHER);
        //
        // /* pour compatibilité :
        // * si on a le parametre _method=add, c'est que l'on a une action
        // nouveau
        // */
        // String selectedId = request.getParameter ("selectedId");
        // /*
        // * Creation dynamique de notre viewBean
        // */
        // viewBean =
        // FWViewBeanActionFactory.newInstance(action,mainDispatcher.getPrefix());
        // /*
        // * pour pouvoir faire un setId
        // * remarque : si il y a d'autre set a faire (si plusieurs id par ex)
        // * il faut le faire dans le beforeAfficher(...)
        // */
        // Class b = Class.forName("globaz.globall.db.BIPersistentObject");
        // Method mSetId = b.getDeclaredMethod("setId", new Class[]
        // {String.class} );
        // mSetId.invoke(viewBean, new Object[] {selectedId});
        //
        //
        // /*
        // * appelle beforeAfficher, puis le Dispatcher, puis met le bean en
        // session
        // */
        // saveViewBean(viewBean, session);
        // viewBean = beforeAfficher(session,request,response,viewBean);
        // viewBean = mainDispatcher.dispatch(viewBean,action);
        // session.removeAttribute("viewBean");
        // session.setAttribute ("viewBean", viewBean);
        //
        // /*
        // * choix destination
        // */
        // if (viewBean.getMsgType().equals(FWViewBean.ERROR) == true) {
        // destination = ERROR_PAGE;
        // } else {
        // destination = getRelativeURL(request,session)+"_de.jsp";
        // }
        //
        // } catch (Exception e) {
        // JadeLogger.error(this ,e);
        // destination = ERROR_PAGE;
        // }
        //
        // /*
        // * redirection vers la destination
        // */
        // servlet.getServletContext().getRequestDispatcher
        // (destination).forward (request, response);
    }

    @Override
    protected void deleguerActionAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        actionAfficherPere(session, request, response, mainDispatcher);
    }

    /**
     * @see globaz.apg.servlet.APAbstractDroitDTOAction#getViewBeanClass()
     */
    @Override
    protected Class getViewBeanClass() {
        return APPereMatViewBean.class;
    }

    /**
     * @see globaz.apg.servlet.APAbstractDroitDTOAction#initViewBean(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.apg.vb.droits.APDroitDTO, javax.servlet.http.HttpSession)
     */
    @Override
    protected FWViewBeanInterface initViewBean(FWViewBeanInterface viewBean, APDroitDTO droitDTO, HttpSession session) {
        ((APPereMatViewBean) viewBean).setDroitDTO(droitDTO);

        return viewBean;
    }

}
