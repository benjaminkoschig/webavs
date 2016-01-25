/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.beneficiairepc.AFJournalQuittance;
import globaz.naos.db.beneficiairepc.AFQuittanceListViewBean;
import globaz.naos.db.beneficiairepc.AFQuittancePCGComptabilisationCIProcessViewBean;
import globaz.naos.db.beneficiairepc.AFQuittancePCGFacturationProcessViewBean;
import globaz.naos.db.beneficiairepc.AFQuittanceViewBean;
import globaz.pyxis.summary.TIActionSummary;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Adhésion.
 * 
 * @author sau
 */
public class AFActionBeneficiairepc extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionBeneficiairepc
     * 
     * @param servlet
     */
    public AFActionBeneficiairepc(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        // super.actionCustom(session, request, response, dispatcher);
        String _destination = getRelativeURLwithoutClassPart(request, session) + "quittance_de.jsp";
        if ("afficherNom".equals(getAction().getActionPart())) {
            try {
                AFQuittanceViewBean viewBean = new AFQuittanceViewBean();
                JSPUtils.setBeanProperties(request, viewBean);
                // On va setter l'idTiers
                viewBean.setNumAffilie(viewBean.getIdAffBeneficiaire());
                AFAffiliationManager man = new AFAffiliationManager();
                man.setISession(dispatcher.getSession());
                man.setForAffilieNumero(viewBean.getIdAffBeneficiaire());
                man.find();
                AFAffiliation aff = (AFAffiliation) man.getFirstEntity();
                viewBean.setIdTiers(aff.getIdTiers());
                session.setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, aff.getIdTiers());
                session.setAttribute("viewBean", viewBean);
            } catch (Exception ex) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else if ("afficherGenerer".equals(getAction().getActionPart())) {
            _destination = "";
            try {
                String idJournalQuittances = request.getParameter("idJournalQuittances");
                AFQuittancePCGFacturationProcessViewBean viewBean = new AFQuittancePCGFacturationProcessViewBean();
                viewBean.setIdJournalQuittances(idJournalQuittances);
                AFJournalQuittance quittance = new AFJournalQuittance();
                quittance.setISession(dispatcher.getSession());
                quittance.setIdJournalQuittance(idJournalQuittances);
                quittance.retrieve();
                if (!quittance.isNew()) {
                    viewBean.setLibelleJournal(quittance.getDescriptionJournal());
                }

                viewBean.setISession(dispatcher.getSession());
                session.setAttribute("viewBean", viewBean);

                _destination = getRelativeURLwithoutClassPart(request, session) + "processGenererQuittances_de.jsp";
            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            /*
             * affiche la prochaine page
             */
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else if ("executerGenerer".equals(getAction().getActionPart())) {
            AFQuittancePCGFacturationProcessViewBean process = (AFQuittancePCGFacturationProcessViewBean) session
                    .getAttribute("viewBean");
            BTransaction transaction = null;
            try {
                transaction = new BTransaction((BSession) dispatcher.getSession());
                transaction.getSession().newTransaction();
                transaction.openTransaction();
                process.setTransaction(transaction);
                process.executeProcess();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (transaction != null) {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } else if ("afficherComptabiliser".equals(getAction().getActionPart())) {
            _destination = "";
            try {
                String idJournalQuittances = request.getParameter("idJournalQuittances");
                AFQuittancePCGComptabilisationCIProcessViewBean viewBean = new AFQuittancePCGComptabilisationCIProcessViewBean();
                viewBean.setIdJournalQuittances(idJournalQuittances);
                AFJournalQuittance quittance = new AFJournalQuittance();
                quittance.setISession(dispatcher.getSession());
                quittance.setIdJournalQuittance(idJournalQuittances);
                quittance.retrieve();
                if (!quittance.isNew()) {
                    viewBean.setLibelleJournal(quittance.getDescriptionJournal());
                }

                viewBean.setISession(dispatcher.getSession());
                session.setAttribute("viewBean", viewBean);

                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "processComptabiliserCiQuittances_de.jsp";
            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            /*
             * affiche la prochaine page
             */
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }
    }

    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        AFQuittanceListViewBean vBean = (AFQuittanceListViewBean) viewBean;
        // Positionnement selon les critères de recherche
        return vBean;
    }

    /*
     * protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse
     * response, FWViewBeanInterface viewBean) { System.out.println("Before afficher"); return
     * super.beforeAfficher(session, request, response, viewBean); }
     */

}
