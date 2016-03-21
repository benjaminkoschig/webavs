package ch.globaz.al.businessimpl.services.rafam;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.al.business.constantes.enumerations.RafamImportProtocolFields;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.services.rafam.AnnonceRafamImportProtocoleService;
import ch.globaz.al.businessimpl.documents.excel.ALAbstractExcelServiceImpl;

/**
 * Implémentation du service permettant la génération d'un protocole lors de l'importation du fichier d'annonces RAFam
 * 
 * @author jts
 * 
 */
public class AnnonceRafamImportProtocoleServiceImpl extends ALAbstractExcelServiceImpl implements
        AnnonceRafamImportProtocoleService {

    @Override
    public String createProtocole(ArrayList<HashMap<RafamImportProtocolFields, String>> protocole)
            throws JadeApplicationException {
        try {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("data", protocole);
            return createDocAndSave(param);
        } catch (Exception e) {
            throw new ALDocumentException(
                    "AnnoncesRafamProtocoleServiceImpl#createProtocole : Unable to create the RAFam protocole : "
                            + e.getMessage(), e);
        }
    }

    @Override
    protected String getModelName() {
        return "protocoleImportAnnoncesRAFam.xml";
    }

    @Override
    protected String getOutputName() {
        return "Protocole_Importation_RAFam_" + JadeDateUtil.getGlobazFormattedDate(new Date());
    }

    @Override
    protected IMergingContainer prepareData(Map<String, Object> parameters)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        CommonExcelmlContainer container = new CommonExcelmlContainer();

        for (HashMap<RafamImportProtocolFields, String> err : (ArrayList<HashMap<RafamImportProtocolFields, String>>) parameters
                .get("data")) {
            container.put("VALUE_RECORD_NUMBER", err.get(RafamImportProtocolFields.RECORD_NUMBER));
            container.put("VALUE_NSS_ALLOCATAIRE", err.get(RafamImportProtocolFields.NSS_ALLOCATAIRE));
            container.put("VALUE_NSS_ENFANT", err.get(RafamImportProtocolFields.NSS_ENFANT));
            container.put("VALUE_ERRORS", err.get(RafamImportProtocolFields.ERRORS));
            container.put("VALUE_MSG_ERR", err.get(RafamImportProtocolFields.MSG_ERR));
        }

        return container;
    }
}
