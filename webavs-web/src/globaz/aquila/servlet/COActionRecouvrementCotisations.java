package globaz.aquila.servlet;

import globaz.aquila.db.irrecouvrables.CORecouvrementSelectionMontantViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.osiris.db.irrecouvrable.CARecouvrementPosteContainer;
import globaz.osiris.db.irrecouvrable.CATypeDeRecouvrementPoste;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class COActionRecouvrementCotisations extends COActionRecouvrementAbstract {

    public COActionRecouvrementCotisations(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        return viewBean;
    }

    /**
     * Actualise le conteneur de poste de recouvrements avec les données contenues dans la requête.
     * Le nouveau conteneur est enregistré en session
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void actualiserVentilation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        try {
            CARecouvrementPosteContainer recouvrementPosteContainer = initRecouvrementPostesContainer(request);
            mainDispatcher.getSession().setAttribute("recouvrementPosteContainer", recouvrementPosteContainer);
            super.actionAfficher(session, request, response, mainDispatcher);
        } catch (Exception e) {
            goSendRedirect(FWDefaultServletAction.ERROR_PAGE, request, response);
        }
    }

    /**
     * Action d'affichage de l'écran de ventilation des recouvrements.
     * 
     * S'il s'agit du premier affichage de la page, elle est directement affichée. En cas d'actualisation, un conteneur
     * de poste de recouvrements est initialisé puis mis en session
     * 
     * L'action à exécuté est déterminé par la présence du paramètre <code>isInitialized</code> dans la requête.
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String isInitialized = JSPUtils.getParameter(request, "isInitialized");
        mainDispatcher.getSession().removeAttribute("recouvrementPosteContainer");

        if (isInitialized != null && "1".equals(isInitialized)) {
            actualiserVentilation(session, request, response, mainDispatcher);
        } else {
            afficher(session, request, response, mainDispatcher);
        }
    }

    /**
     * Affiche la page de ventilation si les données du viewBean sont valide. Dans le cas contraire réaffiche la page de
     * sélection des montants
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void afficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CORecouvrementSelectionMontantViewBean viewBean = (CORecouvrementSelectionMontantViewBean) session
                .getAttribute(("viewBean"));

        String destination = FWDefaultServletAction.ERROR_PAGE;

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            if (viewBean.isValid()) {
                super.actionAfficher(session, request, response, mainDispatcher);
            } else {
                destination = "/" + getAction().getApplicationPart()
                        + "?userAction=aquila.irrecouvrables.recouvrementSelectionMontant.reAfficher";
                goSendRedirect(destination, request, response);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
            goSendRedirect(destination, request, response);
        }
    }

    /**
     * Initialise un conteneur de postes de recouvrement avec les données contenues dans la requête
     * 
     * @param request requête
     * @return le conteneur initialisé
     */
    private CARecouvrementPosteContainer initRecouvrementPostesContainer(HttpServletRequest request)
            throws ServletException, IOException {

        Map<String, Map<String, String>> postes = getParametersMap(request,
                "(postes\\[)([0-9]+)(\\]\\[)([a-zA-Z]+)(\\])");

        CARecouvrementPosteContainer recouvrementPosteContainer = new CARecouvrementPosteContainer();

        for (Map<String, String> poste : postes.values()) {

            String idRubriqueIrrecouvrable = poste.get("idRubriqueIrrecouvrable");
            String idRubriqueRecouvrement = poste.get("idRubriqueRecouvrement");
            String idRubrique = poste.get("idRubrique");
            String libelleRubriqueRecouvrement = poste.get("libelleRubriqueRecouvrement");
            String numeroRubriqueIrrecouvrable = poste.get("numeroRubriqueIrrecouvrable");
            String numeroRubriqueRecouvrement = poste.get("numeroRubriqueRecouvrement");
            String ordrePriorite = poste.get("ordrePriorite");
            Integer annee = !JadeNumericUtil.isEmptyOrZero(poste.get("annee")) ? new Integer(poste.get("annee").trim())
                    : 0;

            BigDecimal valeurInitialeCotAmortie = getBigDecimal(poste.get("valeurInitialeCotAmortie"));
            BigDecimal valeurInitialeCotRecouvrement = getBigDecimal(poste.get("valeurInitialeCotRecouvrement"));

            BigDecimal recouvrement = new BigDecimal("0.0");
            BigDecimal montantNoteDeCredit = getBigDecimal(poste.get("montantNoteDeCredit"));
            recouvrement = recouvrement.add(montantNoteDeCredit);

            BigDecimal cumulRecouvrementCotisationAmortieCorrigee = getBigDecimal(poste
                    .get("cumulRecouvrementCotisationAmortie"));
            BigDecimal cumulRecouvrementCotisationAmortie = getBigDecimal(poste
                    .get("cumulRecouvrementCotisationAmortieCorrigee"));

            BigDecimal cumulCotisationAmortieCorrigee = getBigDecimal(poste.get("cumulCotisationAmortie"));
            BigDecimal cumulCotisationAmortie = getBigDecimal(poste.get("cumulCotisationAmortieCorrigee"));

            recouvrementPosteContainer.addPosteContainer(annee, idRubrique, idRubriqueIrrecouvrable,
                    numeroRubriqueIrrecouvrable, idRubriqueRecouvrement, numeroRubriqueRecouvrement,
                    libelleRubriqueRecouvrement, cumulCotisationAmortie, cumulCotisationAmortieCorrigee,
                    cumulRecouvrementCotisationAmortie, cumulRecouvrementCotisationAmortieCorrigee, recouvrement,
                    ordrePriorite, valeurInitialeCotAmortie, valeurInitialeCotRecouvrement, montantNoteDeCredit,
                    CATypeDeRecouvrementPoste.getEnumFromCodeSystem(poste.get("type")));
        }
        return recouvrementPosteContainer;
    }
}
