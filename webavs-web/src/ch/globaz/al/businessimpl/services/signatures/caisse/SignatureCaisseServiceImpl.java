package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class SignatureCaisseServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseService {

    @Override
    public String getLibelleSignature(/* String nomCaisse, */String activiteAllocataire, String typeDocument)
            throws JadeApplicationException, JadePersistenceException {
        return "Signature générique";
    }
}
