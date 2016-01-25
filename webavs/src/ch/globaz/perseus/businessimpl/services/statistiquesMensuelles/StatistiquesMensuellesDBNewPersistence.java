package ch.globaz.perseus.businessimpl.services.statistiquesMensuelles;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.statistique.StatistiquesMensuellesDemPcfDecException;
import ch.globaz.perseus.business.exceptions.models.statistique.StatistiquesMensuellesEnfantsDemandeException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.demande.SimpleDemandeSearchModel;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesDemPcfDec;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesDemPcfDecSearchModel;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesEnfantDemande;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesEnfantDemandeSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.statsmensuelles.Administration;
import ch.globaz.perseus.business.statsmensuelles.TypeStat;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.businessimpl.utils.PerseusPersistenceUtil;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author RCO Afin d'isoler les appels à la base de données dans une classe réalisant uniquement cette responsabilité,
 *         j'ai créée cette classe. Elle contient des méthodes statiques qui sont spécifiques aux statistiques
 *         mensuelles.
 */
public class StatistiquesMensuellesDBNewPersistence {

    /**
     * Retourne la décision précédente en fonction de l'id de la demande
     * 
     * @param demande
     *            {@link SimpleDemande}
     * @return {@link Decision}
     */
    public static Decision getDecisionPrecedente(SimpleDemande demande) {
        Demande demandePrecedante = null;
        Decision decisionPrecedante = null;
        try {

            // Retrouver la date à laquelle on va rechercher une demande active
            String dateFin = "";
            if (JadeStringUtil.isEmpty(demande.getDateFin())) {
                // Cas où la demande sera active après
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            } else if (!JadeDateUtil.isDateMonthYearAfter(demande.getDateFin().substring(3), PerseusServiceLocator
                    .getPmtMensuelService().getDateDernierPmt())) {
                // Cas où la demande est purement retro active
                dateFin = demande.getDateFin();
            } else {
                // cas normalement impossible puisque ca voudrait dire que la demande se ferme dans le future et se
                // n'est pas possible
                // mais on prend quand même le dernier paiement mensuel
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            }

            DemandeSearchModel ds = new DemandeSearchModel();
            ds.setForIdDossier(demande.getIdDossier());
            ds.setForNotIdDemande(demande.getId());
            ds.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            ds.setForDateValable(dateFin);
            ds.setOrderBy(DemandeSearchModel.ORDER_BY_DATETIME_DECISION_DESC);
            ds.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            ds = PerseusServiceLocator.getDemandeService().search(ds);
            // On peut considérer que la première est la dernière décision prise
            if (ds.getSize() > 0) {
                demandePrecedante = (Demande) ds.getSearchResults()[0];
            } else {

                ds.setForIdDossier(demande.getIdDossier());
                ds.setForNotIdDemande(demande.getId());
                ds.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
                ds.setForDateValable(JadeDateUtil.addDays(demande.getDateDebut(), -1));
                ds.setOrderBy(DemandeSearchModel.ORDER_BY_DATETIME_DECISION_DESC);
                ds = PerseusServiceLocator.getDemandeService().search(ds);

                if (ds.getSize() > 0) {
                    demandePrecedante = (Demande) ds.getSearchResults()[0];
                }
            }

            if (null != demandePrecedante) {
                DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
                decisionSearchModel.setForIdDemande(demandePrecedante.getSimpleDemande().getIdDemande());
                decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);

                for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
                    decisionPrecedante = (Decision) model;
                    break;
                }
            }

            return decisionPrecedante;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, List<StatistiquesMensuellesEnfantDemande>> getIdDemandeDateNaissanceEnfant(
            StatistiquesMensuellesEnfantDemandeSearchModel smedsm, Map<String, String> mapIdDemandeMontant,
            List<String> idDemandePourEnfant) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        smedsm.setForIdDemande(idDemandePourEnfant);
        smedsm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        try {
            smedsm = PerseusServiceLocator.getStatistiquesMensuellesEnfantDemandeService().search(smedsm);
        } catch (StatistiquesMensuellesEnfantsDemandeException e) {
            e.printStackTrace();
        }

        List<StatistiquesMensuellesEnfantDemande> listStatEnfants = PerseusPersistenceUtil.typeSearch(smedsm,
                StatistiquesMensuellesEnfantDemande.class);

        // on fait un group by par idDossier
        Map<String, List<StatistiquesMensuellesEnfantDemande>> idDemandeDateNaissanceEnfant = JadeListUtil.groupBy(
                listStatEnfants, new Key<StatistiquesMensuellesEnfantDemande>() {
                    @Override
                    public String exec(StatistiquesMensuellesEnfantDemande modele) {
                        return modele.getDemande().getIdDossier(); // On définit sur quoi on fait le GROUPBY
                    }
                });

        for (List<StatistiquesMensuellesEnfantDemande> listEnfant : idDemandeDateNaissanceEnfant.values()) {
            for (StatistiquesMensuellesEnfantDemande enfant : listEnfant) {
                enfant.setMontant(mapIdDemandeMontant.get(enfant.getDemande().getIdDossier()));
            }
        }

        return idDemandeDateNaissanceEnfant;
    }

    /**
     * Méthode retournant une liste de {@link StatistiquesMensuellesDemPcfDec}
     * 
     * @param moisDebut
     * @param moisFin
     * @return {@List}<{@link StatistiquesMensuellesDemPcfDec}>
     * @throws StatistiquesMensuellesDemPcfDecException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static List<StatistiquesMensuellesDemPcfDec> getListeStatistiquesMensuellesDemPcfDec(String moisDebut,
            String moisFin) throws StatistiquesMensuellesDemPcfDecException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Charger les données
        StatistiquesMensuellesDemPcfDecSearchModel statstMensDemPcfDecSearchmodel = new StatistiquesMensuellesDemPcfDecSearchModel();
        String dateDebutFormatte = JadeDateUtil.getFirstDateOfMonth(moisDebut);
        String dateFinFormatte = JadeDateUtil.getLastDateOfMonth(moisFin);

        statstMensDemPcfDecSearchmodel.setForDateFin(dateFinFormatte);
        statstMensDemPcfDecSearchmodel.setForDateDebut(dateDebutFormatte);
        statstMensDemPcfDecSearchmodel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        statstMensDemPcfDecSearchmodel = PerseusServiceLocator.getStatistiquesMensuellesDemPcfDecService().search(
                statstMensDemPcfDecSearchmodel);

        // conversion en list du modèle de recherche
        List<StatistiquesMensuellesDemPcfDec> listeStatsMensuelles = PerseusPersistenceUtil.typeSearch(
                statstMensDemPcfDecSearchmodel, StatistiquesMensuellesDemPcfDec.class);
        return listeStatsMensuelles;

    }

    /**
     * 
     * @param mapIdDemandePourEnfants
     * @return {@link Map}<{@link String}, {@link List}<{@link StatistiquesMensuellesEnfantDemande}>> En key on a des
     *         idDossier et en value une liste contenant des dates de naissance d'enfants (déjà trié avec le plus jeune
     *         d'abord)
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static Map<String, List<StatistiquesMensuellesEnfantDemande>> getListPlusJeuneEnfant(
            Map<String, Map<TypeStat, List<StatistiquesMensuellesDemPcfDec>>> mapIdDemandePourEnfants, TypeStat typeStat)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Charger les données
        StatistiquesMensuellesEnfantDemandeSearchModel smedsm = new StatistiquesMensuellesEnfantDemandeSearchModel();

        Map<String, String> mapIdDossierMontant = new HashMap<String, String>();
        List<String> idDemandePourEnfant = new ArrayList<String>();

        for (Map<TypeStat, List<StatistiquesMensuellesDemPcfDec>> mapListDeStatsEnfants : mapIdDemandePourEnfants
                .values()) {

            if (mapListDeStatsEnfants.containsKey(typeStat)) {
                for (StatistiquesMensuellesDemPcfDec idAChercher : mapListDeStatsEnfants.get(typeStat)) {
                    mapIdDossierMontant.put(idAChercher.getDemande().getIdDossier(), idAChercher.getMontant());
                    idDemandePourEnfant.add(idAChercher.getId());
                }
            }
        }

        /*
         * Si notre liste d'id demande est plus grande on a des requêtes trop longues. Après test, j'ai remarqué que
         * 2000 id passent donc c'est la limite que j'ai choisit
         */
        int tailleMax = 2000;
        if (idDemandePourEnfant.size() > tailleMax) {
            List<String> listReduiteIdDemandePourEnfant = new ArrayList<String>();

            Map<String, List<StatistiquesMensuellesEnfantDemande>> idDemandedateNaissanceEnfant = new HashMap<String, List<StatistiquesMensuellesEnfantDemande>>();

            int tailleArrivee = tailleMax;
            int tailleDepart = 0;
            boolean resteIdAVerifier = true;

            do {
                listReduiteIdDemandePourEnfant = new ArrayList<String>();
                for (int i = tailleDepart; i < tailleArrivee; i++) {
                    listReduiteIdDemandePourEnfant.add(idDemandePourEnfant.get(i));
                }

                Map<String, List<StatistiquesMensuellesEnfantDemande>> retourList = StatistiquesMensuellesDBNewPersistence
                        .getIdDemandeDateNaissanceEnfant(smedsm, mapIdDossierMontant, listReduiteIdDemandePourEnfant);

                for (String key : retourList.keySet()) {
                    idDemandedateNaissanceEnfant.put(key, retourList.get(key));
                }

                if (tailleArrivee >= idDemandePourEnfant.size()) {
                    resteIdAVerifier = false;
                } else {
                    tailleDepart = tailleArrivee;
                    if (idDemandePourEnfant.size() < tailleArrivee + tailleMax) {
                        tailleArrivee = idDemandePourEnfant.size();
                    } else {
                        tailleArrivee += tailleMax;
                    }
                }
            } while (resteIdAVerifier);
            return idDemandedateNaissanceEnfant;
        } else {
            return StatistiquesMensuellesDBNewPersistence.getIdDemandeDateNaissanceEnfant(smedsm, mapIdDossierMontant,
                    idDemandePourEnfant);
        }
    }

    /**
     * Méthode retournant un map contenant :
     * <ul>
     * <li>en key : un id administration</li>
     * <li>en value : une {@link List} d'{@link Administration}</li>
     * </ul>
     * 
     * @return TreeMap<String, Administration>
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static TreeMap<String, Administration> getMapAdministrations() throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {
        AdministrationSearchComplexModel administrationSearchComplexModel = new AdministrationSearchComplexModel();
        administrationSearchComplexModel.setForCanton(IPFConstantes.CS_CANTON_VAUD);
        administrationSearchComplexModel.setForGenreAdministration(IPFConstantes.CS_AGENCE_COMMUNALE);
        administrationSearchComplexModel.setOrderKey("des2des1");
        administrationSearchComplexModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        administrationSearchComplexModel = TIBusinessServiceLocator.getAdministrationService().find(
                administrationSearchComplexModel);

        TreeMap<String, Administration> administrations = new TreeMap<String, Administration>();

        for (JadeAbstractModel administrationAbstractModel : administrationSearchComplexModel.getSearchResults()) {
            AdministrationComplexModel adm = (AdministrationComplexModel) administrationAbstractModel;
            administrations.put(adm.getId(), new Administration(adm.getId(), adm.getTiers().getDesignation2()));
        }

        return administrations;
    }

    /**
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     * 
     */
    public static Map<String, Map<TypeStat, Boolean>> getMapDossierRI() throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleDemandeSearchModel demandeSearchModel = new SimpleDemandeSearchModel();
        demandeSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        demandeSearchModel.setWhereKey(SimpleDemandeSearchModel.WITH_OUT_WHERE);
        demandeSearchModel.setOrderKey(SimpleDemandeSearchModel.ORDER_BY_DATE_DEBUT_DATE_VALIDATION);

        demandeSearchModel = PerseusImplServiceLocator.getSimpleDemandeService().search(demandeSearchModel);

        Map<String, Map<TypeStat, Boolean>> mapDossierRI = new HashMap<String, Map<TypeStat, Boolean>>();

        for (JadeAbstractModel abstractDemande : demandeSearchModel.getSearchResults()) {
            SimpleDemande demande = (SimpleDemande) abstractDemande;

            if (CSEtatDemande.VALIDE.getCodeSystem().equals(demande.getCsEtatDemande())) {
                if (mapDossierRI.containsKey(demande.getIdDossier())) {
                    if (!mapDossierRI.get(demande.getIdDossier()).containsKey(TypeStat.AUTRE)) {
                        mapDossierRI.get(demande.getIdDossier()).put(TypeStat.AUTRE, demande.getFromRI());
                    }
                } else {
                    Map<TypeStat, Boolean> mapTemporaire = new HashMap<TypeStat, Boolean>();
                    mapTemporaire.put(TypeStat.AUTRE, demande.getFromRI());
                    mapDossierRI.put(demande.getIdDossier(), mapTemporaire);
                }
            }

            if (mapDossierRI.containsKey(demande.getIdDossier())) {
                if (!mapDossierRI.get(demande.getIdDossier()).containsKey(TypeStat.DEMANDE)) {
                    mapDossierRI.get(demande.getIdDossier()).put(TypeStat.DEMANDE, demande.getFromRI());
                }
            } else {
                Map<TypeStat, Boolean> mapTemporaire = new HashMap<TypeStat, Boolean>();
                mapTemporaire.put(TypeStat.DEMANDE, demande.getFromRI());
                mapDossierRI.put(demande.getIdDossier(), mapTemporaire);
            }
        }

        return mapDossierRI;
    }
}
