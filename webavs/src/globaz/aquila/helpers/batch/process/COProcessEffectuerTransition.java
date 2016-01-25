/**
 *
 */
package globaz.aquila.helpers.batch.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxFactory;
import globaz.aquila.db.batch.COTransitionViewBean;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.util.COActionUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import java.util.List;

/**
 * @author SEL
 */
public class COProcessEffectuerTransition extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private COTransitionAction action = null;
    private COContentieux contentieux = null;
    private List fraisEtInterets = null;
    private String idContentieux = "";
    private String idEtapeSuivante = "";
    private List<CAInteretManuelVisualComponent> interetCalcule = null;
    private String libSequence = "";

    private Boolean refresh;

    private String selectedId = "";
    private List taxes = null;
    private COTransitionViewBean viewBean = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        COContentieux contentieux = chargerContentieux();
        // Si on veut effectuer une transition

        try {
            // Recherche la transition à effectuer
            if (action != null) {
                try {
                    if (ICOEtape.CS_ETAPE_MANUELLE.equals(action.getTransition().getEtapeSuivante().getLibEtape())) {
                        action.canExecute(contentieux, getTransaction(), false);
                    } else {
                        action.canExecute(contentieux, getTransaction());
                    }
                } catch (COTransitionException e) {
                    // Ne pas bloquer une étape lancée manuellement si le solde
                    // est minime
                    if ((e.getMessageId() != null) && !e.getMessageId().equals("SEUIL_MINIMAL_INFERIEUR")) {
                        throw e;
                    }
                }

                // on sauve le contentieux s'il n'existe pas encore
                if (COContentieux.ID_CONTENTIEUX_BIDON.equals(contentieux.getIdContentieux())) {
                    // HACK: dans le cas de la création d'un nouveau
                    // contentieux, on remplace les dates
                    contentieux.setDateExecution(action.getDateExecution());
                    contentieux.setDateOuverture(action.getDateExecution());
                    contentieux.add(getTransaction());

                    /*
                     * pour éviter que l'on se trompe une prochaine fois en pensant que le contentieux a été ajouté
                     * alors que ce n'est pas le cas
                     */
                    if (contentieux.getSession().hasErrors()) {
                        contentieux.setIdContentieux(COContentieux.ID_CONTENTIEUX_BIDON);
                    }
                }

                // si le contentieux devait être sauvé et l'a été sans erreur,
                // on exécute l'action
                if (!getTransaction().hasErrors() && !contentieux.getSession().hasErrors()) {
                    action.setTaxes(getTaxes());
                    action.setFrais(getFraisEtInterets());
                    action.setInteretCalcule(getInteretCalcule());

                    COServiceLocator.getTransitionService().executerTransition(getSession(), getTransaction(),
                            contentieux, getViewBean(), action);
                }
            }
        } catch (Exception e) {
            String message = e.getMessage();

            if (e instanceof COTransitionException) {
                // si c'est une COTransitionException, le message est un label
                message = COActionUtils.getMessage(getSession(), (COTransitionException) e);
            }

            // s'il n'y a pas de message, on utilise le nom de l'exception
            if (JadeStringUtil.isEmpty(message)) {
                message = e.toString();
            }

            getTransaction().addErrors(message);
        }

        // Rattache les messages au viewBean et fermer la transaction
        transferMessagesToViewBean(getSession(), getTransaction(), contentieux);

        return false;
    }

    /**
     * si les parametres 'libSequence' et 'selectedId' se trouvent dans la requete et son différent de ceux du
     * contentieux en session, charge le contentieux correspondant, sinon retourne le contentieux se trouvant dans la
     * session http.
     * 
     * @return un viewBean de contentieux
     */
    protected COContentieux chargerContentieux() {
        COContentieux contentieux = getContentieux();
        String selectedId = getSelectedId();

        if (shouldReloadContentieux()) {
            /*
             * on remplace le contentieux en session qui peut ne pas être le bon.
             */
            try {
                contentieux = COContentieuxFactory.loadContentieuxViewBean(getSession(), selectedId);
                COServiceLocator.getTaxeService().initMontantsTaxes(getSession(), contentieux);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // renseigner la session si ce n'est pas le cas
        if (contentieux.getSession() == null) {
            contentieux.setSession(getSession());
        }

        return contentieux;
    }

    /**
     * @return the action
     */
    public COTransitionAction getAction() {
        return action;
    }

    /**
     * @return the contentieux
     */
    public COContentieux getContentieux() {
        return contentieux;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * @return the fraisEtInterets
     */
    public List getFraisEtInterets() {
        return fraisEtInterets;
    }

    /**
     * @return the idContentieux
     */
    public String getIdContentieux() {
        return idContentieux;
    }

    /**
     * @return the idEtapeSuivante
     */
    public String getIdEtapeSuivante() {
        return idEtapeSuivante;
    }

    /**
     * @return the interetCalcule
     */
    public List<CAInteretManuelVisualComponent> getInteretCalcule() {
        return interetCalcule;
    }

    /**
     * @return the libSequence
     */
    public String getLibSequence() {
        return libSequence;
    }

    /**
     * @return the refresh
     */
    public Boolean getRefresh() {
        return refresh;
    }

    /**
     * @return the selectedId
     */
    public String getSelectedId() {
        return selectedId;
    }

    /**
     * @return the taxes
     */
    public List getTaxes() {
        return taxes;
    }

    /**
     * @return the viewBean
     */
    public COTransitionViewBean getViewBean() {
        return viewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(COTransitionAction action) {
        this.action = action;
    }

    /**
     * @param contentieux
     *            the contentieux to set
     */
    public void setContentieux(COContentieux contentieux) {
        this.contentieux = contentieux;
    }

    /**
     * @param fraisEtInterets
     *            the fraisEtInterets to set
     */
    public void setFraisEtInterets(List fraisEtInterets) {
        this.fraisEtInterets = fraisEtInterets;
    }

    /**
     * @param idContentieux
     *            the idContentieux to set
     */
    public void setIdContentieux(String idContentieux) {
        this.idContentieux = idContentieux;
    }

    /**
     * @param idEtapeSuivante
     *            the idEtapeSuivante to set
     */
    public void setIdEtapeSuivante(String idEtapeSuivante) {
        this.idEtapeSuivante = idEtapeSuivante;
    }

    /**
     * @param interetCalcule
     *            the interetCalcule to set
     */
    public void setInteretCalcule(List<CAInteretManuelVisualComponent> interetCalcule) {
        this.interetCalcule = interetCalcule;
    }

    /**
     * @param libSequence
     *            the libSequence to set
     */
    public void setLibSequence(String libSequence) {
        this.libSequence = libSequence;
    }

    /**
     * @param refresh
     *            the refresh to set
     */
    public void setRefresh(Boolean refresh) {
        this.refresh = refresh;
    }

    /**
     * @param selectedId
     *            the selectedId to set
     */
    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @param taxes
     *            the taxes to set
     */
    public void setTaxes(List taxes) {
        this.taxes = taxes;
    }

    /**
     * @param vb
     *            the viewBean to set
     */
    public void setViewBean(COTransitionViewBean vb) {
        viewBean = vb;
    }

    /**
     * Retourne vrai si le contentieux en session devrait être rechargé en fonction de ce qui se trouve dans la requête.
     * 
     * @return
     */
    protected boolean shouldReloadContentieux() {
        if ((getContentieux() == null) || getRefresh().booleanValue()
                || !JadeStringUtil.equals(getSelectedId(), getContentieux().getIdContentieux(), false)
                || !JadeStringUtil.equals(getLibSequence(), getContentieux().getLibSequence(), false)) {
            return true;
        }

        return false;
    }

    /**
     * Transfére les messages de la session et de la transaction vers le viewBean en prenant garde de conserver les
     * messages déja existants.
     * 
     * @param session
     *            la session
     * @param transaction
     *            la transaction depuis laquelle récupérer les messages
     * @param entity
     *            le viewBean dans lequel stocker les messages
     */
    private void transferMessagesToViewBean(BSession session, BTransaction transaction, BEntity entity) {
        if (!session.hasErrors() && !transaction.hasErrors()) {
            return;
        }

        // on copie les erreurs depuis la session et la transaction dans le
        // viewBean
        StringBuffer errors = new StringBuffer(entity.getMessage());

        if (errors.length() > 0) {
            errors.append("\n");
        }

        if (session.hasErrors()) {
            errors.append(session.getErrors().toString());
            errors.append("\n");
        }

        if (transaction.hasErrors()) {
            errors.append(transaction.getErrors().toString());
        }

        entity.setMessage(errors.toString());
        entity.setMsgType(FWViewBeanInterface.ERROR);
    }

}
