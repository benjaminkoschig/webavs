package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.perseus.utils.PFUserHelper;
import java.util.Date;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.lot.SimplePrestation;
import ch.globaz.perseus.business.models.lot.SimplePrestationSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class ListeVerificationRentePont extends PerseusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeVerificationRentePont.xml";
    private PerseusContainer container;
    private String mois = null;
    private String outPutName = "liste_verificationsRentePont";

    public ListeVerificationRentePont() {
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
        return ListeVerificationRentePont.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        List<RentePont> listEnCours = PerseusServiceLocator.getPmtMensuelRentePontService().listRentePontEnCours(mois,
                false);
        List<RentePont> listMoisDernier = PerseusServiceLocator.getPmtMensuelRentePontService().listRentePontEnCours(
                JadeDateUtil.addMonths("01." + mois, -1).substring(3), false);

        container.put("date", JACalendar.todayJJsMMsAAAA());
        container.put("moisCourant", mois);

        container.put("nbrMoisDernier", Integer.toString(listMoisDernier.size()));
        container.put("nbrVersement", Integer.toString(listEnCours.size()));

        Float montantMoisDernier = new Float(0);
        for (RentePont rp : listMoisDernier) {
            montantMoisDernier += Float.parseFloat(rp.getSimpleRentePont().getMontant());
        }
        container.put("montantMoisDernier", new FWCurrency(montantMoisDernier.toString()).toStringFormat());

        Float montantVersement = new Float(0);
        for (RentePont rp : listEnCours) {
            montantVersement += Float.parseFloat(rp.getSimpleRentePont().getMontant());
        }
        container.put("montantVersement", new FWCurrency(montantVersement.toString()).toStringFormat());

        // Calcul du nombre de décisions prises
        compterLot(CSTypeLot.LOT_DECISION_RP, "nbrDecisions", "montantDecisions");

        // Calcul du nombre de factures versées
        compterLot(CSTypeLot.LOT_FACTURES_RP, "nbrFactures", "montantFactures");

        Float totalMensuel = new Float(0);
        for (RentePont rp : listEnCours) {
            String requerant = rp.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel();
            requerant += " / "
                    + rp.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1();
            requerant += " " + rp.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2();
            container.put("requerant", requerant);
            // Recherche de l'adresse de paiement
            AdresseTiersDetail adr = PFUserHelper.getAdressePaiementAssure(rp.getSimpleRentePont()
                    .getIdTiersAdressePaiement(), rp.getSimpleRentePont().getIdDomaineApplicatifAdressePaiement(),
                    JadeDateUtil.getGlobazFormattedDate(new Date()));

            container.put("adressePaiement", fromatBanque(adr));
            container.put("montantMensuel", rp.getSimpleRentePont().getMontant());
            totalMensuel += Float.parseFloat(rp.getSimpleRentePont().getMontant());
        }

        container.put("totalMensuel", new FWCurrency(totalMensuel.toString()).toStringFormat());

        return container;
    }
}
