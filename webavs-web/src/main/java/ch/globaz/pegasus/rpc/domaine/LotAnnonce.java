package ch.globaz.pegasus.rpc.domaine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.pegasus.business.domaine.exception.PegasusBusinessDomainException;

public class LotAnnonce implements DomainEntity, LotAnnonceRpc {
    private String id;
    private String idJob;
    private Date dateEnvoi;
    private EtatLot etat;
    private TypeLot type;
    private String spy;
    private Collection<AnnonceRpc> annonces = Collections.synchronizedCollection(new ArrayList<AnnonceRpc>());

    public LotAnnonce() {

    }

    public LotAnnonce(String id, Date dateEnvoi, EtatLot etat, String idJob) {
        this.id = id;
        this.dateEnvoi = dateEnvoi;
        this.etat = etat;
        this.idJob = idJob;
    }

    public LotAnnonce(Date dateEnvoi, EtatLot etat, String idJob, TypeLot type) {
        this.dateEnvoi = dateEnvoi;
        this.etat = etat;
        this.idJob = idJob;
        this.type = type;
    }

    @Override
    public void changeEtatToGenerationTermine() {
        etat = EtatLot.GENERATION_TEMINE;
    }

    @Override
    public void addTodayAtDateEnvoi() {
        if (dateEnvoi == null) {
            dateEnvoi = Date.now();
        } else {
            throw new PegasusBusinessDomainException(
                    "IL n'est pas possible de changer la date d'envoi car une date est déja existante ("
                            + dateEnvoi.getSwissValue() + ")");
        }
    }

    @Override
    public String getIdJob() {
        return idJob;
    }

    @Override
    public void addAnnonce(AnnonceRpc annonce) {
        annonces.add(annonce);
    }

    @Override
    public TypeLot getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Collection<AnnonceRpc> getAnnonces() {
        return annonces;
    }

    @Override
    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    @Override
    public EtatLot getEtat() {
        return etat;
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
