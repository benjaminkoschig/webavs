package globaz.aquila.process.batch.utils;

import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.aquila.pojo.AdresseJournalContentieuxExcelmlPojo;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.tiers.COTiersService;
import globaz.aquila.util.COAdresseUtils;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.norma.db.fondation.PATraductionHelper;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.util.List;

public abstract class COAbstractJournalContentieuxExcelml {

    public static final String EXCELML_JOURNAL_CONTENTIEUX_NUMERO_INFOROM = "0299GCO";

    public static final String EXCELML_MODEL_CELL_NAME_DATE_DOCUMENT = "CELL_DATE_DOCUMENT";
    public static final String EXCELML_MODEL_CELL_NAME_DATE_REFERENCE = "CELL_DATE_REFERENCE";
    public static final String EXCELML_MODEL_CELL_NAME_MODE_EDITION = "CELL_MODE_EDITION";
    public static final String EXCELML_MODEL_CELL_NAME_ROLE = "CELL_ROLE";

    public static final String EXCELML_MODEL_COL_NAME_ADRESSE_CONTENTIEUX = "COL_ADRESSE_CONTENTIEUX";
    public static final String EXCELML_MODEL_COL_NAME_CANTON_ADRESSE_CONTENTIEUX = "COL_CANTON_ADRESSE_CONTENTIEUX";

    public static final String EXCELML_MODEL_COL_NAME_ADRESSE_DOMICILE = "COL_ADRESSE_DOMICILE";
    public static final String EXCELML_MODEL_COL_NAME_CANTON_ADRESSE_DOMICILE = "COL_CANTON_ADRESSE_DOMICILE";

    public static final String EXCELML_MODEL_COL_NAME_ADRESSE_COURRIER = "COL_ADRESSE_COURRIER";
    public static final String EXCELML_MODEL_COL_NAME_CANTON_ADRESSE_COURRIER = "COL_CANTON_ADRESSE_COURRIER";

    public static final String EXCELML_MODEL_COL_NAME_DATE_FACTURE = "COL_DATE_FACTURE";
    public static final String EXCELML_MODEL_COL_NAME_ETAPE_CONTENTIEUX = "COL_ETAPE_CONTENTIEUX";
    public static final String EXCELML_MODEL_COL_NAME_MONTANT_INITIAL_SECTION = "COL_MONTANT_INITIAL_SECTION";
    public static final String EXCELML_MODEL_COL_NAME_MONTANT_POURSUITE_SECTION = "COL_MONTANT_POURSUITE_SECTION";
    public static final String EXCELML_MODEL_COL_NAME_NOM_AFFILIE = "COL_NOM_AFFILIE";
    public static final String EXCELML_MODEL_COL_NAME_NUMERO_AFFILIE = "COL_NUMERO_AFFILIE";
    public static final String EXCELML_MODEL_COL_NAME_NUMERO_SECTION = "COL_NUMERO_SECTION";
    public static final String EXCELML_MODEL_COL_NAME_ROLE_AFFILIE = "COL_ROLE_AFFILIE";
    public static final String EXCELML_MODEL_COL_NAME_SOLDE_COMPTE = "COL_SOLDE_COMPTE";

    private CommonExcelmlContainer containerJournalContentieuxExcelml = null;
    private String dateDocument = "";
    private String dateReference = "";
    private List<String> listIdRoles = null;
    private boolean modePrevisionnel = false;
    private String modelName = null;
    private String modelNameOutput = null;

    /**
     * @param theDateReference
     * @param theDateDocument
     * @param theModePrevisionnel
     * @param theListIdRoles
     */
    public COAbstractJournalContentieuxExcelml(final String theDateReference, final String theDateDocument,
            final boolean theModePrevisionnel, final List<String> theListIdRoles, final String modelName,
            final String modelNameOutput) {
        containerJournalContentieuxExcelml = new CommonExcelmlContainer();
        dateReference = theDateReference;
        dateDocument = theDateDocument;
        modePrevisionnel = theModePrevisionnel;
        listIdRoles = theListIdRoles;
        this.modelName = modelName;
        this.modelNameOutput = modelNameOutput;
    }

    protected abstract void addRowInExcelmlInfoComplementaire(BSession theSession, COContentieux theContentieux,
            COEtape theEtape);

    /**
     * @param theSession
     * @throws Exception
     */
    public void addHeaderInExcelml(BSession theSession) throws Exception {

        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_CELL_NAME_DATE_REFERENCE, dateReference);
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_CELL_NAME_DATE_DOCUMENT, dateDocument);

        String theModeEdition = theSession.getLabel("JOURNAL_CONTENTIEUX_MODE_EDITION_DEFINITIF");
        if (modePrevisionnel) {
            theModeEdition = theSession.getLabel("JOURNAL_CONTENTIEUX_MODE_EDITION_PREVISIONNEL");
        }

        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_CELL_NAME_MODE_EDITION, theModeEdition);

        String theLibellesRoles = getLibellesRolesFromListIdRoles(theSession);
        containerJournalContentieuxExcelml.put(COAbstractJournalContentieuxExcelml.EXCELML_MODEL_CELL_NAME_ROLE,
                theLibellesRoles);
    }

    /**
     * @param session
     * @param contentieux
     * @param etape
     */
    public void addRowInExcelml(BSession session, COContentieux contentieux, COEtape etape) {
        CASection section = contentieux.getSection();
        CACompteAnnexe compteAnnexe = (CACompteAnnexe) section.getCompteAnnexe();

        String numeroAffilie = compteAnnexe.getIdExterneRole();
        String nomAffilie = compteAnnexe.getDescription();
        String role = compteAnnexe.getCARole().getDescription();
        String numeroSection = section.getIdExterne();
        String dateFacture = section.getDateSection();
        // Bug 8619
        String montantInitialSection;
        try {
            montantInitialSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(session,
                    contentieux.getIdSection())[0];
        } catch (Exception e1) {
            montantInitialSection = "0";
        }
        String soldeSection = section.getSolde();
        String soldeCompteAnnexe = compteAnnexe.getSolde();
        String etapeContentieuxLibelle = etape.getLibEtapeLibelle();

        AdresseJournalContentieuxExcelmlPojo adresseJournalContentieuxExcelmlPojo = construitJournalContentieuxExcelmlPojo(
                session, compteAnnexe.getTiers(), contentieux, etape, dateDocument);

        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_NUMERO_AFFILIE, numeroAffilie);
        containerJournalContentieuxExcelml.put(COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_NOM_AFFILIE,
                nomAffilie);
        containerJournalContentieuxExcelml.put(COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_ROLE_AFFILIE,
                role);
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_NUMERO_SECTION, numeroSection);
        containerJournalContentieuxExcelml.put(COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_DATE_FACTURE,
                dateFacture);
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_MONTANT_INITIAL_SECTION,
                montantInitialSection);
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_MONTANT_POURSUITE_SECTION, soldeSection);
        containerJournalContentieuxExcelml.put(COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_SOLDE_COMPTE,
                soldeCompteAnnexe);
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_ETAPE_CONTENTIEUX, etapeContentieuxLibelle);

        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_ADRESSE_CONTENTIEUX,
                adresseJournalContentieuxExcelmlPojo.getAdresseContentieux());
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_CANTON_ADRESSE_CONTENTIEUX,
                adresseJournalContentieuxExcelmlPojo.getCantonAdresseContentieux());

        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_ADRESSE_DOMICILE,
                adresseJournalContentieuxExcelmlPojo.getAdresseDomicile());
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_CANTON_ADRESSE_DOMICILE,
                adresseJournalContentieuxExcelmlPojo.getCantonAdresseDomicile());

        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_ADRESSE_COURRIER,
                adresseJournalContentieuxExcelmlPojo.getAdresseCourrier());
        containerJournalContentieuxExcelml.put(
                COAbstractJournalContentieuxExcelml.EXCELML_MODEL_COL_NAME_CANTON_ADRESSE_COURRIER,
                adresseJournalContentieuxExcelmlPojo.getCantonAdresseCourrier());

        addRowInExcelmlInfoComplementaire(session, contentieux, etape);
    }

    /**
     * @param theSession
     * @return
     * @throws Exception
     */
    public String createFileJournalContentieuxExcelml(BSession theSession) throws Exception {

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + COApplication.APPLICATION_AQUILA_ROOT
                + "/model/excelml/" + theSession.getIdLangueISO().toUpperCase() + "/" + modelName;

        String xmlOutputPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(modelNameOutput);

        xmlOutputPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xmlOutputPath,
                containerJournalContentieuxExcelml);

        return xmlOutputPath;
    }

    private AdresseJournalContentieuxExcelmlPojo construitJournalContentieuxExcelmlPojo(BSession session,
            IntTiers tiers, COContentieux contentieux, COEtape etape, String dateExecution) {

        AdresseJournalContentieuxExcelmlPojo adresseJournalContentieuxExcelmlPojo = new AdresseJournalContentieuxExcelmlPojo();

        try {

            COTiersService tiersService = COServiceLocator.getTiersService();
            TITiers pyTiers = tiersService.loadTiers(session, tiers);

            // Adresse Contentieux
            TIAdresseDataSource adresseDataSourceContentieux = pyTiers.getAdresseAsDataSource(
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, IConstantes.CS_APPLICATION_CONTENTIEUX, contentieux
                            .getCompteAnnexe().getIdExterneRole(), dateExecution, false, null);
            if (adresseDataSourceContentieux == null) {
                adresseDataSourceContentieux = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_CONTENTIEUX, dateExecution, false);
            }
            if (adresseDataSourceContentieux != null) {

                String adresseContentieux = COAdresseUtils.getAdresseContentieuxStringFromDataSource(
                        adresseDataSourceContentieux, session, tiers, tiersService, contentieux, etape, dateExecution);
                String cantonAdresseContentieux = adresseDataSourceContentieux.canton;

                adresseJournalContentieuxExcelmlPojo.setAdresseContentieux(adresseContentieux);
                adresseJournalContentieuxExcelmlPojo.setCantonAdresseContentieux(cantonAdresseContentieux);
            }

            // Adresse Domicile
            TIAdresseDataSource adresseDataSourceDomicile = COAdresseUtils.getAdresseDomicileData(session, tiers,
                    tiersService, contentieux, dateExecution, null);

            if (adresseDataSourceDomicile != null) {

                String adresseDomicile = COAdresseUtils.getAdresseContentieuxStringFromDataSource(
                        adresseDataSourceDomicile, session, tiers, tiersService, contentieux, etape, dateExecution);
                String cantonAdresseDomicile = adresseDataSourceDomicile.canton;

                adresseJournalContentieuxExcelmlPojo.setAdresseDomicile(adresseDomicile);
                adresseJournalContentieuxExcelmlPojo.setCantonAdresseDomicile(cantonAdresseDomicile);
            }

            // Adresse de Courrier
            TIAdresseDataSource adresseDataSourceCourrier = pyTiers.getAdresseAsDataSource(
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, IConstantes.CS_APPLICATION_DEFAUT, contentieux
                            .getCompteAnnexe().getIdExterneRole(), dateExecution, false, null);

            if (adresseDataSourceCourrier == null) {
                adresseDataSourceCourrier = pyTiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_DEFAUT, dateExecution, false);
            }

            if (adresseDataSourceCourrier != null) {

                String adresseCourrier = COAdresseUtils.getAdresseContentieuxStringFromDataSource(
                        adresseDataSourceCourrier, session, tiers, tiersService, contentieux, etape, dateExecution);
                String cantonAdresseCourrier = adresseDataSourceCourrier.canton;

                adresseJournalContentieuxExcelmlPojo.setAdresseCourrier(adresseCourrier);
                adresseJournalContentieuxExcelmlPojo.setCantonAdresseCourrier(cantonAdresseCourrier);
            }

        } catch (Exception e) {
            // Nothing todo
        }

        return adresseJournalContentieuxExcelmlPojo;
    }

    /**
     * @return
     */
    public CommonExcelmlContainer getContainerJournalContentieuxExcelml() {
        return containerJournalContentieuxExcelml;
    }

    /**
     * @return
     */
    public String getDateDocument() {
        return dateDocument;
    }

    /**
     * @return
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * @param theSession
     * @return
     * @throws Exception
     */
    private String getLibellesRolesFromListIdRoles(BSession theSession) throws Exception {

        StringBuilder stringBuilderLibellesRoles = new StringBuilder();

        for (String aIdRole : listIdRoles) {

            if (stringBuilderLibellesRoles.length() >= 1) {
                stringBuilderLibellesRoles.append(", ");
            }

            CARole aRole = new CARole();
            aRole.setSession(theSession);
            aRole.setIdRole(aIdRole);
            aRole.retrieve();

            String roleLibelle = PATraductionHelper.translate(theSession, aRole.getIdTraduction(),
                    theSession.getIdLangueISO());

            stringBuilderLibellesRoles.append(roleLibelle);
        }

        return stringBuilderLibellesRoles.toString();
    }

    /**
     * @return
     */
    public List<String> getListIdRoles() {
        return listIdRoles;
    }

    /**
     * @return
     */
    public boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * @param containerJournalContentieuxExcelml
     */
    public void setContainerJournalContentieuxExcelml(CommonExcelmlContainer containerJournalContentieuxExcelml) {
        this.containerJournalContentieuxExcelml = containerJournalContentieuxExcelml;
    }

    /**
     * @param dateDocument
     */
    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    /**
     * @param dateReference
     */
    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    /**
     * @param listIdRoles
     */
    public void setListIdRoles(List<String> listIdRoles) {
        this.listIdRoles = listIdRoles;
    }

    /**
     * @param modePrevisionnel
     */
    public void setModePrevisionnel(boolean modePrevisionnel) {
        this.modePrevisionnel = modePrevisionnel;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelNameOutput() {
        return modelNameOutput;
    }
}
