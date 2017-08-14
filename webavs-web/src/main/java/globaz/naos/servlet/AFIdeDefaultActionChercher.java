package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.ide.AFIdeSearchListViewBean;
import globaz.naos.properties.AFProperties;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import ch.globaz.common.properties.PropertiesException;

/**
 * Classe permettant la gestion des actions pour l'entité Affiliation. Avec l'ajonction de la fonctionnalité d'encodage
 * Base64 d'aun param (besoin de l'IDE)
 * 
 * @author cel
 */
public class AFIdeDefaultActionChercher extends AFDefaultActionChercher {

    private static final String PARAM_TO_BASE64 = "forRaisonSociale";
    private static final String NAOS_IDE_ACTION_BASE64_PARAM = "naos.ide.ideSearch.chercher";

    public AFIdeDefaultActionChercher(FWServlet servlet) {
        super(servlet);
    }

    /***
     * Surcharge pour ajouter le cs du code Noga de l'IDE choisi via le bouton [...]
     */
    @Override
    protected void actionChoisir(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {
            if (!AFProperties.NOGA_SYNCHRO_REGISTRE.getValue().isEmpty()
                    && AFProperties.NOGA_SYNCHRO_REGISTRE.getValue() != null
                    && AFProperties.NOGA_SYNCHRO_REGISTRE.getBooleanValue()) {

                AFIdeSearchListViewBean listViewBean = (AFIdeSearchListViewBean) session.getAttribute("listViewBean");
                int selectedIndex = Integer.parseInt(request.getParameter("selectedIndex"));

                // On récupère l'id selectionner et on récupère le code noga dans la liste des résultats
                String codeNoga = listViewBean.getCodeNogaForIndex(selectedIndex);

                BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
                String csCodeNoga = retrieveCsCodeNoga(codeNoga, bsession);

                // Finalement on set le csCodeNoga du viewBean de l'écran d'affiliation
                AFAffiliation viewBean;
                try {
                    viewBean = (AFAffiliation) session.getAttribute("viewBean");
                    viewBean.setCodeNoga(csCodeNoga);
                    session.setAttribute("viewBean", viewBean);
                } catch (Exception e) {
                    // Si le viewBean n'est pas trouvé ça veut dire on vient de l'écran CAF0044
                    globaz.naos.db.wizard.AFWizard viewBeanNew = (globaz.naos.db.wizard.AFWizard) session
                            .getAttribute("viewBean");
                    viewBeanNew.setCodeNoga(csCodeNoga);
                    session.setAttribute("viewBean", viewBeanNew);
                }
            }
        } catch (NumberFormatException e) {
            JadeLogger.info(e, e.getMessage());
        } catch (PropertiesException e) {
            JadeLogger.info(e, e.getMessage());
        }

        super.actionChoisir(session, request, response, mainDispatcher);
    }

    /***
     * Permet de retrouver le code système du code Noga dans les familles de paramètre, si on le trouve pas on retourne
     * 0
     * 
     * @param codeNoga
     * @param bsession
     * @return
     */
    private String retrieveCsCodeNoga(String codeNoga, BSession bsession) {
        String csCodeNoga;
        try {
            FWParametersSystemCodeManager param = new FWParametersSystemCodeManager();

            param.setSession(bsession);
            param.setForCodeUtilisateur(codeNoga);

            param.find(BManager.SIZE_NOLIMIT);

            if (param.size() == 0) {
                csCodeNoga = "0";
            } else {
                csCodeNoga = ((FWParametersSystemCode) param.getFirstEntity()).getIdCode();
            }
        } catch (Exception e) {
            csCodeNoga = "0";
        }
        return csCodeNoga;
    }

    /**
     * Override for IDE (inforom 0050)
     * since 1.15.01 (02/11/2015)
     * author CEL
     * 
     * le but de l'override est de faire un encodage base64 sur la valeur d'un param avant le get pour escaper les
     * caractère non autorisée dans l'url (comme le &)
     * 
     */
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        // System.out.println("actionSelectionnerStart\n");
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        // System.out.println("viewBean statut : "+viewBean.getMsgType());
        try {
            // set les properties
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);
            String selectorName = request.getParameter("selectorName");

            Object[] ProviderActionParams = (Object[]) session.getAttribute(selectorName + "ProviderActionParams");
            String providerApplication = (String) session.getAttribute(selectorName + "ProviderApplication");
            String providerPrefix = (String) session.getAttribute(selectorName + "ProviderPrefix");
            String redirectUrl = (String) session.getAttribute(selectorName + "RedirectUrl");

            session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_METHODS,
                    session.getAttribute(selectorName + "Methods"));
            session.setAttribute("redirectUrl", redirectUrl);

            // destination
            destination = "/" + session.getAttribute(selectorName + "ProviderApplication") + "?userAction="
                    + session.getAttribute(selectorName + "ProviderAction") + "&colonneSelection=yes";
            // eventuel parametres a transamettre pour l'url du provider
            if (ProviderActionParams != null) {
                for (int nbParams = 0; nbParams < ProviderActionParams.length; nbParams++) {
                    String fieldName = ((String[]) ProviderActionParams[nbParams])[0];
                    String paramName = ((String[]) ProviderActionParams[nbParams])[1];
                    String paramValue = request.getParameter(fieldName);
                    if (NAOS_IDE_ACTION_BASE64_PARAM.equalsIgnoreCase(""
                            + session.getAttribute(selectorName + "ProviderAction"))
                            && paramName.equalsIgnoreCase(PARAM_TO_BASE64)) {
                        paramValue = new String(Base64.encodeBase64URLSafe(paramValue.getBytes()));
                    }
                    destination += "&" + paramName + "=" + paramValue;
                }
            }

            FWController controller = (FWController) session.getAttribute("objController");
            // save la session :
            saveContext(request); // marche pas avec tomcat
            // (concurentModificationException)

            // a voir...

            controller = new FWDispatcher(controller.getSession(), providerApplication, providerPrefix);

            session.setAttribute("objController", controller);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        // il faut utiliser repsonse.sendRedirect
        // sinon on a pyxis/helios/helios par ex.
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        // response.sendRedirect(_destination);
    }

    /**
     * Sauvegarde le contexte de la session HTTP.
     * 
     * @param request
     *            la requête
     */
    private void saveContext(HttpServletRequest request) throws javax.servlet.ServletException, java.io.IOException {
        Hashtable context = getSessionAttributesTable(request);
        // _removeAllSessionAttributes(request); // marche pas avec tomcat
        request.getSession().setAttribute("globazContext", context);
    }

    /**
     * Renvoie une table contenant les attributs de la session HTTP.
     * 
     * @return une table contenant les attributs de la session HTTP
     * @param request
     *            la requête
     */
    private Hashtable getSessionAttributesTable(HttpServletRequest request) {
        Hashtable table = new Hashtable();
        HttpSession httpSession = request.getSession();
        for (Enumeration enumeration = httpSession.getAttributeNames(); enumeration.hasMoreElements();) {
            String name = (String) enumeration.nextElement();
            table.put(name, httpSession.getAttribute(name));
        }
        return table;
    }

}
