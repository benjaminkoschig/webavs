package ch.globaz.pegasus.businessimpl.services.transfertDossier;

import ch.globaz.pegasus.business.constantes.transfertdossier.TransfertDossierBuilderType;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.services.transfertDossier.ITransfertDossierBuilder;
import ch.globaz.pegasus.business.services.transfertDossier.TransfertDossierBuilderProviderService;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.TransfertDossierSansPCBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.TransfertDossierSuppressionPCBuilder;

public class TransfertDossierBuilderProviderServiceImpl implements TransfertDossierBuilderProviderService {

    @Override
    public ITransfertDossierBuilder getBuilderFor(String typeBuilder) throws TransfertDossierException {
        if (TransfertDossierBuilderType.DEMANDE_INITIALE_SANS_PC.equals(typeBuilder)) {
            return new TransfertDossierSansPCBuilder();
        } else if (TransfertDossierBuilderType.DEMANDE_EN_COURS.equals(typeBuilder)) {
            return new TransfertDossierSuppressionPCBuilder();
        } else {
            throw new TransfertDossierException("Transfert Dossier Builder type unknown : " + typeBuilder.toString());
        }
    }

}
