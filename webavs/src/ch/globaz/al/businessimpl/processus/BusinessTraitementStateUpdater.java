package ch.globaz.al.businessimpl.processus;

import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import java.sql.SQLException;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Thread s'occupant de mettre à jour l'état d'un traitement métier
 * 
 * @author GMO
 * 
 */
class BusinessTraitementStateUpdater implements Runnable {

    /**
     * Méthode mettant à jour l'état d'un traitement via un nouveau Thread
     * 
     * @param idTraitement
     *            id du traitement à mettre à jour
     * @param csNewState
     *            nouvel état du traitement
     * @throws JadeApplicationException
     *             levée si l'état n'a pas pu être mis à jour
     */
    static void updateState(String idTraitement, String csNewState) throws JadeApplicationException {
        BusinessTraitementStateUpdater businessTraitementStateUpdater = new BusinessTraitementStateUpdater();
        businessTraitementStateUpdater.setNewState(idTraitement, csNewState);
        businessTraitementStateUpdater.setCallerContext(JadeThread.currentContext().getContext());
        Thread myThread = new Thread(businessTraitementStateUpdater);
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            throw new ALProcessusException(
                    "BusinessTraitementStateUpdater#updateState : unable to update state, thread interrupted", e);
        }
    }

    /**
     * contexte appelant
     */
    private JadeContext callerCtxt = null;
    /**
     * id du traitement à mettre à jour
     */
    private String idTraitementToUpdate = null;
    /**
     * état du traitement
     */
    private String newState = null;

    /**
     * Méthode créant un contexte
     * 
     * @return JadeContexte
     */
    private JadeContext createContext() {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(callerCtxt.getApplicationId());
        ctxtImpl.setLanguage(callerCtxt.getLanguage());
        ctxtImpl.setUserEmail(callerCtxt.getUserEmail());
        ctxtImpl.setUserId(callerCtxt.getUserId());
        ctxtImpl.setUserName(callerCtxt.getUserName());
        context = new JadeThreadContext(ctxtImpl);
        return context.getContext();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            JadeThreadActivator.startUsingJdbcContext(this, createContext());

            TraitementPeriodiqueModel traitementToUpdate = ALServiceLocator.getTraitementPeriodiqueModelService().read(
                    idTraitementToUpdate);
            traitementToUpdate.setEtat(newState);
            ALServiceLocator.getTraitementPeriodiqueModelService().update(traitementToUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    /**
     * Définit le contexte appelant
     * 
     * @param ctxt
     *            Contexte à utiliser
     */
    private void setCallerContext(JadeContext ctxt) {
        callerCtxt = ctxt;
    }

    /**
     * Définit l'état du traitement ainsi que le traitement à mettre à jour
     * 
     * @param idTraitement
     *            id du traitement à mettre à jour
     * @param csNewState
     *            code système du nouvel état
     */
    private void setNewState(String idTraitement, String csNewState) {
        idTraitementToUpdate = idTraitement;
        newState = csNewState;
    }

}
