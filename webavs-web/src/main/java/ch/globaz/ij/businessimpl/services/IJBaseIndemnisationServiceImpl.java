package ch.globaz.ij.businessimpl.services;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prestations.IIJIJCalculee;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.regles.IJPrononceRegles;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import java.util.Iterator;
import ch.globaz.ij.business.models.IJAbsence;
import ch.globaz.ij.business.models.IJAbsenceSearchModel;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisation;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisationSearchModel;
import ch.globaz.ij.business.services.IJAbsenceService;
import ch.globaz.ij.business.services.IJBaseIndemnisationService;
import ch.globaz.ij.business.services.IJServiceLocator;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;

public class IJBaseIndemnisationServiceImpl implements IJBaseIndemnisationService {

    @Override
    public int count(IJSimpleBaseIndemnisationSearchModel search) throws ServiceBusinessException,
            ServiceTechnicalException {
        if (search == null) {
            throw new ServiceTechnicalException(
                    "IJBaseIndemnisationServiceImpl : unable to count values because searchModel is null");
        }
        try {
            return JadePersistenceManager.count(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException("IJBaseIndemnisationServiceImpl : unable to count values");
        }
    }

    @Override
    public IJSimpleBaseIndemnisation create(IJSimpleBaseIndemnisation entity) throws ServiceBusinessException,
            ServiceTechnicalException {

        // Récupération de la session
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session == null) {
            throw new ServiceTechnicalException("Can not retreive user session");
        }

        IJBaseIndemnisation baseIndemnisation = null;
        BITransaction transaction = null;
        try {
            try {
                transaction = session.newTransaction();
                transaction.openTransaction();

                // Récupération de l'entitée en db
                baseIndemnisation = new IJBaseIndemnisation();
                baseIndemnisation.setSession(session);
                baseIndemnisation.setDateDebutPeriode(entity.getDateDeDebut());
                baseIndemnisation.setDateFinPeriode(entity.getDateDeFin());
                baseIndemnisation.setNombreJoursExterne(entity.getJoursExternes());
                baseIndemnisation.setNombreJoursInterne(entity.getJoursInternes());
                baseIndemnisation.setNombreJoursInterruption(entity.getJoursInterruption());
                baseIndemnisation.setCsMotifInterruption(entity.getMotifInterruption());
                baseIndemnisation.setIdPrononce(entity.getIdPrononce());
                baseIndemnisation.setCsCantonImpotSource(entity.getCsCantonImposition());
                baseIndemnisation.setTauxImpotSource(entity.getTauxImposition());
                baseIndemnisation.setRemarque(entity.getRemarque());
                baseIndemnisation.add(transaction);

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw new ServiceTechnicalException("Can not create new IJBaseIndemnisation. Error : " + e.toString());
            } finally {
                if (transaction != null) {
                    try {
                        if (!transaction.hasErrors() && !transaction.isRollbackOnly()) {
                            transaction.commit();
                        } else {
                            transaction.rollback();
                        }
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            throw new ServiceTechnicalException(e.toString(), e);
        }
        entity.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
        return entity;
    }

    @Override
    public IJSimpleBaseIndemnisation delete(IJSimpleBaseIndemnisation entity) throws ServiceBusinessException,
            ServiceTechnicalException {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session == null) {
            throw new ServiceTechnicalException("Can not retreive user session");
        }

        IJSimpleBaseIndemnisation returnedEntity = new IJSimpleBaseIndemnisation();
        returnedEntity.setIdPrononce(entity.getIdPrononce());
        BITransaction transaction = null;
        try {

            transaction = session.newTransaction();
            transaction.openTransaction();

            IJBaseIndemnisation baseIndemnisation = new IJBaseIndemnisation();
            baseIndemnisation.setIdBaseIndemisation(entity.getIdBaseIndemnisation());
            baseIndemnisation.setSession(session);
            baseIndemnisation.retrieve(transaction);

            if (baseIndemnisation.isNew()) {
                throw new ServiceTechnicalException("Can not delete IJBaseIndemnisation with id : "
                        + entity.getIdBaseIndemnisation() + " because it doesn't exist");
            }

            // Il s'agit d'une base enfant dans l'état validé.
            if (!JadeStringUtil.isIntegerEmpty(entity.getIdParent())
                    && (IIJBaseIndemnisation.CS_VALIDE.equals(entity.getEtatCS()) || IIJBaseIndemnisation.CS_OUVERT
                            .equals(entity.getEtatCS()))) {

                // s'il y a plusieurs enfants pour un parent, il faut reprendre
                // la base avant celle que l'on supprime
                // non-javadoc
                //
                // Exemple : | idBaseInd | idParent |
                // | --------- | -------- |
                // 1) | 842 | 0 |
                // 2) | 979 | 842 |
                // 3) | 980 | 842 |
                //
                // Si par exemple, 1) et 2) sont dans l'état annulé et que 3)
                // est dans l'état validé. Et que l'on veut
                // supprimer la base 3), alors on va laisser la 1) telle quelle,
                // mais par contre, on va mettre la 2) dans l'état
                // communiqué après la suppression de la 3)
                //

                // Pour cela, on regarde tout d'abord s'il y a plusieurs bases
                // avec l'idParent de la base
                IJBaseIndemnisationManager baseIndMan = new IJBaseIndemnisationManager();
                baseIndMan.setSession(session);
                baseIndMan.setForIdParent(entity.getIdParent());
                baseIndMan.setOrderBy(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION + " DESC");
                baseIndMan.find(transaction);

                // S'il y en a plusieurs
                if (baseIndMan.size() > 1) {

                    // orderByParDefaut --> Id BaseIndemnisations DESC.
                    // Donc, il faut prendre le deuxième de la liste
                    int nb = 0;
                    for (Iterator<?> iter = baseIndMan.iterator(); iter.hasNext();) {
                        IJBaseIndemnisation baseInd = (IJBaseIndemnisation) iter.next();
                        nb++;
                        if (nb == 2) {
                            if (!baseInd.isNew() && IIJBaseIndemnisation.CS_ANNULE.equals(baseInd.getCsEtat())) {
                                baseInd.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                                baseInd.update(transaction);
                            }
                        }
                    }

                    // S'il n'y a pas plusieurs enfants, on va donc modifier le parent !
                } else {

                    // On récupère la base parente
                    IJBaseIndemnisation parent = new IJBaseIndemnisation();
                    parent.setSession(session);
                    parent.setIdBaseIndemisation(entity.getIdParent());
                    parent.retrieve(transaction);
                    if (!parent.isNew() && IIJBaseIndemnisation.CS_ANNULE.equals(parent.getCsEtat())) {
                        parent.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                        parent.update(transaction);
                    }
                }
            }

            if (!transaction.hasErrors()) {
                // On supprime toutes les absences associées à la base d'indemnisation
                try {
                    IJAbsenceService absenceService = IJServiceLocator.getAbsenceService();
                    IJAbsenceSearchModel sm = new IJAbsenceSearchModel();
                    sm.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());
                    absenceService.search(sm);
                    for (JadeAbstractModel t : sm.getSearchResults()) {
                        absenceService.deleteWithoutBaseIndemnisationUpdate((IJAbsence) t);
                    }
                    // On supprime la base d'indemnisation.
                    baseIndemnisation.delete(transaction);
                } catch (JadeApplicationServiceNotAvailableException e) {
                    throw new ServiceTechnicalException(
                            "Can not delete absences because service IJAbsenceService is unreachable");
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                JadeLogger.error(this, e.toString());
                throw new ServiceTechnicalException("Suppression can not be done because an exception was thrown", e);
            }
        } finally {
            try {
                if (transaction != null) {
                    try {
                        if (!transaction.hasErrors() && !transaction.isRollbackOnly()) {
                            transaction.commit();
                        } else {
                            transaction.rollback();
                        }
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, e.toString());
                throw new ServiceTechnicalException(e.toString(), e);
            }
        }
        return returnedEntity;
    }

    /**
     * Cette méthode est un peu particulière... En théorie, l'id de la base d'indemnisation
     */
    @Override
    public IJSimpleBaseIndemnisation getNewEntity(String idPrononce) throws ServiceBusinessException,
            ServiceTechnicalException {

        if (JadeStringUtil.isBlankOrZero(idPrononce)) {
            throw new ServiceTechnicalException(
                    "Can not get new IJBaseIndemnisation entity because id prononce is empty");
        }

        BSession session = BSessionUtil.getSessionFromThreadContext();
        IJPrononce prononce = new IJPrononce();
        prononce.setSession(session);
        prononce.setIdPrononce(idPrononce);
        try {
            prononce.retrieve();
        } catch (Exception e) {
            throw new ServiceTechnicalException("Exception occurs when retieving prononce with id : " + idPrononce
                    + " from database");
        }
        if (prononce.isNew()) {
            throw new ServiceTechnicalException("Any prononce founded in database with id : " + idPrononce
                    + " can not create new IJBase_Indemnisation");
        }

        IJSimpleBaseIndemnisation baseIndemnisation = new IJSimpleBaseIndemnisation();
        setDefaultDate(baseIndemnisation);
        baseIndemnisation.setCsCantonImposition(prononce.getCsCantonImpositionSource());
        baseIndemnisation.setTauxImposition(prononce.getTauxImpositionSource());
        if(IIJPrononce.CS_FPI.equals(prononce.getCsTypeIJ())) {
            baseIndemnisation.setJoursExternes(IIJPrestation.JOUR_FPI);
        }
        return baseIndemnisation;
    }

    @Override
    public IJSimpleBaseIndemnisation read(String idEntity) throws ServiceBusinessException, ServiceTechnicalException {
        if (JadeStringUtil.isBlankOrZero(idEntity)) {
            throw new ServiceTechnicalException("Can not read IJBaseIndemnisation entity because idEntity is empty");
        }

        IJSimpleBaseIndemnisation entity = new IJSimpleBaseIndemnisation();
        IJSimpleBaseIndemnisation returnedEntity = null;
        try {
            entity.setIdBaseIndemnisation(idEntity);
            returnedEntity = (IJSimpleBaseIndemnisation) JadePersistenceManager.read(entity);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnedEntity;
    }

    @Override
    public IJSimpleBaseIndemnisationSearchModel search(IJSimpleBaseIndemnisationSearchModel search)
            throws ServiceBusinessException, ServiceTechnicalException {
        IJSimpleBaseIndemnisationSearchModel returnedSearchModel = null;
        try {
            returnedSearchModel = (IJSimpleBaseIndemnisationSearchModel) JadePersistenceManager.search(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnedSearchModel;
    }

    private IJSimpleBaseIndemnisation setDefaultDate(IJSimpleBaseIndemnisation entity) {
        String t = JadeDateUtil.getGlobazFormattedDate(new Date());
        String dateDeDebut = JadeDateUtil.getFirstDateOfMonth(t);
        String dateDeFin = JadeDateUtil.getLastDateOfMonth(dateDeDebut);
        entity.setDateDeDebut(dateDeDebut);
        entity.setDateDeFin(dateDeFin);
        return entity;
    }

    @Override
    public IJSimpleBaseIndemnisation update(IJSimpleBaseIndemnisation entity) throws ServiceBusinessException,
            ServiceTechnicalException {

        IJBaseIndemnisation baseIndemnisation = null;
        try {

            // Récupération de la session
            BSession session = BSessionUtil.getSessionFromThreadContext();
            if (session == null) {
                throw new ServiceTechnicalException("Can not retreive user session");
            }

            // Récupération de l'entitée en db
            baseIndemnisation = new IJBaseIndemnisation();
            baseIndemnisation.setSession(session);
            baseIndemnisation.setId(entity.getId());
            baseIndemnisation.retrieve();

            // Récupération des champs à mettre à jour
            baseIndemnisation.setDateDebutPeriode(entity.getDateDeDebut());
            baseIndemnisation.setDateFinPeriode(entity.getDateDeFin());
            baseIndemnisation.setNombreJoursExterne(entity.getJoursExternes());
            baseIndemnisation.setNombreJoursInterne(entity.getJoursInternes());
            baseIndemnisation.setNombreJoursInterruption(entity.getJoursInterruption());
            baseIndemnisation.setCsMotifInterruption(entity.getMotifInterruption());
            baseIndemnisation.setCsCantonImpotSource(entity.getCsCantonImposition());
            baseIndemnisation.setTauxImpotSource(entity.getTauxImposition());

            // Ajouter un espace avant le retour à la ligne
            // parce que JASPER n'arrive pas à interpréter deux retours à la ligne successive
            String remarque = entity.getRemarque().replaceAll("\r\n", " \n");
            baseIndemnisation.setRemarque(remarque);

            BITransaction transaction = session.newTransaction();
            try {
                transaction.openTransaction();

                // réinitialiser la base
                IJBaseIndemnisationRegles.reinitialiser(session, transaction, baseIndemnisation);
                baseIndemnisation.update(transaction);

                // réinitialiser l'etat du prononce si necessaire
                IJPrononce prononce = IJPrononce.loadPrononce(session, transaction, baseIndemnisation.getIdPrononce(),
                        baseIndemnisation.getCsTypeIJ());
                IJPrononceRegles.verifierEtatAttente(session, transaction, prononce);
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
            } finally {
                if (transaction != null) {
                    try {
                        if (!transaction.hasErrors() && !transaction.isRollbackOnly()) {
                            transaction.commit();
                        } else {
                            transaction.rollback();
                        }
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            throw new ServiceTechnicalException(e.toString(), e);
        }

        entity.setDateDeDebut(baseIndemnisation.getDateDebutPeriode());
        entity.setDateDeFin(baseIndemnisation.getDateFinPeriode());
        entity.setJoursExternes(baseIndemnisation.getNombreJoursExterne());
        entity.setJoursInternes(baseIndemnisation.getNombreJoursInterne());
        entity.setJoursInterruption(baseIndemnisation.getNombreJoursInterruption());
        entity.setMotifInterruption(baseIndemnisation.getCsMotifInterruption());
        return entity;
    }
}
