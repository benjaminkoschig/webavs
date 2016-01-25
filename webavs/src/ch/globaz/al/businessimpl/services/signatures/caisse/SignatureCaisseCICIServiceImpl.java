package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseCICIService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class SignatureCaisseCICIServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseCICIService {

    @Override
    public String getLibelleSignature(/* String nomCaisse, */String activiteAllocataire, String typeDocument)
            throws JadeApplicationException, JadePersistenceException {
        // TODO contrôle des paramètres
        if (JadeStringUtil.equals(ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_IND, typeDocument, false)) {
            return ALConstDocument.SIGNATURE_CAISSE_CICICAM_ECHEANCE;
        } else {
            return ALConstDocument.SIGNATURE_CAISSE_CICICAM;
        }
    }

}
