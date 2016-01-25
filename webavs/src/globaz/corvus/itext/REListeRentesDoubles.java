package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteJoinPersonneAvs;
import globaz.corvus.db.rentesaccordees.RERenteJoinPersonneAvsManager;
import globaz.corvus.process.REGenererListesVerificationProcess;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * Imprime la liste des rentes à doubles pour un mois donné
 * 
 * @author BSC
 */
public class REListeRentesDoubles extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String lastIdTiers;
    private String mois;
    private String moisMoinsUn;

    public REListeRentesDoubles() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
    }

    public REListeRentesDoubles(BProcess parent) throws Exception {
        this();
        super.setParentWithCopy(parent);
    }

    public REListeRentesDoubles(BSession session) {
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("LISTE_RDB_TITRE"),
                new RERenteJoinPersonneAvsManager(), REApplication.DEFAULT_APPLICATION_CORVUS);

        lastIdTiers = null;
        mois = null;
        moisMoinsUn = null;
    }

    @Override
    public void _beforeExecuteReport() {

        try {
            moisMoinsUn = JadeDateUtil.addMonths("01." + getMois(), -1).substring(3);

            // on ajoute au doc info le numéro de référence inforom
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_RENTES_DOUBLES);

            RERenteJoinPersonneAvsManager manager = (RERenteJoinPersonneAvsManager) _getManager();
            manager.setSession(getSession());
            manager.setForDate(getMois());
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("LISTE_RDB_TITRE") + " du " + getMois());

            getDocumentInfo().setDocumentProperty(REGenererListesVerificationProcess.PROPERTY_DOCUMENT_ORDER,
                    REGenererListesVerificationProcess.LISTE_RENTES_DOUBLES_ORDER);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    protected void addRow(BEntity value) throws FWIException {
        RERenteJoinPersonneAvs entity = (RERenteJoinPersonneAvs) value;

        if (JadeStringUtil.isBlankOrZero(entity.getDateFinDroit())
                || !JadeDateUtil.isDateMonthYearAfter(moisMoinsUn, entity.getDateFinDroit())) {
            try {

                // on n'ecrit qu'une fois le nom de chaque assure
                if ((lastIdTiers == null) || !lastIdTiers.equals(entity.getIdTiers())) {
                    _addCell(getDetailTiersFormatte(entity));
                } else {
                    _addCell("");
                }

                // le detail de la retenue
                String fractionRente = "";
                if (JadeStringUtil.isBlankOrZero(entity.getFractionRente())) {
                    fractionRente = "0";
                } else {
                    fractionRente = entity.getFractionRente();
                }

                _addCell(entity.getDateDebutDroit());
                _addCell(entity.getDateFinDroit());
                _addCell(entity.getCodePrestation() + "." + fractionRente);
                _addCell(new FWCurrency(entity.getMontantPrestation()).toStringFormat());

                lastIdTiers = entity.getIdTiers();
            } catch (Exception e) {
                throw new FWIException(e);
            }
        }
    }

    /**
     * Méthode pour charger une ligne vide avec le message : Aucune erreur pour la période : mm.aaaa
     * 
     * @throws FWIException
     */
    protected void addRowVoid() throws FWIException {
        StringBuffer cellule = new StringBuffer();
        cellule.append(getSession().getLabel("LISTE_ERR_AUCUNE_ERREUR") + getMois());
        _addCell(cellule.toString());
    }

    private String getDetailTiersFormatte(RERenteJoinPersonneAvs entity) {
        StringBuilder detail = new StringBuilder();

        RETiersForJspUtils tiersUtils = RETiersForJspUtils.getInstance(getSession());

        detail.append(entity.getNssBeneficiaire()).append(" / ");
        detail.append(entity.getNomBeneficiaire()).append(" ");
        detail.append(entity.getPrenomBeneficiaire()).append(" / ");
        detail.append(entity.getDateNaissanceBeneficiaire()).append(" / ");
        detail.append(tiersUtils.getLibelleCourtSexe(entity.getCsSexeBeneficiaire())).append(" / ");
        detail.append(tiersUtils.getLibellePays(entity.getIdPaysBeneficiaire()));
        return detail.toString();
    }

    @Override
    protected String getGroupValue(int level, BEntity entity) throws FWIException {
        return ((REPrestationsAccordees) entity).getIdTiersBeneficiaire();
    }

    public String getMois() {
        return mois;
    }

    @Override
    protected void summary() throws FWIException {
        // Si aucune donnée, on insert une ligne avec message "liste vide".
        if (_getManager().size() == 0) {
            addRowVoid();
        }
    }

    @Override
    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("LISTE_RDB_DETAIL_ASSURE"), 3);
        this._addColumnRight(getSession().getLabel("LISTE_RDB_DEBUT_DROIT"), 1);
        this._addColumnRight(getSession().getLabel("LISTE_RDB_FIN_DROIT"), 1);
        this._addColumnRight(getSession().getLabel("LISTE_RDB_CODE_PRESTATION"), 1);
        this._addColumnRight(getSession().getLabel("LISTE_RDB_MONTANT"), 1);
        _groupManual();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setMois(String string) {
        mois = string;
    }
}
