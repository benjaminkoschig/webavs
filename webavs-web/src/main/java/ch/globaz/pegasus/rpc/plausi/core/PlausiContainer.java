package ch.globaz.pegasus.rpc.plausi.core;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.variablemetier.SimpleVariableMetier;
import ch.globaz.pegasus.business.models.variablemetier.SimpleVariableMetierSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.gz.gz001.RpcPlausiGZ001;
import ch.globaz.pegasus.rpc.plausi.gz.gz002.RpcPlausiGZ002;
import ch.globaz.pegasus.rpc.plausi.gz.gz003.RpcPlausiGZ003;
import ch.globaz.pegasus.rpc.plausi.gz.gz004.RpcPlausiGZ004;
import ch.globaz.pegasus.rpc.plausi.intra.pi002.RpcPlausiPI002;
import ch.globaz.pegasus.rpc.plausi.intra.pi003.RpcPlausiPI003;
import ch.globaz.pegasus.rpc.plausi.intra.pi004.RpcPlausiPI004;
import ch.globaz.pegasus.rpc.plausi.intra.pi008.RpcPlausiPI008;
import ch.globaz.pegasus.rpc.plausi.intra.pi009.RpcPlausiPI009;
import ch.globaz.pegasus.rpc.plausi.intra.pi011.RpcPlausiPI011;
import ch.globaz.pegasus.rpc.plausi.intra.pi013.RpcPlausiPI013;
import ch.globaz.pegasus.rpc.plausi.intra.pi014.RpcPlausiPI014;
import ch.globaz.pegasus.rpc.plausi.intra.pi015.RpcPlausiPI015;
import ch.globaz.pegasus.rpc.plausi.intra.pi021.RpcPlausiPI021;
import ch.globaz.pegasus.rpc.plausi.intra.pi023.RpcPlausiPI023;
import ch.globaz.pegasus.rpc.plausi.intra.pi024.RpcPlausiPI024;
import ch.globaz.pegasus.rpc.plausi.intra.pi025.RpcPlausiPI025;
import ch.globaz.pegasus.rpc.plausi.intra.pi028.RpcPlausiPI028;
import ch.globaz.pegasus.rpc.plausi.intra.pi033.RpcPlausiPI033;
import ch.globaz.pegasus.rpc.plausi.intra.pi042.RpcPlausiPI042;
import ch.globaz.pegasus.rpc.plausi.intra.pi043.RpcPlausiPI043;
import ch.globaz.pegasus.rpc.plausi.intra.pi044.RpcPlausiPI044;
import ch.globaz.pegasus.rpc.plausi.intra.pi046.RpcPlausiPI046;
import ch.globaz.pegasus.rpc.plausi.intra.pi049.RpcPlausiPI049;
import ch.globaz.pegasus.rpc.plausi.intra.pi064.RpcPlausiPI064;
import ch.globaz.pegasus.rpc.plausi.simple.ps010.RpcPlausiPS010;
import ch.globaz.pegasus.rpc.plausi.simple.ps011.RpcPlausiPS011;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class PlausiContainer {

    private List<RpcPlausiMetier<? extends PlausiResult>> listMetier = new ArrayList<RpcPlausiMetier<? extends PlausiResult>>();
    private List<RpcPlausiMetier<? extends PlausiResult>> plausisSkippedOnPrevalidation = new ArrayList<RpcPlausiMetier<? extends PlausiResult>>();

    public PlausiContainer(Date date) throws VariableMetierException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        this(loadVariablesMetier(date));
    }
    
    public PlausiContainer(SimpleVariableMetierSearch variablesMetier) {
        listMetier.add(new RpcPlausiPI002(new Montant(300)));
        listMetier.add(new RpcPlausiPI003());
        listMetier.add(new RpcPlausiPI008(new Montant(300)));
        listMetier.add(new RpcPlausiPI009());
        listMetier.add(new RpcPlausiPI011(new Montant(300)));
        listMetier.add(new RpcPlausiPI013());
        listMetier.add(new RpcPlausiPI014());
        listMetier.add(new RpcPlausiPI015());
        listMetier.add(new RpcPlausiPI028(new Montant(112500), new Montant(300000))); // Par1 & Par2
        listMetier.add(new RpcPlausiPI033());
        listMetier.add(new RpcPlausiPI042());
        listMetier.add(new RpcPlausiPI043());
        listMetier.add(new RpcPlausiPI044());
        listMetier.add(new RpcPlausiPI046(new Montant(300)));
        listMetier.add(new RpcPlausiPI049());
        listMetier.add(new RpcPlausiGZ001());
        listMetier.add(new RpcPlausiGZ002());
        listMetier.add(new RpcPlausiGZ003());
        listMetier.add(new RpcPlausiGZ004());

        listMetier.add(initPlausi064());

        listMetier.add(new RpcPlausiPI004());
        listMetier.add(new RpcPlausiPS010());
        RpcPlausiPS011 plausi11 = new RpcPlausiPS011();
        listMetier.add(plausi11);
        plausisSkippedOnPrevalidation.add(plausi11);
        listMetier.add(new RpcPlausiPI021());
        listMetier.add(new RpcPlausiPI023());
        listMetier.add(new RpcPlausiPI024());
        
        Montant par1 = getMontant(IPCVariableMetier.CS_BESOINS_VITAUX_CELIBATAIRES, variablesMetier);
        Montant par2 = getMontant(IPCVariableMetier.CS_BESOINS_VITAUX_COUPLES, variablesMetier);
        Montant par3 = getMontant(IPCVariableMetier.CS_BESOINS_VITAUX_CELIBATAIRES, variablesMetier);
        Montant par4 = getMontant(IPCVariableMetier.CS_BESOINS_VITAUX_2_ENFANTS, variablesMetier);
        Montant par5 = getMontant(IPCVariableMetier.CS_BESOINS_VITAUX_4_ENFANTS, variablesMetier);
        Montant par6 = getMontant(IPCVariableMetier.CS_BESOINS_VITAUX_5_ENFANTS, variablesMetier);
        listMetier.add(new RpcPlausiPI025(par1, par2, par3, par4, par5, par6));
     // listMetier.add(new RpcPlausiPI027(new Montant(37500), new Montant(60000), new Montant(15000)));
    }

    public PlausisResults buildPlausis(RpcData rpcData) {
        Set<RpcPlausiCategory> inCategory = EnumSet.allOf(RpcPlausiCategory.class);
        return buildPlausisInCategory(rpcData, inCategory, isDateSimulation());
    }
    
    public static boolean isDateSimulation() {
        Map<String, String> simulation;
        try {
            simulation = EPCProperties.RPC_SIMULATION.getValueJson();
            return simulation.get("date") != null;
        } catch (PropertiesException e) {
            return false;
        }
        
    }

    public PlausisResults buildPlausisInCategory(RpcData rpcData, Set<RpcPlausiCategory> inCategory,
            boolean skippedOnPrevalidation) {

        PlausisResults plausisResults = new PlausisResults();

        for (RpcPlausiMetier<? extends PlausiResult> plausi : listMetier) {
            if (inCategory.contains(plausi.getCategory())
                    && !(skippedOnPrevalidation && plausisSkippedOnPrevalidation.contains(plausi))) {
                plausisResults.addAll(buildPlausisMetier(plausi, rpcData));
            }
        }

        return plausisResults;
    }

    static List<PlausiResult> buildPlausisMetier(RpcPlausiMetier<?> plausi, RpcData rpcData) {
        List<PlausiResult> plausisRsults = new ArrayList<PlausiResult>();

        AnnonceCase annonceCase = rpcData.getAnnonce();

        for (AnnonceDecision decision : annonceCase.getDecisions()) {
            RpcPlausiApplyToDecision applyTo = resolveApplyTo(decision.getAnnonce(), rpcData.hasVersionDroit());
            if (plausi.getApplyTo().contains(applyTo)) {
                plausisRsults.add(plausi.buildPlausi(decision, annonceCase));
            }
        }

        return plausisRsults;
    }

    static RpcPlausiApplyToDecision resolveApplyTo(RpcDecisionAnnonceComplete data, boolean hasVersionDroit) {
        if (hasVersionDroit) {
            if (data.getPcaDecision().getPca().getEtatCalcul().isRefus() || TypeDecision.SUPPRESSION_SANS_CALCUL.equals(data.getPcaDecision().getDecision().getType())) {
                return RpcPlausiApplyToDecision.REJECT_FULL;
            } else {
                return RpcPlausiApplyToDecision.POSITIVE;
            }
        } else {
            return RpcPlausiApplyToDecision.REJECT_SMALL;
        }
    }

    private static RpcPlausiPI064 initPlausi064() {
        return new RpcPlausiPI064(new HashMap<Integer, Double>() {
            {
                put(994, (double) 1);
                put(0, ((double) 2 / (double) 3));
            }
        }, new HashMap<Integer, Montant>() {
            {
                put(994, Montant.ZERO);
            }
        });
    }
    
    private Montant getMontant(String codeSystem, SimpleVariableMetierSearch variablesMetier) {
        for (JadeAbstractModel absDonnee : variablesMetier.getSearchResults()) {
            SimpleVariableMetier donnee = (SimpleVariableMetier) absDonnee;
            if (donnee.getCsTypeVariableMetier().equals(codeSystem)) {
                return new Montant(donnee.getMontant());
            }
        }
        return null;
    }
    
    private static SimpleVariableMetierSearch loadVariablesMetier(Date date) throws VariableMetierException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleVariableMetierSearch variablesMetier = new SimpleVariableMetierSearch();
        variablesMetier.setWhereKey("withDateValable");
        variablesMetier.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        variablesMetier.setForDateValable(date.getSwissMonthValue());
        variablesMetier = PegasusImplServiceLocator.getSimpleVariableMetierService().search(variablesMetier);
        return variablesMetier;
    }
    
}