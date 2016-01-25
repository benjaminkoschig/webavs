package globaz.itucana.model;

import globaz.itucana.constantes.IPropertiesNames;
import globaz.itucana.exception.TUInitPropertiesException;
import globaz.itucana.exception.TUInitTucanaInterfaceException;
import globaz.itucana.exception.TUModelInstanciationException;
import globaz.itucana.properties.TUPropertiesProvider;

/**
 * Classe permettant de charger les mod�les
 * 
 * @author fgo date de cr�ation : 7 juin 2006
 * @version : version 1.0
 * 
 */
public abstract class TUModelLoader {

    /**
     * r�cup�re une instance de mod�le
     * 
     * @param iCommonModelClazz
     * @param clazzPropertyName
     * @return
     * @throws TUInitPropertiesException
     * @throws TUModelInstanciationException
     */
    private static ITUModelBouclement getModelInstance(String clazzPropertyName) throws TUInitPropertiesException,
            TUModelInstanciationException {
        try {
            return (ITUModelBouclement) TUModelLoader.class.getClassLoader()
                    .loadClass(TUPropertiesProvider.getInstance().getProperty(clazzPropertyName)).newInstance();

            //
            // (ITUModelBouclement) ClassLoader
            // .getSystemClassLoader()
            // .loadClass(TUPropertiesProvider.getInstance().getProperty(clazzPropertyName))
            // .newInstance();
        } catch (ClassNotFoundException e) {
            throw new TUModelInstanciationException(e);
        } catch (InstantiationException e) {
            throw new TUModelInstanciationException(e);
        } catch (IllegalAccessException e) {
            throw new TUModelInstanciationException(e);
        }
    }

    /**
     * R�cup�re un mod�le bouclement
     * 
     * @return
     * @throws TUInitTucanaInterfaceException
     */
    public static ITUModelBouclement getNewBouclementModel() throws TUInitTucanaInterfaceException {
        return getModelInstance(IPropertiesNames.MODEL_BOUCLEMENT);
    }
}
