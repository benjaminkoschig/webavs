/*
 * Créé le 21 août 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.api.musca;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.pavo.db.inscriptions.CIJournal;

/**
 * @author JMC
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DSComptaJournauxCI extends BProcess implements IntModuleFacturation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private IFAPassage pass = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // TODO Raccord de méthode auto-généré

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        String idPassage = pass.getIdPassage();
        if (JadeStringUtil.isBlankOrZero(idPassage)) {
            return true;
        }
        DSDeclarationListViewBean declMgr = new DSDeclarationListViewBean();
        declMgr.setSession(getSession());
        declMgr.setForIdPassageFac(idPassage);
        declMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        declMgr.find(getTransaction());
        for (int i = 0; i < declMgr.size(); i++) {
            if (isAborted()) {
                break;
            }
            CIJournal journalCi = new CIJournal();
            DSDeclarationViewBean decl = (DSDeclarationViewBean) declMgr.get(i);
            if (!JadeStringUtil.isBlankOrZero(decl.getIdJournal())) {
                journalCi = new CIJournal();
                journalCi.setSession((BSession) getSessionCI(getSession()));
                journalCi.setIdJournal(decl.getIdJournal());
                journalCi.setFromFacturationCompta(true);
                journalCi.retrieve();
                if (!journalCi.isNew()) {
                    journalCi.comptabiliser("", "", getTransaction(), this);
                }
            }
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getMemoryLog().logMessage(
                        "Journal CI No " + journalCi.getIdJournal() + " " + getTransaction().getErrors().toString(),
                        FWViewBeanInterface.ERROR, "Comptabilisation des journaux CI");
                getTransaction().rollback();
            }
        }
        if (!isOnError() && !isOnError()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRecomptabiliser(globaz .musca.api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRegenerer(globaz.musca .api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrCom(globaz. musca.api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#avantRepriseErrGen(globaz. musca.api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    public boolean comptabiliseJournauxCI(IFAPassage passage, BProcess context) throws Exception {
        DSComptaJournauxCI procFacturation = new DSComptaJournauxCI();

        // copier le process parent
        procFacturation.setParentWithCopy(context);
        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setSendCompletionMail(false);
        procFacturation.setPass(passage);
        procFacturation.executeProcess();
        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#comptabiliser(globaz.musca .api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliseJournauxCI(passage, context);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.musca.external.IntModuleFacturation#generer(globaz.musca.api. IFAPassage, globaz.globall.db.BProcess)
     */
    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * @return
     */
    public IFAPassage getPass() {
        return pass;
    }

    public BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#imprimer(globaz.musca.api. IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Raccord de méthode auto-généré
        return GlobazJobQueue.UPDATE_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#recomptabiliser(globaz.musca .api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#regenerer(globaz.musca.api .IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#repriseOnErrorCompta(globaz .musca.api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {

        return comptabiliseJournauxCI(passage, context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#repriseOnErrorGen(globaz.musca .api.IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }

    /**
     * @param passage
     */
    public void setPass(IFAPassage passage) {
        pass = passage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.musca.external.IntModuleFacturation#supprimer(globaz.musca.api .IFAPassage,
     * globaz.globall.db.BProcess)
     */
    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        // TODO Raccord de méthode auto-généré
        return false;
    }
}
