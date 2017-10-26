package ch.globaz.pegasus.rpc.domaine;

import java.util.Collection;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.repository.DomainEntity;

public interface LotAnnonceRpc extends DomainEntity {

    void addTodayAtDateEnvoi();

    void addAnnonce(AnnonceRpc annonce);

    void changeEtatToGenerationTermine();

    TypeLot getType();

    Collection<AnnonceRpc> getAnnonces();

    Date getDateEnvoi();

    EtatLot getEtat();

    String getIdJob();

    @Override
    void setId(String id);

    @Override
    String getId();

}