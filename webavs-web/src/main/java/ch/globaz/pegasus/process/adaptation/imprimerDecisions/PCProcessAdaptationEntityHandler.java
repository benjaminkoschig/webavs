package ch.globaz.pegasus.process.adaptation.imprimerDecisions;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCHomes;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpression;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpressionAncienne;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpressionAncienneSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.process.adaptation.TypeChambreHomeSeach;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeField;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeFieldSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.pcaccordee.Regimes02RFMVo;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCProcessAdaptationEntityHandler implements JadeProcessEntityInterface, JadeProcessEntityNeedProperties,
        JadeProcessEntityDataFind<PCProcessAdapationEnum> {

    private static final String NUMERO_AGENCE_PAR_DEFAUT = "000";
    private CaisseInfoPropertiesWrapper caisseInfosProperties = null;
    private String dateValidite;
    private final List<CommunicationAdaptationElement> globalContainer;
    private String idDecisionHeaderConjoint = null;
    private String idDecisionHeaderRequerant = null;
    private String idDemande = null;
    private List<PCAccordeeAdaptationImpression> pcAccordees = null;
    private List<PCAccordeeAdaptationImpressionAncienne> pcAccordeesPrecedentes = null;
    private PCAccordeeAdaptationImpression pcaConjoint = null;
    private PCAccordeeAdaptationImpression pcaRequerant = null;
    private Map<Enum<?>, String> properties = null;
    private Map<PCProcessAdapationEnum, String> propertiesCalcules;

    public PCProcessAdaptationEntityHandler(List<CommunicationAdaptationElement> container,
            CaisseInfoPropertiesWrapper caisseInfosProperties) {
        globalContainer = container;
        this.caisseInfosProperties = caisseInfosProperties;
    }

    private CommunicationAdaptationElement createConteneurAndInitialize(String idTiersAyantDroit, Demande demande,
                                                                        boolean isInTypeChambreNonMedicalise, String idDecision) throws JadePersistenceException, DemandeException,
            JadeApplicationServiceNotAvailableException, AdaptationException {
        CommunicationAdaptationElement current;
        current = new CommunicationAdaptationElement();
        current.setIdGestionnaire(demande.getSimpleDemande().getIdGestionnaire());
        current.setInTypeChambreNonMedicalise(isInTypeChambreNonMedicalise);
        current.setIdDecision(idDecision);
        initialiseConteneurs(current, idTiersAyantDroit);
        globalContainer.add(current);
        return current;
    }

    public String getDateValiditeMoisAnnee() {
        return dateValidite.substring(5);
    }

    private void initialiseConteneurs(CommunicationAdaptationElement container, String idTiers)
            throws JadePersistenceException, DemandeException, JadeApplicationServiceNotAvailableException,
            AdaptationException {
        // récupère les données de l'ayant droit
        try {
            PersonneEtendueComplexModel ayantDroit = TIBusinessServiceLocator.getPersonneEtendueService().read(idTiers);
            container.setNssAyantDroit(ayantDroit.getPersonneEtendue().getNumAvsActuel());
            container.setNomAyantDroit(PegasusUtil.formatNomPrenom(ayantDroit.getTiers()));
            container.setIdTiersAyantDroit(ayantDroit.getTiers().getIdTiers());
        } catch (JadeApplicationException e) {
            throw new AdaptationException("An error happened while trying to read tiers(" + idTiers + ")", e);
        }

    }

    private boolean isInChambreNoMedicalise(PCAccordeeAdaptationImpression pca) throws JadePersistenceException {
        if (pca == null) {
            throw new IllegalArgumentException("Unable to isInChambreNoMedicalise, the pca is null!");
        }
        if (IPCPCAccordee.CS_GENRE_PC_HOME.equals(pca.getCsGenrePC())) {
            TypeChambreHomeSeach search = new TypeChambreHomeSeach();
            search.setForCsCategorie(IPCHomes.CS_MEDICALISE);
            search.setForIdTiers(pca.getIdTiers());
            search.setForDateValable(properties.get(PCProcessAdapationEnum.DATE_ADAPTATION));
            return JadePersistenceManager.count(search) > 0;
        } else {
            return false;
        }
    }

    private List<RenteMembreFamilleCalculeField> loadDonneesPrestation(String dateValidite)
            throws JadePersistenceException, RenteAvsAiException, JadeApplicationServiceNotAvailableException {
        RenteMembreFamilleCalculeFieldSearch search = new RenteMembreFamilleCalculeFieldSearch();
        // search.setForCsEtatVersionDroit(IPCDroits.CS_HISTORISE);
        search.setForCsEtatVersionDroit(IPCDroits.CS_VALIDE);

        search.setForIdDemandePC(idDemande);
        search.setOrderKey(RenteMembreFamilleCalculeSearch.ORDER_DROIT);
        // search.setWhereKey(RenteMembreFamilleCalculeSearch.WITH_DATE_VALABLE);

        search.setWhereKey("withDateValableCommunicationAdaptation");

        search.setForDate(dateValidite.substring(3));
        search.setForIsPlanRetenu(true);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = PegasusServiceLocator.getRenteIjApiService().searchRenteMembreFamilleCalcule(search);

        List<RenteMembreFamilleCalculeField> result = new ArrayList<RenteMembreFamilleCalculeField>();

        for (JadeAbstractModel absDonnee : search.getSearchResults()) {
            RenteMembreFamilleCalculeField rente = (RenteMembreFamilleCalculeField) absDonnee;
            result.add(rente);
        }

        return result;
    }

    private void loadPCAccordees() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, AdaptationException {

        try {

            // récupération des nouvelles PCAccordees
            pcAccordees = new ArrayList<PCAccordeeAdaptationImpression>();

            pcaRequerant = PegasusServiceLocator.getPCAccordeeAdaptationImpressionService().search(
                    idDecisionHeaderRequerant);

            // ajout dans la liste
            pcAccordees.add(pcaRequerant);
            // Si conjoint, ajout
            if (idDecisionHeaderConjoint != null) {
                pcaConjoint = PegasusServiceLocator.getPCAccordeeAdaptationImpressionService().search(
                        idDecisionHeaderConjoint);
                pcAccordees.add(pcaConjoint);
            }

            String idDroit = pcaRequerant.getIdDroit();
            String idVersionDroit = pcaRequerant.getIdVersionDroit();

            // récupère les anciennes pcaccordees
            pcAccordeesPrecedentes = new ArrayList<PCAccordeeAdaptationImpressionAncienne>();
            PCAccordeeAdaptationImpressionAncienneSearch searchAnciens = new PCAccordeeAdaptationImpressionAncienneSearch();
            searchAnciens.setWhereKey("anciennesPCA");
            searchAnciens.setForIdDroit(idDroit);
            searchAnciens.setForIdVersionDroit(idVersionDroit);
            // searchAnciens.setForNoVersion(String.valueOf(Integer.parseInt(noVersion) - 1));
            searchAnciens.setForDateValidite(JadeDateUtil.convertDateMonthYear((PegasusDateUtil.addMonths(dateValidite,
                    -1))));

            searchAnciens = PegasusServiceLocator.getPCAccordeeAdaptationImpressionService().search(searchAnciens);

            for (JadeAbstractModel absModel : searchAnciens.getSearchResults()) {
                PCAccordeeAdaptationImpressionAncienne pca = (PCAccordeeAdaptationImpressionAncienne) absModel;
                pca.setPrecedent(true);
                pcAccordeesPrecedentes.add(pca);
            }

        } catch (PCAccordeeException e) {
            throw new AdaptationException("An error happened while trying to get decisions!", e);
        }

    }

    /**
     * Test définissant si la caisse avs versant la rente principale est la caisse taritant la décision On va tester
     * ici, le champ noCaisse avs saisi dans les données personelles, et les numeros de caisse Saisie dans les
     * propriétés.
     * 
     * Règles métiers: Chaine vide (ou nulle) --> on log un warning et on retourne false (ca ne matche pas) Si point pas
     * présent, on travaille si et seulement si, le numero de la caisse ne dépasse pas 3 caractere, dans ce cas, on
     * définis la valeur 000 pour le numero d'agence, et on test Si point ok, on teste les deux valeurs avec celles en
     * propriétés
     * 
     * @return
     */
    private boolean numeroCaisseAvsMatchCaisseVersantRente(String noCaisseAvs) {

        // null ou vide, cela ne matche pas
        if ((null == noCaisseAvs) || JadeStringUtil.isBlankOrZero(noCaisseAvs)) {
            JadeThread.logWarn(PCProcessAdaptationEntityHandler.class.getName(),
                    "pegasus.process.adaptation.noAgenceAvsVide");
            return false;
        }

        // si cela matche avec le noCaisse formaté, c'est ok on s'arrete la
        if (noCaisseAvs.equals(caisseInfosProperties.getNoCaisseFormatee())) {
            return true;
        }
        // on insiste, test sur les deux champ agences et caisse idépendant
        else {
            String noCaisseProp = caisseInfosProperties.getNoCaisse();
            String noAgenceProp = caisseInfosProperties.getNoAgence();

            String[] noSplit = noCaisseAvs.split("\\.");

            String noCaisse;
            String noAgence;

            // Test taille tableau, si
            switch (noSplit.length) {
                case 1:
                    if (noSplit[0].length() > 3) {
                        JadeThread.logWarn(PCProcessAdaptationEntityHandler.class.getName(),
                                "pegasus.process.adaptation.noAgenceAvsMalFormatte" + " " + noCaisseAvs);
                        return false;
                    } else {
                        noCaisse = noSplit[0];
                        noAgence = PCProcessAdaptationEntityHandler.NUMERO_AGENCE_PAR_DEFAUT;
                    }
                    break;

                case 2:
                    noCaisse = noSplit[0];
                    noAgence = noSplit[1];
                    break;

                default:
                    JadeThread.logWarn(PCProcessAdaptationEntityHandler.class.getName(),
                            "pegasus.process.adaptation.noAgenceAvsMalFormatte" + " " + noCaisseAvs);
                    return false;

            }

            // on teste la cohlrence des deux parties du numero, caisse et agence
            return ((Integer.parseInt(noCaisseProp) == Integer.parseInt(noCaisse)) && (Integer.parseInt(noAgenceProp) == Integer
                    .parseInt(noAgence)));
        }

    }

    private void processPCAccordees(CommunicationAdaptationElement container,
            CommunicationAdaptationElement containerConjoint) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, AdaptationException {
        for (PCAccordeeAdaptationImpression pca : pcAccordees) {
            PCAccordeeAdaptationImpressionAncienne pcaPrecedenteTrouvee = null;
            for (PCAccordeeAdaptationImpressionAncienne pcaPrecedente : pcAccordeesPrecedentes) {
                if (pca.getNssAyantDroit().equals(pcaPrecedente.getNssAyantDroit())) {
                    pcaPrecedenteTrouvee = pcaPrecedente;
                    break;
                }
            }

            if ((containerConjoint != null) && (pca == pcaConjoint)) {
                containerConjoint.addPCAccordee(pca, pcaPrecedenteTrouvee);
            } else {
                if (container != null) {
                    container.addPCAccordee(pca, pcaPrecedenteTrouvee);
                }
            }

        }

        // TODO récupère régime RFM
    }

    private void processPlanDeCalcul(CommunicationAdaptationElement container, SimplePlanDeCalcul planRetenu)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException {

        TupleDonneeRapport tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(
                new String(planRetenu.getResultatCalcul()));

        final String[] champs = new String[] { IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TOTAL,
                IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL, IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL,
                IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL,
                IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL,
                IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL,
                IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
                IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL,
                IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE,
                IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL };

        for (String champ : champs) {
            container.addClePlanCalcul(champ, tupleRoot);
        }
    }

    private void processPrestations(CommunicationAdaptationElement container,
            CommunicationAdaptationElement containerConjoint) throws JadePersistenceException, RenteAvsAiException,
            JadeApplicationServiceNotAvailableException {
        // récupère données financières
        List<RenteMembreFamilleCalculeField> rentesActuelles = loadDonneesPrestation(dateValidite);
        List<RenteMembreFamilleCalculeField> rentesPrecedentes = loadDonneesPrestation(PegasusDateUtil.addMonths(
                dateValidite, -1));

        if (container != null) {
            container.setDateValidite(dateValidite);
        }

        if (containerConjoint != null) {
            containerConjoint.setDateValidite(dateValidite);
        }

        for (RenteMembreFamilleCalculeField rente : rentesActuelles) {

            if (numeroCaisseAvsMatchCaisseVersantRente(rente.getNoCaisseAvs())) {
                RenteMembreFamilleCalculeField precedent = null;
                for (RenteMembreFamilleCalculeField renteMembreFamilleCalculeField : rentesPrecedentes) {
                    if (renteMembreFamilleCalculeField.getIdEntity().equals(rente.getIdEntity())) {
                        precedent = renteMembreFamilleCalculeField;
                        break;
                    }
                }
                if ((containerConjoint != null)
                        && IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(rente.getCsRoleFamillePC())) {
                    containerConjoint.addPrestationDonneeFinanciere(rente, precedent);
                } else {
                    if (container != null) {
                        container.addPrestationDonneeFinanciere(rente, precedent);
                    }
                }
            }

        }

    }

    private void processRegimesRFM(CommunicationAdaptationElement container,
            CommunicationAdaptationElement containerConjoint) throws JadeApplicationServiceNotAvailableException,
            Exception {

        String idTiersRequerant = container.getIdTiersAyantDroit();
        String idTiersConjoint = null;
        // liste pour recherche in
        ArrayList<String> listeTiers = new ArrayList<String>();
        listeTiers.add(idTiersRequerant);
        // si on a un conjoint on recupère lîdTiers
        if (containerConjoint != null) {
            idTiersConjoint = containerConjoint.getIdTiersAyantDroit();
            listeTiers.add(idTiersConjoint);
        }

        // liste des régimes pour requérant et conjoint
        ArrayList<Regimes02RFMVo> listeRegime = new ArrayList<Regimes02RFMVo>();

        // Recupération de la liste des régimes pour un tiers donné (ou deux si conjoint)
        listeRegime = PegasusServiceLocator.getPCAccordeeAdaptationImpressionService().searchRegimeRFM(listeTiers,
                dateValidite);

        // on gourpe les prestations de regimes par tiers
        Map<String, List<Regimes02RFMVo>> regimeByTiers = JadeListUtil.groupBy(listeRegime, new Key<Regimes02RFMVo>() {
            @Override
            public String exec(Regimes02RFMVo e) {
                return e.getIdTiers();
            }
        });

        // iteration
        for (String idTier : regimeByTiers.keySet()) {

            // Si plus de deux regimes
            if (regimeByTiers.get(idTier).size() > 2) {
                throw new PCAccordeeException("The regimes for the tier [id:" + idTier
                        + "] contain more than 2 prestations, while it must contains two maximum!");
            }
            // Si pas de --> probéème
            Regimes02RFMVo regimeCourant = null;
            Regimes02RFMVo regimeAncien = null;

            // par défaut regime courant, premier de la liste
            regimeCourant = regimeByTiers.get(idTier).get(0);
            // si 2 regimes retournées, on check la date de fin
            if (regimeByTiers.get(idTier).size() == 2) {
                // Si date de fin pas vide, on interverti les prestations
                if (!JadeStringUtil.isBlankOrZero(regimeCourant.getDateFinDroit())) {
                    regimeCourant = regimeByTiers.get(idTier).get(1);
                    regimeAncien = regimeByTiers.get(idTier).get(0);
                } else {
                    regimeAncien = regimeByTiers.get(idTier).get(1);
                }
            }

            // requérant
            if (idTier.equals(idTiersRequerant)) {
                container.addRegimeRFM(regimeCourant, regimeAncien);
            }
            // Conjoint
            else {
                if (containerConjoint != null) {
                    containerConjoint.addRegimeRFM(regimeCourant, regimeAncien);
                } else {
                    JadeThread.logWarn("", "Le régime courant n'a pas été inclus dans le décompte: idTiers:" + idTier
                            + ", csRégime:" + regimeCourant.getCsGenrePrestationAccordee());
                }
            }
        }

    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        dateValidite = "01." + properties.get(PCProcessAdapationEnum.DATE_ADAPTATION);

        idDecisionHeaderRequerant = propertiesCalcules.get(PCProcessAdapationEnum.ID_DECISION_HEADER);
        String idTiersAyantDroit = propertiesCalcules.get(PCProcessAdapationEnum.ID_TIERS_AYANT_DROIT);
        String idTiersConjoint = propertiesCalcules.get(PCProcessAdapationEnum.ID_TIERS_CONJOINT);

        if (propertiesCalcules.containsKey(PCProcessAdapationEnum.ID_DECISION_HEADER_CONJOINT)) {
            idDecisionHeaderConjoint = propertiesCalcules.get(PCProcessAdapationEnum.ID_DECISION_HEADER_CONJOINT);
        }

        // récupère PC accordée en premier afin de déterminer le genre de pc

        loadPCAccordees();

        Demande demande = PegasusServiceLocator.getDemandeService().read(idDemande);
        CommunicationAdaptationElement current = null;
        CommunicationAdaptationElement currentConjoint = null;

        current = createConteneurAndInitialize(idTiersAyantDroit, demande, isInChambreNoMedicalise(pcaRequerant),pcaRequerant.getIdDecision());

        if (!JadeStringUtil.isBlankOrZero(idDecisionHeaderConjoint)) {
            currentConjoint = createConteneurAndInitialize(idTiersConjoint, demande,
                    isInChambreNoMedicalise(pcaConjoint),pcaConjoint.getIdDecision());
        }

        if (current != null) {
            current.setDateValidite(dateValidite);
        }

        if (currentConjoint != null) {
            currentConjoint.setDateValidite(dateValidite);
        }

        // Traitement des rentes, si définit dans les propriétés
        if (PCproperties.getBoolean(EPCProperties.DISPLAY_RENTES_DECISION_ADAPTATION)) {
            processPrestations(current, currentConjoint);
        }

        // on traite les pca
        processPCAccordees(current, currentConjoint);

        try {
            // on traite les regime RFM
            if (current != null) {
                processRegimesRFM(current, currentConjoint);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PCAccordeeException("An error happened during retrieving Regime RFM");
        }

        // on traite les plan de calcul
        if (current != null) {
            processPlanDeCalcul(current, pcaRequerant.getPlanRetenu());
        }

        if (currentConjoint != null) {
            processPlanDeCalcul(currentConjoint, pcaConjoint.getPlanRetenu());
        }

    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        idDemande = entity.getIdRef();
    }

    @Override
    public void setData(Map<PCProcessAdapationEnum, String> map) {
        propertiesCalcules = map;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

}
