package globaz.corvus.itext;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordeesListeRecap;
import globaz.corvus.db.rentesaccordees.RERecapitulationPaiementManager;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.db.retenues.RERetenuesPaiementManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.prestation.business.constantes.CodeRecapitulationPcRfm;

public class RERecapitulationPaiementAdapter {

    private String forMois;

    private FWCurrency montantAPIAI = new FWCurrency("0.00");
    private FWCurrency montantAPIAVS = new FWCurrency("0.00");
    private FWCurrency montantPCAIStandard = new FWCurrency("0.00");
    private FWCurrency montantPCAVSStandard = new FWCurrency("0.00");
    private FWCurrency montantREOAI = new FWCurrency("0.00");
    private FWCurrency montantREOAVS = new FWCurrency("0.00");
    private FWCurrency montantRFMAI = new FWCurrency("0.00");
    private FWCurrency montantRFMAVS = new FWCurrency("0.00");
    private FWCurrency montantROAI = new FWCurrency("0.00");
    private FWCurrency montantROAVS = new FWCurrency("0.00");
    private FWCurrency montantTotal2AI = new FWCurrency("0.00");
    private FWCurrency montantTotal2AVS = new FWCurrency("0.00");
    private FWCurrency montantTotal2General = new FWCurrency("0.00");
    private FWCurrency montantTotalAI = new FWCurrency("0.00");
    private FWCurrency montantTotalAVS = new FWCurrency("0.00");
    private FWCurrency montantTotalGeneral = new FWCurrency("0.00");
    private FWCurrency montantPCAVSAllocationNoel = new FWCurrency("0.00");
    private FWCurrency montantPCAIAllocationNoel = new FWCurrency("0.00");

    private int nbRAAPIAI;
    private int nbRAAPIAVS;
    private int nbRAPCAIStandard;
    private int nbRAPCAVSStandard;
    private int nbRAREOAI;
    private int nbRAREOAVS;
    private int nbRARFMAI;
    private int nbRARFMAVS;
    private int nbRAROAI;
    private int nbRAROAVS;
    private int nbTotal2AI; // PC / RFM
    private int nbTotal2AVS; // PC / RFM
    private int nbTotal2General; // PC / RFM
    private int nbTotalAI;
    private int nbTotalAVS;
    private int nbTotalGeneral;
    private int nbRAPCAVSAllocationNoel;
    private int nbRAPCAIAllocationNoel;

    private FWCurrency retenuesAPIAI = new FWCurrency("0.00");
    private FWCurrency retenuesAPIAVS = new FWCurrency("0.00");
    private FWCurrency retenuesPCAIStandard = new FWCurrency("0.00");
    private FWCurrency retenuesPCAVSStandard = new FWCurrency("0.00");
    private FWCurrency retenuesREOAI = new FWCurrency("0.00");
    private FWCurrency retenuesREOAVS = new FWCurrency("0.00");
    private FWCurrency retenuesROAI = new FWCurrency("0.00");
    private FWCurrency retenuesROAVS = new FWCurrency("0.00");
    private FWCurrency retenuesTotal2AI = new FWCurrency("0.00");
    private FWCurrency retenuesTotal2AVS = new FWCurrency("0.00");
    private FWCurrency retenuesTotal2General = new FWCurrency("0.00");
    private FWCurrency retenuesTotalAI = new FWCurrency("0.00");
    private FWCurrency retenuesTotalAVS = new FWCurrency("0.00");
    private FWCurrency retenuesTotalGeneral = new FWCurrency("0.00");
    private FWCurrency retenuesPCAVSAllocationNoel = new FWCurrency("0.00");
    private FWCurrency retenuesPCAIAllocationNoel = new FWCurrency("0.00");

    private BSession session;

    public RERecapitulationPaiementAdapter(BSession session, String forMoisAnneeComptable) {
        this.session = session;
        forMois = forMoisAnneeComptable;
    }

    public void addMontantAPIAI(FWCurrency i) {
        montantAPIAI.add(i);
        montantTotalAI.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantAPIAVS(FWCurrency i) {
        montantAPIAVS.add(i);
        montantTotalAVS.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantPCAIStandard(FWCurrency i) {
        montantPCAIStandard.add(i);
        montantTotal2AI.add(i);
        montantTotal2General.add(i);
    }

    public void addMontantPCAIAllocationNoel(FWCurrency i) {
        montantPCAIAllocationNoel.add(i);
        montantTotal2AI.add(i);
        montantTotal2General.add(i);
    }

    public void addMontantPCAVSStandard(FWCurrency i) {
        montantPCAVSStandard.add(i);
        montantTotal2AVS.add(i);
        montantTotal2General.add(i);
    }

    public void addMontantPCAVSAllocationNoel(FWCurrency i) {
        montantPCAVSAllocationNoel.add(i);
        montantTotal2AVS.add(i);
        montantTotal2General.add(i);
    }

    public void addMontantREOAI(FWCurrency i) {
        montantREOAI.add(i);
        montantTotalAI.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantREOAVS(FWCurrency i) {
        montantREOAVS.add(i);
        montantTotalAVS.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantRFMAI(FWCurrency i) {
        montantRFMAI.add(i);
        montantTotal2AI.add(i);
        montantTotal2General.add(i);
    }

    public void addMontantRFMAVS(FWCurrency i) {
        montantRFMAVS.add(i);
        montantTotal2AVS.add(i);
        montantTotal2General.add(i);
    }

    public void addMontantROAI(FWCurrency i) {
        montantROAI.add(i);
        montantTotalAI.add(i);
        montantTotalGeneral.add(i);
    }

    public void addMontantROAVS(FWCurrency i) {
        montantROAVS.add(i);
        montantTotalAVS.add(i);
        montantTotalGeneral.add(i);
    }

    public void addNbRAAPIAI(int i) {
        nbRAAPIAI += i;
        nbTotalAI += i;
        nbTotalGeneral += i;
    }

    public void addNbRAAPIAVS(int i) {
        nbRAAPIAVS += i;
        nbTotalAVS += i;
        nbTotalGeneral += i;
    }

    public void addNbRAPCAIStandard(int i) {
        nbRAPCAIStandard += i;
        nbTotal2AI += i;
        nbTotal2General += i;
    }

    public void addNbRAPCAIAllocationNoel(int i) {
        nbRAPCAIAllocationNoel += i;
        nbTotal2AI += i;
        nbTotal2General += i;
    }

    public void addNbRAPCAVSStandard(int i) {
        nbRAPCAVSStandard += i;
        nbTotal2AVS += i;
        nbTotal2General += i;
    }

    public void addnbRAPCAVSAllocationNoel(int i) {
        nbRAPCAVSAllocationNoel += i;
        nbTotal2AVS += i;
        nbTotal2General += i;
    }

    public void addNbRAREOAI(int i) {
        nbRAREOAI += i;
        nbTotalAI += i;
        nbTotalGeneral += i;
    }

    public void addNbRAREOAVS(int i) {
        nbRAREOAVS += i;
        nbTotalAVS += i;
        nbTotalGeneral += i;
    }

    public void addNbRARFMAI(int i) {
        nbRARFMAI += i;
        nbTotal2AI += i;
        nbTotal2General += i;
    }

    public void addNbRARFMAVS(int i) {
        nbRARFMAVS += i;
        nbTotal2AVS += i;
        nbTotal2General += i;
    }

    public void addNbRAROAAI(int i) {
        nbRAROAI += i;
        nbTotalAI += i;
        nbTotalGeneral += i;
    }

    public void addNbRAROAVS(int i) {
        nbRAROAVS += i;
        nbTotalAVS += i;
        nbTotalGeneral += i;
    }

    public void addRetenuesAPIAI(FWCurrency i) {
        retenuesAPIAI.add(i);
        retenuesTotalAI.add(i);
        retenuesTotalGeneral.add(i);
    }

    public void addRetenuesAPIAVS(FWCurrency i) {
        retenuesAPIAVS.add(i);
        retenuesTotalAVS.add(i);
        retenuesTotalGeneral.add(i);
    }

    public void addRetenuesPCAIStandard(FWCurrency i) {
        retenuesPCAIStandard.add(i);
        retenuesTotal2AI.add(i);
        retenuesTotal2General.add(i);
    }

    public void addRetenuesPCAIAllocationNodel(FWCurrency i) {
        retenuesPCAIAllocationNoel.add(i);
        retenuesTotal2AI.add(i);
        retenuesTotal2General.add(i);
    }

    public void addRetenuesPCAVSStandard(FWCurrency i) {
        retenuesPCAVSStandard.add(i);
        retenuesTotal2AVS.add(i);
        retenuesTotal2General.add(i);
    }

    public void addRetenuesPCAVSAllocationNoel(FWCurrency i) {
        retenuesPCAVSAllocationNoel.add(i);
        retenuesTotal2AVS.add(i);
        retenuesTotal2General.add(i);
    }

    public void addRetenuesREOAI(FWCurrency i) {
        retenuesREOAI.add(i);
        retenuesTotalAI.add(i);
        retenuesTotalGeneral.add(i);
    }

    public void addRetenuesREOAVS(FWCurrency i) {
        retenuesREOAVS.add(i);
        retenuesTotalAVS.add(i);
        retenuesTotalGeneral.add(i);
    }

    public void addRetenuesROAI(FWCurrency i) {
        retenuesROAI.add(i);
        retenuesTotalAI.add(i);
        retenuesTotalGeneral.add(i);
    }

    public void addRetenuesROAVS(FWCurrency i) {
        retenuesROAVS.add(i);
        retenuesTotalAVS.add(i);
        retenuesTotalGeneral.add(i);
    }

    private FWCurrency calculerRetenuePourPrestation(REPrestationsAccordeesListeRecap prAcc) throws Exception {

        RERetenuesPaiementManager manager = new RERetenuesPaiementManager();
        manager.setSession(session);
        manager.setForIdRenteAccordee(prAcc.getIdPrestationAccordee());
        manager.setSeulementEnCours(true);
        manager.find();

        FWCurrency retenueTotale = new FWCurrency("0.0");

        for (RERetenuesPaiement entity : manager.getContainerAsList()) {

            // calcul pour la "prochaine retenue"
            FWCurrency montantDejaRetenu = new FWCurrency(entity.getMontantDejaRetenu());
            FWCurrency montantRetenuMensuel = new FWCurrency(entity.getMontantRetenuMensuel());
            FWCurrency prochaineRetenue = null;

            if (IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(entity.getCsTypeRetenue())) {

                // pour l'imposition à la source, le taux peut être donné de plusieurs manières
                if (!JadeStringUtil.isDecimalEmpty(entity.getTauxImposition())) {

                    // donne par un taux fixe
                    String montantRA = entity.getRenteAccordee().getMontantPrestation();

                    montantRetenuMensuel = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                            * (new FWCurrency(entity.getTauxImposition())).floatValue());
                    montantRetenuMensuel.round(FWCurrency.ROUND_ENTIER);

                } else if (!JadeStringUtil.isDecimalEmpty(entity.getMontantRetenuMensuel())) {

                    // un montant fixe
                    montantRetenuMensuel = new FWCurrency(entity.getMontantRetenuMensuel());

                } else {

                    // donne par un canton
                    String montantRA = entity.getRenteAccordee().getMontantPrestation();

                    // recherche du taux
                    PRTauxImpositionManager tManager = new PRTauxImpositionManager();
                    tManager.setSession(session);
                    tManager.setForCsCanton(entity.getCantonImposition());
                    tManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
                    tManager.find();

                    PRTauxImposition t = (PRTauxImposition) tManager.getFirstEntity();
                    String taux = "0.0";
                    if (t != null) {
                        taux = t.getTaux();
                    }

                    montantRetenuMensuel = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                            * (new FWCurrency(taux)).floatValue());
                    montantRetenuMensuel.round(FWCurrency.ROUND_ENTIER);
                }

                prochaineRetenue = montantRetenuMensuel;
            } else if (IRERetenues.CS_TYPE_ADRESSE_PMT.equals(entity.getCsTypeRetenue())) {
                prochaineRetenue = new FWCurrency("0.00");
            } else {
                // tmp = montantTotalRetenue - montantDejaRetenu
                FWCurrency tmp = new FWCurrency(entity.getMontantTotalARetenir());
                tmp.sub(montantDejaRetenu);
                // si le montant de la retenue est plus grand que tmp
                if (montantRetenuMensuel.compareTo(tmp) > 0) {
                    prochaineRetenue = tmp;
                } else {
                    prochaineRetenue = montantRetenuMensuel;
                }
            }

            retenueTotale.add(prochaineRetenue);
        }
        return retenueTotale;
    }

    /**
     * Charge les infos et trie les resultats par genre de prestations.
     * 
     * @throws Exception
     */
    public void chargerParGenrePrestation() throws Exception {

        RERecapitulationPaiementManager mgr = new RERecapitulationPaiementManager();
        mgr.setSession(session);
        mgr.setForMoisFinRANotEmptyAndHigherOrEgal(forMois);
        mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL + ", "
                + IREPrestationAccordee.CS_ETAT_DIMINUE);

        BTransaction transaction = session.getCurrentThreadTransaction();
        BStatement statement = null;
        boolean hasOpenedTransaction = false;
        try {
            if (transaction == null) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();
                hasOpenedTransaction = true;
            }

            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (REPrestationsAccordeesListeRecap prAcc : mgr.getContainerAsList()) {

                switch (REPrestationsAccordees.getGroupeGenreRente(prAcc.getCodePrestation().toString())) {

                    case REPrestationsAccordees.GROUPE_RO_AVS:

                        // Rentes ordinaires AVS
                        addMontantROAVS(prAcc.getMontantPrestation());
                        addNbRAROAVS(1);

                        if (prAcc.isPrestationBloquee()) {
                            addRetenuesROAVS(prAcc.getMontantPrestation());
                        } else if (prAcc.isRetenues()) {
                            addRetenuesROAVS(calculerRetenuePourPrestation(prAcc));
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_REO_AVS:

                        // Rentes extraordinaires AVS
                        addMontantREOAVS(prAcc.getMontantPrestation());
                        addNbRAREOAVS(1);

                        if (prAcc.isPrestationBloquee()) {
                            addRetenuesREOAVS(prAcc.getMontantPrestation());
                        } else if (prAcc.isRetenues()) {
                            addRetenuesREOAVS(calculerRetenuePourPrestation(prAcc));
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_RO_AI:

                        // Rentes AI ordinaires
                        addMontantROAI(prAcc.getMontantPrestation());
                        addNbRAROAAI(1);

                        if (prAcc.isPrestationBloquee()) {
                            addRetenuesROAI(prAcc.getMontantPrestation());
                        } else if (prAcc.isRetenues()) {
                            addRetenuesROAI(calculerRetenuePourPrestation(prAcc));
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_REO_AI:

                        // Rentes AI extraordinaires
                        addMontantREOAI(prAcc.getMontantPrestation());
                        addNbRAREOAI(1);

                        if (prAcc.isPrestationBloquee()) {
                            addRetenuesREOAI(prAcc.getMontantPrestation());
                        } else if (prAcc.isRetenues()) {
                            addRetenuesREOAI(calculerRetenuePourPrestation(prAcc));
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_API_AVS:

                        // Rentes API - AVS
                        addMontantAPIAVS(prAcc.getMontantPrestation());
                        addNbRAAPIAVS(1);

                        if (prAcc.isPrestationBloquee()) {
                            addRetenuesAPIAVS(prAcc.getMontantPrestation());
                        } else if (prAcc.isRetenues()) {
                            addRetenuesAPIAVS(calculerRetenuePourPrestation(prAcc));
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_API_AI:

                        // Rentes extraordinaires AI
                        addMontantAPIAI(prAcc.getMontantPrestation());
                        addNbRAAPIAI(1);

                        if (prAcc.isPrestationBloquee()) {
                            addRetenuesAPIAI(prAcc.getMontantPrestation());
                        } else if (prAcc.isRetenues()) {
                            addRetenuesAPIAI(calculerRetenuePourPrestation(prAcc));
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_PC_AVS:
                        // PC - AVS

                        if (prAcc.getCodePrestation().isPCAllocationNoel()) {
                            addMontantPCAVSAllocationNoel(prAcc.getMontantPrestation());
                            addnbRAPCAVSAllocationNoel(1);

                            if (prAcc.isPrestationBloquee()) {
                                addRetenuesPCAVSAllocationNoel(prAcc.getMontantPrestation());
                            } else if (prAcc.isRetenues()) {
                                addRetenuesPCAVSAllocationNoel(calculerRetenuePourPrestation(prAcc));
                            }
                        } else {
                            addMontantPCAVSStandard(prAcc.getMontantPrestation());
                            addNbRAPCAVSStandard(1);

                            if (prAcc.isPrestationBloquee()) {
                                addRetenuesPCAVSStandard(prAcc.getMontantPrestation());
                            } else if (prAcc.isRetenues()) {
                                addRetenuesPCAVSStandard(calculerRetenuePourPrestation(prAcc));
                            }
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_PC_AI:

                        // PC - AI
                        if (prAcc.getCodePrestation().isPCAllocationNoel()) {
                            addMontantPCAIAllocationNoel(prAcc.getMontantPrestation());
                            addNbRAPCAIAllocationNoel(1);

                            if (prAcc.isPrestationBloquee()) {
                                addRetenuesPCAIAllocationNodel(prAcc.getMontantPrestation());
                            } else if (prAcc.isRetenues()) {
                                addRetenuesPCAIAllocationNodel(calculerRetenuePourPrestation(prAcc));
                            }
                        } else {
                            addMontantPCAIStandard(prAcc.getMontantPrestation());
                            addNbRAPCAIStandard(1);

                            if (prAcc.isPrestationBloquee()) {
                                addRetenuesPCAIStandard(prAcc.getMontantPrestation());
                            } else if (prAcc.isRetenues()) {
                                addRetenuesPCAIStandard(calculerRetenuePourPrestation(prAcc));
                            }
                        }
                        break;

                    case REPrestationsAccordees.GROUPE_RFM_AVS:

                        // RFM - AVS
                        addMontantRFMAVS(prAcc.getMontantPrestation());
                        addNbRARFMAVS(1);
                        break;

                    case REPrestationsAccordees.GROUPE_RFM_AI:

                        // RFM - AI
                        addMontantRFMAI(prAcc.getMontantPrestation());
                        addNbRARFMAI(1);
                        break;
                }
            }
        } finally {
            try {
                if (statement != null) {
                    statement.closeStatement();
                }
            } finally {
                if (hasOpenedTransaction) {
                    transaction.closeTransaction();
                }
            }
        }
    }

    private static FWCurrency sumMontants(FWCurrency... montants) {
        FWCurrency sum = new FWCurrency(0.0);

        for (FWCurrency item : montants) {
            sum.add(item);
        }

        return sum;
    }

    public Map<CodeRecapitulationPcRfm, String> getMapRecapPcRfm() {
        Map<CodeRecapitulationPcRfm, String> map = new HashMap<CodeRecapitulationPcRfm, String>();

        map.put(CodeRecapitulationPcRfm.MontantTotalPC_AI,
                sumMontants(getMontantPCAIStandard(), getMontantPCAIAllocationNoel()).toStringFormat());
        map.put(CodeRecapitulationPcRfm.MontantRetenuesPC_AI,
                sumMontants(getRetenuesPCAIStandard(), getRetenuesPCAIAllocationNoel()).toStringFormat());
        map.put(CodeRecapitulationPcRfm.NombrePrestationsPC_AI,
                Integer.toString(getNbRAPCAIStandard() + getNbRAPCAIAllocationNoel()));

        map.put(CodeRecapitulationPcRfm.MontantTotalPC_AVS,
                sumMontants(getMontantPCAVSStandard(), getMontantPCAVSAllocationNoel()).toStringFormat());
        map.put(CodeRecapitulationPcRfm.MontantRetenuesPC_AVS,
                sumMontants(getRetenuesPCAVSStandard(), getRetenuesPCAVSAllocationNoel()).toStringFormat());
        map.put(CodeRecapitulationPcRfm.NombrePrestationsPC_AVS,
                Integer.toString(getNbRAPCAVSStandard() + getNbRAPCAVSAllocationNoel()));

        map.put(CodeRecapitulationPcRfm.MontantTotalRFM_AI, getMontantRFMAI().toStringFormat());
        map.put(CodeRecapitulationPcRfm.MontantRetenuesRFM_AI, new FWCurrency(0).toStringFormat());
        map.put(CodeRecapitulationPcRfm.NombrePrestationsRFM_AI, Integer.toString(getNbRARFMAI()));

        map.put(CodeRecapitulationPcRfm.MontantTotalRFM_AVS, getMontantRFMAVS().toStringFormat());
        map.put(CodeRecapitulationPcRfm.MontantRetenuesRFM_AVS, new FWCurrency(0).toStringFormat());
        map.put(CodeRecapitulationPcRfm.NombrePrestationsRFM_AVS, Integer.toString(getNbRARFMAVS()));

        return map;
    }

    public FWCurrency getMontantAPIAI() {
        return montantAPIAI;
    }

    public FWCurrency getMontantAPIAVS() {
        return montantAPIAVS;
    }

    public FWCurrency getMontantPCAIStandard() {
        return montantPCAIStandard;
    }

    public FWCurrency getMontantPCAVSStandard() {
        return montantPCAVSStandard;
    }

    public FWCurrency getMontantREOAI() {
        return montantREOAI;
    }

    public FWCurrency getMontantREOAVS() {
        return montantREOAVS;
    }

    public FWCurrency getMontantPCAVSAllocationNoel() {
        return montantPCAVSAllocationNoel;
    }

    public FWCurrency getMontantPCAIAllocationNoel() {
        return montantPCAIAllocationNoel;
    }

    public FWCurrency getMontantRFMAI() {
        return montantRFMAI;
    }

    public FWCurrency getMontantRFMAVS() {
        return montantRFMAVS;
    }

    public FWCurrency getMontantROAI() {
        return montantROAI;
    }

    public FWCurrency getMontantROAVS() {
        return montantROAVS;
    }

    public FWCurrency getMontantTotal2AI() {
        return montantTotal2AI;
    }

    public FWCurrency getMontantTotal2AVS() {
        return montantTotal2AVS;
    }

    public FWCurrency getMontantTotal2General() {
        return montantTotal2General;
    }

    public FWCurrency getMontantTotalAI() {
        return montantTotalAI;
    }

    public FWCurrency getMontantTotalAVS() {
        return montantTotalAVS;
    }

    public FWCurrency getMontantTotalGeneral() {
        return montantTotalGeneral;
    }

    public int getNbRAAPIAI() {
        return nbRAAPIAI;
    }

    public int getNbRAAPIAVS() {
        return nbRAAPIAVS;
    }

    public int getNbRAPCAIStandard() {
        return nbRAPCAIStandard;
    }

    public int getNbRAPCAIAllocationNoel() {
        return nbRAPCAIAllocationNoel;
    }

    public int getNbRAPCAVSStandard() {
        return nbRAPCAVSStandard;
    }

    public int getNbRAPCAVSAllocationNoel() {
        return nbRAPCAVSAllocationNoel;
    }

    public int getNbRAREOAI() {
        return nbRAREOAI;
    }

    public int getNbRAREOAVS() {
        return nbRAREOAVS;
    }

    public int getNbRARFMAI() {
        return nbRARFMAI;
    }

    public int getNbRARFMAVS() {
        return nbRARFMAVS;
    }

    public int getNbRAROAI() {
        return nbRAROAI;
    }

    public int getNbRAROAVS() {
        return nbRAROAVS;
    }

    public int getNbTotal2AI() {
        return nbTotal2AI;
    }

    public int getNbTotal2AVS() {
        return nbTotal2AVS;
    }

    public int getNbTotal2General() {
        return nbTotal2General;
    }

    public int getNbTotalAI() {
        return nbTotalAI;
    }

    public int getNbTotalAVS() {
        return nbTotalAVS;
    }

    public int getNbTotalGeneral() {
        return nbTotalGeneral;
    }

    public FWCurrency getRetenuesAPIAI() {
        return retenuesAPIAI;
    }

    public FWCurrency getRetenuesAPIAVS() {
        return retenuesAPIAVS;
    }

    public FWCurrency getRetenuesPCAIStandard() {
        return retenuesPCAIStandard;
    }

    public FWCurrency getRetenuesPCAVSStandard() {
        return retenuesPCAVSStandard;
    }

    public FWCurrency getRetenuesREOAI() {
        return retenuesREOAI;
    }

    public FWCurrency getRetenuesREOAVS() {
        return retenuesREOAVS;
    }

    public FWCurrency getRetenuesRFMAI() {
        return new FWCurrency(0);
    }

    public FWCurrency getRetenuesRFMAVS() {
        return new FWCurrency(0);
    }

    public FWCurrency getRetenuesROAI() {
        return retenuesROAI;
    }

    public FWCurrency getRetenuesROAVS() {
        return retenuesROAVS;
    }

    public FWCurrency getRetenuesTotal2AI() {
        return retenuesTotal2AI;
    }

    public FWCurrency getRetenuesTotal2AVS() {
        return retenuesTotal2AVS;
    }

    public FWCurrency getRetenuesTotal2General() {
        return retenuesTotal2General;
    }

    public FWCurrency getRetenuesTotalAI() {
        return retenuesTotalAI;
    }

    public FWCurrency getRetenuesTotalAVS() {
        return retenuesTotalAVS;
    }

    public FWCurrency getRetenuesTotalGeneral() {
        return retenuesTotalGeneral;
    }

    public FWCurrency getRetenuesPCAIAllocationNoel() {
        return retenuesPCAIAllocationNoel;
    }

    public FWCurrency getRetenuesPCAVSAllocationNoel() {
        return retenuesPCAVSAllocationNoel;
    }

    public FWCurrency getVersementAPIAI() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesAPIAI().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantAPIAI());
        return totalFinal;
    }

    public FWCurrency getVersementAPIAVS() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesAPIAVS().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantAPIAVS());
        return totalFinal;
    }

    public FWCurrency getVersementPCAIStandard() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesPCAIStandard().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantPCAIStandard());
        return totalFinal;
    }

    public FWCurrency getVersementPCAIAllocationNoel() {
        FWCurrency output = new FWCurrency(getRetenuesPCAIAllocationNoel().toStringFormat());
        output.negate();
        output.add(getMontantPCAIAllocationNoel());
        return output;
    }

    public FWCurrency getVersementPCAVSStandard() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesPCAVSStandard().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantPCAVSStandard());
        return totalFinal;
    }

    public FWCurrency getVersementPCAVSAllocationNoel() {
        FWCurrency output = new FWCurrency(getRetenuesPCAVSAllocationNoel().toStringFormat());
        output.negate();
        output.add(getMontantPCAVSAllocationNoel());
        return output;
    }

    public FWCurrency getVersementREOAI() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesREOAI().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantREOAI());
        return totalFinal;
    }

    public FWCurrency getVersementREOAVS() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesREOAVS().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantREOAVS());
        return totalFinal;
    }

    public FWCurrency getVersementRFMAI() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesRFMAI().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantRFMAI());
        return totalFinal;
    }

    public FWCurrency getVersementRFMAVS() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesRFMAVS().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantRFMAVS());
        return totalFinal;
    }

    public FWCurrency getVersementROAI() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesROAI().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantROAI());
        return totalFinal;
    }

    public FWCurrency getVersementROAVS() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesROAVS().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantROAVS());
        return totalFinal;
    }

    public FWCurrency getVersementTotal2AI() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesTotal2AI().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantTotal2AI());
        return totalFinal;
    }

    public FWCurrency getVersementTotal2AVS() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesTotal2AVS().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantTotal2AVS());
        return totalFinal;
    }

    public FWCurrency getVersementTotal2General() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesTotal2General().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantTotal2General());
        return totalFinal;
    }

    public FWCurrency getVersementTotal3General() {
        FWCurrency t = new FWCurrency(getVersementTotalGeneral().toString());
        t.add(getVersementTotal2General().toString());
        return t;
    }

    public FWCurrency getVersementTotalAI() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesTotalAI().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantTotalAI());
        return totalFinal;
    }

    public FWCurrency getVersementTotalAVS() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesTotalAVS().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantTotalAVS());
        return totalFinal;
    }

    public FWCurrency getVersementTotalGeneral() {
        FWCurrency totalFinal = new FWCurrency(getRetenuesTotalGeneral().toStringFormat());
        totalFinal.negate();
        totalFinal.add(getMontantTotalGeneral());
        return totalFinal;
    }

    public void setMontantAPIAI(FWCurrency montantAPIAI) {
        this.montantAPIAI = montantAPIAI;
    }

    public void setMontantAPIAVS(FWCurrency montantAPIAVS) {
        this.montantAPIAVS = montantAPIAVS;
    }

    public void setMontantPCAIStandard(FWCurrency montantPCAIStandard) {
        this.montantPCAIStandard = montantPCAIStandard;
    }

    public void setMontantPCAVSStandard(FWCurrency montantPCAVSStandard) {
        this.montantPCAVSStandard = montantPCAVSStandard;
    }

    public void setMontantREOAI(FWCurrency montantREOAI) {
        this.montantREOAI = montantREOAI;
    }

    public void setMontantREOAVS(FWCurrency montantREOAVS) {
        this.montantREOAVS = montantREOAVS;
    }

    public void setMontantRFMAI(FWCurrency montantRFMAI) {
        this.montantRFMAI = montantRFMAI;
    }

    public void setMontantRFMAVS(FWCurrency montantRFMAVS) {
        this.montantRFMAVS = montantRFMAVS;
    }

    public void setMontantROAI(FWCurrency montantROAI) {
        this.montantROAI = montantROAI;
    }

    public void setMontantROAVS(FWCurrency montantROAVS) {
        this.montantROAVS = montantROAVS;
    }

    public void setMontantTotal2AI(FWCurrency montantTotal2AI) {
        this.montantTotal2AI = montantTotal2AI;
    }

    public void setMontantTotal2AVS(FWCurrency montantTotal2AVS) {
        this.montantTotal2AVS = montantTotal2AVS;
    }

    public void setMontantTotal2General(FWCurrency montantTotal2General) {
        this.montantTotal2General = montantTotal2General;
    }

    public void setMontantTotalAI(FWCurrency montantTotalAI) {
        this.montantTotalAI = montantTotalAI;
    }

    public void setMontantTotalAVS(FWCurrency montantTotalAVS) {
        this.montantTotalAVS = montantTotalAVS;
    }

    public void setMontantTotalGeneral(FWCurrency montantTotalGeneral) {
        this.montantTotalGeneral = montantTotalGeneral;
    }

    public void setNbRAAPIAI(int nbRAAPIAI) {
        this.nbRAAPIAI = nbRAAPIAI;
    }

    public void setNbRAAPIAVS(int nbRAAPIAVS) {
        this.nbRAAPIAVS = nbRAAPIAVS;
    }

    public void setNbRAPCAIStandard(int nbRAPCAIStandard) {
        this.nbRAPCAIStandard = nbRAPCAIStandard;
    }

    public void setNbRAPCAIAllocationNoel(int nbRAPCAIAllocationNoel) {
        this.nbRAPCAIAllocationNoel = nbRAPCAIAllocationNoel;
    }

    public void setNbRAPCAVSStandard(int nbRAPCAVSStandard) {
        this.nbRAPCAVSStandard = nbRAPCAVSStandard;
    }

    public void setNbRAPCAVSAllocationNoel(int nbRAPCAVSAllocationNoel) {
        this.nbRAPCAVSAllocationNoel = nbRAPCAVSAllocationNoel;
    }

    public void setNbRAREOAI(int nbRAREOAI) {
        this.nbRAREOAI = nbRAREOAI;
    }

    public void setNbRAREOAVS(int nbRAREOAVS) {
        this.nbRAREOAVS = nbRAREOAVS;
    }

    public void setNbRARFMAI(int nbRARFMAI) {
        this.nbRARFMAI = nbRARFMAI;
    }

    public void setNbRARFMAVS(int nbRARFMAVS) {
        this.nbRARFMAVS = nbRARFMAVS;
    }

    public void setNbRAROAI(int nbRAROAI) {
        this.nbRAROAI = nbRAROAI;
    }

    public void setNbRAROAVS(int nbRAROAVS) {
        this.nbRAROAVS = nbRAROAVS;
    }

    public void setNbTotal2AI(int nbTotal2AI) {
        this.nbTotal2AI = nbTotal2AI;
    }

    public void setNbTotal2AVS(int nbTotal2AVS) {
        this.nbTotal2AVS = nbTotal2AVS;
    }

    public void setNbTotal2General(int nbTotal2General) {
        this.nbTotal2General = nbTotal2General;
    }

    public void setNbTotalAI(int nbTotalAI) {
        this.nbTotalAI = nbTotalAI;
    }

    public void setNbTotalAVS(int nbTotalAVS) {
        this.nbTotalAVS = nbTotalAVS;
    }

    public void setNbTotalGeneral(int nbTotalGeneral) {
        this.nbTotalGeneral = nbTotalGeneral;
    }

    public void setRetenuesTotal2AI(FWCurrency retenuesTotal2AI) {
        this.retenuesTotal2AI = retenuesTotal2AI;
    }

    public void setRetenuesTotal2AVS(FWCurrency retenuesTotal2AVS) {
        this.retenuesTotal2AVS = retenuesTotal2AVS;
    }

    public void setRetenuesTotal2General(FWCurrency retenuesTotal2General) {
        this.retenuesTotal2General = retenuesTotal2General;
    }
}