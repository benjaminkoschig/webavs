/*
 * Créé le 17 nov. 06
 */
package globaz.ij.helpers.process;

import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.ij.db.decisions.IJDecisionIJAI;
import globaz.ij.vb.process.IJValiderDecisionViewBean;
import globaz.jade.log.JadeLogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class IJValiderDecisionHelper extends FWHelper {

    private static final Class[] PARAMS = new Class[] { FWViewBeanInterface.class, FWAction.class, BSession.class };

    public static final String SET_SESSION = "setSession";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            IJDecisionIJAI decision = new IJDecisionIJAI();
            decision.setSession((BSession) session);
            decision.setIdDecision(((IJValiderDecisionViewBean) viewBean).getIdDecision());
            decision.retrieve(transaction);

            ((IJValiderDecisionViewBean) viewBean).setDateDecision(decision.getDateDecision());
            ((IJValiderDecisionViewBean) viewBean).setDateSurDocument(decision.getDateSurDocument());
            ((IJValiderDecisionViewBean) viewBean).setDateEnvoiSedex(decision.getDateEnvoiSedex());
            ((IJValiderDecisionViewBean) viewBean).setDateMiseEnGed(decision.getDateMiseEnGed());
            ((IJValiderDecisionViewBean) viewBean).setCsEtatMiseEnGed(decision.getCsEtatMiseEnGed());
            ((IJValiderDecisionViewBean) viewBean).setCsEtatSEDEX(decision.getCsEtatSEDEX());
            ((IJValiderDecisionViewBean) viewBean).setIsDecisionValidee(decision.getIsDecisionValidee());
            ((IJValiderDecisionViewBean) viewBean).setEMailAddress(decision.getEmailAdresse());

            if (transaction.hasErrors()) {
                transaction.setRollbackOnly();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        BTransaction transaction = null;

        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();
            IJValiderDecisionViewBean vb = (IJValiderDecisionViewBean) viewBean;
            if (vb.getIsValiderDecision() != null && vb.getIsValiderDecision().booleanValue()) {
                IJDecisionIJAI dec = new IJDecisionIJAI();
                dec.setSession((BSession) session);
                dec.setIdDecision(vb.getIdDecision());
                dec.retrieve(transaction);
                if (dec.isNew()) {
                    throw new Exception("Decision not found. Id = " + dec.getIdDecision());
                }
                dec.setIsDecisionValidee(Boolean.TRUE);
                dec.setDateDecision(JACalendar.todayJJsMMsAAAA());
                dec.update(transaction);

                // On lance l'impression, avec les paramètres stockés en DB.....
                doImprimerDecision(dec);

            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        try {
                            transaction.rollback();
                        } catch (Exception e) {
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                            viewBean.setMessage(e.toString());
                        }
                    } else {
                        try {
                            transaction.commit();
                        } catch (Exception e) {
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                            viewBean.setMessage(e.toString());
                        }
                    }
                } finally {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(e.toString());
                    }
                }
            }
        }
    }

    /**
     * recherche une méthode PUBLIQUE de cette classe qui porte le nom de la partie action de l'instance 'action' et
     * prend les mêmes paramètres que cette méthode sauf pour BSession a la place de BISession et l'invoke.
     * 
     * <p>
     * le tout est fait dans un bloc try...catch, il est donc possible de lancer une exception dans ces méthodes. Cette
     * méthode retourne ensuite le viewBean retourné par ladite méthode ou celui transis en argument si la méthode n'a
     * pas de return value.
     * </p>
     * 
     * <p>
     * si la partie action contient la chaine SET_ACTION, la session est simplement settee dans le viewBean et celui-ci
     * est retourné.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ClassCastException
     *             si session n'est pas une instance de BSession
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    protected FWViewBeanInterface deleguerExecute(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws ClassCastException {
        if (SET_SESSION.equals(action.getActionPart())) {
            viewBean.setISession(session);

            return viewBean;
        } else {
            try {
                Method method = getClass().getMethod(action.getActionPart(), PARAMS);
                Object retValue = method.invoke(this, new Object[] { viewBean, action, (BSession) session });

                if ((retValue != null) && (retValue instanceof FWViewBeanInterface)) {
                    return (FWViewBeanInterface) retValue;
                }
            } catch (InvocationTargetException ie) {
                if (ie.getTargetException() != null) {
                    ((BSession) session).addError(ie.getTargetException().getMessage());
                } else {
                    JadeLogger.error(ie, ie.getMessage());
                    ((BSession) session).addError(ie.getMessage());
                }
            } catch (Exception e) {
                JadeLogger.error(e, e.getMessage());
                ((BSession) session).addError(e.getMessage());
            }

            return viewBean;
        }
    }

    private void doImprimerDecision(IJDecisionIJAI decision) throws Exception {

        // on enregistre les scalbleProperties du document
        CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();
        ICTScalableDocumentProperties scalableProperties = factory.createNewScalableDocumentProperties();

        scalableProperties.setParameter("idPrononce", decision.getIdPrononce());
        // scalableProperties.setParameter("idDemande", ???);
        scalableProperties.setParameter("eMailAddress", decision.getEmailAdresse());
        scalableProperties.setParameter("dateSurDocument", decision.getDateSurDocument());
        scalableProperties.setParameter("idTierAdresseCourrier", decision.getIdTiersAdrCourrier());
        scalableProperties.setParameter("beneficiaire", decision.getBeneficiaire());
        scalableProperties.setParameter("idTierAdressePaiement", decision.getIdTiersAdrPmt());
        scalableProperties.setParameter("idPersonneReference", decision.getIdPersonneReference());
        scalableProperties.setParameter("cantonTauxImposition", decision.getCsCantonTauxImposition());
        scalableProperties.setParameter("tauxImposition", decision.getTauxImposition());
        scalableProperties.setParameter("remarque", decision.getRemarques());
        if (decision.getIsSendToGed() != null && decision.getIsSendToGed().booleanValue()) {
            scalableProperties.setParameter("isSentToGed", "true");
            scalableProperties.setParameter("isSendToGed", "true");
        } else {
            scalableProperties.setParameter("isSentToGed", "false");
            scalableProperties.setParameter("isSendToGed", "false");
        }
        scalableProperties.setParameter("garantitRevision", decision.getNoRevAGarantir());

        // on cherche l'id du tiers principal
        scalableProperties.setIdTiersPrincipal(decision.getIdTiersPrincipal());
        // TODO Prendre le bon document (Decision IJ) depuis IJ.properties
        scalableProperties.setIdDocument("14000000001");

        /*
         * // les parametres du premier ecran ((ICTScalableDocument)viewBean).setScalableDocumentProperties
         * (scalableProperties); ((ICTScalableDocument)viewBean).setEMailAddress( request.getParameter("eMailAddress"));
         * 
         * // pour la navigation ((ICTScalableDocument)viewBean).setWantSelectParagraph(false);
         * ((ICTScalableDocument)viewBean).setWantEditParagraph(false);
         * ((ICTScalableDocument)viewBean).setWantSelectAnnexeCopie(true);
         */
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

}
