package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationViewBean;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.common.sql.QueryUpdateExecutor;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.mercato.notification.Notification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.ws.utils.UtilsService;

public class MiseAJourCotisationsPosteTravailTracker extends BAbstractEntityExternalServiceWithContext {

    private RequestFactory requestFactory = new RequestFactory();
    private BEntity currentEntity = null;

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        currentEntity = entity;
        // Retrieve date from DB
    }

    @Override
    public void init(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        currentEntity = entity;
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        currentEntity = entity;
        BSession session = UtilsService.initSession();
        AFCotisationViewBean vbCotisation = (AFCotisationViewBean) entity;
        AFCotisation vbOldCotisation = vbCotisation.getOldCotisation();
        /*
         * Executé uniquement en cas de radiation
         */
        if (vbCotisation != null) {
            try {
                JadeThreadContext threadContext = initThreadContext(session);
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                DateTimeFormatter frmt = DateTimeFormat.forPattern("dd.MM.yyyy");
                String dateFin = "";

                // Radiation
                if (!vbCotisation.getDateFin().isEmpty()) {
                    dateFin = new DateTime(frmt.parseDateTime(vbCotisation.getDateFin())).toString("yyyyMMdd");

                    QueryUpdateExecutor.executeUpdate(
                            Requests.getUpdateCotisationsPoste(vbCotisation.getAffiliationId(), dateFin, vbCotisation),
                            null, session);
                    executeUpdateDecomptesPoste(vbCotisation.getAffiliationId(), dateFin);
                    notifyMercato(vbCotisation.getAffiliationId(), InfoType.RADIATION_COTISATION_POSTE_LPP, dateFin,
                            entity.getSession());

                }
                // Deradiation
                else if (vbOldCotisation != null && !vbOldCotisation.getDateFin().isEmpty()) {
                    dateFin = new DateTime(frmt.parseDateTime(vbOldCotisation.getDateFin())).toString("yyyyMMdd");
                    QueryUpdateExecutor.executeUpdate(Requests.getDeradiationCotisationsPoste(
                            vbCotisation.getAffiliationId(), dateFin, vbOldCotisation), null, session);
                    executeUpdateDecomptesPoste(vbCotisation.getAffiliationId(), dateFin);
                }

            } catch (Exception e) {
                session.addError(e.getMessage());
                JadeLogger.error(e, e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }
        }
    }

    private void executeUpdateDecomptesPoste(String idEmployeur, String dateATraiter) {
        try {
            VulpeculaServiceLocator.getDecompteService().actualiserDecompteForExternalServices(idEmployeur,
                    dateATraiter);
        } catch (JadePersistenceException e) {
            JadeLogger.info(this, this.getClass().getName()
                    + " : Exception when updating decomptes, persistence exception : " + e.toString());
        } catch (UnsatisfiedSpecificationException e) {
            JadeLogger.info(this, this.getClass().getName()
                    + " : Exception when updating decomptes, specification exception : " + e.toString());
        }
    }

    private JadeThreadContext initThreadContext(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    private void notifyMercato(String idAffilie, InfoType ajoutCotisationPosteLpp, String dateFin, BSession bSession) {
        List<String> cotisationLppIds = new ArrayList<String>();
        List<PosteTravail> postes = VulpeculaServiceLocator.getPosteTravailService().findPostesActifsByIdAffilie(
                idAffilie);
        Date formatedDateFin = new Date(dateFin);
        for (PosteTravail poste : postes) {
            List<AdhesionCotisationPosteTravail> listAdhesion = VulpeculaRepositoryLocator
                    .getAdhesionCotisationPosteRepository().findByIdPosteTravail(poste.getId());
            for (AdhesionCotisationPosteTravail adhesion : listAdhesion) {
                if (adhesion.isLPP() && adhesion.getPeriode() != null
                        && adhesion.getPeriode().getDateFin().equals(formatedDateFin)) {
                    cotisationLppIds.add(adhesion.getId());
                }
            }
        }
        for (String idLpp : cotisationLppIds) {
            Notification notification = new Notification(ajoutCotisationPosteLpp, idLpp);
            requestFactory.persistFromAnciennePersistance(notification, bSession);
        }
    }

    private static class Requests {

        /*
         * ===============================================================================
         * ========================= REQUÈTES DE RADIATION ===============================
         * ===============================================================================
         */

        /**
         * Retourne la requête de mise à jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getUpdateCotisationsPoste(String numAff, String dateFin, AFCotisationViewBean vbCotisation) {
            StringBuilder sql = new StringBuilder();
            String idCotisation = vbCotisation.getId();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES cotiPostes SET cotiPostes.DATE_FIN=").append(dateFin);
            sql.append(" WHERE (cotiPostes.DATE_FIN = 0 OR cotiPostes.DATE_FIN is null or cotiPostes.DATE_FIN > ")
                    .append(dateFin);
            sql.append(" ) AND cotiPostes.ID_PT_POSTES_TRAVAILS IN (");
            sql.append(" SELECT postes.ID FROM SCHEMA.PT_POSTES_TRAVAILS postes WHERE ID_AFAFFIP = ").append(numAff);
            sql.append(" ) AND cotiPostes.ID_AFCOTIP =").append(idCotisation);
            return sql.toString();
        }

        /*
         * ===============================================================================
         * ========================= REQUÈTES DE DERADIATION =============================
         * ===============================================================================
         */

        /**
         * Retourne la requête de mise à jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getDeradiationCotisationsPoste(String numAff, String dateFin, AFCotisation vbCotisation) {
            StringBuilder sql = new StringBuilder();
            String idCotisation = vbCotisation.getId();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES cotiPostes SET cotiPostes.DATE_FIN = 0");
            sql.append(" WHERE cotiPostes.DATE_FIN = ").append(dateFin);
            sql.append(" AND cotiPostes.ID_PT_POSTES_TRAVAILS IN (");
            sql.append(" SELECT postes.ID FROM SCHEMA.PT_POSTES_TRAVAILS postes WHERE ID_AFAFFIP = ").append(numAff);
            sql.append(" ) AND cotiPostes.ID_AFCOTIP = ").append(idCotisation);
            return sql.toString();
        }
    }
}
