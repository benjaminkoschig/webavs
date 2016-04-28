package globaz.corvus.api.arc.downloader.domaine;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.arc.downloader.REAnnoncesHermesMap;
import globaz.corvus.api.arc.downloader.REDownloaderInscriptionsCI;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TIPersonne;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import ch.globaz.hera.business.models.famille.MembreFamilleSearch;
import ch.globaz.hera.business.services.HeraServiceLocator;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimaps;

/**
 * Classe gérant le recherche du numero nss/navs pour le traitement d'importation de ci dans les rentes
 * 
 * @see globaz.corvus.api.arc.downloader.RETraitementImportAnnonces
 * 
 */
public final class NSSAyantDroitV1 {

    enum AnnonceCINSSFinderCharacterPosition {
        CI_NORMAL_START(36),
        CI_NORMAL_SIZE(11),
        CI_ADDITIONNEL_START(31),
        CI_ADDITIONNEL_SIZE(11);

        public int valeur;

        AnnonceCINSSFinderCharacterPosition(int valeur) {
            this.valeur = valeur;
        }

    }

    private String nss;
    private String nssRecuDansAnnonce;
    private final REAnnoncesHermesMap annonce;
    private BSession session;
    FWMemoryLog log;
    PRTiersWrapper tiers;

    /**
     * 
     * @param annonce l'annonce CI en entrée permettant de trouver le nss, NullPointer Exception si null
     * @param session la session, NullPointer Exception si null
     * @param log l'instance de FWMemoryLog permettant de logguer le traitement, NullPointer Exception si null
     */
    public NSSAyantDroitV1(REAnnoncesHermesMap annonce, BSession session, FWMemoryLog log) {

        Preconditions.checkNotNull(annonce, "The annonce can't be null");
        Preconditions.checkNotNull(session, "The session can't be null");
        Preconditions.checkNotNull(log, "The FWMemoryLog can't be null");
        this.annonce = annonce;
        this.session = session;
        this.log = log;

    }

    protected NSSAyantDroitV1() {
        annonce = null;
    };

    public PRTiersWrapper getTiers() {
        return tiers;
    }

    public boolean existInPyxs() {
        return tiers != null;
    }

    public String getNss() {
        return nss;
    }

    public String getNssRecuDansAnnonce() {
        return nssRecuDansAnnonce;
    }

    protected PRTiersWrapper findTiersWrapperById(String idTiers) throws Exception {
        return PRTiersHelper.getTiersById(session, idTiers);
    }

    /**
     * Retourne une instance de la classe en effectuant le traitement du nss en plus
     * 
     * @param type le type de ci
     * @return une instance de <code>NSSAyantDroitV1</code> avec le traitement d'affection du nss effectué
     * @throws Exception
     */
    public NSSAyantDroitV1 forTypeCI(TypeCI type) throws Exception {

        String findingNss;

        // recupération depuis annonce
        findingNss = recuperationNSSNAVSDpuisAnnonce(type);

        // recheche dans nssra
        findingNss = findNssNavsInNSSRA(findingNss);

        // rechechre dans ci avec bumeros lies
        List<PRTiersWrapper> tiersTrouves;
        tiersTrouves = findNssNavsInCi(findingNss);

        List<String> idTiersATraiter = findIdTiersWithNewNSs(tiersTrouves);

        if (idTiersATraiter.size() == 1) {                                         // 1 tiers trouvés, on utilise le nss

            PRTiersWrapper tiersWrapper = findTiersWrapperById(idTiersATraiter.get(0));
            tiers = tiersWrapper;
            findingNss = NSUtil.unFormatAVS(tiers.getNSS());

        } else if (idTiersATraiter.size() > 1) {                                   // Plusieurs tiers trouvé

            if (areTiersInCollectionIdentical(idTiersATraiter)) {                  // le tiers sont identiques, on utilise le nss
                findingNss = tiersTrouves.get(0).getNSS();
                tiers = tiersTrouves.get(0);
            } else {

                List<String> idTiersWithDemandePrestation = filtreTiersHasDemandePrestation(idTiersATraiter);

                boolean hasDemantePrestations = !idTiersWithDemandePrestation.isEmpty();

                List<PRTiersWrapper> tiersPresentSF = null;

                if (!hasDemantePrestations) {
                    tiersPresentSF = findTiersInSF(idTiersATraiter);

                    if (tiersPresentSF.size() == 1) {
                        findingNss = tiersPresentSF.get(0).getNSS();
                    }
                }

                List<PRTiersWrapper> tiersActif = null;

                if (null == tiersPresentSF) {// dans le cas d'une demande de prestation
                    tiersActif = getTiersActif(findingNss, idTiersWithDemandePrestation);
                } else {// pas de demande de prestations
                    tiersActif = getTiersActif(findingNss, getTiersAsId(tiersPresentSF));
                }

                if (tiersActif.size() == 1) {
                    findingNss = tiersActif.get(0).getNSS();
                    tiers = tiersTrouves.get(0);
                } else if (tiersActif.size() == 0) {
                    logMessage("Aucun tiers actif trouvés");
                } else {

                    StringBuilder msgAppend = new StringBuilder("");

                    for (PRTiersWrapper tier : tiersActif) {
                        msgAppend.append("[idTiers: ").append(tier.getIdTiers()).append(", NSS: ")
                                .append(tier.getNSS()).append("]");
                    }

                }

            }
        }

        nss = findingNss;

        return this;
    }

    private List<String> getTiersAsId(List<PRTiersWrapper> tiersWrapper) throws Exception {

        List<String> idTiers = new ArrayList<String>();

        for (PRTiersWrapper tier : tiersWrapper) {
            idTiers.add(tier.getIdTiers());
        }

        return idTiers;
    }

    private List<String> findIdTiersWithNewNSs(List<PRTiersWrapper> tiersTrouves) throws Exception {

        List<String> idTiersWithNewNss = new ArrayList<String>();

        if (!session.isConnected()) {
            session = (BSession) PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS);
        }

        for (PRTiersWrapper tiers : tiersTrouves) {
            TIPersonneAvsManager mgr = new TIPersonneAvsManager();

            mgr.setISession(session);
            mgr.setForNumAvsActuel(tiers.getNSS());
            mgr.setForIncludeInactif(true);
            mgr.find();

            if (!mgr.getContainer().isEmpty()) {

                for (Object o : mgr.getContainer()) {
                    TIPersonne personne = (TIPersonne) o;
                    idTiersWithNewNss.add(personne.getIdTiers());
                }
            }
        }

        return idTiersWithNewNss;

    }

    private List<PRTiersWrapper> findNssNavsInCi(String nss) throws Exception {
        List<PRTiersWrapper> tiersTrouves;
        RECILiesWrapper wrapper = findNssNavsInCI(nss);
        // Si nss trouves
        if (!wrapper.getNssLiesTrouves().isEmpty()) {
            tiersTrouves = findTiersByNss(Arrays.copyOf(wrapper.getNssLiesTrouves().toArray(), wrapper
                    .getNssLiesTrouves().toArray().length, String[].class));
        } else {
            tiersTrouves = findTiersByNss(nss);
        }
        return tiersTrouves;
    }

    private String recuperationNSSNAVSDpuisAnnonce(TypeCI type) {
        String nss;
        // on recherche le nss dans l'annonce
        nss = findNssNavsInAnnonce(type);
        nssRecuDansAnnonce = new String(nss);

        // conversion du numero en nss
        if (nss.startsWith("-")) {
            nss = JadeStringUtil.change(nss, "-", "756");
        }
        return nss;
    }

    private void logMessage(String text) {
        log.logMessage(text, FWMessage.INFORMATION, this.getClass().getName());
    }

    private List<PRTiersWrapper> getTiersActif(String nss, List<String> idTiersToDeal) throws Exception {

        List<PRTiersWrapper> tiersActif = new ArrayList<PRTiersWrapper>();

        for (String idTiers : idTiersToDeal) {

            PRTiersWrapper tier = findTiersWrapperById(idTiers);

            if (!tier.isInactif()) {
                tiersActif.add(tier);
            }
        }

        return tiersActif;
    }

    private List<PRTiersWrapper> findTiersInSF(List<String> idTiersTrouves) {

        List<PRTiersWrapper> tiersPresentSF = new ArrayList<PRTiersWrapper>();

        try {
            BSessionUtil.initContext(session, this);

            for (String tivb : idTiersTrouves) {

                PRTiersWrapper tier = findTiersWrapperById(tivb);

                MembreFamilleSearch search = new MembreFamilleSearch();

                search.setForIdTiers(tivb);
                if (HeraServiceLocator.getMembreFamilleService().count(search) > 0) {
                    tiersPresentSF.add(tier);
                }

            }
        } catch (Exception e) {
            JadeLogger.error(this, "An error happened during retrieving MembreFamille: " + e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }

        return tiersPresentSF;

    }

    private List<String> filtreTiersHasDemandePrestation(List<String> idTiersTrouves) throws Exception {

        List<String> idTiersWithDemandePrestations = new ArrayList<String>();

        PRDemandeManager m;

        for (String tivb : idTiersTrouves) {

            m = new PRDemandeManager();
            m.setForTypeDemande(IPRDemande.CS_TYPE_RENTE);
            m.setForIdTiers(tivb);
            m.setSession(session);
            m.find();

            if (!m.getContainer().isEmpty()) {
                // iteration sur le tiers ayant une demande de rente
                for (Object t : m.getContainer()) {
                    PRDemande demandePourTiers = (PRDemande) t;

                    idTiersWithDemandePrestations.add(demandePourTiers.getIdTiers());
                }
            }
        }

        return idTiersWithDemandePrestations;
    }

    @SuppressWarnings("unchecked")
    private List<String> getListIdCiesLies(CICompteIndividuel compteIndividuel, BSession session) throws Exception {
        List<String> idsCiLies = compteIndividuel.getIdCILies(session.getCurrentThreadTransaction());

        idsCiLies.remove(new String(compteIndividuel.getId()));

        return idsCiLies;
    }

    private RECILiesWrapper findNssNavsInCI(String nss) throws Exception {
        CICompteIndividuel compteIndividuel = CICompteIndividuel.loadCI(nss, session.getCurrentThreadTransaction());// TODO
                                                                                                                    // vois
        RECILiesWrapper wrapper = new RECILiesWrapper();                                                                                                      // formattage

        // si le ci retourné est null, on s'arrete là
        if (null != compteIndividuel) {

            wrapper.setCompteIndividuel(compteIndividuel);

            List<String> idsCiLies = getListIdCiesLies(compteIndividuel, session);

            // parcours des ci lies et récuératoin du ou des nss
            for (String idCi : idsCiLies) {
                CICompteIndividuel ci = new CICompteIndividuel();
                ci.setId(idCi);
                ci.setSession(session);
                ci.retrieve();

                // Si ci retrouvé ET que ci contient un nouveau nss
                if (!ci.isNew() && ci.getNnss()) {
                    wrapper.addNssLie(ci.getNssFormate());

                }
            }

        }
        return wrapper;
    }

    protected static ImmutableMap<String, Collection<PRTiersWrapper>> groupTiersListByIdTiers(
            List<PRTiersWrapper> tiersTrouves) {

        Preconditions.checkNotNull(tiersTrouves);

        Function<PRTiersWrapper, String> groupeByIdTiers = new Function<PRTiersWrapper, String>() {

            @Override
            public String apply(PRTiersWrapper tiers) {
                return tiers.getIdTiers();
            }

        };

        return Multimaps.index(tiersTrouves, groupeByIdTiers).asMap();
    }

    protected boolean areTiersInCollectionIdentical(List<String> idTiersTrouves) throws Exception {

        List<PRTiersWrapper> tiersWrapperWithId = new ArrayList<PRTiersWrapper>();

        for (String idTiers : idTiersTrouves) {
            tiersWrapperWithId.add(findTiersWrapperById(idTiers));
        }

        ImmutableMap<String, Collection<PRTiersWrapper>> tiersGroupeByIdTiers = groupTiersListByIdTiers(tiersWrapperWithId);

        return tiersGroupeByIdTiers.keySet().size() == 1;

    }

    /**
     * Recherche nu nss ou du numero avs dans l'annonce
     * 
     * @param type le type de ci
     * @param nss le nss au format String
     * @return le nss récupéré dans l'annonce
     */
    protected String findNssNavsInAnnonce(TypeCI type) {

        String nss;

        switch (type) {
            case CI_NORMAL:

                nss = JadeStringUtil.substring(annonce.getAnnonce().getChampEnregistrement(),
                        AnnonceCINSSFinderCharacterPosition.CI_NORMAL_START.valeur,
                        AnnonceCINSSFinderCharacterPosition.CI_NORMAL_SIZE.valeur);
                break;

            case CI_ADDITIONNEL:
                nss = JadeStringUtil.substring(annonce.getAnnonce().getChampEnregistrement(),
                        AnnonceCINSSFinderCharacterPosition.CI_ADDITIONNEL_START.valeur,
                        AnnonceCINSSFinderCharacterPosition.CI_ADDITIONNEL_SIZE.valeur);
                break;

            default:
                throw new IllegalArgumentException("The type passed in parameter must be of type "
                        + TypeCI.class.getName());
        }
        return nss;
    }

    /**
     * Recherche dans NSSRA (si existante) si une correspondance existe pour le nss7navs passé en paramètre
     * Si correspondance existante, redéfinition du nss/navs
     * 
     * @param nssToMatch le nss avec lequel on chercher une correpondance
     */
    private String findNssNavsInNSSRA(String nssToMatch) {

        String nssConcordance = NSUtil.returnNNSS(session, nssToMatch);

        if (nssConcordance != null) {
            nssToMatch = nssConcordance;
        }

        return nssToMatch;
    }

    private List<PRTiersWrapper> findTiersByNss(String... nss) throws Exception {
        List<PRTiersWrapper> personnesAvs = new ArrayList<PRTiersWrapper>();

        for (String nssToFind : nss) {

            PRTiersWrapper tiers = REDownloaderInscriptionsCI.getTiersFromNss(session,
                    session.getCurrentThreadTransaction(), nssToFind);

            if (tiers != null) {
                personnesAvs.add(tiers);
            }

        }

        return personnesAvs;

    }

    public boolean areTwoNssEquals() {
        return nssRecuDansAnnonce.equals(nss);
    }

    void setNss(String nss) {
        this.nss = new String(nss);
    }

    @Override
    public String toString() {
        return "NSSAyantDroitV1 [nss=" + nss + ", nssRecuDansAnnonce=" + nssRecuDansAnnonce + "]";
    }

}
