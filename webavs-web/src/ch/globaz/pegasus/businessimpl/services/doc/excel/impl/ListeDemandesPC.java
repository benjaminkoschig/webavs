package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
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
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRStringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class ListeDemandesPC extends PegasusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeDemandesPC.xml";
    public final static String MODEL_NAME_COM_POL = "listeDemandesPC_CommunePolitique.xml";

    private boolean wantCommunePolitique;

    private String dateDebut = null;
    private String dateFin = null;
    private String idGestionnaire = null;
    private String outPutName = "liste_Demandes_PC";

    private ExcelmlWorkbook createDoc(String dateDebut, String dateFin, String idGestionnaire) throws DocException {
        if (dateDebut == null) {
            throw new DocException("Unable to execute createDoc, the dateMonth is null!");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idGestionnaire = idGestionnaire;
        return this.createDoc();
    }

    public String createDocAndSave(String dateDebut, String dateFin, String idGestionnaire) throws Exception {
        wantCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        ExcelmlWorkbook wk = this.createDoc(dateDebut, dateFin, idGestionnaire);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    @Override
    public String getModelName() {
        if (wantCommunePolitique) {
            return ListeDemandesPC.MODEL_NAME_COM_POL;
        }
        return ListeDemandesPC.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        PegasusContainer container = new PegasusContainer();

        BSession session = BSessionUtil.getSessionFromThreadContext();

        String titre = session.getLabel("EXCEL_LISTE_DEMANDES_PC_TITRE");
        titre = PRStringUtils.replaceString(titre, "{dategeneration}", JadeDateUtil.getGlobazFormattedDate(new Date()));
        titre = PRStringUtils.replaceString(titre, "{datedebut}", dateDebut);
        titre = PRStringUtils.replaceString(titre, "{datefin}", dateFin);

        container.put("TITLE", titre.toString());
        container.put("INDE_USER", session.getUserName());
        // remplacement des entêtes
        String[] champs = new String[] { "CHAMP_USER", "CHAMP_COMMUNE_POLITIQUE", "CHAMP_NSS", "CHAMP_NOM",
                "CHAMP_ETAT_DEMANDE", "CHAMP_DATE_DEPOT", "CHAMP_GESTIONNAIRE" };
        for (String champ : champs) {
            container.put(champ, session.getLabel("EXCEL_LISTE_DEMANDES_PC_" + champ));
        }

        // recherche des données

        DemandeSearch searchModel = new DemandeSearch();
        searchModel.setWhereKey("listeDemandesPC");
        searchModel.setOrderKey("nomPrenom");
        searchModel.setForDateDepotMin("01." + dateDebut);
        searchModel.setForDateDepotMax(PegasusDateUtil.setDateMaxDayOfMonth(dateFin));
        if (!JadeStringUtil.isBlankOrZero(idGestionnaire)) {
            searchModel.setForIdGestionnaire(idGestionnaire);
        }
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel = PegasusServiceLocator.getDemandeService().search(searchModel);

        // D0118 remplir une liste de POJO avec le resultset
        List<POJO> list = new ArrayList<POJO>();
        Set<String> setIdTiers = new HashSet<String>();

        // enrichissement du container
        for (JadeAbstractModel absDonnee : searchModel.getSearchResults()) {
            Demande donnee = (Demande) absDonnee;

            PersonneEtendueComplexModel personne = donnee.getDossier().getDemandePrestation().getPersonneEtendue();
            POJO pojo = new POJO();
            pojo.setNss(personne.getPersonneEtendue().getNumAvsActuel());
            pojo.setNomPrenom(PegasusUtil.formatNomPrenom(personne.getTiers()));
            pojo.setNom(personne.getTiers().getDesignation1());
            pojo.setPrenom(personne.getTiers().getDesignation2());
            pojo.setEtatDemande(session.getCodeLibelle(donnee.getSimpleDemande().getCsEtatDemande()));
            pojo.setDateDepot(donnee.getSimpleDemande().getDateDepot());
            pojo.setGestionnaire(donnee.getSimpleDemande().getIdGestionnaire());
            pojo.setIdTiers(personne.getPersonneEtendue().getIdTiers());
            setIdTiers.add(personne.getPersonneEtendue().getIdTiers());
            list.add(pojo);
        }
        // --------------------------------
        // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
        try {
            if (wantCommunePolitique && !list.isEmpty()) {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(PegasusDateUtil.setDateMaxDayOfMonth(dateFin));
                Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date, session);

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
            container.put("INDE_COMMUNE_POLITIQUE", pojo.getCommunePolitique());
            container.put("INDE_NSS", pojo.getNss());
            container.put("INDE_NOM", pojo.getNomPrenom());
            container.put("INDE_ETAT_DEMANDE", pojo.getEtatDemande());
            container.put("INDE_DATE_DEPOT", pojo.getDateDepot());
            container.put("INDE_GESTIONNAIRE", pojo.getGestionnaire());
        }
        return container;
    }

    private class POJO implements Comparable<POJO> {
        private String idTiers;
        private String nss;
        private String nomPrenom;
        private String nom;
        private String prenom;
        private String etatDemande;
        private String dateDepot;
        private String gestionnaire;
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

        public final String getNomPrenom() {
            return nomPrenom;
        }

        public final void setNomPrenom(String nomPrenom) {
            this.nomPrenom = nomPrenom;
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

        public final String getEtatDemande() {
            return etatDemande;
        }

        public final void setEtatDemande(String etatDemande) {
            this.etatDemande = etatDemande;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        public final String getDateDepot() {
            return dateDepot;
        }

        public final void setDateDepot(String dateDepot) {
            this.dateDepot = dateDepot;
        }

        public final String getGestionnaire() {
            return gestionnaire;
        }

        public final void setGestionnaire(String gestionnaire) {
            this.gestionnaire = gestionnaire;
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

            return getPrenom().compareTo(o.getPrenom());
        }
    }

}
