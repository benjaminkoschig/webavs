package globaz.ij.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAVector;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prononces.IJPrononceJointDemande;
import globaz.ij.db.prononces.IJPrononceJointDemandeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lyra.process.LYAbstractListGenerator;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IJListeDateEcheancesPrononces extends LYAbstractListGenerator {

    private static final long serialVersionUID = 1L;
    public FWMemoryLog journalLog = new FWMemoryLog();
    private boolean ajouterCommunePolitique = false;

    public IJListeDateEcheancesPrononces() throws Exception {
        this(new BSession(IJApplication.DEFAULT_APPLICATION_IJ));
    }

    public IJListeDateEcheancesPrononces(BProcess parent) throws Exception {
        this();
        super.setParentWithCopy(parent);
    }

    public IJListeDateEcheancesPrononces(BSession session) {
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("LISTE_ECHEANCE_TITRE"),
                new IJPrononceJointDemandeManager(), IJApplication.DEFAULT_APPLICATION_IJ);
    }

    @Override
    public void _beforeExecuteReport() {

        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.ECHEANCES_REVISION_CAS_IJ);

        IJPrononceJointDemandeManager manager = (IJPrononceJointDemandeManager) _getManager();
        manager.setSession(getSession());
        manager.setForDateEcheance(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getMoisTraitement()));
        manager.setOrderBy(IJPrononceJointDemande.FIELDNAME_NOM + "," + IJPrononceJointDemande.FIELDNAME_PRENOM);
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("LISTE_ECHEANCE_TITRE") + " au " + getMoisTraitement());

        super._beforeExecuteReport();
    }

    @Override
    protected void bindPageHeader() throws Exception {

        if (isAjouterCommunePolitique()) {
            _addHeaderLine(getFontCompanyName(), _getCompanyName(), getFontDate(), null, getFontDate(),
                    JACalendar.format(JACalendar.today()));

        } else {
            _addHeaderLine(getFontCompanyName(), _getCompanyName(), null, null, getFontDate(),
                    JACalendar.format(JACalendar.today()));
        }

        _addHeaderLine(null, null, getFontDocumentTitle(), _getDocumentTitle(), null, null);

        _addHeaderLine(getFontDate(),
                getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()) + " : "
                        + getSession().getUserId(), null, null, null, null);
    }

    @Override
    protected void _validate() throws Exception {

        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {

            journalLog.logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "IJListeDateEcheancesPrononces");
            getMemoryLog().logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "IJListeDateEcheancesPrononces");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (getSession().hasErrors()) {

            journalLog.logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                    "IJListeDateEcheancesPrononces");
            getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                    "IJListeDateEcheancesPrononces");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (!journalLog.hasMessages()) {
            journalLog.logMessage(getSession().getLabel("EXECUTION_OK"), FWMessage.INFORMATION, "");
        }

        if (journalLog.hasMessages()) {
            FWLog log = journalLog.saveToFWLog(getTransaction());
            setIdLog(log.getIdLog());
        }

        super._validate();
    }

    @Override
    protected void addRow(BEntity value) throws FWIException {
        IJPrononceJointDemande entity = (IJPrononceJointDemande) value;

        try {
            // le detail de la retenue
            if (isAjouterCommunePolitique()) {
                _addCell(entity.getCommunePolitique());
            }
            _addCell(entity.getIdPrononce());
            _addCell(entity.getNoAVS() + " / " + entity.getNom() + " " + entity.getPrenom() + " / "
                    + getLibelleCourtSexe(entity.getCsSexe()) + " / " + getLibellePays(entity.getCsNationalite()));
            _addCell(getSession().getCodeLibelle(entity.getCsEtatDemande()));
            _addCell(entity.getDateEcheance());
            _addCell(getSession().getCodeLibelle(entity.getCsMotifEcheance()));

        } catch (Exception e) {
            throw new FWIException(e);
        }
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est passé en paramètre
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est passé en paramètre
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

    }

    /**
     * Initialise la table des données
     * <p>
     * <u>Utilisation</u>:
     * <ul>
     * <li><code>_addColumn(..)</code> permet de déclarer les colonnes
     * <li><code>_group...(..)</code> permet de déclarer les groupages
     * </ul>
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#initializeTable()
     **/
    @Override
    protected void initializeTable() {
        if (isAjouterCommunePolitique()) {
            Set<String> setIdTiers = new HashSet<String>();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Integer.parseInt(getMoisTraitement().substring(0, 2)) - 1);
            cal.set(Calendar.YEAR, Integer.parseInt(getMoisTraitement().substring(3)));
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            IJPrononceJointDemandeManager manager = (IJPrononceJointDemandeManager) _getManager();

            for (int i = 0; i < manager.size(); i++) {
                IJPrononceJointDemande demande = (IJPrononceJointDemande) manager.getEntity(i);
                if (!JadeStringUtil.isBlankOrZero(demande.getIdTiers())) {
                    setIdTiers.add(demande.getIdTiers());
                }
            }

            Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, cal.getTime(),
                    getSession());

            JAVector vectorManagerContainer = manager.getContainer();
            for (Object element : vectorManagerContainer) {
                IJPrononceJointDemande demande = (IJPrononceJointDemande) element;
                String communePolitique = mapCommuneParIdTiers.get(demande.getIdTiers());
                if (!JadeStringUtil.isEmpty(communePolitique)) {
                    demande.setCommunePolitique(communePolitique);
                }
            }

            Collections.sort(vectorManagerContainer);

            this._addColumnCenter(getSession()
                    .getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 1);
        }
        this._addColumnLeft(getSession().getLabel("LISTE_ECHEANCE_IDPRONONCE"), 1);
        this._addColumnLeft("", 3);
        this._addColumnLeft(getSession().getLabel("LISTE_ECHEANCE_CSETATDEMANDE"), 1);
        this._addColumnLeft(getSession().getLabel("LISTE_ECHEANCE_DATECHEANCE"), 1);
        this._addColumnLeft(getSession().getLabel("LISTE_ECHEANCE_CSMOTIFECHEANCE"), 1);

        _groupManual();

    }

    public boolean isAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }
}