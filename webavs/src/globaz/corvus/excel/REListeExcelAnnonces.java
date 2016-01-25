/*
 * Créé le 16 sept. 08
 */
package globaz.corvus.excel;

import globaz.corvus.db.annonces.REAnnoncesGroupePrestations;
import globaz.corvus.db.annonces.REAnnoncesGroupePrestationsManager;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author BSC
 * 
 *         Imprime la liste des annonces pour un mois donné
 */
public class REListeExcelAnnonces extends REAbstractListExcel {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String mois = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public REListeExcelAnnonces(BSession session) {
        super(session, "REListeExcelAnnonces", "Liste des annonces");
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getMois() {
        return mois;
    }

    private void initListe() {

        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(getSession().getLabel("LISTE_EXA_NSS"));
        colTitles.add(getSession().getLabel("LISTE_EXA_NOM"));
        colTitles.add(getSession().getLabel("LISTE_EXA_PRENOM"));
        colTitles.add(getSession().getLabel("LISTE_EXA_DEBUT_DROIT"));
        colTitles.add(getSession().getLabel("LISTE_EXA_FIN_DROIT"));
        colTitles.add(getSession().getLabel("LISTE_EXA_CODE_MUTATION"));
        colTitles.add(getSession().getLabel("LISTE_EXA_GENRE_PRESTATION"));
        colTitles.add(getSession().getLabel("LISTE_EXA_MONTANT_AUGMENTATION"));
        colTitles.add(getSession().getLabel("LISTE_EXA_MONTANT_DIMINUTION"));

        createSheet(FWMessageFormat.format(getSession().getLabel("LISTE_EXA_TITRE"), mois));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter();

        // définition de la taille des cellules
        int numCol = 0;

        currentSheet.setColumnWidth((short) numCol++, (short) 4500); // NSS
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // NOM
        currentSheet.setColumnWidth((short) numCol++, (short) 6000); // PRENOM
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // DEBUT
        // DROIT
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // FIN
        // DROIT
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // CODE
        // MUTATION
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // GENRE
        // PRESTATION
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // MONTANT
        // AUGMENTATION
        currentSheet.setColumnWidth((short) numCol++, (short) 5000); // MONTANT
        // DIMINUTION
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(REAnnoncesGroupePrestationsManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = manager.cursorOpen(transaction);

        REAnnoncesGroupePrestations annonce = null;

        // création de la page
        initListe();

        // parcours du manager et remplissage des cell
        while ((annonce = (REAnnoncesGroupePrestations) manager.cursorReadNext(statement)) != null && !annonce.isNew()) {
            if (annonce != null) {
                createRow();

                // Recherche des information sur le tiers beneficiaire
                PRTiersWrapper assure = PRTiersHelper.getPersonneAVS(getSession(), annonce.getIdTiers());
                if (assure != null) {
                    createCell(assure.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), getStyleListLeft()); // NSS
                    createCell(assure.getProperty(PRTiersWrapper.PROPERTY_NOM), getStyleListLeft()); // NOM
                    createCell(assure.getProperty(PRTiersWrapper.PROPERTY_PRENOM), getStyleListLeft()); // PRENOM

                } else {
                    createCell("", getStyleListLeft()); // NSS
                    createCell("", getStyleListLeft()); // NOM
                    createCell("", getStyleListLeft()); // PRENOM
                }

                String debutDroit = annonce.getDebutDroit();
                if (debutDroit.length() == 3) {
                    debutDroit = "0" + debutDroit;
                }
                createCell(PRDateFormater.convertDate_MMAA_to_MMxAAAA(debutDroit), getStyleDateMMAAAA()); // DEBUT
                // DROIT

                String finDroit = annonce.getFinDroit();
                if (finDroit.length() == 3) {
                    finDroit = "0" + finDroit;
                }
                createCell(PRDateFormater.convertDate_MMAA_to_MMxAAAA(finDroit), getStyleDateMMAAAA()); // FIN
                // DROIT
                createCell(annonce.getCodeMutation(), getStyleDate()); // CODE
                // MUTATION
                createCell(annonce.getGenrePrestation(), getStyleDate()); // GENRE
                // PRESTATION

                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                // en fonction du code d'application de l'annonce, on definit si
                // augmentation ou diminution
                // /////////////////////////////////////////////////////////////////////////////////////////////////////
                if (annonce.getCodeApplication().equals("41") || annonce.getCodeApplication().equals("44")) {

                    // si une date de fin, on force a zero, sinon on renseigne
                    // l'augmentation
                    if (JadeStringUtil.isEmpty(annonce.getFinDroit())) {

                        Double montantAugmentation = new Double(annonce.getMensualitePrestationsFrancs()); // MONTANT
                        // AUGMENTATION
                        createCell(montantAugmentation.doubleValue(), false);

                        Double montantDiminution = new Double("0.00"); // MONTANT
                        // DIMINUTION
                        createCell(montantDiminution.doubleValue(), false);

                    } else {

                        Double montantAugmentation = new Double("0.00"); // MONTANT
                        // AUGMENTATION
                        createCell(montantAugmentation.doubleValue(), false);

                        Double montantDiminution = new Double("0.00"); // MONTANT
                        // DIMINUTION
                        createCell(montantDiminution.doubleValue(), false);

                    }
                }
                if (annonce.getCodeApplication().equals("42") || annonce.getCodeApplication().equals("45")) {

                    Double montantAugmentation = new Double("0.00"); // MONTANT
                    // AUGMENTATION
                    createCell(montantAugmentation.doubleValue(), false);

                    Double montantDiminution = new Double(annonce.getMensualitePrestationsFrancs()); // MONTANT
                    // DIMINUTION
                    createCell(montantDiminution.doubleValue(), false);

                }
                if (annonce.getCodeApplication().equals("43") || annonce.getCodeApplication().equals("46")) {

                    Double montantAugmentation = new Double("0.00"); // MONTANT
                    // AUGMENTATION
                    createCell(montantAugmentation.doubleValue(), false);

                    Double montantDiminution = new Double("0.00"); // MONTANT
                    // DIMINUTION
                    createCell(montantDiminution.doubleValue(), false);

                }
            }
        }
        return currentSheet;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

}
