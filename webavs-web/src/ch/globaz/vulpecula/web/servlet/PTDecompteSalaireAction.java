/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.decomptesalaire.PTCotisationsAjaxViewBean;
import globaz.vulpecula.vb.decomptesalaire.PTDecomptesalaireAjaxViewBean;
import globaz.vulpecula.vb.decomptesalaire.PTDecomptesalaireViewBean;
import globaz.vulpecula.vb.decomptesalaire.PTHistoriqueSalaireAjaxViewBean;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.web.gson.AbsenceGSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gère les actions pour une ligne décompte (salaire travailleur)
 * 
 * @since Web@BMS 0.01.02
 */
public class PTDecompteSalaireAction extends FWDefaultServletAction {

    public PTDecompteSalaireAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTDecomptesalaireAjaxViewBean) {
            PTDecomptesalaireAjaxViewBean vb = (PTDecomptesalaireAjaxViewBean) viewBean;
            setAbsencesGSON(request, vb);
        }
        return super.beforeModifier(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTDecomptesalaireAjaxViewBean) {
            PTDecomptesalaireAjaxViewBean vb = (PTDecomptesalaireAjaxViewBean) viewBean;
            setAbsencesGSON(request, vb);
        }
        return super.beforeAjouter(session, request, response, viewBean);
    }

    private void setAbsencesGSON(final HttpServletRequest request, final PTDecomptesalaireAjaxViewBean viewBean) {
        Gson gson = new Gson();

        String absences = request.getParameter("absencesGSON");
        List<AbsenceGSON> absencesGSON = gson.fromJson(absences, new TypeToken<List<AbsenceGSON>>() {
        }.getType());
        viewBean.setAbsencesGSON(absencesGSON);
    }

    private void setIdDecompteSalaire(final HttpServletRequest request, final PTCotisationsAjaxViewBean viewBean) {
        final String idDecompteSalaireParameter = "idDecompteSalaire";
        final String idDecompteSalaire = request.getParameter(idDecompteSalaireParameter);

        viewBean.setIdDecompteSalaire(idDecompteSalaire);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                selectedId = request.getParameter("id");
            }

            /*
             * Creation dynamique de notre viewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            /*
             * pour pouvoir faire un setId remarque : si il y a d'autre set a faire (si plusieurs id par ex) il faut le
             * faire dans le beforeAfficher(...)
             */
            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            /*
             * initialisation du viewBean
             */
            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        if (!response.isCommitted()) {
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        }

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        String idDecompte = null;
        String idDecompteSalaire = null;
        if (request.getParameter("idDecompte") != null && viewBean instanceof PTDecomptesalaireViewBean) {
            PTDecomptesalaireViewBean vb = (PTDecomptesalaireViewBean) viewBean;
            vb.setIdDecompte(request.getParameter("idDecompte"));
            idDecompte = vb.getIdDecompte();

            // Si on vient de la saisie rapide, on redirige sur la 1ère ligne de décompte salaire
            // Si pas de salaire saisie, on redirige sur le décompte
            if (request.getParameter("provenance") != null) {
                if (idDecompte != null) {
                    idDecompteSalaire = VulpeculaServiceLocator.getDecompteSalaireService()
                            .findFirstRowOfLigneForDecompte(idDecompte);

                    if (JadeNumericUtil.isEmptyOrZero(idDecompteSalaire)) {
                        String destination = "/" + getAction().getApplicationPart() + "?userAction="
                                + getAction().getApplicationPart()
                                + ".decomptedetail.decomptedetail.afficher&selectedId=" + idDecompte;
                        try {
                            response.sendRedirect(request.getContextPath() + destination);
                            ((FWUrlsStack) session.getAttribute(FWServlet.URL_STACK)).pop();
                            return super.beforeAfficher(session, request, response, viewBean);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                vb.setId(idDecompteSalaire);
            }

            session.setAttribute("viewBean", vb);
        }

        if (viewBean instanceof PTDecomptesalaireAjaxViewBean) {
            PTDecomptesalaireAjaxViewBean vb = (PTDecomptesalaireAjaxViewBean) viewBean;

            if (request.getParameter("idDecompte") != null) {
                idDecompte = request.getParameter("idDecompte");
                vb.setIdDecompte(idDecompte);
            }
            if (request.getParameter("sequence") != null) {
                String sequence = request.getParameter("sequence");
                vb.setSequence(sequence);
            }

            if (request.getParameter("navigation") != null) {
                String navigation = request.getParameter("navigation");
                vb.setNavigation(navigation);
            }

            if (request.getParameter("idDecompteSalaire") != null) {
                if (idDecompteSalaire == null) {
                    idDecompteSalaire = request.getParameter("idDecompteSalaire");
                }
                vb.setIdDecompteSalaire(idDecompteSalaire);
            }

            session.setAttribute(FWServlet.VIEWBEAN, vb);
        } else if (viewBean instanceof PTCotisationsAjaxViewBean) {
            PTCotisationsAjaxViewBean vb = (PTCotisationsAjaxViewBean) viewBean;
            setIdDecompteSalaire(request, vb);
            Montant masse = Montant.ZERO;
            Montant ac2 = Montant.ZERO;
            Montant franchise = Montant.ZERO;
            String masseSalariale = request.getParameter(PTConstants.MASSE_SALARIALE);
            String masseAC2 = request.getParameter(PTConstants.MASSE_AC2);
            String masseFranchise = request.getParameter(PTConstants.MASSE_FRANCHISE);
            if (masseSalariale != null) {
                masse = new Montant(masseSalariale);
            }
            if (!JadeStringUtil.isEmpty(masseAC2)) {
                ac2 = Montant.valueOf(masseAC2);
            }
            if (!JadeStringUtil.isEmpty(masseFranchise)) {
                franchise = Montant.valueOf(masseFranchise);
            }
            vb.setMasseSalariale(masse);
            vb.setMasseAC2(ac2);
            vb.setMasseFranchise(franchise);

        } else if (viewBean instanceof PTHistoriqueSalaireAjaxViewBean) {
            PTHistoriqueSalaireAjaxViewBean vb = (PTHistoriqueSalaireAjaxViewBean) viewBean;
            vb.setIdPosteTravail(request.getParameter("idPosteTravail"));
        }

        if (!JadeStringUtil.isEmpty(idDecompte)) {
            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(idDecompte);
            if (!VulpeculaServiceLocator.getUsersService().hasRightAccesSecurity(
                    decompte.getEmployeur().getAccesSecurite())) {
                String destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + ".decompte.decompte.afficher&protected=true";
                try {
                    servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
                    ((FWUrlsStack) session.getAttribute(FWServlet.URL_STACK)).pop();
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }
}
