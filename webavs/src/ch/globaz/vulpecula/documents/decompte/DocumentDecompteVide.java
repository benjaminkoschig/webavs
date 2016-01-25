package ch.globaz.vulpecula.documents.decompte;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BApplication;
import globaz.globall.util.JANumberFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.models.decompte.TableauContributions;
import ch.globaz.vulpecula.models.decompte.TableauContributions.EntreeContribution;
import ch.globaz.vulpecula.models.decompte.TableauContributions.TypeContribution;
import ch.globaz.vulpecula.util.I18NUtil;
import com.sun.star.lang.NullPointerException;

/**
 * Classe parente des documents de décomptes vides
 * 
 * @since WebBMS 1.0
 */
public abstract class DocumentDecompteVide extends VulpeculaDocumentManager<DecompteContainer> {
    private static final long serialVersionUID = 333224584795958537L;

    private static final String CHF = "CHF";
    private static final String TRANCHE_AC2_MAX = "99'999.00";
    private static final String TRANCHE_AC2_MIN = CHF + " 10'501.00";
    private static final String TRANCHE_AC_MAX = "10'500.00";
    private static final String TRANCHE_AC_MIN = CHF + " 0.00";

    private static final String LABEL_DOCUMENT_MONTANT_PAYABLE = "DOCUMENT_MONTANT_PAYABLE";
    private static final String LABEL_DOCUMENT_TOTAL_DES_CONTRIBUTIONS = "DOCUMENT_TOTAL_DES_CONTRIBUTIONS";
    private static final String LABEL_DOCUMENT_TIMBRE_ET_SIGNATURE = "DOCUMENT_TIMBRE_ET_SIGNATURE";
    private static final String LABEL_DOCUMENT_DECOMPTE_CERTIFIE_EXACT = "DOCUMENT_DECOMPTE_CERTIFIE_EXACT";
    private static final String F_GROUPE_LABEL = "F_GROUPE_LABEL";

    private static final int STATE_IDLE = 0;
    private static final int STATE_SALAIRES = 1;
    private static final int STATE_CONTRIB = 2;

    private int state = DocumentDecompteVide.STATE_IDLE;
    private Decompte decompte;

    public DocumentDecompteVide() throws Exception {
        super();
    }

    /**
     * Constructeur, on l'on passe les décomptes que l'on désire imprimer
     * 
     * @param decomptes List<Decompte>
     * @throws Exception
     */
    public DocumentDecompteVide(final DecompteContainer element, final String documentName, final String numeroInforom)
            throws Exception {
        super(element, documentName, numeroInforom);
        setHeaderName(DocumentConstants.HEADER_LANDSCAPE_BMS);
        decompte = element.getDecompte();
    }

    /**
     * On surcharge la méthode next() afin de gérer le document des contrib
     */
    @Override
    public boolean next() throws FWIException {
        switch (state) {
            case STATE_IDLE:
                // Vérifie s'il y a un document suivant
                if (super.next()) {
                    // La lettre n'est pas créée pour les décomptes ayant la coche BVR
                    if (decompte.isReceptionnable() && decompte.isBVR()) {
                        return next();
                    }

                    state = DocumentDecompteVide.STATE_SALAIRES;
                    // on va créer la lettre
                    return true;
                } else {
                    // il n'y a plus de documents à créer
                    return false;
                }
            case STATE_SALAIRES:
                state = DocumentDecompteVide.STATE_CONTRIB;
                return true;
            default:
                // on regarder s'il y a encore des documents à traiter.
                state = DocumentDecompteVide.STATE_IDLE;
                // Passe au document suivant
                return next();
        }
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();

        Decompte decompte = getCurrentElement().getDecompte();

        beanReport.setAdresse(decompte.getFullAdressePrincipaleFormatte());
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());

        beanReport.setDate(decompte.getDateEtablissementAsSwissValue());
        beanReport.setNoAffilie(decompte.getEmployeurAffilieNumero());
        beanReport.setConfidentiel(true);

        return beanReport;
    }

    /**
     * Remplissage du document d'après le décompte
     */
    @Override
    public void fillFields() throws Exception {
        if (decompte == null) {
            throw new NullPointerException("Le décompte est null !!");
        }

        BApplication application = getSession().getApplication();
        String langue = getEmployeurLangue(decompte);

        setParametres(DocumentConstants.P_DECOMPTE_DESCRIPTION, getDescriptionDecompte(decompte));
        setParametres(DocumentConstants.P_DECOMPTE_ID,
                application.getLabel(DocumentConstants.DOCUMENT_ID_DECOMPTE, langue) + " : " + decompte.getId());
        fillDocumentTitle();

        if (state == STATE_SALAIRES) {
            setParametres(DocumentConstants.P_TOTAL_SALAIRE,
                    application.getLabel(DocumentConstants.DOCUMENT_TOTAL_DES_SALAIRES, langue));
            fillDataSourceSalaire();
        } else {
            setParametres(DocumentConstants.P_CERTIFIE_EXACT,
                    application.getLabel(DocumentDecompteVide.LABEL_DOCUMENT_DECOMPTE_CERTIFIE_EXACT, langue));
            setParametres(DocumentConstants.P_TIMBRE_SIGNATURE,
                    application.getLabel(DocumentDecompteVide.LABEL_DOCUMENT_TIMBRE_ET_SIGNATURE, langue));
            setParametres(DocumentConstants.P_LIEU_DATE, application.getLabel("DOCUMENT_LIEU_ET_DATE", langue));
            setParametres(DocumentConstants.P_TOTAL_CONTRI,
                    application.getLabel(DocumentDecompteVide.LABEL_DOCUMENT_TOTAL_DES_CONTRIBUTIONS, langue));
            setParametres(DocumentConstants.P_MONTANT_PAYABLE,
                    application.getLabel(DocumentDecompteVide.LABEL_DOCUMENT_MONTANT_PAYABLE, langue));
            setParametres(DocumentConstants.P_MONTANT_TOTAL, getMontantTotal());

            fillDataSourceContrib(application, langue);
        }
    }

    private String getMontantTotal() throws Exception {
        return formatMontant(decompte.getMontantContributionTotal());
    }

    /**
     * @return
     */
    private String formatMontant(Montant montant) {
        String sTotal = "";
        if (!montant.isZero()) {
            sTotal = JANumberFormatter.format(montant.getValue(), 0.01, 2, JANumberFormatter.NEAR);
        }

        return sTotal;
    }

    private String formatTaux(Taux taux) {
        return JANumberFormatter.format(taux.getValue(), 0.01, 2, JANumberFormatter.NEAR);
    }

    /**
     * 
     */
    protected abstract void fillDataSourceSalaire();

    /**
     * Document commun à tous le décompte vide
     * 
     * @param application
     * @param langue
     */
    protected void fillDataSourceContrib(BApplication application, String langue) {
        setTemplateFile(DocumentConstants.DECOMPTE_VIDE_CONTRIB_TEMPLATE);
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
                    collection.add(buildLine(new EntreeContribution(Taux.ZERO(), Montant.ZERO)));
                } else {
                    for (EntreeContribution entree : entry.getValue()) {
                        collection.add(buildLine(entree));
                    }
                }
            }

            if (entry.getKey().isAc()) {
                collection.add(buildTitleLine(application.getLabel(DocumentConstants.LABEL_DOCUMENT_AC, langue)));
                if (entry.getValue().isEmpty()) {
                    collection.add(buildLine(new EntreeContribution(Taux.ZERO(), Montant.ZERO),
                            DocumentDecompteVide.TRANCHE_AC_MIN, DocumentDecompteVide.TRANCHE_AC_MAX));
                } else {
                    for (EntreeContribution entree : entry.getValue()) {
                        collection.add(buildLine(entree, DocumentDecompteVide.TRANCHE_AC_MIN,
                                DocumentDecompteVide.TRANCHE_AC_MAX));
                    }
                }
            }

            if (entry.getKey().isAc2() && !entry.getValue().isEmpty()) {
                collection.add(buildTitleLine(application.getLabel(DocumentConstants.LABEL_DOCUMENT_AC2, langue)));
                for (EntreeContribution entree : entry.getValue()) {
                    collection.add(buildLine(entree, DocumentDecompteVide.TRANCHE_AC2_MIN,
                            DocumentDecompteVide.TRANCHE_AC2_MAX));
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

    private Map<String, Object> buildLine(EntreeContribution entree) {
        return this.buildLine(entree, null, null);
    }

    /**
     * @param entree
     * @return
     */
    private Map<String, Object> buildLine(EntreeContribution entree, String col2, String col3) {
        String sTaux = formatTaux(entree.getTaux());
        String sMasse = formatMontant(entree.getMasse());
        String sMontant = formatMontant(entree.getMontant());

        Map<String, Object> ligneCaisseSociale = new HashMap<String, Object>();
        ligneCaisseSociale.put(DocumentConstants.COL_1, sTaux + "%");
        ligneCaisseSociale.put(DocumentConstants.F_MASSE, sMasse);
        ligneCaisseSociale.put(DocumentConstants.F_MONTANT, sMontant);
        try {
            ligneCaisseSociale.put(DocumentConstants.F_SUR, getLabel("DOCUMENT_SUR"));
        } catch (Exception ex) {
            ligneCaisseSociale.put(DocumentConstants.F_SUR, "");
        }

        if (col2 != null) {
            ligneCaisseSociale.put(DocumentConstants.COL_2, col2);
        }
        if (col3 != null) {
            ligneCaisseSociale.put(DocumentConstants.COL_3, col3);
        }

        return ligneCaisseSociale;
    }

    private String getDescriptionDecompte(Decompte decompte) {
        Locale locale = I18NUtil.getLocaleOf(decompte.getEmployeurLangue());
        return decompte.getDescription(locale);
    }

    private void fillDocumentTitle() {
        setDocumentTitle(decompte.getEmployeurAffilieNumero());
    }

    /**
     * @param decompte
     * @return
     */
    protected String getEmployeurLangue(Decompte decompte) {
        return I18NUtil.getLanguesOf(decompte.getEmployeurCSLangue()).getCodeIso();
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {
        // Pas de catalogue de textes pour ce document
        return null;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return getCurrentElement().getDecompte().getEmployeurLangue();
    }

    /**
     * @param linesNumber
     * @return
     */
    public static int linesToAdd(int linesNumber, int NB_LINE_MAX) {
        int nbPage = linesNumber / NB_LINE_MAX;
        int nbLigneLastPage = linesNumber - (NB_LINE_MAX * nbPage);
        int ligneDisponibleRestante = NB_LINE_MAX - nbLigneLastPage;
        int ligneARajouter = ligneDisponibleRestante - 1;

        if (ligneARajouter <= 0) {
            ligneARajouter = NB_LINE_MAX + ligneARajouter;
        }
        return ligneARajouter;
    }
}
