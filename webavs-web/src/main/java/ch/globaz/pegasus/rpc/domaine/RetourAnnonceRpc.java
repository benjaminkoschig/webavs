package ch.globaz.pegasus.rpc.domaine;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.pegasus.rpc.business.models.SimpleLienAnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public interface RetourAnnonceRpc extends DomainEntity {

    void changeStatusTo(StatusRetourAnnonce etat);

    RpcPlausiType getType();

    TypeViolationPlausi getTypeViolationPlausi();

    AnnonceRpc getAnnonce();

    StatusRetourAnnonce getStatus();

    String getIdLien();

    RpcPlausiCategory getCategorie();

    String getCodePlausi();

    String getOfficePC();

    String getOfficePCConflit();

    String getNssAnnonce();

    String getNssPersonne();

    String getCaseIdConflit();

    String getDecisionIdConflit();

    Date getValidFromConflit();

    Date getValidToConflit();

    String getRemarque();

    void setRemarque(String remarque);

    SimpleLienAnnonceDecision getIdLienAnnonceDecision();

    void setIdLienAnnonceDecision(SimpleLienAnnonceDecision idLienAnnonceDecision);

    void setIdLien(String idLien);

    @Override
    void setId(String id);

    @Override
    String getId();

}