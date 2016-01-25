package globaz.babel.helpers.annexes;

import globaz.babel.db.annexes.CTDefaultAnnexes;
import globaz.babel.db.annexes.CTTypeDocumentJointAnnexes;
import globaz.babel.db.annexes.CTTypeDocumentJointAnnexesManager;
import globaz.babel.db.cat.CTDocument;
import globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTDocumentJointDefaultAnnexesHelper extends FWHelper {

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

        CTDocumentJointDefaultAnnexesViewBean vb = (CTDocumentJointDefaultAnnexesViewBean) viewBean;

        CTDefaultAnnexes defAnn = new CTDefaultAnnexes();
        defAnn.setISession(session);
        defAnn.setIdDocument(vb.getIdDocument());
        defAnn.setCsTypeDocument(vb.getCsTypeDocument());
        defAnn.setCsAnnexe(vb.getCsAnnexe());
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

        CTDocumentJointDefaultAnnexesViewBean docJDefAnn = (CTDocumentJointDefaultAnnexesViewBean) viewBean;

        // chercher les infos du document
        CTDocument doc = new CTDocument();
        doc.setISession(session);
        doc.setIdDocument(docJDefAnn.getIdDocument());
        doc.retrieve();

        docJDefAnn.setCsDestinataireDocument(doc.getCsDestinataire());
        docJDefAnn.setCsDomaineDocument(doc.getCsDomaine());
        docJDefAnn.setCsTypeDocument(doc.getCsTypeDocument());
        docJDefAnn.setNomDocument(doc.getNom());

    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultAnnexesViewBean vb = (CTDocumentJointDefaultAnnexesViewBean) viewBean;

        CTDefaultAnnexes defAnn = new CTDefaultAnnexes();
        defAnn.setISession(session);
        defAnn.setIdDefaultAnnexes(vb.getIdDefaultAnnexe());
        defAnn.retrieve();

        defAnn.delete();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultAnnexesViewBean vb = (CTDocumentJointDefaultAnnexesViewBean) viewBean;

        // recherche la famille du CS des annexes pour ce type et domaine de
        // document
        CTTypeDocumentJointAnnexesManager TDJAManager = new CTTypeDocumentJointAnnexesManager();
        TDJAManager.setSession(vb.getSession());
        TDJAManager.setForCsTypeDocument(vb.getCsTypeDocument());
        TDJAManager.setForCsDomaineDocument(vb.getCsDomaineDocument());

        try {
            TDJAManager.find();
        } catch (Exception e) {
            vb.setMessage(e.toString());
            vb.setMsgType(FWMessage.ERREUR);
        }

        if (TDJAManager.size() > 0) {
            CTTypeDocumentJointAnnexes typeDocJAnnexes = (CTTypeDocumentJointAnnexes) TDJAManager.get(0);

            if (typeDocJAnnexes != null) {
                vb.setCsGroupeAnnexes(typeDocJAnnexes.getCsGroupe());
            }
        }

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

        CTDocumentJointDefaultAnnexesViewBean vb = (CTDocumentJointDefaultAnnexesViewBean) viewBean;

        CTDefaultAnnexes defAnn = new CTDefaultAnnexes();
        defAnn.setISession(session);
        defAnn.setIdDefaultAnnexes(vb.getIdDefaultAnnexe());
        defAnn.retrieve();

        vb.setCsAnnexe(defAnn.getCsAnnexe());
        vb.setCsTypeDocument(defAnn.getCsTypeDocument());
        vb.setIdDocument(defAnn.getIdDocument());

        // recherche la famille du CS des annexes pour ce type et domaine de
        // document
        CTTypeDocumentJointAnnexesManager TDJAManager = new CTTypeDocumentJointAnnexesManager();
        TDJAManager.setSession(vb.getSession());
        TDJAManager.setForCsTypeDocument(vb.getCsTypeDocument());
        TDJAManager.setForCsDomaineDocument(vb.getCsDomaineDocument());

        try {
            TDJAManager.find();
        } catch (Exception e) {
            vb.setMessage(e.toString());
            vb.setMsgType(FWMessage.ERREUR);
        }

        if (TDJAManager.size() > 0) {
            CTTypeDocumentJointAnnexes typeDocJAnnexes = (CTTypeDocumentJointAnnexes) TDJAManager.get(0);

            if (typeDocJAnnexes != null) {
                vb.setCsGroupeAnnexes(typeDocJAnnexes.getCsGroupe());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        CTDocumentJointDefaultAnnexesViewBean vb = (CTDocumentJointDefaultAnnexesViewBean) viewBean;

        CTDefaultAnnexes defAnn = new CTDefaultAnnexes();
        defAnn.setISession(session);
        defAnn.setIdDefaultAnnexes(vb.getIdDefaultAnnexe());
        defAnn.retrieve();

        defAnn.setCsAnnexe(vb.getCsAnnexe());

        defAnn.update();
    }

}
