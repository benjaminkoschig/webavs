package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import ch.globaz.vulpecula.documents.rectificatif.PTImpressionRectificatifProcess;

public class PTImpressionRectificatifImpl extends PTImpression {

    public PTImpressionRectificatifImpl() {
        super();
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {

        PTImpressionRectificatifProcess process = new PTImpressionRectificatifProcess();
        process.setParentWithCopy(context);
        process.setNumeroPassageFacturation(passage.getIdPassage());
        process.setEMailAddress(context.getEMailAddress());
        process.executeProcess();

        // contr�ler si le process a fonctionn�
        return !(context.isAborted() || context.isOnError() || context.getSession().hasErrors());
    }
}
