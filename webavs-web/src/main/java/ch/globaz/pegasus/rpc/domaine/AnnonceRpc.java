package ch.globaz.pegasus.rpc.domaine;

import java.util.Collection;
import java.util.List;
import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;

public interface AnnonceRpc extends DomainEntity {

    EtatAnnonce getEtat();

    void setEtat(EtatAnnonce etat);

    CodeTraitement getCodeTraitement();

    void setCodeTraitement(CodeTraitement codeTraitement);

    Collection<RpcDecisionWithIdPlanCal> getDecisions();

    void setDecisions(List<RpcDecisionWithIdPlanCal> decisions);

    Dossier getDossier();

    VersionDroit getVersionDroit();

    void setDossier(Dossier dossier);

    void setDemande(Demande demande);

    Demande getDemande();

    void setVersionDroit(VersionDroit versionDroit);

    LotAnnonceRpc getLot();

    void setLot(LotAnnonceRpc lot);

    TypeAnnonce getType();

    void setType(TypeAnnonce type);
}