package globaz.phenix.itext.taxation.definitive;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitive;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitiveManager;
import globaz.pyxis.db.tiers.TITiers;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Pourcentage;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;

public class CPListeTaxationDefinitiveXlsPdf extends BProcess {
    public final static String NUM_REF_INFOROM_LISTE_TAXA_DEF = "0157CFA";

    private final SimpleOutputListBuilderJade builder = SimpleOutputListBuilderJade.newInstance();
    private String noPassage = "";

    public String getNoPassage() {
        return noPassage;
    }

    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // this.getDocumentInfo().setDocumentTypeNumber(CPListeTaxationDefinitive.NUM_REF_INFOROM_LISTE_TAXA_DEF);
        // _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
        // ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        //
        List<TaxationDefinitiveForList> listOutput = new ArrayList<TaxationDefinitiveForList>();
        CPTaxationDefinitiveManager manager = new CPTaxationDefinitiveManager();
        manager.setSession(getSession());
        manager.setForNoPassage(noPassage);
        manager.setForEtatPrestation(CPTaxationDefinitiveManager.CS_ETAT_PRESTATION_DEFINITIF);
        manager.setForTypeDecisionIn(CPTaxationDefinitiveManager.CS_DECISION_DEFINITIVE + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_REMISE + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_RECTIFICATIVE + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_REDUCTION);

        manager.find(BManager.SIZE_NOLIMIT);
        List<CPTaxationDefinitive> list = manager.toList();
        for (CPTaxationDefinitive elem : list) {

            BigDecimal rd = null;
            BigDecimal ri = null;
            BigDecimal ecart = null;
            try {
                rd = new BigDecimal(elem.getRevenuDeterminant());
                ri = new BigDecimal(elem.getRevenuIndependant());

                ecart = new BigDecimal(ri.toString());
                ecart = ecart.multiply(new BigDecimal(100));
                ecart = ecart.divide(rd, BigDecimal.ROUND_HALF_EVEN);

                ecart = ecart.subtract(new BigDecimal(100));
            } catch (Exception e) {
                ecart = new BigDecimal(0);
            }

            String designation = " ";
            try {
                AFAffiliationManager affMgr = new AFAffiliationManager();
                affMgr.setForAffilieNumero(elem.getNoAffilie());
                affMgr.setOrder(AFAffiliation.FIELDNAME_AFF_DDEBUT + " DESC");
                affMgr.setSession(getSession());
                affMgr.setForTypesAffPersonelles();
                affMgr.find();

                if (!affMgr.isEmpty()) {
                    AFAffiliation affiliation = (AFAffiliation) affMgr.getFirstEntity();

                    TITiers tiers = new TITiers();
                    tiers.setIdTiers(affiliation.getIdTiers());
                    tiers.setSession(getSession());
                    tiers.retrieve();

                    if (!tiers.isNew()) {

                        if (!JadeStringUtil.isBlankOrZero(tiers.getDesignation1())) {
                            designation += tiers.getDesignation1() + " ";
                        }

                        if (!JadeStringUtil.isBlankOrZero(tiers.getDesignation2())) {
                            designation += tiers.getDesignation2() + " ";
                        }

                    }

                }

            } catch (Exception e) {
                designation = "";
            }

            TaxationDefinitiveForList taxationDefinitiveForList = new TaxationDefinitiveForList();
            taxationDefinitiveForList.setNss(elem.getNss());
            taxationDefinitiveForList.setNumAffillie(elem.getNoAffilie());
            taxationDefinitiveForList.setDesignation(designation);
            taxationDefinitiveForList.setDateDebut(elem.getDateDebut());
            taxationDefinitiveForList.setDateFin(elem.getDateFin());
            taxationDefinitiveForList.setRevenuDefinitif(new Montant(elem.getRevenuDeterminant()));
            taxationDefinitiveForList.setApgSurLeRevenu(new Montant(elem.getRevenuIndependant()));
            taxationDefinitiveForList.setEcart(new Pourcentage(ecart.doubleValue()));
            // taxationDefinitiveForList.setAnneeTaxation(anneeTaxation);
            listOutput.add(taxationDefinitiveForList);
        }

        builder.session(getSession());

        builder.g().addTranslater(getSession());
        builder.outputNameAndAddPath(NUM_REF_INFOROM_LISTE_TAXA_DEF + "_PRESTATIONS");
        builder.addList(listOutput).classElementList(TaxationDefinitiveForList.class);
        Details details = new Details();
        JadePublishDocumentInfo documentInfo = createDocumentInfo();

        String nomCaise = FWIImportProperties.getInstance().getProperty(documentInfo,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase());
        details.add(nomCaise, "");
        details.newLigne();
        builder.addHeaderDetails(details);
        builder.addTitle("Liste des taxations définitives APG/Maternité", Align.CENTER);

        File file = builder.asPdf().build();
        builder.close();
        documentInfo.setPublishDocument(true);
        documentInfo.setDocumentTypeNumber(NUM_REF_INFOROM_LISTE_TAXA_DEF);
        this.registerAttachedDocument(documentInfo, file.getAbsolutePath());
        return true;
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    @Override
    protected void _executeCleanUp() {
        builder.close();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
