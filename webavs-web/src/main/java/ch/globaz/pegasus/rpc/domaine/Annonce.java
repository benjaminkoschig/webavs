package ch.globaz.pegasus.rpc.domaine;

import java.util.Collection;
import java.util.List;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;

public class Annonce implements AnnonceRpc {
    private String id;
    private EtatAnnonce etat;
    private CodeTraitement codeTraitement;
    private Collection<RpcDecisionWithIdPlanCal> decisions;
    private TypeAnnonce type;
    private Dossier dossier;
    private String spy;
    private LotAnnonceRpc lot;
    private Demande demande;
    private VersionDroit versionDroit;

    @Override
    public Demande getDemande() {
        return demande;
    }

    @Override
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public VersionDroit getVersionDroit() {
        return versionDroit;
    }

    @Override
    public void setVersionDroit(VersionDroit versionDroit) {
        this.versionDroit = versionDroit;
    }

    @Override
    public EtatAnnonce getEtat() {
        return etat;
    }

    @Override
    public void setEtat(EtatAnnonce etat) {
        this.etat = etat;
    }

    @Override
    public CodeTraitement getCodeTraitement() {
        return codeTraitement;
    }

    @Override
    public void setCodeTraitement(CodeTraitement codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    @Override
    public Collection<RpcDecisionWithIdPlanCal> getDecisions() {
        return decisions;
    }

    @Override
    public void setDecisions(List<RpcDecisionWithIdPlanCal> decisions) {
        this.decisions = decisions;
    }

    @Override
    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public TypeAnnonce getType() {
        return type;
    }

    @Override
    public void setType(TypeAnnonce type) {
        this.type = type;
    }

    @Override
    public LotAnnonceRpc getLot() {
        return lot;
    }

    @Override
    public void setLot(LotAnnonceRpc lot) {
        this.lot = lot;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }
}
