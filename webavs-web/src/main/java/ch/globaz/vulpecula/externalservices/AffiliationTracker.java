package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
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
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurService;
import ch.globaz.vulpecula.domain.models.decompte.DecomptePOJO;
import ch.globaz.vulpecula.domain.models.decompte.PosteTravailPOJO;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.ws.utils.UtilsService;

public class AffiliationTracker extends BAbstractEntityExternalServiceWithContext {
    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        Notification notification = new Notification(InfoType.NOUVELLE_AFFILIATION, entity.getId());
        requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
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

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        BSession session = UtilsService.initSession();
        AFAffiliationViewBean vbAffiliation = (AFAffiliationViewBean) entity;

        /*
         * Executé uniquement en cas de radiation
         */
        if (!vbAffiliation.getDateFin().isEmpty()) {
            try {
                JadeThreadContext threadContext = initThreadContext(session);
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                EmployeurService employeurService = VulpeculaServiceLocator.getEmployeurService();
                Employeur emp = employeurService.findByIdAffilie(entity.getId());

                DateTimeFormatter frmt = DateTimeFormat.forPattern("dd.MM.yyyy");
                String dateFin = new DateTime(frmt.parseDateTime(emp.getDateFin())).toString("yyyyMMdd");

                List<PosteTravailPOJO> postes = QueryExecutor.execute(
                        Requests.getEBusinessPosteTravail(emp.getAffilieNumero()), PosteTravailPOJO.class, session);
                if (emp.isEBusiness()) {
                    List<DecomptePOJO> decomptes = QueryExecutor.execute(
                            Requests.getEBusinessDecompteRequest(emp.getAffilieNumero()), DecomptePOJO.class, session);
                    for (DecomptePOJO dec : decomptes) {
                        VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(dec.getIdDecompte(),
                                emp.getId(), TypeDecompte.fromValue(dec.getType()));
                    }

                    for (PosteTravailPOJO poste : postes) {
                        VulpeculaServiceLocator.getTravailleurService().notifierSynchronisationEbu(
                                poste.getIdTravailleur(), poste.getCorreTra(), poste.getCorrePost(), "0");
                    }
                }
                QueryUpdateExecutor.executeUpdate(Requests.getUpdateCotisationsPoste(emp.getAffilieNumero(), dateFin), null,
                        session);
                QueryUpdateExecutor.executeUpdate(Requests.getUpdateCaissesMaladiePoste(emp.getAffilieNumero(), dateFin),
                        null, session);
                QueryUpdateExecutor.executeUpdate(Requests.getUpdatePosteTravail(emp.getAffilieNumero(), dateFin), null,
                        session);
                QueryUpdateExecutor.executeUpdate(Requests.getUpdateDecompteRequest(emp.getAffilieNumero()), null, session);

                notifyMercato(postes, InfoType.RADIATION_COTISATION_POSTE_LPP, entity.getSession());

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

        /*
         * Déradiation de l'affiliation
         */
        AFAffiliationViewBean vbAffiliation = (AFAffiliationViewBean) entity;
        if (!vbAffiliation.getDateFinSave().isEmpty() && vbAffiliation.getDateFin().isEmpty()) {
            BSession session = UtilsService.initSession();
            DateTimeFormatter frmt = DateTimeFormat.forPattern("dd.MM.YYY");
            String dateFin = frmt.parseDateTime(vbAffiliation.getDateFinSave()).toString("yyyyMMdd");

            try {

                JadeThreadContext threadContext = initThreadContext(session);
                JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
                Employeur emp = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(entity.getId());

                List<PosteTravailPOJO> postes = QueryExecutor.execute(
                        Requests.getDeradiationEBusinessPosteTravail(vbAffiliation.getAffilieNumero(), dateFin),
                        PosteTravailPOJO.class, session);

                // Synchronisations EBusiness
                if (emp.isEBusiness()) {

                    List<DecomptePOJO> decomptes = QueryExecutor.execute(
                            Requests.getDeradiationEBusinessDecompteRequest(emp.getAffilieNumero(), dateFin),
                            DecomptePOJO.class, session);
                    for (DecomptePOJO dec : decomptes) {
                        VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(dec.getIdDecompte(),
                                emp.getId(), TypeDecompte.fromValue(dec.getType()));
                    }

                    for (PosteTravailPOJO poste : postes) {
                        VulpeculaServiceLocator.getTravailleurService().notifierSynchronisationEbu(
                                poste.getIdTravailleur(), poste.getCorreTra(), poste.getCorrePost(), "0");
                    }
                }

                // Execution des requêtes de mise à jour
                QueryUpdateExecutor.executeUpdate(
                        Requests.getDeradiationUpdateCotisationsPoste(vbAffiliation.getAffilieNumero(), dateFin), null,
                        session);
                QueryUpdateExecutor.executeUpdate(
                        Requests.getDeriadiationUpdateCaissesMaladiePoste(vbAffiliation.getAffilieNumero(), dateFin),
                        null, session);
                QueryUpdateExecutor.executeUpdate(
                        Requests.getDeradiationUpdatePosteTravail(vbAffiliation.getAffilieNumero(), dateFin), null,
                        session);
                QueryUpdateExecutor.executeUpdate(
                        Requests.getDeradiationUpdateDecompteRequest(vbAffiliation.getAffilieNumero(), dateFin), null,
                        session);

                notifyMercato(postes, InfoType.AJOUT_COTISATION_POSTE_LPP, entity.getSession());

            } catch (Exception e) {
                session.addError(e.getMessage());
                JadeLogger.error(e, e.getMessage());
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }
        }
    }

    private void notifyMercato(List<PosteTravailPOJO> postes, InfoType ajoutCotisationPosteLpp, BSession bSession) {
        List<String> cotisationLppIds = new ArrayList<String>();
        for (PosteTravailPOJO poste : postes) {
            List<AdhesionCotisationPosteTravail> listAdhesion = VulpeculaRepositoryLocator
                    .getAdhesionCotisationPosteRepository().findByIdPosteTravail(poste.getIdPost());
            for (AdhesionCotisationPosteTravail adhesion : listAdhesion) {
                if (adhesion.isLPP()) {
                    cotisationLppIds.add(adhesion.getId());
                }

            }
        }
        for (String idLpp : cotisationLppIds) {
            Notification notification = new Notification(ajoutCotisationPosteLpp, idLpp);
            requestFactory.persistFromAnciennePersistance(notification, bSession);
        }
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }

    private static class Requests {

        /*
         * ===============================================================================
         * ========================= REQUÈTES DE RADIATION ===============================
         * ===============================================================================
         */

        /**
         * Requête de mise à jour des décomptes
         * 
         * @return Retourne la classe permettant de mettre à jour les décomptes post-radiation
         */
        static String getUpdateDecompteRequest(String numAff) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_DECOMPTES SET CS_ETAT='68012008' ");
            sql.append("WHERE SCHEMA.PT_DECOMPTES.ID IN (");
            sql.append("SELECT dec.ID FROM SCHEMA.PT_DECOMPTES dec ");
            sql.append("INNER JOIN SCHEMA.PT_EMPLOYEURS emp ON emp.ID_AFAFFIP = dec.ID_PT_EMPLOYEURS ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON aff.MAIAFF = emp.ID_AFAFFIP ");
            sql.append("WHERE aff.MALNAF = '").append(numAff).append("' ");
            sql.append("AND dec.CS_ETAT IN (68012001, 68012002, 68012003, 68012012) ");
            sql.append("AND aff.MADFIN > 0 ");
            sql.append("AND CAST(SUBSTR(CAST(aff.MADFIN AS VARCHAR(6)),1,6) AS INTEGER) < dec.PERIODE_DEBUT)");

            return sql.toString();
        }

        /**
         * Requête de synchronisation des décomptes
         * 
         * @return Retourne les décomptes à mettre à jour après radiation d'une affiliation
         */
        static String getEBusinessDecompteRequest(String numAff) {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT dec.ID as idDecompte, dec.CS_TYPE as type ");
            sql.append("FROM SCHEMA.PT_DECOMPTES dec ");
            sql.append("INNER JOIN SCHEMA.PT_EMPLOYEURS emp ON emp.ID_AFAFFIP = dec.ID_PT_EMPLOYEURS ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON aff.MAIAFF = emp.ID_AFAFFIP ");
            sql.append("WHERE aff.MALNAF = '").append(numAff).append("' ");
            sql.append("AND dec.CS_ETAT IN (68012001, 68012002, 68012003, 68012012) ");
            sql.append("AND aff.MADFIN > 0 ");
            sql.append("AND CAST(SUBSTR(CAST(aff.MADFIN AS VARCHAR(6)),1,6) AS INTEGER)< dec.PERIODE_DEBUT ");

            return sql.toString();
        }

        /**
         * Retourne la requête de mise à jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getUpdateCotisationsPoste(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES SET DATE_FIN=").append(dateFin)
                    .append(" WHERE ID IN (");
            sql.append("SELECT adcoti.ID as cotiId FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("INNER JOIN SCHEMA.PT_ADHESIONS_COTIS_POSTES adcoti ON post.ID = adcoti.ID_PT_POSTES_TRAVAILS ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND (post.DATE_FIN_ACTIVITE = 0 OR post.DATE_FIN_ACTIVITE >= aff.MADFIN) ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND (adcoti.DATE_FIN = 0 OR adcoti.DATE_FIN >= aff.MADFIN))");

            return sql.toString();
        }

        /**
         * Mise à jour des caisses maladies liées au poste de travail.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getUpdateCaissesMaladiePoste(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_CAISSES_MALADIES SET MOIS_FIN = SUBSTR('").append(dateFin)
                    .append("',1,6) WHERE ID IN (");
            sql.append("SELECT cai.ID FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("INNER JOIN SCHEMA.PT_CAISSES_MALADIES cai ON cai.ID_PT_POSTES_TRAVAILS = post.ID ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND (post.DATE_FIN_ACTIVITE = 0 OR post.DATE_FIN_ACTIVITE >= aff.MADFIN) ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND(cai.MOIS_FIN = 0 OR cai.MOIS_FIN > SUBSTR(aff.MADFIN,1,6)))");

            return sql.toString();
        }

        /**
         * Permet de mettre à jour les postes de travail pour une affiliation radiée
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getUpdatePosteTravail(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_POSTES_TRAVAILS SET DATE_FIN_ACTIVITE = ").append(dateFin)
                    .append(" WHERE ID IN (");
            sql.append("SELECT post.ID FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND (post.DATE_FIN_ACTIVITE = 0 OR post.DATE_FIN_ACTIVITE > aff.MADFIN)) ");

            return sql.toString();
        }

        /**
         * Permet de ressortir tous les postes de travail pour une affiliation.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @return
         */
        static String getEBusinessPosteTravail(String numAff) {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT tra.ID AS IDTRAVAILLEUR, tra.CORRELATION_ID CORRETRA, post.POSTE_CORRELATION_ID as CORREPOST, post.ID as IDPOST ");
            sql.append("FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("INNER JOIN SCHEMA.PT_TRAVAILLEURS tra ON post.ID_PT_TRAVAILLEURS = tra.ID ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND (post.DATE_FIN_ACTIVITE = 0 OR post.DATE_FIN_ACTIVITE >= aff.MADFIN) ");
            return sql.toString();
        }

        /*
         * ===============================================================================
         * ========================= REQUÈTES DE DERADIATION =============================
         * ===============================================================================
         */

        /**
         * Requête de mise à jour des décomptes
         * 
         * @return Retourne la classe permettant de mettre à jour les décomptes post-radiation
         */
        static String getDeradiationUpdateDecompteRequest(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_DECOMPTES SET CS_ETAT='68012002' ");
            sql.append("WHERE SCHEMA.PT_DECOMPTES.ID IN (");
            sql.append("SELECT dec.ID FROM SCHEMA.PT_DECOMPTES dec ");
            sql.append("INNER JOIN SCHEMA.PT_EMPLOYEURS emp ON emp.ID_AFAFFIP = dec.ID_PT_EMPLOYEURS ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON aff.MAIAFF = emp.ID_AFAFFIP ");
            sql.append("WHERE aff.MALNAF = '").append(numAff).append("' ");
            sql.append("AND dec.CS_ETAT = 68012008 ");
            sql.append("AND dec.PERIODE_FIN > SUBSTR(").append(dateFin).append(",1,6))");

            return sql.toString();
        }

        /**
         * Requête de synchronisation des décomptes
         * 
         * @return Retourne les décomptes à mettre à jour après radiation d'une affiliation
         */
        static String getDeradiationEBusinessDecompteRequest(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT dec.ID as idDecompte, dec.CS_TYPE as type ");
            sql.append("FROM SCHEMA.PT_DECOMPTES dec ");
            sql.append("INNER JOIN SCHEMA.PT_EMPLOYEURS emp ON emp.ID_AFAFFIP = dec.ID_PT_EMPLOYEURS ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON aff.MAIAFF = emp.ID_AFAFFIP ");
            sql.append("WHERE aff.MALNAF = '").append(numAff).append("' ");
            sql.append("AND dec.CS_ETAT = 68012008 ");
            sql.append("AND dec.PERIODE_FIN > SUBSTR(").append(dateFin).append(",1,6)");

            return sql.toString();
        }

        /**
         * Retourne la requête de mise à jour des cotisations pour les postes de travail d'une affiliation.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getDeradiationUpdateCotisationsPoste(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_ADHESIONS_COTIS_POSTES SET DATE_FIN=0 WHERE ID IN (");
            sql.append("SELECT adcoti.ID as cotiId FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("INNER JOIN SCHEMA.PT_ADHESIONS_COTIS_POSTES adcoti ON post.ID = adcoti.ID_PT_POSTES_TRAVAILS ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND post.DATE_FIN_ACTIVITE = ").append(dateFin).append(" ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND adcoti.DATE_FIN = ").append(dateFin).append(")");

            return sql.toString();
        }

        /**
         * Mise à jour des caisses maladies liées au poste de travail.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getDeriadiationUpdateCaissesMaladiePoste(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_CAISSES_MALADIES SET MOIS_FIN = 0 WHERE ID IN (");
            sql.append("SELECT cai.ID FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("INNER JOIN SCHEMA.PT_CAISSES_MALADIES cai ON cai.ID_PT_POSTES_TRAVAILS = post.ID ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND post.DATE_FIN_ACTIVITE = ").append(dateFin).append(" ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND cai.MOIS_FIN = SUBSTR(").append(dateFin).append(",1,6))");

            return sql.toString();
        }

        /**
         * Permet de mettre à jour les postes de travail pour une affiliation radiée
         * 
         * @param numAff Numéro d'affilier concerné.
         * @param dateFin Date de fin d'affiliation
         * @return
         */
        static String getDeradiationUpdatePosteTravail(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE SCHEMA.PT_POSTES_TRAVAILS SET DATE_FIN_ACTIVITE = 0 WHERE ID IN (");
            sql.append("SELECT post.ID FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND post.DATE_FIN_ACTIVITE = ").append(dateFin).append(")");

            return sql.toString();
        }

        /**
         * Permet de ressortir tous les postes de travail pour une affiliation.
         * 
         * @param numAff Numéro d'affilier concerné.
         * @return
         */
        static String getDeradiationEBusinessPosteTravail(String numAff, String dateFin) {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT tra.ID AS IDTRAVAILLEUR, tra.CORRELATION_ID CORRETRA, post.POSTE_CORRELATION_ID as CORREPOST, post.ID as IDPOST FROM SCHEMA.PT_POSTES_TRAVAILS post ");
            sql.append("INNER JOIN SCHEMA.AFAFFIP aff ON post.ID_AFAFFIP = aff.MAIAFF ");
            sql.append("INNER JOIN SCHEMA.PT_TRAVAILLEURS tra ON post.ID_PT_TRAVAILLEURS = tra.ID ");
            sql.append("WHERE aff.MADFIN <> 0 ");
            sql.append("AND aff.MALNAF='").append(numAff).append("' ");
            sql.append("AND post.DATE_FIN_ACTIVITE = ").append(dateFin).append(" ");
            return sql.toString();
        }
    }

}
