package globaz.phenix.itext.taxation.definitive;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitive;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitiveManager;
import globaz.pyxis.db.tiers.TITiers;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Pourcentage;

class LoadTaxationsDefinitives {

    public List<TaxationDefinitiveForList> load(BSession session, ListTaxationsDefinitivesCriteria criteria) {

        List<TaxationDefinitiveForList> listOutput = new ArrayList<TaxationDefinitiveForList>();
        CPTaxationDefinitiveManager manager = new CPTaxationDefinitiveManager();
        manager.setSession(session);
        manager.setForNoPassage(criteria.getNoPassage());
        manager.setForEtatPrestation(CPTaxationDefinitiveManager.CS_ETAT_PRESTATION_DEFINITIF);
        manager.setForTypeDecisionIn(CPTaxationDefinitiveManager.CS_DECISION_DEFINITIVE + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_REMISE + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_RECTIFICATIVE + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_REDUCTION);

        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e1) {
            throw new RuntimeException(
                    "Impossible de charger les taxations definities avec ces critéres : " + criteria, e1);
        }
        List<CPTaxationDefinitive> list = manager.toList();
        for (CPTaxationDefinitive elem : list) {

            String designation = " ";
            try {
                AFAffiliationManager affMgr = new AFAffiliationManager();
                affMgr.setForAffilieNumero(elem.getNoAffilie());
                affMgr.setOrder(AFAffiliation.FIELDNAME_AFF_DDEBUT + " DESC");
                affMgr.setSession(session);
                affMgr.setForTypesAffPersonelles();
                affMgr.find();

                if (!affMgr.isEmpty()) {
                    AFAffiliation affiliation = (AFAffiliation) affMgr.getFirstEntity();

                    TITiers tiers = new TITiers();
                    tiers.setIdTiers(affiliation.getIdTiers());
                    tiers.setSession(session);
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
            taxationDefinitiveForList.setEcart(computEcart(elem));
            // taxationDefinitiveForList.setAnneeTaxation(anneeTaxation);
            listOutput.add(taxationDefinitiveForList);
        }
        return listOutput;
    }

    private Pourcentage computEcart(CPTaxationDefinitive elem) {
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

        return new Pourcentage(ecart.doubleValue());
    }

}
