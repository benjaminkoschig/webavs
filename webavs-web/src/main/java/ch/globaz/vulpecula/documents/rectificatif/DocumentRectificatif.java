package ch.globaz.vulpecula.documents.rectificatif;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.CommonProperties;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BApplication;
import globaz.globall.util.JANumberFormatter;
import globaz.pyxis.api.ITIRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.BulletinVersementReference;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.NumeroReferenceFactory;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.NumeroReference;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.external.models.pyxis.Role;
import ch.globaz.vulpecula.models.decompte.TableauContributions;
import ch.globaz.vulpecula.models.decompte.TableauContributions.EntreeContribution;
import ch.globaz.vulpecula.models.decompte.TableauContributions.TypeContribution;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.I18NUtil;

/**
 * @author JPA | Créé le 8 juillet 2014
 * 
 *         Création du document rectificatif
 * 
 */
public class DocumentRectificatif extends VulpeculaDocumentManager<Decompte> {
    private static final long serialVersionUID = 1L;

    // Paramètres et champs pour le récapitulatif
    private static final String P_PERSONNE_REFERENCE = "P_PERSONNE_REFERENCE";
    private static final String P_CONCERNE = "P_CONCERNE";
    private static final String P_TITRE = "P_TITRE";
    private static final String P_P1 = "P_P1";
    private static final String P_P2 = "P_P2";
    private static final String P_TOTAL_CONTRIBUTIONS = "P_TOTAL_CONTRIBUTIONS";

    private static final String P_P3 = "P_P3";
    private static final String P_P4 = "P_P4";
    private static final String P_REMARQUE_RECTIF = "P_REMARQUE_RECTIF";
    private static final String P_SALUTATIONS = "P_SALUTATIONS";

    private static final String F_LETTRE = "F_LETTRE";
    private static final String F_GROUPE_LABEL = "F_GROUPE_LABEL";
    private static final String F_TAUX_CONTRIBUTION = "F_TAUX_CONTRIBUTION";

    private static final String CTPARAMETRE_ANNEE = "annee";
    private static final String CTPARAMETRE_MOIS = "mois";
    private static final String CTPARAMETRE_NO_DECOMPTE = "noDecompte";
    private static final String CTPARAMETRE_TITRE = "titre";
    private static final String CTPARAMETRE_MONTANT_DU = "montantDu";
    private static final String CTPARAMETRE_MONTANT = "montant";

    // Définition des états possible pour l'impression du document
    private static final int STATE_IDLE = 0;
    private static final int STATE_LETTRE = 1;
    private int status = STATE_IDLE;

    /**
     * Constructeur
     */
    public DocumentRectificatif() throws Exception {
        this(null);
    }

    /**
     * Constructeur, on l'on passe les décomptes que l'on désire imprimer
     * 
     * @param decomptes List<Decompte>
     * @throws Exception
     */
    public DocumentRectificatif(final Decompte decompte) throws Exception {
        super(decompte, DocumentConstants.RECTIFICATIF_CT_NAME, DocumentConstants.RECTIFICATIF_TYPE_NUMBER);
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        Decompte decompte = getCurrentElement();

        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                decompte.getEmployeurIdTiers());

        beanReport.setAdresse(adresse.getAdresseFormatte());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());

        beanReport.setDate(Date.now().getSwissValue());
        beanReport.setNoAffilie(decompte.getEmployeurAffilieNumero());
        beanReport.setConfidentiel(true);

        return beanReport;
    }

    private String getConcerne() throws Exception {
        Decompte decompte = getCurrentElement();
        Locale locale = I18NUtil.getLocaleOf(decompte.getEmployeurLangue());

        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(DocumentRectificatif.CTPARAMETRE_NO_DECOMPTE, decompte.getId());
        parametres.put(DocumentRectificatif.CTPARAMETRE_MOIS, decompte.getDescription(locale));
        // Ce paramètre n'est plus utilisé
        parametres.put(DocumentRectificatif.CTPARAMETRE_ANNEE, "");

        return getTexteFormatte(1, 1, parametres);
    }

    private String getTitre() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(getPolitesse());
        sb.append(",");
        return sb.toString();
    }

    /**
     * Retourne la politesse de l'employeur.
     * Si la politesse spécifique est inexistante, c'est le code système par défaut qui est affiché.
     * 
     * @return
     */
    private String getPolitesse() throws Exception {
        Decompte decompte = getCurrentElement();
        return CodeSystemUtil.getFormulePolitesse(getSession(), decompte.getIdTiers());
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        setTemplateFile(getJasperTemplate());
        Decompte decompte = getCurrentElement();
        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), decompte.getEmployeurIdTiers(), getSession(),
                    ITIRole.CS_AFFILIE, decompte.getEmployeurAffilieNumero(), "");
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), "Error", this.getClass().getName());
        }
        getDocumentInfo().setDocumentProperty("idDecompte", decompte.getId());
        getDocumentInfo().setDocumentProperty("decompte.periode",
                decompte.getMoisPeriodeFin() + decompte.getAnneePeriodeFin());

        computeTotalPage();
    }

    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
        getDocumentInfo().setDocumentProperty("document.title", getFileTitle());
    }

    /**
     * Remplissage du document d'après le décompte
     */
    @Override
    public void fillFields() throws Exception {
        imprimerDocument();
    }

    private void imprimerDocument() throws Exception {
        fillDocumentTitle();

        setParametres(P_PERSONNE_REFERENCE,
                VulpeculaServiceLocator.getDecompteService().findTextePersonneReference(getCurrentElement()));
        setParametres(P_CONCERNE, getConcerne());
        setParametres(P_TITRE, getTitre());
        setParametres(P_P1, getParagraphe1());
        setParametres(P_REMARQUE_RECTIF, getCurrentElement().getRemarqueRectification());
        setParametres(P_P2, getParagraphe2());

        fillDataSource();

        setParametres(P_TOTAL_CONTRIBUTIONS, getTexte(4, 1));
        setParametres(DocumentConstants.P_MONTANT_TOTAL, getMontantTotal());
        setParametres(P_P3, getParagraphe3());
        if (isMontantDifferencePositive()) {
            setParametres(P_P4, getTexte(6, 1));
        } else {
            setParametres(P_P4, getTexte(6, 2));
        }
        setParametres(P_SALUTATIONS, getSalutations());


        // Evolution QR-Facture
        if (CommonProperties.QR_FACTURE.getBooleanValue()) {
            // -- QR
            qrFacture = new ReferenceQR();
            qrFacture.setSession(getSession());
            // Initialisation des variables du document
            initVariableQR(getCurrentElement());
            // Génération du document QR
            qrFacture.initQR(this);
        } else {
            fillBVR();
        }
    }

    private String getParagraphe1() throws Exception {
        Decompte decompte = getCurrentElement();

        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(DocumentRectificatif.CTPARAMETRE_ANNEE, decompte.getAnneePeriodeDebut());

        StringBuilder sb = new StringBuilder();
        sb.append(getTexte(2, 1));
        return sb.toString();
    }

    private String getParagraphe2() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(getTexte(3, 1));
        return sb.toString();
    }

    private String getMontantTotal() {
        Decompte decompte = getCurrentElement();

        return JANumberFormatter.format(decompte.getMontantContributionTotal().getValue(), 0.05, 2,
                JANumberFormatter.NEAR);
    }

    private String getParagraphe3() throws Exception {
        Decompte decompte = getCurrentElement();

        Map<String, String> parametres = new HashMap<>();
        parametres.put(DocumentRectificatif.CTPARAMETRE_MONTANT,
                JANumberFormatter.format(decompte.getMontantControle().getValue(), 0.05, 2, JANumberFormatter.NEAR));
        parametres.put(DocumentRectificatif.CTPARAMETRE_MONTANT_DU, JANumberFormatter.format(decompte
                .getMontantDifference().absolute().getValue(), 0.05, 2, JANumberFormatter.NEAR));

        if (isMontantDifferencePositive()) {
            return getTexteFormatte(5, 1, parametres);
        } else {
            return getTexteFormatte(5, 2, parametres);
        }
    }

    private boolean isMontantDifferencePositive() {
        return getCurrentElement().getMontantDifference().isPositive();
    }

    private String getSalutations() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(DocumentRectificatif.CTPARAMETRE_TITRE, getPolitesse());
        return getTexteFormatte(9, 1, parametres);
    }

    private void fillDataSource() throws Exception {
        Decompte decompte = getCurrentElement();
        BApplication application = getSession().getApplication();
        String langue = I18NUtil.getLanguesOf(decompte.getEmployeurCSLangue()).getCodeIso();
        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        // TableCaissesSociales
        TableauContributions tableContribution = new TableauContributions(decompte);
        for (Map.Entry<TypeContribution, Collection<EntreeContribution>> entry : tableContribution.entrySet()) {
            if (entry.getKey().isCaissesSociales()) {
                collection.add(buildTitleLine(application.getLabel(DocumentConstants.LABEL_DOCUMENT_CAISSES_SOCIALES,
                        langue)));

                for (EntreeContribution entree : entry.getValue()) {
                    collection.add(buildLine(entree));
                }
            }

            if (entry.getKey().isAvs()) {
                collection
                        .add(buildTitleLine(application.getLabel(DocumentConstants.LABEL_DOCUMENT_AVS_AI_APG, langue)));

                if (entry.getValue().isEmpty()) {
                    collection.add(buildLine(EntreeContribution.empty()));
                } else {
                    for (EntreeContribution entree : entry.getValue()) {
                        collection.add(buildLine(entree));
                    }
                }
            }

            if (entry.getKey().isAc()) {
                collection.add(buildTitleLine(application.getLabel(DocumentConstants.LABEL_DOCUMENT_AC, langue)));

                if (entry.getValue().isEmpty()) {
                    collection.add(buildLine(EntreeContribution.empty()));
                } else {
                    for (EntreeContribution entree : entry.getValue()) {
                        collection.add(buildLine(entree));
                    }
                }
            }

            if (entry.getKey().isAc2() && !entry.getValue().isEmpty()) {
                collection.add(buildTitleLine(application.getLabel(DocumentConstants.LABEL_DOCUMENT_AC2, langue)));

                for (EntreeContribution entree : entry.getValue()) {
                    collection.add(buildLine(entree));
                }
            }

            if (entry.getKey().isAf() && !entry.getValue().isEmpty()) {
                collection.add(buildTitleLine(application.getLabel(DocumentConstants.LABEL_DOCUMENT_AF, langue)));

                for (EntreeContribution entree : entry.getValue()) {
                    collection.add(buildLine(entree));
                }
            }
        }

        setDataSource(collection);
    }

    /**
     * @param libelle
     * @param lettre
     * @return
     */
    private Map<String, Object> buildTitleLine(String libelle) {
        Map<String, Object> ac = new HashMap<String, Object>();
        ac.put(F_GROUPE_LABEL, libelle);

        return ac;
    }

    /**
     * @param entree
     * @return
     */
    private Map<String, Object> buildLine(EntreeContribution entree) {
        String sTaux = JANumberFormatter.format(entree.getTaux().getValue(), 0.001, 3, JANumberFormatter.NEAR);
        String sMasse = JANumberFormatter.format(entree.getMasse().getValueNormalisee(), 0.05, 2,
                JANumberFormatter.NEAR);
        String sMontant = JANumberFormatter.format(entree.getMontant().getValue(), 0.01, 2, JANumberFormatter.NEAR);

        Map<String, Object> ligneCaisseSociale = new HashMap<String, Object>();
        ligneCaisseSociale.put(F_TAUX_CONTRIBUTION, sTaux + "%");
        ligneCaisseSociale.put(DocumentConstants.F_MASSE, sMasse);
        ligneCaisseSociale.put(DocumentConstants.F_MONTANT, sMontant);
        try {
            ligneCaisseSociale.put(DocumentConstants.F_SUR, getLabel("DOCUMENT_SUR"));
        } catch (Exception ex) {
            ligneCaisseSociale.put(DocumentConstants.F_SUR, "");
        }
        return ligneCaisseSociale;
    }

    /**
     * Rempli le BVR.
     * 
     * @throws Exception
     */
    private void fillBVR() throws Exception {
        Decompte decompte = getCurrentElement();
        Montant montant = decompte.getMontantDifference();

        NumeroReference numRef = NumeroReferenceFactory.createNumeroReference(Role.AFFILIE_PARITAIRE, decompte
                .getEmployeur().getAffilieNumero(), TypeSection.BULLETIN_NEUTRE, decompte.getNumeroDecompte());

        BulletinVersementReference bvr = new BulletinVersementReference(getSession(), numRef, montant);

        try {
            String sAdresse = loadAdresse(decompte);
            super.setParametres(DocumentConstants.P_PAR, sAdresse);
            super.setParametres(DocumentConstants.P_VERSE, bvr.getLigneReference() + "\n" + sAdresse);
        } catch (Exception e1) {
            getMemoryLog().logMessage("Adresse du décompte manquante" + " : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
        try {
            super.setParametres(DocumentConstants.P_ADRESSE, bvr.getAdresseBVR(getCodeLangue()));
            super.setParametres(DocumentConstants.P_ADRESSECOPY, bvr.getAdresseBVR(getCodeLangue()));
            super.setParametres(DocumentConstants.P_COMPTE, bvr.getNumeroCC());
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, bvr.getLigneReference());
            super.setParametres(DocumentConstants.P_OCR, bvr.getOcrb());
        } catch (Exception e1) {
            getMemoryLog().logMessage("Problème BVR" + " : " + e1.getMessage(), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
        }

        if (!montant.isNegative() && !montant.isZero()) {
            super.setParametres(DocumentConstants.P_FRANC, String.valueOf(montant.getMontantSansCentimes()));
            super.setParametres(DocumentConstants.P_CENTIME, montant.getCentimesWith0());
        } else if (montant.isZero()) {
            super.setParametres(DocumentConstants.P_FRANC,
                    String.valueOf(decompte.getMontantContributionTotal().getMontantSansCentimes()));
            super.setParametres(DocumentConstants.P_CENTIME, decompte.getMontantContributionTotal().getCentimesWith0());
        } else {
            super.setParametres(DocumentConstants.P_FRANC, "XXXXXXXXXXXXXXX");
            super.setParametres(DocumentConstants.P_CENTIME, "XX");
        }
    }

    /**
     * @param decompte
     * @return
     */
    private String loadAdresse(Decompte decompte) {
        if (decompte.getEmployeur().getAdressePrincipale() == null) {
            decompte.getEmployeur().setAdressePrincipale(
                    VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                            decompte.getEmployeurIdTiers()));
        }
        return decompte.getAdressePrincipaleFormatte();
    }

    private void fillDocumentTitle() {
        Decompte decompte = getCurrentElement();
        setDocumentTitle(decompte.getEmployeur().getConvention().getCode() + "-" + decompte.getEmployeurAffilieNumero());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.RECTIFICATIF_SUBJECT;
    }

    /**
     * définit la template a utiliser
     */
    @Override
    public String getJasperTemplate() {
        return DocumentConstants.RECTIFICATIF_TEMPLATE;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return getCurrentElement().getEmployeurLangue();
    }

    @Override
    public boolean next() throws FWIException {
        switch (status) {
            case STATE_IDLE:
                status = STATE_LETTRE;
                return super.next();
        }
        return false;
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {
        return DocumentType.RECTIFICATIF.getCsCode();
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.RECTIFICATIF_CT_NAME;
    }
}
