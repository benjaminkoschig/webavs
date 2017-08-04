package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRStringUtils;
import globaz.webavs.common.CommonExcelmlContainer;
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
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListRevisions;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListRevisionsSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;

public class ListeExcelRevisions extends PegasusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeRevisions.xml";
    // version avec commune politique
    public final static String MODEL_NAME_COM_POL = "listeRevisions_CommunePolitique.xml";

    private boolean wantCommunePolitique;
    private String annee = null;

    private String moisAnnee = null;
    private String outPutName = "liste_revisions";

    public ExcelmlWorkbook createDoc(String annee, String moisAnnee) throws DocException {
        if (JadeStringUtil.isBlankOrZero(annee) && JadeStringUtil.isBlankOrZero(moisAnnee)) {
            throw new DocException("Unable to execute createDoc, annee and moisAnnee are null!");
        }

        if (!JadeStringUtil.isBlankOrZero(annee) && !JadeStringUtil.isBlankOrZero(moisAnnee)) {
            throw new DocException("Unable to execute createDoc, annee and moisAnnee are both used!");
        }

        this.annee = annee;
        this.moisAnnee = moisAnnee;
        return this.createDoc();
    }

    public String createDocAndSave(String annee, String moisAnnee) throws Exception {
        wantCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        ExcelmlWorkbook wk = this.createDoc(annee, moisAnnee);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    @Override
    public String getModelName() {
        if (wantCommunePolitique) {
            return ListeExcelRevisions.MODEL_NAME_COM_POL;
        }
        return ListeExcelRevisions.MODEL_NAME;

    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        // container de data pour l'excel
        CommonExcelmlContainer container = new CommonExcelmlContainer();
        // Recherche des cas
        ListRevisionsSearch listRevisionsSearch = new ListRevisionsSearch();
        listRevisionsSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        String dateTitre = " ";
        if (!JadeStringUtil.isBlankOrZero(moisAnnee)) {
            listRevisionsSearch.setForMoisAnnee(moisAnnee);
            listRevisionsSearch.setForDateFin(moisAnnee);

            dateTitre += moisAnnee;
        } else {
            listRevisionsSearch.setForMoisAnneeGreaterOrEquals("01." + annee);
            listRevisionsSearch.setForMoisAnneeLessOrEquals("12." + annee);
            listRevisionsSearch.setForDateFin("12." + annee);
            dateTitre += annee;
        }

        // K160811_003 :
        // si on précise pas la date début à 0 la requete va contrôler que la date est soit null soit = à 0, mais pas
        // plus grand
        listRevisionsSearch.setForDateDebut("0");

        try {
            listRevisionsSearch = PegasusServiceLocator.getDemandeService().searchRevisions(listRevisionsSearch);
        } catch (DemandeException e) {
            throw new DocException("Unable to search the demandes for revision", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DocException("Service not available", e);
        } catch (JadePersistenceException e) {
            throw new DocException("Persistence exception", e);
        }

        // init du container de data pour le document avec les titres
        String titre = JadeThread.getMessage("pegasus.excel.listeRevisions.libelleTitre");
        titre = PRStringUtils.replaceString(titre, "{annee}", dateTitre);
        titre = PRStringUtils.replaceString(titre, "{dategeneration}", JACalendar.todayJJsMMsAAAA());
        container.put("libelleTitre", titre);

        String[] champs = new String[] { "libelleUser", "libelleCommunePolitique", "libelleNSS", "libelleNom",
                "libelleDateNaissance", "libelleSexe", "libelleNationalite", "libelleGestionnaire",
                "libelleDateRevision", "libelleMotifRevision", "libelleAgenceCommunale" };
        for (String champ : champs) {
            container.put(champ, JadeThread.getMessage("pegasus.excel.listeRevisions." + champ));
        }

        // D0118 remplir une liste de POJO avec le resultset
        BSession session = BSessionUtil.getSessionFromThreadContext();
        container.put("user", session.getUserName());
        List<POJO> list = new ArrayList<POJO>();
        Set<String> setIdTiers = new HashSet<String>();
        String lastIdTiers = null;
        for (int i = 0; i < listRevisionsSearch.getSize(); i++) {
            ListRevisions entity = (ListRevisions) listRevisionsSearch.getSearchResults()[i];

            if (entity.getIdTiers().equals(lastIdTiers)) {
                continue;
            }

            POJO pojo = new POJO();
            pojo.setNss(entity.getNumAvsActuel());
            pojo.setNom(stripNonValidXMLCharacters(PegasusUtil.formatNomPrenom(entity.getDesignation1(),
                    entity.getDesignation2())));

            pojo.setDateNaissance(entity.getDateNaissance());
            pojo.setSexe(session.getCodeLibelle(entity.getSexe()));
            pojo.setNationalite(stripNonValidXMLCharacters(session.getCodeLibelle(session.getSystemCode("CIPAYORI",
                    entity.getIdPays()))));
            pojo.setGestionnaire(entity.getIdGestionnaire());
            pojo.setDateRevision(entity.getDateProchaineRevision());
            pojo.setMotifRevision(stripNonValidXMLCharacters(entity.getMotifProchaineRevision()));
            String agenceInfo = entity.getAgenceDesignation1() + " " + entity.getAgenceDesignation2();
            if (JadeStringUtil.isBlank(agenceInfo)) {
                agenceInfo = JadeThread.getMessage("pegasus.excel.listeRevisions.agenceNotFoundMessage");
            }

            pojo.setAgenceCommunale(stripNonValidXMLCharacters(agenceInfo));
            pojo.setIdTiers(entity.getIdTiers());
            lastIdTiers = entity.getIdTiers();
            setIdTiers.add(entity.getIdTiers());
            list.add(pojo);
        }
        // --------------------------------
        // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
        try {
            if (wantCommunePolitique) {
                String finalDate = JadeDateUtil.getLastDateOfMonth("01."
                        + (JadeStringUtil.isBlankOrZero(moisAnnee) ? "12." + annee : moisAnnee));
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(finalDate);
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
            container.put("commune_politique", pojo.getCommunePolitique());

            container.put("nss", pojo.getNss());
            container.put("nom", pojo.getNom());
            container.put("date_naissance", pojo.getDateNaissance());
            container.put("sexe", pojo.getSexe());
            container.put("nationalite", pojo.getNationalite());

            container.put("gestionnaire", pojo.getGestionnaire());
            container.put("dateRevision", pojo.getDateRevision());
            container.put("motif_revision", pojo.getMotifRevision());

            container.put("agence_communale", pojo.getAgenceCommunale());

        }

        return container;
    }

    private String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if ((in == null) || ("".equals(in))) {
            return ""; // vacancy test.
        }
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF))
                    || ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF))) {
                out.append(current);
            }
        }
        return out.toString();
    }

    private class POJO implements Comparable<POJO> {
        private String idTiers;
        private String nss;
        private String nom;
        private String dateNaissance;
        private String sexe;
        private String nationalite;
        private String gestionnaire;
        private String dateRevision;
        private String motifRevision;
        private String agenceCommunale;
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

        public final String getDateNaissance() {
            return dateNaissance;
        }

        public final void setDateNaissance(String setDateNaissance) {
            dateNaissance = setDateNaissance;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        public final String getSexe() {
            return sexe;
        }

        public final void setSexe(String sexe) {
            this.sexe = sexe;
        }

        public final String getNationalite() {
            return nationalite;
        }

        public final void setNationalite(String nationalite) {
            this.nationalite = nationalite;
        }

        public final String getGestionnaire() {
            return gestionnaire;
        }

        public final void setGestionnaire(String gestionnaire) {
            this.gestionnaire = gestionnaire;
        }

        public final String getDateRevision() {
            return dateRevision;
        }

        public final void setDateRevision(String dateRevision) {
            this.dateRevision = dateRevision;
        }

        public final String getMotifRevision() {
            return motifRevision;
        }

        public final void setMotifRevision(String motifRevision) {
            this.motifRevision = motifRevision;
        }

        public final String getAgenceCommunale() {
            return agenceCommunale;
        }

        public final void setAgenceCommunale(String agenceCommunale) {
            this.agenceCommunale = agenceCommunale;
        }

        @Override
        public int compareTo(POJO o) {
            if (wantCommunePolitique) {
                int result1 = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (result1 != 0) {
                    return result1;
                }
            }

            int result2 = getNss().compareTo(o.getNss());
            if (result2 != 0) {
                return result2;
            }

            return getNom().compareTo(o.getNom());
        }
    }

}
