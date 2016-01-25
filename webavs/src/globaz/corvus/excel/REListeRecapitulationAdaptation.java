package globaz.corvus.excel;

import globaz.corvus.api.adaptation.IREAdaptationRente;
import globaz.corvus.db.adaptation.REPmtFictif;
import globaz.corvus.db.adaptation.REPmtFictifManager;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiersManager;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * @author HPE
 */
public class REListeRecapitulationAdaptation extends REAbstractListExcel {

    private String eMailAddress = "";
    private String moisAnnee = "";

    public REListeRecapitulationAdaptation(BSession session) {
        super(session, "REListeRecapitulationAdaptation", session.getLabel("PROCESS_LISTE_RECAP_ADA_OBJET_MAIL"));
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    private void remplirEnTeteTableau() {

        // Titres des colonnes
        ArrayList<String> colTitles = new ArrayList<String>();
        colTitles.add("");
        colTitles.add(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_AVS_ORD"));
        colTitles.add(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_AVS_EXT"));
        colTitles.add(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_API_AVS"));
        colTitles.add(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_AI_ORD"));
        colTitles.add(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_AI_EXT"));
        colTitles.add(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_API_AI"));
        colTitles.add(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TOTAUX"));

        createSheet(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_REC_AD") + " " + getMoisAnnee());

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter();

        // définition de la taille des cellules
        int numCol = 0;

        currentSheet.setColumnWidth((short) numCol++, (short) 11000); // Description
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // AVS Ordinaire
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // AVS Extraordinaire
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // API AVS
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // AI Ordinaire
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // AI Extraordinaire
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // API AI
        currentSheet.setColumnWidth((short) numCol++, (short) 3500); // Totaux

    }

    /**
     * Retourne l'id de la récap et vide si pas trouvé
     * 
     * @param vdate
     * @param session
     * @return
     */
    private String loadRecapMensuelle(String vdate, BSession session) {
        RERecapMensuelle recap = new RERecapMensuelle();
        recap.setSession(session);
        recap.setAlternateKey(RERecapMensuelle.DATE_RAPPORT_KEY);
        recap.setDateRapportMensuel(vdate);
        try {
            recap.retrieve();
            if (recap.isNew()) {
                return "";
            } else {
                return recap.getIdRecapMensuelle();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Rempli le 1er tableau
     * 
     * @throws Exception
     */
    private void remplirPremierePartie() throws Exception {

        // Données du paiement fictif de janvier avant adaptation
        REPmtFictif paiementFictifAvantAdaptation = chargerPaiementFictif(getMoisAnnee(),
                IREAdaptationRente.CS_TYPE_PAIEMENT_FICTIF, getSession().getLabel("PROCESS_LISTE_RECAP_ADA_ERR_1")
                        + " " + getMoisAnnee());

        // Données du paiement fictif de janvier après adaptation
        REPmtFictif paiementFictifApresAdaptation = chargerPaiementFictif(getMoisAnnee(),
                IREAdaptationRente.CS_TYPE_RECAP_AUGMENTATION, getSession().getLabel("PROCESS_LISTE_RECAP_ADA_ERR_3")
                        + " " + getMoisAnnee());

        FWCurrency montantAVSOrdinairePaiementFictif = new FWCurrency(
                paiementFictifAvantAdaptation.getMontantAVSOrdinaires());
        FWCurrency montantAVSExtraordinairePaiementFictif = new FWCurrency(
                paiementFictifAvantAdaptation.getMontantAVSExtraordinaires());
        FWCurrency montantAPIAVSPaiementFictif = new FWCurrency(paiementFictifAvantAdaptation.getMontantAPIAVS());
        FWCurrency montantAIOrdinairePaiementFictif = new FWCurrency(
                paiementFictifAvantAdaptation.getMontantAIOrdinaires());
        FWCurrency montantAIExtraordinairePaiementFictif = new FWCurrency(
                paiementFictifAvantAdaptation.getMontantAIExtraordinaires());
        FWCurrency montantAPIAIPaiementFictif = new FWCurrency(paiementFictifAvantAdaptation.getMontantAPIAI());
        FWCurrency totalPaiementFictif = new FWCurrency(paiementFictifAvantAdaptation.getMontantTotalGeneral());

        // création de la ligne 3 : Rentes nettes avant augmentation
        // les données proviennent du paiement fictif fait avant l'adaptation
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_1"), getStyleListLeft());
        // colonne B : AVS ordinaire
        this.createCell(montantAVSOrdinairePaiementFictif.doubleValue(), false);
        // colonne C : AVS extraordinaire
        this.createCell(montantAVSExtraordinairePaiementFictif.doubleValue(), false);
        // colonne D : API AVS
        this.createCell(montantAPIAVSPaiementFictif.doubleValue(), false);
        // colonne E : AI ordinaire
        this.createCell(montantAIOrdinairePaiementFictif.doubleValue(), false);
        // colonne F : AI extraordinaire
        this.createCell(montantAIExtraordinairePaiementFictif.doubleValue(), false);
        // colonne G : API AI
        this.createCell(montantAPIAIPaiementFictif.doubleValue(), false);
        // colonne H : total de la ligne
        this.createCell(totalPaiementFictif.doubleValue(), false);

        FWCurrency AVSOrd3 = new FWCurrency(paiementFictifApresAdaptation.getMontantAVSOrdinaires());
        FWCurrency AVSExt3 = new FWCurrency(paiementFictifApresAdaptation.getMontantAVSExtraordinaires());
        FWCurrency APIAVS3 = new FWCurrency(paiementFictifApresAdaptation.getMontantAPIAVS());
        FWCurrency AIOrd3 = new FWCurrency(paiementFictifApresAdaptation.getMontantAIOrdinaires());
        FWCurrency AIExt3 = new FWCurrency(paiementFictifApresAdaptation.getMontantAIExtraordinaires());
        FWCurrency APIAI3 = new FWCurrency(paiementFictifApresAdaptation.getMontantAPIAI());
        FWCurrency totaux3 = new FWCurrency(paiementFictifApresAdaptation.getMontantTotalGeneral());

        AVSOrd3.sub(montantAVSOrdinairePaiementFictif);
        AVSExt3.sub(montantAVSExtraordinairePaiementFictif);
        APIAVS3.sub(montantAPIAVSPaiementFictif);
        AIOrd3.sub(montantAIOrdinairePaiementFictif);
        AIExt3.sub(montantAIExtraordinairePaiementFictif);
        APIAI3.sub(montantAPIAIPaiementFictif);
        totaux3.sub(totalPaiementFictif);

        // création de la ligne 4 : Adaptation (1)
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_3"), getStyleListLeft());
        // colonne B : AVS ordinaire
        createCellFormula("B23", getStyleMontant());
        // colonne C : AVS extraordinaire
        createCellFormula("C23", getStyleMontant());
        // colonne D : API AVS
        createCellFormula("D23", getStyleMontant());
        // colonne E : AI ordinaire
        createCellFormula("E23", getStyleMontant());
        // colonne F : AI extraordinaire
        createCellFormula("F23", getStyleMontant());
        // colonne G : API AI
        createCellFormula("G23", getStyleMontant());
        // colonne H : total de la ligne
        createCellFormula("H23", getStyleMontant());

        // création de la ligne 5 : Rentes nettes après augmentation
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_1A"), getStyleListTitleLeft());
        // colonne B : AVS ordinaire
        createCellFormula("B3+B4", getStyleMontantTotal());
        // colonne C : AVS extraordinaire
        createCellFormula("C3+C4", getStyleMontantTotal());
        // colonne D : API AVS
        createCellFormula("D3+D4", getStyleMontantTotal());
        // colonne E : AI ordinaire
        createCellFormula("E3+E4", getStyleMontantTotal());
        // colonne F : AI extraordinaire
        createCellFormula("F3+F4", getStyleMontantTotal());
        // colonne G : API AI
        createCellFormula("G3+G4", getStyleMontantTotal());
        // colonne H : total de la ligne
        createCellFormula("H3+H4", getStyleMontantTotal());

    }

    /**
     * Rempli le deuxième tableau ainsi que le solde net en dessous
     * 
     * @throws Exception
     */
    private void remplirDeuxiemePartie() throws Exception {

        JACalendar cal = new JACalendarGregorian();
        String moisAnneePrecedent = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(cal.addMonths(getMoisAnnee(), -1));
        // récap de décembre (mois précédant)
        REDetailRecapMensuelleViewBean recapDecembre = chargerRecapMensuelle(moisAnneePrecedent,
                getSession().getLabel("PROCESS_LISTE_RECAP_ADA_ERR_2") + " " + moisAnneePrecedent);

        // récap de janvier
        REDetailRecapMensuelleViewBean recapJanvier = chargerRecapMensuelle(getMoisAnnee(),
                getSession().getLabel("PROCESS_LISTE_RECAP_ADA_ERR_2") + " " + getMoisAnnee());

        // extraction des totaux de la récap de décembre
        FWCurrency montantAVSOrdinaireRecapDecembre = new FWCurrency();
        montantAVSOrdinaireRecapDecembre.add(recapDecembre.getElem500001().getMontant());
        montantAVSOrdinaireRecapDecembre.add(recapDecembre.getElem500002().getMontant());
        montantAVSOrdinaireRecapDecembre.sub(recapDecembre.getElem500004().getMontant());

        FWCurrency montantAVSExtraordinaireRecapDecembre = new FWCurrency();
        montantAVSExtraordinaireRecapDecembre.add(recapDecembre.getElem501001().getMontant());
        montantAVSExtraordinaireRecapDecembre.add(recapDecembre.getElem501002().getMontant());
        montantAVSExtraordinaireRecapDecembre.sub(recapDecembre.getElem501004().getMontant());

        FWCurrency montantAPIAVSRecapDecembre = new FWCurrency();
        montantAPIAVSRecapDecembre.add(recapDecembre.getElem503001().getMontant());
        montantAPIAVSRecapDecembre.add(recapDecembre.getElem503002().getMontant());
        montantAPIAVSRecapDecembre.sub(recapDecembre.getElem503004().getMontant());

        FWCurrency montantAIOrdinaireRecapDecembre = new FWCurrency();
        montantAIOrdinaireRecapDecembre.add(recapDecembre.getElem510001().getMontant());
        montantAIOrdinaireRecapDecembre.add(recapDecembre.getElem510002().getMontant());
        montantAIOrdinaireRecapDecembre.sub(recapDecembre.getElem510004().getMontant());

        FWCurrency montantAIExtraordinaireRecapDecembre = new FWCurrency();
        montantAIExtraordinaireRecapDecembre.add(recapDecembre.getElem511001().getMontant());
        montantAIExtraordinaireRecapDecembre.add(recapDecembre.getElem511002().getMontant());
        montantAIExtraordinaireRecapDecembre.sub(recapDecembre.getElem511004().getMontant());

        FWCurrency montantAPIAIRecapDecembre = new FWCurrency();
        montantAPIAIRecapDecembre.add(recapDecembre.getElem513001().getMontant());
        montantAPIAIRecapDecembre.add(recapDecembre.getElem513002().getMontant());
        montantAPIAIRecapDecembre.sub(recapDecembre.getElem513004().getMontant());

        FWCurrency totalRecapDecembre = new FWCurrency();
        totalRecapDecembre.add(montantAVSOrdinaireRecapDecembre);
        totalRecapDecembre.add(montantAVSExtraordinaireRecapDecembre);
        totalRecapDecembre.add(montantAPIAVSRecapDecembre);
        totalRecapDecembre.add(montantAIOrdinaireRecapDecembre);
        totalRecapDecembre.add(montantAIExtraordinaireRecapDecembre);
        totalRecapDecembre.add(montantAPIAIRecapDecembre);

        // création de la ligne 7 : Données de la récapitulation du mois en cours
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_2"), getStyleListLeft());
        // colonne B : AVS ordinaire
        this.createCell(montantAVSOrdinaireRecapDecembre.doubleValue(), false);
        // colonne C : AVS extraordinaire
        this.createCell(montantAVSExtraordinaireRecapDecembre.doubleValue(), false);
        // colonne D : API AVS
        this.createCell(montantAPIAVSRecapDecembre.doubleValue(), false);
        // colonne E : AI ordinaire
        this.createCell(montantAIOrdinaireRecapDecembre.doubleValue(), false);
        // colonne F : AI extraordinaire
        this.createCell(montantAIExtraordinaireRecapDecembre.doubleValue(), false);
        // colonne G : API AI
        this.createCell(montantAPIAIRecapDecembre.doubleValue(), false);
        // colonne H : total de la ligne
        this.createCell(totalRecapDecembre.doubleValue(), false);

        // création de la ligne 8 : Adaptation (1)
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_3"), getStyleListLeft());
        // colonne B : AVS ordinaire
        createCellFormula("B23", getStyleMontant());
        // colonne C : AVS extraordinaire
        createCellFormula("C23", getStyleMontant());
        // colonne D : API AVS
        createCellFormula("D23", getStyleMontant());
        // colonne E : AI ordinaire
        createCellFormula("E23", getStyleMontant());
        // colonne F : AI extraordinaire
        createCellFormula("F23", getStyleMontant());
        // colonne G : API AI
        createCellFormula("G23", getStyleMontant());
        // colonne H : total de la ligne
        createCellFormula("H23", getStyleMontant());

        FWCurrency montantDiminutionAVSOrdinaireRecapJanvier = new FWCurrency(recapJanvier.getElem500004().getMontant());
        FWCurrency montantDiminutionAVSExtraordinaireRecapJanvier = new FWCurrency(recapJanvier.getElem501004()
                .getMontant());
        FWCurrency montantDiminutionAPIAVSRecapJanvier = new FWCurrency(recapJanvier.getElem503004().getMontant());
        FWCurrency montantDiminutionAIOrdinaireRecapJanvier = new FWCurrency(recapJanvier.getElem510004().getMontant());
        FWCurrency montantDiminutionAIExtraordinaireRecapJanvier = new FWCurrency(recapJanvier.getElem511004()
                .getMontant());
        FWCurrency montantDiminutionAPIAIRecapJanvier = new FWCurrency(recapJanvier.getElem513004().getMontant());

        FWCurrency totalDiminutionRecapJanvier = new FWCurrency();
        totalDiminutionRecapJanvier.add(montantDiminutionAVSOrdinaireRecapJanvier);
        totalDiminutionRecapJanvier.add(montantDiminutionAVSExtraordinaireRecapJanvier);
        totalDiminutionRecapJanvier.add(montantDiminutionAPIAVSRecapJanvier);
        totalDiminutionRecapJanvier.add(montantDiminutionAIOrdinaireRecapJanvier);
        totalDiminutionRecapJanvier.add(montantDiminutionAIExtraordinaireRecapJanvier);
        totalDiminutionRecapJanvier.add(montantDiminutionAPIAIRecapJanvier);

        FWCurrency montantAugmentationAVSOrdinaireRecapJanvier = new FWCurrency(recapJanvier.getElem500002()
                .getMontant());
        FWCurrency montantAugmentationAVSExtraordinaireRecapJanvier = new FWCurrency(recapJanvier.getElem501002()
                .getMontant());
        FWCurrency montantAugmentationAPIAVSRecapJanvier = new FWCurrency(recapJanvier.getElem503002().getMontant());
        FWCurrency montantAugmentationAIOrdinaireRecapJanvier = new FWCurrency(recapJanvier.getElem510002()
                .getMontant());
        FWCurrency montantAugmentationAIExtraordinaireRecapJanvier = new FWCurrency(recapJanvier.getElem511002()
                .getMontant());
        FWCurrency montantAugmentationAPIAIRecapJanvier = new FWCurrency(recapJanvier.getElem513002().getMontant());

        FWCurrency totalAugmentationRecapJanvier = new FWCurrency();
        totalAugmentationRecapJanvier.add(montantAugmentationAVSOrdinaireRecapJanvier);
        totalAugmentationRecapJanvier.add(montantAugmentationAVSExtraordinaireRecapJanvier);
        totalAugmentationRecapJanvier.add(montantAugmentationAPIAVSRecapJanvier);
        totalAugmentationRecapJanvier.add(montantAugmentationAIOrdinaireRecapJanvier);
        totalAugmentationRecapJanvier.add(montantAugmentationAIExtraordinaireRecapJanvier);
        totalAugmentationRecapJanvier.add(montantAugmentationAPIAIRecapJanvier);

        // création de la ligne 9 : Diminution mois suivant
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_4"), getStyleListLeft());
        // colonne B : AVS ordinaire
        this.createCell(montantDiminutionAVSOrdinaireRecapJanvier.doubleValue(), false);
        // colonne C : AVS extraordinaire
        this.createCell(montantDiminutionAVSExtraordinaireRecapJanvier.doubleValue(), false);
        // colonne D : API AVS
        this.createCell(montantDiminutionAPIAVSRecapJanvier.doubleValue(), false);
        // colonne E : AI ordinaire
        this.createCell(montantDiminutionAIOrdinaireRecapJanvier.doubleValue(), false);
        // colonne F : AI extraordinaire
        this.createCell(montantDiminutionAIExtraordinaireRecapJanvier.doubleValue(), false);
        // colonne G : API AI
        this.createCell(montantDiminutionAPIAIRecapJanvier.doubleValue(), false);
        // colonne H : total de la ligne
        this.createCell(totalDiminutionRecapJanvier.doubleValue(), false);

        // création de la ligne 10 : Augmentation mois suivant
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_13"), getStyleListLeft());
        // colonne B : AVS ordinaire
        this.createCell(montantAugmentationAVSOrdinaireRecapJanvier.doubleValue(), false);
        // colonne C : AVS extraordinaire
        this.createCell(montantAugmentationAVSExtraordinaireRecapJanvier.doubleValue(), false);
        // colonne D : API AVS
        this.createCell(montantAugmentationAPIAVSRecapJanvier.doubleValue(), false);
        // colonne E : AI ordinaire
        this.createCell(montantAugmentationAIOrdinaireRecapJanvier.doubleValue(), false);
        // colonne F : AI extraordinaire
        this.createCell(montantAugmentationAIExtraordinaireRecapJanvier.doubleValue(), false);
        // colonne G : API AI
        this.createCell(montantAugmentationAPIAIRecapJanvier.doubleValue(), false);
        // colonne H : total de la ligne
        this.createCell(totalAugmentationRecapJanvier.doubleValue(), false);

        // Ligne vide
        createRow();

        // création de la ligne 12 : Solde net
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_5"), getStyleListTitleLeft());
        // colonne B : AVS ordinaire
        createCellFormula("B7+B8-B9+B10", getStyleMontantTotal());
        // colonne C : AVS extraordinaire
        createCellFormula("C7+C8-C9+C10", getStyleMontantTotal());
        // colonne D : API AVS
        createCellFormula("D7+D8-D9+D10", getStyleMontantTotal());
        // colonne E : AI ordinaire
        createCellFormula("E7+E8-E9+E10", getStyleMontantTotal());
        // colonne F : AI extraordinaire
        createCellFormula("F7+F8-F9+F10", getStyleMontantTotal());
        // colonne G : API AI
        createCellFormula("G7+G8-G9+G10", getStyleMontantTotal());
        // colonne H : total de la ligne
        createCellFormula("H7+H8-H9+H10", getStyleMontantTotal());

    }

    private void remplirTroisiemePartie() throws Exception {

        // Ligne 15 : titre du tableau
        createRow();
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_6"), getStyleListTitleLeft());

        Set<RERentesAdapteesJointRATiers> rentesAugmenteesDeManiereAutomatique = new HashSet<RERentesAdapteesJointRATiers>();
        Set<RERentesAdapteesJointRATiers> rentesAugmenteesParLaCentrale = new HashSet<RERentesAdapteesJointRATiers>();
        Set<RERentesAdapteesJointRATiers> rentesAugmenteesManuellement = new HashSet<RERentesAdapteesJointRATiers>();

        JADate date = new JADate(getMoisAnnee());

        RERentesAdapteesJointRATiersManager rentesAdapMgr = new RERentesAdapteesJointRATiersManager();
        rentesAdapMgr.setSession(getSession());
        rentesAdapMgr.setForAnneeAdaptation(String.valueOf(date.getYear()));
        rentesAdapMgr.find(BManager.SIZE_NOLIMIT);

        for (RERentesAdapteesJointRATiers rentesAdap : rentesAdapMgr.getContainerAsList()) {

            if (IREAdaptationRente.CS_TYPE_AUG_ADAPTATION_AUTO.equals(rentesAdap.getCsTypeAdaptation())) {
                rentesAugmenteesDeManiereAutomatique.add(rentesAdap);
            } else if (IREAdaptationRente.CS_TYPE_AUG_DECISIONS_DECEMBRE.equals(rentesAdap.getCsTypeAdaptation())) {
                rentesAugmenteesParLaCentrale.add(rentesAdap);
            } else if (IREAdaptationRente.CS_TYPE_AUG_TRAITEMENT_MANUEL.equals(rentesAdap.getCsTypeAdaptation())) {
                rentesAugmenteesManuellement.add(rentesAdap);
            } else if (IREAdaptationRente.CS_TYPE_NON_AUGMENTEES.equals(rentesAdap.getCsTypeAdaptation())) {
                // rien à faire pour ces cas
            } else {
                throw new Exception(getSession().getLabel("ERREUR_ADAPTATION_TYPE_INCONNU"));
            }
        }

        // Lignes 16 et 17 : Cas traités par la centrale
        remplirLignesPourTypeAdaptation(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_7"),
                rentesAugmenteesDeManiereAutomatique);

        // Lignes 18 et 19 : Cas adaptés manuellement
        remplirLignesPourTypeAdaptation(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_9"),
                rentesAugmenteesManuellement);

        // Lignes 20 et 21 : Cas augmentés par le programme Java
        remplirLignesPourTypeAdaptation(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_10"),
                rentesAugmenteesParLaCentrale);
    }

    private void remplirLignesPourTypeAdaptation(String nomRubrique, Set<RERentesAdapteesJointRATiers> rentesAugmentees) {

        FWCurrency montantRentesOrdinaireAVS = new FWCurrency();
        FWCurrency montantRentesExtraordinaireAVS = new FWCurrency();
        FWCurrency montantRentesAPIAVS = new FWCurrency();
        FWCurrency montantRentesOrdinaireAI = new FWCurrency();
        FWCurrency montantRentesExtraordinaireAI = new FWCurrency();
        FWCurrency montantRentesAPIAI = new FWCurrency();

        FWCurrency montantTotalRentes = new FWCurrency();

        int nombreRentesOrdinaireAVS = 0;
        int nombreRentesExtraordinaireAVS = 0;
        int nombreRentesAPIAVS = 0;
        int nombreRentesOrdinaireAI = 0;
        int nombreRentesExtraordinaireAI = 0;
        int nombreRentesAPIAI = 0;

        int nombreTotalRentes = 0;

        for (RERentesAdapteesJointRATiers renteAdap : rentesAugmentees) {

            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(renteAdap
                    .getCodePrestation()));

            FWCurrency difference = new FWCurrency(renteAdap.getNouveauMontantPrestation());
            difference.sub(renteAdap.getAncienMontantPrestation());

            if (codePrestation.isVieillesse() || codePrestation.isSurvivant()) {

                if (codePrestation.isRenteOrdinaire()) {

                    // Rentes ordinaires AVS
                    montantRentesOrdinaireAVS.add(difference);
                    nombreRentesOrdinaireAVS++;

                } else if (codePrestation.isRenteExtraordinaire()) {

                    // Rentes extraordinaires AVS
                    montantRentesExtraordinaireAVS.add(difference);
                    nombreRentesExtraordinaireAVS++;

                }
            } else if (codePrestation.isAPIAVS()) {
                // Allocation pour impotent AVS
                montantRentesAPIAVS.add(difference);
                nombreRentesAPIAVS++;

            } else if (codePrestation.isAI()) {

                if (codePrestation.isRenteOrdinaire()) {
                    // Rentes ordinaires AI
                    montantRentesOrdinaireAI.add(difference);
                    nombreRentesOrdinaireAI++;

                } else if (codePrestation.isRenteExtraordinaire()) {
                    // Rentes extraordinaires AI
                    montantRentesExtraordinaireAI.add(difference);
                    nombreRentesExtraordinaireAI++;

                }
            } else if (codePrestation.isAPIAI()) {
                // Allocation pour impotent AI
                montantRentesAPIAI.add(difference);
                nombreRentesAPIAI++;

            }

            montantTotalRentes.add(difference);
            nombreTotalRentes++;

        }

        // montants
        createRow();
        // colonne A : description de la ligne
        this.createCell(nomRubrique, getStyleListLeft());
        // colonne B : AVS ordinaire
        this.createCell(montantRentesOrdinaireAVS.doubleValue(), false);
        // colonne C : AVS extraordinaire
        this.createCell(montantRentesExtraordinaireAVS.doubleValue(), false);
        // colonne D : API AVS
        this.createCell(montantRentesAPIAVS.doubleValue(), false);
        // colonne E : AI ordinaire
        this.createCell(montantRentesOrdinaireAI.doubleValue(), false);
        // colonne F : AI extraordinaire
        this.createCell(montantRentesExtraordinaireAI.doubleValue(), false);
        // colonne G : API AI
        this.createCell(montantRentesAPIAI.doubleValue(), false);
        // colonne H : total de la ligne
        this.createCell(montantTotalRentes.doubleValue(), false);

        // nombres de cas
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_8"), getStyleListLeft());
        // colonne B : AVS ordinaire
        this.createCell(nombreRentesOrdinaireAVS, getStyleNombre());
        // colonne C : AVS extraordinaire
        this.createCell(nombreRentesExtraordinaireAVS, getStyleNombre());
        // colonne D : API AVS
        this.createCell(nombreRentesAPIAVS, getStyleNombre());
        // colonne E : AI ordinaire
        this.createCell(nombreRentesOrdinaireAI, getStyleNombre());
        // colonne F : AI extraordinaire
        this.createCell(nombreRentesExtraordinaireAI, getStyleNombre());
        // colonne G : API AI
        this.createCell(nombreRentesAPIAI, getStyleNombre());
        // colonne H : total de la ligne
        this.createCell(nombreTotalRentes, getStyleNombre());

    }

    private REPmtFictif chargerPaiementFictif(String mois, String csTypeDonneesPaiementFictif, String messageEnCasErreur)
            throws Exception {

        REPmtFictifManager pmtFicMgr = new REPmtFictifManager();
        pmtFicMgr.setSession(getSession());
        pmtFicMgr.setForCsTypeDonnee(csTypeDonneesPaiementFictif);
        pmtFicMgr.setForMoisAnnee(mois);
        pmtFicMgr.find();

        if (pmtFicMgr.isEmpty()) {
            throw new Exception(messageEnCasErreur);
        }

        return (REPmtFictif) pmtFicMgr.getFirstEntity();
    }

    private REDetailRecapMensuelleViewBean chargerRecapMensuelle(String mois, String messageEnCasErreur)
            throws Exception {

        String idRecap = loadRecapMensuelle(mois, getSession());

        if (JadeStringUtil.isBlankOrZero(idRecap)) {
            throw new Exception(messageEnCasErreur);
        }

        REDetailRecapMensuelleViewBean recap = new REDetailRecapMensuelleViewBean();
        recap.setIdRecapMensuelle(idRecap);
        recap.setSession(getSession());
        recap.retrieve();

        return recap;
    }

    private void remplirTotalAugmentation() {

        // création de la ligne 22 : Augmentation nette (Adaptation)
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_11"), getStyleListTitleLeft());
        // colonne B : AVS ordinaire
        createCellFormula("B16+B18+B20", getStyleMontantTotal());
        // colonne C : AVS extraordinaire
        createCellFormula("C16+C18+C20", getStyleMontantTotal());
        // colonne D : API AVS
        createCellFormula("D16+D18+D20", getStyleMontantTotal());
        // colonne E : AI ordinaire
        createCellFormula("E16+E18+E20", getStyleMontantTotal());
        // colonne F : AI extraordinaire
        createCellFormula("F16+F18+F20", getStyleMontantTotal());
        // colonne G : API AI
        createCellFormula("G16+G18+G20", getStyleMontantTotal());
        // colonne H : total de la ligne
        createCellFormula("H16+H18+H20", getStyleMontantTotal());

    }

    private void remplirPourcentage() {

        // création de la ligne 22 : Augmentation nette (Adaptation)
        createRow();
        // colonne A : description de la ligne
        this.createCell(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_TITRE_12"), getStyleListTitleLeft());
        // colonne B : AVS ordinaire
        createCellFormula("B23/B3*100", getStyleMontant());
        // colonne C : AVS extraordinaire
        createCellFormula("C23/C3*100", getStyleMontant());
        // colonne D : API AVS
        createCellFormula("D23/D3*100", getStyleMontant());
        // colonne E : AI ordinaire
        createCellFormula("E23/E3*100", getStyleMontant());
        // colonne F : AI extraordinaire
        createCellFormula("F23/F3*100", getStyleMontant());
        // colonne G : API AI
        createCellFormula("G23/G3*100", getStyleMontant());
        // colonne H : total de la ligne
        createCellFormula("H23/H3*100", getStyleMontant());
    }

    /**
     * construction de la feuille Excel
     */
    public HSSFSheet populateSheetListe(BTransaction transaction) throws Exception {

        // création de l'en-tête des tableaux (lignes 1 et 2)
        remplirEnTeteTableau();

        // création du 1er tableau (lignes 3 à 5)
        remplirPremierePartie();

        // Ligne vide (ligne 6)
        createRow();

        // création du 2ème tableau et du solde net (lignes 7 à 12)
        remplirDeuxiemePartie();

        // Lignes vides (lignes 13 et 14)
        createRow();
        createRow();

        // création du troisième tableau (lignes 15 à 21)
        remplirTroisiemePartie();

        // Ligne vide (ligne 22)
        createRow();

        // création de la ligne du total de l'augmentation (ligne 23)
        remplirTotalAugmentation();

        // Ligne vide (ligne 24)
        createRow();

        // création de la ligne de pourcentage de différence (ligne 25)
        remplirPourcentage();

        return currentSheet;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }
}
