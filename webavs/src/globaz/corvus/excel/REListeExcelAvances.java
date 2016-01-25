package globaz.corvus.excel;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.db.avances.REAvanceJointTiers;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.op.common.merge.IMergingContainer;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.utils.excel.ExcelAbstractDocumentGenerator;

/**
 * Classe générant la liste des avances RENTES, PC et RFM avec exceml
 * 
 * Convention de mapping pour le nommage des cellules excel
 * 2 premier caratères: onglet PC, RE, RF
 * 2 suivants, type de texte: LA pour label fixe, TH pour header de table, VA pour valeur de données
 * 2 suivants, type avances: ME, mensuel, UN unique ou GL global
 * Exemple: RELAGLTITRE, titre de la feuille Rentes
 * 
 * @author sce
 * 
 */
public class REListeExcelAvances extends ExcelAbstractDocumentGenerator {

    private List<REAvanceJointTiers> avancesMensuel = null;
    private List<REAvanceJointTiers> avancesUniques = null;
    private BSession session;
    private boolean ajouterCommunePolitique = false;

    public REListeExcelAvances(BSession session, String annee, List<REAvanceJointTiers> avancesUniques,
            List<REAvanceJointTiers> avancesMensuel, boolean ajouterCommunePolitique) {
        super();
        this.session = session;
        this.avancesUniques = avancesUniques;
        this.avancesMensuel = avancesMensuel;
        this.ajouterCommunePolitique = ajouterCommunePolitique;

        // this.session = session;
        // this.annee = annee;
        // this.familles = familles;
    }

    /**
     * Remplissage de ligne vide, et du total associé (unique ou mensuel)
     * 
     * @param data
     */
    private void fillLigneVide(CommonExcelmlContainer data, String excelFields[]) {
        data.put(excelFields[0], "");
        data.put(excelFields[1], "");
        data.put(excelFields[2], "");
        data.put(excelFields[3], "");
        data.put(excelFields[4], "");
        data.put(excelFields[5], "");
        data.put(excelFields[6], "0");
        data.put(excelFields[7], "0.00");
        if (ajouterCommunePolitique) {
            data.put(excelFields[8], "");
        }
    }

    private void fillTotalGeneral(CommonExcelmlContainer data, String fieldNbreValeur, String fieldMontan,
            ArrayList<Object> listeUnique, ArrayList<Object> listeMensuel) {
        // Calcule nbre valeurslisteU
        int nbreVal = (Integer) listeUnique.get(1) + (Integer) listeMensuel.get(1);
        BigDecimal montantTotal = new BigDecimal(0);
        montantTotal = montantTotal.add((BigDecimal) listeUnique.get(0));
        montantTotal = montantTotal.add((BigDecimal) listeMensuel.get(0));

        if (montantTotal.intValue() == 0) {
            data.put(fieldMontan, "0.00");
        } else {
            data.put(fieldMontan, montantTotal.toString());
        }
        data.put(fieldNbreValeur, "" + nbreVal);

    }

    /**
     * Remplissage des valeurs par bloc (paiement unique, paiement mensuel)
     * 
     * @param csDomaine
     * @param data
     * @param xlsFields
     * @param forPaiementUnique
     */
    private ArrayList<Object> fillValeurs(String csDomaine, CommonExcelmlContainer data, String xlsFields[],
            boolean forPaiementUnique) {

        // liste de groupe, paiement unique ou paiement mensuel
        List<REAvanceJointTiers> listeAvances = null;

        if (forPaiementUnique) {
            listeAvances = avancesUniques;
        } else {
            listeAvances = avancesMensuel;
        }

        // montant totaux pour le domaine
        BigDecimal montantTotal = new BigDecimal(0.00f);
        // nbres de valeurs
        int nbreValeurs = 0;

        // booleen test si valeur trouvée pour le domaine
        boolean valuesFind = false;

        for (REAvanceJointTiers avance : listeAvances) {

            if (avance.getAvance().getCsDomaineAvance().equals(csDomaine)) {

                if (ajouterCommunePolitique) {
                    data.put(xlsFields[8], avance.getCommunePolitique());
                }

                // acompte unique
                if (forPaiementUnique) {
                    data.put(xlsFields[0], avance.getNss());
                    data.put(xlsFields[1], avance.getNom() + " " + avance.getPrenom());
                    data.put(xlsFields[2], avance.getDateNaissance());
                    data.put(xlsFields[3], avance.getTypeDemande());
                    data.put(xlsFields[4], avance.getAvance().getDateDebutPmt1erAcompte());
                    data.put(xlsFields[5], avance.getAvance().getMontant1erAcompte());
                    // increment nbres valeurs et montant total
                    nbreValeurs++;
                    montantTotal = montantTotal.add(new BigDecimal(avance.getAvance().getMontant1erAcompte()));
                    valuesFind = true;
                } else {
                    // acomptes mensuel
                    data.put(xlsFields[0], avance.getNss());
                    data.put(xlsFields[1], avance.getNom() + " " + avance.getPrenom());
                    data.put(xlsFields[2], avance.getDateNaissance());
                    data.put(xlsFields[3], avance.getTypeDemande());
                    data.put(xlsFields[4], avance.getAvance().getDateDebutAcompte());
                    data.put(xlsFields[5], avance.getAvance().getMontantMensuel());
                    // increment nbres valeurs et montant total
                    nbreValeurs++;
                    montantTotal = montantTotal.add(new BigDecimal(avance.getAvance().getMontantMensuel()));
                    valuesFind = true;
                }
            }
        }

        // gestion des lignes si pas de valeur trouvées
        if (!valuesFind) {
            fillLigneVide(data, xlsFields);
        } else {
            // Total paiements uniques
            data.put(xlsFields[6], "" + nbreValeurs);
            data.put(xlsFields[7], montantTotal.toString());
        }

        ArrayList retour = new ArrayList();
        retour.add(montantTotal);
        retour.add(nbreValeurs);
        return retour;
    }

    @Override
    public String getModelPath() {
        // return "corvus/excelml/avances_listes.xml";

        if (ajouterCommunePolitique) {
            return "corvus/excelml/liste_avances_commune_politique.xml";
        }
        return "corvus/excelml/liste_avances.xml";
    }

    @Override
    public String getOutputName() {
        return "ListeDesAvances";
    }

    public BSession getSession() {
        return session;
    }

    private void initCommunePolitique() {

        Set<String> setIdTiers = new HashSet<String>();
        Map<String, String> mapIdTiersCommunePolitique = new HashMap<String, String>();
        for (REAvanceJointTiers avance : avancesUniques) {

            if (!JadeStringUtil.isBlankOrZero(avance.getAvance().getIdTiersBeneficiaire())) {
                setIdTiers.add(avance.getAvance().getIdTiersBeneficiaire());
            }

        }

        for (REAvanceJointTiers avance : avancesMensuel) {

            if (!JadeStringUtil.isBlankOrZero(avance.getAvance().getIdTiersBeneficiaire())) {
                setIdTiers.add(avance.getAvance().getIdTiersBeneficiaire());
            }

        }

        mapIdTiersCommunePolitique = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(), getSession());

        for (REAvanceJointTiers avance : avancesUniques) {

            String communePolitique = mapIdTiersCommunePolitique.get(avance.getAvance().getIdTiersBeneficiaire());
            if (!JadeStringUtil.isEmpty(communePolitique)) {
                avance.setCommunePolitique(communePolitique);
            }

        }

        for (REAvanceJointTiers avance : avancesMensuel) {

            String communePolitique = mapIdTiersCommunePolitique.get(avance.getAvance().getIdTiersBeneficiaire());
            if (!JadeStringUtil.isEmpty(communePolitique)) {
                avance.setCommunePolitique(communePolitique);
            }

        }

        Collections.sort(avancesUniques);
        Collections.sort(avancesMensuel);

    }

    @Override
    public IMergingContainer loadData() throws Exception {

        if (ajouterCommunePolitique) {
            initCommunePolitique();
        }

        /** Container excel */
        CommonExcelmlContainer data = new CommonExcelmlContainer();

        /** Remplissage global */
        remplirValeursGlobale(data);
        remplirValeursRFM(data);
        remplirValeursPC(data);
        remplirValeursRentes(data);

        // TODO Auto-generated method stub
        return data;
    }

    /**
     * Remplissage des valeurs globales
     * 
     * @param data
     */
    private void remplirValeursGlobale(CommonExcelmlContainer data) {
        // Titre caisse par onglet
        // data.put("RELAGL_CAISSE", this.session.getLabel("EXCEL_AVANCES_TITRE_CAISSE"));
        // data.put("PCLAGL_CAISSE", this.session.getLabel("EXCEL_AVANCES_TITRE_CAISSE"));
        // data.put("RFLAGL_CAISSE", this.session.getLabel("EXCEL_AVANCES_TITRE_CAISSE"));

        // Titre onglet
        data.put("RELAGL_TITRE", session.getLabel("EXCEL_AVANCES_TITRE_RENTES"));
        data.put("PCLAGL_TITRE", session.getLabel("EXCEL_AVANCES_TITRE_PC"));
        data.put("RFLAGL_TITRE", session.getLabel("EXCEL_AVANCES_TITRE_RFM"));

        if (ajouterCommunePolitique) {
            data.put("RELAGL_UTILISATEUR",
                    session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()));
            data.put("PCLAGL_UTILISATEUR",
                    session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()));
            data.put("RFLAGL_UTILISATEUR",
                    session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()));

            data.put("REVAGL_UTILISATEUR", getSession().getUserId());
            data.put("PCVAGL_UTILISATEUR", getSession().getUserId());
            data.put("RFVAGL_UTILISATEUR", getSession().getUserId());

        }

        // Header tableau
        if (ajouterCommunePolitique) {
            data.put("RETHGL_CP", session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()));
        }

        data.put("RETHGL_NSS", session.getLabel("EXCEL_AVANCES_HEADER_TAB_NSS"));
        data.put("RETHGL_NOM", session.getLabel("EXCEL_AVANCES_HEADER_TAB_NOM_PRENOM"));
        data.put("RETHGL_NAISS", session.getLabel("EXCEL_AVANCES_HEADER_TAB_DATE_NAISS"));
        data.put("RETHGL_TDEMANDE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_TYPE_DEMANDE"));
        data.put("RETHGL_VALEUR_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_VALEUR"));
        data.put("RETHGL_MONTANT_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_UNIQUE"));
        data.put("RETHGL_VALEUR_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_VALEUR"));
        data.put("RETHGL_MONTANT_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_MENSUEL"));

        if (ajouterCommunePolitique) {
            data.put("PCTHGL_CP", session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()));
        }
        data.put("PCTHGL_NSS", session.getLabel("EXCEL_AVANCES_HEADER_TAB_NSS"));
        data.put("PCTHGL_NOM", session.getLabel("EXCEL_AVANCES_HEADER_TAB_NOM_PRENOM"));
        data.put("PCTHGL_NAISS", session.getLabel("EXCEL_AVANCES_HEADER_TAB_DATE_NAISS"));
        data.put("PCTHGL_TDEMANDE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_TYPE_DEMANDE"));
        data.put("PCTHGL_VALEUR_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_VALEUR"));
        data.put("PCTHGL_MONTANT_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_UNIQUE"));
        data.put("PCTHGL_VALEUR_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_VALEUR"));
        data.put("PCTHGL_MONTANT_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_MENSUEL"));

        if (ajouterCommunePolitique) {
            data.put("RFTHGL_CP", session.getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()));
        }
        data.put("RFTHGL_NSS", session.getLabel("EXCEL_AVANCES_HEADER_TAB_NSS"));
        data.put("RFTHGL_NOM", session.getLabel("EXCEL_AVANCES_HEADER_TAB_NOM_PRENOM"));
        data.put("RFTHGL_NAISS", session.getLabel("EXCEL_AVANCES_HEADER_TAB_DATE_NAISS"));
        data.put("RFTHGL_TDEMANDE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_TYPE_DEMANDE"));
        data.put("RFTHGL_VALEUR_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_VALEUR"));
        data.put("RFTHGL_MONTANT_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_UNIQUE"));
        data.put("RFTHGL_VALEUR_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_VALEUR"));
        data.put("RFTHGL_MONTANT_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_MENSUEL"));

        // Titre zones
        data.put("RELAGL_ACOMPTE_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_UNIQUE"));
        data.put("RELAGL_ACOMPTE_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_MENSUEL"));

        data.put("PCLAGL_ACOMPTE_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_UNIQUE"));
        data.put("PCLAGL_ACOMPTE_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_MENSUEL"));

        data.put("RFLAGL_ACOMPTE_UNIQUE", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_UNIQUE"));
        data.put("RFLAGL_ACOMPTE_MENSUEL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_A_MENSUEL"));

        // Total général
        data.put("RELAGL_GENERAL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_TOTAL_GENERAL"));
        data.put("PCLAGL_GENERAL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_TOTAL_GENERAL"));
        data.put("RFLAGL_GENERAL", session.getLabel("EXCEL_AVANCES_HEADER_TAB_TOTAL_GENERAL"));

        // Total général
        // data.put("LABEL_RENTES_TOTAL_GENERAL", this.session.getLabel("EXCEL_AVANCES_HEADER_TAB_TOTAL_GENERAL"));

    }

    private void remplirValeursPC(CommonExcelmlContainer data) {

        String[] uniqueXLSFields = new String[] { "PCVAUN_NSS", "PCVAUN_NOM", "PCVAUN_NAISS", "PCVAUN_TDEMANDE",
                "PCVAUN_VALEUR", "PCVAUN_MONTANT", "PCVAUN_TOTAL_VALEUR", "PCVAUN_TOTAL_MONTANT", "PCVAUN_CP" };
        String[] mensuelXLSFields = new String[] { "PCVAAM_NSS", "PCVAAM_NOM", "PCVAAM_NAISS", "PCVAAM_TDEMANDE",
                "PCVAAM_VALEUR", "PCVAAM_MONTANT", "PCVAAM_TOTAL_VALEUR", "PCVAAM_TOTAL_MONTANT", "PCVAAM_CP" };
        // *************** paiement unique
        ArrayList<Object> listeResultatUnique = fillValeurs(IREAvances.CS_DOMAINE_AVANCE_PC, data, uniqueXLSFields,
                true);
        // *************** paiement mensuels
        ArrayList<Object> listeResultatMensuel = fillValeurs(IREAvances.CS_DOMAINE_AVANCE_PC, data, mensuelXLSFields,
                false);
        // *************** Total general
        fillTotalGeneral(data, "PCVAAM_TOTAL_AVANCES", "PCVAAM_TOTAL_MONTAN", listeResultatUnique, listeResultatMensuel);
    }

    private void remplirValeursRentes(CommonExcelmlContainer data) {
        String[] uniqueXLSFields = new String[] { "REVAUN_NSS", "REVAUN_NOM", "REVAUN_NAISS", "REVAUN_TDEMANDE",
                "REVAUN_VALEUR", "REVAUN_MONTANT", "REVAUN_TOTAL_VALEUR", "REVAUN_TOTAL_MONTANT", "REVAUN_CP" };
        String[] mensuelXLSFields = new String[] { "REVAAM_NSS", "REVAAM_NOM", "REVAAM_NAISS", "REVAAM_TDEMANDE",
                "REVAAM_VALEUR", "REVAAM_MONTANT", "REVAAM_TOTAL_VALEUR", "REVAAM_TOTAL_MONTANT", "REVAAM_CP" };
        // *************** paiement unique
        ArrayList<Object> listeValeurUnique = fillValeurs(IREAvances.CS_DOMAINE_AVANCE_RENTE, data, uniqueXLSFields,
                true);
        // *************** paiement mensuels
        ArrayList<Object> listeValeurMensuel = fillValeurs(IREAvances.CS_DOMAINE_AVANCE_RENTE, data, mensuelXLSFields,
                false);

        fillTotalGeneral(data, "REVAAM_TOTAL_AVANCES", "REVAAM_TOTAL_MONTAN", listeValeurUnique, listeValeurMensuel);
        // // Acomptes uniques
        // data.put("REVAUN_NSS", "756.1234.34.34");
        // data.put("REVAUN_NOM", "Deourd Daladier");
        // data.put("REVAUN_NAISS", "21.12.2013");
        // data.put("REVAUN_TDEMANDE", "Invalidité");
        // data.put("REVAUN_VALEUR", "10.12.2011");
        // data.put("REVAUN_MONTANT", "1234.54");
        //
        // data.put("REVAUN_NSS", "756.1212.34.34");
        // data.put("REVAUN_NOM", "Deourd Duladier");
        // data.put("REVAUN_NAISS", "21.12.2213");
        // data.put("REVAUN_TDEMANDE", "Invalidité");
        // data.put("REVAUN_VALEUR", "10.12.2011");
        // data.put("REVAUN_MONTANT", "1234.54");
        //
        // // Total
        // data.put("REVAUN_TOTAL_VALEUR", "2");
        // data.put("REVAUN_TOTAL_MONTANT", "1239.08");
        //
        // // Acomptes mensuels
        // data.put("REVAAM_NSS", "756.1234.34.34");
        // data.put("REVAAM_NOM", "Deourd Daladier");
        // data.put("REVAAM_NAISS", "21.12.2013");
        // data.put("REVAAM_TDEMANDE", "Invalidité");
        // data.put("REVAAM_VALEUR", "10.12.2011");
        // data.put("REVAAM_MONTANT", "234.54");
        //
        // data.put("REVAAM_NSS", "756.9999.34.34");
        // data.put("REVAAM_NOM", "Deourd Dulasaadier");
        // data.put("REVAAM_NAISS", "21.12.2213");
        // data.put("REVAAM_TDEMANDE", "Invalidité");
        // data.put("REVAAM_VALEUR", "10.02.2011");
        // data.put("REVAAM_MONTANT", "124.54");
        //
        // // Total
        // data.put("REVAAM_TOTAL_VALEUR", "9");
        // data.put("REVAAM_TOTAL_MONTANT", "999.08");

        // general

    }

    private void remplirValeursRFM(CommonExcelmlContainer data) {
        String[] uniqueXLSFields = new String[] { "RFVAUN_NSS", "RFVAUN_NOM", "RFVAUN_NAISS", "RFVAUN_TDEMANDE",
                "RFVAUN_VALEUR", "RFVAUN_MONTANT", "RFVAUN_TOTAL_VALEUR", "RFVAUN_TOTAL_MONTANT", "RFVAUN_CP" };
        String[] mensuelXLSFields = new String[] { "RFVAAM_NSS", "RFVAAM_NOM", "RFVAAM_NAISS", "RFVAAM_TDEMANDE",
                "RFVAAM_VALEUR", "RFVAAM_MONTANT", "RFVAAM_TOTAL_VALEUR", "RFVAAM_TOTAL_MONTANT", "RFVAAM_CP" };
        // *************** paiement unique
        ArrayList<Object> listeUnique = fillValeurs(IREAvances.CS_DOMAINE_AVANCE_RFM, data, uniqueXLSFields, true);
        // *************** paiement mensuels
        ArrayList<Object> listeMensuel = fillValeurs(IREAvances.CS_DOMAINE_AVANCE_RFM, data, mensuelXLSFields, false);

        fillTotalGeneral(data, "RFVAAM_TOTAL_AVANCES", "RFVAAM_TOTAL_MONTAN", listeUnique, listeMensuel);
    }
    /*
     * private void remplirValeursPC(CommonExcelmlContainer data) {
     * // montant total
     * BigDecimal montantTotalUnique = new BigDecimal(0);
     * BigDecimal montantTotalMensuel = new BigDecimal(0);
     * // nbres de valeurs
     * int nbreValeursUnique = 0;
     * int nbreValeursMensuel = 0;
     * 
     * // Acomptes uniques, itération sur les avances uniques, type pc uniquement
     * for (REAvanceJointTiers avance : this.avancesUniques) {
     * // type pc
     * if (avance.getAvance().getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
     * data.put("PCVAUN_NSS", avance.getNss());
     * data.put("PCVAUN_NOM", avance.getNom() + " " + avance.getPrenom());
     * data.put("PCVAUN_NAISS", avance.getDateNaissance());
     * data.put("PCVAUN_TDEMANDE", avance.getTypeDemande());
     * data.put("PCVAUN_VALEUR", avance.getAvance().getDateDebutPmt1erAcompte());
     * data.put("PCVAUN_MONTANT", avance.getAvance().getMontant1erAcompte());
     * nbreValeursUnique++;
     * montantTotalUnique = montantTotalUnique.add(new BigDecimal(avance.getAvance().getMontant1erAcompte()));
     * }
     * }
     * // Total
     * data.put("PCVAUN_TOTAL_VALEUR", "" + nbreValeursUnique);
     * data.put("PCVAUN_TOTAL_MONTANT", montantTotalUnique.toString());
     * 
     * // Acomptes mensuels
     * for (REAvanceJointTiers avance : this.avancesMensuel) {
     * // type pc
     * if (avance.getAvance().getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
     * data.put("PCVAAM_NSS", avance.getNss());
     * data.put("PCVAAM_NOM", avance.getNom() + " " + avance.getPrenom());
     * data.put("PCVAAM_NAISS", avance.getDateNaissance());
     * data.put("PCVAAM_TDEMANDE", avance.getTypeDemande());
     * data.put("PCVAAM_VALEUR", avance.getAvance().getDateDebutAcompte());
     * data.put("PCVAAM_MONTANT", avance.getAvance().getMontantMensuel());
     * nbreValeursMensuel++;
     * montantTotalMensuel = montantTotalMensuel.add(new BigDecimal(avance.getAvance().getMontantMensuel()));
     * }
     * 
     * }
     * // Total
     * data.put("PCVAAM_TOTAL_VALEUR", "" + nbreValeursMensuel);
     * data.put("PCVAAM_TOTAL_MONTANT", montantTotalMensuel.toString());
     * }
     * 
     * private void remplirValeursRentes(CommonExcelmlContainer data) {
     * // montant total
     * BigDecimal montantTotalUnique = new BigDecimal(0);
     * BigDecimal montantTotalMensuel = new BigDecimal(0);
     * // nbres de valeurs
     * int nbreValeursUnique = 0;
     * int nbreValeursMensuel = 0;
     * 
     * // Acomptes uniques, itération sur les avances uniques, type rentes uniquement
     * for (REAvanceJointTiers avance : this.avancesUniques) {
     * // type pc
     * if (avance.getAvance().getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_RENTE)) {
     * data.put("REVAUN_NSS", avance.getNss());
     * data.put("REVAUN_NOM", avance.getNom() + " " + avance.getPrenom());
     * data.put("REVAUN_NAISS", avance.getDateNaissance());
     * data.put("REVAUN_TDEMANDE", avance.getTypeDemande());
     * data.put("REVAUN_VALEUR", avance.getAvance().getDateDebutPmt1erAcompte());
     * data.put("REVAUN_MONTANT", avance.getAvance().getMontant1erAcompte());
     * nbreValeursUnique++;
     * montantTotalUnique = montantTotalUnique.add(new BigDecimal(avance.getAvance().getMontant1erAcompte()));
     * }
     * }
     * // Total
     * data.put("REVAUN_TOTAL_VALEUR", "" + nbreValeursUnique);
     * data.put("REVAUN_TOTAL_MONTANT", montantTotalUnique.toString());
     * 
     * // Acomptes mensuels
     * for (REAvanceJointTiers avance : this.avancesMensuel) {
     * // type pc
     * if (avance.getAvance().getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_RENTE)) {
     * data.put("REVAAM_NSS", avance.getNss());
     * data.put("REVAAM_NOM", avance.getNom() + " " + avance.getPrenom());
     * data.put("REVAAM_NAISS", avance.getDateNaissance());
     * data.put("REVAAM_TDEMANDE", avance.getTypeDemande());
     * data.put("REVAAM_VALEUR", avance.getAvance().getDateDebutAcompte());
     * data.put("REVAAM_MONTANT", avance.getAvance().getMontantMensuel());
     * nbreValeursMensuel++;
     * montantTotalMensuel = montantTotalMensuel.add(new BigDecimal(avance.getAvance().getMontantMensuel()));
     * }
     * 
     * }
     * // Total
     * data.put("REVAAM_TOTAL_VALEUR", "" + nbreValeursMensuel);
     * data.put("REVAAM_TOTAL_MONTANT", montantTotalMensuel.toString());
     * 
     * }
     * 
     * private void remplirValeursRFM(CommonExcelmlContainer data) {
     * // montant total
     * BigDecimal montantTotalUnique = new BigDecimal(0);
     * BigDecimal montantTotalMensuel = new BigDecimal(0);
     * // nbres de valeurs
     * int nbreValeursUnique = 0;
     * int nbreValeursMensuel = 0;
     * 
     * // Acomptes uniques, itération sur les avances uniques, type rentes uniquement
     * for (REAvanceJointTiers avance : this.avancesUniques) {
     * // type pc
     * if (avance.getAvance().getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_RFM)) {
     * data.put("RFVAUN_NSS", avance.getNss());
     * data.put("RFVAUN_NOM", avance.getNom() + " " + avance.getPrenom());
     * data.put("RFVAUN_NAISS", avance.getDateNaissance());
     * data.put("RFVAUN_TDEMANDE", avance.getTypeDemande());
     * data.put("RFVAUN_VALEUR", avance.getAvance().getDateDebutPmt1erAcompte());
     * data.put("RFVAUN_MONTANT", avance.getAvance().getMontant1erAcompte());
     * nbreValeursUnique++;
     * montantTotalUnique = montantTotalUnique.add(new BigDecimal(avance.getAvance().getMontant1erAcompte()));
     * }
     * }
     * // Total
     * data.put("RFVAUN_TOTAL_VALEUR", "" + nbreValeursUnique);
     * data.put("RFVAUN_TOTAL_MONTANT", montantTotalUnique.toString());
     * 
     * // Acomptes mensuels
     * for (REAvanceJointTiers avance : this.avancesMensuel) {
     * // type pc
     * if (avance.getAvance().getCsDomaineAvance().equals(IREAvances.CS_DOMAINE_AVANCE_RFM)) {
     * data.put("RFVAAM_NSS", avance.getNss());
     * data.put("RFVAAM_NOM", avance.getNom() + " " + avance.getPrenom());
     * data.put("RFVAAM_NAISS", avance.getDateNaissance());
     * data.put("RFVAAM_TDEMANDE", avance.getTypeDemande());
     * data.put("RFVAAM_VALEUR", avance.getAvance().getDateDebutAcompte());
     * data.put("RFVAAM_MONTANT", avance.getAvance().getMontantMensuel());
     * nbreValeursMensuel++;
     * montantTotalMensuel = montantTotalMensuel.add(new BigDecimal(avance.getAvance().getMontantMensuel()));
     * }
     * 
     * }
     * // Total
     * data.put("RFVAAM_TOTAL_VALEUR", "" + nbreValeursMensuel);
     * data.put("RFVAAM_TOTAL_MONTANT", montantTotalMensuel.toString());// Acomptes uniques
     * }
     */

}
