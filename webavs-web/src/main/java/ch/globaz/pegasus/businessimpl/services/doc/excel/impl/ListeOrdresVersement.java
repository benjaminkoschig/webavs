package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.osiris.api.APIEcriture;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.OrdreversementTiers;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.Ecriture;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.OrdreVersementCompta;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PrestationOperations;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.TypeEcriture;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationData;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationHandler;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class ListeOrdresVersement extends PegasusAbstractExcelServiceImpl {

    private static final String DESCRIPTION_LOT = "descriptionLot";
    public final static String MODEL_NAME = "listeOrdresVersement.xml";
    public final static String MODEL_NAME_COM_POL = "listeOrdresVersement_CommunePolitique.xml";

    private boolean wantCommunePolitique;
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
        wantCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        ExcelmlWorkbook wk = this.createDoc(idLot);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    @Override
    public String getModelName() {
        if (wantCommunePolitique) {
            return ListeOrdresVersement.MODEL_NAME_COM_POL;
        }
        return ListeOrdresVersement.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    private String formatDescPersonne(OrdreversementTiers personne) {
        return personne.getNumAvs()
                + new String(new char[] { '\r', '\n' })
                + personne.getDesignation1()
                + " "
                + personne.getDesignation2()
                + new String(new char[] { '\r', '\n' })
                + personne.getDateNaissance()
                + " / "
                + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(personne.getSexe())
                + " / "
                + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                        BSessionUtil.getSessionFromThreadContext().getSystemCode("CIPAYORI", personne.getIdPays()));
    }

    private String fromatBanque(AdresseTiersDetail adresse) {
        String format = "";
        if ((adresse != null) && (adresse.getFields() != null)) {
            format = adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE)
                    + new String(new char[] { '\r', '\n' })
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CLEARING)
                    + new String(new char[] { '\r', '\n' })
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1)
                    + new String(new char[] { '\r', '\n' })
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_NPA) + " "
                    + adresse.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_LOCALITE);
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
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        CommonExcelmlContainer container = new CommonExcelmlContainer();
        BSession session = BSessionUtil.getSessionFromThreadContext();
        SimpleLot lot = CorvusServiceLocator.getLotService().read(idLot);

        container.put(ListeOrdresVersement.DESCRIPTION_LOT, lot.getDescription());
        container.put("toDay", JACalendar.todayJJsMMsAAAA());
        container.put("labelAssuree", JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelAssuree"));
        container.put("labelTypeOv", JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelType"));
        container.put("labelAdressePaiement",
                JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelAdressePaiement"));
        container.put("labelMontant", JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelMontant"));
        container.put("labelCommunePolitique",
                JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelCommunePolitique"));
        container.put("labelUser", JadeThread.getMessage("pegasus.excel.listeOrdresVersment.labelUser"));
        container.put("user", session.getUserName());
        try {
            BigDecimal total = new BigDecimal(0);
            BigDecimal totalDette = new BigDecimal(0);

            ComptabilisationData data = ComptabilisationHandler.generateJournal(idLot);

            Map<String, OrdreversementTiers> mapPersonneByNss = new HashMap<String, OrdreversementTiers>();

            for (Entry<String, OrdreversementTiers> entry : data.getMapIdTierDescription().entrySet()) {
                mapPersonneByNss.put(entry.getValue().getNumAvs().trim(), entry.getValue());
            }

            // D0118 remplir une liste de POJO avec le resultset
            List<POJO> list = new ArrayList<POJO>();
            Set<String> setIdTiers = new HashSet<String>();

            for (PrestationOperations operations : data.getJournalConteneur().getOperations()) {

                for (Ecriture ecriture : operations.getEcritures()) {
                    if (ecriture.getTypeEcriture().equals(TypeEcriture.DETTE)
                            && APIEcriture.CREDIT.equals(ecriture.getCodeDebitCredit())) {

                        OrdreversementTiers personne = data.getMapIdTierDescription().get(
                                ecriture.getCompteAnnexe().getIdTiers());
                        // La dette est probablement lié à un enfant on n'a pas
                        if (personne == null) {
                            personne = mapPersonneByNss.get(operations.getNumAvsRequerant().trim());
                        }
                        fillPojoListEcriture(list, setIdTiers, ecriture, personne);

                        totalDette = totalDette.add(ecriture.getMontant());
                        total = total.add(ecriture.getMontant());
                    }
                }

                for (OrdreVersementCompta ov : operations.getOrdresVersements()) {
                    OrdreversementTiers ovPersonne = data.getMapIdTierDescription().get(ov.getIdTiers());

                    OrdreversementTiers personne = resolveAssure(data, ov);

                    fillPojoListOV(list, setIdTiers, lot, ov, personne, ovPersonne);
                    total = total.add(ov.getMontant());
                }
            }

            // --------------------------------
            // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
            try {
                if (wantCommunePolitique && !list.isEmpty()) {
                    Date date = new SimpleDateFormat("dd.MM.yyyy").parse(PegasusDateUtil
                            .setDateMaxDayOfMonth(JadeStringUtil.isEmpty(lot.getDateEnvoi()) ? JACalendar
                                    .todayJJsMMsAAAA() : lot.getDateEnvoi()));
                    Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date,
                            session);

                    for (POJO pojo : list) {
                        pojo.setCommunePolitique(mapCommuneParIdTiers.get(pojo.getIdTiers()));
                    }
                }
            } catch (Exception e) {
                new DocException("Unable to search CommunePolitique by idTiers", e);
            }
            // ------------------------------
            // Tri et remplir le container depuis la liste triée
            Collections.sort(list);
            for (POJO pojo : list) {
                container.put("communePolitique", pojo.getCommunePolitique());
                container.put("typeOv", pojo.getTypeOv());

                container.put("descriptionAssure", pojo.getDescriptionAssure());
                container.put("adressePaiment", pojo.getAdressePaiment());
                container.put("adresseTiers", pojo.getAdresseTiers());
                container.put("montant", pojo.getMontant());
            }
            container.put("total", new FWCurrency(total.toString()).toStringFormat());
            container.put("totalDette", new FWCurrency(totalDette.toString()).toStringFormat());
            container.put("totalOrdreGroupe", new FWCurrency(total.subtract(totalDette).toString()).toStringFormat());

        } catch (OrdreVersementException e) {
            throw new DocException("Unable to search the ordreVersement", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DocException("Service not available", e);
        } catch (JadePersistenceException e) {
            throw new DocException("Persistence exception", e);
        } catch (JAException e) {
            throw new DocException("Jade exception", e);
        }

        return container;
    }

    public void fillPojoListEcriture(List<POJO> list, Set<String> setIdTiers, Ecriture e, OrdreversementTiers personne)
            throws JadePersistenceException, JadeApplicationException, JadeApplicationServiceNotAvailableException {
        AdresseTiersDetail detailAdressePaiementTiers = null;

        String adressePaiement = CABusinessServiceLocator.getSectionService().findDescription(
                e.getSectionSimple().getIdSection());

        JadeCodeSysteme code = JadeBusinessServiceLocator.getCodeSystemeService().getCodeSysteme(
                e.getOrdreVersement().getCsType());
        String cs = code.getTraduction(Langues.getLangueDepuisCodeIso(BSessionUtil.getSessionFromThreadContext()
                .getIdLangueISO()));

        POJO pojo = new POJO();
        pojo.setTypeOv(cs);
        pojo.setDescriptionAssure(formatDescPersonne(personne));
        pojo.setAdressePaiment(adressePaiement);
        pojo.setAdresseTiers(fromatTier(detailAdressePaiementTiers));
        pojo.setMontant(new FWCurrency(e.getMontant().toString()).toStringFormat());
        pojo.setNss(personne.getNumAvs());
        pojo.setNom(personne.getDesignation1());
        pojo.setPrenom(personne.getDesignation2());
        pojo.setIdTiers(personne.getIdTiers());
        setIdTiers.add(personne.getIdTiers());
        list.add(pojo);
    }

    public void fillPojoListOV(List<POJO> list, Set<String> setIdTiers, SimpleLot lot, OrdreVersementCompta ov,
            OrdreversementTiers personne, OrdreversementTiers ovPersonne) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {
        AdresseTiersDetail detailAdressePaiementTiers = null;
        String adressePaiement = "";
        if (!JadeStringUtil.isEmpty(ov.getIdTiersAdressePaiement())) {
            detailAdressePaiementTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                    ov.getIdTiersAdressePaiement(), Boolean.TRUE, ov.getIdDomaineApplication(),
                    JadeStringUtil.isEmpty(lot.getDateEnvoi()) ? JACalendar.todayJJsMMsAAAA() : lot.getDateEnvoi(), "");
        }

        adressePaiement = fromatBanque(detailAdressePaiementTiers);

        JadeCodeSysteme code = JadeBusinessServiceLocator.getCodeSystemeService().getCodeSysteme(ov.getCsTypeOv());
        String cs = code.getTraduction(Langues.getLangueDepuisCodeIso(BSessionUtil.getSessionFromThreadContext()
                .getIdLangueISO()));

        String desPersonne = "";
        if (ovPersonne != null) {
            desPersonne = ovPersonne.getDesignation1() + " " + ovPersonne.getDesignation2();
        }

        POJO pojo = new POJO();
        pojo.setTypeOv(cs + " " + desPersonne);
        pojo.setDescriptionAssure(formatDescPersonne(personne));
        pojo.setAdressePaiment(adressePaiement);
        pojo.setAdresseTiers(fromatTier(detailAdressePaiementTiers));
        pojo.setMontant(new FWCurrency(ov.getMontant().toString()).toStringFormat());
        pojo.setNss(personne.getNumAvs());
        pojo.setNom(personne.getDesignation1());
        pojo.setPrenom(personne.getDesignation2());
        pojo.setIdTiers(personne.getIdTiers());
        setIdTiers.add(personne.getIdTiers());
        list.add(pojo);
    }

    private OrdreversementTiers resolveAssure(ComptabilisationData data, OrdreVersementCompta ov) throws DocException {
        OrdreversementTiers personne = null;
        personne = data.getMapIdTierDescription().get(ov.getCompteAnnexe().getIdTiers());
        if (ov.isTypeBeneficiaire()) {
            if (!ov.isRequerant()) {
                personne = data.getMapIdTierDescription().get(ov.getIdTiers());
            }
        }

        if (personne == null) {
            throw new DocException(
                    "Impossible de trouver la  déscription de l'asssuré avec l'idTiers du compte annexe:"
                            + ov.getCompteAnnexe().getIdTiers() + ", idTiers lié à l'OV:" + ov.getIdTiers());
        }

        return personne;
    }

    private class POJO implements Comparable<POJO> {
        private String idTiers;
        private String nss;
        private String nom;
        private String prenom;
        private String descriptionAssure;
        private String typeOv;
        private String adressePaiment;
        private String adresseTiers;
        private String montant;
        private String communePolitique;

        public final String getIdTiers() {
            return idTiers;
        }

        public final void setIdTiers(String idTiers) {
            this.idTiers = idTiers;
        }

        public final String getNss() {
            return nss;
        }

        public final void setNss(String nss) {
            this.nss = nss;
        }

        public final String getNom() {
            return nom;
        }

        public final void setNom(String nom) {
            this.nom = nom;
        }

        public final String getPrenom() {
            return prenom;
        }

        public final void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public final String getDescriptionAssure() {
            return descriptionAssure;
        }

        public final void setDescriptionAssure(String descriptionAssure) {
            this.descriptionAssure = descriptionAssure;
        }

        public final String getTypeOv() {
            return typeOv;
        }

        public final void setTypeOv(String typeOv) {
            this.typeOv = typeOv;
        }

        public final String getAdressePaiment() {
            return adressePaiment;
        }

        public final void setAdressePaiment(String adressePaiment) {
            this.adressePaiment = adressePaiment;
        }

        public final String getAdresseTiers() {
            return adresseTiers;
        }

        public final void setAdresseTiers(String adresseTiers) {
            this.adresseTiers = adresseTiers;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        public final String getMontant() {
            return montant;
        }

        public final void setMontant(String montant) {
            this.montant = montant;
        }

        @Override
        public int compareTo(POJO o) {
            if (wantCommunePolitique) {
                int result1 = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (result1 != 0) {
                    return result1;
                }
            }

            int result2 = getNom().compareTo(o.getNom());
            if (result2 != 0) {
                return result2;
            }

            int result3 = getPrenom().compareTo(o.getPrenom());
            if (result3 != 0) {
                return result3;
            }

            return getNss().compareTo(o.getNss());
        }
    }
}
