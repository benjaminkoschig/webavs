package ch.globaz.pegasus.process.adaptation.laprams;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPrecedante;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.pegasus.process.util.AbstractEntity;

public class PCProcessAdaptationEntityHandler extends AbstractEntity implements JadeProcessEntityNeedProperties,
        JadeProcessEntityDataFind<PCProcessAdapationEnum> {

    private Map<Enum<?>, String> properties = null;
    private Map<PCProcessAdapationEnum, String> map = null;

    public PCProcessAdaptationEntityHandler() {

    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        DecisionApresCalcul dc = readDecision(map.get(PCProcessAdapationEnum.ID_DECISION_HEADER));
        generateAnnonceLaprams(dc);

        if (!JadeStringUtil.isBlankOrZero(map.get(PCProcessAdapationEnum.ID_DECISION_HEADER_CONJOINT))) {
            DecisionApresCalcul dcConjoint = readDecision(map.get(PCProcessAdapationEnum.ID_DECISION_HEADER_CONJOINT));
            generateAnnonceLaprams(dcConjoint);
        }

    }

    private void generateAnnonceLaprams(DecisionApresCalcul dc) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, AnnonceException, DecisionException {
        if (dc.getPcAccordee().getSimplePCAccordee().getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
            String dateFin = dc.getPcAccordee().getSimplePCAccordee().getDateFin();
            String dateDebut = dc.getPcAccordee().getSimplePCAccordee().getDateDebut();
            if (JadeStringUtil.isBlankOrZero(dateFin)) {
                dateFin = "12." + dateDebut.substring(3);
            }

            List<PcaForDecompte> pcasReplaced = PcaPrecedante.findPcaToReplaced(dateDebut, dateFin, dc
                    .getVersionDroit().getSimpleDroit().getIdDroit(), dc.getVersionDroit().getSimpleVersionDroit()
                    .getNoVersion());

            List<DecisionApresCalcul> dcs = new ArrayList<DecisionApresCalcul>();
            dcs.add(dc);
            PegasusServiceLocator.getValidationDecisionService().createAnnoncesLaprams(dcs, pcasReplaced);
        }
    }

    private DecisionApresCalcul readDecision(String idDecisionHeader) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException {
        DecisionApresCalculSearch search = new DecisionApresCalculSearch();
        search.setForIdDecisionHeader(idDecisionHeader);
        search = PegasusServiceLocator.getDecisionApresCalculService().search(search);
        if (search.getSize() > 1) {
            throw new DecisionException("Trop de décision trouver avec cette id:" + search.getForIdDecisionHeader());
        } else if (search.getSize() == 0) {
            throw new DecisionException("Aucune décision trouvé avec cette id:" + search.getForIdDecisionHeader());
        }

        DecisionApresCalcul dc = (DecisionApresCalcul) search.getSearchResults()[0];
        return dc;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

    @Override
    public void setData(Map<PCProcessAdapationEnum, String> hashMap) {
        map = hashMap;
    }
}
