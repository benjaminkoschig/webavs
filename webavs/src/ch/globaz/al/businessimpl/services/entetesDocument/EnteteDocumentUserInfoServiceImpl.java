package ch.globaz.al.businessimpl.services.entetesDocument;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.services.entetesDocument.EnteteDocumentUserInfoService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * classe permettant d'ajouter les infos de l'utilisateur à un document
 * 
 * @author PTA
 * 
 */
public class EnteteDocumentUserInfoServiceImpl extends ALAbstractBusinessServiceImpl implements
        EnteteDocumentUserInfoService {

    @Override
    public void addUserInfoToDocument(DocumentData document, HashMap<String, String> infoUser)
            throws JadeApplicationException, JadePersistenceException {
        // Contrôle des paramètres
        if (document == null) {
            throw new ALDocumentException("EnteteDocumentUserInfoServiceImpl#addUserInfoToDocument: document is null") {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;
            };
        }
        if (infoUser == null) {
            throw new ALDocumentException("EnteteDocumentUserInfoServiceImpl#addUserInfoToDocument: infoUser is null") {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;
            };
        }
        if (!JadeStringUtil.isEmpty(infoUser.get(ALConstDocument.USER_NAME))) {
            document.addData(ALConstDocument.NAME_VALUE, infoUser.get(ALConstDocument.USER_NAME));
        }
        if (!JadeStringUtil.isEmpty(infoUser.get(ALConstDocument.USER_PHONE))) {
            document.addData(ALConstDocument.PHONE_VALUE, infoUser.get(ALConstDocument.USER_PHONE));
        }
        if (!JadeStringUtil.isEmpty(infoUser.get(ALConstDocument.USER_MAIL))) {
            document.addData(ALConstDocument.MAIL_VALUE, infoUser.get(ALConstDocument.USER_MAIL));
        }

    }

}
