package globaz.osiris.print.itext;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BIDocument;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APIEtape;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAEvenementContentieuxForEtapeManager;
import globaz.osiris.db.contentieux.CATaxe;
import globaz.osiris.formatter.CAAdresseCourrierFormatter;
import globaz.osiris.translation.CACodeSystem;
import java.util.Enumeration;

/**
 * @author sel
 */
public class CATaxation extends CADocumentContentieux {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String MONTANT_ZERO = "0.00";
    private String dateDocument = "";
    private String ligneAdresse1 = "";
    private String ligneAdresse2 = "";
    private String ligneAdresse3 = "";
    private String ligneAdresse4 = "";
    private String ligneAdresse5 = "";
    private String ligneAdresse6 = "";

    private String ligneAdresse7 = "";
    private Boolean modePrevisionnel = new Boolean(false);
    private String taxe = "";

    /**
     * Commentaire relatif au constructeur.
     * 
     * @throws Exception
     */
    public CATaxation() throws Exception {
        super();
    }

    /**
     * @throws Exception
     *             La description de l'exception.
     */
    @Override
    public void afterPrint(BTransaction transaction) throws Exception {
    }

    /**
     * Method batchPrint.
     * 
     * @return String
     * @throws Exception
     *             Exception
     */
    public String batchPrint() throws Exception {
        return null;
    }

    /**
     * @exception Exception
     *                La description de l'exception.
     */
    @Override
    public void beforePrint(BTransaction transaction) throws Exception {
        // Définit le type de document à utiliser.
        setTypeDocument(CACodeSystem.CS_TYPE_TAXATION);
        // Rechercher les données pour afficher sur l'écran
        // Rechercher Adresse
        CAAdresseCourrierFormatter fmt = new CAAdresseCourrierFormatter(getTiers(), getAdresseCourrier());
        // Affiche le tiers avec le prénom puis le nom
        fmt.setNomFirst(false);
        String[] adresseCourrierAffiche = fmt.getAdresseLines(7);

        // Recherche Nom et prénom
        ligneAdresse1 = adresseCourrierAffiche[0];
        ligneAdresse2 = adresseCourrierAffiche[1];
        ligneAdresse3 = adresseCourrierAffiche[2];
        ligneAdresse4 = adresseCourrierAffiche[3];
        ligneAdresse5 = adresseCourrierAffiche[4];
        ligneAdresse6 = adresseCourrierAffiche[5];
        ligneAdresse7 = adresseCourrierAffiche[6];
        // rechercher info section
        FWCurrency cMontant = new FWCurrency(0.00);
        cMontant.add(getSection().getSolde());
        cMontant.add(getTaxe());
        // Recherche les informations pour remplir le bordereau
        dateDocument = getDateDocument();
    }

    @Override
    public void createDataSource() throws Exception {
        // N'est pas utilisé mais doit être présent !
    }

    public String getConcerne() {
        StringBuilder concerne = new StringBuilder("");
        dumpNiveau(1, concerne, "\n");
        return this.formatMessage(concerne, new Object[] { getSection().getIdExterne(),
                getSection().getCompteAnnexe().getIdExterneRole(), getSection().getDescription() });
    }

    /**
     * @return
     */
    public String getDateDocument() {
        return dateDocument;
    }

    /**
     * @return globaz.framework.printing.FWDocument
     */
    @Override
    public BIDocument getDocumentClass() throws Exception {
        return new CATaxation_Doc();
    }

    /**
     * Cette méthode permet de récupérer un événement contentieux pour une étape spécifique Il faut renseigner les
     * informations suivantes.
     * 
     * @param session
     * @param idSection
     * @param idSeqCon
     * @param typeEtape
     * @return CAEvenementContentieux, retourne null en cas d'erreur
     * @throws Exception
     */
    private CAEvenementContentieux getEvenementContentieuxForEtape(BSession session, String idSection, String idSeqCon,
            String typeEtape) throws Exception {
        CAEvenementContentieux evCont = null;
        CAEvenementContentieuxForEtapeManager evEtapeMan = new CAEvenementContentieuxForEtapeManager();
        evEtapeMan.setISession(session);
        evEtapeMan.setForIdSection(idSection);
        evEtapeMan.setForIdSeqCon(idSeqCon);
        evEtapeMan.setForTypeEtape(typeEtape);
        evEtapeMan.find(0);
        if (evEtapeMan.isEmpty()) {
            throw new Exception("Pas de sommation trouvée pour cet id de section" + idSection);
        }
        evCont = (CAEvenementContentieux) evEtapeMan.getFirstEntity();
        return evCont;
    }

    /**
     * @return String
     */
    public String getLigneAdresse1() {
        return ligneAdresse1;
    }

    /**
     * @return String
     */
    public String getLigneAdresse2() {
        return ligneAdresse2;
    }

    /**
     * @return String
     */
    public String getLigneAdresse3() {
        return ligneAdresse3;
    }

    /**
     * @return String
     */
    public String getLigneAdresse4() {
        return ligneAdresse4;
    }

    /**
     * @return String
     */
    public String getLigneAdresse5() {
        return ligneAdresse5;
    }

    /**
     * @return String
     */
    public String getLigneAdresse6() {
        return ligneAdresse6;
    }

    /**
     * @return String
     */
    public String getLigneAdresse7() {
        return ligneAdresse7;
    }

    public String getSignature() {
        StringBuilder signature = new StringBuilder("");
        dumpNiveau(6, signature, "\n");
        return signature.toString();
    }

    /**
     * Cette méthode permet de récupérer le total des taxes
     * 
     * @return String
     */
    public String getTaxe() {
        // Récupération des différentes taxes
        Enumeration taxes = listTaxes();
        CATaxe t;
        FWCurrency cMontant = new FWCurrency(0.00);
        if ((taxes != null) && taxes.hasMoreElements()) {
            while (taxes.hasMoreElements()) {
                t = (globaz.osiris.db.contentieux.CATaxe) taxes.nextElement();
                cMontant.add(t.getMontantTaxe());
            }
            taxe = cMontant.toString();
        } else {
            taxe = CATaxation.MONTANT_ZERO;
        }
        return taxe;
    }

    public String getTexteCorps(int niveau, int position) {
        return super.getTexte(niveau, position).toString();
    }

    public String getTexteDebut() {
        StringBuilder corps = new StringBuilder("");
        dumpNiveau(3, corps, "\n\n");
        CAEvenementContentieux evSommation;
        try {
            evSommation = getEvenementContentieuxForEtape(getSession(), getSection().getIdSection(),
                    getParametreEtape().getIdSequenceContentieux(), APIEtape.SOMMATION);
            return this.formatMessage(corps,
                    new Object[] { "\n", getTiers().getPolitesse(), evSommation.getDateExecution() });
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getTexteFin() {
        StringBuilder texteFin = new StringBuilder("");
        dumpNiveau(5, texteFin, "\n\n");
        return this.formatMessage(texteFin, new Object[] { "\n", getTiers().getPolitesse() });

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntDocumentContentieux#isModePrevisionnel()
     */
    @Override
    public Boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * @param string
     */
    public void setDateDocument(String string) {
        dateDocument = string;
    }

    /**
     * @param modePervisionnel
     *            boolean
     */
    @Override
    public void setModePrevisonnel(Boolean booleen) {
        modePrevisionnel = booleen;
    }

}
