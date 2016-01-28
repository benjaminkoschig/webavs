package ch.globaz.al.businessimpl.services.dossiers;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.dossiers.RadiationAutomatiqueDossiersProtocoleService;
import ch.globaz.al.business.services.dossiers.RadiationAutomatiqueService;
import ch.globaz.al.businessimpl.documents.excel.ALAbstractExcelServiceImpl;
import ch.globaz.al.businessimpl.documents.excel.ALContainer;

/**
 * Implémentation du service permettant la génération d'un protocole lors de la radiation automatique de dossier (
 * {@link RadiationAutomatiqueService})
 * 
 * @author jts
 * 
 */
public class RadiationAutomatiqueDossiersProtocoleServiceImpl extends ALAbstractExcelServiceImpl implements
        RadiationAutomatiqueDossiersProtocoleService {

    @Override
    public String createProtocole(List<RadiationAutomatiqueResult> log, HashMap<String, String> errors)
            throws JadeApplicationException {
        try {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("data", log);
            param.put("errors", errors);
            return createDocAndSave(param);
        } catch (Exception e) {
            throw new ALDocumentException(
                    "RadiationAutomatiqueDossiersProtocoleServiceImpl#createProtocole : Unable to create the protocole : "
                            + e.getMessage(), e);
        }
    }

    @Override
    protected String getModelName() {
        return "protocoleRadiationAutomatiqueDossiers.xml";
    }

    @Override
    protected String getOutputName() {
        return "Protocole_RadiationAutomatiqueDossiers_" + JadeDateUtil.getGlobazFormattedDate(new Date());
    }

    @Override
    protected IMergingContainer prepareData(Map<String, Object> parameters)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        ALContainer container = new ALContainer();

        List<RadiationAutomatiqueResult> list = (List<RadiationAutomatiqueResult>) parameters.get("data");
        Collections.sort(list);

        for (RadiationAutomatiqueResult result : list) {

            DossierComplexModel dossier = result.getDossier();
            String motif = result.getMotifRadiation();

            container.put("VALUE_AFFILIE", dossier.getDossierModel().getNumeroAffilie());
            container.put("VALUE_NSS", dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                    .getPersonneEtendue().getNumAvsActuel());
            container.put("VALUE_NOM", dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                    .getDesignation1());
            container.put("VALUE_PRENOM", dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                    .getTiers().getDesignation2());
            container.put("VALUE_MOTIF", JadeCodesSystemsUtil.getCodeLibelle(motif));
            container.put("VALUE_DOSSIER", dossier.getId());
        }

        for (Entry<String, String> entry : ((HashMap<String, String>) parameters.get("errors")).entrySet()) {
            container.put("VALUE_ERR_DOSSIER", entry.getKey());
            container.put("VALUE_ERR", entry.getValue());
        }

        return container;
    }

}
