/*
 * Créé le 29.07.2009
 */
package globaz.libra.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.dossiers.LIDossiersJointTiers;
import globaz.libra.db.dossiers.LIDossiersJointTiersManager;
import globaz.libra.utils.LIRecherchesDTO;
import globaz.libra.vb.dossiers.LIDossiersJointTiersListViewBean;
import globaz.libra.vb.dossiers.LIDossiersJointTiersViewBean;
import globaz.libra.vb.dossiers.LIDossiersViewBean;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * 
 * @author HPE
 * 
 */
public class LIDossiersAction extends LIDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public LIDossiersAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof LIDossiersViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_DOSSIERS_RC + "." + FWAction.ACTION_CHERCHER;
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                selectedId = request.getParameter("id");
            }

            // Si on vient d'une autre application, le selectedId est vide,
            // c'est idExterne qui est renseigné
            if (JadeStringUtil.isBlankOrZero(selectedId)) {
                String idExterne = request.getParameter("idExterne");

                LIDossiersJointTiersManager dosMgr = new LIDossiersJointTiersManager();
                dosMgr.setSession((BSession) mainDispatcher.getSession());
                dosMgr.setForIdExterne(idExterne);
                dosMgr.find();

                if (!dosMgr.isEmpty()) {
                    selectedId = ((LIDossiersJointTiers) dosMgr.getFirstEntity()).getIdDossier();
                }

                // Setter l'action dans le dto
                LIRecherchesDTO dto = (LIRecherchesDTO) PRSessionDataContainerHelper.getData(session,
                        PRSessionDataContainerHelper.KEY_LIBRA_DTO);
                if (null == dto) {
                    dto = new LIRecherchesDTO();
                }

                LIDossiers dossier = new LIDossiers();
                dossier.setSession((BSession) mainDispatcher.getSession());
                dossier.setIdDossier(selectedId);
                dossier.retrieve();

                LIDomaines domaine = new LIDomaines();
                domaine.setSession((BSession) mainDispatcher.getSession());
                domaine.setIdDomaine(dossier.getIdDomaine());
                domaine.retrieve();

                String urlRetour = "";

                if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_PC)) {
                    urlRetour = "pegasus?userAction=pegasus.dossier.dossier.afficher&selectedId=" + idExterne;
                } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_RFM)) {
                    urlRetour = "cygnus?userAction=cygnus.dossiers.dossierJointTiers.afficher&selectedId=" + idExterne;
                } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_RENTES)) {
                    urlRetour = "corvus?userAction=corvus.demandes.saisieDemandeRente.afficher&selectedId=" + idExterne;
                } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_AF)) {
                    urlRetour = "al?userAction=al.dossier.dossierMain.afficher&selectedId=" + idExterne;
                } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_AMAL)) {
                    urlRetour = "amal?userAction=amal.famille.famille.afficher&selectedId=" + idExterne;
                } else {
                    urlRetour = "";
                }

                dto.setUrlRetour(urlRetour);
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_LIBRA_DTO, dto);

            }

            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        LIDossiersJointTiersViewBean viewBean = new LIDossiersJointTiersViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());
        this.saveViewBean(viewBean, request);

        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionExporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            getAction().changeActionPart(FWAction.ACTION_LISTER);

            FWViewBeanInterface viewBean = new LIDossiersJointTiersListViewBean();

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            if (viewBean instanceof BManager) {
                ((BManager) viewBean).changeManagerSize(BManager.SIZE_NOLIMIT);
            }

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_print.jsp";

        } catch (InvocationTargetException e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } catch (IllegalAccessException e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        LIDossiersJointTiersListViewBean listViewBean = (LIDossiersJointTiersListViewBean) viewBean;

        LIRecherchesDTO dto = (LIRecherchesDTO) PRSessionDataContainerHelper.getData(session,
                PRSessionDataContainerHelper.KEY_LIBRA_DTO);

        if (null == dto) {
            dto = new LIRecherchesDTO();
        }

        if (listViewBean.getForIdDomaine().startsWith("IN")) {
            dto.setIdDomaine("");
        } else {
            dto.setIdDomaine(listViewBean.getForIdDomaine());
        }

        dto.setIdGroupe(listViewBean.getForIdGroupe());
        dto.setIdUtilisateur(listViewBean.getForIdUtilisateur());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_LIBRA_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

    public void rediriger(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String destination = "";
        String selectedId = request.getParameter("selectedId");

        LIDossiers dossier = new LIDossiers();
        dossier.setSession((BSession) mainDispatcher.getSession());
        dossier.setIdDossier(selectedId);
        dossier.retrieve();

        LIDomaines domaine = new LIDomaines();
        domaine.setSession((BSession) mainDispatcher.getSession());
        domaine.setIdDomaine(dossier.getIdDomaine());
        domaine.retrieve();

        String idExterne = dossier.getIdExterne();

        if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_PC)) {
            destination = "pegasus?userAction=pegasus.dossier.dossier.afficher&selectedId=" + idExterne;
        } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_RFM)) {
            destination = "cygnus?userAction=cygnus.dossiers.dossierJointTiers.afficher&selectedId=" + idExterne;
        } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_RENTES)) {
            destination = "corvus?userAction=corvus.demandes.saisieDemandeRente.afficher&selectedId=" + idExterne;
        } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_AF)) {
            destination = "al?userAction=al.dossier.dossierMain.afficher&selectedId=" + idExterne;
        } else if (domaine.getCsDomaine().equals(ILIConstantesExternes.CS_DOMAINE_AMAL)) {
            destination = "amal?userAction=amal.famille.famille.afficher&selectedId=" + idExterne;
        } else {
            destination = "libra?userAction=libra.dossiers.dossiersJointTiers.chercher";
        }

        goSendRedirect(destination, request, response);

    }

}
