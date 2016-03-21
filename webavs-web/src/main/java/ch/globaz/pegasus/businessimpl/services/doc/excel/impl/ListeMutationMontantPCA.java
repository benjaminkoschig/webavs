package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.prestation.application.PRAbstractApplication;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonProperties;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.vo.lot.MutationPCA;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.web.application.PCApplication;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.models.recap.RecapitulationPcRfm;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;

public class ListeMutationMontantPCA extends PegasusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeMutationMontantPCA.xml";
    private String dateMonth = null;
    private String outPutName = "liste_Mutation_Montant_PCA";

    private void addListTocontainer(List<MutationPCA> list, CommonExcelmlContainer container, String type,
            Map<String, Object> containerInfo, boolean future) {

        BigDecimal totalAugmentation = new BigDecimal(0);

        BigDecimal totalMoisCourant = new BigDecimal(0);
        BigDecimal totalDiminution = new BigDecimal(0);
        BigDecimal zero = new BigDecimal(0);
        BigDecimal retroTotal = new BigDecimal(0);

        BigDecimal augementaiton;
        BigDecimal diminution;
        BigDecimal moisCourant;
        BigDecimal moisPrecedant;

        Comparator<MutationPCA> comparator = new Comparator<MutationPCA>() {
            @Override
            public int compare(MutationPCA o1, MutationPCA o2) {
                int cmpNom = o1.getNom().compareTo(o2.getNom());
                return cmpNom != 0 ? cmpNom : o1.getPrenom().compareTo(o2.getPrenom());
            }
        };

        Collections.sort(list, comparator);

        BigDecimal calcule;

        String nss = type + "_NSS";
        String nom = type + "_NOM";
        String prenom = type + "_PRENOM";
        String aug = type + "_AUGMENTATION";
        String dim = type + "_DIMINUTION";

        String avsToAI = "";

        String infoIcone = "";

        for (MutationPCA mutation : list) {
            // totalRetro = totalRetro.add(new BigDecimal(JadeStringUtil.isEmpty(mutation.getMontantRetro()) ? "0"
            // : mutation.getMontantRetro()));

            if (!IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getTypeDecision())) {
                moisCourant = new BigDecimal(mutation.getMontantActuel());
            } else {
                moisCourant = new BigDecimal(0);
            }
            moisPrecedant = new BigDecimal(mutation.getAncienMontant());
            FWCurrency retro = new FWCurrency(mutation.getMontantRetro());

            diminution = zero;
            augementaiton = zero;

            calcule = moisCourant.subtract(moisPrecedant);
            augementaiton = calcule;

            boolean dateFinIsNull = false;
            if (JadeStringUtil.isBlankOrZero(mutation.getDateFinPcaActuel())) {
                dateFinIsNull = true;
            }

            // Si la décision est de type santdard cela signifi que l'on a du courant et du retro
            if ((IPCDecision.CS_PREP_STANDARD.equals(mutation.getCsTypePreparationDecision()) || IPCDecision.CS_PREP_COURANT
                    .equals(mutation.getCsTypePreparationDecision())) && dateFinIsNull) {

                if (mutation.isAvsToAi()) {
                    avsToAI = "<-";
                } else if (mutation.isAiToAvs()) {
                    avsToAI = "->";
                } else {
                    avsToAI = "";
                }

                infoIcone = ((mutation.isAugementationFutur() && !future) ? "F" : "") + " " + avsToAI;

                if (type.contains("AVS")) {// si on fait le passage de AI à AVS on a automatiquement une augementation
                                           // pour l'avs et une diminution pour l'ai
                    if ((future && mutation.isAugementationFutur())
                            || (!mutation.isAugementationFutur() && (mutation.isAiToAvs() || (!mutation.isAvsToAi() && !mutation
                                    .isAiToAvs())))) {
                        container.put(nss, mutation.getNss().toString());
                        container.put(nom, mutation.getNom().toString());
                        container.put(prenom, mutation.getPrenom().toString());
                        if (!mutation.isPurRetro()) {
                            container.put(aug, new FWCurrency(moisCourant.toString()).toStringFormat());
                        }

                        container.put(dim, "");
                        container.put(type + "_AI_TO_AVS", infoIcone);
                        if (retro.floatValue() > 0) {
                            container.put(type + "_RETRO", retro.toStringFormat());
                            retroTotal = retroTotal.add(retro.getBigDecimalValue());
                        } else {
                            container.put(type + "_RETRO", "");
                        }
                        container.put(type + "_periode",
                                mutation.getDateDebutPcaActuel() + " - " + mutation.getDateFinPcaActuel());

                        totalAugmentation = totalAugmentation.add(moisCourant);
                        // on n'affiche pas la ligne car elle se trouve du coté ai. On ne peut pas avoir de diminution
                        // pour la 1er version
                    }

                    // sin on a un passage a un couple séparé par la maladie il ne faut pas diminué deux foi
                    if (!mutation.isToSeparerMal()) {
                        // On ne peut pas avoir du retro avec une décision courante

                        if ((!future && mutation.isAugementationFutur())
                                || (!mutation.isAugementationFutur() && (!IPCDecision.CS_PREP_COURANT.equals(mutation
                                        .getCsTypePreparationDecision()) && (!mutation.isPurRetro()
                                        && !mutation.isAiToAvs() && (mutation.isAvsToAi() || !mutation.getNoVersion()
                                        .equals("1")))))) {
                            container.put(nss, mutation.getNss().toString());
                            container.put(nom, mutation.getNom().toString());
                            container.put(prenom, mutation.getPrenom().toString());
                            container.put(aug, "");
                            container.put(dim, new FWCurrency(moisPrecedant.toString()).toStringFormat());
                            container.put(type + "_AI_TO_AVS", infoIcone);
                            container.put(type + "_RETRO", "");
                            container.put(type + "_PERIODE", "");
                            totalDiminution = totalDiminution.add(moisPrecedant);
                        }
                    }
                } else {
                    if ((future && mutation.isAugementationFutur())
                            || (!mutation.isAugementationFutur() && (mutation.isAvsToAi() || (!mutation.isAvsToAi() && !mutation
                                    .isAiToAvs())))) {
                        container.put(nss, mutation.getNss().toString());
                        container.put(nom, mutation.getNom().toString());
                        container.put(prenom, mutation.getPrenom().toString());
                        if (!mutation.isPurRetro()) {

                            container.put(aug, new FWCurrency(moisCourant.toString()).toStringFormat());
                        }
                        container.put(dim, "");
                        container.put(type + "_AI_TO_AVS", infoIcone);

                        if (retro.floatValue() > 0) {
                            container.put(type + "_RETRO", retro.toStringFormat());
                            retroTotal = retroTotal.add(retro.getBigDecimalValue());
                        } else {
                            container.put(type + "_RETRO", "");
                        }
                        container.put(type + "_PERIODE",
                                mutation.getDateDebutPcaActuel() + " - " + mutation.getDateFinPcaActuel());

                        totalAugmentation = totalAugmentation.add(moisCourant);

                        // on n'affiche pas la ligne car elle se trouve du coté ai. On ne peut pas avoir de diminution
                        // pour la 1er version
                    }
                    // si on a un passage a un couple séparé par la maladie il ne faut pas diminué deux foi
                    if (!mutation.isToSeparerMal()) {
                        if ((!future && mutation.isAugementationFutur())
                                || (!mutation.isAugementationFutur()
                                        && !IPCDecision.CS_PREP_COURANT.equals(mutation.getCsTypePreparationDecision()) && (!mutation
                                        .isPurRetro() && !mutation.isAvsToAi() && (mutation.isAiToAvs() || !mutation
                                        .getNoVersion().equals("1"))))) {
                            container.put(nss, mutation.getNss().toString());
                            container.put(nom, mutation.getNom().toString());
                            container.put(prenom, mutation.getPrenom().toString());
                            container.put(aug, "");
                            container.put(dim, new FWCurrency(moisPrecedant.toString()).toStringFormat());
                            container.put(type + "_AI_TO_AVS", infoIcone);
                            container.put(type + "_RETRO", "");
                            container.put(type + "_PERIODE", "");
                            totalDiminution = totalDiminution.add(moisPrecedant);

                        }
                    }
                }

            } else // Si la décision est de type courant cela signifie que l'on du que du retro
            if (!IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getTypeDecision())
                    && (IPCDecision.CS_PREP_RETRO.equals(mutation.getCsTypePreparationDecision()) || (!dateFinIsNull))) {
                container.put(nss, mutation.getNss().toString());
                container.put(nom, mutation.getNom().toString());
                container.put(prenom, mutation.getPrenom().toString());
                container.put(aug, "");
                container.put(dim, "");
                container.put(type + "_AI_TO_AVS", "");
                container.put(type + "_RETRO", retro.toStringFormat());
                container.put(type + "_PERIODE",
                        mutation.getDateDebutPcaActuel() + " - " + mutation.getDateFinPcaActuel());
                retroTotal = retroTotal.add(retro.getBigDecimalValue());

            } else if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getTypeDecision())) {
                container.put(nss, mutation.getNss().toString());
                container.put(nom, mutation.getNom().toString());
                container.put(prenom, mutation.getPrenom().toString());
                container.put(aug, "");
                container.put(dim, new FWCurrency(moisPrecedant.toString()).toStringFormat());
                container.put(type + "_AI_TO_AVS", "S " + ((mutation.isAugementationFutur() && !future) ? "F" : ""));
                container.put(type + "_RETRO", "");
                container.put(type + "_PERIODE",
                        mutation.getDateDebutPcaActuel() + " - " + mutation.getDateFinPcaActuel());
                totalDiminution = totalDiminution.add(moisPrecedant);
            }
        }

        containerInfo.put(type + "_TOTAL_AUGMENTATION", totalAugmentation);
        containerInfo.put(type + "_TOTAL_DIMINUTION", totalDiminution);
        containerInfo.put(type + "_TOTAL_RETRO", retroTotal);
        containerInfo.put(type + "_TOTAL_RETRO_COURANT", retroTotal);

        container.put(type + "_TOTAL_RETRO", new FWCurrency(retroTotal.toString()).toStringFormat());
        container.put(type + "_TOTAL_AUGMENTATION", new FWCurrency(totalAugmentation.toString()).toStringFormat());
        container.put(type + "_TOTAL_DIMINUTION", new FWCurrency(totalDiminution.toString()).toStringFormat());
    }

    public ExcelmlWorkbook createDoc(String dateMonth) throws DocException {
        if (dateMonth == null) {
            throw new DocException("Unable to execute createDoc, the dateMonth is null!");
        }
        this.dateMonth = dateMonth;
        return this.createDoc();
    }

    public String createDocAndSave(String idLot) throws Exception {
        ExcelmlWorkbook wk = this.createDoc(idLot);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    private Map<String, Object> createListesMutations(CommonExcelmlContainer container)
            throws JadePersistenceException, MutationException, JadeApplicationServiceNotAvailableException {
        // JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + this.dateMonth, -1))
        List<MutationPCA> list = PegasusImplServiceLocator.getMutationService().findMutationMontantPCA(dateMonth);

        List<MutationPCA> listAvs = new LinkedList<MutationPCA>();
        List<MutationPCA> listAI = new LinkedList<MutationPCA>();

        List<MutationPCA> listAvsFutur = new LinkedList<MutationPCA>();
        List<MutationPCA> listAIFutur = new LinkedList<MutationPCA>();

        List<MutationPCA> listAdaptationAvs = new LinkedList<MutationPCA>();
        List<MutationPCA> listAdaptationAI = new LinkedList<MutationPCA>();

        Map<String, Object> containerInfo = new HashMap<String, Object>();
        for (MutationPCA mutation : list) {
            // On ne fait pas de distinction dans le cas de supression
            // et on ne peut pas avoir de passage de l'AVS à l'AI dans le cas de supression.
            if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getTypeDecision())) {
                mutation.setTypePcActuel(mutation.getTypePcPrecedant());
            }
            if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(mutation.getTypePcActuel())) {
                if (mutation.getCsMotif().equals(IPCDroits.CS_MOTIF_DROIT_ADAPTATION)) {
                    mutation.setAugementationFutur(false); // Comme on met tout dans le meme onglet on ne fait pas de
                                                           // distinction
                    listAdaptationAI.add(mutation);

                } else {
                    if (!mutation.getTypePcActuel().equals(mutation.getTypePcPrecedant())) {
                        mutation.setAvsToAi(true);
                        // On doit aussi filtrer le cas de retro car on l'affiche déja dans l'AI
                        if (!mutation.isPurRetro()) {
                            listAvs.add(mutation);
                        }

                    } else {
                        mutation.setAvsToAi(false);
                    }
                    if (mutation.isAugementationFutur()) {
                        listAIFutur.add(mutation);
                    }
                    // On ajoute dans tout les cas car il faut afficher la diminution du future sauf si il y un
                    // transfert
                    // On filtre les première versions des pca car il ne peut pas avoir de diminution

                    if (!(mutation.isAugementationFutur() && mutation.isAvsToAi())
                            && !(mutation.isAugementationFutur() && mutation.getNoVersion().equals("1"))) {
                        listAI.add(mutation);
                    }
                }

            } else {

                if (mutation.getCsMotif().equals(IPCDroits.CS_MOTIF_DROIT_ADAPTATION)) {
                    mutation.setAugementationFutur(false); // Comme on met tout dans le meme onglet on ne fait pas de
                                                           // distinction
                    listAdaptationAvs.add(mutation);
                } else {
                    if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(mutation.getTypePcPrecedant())) {
                        mutation.setAiToAvs(true);
                        // On doit aussi filtrer le cas de retro car on l'affiche déja dans l'AVS
                        if (!mutation.isPurRetro()) {
                            listAI.add(mutation);
                        }
                    } else {
                        mutation.setAiToAvs(false);
                    }

                    if (mutation.isAugementationFutur()) {
                        listAvsFutur.add(mutation);
                    }
                    // On ajoute dans tout les cas car il faut afficher la diminution du future sauf si il y un
                    // transfert
                    // On filtre les première versions des pca car il ne peut pas avoir de diminution

                    if (!(mutation.isAugementationFutur() && mutation.isAiToAvs())
                            && !(mutation.isAugementationFutur() && mutation.getNoVersion().equals("1"))) {
                        listAvs.add(mutation);
                    }
                }
            }

        }

        addListTocontainer(listAdaptationAvs, container, "AVS_ADAPTATION", containerInfo, false);
        addListTocontainer(listAdaptationAI, container, "AI_ADAPTATION", containerInfo, false);

        addListTocontainer(listAvs, container, "AVS", containerInfo, false);
        addListTocontainer(listAI, container, "AI", containerInfo, false);

        //
        addListTocontainer(listAvsFutur, container, "AVS_FUTUR", containerInfo, true);
        addListTocontainer(listAIFutur, container, "AI_FUTUR", containerInfo, true);
        //

        List<MutationPCA> listOldFutur = PegasusImplServiceLocator.getMutationService().findOldAugmentationFutur(
                dateMonth);
        List<MutationPCA> listOldFuturAvs = new LinkedList<MutationPCA>();
        List<MutationPCA> listOldFuturAi = new LinkedList<MutationPCA>();

        for (MutationPCA mutation : listOldFutur) {
            if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(mutation.getTypePcActuel())) {
                if (!mutation.getTypePcActuel().equals(mutation.getTypePcPrecedant())) {
                    mutation.setAvsToAi(true);
                    listOldFuturAvs.add(mutation);
                }
                listOldFuturAi.add(mutation);
            } else {
                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(mutation.getTypePcPrecedant())) {
                    mutation.setAiToAvs(true);
                    listOldFuturAi.add(mutation);
                }
                listOldFuturAvs.add(mutation);
            }
        }

        addListTocontainer(listOldFuturAvs, container, "AVS_FUTUR_PASSE", containerInfo, true);
        addListTocontainer(listOldFuturAi, container, "AI_FUTUR_PASSE", containerInfo, true);

        return containerInfo;
    }

    private void fillInfoRecap(CommonExcelmlContainer container, RecapitulationPcRfm recapitulationPcRfm, String type)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, MutationException {

        double total = recapitulationPcRfm.getMontantTotalPCAVS().getBigDecimalValue()
                .add(recapitulationPcRfm.getMontantTotalPCAI().getBigDecimalValue()).doubleValue();

        container.put("AVS_" + type + "_nbDossier", String.valueOf(recapitulationPcRfm.getNbPrestationPCAVS()));
        container.put("AVS_" + type + "_montantEnCours", recapitulationPcRfm.getMontantTotalPCAVS().toStringFormat());
        container.put("AVS_" + type + "_versement", recapitulationPcRfm.getMontantTotalPCAVS().toStringFormat());
        container.put("AI_" + type + "_nbDossier", String.valueOf(recapitulationPcRfm.getNbPrestationPCAI()));
        container.put("AI_" + type + "_montantEnCours", recapitulationPcRfm.getMontantTotalPCAI().toStringFormat());
        container.put("AI_" + type + "_versement", recapitulationPcRfm.getMontantTotalPCAI().toStringFormat());
        container.put(type + "_total", new FWCurrency(String.valueOf(total)).toStringFormat());
    }

    public BigDecimal fillRecapMutation(String type, CommonExcelmlContainer container,
            Map<String, Object> containerInfo, RecapitulationPcRfm infoRecapCourant,
            RecapitulationPcRfm infoRecapPrecedent, RecapitulationPcRfm infoRecapPasseFutur) {
        BigDecimal recapMoisPrecedent = new BigDecimal(0);
        BigDecimal recapMoisCourant = new BigDecimal(0);

        if ("AVS".equals(type)) {
            recapMoisPrecedent = infoRecapPrecedent.getMontantTotalPCAVS().getBigDecimalValue();
            recapMoisCourant = infoRecapCourant.getMontantTotalPCAVS().getBigDecimalValue();
        } else {
            recapMoisPrecedent = infoRecapPrecedent.getMontantTotalPCAI().getBigDecimalValue();
            recapMoisCourant = infoRecapCourant.getMontantTotalPCAI().getBigDecimalValue();
        }

        FWCurrency totalAugmentation = new FWCurrency(containerInfo.get(type + "_TOTAL_AUGMENTATION").toString());
        FWCurrency totalDiminution = new FWCurrency(containerInfo.get(type + "_TOTAL_DIMINUTION").toString());

        // BigDecimal recapFuPaAugmentation = (BigDecimal) containerInfo.get(type + "_FUTUR_PASSE_TOTAL_AUGMENTATION");
        // BigDecimal recapFuPaDiminution = (BigDecimal) containerInfo.get(type + "_FUTUR_PASSE_TOTAL_DIMINUTION");
        BigDecimal augmentationFutur = (BigDecimal) containerInfo.get(type + "_FUTUR_TOTAL_AUGMENTATION");
        // BigDecimal totalPrecedent = recapMoisPrecedent.add(recapFuPaAugmentation).subtract(recapFuPaDiminution);

        BigDecimal adaptation = ((BigDecimal) containerInfo.get(type + "_ADAPTATION_TOTAL_AUGMENTATION"))
                .subtract((BigDecimal) containerInfo.get(type + "_ADAPTATION_TOTAL_DIMINUTION"));
        BigDecimal sousTotal = recapMoisPrecedent.add(
                totalAugmentation.getBigDecimalValue().subtract(totalDiminution.getBigDecimalValue())).add(adaptation);

        BigDecimal totalPrestaton = sousTotal.add(augmentationFutur);
        BigDecimal difference = recapMoisCourant.subtract(totalPrestaton);
        BigDecimal retro = (BigDecimal) containerInfo.get(type + "_TOTAL_RETRO");
        BigDecimal totalPaiment = recapMoisPrecedent.add(totalAugmentation.getBigDecimalValue()).add(retro);
        // container.put(type + "_RECAP_MOIS_PRECEDENT", new
        // FWCurrency(recapMoisPrecedent.toString()).toStringFormat());
        // container.put(type + "_RECAP_FUTUR_PASSE_TOTAL_AUGMENTATION",
        // new FWCurrency(recapFuPaAugmentation.toString()).toStringFormat());
        // container.put(type + "_RECAP_FUTUR_PASSE_TOTAL_DIMINUTION",
        // new FWCurrency(recapFuPaDiminution.toString()).toStringFormat());
        // container.put(type + "_TOTAL_PRECEDENT", new FWCurrency(totalPrecedent.toString()).toStringFormat());

        // container.put(type + "_mutation_courant", (new FWCurrency(totalCourant.toString())).toStringFormat());

        // BigDecimal totalCourant = totalPrecedent.add(totalAugmentation.getBigDecimalValue()).subtract(
        // totalDiminution.getBigDecimalValue());

        containerInfo.put(type + "_TOTAL_AUGMENTATION", totalAugmentation);
        containerInfo.put(type + "_TOTAL_DIMINUTION", totalDiminution);

        container.put(type + "_RECAP_ADAPTATION", new FWCurrency(adaptation.toString()).toStringFormat());

        container.put(type + "_RECAP_MUTATION_AUGMENTATION", totalAugmentation.toStringFormat());
        container.put(type + "_RECAP_MUTATION_DIMINUTION", totalDiminution.toStringFormat());
        container.put(type + "_RECAP_SOUS_TOTAL_MOTANT", (new FWCurrency(sousTotal.toString())).toStringFormat());

        container.put(type + "_RECAP_MUTATION_AUGMENTATION_FUTUR",
                new FWCurrency(augmentationFutur.toString()).toStringFormat());

        container.put(type + "_RECAP_MUTATION_TOTAL", new FWCurrency(totalPrestaton.toString()).toStringFormat());
        container.put(type + "_RECAP_TEST_DIFFERENCE", new FWCurrency(difference.toString()).toStringFormat());

        container.put(type + "_RECAP_TOTAL_RETRO", new FWCurrency(retro.toString()).toStringFormat());
        container.put(type + "_RECAP_TOTAL_PAIEMENT", new FWCurrency(totalPaiment.toString()).toStringFormat());

        return totalPrestaton;
    }

    public RecapitulationPcRfm findReacpituationPC(String date) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, MutationException {
        RecapitulationPcRfm recapitulationPcRfm = null;
        try {
            recapitulationPcRfm = PrestationCommonServiceLocator.getRecapitulationPcRfmService().findInfoRecapByDate(
                    date);

        } catch (PrestationCommonException e) {
            throw new MutationException("Unable to find the infoRecap", e);
        }

        if (recapitulationPcRfm == null) {
            throw new MutationException("No data found for the infoRecap for this date (" + date + ")");
        }
        return recapitulationPcRfm;
    }

    public RecapitulationPcRfm findRecapPcaReprac(CommonExcelmlContainer container) throws JadePersistenceException {
        // @formatter:off
		String sql = "Select * from ( "
				+ "select sum(planCalcule.CVMPCM) as SOMME,  count(planCalcule.CVMPCM) as NOMBRE, pcAccordee.CUTTYP as TYPEPC, 1 as ORDRE "
				+ "from {schema}.PCPCACC pcAccordee "
				+ "inner join {schema}.PCPLCAL planCalcule "
				+ "on pcAccordee.CUIPCA = planCalcule.CVIPCA "
				+ "where planCalcule.CVBPLR = 1 "
				+ "and planCalcule.CVLEPC <> 64061003 "// STATUS_REFUS
				+ "and (pcAccordee.CUTETA = 64029002 or pcAccordee.CUTETA = 64029003) " // etat validé et courant validé
				+ "and pcAccordee.CUDDEB <= {date} "
				+ "and (pcAccordee.CUDFIN >= {date} or (pcAccordee.CUDFIN is null or pcAccordee.CUDFIN = 0)) "
				+ "group by pcAccordee.CUTTYP "
				+ "UNION "
				+ "select sum(presation.ZTMPRE) as SOMME, count(presation.ZTMPRE) as NOMBRE, cast(presation.ZTLCPR as DECIMAL) as TYPEPC, 2 as ORDRE "
				+ "from {schema}.REPRACC presation "
				+ "where presation.ZTTGEN = 52849002 "// type PC
				// etat valide ou partiel ou (diminué et en cours)
				+ "and (presation.ZTTETA = 52820002 or presation.ZTTETA = 52820003 or (presation.ZTTETA = 52820004 and presation.ZTDFDR >= {date}))"
				+ "and presation.ZTDDDR <= {date} "
				+ "and (presation.ZTDFDR >= {date} or (presation.ZTDFDR is null or presation.ZTDFDR = 0)) "
				+ "group by presation.ZTLCPR " + ") " + "order by ordre";

		sql = sql.replace("{schema}", JadePersistenceUtil.getDbSchema());
		sql = sql.replace(
				"{date}",
				JadePersistenceUtil.parseMonthYearToSql(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
						+ dateMonth, +1))));

		ArrayList<HashMap<String, Object>> result = PersistenceUtil.executeQuery(sql, this.getClass());

		Map<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();
		for (HashMap<String, Object> value : result) {
			map.put(String.valueOf(value.get("TYPEPC")), value);
		}

		Map<String, Object> pcaAi = map.get(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
		Map<String, Object> pcaSurvivant = map.get(IPCPCAccordee.CS_TYPE_PC_SURVIVANT);
		Map<String, Object> pcaViellesse = map.get(IPCPCAccordee.CS_TYPE_PC_VIELLESSE);

		Map<String, Object> repracAi = map.get("150");
		Map<String, Object> repracSurvivant = map.get("113");
		Map<String, Object> repracViellesse = map.get("110");

		Integer montantRepracAvs = getInfo("SOMME", repracSurvivant) + getInfo("SOMME", repracViellesse);
		Integer nbDossierReprac = getInfo("NOMBRE", repracSurvivant) + getInfo("NOMBRE", repracViellesse);

		Integer montantPCAAi = getInfo("SOMME", pcaAi);
		Integer montantPCAAvs = getInfo("SOMME", pcaSurvivant) + getInfo("SOMME", pcaViellesse);
		// On ne peut pas avoir le même nombre de dossier que dans la reprac, du au couple avec 2 rentes

		RecapitulationPcRfm recap = new RecapitulationPcRfm();

		recap.setMontantTotalPCAI(new FWCurrency(getInfo("SOMME", repracAi)));
		recap.setNbPrestationPCAI(getInfo("NOMBRE", repracAi));

		recap.setMontantTotalPCAVS(new FWCurrency(montantRepracAvs));
		recap.setNbPrestationPCAVS(nbDossierReprac);

		// on test si on n'a le même montant dans les pca et les reprac
		if ((montantPCAAvs.floatValue() != recap.getMontantTotalPCAVS().floatValue())
				|| (montantPCAAi.floatValue() != recap.getMontantTotalPCAI().floatValue())) {
			container.put(
					"ERROR_MONTANT_PCA_REPRAC",
					BSessionUtil.getSessionFromThreadContext().getLabel(
							"XLS_LISTE_MUTATION_D_MONTANT_PCA_REPRAC_NOT_EQUAL"));
			container.put("CONTROL_MONTANT_PCA_AI", new FWCurrency(montantPCAAi).toStringFormat());
			container.put("CONTROL_MONTANT_PCA_AVS", new FWCurrency(montantPCAAvs).toStringFormat());
			container.put("CONTROL_MONTANT_REPRAC_AI", recap.getMontantTotalPCAI().toStringFormat());
			container.put("CONTROL_MONTANT_REPRAC_AVS", recap.getMontantTotalPCAVS().toStringFormat());

			container.put("repracAi", "repracAi");
			container.put("repracAvs", "repracAvs");
			container.put("pcaAI", "pcaAI");
			container.put("pcaAvs", "pcaAvs");
		} else {
			container.put("ERROR_MONTANT_PCA_REPRAC", "");
			container.put("CONTROL_MONTANT_PCA_AI", "");
			container.put("CONTROL_MONTANT_PCA_AVS", "");
			container.put("CONTROL_MONTANT_REPRAC_AI", "");
			container.put("CONTROL_MONTANT_REPRAC_AVS", "");

			container.put("repracAi", "");
			container.put("repracAvs", "");
			container.put("pcaAI", "");
			container.put("pcaAvs", "");
		}
		return recap;
	}

	private Integer getInfo(String key, Map<String, Object> map) {
		if (map != null) {
			return (int) Double.parseDouble((String.valueOf(map.get(key))));
		}
		return 0;
	}

	@Override
	public String getModelName() {
		return ListeMutationMontantPCA.MODEL_NAME;
	}

	@Override
	public String getOutPutName() {
		return outPutName;
	}

	@Override
	public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
			JadePersistenceException, JadeApplicationException {
	    CommonExcelmlContainer container = new CommonExcelmlContainer();

		Map<String, Object> containerInfo = createListesMutations(container);
		recapPaiement(container, containerInfo);

		// container.put("labelAssuree", JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelAssuree"));

		container.put("DATE_MOIS_COURANT", dateMonth);
		container.put("DATE_MOIS_PASSE",
				JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + dateMonth, -1)));
		container.put("DATE_PAIEMENT",
				JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + dateMonth, +1)));

		container.put("dateMois", dateMonth);
		container.put("typePC", JACalendar.todayJJsMMsAAAA());
		container.put("moisConsernee", dateMonth);
		container.put("toDay", JACalendar.todayJJsMMsAAAA());

		try {
			container.put("noCaisse", PRAbstractApplication.getApplication(PCApplication.DEFAULT_APPLICATION_PEGASUS)
					.getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE));
		} catch (RemoteException e) {
			throw new MutationException("Unable to get the KEY_NO_CAISSE_FORMATE", e);
		} catch (Exception e) {
			throw new MutationException("Unable to get the KEY_NO_CAISSE_FORMATE", e);
		}

		return container;
	}

	public void recapPaiement(CommonExcelmlContainer container, Map<String, Object> containerInfo) throws MutationException,
			JadeApplicationServiceNotAvailableException, JadePersistenceException, PrestationCommonException {

		RecapitulationPcRfm infoRecapCourant = findRecapPcaReprac(container);
		fillInfoRecap(container, infoRecapCourant, "courant");

		// infoRecap precedent
		RecapitulationPcRfm infoRecapPrecedent = findReacpituationPC(dateMonth);
		fillInfoRecap(container, infoRecapPrecedent, "precedent");

		//
		RecapitulationPcRfm infoRecapPasseFutur = PrestationCommonServiceLocator.getRecapitulationPcRfmService()
				.findInfoRecapByDate(
						JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + dateMonth, -1)));

		BigDecimal avsTotal = fillRecapMutation("AVS", container, containerInfo, infoRecapCourant,
				infoRecapPrecedent, infoRecapPasseFutur);
		BigDecimal aiTotal = fillRecapMutation("AI", container, containerInfo, infoRecapCourant,
				infoRecapPrecedent, infoRecapPasseFutur);

		BigDecimal total = avsTotal.add(aiTotal);

		container.put("mutation_total", new FWCurrency(total.toString()).toStringFormat());

		// BigDecimal aiTotalRetroCourant = infoRecapPrecedent.getMontantTotalPCAI().getBigDecimalValue()
		// .add((BigDecimal) containerInfo.get("AI_TOTAL_RETRO"));
		// BigDecimal aiTotalPaiement = aiTotalRetroCourant.add(
		// (BigDecimal) containerInfo.get("AI_FUTUR_TOTAL_AUGMENTATION")).subtract(
		// (BigDecimal) containerInfo.get("AI_FUTUR_TOTAL_DIMINUTION"));
		//
		// BigDecimal avsTotalRetroCourant = infoRecapPrecedent.getMontantTotalPCAVS().getBigDecimalValue()
		// .add((BigDecimal) containerInfo.get("AVS_TOTAL_RETRO"));
		// BigDecimal avsTotalPaiement = avsTotalRetroCourant.add(
		// (BigDecimal) containerInfo.get("AVS_FUTUR_TOTAL_AUGMENTATION")).subtract(
		// (BigDecimal) containerInfo.get("AVS_FUTUR_TOTAL_DIMINUTION"));
		//
		// container.put("AI_RECAP_TOTAL_RETRO",
		// new FWCurrency(containerInfo.get("AI_TOTAL_RETRO").toString()).toStringFormat());
		// container.put("AI_TOTAL_RETRO_COURANT", new FWCurrency(aiTotalPaiement.toString()).toStringFormat());
		// container.put("AI_RECAP_TOTAL_COURANT", new FWCurrency(aiTotal.toString()).toStringFormat());

		// container.put("AI_TOTAL_AUGMENTATION_FUTUR", new FWCurrency(containerInfo.get("AI_FUTUR_TOTAL_AUGMENTATION")
		// .toString()).toStringFormat());
		// container.put("AI_TOTAL_DIMINUTION_FUTUR", new FWCurrency(containerInfo.get("AI_FUTUR_TOTAL_DIMINUTION")
		// .toString()).toStringFormat());

		// container.put("AI_TOTAL_PAIEMENT", new FWCurrency(aiTotalPaiement.toString()).toStringFormat());

		// container.put("AVS_RECAP_TOTAL_RETRO",
		// new FWCurrency(containerInfo.get("AVS_TOTAL_RETRO").toString()).toStringFormat());
		// container.put("AVS_TOTAL_RETRO_COURANT", new FWCurrency(avsTotalRetroCourant.toString()).toStringFormat());
		// container.put("AVS_RECAP_TOTAL_COURANT", new FWCurrency(avsTotal.toString()).toStringFormat());
		// container.put("AVS_TOTAL_AUGMENTATION_FUTUR", new
		// FWCurrency(containerInfo.get("AVS_FUTUR_TOTAL_AUGMENTATION")
		// .toString()).toStringFormat());
		// container.put("AVS_TOTAL_DIMINUTION_FUTUR", new FWCurrency(containerInfo.get("AVS_FUTUR_TOTAL_DIMINUTION")
		// .toString()).toStringFormat());

		// container.put("AVS_TOTAL_PAIEMENT", new FWCurrency(avsTotalPaiement.toString()).toStringFormat());

		// container.put("mutation_total", new FWCurrency((courantAVS.add(courantAI)).doubleValue()).toStringFormat());
	}
}
