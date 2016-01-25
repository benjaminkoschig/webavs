package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseCCJUService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service générant la signature de la caisse
 * 
 * @author PTA
 * 
 */

public class SignatureCaisseCCJUServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseCCJUService {

    @Override
    public String getLibelleSignature(String activiteAllocataire, String typeDocument) throws JadeApplicationException,
            JadePersistenceException {
        // TODO contrôle des paramètres
        if (JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_AGRICULTEUR, false)
                || JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, false)
                || JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, false)) {
            return ALConstDocument.SIGNATURE_CAISSE_COMP_CCJU;
        } else {
            return ALConstDocument.SIGNATURE_CAISSE_ALLOC_CCJU;
        }
    }
}
