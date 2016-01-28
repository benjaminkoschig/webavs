package globaz.babel.helpers.copies;

import globaz.babel.db.cat.CTDocument;
import globaz.babel.db.copies.CTDefaultCopies;
import globaz.babel.utils.CTCsMapperUtils;
import globaz.babel.vb.copies.CTDocumentJointDefaultCopiesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTDocumentJointDefaultCopiesHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_add(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultCopiesViewBean vb = (CTDocumentJointDefaultCopiesViewBean) viewBean;

        CTDefaultCopies defAnn = new CTDefaultCopies();
        defAnn.setISession(session);
        defAnn.setIdDocument(vb.getIdDocument());
        defAnn.setCsTypeDocument(vb.getCsTypeDocument());
        defAnn.setCsTypeIntervenant(vb.getCsTypeIntervenant());
        defAnn.add();
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultCopiesViewBean docJDefCop = (CTDocumentJointDefaultCopiesViewBean) viewBean;

        // chercher les infos du document
        CTDocument doc = new CTDocument();
        doc.setISession(session);
        doc.setIdDocument(docJDefCop.getIdDocument());
        doc.retrieve();

        docJDefCop.setCsDestinataireDocument(doc.getCsDestinataire());
        docJDefCop.setCsDomaineDocument(doc.getCsDomaine());
        docJDefCop.setCsTypeDocument(doc.getCsTypeDocument());
        docJDefCop.setNomDocument(doc.getNom());

    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultCopiesViewBean vb = (CTDocumentJointDefaultCopiesViewBean) viewBean;

        CTDefaultCopies defCop = new CTDefaultCopies();
        defCop.setISession(session);
        defCop.setIdDefaultCopie(vb.getIdDefaultCopie());
        defCop.retrieve();

        defCop.delete();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultCopiesViewBean vb = (CTDocumentJointDefaultCopiesViewBean) viewBean;

        // recherche la famille du CS des intervenant pour ce domaine de
        // document
        vb.setCsGroupeTypeIntervenant(CTCsMapperUtils.getCsIntervanantTypeForCsDomaineCatalogue(vb
                .getCsDomaineDocument()));

        super._init(viewBean, action, session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultCopiesViewBean vb = (CTDocumentJointDefaultCopiesViewBean) viewBean;

        CTDefaultCopies defCop = new CTDefaultCopies();
        defCop.setISession(session);
        defCop.setIdDefaultCopie(vb.getIdDefaultCopie());
        defCop.retrieve();

        vb.setCsTypeIntervenant(defCop.getCsTypeIntervenant());
        vb.setCsTypeDocument(defCop.getCsTypeDocument());
        vb.setIdDocument(defCop.getIdDocument());

        // recherche la famille du CS des intervenant pour ce domaine de
        // document
        vb.setCsGroupeTypeIntervenant(CTCsMapperUtils.getCsIntervanantTypeForCsDomaineCatalogue(vb
                .getCsDomaineDocument()));

    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultCopiesViewBean vb = (CTDocumentJointDefaultCopiesViewBean) viewBean;

        CTDefaultCopies defCop = new CTDefaultCopies();
        defCop.setISession(session);
        defCop.setIdDefaultCopie(vb.getIdDefaultCopie());
        defCop.retrieve();

        defCop.setCsTypeIntervenant(vb.getCsTypeIntervenant());

        defCop.update();
    }

}
