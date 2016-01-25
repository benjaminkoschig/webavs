/**
 * 
 */
package ch.globaz.al.utils;

import globaz.jade.common.JadeClassCastException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedDocument;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import java.util.List;

/**
 * Classe utilitaire lié aux documents
 * 
 * @author pta
 * 
 */
public class ALDocumentUtils {

    /**
     * Méthode qui retourne le type de document en fonction du numéro inform
     * 
     * @param numeroInforom
     * @return
     * @throws JadeApplicationException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws NullPointerException
     * @throws ClassCastException
     * @throws JadeClassCastException
     */
    public static String getDocumentType(String numeroInforom) throws JadeApplicationException,
            JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {

        if (JadeGedFacade.isInstalled()) {
            List<?> documents = JadeGedFacade.getDocumentList();

            for (Object object : documents) {

                JadeGedDocument document = (JadeGedDocument) object;
                if (document.getApplicationDocumentType().equalsIgnoreCase(numeroInforom)) {
                    return document.getGedDocumentType();
                }

            }
        }
        return "";
    }

}
