package ch.globaz.vulpecula.documents.decompte;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.print.COParameter;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import java.util.HashMap;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.BulletinVersementReference;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.NumeroReferenceFactory;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.documents.catalog.VulpeculaDocumentManager;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.NumeroReference;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.external.models.pyxis.Role;
import com.sun.star.lang.NullPointerException;

/**
 * @author Arnaud Geiser (AGE) | Créé le 3 juin 2014 / Adapté par JPA et SEL 17.06.2014
 * 
 *         Création du document BVR vide pour envoi, lors de la création de décomptes vides.
 */
public class DocumentDecompteBVR extends VulpeculaDocumentManager<DecompteContainer> {

    private static final long serialVersionUID = 2002626138123336066L;


    /**
     * Constructeur
     */
    public DocumentDecompteBVR() throws Exception {
        super();
    }

    /**
     * Constructeur, on l'on passe les décomptes que l'on désire imprimer
     * 
     * @param decompteContainer List<Decompte>
     * @throws Exception
     */
    public DocumentDecompteBVR(final DecompteContainer decompteContainer) throws Exception {
        super(decompteContainer, DocumentConstants.DECOMPTE_BVR_FILENAME, DocumentConstants.DECOMPTE_BVR_TYPE_NUMBER);
        Decompte decompte = decompteContainer.getDecompte();
        if (TypeDecompte.CONTROLE_EMPLOYEUR.equals(decompte.getType()) || decompte.isTraiterAsSpecial()) {
            typeSection = TypeSection.DECOMPTE_COTISATION;
        }
    }

    @Override
    public CaisseHeaderReportBean giveBeanHeader() throws Exception {
        CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();

        Decompte decompte = getCurrentElement().getDecompte();
        Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                decompte.getEmployeurIdTiers());

        beanReport.setAdresse(adresse.getAdresseFormatte());
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
        Decompte decompte = getCurrentElement().getDecompte();

        if (decompte == null) {
            throw new NullPointerException("Le décompte est null !!");
        }

        setParametres("P_COMMENTAIRES", getTexte(2, 1));
        // On remplace les paramètres du catalogue de texte par les données du décompte
        HashMap<String, String> parametres = new HashMap<String, String>();
        parametres.put("noDecompte", decompte.getId());
        parametres.put("periode", decompte.getPeriode().toString());

        String texteFormatte = getTexteFormatte(1, 1, parametres);
        setParametres("P_NO_DECOMPTE", texteFormatte);

        // Evolution QR-Facture
        if (CommonProperties.QR_FACTURE.getBooleanValue()) {
            // -- QR
            qrFacture = new ReferenceQR();
            qrFacture.setSession(getSession());
            // Initialisation des variables du document
            initVariableQR(decompte);
            // Génération du document QR
            qrFacture.setSubReportQR("QR_FACTURE_TEMPLATE_BMS.jasper");
            qrFacture.initQR(this);
        } else {

            fillBVR(decompte.getMontantContributionTotal());
        }

        fillDocumentTitle();
    }

    private void fillDocumentTitle() {
        Decompte decompte = getCurrentElement().getDecompte();
        // BMS-2502 Demande de Mme Dell'Estate le 01.09.2016
        // setDocumentTitle(decompte.getEmployeurAffilieNumero());
        setDocumentTitle(decompte.getEmployeur().getConvention().getCode() + "-" + decompte.getEmployeurAffilieNumero());

    }

    /**
     * Rempli le BVR.
     * 
     * @throws Exception
     */
    private void fillBVR(final Montant montant) throws Exception {
        Decompte decompte = getCurrentElement().getDecompte();
        NumeroReference numRef = NumeroReferenceFactory.createNumeroReference(Role.AFFILIE_PARITAIRE, decompte
                .getEmployeur().getAffilieNumero(), typeSection, decompte.getNumeroDecompte());
        BulletinVersementReference bvr = new BulletinVersementReference(getSession(), numRef);

        try {
            String sAdresse = decompte.getEmployeur().getAdressePrincipale().toString();
            super.setParametres(DocumentConstants.P_VERSE, bvr.getLigneReference() + "\n" + sAdresse);
            super.setParametres(DocumentConstants.P_PAR, sAdresse);
        } catch (Exception e1) {
            getMemoryLog().logMessage("Adresse du décompte manquante" + " : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
        try {
            // Modification suite à QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE_BMS.jasper");

            super.setParametres(DocumentConstants.P_ADRESSE, bvr.getAdresseBVR(getCodeLangue()));
            super.setParametres(DocumentConstants.P_ADRESSECOPY, bvr.getAdresseBVR(getCodeLangue()));
            super.setParametres(DocumentConstants.P_COMPTE, bvr.getNumeroCC());

            super.setParametres(FWIImportParametre.PARAM_REFERENCE, bvr.getLigneReference());
            super.setParametres(DocumentConstants.P_OCR, bvr.getOcrb());
        } catch (Exception e1) {
            getMemoryLog().logMessage("Problème BVR" + " : " + e1.getMessage(), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
        }

        if (!montant.isNegative() && !montant.isZero()) {
            super.setParametres(DocumentConstants.P_FRANC, String.valueOf(montant.getMontantSansCentimes()));
            super.setParametres(DocumentConstants.P_CENTIME, montant.getCentimesWith0());
        } else if (montant.isZero()) {
            super.setParametres(DocumentConstants.P_FRANC, "");
            super.setParametres(DocumentConstants.P_CENTIME, "");
        } else {
            super.setParametres(DocumentConstants.P_FRANC, "XXXXXXXXXXXXXXX");
            super.setParametres(DocumentConstants.P_CENTIME, "XX");
        }
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.DECOMPTE_BVR_SUBJECT;
    }

    /**
     * définit la template a utiliser
     */
    @Override
    public String getJasperTemplate() {
        return DocumentConstants.DECOMPTE_BVR_QR_TEMPLATE;
    }

    @Override
    public CodeLangue getCodeLangue() {
        return getCurrentElement().getDecompte().getEmployeurLangue();
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {
        return DocumentType.DECOMPTE_BVR.getCsCode();
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.DECOMPTE_BVR_CT_NAME;
    }
}
