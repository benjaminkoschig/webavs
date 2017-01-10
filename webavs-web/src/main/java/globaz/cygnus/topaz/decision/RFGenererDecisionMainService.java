package globaz.cygnus.topaz.decision;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.cygnus.RFTypeDecisionEnum;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.api.decisions.IRFCodesIsoLangue;
import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.api.decisions.IRFGenererDocumentDecision;
import globaz.cygnus.api.genrePC.IRFGenrePc;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.api.prestationsaccordees.IRFGenrePrestations;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.services.genererDecision.RFGenererDecisionRestitutionService;
import globaz.cygnus.services.genererDecision.RFGenererDecisionService;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.services.validerDecision.RFDemandeValidationData;
import globaz.cygnus.services.validerDecision.RFMotifRefusDemandeValidationData;
import globaz.cygnus.topaz.RFAbstractDocumentOO;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.utils.ged.PRGedUtils;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TITiers;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.cygnus.business.constantes.ERFProperties;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * author fha
 */
public class RFGenererDecisionMainService extends RFAbstractDocumentOO implements IRFDecisions {

    public static final String FICHIER_MODELE_DOCUMENT_DECISION_RFM = "RF_DOCUMENT_DECISION";

    private static final int NOM_PRENOM_SIZE = 20;
    private String adresse = "";
    private String adressePaiement = "";
    private Boolean ajoutSignature = Boolean.FALSE;
    private String anneeFacture = "";
    private String anneeQD = "";
    private String copie = "COPIE";
    protected final String DATE_FIN_PAIEMENT = "{dateFinPaiement}";
    private String dateDecision = "";
    private String dateDemande = "";
    private String dateNaissance = "";
    private String datesReception = "";
    private RFDecisionDocumentData decisionDocument;
    private Boolean decisionMensuelleRetro = Boolean.FALSE;
    private Boolean decisionMensuelleUnMois = Boolean.FALSE;
    RFTypeDecisionEnum decisionToPrint;
    private JadePublishDocumentInfo docInfo = null;
    private boolean documentRestitutionEnErreur = Boolean.FALSE;
    private String formatteMoisRetroFinMoinsUn = "";
    private NumberFormat formatter = new DecimalFormat("###,##0.00");
    private boolean isMontantARestituer = Boolean.FALSE;
    private ICTDocument mainDocument;
    private FWMemoryLog memoryLog;
    public String messageOv = "";
    private String moisFuture = "";
    private String moisRetroDebut = "";
    private String moisRetroFinMoinsUn = null;
    protected final String MONTANT_PAIEMENT_MENSUEL = "{montantPaiementMensuel}";
    FWCurrency montantARestituerDecisionRestitution = null;
    private FWCurrency montantCourant;
    private String montantDiminutionQD = "";
    private String montantExcedentRevenu = "";
    private String montantFuture = "";
    private String montantMensuel = "";
    private String montantRemboursement = "";
    private String montantTotal = "";
    private BigDecimal montantTotalARembourser = null;
    private BigDecimal montantTotalDu = null;
    private BigDecimal montantTotalVersee = null;
    private int nbMois = 0;
    private int nbMoisTotal = 0;
    private String nomAdressePaiement = "";
    private String nomGestionnaire = "";
    private String npa = "";
    private String nss = "";
    private String numeroDecision = "";
    private StringBuffer pdfDecisionURL = null;
    private String prenomAdressePaiement = "";
    private RFGenererDecisionRestitutionService rfGenererDecisionRestitutionService = null;
    private BSession sessionCygnus;
    private String sommeMontantRetro = "";
    private String sousTypeRegime = "";
    private String telGestionnaire = "";
    private String telGestionnaireGrpLettre = "";
    private String texteLibre = "";
    private String titre = "";
    private String typePaiement = "";
    private String ville = "";
    private RFGenererDecisionService rfGenererDecisionService = null;

    public RFGenererDecisionMainService() {
        // TODO : voir si c'est ok de l'appeler comme ça.
        this(false);
    }

    public RFGenererDecisionMainService(Boolean isCopie) {
        super();
        rfGenererDecisionService = new RFGenererDecisionService();
        this.isCopie = isCopie;
    }

    private void addErreurMail(FWMemoryLog memoryLog, String message, String source) {
        memoryLog.logMessage(message, new Integer(JadeBusinessMessageLevels.ERROR).toString(), source);
    }

    /**
     * Methode pour afficher en bas de page, les annexes et/ou copies de décisions ponctuelles et mensuelles
     * 
     * @param afficheAnnexes
     * @throws Exception
     */
    protected void ajoutAnnexesEtCopies(boolean isDecisionPonctuelle, boolean isDecisionMensuelle,
            boolean isDecisionRestitution, boolean afficheAnnexes, boolean afficheCopies) throws Exception {
        try {

            // Si l'un des paramètres est à 'true'
            if ((afficheAnnexes && hasAnnexes()) || (afficheCopies && !decisionDocument.getCopieDecision().isEmpty())) {

                // Initialisation du nom de tableau
                Collection tabAnnexesEtCopies = new Collection(
                        IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_ANNEXES_ET_COPIES);
                /*
                 * Déclaration de variables
                 */
                boolean firstAnnexe = true;
                // Déclaration d'une variable pour y concaténer les textes des cases à cocher
                String caseBox = null;
                // Déclaration d'une variable pour y insérer les textes saisie
                String textBox = decisionDocument.getTexteAnnexe();
                String adresse = null;
                boolean firstCopie = true;

                /*
                 * Appel du tableau corespondant
                 */
                // Insertion du tableau des annexes et copies
                data.addData("isTableauAnnexesEtCopiesInclude", "STANDARD");

                /*
                 * Remplissage du tableau selon type de décision
                 */
                // Si le document provient d'une décision ponctuelle
                if (isDecisionPonctuelle) {

                    // Si concerne les annexes de décision ponctuelle
                    if (afficheAnnexes && hasAnnexes()) {

                        // Déclaration de la ligne du tableau
                        DataList ligneAnnexe = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_DATA);
                        // Ajout de la ligne à la collection
                        tabAnnexesEtCopies.add(ligneAnnexe);

                        // Insertion du titre : Annexe, dans la ligne
                        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE, mainDocument.getTextes(10)
                                .getTexte(1).getDescription());

                        // Si coché, chargement du texte lié à la case cochée de l'écran dans la variable caseBox
                        if (decisionDocument.getIsBordereauAccompagnement()) {
                            caseBox = mainDocument.getTextes(10).getTexte(3).getDescription();
                            firstAnnexe = false;
                        }

                        // Si coché, chargement du texte lié à la case cochée de l'écran dans la variable caseBox
                        if (decisionDocument.getIsBulletinVersement()) {
                            if (firstAnnexe) {
                                caseBox = mainDocument.getTextes(10).getTexte(4).getDescription();
                                firstAnnexe = false;
                            } else {
                                caseBox = caseBox + ", " + mainDocument.getTextes(10).getTexte(4).getDescription();
                            }
                        }

                        // Si coché, chargement du texte lié à la case cochée de l'écran dans la variable caseBox
                        if (decisionDocument.getIsDecompteRetour()) {
                            if (firstAnnexe) {
                                caseBox = mainDocument.getTextes(10).getTexte(5).getDescription();
                                firstAnnexe = false;
                            } else {
                                caseBox = caseBox + ", " + mainDocument.getTextes(10).getTexte(5).getDescription();
                            }
                        }

                        // Ajout dans la première ligne des annexes, la variable caseBox si celle-ci est renseignée
                        if (!JadeStringUtil.isEmpty(caseBox)) {
                            ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, caseBox);
                        }

                        // Ajout du texte remarques si celui-ci est renseigné
                        if (!JadeStringUtil.isEmpty(textBox)) {

                            // Vérification que le texte saisie à la main se termine par un "."
                            if (!textBox.endsWith(".")) {
                                textBox = textBox + ".";
                            }

                            // Si la première ligne des annexes n'est pas utilisé par les cases à cocher, on l'utilise
                            // pour le texte libre
                            if (JadeStringUtil.isEmpty(caseBox)) {

                                // Renseignement de la ligne par le texte libre
                                ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, textBox);

                            } else {
                                // Sinon on déclare la deuxième ligne pour y insérer le texte libre
                                DataList ligneAnnexeTexteLibre = new DataList(
                                        IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_TEXTE_DATA);

                                // Insertion de la deuxième ligne dans le tableau
                                tabAnnexesEtCopies.add(ligneAnnexeTexteLibre);

                                // Renseignement de la deuxième ligne par le texte saisie manuellement
                                ligneAnnexeTexteLibre.addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_ANNEXE, textBox);
                            }
                        }
                    }

                    // Si concerne les copies de décision ponctuelle
                    if (afficheCopies && !decisionDocument.getCopieDecision().isEmpty()) {

                        // Déclaration de la ligne du tableau
                        DataList ligneCopieTitre = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_COPIE_DATA);

                        // Ajout de la ligne à la collection
                        tabAnnexesEtCopies.add(ligneCopieTitre);

                        // Insertion du titre : copie(s)
                        ligneCopieTitre.addData(IRFGenererDocumentDecision.CAT_TEXTE_COPIE, mainDocument.getTextes(10)
                                .getTexte(2).getDescription());

                        // Parcours de chaque copie
                        for (RFCopieDecisionsValidationData copie : decisionDocument.getCopieDecision()) {

                            // Récupération de chaque adresse
                            adresse = PRTiersHelper.getAdresseCourrierFormatee(getSessionCygnus(),
                                    copie.getIdDestinataire(), "",
                                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).replaceAll("\n", ", ");

                            if (!JadeStringUtil.isEmpty(adresse)) {

                                // Modification du dernier caratère par "."
                                adresse = adresse.substring(0, adresse.length() - 2) + ".";

                                // Insertion de la première adresse sur la première ligne des copies, si celle-ci n'est
                                // pas utilisée
                                if (firstCopie) {

                                    // Insertion de l'adresse sur la première ligne des copies
                                    ligneCopieTitre.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_COPIE, adresse);

                                    // Changement de l'état pour que les adresses suivantes soient insérées sur la
                                    // deuxième ligne
                                    firstCopie = false;

                                    // Réinitialisation de la variable adresse
                                    adresse = null;
                                } else {
                                    // Sinon on déclare la deuxième ligne des copies pour y insérer les adresses
                                    // suivantes
                                    DataList ligneCopieSuivante = new DataList(
                                            IRFGenererDocumentDecision.CAT_TEXTE_COPIE_LIGNE_DATA);

                                    // Ajout de la deuxième ligne des copies dans la collection
                                    tabAnnexesEtCopies.add(ligneCopieSuivante);

                                    // Insertion de l'adresse sur la deuxième ligne
                                    ligneCopieSuivante.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_COPIE, "");
                                    ligneCopieSuivante
                                            .addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_COPIES, adresse);
                                }
                            }
                        }
                    }
                }

                // Si le document provient d'une décision de restitution
                if (isDecisionRestitution) {

                    // Insertion du titre annexe
                    DataList ligneAnnexe = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_DATA);
                    // Insertion du titre : annexe
                    ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE, mainDocument.getTextes(4)
                            .getTexte(5).getDescription());

                    // Insertion du texte annexe
                    if (isMontantARestituer) {
                        // Insertion du texte "-ment."
                        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, mainDocument
                                .getTextes(4).getTexte(6).getDescription());

                        // Ajout de la ligne à la collection
                        tabAnnexesEtCopies.add(ligneAnnexe);

                        DataList annexeSecondeLigne = new DataList(
                                IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_TEXTE_DATA);

                        // Insertion du texte "Bulletin de versement"
                        annexeSecondeLigne.addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_ANNEXE, mainDocument
                                .getTextes(4).getTexte(10).getDescription());

                        tabAnnexesEtCopies.add(annexeSecondeLigne);

                    } else {
                        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, mainDocument
                                .getTextes(4).getTexte(6).getDescription());

                        // Ajout de la ligne à la collection
                        tabAnnexesEtCopies.add(ligneAnnexe);
                    }

                    if (decisionDocument.getCopieDecision().size() > 0) {
                        // Déclaration de la ligne du tableau
                        DataList ligneCopieTitre = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_COPIE_DATA);

                        boolean isTitreCopie = false;

                        // Parcours de chaque copie
                        for (RFCopieDecisionsValidationData copie : decisionDocument.getCopieDecision()) {

                            // Récupération de chaque adresse
                            adresse = PRTiersHelper.getAdresseCourrierFormatee(getSessionCygnus(),
                                    copie.getIdDestinataire(), "",
                                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).replaceAll("\n", ", ");

                            if (!JadeStringUtil.isEmpty(adresse)) {

                                // Modification du dernier caratère par "."
                                adresse = adresse.substring(0, adresse.length() - 2) + ".";

                                isTitreCopie = true;

                                // Insertion de la première adresse sur la première ligne des copies, si celle-ci n'est
                                // pas utilisée
                                if (firstCopie) {

                                    // Insertion de l'adresse sur la première ligne des copies
                                    ligneCopieTitre.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_COPIE, adresse);

                                    // Changement de l'état pour que les adresses suivantes soient insérées sur la
                                    // deuxième ligne
                                    firstCopie = false;

                                    // Réinitialisation de la variable adresse
                                    adresse = null;
                                } else {
                                    // Sinon on déclare la deuxième ligne des copies pour y insérer les adresses
                                    // suivantes
                                    DataList ligneCopieSuivante = new DataList(
                                            IRFGenererDocumentDecision.CAT_TEXTE_COPIE_LIGNE_DATA);

                                    // Insertion de l'adresse sur la deuxième ligne
                                    ligneCopieSuivante.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_COPIE, "");
                                    ligneCopieSuivante
                                            .addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_COPIES, adresse);

                                    // Ajout de la deuxième ligne des copies dans la collection
                                    tabAnnexesEtCopies.add(ligneCopieSuivante);
                                }
                            }
                        }
                        if (isTitreCopie) {
                            // Insertion du titre : copie(s)
                            ligneCopieTitre.addData(IRFGenererDocumentDecision.CAT_TEXTE_COPIE,
                                    mainDocument.getTextes(4).getTexte(7).getDescription());
                        }

                        // Ajout de la ligne à la collection
                        tabAnnexesEtCopies.add(ligneCopieTitre);
                    }
                }

                // Si le document provient d'une décision mensuelle (régime)
                if (isDecisionMensuelle) {

                    // Si concerne les annexes de décision mensuelle
                    if (afficheAnnexes && hasAnnexes()) {
                        // Déclaration de la ligne du tableau
                        DataList ligneAnnexe = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_DATA);
                        // Ajout de la ligne à la collection
                        tabAnnexesEtCopies.add(ligneAnnexe);

                        // Insertion du titre
                        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE, mainDocument.getTextes(11)
                                .getTexte(1).getDescription());

                        // Renseignement de la variable caseBox avec le texte spécifique au type de décision mensuelle
                        // (régime)
                        caseBox = mainDocument.getTextes(11).getTexte(2).getDescription();

                        // Enlève le '.' en fin de texte si il y en a un
                        if (caseBox.endsWith(".")) {
                            caseBox = caseBox.substring(0, adresse.length() - 2) + "";
                        }

                        // Mise à l'état 'false' pour que la variable caseBox se concatène avec les autres textes
                        firstAnnexe = false;

                        // Si coché, chargement du texte lié à la case cochée de l'écran dans la variable caseBox
                        if (decisionDocument.getIsBordereauAccompagnement()) {
                            if (firstAnnexe) {
                                caseBox = mainDocument.getTextes(14).getTexte(1).getDescription();
                            } else {
                                caseBox = caseBox + ", " + mainDocument.getTextes(14).getTexte(1).getDescription();
                            }
                        }

                        // Si coché, chargement du texte lié à la case cochée de l'écran dans la variable caseBox
                        if (decisionDocument.getIsBulletinVersement()) {
                            if (firstAnnexe) {
                                caseBox = mainDocument.getTextes(14).getTexte(2).getDescription();
                            } else {
                                caseBox = caseBox + ", " + mainDocument.getTextes(14).getTexte(2).getDescription();
                            }
                        }

                        // Si coché, chargement du texte lié à la case cochée de l'écran dans la variable caseBox
                        if (decisionDocument.getIsDecompteRetour()) {
                            if (firstAnnexe) {
                                caseBox = mainDocument.getTextes(14).getTexte(3).getDescription();
                            } else {
                                caseBox = caseBox + ", " + mainDocument.getTextes(14).getTexte(3).getDescription();
                            }
                        }

                        // Insertion des cases coché ou du texte saisie pour la première ligne, et ajout d'un "."
                        if (!JadeStringUtil.isEmpty(caseBox)) {
                            ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, caseBox + ".");
                        } else {
                            ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, textBox);
                        }

                        // Si présent, chargement du texte saisie manuellement dans l'écran
                        if (!JadeStringUtil.isEmpty(decisionDocument.getTexteRemarque())) {
                            DataList ligneTexteLibreAnnexe = new DataList(
                                    IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_TEXTE_DATA);
                            // Vérification que le texte saisie à la main se termine par un "."
                            if (!textBox.endsWith(".")) {
                                textBox = textBox + ".";
                            }

                            // Si ce n'est pas la première annexe, on ajoute le texte sur la deuxieme ligne.
                            if (!firstAnnexe) {
                                tabAnnexesEtCopies.add(ligneTexteLibreAnnexe);
                                // Ajout de la ligne dans la DataList
                                ligneTexteLibreAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_ANNEXE, textBox);
                            } else {
                                // Sinon on met le texte saisie à la mains sur la première ligne des annexes
                                ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, textBox);
                            }

                        }

                    } else {
                        // Si aucunes annexes spécifiés, on affiche quand même le texte :
                        // "Documents originaux en retour"

                        // Déclaration de la ligne du tableau
                        DataList ligneAnnexe = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE_DATA);

                        // Ajout de la ligne à la collection
                        tabAnnexesEtCopies.add(ligneAnnexe);

                        // Insertion du titre
                        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_ANNEXE, mainDocument.getTextes(11)
                                .getTexte(1).getDescription());

                        // Renseignement de la variable caseBox avec le texte spécifique au type de décision mensuelle
                        // (régime)
                        caseBox = mainDocument.getTextes(11).getTexte(2).getDescription();

                        // Insertion du texte dans la ligne
                        ligneAnnexe.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_ANNEXE, caseBox + ".");
                    }

                    // Si concerne les copies de décision mensuelle
                    if (afficheCopies && !decisionDocument.getCopieDecision().isEmpty()) {

                        // Déclaration de la ligne du tableau
                        DataList ligneCopieTitre = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_COPIE_DATA);
                        // Insertion de la ligne dans la collection
                        tabAnnexesEtCopies.add(ligneCopieTitre);

                        // Insertion du titre Copie(s):
                        ligneCopieTitre.addData(IRFGenererDocumentDecision.CAT_TEXTE_COPIE, mainDocument.getTextes(11)
                                .getTexte(3).getDescription());

                        // Insertion du texte à la première ligne des copies
                        boolean firstCopieUtilise = false;
                        if (!RFPropertiesUtils.utiliserDecisionAvecExcedantRevenu()) {
                            ligneCopieTitre.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_COPIE, mainDocument
                                    .getTextes(11).getTexte(4).getDescription());
                            firstCopieUtilise = true;
                        }

                        // Parcours de chaque tiers recevant une copie
                        for (RFCopieDecisionsValidationData copie : decisionDocument.getCopieDecision()) {
                            this.adresse = PRTiersHelper.getAdresseDomicileFormatee(getSessionCygnus(),
                                    copie.getIdDestinataire()).replaceAll("\n", ", ");
                            // Insertion de chaque tiers en copie
                            if (!JadeStringUtil.isBlankOrZero(this.adresse)) {

                                // ligneCopie.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_COPIE, "");
                                if (firstCopieUtilise) {

                                    DataList ligneCopie = new DataList(
                                            IRFGenererDocumentDecision.CAT_TEXTE_COPIE_LIGNE_DATA);

                                    ligneCopie.addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_COPIES, this.adresse);

                                    tabAnnexesEtCopies.add(ligneCopie);
                                } else {
                                    ligneCopieTitre.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIRST_COPIE,
                                            this.adresse);
                                    firstCopieUtilise = true;
                                }
                            }
                        }
                    }
                }
                // Retourne la collection
                data.add(tabAnnexesEtCopies);
            } else {
                // Si aucuns paramêtres à 'true', pas d'insertion de tableau
                data.addData("isTableauAnnexesEtCopiesInclude", "NONE");
            }
        } catch (Exception e) {
            addErreurMail(memoryLog, e.getMessage(), "RFGenererDecisionMainService:ajouterAnnexesEtCopies");
            throw new Exception(e.toString());
        }
    }

    private void ajoutApresTableau(RFCopieDecisionsValidationData... copie) throws Exception {

        // figure seulement sur l'original, pas pour les copies
        if ((copie.length == 0) || copie[0].getIsVersement()) {

            // Affiche la personne à pour qui le versement aura lieu si montant différent de 0.-
            if (!JadeStringUtil.isBlankOrZero(montantTotalARembourser.toString())) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE_VERSEMENT, PRStringUtils.replaceString(
                        PRStringUtils.replaceString(mainDocument.getTextes(9).getTexte(1).getDescription(),
                                IRFGenererDocumentDecision.TIERS_NOM, nomAdressePaiement),
                        IRFGenererDocumentDecision.TIERS_PRENOM, prenomAdressePaiement));
            }

            // Affichage du solde d'excedent de de revenu si différent de 0
            if (!JadeStringUtil.isBlankOrZero(decisionDocument.getMontantSoldeExcedantRevenu())) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_SOLDE_EXCEDENT_REVENU, PRStringUtils.replaceString(
                        mainDocument.getTextes(9).getTexte(3).getDescription(),
                        IRFGenererDocumentDecision.SOLDE_EXCEDENT_REVENU,
                        formatter.format(Double.valueOf(decisionDocument.getMontantSoldeExcedantRevenu()))));
            }

            // Affichage d'une remarque si avances SAS (13.6)
            if (hasRemarqueAvancesSas()) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE_AVANCES_SAS, mainDocument.getTextes(9)
                        .getTexte(4).getDescription());
            }
        }

        // Affichage du texte lié au dépassement de QD pris en charge par le DSAS, si properties à TRUE et montantDsas >
        // 0.-
        if ((RFPropertiesUtils.affichageTextesLiesAuxRemboursmentsParDsas())
                && (!JadeStringUtil.isBlankOrZero(decisionDocument.getMontantARembourserParLeDsas()))) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ASTERISQUE_DEPASSEMENT_QD, PRStringUtils.replaceString(
                    mainDocument.getTextes(9).getTexte(2).getDescription(),
                    IRFGenererDocumentDecision.CAT_TEXTE_ASTERISQUE_MONTANT_A_REMBOURSER_DSAS,
                    new FWCurrency(formatter.format(Double.valueOf(decisionDocument.getMontantARembourserParLeDsas())))
                            .toStringFormat()));
        }

        // Spécifique à l'agence de Lausanne
        // Insertion de remarques en cas de demandes refusées, pour que l'assuré puissent demander un remboursement
        // auprès d'un autre organisme.
        if (ERFProperties.GESTION_TEXTE_REDIRECTION.getBooleanValue()) {
            chargerTextesRecoursSiDemandeRefusee(data);
        }
    }

    // Ajouter les copies des annexes
    private void ajoutCopiesAnnexes() throws Exception {

        StringBuffer strAnnexesCopiesTitreBfr = new StringBuffer();
        StringBuffer strAnnexesCopiesValeurBfr = new StringBuffer();
        StringBuffer strDeuxPointsBfr = new StringBuffer();

        // ajout des copies
        if ((null != copies) && copies.hasNext()) {
            // Ajout du libelle "copie à:"
            strAnnexesCopiesTitreBfr.append(mainDocument.getTextes(12).getTexte(1).getDescription());
            strDeuxPointsBfr.append(mainDocument.getTextes(12).getTexte(3).getDescription());
            // Ajout des copies
            while (copies.hasNext()) {
                ICTScalableDocumentCopie intervenantCopie = (ICTScalableDocumentCopie) copies.next();
                if (null != intervenantCopie) {
                    if (copies.hasNext()) {
                        strAnnexesCopiesValeurBfr.append(PRTiersHelper.getAdresseDomicileFormatee(getSessionCygnus(),
                                intervenantCopie.getIdTiers() + "\n"));
                        strAnnexesCopiesTitreBfr.append("\n");
                        strDeuxPointsBfr.append("\n");
                    } else {
                        strAnnexesCopiesValeurBfr.append(PRTiersHelper.getAdresseDomicileFormatee(getSessionCygnus(),
                                intervenantCopie.getIdTiers() + "\n"));
                    }
                }
            }
        }

        // ajout des annexes
        if ((null != annexes) && annexes.hasNext()) {
            // Ajout du libelle "annexes:"
            strAnnexesCopiesTitreBfr.append(mainDocument.getTextes(12).getTexte(2).getDescription());
            strDeuxPointsBfr.append(mainDocument.getTextes(12).getTexte(3).getDescription());
            // Ajout des annexes
            while (annexes.hasNext()) {
                ICTScalableDocumentAnnexe nomDocumentAnnexe = (ICTScalableDocumentAnnexe) annexes.next();
                if (null != nomDocumentAnnexe) {
                    if (annexes.hasNext()) {
                        strAnnexesCopiesValeurBfr.append(nomDocumentAnnexe.getLibelle() + "\n");
                        strAnnexesCopiesTitreBfr.append("\n");
                        strDeuxPointsBfr.append("\n");
                    } else {
                        strAnnexesCopiesValeurBfr.append(nomDocumentAnnexe.getLibelle());
                    }
                }
            }
        }

        if (!JadeStringUtil.isEmpty(strAnnexesCopiesTitreBfr.toString())) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_ANNEXE_COPIE, strAnnexesCopiesTitreBfr.toString());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_ANNEXE_COPIE, strAnnexesCopiesValeurBfr.toString());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_DP, strDeuxPointsBfr.toString());
        }
    }

    public void ajoutDecompte(RFCopieDecisionsValidationData... copie) throws Exception {
        try {

            ajoutTableauDecompteDecisionPonctuelle();

            ajoutApresTableau(copie);

        } catch (Exception e) {
            addErreurMail(memoryLog, e.getMessage(), "RFGenererDecisionMainService:ajoutDecompte");
            throw new Exception(e.toString());
        }
    }

    /**
     * Methode pour ajouter la signature en bas de document
     * 
     * @throws Exception
     */
    private void ajouterSignature() throws Exception {
        if (ajoutSignature) {
            try {
                // Appel du tableau "tabSignature"
                Collection tabSignature = new Collection(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_SIGNATURE);

                // Récupération de la variable du tableau
                DataList lineSignature = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_SIGNATURE_DATA);

                // Insertion de la variable dans le tableau
                tabSignature.add(lineSignature);

                CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

                // Recherche de l'adresse du tiers
                String adresse = PRTiersHelper.getAdresseCourrierFormatee(getSessionCygnus(), idTiers, "",
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).toString();

                crBean.setAdresse(adresse);

                // Lieu et date
                // crBean.setDate(JACalendar.format(this.dateSurDocument, this.codeIsoLangue));

                // Ajout du NSS
                crBean.setNoAvs(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

                caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                        getSessionCygnus().getApplication(), codeIsoLangue);
                caisseHelper.setTemplateName(RFGenererDecisionMainService.FICHIER_MODELE_DOCUMENT_DECISION_RFM);

                // Insertion de la signature
                data = caisseHelper.addSignatureParameters(data, crBean);

                // Chargement de la signature
                if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {
                    if (isMontantARestituer) {
                        data.addData("idSignature", "SIGNATURE_RFM_SANS_IMAGE");
                    } else {
                        data.addData("idSignature", "SIGNATURE_RFM_STANDART");
                    }
                } else {
                    data.addData("idSignature", "SIGNATURE_RFM_STANDART");
                }

            } catch (Exception e) {
                addErreurMail(memoryLog, e.getMessage(), "RFGenererDecisionMainService:remplirSignature");
                throw new Exception(e.toString());
            }
        } else {
            data.addData("idSignature", "NONE");
        }
    }

    /**
     * Methode pour ajouter le tableau de décompte dans une décision de régime
     * 
     * @param afficherDecompte
     * @throws Exception
     */
    protected void ajouterTableauDecompteRegime(Boolean afficherDecompte) throws Exception {
        if (afficherDecompte) {

            Collection tabDecompte = new Collection(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_DECOMPTE);

            if (!IRFTypePaiement.PAIEMENT_FUTURE.equals(decisionDocument.getTypePaiement())) {

                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECOMPTE, mainDocument.getTextes(6).getTexte(1)
                        .getDescription());
                String valeurMonetaire = mainDocument.getTextes(3).getTexte(6).getDescription();
                FWCurrency montantTotal = new FWCurrency(0);

                if (!decisionMensuelleUnMois) {

                    if (nbMois != 0) {
                        // Insertion de la ligne du tableau
                        DataList line1 = new DataList("retro");
                        tabDecompte.add(line1);

                        // %=retro=%
                        line1.addData(IRFGenererDocumentDecision.CAT_TEXTE_DEBUT_FIN_RETRO, PRStringUtils
                                .replaceString(
                                        PRStringUtils.replaceString(mainDocument.getTextes(6).getTexte(2)
                                                .getDescription(), IRFGenererDocumentDecision.MOIS_RETRO_DEBUT,
                                                moisRetroDebut), IRFGenererDocumentDecision.MOIS_RETRO_FIN,
                                        formatteMoisRetroFinMoinsUn));

                        // %=nombreMois=%
                        line1.addData(IRFGenererDocumentDecision.CAT_TEXTE_NB_MOIS_RETRO, PRStringUtils.replaceString(
                                mainDocument.getTextes(6).getTexte(3).getDescription(),
                                IRFGenererDocumentDecision.NB_MOIS, new Integer(nbMois).toString()));
                        // %=montantMensuel=%
                        line1.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_MENSUEL_RETRO, PRStringUtils
                                .replaceString(mainDocument.getTextes(6).getTexte(4).getDescription(),
                                        IRFGenererDocumentDecision.MONTANT_MENSUEL,
                                        formatter.format(Double.valueOf(montantMensuel))));
                        // Insertion de la valeur monetaire
                        line1.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, valeurMonetaire);
                        // %=montantRetro=%
                        line1.addData(IRFGenererDocumentDecision.CAT_TEXTE_SOMME_MONTANT_RETRO, PRStringUtils
                                .replaceString(mainDocument.getTextes(6).getTexte(5).getDescription(),
                                        IRFGenererDocumentDecision.MONTANT_RETRO,
                                        formatter.format(Double.valueOf(sommeMontantRetro))));
                        montantTotal.add(Double.valueOf(sommeMontantRetro));
                    }
                }

                if (!decisionMensuelleRetro) {

                    DataList line2 = new DataList("future");
                    tabDecompte.add(line2);
                    // %=future=%
                    line2.addData(IRFGenererDocumentDecision.CAT_TEXTE_MOIS_FUTURE, PRStringUtils.replaceString(
                            mainDocument.getTextes(6).getTexte(6).getDescription(),
                            IRFGenererDocumentDecision.MOIS_FUTURE, moisFuture));
                    // Insertion de la valeur monetaire
                    line2.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, valeurMonetaire);
                    // %=montantFuture=%
                    line2.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_FUTURE,
                            PRStringUtils.replaceString(mainDocument.getTextes(6).getTexte(7).getDescription(),
                                    IRFGenererDocumentDecision.MONTANT_FUTURE,
                                    formatter.format(Double.valueOf(montantMensuel))));
                    montantTotal.add(Double.valueOf(montantMensuel));
                }

                // Insertion des avances
                DataList line3 = new DataList("compensationAvance");
                tabDecompte.add(line3);
                line3.addData(IRFGenererDocumentDecision.CAT_TEXTE_AVANCE_REGIME, mainDocument.getTextes(6)
                        .getTexte(10).getDescription());
                // Insertion de la valeur monetaire
                line3.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, valeurMonetaire);
                line3.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_AVANCE,
                        formatter.format(Double.valueOf(decisionDocument.getMontantDette())));
                montantTotal.add(Double.valueOf(Double.valueOf(decisionDocument.getMontantDette())));

                DataList line4 = new DataList("total");
                tabDecompte.add(line4);
                // %=total=%
                line4.addData(IRFGenererDocumentDecision.CAT_TEXTE_TOTAL, mainDocument.getTextes(6).getTexte(8)
                        .getDescription());
                // Insertion de la valeur monetaire
                line4.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, valeurMonetaire);
                // %=montantTotal=%
                line4.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_TOTAL, PRStringUtils.replaceString(
                        mainDocument.getTextes(6).getTexte(9).getDescription(),
                        IRFGenererDocumentDecision.MONTANT_TOTAL,
                        formatter.format(Double.valueOf(montantTotal.toString()))));
            }

            // Insertion du template de decompte
            data.addData("isTableauDecompteInclude", "STANDARD");

            data.add(tabDecompte);
        } else {
            data.addData("isTableauDecompteInclude", "NONE");
        }
    }

    /**
     * Methode pour ajouter le tableau de versement dans une décision de régime.
     * 
     * @param afficherVersement
     * @throws Exception
     */
    protected void ajouterTableauVersementRegime(Boolean afficherVersement) throws Exception {

        if (afficherVersement) {
            // Appel du tableau de versement
            Collection tabVersement = new Collection(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_VERSEMENT);

            // Récupération des variables du tableau
            DataList lineVersement1 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_DATA1);
            DataList lineVersement2 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_DATA2);
            DataList lineVersement3 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_DATA3);

            // Insertion des variable dans le tableau
            tabVersement.add(lineVersement1);
            tabVersement.add(lineVersement2);
            tabVersement.add(lineVersement3);

            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_TITRE, mainDocument.getTextes(5).getTexte(1)
                    .getDescription());
            // %=versement=%
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT, mainDocument.getTextes(5).getTexte(2)
                    .getDescription());
            // %=qui=%
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_QUI, mainDocument.getTextes(5).getTexte(3)
                    .getDescription());

            // %=adresseVersement=%
            String adresseTiersPaiement = PRTiersHelper.getAdresseCourrierFormatee(getSessionCygnus(),
                    decisionDocument.getIdTiersAdressePaiement(), "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE_VERSEMENT, PRStringUtils.replaceString(
                    mainDocument.getTextes(5).getTexte(4).getDescription(), IRFGenererDocumentDecision.ADRESSE,
                    adresseTiersPaiement));

            // %=adressePaiementVersement=%
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE_PAIEMENT_VERSEMENT, PRStringUtils.replaceString(
                    mainDocument.getTextes(5).getTexte(5).getDescription(),
                    IRFGenererDocumentDecision.TIERS_ADRESSE_PAIEMENT, adressePaiement));
            // %=commentaireVersement=%
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_COMMENTAIRE_VERSEMENT, mainDocument.getTextes(5)
                    .getTexte(6).getDescription());

            // Insetion du template de versement
            data.addData("isTableauVersementInclude", "STANDARD");

        } else {
            data.addData("isTableauVersementInclude", "NONE");
        }
    }

    public void ajoutMotifRefusOMAV(String montantOAI, Collection table) {
        DataList line16 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_MOTIF_REFUS);
        table.add(line16);
        line16.addData(IRFGenererDocumentDecision.CAT_TEXTE_MOTIF_REFUS,
                " - " + getSession().getLabel("PROCESS_VALIDER_DECISIONS_MOTIF_REFUS_OMAV"));
        line16.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_MOTIF,
                "-" + new FWCurrency(montantOAI).toStringFormat());
    }

    // ajout des informations en pied de page
    protected void ajoutPiedDePage() {
        // debut pied de page
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PAGE, mainDocument.getTextes(5).getTexte(2).getDescription());

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_COUNT_ADD, "0");

        // fin pied de page
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIN_PAGE, mainDocument.getTextes(5).getTexte(3)
                .getDescription());
    }

    // ajout des informations en pied de page
    protected void ajoutPiedDePageRegime() {
        // debut pied de page
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PAGE, mainDocument.getTextes(12).getTexte(1).getDescription());

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_COUNT_ADD, "0");

        // fin pied de page
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_FIN_PAGE, mainDocument.getTextes(12).getTexte(2)
                .getDescription());
    }

    /*
     * Insertion du tableau dans le document PDF
     */
    protected void ajoutTableauDecompteDecisionPonctuelle() throws Exception {

        // création d'un tableau pour afficher les valeurs sur plusieurs lignes
        Collection table = new Collection(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU);

        // resumé décision
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RESUME_DECISION_LIGNE1, PRStringUtils.replaceString(
                PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument.getTextes(6).getTexte(1)
                        .getDescription(), IRFGenererDocumentDecision.ANNEE_FACTURE, decisionDocument.getAnneeQD()),
                        IRFGenererDocumentDecision.NUMERO_DECISION, decisionDocument.getNumeroDecision()),
                IRFGenererDocumentDecision.DATE_DECISION, decisionDocument.getDateDecision_JourMoisAnnee()));

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RESUME_DECISION_LIGNE2, PRStringUtils.replaceString(
                PRStringUtils.replaceString(PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument
                        .getTextes(6).getTexte(2).getDescription(), IRFGenererDocumentDecision.TIERS_NSS, nss),
                        IRFGenererDocumentDecision.TIERS_NOM, nom), IRFGenererDocumentDecision.TIERS_PRENOM, prenom),
                IRFGenererDocumentDecision.DATE_NAISSANCE, dateNaissance));

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_EN_TETE_DECOMPTE,
                mainDocument.getTextes(6).getTexte(3).getDescription());

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_EN_TETE_MONTANT_DEMANDE, mainDocument.getTextes(6)
                .getTexte(4).getDescription());

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_EN_TETE_MONTANT_NON_RECONNU, mainDocument
                .getTextes(6).getTexte(5).getDescription());

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_EN_TETE_MONTANT_ADMIS, mainDocument.getTextes(6)
                .getTexte(6).getDescription());

        // pour chaque demande
        BigDecimal[] soldeExcedentDepassementQd = new BigDecimal[2];

        if ((typePaiement.equals(IRFTypePaiement.PAIEMENT_RETROACTIF) || (typePaiement
                .equals(IRFTypePaiement.PAIEMENT_FUTURE)))) {

            for (RFDemandeValidationData demande : decisionDocument.getDecisionDemande()) {

                // Retrieve d'informations pour la création de la décision
                PRTiersWrapper tiersWrapperFournisseur = PRTiersHelper.getTiersParId(getSession(),
                        demande.getIdFournisseur());
                if (null == tiersWrapperFournisseur) {
                    tiersWrapperFournisseur = PRTiersHelper.getAdministrationParId(getSession(),
                            demande.getIdFournisseur());
                }
                // Assure concerné
                getAssureConcerne(demande.getIdTiersAssureConcerne(), table);

                // Type de demande
                getTypeDemande(table, demande);

                // Facture
                getFacture(demande, table);

                // Fournisseur
                getFournisseur(table, tiersWrapperFournisseur, demande);

                // Date traitement
                getDatesTraitement(demande, table, 0);

                // Motif de refus
                soldeExcedentDepassementQd = getMotifsRefus(demande, table);

                // Mis à charge RFM
                getChargeRFM(table, demande, 0, soldeExcedentDepassementQd[0], soldeExcedentDepassementQd[1]);
            }
        } else {// paiement courant : on passe deux fois
            for (int i = 1; i <= 2; i++) {
                RFDemandeValidationData demande = decisionDocument.getDecisionDemande().iterator().next();

                // Retrieve d'informations pour la création de la décision
                PRTiersWrapper tiersWrapperFournisseur = null;
                // if (i == 1) {
                tiersWrapperFournisseur = PRTiersHelper.getTiersParId(getSession(), demande.getIdFournisseur());
                if (null == tiersWrapperFournisseur) {
                    tiersWrapperFournisseur = PRTiersHelper.getAdministrationParId(getSession(),
                            demande.getIdFournisseur());
                }
                // }

                /*
                 * Assure concerné
                 */
                getAssureConcerne(demande.getIdTiersAssureConcerne(), table);

                /*
                 * Type de demande
                 */
                getTypeDemande(table, demande);

                /*
                 * Facture
                 */
                getFacture(demande, table);

                /*
                 * Fournisseur
                 */
                getFournisseur(table, tiersWrapperFournisseur, demande);

                /*
                 * Date traitement
                 */
                getDatesTraitement(demande, table, i);

                /*
                 * Motif de refus
                 */
                soldeExcedentDepassementQd = getMotifsRefus(demande, table);

                /*
                 * Mis à charge RFM
                 */
                getChargeRFM(table, demande, i, soldeExcedentDepassementQd[0], soldeExcedentDepassementQd[1]);

            }
        }

        // calcul et écriture des totaux
        calculEtEcritureTotaux(table);
    }

    private void buildTabDecisionDues(Collection tabDecompteRestitutionRFM) throws Exception {

        // Insertion du titre des décisions dues
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECOMPTE_DU_RESTITUTION, mainDocument.getTextes(3)
                .getTexte(1).getDescription());

        // Récupération des nouvelles décisions concernées
        ArrayList<RFDecision> listeDecisionsDues = new ArrayList<RFDecision>();
        listeDecisionsDues = rfGenererDecisionRestitutionService.getListeDecisionsDues(decisionDocument, getSession());
        int nbDecisionsDues = listeDecisionsDues.size();
        for (RFDecision decisionDue : listeDecisionsDues) {

            nbDecisionsDues--;
            String dateDecisionDue = "";
            if (!JadeStringUtil.isEmpty(decisionDue.getDateValidation())) {
                dateDecisionDue = decisionDue.getDateValidation();
            } else {
                dateDecisionDue = getDateSurDocument();
            }

            // Insertion de la ligne du tableau
            DataList ligneDecisionDueItr = new DataList(
                    IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_DECOMPTE_DU_RESTITUTION);
            String texteDecisionNumero = PRStringUtils.replaceString(mainDocument.getTextes(3).getTexte(2)
                    .getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_NUMERO_DECISION_RESTITUTION,
                    decisionDue.getNumeroDecision());
            ligneDecisionDueItr.addData(IRFGenererDocumentDecision.CAT_TEXTE_DECOMPTE_DU, PRStringUtils.replaceString(
                    texteDecisionNumero, IRFGenererDocumentDecision.DATE_DECISION_DU, dateDecisionDue));
            tabDecompteRestitutionRFM.add(ligneDecisionDueItr);

            // Insertion de chaque demande corrigée appartenant à la décision
            ArrayList<RFDemande> listeDemandesDues = new ArrayList<RFDemande>();
            listeDemandesDues = rfGenererDecisionRestitutionService.getListeDemandesDues(decisionDocument,
                    decisionDue.getIdDecision(), getSession());
            int nbDemandesDues = listeDemandesDues.size();
            for (RFDemande demandeDue : listeDemandesDues) {

                nbDemandesDues--;
                if (((nbDemandesDues > 0) && (nbDecisionsDues > 0)) || ((nbDemandesDues > 0) && (nbDecisionsDues == 0))
                        || ((nbDemandesDues == 0) && (nbDecisionsDues > 0))) {
                    // Insertion de chaque ligne, sauf la dernière
                    DataList ligneDemandeDue = new DataList(IRFGenererDocumentDecision.LIGNE_DEMANDES_DUES);

                    ligneDemandeDue.addData(IRFGenererDocumentDecision.CAT_TEXTE_DEMANDE_DUE, PRStringUtils
                            .replaceString(mainDocument.getTextes(3).getTexte(8).getDescription(),
                                    IRFGenererDocumentDecision.CAT_TEXTE_DEMANDE_NUMERO, demandeDue.getIdDemande()));
                    ligneDemandeDue.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, mainDocument
                            .getTextes(3).getTexte(3).getDescription());
                    ligneDemandeDue.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_ITR_DU, new FWCurrency(
                            demandeDue.getMontantAPayer()).toStringFormat());
                    tabDecompteRestitutionRFM.add(ligneDemandeDue);

                    montantTotalDu = montantTotalDu.add(new BigDecimal(demandeDue.getMontantAPayer()));

                    // BZ9451 : Ajout du montant pris en charge par DSAS, si propertie 'forcer paiement' utilisée
                    if (RFPropertiesUtils.afficherCaseForcerPaiementDemande().equals(Boolean.TRUE)) {
                        // Si checkBox 'forcé paiement' à TRUE sur la demande
                        if (demandeDue.getIsForcerPaiement()) {
                            BigDecimal montantDSAS = new BigDecimal(demandeDue.getMontantFacture());
                            montantDSAS = montantDSAS.subtract(new BigDecimal(demandeDue.getMontantAPayer()));

                            montantTotalDu = montantTotalDu.add(new BigDecimal(montantDSAS.toString()));
                        }
                    }

                } else {

                    // Insertion de la dernière ligne
                    DataList ligneFinalDecisionsDues = new DataList(
                            IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_DECOMPTE_DU_FINAL_RESTITUTION);

                    ligneFinalDecisionsDues.addData(IRFGenererDocumentDecision.CAT_TEXTE_DECOMPTE_DU_FINAL,
                            PRStringUtils.replaceString(mainDocument.getTextes(3).getTexte(8).getDescription(),
                                    IRFGenererDocumentDecision.CAT_TEXTE_DEMANDE_NUMERO, demandeDue.getIdDemande()));
                    ligneFinalDecisionsDues.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF,
                            mainDocument.getTextes(3).getTexte(3).getDescription());
                    ligneFinalDecisionsDues.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_DU, new FWCurrency(
                            demandeDue.getMontantAPayer()).toStringFormat());
                    montantTotalDu = montantTotalDu.add(new BigDecimal(demandeDue.getMontantAPayer()));

                    // BZ9451 : Ajout du montant pris en charge par DSAS, si propertie 'forcer paiement' utilisée
                    if (RFPropertiesUtils.afficherCaseForcerPaiementDemande().equals(Boolean.TRUE)) {
                        // Si checkBox 'forcé paiement' à TRUE sur la demande
                        if (demandeDue.getIsForcerPaiement()) {
                            BigDecimal montantDSAS = new BigDecimal(demandeDue.getMontantFacture());
                            montantDSAS = montantDSAS.subtract(new BigDecimal(demandeDue.getMontantAPayer()));

                            montantTotalDu = montantTotalDu.add(new BigDecimal(montantDSAS.toString()));
                        }
                    }

                    ligneFinalDecisionsDues.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_DU_TOTAL,
                            new FWCurrency(montantTotalDu.toString()).toStringFormat());
                    tabDecompteRestitutionRFM.add(ligneFinalDecisionsDues);
                }
            }
        }
    }

    private void buildTabDecisionsVersees(Collection tabDecompteRestitutionRFM) throws Exception {
        // Tableau : anciennes décisions
        // *****************************
        // Insertion du titre des décisions versés
        DataList ligneTitreDecisionsVersees = new DataList(
                IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_TITRE_DECOMPTE_VERSE_RESTITUTION);
        ligneTitreDecisionsVersees.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECOMPTE_VERSE_RESTITUTION,
                mainDocument.getTextes(3).getTexte(4).getDescription());
        tabDecompteRestitutionRFM.add(ligneTitreDecisionsVersees);

        ArrayList<RFDecision> listeDecisionsVersees = new ArrayList<RFDecision>();
        listeDecisionsVersees = rfGenererDecisionRestitutionService.getListeDecisionsVersees(decisionDocument,
                getSession());
        int nbDecisionsVersees = listeDecisionsVersees.size();

        for (RFDecision decisionVersee : listeDecisionsVersees) {

            nbDecisionsVersees--;

            DataList ligneDecisionVerseeItr = new DataList(
                    IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_DECOMPTE_VERSE_RESTITUTION);
            String texteDecisionNumero = PRStringUtils.replaceString(mainDocument.getTextes(3).getTexte(5)
                    .getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_NUMERO_DECISION_RESTITUTION,
                    decisionVersee.getNumeroDecision());
            ligneDecisionVerseeItr.addData(IRFGenererDocumentDecision.CAT_TEXTE_DECOMPTE_VERSE, PRStringUtils
                    .replaceString(texteDecisionNumero, IRFGenererDocumentDecision.DATE_DECISION_VERSE,
                            decisionVersee.getDateValidation()));
            tabDecompteRestitutionRFM.add(ligneDecisionVerseeItr);

            ArrayList<RFDemande> listeDemandesVersees = new ArrayList<RFDemande>();
            listeDemandesVersees = rfGenererDecisionRestitutionService.getListeDemandesVersees(decisionDocument,
                    decisionVersee.getIdDecision(), sessionCygnus);
            int nbDemandesVersees = listeDemandesVersees.size();
            for (RFDemande demandeVersee : listeDemandesVersees) {

                nbDemandesVersees--;

                if (((nbDemandesVersees > 0) && (nbDecisionsVersees > 0))
                        || ((nbDemandesVersees > 0) && (nbDecisionsVersees == 0))
                        || ((nbDemandesVersees == 0) && (nbDecisionsVersees > 0))) {
                    // Insertion de chaque ligne, sauf la dernière
                    DataList ligneDemandeVersee = new DataList(IRFGenererDocumentDecision.LIGNE_DEMANDES_VERSEES);

                    ligneDemandeVersee.addData(IRFGenererDocumentDecision.CAT_TEXTE_DEMANDE_VERSEE, PRStringUtils
                            .replaceString(mainDocument.getTextes(3).getTexte(8).getDescription(),
                                    IRFGenererDocumentDecision.CAT_TEXTE_DEMANDE_NUMERO, demandeVersee.getIdDemande()));
                    ligneDemandeVersee.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, mainDocument
                            .getTextes(3).getTexte(3).getDescription());
                    ligneDemandeVersee.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_ITR_VERSE, new FWCurrency(
                            demandeVersee.getMontantAPayer()).toStringFormat());
                    montantTotalVersee = montantTotalVersee.add(new BigDecimal(demandeVersee.getMontantAPayer()));

                    tabDecompteRestitutionRFM.add(ligneDemandeVersee);

                    // BZ9451 : Ajout du montant pris en charge par DSAS, si propertie 'forcer paiement' utilisée
                    if (RFPropertiesUtils.afficherCaseForcerPaiementDemande().equals(Boolean.TRUE)) {
                        // Si checkBox 'forcé paiement' à TRUE sur la demande
                        if (demandeVersee.getIsForcerPaiement()) {
                            BigDecimal montantDSAS = new BigDecimal(demandeVersee.getMontantFacture());
                            montantDSAS = montantDSAS.subtract(new BigDecimal(demandeVersee.getMontantAPayer()));

                            montantTotalVersee = montantTotalVersee.add(new BigDecimal(montantDSAS.toString()));
                        }
                    }

                } else {
                    // Insertion de la dernière ligne
                    DataList ligneFinalDecisionsVersee = new DataList(
                            IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_DECOMPTE_VERSE_FINAL_RESTITUTION);

                    ligneFinalDecisionsVersee.addData(IRFGenererDocumentDecision.CAT_TEXTE_DECOMPTE_VERSE_FINAL,
                            PRStringUtils.replaceString(mainDocument.getTextes(3).getTexte(8).getDescription(),
                                    IRFGenererDocumentDecision.CAT_TEXTE_DEMANDE_NUMERO, demandeVersee.getIdDemande()));
                    ligneFinalDecisionsVersee.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF,
                            mainDocument.getTextes(3).getTexte(3).getDescription());
                    ligneFinalDecisionsVersee.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_VERSE,
                            new FWCurrency(demandeVersee.getMontantAPayer()).toStringFormat());
                    montantTotalVersee = montantTotalVersee.add(new BigDecimal(demandeVersee.getMontantAPayer()));

                    // BZ9451 : Ajout du montant pris en charge par DSAS, si propertie 'forcer paiement' utilisée
                    if (RFPropertiesUtils.afficherCaseForcerPaiementDemande().equals(Boolean.TRUE)) {
                        // Si checkBox 'forcé paiement' à TRUE sur la demande
                        if (demandeVersee.getIsForcerPaiement()) {
                            BigDecimal montantDSAS = new BigDecimal(demandeVersee.getMontantFacture());
                            montantDSAS = montantDSAS.subtract(new BigDecimal(demandeVersee.getMontantAPayer()));

                            montantTotalVersee = montantTotalVersee.add(new BigDecimal(montantDSAS.toString()));
                        }
                    }

                    ligneFinalDecisionsVersee.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_VERSE_TOTAL,
                            new FWCurrency(montantTotalVersee.toString()).toStringFormat());

                    tabDecompteRestitutionRFM.add(ligneFinalDecisionsVersee);
                }
            }
        }
    }

    private void buildTabTotalDecisions(Collection tabDecompteRestitutionRFM) {

        DataList texteRemboursement = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_TEXTE_REMBOURSEMENT);
        if (montantTotalDu.subtract(montantTotalVersee).floatValue() > 0) {
            // La somme des nouvelles décisions est plus grande que celle des anciennes décision
            texteRemboursement.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMBOURSEMENT, mainDocument.getTextes(3)
                    .getTexte(7).getDescription());
            // Insertion de la valeur monetaire
            texteRemboursement.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, mainDocument
                    .getTextes(3).getTexte(3).getDescription());

            montantARestituerDecisionRestitution = new FWCurrency(montantTotalDu.subtract(montantTotalVersee)
                    .toString());

            // Insertion du montant
            texteRemboursement.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_REMBOURSEMENT,
                    montantARestituerDecisionRestitution.toStringFormat());

            isMontantARestituer = false;

        } else {
            texteRemboursement.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMBOURSEMENT, mainDocument.getTextes(3)
                    .getTexte(6).getDescription());
            // Insertion de la valeur monetaire
            texteRemboursement.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF, mainDocument
                    .getTextes(3).getTexte(3).getDescription());

            montantARestituerDecisionRestitution = new FWCurrency(montantTotalVersee.subtract(montantTotalDu).abs()
                    .toString());

            // Insertion du montant
            texteRemboursement.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_REMBOURSEMENT,
                    montantARestituerDecisionRestitution.toStringFormat());

            isMontantARestituer = true;
            decisionDocument.setIsBulletinVersement(true);
        }

        tabDecompteRestitutionRFM.add(texteRemboursement);
    }

    protected void calculEtEcritureTotaux(Collection table) throws Exception {
        /*
         * Calcul et écriture des totaux (somme des demandes) dans le document PDF
         */
        BigDecimal excedentRevenu = new BigDecimal(decisionDocument.getExcedentRevenu());
        BigDecimal depassementQD = new BigDecimal(decisionDocument.getDepassementQD());

        DataList line2 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_TOTAL);
        table.add(line2);
        line2.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_TOTAL_TEXTE, mainDocument.getTextes(8).getTexte(1)
                .getDescription());

        BigDecimal montantTotalMisAChargeBigDec = new BigDecimal(decisionDocument.getMontantTotal())
                .add(excedentRevenu).add(depassementQD);

        // line2.addData("montantTotal", df.format(montantTotalBigDec.toString()));
        line2.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_TOTAL,
                new FWCurrency(montantTotalMisAChargeBigDec.toString()).toStringFormat());

        DataList line3 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_DEPASSEMENT_QD);
        table.add(line3);
        line3.addData(IRFGenererDocumentDecision.CAT_TEXTE_DEPASSEMENT_QD_TEXTE, mainDocument.getTextes(8).getTexte(2)
                .getDescription());
        line3.addData(
                IRFGenererDocumentDecision.CAT_TEXTE_DEPASSEMENT_QD,
                depassementQD.compareTo(BigDecimal.ZERO) == 0 ? new FWCurrency(decisionDocument.getDepassementQD())
                        .toStringFormat() : "-" + new FWCurrency(decisionDocument.getDepassementQD()).toStringFormat());

        DataList line4 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_EXCEDENT_REVENU);
        table.add(line4);
        line4.addData(IRFGenererDocumentDecision.CAT_TEXTE_EXCEDENT_REVENU_TEXTE, mainDocument.getTextes(8).getTexte(3)
                .getDescription());
        line4.addData(
                IRFGenererDocumentDecision.CAT_TEXTE_EXCEDENT_REVENU,
                excedentRevenu.compareTo(BigDecimal.ZERO) == 0 ? new FWCurrency(decisionDocument.getExcedentRevenu())
                        .toStringFormat() : "-" + new FWCurrency(decisionDocument.getExcedentRevenu()).toStringFormat());

        // DataLine5 : AVANCE
        if (JadeStringUtil.isEmpty(decisionDocument.getMontantDette())) {
            decisionDocument.setMontantDette(new FWCurrency(0).toString());
        }
        DataList line5 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_COMPENSATION_AVANCE);
        table.add(line5);
        line5.addData(IRFGenererDocumentDecision.CAT_TEXTE_COMPENSATION_AVANCE_TEXTE, mainDocument.getTextes(8)
                .getTexte(7).getDescription());
        line5.addData(IRFGenererDocumentDecision.CAT_TEXTE_COMPENSATION_AVANCE_MONTANT,
                new FWCurrency(formatter.format(Double.valueOf(decisionDocument.getMontantDette()))).toStringFormat());

        DataList line6 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER_RFM);
        table.add(line6);
        line6.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER_RFM_TEXTE, mainDocument.getTextes(8)
                .getTexte(4).getDescription());

        FWCurrency montantTotalARembourserParRFM = new FWCurrency(decisionDocument.getMontantTotal());
        montantTotalARembourserParRFM.add(new FWCurrency(decisionDocument.getMontantDette()));
        line6.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER_RFM,
                new FWCurrency(formatter.format(Double.valueOf(montantTotalARembourserParRFM.toString())))
                        .toStringFormat());

        if (RFPropertiesUtils.affichageTextesLiesAuxRemboursmentsParDsas()) {
            DataList line7 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER_DSAS);
            table.add(line7);

            // Si montant à rembourser par DSAS = 0.-, on cache l'asterisque en bout de phrase
            if (JadeStringUtil.isBlankOrZero(decisionDocument.getMontantARembourserParLeDsas())) {
                line7.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER_DSAS_TEXTE, mainDocument
                        .getTextes(8).getTexte(5).getDescription().replace("*", ""));
            } else {
                line7.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER_DSAS_TEXTE, mainDocument
                        .getTextes(8).getTexte(5).getDescription());
            }

            FWCurrency montantTotalARembourserParDSAS = new FWCurrency(
                    decisionDocument.getMontantARembourserParLeDsas());
            line7.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER_DSAS,
                    new FWCurrency(formatter.format(Double.valueOf(montantTotalARembourserParDSAS.toString())))
                            .toStringFormat());
        }

        montantTotalARembourser = new BigDecimal(decisionDocument.getMontantTotal());

        if (RFPropertiesUtils.affichageTextesLiesAuxRemboursmentsParDsas()) {
            montantTotalARembourser = montantTotalARembourser.add(new BigDecimal(decisionDocument
                    .getMontantARembourserParLeDsas()));
        }

        DataList line8 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_TOTAL_A_REMBOURSER);
        table.add(line8);
        line8.addData(IRFGenererDocumentDecision.CAT_TEXTE_TOTAL_A_REMBOURSER_TEXTE, mainDocument.getTextes(8)
                .getTexte(6).getDescription());

        FWCurrency montantTotalARembourser = new FWCurrency(this.montantTotalARembourser.toString());
        montantTotalARembourser.add(new FWCurrency(decisionDocument.getMontantDette()));

        line8.addData(IRFGenererDocumentDecision.CAT_TEXTE_TOTAL_A_REMBOURSER,
                new FWCurrency(montantTotalARembourser.toString()).toStringFormat());

        data.add(table);
    }

    /*
     * On n'utilise pas cette méthode pour ne pas avoir à la charger pour chaque décision
     */
    @Override
    public void chargerCatalogueTexte() throws Exception {
        // Auto-generated method stub
    }

    @Override
    public void chargerDonneesEnTete() throws Exception {

        try {
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            // Recherche de l'adresse du tiers
            String adresse = PRTiersHelper.getAdresseCourrierFormatee(getSessionCygnus(), idTiers, "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE).toString();

            crBean.setAdresse(adresse);

            if (JadeStringUtil.isEmpty(dateSurDocument)) {
                dateSurDocument = decisionDocument.getDateDecision_JourMoisAnnee();
            }

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            // Ajout du NSS
            crBean.setNoAvs(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                    getSessionCygnus().getApplication(), codeIsoLangue);
            caisseHelper.setTemplateName(RFGenererDecisionMainService.FICHIER_MODELE_DOCUMENT_DECISION_RFM);

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Chargement du header (en-tête)
            data.addData("idEntete", "HEADER_CAISSE");

            // Chargement du téléphone du gestionnaire
            if (!JadeStringUtil.isEmpty(telGestionnaire)) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE, telGestionnaire);
            }

            // ### RUBRIQUES SPECIFIQUES A LAUSANNE ###
            // ----------------------------------------
            // Chargement du nom complet du gestionnaire (spécifique AGLAU)
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_GESTIONNAIRE_AGLA,
                    getNomGestionnaireAAfficher(getDecisionDocument().getGestionnaire()));
            // Chargement du telephone du gestionnaire (specifique AGLAU)
            if (!JadeStringUtil.isEmpty(telGestionnaireGrpLettre)) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE_AGLAU, telGestionnaireGrpLettre);
            } else {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE_AGLAU, telGestionnaire);
            }
            // Chargement de l'id gestionnaire pour les décisions qui contiennent cette variable (spécifique AGLAU)
            data.addData(IRFGenererDocumentDecision.ID_GESTIONNAIRE_VALIDATION, getDecisionDocument().getGestionnaire());
            // Chargement du NSS de l'assuré
            data.addData("NSS_BENEFICIAIRE", nss);
            // --------------------------------------------
            // ### FIN RUBRIQUES SPECIFIQUES A LAUSANNE ###

        } catch (Exception e) {
            addErreurMail(memoryLog, e.getMessage(), "RFGenererDecisionMainService:chargerDonneesEnTete");
            throw new Exception(e.toString());
        }
    }

    /**
     * Set le template qui correspond à la décision en cours, au data
     * 
     * @param decisionEnum
     */
    private void chargerTemplateDecision(RFTypeDecisionEnum decisionEnum) {
        data.addData(decisionEnum.getTopazAttribute(), decisionEnum.getTopazValue());
    }

    private void chargerTextesRecoursSiDemandeRefusee(DocumentData data) throws Exception {
        boolean containDemandeRefusee = Boolean.FALSE;

        for (RFDemandeValidationData demande : decisionDocument.getDecisionDemande()) {

            // si au moins une des demandes à la case à cocher
            // "texte de redirection" de cochée.
            if (demande.getIsTexteRedirection()) {
                containDemandeRefusee = Boolean.TRUE;
                break;
            }
        }

        if (containDemandeRefusee) {

            // Recherche de la QD principale du bénéficiaire
            RFQdPrincipale qdPrincipale = new RFQdPrincipale();
            qdPrincipale.setSession(sessionCygnus);
            qdPrincipale.setIdQdPrincipale(decisionDocument.getIdQdPrincipale());
            qdPrincipale.retrieve();

            // Si QD trouvée, on charge le texte en fonction du type de QD du bénéficiaire
            if ((qdPrincipale != null) && !JadeStringUtil.isEmpty(qdPrincipale.getCsGenrePCAccordee())
                    && !JadeStringUtil.isEmpty(qdPrincipale.getCsTypePCAccordee())) {

                String genrePcAssure = qdPrincipale.getCsGenrePCAccordee();
                String typePcAssure = qdPrincipale.getCsTypePCAccordee();

                // SI ASSURE A L'AVS + A DOMICILE
                if (genrePcAssure.equals(IRFGenrePc.GENRE_PC_DOMICILE)
                        && (typePcAssure.equals(IRFGenrePrestations.CS_GENRE_SURVIVANT) || typePcAssure
                                .equals(IRFGenrePrestations.CS_GENRE_VIEILLESSE))) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_RECOURS_1, mainDocument.getTextes(13)
                            .getTexte(1).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_1_DEBUT, mainDocument.getTextes(13)
                            .getTexte(2).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEXTE_RECOURS_1_GRAS, mainDocument.getTextes(13)
                            .getTexte(3).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEXTE_RECOURS_1_FIN, mainDocument.getTextes(13)
                            .getTexte(4).getDescription());
                }

                // SI ASSURE A L'AI + A DOMICILE
                if (genrePcAssure.equals(IRFGenrePc.GENRE_PC_DOMICILE)
                        && (qdPrincipale.getCsTypePCAccordee().equals(IRFGenrePrestations.CS_GENRE_INVALIDITE))) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_RECOURS_2, mainDocument.getTextes(13)
                            .getTexte(5).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_2_DEBUT, mainDocument.getTextes(13)
                            .getTexte(6).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_2_GRAS, mainDocument.getTextes(13)
                            .getTexte(7).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_2_SUITE, mainDocument.getTextes(13)
                            .getTexte(8).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_2_ITALIQUE, mainDocument.getTextes(13)
                            .getTexte(9).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_2_FIN, mainDocument.getTextes(13)
                            .getTexte(10).getDescription());
                }

                // SI ASSURE A AVS/AI + EMS SASH
                if (qdPrincipale.getRemboursementRequerant().equals(IRFQd.CS_TYPE_REMBOURSEMENT_SASH)) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_RECOURS_3, mainDocument.getTextes(13)
                            .getTexte(11).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_3_DEBUT, mainDocument.getTextes(13)
                            .getTexte(12).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_3_GRAS, mainDocument.getTextes(13)
                            .getTexte(13).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_3_SUITE, mainDocument.getTextes(13)
                            .getTexte(14).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_3_FIN, mainDocument.getTextes(13)
                            .getTexte(15).getDescription());
                }

                // SI ASSURE A AVS/AI + EMS SPAS
                if (qdPrincipale.getRemboursementRequerant().equals(IRFQd.CS_TYPE_REMBOURSEMENT_SPAS)) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_RECOURS_4, mainDocument.getTextes(13)
                            .getTexte(16).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_4_DEBUT, mainDocument.getTextes(13)
                            .getTexte(17).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_4_GRAS, mainDocument.getTextes(13)
                            .getTexte(18).getDescription());
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_4_FIN, mainDocument.getTextes(13)
                            .getTexte(19).getDescription());
                }

                if (qdPrincipale.getRemboursementRequerant().equals(IRFQd.CS_TYPE_REMBOURSEMENT_SASH)
                        || qdPrincipale.getRemboursementRequerant().equals(IRFQd.CS_TYPE_REMBOURSEMENT_SPAS)) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_INFO_EMS, mainDocument.getTextes(13)
                            .getTexte(20).getDescription());
                }

            }
            // Sinon, on charge une phrase lié à l'absence de QD (pas de droit PC)
            else {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_5_DEBUT,
                        mainDocument.getTextes(13).getTexte(21).getDescription());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_5_ITALIQUE, mainDocument.getTextes(13)
                        .getTexte(22).getDescription());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RECOURS_5_FIN, mainDocument.getTextes(13)
                        .getTexte(23).getDescription());
            }
        }
    }

    /**
     * Set le catalogue de textes qui correspond à la décision en cours, à ICTDocument
     * 
     * @param catalogueMultiLangue
     * @param decisionEnum
     */
    private void chargerTypeCatalogueTexteDecision(Hashtable<String, ICTDocument> catalogueMultiLangue,
            RFTypeDecisionEnum decisionEnum) {
        // ...selection du catalogue de texte
        setMainDocument(catalogueMultiLangue.get(decisionEnum.getCsTypedocument() + "_" + codeIsoLangue));
    }

    /**
     * Set le modèle de document qui correspond à la décision en cours, au documentHelper
     * 
     * @param decisionEnum
     */
    private void chargerTypeDocumentDecision(RFTypeDecisionEnum decisionEnum) {
        documentHelper.setCsTypeDocument(decisionEnum.getCsTypedocument());
    }

    public void generationLettre(JadePrintDocumentContainer container, boolean miseEnGed, FWMemoryLog memoryLog,
            boolean isDecisionPonctuelle, boolean isDecisionMensuelle, boolean isDecisionRestitution,
            ICTDocument documentHelper, Hashtable<String, ICTDocument> catalogueMultiLangue,
            RFCopieDecisionsValidationData... copie) throws Exception {
        try {
            // Set la session à RFAbstractDocumentOO
            session = sessionCygnus;

            // this.documentHelper = documentHelper;

            // Retrieve d'informations pour la création de la décision
            tiersWrapper = PRTiersHelper.getTiersParId(getSessionCygnus(), idTiers);
            if (null == tiersWrapper) {
                tiersWrapper = PRTiersHelper.getAdministrationParId(getSessionCygnus(), idTiers);
            }

            if (null != tiersWrapper) {

                // on charge la langue qui correspond
                codeIsoLangue = getSessionCygnus().getCode(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

                // infos du tiers
                nom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);
                prenom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                sexe = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_SEXE);
                nss = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                titre = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_TITRE);
                dateNaissance = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
                adresse = getAdresseCourrier();
                npa = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA);
                telGestionnaire = getTelephoneGestionnaire();
                nomGestionnaire = getNomGestionnaire();

                // Chargement informations principales
                data = new DocumentData();

                // Création des paramètres pour l'en-tête
                chargerDonneesEnTete();

                // récupération de la formule de politesse associée à la langue du tiers
                String formulePolitesse = resolveFormulePolitesse();

                // Recherche du document qui va être généré
                // Si décision de restitution
                if (isDecisionRestitution) {
                    // appel de méthode de remplissage du document
                    remplirDocumentPaiementRestitution(container, miseEnGed, catalogueMultiLangue, formulePolitesse,
                            copie);
                    // Ajout des copies
                    ajoutCopiesAnnexes();
                }

                // Sinon si décision ponctuelle
                else if (isDecisionPonctuelle) {
                    // appel de méthode de remplissage du document
                    remplirDocumentPaiementPonctuel(container, miseEnGed, catalogueMultiLangue, formulePolitesse, copie);

                    // Ajout des copies
                    ajoutCopiesAnnexes();

                }

                // Sinon si décision de régime
                else if (isDecisionMensuelle) {
                    // Si propriété à TRUE, on retire les décisions de régime dans le document PDF.
                    if (!RFPropertiesUtils.retirerDecisionsRegime()) {
                        // initialisation des champs liés à la décision de régime
                        initialiserChampsPaiementDecisionRegime();
                        // appel de méthode de remplissage du document
                        remplirDocumentPaiementMensuelRegime(container, miseEnGed, catalogueMultiLangue,
                                formulePolitesse, copie);
                        // Ajout des copies
                        ajoutCopiesAnnexes();
                    }
                }

                if ((decisionToPrint != null) && decisionToPrint.getHasModel()) {
                    setDocumentData(data);
                }

            } else {
                addErreurMail(memoryLog, "Erreur : Pas d'adresse tiers (RFM_validerDecisionOO.generationLettre())",
                        "RFGenererDecisionMainService:generationLettre");
                throw new Exception("Erreur : Pas d'adresse tiers (RFM_validerDecisionOO.generationLettre())");
            }

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    @Override
    public String getAdresse() {
        return adresse;
    }

    public String getAdresseCourrier() throws Exception {

        String adresse = PRTiersHelper.getAdresseCourrierFormatee(getSessionCygnus(), idTiers, "",
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);

        return adresse;
    }

    public String getAnneeFacture() {
        return anneeFacture;
    }

    /*
     * remplir l'assuré concerné
     */
    protected void getAssureConcerne(String idTierAssureConcerne, Collection table) throws Exception {

        // Recherche des nom et prenom de l'idTierAssureConcerne

        PRTiersWrapper tiersConcerne = PRTiersHelper.getTiersAdresseParId(getSession(), idTierAssureConcerne);

        String strAssureDemande = mainDocument.getTextes(11).getTexte(1).getDescription() + " "
                + tiersConcerne.getNom() + " " + tiersConcerne.getPrenom();
        DataList line11 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_ASSURE_DEMANDES);

        line11.addData(IRFGenererDocumentDecision.CAT_TEXTE_ASSURE_DEMANDES, strAssureDemande);

        table.add(line11);

    }

    /*
     * remplir le montant mis à charge RFM
     */
    protected void getChargeRFM(Collection table, RFDemandeValidationData demande, int iterator,
            BigDecimal soldeExcedentRevenu, BigDecimal depassementQd) throws Exception {

        DataList line17 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_MONTANT_RFM);
        table.add(line17);

        line17.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_RFM_TEXTE, mainDocument.getTextes(7).getTexte(1)
                .getDescription());

        // si partie retroactive d'un paiement mensuel on a le montant dans la prestation associée
        // si partie future d'un paiement mensuel il faut rechercher dans la table RFMAccordee -> requete
        // BigDecimal[] RFMAPrest = this.recherchePrestationsMensuelles(this.decisionDocument.getIdDecision());

        String montantAccepte = "";
        if (typePaiement.equals(IRFTypePaiement.PAIEMENT_RETROACTIF)
                || typePaiement.equals(IRFTypePaiement.PAIEMENT_FUTURE)) {

            montantAccepte = new BigDecimal(demande.getMontantAccepte()).add(soldeExcedentRevenu).add(depassementQd)
                    .toString();

        } else {
            if (typePaiement.equals(IRFTypePaiement.PAIEMENT_COURANT)) {
                if (iterator == 1) {
                    montantAccepte = new BigDecimal(decisionDocument.getMontantCourantPartieRetroactive()).add(
                            soldeExcedentRevenu).toString();
                    montantAccepte = new BigDecimal(decisionDocument.getMontantCourantPartieRetroactive()).add(
                            depassementQd).toString();
                }
                if (iterator == 2) {

                    montantAccepte = new BigDecimal(decisionDocument.getMontantTotal()).add(
                            new BigDecimal(decisionDocument.getMontantCourantPartieRetroactive()).negate()).toString();

                }
            }
        }

        line17.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_ACCEPTE,
                new FWCurrency(montantAccepte).toStringFormat());

        // cas d'un paiement future ou courant : on ajoute une ligne d'information pour le montant futur à
        // verser
        if (((typePaiement.equals(IRFTypePaiement.PAIEMENT_COURANT)) && (iterator == 2))
                || (typePaiement.equals(IRFTypePaiement.PAIEMENT_FUTURE))) {
            DataList line18 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_REM_PAIEMENT_MENSUEL);
            table.add(line18);
            line18.addData(IRFGenererDocumentDecision.CAT_TEXTE_PAIEMENT_MENSUEL, PRStringUtils.replaceString(
                    PRStringUtils.replaceString(mainDocument.getTextes(7).getTexte(2).getDescription(),
                            DATE_FIN_PAIEMENT, " " + demande.getDateFinTraitement()), MONTANT_PAIEMENT_MENSUEL,
                    new FWCurrency(decisionDocument.getMontantCourantPartieFuture()).toStringFormat()));
        }
    }

    public String getCopie() {
        return copie;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDatesReception() {
        return datesReception;
    }

    /*
     * remplir la date traitement RFMAPrest : {montantPrestation,montantPrestationAccordee}
     */
    protected String getDatesTraitement(RFDemandeValidationData demande, Collection table, int iteration) {
        String montantPaiement = "";
        String dateFinTraitement = "";
        String dateDebutTraitement = demande.getDateDebutTraitement();
        String strDetailDemande = "";

        // si paiement mensuel et pas de date de fin on met le 31.12 de l'annee QD
        // changer un peu ce qu'on marque dans le cas d'une décision mensuelle.
        if (!JadeStringUtil.isEmpty(demande.getDateFinTraitement())) {
            dateFinTraitement = demande.getDateFinTraitement();
        } else {
            // dateFinTraitement = "31.12." + this.decisionDocument.getAnneeQD();
            dateFinTraitement = "-";
        }

        // traitement particulier en fonction du type de paiement
        if (typePaiement.equals(IRFTypePaiement.PAIEMENT_RETROACTIF)
                || typePaiement.equals(IRFTypePaiement.PAIEMENT_FUTURE)) {

            if (!JadeStringUtil.isBlankOrZero(dateDebutTraitement)) {
                strDetailDemande = mainDocument.getTextes(11).getTexte(2).getDescription() + " " + dateDebutTraitement
                        + " " + mainDocument.getTextes(11).getTexte(3).getDescription() + " " + dateFinTraitement;
            } else {
                strDetailDemande = mainDocument.getTextes(11).getTexte(2).getDescription() + " - ";
            }

            montantPaiement = demande.getMontantFacture();

        } else {
            // paiement courant
            if (typePaiement.equals(IRFTypePaiement.PAIEMENT_COURANT)) {
                String dateDernierPaiement = decisionDocument.getDateFinRetro();
                if (iteration == 1) {
                    // écriture de la partie rétro
                    strDetailDemande = mainDocument.getTextes(11).getTexte(2).getDescription() + " "
                            + dateDebutTraitement + " " + mainDocument.getTextes(11).getTexte(3).getDescription()
                            + " 01." + dateDernierPaiement;
                    montantPaiement = decisionDocument.getMontantCourantPartieRetroactive();
                }
                if (iteration == 2) {
                    // ecriture de la partie future de ce paiement courant
                    strDetailDemande = mainDocument.getTextes(11).getTexte(4).getDescription() + " 01."
                            + dateDernierPaiement;

                    // montantPaiement = this.decisionDocument.getMontantCourantPartieFuture();
                    montantPaiement = new BigDecimal(decisionDocument.getMontantTotal()).add(
                            new BigDecimal(decisionDocument.getMontantCourantPartieRetroactive()).negate()).toString();
                }
            }
        }

        // fin traitement paiement
        DataList line15 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_TRAITEMENT);
        table.add(line15);
        line15.addData(IRFGenererDocumentDecision.CAT_TEXTE_TRAITEMENT, strDetailDemande);
        line15.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_DEMANDES,
                new FWCurrency(montantPaiement).toStringFormat());
        return dateFinTraitement;
    }

    public RFDecisionDocumentData getDecisionDocument() {
        return decisionDocument;
    }

    // @Override
    // public Iterator getCopies() {
    // return this.copies;
    // }

    public JadePublishDocumentInfo getDocInfo() {
        return docInfo;
    }

    /*
     * remplir la facture
     */
    protected void getFacture(RFDemandeValidationData demande, Collection table) {

        String dateFacture = demande.getDateFacture();

        String strDetailDemande = mainDocument.getTextes(11).getTexte(5).getDescription() + " " + dateFacture;
        DataList line13 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_FACTURE);
        table.add(line13);
        line13.addData(IRFGenererDocumentDecision.CAT_TEXTE_FACTURE, strDetailDemande);
        line13.addData("NUM_DEMANDE",
                "(" + mainDocument.getTextes(11).getTexte(8).getDescription() + " " + demande.getIdDemande() + ")");
    }

    private String getFirstNameLastNameGestionnaire(String gestionnaire) throws Exception {

        JadeUser userName;

        try {
            userName = getSessionCygnus().getApplication()._getSecurityManager()
                    .getUserForVisa(getSessionCygnus(), gestionnaire);

            // Si propriété groupeDeLettre à true, on surcharge le numéro par celui du gestionnaire en charge.
            telGestionnaireGrpLettre = userName.getPhone();

        } catch (Exception ex) {
            throw new Exception("Problem with id gestionnaire: [" + gestionnaire + "] /n" + ex.getMessage());
        }
        return userName.getFirstname() + " " + userName.getLastname();
    }

    /*
     * remplir le fournisseur
     */
    protected void getFournisseur(Collection table, PRTiersWrapper tiersWrapperFournisseur,
            RFDemandeValidationData demande) {

        String strDetailDemande = "";

        if (JadeStringUtil.isBlankOrZero(demande.getRemarqueFournisseur())) {
            if (tiersWrapperFournisseur != null) {
                strDetailDemande = tiersWrapperFournisseur.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tiersWrapperFournisseur.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            }
        } else {
            strDetailDemande = demande.getRemarqueFournisseur();
        }

        DataList line14 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_FOURNISSEUR);
        table.add(line14);
        line14.addData(IRFGenererDocumentDecision.CAT_TEXTE_FOURNISSEUR, strDetailDemande);

    }

    private String getGroupeGestionnaire(char premiereLettreNomBeneficiaire) throws Exception {

        // On récupère une map d'interval de lettre (Ex: D-O) par gestionnaire
        Map<String, String> intervalDeLettreParGestMap = RFPropertiesUtils.getIntervalDeLettreParGestionnaire();

        // On détermine à quel interval de lettre appartient la décision
        for (String intervalleKey : intervalDeLettreParGestMap.keySet()) {

            String[] intervalles = intervalleKey.split("-");
            char lettreDebut = intervalles[0].charAt(0);
            char lettreFin = intervalles[1].charAt(0);

            if ((premiereLettreNomBeneficiaire >= lettreDebut) && (premiereLettreNomBeneficiaire <= lettreFin)) {
                return intervalDeLettreParGestMap.get(intervalleKey);
            }
        }
        return "";
    }

    public Boolean getIsCopie() {
        return isCopie;
    }

    public ICTDocument getMainDocument() {
        return mainDocument;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    public String getMessageOv() {
        return messageOv;
    }

    public FWCurrency getMontantCourant() {
        return montantCourant;
    }

    public String getMontantFuture() {
        return montantFuture;
    }

    /*
     * remplir le motif de refus renvoi le montant du motif de refus systeme solde excédent de revenu
     */
    protected BigDecimal[] getMotifsRefus(RFDemandeValidationData demande, Collection table) throws Exception {
        boolean first = true;
        String strMotif = "";
        String strMontantMotif = "";
        BigDecimal soldeExcedentRevenu = new BigDecimal(0);
        BigDecimal depassementQd = new BigDecimal(0);
        BigDecimal sommeMotifDeRefus = new BigDecimal(0);

        // ajout des motifs de refus OMAV (type de soin 5)
        if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(demande.getIdTypeSoin())) {
            ajoutMotifRefusOMAV(demande.getMontantOAI(), table);
        }

        // Gestion des motifs de refus utilisateurs lié à un montant et gestion de l'excédent de revenu
        for (RFMotifRefusDemandeValidationData motifs : demande.getMotifsRefus()) {

            // ne pas ajouter le motif de refus systeme solde excédent de revenu
            if (!IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU.equals(motifs.getIdMotifRefusSysteme())
                    && !IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE.equals(motifs.getIdMotifRefusSysteme())) {

                if (!(JadeStringUtil.isBlankOrZero(motifs.getMontantMotif()) && JadeStringUtil.isBlankOrZero(motifs
                        .getIdMotifRefusSysteme()))) {

                    String description = setDescription(motifs);
                    if (first) {
                        // en fonction de codeIsoLangue
                        strMotif = " - " + description;
                        strMontantMotif = "-" + motifs.getMontantMotif();

                    } else {
                        strMotif = " - " + description;
                        strMontantMotif = "-" + motifs.getMontantMotif();
                    }
                    DataList line16 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_MOTIF_REFUS);
                    table.add(line16);
                    line16.addData(IRFGenererDocumentDecision.CAT_TEXTE_MOTIF_REFUS, strMotif);
                    line16.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_MOTIF,
                            new FWCurrency(strMontantMotif).toStringFormat());
                    strMotif = "";
                    strMontantMotif = "";

                    sommeMotifDeRefus = sommeMotifDeRefus
                            .add(new BigDecimal(motifs.getMontantMotif().replace("'", "")));

                }

            } else {
                if (IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU.equals(motifs.getIdMotifRefusSysteme())) {
                    soldeExcedentRevenu = new BigDecimal(motifs.getMontantMotif());
                } else if (IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE.equals(motifs.getIdMotifRefusSysteme())) {
                    depassementQd = new BigDecimal(motifs.getMontantMotif());
                }
            }
        }
        // Gestion des motifs de refus utilisateurs non lié à un montant
        first = true;
        for (RFMotifRefusDemandeValidationData motifs : demande.getMotifsRefus()) {

            // ne pas ajouter le motif de refus systeme solde excédent de revenu
            if (!IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU.equals(motifs.getIdMotifRefusSysteme())) {

                if (JadeStringUtil.isBlankOrZero(motifs.getMontantMotif())
                        && JadeStringUtil.isBlankOrZero(motifs.getIdMotifRefusSysteme())) {

                    String description = setDescription(motifs);
                    if (first) {
                        // en fonction de codeIsoLangue
                        strMotif = " - " + description;
                        strMontantMotif = "-"
                                + new BigDecimal(demande.getMontantFacture().replace("'", "")).add(
                                        sommeMotifDeRefus.negate()).toString();
                        first = false;

                    } else {
                        strMotif = " - " + description;
                        strMontantMotif = "-0.00";
                    }
                    DataList line16 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_MOTIF_REFUS);
                    table.add(line16);
                    line16.addData(IRFGenererDocumentDecision.CAT_TEXTE_MOTIF_REFUS, strMotif);
                    line16.addData(IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_MOTIF,
                            new FWCurrency(strMontantMotif).toStringFormat());
                    strMotif = "";
                    strMontantMotif = "";
                }
            }
        }
        return new BigDecimal[] { soldeExcedentRevenu, depassementQd };
    }

    /**
     * Retourne le nom prénom du gestionnaire
     * 
     * @return java.lang.String
     * @throws Exception
     */
    public String getNomGestionnaire() throws Exception {
        if (JadeStringUtil.isEmpty(decisionDocument.getGestionnaire())) {
            return "";
        } else {
            return getFirstNameLastNameGestionnaire(decisionDocument.getGestionnaire());
        }
    }

    private String getNomGestionnaireAAfficher(String nomGestionnaireDecision) throws Exception {
        String nomGestionnaireAAfficher = "";
        if (!RFPropertiesUtils.utiliserGroupesGestionnaires()) {
            nomGestionnaireAAfficher = nomGestionnaireDecision;
        } else {
            nomGestionnaireAAfficher = getFirstNameLastNameGestionnaire(getGroupeGestionnaire(nom.substring(0, 1)
                    .toUpperCase().charAt(0)));

        }
        return nomGestionnaireAAfficher;
    }

    public String getNpa() {
        return npa;
    }

    public String getNss() {
        return nss;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public StringBuffer getPdfDecisionURL() {
        return pdfDecisionURL;
    }

    public BSession getSessionCygnus() {
        return sessionCygnus;
    }

    /**
     * Retourne le n° de téléphone du gestionnaire
     * 
     * @return java.lang.String
     * @throws Exception
     */
    public String getTelephoneGestionnaire() throws Exception {
        JadeUser user = getSessionCygnus().getApplication()._getSecurityManager()
                .getUserForVisa(getSessionCygnus(), decisionDocument.getGestionnaire());

        // si le user n'est pas vide, rapatrier les données de détails du user
        if (user != null) {
            return user.getPhone();
        }
        return "";
    }

    public String getTelGestionnaire() {
        return telGestionnaire;
    }

    public String getTexteLibre() {
        return texteLibre;
    }

    @Override
    public String getTitre() {
        return titre;
    }

    @Override
    public String getTitreComplet() {
        if (ALCSAllocataire.TITRE_MONSIEUR.equals(titre)) {
            return getSessionCygnus().getLabel("JSP_RF_DOCUMENT_MONSIEUR_COMPLET");
        } else if (ALCSAllocataire.TITRE_MADAME.equals(titre)) {
            return getSessionCygnus().getLabel("JSP_RF_DOCUMENT_MADAME_COMPLET");
        } else if (ALCSAllocataire.TITRE_MADEMOISELLE.equals(titre)) {
            return getSessionCygnus().getLabel("JSP_RF_DOCUMENT_MADEMOISELLE_COMPLET");
        }
        return "";
    }

    /**
     * Retourne le document de décision complémentaire à imprimer (avec excedent)
     * 
     * @param montantAccepteDemande
     * @param montantExcedantRevenu
     * @return
     */
    private RFTypeDecisionEnum getTypeDecisionComplementaireToPrint(BigDecimal montantAccepteDemande,
            BigDecimal montantExcedantRevenu) {
        if (montantExcedantRevenu.compareTo(new BigDecimal(0)) > 0) {
            if (montantAccepteDemande.compareTo(new BigDecimal(0)) > 0) {
                return RFTypeDecisionEnum.RFM_DECISION_REGIME_AVEC_EXCEDENT_OCTROI;
            } else {
                return RFTypeDecisionEnum.RFM_DECISION_REGIME_AVEC_EXCEDENT_REFUS;
            }

        } else {
            return getTypeDecisionStandardToPrint(montantAccepteDemande);
        }
    }

    /**
     * Retourne le document de décision à imprimer standard (sans excedent)
     * 
     * @param montantAccepteDemande
     * @return
     */
    private RFTypeDecisionEnum getTypeDecisionStandardToPrint(BigDecimal montantAccepteDemande) {

        if (montantAccepteDemande.compareTo(new BigDecimal(0)) > 0) {
            return RFTypeDecisionEnum.RFM_DECISION_REGIME_SANS_EXCEDENT_OCTROI;
        } else {
            return RFTypeDecisionEnum.RFM_DECISION_REGIME_SANS_EXCEDENT_REFUS;
        }
    }

    /*
     * remplir le type de soin
     */
    protected void getTypeDemande(Collection table, RFDemandeValidationData demande) {
        CodeSystem codeSystem = CodeSystemUtils.searchCodeSystemTraduction(demande.getIdTypeSoin(), sessionCygnus,
                codeIsoLangue);
        String strDetailDemande = codeSystem.getTraduction();
        DataList line12 = new DataList(IRFGenererDocumentDecision.CAT_TEXTE_LIGNE_TYPE_DEMANDE);
        table.add(line12);
        line12.addData(IRFGenererDocumentDecision.CAT_TEXTE_TYPE_DEMANDE, strDetailDemande);
    }

    public String getVille() {
        return ville;
    }

    private Boolean hasAnnexes() {
        return decisionDocument.getIsBordereauAccompagnement() || decisionDocument.getIsBulletinVersement()
                || decisionDocument.getIsDecompteRetour();

    }

    private boolean hasRemarqueAvancesSas() {

        boolean hasRemarque = true;

        for (RFDemandeValidationData demandeValCourante : decisionDocument.getDecisionDemande()) {
            if (!(demandeValCourante.getIdTypeSoin().equals(IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13) && demandeValCourante
                    .getIdSousTypeSoin().equals(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_AVANCES))) {
                hasRemarque = false;
            }
        }

        return hasRemarque;
    }

    private void hasTableauVersementEtDecompteCopie(RFCopieDecisionsValidationData... copie) throws Exception {
        // test si il faut ajouter le tableau de versement au tiers en copie
        if ((copie.length == 0) || copie[0].getIsVersement()) {
            ajouterTableauVersementRegime(true);
        } else {
            ajouterTableauVersementRegime(false);
        }

        // test si il faut ajouter le tableau de décompte au tiers en copie
        if ((!decisionDocument.getTypePaiement().equals(IRFTypePaiement.PAIEMENT_FUTURE))
                || ((copie.length != 0) && copie[0].getIsDecompte())) {
            ajouterTableauDecompteRegime(true);
        } else {
            ajouterTableauDecompteRegime(false);
        }
    }

    private void initialiserChampsPaiementDecisionRegime() throws Exception {

        // récupération de la demande associée à cette décision
        RFDemandeValidationData demande = null;
        if (decisionDocument.getDecisionDemande().size() == 1) {
            demande = decisionDocument.getDecisionDemande().get(0);

            // recherche de l'adresse de paiement
            TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(sessionCygnus,
                    sessionCygnus.getCurrentThreadTransaction(), decisionDocument.getIdTiersAdressePaiement(),
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

                adressePaiement = tiAdrPaiBanFor.format(source) + "\n";

            } else {
                adressePaiement = "";
            }

            anneeQD = decisionDocument.getAnneeQD();

            // formatage de la date de décision
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRENCH);
            if (8 == demande.getDateReception().length()) {
                demande.setDateReception((demande.getDateReception().substring(6) + "."
                        + demande.getDateReception().substring(4, 6) + "." + demande.getDateReception().substring(0, 4)));
                if (JadeDateUtil.isGlobazDate(demande.getDateReception())) {
                    Date d = JadeDateUtil.getGlobazDate(demande.getDateReception());
                    dateDemande = df.format(d);
                }
            }

            // initialisation pour décision rétroactive
            if (IRFTypePaiement.PAIEMENT_RETROACTIF.equals(decisionDocument.getTypePaiement())) {
                initialiserDecisionRetroactive(demande, df);

            } else {

                if (!JadeStringUtil.isEmpty(decisionDocument.getDateFinRetro())) {
                    moisFuture = rfGenererDecisionService.getDayAndMonthFormatted(decisionDocument.getDateFinRetro(),
                            codeIsoLangue);
                }

                // initialisation pour décision courante
                if (IRFTypePaiement.PAIEMENT_COURANT.equals(decisionDocument.getTypePaiement())) {
                    initialiserDecisionCourant(demande, df);
                }
            }

            // 1er caractère en maj.
            if (!JadeStringUtil.isEmpty(moisRetroDebut)) {
                moisRetroDebut = moisRetroDebut.substring(0, 1).toUpperCase() + moisRetroDebut.substring(1);
            }

            if (!JadeStringUtil.isEmpty(moisRetroFinMoinsUn)) {
                formatteMoisRetroFinMoinsUn = rfGenererDecisionService.getDayAndMonthFormatted(moisRetroFinMoinsUn,
                        codeIsoLangue);
            }

            // montantAccepte - depassementQD
            if (!JadeStringUtil.isEmpty(decisionDocument.getDepassementQD())
                    && !JadeStringUtil.isEmpty(demande.getMontantAccepte())
                    && JadeNumericUtil.isNumeric(decisionDocument.getDepassementQD())) {
                montantDiminutionQD = new BigDecimal(demande.getMontantAccepte()).add(
                        new BigDecimal(decisionDocument.getDepassementQD()).negate()).toString();
            }

            // si il a une date de fin la meme année que la date de debut
            if (!JadeStringUtil.isEmpty(demande.getDateFinTraitement())
                    && demande.getDateDebutTraitement().substring(6)
                            .equals(demande.getDateFinTraitement().substring(6))) {
                nbMoisTotal = JadeDateUtil.getNbMonthsBetween(demande.getDateDebutTraitement(),
                        demande.getDateFinTraitement()) + 1;
            } else {
                nbMoisTotal = JadeDateUtil.getNbMonthsBetween(demande.getDateDebutTraitement(), "01.12."
                        + demande.getDateDebutTraitement().substring(6)) + 1;
            }

            montantExcedentRevenu = decisionDocument.getExcedentRevenu();
            if (!JadeStringUtil.isBlankOrZero(demande.getMontantAccepte())) {
                montantMensuel = demande.getMontantMensuel();
            } else {
                montantMensuel = "";
            }
            montantRemboursement = demande.getMontantAccepte();

            if (!JadeStringUtil.isEmpty(demande.getDateDebutTraitement())
                    && !JadeStringUtil.isEmpty(moisRetroFinMoinsUn)) {
                nbMois = JadeDateUtil.getNbMonthsBetween(demande.getDateDebutTraitement(),
                        moisRetroFinMoinsUn.toString()) + 1;
            }

            montantFuture = decisionDocument.getMontantCourantPartieFuture();

            if (!JadeStringUtil.isEmpty(montantMensuel)) {

                if (!IRFTypePaiement.PAIEMENT_FUTURE.equals(decisionDocument.getTypePaiement())) {
                    sommeMontantRetro = new BigDecimal(nbMois).multiply(new BigDecimal(montantMensuel)).toString();
                }
            }

            if (!JadeStringUtil.isEmpty(demande.getIdSousTypeSoin())) {
                sousTypeRegime = getSessionCygnus().getCodeLibelle(demande.getIdSousTypeSoin());
            }

            // 1ere lettre en majuscule
            if (!JadeStringUtil.isEmpty(moisFuture)) {
                moisFuture = moisFuture.substring(0, 1).toUpperCase() + moisFuture.substring(1);
            }

            texteLibre = decisionDocument.getTexteRemarque();
            setNumeroDecision(decisionDocument.getNumeroDecision());

            // attention si il n'y a qu'un mois de rétroactif on affiche qu'une ligne
            if (!JadeStringUtil.isEmpty(sommeMontantRetro)) {
                montantTotal = new BigDecimal(sommeMontantRetro).toString();
            }
            if (nbMois > 0) {
                decisionMensuelleUnMois = false;
            }
            if (IRFTypePaiement.PAIEMENT_RETROACTIF.equals(decisionDocument.getTypePaiement())) {
                decisionMensuelleRetro = true;
            } else {
                if (!JadeStringUtil.isEmpty(montantTotal) && !JadeStringUtil.isEmpty(montantMensuel)) {
                    montantTotal = (new BigDecimal(montantTotal).add(new BigDecimal(montantMensuel))).toString();
                } else {
                    montantTotal = montantMensuel;
                }
            }

        } else {
            throw new Exception("initialiserChampsPaiementMensuelRegime: demande non trouvée");
        }

    }

    private void initialiserDecisionCourant(RFDemandeValidationData demande, DateFormat df) throws JAException {
        if (!JadeStringUtil.isEmpty(decisionDocument.getDateDebutRetro())) {
            moisRetroDebut = rfGenererDecisionService.getDayAndMonthFormatted(decisionDocument.getDateDebutRetro(),
                    codeIsoLangue);
        }
        if (!JadeStringUtil.isEmpty(decisionDocument.getDateFinRetro())) {
            moisRetroFinMoinsUn = JadeDateUtil.addMonths(decisionDocument.getDateFinRetro(), -1);
        }

    }

    private void initialiserDecisionRetroactive(RFDemandeValidationData demande, DateFormat df) throws Exception {
        moisRetroDebut = df.format(JadeDateUtil.getGlobazDate(demande.getDateDebutTraitement())).substring(2);

        // pas vide et la meme année
        if (!JadeStringUtil.isEmpty(demande.getDateFinTraitement())
                && PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demande.getDateFinTraitement()).equals(anneeQD)) {
            moisRetroFinMoinsUn = demande.getDateFinTraitement();
        } else {
            moisRetroFinMoinsUn = "01.12."
                    + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demande.getDateDebutTraitement());
        }

        if (!JadeStringUtil.isEmpty(demande.getDateFinTraitement())) {
            moisFuture = df.format(JadeDateUtil.getGlobazDate(demande.getDateFinTraitement())).substring(2);
        } else {
            moisFuture = df
                    .format(JadeDateUtil.getGlobazDate("01.12." + demande.getDateDebutTraitement().substring(6)))
                    .substring(2);
        }

    }

    /**
     * Methode pour permettre de tronquer sur 20 positions le nom/prenom affiché dans la ligne technique
     * 
     * @param origin
     * @param size
     * @return
     */
    private String limiteStringSize(String origin, int size) {
        StringBuilder s = new StringBuilder(origin);
        s.setLength(size);
        return s.toString().trim();
    }

    /**
     * Methode pour charger le tableau de decompte d'une décision de restitution
     */
    private void loadTableauDecisionRestitution() throws Exception {
        // Chargement du template dans le document
        data.addData("isTableauRestitutionInclude", "STANDARD");

        montantTotalDu = new BigDecimal(0);
        montantTotalVersee = new BigDecimal(0);

        // Création d'une Collection pour l'ensemble du tableau
        Collection tabDecompteRestitutionRFM = new Collection(
                IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_DECOMPTE_RESTITUTION);

        // Blocs du tableau de décompte des décisions
        // ******************************************
        buildTabDecisionDues(tabDecompteRestitutionRFM);

        buildTabDecisionsVersees(tabDecompteRestitutionRFM);

        buildTabTotalDecisions(tabDecompteRestitutionRFM);

        // Insertion de la collection au data
        data.add(tabDecompteRestitutionRFM);
    }

    protected void recupererDates(StringBuilder formatedDatesReception) throws Exception {

        String dateDebutTraitementFormate = "";

        Set<String> datesDemandes = new HashSet<String>();

        for (RFDemandeValidationData demande : decisionDocument.getDecisionDemande()) {
            datesDemandes.add(demande.getDateReception());
            if (!JadeStringUtil.isBlankOrZero(demande.getDateDebutTraitement())) {
                dateDebutTraitementFormate = demande.getDateDebutTraitement();
            } else {
                dateDebutTraitementFormate = demande.getDateFacture();
            }

        }

        int i = 0;
        for (String dateReception : datesDemandes) {

            i++;
            formatedDatesReception.append(JACalendar.format(dateReception, codeIsoLangue));

            if (datesDemandes.size() > i) {
                formatedDatesReception.append(", ");
            }
        }

        Date dateSurDocument = JadeDateUtil.getGlobazDate(this.dateSurDocument);

        if (JadeDateUtil.isGlobazDate(decisionDocument.getDateDecision_JourMoisAnnee())) {
            dateSurDocument = JadeDateUtil.getGlobazDate(this.dateSurDocument);
        }

        if (dateSurDocument != null) {
            // Set la date jour+mois+annee identique à celle saisie dans l'écran
            decisionDocument.setDateDecision_JourMoisAnnee(JACalendar.format(this.dateSurDocument, codeIsoLangue));

            // Set la date tronqué mois+annee identique à celle saisie dans l'écran
            decisionDocument.setDateDecision_MoisAnnee(rfGenererDecisionService.getDayAndMonthFormatted(
                    this.dateSurDocument, codeIsoLangue));
        }
        // Set la date modifié
        decisionDocument.setDateDebutTraitementFormate(rfGenererDecisionService.getDateFormatterWithLanguage(
                dateDebutTraitementFormate, codeIsoLangue));

    }

    /**
     * Methode pour insérer les valeurs dans le document de la décision de régime standard
     * 
     * @param container
     * @param miseEnGed
     * @param formatedDatesReception
     * @param copie
     * @throws Exception
     */
    protected void remplirCorpsDocumentDecisionMensuelleRegime(JadePrintDocumentContainer container, boolean miseEnGed,
            StringBuilder formatedDatesReception, String formulePolitesse, RFCopieDecisionsValidationData... copie)
            throws Exception {

        // resumé décision
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RESUME_DECISION_LIGNE1, PRStringUtils.replaceString(
                PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument.getTextes(13).getTexte(1)
                        .getDescription(), IRFGenererDocumentDecision.ANNEE_FACTURE, decisionDocument.getAnneeQD()),
                        IRFGenererDocumentDecision.NUMERO_DECISION, decisionDocument.getNumeroDecision()),
                IRFGenererDocumentDecision.DATE_DECISION, decisionDocument.getDateDecision_JourMoisAnnee()));

        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_RESUME_DECISION_LIGNE2, PRStringUtils.replaceString(
                PRStringUtils.replaceString(PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument
                        .getTextes(13).getTexte(2).getDescription(), IRFGenererDocumentDecision.TIERS_NSS, nss),
                        IRFGenererDocumentDecision.TIERS_NOM, nom), IRFGenererDocumentDecision.TIERS_PRENOM, prenom),
                IRFGenererDocumentDecision.DATE_NAISSANCE, dateNaissance));

        // Gestionnaire
        String nomGestionnaireAAfficher = getNomGestionnaireAAfficher(nomGestionnaire);
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_GESTIONNAIRE, PRStringUtils.replaceString(mainDocument
                .getTextes(1).getTexte(1).getDescription(), IRFGenererDocumentDecision.GESTIONNAIRE_NOM,
                nomGestionnaireAAfficher));

        // Telephone gestionnaire
        if (!JadeStringUtil.isEmpty(telGestionnaire)) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE, PRStringUtils.replaceString(
                    mainDocument.getTextes(1).getTexte(2).getDescription(),
                    IRFGenererDocumentDecision.GESTIONNAIRE_TELEPHONE, telGestionnaire));
        }

        // Adresse du bénéficiaire
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE, PRStringUtils.replaceString(mainDocument
                .getTextes(2).getTexte(1).getDescription(), IRFGenererDocumentDecision.ADRESSE, adresse));

        // Texte 'Copie'
        if (isCopie) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_IS_COPIE, mainDocument.getTextes(3).getTexte(1)
                    .getDescription());
        }

        // Numéro de décision
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_NUMERO_DECISION, PRStringUtils.replaceString(mainDocument
                .getTextes(3).getTexte(2).getDescription(), IRFGenererDocumentDecision.NUMERO_DECISION,
                getNumeroDecision()));

        // Nss
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_NSS, PRStringUtils.replaceString(mainDocument.getTextes(3)
                .getTexte(3).getDescription(), IRFGenererDocumentDecision.TIERS_NSS, nss));

        // Ayant droit
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_AYANT_DROIT, PRStringUtils.replaceString(PRStringUtils
                .replaceString(mainDocument.getTextes(3).getTexte(4).getDescription(),
                        IRFGenererDocumentDecision.TIERS_NOM, nom), IRFGenererDocumentDecision.TIERS_PRENOM, prenom));

        // Date décision
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_DATE_DECISION, PRStringUtils.replaceString(mainDocument
                .getTextes(3).getTexte(5).getDescription(), IRFGenererDocumentDecision.DATE_DECISION,
                decisionDocument.getDateDecision_JourMoisAnnee()));

        // Titre
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE, PRStringUtils.replaceString(mainDocument.getTextes(4)
                .getTexte(1).getDescription(), IRFGenererDocumentDecision.TIERS_TITRE, formulePolitesse));

        // Paragraphe 1
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE1, PRStringUtils.replaceString(mainDocument
                .getTextes(4).getTexte(2).getDescription(), IRFGenererDocumentDecision.DATE_DEMANDE, dateDemande));

        // Paragraphe 2
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE2, mainDocument.getTextes(4).getTexte(3)
                .getDescription());

        // Paragraphe 3
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE3, PRStringUtils.replaceString(PRStringUtils
                .replaceString(mainDocument.getTextes(4).getTexte(4).getDescription(),
                        IRFGenererDocumentDecision.INTITULE_SOUS_TYPE_REGIME, sousTypeRegime),
                IRFGenererDocumentDecision.MONTANT_REMBOURSEMENT,
                formatter.format(Double.valueOf(montantRemboursement))));

        // Paragraphe 3 si excédent de revenu différent de Frs 0.-
        if (!JadeStringUtil.isBlankOrZero(montantExcedentRevenu)) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE3_EXCEDENT, PRStringUtils.replaceString(
                    mainDocument.getTextes(4).getTexte(5).getDescription(),
                    IRFGenererDocumentDecision.MONTANT_EXCEDENT_REVENU_PARAGRAPHE, montantExcedentRevenu));
        }

        // Paragraphe 4
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE4, PRStringUtils.replaceString(PRStringUtils
                .replaceString(mainDocument.getTextes(4).getTexte(6).getDescription(),
                        IRFGenererDocumentDecision.MONTANT_MENSUEL, formatter.format(Double.valueOf(montantMensuel))),
                IRFGenererDocumentDecision.DATE_PREMIER_VERSEMENT, decisionDocument.getDateDebutTraitementFormate()));

        // Paragraphe 'Remarque' (si ce n'est pas une copie pour autre destinataire, ou que la copie n'a pas de
        // remarque)
        if ((copie.length == 0) || copie[0].getHasRemarque()) {

            // Ajout du titre "Remarque" si l'une des 2 conditions est à true
            if ((decisionDocument.getIsBordereauAccompagnement()) || (!JadeStringUtil.isEmpty(texteLibre))) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_REMARQUE, mainDocument.getTextes(7).getTexte(1)
                        .getDescription());
            }

            // Création d'une variable pour insérer le(s) texte(s)
            String textesRemarque = "";

            // Ajout du texte fixe si la case bordereau est cochée
            if (decisionDocument.getIsBordereauAccompagnement()) {
                textesRemarque = mainDocument.getTextes(7).getTexte(3).getDescription();

            }
            // Ajout du texte libre si présent
            if (!JadeStringUtil.isEmpty(texteLibre)) {
                // Concaténation des 2 textes si le premier est renseigné
                if (!textesRemarque.isEmpty()) {
                    textesRemarque = textesRemarque + "\n" + texteLibre;
                }
                // Sinon, ajout uniquement du texte libre
                else {
                    textesRemarque = texteLibre;
                }
            }

            // Insertion des textes si l'une des condition est a true
            if ((decisionDocument.getIsBordereauAccompagnement()) || (!JadeStringUtil.isEmpty(texteLibre))) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE, PRStringUtils.replaceString(mainDocument
                        .getTextes(7).getTexte(2).getDescription(), IRFGenererDocumentDecision.TEXTE_LIBRE,
                        textesRemarque));
            }
        }

        // Information à l'assuré
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_INFOS, mainDocument.getTextes(8).getTexte(1)
                .getDescription());
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE_INFO1, PRStringUtils.replaceString(PRStringUtils
                .replaceString(PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument.getTextes(8)
                        .getTexte(2).getDescription(), IRFGenererDocumentDecision.ANNEE_QD, anneeQD),
                        IRFGenererDocumentDecision.MONTANT_DIMINUTION_QD, formatter.format(Double
                                .valueOf(montantDiminutionQD))), IRFGenererDocumentDecision.NB_MOIS_TOTAL, new Integer(
                        nbMoisTotal).toString()), IRFGenererDocumentDecision.MONTANT_MENSUEL, formatter.format(Double
                .valueOf(montantMensuel))));
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE_INFO2, mainDocument.getTextes(8).getTexte(3)
                .getDescription());
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE_INFO3, mainDocument.getTextes(8).getTexte(4)
                .getDescription());

        // Moyens de droit
        if ((copie.length == 0) || copie[0].getHasMoyensDroit()) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_MOYEN_DROIT, mainDocument.getTextes(9).getTexte(1)
                    .getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_MOYEN_DROIT, mainDocument.getTextes(9).getTexte(2)
                    .getDescription());
        }

        // Salutations
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_SALUTATIONS, PRStringUtils.replaceString(mainDocument
                .getTextes(10).getTexte(1).getDescription(), IRFGenererDocumentDecision.TIERS_TITRE, formulePolitesse));

        // ------------------------------------------------------------------------------------------------------------

        // Création d'une nouvelle instance pour la publication dans le docInfo
        docInfo = new JadePublishDocumentInfo();
        docInfo.setPublishDocument(false);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME);
        docInfo.setDocumentType(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME);
        docInfo.setDocumentProperty("annee", JADate.getYear(dateSurDocument).toString());

        // Récupération de la date sur document pour indexation GED
        if (dateSurDocument.isEmpty()) {
            dateSurDocument = decisionDocument.getDateDecision_JourMoisAnnee();
        }
        docInfo.setDocumentDate(dateSurDocument.replace(".", ""));

        if (RFPropertiesUtils.imprimerDecisionsRectoVerso()) {
            docInfo.setDuplex(Boolean.TRUE);
            docInfo.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
        }

        // Si propriété à TRUE, on set des informations supplémentaires dans la ligne technique pour permettre aux
        // client de regrouper les documents pour la mise sous pli
        if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {

            docInfo.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                    IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME);
            docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "O");
            docInfo.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS", nss);
            docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", decisionDocument.getIdTiers());
            docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", decisionDocument.getIdDecision());
            docInfo.setDocumentProperty("AGLA_LT_PCRFM_NOM_TIERS",
                    limiteStringSize(nom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));
            docInfo.setDocumentProperty("AGLA_LT_PCRFM_PRENOM_TIERS",
                    limiteStringSize(prenom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));

            // Si il s'agit d'une copie pour autre destinataire, on écrase les info de la ligne technique, par les
            // info du destinataire
            if (copie.length > 0) {
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "C");
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", copie[0].getIdDestinataire());
            }

        }

        // Mise en GED si les documents sont générés uniquement lors de la validation des décisions
        if (miseEnGed) {
            // Mise en GED du document si ce n'est pas une copie
            if (copie.length == 0) {
                if (PRGedUtils.isDocumentInGed(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME, session)) {
                    docInfo.setArchiveDocument(true);
                }
            }

        }

        TIDocumentInfoHelper.fill(docInfo, getIdTiers(), getSession(), null, null, null);

        // Ajout du document dans le container
        container.addDocument(data, docInfo);
    }

    /**
     * Methode pour insérer les valeurs dans le document de la décision de régime avec excedent - octroi
     * 
     * @param demande
     * @param miseEnGed
     * @param formatedDatesReception
     * @param copie
     * @throws Exception
     */
    protected void remplirCorpsDocumentDecisionMensuelleRegimeAvecExcedantOctroi(JadePrintDocumentContainer container,
            RFDemandeValidationData demande, boolean miseEnGed, StringBuilder formatedDatesReception,
            String formulePolitesse, RFCopieDecisionsValidationData... copie) throws Exception {
        try {
            // Appel du CaisseHeaderReport pour utiliser les fichiers de properties
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                    getSessionCygnus().getApplication(), codeIsoLangue);
            caisseHelper.setTemplateName(RFGenererDecisionMainService.FICHIER_MODELE_DOCUMENT_DECISION_RFM);

            // adresse
            crBean.setAdresse(adresse);
            // this.data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE, this.adresse);

            // Affihcer le libellé : personne de référence
            if (RFPropertiesUtils.afficherLibellePersonneReference()) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIBELLE_REFERENCE, PRStringUtils.replaceString(
                        mainDocument.getTextes(1).getTexte(1).getDescription(),
                        IRFGenererDocumentDecision.GESTIONNAIRE_NOM, getNomGestionnaire()));
            }

            // telephone gestionnaire
            // Affichage si présence d'un numéro
            if (!JadeStringUtil.isEmpty(getTelephoneGestionnaire())) {

                if (RFPropertiesUtils.afficherTelephoneGestionnaire()) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE, mainDocument.getTextes(1)
                            .getTexte(2).getDescription()
                            + " " + getTelGestionnaire());
                }
            }

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            // Envoi du crBean au data
            data = caisseHelper.addHeaderParameters(data, crBean, isCopie);

            // copie
            if (isCopie) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_IS_COPIE, mainDocument.getTextes(1).getTexte(3)
                        .getDescription());
            }

            // nss
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_NSS, PRStringUtils.replaceString(mainDocument
                    .getTextes(1).getTexte(4).getDescription(), IRFGenererDocumentDecision.TIERS_NSS, nss));

            // ayant droit
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_AYANT_DROIT,
                    PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument.getTextes(1).getTexte(5)
                            .getDescription(), IRFGenererDocumentDecision.TIERS_NOM, nom),
                            IRFGenererDocumentDecision.TIERS_PRENOM, prenom));

            // Titre document
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECISION, mainDocument.getTextes(2).getTexte(1)
                    .getDescription());

            // Titre tiers
            String formulePolitesseWithComma = addCommaIFFrench(formulePolitesse);
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE, formulePolitesseWithComma);

            // paragraphe 1
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE1, PRStringUtils.replaceString(mainDocument
                    .getTextes(3).getTexte(1).getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_DATE_DEMANDE,
                    rfGenererDecisionService.getDateFormatterWithLanguage(demande.getDateReception(), codeIsoLangue)));

            // paragraphe 2
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE2, mainDocument.getTextes(3).getTexte(2)
                    .getDescription());

            // paragraphe 3
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE3, mainDocument.getTextes(3).getTexte(3)
                    .getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE3_GRAS, PRStringUtils.replaceString(
                    mainDocument.getTextes(3).getTexte(4).getDescription(),
                    IRFGenererDocumentDecision.CAT_TEXTE_DATE_DEBUT_OCTROI, rfGenererDecisionService
                            .getDateFormatterWithLanguage(demande.getDateDebutTraitement(), codeIsoLangue)));

            // paragraphe 4
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE4, mainDocument.getTextes(3).getTexte(5)
                    .getDescription());

            // tableau de calcul : Création d'une Collection pour l'ensemble du tableau
            // Chargement du template dans le document
            data.addData("isTableauCalculDecisionRegimeOctroiInclude", "STANDARD");

            // Insertion du titre
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_CALCUL, mainDocument.getTextes(4).getTexte(1)
                    .getDescription());

            // Insertion valeur monétaire
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF,
                    mainDocument.getTextes(4).getTexte(2).getDescription());

            // Récupération des montants
            BigDecimal montantFacture = new BigDecimal(demande.getMontantFacture());
            BigDecimal montantAccepte = new BigDecimal(demande.getMontantAccepte());
            BigDecimal montantExcedentRevenu = montantFacture.subtract(montantAccepte);

            // 1ère ligne du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PREMIERE_LIGNE_CALCUL_OCTROI, mainDocument.getTextes(4)
                    .getTexte(3).getDescription());
            data.addData(IRFGenererDocumentDecision.MONTANT_FACTURE, formatter.format(montantFacture.doubleValue()));

            // 2ème ligne du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_DEUXIEME_LIGNE_CALCUL_OCTROI, mainDocument.getTextes(4)
                    .getTexte(4).getDescription());
            data.addData(IRFGenererDocumentDecision.MONTANT_EXCEDENT_REVENU,
                    formatter.format(montantExcedentRevenu.doubleValue()));

            // 3ème ligne du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TROISIEME_LIGNE_CALCUL_OCTROI, mainDocument.getTextes(4)
                    .getTexte(5).getDescription());
            data.addData(IRFGenererDocumentDecision.MONTANT_INDEMNITE_ANNUELLE,
                    formatter.format(montantAccepte.doubleValue()));

            // 4ème ligne du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_QUATRIEME_LIGNE_CALCUL_OCTROI, PRStringUtils
                    .replaceString(mainDocument.getTextes(4).getTexte(6).getDescription(),
                            IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_NBR_MOIS_DECOMPTE_DECISION_OCTROI,
                            Integer.toString(nbMoisTotal/* nbrMoisTraitement */)));
            data.addData(IRFGenererDocumentDecision.MONTANT_INDEMNITE_MENSUELLE, demande.getMontantMensuel());

            // Chargement du template dans le document
            if (JadeStringUtil.isBlankOrZero(decisionDocument.getMontantCourantPartieRetroactive())) {
                data.addData("isTableauDecompteDecisionRegimeAvecExcedantOctroiInclude", "NONE");
            } else {

                // paragraphe 5
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE5, mainDocument.getTextes(5).getTexte(1)
                        .getDescription());

                // tableau de decompte : Création d'une Collection pour l'ensemble du tableau
                Collection tabDecompteDecisionOctroi = new Collection(
                        IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_DECOMPTE_DECISION_AVEC_EXCEDANT_OCTROI);

                // Controle du type de prestation selon dates de traitement : retro / retro+courant / futur
                // Création de boolean pour définir quelles lignes doivent être affichées
                boolean isPurementRetro = false;
                boolean isPurementFutur = false;
                // Si dateDebutRetro ou dateFinRetro vide, il s'agit d'un pure retro ou d'un pure futur
                if (JadeStringUtil.isBlankOrZero(decisionDocument.getDateDebutRetro())
                        || JadeStringUtil.isBlankOrZero(decisionDocument.getDateFinRetro())) {
                    // Si date debutTraitement après dateDecision, il s'agit d'un pure futur
                    if (JadeDateUtil.isDateAfter(decisionDocument.getDecisionDemande().get(0).getDateDebutTraitement(),
                            decisionDocument.getDecisionDemande().get(0).getDateReception())) {
                        isPurementFutur = true;
                    }
                    // Sinon, il s'agit d'un pure retro.
                    else {
                        isPurementRetro = true;
                    }
                }

                data.addData("isTableauDecompteDecisionRegimeAvecExcedantOctroiInclude", "STANDARD");

                // Insertion des titres du tableau
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECOMPTE_DECISION_OCTROI, mainDocument
                        .getTextes(6).getTexte(1).getDescription());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_PERIODE_DECOMPTE_DECISION_OCTROI, mainDocument
                        .getTextes(6).getTexte(2).getDescription());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_NBR_MOIS_DECOMPTE_DECISION_OCTROI, mainDocument
                        .getTextes(6).getTexte(3).getDescription());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_MONTANT_MENSUEL_DECOMPTE_DECISION_OCTROI,
                        mainDocument.getTextes(6).getTexte(4).getDescription());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_MONTANT_TOTAL_DECOMPTE_DECISION_OCTROI,
                        mainDocument.getTextes(6).getTexte(5).getDescription());

                // Calcul des périodes concernées.
                BigDecimal nbrMois = new BigDecimal(0);
                // Si ce n'est pas une décision purement retroactive ou purement futur, on se base sur les dates de la
                // décision.
                if (!isPurementRetro && !isPurementFutur) {
                    nbrMois = new BigDecimal(JadeDateUtil.getNbMonthsBetween(decisionDocument.getDateDebutRetro(),
                            decisionDocument.getDateFinRetro()));
                }
                // Sinon, on se base sur les date de la demande.
                else {
                    nbrMois = new BigDecimal(JadeDateUtil.getNbMonthsBetween(
                            decisionDocument.getDecisionDemande().get(0).getDateDebutTraitement(), decisionDocument
                                    .getDecisionDemande().get(0).getDateFinTraitement()) + 1);
                }

                // Récupération des montants
                BigDecimal montantMensuelMultiplieParNbMois = nbrMois.multiply(new BigDecimal(decisionDocument
                        .getMontantCourantPartieFuture()));
                // Montant à rembourser à un tiers
                BigDecimal montantRemboursementTiers = new BigDecimal(0);
                montantRemboursementTiers = montantRemboursementTiers.add(new BigDecimal(decisionDocument
                        .getMontantDette()));

                // Insertion de la 1ère ligne du tableau
                if (!isPurementFutur) {
                    DataList dataListDecompteRetro = new DataList(
                            IRFGenererDocumentDecision.LIGNE_RETRO_TABLEAU_DECOMPTE_DECISION_OCTROI);

                    dataListDecompteRetro.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_RETROACTIF_DECOMPTE_DECISION_OCTROI, mainDocument
                                    .getTextes(6).getTexte(6).getDescription());
                    String dateFinRetro = PRStringUtils.replaceString(mainDocument.getTextes(6).getTexte(7)
                            .getDescription(),
                            IRFGenererDocumentDecision.CAT_TEXTE_DATE_DEBUT_RETROACTIF_DECOMPTE_DECISION_OCTROI,
                            moisRetroDebut.toLowerCase());
                    dataListDecompteRetro.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_PERIODE_RETROACTIF_DECOMPTE_DECISION_OCTROI,
                            PRStringUtils.replaceString(dateFinRetro,
                                    IRFGenererDocumentDecision.CAT_TEXTE_DATE_FIN_RETROACTIF_DECOMPTE_DECISION_OCTROI,
                                    formatteMoisRetroFinMoinsUn.toLowerCase()));
                    dataListDecompteRetro.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_NBR_MOIS_RETROACTIF_DECOMPTE_DECISION_OCTROI,
                            nbrMois.toString());
                    dataListDecompteRetro.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_MENSUEL_RETROACTIF_DECOMPTE_DECISION_OCTROI,
                            decisionDocument.getMontantCourantPartieFuture());
                    dataListDecompteRetro.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_TOTAL_RETROACTIF_DECOMPTE_DECISION_OCTROI,
                            formatter.format(montantMensuelMultiplieParNbMois.doubleValue()));

                    tabDecompteDecisionOctroi.add(dataListDecompteRetro);
                }

                // Insertion 2ème ligne du tableau
                if (!isPurementFutur) {
                    DataList dataListRemboursementTiers = new DataList(
                            IRFGenererDocumentDecision.LIGNE_REMBOURSEMENT_TABLEAU_DECOMPTE_DECISION_OCTROI);

                    dataListRemboursementTiers.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_REMBOURSEMENT_TIERS_DECOMPTE_DECISION_OCTROI,
                            mainDocument.getTextes(6).getTexte(8).getDescription());
                    dataListRemboursementTiers.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_REMBOURSEMENT_TIERS_DECOMPTE_DECISION_OCTROI,
                            formatter.format(montantRemboursementTiers.doubleValue()));

                    tabDecompteDecisionOctroi.add(dataListRemboursementTiers);
                }

                // Insertion 3ème ligne du tableau
                if (!isPurementRetro && !isPurementFutur) {
                    DataList dataListMoisCourant = new DataList(
                            IRFGenererDocumentDecision.LIGNE_MOIS_COURANT_TABLEAU_DECOMPTE_DECISION_OCTROI);

                    dataListMoisCourant.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_TEXTE_MOIS_COURANT_DECOMPTE_DECISION_OCTROI,
                            mainDocument.getTextes(6).getTexte(9).getDescription());
                    dataListMoisCourant.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_MOIS_COURANT_DECOMPTE_DECISION_OCTROI, moisFuture);

                    dataListMoisCourant.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_NBR_MOIS_COURANT_DECOMPTE_DECISION_OCTROI, "1");
                    dataListMoisCourant.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_MENSUEL_MOIS_COURANT_DECOMPTE_DECISION_OCTROI,
                            decisionDocument.getMontantCourantPartieFuture());
                    dataListMoisCourant.addData(
                            IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_TOTAL_MOIS_COURANT_DECOMPTE_DECISION_OCTROI,
                            decisionDocument.getMontantCourantPartieFuture());

                    tabDecompteDecisionOctroi.add(dataListMoisCourant);

                }

                // Addition des montants
                // Insertion du montant retroactif
                BigDecimal montantTotal = new BigDecimal(0);
                if (isPurementRetro || !isPurementFutur) {
                    montantTotal = montantTotal.add(montantMensuelMultiplieParNbMois);
                }

                // Insertion du montant courant
                if (!isPurementRetro && !isPurementFutur) {
                    montantTotal = montantTotal.add(new BigDecimal(decisionDocument.getMontantCourantPartieFuture()));
                }
                // Sustraction du montant remboursé à un tiers
                montantTotal = montantTotal.subtract(montantRemboursementTiers);

                // Insertion 4ème ligne du tableau
                DataList dataListTotal = new DataList(
                        IRFGenererDocumentDecision.LIGNE_MONTANT_TOTAL_TABLEAU_DECOMPTE_DECISION_OCTROI);

                dataListTotal.addData(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_TOTAL_DECOMPTE_DECISION_OCTROI,
                        PRStringUtils.replaceString(mainDocument.getTextes(6).getTexte(10).getDescription(),
                                IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_NBR_MOIS_DECOMPTE_DECISION_OCTROI,
                                Integer.toString(nbMoisTotal/* nbrMoisTraitement */)));
                dataListTotal.addData(
                        IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_VERSEMENT_TOTAL_DECOMPTE_DECISION_OCTROI,
                        formatter.format(montantTotal.doubleValue()));

                tabDecompteDecisionOctroi.add(dataListTotal);

                // Ajout du tableau au data
                data.add(tabDecompteDecisionOctroi);

            }

            // Paragraphe 6
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE6, PRStringUtils.replaceString(mainDocument
                    .getTextes(7).getTexte(1).getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_NOM_CAISSE,
                    mainDocument.getTextes(7).getTexte(5).getDescription()));

            // tableau de versement
            // Chargement du template dans le document
            data.addData("isTableauVersementDecisionRegimeAvecExcedantOctroiInclude", "STANDARD");

            // Insertion des titre du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_VERSEMENT_DECISION_OCTROI, mainDocument
                    .getTextes(7).getTexte(2).getDescription());
            // Insertion du texte
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_DECISION_OCTROI, mainDocument.getTextes(7)
                    .getTexte(3).getDescription());
            // Insertion de la ligne
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_VERSEMENT_A_DECISION_OCTROI, mainDocument.getTextes(7)
                    .getTexte(4).getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE_VERSEMENT_DECISION_OCTROI, adresse);
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE_PAIEMENT_VERSEMENT_DECISION_OCTROI,
                    adressePaiement);

            // paragraphe 7
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE7_TITRE, mainDocument.getTextes(8).getTexte(1)
                    .getDescription());
            String paragraphe7anneeQd = PRStringUtils.replaceString(mainDocument.getTextes(8).getTexte(2)
                    .getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_ANNEE_QD_PARAGRAPHE7_DECISION_OCTROI,
                    decisionDocument.getAnneeQD());
            String paragraphe7MontantAnnuel = PRStringUtils.replaceString(paragraphe7anneeQd,
                    IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_ANNUEL_PARAGRAPHE7_DECISION_OCTROI,
                    montantAccepte.toString());
            String paragraphe7montantMensuel = PRStringUtils.replaceString(paragraphe7MontantAnnuel,
                    IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_MENSUEL_PARAGRAPHE7_DECISION_OCTROI,
                    decisionDocument.getMontantCourantPartieFuture());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE7_TEXTE, PRStringUtils.replaceString(
                    paragraphe7montantMensuel,
                    IRFGenererDocumentDecision.CAT_TEXTE_NBR_MOIS_INDEMNITE_MENSUEL_PARAGRAPHE7_DECISION_OCTROI,
                    Integer.toString(nbMoisTotal)));

            // paragraphe 8
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE8, mainDocument.getTextes(8).getTexte(3)
                    .getDescription());

            // paragraphe 9
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE9, mainDocument.getTextes(8).getTexte(4)
                    .getDescription());

            // paragraphe 10 : Salutations
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_SALUTATIONS, PRStringUtils.replaceString(mainDocument
                    .getTextes(8).getTexte(5).getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_TITRE_TIERS,
                    formulePolitesse));

            // paragraphe 11
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE11_TITRE, mainDocument.getTextes(9).getTexte(1)
                    .getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE11_TEXTE, mainDocument.getTextes(9).getTexte(2)
                    .getDescription());

            // ------------------------------------------------------------------------------------------------------

            // Création d'une nouvelle instance pour la publication dans le docInfo
            docInfo = new JadePublishDocumentInfo();
            docInfo.setPublishDocument(false);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI);
            docInfo.setDocumentType(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI);
            docInfo.setDocumentProperty("annee", JADate.getYear(dateSurDocument).toString());

            // Récupération de la date sur document pour indexation GED
            if (dateSurDocument.isEmpty()) {
                dateSurDocument = decisionDocument.getDateDecision_JourMoisAnnee();
            }
            docInfo.setDocumentDate(dateSurDocument.replace(".", ""));

            if (RFPropertiesUtils.imprimerDecisionsRectoVerso()) {
                docInfo.setDuplex(Boolean.TRUE);
                docInfo.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
            }

            // Si propriété à TRUE, on set des informations supplémentaires dans la ligne technique pour permettre aux
            // client de regrouper les documents pour la mise sous pli
            if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                        IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "O");
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS", nss);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", decisionDocument.getIdTiers());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", decisionDocument.getIdDecision());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NOM_TIERS",
                        limiteStringSize(nom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_PRENOM_TIERS", prenom);

                // Si il s'agit d'une copie pour autre destinataire, on écrase les info de la ligne technique, par les
                // info du destinataire
                if (copie.length > 0) {
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "C");
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", copie[0].getIdDestinataire());
                }
            }

            // Mise en GED des documents uniquement lors de la validation des décisions
            if (miseEnGed) {
                // Mise en GED du document si ce n'est pas une copie
                if (copie.length == 0) {
                    if (PRGedUtils.isDocumentInGed(
                            IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI, session)) {
                        docInfo.setArchiveDocument(true);
                    }
                }
            }

            TIDocumentInfoHelper.fill(docInfo, getIdTiers(), getSession(), null, null, null);

            // Ajout du document dans le container
            container.addDocument(data, docInfo);

        } catch (Exception e) {
            throw new Exception(
                    "RFGenererDecisionMainService.remplirCorpsDocumentDecisionMensuelleRegimeAvecExcedantOctroi(): "
                            + e.getMessage());
        }
    }

    /**
     * Methode pour insérer les valeurs dans le document de la décision avec excedent - refus
     * 
     * @param demande
     * @param miseEnGed
     * @param formatedDatesReception
     * @param copie
     * @throws Exception
     */
    protected void remplirCorpsDocumentDecisionMensuelleRegimeAvecExcedantRefus(JadePrintDocumentContainer container,
            RFDemandeValidationData demande, boolean miseEnGed, StringBuilder formatedDatesReception,
            String formulePolitesse, RFCopieDecisionsValidationData... copie) throws Exception {

        try {
            // Appel du CaisseHeaderReport pour utiliser les fichiers de properties
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                    getSessionCygnus().getApplication(), codeIsoLangue);
            caisseHelper.setTemplateName(RFGenererDecisionMainService.FICHIER_MODELE_DOCUMENT_DECISION_RFM);

            // adresse
            crBean.setAdresse(adresse);

            // Afficher le libellé : personne de référence, si propertie à TRUE et gestionnaire non vide
            if (RFPropertiesUtils.afficherLibellePersonneReference() && !JadeStringUtil.isEmpty(getNomGestionnaire())) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIBELLE_REFERENCE, PRStringUtils.replaceString(
                        mainDocument.getTextes(1).getTexte(1).getDescription(),
                        IRFGenererDocumentDecision.GESTIONNAIRE_NOM, getNomGestionnaire()));
            }

            // telephone gestionnaire
            // Affichage si présence d'un numéro
            if (!JadeStringUtil.isEmpty(getTelephoneGestionnaire())) {

                if (RFPropertiesUtils.afficherTelephoneGestionnaire()) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE, mainDocument.getTextes(1)
                            .getTexte(2).getDescription()
                            + " " + getTelGestionnaire());
                }
            }

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            // Envoi du crBean au data
            data = caisseHelper.addHeaderParameters(data, crBean, isCopie);

            // copie
            if (isCopie) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_IS_COPIE, mainDocument.getTextes(3).getTexte(1)
                        .getDescription());
            }

            // nss
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_NSS, PRStringUtils.replaceString(mainDocument
                    .getTextes(3).getTexte(2).getDescription(), IRFGenererDocumentDecision.TIERS_NSS, nss));

            // ayant droit
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_AYANT_DROIT,
                    PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument.getTextes(3).getTexte(3)
                            .getDescription(), IRFGenererDocumentDecision.TIERS_NOM, nom),
                            IRFGenererDocumentDecision.TIERS_PRENOM, prenom));

            // Titre document
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECISION, mainDocument.getTextes(3).getTexte(4)
                    .getDescription());

            // Titre tiers
            String formulePolitesseWithComma = addCommaIFFrench(formulePolitesse);
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE, formulePolitesseWithComma);

            // paragraphe 1
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE1, PRStringUtils.replaceString(mainDocument
                    .getTextes(4).getTexte(1).getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_DATE_DEMANDE,
                    rfGenererDecisionService.getDateFormatterWithLanguage(demande.getDateReception(), codeIsoLangue)));

            // paragraphe 2
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE2, mainDocument.getTextes(4).getTexte(2)
                    .getDescription());

            // paragraphe 3
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE3, PRStringUtils.replaceString(mainDocument
                    .getTextes(4).getTexte(3).getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_DATE_DEBUT_OCTROI,
                    rfGenererDecisionService.getDateFormatterWithLanguage(demande.getDateDebutTraitement(),
                            codeIsoLangue)));

            // paragraphe 4
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE4_DEBUT, mainDocument.getTextes(4).getTexte(4)
                    .getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE4_GRAS, mainDocument.getTextes(4).getTexte(5)
                    .getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE4_FIN, mainDocument.getTextes(4).getTexte(6)
                    .getDescription());

            // tableau : Création d'une Collection pour l'ensemble du tableau
            // Chargement du template dans le document
            data.addData("isTableauCalculDecisionRegimeRefusInclude", "STANDARD");

            // tableau
            // Insertion du titre
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_CALCUL, mainDocument.getTextes(5).getTexte(1)
                    .getDescription());

            // Insertion valeur monétaire
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_VALEUR_MONETAIRE_CHF,
                    mainDocument.getTextes(5).getTexte(2).getDescription());

            // Récupération des montants
            BigDecimal montantSoldeExcedentRevenu = new BigDecimal(0);
            if (!JadeStringUtil.isEmpty(decisionDocument.getMontantSoldeExcedantRevenu())) {
                montantSoldeExcedentRevenu = new BigDecimal(decisionDocument.getMontantSoldeExcedantRevenu());
            }
            BigDecimal montantFacture = new BigDecimal(demande.getMontantFacture());
            BigDecimal montantSoldeExcedentRevenuAvantDeduction = montantSoldeExcedentRevenu.add(montantFacture);

            // 1ère ligne du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PREMIERE_LIGNE_CALCUL_REFUS, mainDocument.getTextes(5)
                    .getTexte(3).getDescription());
            data.addData(IRFGenererDocumentDecision.MONTANT_EXCEDENT_REVENU,
                    montantSoldeExcedentRevenuAvantDeduction.toString());

            // 2ème ligne du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_DEUXIEME_LIGNE_CALCUL_REFUS, mainDocument.getTextes(5)
                    .getTexte(4).getDescription());
            data.addData(IRFGenererDocumentDecision.MONTANT_FACTURE, montantFacture.toString());

            // 3ème ligne du tableau
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TROISIEME_LIGNE_CALCUL_REFUS, mainDocument.getTextes(5)
                    .getTexte(5).getDescription());
            data.addData(IRFGenererDocumentDecision.MONTANT_SOLDE_EXCEDENT_REVENU,
                    montantSoldeExcedentRevenu.toString());

            // paragraphe 5
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE5, mainDocument.getTextes(5).getTexte(6)
                    .getDescription());

            // Paragraphe 6 : Salutations
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_SALUTATIONS, PRStringUtils.replaceString(mainDocument
                    .getTextes(5).getTexte(7).getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_TITRE_TIERS,
                    formulePolitesse));

            // paragraphe 7
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE7_TITRE, mainDocument.getTextes(5).getTexte(8)
                    .getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE7_TEXTE, mainDocument.getTextes(5).getTexte(9)
                    .getDescription());

            // ----------------------------------------------------------------------------------------------------------
            // Création d'une nouvelle instance pour la publication dans le docInfo
            docInfo = new JadePublishDocumentInfo();
            docInfo.setPublishDocument(false);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS);
            docInfo.setDocumentType(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS);
            docInfo.setDocumentProperty("annee", JADate.getYear(dateSurDocument).toString());

            // Récupération de la date sur document pour indexation GED
            if (dateSurDocument.isEmpty()) {
                dateSurDocument = decisionDocument.getDateDecision_JourMoisAnnee();
            }
            docInfo.setDocumentDate(dateSurDocument.replace(".", ""));

            if (RFPropertiesUtils.imprimerDecisionsRectoVerso()) {
                docInfo.setDuplex(Boolean.TRUE);
                docInfo.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
            }

            // Si propriété à TRUE, on set des informations supplémentaires dans la ligne technique pour permettre aux
            // client de regrouper les documents pour la mise sous pli
            if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                        IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "O");
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS", nss);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", decisionDocument.getIdTiers());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", decisionDocument.getIdDecision());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NOM_TIERS",
                        limiteStringSize(nom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_PRENOM_TIERS",
                        limiteStringSize(prenom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));

                // Si il s'agit d'une copie pour autre destinataire, on écrase les info de la ligne technique, par les
                // info du destinataire
                if (copie.length > 0) {
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "C");
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", copie[0].getIdDestinataire());
                }
            }

            // Mise en GED des documents uniquement lors de la validation des décisions
            if (miseEnGed) {
                // Mise en GED du document si ce n'est pas une copie
                if (copie.length == 0) {
                    if (PRGedUtils.isDocumentInGed(
                            IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS, session)) {
                        docInfo.setArchiveDocument(true);
                    }
                }
            }

            TIDocumentInfoHelper.fill(docInfo, getIdTiers(), getSession(), null, null, null);

            // Ajout du document dans le container
            container.addDocument(data, docInfo);

        } catch (Exception e) {
            throw new Exception(
                    "Erreur dans le remplissage du document / RFGenererDecisionMainService : remplirCorpsDocumentDecisionMensuelleRegimeAvecExcedantRefus ");
        }
    }

    /**
     * Methode pour insérer les valeurs dans le document de la décision ponctuelle
     * 
     * @param container
     * @param miseEnGed
     * @param formatedDatesReception
     * @param copie
     * @throws Exception
     */
    protected void remplirCorpsDocumentDecisionPonctuelle(boolean miseEnGed, StringBuilder formatedDatesReception,
            String formulePolitesse, RFCopieDecisionsValidationData... copie) throws Exception {

        // adresse
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE, PRStringUtils.replaceString(mainDocument
                .getTextes(1).getTexte(1).getDescription(), IRFGenererDocumentDecision.TIERS_ADRESSE, adresse));

        String nomGestionnaireAAfficher = getNomGestionnaireAAfficher(getNomGestionnaire());

        // Si propriété à TRUE
        if (RFPropertiesUtils.afficherLibellePersonneReference()) {
            // Affihcer le libellé : personne de référence
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIBELLE_REFERENCE, mainDocument.getTextes(1).getTexte(2)
                    .getDescription());

            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_GESTIONNAIRE, nomGestionnaireAAfficher);
        } else {
            // Affihcer le libellé : gestionnaire
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_GESTIONNAIRE, PRStringUtils.replaceString(mainDocument
                    .getTextes(2).getTexte(1).getDescription(), IRFGenererDocumentDecision.GESTIONNAIRE_NOM,
                    nomGestionnaireAAfficher));
        }

        // Affichage du telephone gestionnaire, si présence d'un numéro
        if (!JadeStringUtil.isEmpty(getTelephoneGestionnaire())) {

            if (RFPropertiesUtils.afficherTelephoneGestionnaire()) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE, PRStringUtils.replaceString(
                        mainDocument.getTextes(2).getTexte(2).getDescription(),
                        IRFGenererDocumentDecision.GESTIONNAIRE_TELEPHONE, getTelGestionnaire()));
            } else {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIBELLE_TEL, PRStringUtils.replaceString(" • "
                        + mainDocument.getTextes(2).getTexte(1).getDescription(),
                        IRFGenererDocumentDecision.GESTIONNAIRE_TELEPHONE, getTelGestionnaire()));
            }
        }

        // Copie
        if (isCopie) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_IS_COPIE, mainDocument.getTextes(2).getTexte(3)
                    .getDescription());
        }

        // Nss
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_NSS, PRStringUtils.replaceString(mainDocument.getTextes(2)
                .getTexte(4).getDescription(), IRFGenererDocumentDecision.TIERS_NSS, nss));

        // Ayant droit
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_AYANT_DROIT, PRStringUtils.replaceString(PRStringUtils
                .replaceString(mainDocument.getTextes(2).getTexte(5).getDescription(),
                        IRFGenererDocumentDecision.TIERS_NOM, nom), IRFGenererDocumentDecision.TIERS_PRENOM, prenom));

        // Date décision
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_DATE_DECISION, PRStringUtils.replaceString(mainDocument
                .getTextes(2).getTexte(6).getDescription(), IRFGenererDocumentDecision.DATE_DECISION,
                decisionDocument.getDateDecision_JourMoisAnnee()));

        Collection newTable = new Collection(IRFGenererDocumentDecision.CAT_TEXTE_TABLEAU_DECISION);

        // Année
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_ANNEE, mainDocument.getTextes(3).getTexte(1).getDescription());

        // Numéro décision
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_NUMERO_DECISION, mainDocument.getTextes(3).getTexte(2)
                .getDescription());
        DataList line1 = new DataList("ligne");
        newTable.add(line1);
        line1.addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_ANNEE, PRStringUtils.replaceString(mainDocument
                .getTextes(3).getTexte(3).getDescription(), IRFGenererDocumentDecision.ANNEE_FACTURE,
                decisionDocument.getAnneeQD()));
        line1.addData(IRFGenererDocumentDecision.CAT_TEXTE_VAR_NUMERO_DECISION, PRStringUtils.replaceString(
                mainDocument.getTextes(3).getTexte(4).getDescription(), IRFGenererDocumentDecision.NUMERO_DECISION,
                decisionDocument.getNumeroDecision()));
        data.add(newTable);

        // Titre
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE, PRStringUtils.replaceString(mainDocument.getTextes(4)
                .getTexte(1).getDescription(), IRFGenererDocumentDecision.TIERS_TITRE, formulePolitesse));

        // Paragraphe 1
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE1, PRStringUtils.replaceString(mainDocument
                .getTextes(4).getTexte(2).getDescription(), IRFGenererDocumentDecision.DATE_RECEPTION,
                formatedDatesReception.toString() + addDotIFFrench()));

        // Paragraphe 2
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE2, mainDocument.getTextes(4).getTexte(3)
                .getDescription());

        // Si ce n'est pas une copie ou une copie avec une remarque
        if ((copie.length == 0) || copie[0].getHasRemarque()) {

            // Affichage du titre "Remarque" si l'une des 2 conditions est à true
            if ((decisionDocument.getIsBordereauAccompagnement())
                    || (!JadeStringUtil.isEmpty(decisionDocument.getTexteRemarque()))) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_REMARQUE, mainDocument.getTextes(4).getTexte(4)
                        .getDescription());
            }

            // Création du variable pour y concaténer les différents textes
            String textesRemarque = "";

            // Si case à cocher active, affichage du texte en dur.
            if (decisionDocument.getIsBordereauAccompagnement()) {
                textesRemarque = mainDocument.getTextes(4).getTexte(5).getDescription();
            }

            // Ajout du texte libre si existant.
            if (!JadeStringUtil.isEmpty(decisionDocument.getTexteRemarque())) {
                if (!JadeStringUtil.isEmpty(textesRemarque)) {
                    textesRemarque = textesRemarque + "\n" + decisionDocument.getTexteRemarque();
                } else {
                    textesRemarque = decisionDocument.getTexteRemarque();
                }
            }

            // Insertion de la remarque si l'une des conditions est à true
            if ((decisionDocument.getIsBordereauAccompagnement())
                    || (!JadeStringUtil.isEmpty(decisionDocument.getTexteRemarque()))) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE, textesRemarque);
            }
        }

        // Remarque dépôt (case à cocher)
        if (decisionDocument.getIsPhraseIncitationDepot() == true) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE_DEPOT, mainDocument.getTextes(4).getTexte(9)
                    .getDescription());
        }

        // Remarque retour bv (case à cocher)
        if (decisionDocument.getIsPhraseRetourBV() == true) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE_RETOUR_BV, mainDocument.getTextes(4)
                    .getTexte(10).getDescription());
        }

        // Moyens de droit
        if ((copie.length == 0) || copie[0].getHasMoyensDroit()) {
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_MOYEN_DROIT, mainDocument.getTextes(4).getTexte(6)
                    .getDescription());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_MOYEN_DROIT, mainDocument.getTextes(4).getTexte(7)
                    .getDescription());
        }

        // Salutations
        data.addData(IRFGenererDocumentDecision.CAT_TEXTE_SALUTATIONS, PRStringUtils.replaceString(mainDocument
                .getTextes(4).getTexte(8).getDescription(), IRFGenererDocumentDecision.TIERS_TITRE, formulePolitesse));

    }

    /**
     * Methode pour insérer les valeurs dans le document de la décision de restitution
     * 
     * !!! La décision de restitution n'est pas une entité en DB, elle est simplement générée sur la base de nouvelles
     * décisions issues de corrections sur d'anciennes décisions !!!
     * 
     * @param container
     * @param miseEnGed
     * @param formatedDatesReception
     * @param copie
     * @throws Exception
     */
    protected void remplirCorpsDocumentDecisionRestitution(JadePrintDocumentContainer container, boolean miseEnGed,
            StringBuilder formatedDatesReception, String formulePolitesse, RFCopieDecisionsValidationData... copie)
            throws Exception {

        try {

            // Création d'une instance des services pour les décisions de restitution.
            rfGenererDecisionRestitutionService = new RFGenererDecisionRestitutionService();

            // Appel du CaisseHeaderReport pour utiliser les fichiers de properties
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                    getSessionCygnus().getApplication(), codeIsoLangue);
            caisseHelper.setTemplateName(RFGenererDecisionMainService.FICHIER_MODELE_DOCUMENT_DECISION_RFM);

            // adresse
            crBean.setAdresse(adresse);

            // Affihcer le libellé : personne de référence
            if (RFPropertiesUtils.afficherLibellePersonneReference()) {

                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_LIBELLE_REFERENCE, mainDocument.getTextes(1)
                        .getTexte(1).getDescription());

                String nomGestionnaireAAfficher = getNomGestionnaireAAfficher(getNomGestionnaire());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_GESTIONNAIRE, nomGestionnaireAAfficher);
            }

            // telephone gestionnaire
            // Affichage si présence d'un numéro
            if (!JadeStringUtil.isEmpty(getTelephoneGestionnaire())) {

                if (RFPropertiesUtils.afficherTelephoneGestionnaire()) {
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TEL_GESTIONNAIRE, mainDocument.getTextes(1)
                            .getTexte(2).getDescription()
                            + " " + getTelGestionnaire());
                }
            }

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            // Envoi du crBean au data
            data = caisseHelper.addHeaderParameters(data, crBean, isCopie);

            // copie
            if (isCopie) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_IS_COPIE, mainDocument.getTextes(1).getTexte(3)
                        .getDescription());
            }

            // nss
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_NSS, PRStringUtils.replaceString(mainDocument
                    .getTextes(1).getTexte(4).getDescription(), IRFGenererDocumentDecision.TIERS_NSS, nss));

            // ayant droit
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_AYANT_DROIT,
                    PRStringUtils.replaceString(PRStringUtils.replaceString(mainDocument.getTextes(1).getTexte(5)
                            .getDescription(), IRFGenererDocumentDecision.TIERS_NOM, nom),
                            IRFGenererDocumentDecision.TIERS_PRENOM, prenom));

            // Titre tiers
            String formulePolitesseWithComma = addCommaIFFrench(formulePolitesse);
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE, formulePolitesseWithComma);

            // paragraphe 1
            rfGenererDecisionRestitutionService.loadDateDebutQds(decisionDocument, getSession());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE1, PRStringUtils.replaceString(mainDocument
                    .getTextes(2).getTexte(1).getDescription(), IRFGenererDocumentDecision.DATE_PRESTATION,
                    rfGenererDecisionRestitutionService.getDateDebut()));

            // paragraphe 2
            rfGenererDecisionRestitutionService.loadDateFinQds(decisionDocument, getSession());
            String paragraphe2 = PRStringUtils.replaceString(mainDocument.getTextes(2).getTexte(2).getDescription(),
                    IRFGenererDocumentDecision.DATE_DEBUT_ANCIENNE_PRESTATION,
                    rfGenererDecisionRestitutionService.getDateDebut());
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE2, PRStringUtils.replaceString(paragraphe2,
                    IRFGenererDocumentDecision.DATE_FIN_ANCIENNE_PRESTATION,
                    rfGenererDecisionRestitutionService.getDateFin()));

            // paragraphe 3
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE3, mainDocument.getTextes(2).getTexte(4)
                    .getDescription());

            // ***************************************************************
            // Chargement du tableau de décompte de restitution
            loadTableauDecisionRestitution();

            // Insertion du titre du document en fonction des montants du tableau de décompte
            if (isMontantARestituer) {
                String typeAssurance = rfGenererDecisionRestitutionService.getGenrePrestation(getDecisionDocument()
                        .getGenrePrestation(), decisionDocument.getLangueTiers());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECISION, PRStringUtils.replaceString(
                        mainDocument.getTextes(1).getTexte(6).getDescription(),
                        IRFGenererDocumentDecision.TYPE_ASSURANCE, typeAssurance));
            } else {
                String typeAssurance = rfGenererDecisionRestitutionService.getGenrePrestation(getDecisionDocument()
                        .getGenrePrestation(), decisionDocument.getLangueTiers());
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DECISION, PRStringUtils.replaceString(
                        mainDocument.getTextes(1).getTexte(7).getDescription(),
                        IRFGenererDocumentDecision.TYPE_ASSURANCE, typeAssurance));
            }

            // paragraphe 4
            if (isMontantARestituer) {
                // if (this.rfGenererDecisionRestitutionService.isPrestationAZeroSansCompensation(this.session,
                // this.decisionDocument)) {
                // // TODO Texte a implementer pour bz9668
                // } else {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE4, PRStringUtils.replaceString(mainDocument
                        .getTextes(4).getTexte(1).getDescription(),
                        IRFGenererDocumentDecision.CAT_TEXTE_MONTANT_A_REMBOURSER,
                        montantARestituerDecisionRestitution.toStringFormat()));
                // }
            }

            // Paragraphe 5
            if (isMontantARestituer) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE5, mainDocument.getTextes(4).getTexte(2)
                        .getDescription());
            } else {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_PARAGRAPHE5, mainDocument.getTextes(4).getTexte(3)
                        .getDescription());
            }

            // paragraphe salutation
            data.addData(IRFGenererDocumentDecision.CAT_TEXTE_SALUTATIONS, PRStringUtils.replaceString(mainDocument
                    .getTextes(4).getTexte(4).getDescription(), IRFGenererDocumentDecision.CAT_TEXTE_TITRE_TIERS,
                    formulePolitesse));

            // Texte "remarque au verso"
            if (isMontantARestituer) {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE_AU_VERSO, mainDocument.getTextes(4)
                        .getTexte(8).getDescription());
            } else {
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_REMARQUE_AU_VERSO, mainDocument.getTextes(4)
                        .getTexte(9).getDescription());
            }
            if ((copie.length == 0) || copie[0].getHasMoyensDroit()) {
                // titre moyen droit
                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_MOYEN_DROIT, mainDocument.getTextes(5)
                        .getTexte(1).getDescription());
                // moyen droit

                data.addData(IRFGenererDocumentDecision.CAT_TEXTE_MOYEN_DROIT, PRStringUtils.replaceString(mainDocument
                        .getTextes(5).getTexte(2).getDescription(),
                        IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE_CAISSE, mainDocument.getTextes(6).getTexte(1)
                                .getDescription()));
            }

            if (copie.length == 0) {
                if (isMontantARestituer) {
                    // titre demande remise
                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_TITRE_DEMANDE_REMISE, mainDocument.getTextes(5)
                            .getTexte(3).getDescription());

                    data.addData(IRFGenererDocumentDecision.CAT_TEXTE_DEMANDE_REMISE, PRStringUtils.replaceString(
                            mainDocument.getTextes(5).getTexte(4).getDescription(),
                            IRFGenererDocumentDecision.CAT_TEXTE_ADRESSE_CAISSE, mainDocument.getTextes(6).getTexte(1)
                                    .getDescription()));
                }
            }

            // ---------------------------------------------------------------------------------------------------------

            // Création d'une nouvelle instance pour la publication dans le docInfo
            docInfo = new JadePublishDocumentInfo();
            docInfo.setPublishDocument(false);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(IPRConstantesExternes.RFM_DECISION_DE_RESTITUTION);
            docInfo.setDocumentType(IPRConstantesExternes.RFM_DECISION_DE_RESTITUTION);
            docInfo.setDocumentProperty("annee", JADate.getYear(dateSurDocument).toString());

            // Récupération de la date sur document pour indexation GED
            if (dateSurDocument.isEmpty()) {
                dateSurDocument = decisionDocument.getDateDecision_JourMoisAnnee();
            }
            docInfo.setDocumentDate(dateSurDocument.replace(".", ""));

            // BZ9666 : Implementation du recto/verso
            if (RFPropertiesUtils.imprimerDecisionsRectoVerso()) {
                docInfo.setDuplex(Boolean.TRUE);
                docInfo.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
            }

            // Si propriété à TRUE, on set des informations supplémentaires dans la ligne technique pour permettre aux
            // client de regrouper les documents pour la mise sous pli
            if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {

                // !!! La décision de restitution n'est pas une entité en DB, elle est simplement générée sur la base de
                // nouvelles décision issues de corrections sur d'anciennes décisions, donc pas d'idDecision existant
                // !!!

                // Simulation d'un idDecision basé sur l'idDecision de la première décision nouvellement due
                ArrayList<RFDecision> listeDecisionsDues = new ArrayList<RFDecision>();
                listeDecisionsDues = rfGenererDecisionRestitutionService.getListeDecisionsDues(decisionDocument,
                        getSession());
                // On set cet idDecision simulé à la décision courante
                if (!listeDecisionsDues.isEmpty()) {
                    decisionDocument.setIdDecision(listeDecisionsDues.get(0).getIdDecision() + "_" + "R");
                }

                docInfo.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                        IPRConstantesExternes.RFM_DECISION_DE_RESTITUTION);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "O");
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS", nss);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", decisionDocument.getIdTiers());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", decisionDocument.getIdDecision());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NOM_TIERS",
                        limiteStringSize(nom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_PRENOM_TIERS",
                        limiteStringSize(prenom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));
                if (isMontantARestituer) {
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_IS_RESTITUTION", "N");
                }

                // Si il s'agit d'une copie pour autre destinataire, on écrase les info de la ligne technique, par les
                // info du destinataire
                if (copie.length > 0) {
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "C");
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", copie[0].getIdDestinataire());
                    if (isMontantARestituer) {
                        docInfo.setDocumentProperty("AGLA_LT_PCRFM_IS_RESTITUTION", "N");
                    }
                }
            }

            // Mise en GED des documents uniquement lors de la validation des décisions
            if (miseEnGed) {
                // Mise en GED du document si ce n'est pas une copie
                if (copie.length == 0) {
                    if (PRGedUtils.isDocumentInGed(IPRConstantesExternes.RFM_DECISION_DE_RESTITUTION, session)) {
                        docInfo.setArchiveDocument(true);
                    }
                }
            }

            TIDocumentInfoHelper.fill(docInfo, getIdTiers(), getSession(), null, null, null);

            // Ajout du document dans le container
            container.addDocument(data, docInfo);

        } catch (Exception e) {
            // En cas d'erreur, le document n'est pas inséré dans le container (remplirDecision())
            documentRestitutionEnErreur = true;

            addErreurMail(memoryLog, e.getMessage(), "Erreur dans la préparation des décisions de restitutions");
            getSession().addError(
                    "Erreur dans la préparation des décisions de restitutions \n Pour le tiers " + nom + " " + prenom);
        }
    }

    /**
     * Methode qui va lancer la génération du document, et le retourner au container
     * 
     * @param miseEnGed
     * @param memoryLog
     * @param container
     * @param isDecisionPonctuelle
     * @param isDecisionMensuelle
     * @param isDecisionRestitution
     * @param documentHelper
     * @param catalogueMultiLangue
     * @param copie
     * @return
     * @throws Exception
     */
    public JadePrintDocumentContainer remplirDecision(boolean miseEnGed, FWMemoryLog memoryLog,
            JadePrintDocumentContainer container, boolean isDecisionPonctuelle, boolean isDecisionMensuelle,
            boolean isDecisionRestitution, ICTDocument documentHelper,
            Hashtable<String, ICTDocument> catalogueMultiLangue, RFCopieDecisionsValidationData... copie)
            throws Exception {
        try {

            documentRestitutionEnErreur = false;
            // pubInfos.setPublishDocument(false);
            setIdTiers(decisionDocument.getIdTiers());

            this.generationLettre(container, miseEnGed, memoryLog, isDecisionPonctuelle, isDecisionMensuelle,
                    isDecisionRestitution, documentHelper, catalogueMultiLangue, copie);

            if (getMontantCourant() != null) {
                montantCourant = getMontantCourant();
            }

            // Ajout du document au container, si aucune erreur rencontré lors de sa génération.
            if ((!documentRestitutionEnErreur) && ((decisionToPrint != null) && decisionToPrint.getHasModel())) {
                // container.addDocument(this.getDocumentData(), this.docInfo);
            } else {
                if (documentRestitutionEnErreur) {
                    this.memoryLog.getMessagesInString();
                }
            }

            return container;

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    @Override
    public void remplirDocument() throws Exception {

    }

    /**
     * Methode qui traite les décision mensuelle (régime), analyser selon montants, si il sagit d'une décision avec/sans
     * excedent, avec/sans montantsPayés
     * 
     * @param miseEnGed
     * @param catalogueMultiLangue
     * @param copie
     * @throws Exception
     */
    public void remplirDocumentPaiementMensuelRegime(JadePrintDocumentContainer container, boolean miseEnGed,
            Hashtable<String, ICTDocument> catalogueMultiLangue, String formulePolitesse,
            RFCopieDecisionsValidationData... copie) throws Exception {
        try {

            RFDemandeValidationData demande = decisionDocument.getDecisionDemande().get(0);

            BigDecimal montantExcedantRevenu = new BigDecimal(0);
            if (!JadeStringUtil.isEmpty(decisionDocument.getExcedentRevenu())) {
                montantExcedantRevenu = new BigDecimal(decisionDocument.getExcedentRevenu());
            }

            BigDecimal montantAccepteDemande = new BigDecimal(0);
            if (!JadeStringUtil.isEmpty(demande.getMontantAccepte())) {
                montantAccepteDemande = new BigDecimal(demande.getMontantAccepte());
            }

            // fusionner toutes les dates des demandes pour paragraphe1
            StringBuilder formatedDatesReception = new StringBuilder();
            recupererDates(formatedDatesReception);
            dateDemande = formatedDatesReception.toString();

            RFGenererDecisionServiceOO rfDecisionServiceOO = new RFGenererDecisionServiceOO(eMail);

            // si la propriété PROPERTY_UTILISER_DOCUMENT_DECISION_REGIME_AVEC_EXCEDANT_REVENU est a false
            // documents standards
            if (!RFPropertiesUtils.utiliserDecisionAvecExcedantRevenu()) {
                decisionToPrint = getTypeDecisionStandardToPrint(montantAccepteDemande);
            } else {
                decisionToPrint = getTypeDecisionComplementaireToPrint(montantAccepteDemande, montantExcedantRevenu);
            }

            // si modèle de document inexistant, remonte l'info dans le mail
            if (!decisionToPrint.getHasModel()) {
                rfDecisionServiceOO.addWarningMail(memoryLog,
                        rfDecisionServiceOO.buildMessageWarningRegimeSansModele(getSessionCygnus(), decisionDocument),
                        "RFGenererDecisionMainService : remplirDocumentPaiementMensuelRegime()");
            } else {

                // Set le template correspondant à la décision
                chargerTemplateDecision(decisionToPrint);

                // Set le type de document correspondant à la décision
                chargerTypeDocumentDecision(decisionToPrint);

                // Set le catalogue de textes correspondant à la décision
                chargerTypeCatalogueTexteDecision(catalogueMultiLangue, decisionToPrint);

                // Appel de la methode de construction de la décision
                selectionnerMethodeDeRemplissageDecisionRegime(container, decisionToPrint, demande, miseEnGed,
                        formatedDatesReception, formulePolitesse, copie);

                // test si il faut ajout le tableau d'annexes et copies en bas de page
                Boolean annexes = false;
                Boolean copies = false;
                if ((copie.length == 0) || copie[0].getHasAnnexes()) {
                    annexes = true;
                }

                if ((copie.length == 0) || copie[0].getHasCopies()) {
                    copies = true;
                }

                ajoutAnnexesEtCopies(false, true, false, annexes, copies);

                // test si il faut ajouter la signature au tiers en copie
                if ((copie.length == 0) || copie[0].getHasSignature()) {
                    ajoutSignature = true;
                    ajouterSignature();
                } else {
                    ajoutSignature = false;
                    ajouterSignature();
                }

                ajoutPiedDePageRegime();
            }
        } catch (Exception e) {
            addErreurMail(memoryLog, e.getMessage(),
                    "RFGenererDecisionMainService:remplirDocumentPaiementMensuelRegime");
            throw new Exception(e.toString());
        }
    }

    public void remplirDocumentPaiementPonctuel(JadePrintDocumentContainer container, boolean miseEnGed,
            Hashtable<String, ICTDocument> catalogueMultiLangue, String formulePolitesse,
            RFCopieDecisionsValidationData... copie) throws Exception {
        try {

            // Récupération des infos tiers adresse de paiement.
            String[] nomPrenom = rfGenererDecisionService.getTiersAdressePaiement(sessionCygnus, decisionDocument);
            nomAdressePaiement = nomPrenom[0];
            prenomAdressePaiement = nomPrenom[1];

            // fusionner toutes les dates des demandes pour paragraphe1
            StringBuilder formatedDatesReception = new StringBuilder();
            recupererDates(formatedDatesReception);

            // Chargement de l'Enum correspondant à la décision
            decisionToPrint = RFTypeDecisionEnum.RFM_DECISION_PONCTUELLE;

            // Set le template correspondant à la décision
            chargerTemplateDecision(decisionToPrint);

            // Set le type de document correspondant à la décision
            chargerTypeDocumentDecision(decisionToPrint);

            // Set le catalogue de textes correspondant à la décision
            chargerTypeCatalogueTexteDecision(catalogueMultiLangue, decisionToPrint);

            // Selection de la methode de remplisage du document
            remplirCorpsDocumentDecisionPonctuelle(miseEnGed, formatedDatesReception, formulePolitesse, copie);

            // Appel de la méthode pour construire le décompte en seconde page d'une décision ponctuelle
            // this.ajoutDecompte(copie);

            Boolean annexes = false;
            Boolean copies = false;
            if ((copie.length == 0) || copie[0].getHasAnnexes()) {
                annexes = true;
            }

            if ((copie.length == 0) || copie[0].getHasCopies()) {
                copies = true;
            }

            ajoutAnnexesEtCopies(true, false, false, annexes, copies);

            // test si c'est une copie avec signature ou non
            if ((copie.length == 0) || copie[0].getHasSignature()) {
                ajoutSignature = true;
                ajouterSignature();
            } else {
                ajoutSignature = false;
                ajouterSignature();
            }

            // ajout eventuel du décompte
            if ((copie.length == 0) || copie[0].getIsDecompte()) {

                // Chargement du décompte
                data.addData("decompte", "STANDARD");

                ajoutPiedDePage();
                ajoutDecompte(copie);

            } else {
                data.addData("decompte", "NONE");
            }

            // -------------------------------------------------------------------------------------------------------------

            // Création d'une nouvelle instance pour la publication dans le docInfo
            docInfo = new JadePublishDocumentInfo();
            docInfo.setPublishDocument(false);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(IPRConstantesExternes.RFM_DECISION_PONCTUELLE);
            docInfo.setDocumentType(IPRConstantesExternes.RFM_DECISION_PONCTUELLE);
            docInfo.setDocumentProperty("annee", JADate.getYear(dateSurDocument).toString());

            // Récupération de la date sur document pour indexation GED
            if (dateSurDocument.isEmpty()) {
                dateSurDocument = decisionDocument.getDateDecision_JourMoisAnnee();
            }
            docInfo.setDocumentDate(dateSurDocument.replace(".", ""));

            if (RFPropertiesUtils.imprimerDecisionsRectoVerso()) {
                docInfo.setDuplex(Boolean.TRUE);
                docInfo.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
            }

            // Si propriété à TRUE, on set des informations supplémentaires dans la ligne technique pour permettre aux
            // client de regrouper les documents pour la mise sous pli
            if (RFPropertiesUtils.insererLigneTechniqueDeRegroupement()) {
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_DOC_TYPE_NUMBER",
                        IPRConstantesExternes.RFM_DECISION_PONCTUELLE);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "O");
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NSS_TIERS", nss);
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", decisionDocument.getIdTiers());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_DECISION", decisionDocument.getIdDecision());
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_NOM_TIERS",
                        limiteStringSize(nom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));
                docInfo.setDocumentProperty("AGLA_LT_PCRFM_PRENOM_TIERS",
                        limiteStringSize(prenom, RFGenererDecisionMainService.NOM_PRENOM_SIZE));

                // Si il s'agit d'une copie pour autre destinataire, on écrase les info de la ligne technique, par les
                // info du destinataire
                if (copie.length > 0) {
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_TYPE_DECISION", "C");
                    docInfo.setDocumentProperty("AGLA_LT_PCRFM_ID_TIERS", copie[0].getIdDestinataire());
                }
            }

            // Mise en GED des documents uniquement lors de la validation des décisions
            if (miseEnGed) {
                // Mise en GED du document si ce n'est pas une copie
                if (copie.length == 0) {
                    if (PRGedUtils.isDocumentInGed(IPRConstantesExternes.RFM_DECISION_PONCTUELLE, session)) {
                        docInfo.setArchiveDocument(true);
                    }
                }
            }

            TIDocumentInfoHelper.fill(docInfo, getIdTiers(), getSession(), null, null, null);

            // Ajout du document dans le container
            container.addDocument(data, docInfo);

        } catch (Exception e) {
            addErreurMail(memoryLog, e.getMessage(), "RFGenererDecisionMainService:remplirDocumentPaiementPonctuel()");
            throw new Exception(e.toString());
        }
    }

    public void remplirDocumentPaiementRestitution(JadePrintDocumentContainer container, boolean miseEnGed,
            Hashtable<String, ICTDocument> catalogueMultiLangue, String formulePolitesse,
            RFCopieDecisionsValidationData... copie) throws Exception {
        try {

            // fusionner toutes les dates des demandes pour paragraphe1
            StringBuilder formatedDatesReception = new StringBuilder();
            recupererDates(formatedDatesReception);
            dateDemande = formatedDatesReception.toString();
            decisionDocument.setNom(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM));
            nom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);

            decisionToPrint = RFTypeDecisionEnum.RFM_DECISION_RESTITUTION;

            // Set le template correspondant à la décision
            chargerTemplateDecision(decisionToPrint);

            // Set le type de document correspondant à la décision
            chargerTypeDocumentDecision(decisionToPrint);

            // Set le catalogue de textes correspondant à la décision
            chargerTypeCatalogueTexteDecision(catalogueMultiLangue, decisionToPrint);

            // ... selection du template
            data.addData("idProcess", "RFDecisionRestitutionOO");

            // ... selection du type de document
            documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_RESTITUTION);

            // ...selection du catalogue de texte
            setMainDocument(catalogueMultiLangue.get(IRFCatalogueTexte.CS_DECISION_RESTITUTION + "_" + codeIsoLangue));

            remplirCorpsDocumentDecisionRestitution(container, miseEnGed, formatedDatesReception, formulePolitesse,
                    copie);

            // test si il faut ajouter la signature au tiers en copie
            if ((copie.length == 0) || copie[0].getHasSignature()) {
                ajoutSignature = true;
                ajouterSignature();
            } else {
                ajoutSignature = false;
                ajouterSignature();
            }

            // Test si il faut ajouter les annexes et copies en bas de page.
            Boolean annexes = false;
            Boolean copies = false;
            if ((copie.length == 0) || copie[0].getHasAnnexes()) {
                annexes = true;
            }

            if ((copie.length == 0) || copie[0].getHasCopies()) {
                copies = true;
            }

            ajoutAnnexesEtCopies(false, false, true, annexes, copies);

        } catch (Exception e) {
            addErreurMail(memoryLog, e.getMessage(),
                    "RFGenererDecisionMainService:remplirDocumentPaiementRestitution()");
        }
    }

    /**
     * Methode pour sélectionner la méthode de construction de la décision de régime.
     * 
     * @param decisionEnum
     * @param demande
     * @param miseEnGed
     * @param formatedDatesReception
     * @param copie
     * @throws Exception
     */
    private void selectionnerMethodeDeRemplissageDecisionRegime(JadePrintDocumentContainer container,
            RFTypeDecisionEnum decisionEnum, RFDemandeValidationData demande, boolean miseEnGed,
            StringBuilder formatedDatesReception, String formulePolitesse, RFCopieDecisionsValidationData... copie)
            throws Exception {

        switch (decisionEnum) {

            case RFM_DECISION_REGIME_AVEC_EXCEDENT_OCTROI:
                remplirCorpsDocumentDecisionMensuelleRegimeAvecExcedantOctroi(container, demande, miseEnGed,
                        formatedDatesReception, formulePolitesse, copie);
                break;

            case RFM_DECISION_REGIME_AVEC_EXCEDENT_REFUS:
                remplirCorpsDocumentDecisionMensuelleRegimeAvecExcedantRefus(container, demande, miseEnGed,
                        formatedDatesReception, formulePolitesse, copie);
                break;

            case RFM_DECISION_REGIME_SANS_EXCEDENT_OCTROI:
                remplirCorpsDocumentDecisionMensuelleRegime(container, miseEnGed, formatedDatesReception,
                        formulePolitesse, copie);
                hasTableauVersementEtDecompteCopie(copie);
                break;

            default:
                throw new IllegalArgumentException(
                        "RFGenererDecisionMainService_selectionnerMethodeDeRemplissageDecisionRegime : Aucun modèle ne correspond au type de décision reçue en paramètre");

        }
    }

    @Override
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAnneeFacture(String anneeFacture) {
        this.anneeFacture = anneeFacture;
    }

    public void setCopie(String copie) {
        this.copie = copie;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDatesReception(String datesReception) {
        this.datesReception = datesReception;
    }

    public void setDecisionDocument(RFDecisionDocumentData decisionDocument) {
        this.decisionDocument = decisionDocument;
    }

    public String setDescription(RFMotifRefusDemandeValidationData motifs) throws Exception {
        if (IRFCodesIsoLangue.LANGUE_ISO_DE.equals(codeIsoLangue)) {
            return motifs.getDescriptionDe();
        } else {
            if (IRFCodesIsoLangue.LANGUE_ISO_FR.equals(codeIsoLangue)) {
                return motifs.getDescriptionFr();
            } else {
                if (IRFCodesIsoLangue.LANGUE_ISO_IT.equals(codeIsoLangue)) {
                    return motifs.getDescriptionIt();
                } else {
                    addErreurMail(memoryLog, "getMotifRefus:code langue iso incorrect ou manquant",
                            "RFGenererDecisionMainService:setDescription");
                    throw new Exception("getMotifRefus:code langue iso incorrect ou manquant");
                }
            }
        }
    }

    public void setDocInfo(JadePublishDocumentInfo docInfo) {
        this.docInfo = docInfo;
    }

    public void setIsCopie(Boolean isCopie) {
        this.isCopie = isCopie;
    }

    public void setMainDocument(ICTDocument mainDocument) {
        this.mainDocument = mainDocument;
    }

    public void setMemoryLog(FWMemoryLog memoryLog) {
        this.memoryLog = memoryLog;
    }

    public void setMessageOv(String messageOv) {
        this.messageOv = messageOv;
    }

    public void setMontantCourant(FWCurrency montantCourant) {
        this.montantCourant = montantCourant;
    }

    public void setMontantFuture(String montantFuture) {
        this.montantFuture = montantFuture;
    }

    public void setNomGestionnaire(String nomGestionnaire) {
        this.nomGestionnaire = nomGestionnaire;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setPdfDecisionURL(StringBuffer pdfDecisionURL) {
        this.pdfDecisionURL = pdfDecisionURL;
    }

    public void setSessionCygnus(BSession sessionCygnus) {
        this.sessionCygnus = sessionCygnus;
    }

    public void setTelGestionnaire(String telGestionnaire) {
        this.telGestionnaire = telGestionnaire;
    }

    public void setTexteLibre(String texteLibre) {
        this.texteLibre = texteLibre;
    }

    @Override
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * Cette méthode permet de récupérer un tiers par son id.
     * 
     * @return un tiers (TITiers)
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private TITiers getTiersFromIdTiers() throws JadePersistenceException, JadeApplicationException {
        TITiers tiers = new TITiers();
        tiers.setId(getIdTiers());
        tiers.setSession(getSession());
        try {
            tiers.retrieve();
            return tiers;
        } catch (Exception e) {
            throw new JadePersistenceException("an error happened while loading the tiers with the following id : "
                    + idTiers, e);
        }
    }

    private String resolveFormulePolitesse() throws Exception {
        TITiers tiers = getTiersFromIdTiers();
        String formulePolitesse = tiers.getFormulePolitesse(LanguageResolver.resolveCodeSystemFromLanguage(tiers
                .getLangue()));
        return formulePolitesse;
    }

    private String addCommaIFFrench(String formulePolitesse) {
        Langues langue = LanguageResolver.resolveISOCode(codeIsoLangue);

        if (Langues.Francais.equals(langue)) {
            return formulePolitesse + ",";
        } else {
            return formulePolitesse;
        }
    }

    private String addDotIFFrench() {
        Langues langue = LanguageResolver.resolveISOCode(codeIsoLangue);

        if (Langues.Francais.equals(langue)) {
            return ".";
        } else {
            return "";
        }
    }

}
