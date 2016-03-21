package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.webavs.common.CommonExcelmlContainer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtenduSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtenduSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;

public class ListeAllocationsFamiliales extends PegasusAbstractExcelServiceImpl {

    public final static String MODEL_NAME = "listeAllocationsFamiliales.xml";
    public final static String MODEL_NAME_COM_POL = "listeAllocationsFamiliales_CommunePolitique.xml";

    private boolean wantCommunePolitique;
    private String dateMonth = null;
    private String outPutName = "liste_Allocations_Familiales";

    private ExcelmlWorkbook createDoc(String dateMonth) throws DocException {
        if (dateMonth == null) {
            throw new DocException("Unable to execute createDoc, the dateMonth is null!");
        }
        this.dateMonth = dateMonth;
        return this.createDoc();
    }

    public String createDocAndSave(String date) throws Exception {
        wantCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        ExcelmlWorkbook wk = this.createDoc(date);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        return save(wk, nomDoc);
    }

    @Override
    public String getModelName() {
        if (wantCommunePolitique) {
            return ListeAllocationsFamiliales.MODEL_NAME_COM_POL;
        }
        return ListeAllocationsFamiliales.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    private void createListeAF(CommonExcelmlContainer container, BSession session) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AllocationsFamilialesException {

        // recherche des données

        AllocationsFamilialesEtenduSearch searchAF = new AllocationsFamilialesEtenduSearch();
        searchAF.setForDateMax(dateMonth);
        searchAF.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchAF.setWhereKey("forVersionedListeAF");
        searchAF = PegasusImplServiceLocator.getAllocationsFamilialesEtenduService().search(searchAF);

        // D0118 remplir une liste de POJO avec le resultset
        List<POJO> list = new ArrayList<POJO>();
        Set<String> setIdTiers = new HashSet<String>();

        // enrichissement du container
        for (JadeAbstractModel absDonnee : searchAF.getSearchResults()) {
            AllocationsFamilialesEtendu donnee = (AllocationsFamilialesEtendu) absDonnee;

            if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(donnee.getDroitMembreFamille().getSimpleDroitMembreFamille()
                    .getCsRoleFamillePC())) {

                POJO pojo = new POJO();
                pojo.setNss(donnee.getDroitMembreFamilleRequerant().getMembreFamille().getPersonneEtendue()
                        .getPersonneEtendue().getNumAvsActuel());
                pojo.setNom(PegasusUtil.formatNomPrenom(donnee.getDroitMembreFamilleRequerant().getMembreFamille()
                        .getPersonneEtendue().getTiers()));

                pojo.setNssEnfant(donnee.getDroitMembreFamille().getMembreFamille().getPersonneEtendue()
                        .getPersonneEtendue().getNumAvsActuel());
                pojo.setNomEnfant(PegasusUtil.formatNomPrenom(donnee.getDroitMembreFamille().getMembreFamille()
                        .getPersonneEtendue().getTiers()));
                pojo.setMontant(donnee.getAllocationsFamiliales().getSimpleAllocationsFamiliales().getMontantMensuel());

                String idTiers = donnee.getDroitMembreFamilleRequerant().getMembreFamille().getPersonneEtendue()
                        .getPersonneEtendue().getIdTiers();
                pojo.setIdTiers(idTiers);
                setIdTiers.add(idTiers);
                list.add(pojo);
            }
        }
        // --------------------------------
        // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
        try {
            if (wantCommunePolitique && !list.isEmpty()) {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(PegasusDateUtil.setDateMaxDayOfMonth("01."
                        + dateMonth));
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
            container.put("AF_COMMUNE_POLITIQUE", pojo.getCommunePolitique());
            container.put("AF_NSS", pojo.getNss());
            container.put("AF_NOM", pojo.getNom());
            container.put("AF_NSS_ENFANT", pojo.getNssEnfant());
            container.put("AF_NOM_ENFANT", pojo.getNomEnfant());
            container.put("AF_MONTANT", pojo.getMontant());
        }
    }

    private void createListeIndependants(CommonExcelmlContainer container, BSession session)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RevenuActiviteLucrativeIndependanteException, DroitException {

        // recherche des données

        RevenuActiviteLucrativeIndependanteEtenduSearch searchAF = new RevenuActiviteLucrativeIndependanteEtenduSearch();
        searchAF.setForDateMax(dateMonth);
        searchAF.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchAF = PegasusImplServiceLocator.getRevenuActiviteLucrativeIndependanteEtenduService().search(searchAF);

        Set<String> idDroits = new TreeSet<String>();

        Map<String, String[]> listeDonneesRequerants = new HashMap<String, String[]>();

        for (JadeAbstractModel absDonnee : searchAF.getSearchResults()) {
            RevenuActiviteLucrativeIndependanteEtendu donnee = (RevenuActiviteLucrativeIndependanteEtendu) absDonnee;

            DroitMembreFamille personne = donnee.getDroitMembreFamille();
            if (!IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(personne.getSimpleDroitMembreFamille().getCsRoleFamillePC())) {
                String idDroit = personne.getSimpleDroitMembreFamille().getIdDroit();
                idDroits.add(idDroit);
                listeDonneesRequerants.put(idDroit, new String[] {
                        personne.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel(),
                        PegasusUtil.formatNomPrenom(personne.getMembreFamille().getPersonneEtendue().getTiers()),
                        personne.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getIdTiers() });
            }
        }

        DroitMembreFamilleSearch searchMbr = new DroitMembreFamilleSearch();
        searchMbr.setForInIdDroit(new ArrayList<String>(idDroits));
        searchMbr.setOrderKey("orderByNom");
        searchMbr.setForCsRoletMembreFamilleIn(new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(IPCDroits.CS_ROLE_FAMILLE_ENFANT);
            }
        });

        searchMbr = PegasusServiceLocator.getDroitService().searchDroitMembreFamille(searchMbr);

        // D0118 remplir une liste de POJO avec le resultset
        List<POJO> list = new ArrayList<POJO>();
        Set<String> setIdTiers = new HashSet<String>();

        for (JadeAbstractModel absDonnee : searchMbr.getSearchResults()) {
            DroitMembreFamille personne = (DroitMembreFamille) absDonnee;

            if (!listeDonneesRequerants.containsKey(personne.getSimpleDroitMembreFamille().getIdDroit())) {
                throw new DroitException("Requerant not found for membre famille ("
                        + personne.getSimpleDroitMembreFamille().getIdDroitMembreFamille() + ") !");
            }
            String[] donneesRequerant = listeDonneesRequerants.get(personne.getSimpleDroitMembreFamille().getIdDroit());
            POJO pojo = new POJO();
            pojo.setNss(donneesRequerant[0]);
            pojo.setNom(donneesRequerant[1]);
            pojo.setNssEnfant(personne.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
            pojo.setNomEnfant(PegasusUtil.formatNomPrenom(personne.getMembreFamille().getPersonneEtendue().getTiers()));

            String idTiers = donneesRequerant[2];
            pojo.setIdTiers(idTiers);
            setIdTiers.add(idTiers);
            list.add(pojo);
        }
        // --------------------------------
        // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
        try {
            if (wantCommunePolitique && !list.isEmpty()) {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(PegasusDateUtil.setDateMaxDayOfMonth("01."
                        + dateMonth));
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
            container.put("INDE_NOM", pojo.getNom());
            container.put("INDE_NSS_ENFANT", pojo.getNssEnfant());
            container.put("INDE_NOM_ENFANT", pojo.getNomEnfant());
        }

    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        // remplacement des entêtes
        BSession session = BSessionUtil.getSessionFromThreadContext();
        container.put("USER", session.getUserName());
        String[] champs = new String[] { "CHAMP_USER", "AF_CHAMP_COMMUNE_POLITIQUE", "AF_CHAMP_NSS", "AF_CHAMP_NOM",
                "AF_CHAMP_NSS_ENFANT", "AF_CHAMP_NOM_ENFANT", "AF_CHAMP_MONTANT", "INDE_CHAMP_COMMUNE_POLITIQUE",
                "INDE_CHAMP_NSS", "INDE_CHAMP_NOM", "INDE_CHAMP_NSS_ENFANT", "INDE_CHAMP_NOM_ENFANT" };
        for (String champ : champs) {
            container.put(champ, session.getLabel("EXCEL_LISTE_CAS_AF_" + champ));
        }

        createListeAF(container, session);
        createListeIndependants(container, session);

        return container;
    }

    private class POJO implements Comparable<POJO> {
        private String idTiers;
        private String nss;
        private String nom;
        private String nssEnfant;
        private String nomEnfant;
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

        public final String getNssEnfant() {
            return nssEnfant;
        }

        public final void setNssEnfant(String nssEnfant) {
            this.nssEnfant = nssEnfant;
        }

        public final String getNomEnfant() {
            return nomEnfant;
        }

        public final void setNomEnfant(String nomEnfant) {
            this.nomEnfant = nomEnfant;
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
            return 0;
        }
    }

}
