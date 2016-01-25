package globaz.corvus.servlet;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.vb.mutation.RECommunicationMutationOaiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRHybridAction;
import globaz.prestation.utils.ged.PRGedUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RECommunicationMutationOaiAction extends PRHybridAction {

    public static final String ACTION_AJOUTER_ANNEXE = "ajouter_annexe";
    public static final String ACTION_SUPPRIMER_ANNEXE = "supprimer_annexe";
    public static final String ACTION_IMPRIMER_ANNONCE = "imprimer_annonce";

    public RECommunicationMutationOaiAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Methode pour lancer l'impression de l'annonce de mutation
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        // R�cup�ration de la liste des annexes
        String action = request.getParameter("action");

        RECommunicationMutationOaiViewBean viewBean = (RECommunicationMutationOaiViewBean) session
                .getAttribute(FWServlet.VIEWBEAN);

        if (!JadeStringUtil.isEmpty(action)) {

            if (action.equals(RECommunicationMutationOaiAction.ACTION_IMPRIMER_ANNONCE)) {

                viewBean.setAdresseOfficeAi(new StringBuffer(request.getParameter("adresseOfficeAI")));
                viewBean.setAdresseActuelleTiers(request.getParameter("adresseActuelleTiers"));
                viewBean.setAdresseAncienneTiers(request.getParameter("adresseAncienneTiers"));
                viewBean.setDateDecesAssure(request.getParameter("dateDecesTiers"));
                viewBean.setDateDebutHospitalisation(request.getParameter("dateDebutHospitalisation"));
                viewBean.setDateFinHospitalisation(request.getParameter("dateFinHospitalisation"));
                viewBean.setDateDebutEntreeHome(request.getParameter("dateDebutHome"));
                viewBean.setDateFinEntreeHome(request.getParameter("dateFinHome"));
                viewBean.setTexteObservation(request.getParameter("texteObservation"));
                viewBean.setListeAnnexe(viewBean.getListeAnnexe());

                viewBean.setSendToGed("on".equals(request.getParameter("sendToGed")));
                viewBean.setIsNouvelleAdresseAssure("on".equals(request.getParameter("isAdresseDomicileTiers")));
                viewBean.setIsNouvelleAdresseRepresentantAutorite("on".equals(request
                        .getParameter("isAdresseRepresentantTiers")));
                viewBean.setChangementNom("on".equals(request.getParameter("changementNom")));
                viewBean.setChangementPrenom("on".equals(request.getParameter("changementPrenom")));
                viewBean.setChangementNSS("on".equals(request.getParameter("changementNSS")));
                viewBean.setChangementAutre("on".equals(request.getParameter("changementAutre")));
                viewBean.setInputChangementAutre(request.getParameter("inputChangementAutre"));
            }
        }
        viewBean = (RECommunicationMutationOaiViewBean) dispatcher.dispatch(viewBean, getAction());
        request.setAttribute("viewBean", viewBean);

        /*
         * choix de la destination
         */
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        String theDestination = "";
        if (goesToSuccessDest) {
            theDestination = _getDestExecuterSucces(session, request, response, viewBean);
        } else {
            theDestination = _getDestExecuterEchec(session, request, response, viewBean);
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(theDestination, request, response);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String action = "/corvus?userAction=corvus.demandes.demandeRenteJointPrestationAccordee.chercher";
        return action;
    }

    /**
     * Methode appel� pour le r�-affichage de la jsp
     */
    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RECommunicationMutationOaiViewBean viewBean = (RECommunicationMutationOaiViewBean) session
                .getAttribute(FWServlet.VIEWBEAN);

        try {
            // R�cup�ration des valeurs de la JSP
            // ==================================
            // r�cup�ration de la JSP de l'adresse de l'office AI
            viewBean.setAdresseOfficeAi(new StringBuffer(request.getParameter("adresseOfficeAI")));
            viewBean.setAdresseActuelleTiers(request.getParameter("adresseActuelleTiers"));
            viewBean.setAdresseAncienneTiers(request.getParameter("adresseAncienneTiers"));

            // r�cup�ration de la JSP de la date de d�c�s
            viewBean.setDateDecesAssure(request.getParameter("dateDecesTiers"));

            // r�cup�ration de la JSP de la date de d�but d'hospitalisation
            viewBean.setDateDebutHospitalisation(request.getParameter("dateDebutHospitalisation"));

            // r�cup�ration de la JSP de la date de fin d'hospitalisation
            viewBean.setDateFinHospitalisation(request.getParameter("dateFinHospitalisation"));

            // r�cup�ration de la JSP de la date d'entr�e au home
            viewBean.setDateDebutEntreeHome(request.getParameter("dateDebutHome"));

            // r�cup�ration de la JSP de la date de sortie du home
            viewBean.setDateFinEntreeHome(request.getParameter("dateFinHome"));

            // r�cup�ration de la JSP du texte d'observation
            viewBean.setTexteObservation(request.getParameter("texteObservation"));

            // r�cup�ration de la JSP de boolean si modification adresse de domicile
            String isAdresseDomicileTiers = request.getParameter("isAdresseDomicileTiers");
            if ("on".equals(isAdresseDomicileTiers)) {
                viewBean.setIsNouvelleAdresseAssure(Boolean.TRUE);
            } else {
                viewBean.setIsNouvelleAdresseAssure(Boolean.FALSE);
            }

            // r�cup�ration de la JSP de boolean si modification adresse de representant
            String isAdresseRepresentantTiers = request.getParameter("isAdresseRepresentantTiers");
            if ("on".equals(isAdresseRepresentantTiers)) {
                viewBean.setIsNouvelleAdresseRepresentantAutorite(Boolean.TRUE);
            } else {
                viewBean.setIsNouvelleAdresseRepresentantAutorite(Boolean.FALSE);
            }

            viewBean.setChangementNom("on".equals(request.getParameter("changementNom")));
            viewBean.setChangementPrenom("on".equals(request.getParameter("changementPrenom")));
            viewBean.setChangementNSS("on".equals(request.getParameter("changementNSS")));
            viewBean.setChangementAutre("on".equals(request.getParameter("changementAutre")));
            viewBean.setInputChangementAutre(request.getParameter("inputChangementAutre"));

            // R�cup�ration de la liste des annexes
            String action = request.getParameter("action");

            if (!JadeStringUtil.isEmpty(action)) {

                if (action.equals(RECommunicationMutationOaiAction.ACTION_AJOUTER_ANNEXE)) {
                    String nouvelAnnexe = request.getParameter("nouvelAnnexe");

                    List<String> listAnnexes = viewBean.getListeAnnexe();
                    listAnnexes.add(nouvelAnnexe);

                    viewBean.setListeAnnexe(listAnnexes);
                } else if (action.equals(RECommunicationMutationOaiAction.ACTION_SUPPRIMER_ANNEXE)) {

                    String texteAnnexeToDelete = request.getParameter("selectedIndex");

                    List<String> lst = viewBean.getListeAnnexe();

                    if (!JadeStringUtil.isEmpty(texteAnnexeToDelete)) {

                        Iterator<String> iter = viewBean.getListeAnnexe().iterator();

                        while (iter.hasNext()) {
                            String currentAnnexe = iter.next();

                            if (currentAnnexe.replaceAll(" ", "").equals(texteAnnexeToDelete.replaceAll(" ", ""))) {
                                lst.remove(currentAnnexe);
                                break;
                            }
                        }
                    }

                    viewBean.setListeAnnexe(lst);

                }
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());

        }

        super.actionReAfficher(session, request, response, mainDispatcher);
    }

    /**
     * Methode appel� � l'affichage initial
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        try {
            // Instanciation du viewBean
            FWViewBeanInterface vb = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            vb.setISession(mainDispatcher.getSession());
            RECommunicationMutationOaiViewBean viewBean = (RECommunicationMutationOaiViewBean) vb;

            // Set valeurs au viewBean
            viewBean.setIdDemande(request.getParameter("selectedId"));
            viewBean.setSession((BSession) mainDispatcher.getSession());
            viewBean.setAction(action.toString());

            // on lui donne les parametres en requete.
            JSPUtils.setBeanProperties(request, viewBean);

            // Mise en GED
            ArrayList<String> docNumbers = new ArrayList<String>();
            docNumbers.add(IRENoDocumentInfoRom.LETTRE_COMMUNICATION_MUTATION_OAI);
            if (PRGedUtils.isDocumentsInGed(docNumbers, (BSession) mainDispatcher.getSession())) {
                viewBean.setDisplaySendToGed(true);
            } else {
                viewBean.setDisplaySendToGed(false);
            }

            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            mainDispatcher.dispatch(viewBean, action);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
