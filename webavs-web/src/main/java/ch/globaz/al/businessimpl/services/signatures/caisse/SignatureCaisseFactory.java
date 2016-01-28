package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseAGLSService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseBMSService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseCCJUService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseCCVDService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseCICIService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseCVCIService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseFPVService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseFVEService;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH510Service;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH5110Service;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH513Service;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH514Service;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH515Service;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH517Service;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseService;

/**
 * Classe permettant de dd�finir quel type de service � utiliser pour r�cup�er la signature d'une caisse
 * 
 * @author PTA
 * 
 */

public abstract class SignatureCaisseFactory {
    /**
     * Liste des service disponible en fonction de la caisse
     */

    private static HashMap<String, Class<?>> signatureMap;

    static {
        SignatureCaisseFactory.signatureMap = new HashMap<String, Class<?>>();
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_CCJU, SignatureCaisseCCJUService.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_CICI, SignatureCaisseCICIService.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_CVCI, SignatureCaisseCVCIService.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_CCVD, SignatureCaisseCCVDService.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_H51X, SignatureCaisseH5110Service.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_H510, SignatureCaisseH510Service.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_H513, SignatureCaisseH513Service.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_H514, SignatureCaisseH514Service.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_H515, SignatureCaisseH515Service.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_H517, SignatureCaisseH517Service.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_AGLS, SignatureCaisseAGLSService.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_FPV, SignatureCaisseFPVService.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_FVE, SignatureCaisseFVEService.class);
        SignatureCaisseFactory.signatureMap.put(ALConstCaisse.CAISSE_BMS, SignatureCaisseBMSService.class);

    }

    /**
     * R�cup�re le service de signature en fonction de la <code>date</code> et du nom de la caisse r�cup�r� dans les
     * param�tres
     * 
     * @param date
     *            Date pour laquelle r�cup�rer le service
     * @return Classe � instancier
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static Class getServiceSignatureCaisse() throws JadeApplicationException, JadePersistenceException {

        String caisse = ALServiceLocator.getParametersServices().getNomCaisse();

        if (SignatureCaisseFactory.signatureMap.containsKey(caisse)) {
            return SignatureCaisseFactory.signatureMap.get(caisse);
        } else {
            return SignatureCaisseService.class;
        }
    }
}
