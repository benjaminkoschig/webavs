package globaz.cygnus.process.adaptationAnnuelle.octroi;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationBeforable;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;
import ch.horizon.jaspe.model.JAException;
import com.google.gson.Gson;

public class RFAdaptationAnnuellePopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<RFProcessAdaptationAnnuelleEnum>, JadeProcessPopulationBeforable {

    private static final String DESCRIPTION_ENTITE_SEPARATEUR = " / ";
    private static final String DESCRIPTION_ENTITE_VIDE = " - ";
    private Map<RFProcessAdaptationAnnuelleEnum, String> properties = null;

    @Override
    public void before() throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public Class<RFProcessAdaptationAnnuelleEnum> getEnumForProperties() {
        return RFProcessAdaptationAnnuelleEnum.class;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity arg0) throws JadePersistenceException, JadeApplicationException {
        return null;
    }

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {

        List<JadeProcessEntity> list = new ArrayList<JadeProcessEntity>();

        List<DecisionPcVO> searchDecisionsByDateValidation = PegasusServiceLocator.getDecisionService()
                .searchDecisionsAdaptation();

        Set<String> idsVersionDroitSet = new HashSet<String>();

        Gson gson = new Gson();
        for (DecisionPcVO pcVOs : searchDecisionsByDateValidation) {
            if (pcVOs.getCsTypeDecision().equals(IPCDecision.CS_TYPE_ADAPTATION_AC)
                    && pcVOs.getNoDecision().substring(0, 4).equals(String.valueOf(JACalendar.today().getYear()))) {
                JadeProcessEntity entity = new JadeProcessEntity();
                entity.setIdRef(pcVOs.getIdDecision());
                entity.setDescription(buildDescription(pcVOs));
                entity.setValue1(gson.toJson(pcVOs));
                list.add(entity);
                // Recherche du nombre de demandes PC correspondant aux décisions
                idsVersionDroitSet.add(pcVOs.getIdVersionDroitApc());
            }
        }

        try {
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                    "Process adaptation annuelle RFM: Population",
                    "Nombre de demandes PC: " + String.valueOf(idsVersionDroitSet.size()), null);
        } catch (Exception e) {
            throw new JadePersistenceException(e.getMessage());
        }

        return list;
    }

    @Override
    public void setProperties(Map<RFProcessAdaptationAnnuelleEnum, String> hashMap) {
        properties = hashMap;

    }

    private String buildDescription(DecisionPcVO pcVOs) throws JadePersistenceException {

        StringBuffer descriptionStrBfr = new StringBuffer();
        descriptionStrBfr.append(pcVOs.getNss());
        descriptionStrBfr.append(RFAdaptationAnnuellePopulation.DESCRIPTION_ENTITE_SEPARATEUR);
        descriptionStrBfr.append(retrieveTiersBeneficaire(pcVOs.getIdTiersBeneficiaire()));

        return descriptionStrBfr.toString();
    }

    private String retrieveTiersBeneficaire(String idTiers) throws JadePersistenceException {

        try {
            StringBuffer nomPrenom = new StringBuffer();
            PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersParId(BSessionUtil.getSessionFromThreadContext(),
                    idTiers);
            nomPrenom.append(tiersWrapper.getNom());
            nomPrenom.append(" ");
            nomPrenom.append(tiersWrapper.getPrenom());
            return nomPrenom.toString();
        } catch (JAException e) {
            return RFAdaptationAnnuellePopulation.DESCRIPTION_ENTITE_VIDE;
        } catch (Exception e) {
            throw new JadePersistenceException(e.getMessage());
        }
    }

}
