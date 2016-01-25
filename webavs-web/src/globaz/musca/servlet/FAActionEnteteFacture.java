package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureListViewBean;
import globaz.musca.db.facturation.FAEnteteFactureViewBean;
import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionEnteteFacture extends FWDefaultServletAction {
    public final static String ACTION_RELOAD = "reload";

    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionEnteteFacture(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Méthode redéfinie pour obtenir une redirection sur la page des afacts quand on vient de créer un décompte. Date
     * de création : (26.03.2003 09:09:06)
     * 
     * @param session
     *            javax.servlet.http.HttpSession
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     * @param mainDispatcher
     *            globaz.framework.controller.FWDispatcher
     * @exception javax.servlet.ServletException
     *                La description de l'exception.
     * @exception java.io.IOException
     *                La description de l'exception.
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        try {
            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            // GESTION DES DROITS
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            // Rajouter l'id passer en paramètre dans le viewBean
            FAEnteteFactureViewBean entete = (FAEnteteFactureViewBean) viewBean;
            entete.setISession(mainDispatcher.getSession());
            session.setAttribute("saveIdEntete", entete.getIdEntete());

            /*
             * chois de la destination _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja
             * rempli par l'utilisateur
             */

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                // _destination=
                // getRelativeURL(request,session)+"_de.jsp?_valid=fail&_back=sl";
                _destination = getActionFullURL() + ".reAfficher";
            } else {
                // redirection sur la création d'afact
                int indx = getActionFullURL().indexOf("enteteFacture");
                _destination = getActionFullURL().substring(0, indx - 1);
                _destination += ".afact.afficher&_method=add&_valid=_new";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
            JadeLogger.error(this, e);
        }

        /*
         * redirection vers la destination
         */

        // servlet.getServletContext().getRequestDispatcher
        // (_destination).forward (request, response);
        goSendRedirect(_destination, request, response);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        FAEnteteFactureViewBean viewBean = new FAEnteteFactureViewBean();
        viewBean.setIdPassage(request.getParameter("idPassage"));
        // Sauvegarde de l'id passage passé en paramètre pour le récupérer lors
        // de la création
        session.setAttribute("saveIdPassage", request.getParameter("idPassage"));
        //
        viewBean.setMessage("OK");
        viewBean.setMsgType(FWViewBeanInterface.OK);

        // --- Check view bean

        // si on est dans le context d'une selection, on ajoute l'action dans
        // l'objet globazContext de la session
        if (request.getParameter("selectorName") != null
                && request.getParameter("selectorName").equals("enteteFactureImSelector")) {
            Hashtable parentSession = (Hashtable) session.getAttribute("globazContext");
            parentSession.put("customerAction", getAction());
            // parentSession.put(FWSelectorTag.ATTRIBUT_SELECTOR_CUSTOMERURL,
            // session.getAttribute("redirectUrl"));
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        // Dispatch
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                .forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        if (ACTION_RELOAD.equals(getAction().getActionPart())) {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            try {
                JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        // rechercher dans les properties le nombre d'entête à lister

        FAEnteteFactureListViewBean vBean = (FAEnteteFactureListViewBean) viewBean;

        vBean.changeManagerSize(10); // TBD avec un getProperty
        // Traitement de la zone de recherche "A partir de" dans la langue de
        // l'utilisateur
        vBean.setForIdRole(request.getParameter("forIdRole"));
        vBean.setFromTotalFacture(request.getParameter("fromTotalFacture"));
        vBean.setForIdPassage((String) session.getAttribute("saveIdPassage"));
        vBean.setOrderBy(request.getParameter("triDecomptePassage"));
        vBean.setForTriDecompte(request.getParameter("triDecompte"));
        // Tri par nom de débiteur
        if (request.getParameter("triDecomptePassage").equalsIgnoreCase(FAEnteteFacture.CS_TRI_NOM)) {
            vBean.setFromNom(request.getParameter("fromLibelle"));
        }
        // Tri par n° de débiteur
        if (request.getParameter("triDecomptePassage").equalsIgnoreCase(FAEnteteFacture.CS_TRI_DEBITEUR)) {
            vBean.setFromIdExterneRole(request.getParameter("fromLibelle"));
        }
        // Tri par décompte
        if (request.getParameter("triDecomptePassage").equalsIgnoreCase(FAEnteteFacture.CS_TRI_NUMERO_DECOMTPE)) {
            vBean.setFromIdExterneFacture(request.getParameter("fromLibelle"));
        }
        return vBean;
    }

    /*
     * Traitement lors d'une création avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAEnteteFactureViewBean vBean = (FAEnteteFactureViewBean) viewBean;
        // Initialiser les valeurs par défaut
        vBean.initDefaultValues();
        // Récupération de l'id du passage
        vBean.setIdPassage((String) session.getAttribute("saveIdPassage"));
        return vBean;
    }

}
