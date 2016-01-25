package globaz.draco.servlet;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DSActionSaisieMasseTotaux extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur DSActionPreImpression.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public DSActionSaisieMasseTotaux(FWServlet servlet) {
        super(servlet);
    }

    protected void actionAfficherApresAnnulation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";
        try {

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");

            /*
             * Creation dynamique de notre viewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());

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
            if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getActionFullURL() + ".afficher&_method=add&_valid=";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("valider")) {
            actionValider(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("afficherApresAnnulation")) {
            actionAfficherApresAnnulation(session, request, response, dispatcher);
        }
    }

    protected void actionValider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        BTransaction trans = null;
        try {
            /*
             * recuperation du bean depuis la session
             */

            DSDeclarationViewBean viewBean = (DSDeclarationViewBean) session.getAttribute("viewBean");
            DSDeclarationViewBean declViewBean = null;

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            try {
                // Création et ouverture d'une transaction
                trans = new BTransaction((BSession) ((FWController) session.getAttribute("objController")).getSession());
                trans.openTransaction();
                // On regarde s'il existe déjà une déclaration dans la base de
                // données
                DSDeclarationListViewBean decListViewBean = new DSDeclarationListViewBean();
                decListViewBean.setSession((BSession) ((FWController) session.getAttribute("objController"))
                        .getSession());
                decListViewBean.setForAnnee(viewBean.getAnnee());
                decListViewBean.setForAffiliationId(viewBean.getAffiliationId());
                decListViewBean.setForEtat(viewBean.getEtat());
                decListViewBean.setForTypeDeclaration(viewBean.getTypeDeclaration());
                decListViewBean.find();
                // Si ce n'est pas le cas, on ajoute la déclaration
                if (decListViewBean.size() == 0) {
                    viewBean.retrieve(trans);
                    viewBean.add(trans);
                } else {
                    // Si c'est le cas, on sette tous les paramètres entrés dans
                    // l'écran à la déclaration et on la modifie
                    declViewBean = (DSDeclarationViewBean) decListViewBean.getFirstEntity();
                    declViewBean.retrieve();
                    // On fait ce set pour simuler une saisie à l'écran et pour
                    // que se soit exactement les memes tests qu'une saisie
                    // à l'écran qui sont executés
                    declViewBean.setSaisieEcran("true");
                    declViewBean.setAffiliationId(viewBean.getAffiliationId());
                    declViewBean.setAffilieNumero(viewBean.getAffilieNumero());
                    declViewBean.setAffilieDesEcran(viewBean.getAffilieDesEcran());
                    declViewBean.setDescriptionTiers(viewBean.getDescriptionTiers());
                    declViewBean.setAffilieRadieEcran(viewBean.getAffilieRadieEcran());
                    declViewBean.setTypeAffiliationEcran(viewBean.getTypeAffiliationEcran());
                    declViewBean.setAnnee(viewBean.getAnnee());
                    declViewBean.setDateRetourEff(viewBean.getDateRetourEff());
                    declViewBean.setMasseSalTotal(viewBean.getMasseSalTotal());
                    declViewBean.setMasseACTotal(viewBean.getMasseACTotal());
                    declViewBean.setMasseAC2Total(viewBean.getMasseAC2Total());
                    declViewBean.setTotalControleDS(viewBean.getTotalControleDS());
                    declViewBean.setTypeDeclaration(viewBean.getTypeDeclaration());
                    declViewBean.update(trans);
                }
                // On commit les modications
                trans.commit();
            } catch (Exception e) {
                // On sette l'erreur dans le viewBean afin que la boite de
                // dialogue de l'erreur
                // apparaisse à l'écran
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(trans.getErrors().toString());
                // En cas d'erreur on rollback les modifications effectuées
                if (trans != null) {
                    trans.rollback();
                }
            } finally {
                if (trans != null) {
                    trans.closeTransaction();
                }
            }

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */

            viewBean = (DSDeclarationViewBean) beforeAjouter(session, request, response, viewBean);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            /*
             * choix de la destination
             */
            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getActionFullURL() + ".afficher&_method=add&_valid=";

            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {

            _destination = ERROR_PAGE;
        } finally {
            if (trans != null) {
                try {
                    trans.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }
}