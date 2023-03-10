package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import ch.globaz.pegasus.business.constantes.IPCDecision;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeCodesSystemsUtil;
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
import globaz.webavs.common.CommonExcelmlContainer;
import java.text.SimpleDateFormat;
import java.util.*;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.models.decision.ListDecisionsValidees;
import ch.globaz.pegasus.business.models.decision.ListDecisionsValideesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;

public class ListeDecisionsValidees extends PegasusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeDecisionsValidees.xml";
    public final static String MODEL_NAME_COM_POL = "listeDecisionsValidees_CommunePolitique.xml";

    private boolean wantCommunePolitique;
    private String dateDebut = null;
    private String dateFin = null;
    private String outPutName = "liste_Decisions_Validees";

    private ExcelmlWorkbook createDoc(String dateDebut, String dateFin) throws DocException {
        if (dateDebut == null) {
            throw new DocException("Unable to execute createDoc, the dateDebut is null!");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        return this.createDoc();
    }

    public String createDocAndSave(String dateDebut, String dateFin) throws Exception {
        wantCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        ExcelmlWorkbook wk = this.createDoc(dateDebut, dateFin);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    @Override
    public String getModelName() {
        if (wantCommunePolitique) {
            return ListeDecisionsValidees.MODEL_NAME_COM_POL;
        }
        return ListeDecisionsValidees.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        BSession session = BSessionUtil.getSessionFromThreadContext();

        String titre = session.getLabel("EXCEL_LISTE_DECISIONS_VALIDEES_TITRE");
        titre = PRStringUtils.replaceString(titre, "{dategeneration}", JadeDateUtil.getGlobazFormattedDate(new Date()));
        titre = PRStringUtils.replaceString(titre, "{datedebut}", dateDebut);
        titre = PRStringUtils.replaceString(titre, "{datefin}", dateFin);

        container.put("TITLE", titre.toString());
        container.put("USER", session.getUserName());
        // remplacement des ent?tes
        String[] champs = new String[] { "CHAMP_USER", "CHAMP_COMMUNE_POLITIQUE", "CHAMP_NSS", "CHAMP_NOM",
                "CHAMP_PRENOM", "CHAMP_TYPE_PC", "CHAMP_DATE_DECISION", "CHAMP_TYPE","CHAMP_ETAT", "CHAMP_MOTIF",
                "CHAMP_VALIDE_PAR", "CHAMP_DATE_VALIDATION" };
        for (String champ : champs) {
            container.put(champ, session.getLabel("EXCEL_LISTE_DECISIONS_VALIDEES_" + champ));
        }

        // recherche des donn?es

        ListDecisionsValideesSearch searchModel = new ListDecisionsValideesSearch();
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel.setForDateDecisionMin("01." + dateDebut);
        searchModel.setForDateDecisionMax(PegasusDateUtil.setDateMaxDayOfMonth(dateFin));
        searchModel = PegasusServiceLocator.getListDecisionsValideesService().search(searchModel);

        // D0118 remplir une liste de POJO avec le resultset
        List<POJO> list = new ArrayList<POJO>();
        Set<String> setIdTiers = new HashSet<String>();
        Map<String,String> mapCsToLibelle = new HashMap<>();
        String libelle;
        for (JadeAbstractModel absDonnee : searchModel.getSearchResults()) {
            ListDecisionsValidees donnee = (ListDecisionsValidees) absDonnee;

            POJO pojo = new POJO();
            pojo.setNss(donnee.getNss());
            pojo.setNom(donnee.getNom());
            pojo.setPrenom(donnee.getPrenom());
            pojo.setTypePC(session.getCodeLibelle(donnee.getCsTypePCAccordee()));
            pojo.setDateDecision(donnee.getDateDecision());
            pojo.setType(session.getCodeLibelle(donnee.getTypeDecision()));
            pojo.setValidePar(donnee.getValidePar());
            pojo.setDateValidation(donnee.getDateValidation());
            if(donnee.getDecisionProvisoire()){
                pojo.setEtat(session.getLabel("CHAMPS_PROVISOIRE"));
            }else{
//                switch (donnee.getCsEtat()){
//                    case IPCDecision.CS_ETAT_ENREGISTRE :
//                    case IPCDecision.CS_ETAT_PRE_VALIDE :
//                        if(mapCsToLibelle.containsKey(donnee.getCsEtat())){
//                            libelle = mapCsToLibelle.get(donnee.getCsEtat());
//                        }else{
//                            libelle = JadeCodesSystemsUtil.getCodeLibelle(donnee.getCsEtat());
//                            mapCsToLibelle.put(donnee.getCsEtat(),libelle);
//                        }
//                        pojo.setEtat(libelle);
//                        break;
//                    case IPCDecision.CS_ETAT_DECISION_VALIDE:
//                        pojo.setEtat(session.getLabel("CHAMPS_DEFINITIF"));
//                        break;
//
//                }
                pojo.setEtat(session.getLabel("CHAMPS_DEFINITIF"));
            }


            String motif = donnee.getCsMotifRefus();
            if (JadeStringUtil.isEmpty(motif)) {
                motif = donnee.getCsMotifSuppression();
            }

            pojo.setMotif(session.getCodeLibelle(motif));

            pojo.setIdTiers(donnee.getIdTiers());
            setIdTiers.add(donnee.getIdTiers());
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
        // Tri et remplir le container depuis la liste tri?e
        Collections.sort(list);
        for (POJO pojo : list) {
            container.put("COMMUNE_POLITIQUE", pojo.getCommunePolitique());
            container.put("NSS", pojo.getNss());
            container.put("NOM", pojo.getNom());
            container.put("PRENOM", pojo.getPrenom());
            container.put("TYPE_PC", pojo.getTypePC());
            container.put("DATE_DECISION", pojo.getDateDecision());
            container.put("TYPE", pojo.getType());
            container.put("VALIDE_PAR", pojo.getValidePar());
            container.put("DATE_VALIDATION", pojo.getDateValidation());
            container.put("MOTIF", pojo.getMotif());
            container.put("ETAT",pojo.getEtat());
        }

        return container;
    }

    private class POJO implements Comparable<POJO> {
        private String idTiers;
        private String nss;
        private String nom;
        private String prenom;
        private String typePC;
        private String dateDecision;
        private String type;
        private String validePar;
        private String dateValidation;
        private String motif;
        private String communePolitique;
        private String etat;

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

        public final String getTypePC() {
            return typePC;
        }

        public final void setTypePC(String typePC) {
            this.typePC = typePC;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        public final String getDateDecision() {
            return dateDecision;
        }

        public final void setDateDecision(String dateDecision) {
            this.dateDecision = dateDecision;
        }

        public final String getType() {
            return type;
        }

        public final void setType(String type) {
            this.type = type;
        }

        public final String getValidePar() {
            return validePar;
        }

        public final void setValidePar(String validePar) {
            this.validePar = validePar;
        }

        public final String getDateValidation() {
            return dateValidation;
        }

        public final void setDateValidation(String dateValidation) {
            this.dateValidation = dateValidation;
        }

        public final String getMotif() {
            return motif;
        }

        public final void setMotif(String motif) {
            this.motif = motif;
        }
        public String getEtat() {
            return etat;
        }

        public void setEtat(String etat) {
            this.etat = etat;
        }

        @Override
        public int compareTo(POJO o) {
            if (wantCommunePolitique) {
                int result1 = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (result1 != 0) {
                    return result1;
                }
            }
            return 0;
        }
    }

}
