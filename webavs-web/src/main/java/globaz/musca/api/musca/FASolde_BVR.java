package globaz.musca.api.musca;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.globall.api.BIContainer;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.IFAImpressionFactureProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.process.FAGenericProcess;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.process.FAPassageCompenserProcess;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.application.CAApplication;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import java.util.Iterator;
import java.util.List;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class FASolde_BVR extends FAImpressionGenerique {
    protected CAImpressionBulletinsSoldes_Doc _document = null;
    private FAAfactManager manager = null;

    /**
     * Constructor for FASolde_BVR.
     */
    public FASolde_BVR() {
        super();
    }

    /**
     * @see globaz.musca.external.IntModuleImpression#beginPrinting(IFAPassage, IFAImpressionFactureProcess)
     */
    @Override
    public boolean beginPrinting(IFAPassage intPassage, IFAImpressionFactureProcess c) throws Exception {
        FAImpressionFactureProcess context = (FAImpressionFactureProcess) c;

        FAPassage passage = (FAPassage) intPassage;
        if (_document == null) {
            BSession sessionOsiris = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS).newSession(context.getSession());
            // _document = new CAImpressionBulletinsSoldes_Doc(sessionOsiris);
            _document = new CAImpressionBulletinsSoldes_Doc(sessionOsiris, context);
            _document.setPassage(passage);
            _document.setIsMuscaSource(true);
            _document.setParentWithCopy(context);
            _document.setEMailAddress(context.getEMailAddress());
            _document.setMontantMinimeNeg(((FAApplication) GlobazServer.getCurrentSystem().getApplication(
                    FAApplication.DEFAULT_APPLICATION_MUSCA)).getMontantMinimeNeg());
            _document.setMontantMinimePos(((FAApplication) GlobazServer.getCurrentSystem().getApplication(
                    FAApplication.DEFAULT_APPLICATION_MUSCA)).getMontantMinimePos());
        }
        return true;
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        try {
            FAImpressionFactureProcess process = new FAImpressionFactureProcess();
            process.setParentWithCopy(context);
            process.setIdPassage(passage.getIdPassage());
            process.setEMailAddress(context.getEMailAddress());
            process.setDocumentType(FAImpressionFactureProcess.DOCTYPE_LETTER);
            process.setImpressionClassName(this.getClass().getName());
            process.setEnvoyerGed(((FAGenericProcess) context).getEnvoyerGed());
            process.setCallEcran(((FAGenericProcess) context).getCallEcran());
            process.setUnificationProcess(true);
            process._executeImpressionProcess(passage);

            // S'il y a un process parent et qu'il y a un fichier joint, on
            // remonte ce fichier vers
            // le process parent
            if ((process.getParent() != null) && process.hasAttachedDocuments()) {
                List<?> e = process.getAttachedDocuments();
                for (Iterator<?> iter = e.iterator(); iter.hasNext();) {
                    JadePublishDocument doc = (JadePublishDocument) iter.next();
                    process.getParent().registerAttachedDocument(doc.getPublishJobDefinition().getDocumentInfo(),
                            doc.getDocumentLocation());
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see globaz.musca.external.IntModuleImpression#get_document()
     */
    @Override
    public FWIDocumentInterface get_document() {
        return _document;
    }

    /**
     * @see globaz.musca.external.IntModuleImpression#get_documentManager(FAImpressionFactureProcess)
     */
    @Override
    public BIContainer get_documentManager(IFAImpressionFactureProcess c) {
        FAImpressionFactureProcess context = (FAImpressionFactureProcess) c;
        FAPassageCompenserProcess passage = new FAPassageCompenserProcess();
        passage.setSession(context.getSession());

        manager = new FAAfactManager();
        manager.setSession(context.getSession());
        manager.setIsAfacForBulletinsSoldes(true);
        manager.setForIdPassage(context.getIdPassage());
        manager.setForMontantSuppZero(true);
        manager.setForIdExterneFactureCompensationNotEmpty(Boolean.TRUE);
        manager.setForIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION);
        manager.setForAQuittancer(Boolean.FALSE);
        manager.setOrderBy(FAEnteteFacture.CS_TRI_DEBITEUR);
        manager.wantCallMethodAfter(false);
        try {
            manager.setForCSRubrique(passage.getRubriqueCode(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE));
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // manager.setOrderBy(" IDEXTDEBCOM");
        return manager;
    }

    /**
     * @see globaz.musca.api.musca.FAGenericPrintImpl#imprimer(IFAPassage, BProcess)
     */
    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        // try {
        // FAImpressionFactureProcess process = new
        // FAImpressionFactureProcess();
        // process.setParentWithCopy(context);
        // process.setIdPassage(passage.getIdPassage());
        // process.setEMailAddress(context.getEMailAddress());
        // process.setDocumentType(FAImpressionFactureProcess.DOCTYPE_LETTER);
        // process.setImpressionClassName(this.getClass().getName());
        // process._executeImpressionProcess((FAPassage) passage);
        //
        // //S'il y a un process parent et qu'il y a un fichier joint, on
        // remonte ce fichier vers
        // // le process parent
        // if (process.getParent() != null && process.hasAttachedDocuments()) {
        // List e = process.getAttachedDocuments();
        // for (Iterator iter = e.iterator(); iter.hasNext();) {
        // process.getParent().registerAttachedDocument(((JadePublishDocument)
        // iter.next()).getDocumentLocation());
        // }
        // }
        //
        // } catch (Exception e) {
        // JadeLogger.error(this,e);
        // }
        // //contrôler si le process a fonctionné
        // if (!context.isAborted()) {
        // return true;
        // } else
        return false;
    }

    /**
     * @see globaz.musca.external.IntModuleImpression#print()
     */
    @Override
    public boolean print() throws Exception {
        _document.setEntityList(entityList);
        _document.executeProcess();
        return true;
    }

}
