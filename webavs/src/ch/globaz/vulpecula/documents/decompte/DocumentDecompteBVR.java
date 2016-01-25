package ch.globaz.vulpecula.documents.decompte;

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
 * @author Arnaud Geiser (AGE) | Cr�� le 3 juin 2014 / Adapt� par JPA et SEL 17.06.2014
 * 
 *         Cr�ation du document BVR vide pour envoi, lors de la cr�ation de d�comptes vides.
 */
public class DocumentDecompteBVR extends VulpeculaDocumentManager<DecompteContainer> {

    private static final long serialVersionUID = 2002626138123336066L;
    private TypeSection typeSection = TypeSection.BULLETIN_NEUTRE;

    /**
     * Constructeur
     */
    public DocumentDecompteBVR() throws Exception {
        super();
    }

    /**
     * Constructeur, on l'on passe les d�comptes que l'on d�sire imprimer
     * 
     * @param decomptes List<Decompte>
     * @throws Exception
     */
    public DocumentDecompteBVR(final DecompteContainer decompteContainer) throws Exception {
        super(decompteContainer, DocumentConstants.DECOMPTE_BVR_FILENAME, DocumentConstants.DECOMPTE_BVR_TYPE_NUMBER);
        Decompte decompte = decompteContainer.getDecompte();
        if (TypeDecompte.CONTROLE_EMPLOYEUR.equals(decompte.getType())
                || TypeDecompte.SPECIAL.equals(decompte.getType())) {
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
     * Remplissage du document d'apr�s le d�compte
     */
    @Override
    public void fillFields() throws Exception {
        Decompte decompte = getCurrentElement().getDecompte();

        if (decompte == null) {
            throw new NullPointerException("Le d�compte est null !!");
        }

        setParametres("P_COMMENTAIRES", getTexte(2, 1));
        // On remplace les param�tres du catalogue de texte par les donn�es du d�compte
        HashMap<String, String> parametres = new HashMap<String, String>();
        parametres.put("noDecompte", decompte.getId());
        parametres.put("periode", decompte.getPeriode().toString());

        String texteFormatte = getTexteFormatte(1, 1, parametres);
        setParametres("P_NO_DECOMPTE", texteFormatte);

        fillBVR(decompte.getMontantContributionTotal());

        fillDocumentTitle();
    }

    private void fillDocumentTitle() {
        Decompte decompte = getCurrentElement().getDecompte();
        setDocumentTitle(decompte.getEmployeurAffilieNumero());

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
            getMemoryLog().logMessage("Adresse du d�compte manquante" + " : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
        try {
            super.setParametres(DocumentConstants.P_ADRESSE, bvr.getAdresseBVR(getCodeLangue()));
            super.setParametres(DocumentConstants.P_ADRESSECOPY, bvr.getAdresseBVR(getCodeLangue()));
            super.setParametres(DocumentConstants.P_COMPTE, bvr.getNumeroCC());

            super.setParametres(FWIImportParametre.PARAM_REFERENCE, bvr.getLigneReference());
            super.setParametres(DocumentConstants.P_OCR, bvr.getOcrb());
        } catch (Exception e1) {
            getMemoryLog().logMessage("Probl�me BVR" + " : " + e1.getMessage(), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
        }

        if (!montant.isNegative() && !montant.isZero()) {
            super.setParametres(DocumentConstants.P_FRANC, String.valueOf(montant.getMontantSansCentimes()));
            super.setParametres(DocumentConstants.P_CENTIME, String.valueOf(montant.getCentimes()));
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
     * d�finit la template a utiliser
     */
    @Override
    public String getJasperTemplate() {
        return DocumentConstants.DECOMPTE_BVR_TEMPLATE;
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
