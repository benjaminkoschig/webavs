package globaz.itucana.service;

import globaz.itucana.constantes.TUTypesBouclement;
import globaz.itucana.exception.TUInitTucanaInterfaceException;
import globaz.itucana.exception.TUNoServiceDefinedException;

/**
 * Classe permettant de récupérer le bon service
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public abstract class TUServiceLocator {
    private static TUACMService acmService = null;
    private static TUAFService afService = null;
    private static TUCAService caService = null;
    private static TUCGService cgService = null;

    /**
     * Récupère le service ACM
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    private static TUACMService getACMService() throws TUInitTucanaInterfaceException {
        if (acmService == null) {
            acmService = TUServiceLoader.getACMService();
        }
        return acmService;
    }

    /**
     * Récupère le service AF
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    private static TUAFService getAFService() throws TUInitTucanaInterfaceException {
        if (afService == null) {
            afService = TUServiceLoader.getAFService();
        }
        return afService;
    }

    /**
     * Récupère le service CA
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    private static TUCAService getCAService() throws TUInitTucanaInterfaceException {
        if (caService == null) {
            caService = TUServiceLoader.getCAService();
        }
        return caService;
    }

    /**
     * Récupère le service AG
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    private static TUCGService getCGService() throws TUInitTucanaInterfaceException {
        if (cgService == null) {
            cgService = TUServiceLoader.getCGService();
        }
        return cgService;
    }

    /**
     * Récupère le service en fonction du type de bouclement
     * 
     * @param typesBouclement
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    public static TUCommonService getService(TUTypesBouclement typesBouclement) throws TUInitTucanaInterfaceException {
        TUCommonService commonService = null;
        if (TUTypesBouclement.BOUCLEMENT_ACM.equals(typesBouclement)) {
            commonService = getACMService();
        } else if (TUTypesBouclement.BOUCLEMENT_AF.equals(typesBouclement)) {
            commonService = getAFService();
        } else if (TUTypesBouclement.BOUCLEMENT_CG.equals(typesBouclement)) {
            commonService = getCGService();
        } else if (TUTypesBouclement.BOUCLEMENT_CA.equals(typesBouclement)) {
            commonService = getCAService();
        } else {
            throw new TUNoServiceDefinedException("No service defined for " + typesBouclement.getTypeBouclement());
        }
        return commonService;
    }
}
