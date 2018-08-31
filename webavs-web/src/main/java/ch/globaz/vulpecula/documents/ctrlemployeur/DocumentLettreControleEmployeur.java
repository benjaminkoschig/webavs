package ch.globaz.vulpecula.documents.ctrlemployeur;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.ctrlemployeur.LettreControle;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import globaz.caisse.report.helper.CaisseHeaderReportBean;

public class DocumentLettreControleEmployeur extends VulpeculaDocumentManager<LettreControle> {
    private static final long serialVersionUID = 1L;

    private static final int MAX_YEAR_IN_TITLE = 4;
    private static final String TITLE_YEARS_SEPARATOR = "-";
    private static final String CT_TAG_YEARS_IN_TITLE = "annees";
    private static final String CT_TAG_DATE_CONTROLE = "date";
    private static final String CT_TAG_TIME_CONTROLE = "time";
    private static final String CT_TAG_PERIODE_DEBUT_CONTROLE = "dateDebut";
    private static final String CT_TAG_PERIODE_FIN_CONTROLE = "dateFin";

    private static final String P_CONCERNE = "P_CONCERNE";
    private static final String P_TITRE = "P_TITRE";
    private static final String P_P1 = "P_P1";
    private static final String P_DATE = "P_DATE";
    private static final String P_P2 = "P_P2";
    private static final String P_P3 = "P_P3";
    private static final String P_PUCES = "P_PUCES";
    private static final String P_P4 = "P_P4";
    private static final String P_P5 = "P_P5";
    private static final String P_SALUTATIONS = "P_SALUTATIONS";
    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static boolean isavs;

    public DocumentLettreControleEmployeur() throws Exception {
        this(null, true);
    }

    public DocumentLettreControleEmployeur(LettreControle lettreControle, boolean isAVS) throws Exception {
        super(lettreControle,
                (isAVS(isAVS) ? DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_AVS_CT_NAME
                        : DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_CT_NAME),
                (isAVS ? DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_AVS_TYPE_NUMBER
                        : DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_TYPE_NUMBER));
    }

    /**
     * set la variable pour que lorsque le super constructeur de DocumentManager appel
     * getNomDocumentForCataloguesTextes, notre classe puisse determiner quel nom de catalogue est à utiliser
     *
     * @param isAVS
     * @return
     */
    private static boolean isAVS(boolean isAVS) {
        isavs = isAVS;
        return isavs;
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return (isavs ? DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_AVS_CT_NAME
                : DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_CT_NAME);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_TEMPLATE;
    }

    @Override
    public void fillFields() throws Exception {
        setDocumentTitle(getCurrentElement().getEmployeur().getAffilieNumero());
        setParametres(P_CONCERNE, getConcerne());
        setParametres(P_TITRE, getTexte(2, 1));
        setParametres(P_P1, getTexte(3, 1));
        setParametres(P_DATE, getDate());
        setParametres(P_P2, getParagraphe2());
        setParametres(P_P3, getTexte(6, 1));
        setParametres(P_PUCES, getTexte(7, 1));
        setParametres(P_P4, getTexte(8, 1));
        setParametres(P_P5, getTexte(9, 1));
        setParametres(P_SALUTATIONS, getTexte(10, 1));
        setParametres(P_SIGNATURE, getTexte(11, 1));
    }

    private String getConcerne() throws Exception {
        LettreControle lettreControle = getCurrentElement();
        Set<String> annees = new TreeSet<String>();
        // BMS-2283 Lettre de convocation: Mettre toute les années lorsque la période est > 4 ans
        // if ((lettreControle.getAnneeFin() - lettreControle.getAnneeDebut()) > (MAX_YEAR_IN_TITLE - 1)) {
        // annees.add(String.valueOf(lettreControle.getAnneeDebut()));
        // annees.add(String.valueOf(lettreControle.getAnneeFin()));
        // } else {
        for (int i = lettreControle.getAnneeDebut(); i <= lettreControle.getAnneeFin(); i++) {
            annees.add(String.valueOf(i));
        }
        // }
        Map<String, String> parametres = new HashMap<String, String>();
        String strAnnee = Arrays.toString(annees.toArray());
        // substring to remove [] of the Arrays.toString
        parametres.put(CT_TAG_YEARS_IN_TITLE,
                strAnnee.substring(1, (strAnnee.length() - 1)).replaceAll(",", " " + TITLE_YEARS_SEPARATOR));
        return getTexteFormatte(1, 1, parametres);
    }

    private String getDate() throws Exception {
        LettreControle lettreControle = getCurrentElement();
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(CT_TAG_DATE_CONTROLE,
                lettreControle.getDate().getFullWithWeekDayValue(new Locale(getCodeLangue().getCodeIsoLangue())));
        parametres.put(CT_TAG_TIME_CONTROLE, lettreControle.getHeure());
        return getTexteFormatte(4, 1, parametres);
    }

    private String getParagraphe2() throws Exception {
        LettreControle lettreControle = getCurrentElement();
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(CT_TAG_PERIODE_DEBUT_CONTROLE, "01.01." + String.valueOf(lettreControle.getAnneeDebut()));
        parametres.put(CT_TAG_PERIODE_FIN_CONTROLE, "31.12." + String.valueOf(lettreControle.getAnneeFin()));
        return getTexteFormatte(5, 1, parametres);
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        LettreControle lettreControle = getCurrentElement();

        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        beanReport.setAdresse(lettreControle.getEmployeur().getAdressePrincipale().getAdresseFormatte());
        beanReport.setNomCollaborateur(lettreControle.getReviseur());
        beanReport.setUser(getSession().getUserInfo());
        beanReport.setDate(lettreControle.getDateReference().getSwissValue());
        beanReport.setNoAffilie(lettreControle.getEmployeur().getAffilieNumero());
        beanReport.setConfidentiel(true);
        return beanReport;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return CodeLangue.fromValue(getCurrentElement().getEmployeur().getLangue());
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {

        return DocumentType.LETTRE_CONTROLE_EMPLOYEUR.getCsCode();
    }

}
