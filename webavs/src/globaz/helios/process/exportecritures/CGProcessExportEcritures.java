package globaz.helios.process.exportecritures;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExtendedMvtCompteListViewBean;
import globaz.helios.db.comptes.CGExtendedMvtCompteViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.exportEcritures.CGExportEcritures;
import globaz.helios.db.comptes.exportEcritures.CGExportEcrituresManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.xml.JadeXmlWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.xml.transform.TransformerException;

/**
 * Process d'exportation d'écriture pou'agence de lausanne
 * 
 * @author SCO 30 mars 2010
 */
public class CGProcessExportEcritures extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String NUMERO_REFERENCE_INFOROM = ""; // TODO SCO

    private String dateFinPeriodeComptable;
    private String idComptabilite; // Provisoire ou définitif
    private String idExerciceComptable;
    private String idMandat;
    private String idPeriodeComptable;
    private String libelleExportation = "";
    private String numeroPieceComptable = "";
    private String periodeComptableCode = "";

    /**
     * Constructor for CGProcessExportEcritures.
     */
    public CGProcessExportEcritures() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessExportEcritures.
     * 
     * @param parent
     */
    public CGProcessExportEcritures(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessExportEcritures.
     * 
     * @param session
     */
    public CGProcessExportEcritures(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        boolean status = true;
        try {
            // Charger la période comptable
            // --------------------------------------------------------
            CGPeriodeComptable periodeComptable = retrievePeriodeComptable();

            // Récupération de la période comptable
            // --------------------------------------------------------
            setPeriodeComptableCode(periodeComptable);

            // Récupération de la date de fin de période
            // --------------------------------------------------------
            setDateEcriture(periodeComptable);

            // Création du document XML
            // --------------------------------------------------------
            CGExportEcrituresXmlParser xmlParser = new CGExportEcrituresXmlParser();
            xmlParser.setSession(getSession());

            // Parcours des mouvements du compte
            // --------------------------------------------------------
            creationDesEcritures(xmlParser);

            // Publication du document
            // --------------------------------------------------------
            JadePublishDocumentInfo documentInfo = createDocumentInfo();
            documentInfo.setDocumentTypeNumber(CGProcessExportEcritures.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(documentInfo, exportToFile(xmlParser));

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            status = false;
        }

        return status;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isIntegerEmpty(getIdExerciceComptable())) {
            this._addError(getTransaction(), getSession().getLabel("GLOBAL_EXERCICE_INEXISTANT"));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdPeriodeComptable())) {
            this._addError(getTransaction(), getSession().getLabel("GLOBAL_PERIODE_INEXISTANT"));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdComptabilite())) {
            this._addError(getTransaction(), getSession().getLabel("GLOBAL_TYPE_COMPTABILITE_INEXISTANT"));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdMandat())) {
            this._addError(getTransaction(), getSession().getLabel("GLOBAL_MANDAT_INEXISTANT"));
        }
        if (JadeStringUtil.isIntegerEmpty(getLibelleExportation())) {
            this._addError(getTransaction(), getSession().getLabel("libelleexportationdoitetrerenseigne"));
        }
        if (JadeStringUtil.isIntegerEmpty(getNumeroPieceComptable())) {
            this._addError(getTransaction(), getSession().getLabel("numeropiececomptabledoitetrerenseigne"));
        }

    }

    private String createLibelleCompte(CGExportEcritures compte) {
        String libelle = "";
        if (compte.getLibelle().length() > 30) {
            libelle = compte.getLibelle().substring(0, 30) + ", " + getLibelleExportation() + " "
                    + getPeriodeComptableCode();
        } else {
            libelle = compte.getLibelle() + ", " + getLibelleExportation() + " " + getPeriodeComptableCode();
        }

        return libelle;
    }

    /**
     * Parcours des comptes et créations des écritures a exporter.
     * 
     * @param xmlParser
     * @throws Exception
     */
    private void creationDesEcritures(CGExportEcrituresXmlParser xmlParser) throws Exception {

        CGExportEcrituresManager manager = new CGExportEcrituresManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(getIdExerciceComptable());
        manager.setForIdMandat(getIdMandat());
        manager.setReqPeriodeComptable(getIdPeriodeComptable());
        manager.setReqComptabilite(getIdComptabilite());
        manager.setOrderBy(CGPlanComptableViewBean.FIELD_IDEXTERNE);
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.getSize(); i++) {

            CGExportEcritures compte = (CGExportEcritures) manager.getEntity(i);

            if ((compte != null) && !JadeStringUtil.isEmpty(compte.getIdExterne())) {

                if (CGCompte.CS_COMPTE_EXPLOITATION.equals(compte.getIdDomaine())) {
                    gestionCompteExploitationAdministrationInvestissement(compte, xmlParser);
                } else if (CGCompte.CS_COMPTE_ADMINISTRATION.equals(compte.getIdDomaine())) {
                    gestionCompteAdministration(compte, xmlParser);
                } else if (CGCompte.CS_COMPTE_INVESTISSEMENT.equals(compte.getIdDomaine())) {
                    gestionCompteExploitationAdministrationInvestissement(compte, xmlParser);
                }
            }
        }
    }

    /**
     * Création du manager d'acces aux écritures d'un compte.
     * 
     * @param idCompte
     *            Le numéro d'un compte
     * @return
     */
    private CGExtendedMvtCompteListViewBean creerManager(String idCompte) {

        CGExtendedMvtCompteListViewBean managerMvtCpt = new CGExtendedMvtCompteListViewBean();
        managerMvtCpt.setSession(getSession());
        managerMvtCpt.setForIdExerciceComptable(getIdExerciceComptable());
        managerMvtCpt.setForIdMandat(getIdMandat());
        managerMvtCpt.setReqComptabilite(getIdComptabilite());
        managerMvtCpt.setReqPeriodeComptable(getIdPeriodeComptable());
        managerMvtCpt.setForIdCompte(idCompte);
        managerMvtCpt.wantForEstActive(true);

        return managerMvtCpt;
    }

    /**
     * Exportation du fichier
     * 
     * @param xmlParser
     * @return
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws TransformerException
     */
    private String exportToFile(CGExportEcrituresXmlParser xmlParser) throws UnsupportedEncodingException,
            FileNotFoundException, IOException, TransformerException {
        String destination = Jade.getInstance().getHomeDir() + "/" + CGApplication.APPLICATION_HELIOS_REP
                + "/work/exportEcrituresOut" + System.currentTimeMillis() + ".xml";
        Writer out = new OutputStreamWriter(new FileOutputStream(destination), "ISO-8859-1");
        out.write(JadeXmlWriter.asXML(xmlParser.getDocument()));
        out.close();

        return destination;
    }

    private void gestionCompteAdministration(CGExportEcritures compte, CGExportEcrituresXmlParser xmlParser)
            throws Exception {
        // On prend les comptes d'administration à l'exception du compte 9100.5990.0000, ce compte
        // sert à la mise en compte du résultat d'admin. du secteur 9 transéféré au siège de la caisse
        if ("9100.5990.0000".equals(compte.getIdExterne())) {
            return;
        }
        gestionCompteExploitationAdministrationInvestissement(compte, xmlParser);

    }

    /**
     * @param compte
     * @param xmlParser
     * @throws Exception
     */
    private void gestionCompteExploitationAdministrationInvestissement(CGExportEcritures compte,
            CGExportEcrituresXmlParser xmlParser) throws Exception {

        String libelleCompte = createLibelleCompte(compte);

        FWCurrency currencyDebit = new FWCurrency();
        FWCurrency currencyCredit = new FWCurrency();

        CGExtendedMvtCompteListViewBean managerMvtCpt = creerManager(compte.getIdCompte());
        managerMvtCpt.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (CGCompte.CS_GENRE_CHARGE.equals(compte.getIdGenre())
                || CGCompte.CS_GENRE_PRODUIT.equals(compte.getIdGenre())) {

            for (int j = 0; j < managerMvtCpt.getSize(); j++) {
                CGExtendedMvtCompteViewBean mvtCompte = (CGExtendedMvtCompteViewBean) managerMvtCpt.getEntity(j);

                currencyDebit.add(mvtCompte.getDoit());
                currencyCredit.add(mvtCompte.getAvoir());
            }

            if (!currencyDebit.isZero()) {
                if (currencyDebit.isPositive()) {
                    xmlParser.addEcritureDoubleToXml(null, getDateFinPeriodeComptable(),
                            limiterTailleLibelle(libelleCompte), compte.getIdExterne(), compte.getCcAgenceSiege(),
                            currencyDebit.toString(), getNumeroPieceComptable(), getMemoryLog());
                } else {
                    currencyDebit.abs();
                    xmlParser.addEcritureDoubleToXml(null, getDateFinPeriodeComptable(),
                            limiterTailleLibelle(libelleCompte), compte.getCcAgenceSiege(), compte.getIdExterne(),
                            currencyDebit.toString(), getNumeroPieceComptable(), getMemoryLog());
                }
            }
            if (!currencyCredit.isZero()) {
                if (currencyCredit.isPositive()) {
                    xmlParser.addEcritureDoubleToXml(null, getDateFinPeriodeComptable(),
                            limiterTailleLibelle(libelleCompte), compte.getCcAgenceSiege(), compte.getIdExterne(),
                            currencyCredit.toString(), getNumeroPieceComptable(), getMemoryLog());
                } else {
                    currencyCredit.abs();
                    xmlParser.addEcritureDoubleToXml(null, getDateFinPeriodeComptable(),
                            limiterTailleLibelle(libelleCompte), compte.getIdExterne(), compte.getCcAgenceSiege(),
                            currencyCredit.toString(), getNumeroPieceComptable(), getMemoryLog());
                }
            }
        }
    }

    public String getDateFinPeriodeComptable() {
        return dateFinPeriodeComptable;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("EXPORTATION_ECRITURES_ERROR");
        } else {
            return getSession().getLabel("EXPORTATION_ECRITURES_OK");
        }
    }

    public String getIdComptabilite() {
        return idComptabilite;
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    public String getIdMandat() {
        return idMandat;
    }

    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    public String getLibelleExportation() {
        return libelleExportation;
    }

    public String getNumeroPieceComptable() {
        return numeroPieceComptable;
    }

    public String getPeriodeComptableCode() {
        return periodeComptableCode;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Permet de limiter la taille a 40 caracteres.
     * 
     * @param libelle
     * @return
     */
    private String limiterTailleLibelle(String libelle) {

        if (!JadeStringUtil.isEmpty(libelle) && (libelle.length() > 50)) {
            return libelle.substring(0, 49);
        }

        return libelle;
    }

    private CGPeriodeComptable retrievePeriodeComptable() throws Exception {
        CGPeriodeComptable periodeComptable = new CGPeriodeComptable();
        periodeComptable.setIdPeriodeComptable(getIdPeriodeComptable());
        periodeComptable.setSession(getSession());
        periodeComptable.retrieve(getTransaction());
        return periodeComptable;
    }

    /**
     * Permet de setter la date de passage des écritures<BR>
     * LA date correspond a la date de fin de période comptable.<BR>
     * En cas de probleme, la date sera celle du jour de l'execution de l'exportation
     */
    private void setDateEcriture(CGPeriodeComptable periodeComptable) throws Exception {

        JADate date = JACalendar.today();

        if (!periodeComptable.isNew() && !periodeComptable.hasErrors()) {
            setDateFinPeriodeComptable(JACalendar.format(periodeComptable.getDateFin()));
        } else {
            setDateFinPeriodeComptable(JACalendar.format(date));
        }
    }

    public void setDateFinPeriodeComptable(String dateFinPeriodeComptable) {
        this.dateFinPeriodeComptable = dateFinPeriodeComptable;
    }

    public void setIdComptabilite(String idComptabilite) {
        this.idComptabilite = idComptabilite;
    }

    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    public void setIdPeriodeComptable(String idPeriodeComptable) {
        this.idPeriodeComptable = idPeriodeComptable;
    }

    public void setLibelleExportation(String libelleExportation) {
        this.libelleExportation = libelleExportation;
    }

    public void setNumeroPieceComptable(String numeroPieceComptable) {
        this.numeroPieceComptable = numeroPieceComptable;
    }

    public void setPeriodeComptableCode(CGPeriodeComptable periodeComptable) {
        String periodeComptableCode = "";
        if (!periodeComptable.isNew()) {
            try {
                int year;
                year = JACalendar.getYear(periodeComptable.getDateDebut());
                this.periodeComptableCode = periodeComptable.getCode() + "." + year;
            } catch (JAException e) {
                // Si problème de lecture de la date de début, on ne fait rien de la période comptable
            }

        } else {
            this.periodeComptableCode = periodeComptableCode;
        }
    }

}
