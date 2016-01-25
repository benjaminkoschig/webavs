package ch.globaz.pegasus.businessimpl.services.process.adaptation;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Adresse;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.business.domaine.pca.PcaRequerantConjoint;
import ch.globaz.pegasus.business.domaine.pca.PcaSitutation;
import ch.globaz.pegasus.business.models.pcaccordee.NouvelleEtAnciennePcaCourante;
import ch.globaz.pegasus.business.models.pcaccordee.NouvelleEtAnciennePcaCouranteSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.businessimpl.services.adresse.AdresseLoader;
import ch.globaz.pegasus.businessimpl.services.adresse.TechnicalExceptionWithTiers;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.MembreFamilleLoader;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.PcaConverter;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.service.AdresseService;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

public class PcaLoader {
    private static final Logger LOG = LoggerFactory.getLogger(PcaLoader.class);

    public List<PcaForImpression> load(String idProcess) throws JadePersistenceException, JadeApplicationException {

        boolean onlyChangment = true;

        List<InfoAdaptation> infosAdaptation = findInfosAdaptation(idProcess);
        // infosAdaptation = infosAdaptation.subList(0, 1999);
        LOG.warn("Nb versionDroit found by entity :{} ", infosAdaptation.size());
        Set<String> idsVerionDroit = resolveIdsVersionDroit(infosAdaptation);

        List<NouvelleEtAnciennePcaCourante> nouvelleEtanciennesPcaCourantesDb = PersistenceUtil.searchByLot(
                idsVerionDroit, new PersistenceUtil.SearchLotExecutor<NouvelleEtAnciennePcaCourante>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        NouvelleEtAnciennePcaCouranteSearch anciennePcaCouranteSearch = new NouvelleEtAnciennePcaCouranteSearch();
                        anciennePcaCouranteSearch.setInIdsVersionDroit(ids);
                        anciennePcaCouranteSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        anciennePcaCouranteSearch.setWhereKey("forOldCurent");
                        anciennePcaCouranteSearch = (NouvelleEtAnciennePcaCouranteSearch) JadePersistenceManager
                                .search(anciennePcaCouranteSearch);
                        return anciennePcaCouranteSearch;
                    }
                }, 2000);

        LOG.warn("Nb pca found :{} ", nouvelleEtanciennesPcaCourantesDb.size());
        List<NouvelleEtAnciennePcaCourante> nouvelleEtanciennesPcaCourantes = nouvelleEtanciennesPcaCourantesDb;
        if (onlyChangment) {
            nouvelleEtanciennesPcaCourantes = filtreChangementMontantOuEtatPlanCalcul(nouvelleEtanciennesPcaCourantesDb);
        }

        LOG.warn("Nb pca filtred :{} ", nouvelleEtanciennesPcaCourantes.size());

        Set<String> idsPca = new HashSet<String>();
        for (NouvelleEtAnciennePcaCourante pcas : nouvelleEtanciennesPcaCourantes) {
            idsPca.add(pcas.getNouvellePca().getId());
        }

        MembreFamilleLoader membreFamilleLoader = new MembreFamilleLoader();
        Map<String, MembresFamilles> mapMembresFamilles = membreFamilleLoader
                .loadMembreFamilleComprisDansLeCalculAndGroupByIdPca(new ArrayList<String>(idsPca),
                        RoleMembreFamille.REQUERANT, RoleMembreFamille.CONJOINT);

        LOG.warn("Nb membreFamille found :{} ", mapMembresFamilles.size());
        Date dateAdaptation = findDateAdaptation(idProcess);
        List<Home> homes = loadHomeForTiers(resolveIdsDroit(nouvelleEtanciennesPcaCourantes), dateAdaptation);

        Set<String> idsTiersForHome = new HashSet<String>();
        for (Home home : homes) {
            String id = home.getIdTiersHome();
            if (id != null) {
                idsTiersForHome.add(id);
            }
        }

        LOG.warn("Nb home found :{} ", homes.size());

        Set<String> idsTiersBeneficiaire = resolveIdsTiersBeneficiaire(nouvelleEtanciennesPcaCourantes);
        List<String> listIdsTiers = new ArrayList<String>(idsTiersBeneficiaire);

        AdresseLoader adresseLoader = new AdresseLoader();
        Map<String, List<Adresse>> mapAdressesCourrier = adresseLoader
                .loadLastByIdsTiersAndGroupByIdTiers(listIdsTiers,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER);
        LOG.warn("Nb adresse courrier found :{} ", mapAdressesCourrier.size());

        Map<String, List<Adresse>> mapAdressesDomicile = adresseLoader
                .loadLastByIdsTiersAndGroupByIdTiers(listIdsTiers,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_DOMICILE);

        LOG.warn("Nb adresse domicile found :{} ", mapAdressesCourrier.size());

        adresseLoader = new AdresseLoader();

        Map<String, List<Adresse>> mapAdressesHomeCourrier = adresseLoader.loadLastByIdsTiersAndGroupByIdTiers(
                new ArrayList<String>(idsTiersForHome), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                AdresseService.CS_TYPE_COURRIER);
        LOG.warn("Nb adresse courrier home found :{} ", mapAdressesHomeCourrier.size());

        StopWatch watch = new StopWatch();

        watch.start();
        List<PcaForImpression> pcasForImpressions = buildListPcaForImpression(infosAdaptation,
                nouvelleEtanciennesPcaCourantes, mapMembresFamilles, mapAdressesCourrier, mapAdressesDomicile, homes,
                mapAdressesHomeCourrier);
        watch.stop();

        LOG.warn("Time convert :{} ms", watch.getTime());

        return pcasForImpressions;
    }

    private Date findDateAdaptation(String idProcess) {
        StringBuilder sql = new StringBuilder();
        sql.append("select PROVAL from schema.JAPRPROP where IDEXPR = " + idProcess
                + " and CSTYPE = 'PROVIDED' and PROKEY = 'DATE_ADAPTATION'");
        List<String> values = QueryExecutor.execute(sql.toString(), String.class);
        Date dateAdaptation = new Date(values.get(0));
        return dateAdaptation;
    }

    private List<NouvelleEtAnciennePcaCourante> filtreChangementMontantOuEtatPlanCalcul(
            List<NouvelleEtAnciennePcaCourante> nouvelleEtanciennesPcaCourantesDb) {
        // On filtre que sur le cas qui ont des changements (montant pca ou etat du plan de calcul)
        Predicate<NouvelleEtAnciennePcaCourante> predicate = new Predicate<NouvelleEtAnciennePcaCourante>() {
            @Override
            public boolean apply(NouvelleEtAnciennePcaCourante pcas) {
                SimplePlanDeCalcul ancienPlan = pcas.getAncienPlanDeCalcul();
                SimplePlanDeCalcul nouveauPlan = pcas.getNouveauPlanDeCalcul();
                if (ancienPlan.getEtatPC().equals(nouveauPlan.getEtatPC())) {
                    if (ancienPlan.getMontantPCMensuelle().equals(nouveauPlan.getMontantPCMensuelle())) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
        };
        List<NouvelleEtAnciennePcaCourante> nouvelleEtanciennesPcaCourantes = new ArrayList<NouvelleEtAnciennePcaCourante>(
                Collections2.filter(nouvelleEtanciennesPcaCourantesDb, predicate));
        return nouvelleEtanciennesPcaCourantes;
    }

    private List<PcaForImpression> buildListPcaForImpression(List<InfoAdaptation> infosAdaptation,
            List<NouvelleEtAnciennePcaCourante> nouvelleEtanciennesPcaCourantes,
            Map<String, MembresFamilles> mapMembresFamilles, Map<String, List<Adresse>> mapAdressesCourrier,
            Map<String, List<Adresse>> mapAdressesDomicile, List<Home> homes,
            Map<String, List<Adresse>> mapAdressesHomeCourrier) {
        List<PcaForImpression> pcasForImpressions = new ArrayList<PcaForImpression>();

        List<TechnicalExceptionWithTiers> errors = new ArrayList<TechnicalExceptionWithTiers>();
        Map<String, Collection<InfoAdaptation>> mapVersionInfosAdaptation = groupByVerionDroit(infosAdaptation);
        Map<String, Collection<Home>> mapTiersBeneficiaireHome = groupByTiersBeneficiaire(homes);
        PcaConverter pcaConverter = new PcaConverter();
        for (NouvelleEtAnciennePcaCourante pcaDb : nouvelleEtanciennesPcaCourantes) {
            try {

                Pca nouvellePca = pcaConverter.convert(pcaDb.getNouvellePca(), pcaDb.getNouveauPlanDeCalcul(), pcaDb
                        .getNouvellePrestationsAccordees().getSousCodePrestation());

                Pca anciennePca = pcaConverter.convert(pcaDb.getAnciennePca(), pcaDb.getAncienPlanDeCalcul(), null);

                MembresFamilles membresFamilles = mapMembresFamilles.get(pcaDb.getNouvellePca().getId());
                MembreFamille beneficiaire = null;
                if (membresFamilles != null) {
                    beneficiaire = membresFamilles.resolveByRole(nouvellePca.getRoleBeneficiaire());
                }
                if (beneficiaire == null) {
                    beneficiaire = new MembreFamille();
                    LOG.error("Pas de membre famille trouvé pour cette pca: ", nouvellePca.getId());
                }

                Collection<InfoAdaptation> infoAdaptations = mapVersionInfosAdaptation.get(pcaDb.getNouvellePca()
                        .getIdVersionDroit());
                InfoAdaptation infoAdaptation;
                if (infoAdaptations == null) {
                    LOG.error("rien trouvé ");
                    infoAdaptation = new InfoAdaptation();
                } else if (infoAdaptations.size() > 1) {
                    LOG.error("Trop d'InfoAdaptation trouvé");
                    infoAdaptation = infoAdaptations.iterator().next();
                } else {
                    infoAdaptation = infoAdaptations.iterator().next();
                }

                String idTiersBeneficiaire = pcaDb.getNouvellePrestationsAccordees().getIdTiersBeneficiaire();
                // pcaDb.getNouvellePrestationsAccordees().getIdTiersBeneficiaire()
                Adresse adresseCourrier = resovleAdresse(mapAdressesCourrier, idTiersBeneficiaire);

                Adresse adresseDomicile = resovleAdresse(mapAdressesDomicile, idTiersBeneficiaire);

                Collection<Home> listHome = mapTiersBeneficiaireHome.get(idTiersBeneficiaire);
                Home home = new Home();
                if (listHome != null) {
                    home = listHome.iterator().next();
                    if (listHome.size() > 1) {
                        LOG.error("Trop de home trouvé, idTiersBeneficiaire :{}", idTiersBeneficiaire);
                    }
                    if (nouvellePca.getGenre().isDomicile()) {
                        LOG.error("Home trouvé pour un cas à domicile !");
                    }
                } else if (nouvellePca.getGenre().isHome()) {
                    LOG.error("Home non trouvé pour une PCA de genre home, idTiersBeneficiaire :{}",
                            idTiersBeneficiaire);
                }

                Adresse adresseHome = resovleAdresse(mapAdressesHomeCourrier, home.getIdTiersHome());

                pcasForImpressions.add(new PcaForImpression(nouvellePca, anciennePca, adresseCourrier, adresseDomicile,
                        infoAdaptation, beneficiaire, idTiersBeneficiaire, home, adresseHome));
            } catch (Exception e) {
                errors.add(new TechnicalExceptionWithTiers("Impossible de convertir la pca id:" + pcaDb.getId() + "",
                        pcaDb.getNouvellePrestationsAccordees().getIdTiersBeneficiaire(), e));
            }
        }

        for (TechnicalExceptionWithTiers e : errors) {
            e.printStackTrace();
        }
        return pcasForImpressions;
    }

    private List<Home> loadHomeForTiers(Collection<String> idsDroit, Date dateAdaptation) {
        // Il serait mieux de se basé sur l'idversion droit !
        String sql = "select schema.PCHOME.BLITIE as idTiers, schema.PCHOME.BLLID as numero, schema.PCHOME.BLLBAT as libelle, "
                + "schema.PCHOME.BLITIE as idTiers, schema.PCHOME.BLITIE as idTiersHome, schema.SFMBRFAM.WGITIE as idTiersBeneficiaire "
                + "from schema.PCTAJOHO inner join schema.PCDOFINH on schema.PCDOFINH.BGIDFH = schema.PCTAJOHO.CSIDDF "
                + "inner join schema.PCHOME  on schema.PCHOME.BLIHOM = schema.PCTAJOHO.CSIDHO "
                + "inner join schema.PCDRMBRFA on schema.PCDRMBRFA.BEIDMF = schema.PCDOFINH.BGIDMF "
                + "inner join schema.SFMBRFAM  on schema.SFMBRFAM.WGIMEF = schema.PCDRMBRFA.BEIMEF "
                + "inner join schema.PCVERDRO on schema.PCVERDRO.BDIVDR = schema.PCDOFINH.BGIVDR "
                + "inner join schema.PCDROIT on schema.PCDROIT.BCIDRO = schema.PCVERDRO.BDIDRO "
                + "inner join schema.PCDEMPC on schema.PCDEMPC.BBIDPC = schema.PCDROIT.BCIDPC "
                + "where (schema.PCDOFINH.BGDDFI is null or schema.PCDOFINH.BGDDFI = 0 or schema.PCDOFINH.BGDDFI = "
                + dateAdaptation.getValueMonth()
                + ") and schema.PCDOFINH.BGDDDE <= "
                + dateAdaptation.getValueMonth()
                + " and schema.PCDOFINH.BGBSUP = 2 "
                + " and schema.PCDOFINH.BGIVDR =  (select max(versionDroitMax.BDIVDR) from schema.PCVERDRO versionDroitMax "
                + "inner join  schema.PCDOFINH df on df.BGIVDR = versionDroitMax.BDIVDR "
                + "where df.BGIENT = schema.PCDOFINH.BGIENT and ( df.BGDDFI is null or df.BGDDFI = 0 or df.BGDDFI <= "
                + dateAdaptation.getValueMonth()
                + ") and df.BGDDDE <= "
                + dateAdaptation.getValueMonth()
                + " and versionDroitMax.BDTETA  in (64003004, 64003005) ) "
                + "and schema.PCDROIT.BCIDRO in ("
                + Joiner.on(",").join(idsDroit) + ") order by schema.SFMBRFAM.WGITIE, schema.PCDOFINH.BGDDDE desc ";

        List<Home> homes = new ArrayList<Home>();
        if (!idsDroit.isEmpty()) {
            homes = QueryExecutor.execute(sql.toString(), Home.class);
        }
        return homes;
    }

    private List<InfoAdaptation> findInfosAdaptation(String idProcess) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SCHEMA.PCPCACC.CUIVDR as idVersionDroit, SCHEMA.PCPLCAL.CVIPLC as idPlanCalcul, ");
        sql.append("SCHEMA.PCPCACC.CUIPCA as idPca, montantAncien.PROVAL as montantAnciennePca ");
        sql.append("FROM SCHEMA.JAPREXPR ");
        sql.append("inner join SCHEMA.JAPRENTI on SCHEMA.JAPRENTI.IDEXPR = SCHEMA.JAPREXPR.IDEXPR ");
        sql.append("inner join SCHEMA.JAPRPROP on SCHEMA.JAPRPROP.IDENTI = SCHEMA.JAPRENTI.IDENTI and SCHEMA.JAPRPROP.PROKEY = 'ID_PLAN_CALCUL_RETENU' ");
        sql.append("inner join SCHEMA.JAPRPROP as montantAncien on SCHEMA.JAPRPROP.IDENTI = montantAncien.IDENTI and montantAncien.PROKEY = 'OLD_PC' ");
        sql.append("inner join SCHEMA.JAPRSTEP on SCHEMA.JAPRSTEP.IDSETP = SCHEMA.JAPRENTI.IDCUST ");
        sql.append("inner join SCHEMA.PCPLCAL on SCHEMA.PCPLCAL.CVIPLC = CAST(TRIM(SCHEMA.JAPRPROP.PROVAL) AS DECIMAL(15,0)) ");
        sql.append("inner join SCHEMA.PCPCACC on SCHEMA.PCPCACC.CUIPCA = SCHEMA.PCPLCAL.CVIPCA ");
        sql.append("where (SCHEMA.JAPRSTEP.ORDRE > (select ORDRE from SCHEMA.JAPRSTEP validation where validation.idexpr = ");
        sql.append(idProcess);
        sql.append(" and validation.keystp = '6') ");
        sql.append("or (SCHEMA.JAPRSTEP.ORDRE = (select ORDRE from SCHEMA.JAPRSTEP validation where validation.idexpr = ");
        sql.append(idProcess + " and validation.keystp = '6') and SCHEMA.JAPRENTI.CSETAT != 'ERROR') ) ");
        sql.append(" and SCHEMA.JAPREXPR.IDEXPR = " + idProcess);
        List<InfoAdaptation> infosAdaptation = QueryExecutor.execute(sql.toString(), InfoAdaptation.class);
        return infosAdaptation;
    }

    private Set<String> resolveIdsVersionDroit(List<InfoAdaptation> infosAdaptation) {
        Set<String> idsVerionDroit = new HashSet<String>();
        for (InfoAdaptation infoAdaptation : infosAdaptation) {
            idsVerionDroit.add(infoAdaptation.getIdVersionDroit());
        }
        return idsVerionDroit;
    }

    private Map<String, Collection<InfoAdaptation>> groupByVerionDroit(List<InfoAdaptation> infosAdaptation) {
        ImmutableListMultimap<String, InfoAdaptation> infosAdaptationMultiMap = Multimaps.index(infosAdaptation,
                new Function<InfoAdaptation, String>() {
                    @Override
                    public String apply(InfoAdaptation infoAdaptation) {
                        return infoAdaptation.getIdVersionDroit();
                    }
                });

        Map<String, Collection<InfoAdaptation>> mapVersionInfosAdaptation = infosAdaptationMultiMap.asMap();
        return mapVersionInfosAdaptation;
    }

    private Map<String, Collection<Home>> groupByTiersBeneficiaire(List<Home> homes) {
        ImmutableListMultimap<String, Home> multiMap = Multimaps.index(homes, new Function<Home, String>() {
            @Override
            public String apply(Home home) {
                return home.getIdTiersBeneficiaire();
            }
        });
        Map<String, Collection<Home>> map = multiMap.asMap();
        return map;
    }

    private Adresse resovleAdresse(Map<String, List<Adresse>> mapAdresses, String idTiers) {
        List<Adresse> adresses = mapAdresses.get(idTiers);
        Adresse adresse;
        if (adresses == null) {
            adresse = new Adresse();
        } else {
            adresse = adresses.get(0);
        }
        return adresse;
    }

    String resolveIdTierToUsedForAdresse(PcaRequerantConjoint pcas) {
        String id = null;
        PcaSitutation pcaCas = pcas.resolveCasPca();
        if (pcaCas.isCoupleSepareRequerantEnHome()) {
            id = String.valueOf(pcas.getConjoint().getBeneficiaire().getId());
        } else {
            id = String.valueOf(pcas.getRequerant().getBeneficiaire().getId());
        }
        return id;
    }

    private Set<String> resolveIdsTiersBeneficiaire(List<NouvelleEtAnciennePcaCourante> pcasDb) {
        Set<String> idsTiers = new HashSet<String>();
        for (NouvelleEtAnciennePcaCourante pcaDb : pcasDb) {
            String id = pcaDb.getNouvellePrestationsAccordees().getIdTiersBeneficiaire();
            if (id != null) {
                idsTiers.add(id);
            }
        }
        return idsTiers;
    }

    private Set<String> resolveIdsDroit(List<NouvelleEtAnciennePcaCourante> pcasDb) {
        Set<String> idsTiers = new HashSet<String>();
        for (NouvelleEtAnciennePcaCourante pcaDb : pcasDb) {
            if (IPCPCAccordee.CS_GENRE_PC_HOME.equals(pcaDb.getNouvellePca().getCsGenrePC())) {
                String id = pcaDb.getIdDroit();
                if (id != null) {
                    idsTiers.add(id);
                }
            }
        }
        return idsTiers;
    }
}
