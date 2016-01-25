package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCPMutationPassage;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.mutation.RecapInfoDomaine;
import ch.globaz.pegasus.business.models.mutation.RecapListMutation;
import ch.globaz.pegasus.business.models.mutation.RecapMutation;
import ch.globaz.pegasus.business.models.recap.MutationType;
import ch.globaz.pegasus.business.models.recap.Recap;
import ch.globaz.pegasus.business.models.recap.RecapCategorie;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;

public class ListeMutationPca extends PegasusAbstractExcelServiceImpl {

    private String dateMonth = null;
    private String modelName = null;
    private String outPutName = "liste_Mutation_Montant_PCA";

    private void addLine(PegasusContainer container, String type, RecapMutation mutation, RecapCategorie recapCategorie) {

        String nss = type + "_NSS";
        String nom = type + "_NOM";
        String prenom = type + "_PRENOM";
        String aug = type + "_AUGMENTATION";
        String dim = type + "_DIMINUTION";

        String avsToAI = "";
        String infoIcone = "";

        if (EPCPMutationPassage.AVS_AI.equals(mutation.getPassage())) {
            avsToAI = "AVS-AI";
        } else if (EPCPMutationPassage.AI_AVS.equals(mutation.getPassage())) {
            avsToAI = "AI-AVS";
        } else {
            avsToAI = "";
        }
        infoIcone = avsToAI;

        if (MutationType.FUTURE.equals(mutation.getTypeDeMutation())) {
            infoIcone = ((!recapCategorie.isFutur()) ? "F" : "") + " " + avsToAI;
        }
        if (MutationType.SUPRESSION.equals(mutation.getTypeDeMutation())) {
            infoIcone = "S " + avsToAI;
        }
        if (mutation.isNewDroit()) {
            infoIcone = "N " + infoIcone;
        }

        container.put(nss, mutation.getNss());
        container.put(nom, mutation.getNom());
        container.put(prenom, mutation.getPrenom());

        String montantJourAppont = "";

        if (mutation.getAugmentation() != null) {
            container.put(aug, new FWCurrency(mutation.getAugmentation().toString()).toStringFormat());
            if ((mutation.getJoursAppoint() != null) && (mutation.getJoursAppoint().signum() != 0)) {
                montantJourAppont = new FWCurrency(mutation.getJoursAppoint().toString()).toStringFormat();
            }
        } else {
            container.put(aug, "");
        }
        container.put(type + "_JOURSAPPOINT", montantJourAppont);

        if (mutation.getDimminution() != null) {
            container.put(dim, new FWCurrency(mutation.getDimminution().toString()).toStringFormat());
        } else {
            container.put(dim, "");
        }

        if (mutation.getRetro() != null) {
            container.put(type + "_RETRO", new FWCurrency(mutation.getRetro().toString()).toStringFormat());
        } else {
            container.put(type + "_RETRO", "");
        }
        container.put(type + "_AI_TO_AVS", infoIcone);
        if (mutation.getAllocationNoel().compareTo(new BigDecimal(0)) > 0) {
            container.put(type + "_ALLOCATION_NOEL",
                    new FWCurrency(mutation.getAllocationNoel().toString()).toStringFormat());
        } else {
            container.put(type + "_ALLOCATION_NOEL", "");
        }
        container.put(type + "_PERIODE", mutation.getPeriode());
    }

    private void addListTocontainer(RecapListMutation recapListMutation, PegasusContainer container, String type,
            RecapCategorie recapCategorie) {

        Comparator<RecapMutation> comparator = new Comparator<RecapMutation>() {
            @Override
            public int compare(RecapMutation o1, RecapMutation o2) {
                int cmpNom = o1.getNom().compareTo(o2.getNom());
                return cmpNom != 0 ? cmpNom : o1.getPrenom().compareTo(o2.getPrenom());
            }
        };

        Collections.sort(recapListMutation.getList(), comparator);

        for (RecapMutation mutation : recapListMutation.getList()) {
            addLine(container, type, mutation, recapCategorie);
        }

        container.put(type + "_TOTAL_RETRO",
                new FWCurrency(recapListMutation.getTotalRetro().toString()).toStringFormat());
        container.put(type + "_TOTAL_AUGMENTATION",
                new FWCurrency(recapListMutation.getTotalAugmentation().toString()).toStringFormat());
        container.put(type + "_TOTAL_DIMINUTION",
                new FWCurrency(recapListMutation.getTotalDiminution().toString()).toStringFormat());
        container.put(type + "_TOTAL_ALLOCATION_NOEL", new FWCurrency(recapListMutation.getTotalAllocationNoel()
                .toString()).toStringFormat());
        container.put(type + "_TOTAL_JOURSAPPOINT",
                new FWCurrency(recapListMutation.getTotalJoursAppoint().toString()).toStringFormat());

    }

    public ExcelmlWorkbook createDoc(String dateMonth) throws DocException {
        if (dateMonth == null) {
            throw new DocException("Unable to execute createDoc, the dateMonth is null!");
        }
        this.dateMonth = dateMonth;
        return this.createDoc();
    }

    public String createDocAndSave(String idLot) throws Exception {
        resolveModelNameAndSetIt();
        ExcelmlWorkbook wk = this.createDoc(idLot);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    private void fillInfoRecap(PegasusContainer container, Entry<RecapDomainePca, RecapInfoDomaine> entry) {
        String code = entry.getKey().getCode();
        RecapInfoDomaine info = entry.getValue();
        container.put(code + "_courant_nbDossier", String.valueOf(info.getNbDossierNouveau()));

        container.put(code + "_courant_montantEnCours",
                new FWCurrency(info.getPresationNouveau().doubleValue()).toStringFormat());

        container.put(code + "_courant_versement",
                new FWCurrency(info.getVersementNouveau().doubleValue()).toStringFormat());

        container.put(code + "_precedent_nbDossier", String.valueOf(info.getNbDossierAncien()));

        container.put(code + "_precedent_montantEnCours",
                new FWCurrency(info.getPresationAncien().doubleValue()).toStringFormat());

        container.put(code + "_precedent_versement",
                new FWCurrency(info.getVersementAncien().doubleValue()).toStringFormat());

    }

    public void fillRecapMutation(PegasusContainer container, Entry<RecapDomainePca, RecapInfoDomaine> entry) {
        String type = entry.getKey().getCode();
        RecapInfoDomaine info = entry.getValue();

        container.put(type + "_RECAP_ADAPTATION", new FWCurrency(info.getAdaptation().doubleValue()).toStringFormat());

        container.put(type + "_RECAP_MUTATION_AUGMENTATION",
                new FWCurrency(info.getAugmentation().doubleValue()).toStringFormat());
        container.put(type + "_RECAP_MUTATION_DIMINUTION",
                new FWCurrency(info.getDiminution().doubleValue()).toStringFormat());
        container.put(type + "_RECAP_SOUS_TOTAL_MOTANT",
                (new FWCurrency(info.getSousTotalDimAug().doubleValue()).toStringFormat()));

        container.put(type + "_RECAP_MUTATION_AUGMENTATION_FUTUR", new FWCurrency(info.getAugmentationFuture()
                .doubleValue()).toStringFormat());

        container.put(type + "_RECAP_MUTATION_TOTAL",
                new FWCurrency(info.getTotalMutation().doubleValue()).toStringFormat());
        container.put(type + "_RECAP_TEST_DIFFERENCE",
                new FWCurrency(info.getDifference().doubleValue()).toStringFormat());

        container.put(type + "_RECAP_TOTAL_RETRO", new FWCurrency(info.getRetro().toString()).toStringFormat());

        container.put(type + "_RECAP_TOTAL_PAIEMENT",
                new FWCurrency(info.getTotalPaiement().toString()).toStringFormat());

        container.put(type + "_RECAP_TOTAL_JOURS_APPOINTS",
                new FWCurrency(info.getTotalJoursAppoint().toString()).toStringFormat());

        container.put(type + "_RECAP_TOTAL_ALLOCATIONS_NOEL",
                new FWCurrency(info.getTotalAllocationNoel().toString()).toStringFormat());
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        PegasusContainer container = new PegasusContainer();

        recapPaiement(container);

        // container.put("labelAssuree", JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelAssuree"));

        container.put("DATE_MOIS_COURANT", dateMonth);
        container.put("DATE_MOIS_PASSE",
                JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + dateMonth, -1)));
        container
                .put("DATE_PAIEMENT", JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01." + dateMonth, +1)));

        container.put("dateMois", dateMonth);
        container.put("typePC", JACalendar.todayJJsMMsAAAA());
        container.put("moisConsernee", dateMonth);
        container.put("toDay", JACalendar.todayJJsMMsAAAA());

        container.put("noCaisse", CommonProperties.KEY_NO_CAISSE_FORMATE.getValue());

        return container;
    }

    public void recapPaiement(PegasusContainer container) throws MutationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PrestationCommonException {

        Recap recap = PegasusServiceLocator.getRecapService().createRecap(dateMonth);

        for (Entry<RecapDomainePca, RecapInfoDomaine> entry : recap.getInfosDomaine().entrySet()) {
            fillInfoRecap(container, entry);
            fillRecapMutation(container, entry);
        }
        boolean passTest = true;

        if (recap.getRecapPaimentCourant() != null) {
            for (Entry<RecapDomainePca, BigDecimal> e : recap.getRecapPaimentCourant().getDiffPcaReprac().entrySet()) {
                RecapDomainePca key = e.getKey();
                if (e.getValue().signum() != 0) {
                    if (passTest) {
                        container.put(
                                "ERROR_MONTANT_PCA_REPRAC",
                                BSessionUtil.getSessionFromThreadContext().getLabel(
                                        "XLS_LISTE_MUTATION_D_MONTANT_PCA_REPRAC_NOT_EQUAL"));
                    }
                    passTest = false;

                    FWCurrency montantPca = new FWCurrency(recap.getRecapPaimentCourant().getPca().get(key)
                            .getMontantPresation().toString());
                    container.put("CONTROL_MONTANT_PCA_" + key.getCode(), montantPca.toStringFormat());

                    FWCurrency montantReprac = new FWCurrency(recap.getRecapPaimentCourant().getReprac().get(key)
                            .getMontantPresation().toString());
                    container.put("CONTROL_MONTANT_REPRAC_" + key.getCode(), montantReprac.toStringFormat());

                    container.put("reprac" + key.getCode(), "reprac" + key.getCode());
                    container.put("pca" + key.getCode(), "pca" + key.getCode());
                } else {
                    container.put("CONTROL_MONTANT_PCA_" + key.getCode(), "");
                    container.put("CONTROL_MONTANT_REPRAC_" + key.getCode(), "");
                    container.put("reprac" + key.getCode(), "");
                    container.put("pca" + key.getCode(), "");
                }
            }
        } else {
            container.put("CONTROL_MONTANT_PCA_AI", "");
            container.put("CONTROL_MONTANT_PCA_AVS", "");
            container.put("CONTROL_MONTANT_REPRAC_AI", "");
            container.put("CONTROL_MONTANT_REPRAC_AVS", "");
            container.put("repracAI", "");
            container.put("repracAVS", "");
            container.put("pcaAI", "");
            container.put("pcaAVS", "");
        }

        if (passTest) {
            container.put("ERROR_MONTANT_PCA_REPRAC", "");
        }
        container.put("precedent_total", new FWCurrency(recap.getTotalMois().toString()).toStringFormat());
        container.put("courant_total", new FWCurrency(recap.getTotalPaiement().toString()).toStringFormat());
        container.put("mutation_total", new FWCurrency(recap.getTotalMutation().toString()).toStringFormat());

        for (Entry<RecapCategorie, Map<RecapDomainePca, RecapListMutation>> cat : recap.getRecapMutation().entrySet()) {
            for (Entry<RecapDomainePca, RecapListMutation> domaine : cat.getValue().entrySet()) {
                addListTocontainer(domaine.getValue(), container, domaine.getKey().getCode()
                        + cat.getKey().getCodeForListe(), cat.getKey());
            }
        }

    }

    private void resolveModelNameAndSetIt() throws DocException {
        try {
            if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()) {
                modelName = "listeMutationPca_CCVD.xml";
            } else {
                modelName = "listeMutationPca.xml";
            }
        } catch (PropertiesException e) {
            throw new DocException("Error properties", e);
        }
    }
}
