package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class ListeOrdresVersement extends PerseusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeOrdresVersement.xml";
    private String idLot = null;
    private String outPutName = "liste_Ordres_Versement";

    public ExcelmlWorkbook createDoc(String idLot) throws DocException {
        if (idLot == null) {
            throw new DocException("Unable to execute createDoc, the idLot is null!");
        }
        this.idLot = idLot;
        return this.createDoc();
    }

    public String createDocAndSave(String idLot) throws Exception {
        ExcelmlWorkbook wk = this.createDoc(idLot);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xml";
        return save(wk, nomDoc);
    }

    private String formatDescPersonne(TiersSimpleModel tiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(tiers.getId());
        if (personne.isNew()) {
            return tiers.getDesignation1() + " " + tiers.getDesignation2();
        } else {
            return PRNSSUtil.formatDetailRequerantDetail(
                    personne.getPersonneEtendue().getNumAvsActuel(),
                    personne.getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2(),
                    personne.getPersonne().getDateNaissance(),
                    BSessionUtil.getSessionFromThreadContext().getCodeLibelle(personne.getPersonne().getSexe()),
                    BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                            BSessionUtil.getSessionFromThreadContext().getSystemCode("CIPAYORI",
                                    personne.getTiers().getIdPays())));
        }
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
        }
        return format;
    }

    private String fromatTier(AdresseTiersDetail adresse) {
        String format = "";
        if ((adresse != null) && (adresse.getFields() != null)) {
            format = adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2) + "\n"
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO) + "\n"
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE);
        }
        return format;
    }

    @Override
    public String getModelName() {
        return ListeOrdresVersement.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        CommonExcelmlContainer container = new CommonExcelmlContainer();
        OrdreVersementSearchModel ordreVersementSearch = new OrdreVersementSearchModel();
        ordreVersementSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        ordreVersementSearch.setForIdLot(idLot);

        Lot lot = PerseusServiceLocator.getLotService().read(idLot);

        BigDecimal total = new BigDecimal(0);

        try {
            ordreVersementSearch = PerseusServiceLocator.getOrdreVersementService().search(ordreVersementSearch);
        } catch (LotException e) {
            throw new DocException("Unable to search the ordreVersement", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DocException("Service not available", e);
        } catch (JadePersistenceException e) {
            throw new DocException("Persistence exception", e);
        }

        container.put("descriptionLot", lot.getSimpleLot().getDescription());
        container.put("toDay", JACalendar.todayJJsMMsAAAA());

        container.put("labelAssuree", JadeThread.getMessage("perseus.excel.listeOrdresVersment.labelAssuree"));
        container.put("labeleAdressePaiement",
                JadeThread.getMessage("perseus.excel.listeOrdresVersment.labelAdressePaiement"));
        container.put("labelMontant", JadeThread.getMessage("perseus.excel.listeOrdresVersment.labelMontant"));

        for (int i = 0; i < ordreVersementSearch.getSize(); i++) {
            OrdreVersement entity = (OrdreVersement) ordreVersementSearch.getSearchResults()[i];
            AdresseTiersDetail detailAdressePaiementTiers = null;
            if (!JadeStringUtil.isEmpty(entity.getTiers().getId())) {
                detailAdressePaiementTiers = PFUserHelper.getAdressePaiementAssure(entity.getSimpleOrdreVersement()
                        .getIdTiersAdressePaiement(), entity.getSimpleOrdreVersement().getIdDomaineApplication(),
                        JadeStringUtil.isEmpty(lot.getSimpleLot().getDateEnvoi()) ? JACalendar.todayJJsMMsAAAA() : lot
                                .getSimpleLot().getDateEnvoi());
            }
            if (CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem().equals(
                    entity.getSimpleOrdreVersement().getCsTypeVersement())) {
                if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
                    Decision decision = PerseusServiceLocator.getDecisionService().read(
                            entity.getSimplePrestation().getIdDecisionPcf());
                    if (null != decision) {
                        container.put("descriptionAssure", formatDescPersonne(decision.getDemande().getDossier()
                                .getDemandePrestation().getPersonneEtendue().getTiers()));
                    } else {
                        container.put("descriptionAssure", "Impôt a la source");

                    }
                } else if (CSTypeLot.LOT_DECISION_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
                    RentePont rp = PerseusServiceLocator.getRentePontService().read(
                            entity.getSimplePrestation().getIdDecisionPcf());

                    if (null != rp) {
                        container.put("descriptionAssure", formatDescPersonne(rp.getDossier().getDemandePrestation()
                                .getPersonneEtendue().getTiers()));
                    } else {
                        container.put("descriptionAssure", "Impôt a la source");

                    }
                } else {
                    container.put("descriptionAssure", "Impôt a la source");
                }
                container.put("adressePaiment", "Impôt a la source");
                container.put("adresseTiers", "Impôt a la source");

            } else {
                container.put("descriptionAssure", formatDescPersonne(entity.getTiers()));
                container.put("adressePaiment", fromatBanque(detailAdressePaiementTiers));
                container.put("adresseTiers", fromatTier(detailAdressePaiementTiers));
            }

            container.put("montant", entity.getSimpleOrdreVersement().getMontantVersement());
            BigDecimal montantVersement = new BigDecimal(entity.getSimpleOrdreVersement().getMontantVersement());
            total = total.add(montantVersement);

        }
        container.put("total", new FWCurrency(total.toString()).toStringFormat());
        return container;
    }
}
