package globaz.cygnus.mappingXmlml;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.cygnus.db.ordresversements.RFOrdresVersementsManager;
import globaz.cygnus.db.paiement.RFSimulerValidationDecision;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author FHA
 * 
 */
public class RFXmlmlMappingLogListeControleValiderDecision {

    private static final String SEPARATEUR_TYPE_SOUS_TYPE = "\\.";
    private static final String SEPARATEUR_TYPES_SOUS_TYPES = ",";

    private static String[] getDecompteCodesTypesSousTypesDeSoinsAControler() throws Exception {

        String typesPointSousTypesStr = "";
        String[] typesPointSousTypesTab;
        try {
            typesPointSousTypesStr = RFPropertiesUtils.getDecompteTypeDeSoinsAControler();
            typesPointSousTypesTab = typesPointSousTypesStr
                    .split(RFXmlmlMappingLogListeControleValiderDecision.SEPARATEUR_TYPES_SOUS_TYPES);

        } catch (Exception e) {
            throw new Exception(
                    "RFXmlmlMappingLogListeControleValiderDecision.getDecompteCodesTypesSousTypesDeSoinsAControler(): La propriété "
                            + RFApplication.PROPERTY_DECOMPTE_SOUS_TYPES_DE_SOINS_A_CONTROLER + " est introuvable");
        }

        if (!JadeStringUtil.isBlank(typesPointSousTypesStr)) {

            String[] typesPointSousTypesMaj = new String[typesPointSousTypesTab.length];

            for (int i = 0; i < typesPointSousTypesTab.length; i++) {

                String[] typePointSousType = typesPointSousTypesTab[i]
                        .split(RFXmlmlMappingLogListeControleValiderDecision.SEPARATEUR_TYPE_SOUS_TYPE);

                if (typePointSousType[0].length() == 1) {
                    typePointSousType[0] = "0" + typePointSousType[0];
                }

                if (typePointSousType[1].length() == 1) {
                    typePointSousType[1] = "0" + typePointSousType[1];
                }

                typesPointSousTypesMaj[i] = typePointSousType[0] + "." + typePointSousType[1];
            }

            return typesPointSousTypesMaj;
        } else {
            return null;
        }
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    private static String getLibelleCourtSexe(String csSexe, BSession session) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return session.getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    private static String getLibellePays(String csNationalite, BSession session) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", csNationalite));
        }

    }

    private static String getNpaLocalite(BSession session, String idTiers) throws Exception {
        String npaLocalite = PRTiersHelper.getAdresseDomicileFormatee(session, idTiers);
        // enlever retours à la ligne restants
        if (!JadeStringUtil.isEmpty(npaLocalite)) {
            npaLocalite = npaLocalite.substring(0, npaLocalite.length() - 1);
            // ne garder que la 3ème ligne
            return npaLocalite.substring(npaLocalite.lastIndexOf('\n')).replaceAll("\n", "");
        } else {
            return "";
        }

    }

    private static boolean isPrestationSas(String idPrestation, String idAdressePaiement, String idTiersFondationSas,
            BSession session) throws Exception {

        if (idAdressePaiement.equals(idTiersFondationSas)) {
            RFOrdresVersementsManager rfOrdVerMgr = new RFOrdresVersementsManager();
            rfOrdVerMgr.setSession(session);
            rfOrdVerMgr.setForIdPrestation(idPrestation);
            rfOrdVerMgr.changeManagerSize(0);
            rfOrdVerMgr.find();

            Iterator<RFOrdresVersements> itr = rfOrdVerMgr.iterator();

            while (itr.hasNext()) {

                RFOrdresVersements rfOrdVer = itr.next();

                if (rfOrdVer != null) {

                    if (rfOrdVer.getIdSousTypeSoin().equals(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_AVANCES)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            return false;

        } else {
            return false;
        }
    }

    /**
     * Méthode permettant de créer le corps du document (
     */
    private static BigDecimal loadDetail(RFXmlmlContainer container, RFValidationDecisionPojo validationDecisionPojo,
            BSession session, String[] codesTypesSousTypesDeSoinsAcontroller) throws RFXmlmlException, Exception {

        String npaLocalite = RFXmlmlMappingLogListeControleValiderDecision.getNpaLocalite(session,
                validationDecisionPojo.getIdTiers());

        if (validationDecisionPojo.getAjouterCommunePolitique()) {
            container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_COMMUNE_POLITIQUE,
                    validationDecisionPojo.getCommunePolitique());
        }

        container.put(
                IRFListeControleValiderDecisionListeColumns.DETAIL_NOM_PRENOM,
                "\n"
                        + validationDecisionPojo.getNss()
                        + "\n"
                        + validationDecisionPojo.getNom()
                        + " "
                        + validationDecisionPojo.getPrenom()
                        + " / "
                        + validationDecisionPojo.getDateNaissance()
                        + " / "
                        + RFXmlmlMappingLogListeControleValiderDecision.getLibelleCourtSexe(
                                validationDecisionPojo.getCsSexe(), session)
                        + " / "
                        + RFXmlmlMappingLogListeControleValiderDecision.getLibellePays(
                                validationDecisionPojo.getCsNationalite(), session) + "\n");

        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_NPA, npaLocalite);

        // Recherche du nom et prénom du tiers de l'adresse de paiement
        String descAdressePaiement = "";
        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, validationDecisionPojo.getIdAdressePaiement());
        if (null != tw) {
            String adrPaiementNom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            // recherche de l'adresse de paiement
            TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                    session.getCurrentThreadTransaction(), validationDecisionPojo.getIdAdressePaiement(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                source.load(adresse);

                ITIAdresseFormater tiAdrPaiBanFor;

                // formatter le no de ccp ou le no bancaire
                if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                    tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
                } else {
                    tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
                }

                if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                    descAdressePaiement = "\n" + adrPaiementNom + "\n" + tiAdrPaiBanFor.format(source) + "\n";
                } else {
                    descAdressePaiement = "\n" + tiAdrPaiBanFor.format(source) + "\n";
                }

            } else {
                descAdressePaiement = "";
            }
        } else {
            descAdressePaiement = new RFXmlmlMappingFormatAdminAdressePaiement(session).getIt(validationDecisionPojo
                    .getIdAdressePaiement());
        }

        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_LIBELLE_BANQUE, descAdressePaiement);

        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_MONTANT_PREST, JadeStringUtil
                .isEmpty(validationDecisionPojo.getMontantPrestation()) ? "0.00" : new FWCurrency(
                validationDecisionPojo.getMontantPrestation()).toStringFormat());
        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_ID_PREST,
                validationDecisionPojo.getIdPrestation());

        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_A_CONTROLER,
                RFXmlmlMappingLogListeControleValiderDecision.rechercherTypeSousTypeDeSoinsAControlerParPrestation(
                        validationDecisionPojo.getIdPrestation(), codesTypesSousTypesDeSoinsAcontroller, session));

        // Ajout de chaque montant pour obtenir le total
        return new BigDecimal(validationDecisionPojo.getMontantPrestation());
    }

    /**
     * Methode permettant de creer le corps du document (
     */
    private static void loadDetailVide(RFXmlmlContainer container, BSession session) throws RFXmlmlException, Exception {

        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_NOM_PRENOM, "");
        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_NPA, "");
        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_LIBELLE_BANQUE, "");
        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_MONTANT_PREST, "");
        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_ID_PREST, "");
        container.put(IRFListeControleValiderDecisionListeColumns.DETAIL_A_CONTROLER, "");
    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private static void loadHeader(RFXmlmlContainer container, Boolean isSimulation, String idLot,
            String dateEcheancePaiement, BSession session, boolean wantCommunePolitique) {

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IRFListeControleValiderDecisionListeColumns.HEADER_BLANK_1, "");
        container.put(IRFListeControleValiderDecisionListeColumns.HEADER_BLANK_2, "");
        container.put(IRFListeControleValiderDecisionListeColumns.HEADER_BLANK_3, "");

        container.put(IRFListeControleValiderDecisionListeColumns.HEADER_ID_LOT, idLot);
        container.put(IRFListeControleValiderDecisionListeColumns.HEADER_DATE_DE_TRAITMENT,
                JACalendar.todayJJsMMsAAAA());

        container.put(IRFListeControleValiderDecisionListeColumns.FOOTER_TEXTE_MONTANT_TOTAL,
                session.getLabel("EXCEL_MONTANT_TOTAL_SIMULER_VALIDATION_DECISION"));

        container.put(IRFListeControleValiderDecisionListeColumns.FOOTER_TEXTE_MONTANT_TOTAL_SAS,
                session.getLabel("EXCEL_MONTANT_TOTAL_SAS_SIMULER_VALIDATION_DECISION"));

        if (wantCommunePolitique) {
            container.put("user", session.getUserName());
        }

        if (isSimulation) {
            container.put(IRFListeControleValiderDecisionListeColumns.HEADER_SIMULATION_OU_DEFINITIF,
                    session.getLabel("GENERATION_XLS_SIMULER_VALIDATION_DECISION_SIMULATION"));
            container.put(IRFListeControleValiderDecisionListeColumns.HEADER_DATE_ECHEANCE, "");
            container.put(IRFListeControleValiderDecisionListeColumns.HEADER_VARIABLE_DATE_ECHEANCE, "");
        } else {
            container.put(IRFListeControleValiderDecisionListeColumns.HEADER_SIMULATION_OU_DEFINITIF,
                    session.getLabel("GENERATION_XLS_SIMULER_VALIDATION_DECISION_DEFINITIF"));
            container.put(IRFListeControleValiderDecisionListeColumns.HEADER_DATE_ECHEANCE,
                    session.getLabel("GENERATION_XLS_SIMULER_VALIDATION_DECISION_DATE_ECHEANCE"));
            container.put(IRFListeControleValiderDecisionListeColumns.HEADER_VARIABLE_DATE_ECHEANCE,
                    dateEcheancePaiement);
        }
    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return
     * @throws RFXmlmlException
     * @throws Exception
     */
    public static RFXmlmlContainer loadResults(List<RFSimulerValidationDecision> validationDecisionList,
            BSession session, Boolean isSimulation, String idLot, String dateEcheancePaiement,
            String idTiersFondationSas, boolean wantCommunePolitique) throws RFXmlmlException, Exception {

        BigDecimal montantTotalPrestations = new BigDecimal(0);
        BigDecimal montantTotalPrestationsAvancesSas = new BigDecimal(0);
        Integer nbPrestations = new Integer(0);
        Integer nbPrestationsSas = new Integer(0);
        Set<String> setIdTiers = new HashSet<String>();
        RFXmlmlContainer xmlmlContainer = new RFXmlmlContainer();
        List<RFValidationDecisionPojo> validationDecisionPojoList = new ArrayList<RFValidationDecisionPojo>();

        // chargement du header
        RFXmlmlMappingLogListeControleValiderDecision.loadHeader(xmlmlContainer, isSimulation, idLot,
                dateEcheancePaiement, session, wantCommunePolitique);

        Boolean hasDecisionAValider = Boolean.FALSE;
        String[] codesTypesSousTypesDeSoinsAcontroller = RFXmlmlMappingLogListeControleValiderDecision
                .getDecompteCodesTypesSousTypesDeSoinsAControler();

        // création d'un pojo (utile notemment pour le tri)
        Iterator<RFSimulerValidationDecision> validationDecisionsItr = validationDecisionList.iterator();
        while (validationDecisionsItr.hasNext()) {
            RFSimulerValidationDecision validationDecision = validationDecisionsItr.next();
            setIdTiers.add(validationDecision.getIdTiers());

            RFValidationDecisionPojo validationDecisionPojo = new RFValidationDecisionPojo();
            validationDecisionPojo.setCsNationalite(validationDecision.getCsNationalite());
            validationDecisionPojo.setCsSexe(validationDecision.getCsSexe());
            validationDecisionPojo.setDateNaissance(validationDecision.getDateNaissance());
            validationDecisionPojo.setIdAdressePaiement(validationDecision.getIdAdressePaiement());
            validationDecisionPojo.setIdPrestation(validationDecision.getIdPrestation());
            validationDecisionPojo.setIdTiers(validationDecision.getIdTiers());
            validationDecisionPojo.setMontantPrestation(validationDecision.getMontantPrestation());
            validationDecisionPojo.setNom(validationDecision.getNom());
            validationDecisionPojo.setNss(validationDecision.getNss());
            validationDecisionPojo.setPrenom(validationDecision.getPrenom());

            // ajout du pojo dans la liste de pojos
            validationDecisionPojoList.add(validationDecisionPojo);
        }

        // renseigner la commune politique si nécessaire
        if (wantCommunePolitique) {
            Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(),
                    session);
            for (RFValidationDecisionPojo validationDecisionPojo : validationDecisionPojoList) {
                if (mapCommuneParIdTiers.containsKey(validationDecisionPojo.getIdTiers())) {
                    validationDecisionPojo.setCommunePolitique(mapCommuneParIdTiers.get(validationDecisionPojo
                            .getIdTiers()));
                    validationDecisionPojo.setAjouterCommunePolitique(true);
                }
            }

            // trier la liste de pojo
            Collections.sort(validationDecisionPojoList);
        }

        // re-parcours de toutes les décisions et chargement du détail
        for (RFValidationDecisionPojo validationDecisionPojo : validationDecisionPojoList) {
            if (!hasDecisionAValider) {
                hasDecisionAValider = Boolean.TRUE;
            }

            // chargement du détail
            BigDecimal montantPrestationCourante = RFXmlmlMappingLogListeControleValiderDecision.loadDetail(
                    xmlmlContainer, validationDecisionPojo, session, codesTypesSousTypesDeSoinsAcontroller);

            montantTotalPrestations = montantTotalPrestations.add(montantPrestationCourante);
            nbPrestations++;

            if (RFXmlmlMappingLogListeControleValiderDecision.isPrestationSas(validationDecisionPojo.getIdPrestation(),
                    validationDecisionPojo.getIdAdressePaiement(), idTiersFondationSas, session)) {
                montantTotalPrestationsAvancesSas = montantTotalPrestationsAvancesSas.add(montantPrestationCourante);
                nbPrestationsSas++;
            }
        }

        // Insertion du montant total
        xmlmlContainer.put(IRFListeControleValiderDecisionListeColumns.FOOTER_MONTANT_TOTAL, new FWCurrency(
                montantTotalPrestations.toString()).toStringFormat());

        // Insertion du montant des avances SAS
        xmlmlContainer.put(IRFListeControleValiderDecisionListeColumns.FOOTER_MONTANT_TOTAL_SAS, new FWCurrency(
                montantTotalPrestationsAvancesSas.toString()).toStringFormat());

        // Insertion du montant total - montant des avances SAS
        xmlmlContainer.put(IRFListeControleValiderDecisionListeColumns.FOOTER_MONTANT_TOTAL_MOINS_SAS, new FWCurrency(
                montantTotalPrestations.subtract(montantTotalPrestationsAvancesSas).toString()).toStringFormat());

        // Insertion du nombre de prestations total
        xmlmlContainer.put(IRFListeControleValiderDecisionListeColumns.FOOTER_NOMBRE_PRESTATION_TOTAL,
                nbPrestations.toString());

        // Insertion du nombre de prestations SAS total
        xmlmlContainer.put(IRFListeControleValiderDecisionListeColumns.FOOTER_NOMBRE_PRESTATION_SAS_TOTAL,
                nbPrestationsSas.toString());

        // Insertion du nombre de prestations total
        xmlmlContainer.put(IRFListeControleValiderDecisionListeColumns.FOOTER_NOMBRE_TOTAL_PRESTATION_MOINS_SAS,
                new Integer(nbPrestations - nbPrestationsSas).toString());

        // pas de décision à valider on ne remplit pas les colonnes
        if (!hasDecisionAValider) {
            RFXmlmlMappingLogListeControleValiderDecision.loadDetailVide(xmlmlContainer, session);
        }

        return xmlmlContainer;
    }

    private static String rechercherTypeSousTypeDeSoinsAControlerParPrestation(String idPrestation,
            String[] codesTypesSousTypesDeSoinsAcontroller, BSession session) throws Exception {

        StringBuffer typeDeSoinsAControlerParPrestationBfr = new StringBuffer();

        if (null != codesTypesSousTypesDeSoinsAcontroller) {

            Set<String> idSousTypeTesteSet = new HashSet<String>();

            RFOrdresVersementsManager ordresVersmements = new RFOrdresVersementsManager();
            ordresVersmements.setSession(session);
            ordresVersmements.setForIdPrestation(idPrestation);

            ordresVersmements.changeManagerSize(0);
            ordresVersmements.find();

            int iteration = 0;
            for (Iterator<RFOrdresVersements> it = ordresVersmements.iterator(); it.hasNext();) {
                RFOrdresVersements ordreDeVersement = it.next();

                if (ordreDeVersement != null
                        && ordreDeVersement.getTypeVersement().equals(
                                IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {

                    String[] codeTypeSousTypeOv = RFUtils.getCodesTypeDeSoin(ordreDeVersement.getIdSousTypeSoin(),
                            session);

                    for (String codeTypeSousTypeDeSoinAController : codesTypesSousTypesDeSoinsAcontroller) {

                        String[] codeTypeSousTypeAControllerTab = codeTypeSousTypeDeSoinAController
                                .split(RFXmlmlMappingLogListeControleValiderDecision.SEPARATEUR_TYPE_SOUS_TYPE);

                        if (codeTypeSousTypeAControllerTab[0].equals(codeTypeSousTypeOv[0])
                                && codeTypeSousTypeAControllerTab[1].equals(codeTypeSousTypeOv[1])) {

                            if (!idSousTypeTesteSet.contains(codeTypeSousTypeDeSoinAController)) {

                                if (iteration == 0) {
                                    typeDeSoinsAControlerParPrestationBfr.append(codeTypeSousTypeDeSoinAController);
                                } else {
                                    typeDeSoinsAControlerParPrestationBfr.append(","
                                            + codeTypeSousTypeDeSoinAController);
                                }
                                iteration++;
                                idSousTypeTesteSet.add(codeTypeSousTypeDeSoinAController);

                            }
                        }
                    }
                }
            }
        }

        return typeDeSoinsAControlerParPrestationBfr.toString();
    }
}
