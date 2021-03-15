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
import globaz.jade.client.util.JadeStringUtil;

public class RpcDatasListConverter implements Iterator<RpcDataConverter>, Iterable<RpcDataConverter> {

    private final Iterator<Entry<String, List<RPCDecionsPriseDansLeMois>>> decisions;
    private final Iterator<DecisionRefus> decisionsRefus;
    private final RpcDecisionRequerantConjointConverter rpcDecisionRequerantConjoint;
    private boolean forDecision = false;

    public RpcDatasListConverter(Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision,
            List<DecisionRefus> decisionsRefus, Map<String, RpcAddress> mapAdressesCourrier,
            Map<String, RpcAddress> mapAdressesDomicile, Map<String, RpcAddress> mapAdressesLocalite,
            Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles,
            Map<String, Calcul> mapIdPlanCalculWithCalcul, PersonneAvsConverter personneAvsConverter,
            Parameters parameters, Date dateAnnonceComptable) {

        rpcDecisionRequerantConjoint = new RpcDecisionRequerantConjointConverter(mapAdressesCourrier,
                mapAdressesDomicile, mapAdressesLocalite,
                personneAvsConverter, mapMembresFamilles, mapIdPlanCalculWithCalcul, parameters,
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

    RpcData convertToDomaine(Entry<String, List<RPCDecionsPriseDansLeMois>> entry, DecisionRefus modelRefus) {
        RpcData annonceData = null;
        if(entry != null && !entry.getValue().isEmpty()) {
            VersionDroit versionDroit = toVersionDroit(entry.getValue().get(0).getSimpleVersionDroit());
            Dossier dossier = new Dossier();
            dossier.setId(entry.getValue().get(0).getIdDossier());

            Map<String, List<RPCDecionsPriseDansLeMois>> map = new HashMap<String, List<RPCDecionsPriseDansLeMois>>();

            for (RPCDecionsPriseDansLeMois model : entry.getValue()) {
                if (!map.containsKey(model.keyForGroup())) {
                    map.put(model.keyForGroup(), new ArrayList<RPCDecionsPriseDansLeMois>());
                    map.get(model.keyForGroup()).add(model);
                } else if (isSameRequerant(map.get(model.keyForGroup()).get(0), model)) {
                    // 2 pca pour la même personne sur une même version de droit ? A voir avec les retenues/bloquantes
                    map.put(model.keyForGroup() + "_2", new ArrayList<RPCDecionsPriseDansLeMois>());
                    map.get(model.keyForGroup() + "_2").add(model);
                } else {
                    map.get(model.keyForGroup()).add(model);
                }
            }

            for (Entry<String, List<RPCDecionsPriseDansLeMois>> entryDecisions : map.entrySet()) {

                Demande demande = new Demande();
                demande.setId(entryDecisions.getValue().get(0).getIdDemande());
                demande.setIsFratrie(entryDecisions.getValue().get(0).getIsFratrie());
                if (demande.getIsFratrie() == null) {
                    demande.setIsFratrie(false);
                }
                if (annonceData == null) {
                    annonceData = new RpcData(versionDroit, dossier, demande);
                }
                demande.setEtat(EtatDemande.fromValue(entryDecisions.getValue().get(0).getEtatDemande()));
                String dateDebut = entryDecisions.getValue().get(0).getDateDebutDemande();
                String dateFin = entryDecisions.getValue().get(0).getDateFinDemande();
                String dateArrivee = entryDecisions.getValue().get(0).getDateArriveeDemande();
                demande.setDebut(JadeStringUtil.isEmpty(dateDebut) ? null : new Date(dateDebut));
                demande.setFin(JadeStringUtil.isEmpty(dateFin) ? null : new Date(dateFin));
                demande.setArrivee(JadeStringUtil.isEmpty(dateArrivee) ? null : new Date(dateArrivee));

                annonceData.add(rpcDecisionRequerantConjoint.convert(entryDecisions.getValue(), versionDroit, demande));
            }
        }


//        if(modelRefus != null) {
//            if (annonceData == null) {
//                Dossier dossierRefus = new Dossier();
//                dossierRefus.setId(modelRefus.getDemande().getDossier().getId());
//                Demande demandeRefus = convert(modelRefus.getDemande().getSimpleDemande());
//                annonceData = new RpcData(dossierRefus, demandeRefus);
//            }
//            annonceData.add(rpcDecisionRequerantConjoint.convert(modelRefus));
//        }

        RpcDataDecisionFilter.filtre(annonceData);

        return annonceData;
    }
    
    private boolean isSameRequerant(RPCDecionsPriseDansLeMois model1, RPCDecionsPriseDansLeMois model2) {
        return model1.getNssTiersBeneficiaire().equals(model2.getNssTiersBeneficiaire())
        && model1.getIdTiersRequerant().equals(model2.getIdTiersRequerant())
        && model1.getSimplePCAccordee().getCsRoleBeneficiaire().equals(model2.getSimplePCAccordee().getCsRoleBeneficiaire());
    }

    VersionDroit toVersionDroit(SimpleVersionDroit verDroit) {
        return new VersionDroit(verDroit.getId(), Integer.valueOf(verDroit.getNoVersion()), verDroit.getEtat(),
                verDroit.getMotif(), verDroit.getDemandeInitiale());
    }
}
