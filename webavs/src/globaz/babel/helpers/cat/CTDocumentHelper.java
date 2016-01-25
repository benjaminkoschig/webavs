package globaz.babel.helpers.cat;

import globaz.babel.db.cat.CTElement;
import globaz.babel.db.cat.CTElementManager;
import globaz.babel.db.cat.CTTexte;
import globaz.babel.db.cat.CTTexteManager;
import globaz.babel.db.cat.CTTypeDocument;
import globaz.babel.db.cat.CTTypeDocumentManager;
import globaz.babel.vb.cat.CTDocumentViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.framework.translation.FWTranslation;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTDocumentHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        CTDocumentViewBean document = (CTDocumentViewBean) viewBean;

        if (JadeStringUtil.isEmpty(document.getIdTypeDocument())) {
            // le type de document n'est pas saisi, on va rechercher avec les cs
            // domaines et types documents
            CTTypeDocumentManager types = new CTTypeDocumentManager();

            types.setISession(session);
            types.setForCsDomaine(document.getCsDomaine());
            types.setForCsTypeDocument(document.getCsTypeDocument());
            types.find();

            if (!types.isEmpty()) {
                CTTypeDocument typeDoc = (CTTypeDocument) types.get(0);

                document.setIdTypeDocument(typeDoc.getIdTypeDocument());
                document.setBorneInferieure(typeDoc.getBorneInferieure());
            }
        }

        super._add(viewBean, action, session);
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
        setCsTypesDocumentsExclus((BSession) session, (CTDocumentViewBean) viewBean);
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
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        setCsTypesDocumentsExclus((BSession) session, (CTDocumentViewBean) viewBean);
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
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);
        setCsTypesDocumentsExclus((BSession) session, (CTDocumentViewBean) viewBean);
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
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        CTDocumentViewBean document = (CTDocumentViewBean) viewBean;

        // le type de document est peut-etre faux, on va rechercher avec les cs
        // domaines et types documents
        CTTypeDocumentManager types = new CTTypeDocumentManager();

        types.setISession(session);
        types.setForCsDomaine(document.getCsDomaine());
        types.setForCsTypeDocument(document.getCsTypeDocument());
        types.find();

        if (!types.isEmpty()) {
            document.setIdTypeDocument(((CTTypeDocument) types.get(0)).getIdTypeDocument());
        }

        super._update(viewBean, action, session);
    }

    /**
     * Cree un nouveau document qui contient une copie de tous les textes d'un document.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface copierDocument(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CTDocumentViewBean oldDocument = (CTDocumentViewBean) viewBean;
        CTDocumentViewBean newDocument = new CTDocumentViewBean();
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            oldDocument.retrieve();

            // création du nouveau document
            newDocument.copyDataFromEntity(oldDocument);
            newDocument.setNom(oldDocument.getNom() + " (*)");
            newDocument.setCsTypeDocument(oldDocument.getCsTypeDocument());
            newDocument.setCsDomaine(oldDocument.getCsDomaine());
            newDocument.setIdTypeDocument(oldDocument.getIdTypeDocument());
            newDocument.setDefaut(Boolean.FALSE);
            newDocument.setISession(session);
            newDocument.setBorneInferieure(oldDocument.getBorneInferieure());
            newDocument.add(transaction);

            // copie des elements
            CTElementManager elements = new CTElementManager();
            CTTexteManager textes = new CTTexteManager();

            elements.setISession(session);
            textes.setISession(session);

            elements.setForIdDocument(oldDocument.getIdDocument());
            elements.find(transaction);

            for (int idElement = elements.size(); --idElement >= 0;) {
                CTElement oldElement = (CTElement) elements.get(idElement);
                CTElement newElement = new CTElement();

                newElement.copyDataFromEntity(oldElement);
                newElement.setIdDocument(newDocument.getIdDocument());
                newElement.setISession(session);
                newElement.setBorneInferieure(oldDocument.getBorneInferieure());
                newElement.add(transaction);

                // copie des textes pour cet element
                textes.setForIdElement(oldElement.getIdElement());
                textes.find(transaction);

                for (int idTexte = textes.size(); --idTexte >= 0;) {
                    CTTexte newTexte = new CTTexte();

                    newTexte.copyDataFromEntity((CTTexte) textes.get(idTexte));
                    newTexte.setIdElement(newElement.getIdElement());
                    newTexte.setISession(session);
                    newTexte.setBorneInferieure(oldDocument.getBorneInferieure());
                    newTexte.add(transaction);
                }
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return newDocument;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        // pour l'instant la seule action custom c'est la copie du document
        return copierDocument(viewBean, action, session);
    }

    // cree le set de codes systemes qu'il ne faut pas afficher pour ce domaine.
    private void setCsTypesDocumentsExclus(BSession session, CTDocumentViewBean viewBean) throws Exception {
        if (JadeStringUtil.isEmpty(viewBean.getCsDomaine())) {
            // le domaine n'est pas choisi, probablement qu'il s'agit de la
            // premiere fois ou cet ecran est affiche
            // on recherche donc le premier domaine qu'on trouve
            FWParametersSystemCodeManager codes = FWTranslation.getSystemCodeList(viewBean.getCsGroupeDomaines(),
                    session);

            if (!codes.isEmpty()) {
                viewBean.setCsDomaine(((FWParametersSystemCode) codes.get(0)).getIdCode());
            }
        }

        HashSet csExclus = new HashSet();

        // charger tous les codes de type de documents
        FWParametersSystemCodeManager codes = FWTranslation.getSystemCodeList(viewBean.getCsGroupeTypesDocuments(),
                session);

        for (int idCode = 0; idCode < codes.size(); ++idCode) {
            csExclus.add(((FWParametersSystemCode) codes.get(idCode)).getIdCode());
        }

        // charger la liste des types de documents pour le domaine de catalogue
        // courant
        if (!JadeStringUtil.isEmpty(viewBean.getCsDomaine())) {
            CTTypeDocumentManager types = new CTTypeDocumentManager();

            types.setSession(session);
            types.setForCsDomaine(viewBean.getCsDomaine());
            types.find();

            for (int idType = 0; idType < types.size(); ++idType) {
                csExclus.remove(((CTTypeDocument) types.get(idType)).getCsTypeDocument());
            }
        }

        viewBean.setCsTypesDocumentsExclus(csExclus);
    }
}
