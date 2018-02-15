package globaz.pegasus.vb.rpc;

import java.io.Serializable;
import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceConverter;
import ch.globaz.pegasus.rpc.domaine.Annonce;
import ch.globaz.pegasus.rpc.domaine.AnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionWithIdPlanCal;
import ch.globaz.pyxis.domaine.PersonneAVS;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

public class PCPersonneAnnonceBean implements Serializable, DomainEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AnnonceRpc annonce = new Annonce();
    private PersonneAVS personneAVS = new PersonneAVS();
    private BSession session;

    public PCPersonneAnnonceBean() {

    }

    public PCPersonneAnnonceBean(AnnonceRpc annonce, PersonneAVS personneAVS, BISession session) {
        this.annonce = annonce;
        this.personneAVS = personneAVS;
        this.session = (BSession) session;
    }

    public AnnonceRpc getAnnonce() {
        return annonce;
    }

    public PersonneAVS getPersonne() {
        return personneAVS;
    }

    public String getEtat() {
        return AnnonceConverter.translate(annonce.getEtat(), session);
    }

    public String getCodeTraitement() {
        return AnnonceConverter.translate(annonce.getCodeTraitement(), session);
    }

    public String getPersonneDetail() {
        return RETiersForJspUtils.getInstance(session).getDetailsTiers(personneAVS, true, true);
    }

    public String getDateAnnonce() {
        if (annonce.getLot().getDateEnvoi() != null) {
            return annonce.getLot().getDateEnvoi().getSwissValue();
        }
        return null;
    }

    public String getIdVersionDroit() {
        if (annonce.getVersionDroit() != null) {
            return annonce.getVersionDroit().getId();
        }
        return null;
    }

    public Integer getNumeroVersionDroit() {
        if (annonce.getVersionDroit() != null) {
            return annonce.getVersionDroit().getNumero();
        }
        return null;
    }

    private RpcDecisionWithIdPlanCal resolveDecisionConjoint() {
        for (RpcDecisionWithIdPlanCal decision : annonce.getDecisions()) {
            if (decision.getRoleMembreFamille().isConjoint()) {
                return decision;
            }
        }
        return null;
    }

    private RpcDecisionWithIdPlanCal resolveDecisionRequerant() {
        if (annonce.getDecisions() != null && !annonce.getDecisions().isEmpty()) {
            RpcDecisionWithIdPlanCal decision = annonce.getDecisions().iterator().next();
            if (decision.getRoleMembreFamille().isRequerant()) {
                return decision;
            } else if (annonce.getDecisions().size() > 1) {
                return annonce.getDecisions().iterator().next();
            }
        }
        return null;

    }

    public String getIdTiersRequerant() {
        RpcDecisionWithIdPlanCal decision = resolveDecisionRequerant();
        if (decision != null) {
            return decision.getDecision().getTiersBeneficiaire().getId().toString();
        }
        return null;
    }

    public String getIdTiersConjoint() {
        RpcDecisionWithIdPlanCal decision = resolveDecisionConjoint();
        if (decision != null) {
            return decision.getDecision().getTiersBeneficiaire().getId().toString();
        }
        return null;
    }

    public String getIdPlanCalculRequerant() {
        RpcDecisionWithIdPlanCal decision = resolveDecisionRequerant();
        if (decision != null) {
            return decision.getIdPlanCalcul();
        }
        return null;
    }

    public String getIdPlanCalculConjoint() {
        RpcDecisionWithIdPlanCal decision = resolveDecisionConjoint();
        if (decision != null) {
            return decision.getIdPlanCalcul();
        }
        return null;
    }

    public boolean getIsCoupleSepare() {
        return annonce.getDecisions() != null && annonce.getDecisions().size() > 1;
    }

    public boolean getHasVersionDroit() {
        return annonce.getVersionDroit() != null;
    }

    public String getIdDemande() {
        return annonce.getDemande().getId();
    }

    public String getIdDossier() {
        return annonce.getDossier().getId();
    }

    @Override
    public String getId() {
        return annonce.getId();
    }

    @Override
    public void setId(String id) {
        annonce.setId(id);
    }

    @Override
    public String getSpy() {
        return annonce.getSpy();
    }

    @Override
    public void setSpy(String spy) {
        annonce.setSpy(spy);
    }
}
