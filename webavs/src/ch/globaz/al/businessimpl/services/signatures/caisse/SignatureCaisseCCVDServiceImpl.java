package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseCCVDService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class SignatureCaisseCCVDServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseCCVDService {
    @Override
    public String getLibelleSignature(String activiteAllocataire, String typeDocument) throws JadeApplicationException,
            JadePersistenceException {

        return ALConstDocument.SIGNATURE_CAISSE_CCVD;

    }
}
