package ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement;

import globaz.babel.api.ICTDocument;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.Date;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.demanderenseignement.DemandeRenseignementException;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public abstract class AbstractDemandeRenseignementBuilder extends AbstractPegasusBuilder {

    protected ICTDocument babelDoc = null;
    protected ICTDocument babelDocCommun = null;
    protected ICTDocument babelDocPageGarde = null;
    protected String idDemandePC = null;
    protected String noDocument = null;
    protected PersonneEtendueComplexModel requerant = null;

    public AbstractDemandeRenseignementBuilder() {
        super();
    }

    public JadePrintDocumentContainer build(JadePublishDocumentInfo pubInfo, String idDemandePC, String email,
            String idGestionnaire) throws DemandeRenseignementException {

        this.idDemandePC = idDemandePC;
        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        loadDBEntity();

        allDoc = getDocumentBuilded(allDoc, idGestionnaire);

        String title = getEmailTitle();

        pubInfo.setOwnerEmail(email);
        pubInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, email);
        pubInfo.setArchiveDocument(false);
        pubInfo.setPublishDocument(true);
        pubInfo.setDocumentType(noDocument);
        pubInfo.setDocumentTypeNumber(noDocument);
        pubInfo.setDocumentDate(JadeDateUtil.getFormattedDate(new Date()));

        pubInfo.setDocumentTitle(title);
        pubInfo.setDocumentSubject(title);

        allDoc.setMergedDocDestination(pubInfo);

        return allDoc;
    }

    protected abstract JadePrintDocumentContainer getDocumentBuilded(JadePrintDocumentContainer allDoc,
            String idGestionnaire) throws DemandeRenseignementException;

    protected abstract String getEmailTitle();

    protected abstract void loadDBEntity() throws DemandeRenseignementException;

    public void loadParameters(Map<String, List<String>> parameters) {

    }

}