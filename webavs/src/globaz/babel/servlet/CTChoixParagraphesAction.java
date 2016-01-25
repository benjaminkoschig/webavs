/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.servlet;

import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocument;
import globaz.babel.api.doc.ICTScalableDocumentNiveau;
import globaz.babel.api.doc.ICTScalableDocumentPosition;
import globaz.babel.db.cat.CTDocument;
import globaz.babel.db.cat.CTElement;
import globaz.babel.db.cat.CTElementManager;
import globaz.babel.utils.CTTiersUtils;
import globaz.babel.vb.cat.CTTexteListViewBean;
import globaz.babel.vb.cat.CTTexteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class CTChoixParagraphesAction extends CTDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // unused private static final String VERS_ECRAN_RC = "_rc.jsp";

    public static final String ACTION_AJOUTER_ANNEXE = "ajouter_annexe";

    public static final String ACTION_REAFFICHER = "reAfficher";
    public static final String ACTION_SUPPRIMER_ANNEXE = "supprimer_annexe";
    public static final String ACTION_SUPPRIMER_COPIE = "supprimer_copie";

    public static final String RETOUR_PYXIS = "retour_pyxis";

    /*
     * unused private static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
     * HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };
     */
    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTDocumentJointDefaultAnnexesAction.
     * 
     * @param servlet
     */
    public CTChoixParagraphesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionChangeSelection(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        ICTScalableDocument scalableDocument = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);
        String key = request.getParameter("positionKey");

        if (!JadeStringUtil.isIntegerEmpty(key)) {

            Iterator iter = scalableDocument.getScalableDocumentProperties()
                    .getNiveau(scalableDocument.getSelectedNiveau()).getPositionIterator();

            while (iter.hasNext()) {
                ICTScalableDocumentPosition position = (ICTScalableDocumentPosition) iter.next();
                if (key.equals(position.getKey())) {
                    position.setIsSelected(!position.isSelected());
                }
            }
        }

        return "/babelRoot/editableDocument/choixParagraphes_rc.jsp";
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        boolean directToAnnexesCopies = false;

        try {
            CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();
            ICTScalableDocument viewBean = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);

            // recherche des informations sur le type de document
            CTDocument doc = new CTDocument();
            doc.setSession((BSession) mainDispatcher.getSession());
            doc.setIdDocument(viewBean.getScalableDocumentProperties().getIdDocument());
            doc.retrieve();

            viewBean.setDocumentCsDestinataire(doc.getCsDestinataire());
            viewBean.setDocumentCsDomaine(doc.getCsDomaine());
            viewBean.setDocumentCsType(doc.getCsTypeDocument());

            // recherche des niveaux editables de ce document
            CTElementManager elementsManager = new CTElementManager();
            elementsManager.setSession((BSession) mainDispatcher.getSession());
            elementsManager.setForIdDocument(viewBean.getScalableDocumentProperties().getIdDocument());
            elementsManager.setForSelectable(Boolean.TRUE);
            elementsManager.setOrderBy(CTElement.FIELDNAME_NIVEAU + ", " + CTElement.FIELDNAME_POSITION);
            elementsManager.find();

            // si pas de niveau editable, on va directement a l'écran des
            // annexes et copies
            if (elementsManager.size() < 1) {
                directToAnnexesCopies = true;
            } else {
                // on cherche la langue du tiers principal
                String langueTiersPrincipal = CTTiersUtils.getLangueTiersParIdTiers(mainDispatcher.getSession(),
                        viewBean.getScalableDocumentProperties().getIdTiersPrincipal());

                ICTScalableDocumentNiveau niveau = factory.createNewScalableDocumentNiveau();

                // construction de la structure en memoire
                for (int i = 0; i < elementsManager.size(); i++) {
                    ICTScalableDocumentPosition position = factory.createNewScalableDocumentPosition();
                    CTElement element = (CTElement) elementsManager.get(i);

                    // si le 1er element on cree le premier niveau
                    if (JadeStringUtil.isIntegerEmpty(niveau.getNiveau())) {
                        niveau.setNiveau(element.getNiveau());
                        niveau.setDescription(element.getDescription());
                    }

                    // si l'element est d'un autre niveau, on met le niveau
                    // courant dans
                    // la liste des niveaux du document et on cree un nouveau
                    // niveau
                    if (!niveau.getNiveau().equals(element.getNiveau())) {
                        viewBean.getScalableDocumentProperties().addNiveau(niveau);

                        niveau = factory.createNewScalableDocumentNiveau();
                        niveau.setNiveau(element.getNiveau());
                        niveau.setDescription(element.getDescription());
                    }

                    // set les proprietes de la position
                    position.setPosition(element.getPosition());
                    CTTexteListViewBean textManager = new CTTexteListViewBean();
                    textManager.setSession((BSession) mainDispatcher.getSession());
                    textManager.setForIdElement(element.getIdElement());
                    textManager.setForCodeIsoLangue(mainDispatcher.getSession().getCode(langueTiersPrincipal)
                            .toLowerCase());
                    textManager.find();

                    // on ajoute la position
                    if (textManager.size() > 0) {
                        position.setDescription(((CTTexteViewBean) textManager.get(0)).getDescriptionEscaped());
                        position.setIsSelected(((CTTexteViewBean) textManager.get(0)).getIsSelectedByDefault()
                                .booleanValue());
                        niveau.addPosition(position);
                    }

                }

                viewBean.getScalableDocumentProperties().addNiveau(niveau);
            }

            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (((FWViewBeanInterface) viewBean).getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                if (directToAnnexesCopies) {
                    _destination = viewBean.getNextUrl();
                } else {
                    _destination = getRelativeURL(request, session) + "_rc.jsp?" + "_valid=new";
                }
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher("/babelRoot/editableDocument/choixParagraphes_rcListe.jsp")
                .forward(request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionNiveauPrecedant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        ICTScalableDocument scalableDocument = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);
        scalableDocument.setSelectedNiveau(scalableDocument.getSelectedNiveau() - 1);

        return "/babelRoot/editableDocument/choixParagraphes_rc.jsp?_valid=new";
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionNiveauSuivant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        ICTScalableDocument scalableDocument = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);
        scalableDocument.setSelectedNiveau(scalableDocument.getSelectedNiveau() + 1);

        return "/babelRoot/editableDocument/choixParagraphes_rc.jsp?_valid=new";
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void allerVersEcranPrecedent(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String destination = ((ICTScalableDocument) viewBean).getPreviousUrl() + "&_valid=new&_method=add";

        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);
        goSendRedirect(destination, request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void allerVersEcranSuivant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String destination = ((ICTScalableDocument) viewBean).getNextUrl() + "&_valid=new&_method=add";

        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);
        goSendRedirect(destination, request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String arreterGenererDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        try {
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);
        } catch (Exception e) {
            return ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute(FWServlet.VIEWBEAN);

        return ((ICTScalableDocument) viewBean).getReturnUrl();
    }

}
