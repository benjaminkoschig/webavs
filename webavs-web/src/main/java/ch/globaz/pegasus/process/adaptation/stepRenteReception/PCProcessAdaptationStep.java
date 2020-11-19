package ch.globaz.pegasus.process.adaptation.stepRenteReception;

import globaz.globall.db.BSessionUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.service.HEAnnoncesCentrale;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepEnable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepEnable,
        JadeProcessStepBeforable {

    Map<String, List<IHEOutputAnnonce>> mapAnnonceRentes;
    Map<String, List<RenteAdapationDemande>> rentes;
    Map<String, String> typesRente;

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        rentes = PegasusServiceLocator.getRenteAdapationDemandeService().findByIdProcess(step.getIdExecutionProcess());
        typesRente = createMapTypeRente();
        try {
            mapAnnonceRentes = findAnnonce();
        } catch (Exception e) {
            JadeProcessCommonUtils.addError(e);
        }
    }

    private Map<String, String> createMapTypeRente() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        List<JadeCodeSysteme> listAvs = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(
                "PCGENRREN");

        List<JadeCodeSysteme> listApi = JadeBusinessServiceLocator.getCodeSystemeService().getFamilleCodeSysteme(
                "PCTYPAPI");

        List<JadeCodeSysteme> list = new ArrayList<JadeCodeSysteme>();
        list.addAll(listAvs);
        list.addAll(listApi);
        Map<String, String> map = new HashMap<String, String>();

        for (JadeCodeSysteme jadeCodeSysteme : list) {
            map.put(jadeCodeSysteme.getCodeUtilisateur(Langues.Francais), jadeCodeSysteme.getIdCodeSysteme());
        }
        return map;
    }

    private Map<String, List<IHEOutputAnnonce>> findAnnonce() throws Exception {
        HEAnnoncesCentrale annoncesCentrale = new HEAnnoncesCentrale();

        IHEOutputAnnonce[] annonceRentes = annoncesCentrale.getAnnoncesAdaptationRentesPCForCentrale(BSessionUtil
                .getSessionFromThreadContext().getCurrentThreadTransaction());
        // On regroupe toute les annonces par l'idDroitMembreFamille (referenceInterne)
        Map<String, List<IHEOutputAnnonce>> mapAnnonceRentes = JadeListUtil.groupBy(Arrays.asList(annonceRentes),
                new JadeListUtil.Key<IHEOutputAnnonce>() {
                    @Override
                    public String exec(IHEOutputAnnonce e) {
                        try {
                            return e.getField(IHEAnnoncesViewBean.PC_REFERENCE_INTERNE_OFFICE_PC);
                        } catch (Exception e1) {
                            JadeProcessCommonUtils.addError(e1);
                        }
                        return null;
                    }
                });
        return mapAnnonceRentes;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationEntityHandler(rentes, mapAnnonceRentes, typesRente);
    }

    @Override
    public Boolean isEnabled(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        if ("true".equalsIgnoreCase(map.get(PCProcessAdapationEnum.HAS_ADAPTATION_DES_RENTE))) {
            return true;
        } else {
            return false;
        }
    }

}
