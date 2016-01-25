package ch.globaz.orion.businessimpl.services.pucs;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;

public enum EtatSwissDecPucsFile {

    A_VALIDER(EBProperties.PUCS_SWISS_DEC_DIRECTORY_A_VALIDER),
    A_TRAITER(EBProperties.PUCS_SWISS_DEC_DIRECTORY),
    REJETER(EBProperties.PUCS_SWISS_DEC_DIRECTORY_REFUSER),
    TRAITER(EBProperties.PUCS_SWISS_DEC_DIRECTORY_OK),
    ERREUR(EBProperties.PUCS_SWISS_DEC_DIRECTORY_KO);

    private EBProperties path;

    private EtatSwissDecPucsFile(EBProperties path) {
        this.path = path;
    }

    public String getPath() throws PropertiesException {
        return path.getValue();
    }

}
