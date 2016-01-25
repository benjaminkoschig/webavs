package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.creances.IRECreancier;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.creances.RECreancierManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.itext.REDemandeCompensationAdapter;
import globaz.corvus.utils.REGedUtils;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAHolidays;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.ged.PRGedHelper;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.itext.PRLettreEnTete;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.topaz.PRLettreEnTeteOO;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Création de la demande de compensation avec la solution Topaz
 * 
 * @author HPE
 */
public class REDemandeCompensationOO extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static JACalendarGregorian calendarWithHolidays;

    private static final String FILE_HOLIDAYS_XML = "/holidays.xml";

    private Boolean afficherTauxInv;
    private String codeIsoLangue;
    private String commentaires;
    private RECreancier creancier;
    private DocumentData data;
    private ICTDocument document;
    private ICTDocument documentHelper;
    private String emailAdresse;
    private String idCreancier;
    private String idDemandeRente;
    private Boolean isImprimerTous;
    private Boolean isSendToGed;
    private String moisAnnee;
    private String montant1;
    private String montant2;
    private String montant3;
    private String montant4;
    private FWCurrency montantTotalFinal;
    private String texte1;
    private String texte2;
    private String texte3;
    private String texte4;

    public REDemandeCompensationOO() {
        super();

        afficherTauxInv = Boolean.FALSE;
        codeIsoLangue = "fr";
        commentaires = "";
        creancier = new RECreancier();
        data = null;
        document = null;
        documentHelper = null;
        emailAdresse = "";
        idCreancier = "";
        idDemandeRente = "";
        isImprimerTous = Boolean.FALSE;
        isSendToGed = null;
        moisAnnee = "";
        montant1 = "";
        montant2 = "";
        montant3 = "";
        montant4 = "";
        montantTotalFinal = new FWCurrency();
        texte1 = "";
        texte2 = "";
        texte3 = "";
        texte4 = "";
    }

    private void chargementCatalogueTexte() throws Exception {

        // Chargement du catalogue de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_DEMANDE_COMPENSATION);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            document = documents[0];
        }

    }

    private DocumentData createLettreEnTete(String idTiers) throws Exception {

        PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(getSession());

        // retrieve du tiers
        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);

        if (null == tier) {
            tier = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tier) {
            lettreEnTete.setTierAdresse(tier);
            lettreEnTete.setIdAffilie(creancier.getIdAffilieAdressePmt());
            lettreEnTete.setSession(getSession());
            lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_CORVUS);
            lettreEnTete.setDemandeCompensation(true);
            lettreEnTete.generationLettre();
        }

        return lettreEnTete.getDocumentData();

    }

    public Boolean getAfficherTauxInv() {
        return afficherTauxInv;
    }

    public String getCommentaires() {
        return commentaires;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("TITRE_PROCESS");
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public String getIdCreancier() {
        return idCreancier;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public Boolean getIsImprimerTous() {
        return isImprimerTous;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getMontant1() {
        return montant1;
    }

    public String getMontant2() {
        return montant2;
    }

    public String getMontant3() {
        return montant3;
    }

    public String getMontant4() {
        return montant4;
    }

    @Override
    public String getName() {
        return getSession().getLabel("TITRE_PROCESS");
    }

    public String getTexte1() {
        return texte1;
    }

    public String getTexte2() {
        return texte2;
    }

    public String getTexte3() {
        return texte3;
    }

    public String getTexte4() {
        return texte4;
    }

    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private void remplirChampsStatiques() throws Exception {

        // Remplissage des champs statiques
        data.addData("EN_TETE_AVS_AI", document.getTextes(1).getTexte(1).getDescriptionBrut());
        data.addData("TITRE_DOCUMENT", document.getTextes(1).getTexte(2).getDescriptionBrut());
        data.addData("DETAIL_ASSURE", document.getTextes(1).getTexte(3).getDescriptionBrut());
        data.addData("DETAIL_CAISSE", document.getTextes(1).getTexte(4).getDescriptionBrut());
        data.addData("DETAIL_CREANCIER", document.getTextes(1).getTexte(5).getDescriptionBrut());
        data.addData("TEXTE_ASSURE", document.getTextes(1).getTexte(6).getDescriptionBrut());
        data.addData("DETAIL_CAISSE", document.getTextes(1).getTexte(4).getDescriptionBrut());

        if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
            data.addData("TRAITE_PAR", document.getTextes(1).getTexte(7).getDescriptionBrut());
        }

        data.addData("TITRE_PARTIE_1", document.getTextes(2).getTexte(1).getDescriptionBrut());
        data.addData("SOUS_TITRE_1_PARTIE_1", document.getTextes(2).getTexte(2).getDescriptionBrut());
        data.addData("TEXTE_LACI", document.getTextes(2).getTexte(3).getDescriptionBrut());
        data.addData("TEXTE_LAA", document.getTextes(2).getTexte(4).getDescriptionBrut());
        data.addData("TEXTE_LAMAL", document.getTextes(2).getTexte(5).getDescriptionBrut());
        data.addData("TEXTE_LAM", document.getTextes(2).getTexte(6).getDescriptionBrut());
        data.addData("SOUS_TITRE_2_PARTIE_1", document.getTextes(2).getTexte(7).getDescriptionBrut());
        data.addData("ELEMENT_LISTE_1", document.getTextes(2).getTexte(8).getDescriptionBrut());
        data.addData("ELEMENT_LISTE_2", document.getTextes(2).getTexte(9).getDescriptionBrut());
        data.addData("ELEMENT_LISTE_3", document.getTextes(2).getTexte(10).getDescriptionBrut());
        data.addData("ELEMENT_LISTE_4", document.getTextes(2).getTexte(11).getDescriptionBrut());
        data.addData("ELEMENT_LISTE_5", document.getTextes(2).getTexte(12).getDescriptionBrut());
        data.addData("ELEMENT_LISTE_6", document.getTextes(2).getTexte(13).getDescriptionBrut());
        data.addData("ELEMENT_LISTE_7", document.getTextes(2).getTexte(14).getDescriptionBrut());
        data.addData("SOUS_TITRE_3_PARTIE_1", document.getTextes(2).getTexte(15).getDescriptionBrut());
        data.addData("TEXTE_LISTE_1", document.getTextes(2).getTexte(16).getDescriptionBrut());
        data.addData("TEXTE_LISTE_2", document.getTextes(2).getTexte(17).getDescriptionBrut());
        data.addData("TEXTE_LISTE_3", document.getTextes(2).getTexte(18).getDescriptionBrut());
        data.addData("TEXTE_COMPENSATION_1", document.getTextes(2).getTexte(19).getDescriptionBrut());
        data.addData("OUI", document.getTextes(2).getTexte(20).getDescriptionBrut());
        data.addData("NON", document.getTextes(2).getTexte(21).getDescriptionBrut());
        data.addData("TEXTE_JOINDRE", document.getTextes(2).getTexte(22).getDescriptionBrut());
        data.addData("LIEU_ET_DATE", document.getTextes(2).getTexte(23).getDescriptionBrut());
        data.addData("SIGNATURE_PARTIE_1", document.getTextes(2).getTexte(24).getDescriptionBrut());
        data.addData("SIGNATURE_FINALE_PARTIE_1", document.getTextes(2).getTexte(25).getDescriptionBrut());

        data.addData("TITRE_PARTIE_2", document.getTextes(3).getTexte(1).getDescriptionBrut());
        data.addData("DEBUT_TEXTE_PARTIE_2", document.getTextes(3).getTexte(2).getDescriptionBrut());
        data.addData("PERIODE_DU_1", document.getTextes(3).getTexte(3).getDescriptionBrut());
        data.addData("PERIODE_AU", document.getTextes(3).getTexte(5).getDescriptionBrut());
        data.addData("CHF", document.getTextes(3).getTexte(6).getDescriptionBrut());
        data.addData("MILIEU_TEXTE_PARTIE_2", document.getTextes(3).getTexte(7).getDescriptionBrut());
        data.addData("SIGNATURE_PARTIE_2", document.getTextes(3).getTexte(10).getDescriptionBrut());
        data.addData("TEXTE_COPIES", document.getTextes(3).getTexte(11).getDescriptionBrut());

        data.addData("TITRE_PARTIE_3", document.getTextes(4).getTexte(1).getDescriptionBrut());
        data.addData("TEXTE_COMPENSATION_2", document.getTextes(4).getTexte(2).getDescriptionBrut());
        data.addData("PERIODE_DU_2", document.getTextes(4).getTexte(5).getDescriptionBrut());
        data.addData("AU", document.getTextes(3).getTexte(5).getDescriptionBrut());
        data.addData("TEXTE_PARTIE_3", document.getTextes(4).getTexte(6).getDescription().replaceAll("\\n", ""));
        data.addData("TEXTE_VERSEMENT", document.getTextes(4).getTexte(12).getDescriptionBrut());
        data.addData("SIGNATURE_PARTIE_3", document.getTextes(4).getTexte(8).getDescriptionBrut());

        data.addData("TITRE_PARTIE_4", document.getTextes(5).getTexte(1).getDescriptionBrut());
        data.addData("SIGNATURE_PARTIE_4", document.getTextes(5).getTexte(2).getDescriptionBrut());

        data.addData("TITRE_ANNEXE", document.getTextes(6).getTexte(1).getDescriptionBrut());
        data.addData("COLONNE_DETAIL_REQUERANT", document.getTextes(6).getTexte(2).getDescriptionBrut());
        data.addData("COLONNE_MONTANT_MENSUEL", document.getTextes(6).getTexte(3).getDescriptionBrut());
        data.addData("COLONNE_NB_MOIS", document.getTextes(6).getTexte(4).getDescriptionBrut());
        data.addData("COLONNE_PMT_RETRO", document.getTextes(6).getTexte(5).getDescriptionBrut());
        data.addData("TEXTE_FINAL_ANNEXE", document.getTextes(6).getTexte(6).getDescriptionBrut());

        if (!JadeStringUtil.isBlank(commentaires)) {
            data.addData("TITRE_COMMENTAIRES", document.getTextes(6).getTexte(21).getDescriptionBrut());
        }
    }

    private void remplirPages() throws Exception {

        montantTotalFinal = new FWCurrency("0");

        // adresse de l'assuré (requérant de la demande)
        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(getSession());
        demandeRente.setIdDemandeRente(creancier.getIdDemandeRente());
        demandeRente.retrieve();

        PRDemande demandePrestation = new PRDemande();
        demandePrestation.setSession(getSession());
        demandePrestation.setIdDemande(demandeRente.getIdDemandePrestation());
        demandePrestation.retrieve();

        // Si demande survivant, ne pas afficher l'adresse de l'assuré décédé
        //
        // --> Si 2 parents décédés, prendre l'adresse de l'enfant le plus jeune
        // --> Si 1 parent décédé, prendre l'adresse de l'autre parent

        String adresseAssure = "";
        String idTiersAssure = "";

        if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {

            boolean isConjoint = false;
            String idTierAAfficher = null;
            String idTierEnfantPlusJeune = null;
            JADate dateNaissance = new JADate("00.00.0000");

            JACalendar cal = new JACalendarGregorian();

            // on recherche tous les membres de la famille
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                    ISFSituationFamiliale.CS_DOMAINE_STANDARD, demandePrestation.getIdTiers());
            ISFMembreFamilleRequerant[] mbrsFam = sf.getMembresFamilleRequerant(demandePrestation.getIdTiers());

            // parcourir tous les membres de la famille
            for (int i = 0; i < mbrsFam.length; i++) {
                ISFMembreFamilleRequerant membreFamilleRequerant = mbrsFam[i];

                // si requérant = rien
                if (membreFamilleRequerant.getRelationAuRequerant().equals(
                        ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT)) {
                    continue;
                }

                // si conjoint
                if (membreFamilleRequerant.getRelationAuRequerant().equals(
                        ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT)) {

                    // si conjoint sans date décès
                    if (JadeStringUtil.isBlankOrZero(membreFamilleRequerant.getDateDeces())) {

                        if (isConjoint
                                && ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE.equalsIgnoreCase(membreFamilleRequerant
                                        .getCsEtatCivil())) {
                            idTierAAfficher = membreFamilleRequerant.getIdTiers();
                        } else if (!isConjoint) {
                            isConjoint = true;
                            idTierAAfficher = membreFamilleRequerant.getIdTiers();
                        }
                    }

                }

                // si enfant
                if (membreFamilleRequerant.getRelationAuRequerant().equals(
                        ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT)) {

                    JADate dateNaissanceEnfant = new JADate(membreFamilleRequerant.getDateNaissance());

                    // Trouver l'enfant le plus jeune
                    if (cal.compare(dateNaissanceEnfant, dateNaissance) == JACalendar.COMPARE_FIRSTUPPER) {
                        dateNaissance = dateNaissanceEnfant;
                        idTierEnfantPlusJeune = membreFamilleRequerant.getIdTiers();
                    }

                }

            }

            if (isConjoint) {
                idTiersAssure = idTierAAfficher;

            } else {
                idTiersAssure = idTierEnfantPlusJeune;
            }

        } else {

            idTiersAssure = demandePrestation.getIdTiers();
        }

        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        adresseAssure = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), idTiersAssure,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");

        // Numéro AVS de l'assuré
        PRTiersWrapper requerant = PRTiersHelper.getTiersAdresseParId(getSession(), idTiersAssure);

        if (requerant != null) {
            data.addData("detailAssure", requerant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                    + requerant.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
        }

        if (JadeStringUtil.isBlankOrZero(adresseAssure)) {
            PRTiersWrapper assure = PRTiersHelper.getTiersParId(getSession(), demandePrestation.getIdTiers());
            if (null != assure) {
                adresseAssure = assure.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + assure.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            }
        }

        data.addData("adresseAssure", adresseAssure);

        TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
        tiAdminCaisseMgr.setSession(getSession());
        tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                getSession().getApplication()));
        tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
        tiAdminCaisseMgr.find();

        TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

        String idTiersCaisse = "";
        if (tiAdminCaisse != null) {
            idTiersCaisse = tiAdminCaisse.getIdTiersAdministration();
        }

        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        data.addData("adresseCaisse", PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), idTiersCaisse,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, ""));

        // Adresse du demandeur
        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        data.addData("adresseCreancier", PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                creancier.getIdTiers(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, ""));

        // Ajoute dans l'entête de la lettre qui a traité le dossier si
        // necessaire
        if (("true").equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
            data.addData("user", getSession().getUserFullName() + "\n" + getSession().getUserInfo().getPhone());
        }

        data.addData("refPmt", creancier.getRefPaiement());

        REDemandeCompensationAdapter adapter = new REDemandeCompensationAdapter(getSession(), creancier, getMoisAnnee());
        adapter.traitement();

        // Remplir les champs de la première ligne (montantTotals)
        // Date début période
        data.addData("moisAnneeDebut", adapter.getDateDebut());
        // Date fin période
        data.addData("moisAnneeFin", adapter.getDateFin());
        // // Montant période
        // data.addData("montantRetroactifsTotal",
        // adapter.getMontantTotal().toStringFormat());

        // Lieu et date
        codeIsoLangue = codeIsoLangue.toUpperCase();
        String dateJour = JACalendar.todayJJsMMsAAAA();
        dateJour = JACalendar.format(JACalendar.format(dateJour), codeIsoLangue);

        data.addData("LieuDate", document.getTextes(3).getTexte(12).getDescription() + " " + dateJour);

        // Date de retour
        JADate dateRetour = JACalendar.today(); // date actuel + 20 jours, on
        // prend le jour ouvrable
        // suivant

        JACalendar cal = new JACalendarGregorian();
        dateRetour = cal.addDays(dateRetour, 20);

        URL url = this.getClass().getResource(REDemandeCompensationOO.FILE_HOLIDAYS_XML);
        if (url != null) {
            File f = new File(url.getFile());
            REDemandeCompensationOO.calendarWithHolidays = new JACalendarGregorian(new JAHolidays(f.getPath()));
        } else {
            REDemandeCompensationOO.calendarWithHolidays = new JACalendarGregorian();
        }

        try {
            dateRetour = REDemandeCompensationOO.calendarWithHolidays.getNextWorkingDay(dateRetour);
        } catch (JAException e) {
            throw new Exception(getSession().getLabel("ERREUR_DATE_RETOUR"));
        }

        String temp = PRStringUtils.replaceString(document.getTextes(3).getTexte(8).getDescriptionBrut(),
                "{dateRetour}", JACalendar.format(dateRetour));
        data.addData("FIN_TEXTE_PARTIE_2", temp);

        // Reprendre tous les autres créanciers
        RECreancierManager mgr = new RECreancierManager();
        mgr.setSession(getSession());
        mgr.setForIdDemandeRente(creancier.getIdDemandeRente());
        try {
            mgr.find();
        } catch (Exception e) {
            throw new Exception(getSession().getLabel("ERREUR_CREATION_COPIE"));
        }

        StringBuilder creanciers = new StringBuilder();

        for (int i = 0; i < mgr.size(); i++) {
            RECreancier crean = (RECreancier) mgr.get(i);

            if (!crean.getCsType().equals(IRECreancier.CS_IMPOT_SOURCE)) {

                if (!crean.getIdCreancier().equals(creancier.getIdCreancier())) {

                    try {

                        TITiers tier = new TITiers();
                        tier.setSession(getSession());
                        tier.setIdTiers(crean.getIdTiers());
                        tier.retrieve();

                        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                        // se trouvant dans le fichier corvus.properties
                        String adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), tier.getIdTiers(),
                                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater02(),
                                JACalendar.todayJJsMMsAAAA());

                        if (adresse.length() == 0) {
                            adresse = tier.getNomPrenom();
                        }

                        if (adresse.length() == 0) {
                            adresse = " ";
                        }

                        creanciers.append(adresse).append("\n");

                    } catch (Exception e) {
                        throw new Exception(getSession().getLabel("ERREUR_CREATION_COPIE"));
                    }
                }
            }
        }

        if (creanciers.length() == 0) {
            creanciers.append(" ");
        }

        data.addData("valeurCopies", creanciers.toString());

        Collection newTable = new Collection("tableau1");
        DataList line1 = new DataList("ligneRequerant");

        // Lancement de l'adapter
        adapter = new REDemandeCompensationAdapter(getSession(), creancier, getMoisAnnee());
        adapter.traitement();

        String dateDernierPmt = getMoisAnnee();

        // Première ligne, mettre informations du requérant
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), adapter.getIdTierRequerant());

        if (tiers != null) {

            String detailRequerant = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + "  "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

            line1.addData("detailRequerant", detailRequerant);

        } else {
            line1.addData("detailRequerant", "TIERS NON TROUVE !");
        }

        newTable.add(line1);

        // Deuxième ligne fixe, informations sur le genre de la rente

        DataList line2 = new DataList("ligneGenreDemande");

        REDemandeRente demRente = new REDemandeRente();
        demRente.setSession(getSession());
        demRente.setIdDemandeRente(getIdDemandeRente());
        demRente.retrieve();

        if (!demRente.isNew()) {

            if (demRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
                line2.addData("detailGenreDemande", document.getTextes(6).getTexte(17).getDescriptionBrut());
            } else if (demRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {
                line2.addData("detailGenreDemande", document.getTextes(6).getTexte(18).getDescriptionBrut());
            } else if (demRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {
                line2.addData("detailGenreDemande", document.getTextes(6).getTexte(19).getDescriptionBrut());
            } else if (demRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {
                line2.addData("detailGenreDemande", document.getTextes(6).getTexte(20).getDescriptionBrut());
            }
        } else {
            line2.addData("detailGenreDemande", "DEMANDE NON TROUVEE !");
        }

        newTable.add(line2);

        // BZ 5200
        // Ensuite, par période, afficher tous les noms, montants et nombre de mois
        Set<REDemandeCompensationAdapter.REKeyPeriode> set = adapter.getGroupeRenteHash().keySet();
        Set<String> idTiers = new HashSet<String>();

        for (REDemandeCompensationAdapter.REKeyPeriode keyPeriode : set) {

            /*
             * D0095 : si le montant de la section est zéro (donc si la personne a été incarcérée durant cette période,
             * on ne doit afficher ni l'en-tête de période ni la rente)
             */
            FWCurrency montantTotalPeriode = new FWCurrency();
            for (REDemandeCompensationAdapter.REGroupeRente uneRente : adapter.getGroupeRenteHash().get(keyPeriode)) {
                montantTotalPeriode.add(uneRente.montantAnnuel);
            }

            if (!montantTotalPeriode.isZero()) {
                List<REDemandeCompensationAdapter.REGroupeRente> liste = adapter.getGroupeRenteHash().get(keyPeriode);
                Collections.sort(liste);

                String dateFin = keyPeriode.dateFin;

                if (JadeStringUtil.isBlankOrZero(keyPeriode.dateFin)) {
                    dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDernierPmt);
                }

                String detailPeriode = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut) + " - "
                        + PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(dateFin);

                if (getAfficherTauxInv().booleanValue()) {
                    detailPeriode += ", taux de l'invalidité : " + keyPeriode.tauxInvalidite + "%";
                }

                line1 = new DataList("lignePeriode");
                line1.addData("periodeAssure", detailPeriode);
                newTable.add(line1);

                for (REDemandeCompensationAdapter.REGroupeRente groupeRente : liste) {
                    // Première ligne, mettre informations du requérant
                    PRTiersWrapper tiers2 = PRTiersHelper.getTiersParId(getSession(), groupeRente.idTiers);

                    if (tiers2 != null) {

                        if (!idTiers.contains(tiers2.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                            idTiers.add(tiers2.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                        }

                        String detailAssure = tiers2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + "  "
                                + tiers2.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tiers2.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

                        line1 = new DataList("ligneAssure");
                        line1.addData("detailAssure", detailAssure);

                    } else {
                        line1 = new DataList("ligneAssure");
                        line1.addData("detailAssure", "TIERS NON TROUVE !");
                    }

                    if (JadeStringUtil.isBlankOrZero(groupeRente.moisAnneeFin)) {
                        groupeRente.moisAnneeFin = adapter.getDateFin();
                    }

                    int nbMois = PRDateFormater.nbrMoisEntreDates(new JADate(groupeRente.moisAnneeDebut), new JADate(
                            groupeRente.moisAnneeFin));

                    FWCurrency valeur = new FWCurrency(groupeRente.montantAnnuel.doubleValue() * nbMois);
                    line1.addData("montantMensuel", groupeRente.montantAnnuel.toStringFormat());
                    line1.addData("nbMois", String.valueOf(nbMois));
                    line1.addData("montantTotalPeriode", valeur.toStringFormat());

                    montantTotalFinal.add(valeur);

                    newTable.add(line1);
                    line1 = new DataList("lignePeriode");

                }
            }
        }

        data.add(newTable);
        data.addData("montantTotalTab", montantTotalFinal.toStringFormat());

        // Ajout des dettes calculées sous ordres de versement (si compensé)
        String idDemandeRente = creancier.getIdDemandeRente();
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDemandeRente(idDemandeRente);
        prestMgr.find();

        List<REPrestations> listePrestations = new ArrayList<REPrestations>();

        for (int i = 0; i < prestMgr.size(); i++) {
            REPrestations prestations = (REPrestations) prestMgr.get(i);

            // si l'idTiersBeneficiaire de la décision est dans la liste du
            // haut, prendre la prestation
            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(getSession());
            decision.setIdDecision(prestations.getIdDecision());
            decision.retrieve();

            if (idTiers.contains(decision.getIdTiersBeneficiairePrincipal())) {
                listePrestations.add(prestations);
            }

        }

        List<REOrdresVersements> listeOV = new ArrayList<REOrdresVersements>();

        for (REPrestations prestations : listePrestations) {

            // Pour chaque prestation désirée, on charge les ordres de versements
            REOrdresVersementsManager ovMgr = new REOrdresVersementsManager();
            ovMgr.setSession(getSession());
            ovMgr.setForIdPrestation(prestations.getIdPrestation());
            ovMgr.find();

            for (int i = 0; i < ovMgr.size(); i++) {
                listeOV.add((REOrdresVersements) ovMgr.get(i));
            }
        }

        FWCurrency montantRenteDejaVersees = new FWCurrency();
        FWCurrency montantCreances = new FWCurrency();
        FWCurrency impotSource = new FWCurrency();

        for (REOrdresVersements ov : listeOV) {

            if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE)) {

                if (ov.getIsCompense().booleanValue()) {
                    montantRenteDejaVersees.add(ov.getMontantDette());
                }

            } else if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_AVANCES)
                    || ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_DECISION)
                    || ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_PRST_BLOQUE)
                    || ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION)
                    || ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RETOUR)) {

                if (ov.getIsCompense().booleanValue()) {
                    montantCreances.add(ov.getMontantDette());
                }

            } else if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_IMPOT_SOURCE)) {
                impotSource.add(ov.getMontantDette());
            }

        }

        data.addData("LIGNE_RENTES_DEJA_VERSEES", getTexte1());
        if (JadeStringUtil.isBlankOrZero(getMontant1())) {
            data.addData("montantRentesDejaVersees", montantRenteDejaVersees.toStringFormat());
            montantTotalFinal.sub(montantRenteDejaVersees);
        } else {
            data.addData("montantRentesDejaVersees", (new FWCurrency(getMontant1()).toStringFormat()));
            montantTotalFinal.sub((new FWCurrency(getMontant1())));
        }

        data.addData("LIGNE_SOUS_TOTAL", document.getTextes(6).getTexte(10).getDescriptionBrut());
        data.addData("montantSousTotal", montantTotalFinal.toStringFormat());

        data.addData("LIGNE_CREANCES", getTexte2());
        if (JadeStringUtil.isBlankOrZero(getMontant2())) {
            data.addData("montantCreances", montantCreances.toStringFormat());
            montantTotalFinal.sub(montantCreances);
        } else {
            data.addData("montantCreances", (new FWCurrency(getMontant2()).toStringFormat()));
            montantTotalFinal.sub((new FWCurrency(getMontant2())));
        }

        if ((new FWCurrency(getMontant3()).isPositive())) {
            data.addData("LIGNE_IMPOT_SOURCE", getTexte3());
            if (JadeStringUtil.isBlankOrZero(getMontant3())) {
                data.addData("montantImpotSource", impotSource.toStringFormat());
                montantTotalFinal.sub(impotSource);
            } else {
                data.addData("montantImpotSource", (new FWCurrency(getMontant3()).toStringFormat()));
                montantTotalFinal.sub((new FWCurrency(getMontant3())));
            }

        }

        if (!JadeStringUtil.isBlankOrZero(getTexte4()) && (new FWCurrency(getMontant4()).isPositive())) {
            data.addData("LIGNE_SUPP", getTexte4());
            data.addData("montantSupp", (new FWCurrency(getMontant4()).toStringFormat()));

            montantTotalFinal.sub((new FWCurrency(getMontant4())));

        }

        data.addData("LIGNE_TOTAL_FINAL", document.getTextes(6).getTexte(12).getDescriptionBrut());
        data.addData("montantTotalFinal", montantTotalFinal.toStringFormat());

        // Montant période (2ème page du document)
        data.addData("montantRetroactifsTotal", montantTotalFinal.toStringFormat());

        data.addData("commentaires", commentaires);
    }

    @Override
    public void run() {
        try {

            // Load du créancier de base
            creancier.setSession(getSession());
            creancier.setIdCreancier(getIdCreancier());
            creancier.retrieve(getTransaction());

            JadePublishDocumentInfo pubInfosGen = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosGen.setOwnerEmail(getEmailAdresse());
            pubInfosGen.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
            pubInfosGen.setDocumentTitle(getSession().getLabel("TITRE_PROCESS"));
            pubInfosGen.setDocumentSubject(getSession().getLabel("TITRE_PROCESS"));
            pubInfosGen.setArchiveDocument(false);
            pubInfosGen.setPublishDocument(true);

            REDemandeRente demRente = new REDemandeRente();
            demRente.setSession(getSession());
            demRente.setIdDemandeRente(getIdDemandeRente());
            demRente.retrieve();

            PRDemande demandePrest = new PRDemande();
            demandePrest.setSession(getSession());
            demandePrest.setIdDemande(demRente.getIdDemandePrestation());
            demandePrest.retrieve();

            TIDocumentInfoHelper.fill(pubInfosGen, demandePrest.getIdTiers(), getSession(), null, null, null);

            JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
            allDoc.setMergedDocDestination(pubInfosGen);

            if (!isImprimerTous.booleanValue()) {

                data = new DocumentData();

                JadePublishDocumentInfo pubInfosLettreEnTete = JadePublishDocumentInfoProvider.newInstance(this);
                pubInfosLettreEnTete.setOwnerEmail(getEmailAdresse());
                pubInfosLettreEnTete.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
                pubInfosLettreEnTete.setDocumentTitle(getSession().getLabel("TITRE_PROCESS"));
                pubInfosLettreEnTete.setDocumentSubject(getSession().getLabel("TITRE_PROCESS"));
                pubInfosLettreEnTete.setArchiveDocument(false);
                pubInfosLettreEnTete.setPublishDocument(false);
                pubInfosLettreEnTete.setDocumentType(IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);
                pubInfosLettreEnTete.setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);
                pubInfosLettreEnTete.setDocumentDate(JACalendar.todayJJsMMsAAAA());

                TIDocumentInfoHelper.fill(pubInfosLettreEnTete, demandePrest.getIdTiers(), getSession(), null, null,
                        null);

                allDoc.addDocument(createLettreEnTete(creancier.getIdTiers()), pubInfosLettreEnTete);

                data = new DocumentData();

                data.addData("idProcess", "REDemandeCompensationOO");

                // retrieve du tiers
                PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), demandePrest.getIdTiers());

                if (null == tier) {
                    tier = PRTiersHelper.getAdministrationParId(getSession(), demandePrest.getIdTiers());
                }

                codeIsoLangue = getSession().getCode(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

                chargementCatalogueTexte();
                remplirChampsStatiques();
                remplirPages();

                JadePublishDocumentInfo pubInfosDemComp = JadePublishDocumentInfoProvider.newInstance(this);
                pubInfosDemComp.setOwnerEmail(getEmailAdresse());
                pubInfosDemComp.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
                pubInfosDemComp.setDocumentTitle(getSession().getLabel("TITRE_PROCESS"));
                pubInfosDemComp.setDocumentSubject(getSession().getLabel("TITRE_PROCESS"));
                pubInfosDemComp.setPublishDocument(false);
                pubInfosDemComp.setDocumentType(IRENoDocumentInfoRom.DEMANDE_DE_COMPENSATION);
                pubInfosDemComp.setDocumentTypeNumber(IRENoDocumentInfoRom.DEMANDE_DE_COMPENSATION);
                pubInfosDemComp.setDocumentDate(JACalendar.todayJJsMMsAAAA());
                pubInfosDemComp.setDocumentProperty(
                        REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                        REGedUtils.getCleGedPourTypeRente(getSession(),
                                REGedUtils.getTypeRentePourCetteDemandeRente(getSession(), demRente)));

                // GED ?
                pubInfosDemComp.setArchiveDocument(isSendToGed.booleanValue());

                try {
                    if (getIsSendToGed().booleanValue()) {
                        // bz-5941
                        PRGedHelper h = new PRGedHelper();
                        // Traitement uniquement pour la caisse concernée (CCB)
                        if (h.isExtraNSS(getSession())) {

                            pubInfosDemComp = h.setNssExtraFolderToDocInfo(getSession(), pubInfosDemComp,
                                    demandePrest.getIdTiers());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                TIDocumentInfoHelper.fill(pubInfosDemComp, demandePrest.getIdTiers(), getSession(), null, null, null);

                allDoc.addDocument(data, pubInfosDemComp);

                this.createDocuments(allDoc);

            } else {

                if (creancier.isNew()) {
                    RECreancierManager creMgr = new RECreancierManager();
                    creMgr.setSession(getSession());
                    creMgr.setForIdDemandeRente(getIdDemandeRente());
                    creMgr.find();

                    creancier = (RECreancier) creMgr.getFirstEntity();
                }

                RECreancierManager mgr = new RECreancierManager();
                mgr.setSession(getSession());
                mgr.setForIdDemandeRente(creancier.getIdDemandeRente());
                mgr.find();

                for (int i = 0; i < mgr.size(); i++) {
                    RECreancier crean = (RECreancier) mgr.get(i);

                    if (!crean.getCsType().equals(IRECreancier.CS_IMPOT_SOURCE)) {
                        creancier = crean;

                        data = new DocumentData();

                        JadePublishDocumentInfo pubInfosLettreEnTete = JadePublishDocumentInfoProvider
                                .newInstance(this);
                        pubInfosLettreEnTete.setOwnerEmail(getEmailAdresse());
                        pubInfosLettreEnTete.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
                        pubInfosLettreEnTete.setDocumentTitle(getSession().getLabel("TITRE_PROCESS"));
                        pubInfosLettreEnTete.setDocumentSubject(getSession().getLabel("TITRE_PROCESS"));
                        pubInfosLettreEnTete.setArchiveDocument(false);
                        pubInfosLettreEnTete.setPublishDocument(false);
                        pubInfosLettreEnTete
                                .setDocumentType(IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);
                        pubInfosLettreEnTete
                                .setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);
                        pubInfosLettreEnTete.setDocumentDate(JACalendar.todayJJsMMsAAAA());

                        TIDocumentInfoHelper.fill(pubInfosLettreEnTete, demandePrest.getIdTiers(), getSession(), null,
                                null, null);

                        allDoc.addDocument(createLettreEnTete(creancier.getIdTiers()), pubInfosLettreEnTete);

                        data = new DocumentData();

                        data.addData("idProcess", "REDemandeCompensationOO");

                        REDemandeRente demandeRente = new REDemandeRente();
                        demandeRente.setSession(getSession());
                        demandeRente.setIdDemandeRente(creancier.getIdDemandeRente());
                        demandeRente.retrieve();

                        PRDemande demandePrestation = new PRDemande();
                        demandePrestation.setSession(getSession());
                        demandePrestation.setIdDemande(demandeRente.getIdDemandePrestation());
                        demandePrestation.retrieve();

                        // retrieve du tiers
                        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(),
                                demandePrestation.getIdTiers());

                        if (null == tier) {
                            tier = PRTiersHelper.getAdministrationParId(getSession(), demandePrestation.getIdTiers());
                        }

                        codeIsoLangue = getSession().getCode(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

                        chargementCatalogueTexte();
                        remplirChampsStatiques();
                        remplirPages();

                        JadePublishDocumentInfo pubInfosDemComp = JadePublishDocumentInfoProvider.newInstance(this);
                        pubInfosDemComp.setOwnerEmail(getEmailAdresse());
                        pubInfosDemComp.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
                        pubInfosDemComp.setDocumentTitle(getSession().getLabel("TITRE_PROCESS"));
                        pubInfosDemComp.setDocumentSubject(getSession().getLabel("TITRE_PROCESS"));
                        pubInfosDemComp.setPublishDocument(false);
                        pubInfosDemComp.setDocumentType(IRENoDocumentInfoRom.DEMANDE_DE_COMPENSATION);
                        pubInfosDemComp.setDocumentTypeNumber(IRENoDocumentInfoRom.DEMANDE_DE_COMPENSATION);
                        pubInfosDemComp.setDocumentDate(JACalendar.todayJJsMMsAAAA());

                        // GED ?
                        pubInfosDemComp.setArchiveDocument(isSendToGed.booleanValue());
                        try {
                            if (getIsSendToGed().booleanValue()) {
                                // bz-5941
                                PRGedHelper h = new PRGedHelper();
                                // Traitement uniquement pour la caisse concernée (CCB)
                                if (h.isExtraNSS(getSession())) {

                                    pubInfosDemComp = h.setNssExtraFolderToDocInfo(getSession(), pubInfosDemComp,
                                            demandePrest.getIdTiers());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        TIDocumentInfoHelper.fill(pubInfosDemComp, demandePrest.getIdTiers(), getSession(), null, null,
                                null);

                        allDoc.addDocument(data, pubInfosDemComp);
                    }

                }

            }

            this.createDocuments(allDoc);

        } catch (Exception e) {
            e.printStackTrace();
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, "REDemandeCompensationOO", e.toString()));
        } finally {
            try {
                if (getLogSession().hasMessages()) {
                    sendCompletionMail(new ArrayList<String>() {
                        /**
                         * 
                         */
                        private static final long serialVersionUID = 1L;

                        {
                            this.add(REDemandeCompensationOO.this.getEmailAdresse());
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println(getSession().getLabel("ERREUR_ENVOI_MAIL_COMPLETION"));
            }
        }

    }

    public void setAfficherTauxInv(Boolean afficherTauxInv) {
        this.afficherTauxInv = afficherTauxInv;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public void setIdCreancier(String idCreancier) {
        this.idCreancier = idCreancier;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIsImprimerTous(Boolean isImprimerTous) {
        this.isImprimerTous = isImprimerTous;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setMontant1(String montant1) {
        this.montant1 = montant1;
    }

    public void setMontant2(String montant2) {
        this.montant2 = montant2;
    }

    public void setMontant3(String montant3) {
        this.montant3 = montant3;
    }

    public void setMontant4(String montant4) {
        this.montant4 = montant4;
    }

    public void setTexte1(String texte1) {
        this.texte1 = texte1;
    }

    public void setTexte2(String texte2) {
        this.texte2 = texte2;
    }

    public void setTexte3(String texte3) {
        this.texte3 = texte3;
    }

    public void setTexte4(String texte4) {
        this.texte4 = texte4;
    }
}