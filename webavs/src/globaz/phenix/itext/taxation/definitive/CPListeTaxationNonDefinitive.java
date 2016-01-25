package globaz.phenix.itext.taxation.definitive;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitive;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitiveManager;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class CPListeTaxationNonDefinitive extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public final static String NUM_REF_INFOROM_LISTE_TAXA_DEF = "0157CFA";
    /**
	 * 
	 */
    private static final long serialVersionUID = -5192240794451174398L;

    private String noPassage = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CPListeTaxationDefinitiveModuleImpl.
     */
    public CPListeTaxationNonDefinitive() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "PRESTATIONS", "GLOBAZ", "Liste des taxations non définitives", new CPTaxationDefinitiveManager(),
                "APG");
    }

    /**
     * Crée une nouvelle instance de la classe CPListeTaxationDefinitiveModuleImpl.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public CPListeTaxationNonDefinitive(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "PRESTATIONS", "GLOBAZ", "Liste des taxations définitives", new CPTaxationDefinitiveManager(),
                "APG");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * transfère des paramètres au manager;
     */

    /**
     */
    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber(NUM_REF_INFOROM_LISTE_TAXA_DEF);
        CPTaxationDefinitiveManager manager = (CPTaxationDefinitiveManager) _getManager();
        manager.setSession(getSession());
        manager.setForNoPassage(getNoPassage());
        manager.setForEtatPrestation(CPTaxationDefinitiveManager.CS_ETAT_PRESTATION_DEFINITIF);
        manager.setForTypeDecisionIn(CPTaxationDefinitiveManager.CS_DECISION_CORRECTION + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_PROVISOIRE + ", "
                + CPTaxationDefinitiveManager.CS_DECISION_ACOMPTE);

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new CPTaxationDefinitive());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    /*
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            _addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                _addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        if (JadeStringUtil.isEmpty(getNoPassage())) {
            _addError(getSession().getLabel("LISTE_TAX_DEF_NO_PASSAGE_EMPTY"));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /*
     * Contenu des cellules
     */
    /**
     * @param entity
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        // valeurs
        CPTaxationDefinitive elem = (CPTaxationDefinitive) entity;

        BigDecimal rd = null;
        BigDecimal ri = null;
        BigDecimal ecart = null;
        try {
            rd = new BigDecimal(elem.getRevenuDeterminant());
            ri = new BigDecimal(elem.getRevenuIndependant());

            ecart = new BigDecimal(ri.toString());
            ecart = ecart.multiply(new BigDecimal(100));
            ecart = ecart.divide(rd, BigDecimal.ROUND_HALF_EVEN);

            ecart = ecart.subtract(new BigDecimal(100));
        } catch (Exception e) {
            ecart = new BigDecimal(0);
        }

        _addCell(elem.getNss());
        _addCell(elem.getNoAffilie());
        _addCell(elem.getDateDebut());
        _addCell(elem.getDateFin());
        _addCell("");
        _addCell(JANumberFormatter.format(elem.getRevenuDeterminant()));
        _addCell(JANumberFormatter.format(elem.getRevenuIndependant()));
        _addCell(JANumberFormatter.round(ecart, 1, 0, JANumberFormatter.INF).toString());
    }

    /*
     * Titre de l'email
     */
    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_TAXATION_DEFINITIVE");
    }

    public String getNoPassage() {
        return noPassage;
    }

    /*
     * Initialisation des colonnes et des groupes
     */
    /**
     */
    @Override
    protected void initializeTable() {
        // colonnes
        _addColumnLeft(getSession().getLabel("LISTE_TAX_DEF_NSS"), 10);
        _addColumnLeft(getSession().getLabel("LISTE_TAX_DEF_NAFF"), 10);
        _addColumnLeft(getSession().getLabel("LISTE_TAX_DEF_DATE_DEBUT"), 8);
        _addColumnLeft(getSession().getLabel("LISTE_TAX_DEF_DATE_FIN"), 8);
        _addColumnRight(" ", 1);
        _addColumnRight(getSession().getLabel("LISTE_TAX_DEF_REVENU_DETERMINANT"), 14);
        _addColumnRight(getSession().getLabel("LISTE_TAX_DEF_REVENU_INDEPENDANT"), 14);
        _addColumnRight(getSession().getLabel("LISTE_TAX_DEF_ECART"), 8);

    }

    /**
     * Set la jobQueue
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setNoPassage(String noPassage) {
        this.noPassage = noPassage;
    }
}
