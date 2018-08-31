package ch.globaz.vulpecula.documents.ap;

import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BApplication;
import globaz.globall.util.JACCP;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.BulletinVersementReference;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.NumeroReferenceFactory;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.association.AssociationFacture;
import ch.globaz.vulpecula.domain.models.association.CotisationFacture;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.NumeroReference;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.registre.TypeParamCotisationAP;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.osiris.OrganeExecution;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.AvoirAdressePaiement;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import ch.globaz.vulpecula.external.models.pyxis.Role;
import ch.horizon.jaspe.util.JANumberFormatter;

public class DocumentFactureAP extends VulpeculaDocumentManager<FactureAssociation> {
    private static final long serialVersionUID = 3890679021855028777L;

    private static final String P_SUBREPORT_HEADER = "P_SUBREPORT_HEADER";
    private static final String ASSOCIATION_GENRE_CPP = "68900007";
    private static final String F_ASSOCIATION_LIBELLE = "F_ASSOCIATION_LIBELLE";
    private static final String F_COTISATION_LIBELLE = "F_COTISATION_LIBELLE";
    private static final String F_COTISATION_TOTAL_MONTANT = "F_COTISATION_TOTAL_MONTANT";
    private static final String F_ASSOCIATION_TOTAL_LIBELLE = "F_ASSOCIATION_TOTAL_LIBELLE";
    private static final String F_ASSOCIATION_TOTAL_MONTANT = "F_ASSOCIATION_TOTAL_MONTANT";
    private static final String F_TOTAL_LIBELLE = "F_TOTAL_LIBELLE";
    private static final String F_TOTAL = "F_TOTAL";
    private static final String F_LABEL_COTISATION = "F_LABEL_COTISATION";
    private static final String F_POLITESSE = "F_POLITESSE";
    private static final String F_SIGNATURE = "F_SIGNATURE";

    private static final String F_TITLE = "F_TITLE";
    private static final String F_MASSE = "F_MASSE";
    private static final String F_MONTANT = "F_MONTANT";
    private BApplication application;
    private String descriptionAssociation;
    private String descriptionForBVR;

    public DocumentFactureAP(FactureAssociation facture) throws Exception {
        super(facture, DocumentConstants.FACTURE_AP_NAME, DocumentConstants.FACTURE_AP_TYPE_NUMBER);
        application = getSession().getApplication();
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();
        computeTotalPage();
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {
        return DocumentType.AP.getCsCode();
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return null;
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.FACTURE_AP_TEMPLATE;
    }

    private String findIdTiersAssociation(FactureAssociation facture) {
        for (AssociationFacture assFacture : facture.getAssociations()) {
            return assFacture.getAssociation().getIdTiers();
        }
        return null;
    }

    @Override
    public void fillFields() throws Exception {
        FactureAssociation facture = getCurrentElement();

        setDocumentTitle(facture.getEmployeurAffilieNumero() + " " + facture.getEmployeurRaisonSociale());

        String idTiersAministrationAP;
        if (facture.getModele() == null) {
            idTiersAministrationAP = findIdTiersAssociation(facture);
        } else {
            idTiersAministrationAP = facture.getModele().getIdTiers();
            if (!facture.isMembre()) {
                idTiersAministrationAP = facture.getAssociationParent().getIdTiers();
            } else {
                if (facture.getAssociationParent() != null) {
                    idTiersAministrationAP = facture.getAssociationParent().getIdTiers();
                }
            }
            setParametres(DocumentFactureAP.P_SUBREPORT_HEADER,
                    getHeaderPath(facture.getModele().getJasper(getCodeLangue())));

            // Adresse utilisée pour l'entête
            Adresse adresseCourrier = VulpeculaRepositoryLocator.getAdresseRepository().findAdresseCourrierByIdTiers(
                    idTiersAministrationAP, getCodeLangue());

            if (adresseCourrier != null) {
                setParametres("P_ASSOCIATION", adresseCourrier.getAdresseFormatteWithoutTitre());
            }
        }
        if (!facture.isMembre()) {
            idTiersAministrationAP = facture.getAssociationParent().getIdTiers();
        }
        Collection<Map<String, Object>> data = fillDataSource(facture);
        fillBVR(idTiersAministrationAP);
        fillUserInfo();
        setDataSource(data);
    }

    private void fillUserInfo() {
        setParametres("P_HEADER_NOM_COLLABORATEUR", getSession().getUserFullName());
        setParametres("P_HEADER_TEL_COLLABORATEUR", getSession().getUserInfo().getPhone());
        setParametres("P_HEADER_EMAIL_COLLABORATEUR", getSession().getUserEMail());
    }

    private Collection<Map<String, Object>> fillDataSource(FactureAssociation facture) throws Exception {
        CodeLangue langue = CodeLangue.fromValue(facture.getEmployeurCSLangue());
        String langueIso = langue.getCodeIsoLangue();
        Collection<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (AssociationFacture association : facture.getAssociations()) {
            String description = getLibelleAssociation(facture, association, langue);
            if (descriptionAssociation == null) {
                descriptionAssociation = description;
            }
            if (descriptionForBVR == null) {
                descriptionForBVR = getLibelleAssociationForceParent(facture, association, langue);
            }
            data.add(buildAssociationGroup(description, getLibelleCotisation(facture)));
            for (CotisationFacture cotisation : association.getCotisations()) {
                data.add(buildCotisationGroup(cotisation, langue));
                LigneFactureAssociation ligneFacteur = null;
                LigneFactureAssociation ligneRabais = null;
                for (LigneFactureAssociation ligne : cotisation.getLignes()) {
                    if (ligne.isLigneFacteur()) {
                        ligneFacteur = ligne;
                    } else if (TypeParamCotisationAP.RABAIS.equals(ligne.getTypeParametre())) {
                        ligneRabais = ligne;
                    } else {
                        data.add(buildLigne(ligne));
                    }
                }
                // build ligne facteur
                if (ligneFacteur != null) {
                    data.add(buildLigne(ligneFacteur));
                }
                // build ligne abais
                if (ligneRabais != null) {
                    data.add(buildLigne(ligneRabais));
                }

                data.add(buildCotisationGroupEnd(cotisation));
            }
            buildAssociationReductionGroupEnd(data, association);
            data.add(buildAssociationGroupEnd(association, description, langueIso));
        }
        data.add(buildTotalFacture(facture, langueIso));

        return data;
    }

    private String getLibelleCotisation(FactureAssociation facture) throws Exception {
        if (facture.getAssociationParent().getGenre().equals(ASSOCIATION_GENRE_CPP)) {
            return getLabel("FACTURATION_AP_CONTRIBUTION_LABEL") + " " + facture.getAnnee();
        } else {
            return getLabel("FACTURATION_AP_COTISATION_LABEL") + " " + facture.getAnnee();
        }
    }

    /**
     * @param facture
     * @param association
     * @return
     */
    private String getLibelleAssociation(FactureAssociation facture, AssociationFacture association, CodeLangue langue) {
        String libelle = "";
        if (facture.isMembre()) {

            libelle = association.getAssociation().getDescription(langue);

        } else {
            libelle = facture.getAssociationParent().getDescription(langue);
        }
        return libelle;
    }

    /**
     * @param facture
     * @param association
     * @return
     */
    private String getLibelleAssociationForceParent(FactureAssociation facture, AssociationFacture association,
            CodeLangue langue) {
        String libelle = "";
        if (facture.getAssociationParent() != null) {
            libelle = facture.getAssociationParent().getDescription(langue);
        } else {
            libelle = association.getAssociation().getDescription(langue);
        }
        return libelle;
    }

    private void buildAssociationReductionGroupEnd(Collection<Map<String, Object>> data, AssociationFacture association)
            throws Exception {
        Map<String, Object> dataReduc = new HashMap<String, Object>();
        if (association.isReduction()) {
            dataReduc.put(F_ASSOCIATION_TOTAL_LIBELLE, getLabel("FACTURATION_AP_REDUCTION_ASSOCITAION") + " ("
                    + association.getRabaisAssociation().getValue() + "%)");
            dataReduc.put(F_ASSOCIATION_TOTAL_MONTANT,
                    JANumberFormatter.format(association.getMontantRabais().getValue()));
            data.add(dataReduc);
        }
    }

    private Map<String, Object> buildAssociationGroupEnd(AssociationFacture association, String libelle, String langue) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(F_ASSOCIATION_TOTAL_LIBELLE, application.getLabel("JSP_TOTAL", langue) + " " + libelle);
        data.put(F_ASSOCIATION_TOTAL_MONTANT, JANumberFormatter.format(association.getMontantAssociation().getValue()));
        return data;
    }

    private Map<String, Object> buildTotalFacture(FactureAssociation facture, String langue) throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(F_TOTAL_LIBELLE, application.getLabel("JSP_TOTAL_FACTURE", langue));
        data.put(F_TOTAL, JANumberFormatter.format(facture.getMontantFacture().getValue()));
        data.put(F_POLITESSE, getLabel("FACTURATION_AP_POLITESSE"));
        // Workaround add line to force page break in doc
        String formatedDescirption = descriptionForBVR.concat("\n\n\n\n\n\n\n\n\n");
        data.put(F_SIGNATURE, formatedDescirption);
        return data;
    }

    private Map<String, Object> buildAssociationGroup(String libelleAssociation, String libelleCotisation) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(F_ASSOCIATION_LIBELLE, libelleAssociation);
        data.put(F_LABEL_COTISATION, libelleCotisation);
        return data;
    }

    private Map<String, Object> buildCotisationGroup(CotisationFacture cotisation, CodeLangue langue) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(F_COTISATION_LIBELLE, cotisation.getLibelle(langue));
        return data;
    }

    private Map<String, Object> buildCotisationGroupEnd(CotisationFacture cotisation) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(F_COTISATION_TOTAL_MONTANT, JANumberFormatter.format(cotisation.getMontantTotal().getValue()));
        if (cotisation.isMontantMax()) {
            data.put("F_LABEL_MINMAX", "(Max)");
        }
        if (cotisation.isMontantMin()) {
            data.put("F_LABEL_MINMAX", "(Min)");
        }
        return data;
    }

    private Map<String, Object> buildLigne(LigneFactureAssociation ligne) {
        Map<String, Object> data = new HashMap<String, Object>();
        String title = getTitle(ligne);

        data.put("F_TAUX", ligne.getTauxCotisation().getValue());
        if (ligne.isLigneFacteur()) {
            // on force une valeur afin de faire apparaître la masse pour cotisation dans le document
            data.put("F_TAUX", "1");
        }
        data.put(F_TITLE, title);
        data.put(F_MASSE, JANumberFormatter.format(ligne.getMassePourCotisation().getValue()));
        data.put(F_MONTANT, JANumberFormatter.format(ligne.getMontantCotisation().getValue()));
        return data;
    }

    /**
     * @param ligne
     * @return
     */
    private String getTitle(LigneFactureAssociation ligne) {
        String title = getCodeLibelle(ligne.getTypeParametre().getValue());
        if (!ligne.getTauxCotisation().isZero()) {
            title = title + " (" + ligne.getTauxCotisation().getValue() + "%)";
        }

        if (ligne.getFacteur() != null && ligne.getFacteur() > 0) {
            title = title + " (" + ligne.getFacteur().toString() + ")";
        }
        return title;
    }

    private void fillBVR(String idTiersAministrationAP) throws Exception {
        FactureAssociation facture = getCurrentElement();
        Montant montant = facture.getMontantFacture();

        if (shouldPrintBvr(facture)) {
            try {

                BulletinVersementReference bvr = buildBulletinVersement(idTiersAministrationAP, facture);
                super.setParametres(DocumentConstants.P_ADRESSE, bvr.getAdresseBVR(getCodeLangue()));
                super.setParametres(DocumentConstants.P_ADRESSECOPY, bvr.getAdresseBVR(getCodeLangue()));
                super.setParametres(DocumentConstants.P_COMPTE, bvr.getNumeroCC());
                super.setParametres(FWIImportParametre.PARAM_REFERENCE, bvr.getLigneReference());

                try {
                    super.setParametres(DocumentConstants.P_PAR, facture.getAdresseEmployeur());
                    super.setParametres(DocumentConstants.P_VERSE,
                            bvr.getLigneReference() + "\n" + facture.getAdresseEmployeur());
                } catch (Exception e1) {
                    getMemoryLog().logMessage("Adresse du décompte manquante" + " : " + e1.getMessage(),
                            FWMessage.AVERTISSEMENT, this.getClass().getName());
                }

                if (!montant.isNegative()) {
                    super.setParametres(DocumentConstants.P_OCR, bvr.getOcrb());
                    super.setParametres(DocumentConstants.P_FRANC, String.valueOf(montant.getMontantSansCentimes()));
                    super.setParametres(DocumentConstants.P_CENTIME, montant.getCentimesWith0());
                } else {
                    super.setParametres(DocumentConstants.P_FRANC, "XXXXXXXXXXXXXXX");
                    super.setParametres(DocumentConstants.P_CENTIME, "XX");
                }
            } catch (Exception e1) {
                getMemoryLog().logMessage("Problème BVR" + " : " + e1.getMessage(), FWMessage.AVERTISSEMENT,
                        this.getClass().getName());
            }
        }
    }

    private boolean shouldPrintBvr(FactureAssociation facture) {
        if (JadeStringUtil.isBlankOrZero(facture.getIdPassageFacturation())) {
            return false;
        }
        return true;
    }

    /**
     * @param idTiersAministrationAP
     * @param facture
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private BulletinVersementReference buildBulletinVersement(String idTiersAministrationAP, FactureAssociation facture)
            throws Exception, JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        BulletinVersementReference bvr = null;
        NumeroReference numRef = null;
        String numAdherentBanque = null;
        AdresseTiersDetail adresse = null;
        String ccp = null;

        if (idTiersAministrationAP != null) {
            Contact contact = VulpeculaRepositoryLocator.getContactRepository().findForIdTiersAndNom(
                    idTiersAministrationAP, "CAOREXP");

            if (contact != null) {
                OrganeExecution organeExecution = VulpeculaServiceLocator.getOrganeExecutionService().findById(
                        contact.getPrenom());

                ccp = JACCP.formatWithDash(organeExecution.getNoAdherentBVR());
                numAdherentBanque = organeExecution.getNoAdherent();

                ArrayList<String> liste = new ArrayList<String>();
                liste.add(AvoirAdressePaiement.CS_DOMAINE_FACTURATION_AP);

                adresse = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiersAministrationAP,
                        true, AvoirAdressePaiement.CS_DOMAINE_FACTURATION_AP, Date.now().getSwissValue(), "");
                if (facture.getAssociationParent() != null) {
                    adresse = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                            facture.getAssociationParent().getIdTiers(), true,
                            AvoirAdressePaiement.CS_DOMAINE_FACTURATION_AP, Date.now().getSwissValue(), "");
                }
                if (adresse == null || adresse.getFields() == null) {
                    throw new ViewException(SpecificationMessage.AJ_PERIODE_DEBUT_PLUS_GRANDE_PERIODE_FIN);
                }

                if (!descriptionForBVR.isEmpty() && adresse != null && adresse.getFields() != null) {
                    adresse.getFields().put(AdresseTiersDetail.ADRESSE_VAR_D1, descriptionForBVR);
                    adresse.getFields().put(AdresseTiersDetail.ADRESSE_VAR_D2, "");
                }
            }
        }

        if (facture.isMembre()) {
            numRef = NumeroReferenceFactory.createNumeroReference(Role.ASSOCIATION_PROFESSIONNELLE,
                    facture.getNumeroAffilie(), TypeSection.ASSOCIATION,
                    new NumeroDecompte(facture.getNumeroFacture()), numAdherentBanque);
        } else {
            numRef = NumeroReferenceFactory.createNumeroReference(Role.ASSOCIATION_PROFESSIONNELLE,
                    facture.getNumeroAffilie(), TypeSection.CPP, new NumeroDecompte(facture.getNumeroFacture()),
                    numAdherentBanque);
        }

        bvr = new BulletinVersementReference(numRef, facture.getMontantFacture(), adresse, ccp);
        return bvr;
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
        FactureAssociation facture = getCurrentElement();
        beanReport.setAdresse(formatAdresseToForcePageBreak(facture.getAdresseEmployeur()));
        beanReport.setNomCollaborateur(getSession().getUserFullName());
        beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
        beanReport.setUser(getSession().getUserInfo());

        Date dateDocument = facture.getDateFacturation();
        if (dateDocument == null) {
            dateDocument = Date.now();
        }

        beanReport.setDate(dateDocument.getSwissValue());
        beanReport.setNoAffilie(facture.getNumeroAffilie());
        beanReport.setConfidentiel(true);
        return beanReport;
    }

    private String formatAdresseToForcePageBreak(String adresseFormatee) {
        int nbOfLineToFill = 10;
        int count = StringUtils.countMatches(adresseFormatee, "\n");
        int diff = 0;
        String toReturn = adresseFormatee;
        if (count < nbOfLineToFill) {
            diff = nbOfLineToFill - count;
        }
        for (int i = 0; i < diff; i++) {
            toReturn = toReturn.concat("\n");
        }
        return toReturn;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return CodeLangue.fromValue(getCurrentElement().getEmployeur().getLangue());
    }

    public String getHeaderPath(String header) {
        return Jade.getInstance().getHomeDir() + "vulpeculaRoot/model/ap/" + header;
    }

}
