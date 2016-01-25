package globaz.pavo.db.process;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.splitting.CIMandatSplitting;
import globaz.pavo.db.splitting.CIMandatSplittingManager;
import globaz.pavo.vb.CIAbstractPersistentViewBean;
import java.util.Vector;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIImprimerExtraitViewBean extends CIAbstractPersistentViewBean {
    private String idDossierSplitting = "";
    private Boolean isArchivage = new Boolean(false);
    private Vector refUnique;

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * Returns the idDossierSplitting.
     * 
     * @return String
     */
    public String getIdDossierSplitting() {
        return idDossierSplitting;
    }

    /**
     * @return
     */
    public Boolean getIsArchivage() {
        return isArchivage;
    }

    /**
     * Method loadRefUnique.
     */
    public Vector getRefUniques() {
        refUnique = new Vector();
        BITransaction remoteTransaction = null;
        try {
            // recherche des ref uniques
            BISession remoteSession = ((CIApplication) getSession().getApplication()).getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();

            //
            CIMandatSplittingManager mandats = new CIMandatSplittingManager();
            mandats.setForIdDossierSplitting(getId());
            mandats.setSession(getSession());
            mandats.find();
            for (int i = 0; i < mandats.size(); i++) {
                IHEOutputAnnonce remoteLectureAnnonce = (IHEOutputAnnonce) remoteSession
                        .getAPIFor(IHEOutputAnnonce.class);
                remoteLectureAnnonce.setMethodsToLoad(new String[] { "getRefUnique", "getInputTable" });
                CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
                if (entity.isMandatAutomatique()) {
                    remoteLectureAnnonce.setIdAnnonce(entity.getIdArc());
                    remoteLectureAnnonce.retrieve(remoteTransaction);
                    if (!remoteLectureAnnonce.isNew()) {
                        refUnique.add(remoteLectureAnnonce.getRefUnique());
                    } else {
                        remoteLectureAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
                        remoteLectureAnnonce.setMethodsToLoad(new String[] { "getRefUnique", "getInputTable" });
                        remoteLectureAnnonce.setArchivage(new Boolean(true));
                        remoteLectureAnnonce.setIdAnnonce(entity.getIdArc());
                        remoteLectureAnnonce.retrieve(remoteTransaction);
                        if (!remoteLectureAnnonce.isNew()) {
                            refUnique.add(remoteLectureAnnonce.getRefUnique());
                            isArchivage = new Boolean(true);
                        }
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return refUnique;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * Sets the idDossierSplitting.
     * 
     * @param idDossierSplitting
     *            The idDossierSplitting to set
     */
    public void setIdDossierSplitting(String idDossierSplitting) {
        this.idDossierSplitting = idDossierSplitting;
    }

    /**
     * @param boolean1
     */
    public void setIsArchivage(Boolean boolean1) {
        isArchivage = boolean1;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
