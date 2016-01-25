package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.avisMutation.AFAvisMutation;
import globaz.naos.db.avisMutation.AFAvisMutationManager;
import globaz.naos.itext.AFAvisMutation_Doc;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class AFAvisMutationProcess extends BProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAvisMutation_Doc _doc = null;

    public AFAvisMutationProcess() throws Exception {
        super();

    }

    public AFAvisMutationProcess(BProcess parent) throws Exception {
        super(parent);

    }

    public AFAvisMutationProcess(BSession session) throws Exception {
        super(session);
    }

    @Override
    public void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {

            AFAvisMutationManager avisMutationManager = new AFAvisMutationManager();
            avisMutationManager.setSession(getSession());
            avisMutationManager.find();
            _doc = new AFAvisMutation_Doc(getSession(), null);
            // _doc.setDeleteOnExit(true); // par defaut true;

            for (int i = 0; i < avisMutationManager.getSize(); i++) {

                AFAvisMutation avisMutation = (AFAvisMutation) avisMutationManager.getEntity(i);
                // _doc.setAvisMutation(avisMutation);

                AFAffiliation affiliation = new AFAffiliation();
                affiliation.setSession(getSession());
                affiliation.setAffiliationId(avisMutation.getAffiliationId());

                affiliation.retrieve();
                // _doc.setAffiliation(affiliation);

                String observation = "";

                /*
                 * 
                 * AFAnnonceAffilieManager annonceAffilieManager = new AFAnnonceAffilieManager();
                 * annonceAffilieManager.setSession(getSession()); annonceAffilieManager
                 * .setForAffiliationId(avisMutation.getAffiliationId()); System.
                 * out.println(">>>>>>>>>>>3:affiliationId["+avisMutation. getAffiliationId()+"]");
                 * 
                 * annonceAffilieManager.find(); System.out.println(">>>>>>>>>>>4");
                 * 
                 * for (int j=0;j<annonceAffilieManager.size();j++) {
                 * 
                 * AFAnnonceAffilie annonceAffilie = (AFAnnonceAffilie)annonceAffilieManager.getEntity(j); String
                 * annonceObservation = annonceAffilie.getObservation();
                 * 
                 * if (JAUtil.isStringEmpty(annonceObservation)) {
                 * 
                 * observation += annonceObservation+"\n"; } }
                 */

                _doc.setObservation(observation);

                // _doc.execute();
            }

            // if (_doc.getBatchList().size() == 0) {
            // throw new JAException("Aucun document disponible");
            // }
            // Tous les documents ont été créer -> creation du document finale
            // String documentPath = TransformeToPdf();
            if (super.getEMailAddress() != null) {
                // super.registerAttachedDocument(documentPath);
            }
            return true;
        } catch (JAException ex) {
            super._addError("Le document n'a pas pu être créer : " + ex.getMessage());
            super.setMsgType(super.ERROR);
            super.setMessage("Le document n'a pas pu être créer : " + ex.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void _validate() {
        setSendCompletionMail(false);
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("MSG_EMAIL_INV"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("MSG_EMAIL_INV"));
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            StringBuffer buffer = new StringBuffer("");
            buffer.append(getSession().getLabel("AVISMUT_ERREUR"));

            return buffer.toString();
        } else {
            StringBuffer buffer = new StringBuffer("");
            buffer.append(getSession().getLabel("AVISMUT_SUCCES"));

            return buffer.toString();
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    // protected String TransformeToPdf() throws JAException {
    // FWIExportManager batch = new FWIBatchDocument();
    // batch.setPath(Jade.getInstance().getHomeDir() + "/" +
    // AFApplication.DEFAULT_APPLICATION_naos_REP + "/work/");
    // batch.setFileName("AvisMutation");
    // batch.setTypeRequested(FWIDocumentType.PDF);
    // batch.setIsDeleteOnExit(true);
    // batch.addAll(_doc.getBatchList());
    // try {
    // batch.createOutputFile();
    // } catch (Exception e) {
    // super.setMsgType(super.WARNING);
    //
    // String message = "Document généré en erreur :" + e.getMessage();
    // super._addError(message);
    // super.setMessage(message);
    // throw new JAException(message);
    // }
    // return batch.getNewFilePath();
    // }

}
