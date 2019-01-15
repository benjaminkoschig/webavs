package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.pca.Calcul;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.PcaConverter;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.decision.DecisionConverter;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.PersonsElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;
import ch.globaz.pegasus.rpc.domaine.RpcCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionRequerantConjoint;
import ch.globaz.pegasus.utils.PCApplicationUtil;
import ch.globaz.pegasus.utils.RpcUtil;
import ch.globaz.pyxis.converter.PersonneAvsConverter;
import ch.globaz.pyxis.domaine.PersonneAVS;
import globaz.jade.client.util.JadeStringUtil;

class RpcDecisionRequerantConjointConverter {
    private static final Logger LOG = LoggerFactory.getLogger(RpcDecisionRequerantConjointConverter.class);

    private final Map<String, RpcAddress> mapAdressesCourrier;
    private final Map<String, RpcAddress> mapAdressesDomicile;
    private final PcaConverter pcaConverter = new PcaConverter();
    private final PersonneElementsCalculConverter personneElementsCalculConverter = new PersonneElementsCalculConverter();
    private final PersonneAvsConverter toPersonneAvs;
    private final DecisionConverter decisionConverter = new DecisionConverter();
    private final Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> membresFamilles;
    private final Map<String, Calcul> mapIdPlanCalculWithCalcul;
    private final Parameters parameters;
    private final Date dateAnnonceComptable;

    public RpcDecisionRequerantConjointConverter(Map<String, RpcAddress> mapAdressesCourrier,
            Map<String, RpcAddress> mapAdressesDomicile, PersonneAvsConverter personneAvsConverter,
            Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles,
            Map<String, Calcul> mapIdPlanCalculWithCalcul, Parameters parameters, Date dateAnnonceComptable) {
        this.mapAdressesCourrier = mapAdressesCourrier;
        this.mapAdressesDomicile = mapAdressesDomicile;
        membresFamilles = mapMembresFamilles;
        this.mapIdPlanCalculWithCalcul = mapIdPlanCalculWithCalcul;
        this.parameters = parameters;
        toPersonneAvs = personneAvsConverter;
        this.dateAnnonceComptable = dateAnnonceComptable;
    }

    public RpcDecisionRequerantConjoint convert(List<RPCDecionsPriseDansLeMois> models, VersionDroit versionDroit,
            Demande demande) {
        Map<String, List<MembreFamilleWithDonneesFinanciere>> membresFamilleDonneesFinanciere = membresFamilles
                .get(models.get(0).getSimpleVersionDroit().getIdVersionDroit());
        RpcDecisionRequerantConjoint decisionRequerantConjoint;
        SimpleDecisionHeader decsion = models.get(0).getSimpleDecisionHeader();
        Date dateFin = null;
        Date dateDebut = new Date(decsion.getDateDebutDecision());
        if (decsion.getDateFinDecision() != null && !decsion.getDateFinDecision().isEmpty()) {
            dateFin = new Date(decsion.getDateFinDecision());
        }
        Map<String, List<MembreFamilleWithDonneesFinanciere>> mapMembresFamilleWithDF = new HashMap<String, List<MembreFamilleWithDonneesFinanciere>>();
        for (Entry<String, List<MembreFamilleWithDonneesFinanciere>> entry : membresFamilleDonneesFinanciere
                .entrySet()) {
            if (!mapMembresFamilleWithDF.containsKey(entry.getKey())) {
                mapMembresFamilleWithDF.put(entry.getKey(), new ArrayList<MembreFamilleWithDonneesFinanciere>());
            }
            for (MembreFamilleWithDonneesFinanciere membreFamille : entry.getValue()) {
                mapMembresFamilleWithDF.get(entry.getKey()).add(membreFamille.filtreForPeriode(dateDebut, dateFin));
            }
        }
        List<MembreFamilleWithDonneesFinanciere> membresFam = mapMembresFamilleWithDF
                .get(models.get(0).getIdPCAccordee());
        Parameters para = parameters.filtreByPeriode(dateDebut);

        if (models.size() == 1) {
            RPCDecionsPriseDansLeMois model = models.get(0);
            PcaDecision pcaDecision = converToPcaDecision(model);
            Calcul calcul = resolvePlanCalcul(model);
            TupleDonneeRapport tauxChangeRentesEtrangere = calcul.getTauxAutresRentesEtranger();
            pcaDecision.getPca().setCalcul(calcul);
            RpcCalcul rpcCalcul = new RpcCalcul(calcul, false);
            PersonsElementsCalcul personsElementsCalculs;
            if(rpcCalcul.isCoupleSepare()) {
                MembreFamilleWithDonneesFinanciere membreChoisi = membresFam.get(0);
                for(MembreFamilleWithDonneesFinanciere membre : membresFam) {
                    if(model.getNssTiersBeneficiaire().equals(membre.getFamille().getPersonne().getNss().toString())) {
                        membreChoisi = membre;
                    }
                }
                personsElementsCalculs =  convertToPersonElementsCalcul(membresFam, dateDebut,
                        membreChoisi.getRoleMembreFamille(), true, tauxChangeRentesEtrangere);
            } else {
                personsElementsCalculs  = convertToPersonElementsCalcul(membresFam, dateDebut,
                        tauxChangeRentesEtrangere);
            }

            decisionRequerantConjoint = new RpcDecisionRequerantConjoint(demande, pcaDecision, rpcCalcul, membresFam,
                    personsElementsCalculs, para.getVariablesMetier());
        } else if (models.size() == 2) {
            Map<String, RPCDecionsPriseDansLeMois> mapByRole = groupByCsRoleFamill(models);
            RPCDecionsPriseDansLeMois modelR = mapByRole.get(RoleMembreFamille.REQUERANT.getValue());
            RPCDecionsPriseDansLeMois modelC = mapByRole.get(RoleMembreFamille.CONJOINT.getValue());
            PcaDecision pcaDecisionR = converToPcaDecision(modelR);
            if (modelC == null) {
                if (models.get(0).getIdTiersRequerant()
                        .equals(models.get(0).getSimpleDecisionHeader().getIdTiersBeneficiaire())) {
                    modelR = models.get(0);
                    modelC = models.get(1);
                } else {
                    modelR = models.get(1);
                    modelC = models.get(0);
                }
            }

            Calcul calculR = resolvePlanCalcul(modelR);
            pcaDecisionR.getPca().setCalcul(calculR);
            TupleDonneeRapport tauxChangeRentesEtrangere = calculR.getTauxAutresRentesEtranger();

            // Il s'agit d'un DOM2R
            if (!JadeStringUtil.isBlankOrZero(modelR.getSimplePCAccordee().getIdPrestationAccordeeConjoint())) {
                RpcCalcul rpcCalculR = new RpcCalcul(calculR, false);

                pcaDecisionR.getPca()
                        .setBeneficiaireConjointDom2R(converToPcaDecision(modelC).getDecision().getTiersBeneficiaire());
                PersonsElementsCalcul personsElementsCalculs = convertToPersonElementsCalcul(membresFam, dateDebut,
                        tauxChangeRentesEtrangere);

                decisionRequerantConjoint = new RpcDecisionRequerantConjoint(demande, pcaDecisionR, rpcCalculR,
                        membresFam, personsElementsCalculs, para.getVariablesMetier());
            } else {

                // // Il s'agit d'un couple séparé par la maladie
                // List<MembreFamilleWithDonneesFinanciere> membresFamC = mapMembresFamilleWithDF.get(modelC
                // .getIdPCAccordee());
                RpcCalcul rpcCalculR = new RpcCalcul(calculR, true);

                Calcul calculC = resolvePlanCalcul(modelC);
                PcaDecision pcaDecisionC = converToPcaDecision(modelC);
                pcaDecisionC.getPca().setCalcul(calculC);
                RpcCalcul rpcCalculC = new RpcCalcul(calculC, true);
                PersonsElementsCalcul personsElementsCalculsR = convertToPersonElementsCalcul(membresFam, dateDebut,
                        RoleMembreFamille.REQUERANT, true, tauxChangeRentesEtrangere);
                PersonsElementsCalcul personsElementsCalculsC = convertToPersonElementsCalcul(membresFam, dateDebut,
                        RoleMembreFamille.CONJOINT, true, tauxChangeRentesEtrangere);

                RpcUtil.suffixDecisionId(pcaDecisionR.getDecision(), pcaDecisionC.getDecision());

                decisionRequerantConjoint = new RpcDecisionRequerantConjoint(demande, pcaDecisionR, rpcCalculR,
                        personsElementsCalculsR, pcaDecisionC, rpcCalculC, personsElementsCalculsC, membresFam,
                        para.getVariablesMetier());
            }

        } else {
            throw new RpcTechnicalException(
                    "Trop de décisions trouvées pour cette version droit: " + versionDroit.getId());
        }
        return decisionRequerantConjoint;
    }

    private PersonsElementsCalcul convertToPersonElementsCalcul(
            List<MembreFamilleWithDonneesFinanciere> membresFamilleWithDonneesFinanciere, Date dateDebut,
            TupleDonneeRapport tauxChangeRentesEtrangere) {
        return this.convertToPersonElementsCalcul(membresFamilleWithDonneesFinanciere, dateDebut, null, false,
                tauxChangeRentesEtrangere);
    }

    private PersonsElementsCalcul convertToPersonElementsCalcul(
            List<MembreFamilleWithDonneesFinanciere> membresFamilleWithDonneesFinanciere, Date dateDebut,
            RoleMembreFamille roleMembreFamille, boolean isCoupleSepare, TupleDonneeRapport tauxChangeRentesEtrangere) {
        List<PersonElementsCalcul> personsElementsCalculs = new ArrayList<PersonElementsCalcul>();
        Parameters para = parameters.filtreByPeriode(dateDebut);
        boolean isCantonValais;
        boolean isLvpc;
        try {
            isCantonValais = PCApplicationUtil.isCantonVS();
            isLvpc = EPCProperties.LVPC.getBooleanValue();
        } catch (CalculException e) {
            throw new RpcTechnicalException(e);
        } catch (PropertiesException e) {
            throw new RpcTechnicalException(e);
        }

        List<MembreFamilleWithDonneesFinanciere> membresFam = membresFamilleWithDonneesFinanciere;

        if (isCoupleSepare) {
            for (MembreFamilleWithDonneesFinanciere membreFamilleWithDonneesFinanciere : membresFamilleWithDonneesFinanciere) {
                if (membreFamilleWithDonneesFinanciere.getRoleMembreFamille().equals(roleMembreFamille)) {
                    membresFam = new ArrayList<MembreFamilleWithDonneesFinanciere>();
                    membresFam.add(membreFamilleWithDonneesFinanciere);
                }
            }
        }

        for (MembreFamilleWithDonneesFinanciere membreFamilleWithDonneesFinanciere : membresFam) {
            /**
             * Comme l'information attendu LivingAdress n'est pas claire, et non connu dans WebAVs, laissée vide pour ne
             * pas la renseigner dans l'XML
             * Il faudrait peut-être aller chercher les adresse des homes.
             */
            // RpcAddress courrier = resolveRpcAddress(mapAdressesCourrier, membreFamilleWithDonneesFinanciere,
            // membresFamilleWithDonneesFinanciere, false);

            RpcAddress domicile = resolveRpcAddress(mapAdressesDomicile, membreFamilleWithDonneesFinanciere,
                    membresFamilleWithDonneesFinanciere, true);

            personsElementsCalculs.add(personneElementsCalculConverter.convert(membreFamilleWithDonneesFinanciere, para,
                    dateDebut, isCantonValais, isLvpc, domicile, new RpcAddress(), roleMembreFamille, isCoupleSepare,
                    tauxChangeRentesEtrangere));
        }

        return new PersonsElementsCalcul(personsElementsCalculs);// .fusionElementsRequerantConjoint(roleMembreFamille);
    }

    private RpcAddress resolveRpcAddress(Map<String, RpcAddress> mapAdresses,
            MembreFamilleWithDonneesFinanciere membreFamilleWithDonneesFinanciere,
            List<MembreFamilleWithDonneesFinanciere> membresFamilleWithDonneesFinanciere, boolean mandatory) {
        RpcAddress address = mapAdresses
                .get(String.valueOf(membreFamilleWithDonneesFinanciere.getFamille().getPersonne().getId()));
        // recherche complémentaire si c'est un enfant
        if (mandatory && (address == null || address.isEmpty())) {
            for (MembreFamilleWithDonneesFinanciere membreFamille : membresFamilleWithDonneesFinanciere) {
                if (membreFamille.isRequerant()) {
                    address = mapAdresses.get(String.valueOf(membreFamille.getFamille().getPersonne().getId()));
                }
            }
        }

        if (address == null) {
            address = new RpcAddress();
        }
        return address;
    }

    private Calcul resolvePlanCalcul(RPCDecionsPriseDansLeMois model) {
        String idPlanCalcul = model.getIdPlanDeCalculParent();
        if (idPlanCalcul == null || "0".equals(idPlanCalcul) || idPlanCalcul.isEmpty() || !mapIdPlanCalculWithCalcul.containsKey(idPlanCalcul)) {
            idPlanCalcul = model.getSimplePlanDeCalcul().getId();
        }
        return mapIdPlanCalculWithCalcul.get(idPlanCalcul);
    }

    public RpcDecisionRequerantConjoint convert(DecisionRefus decisionsRefus) {

        decisionsRefus.getDecisionHeader().getSimpleDecisionHeader()
                .setDateDebutDecision(decisionsRefus.getDemande().getSimpleDemande().getDateDebut());
        decisionsRefus.getDecisionHeader().getSimpleDecisionHeader()
                .setDateFinDecision(decisionsRefus.getDemande().getSimpleDemande().getDateFin());

        Decision decision = decisionConverter.convertToDomain(
                decisionsRefus.getDecisionHeader().getSimpleDecisionHeader(),
                decisionsRefus.getSimpleDecisionRefus().getCsMotif());
        decision.setTiersBeneficiaire(
                toPersonneAvs.convertToDomain(decisionsRefus.getDecisionHeader().getPersonneEtendue()));

        Demande demande = new Demande();
        demande.setId(decisionsRefus.getDemande().getId());
        demande.setIsFratrie(decisionsRefus.getDemande().getSimpleDemande().getIsFratrie());

        PcaDecision pcaDecision = new PcaDecision(decision);

        return new RpcDecisionRequerantConjoint(demande, pcaDecision, null, null, null, null);

    }

    private Map<String, RPCDecionsPriseDansLeMois> groupByCsRoleFamill(
            List<RPCDecionsPriseDansLeMois> decionsPriseDansLeMois) {
        Map<String, RPCDecionsPriseDansLeMois> map = new HashMap<String, RPCDecionsPriseDansLeMois>();
        for (RPCDecionsPriseDansLeMois model : decionsPriseDansLeMois) {
            map.put(model.getCsRoleMembreFamille(), model);
        }
        return map;
    }

    private PcaDecision converToPcaDecision(RPCDecionsPriseDansLeMois model) {
        Decision decision = decisionConverter.convertToDomain(model.getSimpleDecisionHeader(), model.getCsMotif());
        PersonneAVS personneAVS = toPersonneAvs.convertToDomain(model.getSimpleTiers(), model.getSimplePersonne(),
                model.getNssTiersBeneficiaire(), decision.getDateDebut());

        decision.setTiersBeneficiaire(personneAVS);
        Pca pca = pcaConverter.convert(model.getSimplePCAccordee(), model.getSimplePlanDeCalcul(), null);
        pca.setBeneficiaire(decision.getTiersBeneficiaire());
        pca.setDateAnnonceComptable(dateAnnonceComptable);

        return new PcaDecision(pca, decision);
    }

}
