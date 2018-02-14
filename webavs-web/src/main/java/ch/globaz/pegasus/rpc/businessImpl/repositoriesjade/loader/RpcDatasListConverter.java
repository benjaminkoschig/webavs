package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.demande.EtatDemande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.pca.Calcul;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pyxis.converter.PersonneAvsConverter;

public class RpcDatasListConverter implements Iterator<RpcDataConverter>, Iterable<RpcDataConverter> {

    private final Iterator<Entry<String, List<RPCDecionsPriseDansLeMois>>> decisions;
    private final Iterator<DecisionRefus> decisionsRefus;
    private final RpcDecisionRequerantConjointConverter rpcDecisionRequerantConjoint;
    private boolean forDecision = false;

    public RpcDatasListConverter(Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision,
            List<DecisionRefus> decisionsRefus, Map<String, RpcAddress> mapAdressesCourrier,
            Map<String, RpcAddress> mapAdressesDomicile,
            Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles,
            Map<String, Calcul> mapIdPlanCalculWithCalcul, PersonneAvsConverter personneAvsConverter,
            Parameters parameters, Date dateAnnonceComptable) {

        rpcDecisionRequerantConjoint = new RpcDecisionRequerantConjointConverter(mapAdressesCourrier,
                mapAdressesDomicile, personneAvsConverter, mapMembresFamilles, mapIdPlanCalculWithCalcul, parameters,
                dateAnnonceComptable);
        decisions = mapDecision.entrySet().iterator();
        this.decisionsRefus = decisionsRefus.iterator();
    }

    @Override
    public Iterator<RpcDataConverter> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (decisionsRefus.hasNext()) {
            forDecision = false;
            return true;
        } else if (decisions.hasNext()) {
            forDecision = true;
            return true;
        }
        return false;
    }

    RpcData read() {
        if (decisions.hasNext()) {
            return new RpcDataConverter(decisions.next(), this).convert();
        } else if (decisionsRefus.hasNext()) {
            return new RpcDataConverter(decisionsRefus.next(), this).convert();
        }
        return null;
    }

    @Override
    public RpcDataConverter next() {
        if (forDecision) {
            return new RpcDataConverter(decisions.next(), this);
        }
        return new RpcDataConverter(decisionsRefus.next(), this);
    }

    @Override
    public void remove() {
        throw new RpcTechnicalException("Operation not supported !");
    }

    private Demande convert(SimpleDemande model) {
        Demande demande = new Demande();
        demande.setId(model.getIdDemande());
        demande.setDebut(new Date(model.getDateDebut()));
        if (model.getDateFin() != null && !model.getDateFin().isEmpty()) {
            demande.setFin(new Date(model.getDateFin()));
        }
        demande.setEtat(EtatDemande.fromValue(model.getCsEtatDemande()));
        return demande;
    }

    RpcData convertToDomaine(DecisionRefus model) {
        Dossier dossier = new Dossier();
        dossier.setId(model.getDemande().getDossier().getId());
        Demande demande = convert(model.getDemande().getSimpleDemande());
        RpcData annonceData = new RpcData(dossier, demande);
        annonceData.add(rpcDecisionRequerantConjoint.convert(model));
        return annonceData;
    }

    RpcData convertToDomaine(Entry<String, List<RPCDecionsPriseDansLeMois>> entry) {
        VersionDroit versionDroit = toVersionDroit(entry.getValue().get(0).getSimpleVersionDroit());
        Dossier dossier = new Dossier();
        dossier.setId(entry.getValue().get(0).getIdDossier());
        Demande demande = new Demande();
        demande.setId(entry.getValue().get(0).getIdDemande());
        demande.setIsFratrie(entry.getValue().get(0).getIsFratrie());
        if (demande.getIsFratrie() == null) {
            demande.setIsFratrie(false);
        }

        RpcData annonceData = new RpcData(versionDroit, dossier, demande);

        Map<String, List<RPCDecionsPriseDansLeMois>> map = new HashMap<String, List<RPCDecionsPriseDansLeMois>>();

        for (RPCDecionsPriseDansLeMois model : entry.getValue()) {
            if (!map.containsKey(model.keyForGroup())) {
                map.put(model.keyForGroup(), new ArrayList<RPCDecionsPriseDansLeMois>());
            }
            map.get(model.keyForGroup()).add(model);
        }

        for (Entry<String, List<RPCDecionsPriseDansLeMois>> entryDecisions : map.entrySet()) {
            annonceData.add(rpcDecisionRequerantConjoint.convert(entryDecisions.getValue(), versionDroit, demande));
        }
        RpcDataDecisionFilter.filtre(annonceData);
        return annonceData;
    }

    VersionDroit toVersionDroit(SimpleVersionDroit verDroit) {
        return new VersionDroit(verDroit.getId(), Integer.valueOf(verDroit.getNoVersion()), verDroit.getEtat(),
                verDroit.getMotif());
    }
}