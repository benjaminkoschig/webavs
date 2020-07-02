package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.demande.EtatDemande;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionWithIdPlanCal;

public class RpcDataConverter {
    private static final Logger LOG = LoggerFactory.getLogger(RpcDataConverter.class);

    private DecisionRefus decisionRefus;
    private Entry<String, List<RPCDecionsPriseDansLeMois>> entryDecision;
    private final RpcDatasListConverter rpcDatasListConverter;

    public RpcDataConverter(DecisionRefus decisionRefus, RpcDatasListConverter rpcDatasListConverter) {
        this.decisionRefus = decisionRefus;
        this.rpcDatasListConverter = rpcDatasListConverter;
    }

    public RpcDataConverter(Entry<String, List<RPCDecionsPriseDansLeMois>> entryDecision,
            RpcDatasListConverter rpcDatasListConverter) {
        this.entryDecision = entryDecision;
        this.rpcDatasListConverter = rpcDatasListConverter;
    }

    public RpcData convert() {
        if (isRefus()) {
            return rpcDatasListConverter.convertToDomaine(decisionRefus);

        }
        return rpcDatasListConverter.convertToDomaine(entryDecision, decisionRefus);
    }

    public boolean isRefus() {
        return decisionRefus != null;
    }

    public Dossier getDossier() {
        Dossier dossier = new Dossier();
        if (isRefus()) {
            dossier.setId(decisionRefus.getDemande().getDossier().getId());
        } else {
            dossier.setId(entryDecision.getValue().get(0).getIdDossier());
        }
        return dossier;
    }

    public Demande getDemande() {
        Demande demande = new Demande();
        if (isRefus()) {
            demande.setId(decisionRefus.getDemande().getId());
            demande.setIsFratrie(decisionRefus.getDemande().getSimpleDemande().getIsFratrie());
            demande.setEtat(EtatDemande.fromValue(decisionRefus.getDemande().getSimpleDemande().getCsEtatDemande()));
            String dateDebut = decisionRefus.getDemande().getSimpleDemande().getDateDebut();
            String dateFin = decisionRefus.getDemande().getSimpleDemande().getDateFin();
            demande.setDebut(JadeStringUtil.isEmpty(dateDebut) ? null : new Date(dateDebut));
            demande.setFin(JadeStringUtil.isEmpty(dateFin) ? null : new Date(dateFin));
        } else {
            demande.setId(entryDecision.getValue().get(0).getIdDemande());
            demande.setIsFratrie(entryDecision.getValue().get(0).getIsFratrie());
            demande.setEtat(EtatDemande.fromValue(entryDecision.getValue().get(0).getEtatDemande()));
            String dateDebut = entryDecision.getValue().get(0).getDateFinDemande();
            String dateFin = entryDecision.getValue().get(0).getDateFinDemande();
            demande.setDebut(JadeStringUtil.isEmpty(dateDebut) ? null : new Date(dateDebut));
            demande.setFin(JadeStringUtil.isEmpty(dateFin) ? null : new Date(dateFin));
        }
        if (demande.getIsFratrie() == null) {
            demande.setIsFratrie(false);
        }
        return demande;
    }

    public List<RpcDecisionWithIdPlanCal> getDecisions() {
        List<RpcDecisionWithIdPlanCal> decisions = new ArrayList<RpcDecisionWithIdPlanCal>();
        if (isRefus()) {
            Decision decision = new Decision();
            decision.setId(decisionRefus.getDecisionHeader().getId());
            decisions.add(new RpcDecisionWithIdPlanCal(decision, null, RoleMembreFamille.REQUERANT));
        } else {
            for (RPCDecionsPriseDansLeMois dec : entryDecision.getValue()) {
                Decision decision = new Decision();
                decision.setId(dec.getSimpleDecisionHeader().getId());
                decisions.add(new RpcDecisionWithIdPlanCal(decision, dec.getSimplePlanDeCalcul().getIdPlanDeCalcul(),
                        RoleMembreFamille.fromValue(dec.getSimplePCAccordee().getCsRoleBeneficiaire())));
            }
        }
        return decisions;
    }

    public VersionDroit getVersionDroit() {
        if (isRefus()) {
            return null;
        } else {
            try {
                return rpcDatasListConverter.toVersionDroit(entryDecision.getValue().get(0).getSimpleVersionDroit());
            } catch (Exception e) {
                return null;
            }
        }
    }

    public String description() {
        String description;
        if (isRefus()) {
            description = "Refus NSS: "
                    + decisionRefus.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel() + ", idDemande: "
                    + decisionRefus.getDemande().getId();
        } else {
            description = "AC NSS: " + entryDecision.getValue().get(0).getNssTiersBeneficiaire() + ", idDemande: "
                    + entryDecision.getValue().get(0).getIdDemande();
        }
        return "CONVERTION - " + description;
    }

}
