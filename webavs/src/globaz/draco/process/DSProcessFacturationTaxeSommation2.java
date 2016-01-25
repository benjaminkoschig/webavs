package globaz.draco.process;

import globaz.draco.application.DSApplication;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CASection;

public class DSProcessFacturationTaxeSommation2 extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ID_CS_MONTANT_SOMMATION = "123002";
    public static final String ID_CS_RUBRIQUE_SOMMATION = "123001";

    public static final String KEY_PARAM_MONTANT_SOMMATION = "MONTANT";
    public static final String KEY_PARAM_RUBRIQUE_SOMMATION = "RUBRIQUE";

    private String etape = "";
    private String idModuleFacturation = "";
    private String libelleAfact = "";
    private String montant = "";
    private globaz.musca.db.facturation.FAPassage passage = null;
    private String rubrique = "";

    public DSProcessFacturationTaxeSommation2() {
        super();
    }

    public DSProcessFacturationTaxeSommation2(BProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        try {

            initProcessAttributes();

            validProcessAttributes();

            FAEnteteFactureManager entManager = new FAEnteteFactureManager();
            entManager.setSession(getSession());
            entManager.setForIdPassage(passage.getIdPassage());
            entManager.setInIdSousType(giveInIdSousType());
            entManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            entManager.find();

            for (int i = 0; i < entManager.size(); i++) {

                if (isAborted()) {
                    break;
                }

                FAEnteteFacture entete = (FAEnteteFacture) entManager.getEntity(i);

                if ((entete != null) && (entete.getIdExterneFacture().length() > 4)) {
                    initVariableTaxeSelonAnnee(entete.getIdExterneFacture().substring(0, 4));
                }

                if ((entete != null) && isIdExterneFactureValid(entete.getIdExterneFacture()) && isEtapeGenere(entete)
                        && !isDejaTaxe(entete)) {

                    generateAfact(entete);
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
        return true;
    }

    protected void generateAfact(FAEnteteFacture enteteFacture) throws Exception {

        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(enteteFacture.getIdEntete());
        afact.setISession(getSession());
        afact.setIdPassage(passage.getIdPassage());
        afact.setNumCaisse(giveIdCaissePrincipale(enteteFacture));
        afact.setIdModuleFacturation(getIdModuleFacturation());
        afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
        afact.setIdRubrique(getRubrique());
        afact.setLibelle(getLibelleAfact());
        afact.setMontantFacture(getMontant());
        afact.add(getTransaction());
    }

    @Override
    protected String getEMailObject() {
        // No email send
        return null;
    }

    public String getEtape() {
        return etape;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getLibelleAfact() {
        return libelleAfact;
    }

    public String getMontant() {
        return montant;
    }

    public FAPassage getPassage() {
        return passage;
    }

    public String getRubrique() {
        return rubrique;
    }

    protected String giveIdCaissePrincipale(FAEnteteFacture entete) throws Exception {
        return AFAffiliationUtil.getIdCaissePrincipale(loadAffilieFromEntete(entete), true, entete
                .getIdExterneFacture().substring(0, 4));
    }

    protected String giveInIdSousType() throws Exception {

        String theInIdSousType = APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL + ","
                + APISection.ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE;

        if ("true".equalsIgnoreCase(GlobazServer.getCurrentSystem()
                .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO)
                .getProperty("sommationDSSurDecompte14", "false"))) {
            theInIdSousType = theInIdSousType + "," + APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE;
        }

        return theInIdSousType;
    }

    protected void initProcessAttributes() throws Exception {
        setEtape(ILEConstantes.CS_DEF_FORMULE_SOMMATION_DS);
    }

    protected void initVariableTaxeSelonAnnee(String annee) throws Exception {
        // PO 9156
        String dateReference = "01.01." + annee;
        setRubrique(FWFindParameter.findParameter(getTransaction(),
                DSProcessFacturationTaxeSommation2.ID_CS_RUBRIQUE_SOMMATION,
                DSProcessFacturationTaxeSommation2.KEY_PARAM_RUBRIQUE_SOMMATION, dateReference, "0", 0));
        setMontant(FWFindParameter.findParameter(getTransaction(),
                DSProcessFacturationTaxeSommation2.ID_CS_MONTANT_SOMMATION,
                DSProcessFacturationTaxeSommation2.KEY_PARAM_MONTANT_SOMMATION, dateReference, "0", 0));
    }

    protected boolean isDejaTaxe(FAEnteteFacture entete) throws Exception {

        CACompteAnnexe cptAnnexe = new CACompteAnnexe();
        cptAnnexe.setSession(getSession());
        cptAnnexe.setAlternateKey(1);
        cptAnnexe.setIdExterneRole(entete.getIdExterneRole());
        cptAnnexe.setIdRole(entete.getIdRole());
        cptAnnexe.retrieve();

        String texteFixeException = "unable to determine if isDejaTaxe : ";

        if ((cptAnnexe == null) || cptAnnexe.isNew()) {
            // throw new Exception(texteFixeException + "Compte Annexe not found for id entete facture "
            // + entete.getIdEntete() + " and Affilie " + entete.getIdExterneRole());

            // ald - 12.10.2011 - Un nouvel affilié n'a pas de compte annexe. C'est la première facturation qui crée le
            // compte annexe
            return false;
        }

        CAOperationManager op = new CAOperationManager();
        op.setSession(getSession());
        op.setForIdCompteAnnexe(cptAnnexe.getIdCompteAnnexe());
        op.setForIdCompte(getRubrique());
        op.setForEtat(APIOperation.ETAT_COMPTABILISE);
        op.find(BManager.SIZE_NOLIMIT);
        op.find();

        for (int i = 0; i < op.size(); i++) {
            CAOperation opEntity = (CAOperation) op.get(i);

            if (opEntity == null) {
                throw new Exception(texteFixeException + "Operation not retrieve for id Compte Annexe "
                        + cptAnnexe.getIdCompteAnnexe() + " and id rubrique " + getRubrique());
            }

            String idExterne = entete.getIdExterneFacture();
            CASection sec = opEntity.getSection();

            if (sec == null) {
                throw new Exception(texteFixeException + "Section not found for id operation "
                        + opEntity.getIdOperation());
            }

            if (APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL.equals(sec.getCategorieSection())
                    || APISection.ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE.equals(sec.getCategorieSection())
                    || APISection.ID_CATEGORIE_SECTION_LTN.equals(sec.getCategorieSection())
                    || APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE.equals(sec.getCategorieSection())) {
                int anneeEnCours = 0;
                int anneeFacture = 0;
                anneeFacture = new JADate(sec.getDateDebutPeriode()).getYear();

                if (idExterne.length() >= 4) {
                    anneeEnCours = Integer.parseInt(idExterne.substring(0, 4));
                }

                if (anneeEnCours == anneeFacture) {
                    return true;
                }
            }
        }

        return false;

    }

    protected boolean isEtapeGenere(FAEnteteFacture entete) throws Exception {
        LUJournalListViewBean viewBean = new LUJournalListViewBean();

        // On sette les données nécessaires à la recherche de l'envoi
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, entete.getIdTiers());
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, entete.getIdExterneRole());
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                DSApplication.DEFAULT_APPLICATION_DRACO);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, entete.getIdTiers());
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE,
                entete.getIdExterneFacture().substring(0, 4));

        // On va regarder s'il y a déjà un envoi correspondant aux critères qui
        // nous intéressent.
        viewBean.setSession(getSession());
        viewBean.setProvenance(provenanceCriteres);
        viewBean.wantComplementJournal(true);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
        viewBean.setForValeurCodeSysteme(getEtape());
        viewBean.find();

        return viewBean.size() >= 1;
    }

    protected boolean isIdExterneFactureValid(String theIdExterneFacture) {
        return (theIdExterneFacture != null) && (theIdExterneFacture.length() == 9)
                && ("000".equals(theIdExterneFacture.substring(6, 9)));
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    protected AFAffiliation loadAffilieFromEntete(FAEnteteFacture entete) throws Exception {

        String annee = entete.getIdExterneFacture().substring(0, 4);

        AFAffiliationManager affiMana = new AFAffiliationManager();
        affiMana.setSession(getSession());
        affiMana.setForAffilieNumero(entete.getIdExterneRole());
        affiMana.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
        affiMana.setForDateDebutAffLowerOrEqualTo("31.12." + annee);
        affiMana.setFromDateFin("01.01." + annee);
        affiMana.changeManagerSize(BManager.SIZE_NOLIMIT);
        affiMana.find();

        if ((affiMana.size() != 1) || (affiMana.getFirstEntity() == null)) {
            throw new Exception("unable to load affiliation " + entete.getIdExterneRole() + " from entete facture "
                    + entete.getIdEntete());
        }

        return (AFAffiliation) affiMana.getFirstEntity();

    }

    public void setEtape(String etape) {
        this.etape = etape;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public void setLibelleAfact(String string) {
        libelleAfact = string;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
    }

    protected void validProcessAttributes() throws Exception {

        StringBuffer notValidAttributes = new StringBuffer();

        if (JadeStringUtil.isBlankOrZero(getEtape())) {
            notValidAttributes.append(" etape is blank or zero");
        }

        if (JadeStringUtil.isBlankOrZero(getIdModuleFacturation())) {
            notValidAttributes.append(" moduleFacturation is blank or zero");
        }

        if (getPassage() == null) {
            notValidAttributes.append(" passage is null");
        }

        if (notValidAttributes.length() >= 1) {
            throw new Exception("process attributes not valid : " + notValidAttributes.toString());
        }

    }

}