/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.servlet;

import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocument;
import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.babel.api.doc.ICTScalableDocumentGenerator;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.babel.db.annexes.CTDocumentJointDefaultAnnexes;
import globaz.babel.db.annexes.CTDocumentJointDefaultAnnexesManager;
import globaz.babel.db.cat.CTDocument;
import globaz.babel.db.copies.CTDocumentJointDefaultCopies;
import globaz.babel.db.copies.CTDocumentJointDefaultCopiesManager;
import globaz.babel.db.intervenants.CTDemandeJointIntervenants;
import globaz.babel.db.intervenants.CTDemandeJointIntervenantsManager;
import globaz.babel.process.CTAbstractJadeJob;
import globaz.babel.utils.CTTiersUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.publish.client.JadePublishDocument;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class CTChoixAnnexesCopiesAction extends CTDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String ACTION_AJOUTER_ANNEXE = "ajouter_annexe";

    public static final String ACTION_REAFFICHER = "reAfficher";
    public static final String ACTION_SUPPRIMER_ANNEXE = "supprimer_annexe";
    public static final String ACTION_SUPPRIMER_COPIE = "supprimer_copie";

    public static final String RETOUR_PYXIS = "retour_pyxis";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTDocumentJointDefaultAnnexesAction.
     * 
     * @param servlet
     */
    public CTChoixAnnexesCopiesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
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

            // recherche des annexes par defaut
            CTDocumentJointDefaultAnnexesManager annexesManager = new CTDocumentJointDefaultAnnexesManager();
            annexesManager.setSession((BSession) mainDispatcher.getSession());
            annexesManager.setForIdDocument(viewBean.getScalableDocumentProperties().getIdDocument());
            annexesManager.find();

            Iterator iter = annexesManager.iterator();
            while (iter.hasNext()) {
                CTDocumentJointDefaultAnnexes annexe = (CTDocumentJointDefaultAnnexes) iter.next();
                ICTScalableDocumentAnnexe scalableAnnexe = factory.createNewScalableDocumentAnnexe();
                scalableAnnexe.setLibelle(annexe.getCsAnnexeLibelle());

                viewBean.getScalableDocumentProperties().addAnnexe(scalableAnnexe);
            }

            // recherche des copies par defaut
            CTDocumentJointDefaultCopiesManager copiesManager = new CTDocumentJointDefaultCopiesManager();
            copiesManager.setSession((BSession) mainDispatcher.getSession());
            copiesManager.setForIdDocument(viewBean.getScalableDocumentProperties().getIdDocument());
            copiesManager.find();

            iter = copiesManager.iterator();
            while (iter.hasNext()) {
                CTDocumentJointDefaultCopies copie = (CTDocumentJointDefaultCopies) iter.next();
                ICTScalableDocumentCopie scalableCopie = factory.createNewScalableDocumentCopie();

                // l'intervenant
                scalableCopie.setCsIntervenant(copie.getCsTypeIntervenant());

                // on cherche le tiers correspondant a ce type d'intervenant
                // pour le tiers principal
                CTDemandeJointIntervenantsManager manager = new CTDemandeJointIntervenantsManager();
                manager.setSession((BSession) mainDispatcher.getSession());
                manager.setForIdDemande(viewBean.getIdDemande());
                manager.setForCsTypeIntervenant(copie.getCsTypeIntervenant());
                manager.find();

                if (manager != null && manager.size() > 0) {
                    CTDemandeJointIntervenants demInt = (CTDemandeJointIntervenants) manager.get(0);

                    if (!JadeStringUtil.isIntegerEmpty(demInt.getIdTiersIntervenant())) {
                        scalableCopie.setIdTiers(demInt.getIdTiersIntervenant());
                        String nom = CTTiersUtils.getPrenomNomTiersParIdTiers(mainDispatcher.getSession(),
                                demInt.getIdTiersIntervenant());
                        scalableCopie.setPrenomNomTiers(nom);
                    } else {
                        scalableCopie.setPrenomNomTiers("Pas de tiers défini!");
                        scalableCopie.setIdTiers("");
                    }
                } else {
                    scalableCopie.setPrenomNomTiers("Pas de tiers défini!");
                    scalableCopie.setIdTiers("");
                }

                viewBean.getScalableDocumentProperties().addCopie(scalableCopie);
            }

            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (((FWViewBeanInterface) viewBean).getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp?" + "_valid=new";
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
     * @see globaz.framework.controller.FWDefaultServletAction#actionReAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();
            ICTScalableDocument viewBean = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);

            // traitement des actions ajouter et supprimer sur les annexes et
            // les copies
            String action = request.getParameter("action");

            // retour des tiers
            if (Boolean.TRUE.toString().equals(viewBean.getScalableDocumentProperties().getParameter(RETOUR_PYXIS))) {

                if (!JadeStringUtil.isEmpty(viewBean.getIdTiersIntervenant())) {
                    ICTScalableDocumentCopie scalableCopie = factory.createNewScalableDocumentCopie();
                    scalableCopie.setIdTiers(viewBean.getIdTiersIntervenant());

                    String nom = CTTiersUtils.getPrenomNomTiersParIdTiers(mainDispatcher.getSession(),
                            viewBean.getIdTiersIntervenant());
                    scalableCopie.setPrenomNomTiers(nom);

                    viewBean.getScalableDocumentProperties().addCopie(scalableCopie);
                    viewBean.setDocumentsPreview(null);
                }

                viewBean.getScalableDocumentProperties().setParameter(RETOUR_PYXIS, Boolean.FALSE.toString());
            } else if (!JadeStringUtil.isEmpty(action)) {

                if (action.equals(ACTION_AJOUTER_ANNEXE)) {
                    String annexe = request.getParameter("nouvelAnnexe");

                    if (!JadeStringUtil.isEmpty(annexe)) {
                        ICTScalableDocumentAnnexe scalableAnnexe = factory.createNewScalableDocumentAnnexe();
                        scalableAnnexe.setLibelle(annexe);
                        viewBean.getScalableDocumentProperties().addAnnexe(scalableAnnexe);
                    }
                    viewBean.setDocumentsPreview(null);

                } else if (action.equals(ACTION_SUPPRIMER_ANNEXE)) {
                    String key = request.getParameter("selectedIndex");

                    if (!JadeStringUtil.isIntegerEmpty(key)) {
                        Iterator iter = viewBean.getScalableDocumentProperties().getAnnexesIterator();

                        while (iter.hasNext()) {
                            ICTScalableDocumentAnnexe currentAnnexe = (ICTScalableDocumentAnnexe) iter.next();

                            if (currentAnnexe.getKey().equals(key)) {
                                viewBean.getScalableDocumentProperties().removeAnnexe(currentAnnexe);
                                break;
                            }
                        }
                    }
                    viewBean.setDocumentsPreview(null);

                } else if (action.equals(ACTION_SUPPRIMER_COPIE)) {
                    String key = request.getParameter("selectedIndex");

                    if (!JadeStringUtil.isIntegerEmpty(key)) {
                        Iterator iter = viewBean.getScalableDocumentProperties().getCopiesIterator();

                        while (iter.hasNext()) {
                            ICTScalableDocumentCopie currentCopie = (ICTScalableDocumentCopie) iter.next();

                            if (currentCopie.getKey().equals(key)) {
                                viewBean.getScalableDocumentProperties().removeCopie(currentCopie);
                                break;
                            }
                        }
                    }
                    viewBean.setDocumentsPreview(null);
                }
            }

            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (((FWViewBeanInterface) viewBean).getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp?" + "_valid=new";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * ecrase une des valeurs sauvee dans la session par FWSelectorTag de telle sorte que l'on sache exactement quelle
     * action sera executee lorsque l'on revient de pyxis et avec quels parametres.
     * 
     * @see FWSelectorTag
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // apres le retour depuis pyxis -> reafficher
        String attribute = (String) session.getAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL);

        // HACK pour enlever la remarque de ATTRIBUT_SELECTOR_CUSTOMERURL
        int i1 = attribute.indexOf("remarque");

        if (i1 >= 0) {
            String newAttribute = attribute.substring(i1, attribute.length());
            int i2 = newAttribute.indexOf("&idDemande");

            if (i2 >= 0) {
                newAttribute = newAttribute.substring(i2, newAttribute.length());
                attribute = attribute.substring(0, i1 - 1) + newAttribute;
            }
        }

        attribute = attribute.replaceAll("afficherDocument", "afficher");
        attribute = attribute.replaceAll("afficher", ACTION_REAFFICHER);

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, attribute);

        ICTScalableDocument viewBean = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);
        viewBean.setDocumentsPreview(null);
        viewBean.getScalableDocumentProperties().setParameter(RETOUR_PYXIS, Boolean.TRUE.toString());
        session.setAttribute(FWServlet.VIEWBEAN, viewBean);

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
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
    public String afficherDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        // generer le document
        try {
            ICTScalableDocument scalableDocVB = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);

            Class c = Class.forName(((ICTScalableDocument) viewBean).getGeneratorImplCalssName());
            BProcess documentGenerator = (BProcess) c.newInstance();

            documentGenerator.setSession((BSession) mainDispatcher.getSession());
            documentGenerator.setEMailAddress(scalableDocVB.getEMailAddress());

            // on retire les copies qui n'ont pas d'id tiers
            removeCopiesWithoutIdTiers(scalableDocVB.getScalableDocumentProperties());

            ((ICTScalableDocumentGenerator) documentGenerator).setScalableDocumentProperties(scalableDocVB
                    .getScalableDocumentProperties());
            documentGenerator.executeProcess();

            List attachedDocumentsList = documentGenerator.getAttachedDocuments();

            for (int i = attachedDocumentsList.size() - 1; i >= 0; i--) {

                JadePublishDocument currentDoc = (JadePublishDocument) attachedDocumentsList.get(i);

                if (!currentDoc.getPublishJobDefinition().getDocumentInfo().getPublishDocument()) {
                    attachedDocumentsList.remove(i);
                }

            }

            if (documentGenerator.getAttachedDocuments() != null && documentGenerator.getAttachedDocuments().size() > 0) {

                scalableDocVB.setDocumentsPreview(documentGenerator.getAttachedDocuments());
            } else {
                scalableDocVB.setDocumentsPreview(null);
            }

            getAction().changeActionPart(FWAction.ACTION_AFFICHER);
        } catch (Exception e) {
            return ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());

        return getRelativeURL(request, session) + "_de.jsp?" + "_valid=new";
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

        ((ICTScalableDocument) viewBean).setDocumentsPreview(null);

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
            ICTScalableDocument scalableDocVB = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);
            scalableDocVB.getScalableDocumentProperties().setParameter("processAction", "SAVE_ONLY");
            Class c = Class.forName(((ICTScalableDocument) viewBean).getGeneratorImplCalssName());

            if (c.newInstance() instanceof BProcess) {

                BProcess documentGeneratorBProcess = (BProcess) c.newInstance();
                documentGeneratorBProcess.setSession((BSession) mainDispatcher.getSession());

                ((ICTScalableDocumentGenerator) documentGeneratorBProcess).setScalableDocumentProperties(scalableDocVB
                        .getScalableDocumentProperties());
                BProcessLauncher.start(documentGeneratorBProcess);

            } else if (c.newInstance() instanceof AbstractJadeJob) {

                AbstractJadeJob documentGeneratorAbstractJadeJob = (AbstractJadeJob) c.newInstance();
                BSession bSession = (BSession) mainDispatcher.getSession();
                bSession.setAttribute(FWServlet.VIEWBEAN, scalableDocVB);
                documentGeneratorAbstractJadeJob.setSession(bSession);

                BProcessLauncher.start(documentGeneratorAbstractJadeJob, false);
            }
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);
        } catch (Exception e) {
            try {
                getAction().changeActionPart(FWAction.ACTION_AFFICHER);
                mainDispatcher.dispatch(viewBean, getAction());
                session.removeAttribute(FWServlet.VIEWBEAN);
                return ((ICTScalableDocument) viewBean).getReturnUrl();
            } catch (Exception e2) {
                return ERROR_PAGE;
            }
        }
        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute(FWServlet.VIEWBEAN);
        return ((ICTScalableDocument) viewBean).getReturnUrl();
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
    public String genererDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        // generer le document
        try {
            ICTScalableDocument scalableDocVB = (ICTScalableDocument) session.getAttribute(FWServlet.VIEWBEAN);

            Class c = Class.forName(((ICTScalableDocument) viewBean).getGeneratorImplCalssName());

            if (c.newInstance() instanceof BProcess) {

                BProcess documentGeneratorBProcess = (BProcess) c.newInstance();
                documentGeneratorBProcess.setSession((BSession) mainDispatcher.getSession());
                documentGeneratorBProcess.setEMailAddress(scalableDocVB.getEMailAddress());

                // on retire les copies qui n'ont pas d'id tiers
                removeCopiesWithoutIdTiers(scalableDocVB.getScalableDocumentProperties());

                ((ICTScalableDocumentGenerator) documentGeneratorBProcess).setScalableDocumentProperties(scalableDocVB
                        .getScalableDocumentProperties());
                BProcessLauncher.start(documentGeneratorBProcess);

            } else if (c.newInstance() instanceof AbstractJadeJob) {

                BSession bSession = (BSession) mainDispatcher.getSession();
                bSession.setAttribute(FWServlet.VIEWBEAN, scalableDocVB);

                if (c.newInstance() instanceof CTAbstractJadeJob) {
                    CTAbstractJadeJob documentGeneratorAbstractJadeJob = (CTAbstractJadeJob) c.newInstance();
                    documentGeneratorAbstractJadeJob.setSession(bSession);

                    documentGeneratorAbstractJadeJob.setScalableDocumentProperties(scalableDocVB
                            .getScalableDocumentProperties());

                    BProcessLauncher.start(documentGeneratorAbstractJadeJob, false);
                } else {
                    AbstractJadeJob documentGeneratorAbstractJadeJob = (AbstractJadeJob) c.newInstance();
                    bSession.setAttribute(FWServlet.VIEWBEAN, scalableDocVB);
                    documentGeneratorAbstractJadeJob.setSession(bSession);

                    BProcessLauncher.start(documentGeneratorAbstractJadeJob, false);
                }
            }

            getAction().changeActionPart(FWAction.ACTION_AFFICHER);
        } catch (Exception e) {
            return ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());

        session.removeAttribute(FWServlet.VIEWBEAN);

        return ((ICTScalableDocument) viewBean).getReturnUrl();
    }

    /**
     * Retire les copies qui n'ont pas d'idTiers
     * 
     * @param documentProperties
     */
    private void removeCopiesWithoutIdTiers(ICTScalableDocumentProperties documentProperties) {

        Iterator copiesIter = documentProperties.getCopiesIterator();

        while (copiesIter.hasNext()) {
            ICTScalableDocumentCopie copie = (ICTScalableDocumentCopie) copiesIter.next();

            // si pas d'idTiers on retire la copie de la liste
            if (JadeStringUtil.isIntegerEmpty(copie.getIdTiers())) {
                copiesIter.remove();
            }
        }
    }
}
