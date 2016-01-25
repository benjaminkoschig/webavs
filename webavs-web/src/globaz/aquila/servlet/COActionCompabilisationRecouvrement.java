package globaz.aquila.servlet;

import globaz.aquila.db.irrecouvrables.COComptabilisationRecouvrementViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.osiris.db.irrecouvrable.CARecouvrementCiContainer;
import globaz.osiris.db.irrecouvrable.CARecouvrementPosteContainer;
import globaz.osiris.db.irrecouvrable.CATypeDeRecouvrementPoste;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action permettant la comptabilisation des recouvrements
 * 
 * @author jts
 * 
 */
public class COActionCompabilisationRecouvrement extends COActionRecouvrementAbstract {

    public COActionCompabilisationRecouvrement(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action déclanchant l'exécution de la comptabilisation du recouvrement.
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination;
        COComptabilisationRecouvrementViewBean viewBean = new COComptabilisationRecouvrementViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setPosteContainer(initRecouvrementPostesContainer(request));
            viewBean.setCiContainer(initCiContainer(request));

            mainDispatcher.dispatch(viewBean, getAction());
            destination = "/" + getAction().getApplicationPart()
                    + "?userAction=aquila.irrecouvrables.recouvrementCompteAnnexe.chercher&process=launched";
            goSendRedirectWithoutParameters(destination, request, response);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
            goSendRedirect(destination, request, response);
        }
    }

    /**
     * Initialise un conteneur de recouvrement CI avec les données contenues dans la requête
     * 
     * @param request requête
     * @return le conteneur initialisé
     */
    private CARecouvrementCiContainer initCiContainer(HttpServletRequest request) throws ServletException, IOException {

        Map<String, Map<String, String>> rectificationsCI = getParametersMap(request,
                "(rectificationCI\\[)([0-9]+)(\\]\\[)([a-zA-Z]+)(\\])");

        CARecouvrementCiContainer ciContainer = new CARecouvrementCiContainer();

        for (Map<String, String> rectification : rectificationsCI.values()) {

            Integer annee = !JadeNumericUtil.isEmptyOrZero(rectification.get("annee")) ? new Integer(rectification.get(
                    "annee").trim()) : 0;
            String genreDecision = rectification.get("genreDecision");
            BigDecimal montantEtatCi = getBigDecimal(rectification.get("montantEtatCi"));
            BigDecimal montantRecouvrement = getBigDecimal(rectification.get("montantRecouvrement"));

            ciContainer.addRecouvrementCi(montantRecouvrement, montantEtatCi, annee, genreDecision);
        }

        return ciContainer;
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
            BigDecimal cumulCotisationAmortie = getBigDecimal(poste.get("cumulCotisationAmortie"));
            BigDecimal cumulRecouvrementCotisationAmortie = getBigDecimal(poste
                    .get("cumulRecouvrementCotisationAmortie"));
            BigDecimal valeurInitialeCotAmortie = getBigDecimal(poste.get("valeurInitialeCotAmortie"));
            BigDecimal valeurInitialeCotRecouvrement = getBigDecimal(poste.get("valeurInitialeCotRecouvrement"));
            BigDecimal cumulCotisationAmortieCorrigee = getBigDecimal(poste.get("cumulCotisationAmortieCorrigee"));
            BigDecimal cumulRecouvrementCotisationAmortieCorrigee = getBigDecimal(poste
                    .get("cumulRecouvrementCotisationAmortieCorrigee"));
            BigDecimal recouvrement = getBigDecimal(poste.get("recouvrement"));
            BigDecimal montantNoteDeCredit = getBigDecimal(poste.get("montantNoteDeCredit"));

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
