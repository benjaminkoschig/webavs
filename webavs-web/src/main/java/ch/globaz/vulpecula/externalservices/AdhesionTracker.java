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
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionViewBean;
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

public class AdhesionTracker extends BAbstractEntityExternalServiceWithContext {
    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        BSession session = UtilsService.initSession();
        AFAdhesionViewBean vbAdhesion = (AFAdhesionViewBean) entity;
        AFAdhesion vbOldAdhesion = vbAdhesion.getOldAdhesion();

        /*
         * Execut� uniquement en cas de radiation
         */
        if (vbAdhesion != null) {
            try {
                JadeThreadContext threadContext = initThreadContext(session);
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                DateTimeFormatter frmt = DateTimeFormat.forPattern("dd.MM.yyyy");
                String dateFin = "";
                // Radiation
                if (!vbAdhesion.getDateFin().isEmpty()) {
                    dateFin = new DateTime(frmt.parseDateTime(vbAdhesion.getDateFin())).toString("yyyyMMdd");
                    QueryUpdateExecutor.executeUpdate(
                            Requests.getUpdateCotisationsPoste(vbAdhesion.getAffiliationId(), dateFin, vbAdhesion),
                            null, session);
                    QueryUpdateExecutor.executeUpdate(
                            Requests.getUpdateCotisationsPosteLpp(vbAdhesion.getAffiliationId(), dateFin, vbAdhesion),
                            null, session);
                    executeUpdateDecomptesPoste(vbAdhesion.getAffiliationId(), dateFin);
                    notifyMercato(vbAdhesion.getAffiliationId(), InfoType.RADIATION_COTISATION_POSTE_LPP, dateFin,
                            entity.getSession());
                }
                // Deradiation
                else if (vbOldAdhesion != null && !vbOldAdhesion.getDateFin().isEmpty()) {
                    dateFin = new DateTime(frmt.parseDateTime(vbOldAdhesion.getDateFin())).toString("yyyyMMdd");
                    QueryUpdateExecutor.executeUpdate(Requests.getDeradiationCotisationsPosteLpp(
                            vbAdhesion.getAffiliationId(), dateFin, vbAdhesion), null, session);
                    QueryUpdateExecutor
                            .executeUpdate(Requests.getDeradiationCotisationsPoste(vbAdhesion.getAffiliationId(),
                                    dateFin, vbAdhesion), null, session);
                    executeUpdateDecomptesPoste(vbAdhesion.getAffiliationId(), dateFin);
                }
            } catch (Exception e) {
                session.addError(e.getMessage());
                JadeLogger.error(e, e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }
        }
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
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
         * ========================= REQU�TES DE RADIATION ===============================
         * ===============================================================================
         */

        /**
         * Retourne la requ�te de mise � jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Num�ro d'affilier concern�.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getUpdateCotisationsPoste(String numAff, String dateFin, AFAdhesionViewBean vbAdhesion) {
            StringBuilder sql = new StringBuilder();
            String idAdhesion = vbAdhesion.getAdhesionId();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES cotiPostes SET cotiPostes.DATE_FIN=").append(dateFin);
            sql.append(" WHERE (cotiPostes.DATE_FIN = 0 OR cotiPostes.DATE_FIN is null or cotiPostes.DATE_FIN > ")
                    .append(dateFin);
            sql.append(" ) AND cotiPostes.ID_PT_POSTES_TRAVAILS IN (");
            sql.append(" SELECT postes.ID FROM SCHEMA.PT_POSTES_TRAVAILS postes WHERE ID_AFAFFIP = ").append(numAff);
            sql.append(" ) AND cotiPostes.ID_AFCOTIP IN (");
            sql.append(" SELECT MEICOT FROM SCHEMA.AFCOTIP");
            sql.append(" WHERE MRIADH = ").append(idAdhesion);
            sql.append(" )");
            return sql.toString();
        }

        /**
         * Retourne la requ�te de mise � jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Num�ro d'affilier concern�.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getUpdateCotisationsPosteLpp(String numAff, String dateFin, AFAdhesionViewBean vbAdhesion) {
            StringBuilder sql = new StringBuilder();
            String idAdhesion = vbAdhesion.getAdhesionId();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES cotiPostes SET cotiPostes.DATE_FIN=").append(dateFin);
            sql.append(" WHERE (cotiPostes.DATE_FIN = 0 OR cotiPostes.DATE_FIN is null or cotiPostes.DATE_FIN > ")
                    .append(dateFin);
            sql.append(" ) AND cotiPostes.ID_PT_POSTES_TRAVAILS IN (");
            sql.append(" SELECT postes.ID FROM SCHEMA.PT_POSTES_TRAVAILS postes WHERE ID_AFAFFIP = ").append(numAff);
            sql.append(" ) AND cotiPostes.ID_AFCOTIP IN (");
            sql.append(" SELECT MEICOT FROM SCHEMA.AFCOTIP");
            sql.append(" INNER JOIN WEBAVSS.AFASSUP ON SCHEMA.AFCOTIP.MBIASS=WEBAVSS.AFASSUP.MBIASS");
            sql.append(" INNER JOIN WEBAVSS.CARUBRP ON SCHEMA.AFASSUP.MBIRUB = WEBAVSS.CARUBRP.IDRUBRIQUE");
            sql.append(" WHERE INACTIVE='2' AND MUIPLA=7284 AND MSIPLC=1001");
            sql.append(" AND MRIADH = ").append(idAdhesion);
            sql.append(" )");
            return sql.toString();
        }

        /*
         * ===============================================================================
         * ========================= REQU�TES DE DERADIATION =============================
         * ===============================================================================
         */

        /**
         * Retourne la requ�te de mise � jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Num�ro d'affilier concern�.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getDeradiationCotisationsPoste(String numAff, String dateFin, AFAdhesionViewBean vbAdhesion) {
            StringBuilder sql = new StringBuilder();
            String idAdhesion = vbAdhesion.getAdhesionId();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES cotiPostes SET cotiPostes.DATE_FIN = 0");
            sql.append(" WHERE cotiPostes.DATE_FIN = ").append(dateFin);
            sql.append(" AND cotiPostes.ID_PT_POSTES_TRAVAILS IN (");
            sql.append(" SELECT postes.ID FROM SCHEMA.PT_POSTES_TRAVAILS postes WHERE ID_AFAFFIP = ").append(numAff);
            sql.append(" ) AND cotiPostes.ID_AFCOTIP IN (");
            sql.append(" SELECT MEICOT FROM SCHEMA.AFCOTIP");
            sql.append(" WHERE MRIADH = ").append(idAdhesion);
            sql.append(" )");

            return sql.toString();
        }

        /**
         * Retourne la requ�te de mise � jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Num�ro d'affilier concern�.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getDeradiationCotisationsPosteLpp(String numAff, String dateFin, AFAdhesionViewBean vbAdhesion) {
            StringBuilder sql = new StringBuilder();
            String idAdhesion = vbAdhesion.getAdhesionId();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES cotiPostes SET cotiPostes.DATE_FIN = 0");
            sql.append(" WHERE cotiPostes.DATE_FIN = ").append(dateFin);
            sql.append(" AND cotiPostes.ID_PT_POSTES_TRAVAILS IN (");
            sql.append(" SELECT postes.ID FROM SCHEMA.PT_POSTES_TRAVAILS postes WHERE ID_AFAFFIP = ").append(numAff);
            sql.append(" ) AND cotiPostes.ID_AFCOTIP IN (");
            sql.append(" SELECT MEICOT FROM SCHEMA.AFCOTIP");
            sql.append(" INNER JOIN WEBAVSS.AFASSUP ON SCHEMA.AFCOTIP.MBIASS=WEBAVSS.AFASSUP.MBIASS");
            sql.append(" INNER JOIN WEBAVSS.CARUBRP ON SCHEMA.AFASSUP.MBIRUB = WEBAVSS.CARUBRP.IDRUBRIQUE");
            sql.append(" WHERE INACTIVE='2' AND MUIPLA=7284 AND MSIPLC=1001");
            sql.append(" AND MRIADH = ").append(idAdhesion);
            sql.append(" )");

            return sql.toString();
        }
    }
}
