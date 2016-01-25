package globaz.corvus.excel;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.ci.REListeCiAdditionnels;
import globaz.corvus.db.ci.REListeCiAdditionnelsManager;
import globaz.corvus.db.ci.TypeListeCiAdditionnels;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.jade.log.JadeLogger;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;

/**
 * <strong>Génère une liste Excel contenant la liste des CI additionnels</strong></br> Plusieurs choix sont possible
 * pour la génération de cette liste. </br>Il sont énuméré par l'enum <strong> @see
 * globaz.corvus.vb.process.REGenererListeCiAdditionnelsViewBean.TypeListeCiAdditionnels. </strong></br> 3 choix sont
 * actuellement possible (04.2013)</br> </br> - NonTraites</br> - Réceptionnes</br> - Traites
 */
public class REListeExcelCiAdditionnels extends REAbstractListExcel {

    private class POJO implements Comparable<POJO> {
        private String idTiers;
        private String nss;
        private String nom;
        private String prenom;
        private FWCurrency montantTotal;
        private String dateReception;
        private String dateTraitement;
        private String prestationEnCours;
        private String dateDebutPrestation;
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

        public final String getDateReception() {
            return dateReception;
        }

        public final void setDateReception(String dateReception) {
            this.dateReception = dateReception;
        }

        public final String getDateTraitement() {
            return dateTraitement;
        }

        public final void setDateTraitement(String dateTraitement) {
            this.dateTraitement = dateTraitement;
        }

        public final String getPrestationEnCours() {
            return prestationEnCours;
        }

        public final void setPrestationEnCours(String prestationEnCours) {
            this.prestationEnCours = prestationEnCours;
        }

        public final String getDateDebutPrestation() {
            return dateDebutPrestation;
        }

        public final void setDateDebutPrestation(String dateDebutPrestation) {
            this.dateDebutPrestation = dateDebutPrestation;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
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
    private final String documentTitleKey = "TITRE_DOCUMENT_LISTE_CI_ADDITIONNELS";
    private String eMailAddress = "";
    private TypeListeCiAdditionnels genreCiAdd = TypeListeCiAdditionnels.RECEPTIONNES;
    private String noAgence;
    private String noCaisse;

    public REListeExcelCiAdditionnels(BSession session) {
        super(session, "REListeCiAdditionnels", "");
        setDocumentTitle(session.getLabel("TITRE_DOCUMENT_LISTE_CI_ADDITIONNELS"));
    }

    private void configColumnsWidth() {
        int numCol = 0;
        if (getAjouterCommunePolitique()) {
            currentSheet.setColumnWidth((short) numCol++, (short) 3000); // COMMUNE POLITIQUE
        }
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // NSS
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // NOM
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // PRENOM
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // MONTANT
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // DATE_RECEPTION
        currentSheet.setColumnWidth((short) numCol++, (short) 3000); // DATE_TRAITEMENT
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // GENRE_PRESTATION
        currentSheet.setColumnWidth((short) numCol++, (short) 4000); // DATE_DEBUT_PRESTATION
    }

    /**
     * Remplit une ligne de la liste en fonction du CI
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
    private void fillRow(POJO pojo, BTransaction transaction) throws Exception {
        if (getAjouterCommunePolitique()) {
            this.createCell(pojo.getCommunePolitique(), getStyleListCenter()); // COMMUNE POLITIQUE
        }
        this.createCell(pojo.getNss(), getStyleListLeft()); // NSS
        this.createCell(pojo.getNom(), getStyleListLeft()); // NOM
        this.createCell(pojo.getPrenom(), getStyleListLeft()); // PRENOM
        this.createCell(pojo.getMontantTotal().toStringFormat(), getStyleMontant()); // MONTANT
        this.createCell(pojo.getDateReception(), getStyleDate()); // DATE_RECEPTION
        this.createCell(pojo.getDateTraitement(), getStyleDate()); // DATE_TRAITEMENT
        this.createCell(pojo.getPrestationEnCours(), getStyleListRight()); // GENRE_PRESTATION
        this.createCell(pojo.getDateDebutPrestation(), getStyleDate()); // DATE_DEBUT_PRESTATION
    }

    /**
     * Return une liste avec les titres de colonne
     * 
     * @return Une liste avec les titres de colonne
     */
    private ArrayList<String> getColumnsTitlesForTemplateA() {
        ArrayList<String> colTitles = new ArrayList<String>();
        if (getAjouterCommunePolitique()) {
            colTitles.add(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()));
        }
        colTitles.add(getSession().getLabel("LISTE_CIA_NSS"));
        colTitles.add(getSession().getLabel("LISTE_CIA_NOM"));
        colTitles.add(getSession().getLabel("LISTE_CIA_PRENOM"));
        colTitles.add(getSession().getLabel("LISTE_CIA_MONTANT"));
        colTitles.add(getSession().getLabel("LISTE_CIA_DATE_RECEPTION"));
        colTitles.add(getSession().getLabel("LISTE_CIA_DATE_TRAITEMENT"));
        colTitles.add(getSession().getLabel("LISTE_CIA_GENRE_PRESTATION"));
        colTitles.add(getSession().getLabel("LISTE_CIA_DATE_DEBUT_PRESTATION"));
        return colTitles;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * Boucle sur toutes les prestations (ordonnées par genrePrestation) et recherche la date de début de la première
     * rentes principale Vieillesse - Survivant - Invalidité
     * 
     * @param prestations
     *            La listes des prestations
     * @return La date de début de la 1ère rente trouvée dont le code prestation est un code prestation d'une rente
     *         principale Vieillesse - Survivant - Invalidité
     */
    private String getDateDebutPrestation(JAVector prestations) {
        for (int ctr = 0; ctr < prestations.size(); ctr++) {
            REPrestationsAccordees prestationsAccordee = (REPrestationsAccordees) prestations.get(ctr);
            if (PRCodePrestationVieillesse.isPrestationPrincipale(prestationsAccordee.getCodePrestation())
                    || PRCodePrestationInvalidite.isPrestationPrincipale(prestationsAccordee.getCodePrestation())
                    || PRCodePrestationSurvivant.isPrestationPrincipale(prestationsAccordee.getCodePrestation())) {
                return prestationsAccordee.getDateDebutDroit();
            }
        }
        return "";
    }

    public final String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public String getDateFin() {
        return dateFin;
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
     * Boucle sur toutes les prestations et retourne une String formatée (ex : 10 / 33 / 50) contenant l'ensemble des
     * codePrestations <strong>HORMIS LES CODES PRESTATIONS API</strong>
     * 
     * @param prestations
     *            La liste des prestations
     * @return une String formaté (ex : 10 / 33 / 50) contenant l'ensemble des codePrestations <strong>HORMIS LES CODES
     *         PRESTATIONS API</strong>
     */
    private String getGenreDePrestationEnCours(JAVector prestations) {
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("unchecked")
        Iterator<REPrestationsAccordees> iter = prestations.iterator();
        while (iter.hasNext()) {
            REPrestationsAccordees prestation = iter.next();
            if (!PRCodePrestationAPI.isCodePrestationAPI(prestation.getCodePrestation())) {
                sb.append(prestation.getCodePrestation());
                if (iter.hasNext()) {
                    sb.append(" / ");
                }
            }
        }
        return sb.toString();
    }

    public final String getNoAgence() {
        return noAgence;
    }

    public final String getNoCaisse() {
        return noCaisse;
    }

    private void initHeader() throws Exception {
        createRow();
        switch (getGenreCiAdd()) {
            case RECEPTIONNES:
                this.createCell(getSession().getLabel("LISTE_CIA_DESC_GENRE_RECEPTIONNES"));
                break;
            case TRAITES:
                this.createCell(getSession().getLabel("LISTE_CIA_DESC_GENRE_TRAITES"));
                break;
            case NON_TRAITES:
                this.createCell(getSession().getLabel("LISTE_CIA_DESC_GENRE_NON_TRAITE"));
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

        // Ajout des infos sur l'utilisateur qui à généré la liste
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
            initTitleRow(getColumnsTitlesForTemplateA());
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

            // parcours du manager et remplissage des cellules
            BStatement statement = manager.cursorOpen(transaction);
            REListeCiAdditionnels ci = null;
            List<POJO> list = new ArrayList<POJO>();
            Set<String> setIdTiers = new HashSet<String>();

            while (((ci = (REListeCiAdditionnels) manager.cursorReadNext(statement)) != null) && !ci.isNew()) {
                if (ci != null) {
                    POJO pojo = new POJO();
                    pojo.setIdTiers(ci.getIdTiers());
                    pojo.setNss(ci.getNss());
                    pojo.setNom(ci.getNom());
                    pojo.setPrenom(ci.getPrenom());
                    pojo.setMontantTotal(new FWCurrency(ci.getMontantTotal()));
                    pojo.setDateReception(ci.getDateReception());
                    pojo.setDateTraitement(ci.getDateTraitement());

                    // Ajout des infos concernant les prestations
                    prestationManager.setForIdTiersBeneficiaire(ci.getIdTiers());
                    prestationManager.find(transaction, BManager.SIZE_NOLIMIT);
                    JAVector prestations = prestationManager.getContainer();
                    pojo.setPrestationEnCours(getGenreDePrestationEnCours(prestations));
                    pojo.setDateDebutPrestation(getDateDebutPrestation(prestations));

                    setIdTiers.add(ci.getIdTiers());
                    list.add(pojo);
                }
            }

            // --------------------------------
            // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
            if (getAjouterCommunePolitique()) {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateFin);
                Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date,
                        getSession());

                for (POJO pojo : list) {
                    pojo.setCommunePolitique(mapCommuneParIdTiers.get(pojo.getIdTiers()));
                }
            }
            // ------------------------------
            // Tri et génération de la liste
            Collections.sort(list);
            for (POJO pojo : list) {
                createRow();
                fillRow(pojo, transaction);
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
