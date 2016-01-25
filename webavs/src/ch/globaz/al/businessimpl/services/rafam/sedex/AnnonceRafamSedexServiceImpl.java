package ch.globaz.al.businessimpl.services.rafam.sedex;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.exceptions.rafam.ALRafamSedexException;
import ch.globaz.al.business.services.rafam.sedex.AnnonceRafamSedexService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service lié à la gestion de l'envoi/réception des annonces RAFam
 * 
 * @author jts
 * 
 */
public class AnnonceRafamSedexServiceImpl extends ALAbstractBusinessServiceImpl implements AnnonceRafamSedexService {

    @Override
    public boolean isAnnonceEmployeurDelegue(String recordNumber) throws JadeApplicationException {
        return this.isAnnonceEmployeurDelegue(null, recordNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.rafam.sedex.AnnonceRafamSedexService#isAnnonceEmployeurDelegue()
     */
    @Override
    public boolean isAnnonceEmployeurDelegue(String internalOfficeReference, String recordNumber)
            throws JadeApplicationException {

        if (JadeStringUtil.isBlank(recordNumber)) {
            throw new ALRafamSedexException(
                    "AnnonceRafamSedexServiceImpl#isAnnonceEmployeurDelegue : recordNumber is empty");
        }

        if (!JadeStringUtil.isEmpty(internalOfficeReference)) {
            return !ALConstRafam.PREFIX_ANNONCE_CAISSE.equals(internalOfficeReference.substring(0, 1));
        } else {
            return recordNumber.length() == 16;
        }
    }

}
