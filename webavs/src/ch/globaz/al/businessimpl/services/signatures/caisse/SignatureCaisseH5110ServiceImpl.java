package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH5110Service;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class SignatureCaisseH5110ServiceImpl extends ALAbstractBusinessServiceImpl implements
        SignatureCaisseH5110Service {

    @Override
    public String getLibelleSignature(String activiteAllocataire, String typeDocument) throws JadeApplicationException,
            JadePersistenceException {
        return ALConstDocument.SIGNATURE_CAISSE_H51X;
    }

}
