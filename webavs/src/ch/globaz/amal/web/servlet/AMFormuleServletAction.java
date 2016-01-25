package ch.globaz.amal.web.servlet;

import globaz.amal.vb.formule.AMChampformuleListViewBean;
import globaz.amal.vb.formule.AMChampformuleViewBean;
import globaz.amal.vb.formule.AMFormuleViewBean;
import globaz.amal.vb.formule.AMGenererformuleViewBean;
import globaz.amal.vb.formule.AMImportformuleViewBean;
import globaz.amal.vb.formule.AMSignetListViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.op.wordml.model.document.WordmlDocument;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.envoi.business.models.parametrageEnvoi.GenererFormule;
import ch.globaz.libra.business.services.LibraServiceLocator;

public class AMFormuleServletAction extends AMAbstractServletAction {

    String idSelected = "";

    public AMFormuleServletAction(FWServlet aServlet) {
        super(aServlet);

    }

    // @Override
    // protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    // String destination = "";
    // AMFormuleViewBean formuleViewBean = (AMFormuleViewBean) viewBean;
    // destination = "/amal?userAction=amal.formule.formule.afficher&selectedId=";
    // destination += formuleViewBean.getFormule().getIdFormule();
    // destination += "&_method=add";
    // return destination;
    // }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = "";
        if (request.getParameter("userAction").startsWith("amal.formule.formule.")) {
            AMFormuleViewBean formuleViewBean = (AMFormuleViewBean) viewBean;
            destination = "/amal?userAction=amal.formule.formule.afficher&selectedId=";
            destination += formuleViewBean.getFormule().getIdFormule();
        } else if (request.getParameter("userAction").startsWith("amal.formule.champformule.")) {
            String idFormule = request.getParameter("signetListModel.simpleSignetJointure.idFormule");
            destination = "/amal?userAction=amal.formule.champformule.chercher&selectedId=" + idFormule;
        } else {
            destination = "/amal?userAction=amal.formule.formule.chercher";
        }

        return destination;
    }

    // @Override
    // protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    // String destination = "";
    // AMFormuleViewBean formuleViewBean = (AMFormuleViewBean) viewBean;
    // destination = "/amal?userAction=amal.formule.formule.afficher&selectedId=";
    // destination += formuleViewBean.getFormule().getIdFormule();
    // destination += "&_method=add";
    // return destination;
    // }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = "";
        destination = "/amal?userAction=amal.formule.formule.afficher&selectedId=";
        if (request.getParameter("userAction").startsWith("amal.formule.formule.")) {
            AMFormuleViewBean formuleViewBean = (AMFormuleViewBean) viewBean;
            destination += formuleViewBean.getFormule().getIdFormule();
        } else if (request.getParameter("userAction").startsWith("amal.formule.formulerappel.")) {
            destination = "/amal?userAction=amal.formule.formule.chercher";
        } else {
            destination = "/amal?userAction=amal.formule.formule.chercher";
        }

        return destination;
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = "";
        if (request.getParameter("userAction").startsWith("amal.formule.formulerappel.")) {
            destination = "/amal?userAction=amal.formule.formule.chercher";
            return destination;
        } else if (request.getParameter("userAction").startsWith("amal.formule.champformule.")) {
            AMChampformuleViewBean champviewBean = (AMChampformuleViewBean) viewBean;
            destination = "/amal?userAction=amal.formule.champformule.chercher&selectedId="
                    + champviewBean.getIdFormule();
            // /webavs/amal?csDocument=Accus%E9+r%E9ception+manuel&forIdFormule=1001&userAction=amal.formule.champ
            return destination;
        } else {
            return super._getDestSupprimerSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        if ("amal.formule.champ.afficher".equalsIgnoreCase(getAction().getElement())) {

            AMSignetListViewBean viewBean = new AMSignetListViewBean();
            String idSignet = request.getParameter("idSignet");
            viewBean.getSimpleSignetSearch().setForIdSignet(idSignet);
            try {
                viewBean.find();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
            }
            session.removeAttribute("signetManager");
            session.setAttribute("signetManager", viewBean);
            // session.removeAttribute(arg0)
            super.actionAfficher(session, request, response, mainDispatcher);

        } else if ("amal.formule.formulerappel.afficher".equalsIgnoreCase(getAction().getElement())) {
            addParametersFrom(request, "&_method=add");
            // ParametersFrom(request, url)request.setAttribute("_method", "add");// .getParameterMap().put("_method",
            // "add");
            super.actionAfficher(session, request, response, mainDispatcher);
        }

        else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionAjouter(session, request, response, mainDispatcher);
        // String idSelected = request.getParameter("idSelected");
        // session.setAttribute("dede", "120");

    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        // String selectedId = request.getParameter("selectedId");

        if ("amal.formule.champformule.chercher".equalsIgnoreCase(getAction().getElement())) {
            // HACK
            // request.getParameterMap().put("idSelected", "1000");
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            try {
                String selectedId = request.getParameter("selectedId");
                JSPUtils.setBeanProperties(request, viewBean);
                viewBean = mainDispatcher
                        .dispatch(viewBean, FWAction.newInstance("amal.formule.champformule.lister")/* this.getAction() */);
                ((AMChampformuleViewBean) viewBean).setIdFormuleSearch(selectedId);
                ((AMChampformuleViewBean) viewBean).retrieveName();

                session.setAttribute("viewBean", viewBean);
            } catch (Exception e) {
                JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
            }

        } else if ("amal.formule.journFormule.chercher".equalsIgnoreCase(getAction().getElement())) {
            // HACK
            String selectedId = request.getParameter("selectedId");

            try {
                // JSPUtils.setBeanProperties(request, viewBean);
                FWAction _action = FWAction.newInstance("amal.formule.formule.lister");
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
                // AMFormuleViewBean viewBean = new AMFormuleViewBean();
                ((AMFormuleViewBean) viewBean).setId(selectedId);
                ((AMFormuleViewBean) viewBean).retrieve();

                session.setAttribute("viewBean", viewBean);

            } catch (Exception e) {
                JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
            }

        }

        super.actionChercher(session, request, response, mainDispatcher);

    }

    @Override
    protected void actionExporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub

        String action = request.getParameter("action");
        String selectedId = request.getParameter("selectedId");
        String isBatch = request.getParameter("isBatch");

        try {
            // Importer fichier
            if (action.equalsIgnoreCase("importer")) {

                AMImportformuleViewBean viewBean = (AMImportformuleViewBean) session.getAttribute("viewBean");
                String fileName = request.getParameter("fileName");
                String destination = getRelativeURL(request, session);

                if (isBatch.equalsIgnoreCase("0")) {

                    GenererFormule genererFormule = new GenererFormule();
                    genererFormule.setIdFormule(selectedId);
                    genererFormule.setFileName(fileName);
                    try {
                        viewBean.importer(genererFormule);
                    } catch (Exception e) { // TODO Auto-generated catch block
                        JadeLogger.error("Failed to import file", e);
                    }

                }
                // creation du fichier d index pour la GED

                LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarqueWithTestDossier(
                        "-" + selectedId, "Importation fichier", "remarque", "-" + selectedId, "62300005", false);

                // redirection vers la recherche de formule

                FWViewBeanInterface viewBean2 = FWViewBeanActionFactory.newInstance(getAction(),
                        mainDispatcher.getPrefix());
                try {

                    JSPUtils.setBeanProperties(request, viewBean2);

                    viewBean2 = mainDispatcher.dispatch(viewBean2,
                            FWAction.newInstance("amal.formule.champformule.lister")/* this.getAction() */);
                    session.setAttribute("viewBean", viewBean);
                } catch (Exception e) {
                    JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
                }
                super.actionChercher(session, request, response, mainDispatcher);

            } else if (action.equalsIgnoreCase("exporter")) {

                try {

                    Object viewBean = session.getAttribute("viewBean");
                    ParametreModelComplex parametreModelComplex = ((AMFormuleViewBean) viewBean)
                            .getParametreModelComplex();
                    if (!(viewBean instanceof AMGenererformuleViewBean)) {
                        viewBean = new AMGenererformuleViewBean();
                        ((AMGenererformuleViewBean) viewBean).setParametreModelComplex(parametreModelComplex);
                    } // else {
                      // viewBean = (AMGenererformuleViewBean)viewBean;
                      // }
                    String fileName = request.getParameter("fileName");
                    String destination = getRelativeURL(request, session);

                    // Interactif
                    if (!((AMGenererformuleViewBean) viewBean).isBatch()) {
                        GenererFormule genererFormule = new GenererFormule();
                        genererFormule.setIdFormule(selectedId);
                        genererFormule.setFileName(fileName);

                        genererFormule = ((AMGenererformuleViewBean) viewBean).exporter(genererFormule);
                        response.setContentType("application/word");

                        String nameFile = ((AMGenererformuleViewBean) viewBean).getParametreModelComplex()
                                .getFormuleList().getFormule().getLibelleDocument();
                        nameFile += ".xml";

                        response.setHeader("Content-Disposition", "attachment; filename=" + nameFile + "");
                        genererFormule.getFichier().write(response.getOutputStream());

                    } else {

                        /*
                         * GenererFormule genererFormule = new GenererFormule();
                         * genererFormule.setIdFormule(selectedId); genererFormule.setFileName(fileName); // try {
                         * 
                         * 
                         * /* genererFormule = ((AMGenererformuleViewBean) viewBean).exporter(genererFormule);
                         * response.setContentType("application/word"); response.setHeader("Content-Disposition",
                         * "attachment; filename=\"fichier.doc");
                         * genererFormule.getFichier().write(response.getOutputStream());fin interactif/
                         * 
                         * /* response.setContentType("application/download; charset=windows-1252");
                         * response.setHeader("Content-Disposition", "attachment; filename=\"fichier");
                         * response.setCharacterEncoding("windows-1252");
                         */

                        /*
                         * response.setContentType("text/plain"); response.setHeader("Content-Disposition",
                         * "attachment; filename=\"fichier");
                         * 
                         * File file = new File("C:\\ACREPC1.odt"); FileInputStream fileIn = new FileInputStream(file);
                         * 
                         * ServletOutputStream out = response.getOutputStream();
                         * 
                         * byte[] outputByte = new byte[4096]; // copy binary contect to output stream while
                         * (fileIn.read(outputByte, 0, 4096) != -1) { response.getOutputStream().write(outputByte, 0,
                         * 4096); } fileIn.close(); out.flush(); out.close();
                         */

                        String pathToRead = session.getServletContext().getRealPath("/");
                        String lastChar = pathToRead.substring(pathToRead.length() - 1);
                        if (lastChar.equals("/") || lastChar.equals("\\")) {
                            pathToRead += "topazTemplates/amal/";
                        } else {
                            pathToRead += "/topazTemplates/amal/";
                        }
                        String nameFile = ((AMGenererformuleViewBean) viewBean).getParametreModelComplex()
                                .getFormuleList().getFormule().getLibelleDocument();
                        String nameDoc = nameFile;
                        nameFile += ".odt";
                        pathToRead += nameFile;
                        InputStream is = new FileInputStream(pathToRead);
                        OutputStream os = response.getOutputStream();

                        response.setContentType("application/download");
                        response.setHeader("Content-Disposition", "attachement;filename=" + nameDoc);

                        int count;
                        byte buf[] = new byte[4096];
                        while ((count = is.read(buf)) > 1) {
                            os.write(buf, 0, count);
                        }
                        is.close();
                        os.close();
                    }

                } catch (Exception e) {
                    JadeLogger.error("Failed to export file", e);
                }

            } else if (action.equalsIgnoreCase("merge")) {

                try {

                    /*
                     * AMDetailfamilleViewBean viewBean = new AMDetailfamilleViewBean(); viewBean.setId("465406");
                     * viewBean.retrieve();
                     * 
                     * viewBean.setIdContribuable(viewBean.getDetailFamille().getIdContribuable());
                     * viewBean.retrieveContribuable();
                     * 
                     * viewBean.setIdFamille(viewBean.getDetailFamille().getIdFamille());
                     * viewBean.retrieveFamilleContribuable();
                     * 
                     * viewBean.getContribuable().getPersonneEtendue().getTiers().getIdTiers();
                     */
                    AMGenererformuleViewBean viewBean2 = (AMGenererformuleViewBean) session.getAttribute("viewBean");

                    response.setContentType("application/word");
                    response.setHeader("Content-Disposition", "attachment; filename=\"test.doc");
                    WordmlDocument doc = (WordmlDocument) viewBean2.merge(new Object());
                    doc.write(response.getOutputStream());
                    // docToMerge.mergeDocument(new AIKeyValuesContainer(container));

                } catch (Exception e) {
                    JadeLogger.error("Failed to export file", e);
                }

            } else {
                super.actionExporter(session, request, response, mainDispatcher);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            JadeLogger.error("Failed in action  export", e);
            // e.printStackTrace();
        }
        // super.actionExporter(session, request, response, mainDispatcher);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionModifier(session, request, response, mainDispatcher);
    }

    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        return super.addParametersFrom(request, url);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub idChampFormule= idFormule
        /*
         * String idChampFormule = request.getParameter("idChampFormule"); String idFormule =
         * request.getParameter("idFormule");
         * 
         * if (!JadeStringUtil.isEmpty(selectedId)) { AMFormuleViewBean formuleViewBean = new AMFormuleViewBean();
         * formuleViewBean.setId(idFormule); if (!JadeStringUtil.isEmpty(contribuableId)) {
         * revenuViewBean.setIdContribuable(contribuableId); } return revenuViewBean; } else { return null; }
         */
        if ("amal.formule.champformule.afficher".equalsIgnoreCase(getAction().getElement())) {
            // HACK - il faut chercher l'information nécessaire pour savoir si
            // le droit doit être créé (bouton nouveau = creation droit
            // initial). Le viewbean est créé afin d'appeller le helper et
            // chercher le nombre de version du droit
            String idFormule = request.getParameter("idFormule");
            String idSignet = request.getParameter("idSignet");
            try {

                JSPUtils.setBeanProperties(request, viewBean);

                ((AMChampformuleViewBean) viewBean).setIdFormuleSearch(idFormule);
                ((AMChampformuleViewBean) viewBean).setIdSignetSearch(idSignet);
                ((AMChampformuleViewBean) viewBean).retrieve();

                session.setAttribute("viewBean", viewBean);
            } catch (Exception e) {
                JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
            }

        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        if (viewBean instanceof AMChampformuleListViewBean) {
            AMChampformuleListViewBean viewBean2 = (AMChampformuleListViewBean) viewBean;
            viewBean2.getSignetListModelSearch().setForIdFormule(request.getParameter("forIdFormule"));
            return super.beforeLister(session, request, response, viewBean2);
        }

        return super.beforeLister(session, request, response, viewBean);

    }

}
