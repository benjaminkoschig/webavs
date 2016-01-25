/*
 * Créé le 18 juil. 07
 */

package globaz.corvus.servlet;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.vb.decisions.KeyPeriodeInfo;
import globaz.corvus.vb.decisions.RECopieDecisionViewBean;
import globaz.corvus.vb.decisions.REPreValiderDecisionViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.process.REValiderDecisionsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SCR
 */

public class REProcessDecisionAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REProcessDecisionAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        REValiderDecisionsViewBean viewBean = null;

        // -X try {
        // -X viewBean = (REValiderDecisionsViewBean) session.getAttribute("viewBean");
        // -X
        // -X } catch (Exception e) {
        // -X }

        try {
            viewBean = (REValiderDecisionsViewBean) session.getAttribute("viewBean");

        } catch (Exception e) {
        }

        String idDecision = "";

        try {
            // -X if (null == viewBean) {
            if (null == viewBean) {
                viewBean = new REValiderDecisionsViewBean();
                idDecision = request.getParameter("selectedId");
                (viewBean).setIdDecision(idDecision);

            } else {

                idDecision = viewBean.getIdDecision();

            }

            // -X
            // -X } else {
            // -X
            // -X idDecision = viewBean.getIdDecision();
            // -X
            // -X }

            String msg = viewBean.getMessage();
            String msgType = viewBean.getMsgType();

            // Seront reseter lors de l'appel au dispatcher !!!
            viewBean.setISession(mainDispatcher.getSession());
            viewBean = (REValiderDecisionsViewBean) mainDispatcher.dispatch(viewBean, getAction());

            if (FWViewBeanInterface.ERROR.equals(msgType)) {
                viewBean.setMessage(msg);
                viewBean.setMsgType(msgType);
            }
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            // -X this.saveViewBean(viewBean, session);
            this.saveViewBean(viewBean, session);

            // test retrieve décision
            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession((BSession) mainDispatcher.getSession());
            decision.setIdDecision(idDecision);

            try {
                decision.retrieve();
            } catch (Exception e) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        if (null != viewBean) {
            if (!viewBean.getTypeTTT().equals("afficher")) {
                viewBean.setDocumentsPreview(null);
            }
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    public String actionValiderDirect(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        REPreValiderDecisionViewBean vb = (REPreValiderDecisionViewBean) viewBean;

        Map map = new TreeMap();
        Map mapCopies = new TreeMap();

        // Reprendre les valeurs pour les keys par la requete et setter une map
        // dans le viewBean pour utilisation dans le helper
        // Création de la clé
        String key = new String();
        String keyCopie = new String();

        // Récupérer tous les paramètres passé dans la requête (par post) et
        // l'envoyer vers l'helper
        for (Enumeration names = request.getParameterNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();

            // Reprise uniquement des paramètres qui nous intéressent (pour 20
            // paramètres maximum)
            if (name.startsWith("dateDeb_") || name.startsWith("dateFin_") || name.startsWith("remarqu_")) {

                // Définition de la clé de tri
                key = name.substring(8);

                // Si key déjà existant, alors on complète
                if (map.containsKey(key)) {
                    KeyPeriodeInfo keyPeriodeInfo = (KeyPeriodeInfo) map.get(key);

                    if (name.startsWith("dateDeb_")) {
                        keyPeriodeInfo.dateDebut = request.getParameter(name);
                    }
                    if (name.startsWith("dateFin_")) {
                        keyPeriodeInfo.dateFin = request.getParameter(name);
                    }
                    if (name.startsWith("remarqu_")) {
                        keyPeriodeInfo.remarque = request.getParameter(name);
                    }

                    map.put(key, keyPeriodeInfo);

                }
                // Sinon on insère simplement les valeurs
                else {

                    KeyPeriodeInfo keyPeriodeInfo = new KeyPeriodeInfo();

                    if (name.startsWith("dateDeb_")) {
                        keyPeriodeInfo.dateDebut = request.getParameter(name);
                    }
                    if (name.startsWith("dateFin_")) {
                        keyPeriodeInfo.dateFin = request.getParameter(name);
                    }
                    if (name.startsWith("remarqu_")) {
                        keyPeriodeInfo.remarque = request.getParameter(name);
                    }

                    map.put(key, keyPeriodeInfo);

                }
            } else if (name.startsWith("isVersem_") || name.startsWith("isBaseCa_") || name.startsWith("isDecomp_")
                    || name.startsWith("isRemarq_") || name.startsWith("isMoyens_") || name.startsWith("isSignat_")
                    || name.startsWith("isAnnexe_") || name.startsWith("isCopies_") || name.startsWith("idCopieD_")
                    || name.startsWith("isPageGa_")) {

                keyCopie = name.substring(9);

                RECopieDecisionViewBean copieDecision;

                // Si key déjà existant, alors on complète
                if (mapCopies.containsKey(keyCopie)) {
                    copieDecision = (RECopieDecisionViewBean) mapCopies.get(keyCopie);
                }
                // Sinon on insère simplement les valeurs
                else {

                    copieDecision = new RECopieDecisionViewBean();
                }

                if (name.startsWith("isVersem_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsVersementA(Boolean.TRUE);
                    } else {
                        copieDecision.setIsVersementA(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isBaseCa_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsBaseCalcul(Boolean.TRUE);
                    } else {
                        copieDecision.setIsBaseCalcul(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isDecomp_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsDecompte(Boolean.TRUE);
                    } else {
                        copieDecision.setIsDecompte(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isRemarq_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsRemarques(Boolean.TRUE);
                    } else {
                        copieDecision.setIsRemarques(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isMoyens_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsMoyensDroit(Boolean.TRUE);
                    } else {
                        copieDecision.setIsMoyensDroit(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isSignat_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsSignature(Boolean.TRUE);
                    } else {
                        copieDecision.setIsSignature(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isAnnexe_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsAnnexes(Boolean.TRUE);
                    } else {
                        copieDecision.setIsAnnexes(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isCopies_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsCopies(Boolean.TRUE);
                    } else {
                        copieDecision.setIsCopies(Boolean.FALSE);
                    }
                }

                if (name.startsWith("isPageGa_")) {
                    if (request.getParameter(name).equals("on")) {
                        copieDecision.setIsPageGarde(Boolean.TRUE);
                    } else {
                        copieDecision.setIsPageGarde(Boolean.FALSE);
                    }
                }

                if (name.startsWith("idCopieD_")) {
                    copieDecision.setIdDecisionCopie(request.getParameter(name));
                }

                mapCopies.put(keyCopie, copieDecision);

            }
        }

        // Mise à jour des copies
        List copiesList = new ArrayList();
        for (Iterator iterator = mapCopies.keySet().iterator(); iterator.hasNext();) {

            String keyCop = (String) iterator.next();

            RECopieDecisionViewBean copieDecisionMap = (RECopieDecisionViewBean) mapCopies.get(keyCop);

            RECopieDecisionViewBean copieDecisionBD = new RECopieDecisionViewBean();
            copieDecisionBD.setSession((BSession) mainDispatcher.getSession());
            copieDecisionBD.setIdDecisionCopie(copieDecisionMap.getIdDecisionCopie());
            copieDecisionBD.retrieve();

            if (!copieDecisionBD.isNew()) {
                copieDecisionBD.setIsVersementA(copieDecisionMap.getIsVersementA());
                copieDecisionBD.setIsBaseCalcul(copieDecisionMap.getIsBaseCalcul());
                copieDecisionBD.setIsDecompte(copieDecisionMap.getIsDecompte());
                copieDecisionBD.setIsRemarques(copieDecisionMap.getIsRemarques());
                copieDecisionBD.setIsMoyensDroit(copieDecisionMap.getIsMoyensDroit());
                copieDecisionBD.setIsSignature(copieDecisionMap.getIsSignature());
                copieDecisionBD.setIsAnnexes(copieDecisionMap.getIsAnnexes());
                copieDecisionBD.setIsCopies(copieDecisionMap.getIsCopies());
                copieDecisionBD.setIsPageGarde(copieDecisionMap.getIsPageGarde());
                copieDecisionBD.update();
                copiesList.add(copieDecisionBD);
            }
        }

        ((REPreValiderDecisionViewBean) viewBean).setCopiesList(copiesList);

        ((REPreValiderDecisionViewBean) viewBean).setMapKey(map);
        ((REPreValiderDecisionViewBean) viewBean).setRemarqueDecision(request.getParameter("remarqueDecision"));
        ((REPreValiderDecisionViewBean) viewBean).setTraiterParDecision(request.getParameter("traiterParDecision"));
        ((REPreValiderDecisionViewBean) viewBean).setDocumentsPreview(null);

        mainDispatcher.dispatch(vb, getAction());

        RENSSDTO dtoNss = new RENSSDTO();

        dtoNss.setNSS("");
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);

        return this.getUserActionURL(request, IREActions.ACTION_DECISIONS, FWAction.ACTION_CHERCHER
                + "&noDemandeRente=" + vb.getIdDemandeRente());

    }

    public String executerTTT(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        REValiderDecisionsViewBean vb = (REValiderDecisionsViewBean) viewBean;

        vb = (REValiderDecisionsViewBean) mainDispatcher.dispatch(vb, getAction());

        if (FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {
            session.setAttribute("viewBean", vb);
            return this
                    .getUserActionURL(request, IREActions.ACTION_PROCESS_VALIDER_DECISIONS, FWAction.ACTION_AFFICHER);

        } else {
            if (vb.getTypeTTT().equals("toutValider")) {
                return this.getUserActionURL(request, IREActions.ACTION_DECISIONS, FWAction.ACTION_CHERCHER
                        + "&noDemandeRente=" + vb.getIdDemandeRente());
            } else {
                return this.getUserActionURL(request, IREActions.ACTION_PROCESS_VALIDER_DECISIONS,
                        FWAction.ACTION_AFFICHER);
            }
        }

    }

}
