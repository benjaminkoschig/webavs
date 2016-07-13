/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.jade.businessimpl.services.codesysteme.JadeCodeSystemeServiceImpl;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * Classe encapsulant les information nécessaires à l'affichage du plan de calcul pc
 */
public class PCPlanCalculHandler {

    private static final String AUTRE_RENTE_STRING_TO_REPLACE = "{textelibre}";
    private static final String DESC_BALISE_FERME = "</span>";

    /************************************************ CONSTANTES ************************************************/
    private static final String DESC_BALISE_OUVRE = "<br /><span class='slib'>";

    private JadeCodeSystemeService codeSystemService = new JadeCodeSystemeServiceImpl();
    private JadeCodeSysteme codeSystem = null;

    /**
     * Constructeur statique pour le chargement du plan de calcul via l'id de la pca<br/>
     * Va charger le plan de calcul défini comme retenu pour la pc<br/>
     * 
     * @param session
     *            l'instance de BSession
     * @param idPca
     *            l'identifiant de la pcAccordée
     * @param idBeneficiaire
     *            l'id tiers bénéficiaire de la pc
     * @return l'instance généré avec l'id de la pc
     * @throws CalculException
     *             exception généré si un problème survient lors de la lecture du plan de calcul
     * @throws RemoteException
     *             si problème avec appel RMI (le cas échéant)
     * @throws PCAccordeeException
     *             exception levé si problème lors de la lecture de la pc
     * @throws JadeApplicationServiceNotAvailableException
     *             exception levé lors de l'appels aux services
     * @throws JadePersistenceException
     *             exception levé lors de la lecture en base de données
     */
    public static PCPlanCalculHandler getHandlerForIdPca(BISession session, String idPca, String idBeneficiaire)
            throws CalculException, RemoteException, PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        PCPlanCalculHandler pcalHandler = new PCPlanCalculHandler(session, idBeneficiaire);
        pcalHandler.loadPlanCalculRetenuForPca(idPca);
        return pcalHandler;
    }

    /**
     * Constructeur statique pour le chargement du plan de calcul via l'id du plan de calcul</br>
     * 
     * @param session
     *            l'instance de BSession
     * @param idPca
     *            l'identifiant de la pcAccordée
     * @param idBeneficiaire
     *            l'id tiers bénéficiaire de la pc
     * @return l'instance généré avec l'id de la pc
     * @throws CalculException
     *             exception généré si un problème survient lors de la lecture du plan de calcul
     * @throws RemoteException
     *             si problème avec appel RMI (le cas échéant)
     * @throws PCAccordeeException
     *             exception levé si problème lors de la lecture de la pc
     * @throws JadeApplicationServiceNotAvailableException
     *             exception levé lors de l'appels aux services
     * @throws JadePersistenceException
     *             exception levé lors de la lecture en base de données
     */
    public static PCPlanCalculHandler getHandlerForIdPlanCalcul(BISession session, String idPcal, String idBeneficiaire)
            throws CalculException, RemoteException, PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        PCPlanCalculHandler pcalHandler = new PCPlanCalculHandler(session, idBeneficiaire);
        pcalHandler.loadPlanCalcul(idPcal);
        return pcalHandler;

    }

    /* Listes des différents blocs du plan de calcul */
    private ArrayList<PCLignePlanCalculHandler> blocDepensesReconnues = new ArrayList<PCLignePlanCalculHandler>(0);

    private ArrayList<PCLignePlanCalculHandler> blocFortune = new ArrayList<PCLignePlanCalculHandler>(0);

    private ArrayList<PCLignePlanCalculHandler> blocResume = new ArrayList<PCLignePlanCalculHandler>(0);
    private ArrayList<PCLignePlanCalculHandler> blocRevenusDeterminants = new ArrayList<PCLignePlanCalculHandler>(0);
    private String conjointInfos = null;
    private ArrayList<String> enfantsCompris = null;
    private String idBeneficiaire = null;
    /********************************************** FIN CONSTANTES **********************************************/
    private String nssInfos = null;
    private SimplePlanDeCalcul planDeCalcul = null;
    private String requerantInfos = null;

    /* Objets session */
    private BISession session = null;

    /* Objet TupleDonneesRapport */
    private TupleDonneeRapport tupleRoot = null;

    /**
     * Constructeur privé. Utilisé par les constrcuteur statique.
     * 
     * @param session
     * @param idBeneficiaire
     * @throws CalculException
     * @throws RemoteException
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private PCPlanCalculHandler(BISession session, String idBeneficiaire) throws CalculException, RemoteException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        this.session = session;
        this.idBeneficiaire = idBeneficiaire;

    }

    private void buildBeneficiaireString(String nom, String prenom, String nss, String naiss) {
        requerantInfos = nom + " " + prenom + " - " + naiss;
        nssInfos = nss;
    }

    private void buildConjointString(String nom, String prenom, String nss, String naiss) {
        conjointInfos = nom + " " + prenom + " - " + naiss;
    }

    private String builDescription(String desc) {
        return PCPlanCalculHandler.DESC_BALISE_OUVRE + desc + PCPlanCalculHandler.DESC_BALISE_FERME;
    }

    private void buildLibellesGeneraux() throws PersonneDansPlanCalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
        search.setForIdPcal(planDeCalcul.getIdPlanDeCalcul());
        search.setForComprisPcal(Boolean.TRUE);
        search.setOrderKey("orderByNaissance");

        enfantsCompris = new ArrayList<String>();

        for (JadeAbstractModel model : PegasusServiceLocator.getPCAccordeeService().search(search).getSearchResults()) {
            if (((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getSimpleDroitMembreFamille()
                    .getCsRoleFamillePC().equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT)) {
                enfantsCompris.add(((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                        .getPrenom()
                        + " "
                        + ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille().getNom()
                        + " - "
                        + ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                .getDateNaissance());
            } else {
                if (idBeneficiaire.equals(((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille()
                        .getMembreFamille().getPersonneEtendue().getTiers().getIdTiers())) {
                    buildBeneficiaireString(((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille()
                            .getMembreFamille().getPrenom(), ((PlanDeCalculWitMembreFamille) model)
                            .getDroitMembreFamille().getMembreFamille().getNom(),
                            ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                    .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel(),
                            ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                    .getDateNaissance());

                } else {

                    buildConjointString(((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille()
                            .getMembreFamille().getPrenom(), ((PlanDeCalculWitMembreFamille) model)
                            .getDroitMembreFamille().getMembreFamille().getNom(),
                            ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                    .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel(),
                            ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                    .getDateNaissance());
                }
            }
        }
    }

    public ArrayList<PCLignePlanCalculHandler> createBlocDepensesReconnues(String langueTiers) throws RemoteException {
        PCGroupeDepensesHandler groupeDepenses = new PCGroupeDepensesHandler(tupleRoot);
        return dealLibelle(groupeDepenses.getGroupList(), false, langueTiers);
    }

    /**
     * Creation du bloc fortune et retour de la liste des ligne du plan de calcul
     * 
     * @return ArrayList contenant les lignes du plan de calcul
     * @throws RemoteException
     */
    public ArrayList<PCLignePlanCalculHandler> createBlocFortune(String langueTiers) throws RemoteException {
        PCGroupeFortuneHandler groupeFortune = new PCGroupeFortuneHandler(tupleRoot);
        return dealLibelle(groupeFortune.getGroupList(), false, langueTiers);
    }

    /**
     * Creation des lignes du bloc final de resume
     * 
     * @throws RemoteException
     */
    public ArrayList<PCLignePlanCalculHandler> createBlocResume(String langueTiers) throws RemoteException {
        PCGroupeTotalHandler groupeTotal = new PCGroupeTotalHandler(tupleRoot);
        return dealLibelle(groupeTotal.getGroupList(langueTiers), true, langueTiers);
    }

    public ArrayList<PCLignePlanCalculHandler> createBlocRevenusDeterminants(String langueTiers)
            throws RemoteException, CalculException {
        PCGroupeRevenusHandler groupeRevenus = new PCGroupeRevenusHandler(tupleRoot);

        return dealLibelle(groupeRevenus.getGroupList(), false, langueTiers);

    }

    private ArrayList<PCLignePlanCalculHandler> dealLibelle(ArrayList<PCLignePlanCalculHandler> liste,
            Boolean isForTotal, String langueTiers) throws RemoteException {

        // pour le groupe total, les libelles des lignes ne sont pas reprises des codessystèmes
        if (isForTotal) {
            // iteration sur toutes les lignes
            for (PCLignePlanCalculHandler ligne : liste) {
                ligne.setLibelle(ligne.getCsCode());
            }
        } else {
            // iteration sur toutes les lignes
            for (PCLignePlanCalculHandler ligne : liste) {
                String libelle = null;

                if (ligne != null) {
                    /* Découpage libelle si description */
                    String[] lib = resolveSystemCode(langueTiers, ligne);
                    /*
                     * Test des ligne ayant une legende pour l'instant imputation fortune nette, et salaire privilegeie
                     */
                    if (ligne.getCsCode().equals(IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL)
                            || ligne.getCsCode().equals(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE)) {
                        lib[0] = lib[0].replace("{fraction}", ligne.getLegende());
                    }

                    if (ligne.getCsCode().equals(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE)) {
                        lib[0] = lib[0].replace("{fraction}", ligne.getLegende());
                    }

                    if (ligne.getCsCode().equals(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_PLAFOND)) {
                        lib[0] = lib[0].replace("{montant}", new FWCurrency(ligne.getLegende()).toStringFormat());
                    }
                    if (ligne.getCsCode().equals(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL)) {
                        lib[0] = lib[0].replace("{montant}", new FWCurrency(ligne.getLegende()).toStringFormat());
                    }
                    // Gestion autre rentes texte libre
                    if (ligne.getCsCode().equals(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_AUTRE_RENTE)) {
                        @SuppressWarnings("rawtypes")
                        Map listeEnfants = tupleRoot.getOrCreateEnfant(ligne.getCsCode()).getEnfants();
                        @SuppressWarnings("rawtypes")
                        Collection values = listeEnfants.values();
                        TupleDonneeRapport sousTuple = (TupleDonneeRapport) values.toArray()[0];
                        lib[0] = PRStringUtils.replaceString(lib[0], PCPlanCalculHandler.AUTRE_RENTE_STRING_TO_REPLACE,
                                sousTuple.getLegende());
                    }
                    // si une ligne libelle, pas description
                    if (lib.length == 1) {
                        libelle = lib[0];
                    } else {
                        libelle = lib[0] + builDescription(lib[1]);
                    }
                    ligne.setLibelle(libelle);
                }

            }
        }

        return liste;
    }

    /**
     * Permet de retrouver le code système et de le place dans un tableau de caractère.
     * Si la langue du tiers est passée en paramètre, on va utiliser le service JadeCodeSystemService. Sinon le
     * traitement traditionnel par la session est laissé.
     * Le traitement traditionnel par la session a été laissé intentionnellement en raison des dépendances de la méthode
     * mère "generateBlocs" avec plusieurs process.
     * 
     * @param langueTiers
     * @param ligne
     * @return un code système utilisable par la méthode dealLibelle
     * @throws RemoteException
     */
    private String[] resolveSystemCode(String langueTiers, PCLignePlanCalculHandler ligne) throws RemoteException {
        String[] lib = null;
        if (langueTiers == null) {
            lib = session.getCodeLibelle(ligne.getCsCode()).split("@");
        } else {
            try {
                codeSystem = codeSystemService.getCodeSysteme(ligne.getCsCode());
            } catch (JadePersistenceException e) {
                throw new CommonTechnicalException("An error happened while trying to load the system code", e);
            }
            String codeSystemTranslated = " ";
            if (codeSystem != null) {
                codeSystemTranslated = codeSystem.getTraduction(LanguageResolver.resolveISOCode(langueTiers));
            }
            lib = codeSystemTranslated.split("@");
        }
        return lib;
    }

    /**
     * Cette signature permet de tenir compte de la langue du tiers bénéficiaire de la demande
     * 
     * @param tupleRoot
     * @param langueTiers
     * @throws RemoteException
     * @throws CalculException
     */
    public void generateBlocs(TupleDonneeRapport tupleRoot, String langueTiers) throws RemoteException, CalculException {
        this.tupleRoot = tupleRoot;
        blocFortune = createBlocFortune(langueTiers);
        blocRevenusDeterminants = createBlocRevenusDeterminants(langueTiers);
        blocDepensesReconnues = createBlocDepensesReconnues(langueTiers);
        blocResume = createBlocResume(langueTiers);
    }

    public void generateBlocs(TupleDonneeRapport tupleRoot) throws RemoteException, CalculException {
        this.generateBlocs(tupleRoot, null);
    }

    /**
     * @return the blocDepensesReconnues
     */
    public ArrayList<PCLignePlanCalculHandler> getBlocDepensesReconnues() {
        return blocDepensesReconnues;
    }

    /**
     * @return the blocFortune
     */
    public ArrayList<PCLignePlanCalculHandler> getBlocFortune() {
        return blocFortune;
    }

    /**
     * @return the blocResume
     */
    public ArrayList<PCLignePlanCalculHandler> getBlocResume() {
        return blocResume;
    }

    /**
     * @return the blocRevenusDeterminants
     */
    public ArrayList<PCLignePlanCalculHandler> getBlocRevenusDeterminants() {
        return blocRevenusDeterminants;
    }

    public String getConjointInfos() {
        return conjointInfos;
    }

    public ArrayList<String> getEnfantsCompris() {
        return enfantsCompris;
    }

    public String getNssInfos() {
        return nssInfos;
    }

    public SimplePlanDeCalcul getPlanDeCalcul() {
        return planDeCalcul;
    }

    public String getRequerantInfos() {
        return requerantInfos;
    }

    private void loadPlanCalcul(String idPcal) throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        planDeCalcul = PegasusServiceLocator.getSimplePlanDeCalculService().read(idPcal);
        buildLibellesGeneraux();

    }

    private void loadPlanCalculRetenuForPca(String idPca) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        planDeCalcul = PegasusServiceLocator.getSimplePlanDeCalculService().readPlanRetenuForIdPca(idPca);
        buildLibellesGeneraux();
    }

}
