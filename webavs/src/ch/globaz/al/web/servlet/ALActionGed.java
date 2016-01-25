package ch.globaz.al.web.servlet;

import globaz.al.vb.dossier.ALDossierMainViewBean;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWActionGedResult;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedCallDefinition;
import globaz.jade.ged.message.JadeGedCallType;
import globaz.jade.ged.message.JadeGedService;
import globaz.jade.ged.target.JadeGedTargetProperties;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.al.business.services.ALServiceLocator;

public class ALActionGed extends ALAbstractDefaultAction {

    public ALActionGed(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        try {
            // System.out.println("*-*-*-*-*-*-*-*-*-*-*-*");
            // System.out.println("* début afficher GED *");
            // System.out.println("*-*-*-*-*-*-*-*-*-*-*-*");

            String blankStr = "";
            // récup du noAffilie depuis la request
            String noAffilie = request.getParameter("noAffiliationId");
            String noRole = request.getParameter("idRole");
            if (JadeStringUtil.isIntegerEmpty(noRole)) {
                noRole = request.getParameter(TIDocumentInfoHelper.ROLE_TIERS_DOCUMENT);
                if (JadeStringUtil.isIntegerEmpty(noRole)) {
                    noRole = "";
                }
            }

            Object vb = session.getAttribute("viewBean");
            ALDossierMainViewBean dossierVB = null;
            if (ALDossierMainViewBean.class.isAssignableFrom(vb.getClass())) {
                dossierVB = (ALDossierMainViewBean) vb;
            }
            if (JadeStringUtil.isBlank(noAffilie)) {
                if (JadeStringUtil.isBlank(noAffilie)) {
                    noAffilie = request.getParameter(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE);
                    if (noAffilie == null) {
                        noAffilie = blankStr;
                        // System.out.println(" ***** pas de numéro d'affilié ("
                        // +
                        // noAffilie + ") !!");
                    } else {
                        // System.out.println(" ***** on a récupéré un numéro d'affilié ("
                        // + noAffilie +
                        // ") depuis la requête (num role formatté)");
                    }
                }
            } else {
                // System.out.println(" ***** on a récupéré un numéro d'affilié ("
                // +
                // noAffilie + ") depuis la requête.");
            }
            // récup du noAVS depuis la request
            String noAVS = request.getParameter("noAVSId");
            if (noAVS == null) {
                noAVS = request.getParameter(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
                if (noAVS == null) {
                    noAVS = blankStr;
                }
            }
            // récup de l'année depuis la request
            String annee = request.getParameter("annee");
            if (annee == null) {
                // annee =
                // request.getParameter(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
                if (annee == null) {
                    annee = blankStr;
                }
            }
            // récup du nomService depuis la request
            String nomService = request.getParameter("serviceNameId");
            String folderType = null;

            if (request.getParameter("userAction").endsWith("allocataire")) {
                folderType = "allocataire";
            } else {
                folderType = "affilie";
            }

            if (JadeStringUtil.isEmpty(nomService)) {
                if (request.getParameter("userAction").endsWith("allocataire")) {
                    nomService = "ALLOC";
                } else {
                    nomService = "AFFIL";
                }
            }

            // vérifier que le service se trouve dans la liste
            boolean isInList = false;

            List services = JadeGedFacade.getServicesList();
            for (Iterator iterator = services.iterator(); iterator.hasNext();) {
                JadeGedService service = (JadeGedService) iterator.next();
                if (service.toString().equals(nomService)) {
                    isInList = true;
                }
            }
            if (!isInList) {
                nomService = blankStr;
            }

            // création des props à passer à la GED
            Properties gedCallProps = new Properties();
            // on veut montrer un dossier: ACTION pour les deux GED
            gedCallProps.setProperty(JadeGedTargetProperties.ACTION, JadeGedTargetProperties.ACTION_OPEN_FOLDER);
            // propriété TI:groupdoc: nom correspondant à un folder
            gedCallProps.setProperty(JadeGedTargetProperties.FOLDER_TYPE, folderType);

            // ajout du nom de service, si on le connait, permet d'éviter la
            // page de
            // sélection du service
            gedCallProps.setProperty(JadeGedTargetProperties.SERVICE, nomService);
            // propriétés business, correspondant à des indexes
            gedCallProps.setProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, noAffilie);

            gedCallProps.setProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                    JadeStringUtil.removeChar(noAffilie, '.'));
            gedCallProps.setProperty(TIDocumentInfoHelper.ROLE_TIERS_DOCUMENT, noRole);
            // indépendant
            gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                    JadeStringUtil.removeChar(noAVS, '.'));
            gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, noAVS);
            if (dossierVB != null) {
                gedCallProps.setProperty("numero.affilie.formatte", dossierVB.getDossierComplexModel()
                        .getDossierModel().getNumeroAffilie());
                gedCallProps.setProperty(
                        "type.dossier",
                        ALServiceLocator.getGedBusinessService().getTypeSousDossier(
                                dossierVB.getDossierComplexModel().getDossierModel()));

                gedCallProps.setProperty("numero.avs.formatte", noAVS);

                String nomPrenom = dossierVB.getDossierComplexModel().getAllocataireComplexModel()
                        .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                        + " "
                        + dossierVB.getDossierComplexModel().getAllocataireComplexModel()
                                .getPersonneEtendueComplexModel().getTiers().getDesignation2();
                gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_ID, dossierVB.getDossierComplexModel()
                        .getAllocataireComplexModel().getPersonneEtendueComplexModel().getId());
                gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_NOM, dossierVB.getDossierComplexModel()
                        .getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1());
                gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_PRENOM, dossierVB.getDossierComplexModel()
                        .getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2());
                gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_NOM_PRENOM, nomPrenom);

            }
            // TODO: utiliser TIDocumentInfoHelper.fill(pubInfos,
            // dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers(), this.getSession(), null,
            // null, null)
            BSession currentSession = (BSession) session.getAttribute("objSession");
            // userId
            //
            gedCallProps.setProperty(JadeGedTargetProperties.USER, currentSession.getUserId());

            gedCallProps.setProperty("annee", annee);
            // technique: besoin de l'adresse ip du client
            gedCallProps.setProperty(JadeGedTargetProperties.IP_ADDRESS, request.getRemoteAddr()); // adresse ip du
                                                                                                   // client
            // gedCallProps.setProperty(JadeGedTargetProperties.IP_ADDRESS,
            // "172.16.69.35"); // adresse ip du client
            System.out
                    .println(" ***** on va appeler la JadeGedFacade avec les properties (service=" + nomService + ")");
            JadeGedCallDefinition result = JadeGedFacade.call(gedCallProps);
            System.out.println(" ***** on va en mettre le résultat en requête");
            request.setAttribute(FWActionGedResult.OBJ_JADE_CALL_RESULT, result);
            JadeGedCallType rType = result.getType();
            System.out.println(" ***** type de résultat GED: " + rType.toString());
            System.out.println(" ***** commande à exécuter: " + result.getCommand());
            System.out.println(" ***** on prépare le servletCall");
            String servletCall = request.getServletPath() + "?userAction=" + FWActionGedResult.FRAMEWORK_GED;
            System.out.println(" ***** on choppe la page " + servletCall + " pour y faire un forward.");
            RequestDispatcher rd = request.getRequestDispatcher(servletCall);
            // System.out.println(" ***** alors on a le RequestDispatcher, qui vaut "
            // + rd + ".");
            rd.forward(request, response);
        } catch (Exception e) {
            ServletException error = new ServletException("GED call failed. ", e);
            throw error;
        }
        // System.out.println("*-*-*-*-*-*-*-*-*-*-*");
        // System.out.println("* fini afficher GED *");
        // System.out.println("*-*-*-*-*-*-*-*-*-*-*");
    }
}
