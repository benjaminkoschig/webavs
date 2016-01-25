package globaz.prestation.ged;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWActionGedResult;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedCallDefinition;
import globaz.jade.ged.message.JadeGedService;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe avec méthode unique d'affichage de dossier GED.
 * 
 * Cette méthode étant dans chacun des modules prestations, inutile de le répéter partout. Modifications donc uniquement
 * dans cette classe à l'avenir...
 * 
 * @author HPE
 */
public class PRGedAffichageDossier {

    public static final void actionAfficherDossierGed(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException, JadeServiceLocatorException, JadeServiceActivatorException,
            NullPointerException, ClassCastException, JadeClassCastException {

        String blankStr = "";

        // récup du noAffilie depuis la request
        String noAffilie = request.getParameter("noAffiliationId");
        if (noAffilie == null) {
            noAffilie = request.getParameter(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE);
            if (noAffilie == null) {
                noAffilie = blankStr;
            }
        }

        // récup du noAVS depuis la request
        String noAVS = request.getParameter("noAVSId");
        if (noAVS == null) {
            noAVS = request.getParameter(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE);
            if (noAVS == null) {
                noAVS = blankStr;
            }
        }

        // Spécifique CCB !!!!

        String idTiersCmpl = request.getParameter("idTiersExtraFolder");
        PRGedHelper h = new PRGedHelper();

        String nssCmpl = "";
        try {
            if (h.isExtraNSS((BSession) mainDispatcher.getSession())) {
                nssCmpl = h.getNssExtraFolder((BSession) mainDispatcher.getSession(), idTiersCmpl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            nssCmpl = blankStr;
        }

        if (nssCmpl == null) {
            nssCmpl = blankStr;
        }

        // récup du nomService depuis la request
        String nomService = request.getParameter("serviceNameId");
        if (nomService == null) {
            nomService = blankStr;
        }

        // vérifier que le service se trouve dans la liste
        boolean isInList = false;

        List<JadeGedService> services = null;

        try {
            String targetName = ((BSession) mainDispatcher.getSession()).getApplication().getProperty("ged.targetName");
            if (targetName != null) {
                services = JadeGedFacade.getServicesList(targetName);
            }
        } catch (Exception ex) {
            services = null;
        }

        if (services == null) {
            services = JadeGedFacade.getServicesList();
        }

        for (JadeGedService service : services) {
            if (service.toString().equals(nomService)) {
                isInList = true;
            }
        }
        if (!isInList) {
            nomService = blankStr;
        }

        // création des props à passer à la GED
        Properties gedCallProps = new Properties();

        // bz-6573
        try {
            int i = 1;
            int idForGed = 1;
            String nssMF = request.getParameter(PRGedHelper.PARAM_NSS_MF + String.valueOf(i));

            // System.out.println("NSS de base = " + noAVS);

            if (!JadeStringUtil.isBlankOrZero(nssMF)) {

                if (!JadeStringUtil.isBlank(noAVS) && !noAVS.equals(nssMF)) {
                    gedCallProps.setProperty(
                            TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE + "." + String.valueOf(idForGed),
                            JadeStringUtil.removeChar(nssMF, '.'));

                    gedCallProps.setProperty(
                            TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE + "." + String.valueOf(idForGed), nssMF);

                    // System.out.println(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE + "."
                    // + String.valueOf(idForGed)
                    // + " = " + JadeStringUtil.removeChar(nssMF, '.'));

                    idForGed++;
                }
                while (nssMF != null) {
                    i++;
                    nssMF = request.getParameter(PRGedHelper.PARAM_NSS_MF + String.valueOf(i));

                    if (!JadeStringUtil.isBlankOrZero(nssMF)) {

                        if (!JadeStringUtil.isBlank(noAVS) && !noAVS.equals(nssMF)) {
                            gedCallProps
                                    .setProperty(
                                            TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE + "."
                                                    + String.valueOf(idForGed), JadeStringUtil.removeChar(nssMF, '.'));

                            gedCallProps.setProperty(
                                    TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE + "." + String.valueOf(idForGed),
                                    nssMF);

                            // System.out.println(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE + "."
                            // + String.valueOf(idForGed) + " = " + JadeStringUtil.removeChar(nssMF, '.'));

                            idForGed++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // USER
        if ((mainDispatcher != null) && (mainDispatcher.getSession() != null)) {
            gedCallProps.setProperty(JadeGedTargetProperties.USER, mainDispatcher.getSession().getUserId());
        }

        // on veut montrer un dossier: ACTION pour les deux GED
        gedCallProps.setProperty(JadeGedTargetProperties.ACTION, JadeGedTargetProperties.ACTION_OPEN_FOLDER);
        // propriété TI:groupdoc: nom correspondant à un folder
        gedCallProps.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "affilie");
        // ajout du nom de service, pour au cas où
        gedCallProps.setProperty(JadeGedTargetProperties.SERVICE, nomService);

        // propriétés business, correspondant à des indexes
        gedCallProps.setProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, noAffilie);
        gedCallProps.setProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                JadeStringUtil.removeChar(noAffilie, '.'));
        gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, noAVS);
        gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(noAVS, '.'));
        gedCallProps.setProperty(PRGedHelper.GED_FOLDER_EXTRA_NSS, JadeStringUtil.isBlank(nssCmpl) ? noAVS : nssCmpl);
        try {
            PRTiersWrapper tiers = PRTiersHelper.getTiers((BSession) session.getAttribute("objSession"), noAVS);
            // Ajout de l'idTiers dans la clé pyxis.tiers.id
            gedCallProps.setProperty(TIDocumentInfoHelper.TIERS_ID, tiers.getIdTiers());

            gedCallProps.setProperty(
                    TIDocumentInfoHelper.TIERS_NOM_PRENOM,
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + ","
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        } catch (Exception e) {
            JadeLogger.warn(PRGedAffichageDossier.class, "Failed to find tiers: " + noAVS);
        }
        // technique: besoin de l'adresse ip du client
        gedCallProps.setProperty(JadeGedTargetProperties.IP_ADDRESS, request.getRemoteAddr());

        try {
            JadeGedCallDefinition result = null;

            try {
                String targetName = ((BSession) mainDispatcher.getSession()).getApplication().getProperty(
                        "ged.targetName");
                if (targetName != null) {
                    result = JadeGedFacade.call(targetName, gedCallProps);
                }
            } catch (Exception ex) {
                result = null;
            }

            if (result == null) {
                result = JadeGedFacade.call(gedCallProps);
            }

            request.setAttribute(FWActionGedResult.OBJ_JADE_CALL_RESULT, result);
            String servletCall = request.getServletPath() + "?userAction=" + FWActionGedResult.FRAMEWORK_GED;
            request.getRequestDispatcher(servletCall).forward(request, response);
        } catch (Exception e) {
            // ServletException error = new
            // ServletException("GED call failed. ", e);
        }
    }
}
