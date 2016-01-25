package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.perseus.utils.PFUserHelper;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.lot.SimplePrestation;
import ch.globaz.perseus.business.models.lot.SimplePrestationSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class ListeVerification extends PerseusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeVerification.xml";
    private PerseusContainer container;
    private String mois = null;
    private String outPutName = "liste_verifications";

    public ListeVerification() {
        container = new PerseusContainer();
    }

    public void compterLot(CSTypeLot typeLot, String nbr, String montant)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        LotSearchModel lotSearchModel = new LotSearchModel();
        lotSearchModel.setForTypeLot(typeLot.getCodeSystem());
        lotSearchModel.setForMoisComptabilisation(mois);
        lotSearchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
        lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
        Float montantInLot = new Float(0);
        Integer nbrInLot = 0;
        for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
            Lot lot = (Lot) model;
            SimplePrestationSearchModel psm = new SimplePrestationSearchModel();
            psm.setForIdLot(lot.getId());
            psm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            psm = PerseusImplServiceLocator.getSimplePrestationService().search(psm);
            for (JadeAbstractModel m : psm.getSearchResults()) {
                Float montantPrest = Float.parseFloat(((SimplePrestation) m).getMontantTotal());
                if (montantPrest > 0) {
                    nbrInLot++;
                    montantInLot += montantPrest;
                }
            }
        }
        container.put(nbr, nbrInLot.toString());
        container.put(montant, new FWCurrency(montantInLot.toString()).toStringFormat());
    }

    public ExcelmlWorkbook createDoc(String mois) throws DocException {
        if (mois == null) {
            throw new DocException("Unable to execute createDoc, the mois is null!");
        }
        this.mois = mois;
        return this.createDoc();
    }

    public String createDocAndSave(String mois) throws Exception {
        ExcelmlWorkbook wk = this.createDoc(mois);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xml";
        return save(wk, nomDoc);
    }

    private String fromatBanque(AdresseTiersDetail adresse) {
        String format = "";
        if ((adresse != null) && (adresse.getFields() != null)) {
            if (JadeStringUtil.isEmpty(adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP))) {
                format = adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE)
                        + new String(new char[] { '\r', '\n' })
                        + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CLEARING)
                        + new String(new char[] { '\r', '\n' })
                        + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1)
                        + new String(new char[] { '\r', '\n' })
                        /* + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D2) + " \n" */
                        + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_NPA) + " "
                        + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_LOCALITE);
            } else {
                format = adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP);
            }
        } else {
            format = "ERREUR : Pas d'adresse de paiement";
        }
        return format;
    }

    @Override
    public String getModelName() {
        return ListeVerification.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        List<PCFAccordee> listDiminuees = PerseusServiceLocator.getPmtMensuelService().listPCFAdiminuees(mois);
        List<PCFAccordee> listAugmentees = PerseusServiceLocator.getPmtMensuelService().listPCFaugmentees(mois);
        List<PCFAccordee> listEnCours = PerseusServiceLocator.getPmtMensuelService().listPCFAencours(mois);
        List<PCFAccordee> listMoisDernier = PerseusServiceLocator.getPmtMensuelService().listPCFAencours(
                JadeDateUtil.addMonths("01." + mois, -1).substring(3));

        container.put("date", JACalendar.todayJJsMMsAAAA());
        container.put("moisCourant", mois);

        container.put("nbrMoisDernier", Integer.toString(listMoisDernier.size()));
        container.put("nbrDiminuees", Integer.toString(listDiminuees.size()));
        container.put("nbrAugmentees", Integer.toString(listAugmentees.size()));
        container.put("nbrVersement", Integer.toString(listEnCours.size()));

        Float montantMoisDernier = new Float(0);
        for (PCFAccordee pcfa : listMoisDernier) {
            montantMoisDernier += Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        }
        container.put("montantMoisDernier", new FWCurrency(montantMoisDernier.toString()).toStringFormat());

        Float montantAugmentees = new Float(0);
        for (PCFAccordee pcfa : listAugmentees) {
            montantAugmentees += Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        }
        container.put("montantAugmentees", new FWCurrency(montantAugmentees.toString()).toStringFormat());

        Float montantDiminuees = new Float(0);
        for (PCFAccordee pcfa : listDiminuees) {
            montantDiminuees += Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        }
        container.put("montantDiminuees", new FWCurrency(montantDiminuees.toString()).toStringFormat());

        Float montantVersement = new Float(0);
        for (PCFAccordee pcfa : listEnCours) {
            montantVersement += Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        }
        container.put("montantVersement", new FWCurrency(montantVersement.toString()).toStringFormat());

        // Calcul du nombre de décisions prises
        compterLot(CSTypeLot.LOT_DECISION, "nbrDecisions", "montantDecisions");

        // Calcul du nombre de factures versées
        compterLot(CSTypeLot.LOT_FACTURES, "nbrFactures", "montantFactures");

        // Liste des assurés qui recoivent une prestation
        HashMap<String, Decision> listDecisions = PerseusServiceLocator.getPmtMensuelService().getDecisionForPcfa(
                listEnCours);
        Float totalMensuel = new Float(0);
        for (PCFAccordee pcfa : listEnCours) {
            Decision decision = listDecisions.get(pcfa.getDemande().getId());
            String requerant = pcfa.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                    .getPersonneEtendue().getNumAvsActuel();
            requerant += " / "
                    + pcfa.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation1();
            requerant += " "
                    + pcfa.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation2();
            container.put("requerant", requerant);
            if (decision != null) {
                // Recherche de l'adresse de paiement
                AdresseTiersDetail adr = PFUserHelper.getAdressePaiementAssure(decision.getSimpleDecision()
                        .getIdTiersAdressePaiement(), decision.getSimpleDecision()
                        .getIdDomaineApplicatifAdressePaiement(), JadeDateUtil.getGlobazFormattedDate(new Date()));

                container.put("adressePaiement", fromatBanque(adr));
            } else {
                container.put("adressePaiement", "ERREUR : Pas de décision !!!!!");
            }
            container.put("montantMensuel", pcfa.getSimplePCFAccordee().getMontant());
            totalMensuel += Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        }

        container.put("totalMensuel", new FWCurrency(totalMensuel.toString()).toStringFormat());

        return container;
    }
}
