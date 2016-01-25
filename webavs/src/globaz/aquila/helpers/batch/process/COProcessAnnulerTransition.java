/**
 *
 */
package globaz.aquila.helpers.batch.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.service.COServiceLocator;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;

/**
 * @author SEL
 */
public class COProcessAnnulerTransition extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private COContentieux contentieux = null;
    private boolean idExtourne = true;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Annule la dernière transition pour le contentieux courant.
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            COHistorique historiqueAnnule = getContentieux().loadLastHistorique(Boolean.FALSE, Boolean.FALSE);
            if (!annulerEtape(getContentieux(), historiqueAnnule, isIdExtourne()).booleanValue()) {
                getTransaction().addErrors(
                        "Error in annulerEtape !! " + getContentieux().getIdContentieux() + "/"
                                + getContentieux().getIdEtape());
            }
        } catch (Exception e) {
            getTransaction().addErrors(
                    getContentieux().getSession().getLabel("AQUILA_ETAPE_ANNULEE_LIBELLE_MAIL_ERROR") + e.getMessage());
        } finally {
            // remonter les erreurs dans le viewBean et fermer la transaction
            transferMessagesToViewBean(getSession(), getTransaction(), getContentieux());
        }

        return true;
    }

    /**
     * @param transaction
     * @param contentieux
     * @param historique
     * @return false s'il y a des erreurs, true sinon.
     */
    private Boolean annulerEtape(COContentieux contentieux, COHistorique historique, boolean idExtourne) {
        Boolean retour = Boolean.TRUE;
        if ((historique == null) || (historique.getEtape() == null)) {
            contentieux.setMessage(contentieux.getSession().getLabel("AQUILA_ERR_HISTORIQUE"));
            contentieux.setMsgType(FWViewBeanInterface.ERROR);
            return Boolean.FALSE;
        } else {
            if (!ICOEtape.CS_CONTENTIEUX_CREE.equals(historique.getEtape().getLibEtape())
                    && !ICOEtape.CS_AUCUNE.equals(historique.getEtape().getLibEtape())) {
                try {
                    // Met le dernier historique à Annule.
                    historique.setAnnule(new Boolean(true));
                    historique.update(getTransaction());

                    if (historique.isImpute().booleanValue()) {
                        COHistorique historiqueAnnule2 = contentieux.loadLastHistorique(Boolean.FALSE, Boolean.FALSE);
                        if (!annulerEtape(contentieux, historiqueAnnule2, idExtourne).booleanValue()) {
                            retour = Boolean.FALSE;
                        }
                    } else {
                        COServiceLocator.getCancelTransitionService().annulerDerniereTransition(
                                contentieux.getSession(), getTransaction(), contentieux, historique, true, idExtourne);
                    }
                } catch (Exception e) {
                    retour = Boolean.FALSE;
                }
                // S'il y a des erreurs on retourne false
                if (getTransaction().hasErrors()) {
                    retour = Boolean.FALSE;
                }
            }
        }

        return retour;
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
     * @return the idExtourne
     */
    public boolean isIdExtourne() {
        return idExtourne;
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
     * @param contentieux
     *            the contentieux to set
     */
    public void setContentieux(COContentieux contentieux) {
        this.contentieux = contentieux;
    }

    /**
     * @param idExtourne
     *            the idExtourne to set
     */
    public void setIdExtourne(boolean idExtourne) {
        this.idExtourne = idExtourne;
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
