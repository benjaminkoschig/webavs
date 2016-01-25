/*
 * Créé le 14 févr. 07
 */
package globaz.naos.process.statOfas;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.naos.itext.statOfas.AFStatOFASControle_Doc;

/**
 * @author mar
 * 
 */

public class AFStatOFASControleProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public AFStatOFASControleProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {

            // Création du document
            AFStatOFASControle_Doc doc = new AFStatOFASControle_Doc(getSession());

            doc.setAnnee(getAnnee());
            doc.setParentWithCopy(this);
            doc.executeProcess();
            // BProcessLauncher.start(doc);

            return true;
        } catch (Exception e) {
            abort();
            e.printStackTrace();
            return false;
        }
    }

    // protected void _validate() throws Exception {
    // setControleTransaction(true);
    // setSendCompletionMail(true);
    // setSendMailOnError(true);
    // }

    public String getAnnee() {
        return annee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_STAT_ERROR");
        } else {
            return getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_STAT");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
