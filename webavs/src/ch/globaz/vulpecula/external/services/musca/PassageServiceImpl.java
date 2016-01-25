package ch.globaz.vulpecula.external.services.musca;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.models.PassageModuleComplexModel;
import ch.globaz.musca.business.models.PassageModuleComplexSearchModel;
import ch.globaz.musca.business.models.PlanFacturationPassageComplexModel;
import ch.globaz.musca.business.models.PlanFacturationPassageSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.repositoriesjade.musca.converters.PassageConverter;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.util.DBUtil;

public class PassageServiceImpl implements PassageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PassageService.class);

    private static final String NATIVE_QUERY_PASSAGE_WITH_PAGINATION = "SELECT pa.IDPASSAGE, pa.LIBELLEPASSAGE, pa.STATUS FROM SCHEMA.FAPASSP pa JOIN ( SELECT DISTINCT ID_FAPASSP AS ID FROM ( SELECT DISTINCT aj.ID_FAPASSP FROM SCHEMA.PT_ABSENCES_JUSTIFIEES aj JOIN SCHEMA.PT_POSTES_TRAVAILS po ON po.ID=aj.ID_PT_POSTES_TRAVAILS WHERE po.ID_AFAFFIP=:idAffiliation UNION ALL SELECT DISTINCT cp.ID_FAPASSP FROM SCHEMA.PT_CONGES_PAYES cp JOIN SCHEMA.PT_POSTES_TRAVAILS po ON po.ID=cp.ID_PT_POSTES_TRAVAILS WHERE po.ID_AFAFFIP=:idAffiliation UNION ALL SELECT DISTINCT sm.ID_FAPASSP FROM SCHEMA.PT_SERVICES_MILITAIRES sm JOIN SCHEMA.PT_POSTES_TRAVAILS po ON po.ID=sm.ID_PT_POSTES_TRAVAILS WHERE po.ID_AFAFFIP=:idAffiliation ) ) idPassages ON idPassages.id=pa.IDPASSAGE ORDER BY ID DESC";

    @Override
    public Passage findById(String idPassage) {
        Passage passage = null;
        PlanFacturationPassageSearchComplexModel searchModel = new PlanFacturationPassageSearchComplexModel();
        searchModel.setForIdPassage(idPassage);
        try {
            JadePersistenceManager.search(searchModel);
            if (searchModel.getSearchResults().length > 0) {
                PlanFacturationPassageComplexModel planFacturationPassageComplexModel = (PlanFacturationPassageComplexModel) searchModel
                        .getSearchResults()[0];
                passage = PassageConverter.getInstance().convertToDomain(
                        planFacturationPassageComplexModel.getPassageModel());
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }

        return passage;
    }

    @Override
    public Passage findPassageActif(String typeModule) throws PassageSearchException {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        Passage passage = new Passage();

        PlanFacturationPassageSearchComplexModel searchModel = new PlanFacturationPassageSearchComplexModel();
        searchModel.setForTypeFacturation(typeModule);
        searchModel.setForEtat(FAPassage.CS_ETAT_OUVERT);
        try {
            List<Passage> passages = findPassagesActifs(typeModule);

            int nbResults = passages.size();
            if (nbResults == 0) {
                throw new PassageSearchException(session.getLabel("JSP_PAS_DE_MODULE_FACTURATION_OUVERT") + " \""
                        + session.getCodeLibelle(typeModule) + "\"");
            }
            if (nbResults >= 2) {
                throw new PassageSearchException(session.getLabel("JSP_PLUSIEURS_MODULES_FACTURATIONS_OUVERTS") + " \""
                        + session.getCodeLibelle(typeModule) + "\"");
            }
            passage = passages.get(0);

        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return passage;
    }

    @Override
    public Passage findPassageActif(TypePrestation typePrestation) throws PassageSearchException {
        switch (typePrestation) {
            case ABSENCES_JUSTIFIEES:
                return findPassageActif(FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES);
            case CONGES_PAYES:
                return findPassageActif(FAModuleFacturation.CS_MODULE_CONGE_PAYE);
            case SERVICES_MILITAIRE:
                return findPassageActif(FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE);
            default:
                throw new IllegalStateException();
        }
    }

    public boolean hasPassageActif(String typeModule, String idPassage) {
        try {
            return findPassagesActifs(typeModule, idPassage).size() > 0;
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        return true;
    }

    private List<Passage> findPassagesActifs(String typeModule) throws JadePersistenceException {
        return findPassagesActifs(typeModule, null);
    }

    private List<Passage> findPassagesActifs(String typeModule, String idPassage) throws JadePersistenceException {
        List<Passage> passages = new ArrayList<Passage>();

        PlanFacturationPassageSearchComplexModel searchModel = new PlanFacturationPassageSearchComplexModel();
        searchModel.setForTypeFacturation(typeModule);
        searchModel.setForEtat(FAPassage.CS_ETAT_OUVERT);
        if (idPassage != null && idPassage.length() != 0) {
            searchModel.setForNotIdPassage(idPassage);
        }
        JadePersistenceManager.search(searchModel);

        for (int i = 0; i < searchModel.getSearchResults().length; i++) {
            PlanFacturationPassageComplexModel planFacturationPassageComplexModel = (PlanFacturationPassageComplexModel) searchModel
                    .getSearchResults()[i];
            PassageModel passageModel = planFacturationPassageComplexModel.getPassageModel();
            passages.add(PassageConverter.getInstance().convertToDomain(passageModel));
        }
        return passages;
    }

    @Override
    public FAPassage createPassageFacturation(String idTypeModule, String libelle, Date dateOuverture, BSession session) {
        String idPlanFacturation = getIdPlanFacturation(idTypeModule);
        return createPassage(idPlanFacturation, libelle, dateOuverture, session);
    }

    @Override
    public boolean createPassageFacturationIfNotExist(BSession session, String idTypeModule, String libelle,
            String idPassage, Date dateOuverture) {
        String idPlanFacturation = getIdPlanFacturation(idTypeModule);
        if (!hasPassageActif(idTypeModule, idPassage)) {
            createPassage(idPlanFacturation, libelle, dateOuverture, session);
            return true;
        }
        return false;
    }

    private String getIdPlanFacturation(String idTypeModule) {
        PassageModuleComplexSearchModel searchModel = new PassageModuleComplexSearchModel();
        searchModel.setInPassageTypeModule(Arrays.asList(idTypeModule));
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }

        int results = searchModel.getSearchResults().length;
        if (results < 1) {
            throw new IllegalStateException("Il n'existe pas de plan de facturation contenant ce module");
        }

        // On récupère les plans de facturation différents
        Set<String> plans = new HashSet<String>();
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            PassageModuleComplexModel passageModuleComplexModel = (PassageModuleComplexModel) model;
            plans.add(passageModuleComplexModel.getIdPlan());
        }

        if (plans.size() > 1) {
            throw new IllegalStateException(
                    "Impossible de déterminer quel passage ouvrir car il existe plusieurs plan de facturation contenant le module "
                            + idTypeModule);
        }
        return plans.iterator().next();
    }

    private FAPassage createPassage(String idPlanFacturation, String libelle, Date dateOuverture, BSession session) {
        FAPassage nouveauPassage = new FAPassage();
        nouveauPassage.setSession(session);
        nouveauPassage.setIdTypeFacturation(FAPassage.CS_TYPE_EXTERNE);
        nouveauPassage.setIdPlanFacturation(idPlanFacturation);
        nouveauPassage.setDateFacturation(dateOuverture.getSwissValue());
        nouveauPassage.setLibelle(libelle);
        try {
            nouveauPassage.add();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
        return nouveauPassage;
    }

    /**
     * <pre>
     *         SELECT
     *         pa.IDPASSAGE, pa.LIBELLEPASSAGE, pa.STATUS
     *         FROM WEBAVSS.FAPASSP pa
     *         JOIN
     *         (
     *            SELECT
     *            DISTINCT IDPASSAGE AS ID
     *            FROM
     *            (
     *               SELECT
     *               DISTINCT p.IDPASSAGE
     *               FROM WEBAVSS.FAPASSP p
     *               JOIN WEBAVSS.PT_ABSENCES_JUSTIFIEES aj ON p.IDPASSAGE=aj.ID_FAPASSP
     *               JOIN WEBAVSS.PT_POSTES_TRAVAILS po ON po.ID=aj.ID_PT_POSTES_TRAVAILS
     *               WHERE po.ID_AFAFFIP=100035
     *               GROUP BY p.IDPASSAGE
     *               UNION
     *               ALL
     *               SELECT
     *               DISTINCT p.IDPASSAGE
     *               FROM WEBAVSS.FAPASSP p
     *               JOIN WEBAVSS.PT_CONGES_PAYES aj ON p.IDPASSAGE=aj.ID_FAPASSP
     *               JOIN WEBAVSS.PT_POSTES_TRAVAILS po ON po.ID=aj.ID_PT_POSTES_TRAVAILS
     *               WHERE po.ID_AFAFFIP=100035
     *               GROUP BY p.IDPASSAGE
     *               UNION
     *               ALL
     *               SELECT
     *               DISTINCT p.IDPASSAGE
     *               FROM WEBAVSS.FAPASSP p
     *               JOIN WEBAVSS.PT_SERVICES_MILITAIRES aj ON p.IDPASSAGE=aj.ID_FAPASSP
     *               JOIN WEBAVSS.PT_POSTES_TRAVAILS po ON po.ID=aj.ID_PT_POSTES_TRAVAILS
     *               WHERE po.ID_AFAFFIP=100035
     *               GROUP BY p.IDPASSAGE
     *            )
     *         )
     *         idPassages ON idPassages.id=pa.IDPASSAGE
     * 
     * </pre>
     */
    @Override
    public List<Passage> findByIdEmployeur(String idEmployeur, int offset, int size) {
        final String ID_PASSAGE = "IDPASSAGE";
        final String LIBELLEPASSAGE = "LIBELLEPASSAGE";
        final String STATUS = "STATUS";

        List<Passage> passages = new ArrayList<Passage>();

        String query = NATIVE_QUERY_PASSAGE_WITH_PAGINATION;
        query = query.replace("SCHEMA", JadePersistenceUtil.getDbSchema());

        Map<String, String> params = new HashMap<String, String>();
        params.put("idAffiliation", idEmployeur);

        try {
            ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, params, offset, size, getClass());
            for (Map<String, Object> row : result) {
                Passage passage = new Passage();
                passage.setId(String.valueOf(row.get(ID_PASSAGE)));
                passage.setLibelle(String.valueOf(row.get(LIBELLEPASSAGE)));
                passage.setStatus(String.valueOf(row.get(STATUS)));
                passages.add(passage);
            }
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        return passages;
    }

    @Override
    public int countByIdEmployeur(String idEmployeur) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idAffiliation", idEmployeur);

        try {
            return DBUtil.count(NATIVE_QUERY_PASSAGE_WITH_PAGINATION, params, this.getClass());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException("La requête n'a pas pu se terminer");
        }
    }

    @Override
    public Passage findOrCreatePassageTO() {
        Passage passage = findPassageTO();
        if (passage == null) {
            createPassageTO();
            passage = findPassageTO();
        }
        return passage;
    }

    private Passage findPassageTO() {
        PlanFacturationPassageSearchComplexModel searchModel = new PlanFacturationPassageSearchComplexModel();
        searchModel.setForTypeFacturation(FAModuleFacturation.CS_MODULE_TAXATION_OFFICE);
        searchModel.setForEtat(FAPassage.CS_ETAT_OUVERT);
        List<PlanFacturationPassageComplexModel> passages = RepositoryJade.searchForAndFetch(searchModel);
        if (passages.size() > 0) {
            PassageModel passage = passages.get(0).getPassageModel();
            return PassageConverter.getInstance().convertToDomain(passage);
        }
        return null;
    }

    /**
     * Création d'un passage de facturation pour les taxations d'office.
     * 
     * @return L'id du nouveau passage
     */
    private String createPassageTO() {
        return createPassageFacturation(FAModuleFacturation.CS_MODULE_TAXATION_OFFICE,
                "Taxation office " + new Date().getSwissValue(), Date.now(), BSessionUtil.getSessionFromThreadContext())
                .getId();
    }

    @Override
    public boolean createPassageForNextWeekIfNotExist(BSession session, String idTypeModule, String libelle,
            String idPassage) {
        Date dateOuverture = Date.now().addDays(7);
        return createPassageFacturationIfNotExist(session, idTypeModule, libelle + dateOuverture.getSwissValue(),
                idPassage, dateOuverture);
    }
}
