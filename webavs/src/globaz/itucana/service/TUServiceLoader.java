package globaz.itucana.service;

import globaz.itucana.adapter.ICommonAdapter;
import globaz.itucana.constantes.IPropertiesNames;
import globaz.itucana.exception.TUInitTucanaInterfaceException;
import globaz.itucana.exception.TUInterfaceException;
import globaz.itucana.exception.TUServiceInstanciationException;
import globaz.itucana.properties.TUPropertiesProvider;

/**
 * Permet de charger les services impl�ment�s par ITucana � partir de propri�t�s d�finissant les classes
 * d'impl�mentation des interfaceAdapter
 * 
 * @author fgo
 */
public abstract class TUServiceLoader {
    /**
     * R�cup�re le service ACM
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    public static TUACMService getACMService() throws TUInitTucanaInterfaceException {
        TUACMService service = null;
        ICommonAdapter adapter = getAdapter(IPropertiesNames.ACM_ADAPTER);
        if (adapter != null) {
            service = new TUACMService(adapter);
        } else {
            throw new TUServiceInstanciationException("ACM Adapter not defined!");
        }
        return service;
    }

    /**
     * R�cup�re un adapteur
     * 
     * @param adapterKey
     * @return
     * @throws TUServiceInstanciationException
     * @throws TUInterfaceException
     */
    private static ICommonAdapter getAdapter(String adapterKey) throws TUInitTucanaInterfaceException {
        try {
            ICommonAdapter commonAdapter = null;
            String adapterClassName = TUPropertiesProvider.getInstance().getProperty(adapterKey);
            if (adapterClassName != null) {
                commonAdapter = (ICommonAdapter) TUServiceLoader.class.getClassLoader().loadClass(adapterClassName)
                        .newInstance();
            }
            return commonAdapter;
        } catch (InstantiationException e) {
            throw new TUServiceInstanciationException("Adapter instanciation problem", e);
        } catch (IllegalAccessException e) {
            throw new TUServiceInstanciationException("Adapter instanciation problem", e);
        } catch (ClassNotFoundException e) {
            throw new TUServiceInstanciationException("Adapter instanciation problem", e);
        }
    }

    /**
     * R�cup�re un service AF
     * 
     * @return
     * @throws TUServiceInstanciationException
     */
    public static TUAFService getAFService() throws TUInitTucanaInterfaceException {
        TUAFService service = null;
        ICommonAdapter adapter = getAdapter(IPropertiesNames.AF_ADAPTER);
        if (adapter != null) {
            service = new TUAFService(adapter);
        } else {
            throw new TUServiceInstanciationException("AF Adapter not defined!");
        }
        return service;
    }

    /**
     * R�cup�re le service CA
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    public static TUCAService getCAService() throws TUInitTucanaInterfaceException {
        TUCAService service = null;
        ICommonAdapter adapter = getAdapter(IPropertiesNames.CA_ADAPTER);
        if (adapter != null) {
            service = new TUCAService(adapter);
        } else {
            throw new TUServiceInstanciationException("CA Adapter not defined!");
        }
        return service;
    }

    /**
     * R�cup�re le service CG
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    public static TUCGService getCGService() throws TUInitTucanaInterfaceException {
        TUCGService service = null;
        ICommonAdapter adapter = getAdapter(IPropertiesNames.CG_ADAPTER);
        if (adapter != null) {
            service = new TUCGService(adapter);
        } else {
            throw new TUServiceInstanciationException("CG Adapter not defined!");
        }
        return service;
    }

}
