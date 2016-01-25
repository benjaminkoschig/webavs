package ch.globaz.amal.businessimpl.services.sedexRP;

import globaz.jade.context.JadeThread;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AnnonceSedexTransformation {
    public static final String JAXB_CURRENT_VERSION = "3";

    public void transformV2ToLast(String annonce) {

    }

    public static SimpleAnnonceSedex transformationCurrentVersion(SimpleAnnonceSedex lastAnnonce) {

        boolean bUpdated = false;

        if (lastAnnonce.getMessageContent().indexOf("pv-5201-000101/2") > 0) {
            // Si on trouve cette chaine, on a une nouvelle décision en version 2
            lastAnnonce.setMessageContent(lastAnnonce.getMessageContent().replaceFirst("pv-common/2", "pv-common/3")
                    .replaceFirst("pv-5201-000101/2", "pv-5201-000101/3"));

            lastAnnonce.setMessageHeader(lastAnnonce.getMessageHeader().replaceFirst("pv-common/2", "pv-common/3")
                    .replaceFirst("pv-5201-000101/2", "pv-5201-000101/3"));

            bUpdated = true;
        } else if (lastAnnonce.getMessageContent().indexOf("pv-5201-000201/2") > 0) {
            // Si on trouve cette chaine, on a une nouvelle décision en version 2
            lastAnnonce.setMessageContent(lastAnnonce.getMessageContent().replaceFirst("pv-common/2", "pv-common/3")
                    .replaceFirst("pv-5201-000201/2", "pv-5201-000201/3"));

            lastAnnonce.setMessageHeader(lastAnnonce.getMessageHeader().replaceFirst("pv-common/2", "pv-common/3")
                    .replaceFirst("pv-5201-000201/2", "pv-5201-000201/3"));

            bUpdated = true;
        }

        if (bUpdated) {
            try {
                AmalServiceLocator.getSimpleAnnonceSedexService().update(lastAnnonce);
            } catch (Exception e) {
                //On ne fait rien, on continue uniquement avec la variable corrigé sans mettre à jour en DB
                JadeThread.logInfo("AnnoncesRPServiceImpl.transformationCurrentVersion()", e.getMessage());
            }
        }
        return lastAnnonce;

    }
}
