package ch.globaz.hera.businessimpl.services.models.famille;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.exceptions.models.RelationConjointException;
import ch.globaz.hera.business.models.famille.DateNaissanceConjoint;
import ch.globaz.hera.business.models.famille.DateNaissanceConjointSearch;
import ch.globaz.hera.business.models.famille.RelationConjoint;
import ch.globaz.hera.business.models.famille.RelationConjointSearch;
import ch.globaz.hera.business.services.models.famille.RelationConjointService;
import ch.globaz.hera.businessimpl.services.HeraAbstractServiceImpl;

public class RelationConjointServiceImpl extends HeraAbstractServiceImpl implements RelationConjointService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.PeriodeService#count(
     * ch.globaz.hera.business.models.famille.SimplePeriodeSearch)
     */
    @Override
    public int count(RelationConjointSearch search) throws RelationConjointException, JadePersistenceException {
        if (search == null) {
            throw new RelationConjointException("Unable to count RelationConjoint, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.PeriodeService#read(java .lang.String)
     */
    @Override
    public RelationConjoint read(String idRelationConjoint) throws JadePersistenceException, RelationConjointException {
        if (JadeStringUtil.isEmpty(idRelationConjoint)) {
            throw new RelationConjointException("Unable to read RelationConjoint, the id passed is null!");
        }
        RelationConjoint relationConjoint = new RelationConjoint();
        relationConjoint.setId(idRelationConjoint);
        return (RelationConjoint) JadePersistenceManager.read(relationConjoint);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.PeriodeService#search
     * (ch.globaz.hera.business.models.famille.SimplePeriodeSearch)
     */
    @Override
    public RelationConjointSearch search(RelationConjointSearch search) throws JadePersistenceException,
            RelationConjointException {
        if (search == null) {
            throw new RelationConjointException("Unable to search RelationConjoint, the search model passed is null!");
        }
        return (RelationConjointSearch) JadePersistenceManager.search(search);
    }

    /**
     * Charge une instance de <code>RelationConjoint</code> pour un idMembreFamille durant une date donnée
     * 
     * @param idMembreFamille
     * @param dateDebut
     * @return instance de <code>RelationConjoint</code> null si pas trouvé
     * @throws JadePersistenceException
     * @throws RelationConjointException si plus d'une relation trouvé
     */
    @Override
    public RelationConjoint readRelationForIdMembreFamilleByDate(String idConjoint, String dateDebut)
            throws JadePersistenceException, RelationConjointException {

        RelationConjoint relation = null;

        if (idConjoint == null) {
            throw new RelationConjointException(
                    "Unable to search the date naissance for conjoint the idConjoint passed is null!");
        }
        if (dateDebut == null || !JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new RelationConjointException(
                    "Unable to search the date naissance for conjoint the dateDebut passed is null or not properly formatted ["
                            + dateDebut + "]");
        }

        RelationConjointSearch search = new RelationConjointSearch();
        search.setForIdConjoint1(idConjoint);
        search.setForIdConjoint2(idConjoint);
        search.setForDateValable(dateDebut);
        search.setWhereKey(RelationConjointSearch.FOR_DATA_VALABLE_WHERE_KEY);
        JadePersistenceManager.search(search);

        if (search.getSearchResults().length > 1) {
            throw new RelationConjointException(
                    "The search model return more than one result for RelationConjoint, idConjoint [" + idConjoint
                            + "], dateDebut [" + dateDebut + "]");
        }
        if (search.getSearchResults().length == 0) {
            return relation;
        }

        return (RelationConjoint) search.getSearchResults()[0];
    }

    /**
     * Chargement d'un objet de type <code>DateNaissanceConjoint</code> encapsulant
     * les informations pour gérer un conjoint.
     * 
     * @param nss, le nss de la personne recherché
     * @throws RelationConjointExcception si 0 ou plus de 1 personne retrouvé
     */
    @Override
    public DateNaissanceConjoint readByNss(String nss) throws JadePersistenceException, RelationConjointException {

        if (JadeStringUtil.isBlank(nss)) {
            throw new RelationConjointException(
                    "Unable to search DateNaissanceConjoint, the nss passed is null or empty!");
        }

        DateNaissanceConjoint toReturn;

        DateNaissanceConjointSearch search = new DateNaissanceConjointSearch();
        search.setForNss(nss);
        search = (DateNaissanceConjointSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length > 1) {
            throw new RelationConjointException("More than one MembreFamille find with this nss[" + nss + "]");
        } else if (search.getSearchResults().length == 0) {
            throw new RelationConjointException("No MembreFamille find with this nss[" + nss + "]");
        } else {
            toReturn = (DateNaissanceConjoint) search.getSearchResults()[0];
        }

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.hera.business.services.models.famille.RelationConjointService#getDateNaissanceConjointForDate(java.
     * lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getDateNaissanceConjointForDate(String nSSRequerant, String dateDebut)
            throws RelationConjointException, JadePersistenceException, MembreFamilleException {

        DateNaissanceConjointHandler handler = new DateNaissanceConjointHandler(nSSRequerant, dateDebut,
                new MembreFamilleServiceImpl(), new RelationConjointServiceImpl());
        return handler.handle();

    }

}
