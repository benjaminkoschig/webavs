package ch.globaz.vulpecula.repositoriesjade.postetravail;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSearchComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSearchComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.TauxOccupationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.TauxOccupationSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.QueryParameters;
import ch.globaz.vulpecula.domain.repositories.postetravail.AdhesionCotisationPosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.repositoriesjade.QueryParametersRegistry;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.AdhesionCotisationPosteTravailConverter;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.OccupationConverter;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.PosteTravailConverter;
import ch.globaz.vulpecula.util.DBUtil;

/***
 * Implémentation Jade de {@link PosteTravailRepository}
 * 
 */
public class PosteTravailRepositoryJade extends
        RepositoryJade<PosteTravail, PosteTravailComplexModel, PosteTravailSimpleModel> implements
        PosteTravailRepository {
    private static final String NATIVE_QUERY_POSTE_TRAVAIL_ORDER_BY_NOM = "SELECT * FROM ( SELECT * FROM (( SELECT po.ID, po.ID_PT_TRAVAILLEURS, tie.HTLDE1, tie.HTLDE2, tie.HTLDU1, tie.HTLDU2, per.HPDNAI, avs.HXNAVS, po.CS_QUALIFICATION, po.CS_GENRE_SALAIRE, po.DATE_DEBUT_ACTIVITE, po.DATE_FIN_ACTIVITE FROM WEBAVSS.PT_POSTES_TRAVAILS po INNER JOIN WEBAVSS.PT_TRAVAILLEURS trav ON trav.ID=po.ID_PT_TRAVAILLEURS INNER JOIN WEBAVSS.TIPAVSP avs ON trav.ID_TITIERP=avs.HTITIE INNER JOIN WEBAVSS.TIPERSP per ON per.HTITIE=avs.HTITIE INNER JOIN WEBAVSS.TITIERP tie ON tie.HTITIE=per.HTITIE WHERE ID_AFAFFIP=:idAffilie AND DATE_FIN_ACTIVITE<=:date AND DATE_FIN_ACTIVITE<>0 ORDER BY DATE_FIN_ACTIVITE DESC ,HTLDE1, HTLDE2 )) UNION ALL ( SELECT * FROM ( ( SELECT po.ID, po.ID_PT_TRAVAILLEURS, tie.HTLDE1, tie.HTLDE2, tie.HTLDU1, tie.HTLDU2, per.HPDNAI, avs.HXNAVS, po.CS_QUALIFICATION, po.CS_GENRE_SALAIRE, po.DATE_DEBUT_ACTIVITE, po.DATE_FIN_ACTIVITE FROM WEBAVSS.PT_POSTES_TRAVAILS po INNER JOIN WEBAVSS.PT_TRAVAILLEURS trav ON trav.ID=po.ID_PT_TRAVAILLEURS INNER JOIN WEBAVSS.TIPAVSP avs ON trav.ID_TITIERP=avs.HTITIE INNER JOIN WEBAVSS.TIPERSP per ON per.HTITIE=avs.HTITIE INNER JOIN WEBAVSS.TITIERP tie ON tie.HTITIE=per.HTITIE WHERE ID_AFAFFIP=:idAffilie AND DATE_FIN_ACTIVITE>:date ) UNION ALL ( SELECT po.ID, po.ID_PT_TRAVAILLEURS, tie.HTLDE1, tie.HTLDE2, tie.HTLDU1, tie.HTLDU2, per.HPDNAI, avs.HXNAVS, po.CS_QUALIFICATION, po.CS_GENRE_SALAIRE, po.DATE_DEBUT_ACTIVITE, po.DATE_FIN_ACTIVITE FROM WEBAVSS.PT_POSTES_TRAVAILS po INNER JOIN WEBAVSS.PT_TRAVAILLEURS trav ON trav.ID=po.ID_PT_TRAVAILLEURS INNER JOIN WEBAVSS.TIPAVSP avs ON trav.ID_TITIERP=avs.HTITIE INNER JOIN WEBAVSS.TIPERSP per ON per.HTITIE=avs.HTITIE INNER JOIN WEBAVSS.TITIERP tie ON tie.HTITIE=per.HTITIE WHERE ID_AFAFFIP=:idAffilie AND (DATE_FIN_ACTIVITE IS NULL OR DATE_FIN_ACTIVITE=0) ) ) ORDER BY HTLDE1, HTLDE2 ) ) WHERE (HTLDU1 LIKE '%:nomPrenom%' OR HTLDU2 LIKE '%:nomPrenom%')";
    private static final String NATIVE_QUERY_POSTE_TRAVAIL_TRAVAILLEUR_ORDER_BY_RAISON_SOCIALE = "SELECT * FROM ( SELECT * FROM ( SELECT po.ID, po.ID_AFAFFIP, aff.MADESC, aff.MADESM, aff.MACONV, po.CS_QUALIFICATION, po.CS_GENRE_SALAIRE, po.DATE_DEBUT_ACTIVITE, po.DATE_FIN_ACTIVITE, adm.HBCADM, tieconv.HTLDE1 FROM WEBAVSS.PT_POSTES_TRAVAILS po INNER JOIN WEBAVSS.AFAFFIP aff ON aff.MAIAFF=po.ID_AFAFFIP INNER JOIN WEBAVSS.TIADMIP adm ON adm.HTITIE=aff.MACONV INNER JOIN WEBAVSS.TITIERP tieconv ON adm.HTITIE=tieconv.HTITIE WHERE po.ID_PT_TRAVAILLEURS=:idTravailleur AND DATE_FIN_ACTIVITE<=:date AND DATE_FIN_ACTIVITE<>0 ORDER BY DATE_FIN_ACTIVITE DESC, MADESM ) UNION ALL SELECT * FROM ( SELECT * FROM ( SELECT po.ID, po.ID_AFAFFIP, aff.MADESC, aff.MADESM, aff.MACONV, po.CS_QUALIFICATION, po.CS_GENRE_SALAIRE, po.DATE_DEBUT_ACTIVITE, po.DATE_FIN_ACTIVITE, adm.HBCADM, tieconv.HTLDE1 FROM WEBAVSS.PT_POSTES_TRAVAILS po INNER JOIN WEBAVSS.AFAFFIP aff ON aff.MAIAFF=po.ID_AFAFFIP INNER JOIN WEBAVSS.TIADMIP adm ON adm.HTITIE=aff.MACONV INNER JOIN WEBAVSS.TITIERP tieconv ON adm.HTITIE=tieconv.HTITIE WHERE po.ID_PT_TRAVAILLEURS=:idTravailleur AND DATE_FIN_ACTIVITE>:date UNION ALL SELECT po.ID, po.ID_AFAFFIP, aff.MADESC, aff.MADESM, aff.MACONV, po.CS_QUALIFICATION, po.CS_GENRE_SALAIRE, po.DATE_DEBUT_ACTIVITE, po.DATE_FIN_ACTIVITE, adm.HBCADM, tieconv.HTLDE1 FROM WEBAVSS.PT_POSTES_TRAVAILS po INNER JOIN WEBAVSS.AFAFFIP aff ON aff.MAIAFF=po.ID_AFAFFIP INNER JOIN WEBAVSS.TIADMIP adm ON adm.HTITIE=aff.MACONV INNER JOIN WEBAVSS.TITIERP tieconv ON adm.HTITIE=tieconv.HTITIE WHERE po.ID_PT_TRAVAILLEURS=:idTravailleur AND (DATE_FIN_ACTIVITE IS NULL OR DATE_FIN_ACTIVITE=0) ) ORDER BY MADESC )) WHERE MADESM LIKE '%:raisonSociale%'";

    @Override
    public PosteTravail create(final PosteTravail posteTravail) {
        if (posteTravail == null) {
            throw new NullPointerException("Le poste de travail ne peut être null");
        }

        try {
            // Conversion du poste de travail et persistance en base de données
            PosteTravail posteTravailCreated = super.create(posteTravail);

            String idPoste = posteTravailCreated.getId();

            // On set l'id ainsi que spy dans l'objet métier
            posteTravail.setId(idPoste);
            posteTravail.setSpy(posteTravailCreated.getSpy());

            // Toutes les cotisations sont persistées en base de données
            for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : posteTravail.getAdhesionsCotisations()) {
                AdhesionCotisationPosteTravailSimpleModel adhesionCotisationPosteTravailSimpleModel = AdhesionCotisationPosteTravailConverter
                        .convertToPersistence(idPoste, adhesionCotisationPosteTravail);
                JadePersistenceManager.add(adhesionCotisationPosteTravailSimpleModel);
            }

            // Tous les taux d'occupations sont persistés en base de données
            for (Occupation occupation : posteTravail.getOccupations()) {
                TauxOccupationSimpleModel tauxOccupationSimpleModel = OccupationConverter.convertToPersistence(
                        posteTravail.getId(), occupation);
                JadePersistenceManager.add(tauxOccupationSimpleModel);
            }
        } catch (JadePersistenceException e) {
            e.printStackTrace();
            try {
                JadeThread.currentJdbcConnection().rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return posteTravail;
    }

    @Override
    public PosteTravail findById(final String id) {
        PosteTravailSearchComplexModel searchModel = new PosteTravailSearchComplexModel();
        searchModel.setForIdPosteTravail(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<PosteTravail> findAll() {
        PosteTravailSearchComplexModel searchModel = new PosteTravailSearchComplexModel();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<PosteTravail> findByIdEmployeur(final String id) {
        PosteTravailSearchComplexModel searchModel = new PosteTravailSearchComplexModel();
        searchModel.setForIdEmployeur(id);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<PosteTravail> findPosteActif(final String id, Date periodeDebut, Date periodeFin) {
        List<PosteTravail> posteTravail = findByIdEmployeur(id);
        return findPosteActif(posteTravail, periodeDebut, periodeFin);
    }

    private List<PosteTravail> findPosteActif(List<PosteTravail> postes, Date periodeDebut, Date periodeFin) {
        List<PosteTravail> posteActifs = new ArrayList<PosteTravail>();

        Periode periodeDemande = new Periode(periodeDebut, periodeFin);

        // Tri la liste retourné par la fonction findByIdEmployeur afin qu'il ne reste que
        // les postes qui ont une période d'activité dans la période demandé
        for (PosteTravail poste : postes) {
            if (poste.isActif(periodeDemande)) {
                posteActifs.add(poste);
            }
        }

        return posteActifs;
    }

    @Override
    public List<PosteTravail> findPosteActifByConventionAndQualification(Date periodeDebut, Date periodeFin,
            String idConvention, List<Qualification> qualifications) {
        List<PosteTravail> posteTravail = findByIdConventionAndQualification(idConvention, qualifications);
        return findPosteActif(posteTravail, periodeDebut, periodeFin);
    }

    @Override
    public List<PosteTravail> findByIdTravailleur(final String id) {
        PosteTravailSearchComplexModel searchModel = new PosteTravailSearchComplexModel();
        searchModel.setForIdTravailleur(id);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<PosteTravail> findAAnnoncer(Date date, boolean isAnnonceMeroba) {
        PosteTravailSearchComplexModel searchModel = new PosteTravailSearchComplexModel();
        searchModel.setForPeriodeDebutLess(date.getSwissValue());
        searchModel.setAnnonceMeroba(isAnnonceMeroba);
        searchModel.setWhereKey(PosteTravailSearchComplexModel.WHERE_AANNONCER);
        List<PosteTravail> postes = searchAndFetch(searchModel);
        loadCotisations(postes);
        return postes;
    }

    @Override
    public void delete(final PosteTravail posteTravail) {
        if (posteTravail == null) {
            throw new NullPointerException("Unable to delete poste travail, the model passed is null!");
        }
        if ((posteTravail.getAdhesionsCotisations() == null) || (posteTravail.getAdhesionsCotisations().size() == 0)) {
            PosteTravailSimpleModel posteTravailSimpleModel = getConverter().convertToPersistence(posteTravail);
            try {
                JadePersistenceManager.delete(posteTravailSimpleModel);
            } catch (JadePersistenceException e) {
                logger.error(e.getMessage());
            }
        }

    }

    @Override
    public PosteTravail update(final PosteTravail posteTravail) {
        super.update(posteTravail);
        try {
            updateOccupations(posteTravail);
            updateAdhesionsCotisations(posteTravail);
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return posteTravail;
    }

    private void updateOccupations(final PosteTravail poste) throws JadePersistenceException {
        TauxOccupationSearchSimpleModel searchModel = new TauxOccupationSearchSimpleModel();
        searchModel.setForIdPosteTravail(String.valueOf(poste.getId()));
        JadePersistenceManager.delete(searchModel);

        for (Occupation occupation : poste.getOccupations()) {
            TauxOccupationSimpleModel tauxOccupationSimpleModel = OccupationConverter.convertToPersistence(
                    poste.getId(), occupation);
            JadePersistenceManager.add(tauxOccupationSimpleModel);
        }
    }

    private void updateAdhesionsCotisations(final PosteTravail poste) throws JadePersistenceException {
        AdhesionCotisationPosteTravailRepository repository = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository();

        List<AdhesionCotisationPosteTravail> adhesionsExistantes = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findByIdPosteTravail(poste.getId());

        List<AdhesionCotisationPosteTravail> liste = poste.getAdhesionsCotisations();
        List<AdhesionCotisationPosteTravail> listeASupprimer = new ArrayList<AdhesionCotisationPosteTravail>(
                adhesionsExistantes);
        // TODO : à trier par date de début pour que les notifications soient traitées dans l'ordre.
        List<AdhesionCotisationPosteTravail> listeAAjouter = new ArrayList<AdhesionCotisationPosteTravail>(
                poste.getAdhesionsCotisations());
        List<AdhesionCotisationPosteTravail> listeAModifier = new ArrayList<AdhesionCotisationPosteTravail>(
                poste.getAdhesionsCotisations());

        listeASupprimer.removeAll(liste);
        listeAAjouter.removeAll(adhesionsExistantes);
        listeAModifier.removeAll(listeAAjouter);
        listeAModifier.removeAll(listeASupprimer);

        for (AdhesionCotisationPosteTravail adhesion : listeASupprimer) {
            repository.delete(poste.getId(), adhesion);
        }

        for (AdhesionCotisationPosteTravail adhesion : listeAAjouter) {
            repository.create(poste.getId(), adhesion);
        }

        for (AdhesionCotisationPosteTravail adhesion : listeAModifier) {
            repository.update(poste.getId(), adhesion);
        }
    }

    /**
     * Charge les postes de travail avec les dépendances (adhesions cotisations) selon un idTravailleur
     */
    @Override
    public List<PosteTravail> findByIdTravailleurWithDependencies(final String idTravailleur) {
        List<PosteTravail> posteTravails = findByIdTravailleur(idTravailleur);
        loadDependencies(posteTravails);
        return posteTravails;
    }

    /**
     * Charge les postes de travail avec les dépendances (adhesions cotisations) selon un idPosteTravail
     */
    @Override
    public PosteTravail findByIdPosteTravailWithDependencies(final String idPosteTravail) {
        PosteTravail posteTravail = findById(idPosteTravail);
        loadCotisations(posteTravail);
        return posteTravail;
    }

    private void loadDependencies(final List<PosteTravail> postesTravail) {
        loadCotisations(postesTravail);
    }

    private void loadCotisations(PosteTravail posteTravail) {
        List<AdhesionCotisationPosteTravail> adhesionCotisationsListe = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findByIdPosteTravail(posteTravail.getId());
        posteTravail.setAdhesionsCotisations(adhesionCotisationsListe);
    }

    private void loadCotisations(final List<PosteTravail> postesTravail) {
        List<String> ids = new ArrayList<String>();
        for (PosteTravail posteTravail : postesTravail) {
            ids.add(posteTravail.getId());
        }

        List<AdhesionCotisationPosteTravailComplexModel> cotisations = RepositoryJade.searchByLot(ids,
                createSearchLotExecutor());

        Map<String, List<AdhesionCotisationPosteTravailComplexModel>> map = JadeListUtil.groupBy(cotisations,
                new JadeListUtil.Key<AdhesionCotisationPosteTravailComplexModel>() {
                    @Override
                    public String exec(final AdhesionCotisationPosteTravailComplexModel e) {
                        return String.valueOf(e.getAdhesionCotisationPosteTravail().getIdPosteTravail());
                    }
                });

        for (PosteTravail poste : postesTravail) {
            String idPoste = poste.getId();
            if (map.containsKey(idPoste)) {
                List<AdhesionCotisationPosteTravailComplexModel> adhesionCotisationComplexModels = map.get(idPoste);
                List<AdhesionCotisationPosteTravail> adhesionPostes = AdhesionCotisationPosteTravailConverter
                        .convertToDomain(adhesionCotisationComplexModels);
                poste.setAdhesionsCotisations(adhesionPostes);
            }
        }

    }

    /**
     * Recherche les adhesionsCotisations selon les ids postes de travail
     * 
     * @return {@link SearchLotExecutor}
     */
    private SearchLotExecutor<AdhesionCotisationPosteTravailComplexModel> createSearchLotExecutor() {
        return new SearchLotExecutor<AdhesionCotisationPosteTravailComplexModel>() {
            @Override
            public List<AdhesionCotisationPosteTravailComplexModel> execute(final List<String> ids) {
                AdhesionCotisationPosteTravailSearchComplexModel searchModel = new AdhesionCotisationPosteTravailSearchComplexModel();
                searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                searchModel.setInId(ids);
                try {
                    JadePersistenceManager.search(searchModel);
                } catch (JadePersistenceException ex) {
                    ex.printStackTrace();
                }
                List<JadeAbstractModel> models = Arrays.asList(searchModel.getSearchResults());
                List<AdhesionCotisationPosteTravailComplexModel> cotisationsPoste = (List<AdhesionCotisationPosteTravailComplexModel>) (List<?>) models;
                return cotisationsPoste;
            }
        };
    }

    @Override
    public void deleteById(String idEntity) {
        PosteTravail poste = new PosteTravail(idEntity);
        delete(poste);
    }

    @Override
    public DomaineConverterJade<PosteTravail, PosteTravailComplexModel, PosteTravailSimpleModel> getConverter() {
        return PosteTravailConverter.getInstance();
    }

    @Override
    public List<PosteTravail> findByIdAffilieWithPagination(String idAffilie, QueryParameters extrasParams, Date date,
            int offset, int size) {
        List<PosteTravail> posteTravails = new ArrayList<PosteTravail>();
        Map<String, String> params = new HashMap<String, String>();
        String query = createFindByIdAffiliationQuery(idAffilie, extrasParams, date, params);
        try {
            ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, params, offset, size,
                    PosteTravailRepositoryJade.class);
            for (HashMap<String, Object> value : result) {
                PosteTravail posteTravail = new PosteTravail();
                Travailleur travailleur = new Travailleur();
                posteTravail.setTravailleur(travailleur);

                travailleur.setId(String.valueOf(value.get("ID_PT_TRAVAILLEURS")));
                travailleur.setDesignation1(String.valueOf(value.get("HTLDE1")));
                travailleur.setDesignation2(String.valueOf(value.get("HTLDE2")));
                if (!JadeNumericUtil.isEmptyOrZero(String.valueOf(value.get("HPDNAI")))) {
                    travailleur.setDateNaissance(new Date(String.valueOf(value.get("HPDNAI"))).getSwissValue());
                }
                travailleur.setNumAvsActuel(String.valueOf(value.get("HXNAVS")));
                posteTravail.setId(String.valueOf(value.get("ID")));
                posteTravail.setQualification(Qualification.fromValue(String.valueOf(value.get("CS_QUALIFICATION"))));
                posteTravail.setTypeSalaire(TypeSalaire.fromValue(String.valueOf(value.get("CS_GENRE_SALAIRE"))));

                String datedebut = String.valueOf(value.get("DATE_DEBUT_ACTIVITE"));
                String dateFin = String.valueOf(value.get("DATE_FIN_ACTIVITE"));
                if (JadeNumericUtil.isEmptyOrZero(dateFin)) {
                    dateFin = null;
                }
                posteTravail.setPeriodeActivite(new Periode(datedebut, dateFin));
                posteTravails.add(posteTravail);
            }
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return posteTravails;
    }

    @Override
    public int countByIdAffilie(String idAffilie, QueryParameters extrasParams, Date date) {
        Map<String, String> params = new HashMap<String, String>();
        String query = createFindByIdAffiliationQuery(idAffilie, extrasParams, date, params);
        try {
            return DBUtil.count(query, params, this.getClass());
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    private String createFindByIdAffiliationQuery(String idAffilie, QueryParameters extrasParams, Date date,
            Map<String, String> params) {
        params.put("idAffilie", idAffilie);
        params.put("date", date.getValue());
        params.putAll(extrasParams.valuesWithCaps());

        String query = NATIVE_QUERY_POSTE_TRAVAIL_ORDER_BY_NOM;
        String idPosteTravail = extrasParams.get(QueryParametersRegistry.PT_ID_POSTE_TRAVAIL);
        String idTravailleur = extrasParams.get(QueryParametersRegistry.PT_ID_TRAVAILLEUR);
        String genreSalaire = extrasParams.get(QueryParametersRegistry.PT_GENRE);
        String qualification = extrasParams.get(QueryParametersRegistry.PT_QUALIFICATION);
        String nss = extrasParams.get(QueryParametersRegistry.PT_NSS);
        String dateNais = extrasParams.get(QueryParametersRegistry.PT_DATE_NAIS);

        if (!JadeStringUtil.isEmpty(idPosteTravail)) {
            query = query + "AND ID>=:idPosteTravail ";
        }
        if (!JadeStringUtil.isEmpty(idTravailleur)) {
            query = query + "AND ID_PT_TRAVAILLEURS>=:idTravailleur ";
        }
        if (!JadeStringUtil.isEmpty(genreSalaire)) {
            query = query += "AND CS_GENRE_SALAIRE=:genre ";
        }
        if (!JadeStringUtil.isEmpty(qualification)) {
            query = query += "AND CS_QUALIFICATION=:qualification ";
        }
        if (!JadeStringUtil.isEmpty(nss)) {
            query = query += "AND HXNAVS LIKE '%:nss%' ";
        }
        if (!JadeStringUtil.isEmpty(dateNais)) {
            query = query + "AND HPDNAI>=:dateNais ";
        }

        if (!JadeStringUtil.isEmpty(idPosteTravail)) {
            query = query + "ORDER BY ID";
        } else if (!JadeStringUtil.isEmpty(idTravailleur)) {
            query = query + "ORDER BY ID_PT_TRAVAILLEURS";
        } else if (!JadeStringUtil.isEmpty(dateNais)) {
            query = query + "ORDER BY HPDNAI";
        }
        return query;
    }

    @Override
    public List<PosteTravail> findByIdTravailleurWithPagination(String idTravailleur, QueryParameters extrasParams,
            Date date, int offset, int size) {
        List<PosteTravail> posteTravails = new ArrayList<PosteTravail>();
        Map<String, String> params = new HashMap<String, String>();
        String query = createFindByIdTravailleurQuery(idTravailleur, extrasParams, date, params);

        try {
            ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, params, offset, size,
                    PosteTravailRepositoryJade.class);
            for (HashMap<String, Object> value : result) {
                PosteTravail posteTravail = new PosteTravail();
                Employeur employeur = new Employeur();
                Convention convention = new Convention();

                employeur.setConvention(convention);
                posteTravail.setEmployeur(employeur);

                employeur.setId(String.valueOf(value.get("ID_AFAFFIP")));
                employeur.setRaisonSociale(String.valueOf(value.get("MADESC")));
                posteTravail.setId(String.valueOf(value.get("ID")));
                posteTravail.setQualification(Qualification.fromValue(String.valueOf(value.get("CS_QUALIFICATION"))));
                posteTravail.setTypeSalaire(TypeSalaire.fromValue(String.valueOf(value.get("CS_GENRE_SALAIRE"))));

                String datedebut = String.valueOf(value.get("DATE_DEBUT_ACTIVITE"));
                String dateFin = String.valueOf(value.get("DATE_FIN_ACTIVITE"));
                if (JadeNumericUtil.isEmptyOrZero(dateFin)) {
                    dateFin = null;
                }
                posteTravail.setPeriodeActivite(new Periode(datedebut, dateFin));

                convention.setDesignation(String.valueOf(value.get("HTLDE1")));
                convention.setCode(String.valueOf(value.get("HBCADM")));
                posteTravails.add(posteTravail);
            }
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return posteTravails;
    }

    private String createFindByIdTravailleurQuery(String idTravailleur, QueryParameters extrasParams, Date date,
            Map<String, String> params) {
        params.put("idTravailleur", idTravailleur);
        params.put("date", date.getValue());
        params.putAll(extrasParams.valuesWithCaps());

        String query = NATIVE_QUERY_POSTE_TRAVAIL_TRAVAILLEUR_ORDER_BY_RAISON_SOCIALE;
        String idPosteTravail = extrasParams.get(QueryParametersRegistry.PT_ID_POSTE_TRAVAIL);
        String idEmployeur = extrasParams.get(QueryParametersRegistry.PT_ID_EMPLOYEUR);
        String genreSalaire = extrasParams.get(QueryParametersRegistry.PT_GENRE);
        String qualification = extrasParams.get(QueryParametersRegistry.PT_QUALIFICATION);
        String convention = extrasParams.get(QueryParametersRegistry.PT_CONVENTION);

        if (!JadeStringUtil.isEmpty(idPosteTravail)) {
            query = query + "AND ID>=:idPosteTravail ";
        }
        if (!JadeStringUtil.isEmpty(idEmployeur)) {
            query = query + "AND ID_AFAFFIP>=:idEmployeur ";
        }
        if (!JadeStringUtil.isEmpty(genreSalaire)) {
            query = query += "AND CS_GENRE_SALAIRE=:genre ";
        }
        if (!JadeStringUtil.isEmpty(qualification)) {
            query = query += "AND CS_QUALIFICATION=:qualification ";
        }
        if (!JadeStringUtil.isEmpty(convention)) {
            query = query += "AND MACONV=:convention ";
        }

        if (!JadeStringUtil.isEmpty(idPosteTravail)) {
            query = query + "ORDER BY ID";
        } else if (!JadeStringUtil.isEmpty(idEmployeur)) {
            query = query + "ORDER BY ID_AFAFFIP";
        }
        return query;
    }

    @Override
    public int countByIdTravailleur(String idTravailleur, QueryParameters extrasParams, Date date) {
        Map<String, String> params = new HashMap<String, String>();
        String query = createFindByIdTravailleurQuery(idTravailleur, extrasParams, date, params);

        try {
            return DBUtil.count(query, params, this.getClass());
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public List<PosteTravail> findByIdConventionAndQualification(String idConvention, List<Qualification> qualifications) {
        PosteTravailSearchComplexModel searchModel = new PosteTravailSearchComplexModel();
        searchModel.setForIdConvention(idConvention);
        searchModel.setForQualficationIn(qualifications);
        return searchAndFetch(searchModel);
    }
}
