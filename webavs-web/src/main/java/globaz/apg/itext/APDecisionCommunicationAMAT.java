package globaz.apg.itext;

import globaz.apg.ApgServiceLocator;
import globaz.apg.api.codesystem.IAPCatalogueTexte;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.application.APApplication;
import globaz.apg.business.service.APEntityService;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.groupdoc.ccju.GroupdocPropagateUtil;
import globaz.apg.module.calcul.APReferenceDataParser;
import globaz.apg.module.calcul.rev2005.APReferenceDataAPG;
import globaz.apg.properties.APProperties;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.*;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.api.IAFAssurance;
import globaz.naos.application.AFApplication;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.external.IntRole;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater01;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRBlankBNumberFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.utils.PRDateUtils;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TITiers;
import java.io.File;
import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.*;

import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.lang.StringUtils;

/**
 * <H1>Description</H1>
 * <p>
 * Génération des décisions pour un droit. Les prestations pour ce droit doivent avoir été préalablement créées et
 * doivent toutes être dans l'état validé.
 * </p>
 * <p>
 * Génére un document général pour l'assuré et un document particulier pour chacun des employeurs.
 * </p>
 *
 * @author vre
 */
public class APDecisionCommunicationAMAT extends FWIDocumentManager {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Définition d'une classe interne pour reprendre la méthode add() qui nous
    // sert
    // à ne pas imprimer le décompte s'il ne contient pas d'échéances
    private class ImporterImpl extends FWIImportManager {

        /**
         *
         */
        public ImporterImpl() throws FWIException {
            setImportPath(APApplication.APPLICATION_APG_REP);
        }

        /**
         * @param root
         * @param source
         * @throws globaz.framework.printing.itext.exception.FWIException
         */
        public ImporterImpl(final String root, final JRDataSource source) throws FWIException {
            super(root, source);
        }

        @Override
        public void add(final Object obj) {
            if (isDecompteRempli) {
                super.add(obj);
            }
        }
    }

    // un iterateur qui filtre les repartitions pour les assures et les
    // repartitions fils.
    private class RepartitionsEmployeurIterator implements Iterator {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        boolean hasNext;
        int idRepartition;
        APRepartitionPaiements repartition;
        APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private RepartitionsEmployeurIterator() throws FWIException {
            repartitions.setSession(getSession());
            repartitions.setForIdPrestation(loadPrestationType().getIdPrestationApg());

            try {
                repartitions.find();
            } catch (final Exception e) {
                throw new FWIException("Impossible de charger les repartitions", e);
            }
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        @Override
        public boolean hasNext() {
            if (!hasNext) {
                while (idRepartition < repartitions.size()) {
                    repartition = (APRepartitionPaiements) repartitions.get(idRepartition++);

                    if (JadeStringUtil.isIntegerEmpty(repartition.getIdParent())
                            && repartition.isBeneficiaireEmployeur()) {
                        hasNext = true;

                        break;
                    }
                }
            }

            return hasNext;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         * @throws NoSuchElementException DOCUMENT ME!
         */
        @Override
        public Object next() {
            if (hasNext) {
                hasNext = false;

                return repartition;
            } else {
                throw new NoSuchElementException("plus d'elements dans cette iteration");
            }
        }

        /**
         * @throws UnsupportedOperationException DOCUMENT ME!
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static final JACalendar CALENDAR = new JACalendarGregorian();
    private static final String DOC_DEC_AMAT_COPIE_ASS = "documents.decision.amat.copie.assure";

    // Nom de fichier jasper pour les décisions
    private static final String FICHIER_MODELE = "AP_DECISION";

    // Permet de filter les MATCIAB2 des décisions pour les faire ressortir sur un document séparé
    private Boolean isMatciab2Decision = Boolean.FALSE;

    private static final String FICHIER_RESULTAT = "decision";
    private static final String ORDER_PRINTING_BY = "orderPrintingBy";
    private static final int STATE_ACM = 11;
    private static final int STATE_MATCIAB2 = 14;
    private static final int STATE_ASSURES = 1;

    private static final int STATE_EMPLOYEURS = 2;

    private static final int STATE_FIN = 3;
    private static final int STATE_FINDEC = 13;

    private static final int STATE_LAMAT = 12;
    private static final int STATE_STANDARD = 10;
    ICaisseReportHelper ch;

    private boolean clearDocumentList = false;

    private String codeIsoLangue = "FR";
    private boolean createDocumentCopie = false;
    private boolean createDocumentCopieFisc = false;
    private String csTypeDocument;
    private String date;
    private PRDemande demande = null;
    private JadePublishDocumentInfo docInfo = getDocumentInfo();
    private String docinfo_affilie_id = "";
    private String docinfo_affilie_tiers_id = "";
    private String docinfo_allocataire_tiers_id = "";
    private ICTDocument documentAssures;
    private boolean documentCopy = false;
    private ICTDocument documentEmployeurs;
    private APDroitMaternite droit;
    private Boolean employeursMultiples;
    private JADate firstBirth = new JADate();
    private ICTDocument helper = null;
    private String idDroit;

    String revenuMoyenDeterminant;
    double revenuMoyenDeterminantMAX;
    private String idTiersAssure = "";
    private boolean isDecompteRempli = false;

    private boolean isIndependant = false;
    private boolean isMoreThanEnfant = false;

    private Boolean isSendToGed = Boolean.FALSE;

    private Locale locale = null;

    private final int nbAdresseDansRepartition = -1;
    private int nbRepAssure = 0;
    private int nbRepEmployeur = 0;
    private final APPrestationManager prestations = new APPrestationManager();

    private APPrestation prestationType;
    private APRepartitionPaiements repartition;

    private RepartitionsEmployeurIterator repartitionsEmployeur;

    private int state = APDecisionCommunicationAMAT.STATE_ASSURES;

    private int state_dec = APDecisionCommunicationAMAT.STATE_STANDARD;

    public Boolean getIsMatciab2Decision() {
        return isMatciab2Decision;
    }

    public void setIsMatciab2Decision(Boolean matciab2Decision) {
        isMatciab2Decision = matciab2Decision;
    }

    public APDecisionCommunicationAMAT() throws FWIException {
        super();
        setImporter(new ImporterImpl());
    }

    /**
     * Crée une nouvelle instance de la classe APGenererDecisionProcess.
     *
     * @param parent DOCUMENT ME!
     * @throws FWIException DOCUMENT ME!
     */
    public APDecisionCommunicationAMAT(final BProcess parent) throws FWIException {
        super(parent, APApplication.APPLICATION_APG_REP, APDecisionCommunicationAMAT.FICHIER_RESULTAT);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererDecisionProcess.
     *
     * @param session DOCUMENT ME!
     * @throws FWIException DOCUMENT ME!
     */
    public APDecisionCommunicationAMAT(final BSession session) throws FWIException {
        super(session, APApplication.APPLICATION_APG_REP, APDecisionCommunicationAMAT.FICHIER_RESULTAT);
    }

    /**
     *
     */
    @Override
    public void beforeBuildReport() {
        try {

            getSession().getAttribute(FWServlet.VIEWBEAN);

            if (APDecisionCommunicationAMAT.STATE_ASSURES == state) {

                buildDecision(documentAssures);
                // pour supprimer la copie faite a l'assure
                if (isDocumentCopy()) {
                    clearDocumentList = true;
                }

                repartitionsEmployeur = new RepartitionsEmployeurIterator();

                if (repartitionsEmployeur.hasNext()) {
                    repartition = (APRepartitionPaiements) repartitionsEmployeur.next();
                    state = APDecisionCommunicationAMAT.STATE_EMPLOYEURS;
                } else {
                    state = APDecisionCommunicationAMAT.STATE_FIN;
                }

            } else if (APDecisionCommunicationAMAT.STATE_EMPLOYEURS == state) {

                if (isIndependant) {
                    // pas de copie pour les independants
                    if (!isDocumentCopy()) {
                        buildDecision(documentAssures);
                    } else {
                        clearDocumentList = true;
                    }
                } else {
                    buildDecision(documentEmployeurs);
                }

                if (repartitionsEmployeur.hasNext()) {
                    repartition = (APRepartitionPaiements) repartitionsEmployeur.next();
                } else {
                    state = APDecisionCommunicationAMAT.STATE_FIN;
                }
            }

        } catch (final Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "APDecisionCommunicationAMAT");
            abort();
        }
    }

    /**
     *
     */
    @Override
    public void beforeExecuteReport() {
        try {
            // le modele
            final String extensionModelCaisse = getSession().getApplication().getProperty("extensionModelITextCaisse");
            if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                setTemplateFile(APDecisionCommunicationAMAT.FICHIER_MODELE + extensionModelCaisse);
                final FWIImportManager im = getImporter();
                final File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                        + FWITemplateType.TEMPLATE_JASPER.toString());
                if ((sourceFile != null) && sourceFile.exists()) {
                    ;
                } else {
                    setTemplateFile(APDecisionCommunicationAMAT.FICHIER_MODELE);
                }
            } else {
                setTemplateFile(APDecisionCommunicationAMAT.FICHIER_MODELE);
            }
        } catch (final Exception e) {
            setTemplateFile(APDecisionCommunicationAMAT.FICHIER_MODELE);
        }

        try {

            chargerCatalogueTexte();

            // Récupérer les répartitions afin de déterminer si il y a 1
            // décompte à l'assuré,
            // ou 1 ou plusieurs à l' (aux) employeur(s)
            // ou 1 ou + pour chacun
            final APRepartitionPaiementsManager repartitionPaiementsManager = new APRepartitionPaiementsManager();
            repartitionPaiementsManager.setSession(getSession());

            for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {

                final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);

                repartitionPaiementsManager.setForIdPrestation(prestation.getIdPrestationApg());
                repartitionPaiementsManager.find(getTransaction());

                for (int idRP = 0; idRP < repartitionPaiementsManager.size(); ++idRP) {
                    final APRepartitionPaiements rp = (APRepartitionPaiements) repartitionPaiementsManager.get(idRP);

                    if (rp.isBeneficiaireEmployeur()) {
                        // si pas d'idAffilie --> Assuré
                        nbRepEmployeur++;
                    } else {
                        nbRepAssure++;
                    }
                }
            }

        } catch (final Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "APDecisionCommunicationAMAT");
            abort();
        }

        if ((documentAssures == null) || (documentEmployeurs == null)) {
            getMemoryLog()
                    .logMessage("catalogue de texte introuvable", FWMessage.ERREUR, "APDecisionCommunicationAMAT");
            abort();
        }
    }

    @Override
    public boolean beforePrintDocument() {
        // pour supprimer la copie faite a l'assure
        if (clearDocumentList) {
            getDocumentList().clear();
            clearDocumentList = false;
        }

        return super.beforePrintDocument();
    }

    private void buildDecision(ICTDocument document) throws Exception {

        // si on genere un document pour un employeur, il faut charger le bon
        // model en fonction de la langue de l'employeur
        if (document.equals(documentEmployeurs)) {
            final PRTiersWrapper employeur = PRTiersHelper.getTiersParId(getISession(), repartition.getIdTiers());

            // le helper est deja setter pour un document employeur
            codeIsoLangue = getSession().getCode(employeur.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            docinfo_allocataire_tiers_id = getIdTiersAssure();
            docinfo_affilie_tiers_id = repartition.getIdTiers();
            docinfo_affilie_id = repartition.getIdAffilie();

            helper.setCodeIsoLangue(codeIsoLangue);
            helper.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);

            if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
                helper.setNom("normal");
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_ACM) {
                helper.setNom("ACM");
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {
                helper.setNom("MATCIAB2");
                chargerCatalogueTexte();
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_LAMAT) {
                helper.setNom("Lamat");
            }

            final ICTDocument[] documents = helper.load();

            if ((documents != null) && (documents.length > 0)) {
                document = documents[0];
            }
        }

        if (document.equals(documentAssures)) {
            final PRTiersWrapper assure = PRTiersHelper.getTiersParId(getISession(),
                    tiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

            docinfo_allocataire_tiers_id = getIdTiersAssure();
            if (repartition != null) {
                docinfo_affilie_tiers_id = repartition.getIdTiers();
                docinfo_affilie_id = repartition.getIdAffilie();
            } else {
                docinfo_affilie_tiers_id = null;
            }

            // le helper est deja setter pour un document employeur
            codeIsoLangue = getSession().getCode(assure.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            helper.setCodeIsoLangue(codeIsoLangue);
            helper.setCsDestinataire(ICTDocument.CS_ASSURE);

            if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
                helper.setNom("normal");
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_ACM) {
                helper.setNom("ACM");
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {
                helper.setNom("MATCIAB2");
                chargerCatalogueTexte();
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_LAMAT) {
                helper.setNom("Lamat");
            }

            final ICTDocument[] documents = helper.load();

            if ((documents != null) && (documents.length > 0)) {
                document = documents[0];
            }
        }

        createDocInfo();

        // charger le helper pour les entetes et pieds de pages
        ch = CaisseHelperFactory.getInstance().getCaisseReportHelper(docInfo, getSession().getApplication(),
                codeIsoLangue);

        Map parametres = getImporter().getParametre();

        if (parametres == null) {
            parametres = new HashMap();
            getImporter().setParametre(parametres);
        } else {
            parametres.clear();
        }

        // l'en-tête du document
        // ---------------------------------------------------------------------------------------
        completeEntete(parametres, document.getCodeIsoLangue());

        // le titre
        // ----------------------------------------------------------------------------------------

        if (getCsTypeDocument().equalsIgnoreCase(IAPCatalogueTexte.CS_DECISION_MAT)) {
            if (hasMATCIAB2() && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {
                parametres.put("PARAM_TITRE", document.getTextes(1).getTexte(203).getDescription());
            } else {
                parametres.put("PARAM_TITRE", document.getTextes(1).getTexte(1).getDescription());
            }
        } else {
            // il s'agit d'une communication
            parametres.put("PARAM_TITRE", document.getTextes(1).getTexte(5).getDescription());
        }

        final Boolean paramCopie = new Boolean(getSession().getApplication().getProperty(
                APDecisionCommunicationAMAT.DOC_DEC_AMAT_COPIE_ASS));

        if ((isDocumentCopy() && paramCopie.booleanValue())
                || ((isDocumentCopy() && state_dec == APDecisionCommunicationAMAT.STATE_STANDARD))) {
            parametres.put("P_COPIE", getTextOrEmpty(document, 1, 4));
        }

        // le corps du document
        // ----------------------------------------------------------------------------------------
        StringBuffer buffer = new StringBuffer();
        ICTTexte texte;
        int count = 0;

        final APEnfantMatManager enfMgr = new APEnfantMatManager();
        enfMgr.setSession(getSession());
        enfMgr.setForIdDroitMaternite(droit.getIdDroit());
        enfMgr.find();

        final JACalendar cal = new JACalendarGregorian();

        if (enfMgr.size() > 1) {
            isMoreThanEnfant = true;

            firstBirth = new JADate("31.12.9999");

            for (final Iterator iterator = enfMgr.iterator(); iterator.hasNext(); ) {
                final APEnfantMat enfant = (APEnfantMat) iterator.next();

                if (cal.compare(new JADate(enfant.getDateNaissance()), firstBirth) == JACalendar.COMPARE_FIRSTLOWER) {
                    firstBirth = new JADate(enfant.getDateNaissance());
                }

            }

        } else {
            if (enfMgr.size() == 1) {
                firstBirth = new JADate(((APEnfantMat) enfMgr.getFirstEntity()).getDateNaissance());
            }
        }

        // fusionner tous les textes sauf les speciaux (position > 100)
        try {
            for (final Iterator textes = document.getTextes(2).iterator(); textes.hasNext(); ) {
                final int position = Integer.parseInt((texte = (ICTTexte) textes.next()).getPosition());

                if (position < 100) { // ignorer les textes speciaux

                    if ((position > 3) && (position < 14)) {

                    } else if (hasNotOnlyMATCIAB1() || position < 3) {

                        if ((buffer.length() > 0) && (count < 3)) {
                            if (!(position == 3 && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2)) {
                                buffer.append("\n\n"); // paragraphe (Seulement pour les 2 premiers blocs !
                            }
                        } else if (buffer.length() > 0) {
                            buffer.append(" "); // Espace pour les ajouts de
                            // lignes sans paragraphes
                        }

                        // si c'est le texte 2.2, et que la décision a plus d'un
                        // enfant, il faut mettre le texte 2.13 à la place
                        if ((position == 2) && isMoreThanEnfant && state_dec != APDecisionCommunicationAMAT.STATE_MATCIAB2) {
                            buffer.append(document.getTextes(2).getTexte(13).getDescription());
                        // si c'est le texte 2.2, et que la décision a plus d'un
                        // enfant et qu'il s'agit de MATCIAB2, il faut mettre le texte 2.201 à la place
                        } else if ((position == 2) && hasMATCIAB2() && isMoreThanEnfant && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {
                            buffer.append(document.getTextes(2).getTexte(201).getDescription());
                        // si c'est le texte 2.2, et que la décision ne possède qu'un seul
                        // enfant et qu'il s'agit de MATCIAB2, il faut mettre le texte 2.200 à la place
                        } else if ((position == 2) && hasMATCIAB2() && !isMoreThanEnfant && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {
                            buffer.append(document.getTextes(2).getTexte(200).getDescription());
                            if (!isEmployeursMultiples()) {
                                buffer.append("\n");
                            }
                        } else {
                            if (!(position == 3 && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2)) {
                                buffer.append(texte.getDescription());
                            }
                        }

                        count++;
                    }
                }
            }
        } catch (final NumberFormatException e) {
            throw new FWIException("Valeur de position incorrecte: " + e.getMessage());
        }

        completeCorps(parametres, buffer, document.getTextes(2));

        // la boucle de détail
        // -----------------------------------------------------------------------------------------
        parametres.put("PARAM_TITRE_DETAIL", document.getTextes(3).getTexte(1).getDescription());
        if (getCsTypeDocument().equalsIgnoreCase(IAPCatalogueTexte.CS_DECISION_MAT)) {
            // s'il s'agit de MATCIAB2, il faut créer le paramètre TITRE_DETAIL2 qui est utilisé dans le document à la place du "TITRE_DETAIL"
            if ((hasMATCIAB1() || hasMATCIAB2()) && (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD || state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2)) {
                parametres.put("PARAM_TITRE_DETAIL2", document.getTextes(3).getTexte(200).getDescription());
            }
        }
        parametres.put("PARAM_ALLOCATION_DU", document.getTextes(3).getTexte(2).getDescription());
        parametres.put("PARAM_AU", document.getTextes(3).getTexte(3).getDescription());
        parametres.put("PARAM_JOURS_A", document.getTextes(3).getTexte(4).getDescription());
        parametres.put("PARAM_CHF", document.getTextes(3).getTexte(5).getDescription());

        // le pied-de-page du document
        // ---------------------------------------------------------------------------------
        buffer.setLength(0); // on recycle

        // fusionner tous les textes sauf les speciaux (position > 100)
        try {
            for (final Iterator textes = document.getTextes(4).iterator(); textes.hasNext()
                    && (Integer.parseInt((texte = (ICTTexte) textes.next()).getPosition()) < 100); ) {

                if (buffer.length() > 0) {
                    buffer.append("\n\n");
                }

                String dateFinMat = JadeStringUtil.isEmpty(droit.getDateRepriseActiv())
                        ? JACalendar.format(droit.getDateFinDroit())
                        : APDecisionCommunicationAMAT.CALENDAR.addDays(JACalendar.format(droit.getDateRepriseActiv()), -1);
                String dateDebutMat = droit.getDateDebutDroit();
                int totalDeJours = PRDateUtils.getNbDayBetween(dateDebutMat, dateFinMat) + 1;

                boolean isPos3ACMEmployeur = Integer.parseInt(texte.getPosition()) == 3;
                isPos3ACMEmployeur &= "ACM".equals(helper.getNom());
                isPos3ACMEmployeur &= ICTDocument.CS_EMPLOYEUR.equals(helper.getCsDestinataire());

                boolean isPos3Standard = Integer.parseInt(texte.getPosition()) == 3;
                isPos3Standard &= "normal".equals(helper.getNom());

                boolean isPos2Standard = Integer.parseInt(texte.getPosition()) == 2;
                isPos2Standard &= "normal".equals(helper.getNom());
                isPos2Standard &= ICTDocument.CS_ASSURE.equals(helper.getCsDestinataire());

                boolean isPos3Lamat = Integer.parseInt(texte.getPosition()) == 3;
                isPos3Lamat &= "Lamat".equals(helper.getNom());
                isPos3Lamat &= ICTDocument.CS_EMPLOYEUR.equals(helper.getCsDestinataire());

                boolean isPos2MATCIAB2 = Integer.parseInt(texte.getPosition()) == 2;
                isPos2MATCIAB2 &= "MATCIAB2".equals(helper.getNom());

                // Cas particulier (Destinataire employeur et lettre ACM), car paragraphe 3 est devenu générique dans le
                // niveau 4 afin d'afficher le bon nombre de jours.
                if (isPos3ACMEmployeur) {
                    totalDeJours = limiteTotalDeJoursAvecMaxLamatEtACM(totalDeJours);
                    buffer.append(traitementLevel4Pos3ACMEmployeur(texte, totalDeJours));
                } else {
                    // si c'est le texte 4.3 ou 4.2, et qu'il s'agit de MATCIAB2, il faut mettre le texte 4.200 à la place
                    if ((Integer.parseInt(texte.getPosition()) == 3 && hasMATCIAB2() && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2 && ICTDocument.CS_EMPLOYEUR.equals(helper.getCsDestinataire()))
                        || (Integer.parseInt(texte.getPosition()) == 2 && hasMATCIAB2() && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2 && ICTDocument.CS_ASSURE.equals(helper.getCsDestinataire()))) {
                        buffer.append(document.getTextes(4).getTexte(200).getDescription());
                    // si c'est le texte 4.3 et qu'il s'agit de Lamat il faut remplacer l'argument par le totalDeJours
                    } else if (isPos3Lamat) {
                        totalDeJours = limiteTotalDeJoursAvecMaxLamatEtACM(totalDeJours);
                        buffer.append(PRStringUtils.formatMessage(new StringBuffer(texte.getDescription()), String.valueOf(totalDeJours)));
                    // si c'est le texte 4.3 ou 4.2 et qu'il s'agit de normal il faut remplacer l'argument par le totalDeJours
                    } else if (isPos3Standard || isPos2Standard || isPos2MATCIAB2) {
                        if (hasMATCIAB2()) {
                            totalDeJours += nombreDeJoursMATCIAB2();
                        }
                        buffer.append(PRStringUtils.formatMessage(new StringBuffer(texte.getDescription()), String.valueOf(totalDeJours)));
                    } else {
                        buffer.append(texte.getDescription());
                    }
                }
            }

        } catch (final NumberFormatException e) {
            throw new FWIException("Valeur de position incorrecte: " + e.getMessage());
        }

        // ajoute le texte impot source si décision standard et le droit est soumis à l'impôt source
        if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD && droit.getIsSoumisImpotSource()) {
            ajouteTexteImpotSource(document, buffer);
            this.setCreateDocumentCopieFisc(true);
        }

        completePied(parametres, buffer, document.getTextes(4));
        parametres.put("PARAM_PIED", buffer.toString());

        switch (state) {
            case STATE_ASSURES:

                buffer.setLength(0);
                buffer.append(document.getTextes(5).getTexte(1).getDescription());

                ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                Hashtable params = new Hashtable();
                params.put(ITITiers.FIND_FOR_IDTIERS, tiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                ITITiers[] t = tiersTitre.findTiers(params);
                if ((t != null) && (t.length > 0)) {
                    tiersTitre = t[0];
                }

                String titre = tiersTitre.getFormulePolitesse(tiersTitre.getLangue());

                buffer = new StringBuffer(PRStringUtils.formatMessage(buffer, titre));
                break;

            case STATE_EMPLOYEURS:

                buffer.setLength(0);
                buffer.append(document.getTextes(5).getTexte(1).getDescription());

                tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                params = new Hashtable();
                params.put(ITITiers.FIND_FOR_IDTIERS, repartition.getIdTiers());
                t = tiersTitre.findTiers(params);
                tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                if ((t != null) && (t.length > 0)) {
                    tiersTitre = t[0];
                }
                titre = tiersTitre.getFormulePolitesse(tiersTitre.getLangue());

                //Force un saut de page correct
                if (hasMATCIAB1() && isEmployeursMultiples() && hasNotOnlyMATCIAB1() && loadPrestations().size() == 7) {
                    buffer.append("\n\n");
                }

                buffer = new StringBuffer(PRStringUtils.formatMessage(buffer, titre));
                break;
        }

        parametres.put("PARAM_AP_DECISION_DETAIL",
                JadeStringUtil
                        .change(getSession().getApplication().getExternalModelPath()
                                + APApplication.APPLICATION_APG_REP, '\\', '/')
                        + "/" + "model" + "/" + "AP_DECISION_DETAIL.jasper");


        parametres.put("PARAM_SALUTATIONS", buffer.toString());

        // ajouter la signature
        // ---------------------------------------------------------------------------------
        try {
            ch.addSignatureParameters(getImporter());
        } catch (final Exception e) {
            throw new FWIException("Impossible de charger le pied de page", e);
        }

        if (isCreateDocumentCopie() && paramCopie.booleanValue()) {
            initCopieACopieA2(document, parametres);
        }
        if (isCreateDocumentCopie() && isCreateDocumentCopieFisc() && state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
            initCopieA2Fisc(document, parametres);
        }
    }

    private String getTextOrEmpty(ICTDocument document, int niveau, int position) {
        try {
            return document.getTextes(niveau).getTexte(position).getDescription();
        } catch (IndexOutOfBoundsException e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, "APDecisionCommunicationAMAT");
            return "";
        }
    }

    private void initCopieACopieA2(ICTDocument document, Map parametres) throws Exception {
        final TITiers t = new TITiers();
        t.setSession(getSession());
        t.setIdTiers(demande.getIdTiers());
        // chargement de la ligne de copie avec le formater
        final String ligne = t.getAdresseAsString(TIAvoirAdresse.CS_COURRIER, APApplication.CS_DOMAINE_ADRESSE_APG,
                JACalendar.todayJJsMMsAAAA(), new PRTiersAdresseCopyFormater01());

        parametres.put("P_COPIE_A", getTextOrEmpty(document, 1, 8));
        parametres.put("P_COPIE_A2", ligne);
    }

    private void initCopieA2Fisc(ICTDocument document, Map parametres) throws Exception {
        String idTiersAdmFiscale = PRTiersHelper.getIdTiersAdministrationFiscale(getSession(), demande.getIdTiers());

        parametres.putIfAbsent("P_COPIE_A2", "");
        if (!JadeStringUtil.isEmpty(idTiersAdmFiscale)) {
            // chargement de la ligne de copie avec le formater
            final String ligneAdmFiscale = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    idTiersAdmFiscale, APApplication.CS_DOMAINE_ADRESSE_APG, "", "",
                    new PRTiersAdresseCopyFormater02(), JACalendar.todayJJsMMsAAAA());
            parametres.put("P_COPIE_A2", parametres.get("P_COPIE_A2") + "\n" + ligneAdmFiscale);
        }
        parametres.putIfAbsent("P_COPIE_A", getTextOrEmpty(document, 1, 8));
    }

    private void ajouteTexteImpotSource(ICTDocument document, StringBuffer buffer) throws Exception {
        String taux = droit.getTauxImpotSource();

        // si aucun taux n'a été défini dans le droit
        if (JadeStringUtil.isDecimalEmpty(taux)) {

            // on va rechercher tous les taux pour ce canton et pour la période de la prestation
            String cantonImposition = PRTiersHelper.getCanton(getSession(), droit.getNpa());

            PRTauxImpositionManager mgrTauxImpot = new PRTauxImpositionManager();
            mgrTauxImpot.setSession(getSession());
            mgrTauxImpot.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
            mgrTauxImpot.setOrderBy(PRTauxImposition.FIELDNAME_DATEDEBUT);
            mgrTauxImpot.setForPeriode(droit.getDateDebutDroit(), droit.getDateFinDroit());
            mgrTauxImpot.setForCsCanton(cantonImposition);
            mgrTauxImpot.find(BManager.SIZE_NOLIMIT);

            List<PRTauxImposition> tauxImpots = mgrTauxImpot.getContainerAsList();

            // on retourne le premier taux trouvé
            if (tauxImpots.size() > 0) {
                taux = tauxImpots.get(0).getTaux();
            }
        }

        // on récupère et on complète le texte avec les paramètres
        String textImpotSource = getTextOrEmpty(document, 4, 105);
        if (StringUtils.isNotBlank(textImpotSource)) {
            buffer.setLength(0);
            buffer.append("\n\n");
            buffer.append(PRStringUtils.replaceString(textImpotSource, "{tauxImposition}", JANumberFormatter.format(taux, 0.01, 2, JANumberFormatter.NEAR)));
        }
    }

    private Boolean hasNotOnlyMATCIAB1() throws FWIException {
        if (hasMATCIAB1()) {
            // Création champs de données document assurées pour tous les types sauf MATCIAB1
            for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {

                final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);

                //<editor-fold defaultstate="collapsed" desc="ALLTYPE">
                if (APTypeDePrestation.STANDARD.isCodeSystemEqual(prestation.getGenre())) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    private Boolean hasMATCIAB1() throws FWIException {
        // Création champs de données document assurées pour tous les types sauf MATCIAB1
        for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {

            final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);

            //<editor-fold defaultstate="collapsed" desc="ALLTYPE">
            if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre())) {
                return true;
            }
        }
        return false;
    }

    private Boolean hasMATCIAB2() throws FWIException {
        // Recherche de prestations MATCIAB2
        for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {

            final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);

            if (APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) {
                return true;
            }
        }
        return false;
    }

    private int nombreDeJoursMATCIAB2() throws FWIException {
        // Recherche de la durée de la prestations MATCIAB2
        for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {

            final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);

            if (APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) {
                return !JadeStringUtil.isIntegerEmpty(prestation.getNombreJoursSoldes()) ? Integer.parseInt(prestation.getNombreJoursSoldes()) : 0;
            }
        }
        return 0;
    }

    /**
     * Pouvoir attribuer dans le niveau 4 position 3 ACM Employeur, le nombre de jours corrects selon si la décision
     * pour l'employeur verse que des ACM ou (ACM et ACM 2)
     *
     * @param texte Le catalogue de texte.
     * @return Le chaine du niveau 4 position 3 avec la nouvelle valeur.
     * @throws Exception Exception due a la recherche de situation professionnelle
     */
    private String traitementLevel4Pos3ACMEmployeur(ICTTexte texte, int totalDeJours) throws Exception {
        final StringBuffer bufferLocal = new StringBuffer(texte.getDescription());

        // reprendre la situation prof pour savoir si elle a un type ACM 2
        final APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
        sitPro.setSession(getSession());
        sitPro.setIdSituationProf(repartition.getIdSituationProfessionnelle());
        sitPro.retrieve();

        if (!sitPro.isNew()) {
            final boolean hasAcm2 = sitPro.getHasAcm2AlphaPrestations();

            if (hasAcm2) {
                totalDeJours += Integer.valueOf(APProperties.PRESTATION_ACM_2_NOMBRE_JOURS.getValue());
            }
        }

        return PRStringUtils.formatMessage(bufferLocal, String.valueOf(totalDeJours));
    }

    private int limiteTotalDeJoursAvecMaxLamatEtACM(int totalDeJours) throws Exception {
        // Calcule la différence entre durée ACM1 et Maternité Federale puis ajoute si besoin la durée ACM2
        // le montant obtenu est ensuite ajouté à la durée totale de la maternité pour être placé dans le texte de la décision.
        int dureeMatAvecACM = Integer.parseInt(APProperties.DROIT_ACM_MAT_DUREE_JOURS.getValue());
        int dureeMatFederale = Integer.parseInt(getSession().getApplication().getProperty(APApplication.PROPERTY_DROIT_MAT_DUREE_JOURS));
        int dureeACMSeulement = dureeMatAvecACM - dureeMatFederale;

        if (totalDeJours <= dureeMatFederale) {
            totalDeJours += dureeACMSeulement;
        } else {
            totalDeJours = dureeMatAvecACM;
        }
        return totalDeJours;
    }

    private String buildOrderPrintingByKey(final String idAffilie, final String idTiers) throws Exception {

        String noAffilieFormatte = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
        String noAvsFormatte = PRBlankBNumberFormater.getEmptyNssFormatte(getSession().getApplication());

        final IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
        final IFormatData avsFormatter = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

        if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
            final IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                    idAffilie, idTiers);
            noAffilieFormatte = affilieFormatter.unformat(affilie.getNumAffilie());
        }

        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);

            if (tierWrapper == null) {
                tierWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
            }

            if (!JadeStringUtil.isEmpty(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                noAvsFormatte = avsFormatter.unformat(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            }
        }

        return noAffilieFormatte + "_" + noAvsFormatte;
    }

    public void chargerCatalogueTexte() {

        try {

            // juste pour avoir les dates de debut et de fin du droit
            droit = new APDroitMaternite();
            droit.setSession(getSession());
            droit.setIdDroit(getIdDroit());
            droit.retrieve();

            // on cherche la langue de l'assure
            demande = new PRDemande();
            demande.setSession(getSession());
            demande.setIdDemande(droit.getIdDemande());
            demande.retrieve();

            setIdTiersAssure(demande.getIdTiers());

            final PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), demande.getIdTiers());
            String codeIsoLangue1 = getSession().getCode(tierWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue1 = PRUtil.getISOLangueTiers(codeIsoLangue1);

            // charger le catalogue de texte
            helper = PRBabelHelper.getDocumentHelper(getISession());

            helper.setCsTypeDocument(IAPCatalogueTexte.CS_DECISION_MAT);
            // decision
            // et
            // communication,
            // meme
            // catalogue
            helper.setCsDomaine(IAPCatalogueTexte.CS_MATERNITE);

            if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
                helper.setNom("normal");
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_ACM) {
                helper.setNom("ACM");
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {
                helper.setNom("MATCIAB2");
            } else if (state_dec == APDecisionCommunicationAMAT.STATE_LAMAT) {
                helper.setNom("Lamat");
            }

            helper.setActif(Boolean.TRUE);

            // pour l'assure
            helper.setCsDestinataire(ICTDocument.CS_ASSURE);

            // on cherche la langue de l'assure
            helper.setCodeIsoLangue(codeIsoLangue1);

            ICTDocument[] documents = helper.load();

            if ((documents != null) && (documents.length > 0)) {
                documentAssures = documents[0];
            }

            // pour les employeurs
            helper.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);

            documents = helper.load();

            if ((documents != null) && (documents.length > 0)) {
                documentEmployeurs = documents[0];
            }

        } catch (final Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "APDecisionCommunicationAMAT");
            abort();
        }

    }

    private Double getMontantMaxSelonCantonSitPro() throws Exception {
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

        // Situations professionnelles
        final APEntityService servicePersistance = ApgServiceLocator.getEntityService();
        final List<APSitProJointEmployeur> apSitProJointEmployeurs = servicePersistance
                .getSituationProfJointEmployeur(getSession(), getTransaction(), idDroit);

        for (APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeurs) {
            Map<IAFAssurance, String> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercherAvecDateDebut(idDroit, apSitProJointEmployeur.getIdAffilie(), getSession());
            for (Map.Entry<IAFAssurance, String> assurance : listAssurance.entrySet()) {
                if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelBE) || assurance.getKey().getAssuranceId().equals(idAssuranceParitaireBE)) {
                    return Double.valueOf(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1", "MATCIABBEM", "0", "", 0));
                } else if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelJU) || assurance.getKey().getAssuranceId().equals(idAssuranceParitaireJU)) {
                    return Double.valueOf(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1", "MATCIABJUM", "0", "", 0));
                }
            }
        }
        return null;
    }

    private void completeCorps(final Map parametres, final StringBuffer buffer, final ICTListeTextes textes)
            throws Exception {
        Object[] arguments = null;

        switch (state) {
            case STATE_ASSURES:
                arguments = completeCorpsAssures(buffer, textes);

                break;

            case STATE_EMPLOYEURS:
                if (isIndependant) {
                    arguments = completeCorpsAssures(buffer, textes);
                } else {
                    arguments = completeCorpsEmployeurs(buffer, textes);
                }

                break;
        }

        // remplacement
        final FWMessageFormat message = createMessageFormat(buffer);

        buffer.setLength(0); // on recycle

        parametres.put("PARAM_CORPS", message.format(arguments, buffer, new FieldPosition(0)).toString());
    }

    private Object[] completeCorpsAssures(final StringBuffer buffer, final ICTListeTextes textes) throws Exception {

        // BZ 5373 : SI IJ AC, recalculer droitAcquis selon règle de calcul ((droitAcquis * 21.7)/30)
        BigDecimal droitAcquis = new BigDecimal(droit.getDroitAcquis());

        if (IAPDroitAPG.CS_IJ_ASSURANCE_CHOMAGE.equals(droit.getCsProvenanceDroitAcquis())) {
            droitAcquis = (droitAcquis.multiply(new BigDecimal("21.7"))).divide(new BigDecimal("30.0"), 2,
                    BigDecimal.ROUND_HALF_UP);

            final FWCurrency droitAcquisArrondiAuCinqCentimes = new FWCurrency(droitAcquis.doubleValue());
            droitAcquisArrondiAuCinqCentimes.round(FWCurrency.ROUND_5CT);
            droitAcquis = new BigDecimal(droitAcquisArrondiAuCinqCentimes.toString());
        }

        if (loadPrestationType().getMontantJournalier().equals(droitAcquis.toString())) {
            buffer.append(" "); // espace
            buffer.append(textes.getTexte(101).getDescription());
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
            if (hasNotOnlyMATCIAB1()) {
                buffer.append(" "); // espace
                buffer.append(textes.getTexte(4).getDescription());
            }
        }

        // ajouter le texte concernant l'employeur si nécessaire
        // 1. Ajouter "L'allocation est calculée en tenant compte"
        // 2. si versement à l'employeur, ajouter
        // "qu'employeur réclame sa propre part"
        // 3. si emp ne verse pas les 80%, ajouter
        // "qu'employeur ne verse pas 80% du salaire habituel"
        // 4. si contrat de travail --> fin, ajouter
        // "qu'on contrat de travail arrive à échéance"
        // --> Attention, plusieurs cas possible, et dans une seule phrase...
        // (Gestion des virgules)

        if (nbRepEmployeur > 0) {

            int nbPhrase = 0;
            boolean isVersementIncomplet = false;
            boolean isContratTravailEcheance = false;

            final APSituationProfessionnelleManager situationProfessionnelleMan = new APSituationProfessionnelleManager();

            situationProfessionnelleMan.setForIdDroit(idDroit);
            situationProfessionnelleMan.setSession(getSession());

            try {
                situationProfessionnelleMan.find();
            } catch (final Exception e) {
                e.printStackTrace();
            }

            for (int idSP = 0; idSP < situationProfessionnelleMan.size(); ++idSP) {
                final APSituationProfessionnelle sp = (APSituationProfessionnelle) situationProfessionnelleMan
                        .get(idSP);

                if (!JadeStringUtil.isIntegerEmpty(sp.getMontantVerse())
                        && !JadeStringUtil.isIntegerEmpty(sp.getPourcentMontantVerse())) {
                    isVersementIncomplet = true;
                }

                if (!JadeStringUtil.isEmpty(sp.getDateFinContrat())) {
                    isContratTravailEcheance = true;
                }
            }

            if (isEmployeursMultiples() || isVersementIncomplet || isContratTravailEcheance) {
                buffer.append(" ");
                buffer.append(PRStringUtils.replaceString(textes.getTexte(6).getDescription(), "{montantAnnuel}", "{10}"));
//                buffer.append(textes.getTexte(6).getDescription());
            }

            if (isEmployeursMultiples()) {
                buffer.append(" ");
                buffer.append(textes.getTexte(7).getDescription());
                nbPhrase++;
            }

            if (isVersementIncomplet) {
                if (nbPhrase > 0) {
                    buffer.append(", ");
                } else {
                    buffer.append(" ");
                }
                buffer.append(textes.getTexte(8).getDescription());
                nbPhrase++;
            }

            if (isContratTravailEcheance) {
                if (nbPhrase > 0) {
                    buffer.append(", ");
                } else {
                    buffer.append(" ");
                }
                buffer.append(textes.getTexte(9).getDescription());
                nbPhrase++;
            }

            if (isEmployeursMultiples() || isVersementIncomplet || isContratTravailEcheance) {
                buffer.append(".");
            }

            if (hasNotOnlyMATCIAB1() || isEmployeursMultiples()) {
                buffer.append("\n");
            }
        }

        // creer les arguments a remplacer dans le texte

        ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
        final Hashtable params = new Hashtable();
        params.put(ITITiers.FIND_FOR_IDTIERS, tiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        tiersTitre.findTiers(params);
        final ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }
        final String titre = tiersTitre.getFormulePolitesse(tiersTitre.getLangue());

        final Object[] arguments = new Object[8];

        arguments[0] = titre;
        arguments[1] = JACalendar.format(firstBirth.toString(), codeIsoLangue);
        arguments[2] = JACalendar.format(droit.getDateDebutDroit());

        arguments[3] = JadeStringUtil.isEmpty(droit.getDateRepriseActiv()) ? JACalendar.format(droit.getDateFinDroit())
                : APDecisionCommunicationAMAT.CALENDAR.addDays(JACalendar.format(droit.getDateRepriseActiv()), -1);
        // arguments[4] =
        // JANumberFormatter.format(Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant()),
        // 1, 2 ,JANumberFormatter.NEAR);
        arguments[5] = loadPrestationType().getMontantJournalier();
        arguments[6] = droit.getDroitAcquis();

        APReferenceDataAPG ref;
        final JADate dateDebut = new JADate(droit.getDateDebutDroit());
        final JADate dateFin = new JADate(droit.getDateFinDroit());

        // début

        final APSituationProfessionnelleManager sitProMan = new APSituationProfessionnelleManager();
        sitProMan.setSession(getSession());
        sitProMan.setForIdDroit(droit.getIdDroit());
        sitProMan.find();

        final BigDecimal revenuAnnuel = sitProMan.getRevenuAnnuelSituationsProfessionnelles();

        // Si c'est une décision standard (Maternité fédérale)
        if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
            ref = (APReferenceDataAPG) APReferenceDataParser.loadReferenceData(getSession(), "MATERNITE", dateDebut,
                    dateFin, dateFin);
            final double montantJournalierMax = ref.getGE().intValue();
            final double montantAnnuelMax = montantJournalierMax * 360;

            if (hasMATCIAB1()) {
                Double tmp = getMontantMaxSelonCantonSitPro();
                if (tmp != null) {
                    revenuMoyenDeterminantMAX = tmp;
                }
            }
            revenuMoyenDeterminant = JANumberFormatter.format(
                        Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant()), 0.05, 2,
                        JANumberFormatter.NEAR);

            // Evite l'erreur d'arrondi stocké dans APPrestation.revenuMoyenDeterminant
            if (hasMATCIAB1()) {
                APPrestation matciab1 = loadPrestationTypeMATCIAB1();
                if (matciab1 != null) {
                    revenuMoyenDeterminant = JANumberFormatter.format(
                            Double.parseDouble(loadPrestationType().getBasicDailyAmount()) + Double.parseDouble(matciab1.getBasicDailyAmount()), 0.05, 2,
                            JANumberFormatter.NEAR);
                }
            }

            boolean isMontantMax = false;

            if (montantJournalierMax <= Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant())) {
                arguments[7] = PRStringUtils.replaceString(textes.getTexte(10).getDescription(), "{montantAnnuelMax}",
                        JANumberFormatter.format(montantAnnuelMax));
                isMontantMax = true;
            } else {
                arguments[7] = PRStringUtils.replaceString(textes.getTexte(11).getDescription(), "{montantAnnuel}",
                        JANumberFormatter.format(revenuAnnuel));
            }

            if (isMontantMax) {
                arguments[4] = JANumberFormatter.format(montantJournalierMax);
            } else {
                arguments[4] = JANumberFormatter.format(
                        Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant()), 1, 2,
                        JANumberFormatter.SUP);
            }

            // Si c'est une décision Lamat (Maternité cantonale)
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_LAMAT) {

            arguments[7] = JANumberFormatter.format(revenuAnnuel);

            // Si c'est une décision complémentaire (ACM)
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_ACM) {

            arguments[7] = JANumberFormatter.format(revenuAnnuel);

            // Si c'est une décision complémentaire (MATCIAB2)
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {

            arguments[7] = JANumberFormatter.format(revenuAnnuel);

            if (hasMATCIAB2()) {
                APPrestation matciab2 = loadPrestationTypeMATCIAB2();
                if (matciab2 != null) {
                    revenuMoyenDeterminant = JANumberFormatter.format(
                            Double.parseDouble(matciab2.getRevenuMoyenDeterminant()), 0.05, 2,
                            JANumberFormatter.NEAR);
                }
            }

        }

        return arguments;
    }

    private Object[] completeCorpsEmployeurs(final StringBuffer buffer, final ICTListeTextes textes) throws Exception {

        final Object[] arguments = new Object[11];

        // ajouter le texte pour les ij si necessaire
        if (loadPrestationType().getMontantJournalier().equals(droit.getDroitAcquis())) {
            buffer.append(" "); // paragraphe
            buffer.append(textes.getTexte(101).getDescription());
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
            if (hasNotOnlyMATCIAB1()) {
                buffer.append(" "); // espace
                buffer.append(textes.getTexte(4).getDescription());
            }
        }

        // ajouter le paragraphe sur la répartition de paiement si nécessaire
        if (isEmployeursMultiples()) {
            buffer.append(" "); // ligne

            // si c'est le texte 2.102, et qu'il s'agit de MATCIAB2, il faut mettre le texte 2.202 à la place
            if ((hasMATCIAB2() && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2)) {
                buffer.append(textes.getTexte(202).getDescription());
            } else {
                buffer.append(textes.getTexte(102).getDescription());
            }

            // calculer le pourcentage de l'employeur
            final double pourcent = Double.parseDouble(repartition.getTauxRJM()) / 100d;

            arguments[8] = repartition.getTauxRJM();
            arguments[9] = JANumberFormatter.format(Double.parseDouble(loadPrestationType().getMontantJournalier())
                    * pourcent, 0.05, 2, JANumberFormatter.NEAR);
        }

        // ajouter textes supp si nécessaire
        if ((nbRepAssure > 0) && (nbRepEmployeur > 0)) {
            if (isSituationProfessionnelleSpeciale()) {
                buffer.append(" ");
                buffer.append(textes.getTexte(103).getDescription());
            }
        }

        if (!hasNotOnlyMATCIAB1()) {
        } else if (hasMATCIAB1() && isEmployeursMultiples()) {
        } else if (hasMATCIAB2() && !isEmployeursMultiples()) {
        } else {
            buffer.append("\n");
        }

        // creer les arguments a remplacer dans le texte

        ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
        final Hashtable params = new Hashtable();
        params.put(ITITiers.FIND_FOR_IDTIERS, repartition.getIdTiers());
        tiersTitre.findTiers(params);
        final ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }
        final String titre = tiersTitre.getFormulePolitesse(tiersTitre.getLangue());

        // String codeIsoLangue =
        // getSession().getCode(tiers().getProperty(PRTiersWrapper.PROPERTY_LANGUE));

        arguments[0] = titre;
        arguments[1] = JACalendar.format(firstBirth.toString(), codeIsoLangue);
        arguments[2] = tiers().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + tiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        arguments[3] = JACalendar.format(droit.getDateDebutDroit());
        arguments[4] = JadeStringUtil.isEmpty(droit.getDateRepriseActiv()) ? JACalendar.format(droit.getDateFinDroit())
                : APDecisionCommunicationAMAT.CALENDAR.addDays(JACalendar.format(droit.getDateRepriseActiv()), -1);
        arguments[5] = JANumberFormatter.format(Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant()),
                1, 2, JANumberFormatter.SUP);
        arguments[6] = loadPrestationType().getMontantJournalier();
        arguments[7] = droit.getDroitAcquis();

        APReferenceDataAPG ref;
        final JADate dateDebut = new JADate(droit.getDateDebutDroit());
        final JADate dateFin = new JADate(droit.getDateFinDroit());

        // début

        final APSituationProfessionnelleManager sitProMan = new APSituationProfessionnelleManager();
        sitProMan.setSession(getSession());
        sitProMan.setForIdDroit(droit.getIdDroit());
        sitProMan.find();

        final BigDecimal revenuAnnuel = sitProMan.getRevenuAnnuelSituationsProfessionnelles();

        // Si c'est une décision standard (Maternité fédérale)
        if (state_dec == APDecisionCommunicationAMAT.STATE_STANDARD) {
            ref = (APReferenceDataAPG) APReferenceDataParser.loadReferenceData(getSession(), "MATERNITE", dateDebut,
                    dateFin, dateFin);
            final double montantJournalierMax = ref.getGE().intValue();
            final double montantAnnuelMax = montantJournalierMax * 360;

            if (hasMATCIAB1()) {
                Double tmp = getMontantMaxSelonCantonSitPro();
                if (tmp != null) {
                    revenuMoyenDeterminantMAX = tmp;
                }
            }
            revenuMoyenDeterminant = JANumberFormatter.format(
                        Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant()), 0.05, 2,
                        JANumberFormatter.NEAR);

            // Evite l'erreur d'arrondi stocké dans APPrestation.revenuMoyenDeterminant
            if (hasMATCIAB1()) {
                APPrestation matciab1 = loadPrestationTypeMATCIAB1();
                if (matciab1 != null) {
                    revenuMoyenDeterminant = JANumberFormatter.format(
                            Double.parseDouble(loadPrestationType().getBasicDailyAmount()) + Double.parseDouble(matciab1.getBasicDailyAmount()), 0.05, 2,
                            JANumberFormatter.NEAR);
                }
            }

            boolean isMontantMax = false;

            if (montantJournalierMax <= Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant())) {
                arguments[10] = PRStringUtils.replaceString(textes.getTexte(5).getDescription(), "{montantAnnuelMax}",
                        JANumberFormatter.format(montantAnnuelMax));
                isMontantMax = true;
            } else {
                arguments[10] = PRStringUtils.replaceString(textes.getTexte(6).getDescription(), "{montantAnnuel}",
                        JANumberFormatter.format(revenuAnnuel));
            }

            if (isMontantMax) {
                arguments[5] = JANumberFormatter.format(montantJournalierMax);
            } else {
                arguments[5] = JANumberFormatter.format(
                        Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant()), 1, 2,
                        JANumberFormatter.SUP);
            }

            // Si c'est une décision Lamat (Maternité cantonale)
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_LAMAT) {

            arguments[10] = JANumberFormatter.format(revenuAnnuel);

            // Si c'est une décision complémentaire (ACM)
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_ACM) {

            arguments[10] = JANumberFormatter.format(revenuAnnuel);

            // Si c'est une décision complémentaire (MATCIAB2)
        } else if (state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2) {

            arguments[10] = JANumberFormatter.format(revenuAnnuel);

            if (hasMATCIAB2()) {
                APPrestation matciab2 = loadPrestationTypeMATCIAB2();
                if (matciab2 != null) {
                    revenuMoyenDeterminant = JANumberFormatter.format(
                            Double.parseDouble(matciab2.getRevenuMoyenDeterminant()), 0.05, 2,
                            JANumberFormatter.NEAR);
                }
            }

        }

        return arguments;
    }

    private void completeEntete(final Map parametres, String codeIsoLangue) throws FWIException, JAException,
            ParseException {
        final CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        codeIsoLangue = this.codeIsoLangue;
        crBean.setDate(JACalendar.format(date, codeIsoLangue));

        try {
            String noAVS;
            // Si on souhaite afficher le dossier traié par qui corresond au gestionnaire
            if (APProperties.PROPERTY_AFFICHER_TRAITE_PAR.getBooleanValue()) {
                noAVS = tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                if (!JadeStringUtil.isBlankOrZero(droit.getIdGestionnaire())) {
                    JadeUserService userService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                            .getUserService();
                    JadeUser gestionnaire;
                    try {
                        gestionnaire = userService.load(droit.getIdGestionnaire());
                        crBean.setNomCollaborateur(gestionnaire.getFirstname() + " " + gestionnaire.getLastname());
                    } catch (Exception e) {
                        throw new FWIException("Impossible de charger le gestionnaire", e);
                    }
                }

            } else {
                noAVS = tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + "\n"
                        + tiers().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM);

            }

            if ("true".equals(getSession().getApplication().getProperty(APApplication.PROPERTY_DISPLAY_NIP))) {
                // On concatène le NIP à la suite du nom.
                // Car si rempli comme pour l'employeur, superposition de
                // plusieurs ligne.
                final String s = getSession().getLabel("NIP") + " "
                        + tiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                noAVS = noAVS + "\n" + s;
            }
            crBean.setNoAvs(noAVS);
        } catch (final Exception e) {
            crBean.setNoAvs(tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + "\n"
                    + tiers().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        }

        switch (state) {
            case STATE_ASSURES:
                completeEnteteAssures(crBean, parametres);

                break;

            case STATE_EMPLOYEURS:
                completeEnteteEmployeurs(crBean, parametres);

                break;
        }

        try {
            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(getSession().getApplication().getProperty(APApplication.PROPERTY_DOC_CONFIDENTIEL))) {

                crBean.setConfidentiel(true);
            }

            ch.addHeaderParameters(getImporter(), crBean);
        } catch (final Exception e) {
            throw new FWIException("Impossible d'ajouter l'en-tete", e);
        }
    }

    private void completeEnteteAssures(final CaisseHeaderReportBean crBean, final Map parametres) throws FWIException {

        // parametres du template
        final String idTiers = tiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        String adresse = "";

        try {
            adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(), idTiers, "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE);
        } catch (final Exception e) {
            throw new FWIException("Impossible de charger l'adresse de l'assuré", e);
        }

        crBean.setAdresse(adresse);

        // titre du document
        final String avs = tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        final String nom = tiers().getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                + tiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM);

        setDocumentTitle(avs + " - " + nom);

        // Complément en-tete (nom et prénom de l'assuré
        // parametres.put("P_HEADER_NOM_PRENOM", nom);

        // Les données de l'entête dès la deuxième page
        // Les autres paramètres sont repris de l'entête normal
        // ----------------------------------------------------------------------------------------------------
        try {
            parametres.put("PARAM_TITRE2", getSession().getApplication()
                    .getLabel("TITRE_DECISION_PAGE2", codeIsoLangue) + getDate());
        } catch (final FWIException e1) {
            e1.printStackTrace();
        } catch (final Exception e1) {
            e1.printStackTrace();
        }
        parametres.put("P_HEADER_NOM", nom);

    }

    private void completeEnteteEmployeurs(final CaisseHeaderReportBean crBean, final Map parametres)
            throws FWIException {
        String adresse = "";
        String numAffilie = "";

        try {

            adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(), repartition.getIdTiers(),
                    repartition.getIdAffilie(), IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE);

            if (!JadeStringUtil.isIntegerEmpty(repartition.getIdAffilie())) {
                final IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getISession(), getSession()
                        .getCurrentThreadTransaction(), repartition.getIdAffilie(), repartition.getIdTiers());

                numAffilie = affilie.getNumAffilie();

                // Renseignement du numéro ide
                AFIDEUtil.addNumeroIDEInDoc(crBean, affilie.getNumeroIDE(), affilie.getIdeStatut());
            }

            crBean.setNoAffilie(numAffilie);

        } catch (final Exception e) {
            throw new FWIException("Impossible de charger l'employeur", e);
        }

        setDocumentTitle(numAffilie + " - " + repartition.getNom());
        crBean.setAdresse(adresse);

        if (JadeStringUtil.isEmpty(tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
            crBean.setNoAffilie(numAffilie);
        }

        // Complément en-tete (nom et prénom de l'assuré
        final String nom = tiers().getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                + tiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        parametres.put("P_HEADER_NOM_PRENOM", nom);

        // Les données de l'entête dès la deuxième page
        // Les autres paramètres sont repris de l'entête normal
        // ----------------------------------------------------------------------------------------------------
        try {
            parametres.put("PARAM_TITRE2", getSession().getApplication()
                    .getLabel("TITRE_DECISION_PAGE2", codeIsoLangue) + getDate());
        } catch (final FWIException e1) {
            e1.printStackTrace();
        } catch (final Exception e1) {
            e1.printStackTrace();
        }
        parametres.put("P_HEADER_NOM", nom);

    }

    private void completePied(final Map parametres, final StringBuffer buffer, final ICTListeTextes textes)
            throws Exception {
        switch (state) {
            case STATE_ASSURES:
                completePiedAssures(parametres, buffer, textes);

                break;

            case STATE_EMPLOYEURS:
                completePiedEmployeurs(parametres, buffer, textes);

                break;
        }
    }

    private void completePiedAssures(final Map parametres, final StringBuffer buffer, final ICTListeTextes textes)
            throws FWIException {

        // insertion de la remarque si elle existe
        if (!JadeStringUtil.isEmpty(droit.getRemarque())) {

            StringBuffer buffer2 = new StringBuffer();
            buffer2.append("\n\n" + textes.getTexte(104).getDescription());
            buffer2 = new StringBuffer(PRStringUtils.formatMessage(buffer2, droit.getRemarque()));

            buffer.append(buffer2.toString());
            // buffer.append("\n");
            // buffer.append(textes.getTexte(103).getDescription());
        }

        // insertion du texte opposition si nécessaire
        if (IAPCatalogueTexte.CS_DECISION_MAT.equals(csTypeDocument)) {
            buffer.append("\n\n" + textes.getTexte(102).getDescription());
        }

    }

    private void completePiedEmployeurs(final Map parametres, final StringBuffer buffer, final ICTListeTextes textes)
            throws Exception {
        // insertion de la remarque si elle existe
        if (!JadeStringUtil.isEmpty(droit.getRemarque())) {

            StringBuffer buffer2 = new StringBuffer();
            buffer2.append("\n\n" + textes.getTexte(104).getDescription());
            buffer2 = new StringBuffer(PRStringUtils.formatMessage(buffer2, droit.getRemarque()));

            buffer.append(buffer2.toString());
            // buffer.append("\n");
            // buffer.append(textes.getTexte(103).getDescription());

        }

        // insertion du texte opposition si nécessaire

        if (IAPCatalogueTexte.CS_DECISION_MAT.equals(csTypeDocument)) {
            buffer.append("\n\n" + textes.getTexte(102).getDescription());
        }

    }

    /**
     * Charger les prestations pour le droit et charger aussi le droit pour les textes de la décision.
     */
    @Override
    public void createDataSource() {
        try {
            setTailleLot(1);
            setImpressionParLot(true);

            // charger les prestations
            final List lignes = new LinkedList();

            chargerCatalogueTexte();

            /*
             * il y a trois possibilites: 1. on construit le document de l'assure. Dans ce cas on affiche les montants
             * des prestations non modifies meme si ce montant sera verse a son employeur.
             *
             * 2. on construit le document de l'employeur et l'assure n'a qu'un seul employeur Dans ce cas on affiche
             * les memes donnees que pour l'assure.
             *
             * 3. on construit le document de l'employeur et l'assure a plusieurs employeurs Dans ce cas on affiche les
             * données corrigees au pro rata de la contribution de l'employeur au revenu journalier moyen de l'assuré.
             * C'est-a-dire qu'on va charger les repartitions de paiements et trouver celle qui correspond à l'employe
             * pour lequel on est en train de construire le document et afficher les montants indiques dans cette
             * repartition de paiement.
             */
            if ((state == APDecisionCommunicationAMAT.STATE_ASSURES) || !isEmployeursMultiples()) {

                final APRepartitionPaiementsManager repartitionPaiementsManager = new APRepartitionPaiementsManager();
                repartitionPaiementsManager.setSession(getSession());

                int nbPrest = 0;

                // Création champs de données document assurées pour tous les types sauf MATCIAB1
                for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {

                    final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);

                    //<editor-fold defaultstate="collapsed" desc="ALLTYPE">
                    if (!APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre()) && !APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) {
                        if (isGenreSameAsStateDecision(prestation)) {

                            nbPrest++;

                            // si idAffilie de la répartition = 0 && que c'est
                            // l'assuré (par idTiers)
                            repartitionPaiementsManager.setForIdPrestation(prestation.getIdPrestationApg());
                            repartitionPaiementsManager.find();

                            for (int idRP = 0; idRP < repartitionPaiementsManager.size(); ++idRP) {
                                final APRepartitionPaiements rp = (APRepartitionPaiements) repartitionPaiementsManager
                                        .get(idRP);

                                // reprendre la situation prof pour voir si indépendant
                                final APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
                                sitPro.setSession(getSession());
                                sitPro.setIdSituationProf(rp.getIdSituationProfessionnelle());
                                sitPro.retrieve();

                                if (sitPro.getIsIndependant().booleanValue()) {
                                    isIndependant = true;
                                } else {
                                    isIndependant = false;
                                }

                                final StringBuffer buffer = new StringBuffer();
                                FWMessageFormat message = createMessageFormat(buffer);

                                if (state == APDecisionCommunicationAMAT.STATE_ASSURES) {
                                    if (JadeStringUtil.isIntegerEmpty(rp.getIdAffilie())
                                            && rp.getIdTiers().equals(getIdTiersAssure())
                                            && !JadeStringUtil.isIntegerEmpty(rp.getMontantBrut())) {

                                        final Map champs = new HashMap();
                                        final double nbJours = Double.parseDouble(prestation.getNombreJoursSoldes());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(6));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_DEBUT",
                                                message.format(new Object[]{prestation.getDateDebut()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(7));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_FIN",
                                                message.format(new Object[]{prestation.getDateFin()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(8));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_NB_JOURS",
                                                message.format(new Object[]{prestation.getNombreJoursSoldes()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(9));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_JOURNALIER",
                                                message.format(
                                                        new Object[]{String.valueOf(JANumberFormatter.format(
                                                                Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2,
                                                                JANumberFormatter.NEAR))}, buffer, new FieldPosition(0))
                                                        .toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(10));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_BRUT",
                                                message.format(
                                                        new Object[]{JANumberFormatter.formatNoRound(rp.getMontantBrut())},
                                                        buffer, new FieldPosition(0)).toString());

                                        lignes.add(champs);
                                    }
                                } else {
                                    if (!JadeStringUtil.isIntegerEmpty(rp.getIdAffilie())
                                            && !JadeStringUtil.isIntegerEmpty(rp.getMontantBrut())) {

                                        final Map champs = new HashMap();
                                        final double nbJours = Double.parseDouble(prestation.getNombreJoursSoldes());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(6));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_DEBUT",
                                                message.format(new Object[]{prestation.getDateDebut()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(7));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_FIN",
                                                message.format(new Object[]{prestation.getDateFin()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(8));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_NB_JOURS",
                                                message.format(new Object[]{prestation.getNombreJoursSoldes()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(9));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_JOURNALIER",
                                                message.format(
                                                        new Object[]{String.valueOf(JANumberFormatter.format(
                                                                Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2,
                                                                JANumberFormatter.NEAR))}, buffer, new FieldPosition(0))
                                                        .toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(10));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_BRUT",
                                                message.format(
                                                        new Object[]{JANumberFormatter.formatNoRound(rp.getMontantBrut())},
                                                        buffer, new FieldPosition(0)).toString());

                                        lignes.add(champs);
                                    }
                                }
                            }
                        }
                    }
                    //</editor-fold>
                }

                // Création champs de données document assurées pour le types MATCIAB1
                boolean textaddedMATCIAB1 = false;
                boolean textaddedMATCIAB2 = false;
                for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {

                    final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);

                    //<editor-fold defaultstate="collapsed" desc="MATCIAB1">
                    if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre()) || APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) {
                        if (isGenreSameAsStateDecision(prestation)) {

                            nbPrest++;

                            // si idAffilie de la répartition = 0 && que c'est
                            // l'assuré (par idTiers)
                            repartitionPaiementsManager.setForIdPrestation(prestation.getIdPrestationApg());
                            repartitionPaiementsManager.find();

                            for (int idRP = 0; idRP < repartitionPaiementsManager.size(); ++idRP) {
                                final APRepartitionPaiements rp = (APRepartitionPaiements) repartitionPaiementsManager
                                        .get(idRP);

                                // reprendre la situation prof pour voir si indépendant
                                final APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
                                sitPro.setSession(getSession());
                                sitPro.setIdSituationProf(rp.getIdSituationProfessionnelle());
                                sitPro.retrieve();

                                if (sitPro.getIsIndependant().booleanValue()) {
                                    isIndependant = true;
                                } else {
                                    isIndependant = false;
                                }

                                final StringBuffer buffer = new StringBuffer();
                                FWMessageFormat message = createMessageFormat(buffer);

                                if (state == APDecisionCommunicationAMAT.STATE_ASSURES) {
                                    if (JadeStringUtil.isIntegerEmpty(rp.getIdAffilie())
                                            && rp.getIdTiers().equals(getIdTiersAssure())
                                            && !JadeStringUtil.isIntegerEmpty(rp.getMontantBrut())) {

                                        final Map champs = new HashMap();
                                        final double nbJours = Double.parseDouble(prestation.getNombreJoursSoldes());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(6));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_DEBUT2",
                                                message.format(new Object[]{prestation.getDateDebut()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(7));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_FIN2",
                                                message.format(new Object[]{prestation.getDateFin()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(8));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_NB_JOURS2",
                                                message.format(new Object[]{prestation.getNombreJoursSoldes()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(9));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_JOURNALIER2",
                                                message.format(
                                                        new Object[]{String.valueOf(JANumberFormatter.format(
                                                                Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2,
                                                                JANumberFormatter.NEAR))}, buffer, new FieldPosition(0))
                                                        .toString());

                                        buffer.setLength(0);
                                        buffer.append(documentAssures.getTextes(3).getTexte(10));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_BRUT2",
                                                message.format(
                                                        new Object[]{JANumberFormatter.formatNoRound(rp.getMontantBrut())},
                                                        buffer, new FieldPosition(0)).toString());

                                        if (APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) { // tableau MATCIAB2 assuré un seul employeurs
                                            if (!textaddedMATCIAB2) {
                                                buffer.setLength(0);
                                                buffer.append(PRStringUtils.replaceString(documentEmployeurs.getTextes(1).getTexte(200).getDescription(), "{0}",
                                                        String.valueOf(revenuMoyenDeterminant)));
                                                message = createMessageFormat(buffer);
                                                buffer.setLength(0);
                                                champs.put(
                                                        "CHAMP_TEXT1",
                                                        message.format(
                                                                new Object[]{}, buffer, new FieldPosition(0))
                                                                .toString());

                                                textaddedMATCIAB2 = true;
                                            }
                                        }

                                        if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre())) {
                                            if (!textaddedMATCIAB1) { // tableau MATCIAB1 assuré un seul employeurs

                                                if (hasNotOnlyMATCIAB1()) {
                                                    buffer.setLength(0);
                                                    buffer.append("\n");
                                                    buffer.append(PRStringUtils.replaceString(documentAssures.getTextes(1).getTexte(200).getDescription(), "{0}",
                                                            String.valueOf(revenuMoyenDeterminant)));
                                                    buffer.append("\n");
                                                } else {
                                                    buffer.setLength(0);
                                                    buffer.append("\n");
                                                }

                                                buffer.append(PRStringUtils.replaceString(documentAssures.getTextes(1).getTexte(201).getDescription(), "{0}",
                                                        JANumberFormatter.format(Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2, JANumberFormatter.NEAR)));
                                                message = createMessageFormat(buffer);
                                                buffer.setLength(0);
                                                champs.put(
                                                        "CHAMP_TEXT1",
                                                        message.format(
                                                                new Object[]{}, buffer, new FieldPosition(0))
                                                                .toString());
                                                buffer.setLength(0);

                                                textaddedMATCIAB1 = true;
                                            }
                                        }

                                        lignes.add(champs);
                                    }
                                } else {
                                    if (!JadeStringUtil.isIntegerEmpty(rp.getIdAffilie())
                                            && !JadeStringUtil.isIntegerEmpty(rp.getMontantBrut())) {

                                        final Map champs = new HashMap();
                                        final double nbJours = Double.parseDouble(prestation.getNombreJoursSoldes());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(6));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_DEBUT2",
                                                message.format(new Object[]{prestation.getDateDebut()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(7));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_FIN2",
                                                message.format(new Object[]{prestation.getDateFin()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(8));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_NB_JOURS2",
                                                message.format(new Object[]{prestation.getNombreJoursSoldes()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(9));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_JOURNALIER2",
                                                message.format(
                                                        new Object[]{String.valueOf(JANumberFormatter.format(
                                                                Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2,
                                                                JANumberFormatter.NEAR))}, buffer, new FieldPosition(0))
                                                        .toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(10));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_BRUT2",
                                                message.format(
                                                        new Object[]{JANumberFormatter.formatNoRound(rp.getMontantBrut())},
                                                        buffer, new FieldPosition(0)).toString());

                                        if (APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) { // tableau MATCIAB2 indépendant/employeur un seul employeur
                                            if (!textaddedMATCIAB2) {
                                                buffer.setLength(0);
                                                buffer.append(PRStringUtils.replaceString(documentEmployeurs.getTextes(1).getTexte(200).getDescription(), "{0}",
                                                        String.valueOf(revenuMoyenDeterminant)));
                                                message = createMessageFormat(buffer);
                                                buffer.setLength(0);
                                                champs.put(
                                                        "CHAMP_TEXT1",
                                                        message.format(
                                                                new Object[]{}, buffer, new FieldPosition(0))
                                                                .toString());
                                                buffer.setLength(0);

                                                textaddedMATCIAB2 = true;
                                            }
                                        }

                                        if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre())) {
                                            if (!textaddedMATCIAB1) { // tableau MATCIAB1 indépendant/employeur un seul employeur

                                                if (hasNotOnlyMATCIAB1()) {
                                                    buffer.setLength(0);
                                                    buffer.append("\n");
                                                    buffer.append(PRStringUtils.replaceString(documentEmployeurs.getTextes(1).getTexte(200).getDescription(), "{0}",
                                                            String.valueOf(revenuMoyenDeterminant)));
                                                    buffer.append("\n");
                                                } else {
                                                    buffer.setLength(0);
                                                    buffer.append("\n");
                                                }

                                                buffer.append(PRStringUtils.replaceString(documentEmployeurs.getTextes(1).getTexte(201).getDescription(), "{0}",
                                                        JANumberFormatter.format(Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2, JANumberFormatter.NEAR)));
                                                message = createMessageFormat(buffer);
                                                buffer.setLength(0);
                                                champs.put(
                                                        "CHAMP_TEXT1",
                                                        message.format(
                                                                new Object[]{}, buffer, new FieldPosition(0))
                                                                .toString());
                                                buffer.setLength(0);

                                                textaddedMATCIAB1 = true;
                                            }
                                        }

                                        lignes.add(champs);
                                    }
                                }
                            }
                        }
                    }
                    //</editor-fold>
                }
            } else if (STATE_EMPLOYEURS == APDecisionCommunicationAMAT.STATE_EMPLOYEURS) {

                final APRepartitionPaiementsManager repartitionPaiementsManager = new APRepartitionPaiementsManager();
                repartitionPaiementsManager.setSession(getSession());

                int nbPrest = 0;

                // Création champs de données document employeurs pour tous les types sauf MATCIAB1
                for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {
                    final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);
                    final Map champs = new HashMap();
                    final double nbJours = Double.parseDouble(prestation.getNombreJoursSoldes());

                    final StringBuffer buffer = new StringBuffer();
                    FWMessageFormat message = createMessageFormat(buffer);

                    //<editor-fold defaultstate="collapsed" desc="ALLTYPE">
                    if (!APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre()) && !APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) {
                        if (isGenreSameAsStateDecision(prestation)) {

                            nbPrest++;

                            repartitionPaiementsManager.setForIdPrestation(prestation.getIdPrestationApg());
                            repartitionPaiementsManager.find();

                            int count = 0;

                            for (int idRP = 0; idRP < repartitionPaiementsManager.size(); ++idRP) {
                                final APRepartitionPaiements rp = (APRepartitionPaiements) repartitionPaiementsManager
                                        .get(idRP);

                                // reprendre la situation prof pour voir si indépendant
                                final APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
                                sitPro.setSession(getSession());
                                sitPro.setIdSituationProf(rp.getIdSituationProfessionnelle());
                                sitPro.retrieve();

                                if (sitPro.getIsIndependant().booleanValue()) {
                                    isIndependant = true;
                                } else {
                                    isIndependant = false;
                                }

                                if (!JadeStringUtil.isIntegerEmpty(rp.getIdAffilie())
                                        || !rp.getIdTiers().equals(getIdTiersAssure())) {

                                    if (JadeStringUtil.isIntegerEmpty(rp.getIdParent())
                                            && rp.getIdAffilie().equals(repartition.getIdAffilie())
                                            && rp.getIdTiers().equals(repartition.getIdTiers())
                                            && rp.getNom().equals(repartition.getNom())
                                            && !JadeStringUtil.isIntegerEmpty(rp.getMontantBrut())) {

                                        count++;

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(6));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_DEBUT",
                                                message.format(new Object[]{prestation.getDateDebut()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(7));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_FIN",
                                                message.format(new Object[]{prestation.getDateFin()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(8));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_NB_JOURS",
                                                message.format(new Object[]{prestation.getNombreJoursSoldes()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(9));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_JOURNALIER",
                                                message.format(
                                                        new Object[]{String.valueOf(JANumberFormatter.format(
                                                                Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2,
                                                                JANumberFormatter.NEAR))}, buffer, new FieldPosition(0))
                                                        .toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(10));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_BRUT",
                                                message.format(
                                                        new Object[]{JANumberFormatter.formatNoRound(rp.getMontantBrut())},
                                                        buffer, new FieldPosition(0)).toString());

                                        break;
                                    }
                                }
                            }
                            if (count > 0) {
                                lignes.add(champs);
                            }
                        }
                    }
                    //</editor-fold>
                }

                // Création champs de données document employeurs pour le type MATCIAB1
                boolean textaddedMATCIAB1 = false;
                boolean textaddedMATCIAB2 = false;
                for (int idPrestation = 0; idPrestation < loadPrestations().size(); ++idPrestation) {
                    final APPrestation prestation = (APPrestation) loadPrestations().get(idPrestation);
                    final Map champs = new HashMap();
                    final double nbJours = Double.parseDouble(prestation.getNombreJoursSoldes());

                    final StringBuffer buffer = new StringBuffer();
                    FWMessageFormat message = createMessageFormat(buffer);

                    //<editor-fold defaultstate="collapsed" desc="MATCIAB1">
                    if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre()) || APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) {
                        if (isGenreSameAsStateDecision(prestation)) {

                            nbPrest++;

                            repartitionPaiementsManager.setForIdPrestation(prestation.getIdPrestationApg());
                            repartitionPaiementsManager.find();

                            int count = 0;

                            for (int idRP = 0; idRP < repartitionPaiementsManager.size(); ++idRP) {
                                final APRepartitionPaiements rp = (APRepartitionPaiements) repartitionPaiementsManager
                                        .get(idRP);

                                // reprendre la situation prof pour voir si indépendant
                                final APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
                                sitPro.setSession(getSession());
                                sitPro.setIdSituationProf(rp.getIdSituationProfessionnelle());
                                sitPro.retrieve();

                                if (sitPro.getIsIndependant().booleanValue()) {
                                    isIndependant = true;
                                } else {
                                    isIndependant = false;
                                }

                                if (!JadeStringUtil.isIntegerEmpty(rp.getIdAffilie())
                                        || !rp.getIdTiers().equals(getIdTiersAssure())) {

                                    if (JadeStringUtil.isIntegerEmpty(rp.getIdParent())
                                            && rp.getIdAffilie().equals(repartition.getIdAffilie())
                                            && rp.getIdTiers().equals(repartition.getIdTiers())
                                            && rp.getNom().equals(repartition.getNom())
                                            && !JadeStringUtil.isIntegerEmpty(rp.getMontantBrut())) {

                                        count++;

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(6));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_DEBUT2",
                                                message.format(new Object[]{prestation.getDateDebut()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(7));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_DATE_FIN2",
                                                message.format(new Object[]{prestation.getDateFin()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(8));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_NB_JOURS2",
                                                message.format(new Object[]{prestation.getNombreJoursSoldes()}, buffer,
                                                        new FieldPosition(0)).toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(9));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_JOURNALIER2",
                                                message.format(
                                                        new Object[]{String.valueOf(JANumberFormatter.format(
                                                                Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2,
                                                                JANumberFormatter.NEAR))}, buffer, new FieldPosition(0))
                                                        .toString());

                                        buffer.setLength(0);
                                        buffer.append(documentEmployeurs.getTextes(3).getTexte(10));
                                        message = createMessageFormat(buffer);
                                        buffer.setLength(0);
                                        champs.put(
                                                "CHAMP_MONTANT_BRUT2",
                                                message.format(
                                                        new Object[]{JANumberFormatter.formatNoRound(rp.getMontantBrut())},
                                                        buffer, new FieldPosition(0)).toString());

                                        if (APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) { // tableau MATCIAB2 indépendant/employeur plusieurs employeurs
                                            if (!textaddedMATCIAB2) {
                                                buffer.setLength(0);
                                                buffer.append(PRStringUtils.replaceString(documentEmployeurs.getTextes(1).getTexte(200).getDescription(), "{0}",
                                                        String.valueOf(JANumberFormatter.format(
                                                                Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2,
                                                                JANumberFormatter.NEAR))));
                                                message = createMessageFormat(buffer);
                                                buffer.setLength(0);
                                                champs.put(
                                                        "CHAMP_TEXT1",
                                                        message.format(
                                                                new Object[]{}, buffer, new FieldPosition(0))
                                                                .toString());
                                                buffer.setLength(0);

                                                textaddedMATCIAB2 = true;
                                            }
                                        }

                                        if (APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre())) {
                                            if (!textaddedMATCIAB1) { // tableau MATCIAB1 indépendant/employeur plusieurs employeurs

                                                // calculer le pourcentage de l'employeur
                                                final double pourcent = Double.parseDouble(rp.getTauxRJM()) / 100d;

                                                double revenuMoyenDeterminantMATCIABLimite = Double.parseDouble(loadPrestationType().getRevenuMoyenDeterminant()) * pourcent;
                                                if (revenuMoyenDeterminantMATCIABLimite >= revenuMoyenDeterminantMAX) {
                                                    revenuMoyenDeterminantMATCIABLimite = revenuMoyenDeterminantMAX;
                                                }

                                                if (hasNotOnlyMATCIAB1()) {
                                                    buffer.setLength(0);
                                                    buffer.append("\n");
                                                    buffer.append(PRStringUtils.replaceString(documentEmployeurs.getTextes(1).getTexte(200).getDescription(), "{0}",
                                                            JANumberFormatter.format(revenuMoyenDeterminantMATCIABLimite, 0.05, 2, JANumberFormatter.NEAR)));
                                                    buffer.append("\n");
                                                } else {
                                                    buffer.setLength(0);
                                                    buffer.append("\n");
                                                }

                                                buffer.append(PRStringUtils.replaceString(documentEmployeurs.getTextes(1).getTexte(201).getDescription(), "{0}",
                                                        JANumberFormatter.format(Double.parseDouble(rp.getMontantBrut()) / nbJours, 0.05, 2, JANumberFormatter.NEAR)));
                                                message = createMessageFormat(buffer);
                                                buffer.setLength(0);
                                                champs.put(
                                                        "CHAMP_TEXT1",
                                                        message.format(
                                                                new Object[]{}, buffer, new FieldPosition(0))
                                                                .toString());
                                                buffer.setLength(0);

                                                textaddedMATCIAB1 = true;
                                            }
                                        }

                                        break;
                                    }
                                }
                            }
                            if (count > 0) {
                                lignes.add(champs);
                            }
                        }
                    }
                    //</editor-fold>
                }
            }

            if (nbAdresseDansRepartition == 0) {
                getMemoryLog().logMessage("Aucune des répartitions pour l'assurée ne contient une adresse de paiement",
                        FWMessage.ERREUR, "");
                abort();

                // Sortir une erreur dans le mail comme quoi la répartition n'a
                // pas d'adresse
            }

            if (lignes.size() == 0) {
                isDecompteRempli = false;

            } else {
                // enregistrer les lignes comme source de données pour la boucle
                // de detail
                isDecompteRempli = true;
                this.setDataSource(lignes);
            }
            // indique si la décision est destinée à l'employeur afin de générer
            // une copie
            // pas de copie pour les independants
            if ((state == APDecisionCommunicationAMAT.STATE_EMPLOYEURS) && isDecompteRempli && !isIndependant) {
                setCreateDocumentCopie(true);
            }
        } catch (final Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "APDecisionCommunicationAMAT");
            abort();
        }
    }

    private boolean isGenreSameAsStateDecision(final APPrestation prestation) {
        boolean isGoodStateAndGenre = false;

        // Genre de prestation STANDARD ET décision à l'état STANDARD
        isGoodStateAndGenre |= APTypeDePrestation.STANDARD.isCodeSystemEqual(prestation.getGenre())
                && state_dec == APDecisionCommunicationAMAT.STATE_STANDARD;

        // OU
        // Genre de prestation (ACM OU ACM2) ET décision à l'état ACM
        isGoodStateAndGenre |= (APTypeDePrestation.ACM_ALFA.isCodeSystemEqual(prestation.getGenre()) || APTypeDePrestation.ACM2_ALFA
                .isCodeSystemEqual(prestation.getGenre())) && state_dec == APDecisionCommunicationAMAT.STATE_ACM;

        // OU
        // Genre de prestation LAMAT ET décision à l'état LAMAT
        isGoodStateAndGenre |= APTypeDePrestation.LAMAT.isCodeSystemEqual(prestation.getGenre())
                && state_dec == APDecisionCommunicationAMAT.STATE_LAMAT;

        // OU
        // Genre de prestation MATCIAB1 ET décision à l'état STANDARD
        isGoodStateAndGenre |= APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre())
                && state_dec == APDecisionCommunicationAMAT.STATE_STANDARD;

        // OU
        // Genre de prestation MATCIAB2 ET décision à l'état MATCIAB2
        isGoodStateAndGenre |= APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())
                && state_dec == APDecisionCommunicationAMAT.STATE_MATCIAB2;

        return isGoodStateAndGenre;
    }

    public void createDocInfo() {

        docInfo = getDocumentInfo();
        docInfo.setPublishDocument(false);

        // on ajoute au doc info le numéro de référence inforom
        docInfo.setDocumentType("globaz.apg.itext.APDecisionCommunicationAMAT");
        docInfo.setDocumentTypeNumber(IPRConstantesExternes.DECISION_MATERNITE);
        docInfo.setDocumentTitle(getSession().getLabel("DOC_DEC_COMM_MAT_TITLE"));
        docInfo.setDocumentDate(getDate());// JJxMMxAAAA

        try {

            // Récupère le service de la ged à utiliser.
            JADate date = new JADate();
            if (JadeStringUtil.isEmpty(getDate())) {
                date = JACalendar.today();
            } else {
                date = new JADate(getDate());
            }

            String annee = "";
            if (!JadeStringUtil.isEmpty(date.toString())) {
                annee = JADate.getYear(date.toString()).toString();
            } else {
                annee = JADate.getYear(date.toString()).toString();
            }

            String yy = String.valueOf(date.getYear());
            yy = yy.substring(2, 4);
            docInfo.setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "A" + yy);

            if (!JadeStringUtil.isEmpty(annee)) {
                docInfo.setDocumentProperty("annee", annee);
            } else {
                docInfo.setDocumentProperty("annee", yy);
            }

            IPRAffilie affilie = null;
            PRTiersWrapper tierWrapper = null;

            if (!JadeStringUtil.isBlankOrZero(docinfo_affilie_tiers_id)) {
                affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(),
                        docinfo_affilie_id, docinfo_affilie_tiers_id);
                tierWrapper = PRTiersHelper.getTiersParId(getSession(), docinfo_affilie_tiers_id);
            } else {
                tierWrapper = droit.loadDemande().loadTiers();
            }

            final IFormatData affilieFormatter = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

            String noAff = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
            String noAffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(noAff);

            if ((affilie != null) && !JadeStringUtil.isBlankOrZero(affilie.getNumAffilie())) {
                noAff = affilie.getNumAffilie();
                noAffNonFormatte = affilieFormatter.unformat(affilie.getNumAffilie());
            }

            // pour un employeur
            if (!JadeStringUtil.isBlankOrZero(docinfo_affilie_tiers_id)) {

                // Dans JadeClientServiceLocator;
                // globaz.apg.itext.APDecomptesAss ou
                // globaz.apg.itext.APDecomptesAff pour respectivement
                // L'assure ou l'affilie.
                // 2 entrées différentes pour différencier le type de document
                // (DTID)
                docInfo.setDocumentType("globaz.apg.itext.APDecisionCommunicationAMAT");

                if (!JadeStringUtil.isIntegerEmpty(docinfo_affilie_id)) {
                    // Employeur affilie --> role AFFILIE
                    if (affilie != null) {
                        TIDocumentInfoHelper.fill(docInfo, affilie.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                                affilie.getNumAffilie(), affilieFormatter.unformat(affilie.getNumAffilie()));
                    } else {
                        TIDocumentInfoHelper.fill(docInfo, docinfo_affilie_tiers_id, getSession(), ITIRole.CS_AFFILIE,
                                noAff, noAffNonFormatte);
                    }

                    final APDroitMaternite droit = new APDroitMaternite();
                    droit.setSession(getSession());
                    droit.setIdDroit(getIdDroit());
                    droit.retrieve();

                    final PRDemande demande = new PRDemande();
                    demande.setSession(getSession());
                    demande.setIdDemande(droit.getIdDemande());
                    demande.retrieve();

                    final PRTiersWrapper assureWrapper = PRTiersHelper
                            .getTiersParId(getSession(), demande.getIdTiers());

                    if ((assureWrapper != null)
                            && !JadeStringUtil.isEmpty(assureWrapper
                            .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                        // On écrase les properties nss... de l'employeur avec
                        // celle de l'assurée
                        docInfo = PRBlankBNumberFormater.fillNss((PRAbstractApplication) getSession().getApplication(),
                                docInfo, assureWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    }

                } else {
                    // Employeur non affilie --> role NON_AFFILIE
                    TIDocumentInfoHelper.fill(docInfo, docinfo_affilie_tiers_id, getSession(),
                            IPRConstantesExternes.OSIRIS_IDEXATION_GED_ROLE_NON_AFFILIE, noAff, noAffNonFormatte);
                }
                docInfo.setDocumentProperty("affilie.tiers.id", docinfo_affilie_tiers_id);
                docInfo.setDocumentProperty("allocataire.tiers.id", getIdTiersAssure());

            }// Fin employeur
            else {
                // Assure --> role APG
                if (affilie != null) {
                    docInfo.setDocumentProperty("affilie.tiers.id", affilie.getIdTiers());
                    docInfo.setDocumentType("globaz.apg.itext.APDecisionCommunicationAMAT");
                    noAff = affilie.getNumAffilie();
                    noAffNonFormatte = affilieFormatter.unformat(affilie.getNumAffilie());

                    if (JadeStringUtil.isBlank(noAff)) {
                        noAff = PRBlankBNumberFormater.getEmptyNoAffilieFormatte();
                        noAffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(noAff);
                    }

                    // TODO a faire par AFDocumentInfoHelper
                    docInfo.setDocumentProperty("numero.affilie.formatte", noAff);
                } else {
                    docInfo.setDocumentType("globaz.apg.itext.APDecisionCommunicationAMAT");
                }

                TIDocumentInfoHelper.fill(docInfo, droit.loadDemande().getIdTiers(), getSession(), IntRole.ROLE_APG,
                        noAff, noAffNonFormatte);
                docInfo.setDocumentProperty("allocataire.tiers.id", getIdTiersAssure());
            }

            if (!isDocumentCopy() && getIsSendToGed().booleanValue()) {
                docInfo.setArchiveDocument(true);

                // création / mise à jour du dossier GroupDoc
                GroupdocPropagateUtil.propagateData(affilie, tierWrapper, droit.getDateReception());

            } else {// Fin test ged
                docInfo.setArchiveDocument(false);
            }

            // Si no avs est vide, le remplacer par des '0'
            final String avsnf = docInfo.getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE);
            final String avsf = docInfo.getDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE);

            if (JadeStringUtil.isBlankOrZero(avsnf) || JadeStringUtil.isBlankOrZero(avsf)) {
                docInfo = PRBlankBNumberFormater.fillEmptyNss(getSession().getApplication(), docInfo);
            }

            if (!JadeStringUtil.isBlankOrZero(docinfo_affilie_tiers_id)) {
                // on ajoute au doc info le critere de tri pour les impressions
                // ORDER_PRINTING_BY
                docInfo.setDocumentProperty(APDecisionCommunicationAMAT.ORDER_PRINTING_BY,
                        buildOrderPrintingByKey(docinfo_affilie_id, docinfo_affilie_tiers_id));
            }

            TIDocumentInfoHelper.fill(docInfo, getIdTiersAssure(), getSession(), IntRole.ROLE_APG, noAff,
                    noAffNonFormatte);

        } catch (final Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("APDecisionCommunicationAMAT.afterPrintDocument():" + e.toString(),
                    FWMessage.ERREUR, "APDecisionCommunicationAMAT");
        }

    }

    private FWMessageFormat createMessageFormat(final StringBuffer pattern) {
        // doubler les apostrophes pour eviter que MessageFormat se trompe
        for (int idChar = pattern.length(); --idChar >= 0; ) {
            if (pattern.charAt(idChar) == '\'') {
                pattern.insert(idChar, '\'');
            }
        }

        // créer un formatteur pour la langue de la session
        final FWMessageFormat retValue = new FWMessageFormat(pattern.toString());

        if (locale == null) {
            locale = new Locale(getSession().getIdLangueISO(), "CH");
        }

        retValue.setLocale(locale);

        return retValue;
    }

    /**
     * getter pour l'attribut cs type documentAssures.
     *
     * @return la valeur courante de l'attribut cs type documentAssures
     */
    public String getCsTypeDocument() {
        return csTypeDocument;
    }

    /**
     * getter pour l'attribut date.
     *
     * @return la valeur courante de l'attribut date
     */
    public String getDate() {
        return date;
    }

    /**
     * getter pour l'attribut id droit.
     *
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    public String getIdTiersAssure() {
        return idTiersAssure;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * Retourne si le document doit avoir une copie
     *
     * @return
     */
    public boolean isCreateDocumentCopie() {
        return createDocumentCopie;
    }

     /**
     * Retourne si le document doit avoir une copie au fisc
     *
     * @return
     */
    public boolean isCreateDocumentCopieFisc() {
        return createDocumentCopieFisc;
    }


    /**
     * Permet de savoir si document de copie
     *
     * @return
     */
    public boolean isDocumentCopy() {
        return documentCopy;
    }

    private boolean isEmployeursMultiples() throws FWIException {
        if (employeursMultiples == null) {
            final APSituationProfessionnelleManager sitPros = new APSituationProfessionnelleManager();

            sitPros.setSession(getSession());
            sitPros.setForIdDroit(idDroit);

            try {
                if (sitPros.getCount() > 1) {
                    employeursMultiples = Boolean.TRUE;
                } else {
                    employeursMultiples = Boolean.FALSE;
                }
            } catch (final Exception e) {
                throw new FWIException("Impossible de charger les situations professionnelles");
            }
        }

        return employeursMultiples.booleanValue();
    }

    /**
     *
     */
    private boolean isSituationProfessionnelleSpeciale() throws Exception {

        final APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setIdSituationProf(repartition.getIdSituationProfessionnelle());
        situationProfessionnelle.setSession(getSession());
        situationProfessionnelle.retrieve();

        if (situationProfessionnelle.getIsVersementEmployeur().booleanValue()) {
            if (!JadeStringUtil.isDecimalEmpty(situationProfessionnelle.getPourcentMontantVerse())) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * READ_SHORT.
     *
     * @return READ_SHORT
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    private APPrestationManager loadPrestations() throws FWIException {
        if (!prestations.isLoaded()) {
            prestations.setSession(getSession());
            prestations.setForIdDroit(getIdDroit());
            // Permet de filter les MATCIAB2 des décisions pour les faire ressortir sur un document séparé
            if (this.isMatciab2Decision) {
                prestations.setForGenre(APTypeDePrestation.MATCIAB2.getCodesystemString());
            } else {
                prestations.setNotForGenre(APTypeDePrestation.MATCIAB2.getCodesystemString());
            }
            prestations.setOrderBy(APPrestation.FIELDNAME_DATEDEBUT);

            try {
                prestations.find();
            } catch (final Exception e) {
                throw new FWIException("Impossible charger les prestations", e);
            }
        }

        return prestations;
    }

    // Retourne la première prestation n'etant pas une prestation de
    // restitution.
    private APPrestation loadPrestationType() throws FWIException {
        if ((prestationType == null) && !loadPrestations().isEmpty()) {

            try {
                for (int i = 0; i < loadPrestations().getCount(); i++) {
                    prestationType = (APPrestation) loadPrestations().get(i);
                    if (IAPPrestation.CS_TYPE_ANNULATION.equals(prestationType.getType())) {
                        continue;
                    } else {
                        // les prestations standard on la priorité
                        if (APTypeDePrestation.STANDARD.isCodeSystemEqual(prestationType.getGenre())) {
                            break;
                        } else {
                            continue;
                        }
                    }
                }
            } catch (final FWIException e) {
                throw e;
            } catch (final Exception e) {
                throw new FWIException(e.getMessage());
            }
        }

        return prestationType;
    }

    // Retourne la première prestation MATCIAB1 n'etant pas une prestation de
    // restitution.
    private APPrestation loadPrestationTypeMATCIAB1() throws FWIException {
        if (!loadPrestations().isEmpty()) {

            try {
                for (int i = 0; i < loadPrestations().getCount(); i++) {
                    APPrestation prestation = (APPrestation) loadPrestations().get(i);
                    if (!IAPPrestation.CS_TYPE_ANNULATION.equals(prestation.getType()) && APTypeDePrestation.MATCIAB1.isCodeSystemEqual(prestation.getGenre())) {
                        return prestation;
                    }
                }
            } catch (final FWIException e) {
                throw e;
            } catch (final Exception e) {
                throw new FWIException(e.getMessage());
            }
        }

        return null;
    }

    // Retourne la première prestation MATCIAB2 n'etant pas une prestation de
    // restitution.
    private APPrestation loadPrestationTypeMATCIAB2() throws FWIException {
        if (!loadPrestations().isEmpty()) {

            try {
                for (int i = 0; i < loadPrestations().getCount(); i++) {
                    APPrestation prestation = (APPrestation) loadPrestations().get(i);
                    if (!IAPPrestation.CS_TYPE_ANNULATION.equals(prestation.getType()) && APTypeDePrestation.MATCIAB2.isCodeSystemEqual(prestation.getGenre())) {
                        return prestation;
                    }
                }
            } catch (final FWIException e) {
                throw e;
            } catch (final Exception e) {
                throw new FWIException(e.getMessage());
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws FWIException DOCUMENT ME!
     */
    @Override
    public boolean next() throws FWIException {

        boolean suite = false;

        switch (state_dec) {

            case STATE_STANDARD:
                if (state != APDecisionCommunicationAMAT.STATE_FIN) {
                    suite = true;
                } else {
                    state = APDecisionCommunicationAMAT.STATE_ASSURES;
                    state_dec = APDecisionCommunicationAMAT.STATE_MATCIAB2;
                    suite = true;
                }
                break;

            case STATE_MATCIAB2:
                if (state != APDecisionCommunicationAMAT.STATE_FIN) {
                    suite = true;
                } else {
                    state = APDecisionCommunicationAMAT.STATE_ASSURES;
                    state_dec = APDecisionCommunicationAMAT.STATE_ACM;
                    suite = true;
                }
                break;

            case STATE_ACM:
                if (state != APDecisionCommunicationAMAT.STATE_FIN) {
                    suite = true;
                } else {
                    state = APDecisionCommunicationAMAT.STATE_ASSURES;
                    state_dec = APDecisionCommunicationAMAT.STATE_LAMAT;
                    suite = true;
                }
                break;

            case STATE_LAMAT:
                if (state != APDecisionCommunicationAMAT.STATE_FIN) {
                    suite = true;
                } else {
                    state = APDecisionCommunicationAMAT.STATE_ASSURES;
                    state_dec = APDecisionCommunicationAMAT.STATE_FINDEC;
                    suite = false;
                }
                break;

            case STATE_FINDEC:
                state = APDecisionCommunicationAMAT.STATE_FIN;
                suite = false;
                break;

            default:
                throw new FWIException("erreur dans le cycle de vie de la génération des décomptes");
        }
        if (suite) {
            setImpressionParLot(true);
        }
        return suite;
    }

    /**
     * Indique si création de document de copie
     *
     * @param newCreateDocumentCopie
     */
    public void setCreateDocumentCopie(final boolean newCreateDocumentCopie) {
        createDocumentCopie = newCreateDocumentCopie;
    }

     /**
     * Indique si création de document de copie au fisc
     *
     * @param newCreateDocumentCopieFisc
     */
    public void setCreateDocumentCopieFisc(final boolean newCreateDocumentCopieFisc) {
        createDocumentCopieFisc = newCreateDocumentCopieFisc;
    }

    /**
     * setter pour l'attribut cs type documentAssures.
     *
     * @param csTypeDocument une nouvelle valeur pour cet attribut
     */
    public void setCsTypeDocument(final String csTypeDocument) {
        this.csTypeDocument = csTypeDocument;
    }

    /**
     * setter pour l'attribut date.
     *
     * @param date une nouvelle valeur pour cet attribut
     */
    public void setDate(final String date) {
        this.date = date;
    }

    /**
     * Modification de l'attribut afin de déterminé s'il s'agit d'un document de copie
     *
     * @param newDocumentCopy
     */
    public void setDocumentCopy(final boolean newDocumentCopy) {
        documentCopy = newDocumentCopy;
    }

    /**
     * setter pour l'attribut id droit.
     *
     * @param idDroit une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(final String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdTiersAssure(final String idTiersAssure) {
        this.idTiersAssure = idTiersAssure;
    }

    public void setIsSendToGed(final Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    private PRTiersWrapper tiers() throws FWIException {
        try {
            final PRDemande demande = droit.loadDemande();
            return PRTiersHelper.getTiersParId(getSession(), demande.getIdTiers());
        } catch (final Exception e) {
            throw new FWIException("TIERS_INTROUVABLE", e);
        }
    }

}
