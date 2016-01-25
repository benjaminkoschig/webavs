package ch.globaz.pegasus.businessimpl.services.transfertDossier;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.services.transfertDossier.TransfertRentePCProviderService;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.DefaultTransfertRentePCBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.SingleTransfertPCAbstractBuilder;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class TransfertRentePCProviderServiceImpl extends AbstractPegasusBuilder implements
        TransfertRentePCProviderService {

    @Override
    public void checkProcessArguments(String idCaisseAgence) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, TransfertDossierException {

        // verification de la nouvelle caisse
        try {
            AdresseTiersDetail nouvelleCaisseDetail = PegasusUtil.getAdresseCascadeByType(idCaisseAgence,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);
            if ((nouvelleCaisseDetail == null) || JadeStringUtil.isEmpty(nouvelleCaisseDetail.getAdresseFormate())) {
                this.logError("pegasus.process.transfertRente.caisseAgence.integrity", idCaisseAgence);
            }
        } catch (JadeApplicationException e) {
            this.logError("pegasus.process.transfertRente.caisseAgence.integrity", idCaisseAgence);
        }

    }

    @Override
    public DefaultTransfertRentePCBuilder getTransfertBuilder() {
        return new DefaultTransfertRentePCBuilder();
    }

    private void logError(String message) {
        JadeThread.logError(TransfertRentePCProviderServiceImpl.class.getName(), message);
    }

    private void logError(String message, String... parametres) {
        JadeThread.logError(TransfertDossierPCProviderServiceImpl.class.getName(), message, parametres);
    }

}
