package globaz.corvus.excel;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.corvus.db.ci.REListeCiAdditionnels;
import globaz.corvus.db.ci.REListeCiAdditionnelsManager;
import globaz.corvus.db.ci.TypeListeCiAdditionnels;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;

/**
 * <strong>Génère une liste Excel contenant la liste des CI additionnels</strong></br> Plusieurs choix sont possible
 * pour la génération de cette liste. Il sont énuméré par l'enum <strong> @see
 * globaz.corvus.vb.process.REGenererListeCiAdditionnelsViewBean.TypeListeCiAdditionnels. </strong> 3 choix sont
 * actuellement possible (04.2013)</br> - ATTENTE_CI_ADD_PROVISOIRE</br> - ATTENTE_CI_ADD_TOUS</br> -
 * ATTENTE_CI_ADD_TRAITE</br>
 */
public class REListeExcelCIEnAttenteDeCIAdditionnel extends REAbstractListExcel {

    private class POJO implements Comparable<POJO> {
        private String idTiers;
        private String nss;
        private String nom;
        private String prenom;
        private FWCurrency montantTotal;
        private String periodeCI;
        private String dateSaiseManuelle;
        private String isDefinitif;
        private String dateLiquidation;
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

        public final FWCurrency getMontantTotal() {
            return montantTotal;
        }

        public final void setMontantTotal(FWCurrency montantTotal) {
            this.montantTotal = montantTotal;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        public final String getPeriodeCI() {
            return periodeCI;
        }

        public final void setPeriodeCI(String periodeCI) {
            this.periodeCI = periodeCI;
        }

        public final String getDateSaiseManuelle() {
            return dateSaiseManuelle;
        }

        public final void setDateSaiseManuelle(String dateSaiseManuelle) {
            this.dateSaiseManuelle = dateSaiseManuelle;
        }

        public final String getIsDefinitif() {
            return isDefinitif;
        }

        public final void setIsDefinitif(String isDefinitif) {
            this.isDefinitif = isDefinitif;
        }

        public final String getDateLiquidation() {
            return dateLiquidation;
        }

        public final void setDateLiquidation(String dateLiquidation) {
            this.dateLiquidation = dateLiquidation;
        }

        @Override
        public int compareTo(POJO o) {
            if (getAjouterCommunePolitique()) {
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

    private String dateDebut = "";
    private String dateDernierPaiement;
    private String dateFin = "";
    private boolean ajouterCommunePolitique;

    /**
     * Clé pour le titre du document
     */
    private final String documentTitleKey = "TITRE_DOCUMENT_LISTE_CI_ATTENTE_CI_ADDITIONNELS";
    private String eMailAddress = "";
    private TypeListeCiAdditionnels genreCiAdd = TypeListeCiAdditionnels.RECEPTIONNES;
    private String noAgence;
    private String noCaisse;

    public REListeExcelCIEnAttenteDeCIAdditionnel(BSession session) {
        super(session, "REListeExcelCIEnAttenteDeCIAdditionnel", "");
        setDocumentTitle(session.getLabel("TITRE_DOCUMENT_LISTE_CI_ATTENTE_CI_ADDITIONNELS"));
    }

    private void configColumnsWidth() {
        int numCol = 0;
        if (getAjouterCommunePolitique()) {
            currentSheet.setColumnWidth((short) numCol++, (short) 3000); // COMMUNE POLITIQUE
        }
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // NSS
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // NOM
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // PRENOM
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // PERIODE
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // MONTANT
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // DATE_SAISIE_MANUELLE
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // DEFINITIF
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // DATE_LIQUIDATION
    }

    /**
     * Remplit une ligne de la liste en fonction du CI et selon le template B
     * 
     * @param prestationManager
     *            Le manager de prestations
     * @param ci
     *            Le CI à traiter
     * @param transaction
     *            La transaction à utiliser
     * @throws Exception
     *             Si des erreurs sont générées par le manager
     */
    private void fillRowForCI(POJO pojo, BTransaction transaction) throws Exception {
        if (getAjouterCommunePolitique()) {
            this.createCell(pojo.getCommunePolitique(), getStyleListCenter()); // COMMUNE POLITIQUE
        }
        this.createCell(pojo.getNss(), getStyleListLeft()); // NSS
        this.createCell(pojo.getNom(), getStyleListLeft()); // NOM
        this.createCell(pojo.getPrenom(), getStyleListLeft()); // PRENOM
        this.createCell(pojo.getPeriodeCI(), getStyleListRight()); // PRENOM
        this.createCell(pojo.getMontantTotal().toStringFormat(), getStyleMontant()); // MONTANT
        this.createCell(pojo.getDateSaiseManuelle(), getStyleDate()); // DATE_SAISIE_MANUELLE
        this.createCell(pojo.getIsDefinitif(), getStyleListCenter()); // DEFINITIF
        this.createCell(pojo.getDateLiquidation(), getStyleDate()); // DATE_DE_LIQUIDATION
    }

    /**
     * Return une liste avec les titres de colonne selon le template B
     * 
     * @return Une liste avec les titres de colonne selon le template
     *         B
     */
    private ArrayList<String> getColumnsTitlesForTemplateB() {
        ArrayList<String> colTitles = new ArrayList<String>();
        if (getAjouterCommunePolitique()) {
            colTitles.add(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()));
        }
        colTitles.add(getSession().getLabel("LISTE_CIA_NSS"));
        colTitles.add(getSession().getLabel("LISTE_CIA_NOM"));
        colTitles.add(getSession().getLabel("LISTE_CIA_PRENOM"));
        colTitles.add(getSession().getLabel("LISTE_CIA_PERIODE"));
        colTitles.add(getSession().getLabel("LISTE_CIA_MONTANT"));
        colTitles.add(getSession().getLabel("LISTE_CIA_DATE_SAISIE_MANUELLE"));
        colTitles.add(getSession().getLabel("LISTE_CIA_DEFINITIF"));
        colTitles.add(getSession().getLabel("LISTE_CIA_DATE_LIQUIDATION"));
        return colTitles;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public final String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public String getDateFin() {
        return dateFin;
    }

    /**
     * Retourne la date de liquidation du CI
     * 
     * @param ci
     *            Le CI à traiter
     * @return La date de liquidation du CI
     */
    private String getDateLiquidationDuCI(REListeCiAdditionnels ci) {
        return formatSpyDate(ci.getpSpy());
    }

    /**
     * Retourne la date de saisie manuelle du CI
     * 
     * @param ci
     *            Le CI à traiter
     * @return La date de saisie manuelle du CI
     */
    private String getDateSaisieManuelleDuCI(REListeCiAdditionnels ci) {
        return formatSpyDate(ci.getcSpy());
    }

    /**
     * Format un CSpy ou un BSpy au format jj.mm.aaaa
     * 
     * @param spy
     * @return
     */
    private String formatSpyDate(String spy) {
        String dateFormatee = "";
        if ((spy != null) && (spy.length() >= 8)) {
            String date = spy.substring(0, 8);
            dateFormatee = PRDateFormater.formatDateFrom(date);
        }
        return dateFormatee;
    }

    /**
     * Retourne la clé à utiliser pour récupérer le label 'titre du document'
     * 
     * @return le label 'titre du document'
     */
    public String getDocumentTitleKey() {
        return documentTitleKey;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public TypeListeCiAdditionnels getGenreCiAdd() {
        return genreCiAdd;
    }

    /**
     * REtourne une String internationalisée (Oui, Non) qui renseigne si le ci est définitif
     * 
     * @param ci
     * @return
     */
    private String getIsCIDefinitif(REListeCiAdditionnels ci) {
        boolean result = REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_TRAITE.equals(ci.getAttenteCiAdditionnel());
        if (result) {
            return getSession().getLabel("TRAITE");
        }
        return "";
    }

    public final String getNoAgence() {
        return noAgence;
    }

    public final String getNoCaisse() {
        return noCaisse;
    }

    /**
     * Retourne la période du C I formatée
     * 
     * @param ci
     *            Le CI à traiter
     * @return La période du C_I formatée
     */
    private String getPeriodeDuCI(REListeCiAdditionnels ci) {
        return ci.getMoisDebutCotisation() + " - " + ci.getMoisFinCotisation() + "." + ci.getAnneeDeCotisation();
    }

    private void initHeader() throws Exception {
        createRow();
        switch (getGenreCiAdd()) {
            case ATTENTE_CI_ADD_TOUS:
                this.createCell(getSession().getLabel("LISTE_CIA_DESC_GENRE_ATTENTE_CI_ADD_TOUS"));
                break;
            case ATTENTE_CI_ADD_PROVISOIRE:
                this.createCell(getSession().getLabel("LISTE_CIA_DESC_GENRE_ATTENTE_CI_ADD_PROVISOIRE"));
                break;
            case ATTENTE_CI_ADD_TRAITE:
                this.createCell(getSession().getLabel("LISTE_CIA_DESC_GENRE_ATTENTE_CI_ADD_TRAITE"));
                break;
            default:
                throwWrongTypeListeCIException();
        }
        this.createCell("");
        this.createCell("");
        this.createCell("");
        getCurrentSheet().addMergedRegion(new Region((short) 0, (short) 0, (short) 0, (short) 3));

        // Cellule avec le texte caisse
        this.createCell(getSession().getLabel("LISTE_CIA_TEXTE_CAISSE") + " : ");
        this.createCell(getNoCaisse() + "." + getNoAgence());

        createRow();
        this.createCell(getSession().getLabel("LISTE_CIA_PERIODE_DE"));
        this.createCell(getDateDebut());
        this.createCell(getSession().getLabel("LISTE_CIA_A"));
        this.createCell(getDateFin());

        if (getAjouterCommunePolitique()) {
            createRow();
            this.createCell(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()));
            this.createCell(getSession().getUserId());
        }
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(REListeCiAdditionnelsManager manager, BTransaction transaction)
            throws Exception {
        try {
            // Initialization du document
            createSheet(getSession().getLabel("LISTE_CIA_TITRE"));
            initHeader();
            initTitleRow(getColumnsTitlesForTemplateB());
            initPage(true);
            createHeader();
            createFooter();
            configColumnsWidth();

            // Préparation du manager des prestations
            REPrestationAccordeeManager prestationManager = new REPrestationAccordeeManager();
            prestationManager.setSession(getSession());
            prestationManager.setForEnCoursAtMois(getDateDernierPaiement());
            prestationManager.setOrderBy(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);
            prestationManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                    + IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_DIMINUE);

            // parcours du manager et remplissage des cell
            BStatement statement = manager.cursorOpen(transaction);
            REListeCiAdditionnels ci = null;
            List<POJO> listPojos = new ArrayList<POJO>();
            Set<String> setIdTiers = new HashSet<String>();

            while (((ci = (REListeCiAdditionnels) manager.cursorReadNext(statement)) != null) && !ci.isNew()) {
                if (ci != null) {
                    POJO pojo = new POJO();
                    pojo.setIdTiers(ci.getIdTiers());
                    pojo.setNss(ci.getNss());
                    pojo.setNom(ci.getNom());
                    pojo.setPrenom(ci.getPrenom());
                    pojo.setPeriodeCI(getPeriodeDuCI(ci));
                    pojo.setMontantTotal(new FWCurrency(ci.getMontantTotal()));
                    pojo.setDateSaiseManuelle(getDateSaisieManuelleDuCI(ci));

                    // On renseigne ces info uniquement si le CI est en état traité
                    if (REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_TRAITE.equals(ci.getAttenteCiAdditionnel())) {
                        pojo.setIsDefinitif(getIsCIDefinitif(ci));
                        pojo.setDateLiquidation(getDateLiquidationDuCI(ci));
                    } else {
                        pojo.setIsDefinitif("");
                        pojo.setDateLiquidation("");
                    }

                    setIdTiers.add(ci.getIdTiers());
                    listPojos.add(pojo);
                }
            }

            // --------------------------------
            // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
            if (getAjouterCommunePolitique()) {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateFin);
                Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date,
                        getSession());

                for (POJO pojo : listPojos) {
                    pojo.setCommunePolitique(mapCommuneParIdTiers.get(pojo.getIdTiers()));
                }
            }
            // Tri de la liste selon nom-prenom-(communePolitique) et génération
            Collections.sort(listPojos);
            for (POJO pojo : listPojos) {
                createRow();
                fillRowForCI(pojo, transaction);
            }
        } catch (Exception exception) {
            JadeLogger.error(this, exception);
            throw exception;
        }

        return currentSheet;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public final void setDateDernierPaiement(String dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setGenreCiAdd(TypeListeCiAdditionnels genreCiAdd) {
        this.genreCiAdd = genreCiAdd;
    }

    public final void setNoAgence(String noAgence) {
        this.noAgence = noAgence;
    }

    public final void setNoCaisse(String noCaisse) {
        this.noCaisse = noCaisse;
    }

    public boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

    private void throwWrongTypeListeCIException() throws Exception {
        throw new Exception("Internal error : Wrong Excel List type for selected TypeListeCiAdditionnels ["
                + genreCiAdd + "]");
    }
}
