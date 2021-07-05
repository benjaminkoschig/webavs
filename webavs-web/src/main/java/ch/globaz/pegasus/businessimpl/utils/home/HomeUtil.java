package ch.globaz.pegasus.businessimpl.utils.home;

import globaz.babel.api.ICTDocument;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.VariablesTemplate;
import ch.globaz.common.document.babel.BabelTextDefinition;
import ch.globaz.common.document.babel.TextGiverBabel;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions.PlanCalculTextDefinition;

public class HomeUtil {

    public static Home readHomeByPlanCacule(SimplePCAccordee pcAccordee, TupleDonneeRapport tupleRoot)
            throws HomeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        Checkers.checkNotNull(pcAccordee, "pcAccordee");
        Checkers.checkNotNull(tupleRoot, "tupleRoot");
        return readHomeByPlanCacule(pcAccordee.getIdPCAccordee(), pcAccordee.getCsRoleBeneficiaire(), tupleRoot);
    }

    /**
     * Permet de trouver le home lié a une pca
     * 
     * @param idPca
     * @param csBeneficiaire
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws HomeException
     */
    public static Home readHomeByPlanCacule(String idPca, String csBeneficiaire)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, HomeException {
        TupleDonneeRapport tuple = readPlanCalculRetenu(idPca);
        Checkers.checkNotNull(idPca, "idPca");
        Checkers.checkNotNull(csBeneficiaire, "csBeneficiaire");
        return readHomeByPlanCacule(idPca, csBeneficiaire, tuple);
    }

    private static String formatDesignationHome(Home home, String template) {
        Map<String, String> variables = new HashMap<String, String>();

        if (home.getAdresse().getLocalite().getNumPostal() != null) {
            variables.put("NPA", home.getAdresse().getLocalite().getNumPostal().substring(0, 4));
        }
        variables.put("LOCALITE", home.getAdresse().getLocalite().getLocalite());
        variables.put("DESIGNATION", home.getAdresse().getTiers().getDesignation1() + " "
                + home.getAdresse().getTiers().getDesignation2());

        variables.put("NOM_BATIMENT", home.getSimpleHome().getNomBatiment());
        variables.put("NUMERO", home.getSimpleHome().getNumeroIdentification());
        variables.put("EST_HORS_CANTON",
                JadeProcessCommonUtils.translateBoolean(home.getSimpleHome().getIsHorsCanton()));
        variables.put("ID", home.getSimpleHome().getId());

        VariablesTemplate variablesTemplate = new VariablesTemplate(template);

        return variablesTemplate.render(variables);
    }

    public static String formatDesignationHome(Home home, TextGiver<BabelTextDefinition> textGiver)
            throws CatalogueTexteException, JadeApplicationServiceNotAvailableException, Exception {
        return HomeUtil.formatDesignationHome(home, textGiver.resolveText(PlanCalculTextDefinition.HOME_DESCRIPTION));
    }

    public static String formatDesignationHome(Home home) throws CatalogueTexteException,
            JadeApplicationServiceNotAvailableException, Exception {
        Map<Langues, CTDocumentImpl> documentsBabel;
        documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForTypeDecision(
                IPCCatalogueTextes.BABEL_DOC_NAME_PLAN_CALCUL);

        // TODO: setter la langue correctement car pour le moment FR par défaut
        ICTDocument document = documentsBabel.get(Langues.Francais);
        TextGiver<BabelTextDefinition> textGiver = new TextGiverBabel(document);
        return formatDesignationHome(home, textGiver);
    }

    private static Home readHomeByPlanCacule(String idPca, String csBeneficiaire, TupleDonneeRapport tuple)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, HomeException {
        // recup du tuple des idHome
        TupleDonneeRapport tupleIdHome = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_ID_HOME);

        Home home = null;
        if (tupleIdHome != null) {
            // recup du bénéficiaire pour rechercher l'idHome dans le blob
            String idHome = tupleIdHome.getLegendeEnfant(csBeneficiaire);
            // recup du home
            if (idHome != null) {
                home = PegasusServiceLocator.getHomeService().read(idHome);
            }
        }
        return home;
    }

    /**
     * Permet de trouver le home partiel lié a une pca
     *
     * @param idPca
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws HomeException
     */
    public static Home readHomePartielByPlanCacule(String idPca, String idTier)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, HomeException {
        TupleDonneeRapport tuple = readPlanCalculRetenu(idPca);
        return readHomePartielByPlanCacule(tuple, idTier);
    }

    private static Home readHomePartielByPlanCacule(TupleDonneeRapport tuple, String idTier)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, HomeException {
        // recup du tuple des idHome
        TupleDonneeRapport tupleIdHome = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL);

        Home home = null;
        if (tupleIdHome != null) {

            String idHome = null;
            TupleDonneeRapport tupleHome = tupleIdHome.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME);

            if(tupleHome != null) {
                idHome = String.valueOf(tupleHome.getValeur());
            } else {
                TupleDonneeRapport tupleRequerant = tupleIdHome.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_IDTIER_REQUERANT);
                TupleDonneeRapport tupleConjoint = tupleIdHome.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_IDTIER_CONJOINT);

                if (tupleConjoint != null && Float.valueOf(idTier) == tupleConjoint.getValeur()) {
                    TupleDonneeRapport tupleHomeConjoint = tupleIdHome.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME_CONJOINT);
                    idHome = String.valueOf(tupleHomeConjoint.getValeur());
                } else {
                    TupleDonneeRapport tupleHomeRequerant = tupleIdHome.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME_REQUERANT);
                    idHome = String.valueOf(tupleHomeRequerant.getValeur());
                }
            }
            // recup du home
            if (idHome != null) {
                home = PegasusServiceLocator.getHomeService().read(idHome);
            }
        }
        return home;
    }

    private static TupleDonneeRapport readPlanCalculRetenu(String idPca) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        // Recherche du pla de calcul et du blob
        SimplePlanDeCalculSearch search = new SimplePlanDeCalculSearch();
        search.setForIdPCAccordee(idPca);
        search.setForIsPlanRetenu(Boolean.TRUE);
        SimplePlanDeCalcul plan = (SimplePlanDeCalcul) JadePersistenceManager.search(search, true).getSearchResults()[0];
        String byteToArrayString = new String(plan.getResultatCalcul());
        TupleDonneeRapport tuple = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(
                byteToArrayString);
        return tuple;
    }

}
