package globaz.pegasus.vb.downloadDocument;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.widget.JadeApplicationServiceReflection;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.op.common.model.document.Document;

public class PCDownloadDocumentViewBean extends JadeApplicationServiceReflection implements BIPersistentObject,
        FWViewBeanInterface {

    private String docName = null;
    private String docType = null;
    private Document document = null;
    private String id = null;
    private String nomDocUUID = null;

    private String path = null;

    private boolean preparerDoc = false;
    private String size = null;

    private String typeDocument = null;

    @Override
    public void add() throws Exception {
        throw new Exception("Not implemented");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("Not implemented");
    }

    public String getDocName() {
        return docName;
    }

    public String getDocType() {
        return docType;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BISession getISession() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMsgType() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNomDocUUID() {
        return nomDocUUID;
    }

    public String getPath() {
        return path;
    }

    public String getSize() {
        return size;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public boolean isPreparerDoc() {
        return preparerDoc;
    }

    @Override
    public void retrieve() throws Exception {
        // this.typeDoc = TypeDoc.valueOf(this.typeDoc1);
        if (JadeStringUtil.isEmpty(nomDocUUID)) {
            // this.document = (Document) this.invokMethodForAfficher();
            path = (String) invokMethodForAfficher();
        }
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void setISession(BISession newSession) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMessage(String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMsgType(String msgType) {
        // TODO Auto-generated method stub

    }

    public void setNomDocUUID(String nomDocUUID) {
        this.nomDocUUID = nomDocUUID;
    }

    public void setPreparerDoc(boolean preparerDoc) {
        this.preparerDoc = preparerDoc;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    @Override
    public void update() throws Exception {
        throw new Exception("Not implemented");
    }

}
