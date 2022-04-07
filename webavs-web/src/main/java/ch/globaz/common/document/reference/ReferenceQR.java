package ch.globaz.common.document.reference;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.GenerationQRCode;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import globaz.aquila.print.COParameter;
import globaz.babel.api.ICTListeTextes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JABVR;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.i18n.JadeI18n;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.api.APISection;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@Slf4j
public class ReferenceQR extends AbstractReference {

    public static final String DEVISE_DEFAUT = "CHF";
    private static final String CHAR_FIN_LIGNE = "\r\n";
    private static final String CHAR_FIN_LIGNE_DEB = "\n";
    private static final String CODE_PAYS_DEFAUT = "CH";
    private static final String ESPACE = " ";
    private static final String LANGUE_PAR_DEFAUT = "FR";

    private String subReportQR = "QR_FACTURE_TEMPLATE.jasper";
    private String subReportQRCurrentPage = "QR_FACTURE_TEMPLATE_CURRENT_PAGE.jasper";

    private static final String QR_IBAN = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "type.reference.qr.iban");
    private static final String IBAN = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "type.reference.iban");
    private static final String SANS_REF = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "type.sans.reference");
    private static final String END_OF_PAYMENT = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "end.of.payment");
    private static final String QR_TYPE = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "type.qr");
    private static final String DEFAULT_VERSION = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "default.version");
    private static final String CODING_TYPE = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "coding.type");
    public static final String COMBINE = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "adresse.combine");
    public static final String STRUCTURE = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "adresse.structure");

    private String adresseCopy = StringUtils.EMPTY;
    private String adresseDebiteur = StringUtils.EMPTY;
    private String compte = StringUtils.EMPTY;
    private String reference = StringUtils.EMPTY;
    private String montantSansCentimes = StringUtils.EMPTY;
    private String montantCentimes = StringUtils.EMPTY;
    private String nomAV1 = StringUtils.EMPTY;
    private String nomAV2 = StringUtils.EMPTY;
    private String infosAdditionnelles = StringUtils.EMPTY;
    private String pInfoAddErreur = StringUtils.EMPTY;

    private String qrType = QR_TYPE;
    private String version = DEFAULT_VERSION;
    private String codingType = CODING_TYPE;
    private String creNom = StringUtils.EMPTY;
    private String creAdressTyp = StringUtils.EMPTY;
    private String creRueOuLigneAdresse1 = StringUtils.EMPTY;
    private String creNumMaisonOuLigneAdresse2 = StringUtils.EMPTY;
    private String creCodePostal = StringUtils.EMPTY;
    private String creLieu = StringUtils.EMPTY;
    private String crePays = StringUtils.EMPTY;

    // Non utilis� pour le moment - Pour une utilisation future.
    private String crefNom = StringUtils.EMPTY;
    private String crefAdressTyp = StringUtils.EMPTY;
    private String crefRueOuLigneAdresse1 = StringUtils.EMPTY;
    private String crefNumMaisonOuLigneAdresse2 = StringUtils.EMPTY;
    private String crefCodePostal = StringUtils.EMPTY;
    private String crefLieu = StringUtils.EMPTY;
    private String crefPays = StringUtils.EMPTY;

    private String montant = StringUtils.EMPTY;
    private String monnaie = StringUtils.EMPTY;
    private String debfNom = StringUtils.EMPTY;
    private String debfAdressTyp = StringUtils.EMPTY;
    private String debfRueOuLigneAdresse1 = StringUtils.EMPTY;
    private String debfNumMaisonOuLigneAdresse2 = StringUtils.EMPTY;
    private String debfCodePostal = StringUtils.EMPTY;
    private String debfLieu = StringUtils.EMPTY;
    private String debfPays = StringUtils.EMPTY;
    private String typeReference = StringUtils.EMPTY;
    private String communicationNonStructuree = StringUtils.EMPTY;
    private String trailer = END_OF_PAYMENT;
    private String infoFacture = StringUtils.EMPTY;
    private String pa1Param = StringUtils.EMPTY;
    private String pa2Param = StringUtils.EMPTY;
    private String UID = StringUtils.EMPTY;

    // Boolean qui permet d'activer un QR Neutre
    private boolean qrNeutre = false;
    private boolean montantMinimeOuMontantReporter = false;
    private boolean recouvrementDirect = false;

    private Map<String, String> parameters = new HashMap<>();

    // Pour le moment le QR Code est d�fini en String. Sera modifi� par la suite
    private String qRCode;

    // Param de langue du document
    private String langueDoc = LANGUE_PAR_DEFAUT;

    public ReferenceQR() {
        super();
    }

    public FWIDocumentManager initQR(FWIDocumentManager document, List<ReferenceQR> qrFactures) {

        // Initialisation des ent�tes de la QR-Facture
        initEnteteQR(document);

        // Initialiation d'un UID pour la referenceQR pour le nom de l'image QR
        initUID(document.getDocumentInfo().getDocumentUID());

        // Initialisation des params de la QR-Facture
        initParamQR();

        for (Map.Entry element : parameters.entrySet()) {
            document.setParametres(element.getKey(), Objects.isNull(element.getValue()) ? "" : element.getValue());
        }

        // Ajoute la r�f�renceQR dans la liste des QR g�n�r�es
        qrFactures.add(this);

        return document;
    }

    /**
     * M�thode qui g�n�re un uid pour la referenceQR
     */
    public void initUID(String documentUID) {
        this.setUID(documentUID + "_" + new Date().getTime());
    }

    /**
     * M�thode qui charge les ent�tes du documents QR-Facture
     *
     * @param document
     */
    private void initEnteteQR(FWIDocumentManager document) {

        try {
            parameters.put(COParameter.P_SUBREPORT_QR, document.getImporter().getImportPath() + subReportQR);

            // QR sur current page. Pour le moment on laisse le QR avec toutes les informations
            parameters.put(COParameter.P_SUBREPORT_QR_CURRENT_PAGE, document.getImporter().getImportPath() + subReportQR);
            parameters.put(COParameter.P_TITRE_1, getSession().getApplication().getLabel("QR_RECEPISSE", langueDoc));
            parameters.put(COParameter.P_TITRE_2, getSession().getApplication().getLabel("QR_SECTION_PAIEMENT", langueDoc));
            parameters.put(COParameter.P_POINT_DEPOT, getSession().getApplication().getLabel("QR_POINT_DEPOT", langueDoc));
            parameters.put(COParameter.P_MONNAIE_TITRE, getSession().getApplication().getLabel("QR_MONNAIE", langueDoc));
            parameters.put(COParameter.P_MONTANT_TITRE, getSession().getApplication().getLabel("MONTANT", langueDoc));
            parameters.put(COParameter.P_SUBREPORT_ZONE_INDICATIONS, document.getImporter().getImportPath() + "QR_CODE_ZONE_INDICATIONS.jasper");
            parameters.put(COParameter.P_SUBREPORT_RECEPISE, document.getImporter().getImportPath() + "QR_CODE_RECEPISE.jasper");
            parameters.put(COParameter.P_COMPTE_TITRE, getSession().getApplication().getLabel("QR_COMPTE_PAYABLE", langueDoc));
            parameters.put(COParameter.P_PAR_TITRE, getSession().getApplication().getLabel("QR_PAYABLE_PAR", langueDoc));
            parameters.put(COParameter.P_REF_TITRE, getSession().getApplication().getLabel("REFERENCE", langueDoc));
            parameters.put(COParameter.P_INFO_ADD_TITRE, getSession().getApplication().getLabel("QR_INFO_SUPP", langueDoc));

            pInfoAddErreur = getSession().getApplication().getLabel("INFO_ADD_ERREUR", langueDoc);
            parameters.put(COParameter.P_INFO_ADD_ERREUR, pInfoAddErreur);
        } catch (Exception e) {
            throw new CommonTechnicalException("Probl�me � l'initialisation des ent�tes QR : ", e);
        }
    }

    /**
     * M�thode qui charge les param�tres variables de la QR-Facture
     */
    public void initParamQR() {
        parameters.put(COParameter.P_QR_CODE_PATH, GenerationQRCode.generateSwissQrCode(this, generationPayLoad()));
        // Activation ou non des traitill�s et ciseaux sur Qr Facture
        if (addTraitille()) {
            parameters.put(COParameter.P_CISEAU_VERTICAL_PATH, getDefaultModelPath() + "/ciseau_vertical.jpg");
            parameters.put(COParameter.P_CISEAU_HORIZ_PATH, getDefaultModelPath() + "/ciseau_horiz.png");
        }

        parameters.put(COParameter.P_MONNAIE, monnaie);

        // Si l'on est sur un QR Neutre, dans ce cas, il doit �tre sans montant.
        if (!qrNeutre) {
            if (isNotUseForPaiement()) {
                parameters.put(COParameter.P_MONTANT, "0.00");
                parameters.put(COParameter.P_INFO_ADD, (pInfoAddErreur + RETOUR_LIGNE + communicationNonStructuree + RETOUR_LIGNE + infoFacture).trim());
            } else {
                parameters.put(COParameter.P_MONTANT, JANumberFormatter.format(montant));
                parameters.put(COParameter.P_INFO_ADD, (communicationNonStructuree + RETOUR_LIGNE + infoFacture).trim());
            }
        } else {
            parameters.put(COParameter.P_INFO_ADD, (communicationNonStructuree + RETOUR_LIGNE + infoFacture).trim());
        }

        parameters.put(FWIImportParametre.PARAM_REFERENCE, getReference());
        parameters.put(COParameter.P_PARAM_1, pa1Param);
        parameters.put(COParameter.P_PARAM_2, pa2Param);
        if (!COMBINE.equals(creAdressTyp)) {
            parameters.put(COParameter.P_COMPTE, (compte + RETOUR_LIGNE + creNom + RETOUR_LIGNE + creRueOuLigneAdresse1 + ESPACE + creNumMaisonOuLigneAdresse2 + RETOUR_LIGNE + creCodePostal + ESPACE + creLieu).trim());
        } else {
            parameters.put(COParameter.P_COMPTE, (compte + RETOUR_LIGNE + creNom + creRueOuLigneAdresse1 + RETOUR_LIGNE + creNumMaisonOuLigneAdresse2).trim());
        }
        if (!COMBINE.equals(debfAdressTyp)) {
            parameters.put(COParameter.P_PAR, (debfNom + RETOUR_LIGNE + debfRueOuLigneAdresse1 + ESPACE + debfNumMaisonOuLigneAdresse2 + RETOUR_LIGNE + debfCodePostal + ESPACE + debfLieu).trim());
        } else {
            parameters.put(COParameter.P_PAR, (debfNom + RETOUR_LIGNE + debfRueOuLigneAdresse1 + RETOUR_LIGNE + debfNumMaisonOuLigneAdresse2).trim());
        }


    }

    /**
     * M�thode qui va v�rifier le param�tre d'activation des traitill�s sur QrFacture
     *
     * @return boolean
     */
    private boolean addTraitille() {
        try {
            return CommonProperties.ACTIVATION_TRAITILLE_QR_FACTURE.getBooleanValue();
        } catch (PropertiesException e) {
            LOG.error("ReferenceQR#addTraitille() - Propertie common.qrFacture.traitille manquante.", e);
            return false;
        }
    }

    private String generationPayLoad() {
        StringBuilder builder = new StringBuilder();

        builder.append(qrType).append(CHAR_FIN_LIGNE);
        builder.append(version).append(CHAR_FIN_LIGNE);
        builder.append(codingType).append(CHAR_FIN_LIGNE);
        builder.append(getCompteWithoutSpace()).append(CHAR_FIN_LIGNE);

        builder.append(creAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(creNom.replace(CHAR_FIN_LIGNE, ESPACE)).append(CHAR_FIN_LIGNE);
        builder.append(creRueOuLigneAdresse1.replace(CHAR_FIN_LIGNE, ESPACE)).append(CHAR_FIN_LIGNE);
        builder.append(creNumMaisonOuLigneAdresse2.replace(CHAR_FIN_LIGNE, ESPACE)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(creAdressTyp, COMBINE) ? StringUtils.EMPTY : creCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(creAdressTyp, COMBINE) ? StringUtils.EMPTY : creLieu)).append(CHAR_FIN_LIGNE);
        builder.append(Objects.isNull(getCrePays()) ? CODE_PAYS_DEFAUT : getCrePaysVar()).append(CHAR_FIN_LIGNE);

        builder.append(crefAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(crefNom.isEmpty() ? crefRueOuLigneAdresse1.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ") : crefNom).append(CHAR_FIN_LIGNE);
        builder.append(crefNom.isEmpty() ? "" : crefRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
        builder.append(crefNumMaisonOuLigneAdresse2.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ")).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(crefAdressTyp, COMBINE) ? StringUtils.EMPTY : crefCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(crefAdressTyp, COMBINE) ? StringUtils.EMPTY : crefLieu)).append(CHAR_FIN_LIGNE);
        if (crefNom.isEmpty()) {
            builder.append(CHAR_FIN_LIGNE);
        } else {
            builder.append(Objects.isNull(getCrefPays()) ? CODE_PAYS_DEFAUT : getCrefPays()).append(CHAR_FIN_LIGNE);
        }

        // Dans le cadre d'un bulletin neutre, on ne renseigne pas de montant.
        if (qrNeutre) {
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
        } else if (new Montant(montant).isNegative() || montantMinimeOuMontantReporter || recouvrementDirect) {
            builder.append("0.00").append(CHAR_FIN_LIGNE);
        } else {
            builder.append(montant).append(CHAR_FIN_LIGNE);
        }
        builder.append(monnaie).append(CHAR_FIN_LIGNE);
        builder.append(debfAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(debfNom.isEmpty() ? debfRueOuLigneAdresse1.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ") : debfNom).append(CHAR_FIN_LIGNE);
        builder.append(debfNom.isEmpty() ? "" : debfRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
        builder.append(debfNumMaisonOuLigneAdresse2.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ")).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(debfAdressTyp, COMBINE) ? StringUtils.EMPTY : debfCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(debfAdressTyp, COMBINE) ? StringUtils.EMPTY : debfLieu)).append(CHAR_FIN_LIGNE);
        builder.append(Objects.isNull(getDebfPays()) ? CODE_PAYS_DEFAUT : getDebfPays()).append(CHAR_FIN_LIGNE);

        // R�f�rence paiement
        builder.append(typeReference).append(CHAR_FIN_LIGNE);
        builder.append(getReferenceWithoutSpace()).append(CHAR_FIN_LIGNE);

        // Info Supp
        if (!new Montant(montant).isPositive() || montantMinimeOuMontantReporter || recouvrementDirect) {
            builder.append(pInfoAddErreur + " " + communicationNonStructuree).append(CHAR_FIN_LIGNE);
        } else {
            builder.append(communicationNonStructuree).append(CHAR_FIN_LIGNE);
        }

        builder.append(trailer);
        if (!StringUtils.isEmpty(infoFacture)) {
            builder.append(CHAR_FIN_LIGNE).append(infoFacture);
        }
        // Proc�dure alternative
        if (!StringUtils.isEmpty(pa1Param)) {
            builder.append(CHAR_FIN_LIGNE).append(pa1Param);
        }
        if (!StringUtils.isEmpty(pa2Param)) {
            builder.append(CHAR_FIN_LIGNE).append(pa2Param);
        }

        return builder.toString();
    }

    /**
     * On contr�le si la QR facture doit �tre utilis� pour le paiement ou non. R�gle pour ne pas l'utiliser:
     * - montant non positif
     * - montant minime ou reporter
     * - recouvrement direct
     *
     * @return true si on ne doit pas utiliser cette QR facture pour le paiement.
     */
    private boolean isNotUseForPaiement() {
        return !new Montant(montant).isPositive() || montantMinimeOuMontantReporter || recouvrementDirect;
    }

    /**
     * M�thode de g�n�ration de la r�f�rence QR pour Contentieux depuis la section
     * <p>
     *
     * @param section la section
     */
    public void genererReferenceQR(APISection section) throws CATechnicalException {
        genererReferenceQR(section.getCompteAnnexe().getIdRole(), section.getCompteAnnexe().getIdExterneRole(), false, section.getIdTypeSection()
                , section.getIdExterne(), section.getIdCompteAnnexe(), section.getSolde());

    }

    /**
     * M�thode de g�n�ration de la r�f�rence QR
     *
     * @param idRole
     * @param idExterneRole
     * @param isPlanPaiement
     * @param idTypeSection
     * @param idExterne
     * @param idCompteAnnexe
     */
    public void genererReferenceQR(String idRole, String idExterneRole, Boolean isPlanPaiement, String idTypeSection, String idExterne, String idCompteAnnexe, String solde) throws CATechnicalException {
        String refQR;
        try {
            refQR = genererNumReference(idRole, idExterneRole, isPlanPaiement, idTypeSection, idExterne, idCompteAnnexe);
        } catch (Exception e) {
            throw new CATechnicalException("Erreur � la g�n�ration de la r�f�rence QR :", e);
        }

        if (JadeStringUtil.isDecimalEmpty(montant)) {
            montant = solde;
        }

        genererReference(refQR, montant);

        if (StringUtils.isEmpty(reference) || montantMinimeOuMontantReporter || recouvrementDirect) {
            setReference(REFERENCE_NON_FACTURABLE);
            this.typeReference = SANS_REF;
        } else {
            this.typeReference = QR_IBAN;
        }

    }

    /**
     * G�n�ration de la r�f�rence
     *
     * @param reference
     * @param montant
     * @throws CATechnicalException
     */

    public void genererReference(String reference, String montant) throws CATechnicalException {
        if (new FWCurrency(montant).isPositive() || qrNeutre) {
            try {
                JABVR bvr = new JABVR(montant, reference, getNoAdherent());
                setReference(bvr.get_ligneReference());
            } catch (Exception e) {
                throw new CATechnicalException("Erreur lors de la r�cup�ration du N� Adh�rent :", e);
            }
        }
    }

    /**
     * M�thode de g�n�ration de la r�f�rence QR pour Facturation
     * <p>
     *
     * @param enteteFacture l'ent�te facture
     */
    public void genererReferenceQRFact(FAEnteteFacture enteteFacture) throws Exception {
        String refQR = genererNumReference(enteteFacture.getIdRole(), enteteFacture.getIdExterneRole(), false,
                JadeStringUtil.rightJustifyInteger(enteteFacture.getIdTypeFacture(), 2), enteteFacture.getIdExterneFacture(), null);

        JABVR bvr = null;

        if ((new FWCurrency(enteteFacture.getTotalFacture()).isPositive()
                && !recouvrementDirect)
                || (Objects.nonNull(enteteFacture) && APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(enteteFacture.getIdTypeFacture()))) {
            bvr = new JABVR(JANumberFormatter.deQuote(enteteFacture.getTotalFacture()), refQR, getNoAdherent());
        }

        if (!(new FWCurrency(enteteFacture.getTotalFacture()).isZero() || montantMinimeOuMontantReporter)
                && (bvr != null)) {
            if (!recouvrementDirect) {
                setReference(bvr.get_ligneReference());
                this.typeReference = QR_IBAN;
            } else {
                setReference(REFERENCE_NON_FACTURABLE);
                this.typeReference = SANS_REF;
            }
        } else {
            setReference(REFERENCE_NON_FACTURABLE);
            this.typeReference = SANS_REF;

        }

        if (qrNeutre && (bvr != null)) {
            setReference(bvr.get_ligneReference());
            this.typeReference = QR_IBAN;
        }
    }

    /**
     * M�thode qui g�n�re l'adresse du d�biteur depuis la DB
     * <p>
     * Si l'adresse est trouv� (unique) alors renvoi true et charge les param�tres
     *
     * @param idTiers
     * @return Boolean
     * @throws Exception
     */
    public Boolean genererAdresseDebiteur(String idTiers, String typeAdresse, String domaineCourrier, String idExterneRole, boolean herite, String date) throws Exception {

        TITiers tiers = getTiers(idTiers);
        if (Objects.isNull(tiers)) {
            return false;
        }
        TIAdresseDataSource adresse = tiers.getAdresseAsDataSource(typeAdresse, domaineCourrier, idExterneRole, date, herite, getLangueDoc());


        if (Objects.nonNull(adresse)) {
            // Chargement des donn�es d�biteurs sur le QR
            debfAdressTyp = STRUCTURE;
            debfNom = adresse.fullLigne1;
            debfPays = adresse.paysIso;
            debfCodePostal = adresse.localiteNpa;
            debfLieu = adresse.localiteNom;
            debfRueOuLigneAdresse1 = adresse.rue;
            debfNumMaisonOuLigneAdresse2 = adresse.numeroRue;
            return true;
        } else {
            return false;
        }
    }

    public void recupererIban() throws Exception {
        this.compte = this.getNumeroCC(getLangueDoc());
    }


    /**
     * M�thode qui va setter l'adresse cr�diteur
     * depuis une adresse combin�e
     */
    public void genererCreAdresse() {

        String[] adresseSplit = getAdresse(getLangueDoc()).split("\r\n");

        this.creAdressTyp = COMBINE;

        StringBuilder nomCreancierComplet = new StringBuilder();

        for (int i = 0; i < adresseSplit.length; i++) {
            if (i == (adresseSplit.length - 1)) {
                this.setCreNumMaisonOuLigneAdresse2(adresseSplit[i]);
            } else if (i == (adresseSplit.length - 2)) {
                this.setCreRueOuLigneAdresse1(adresseSplit[i]);
            } else {
                nomCreancierComplet.append(adresseSplit[i] + "\r\n");
            }
        }
        this.setCreNom(nomCreancierComplet.toString());
    }

    public String getAdresseCopy() {
        return adresseCopy;
    }

    public void setAdresseCopy(String adresseCopy) {
        this.adresseCopy = adresseCopy;
    }

    public String getAdresseDebiteur() {
        return adresseDebiteur;
    }

    public void setAdresseDebiteur(String adresseDebiteur) {
        this.adresseDebiteur = adresseDebiteur;
    }

    public String getCompte() {
        return compte;
    }

    public String getCompteWithoutSpace() {
        return compte.replaceAll("\\s", "");
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public String getReferenceWithoutSpace() {
        return reference.replaceAll("\\s", "");
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMontantSansCentimes() {
        return montantSansCentimes;
    }

    public void setMontantSansCentimes(String montantSansCentimes) {
        this.montantSansCentimes = montantSansCentimes;
    }

    public String getMontantCentimes() {
        return montantCentimes;
    }

    public void setMontantCentimes(String montantCentimes) {
        this.montantCentimes = montantCentimes;
    }

    public String getNomAV1() {
        return nomAV1;
    }

    public void setNomAV1(String nomAV1) {
        this.nomAV1 = nomAV1;
    }

    public String getNomAV2() {
        return nomAV2;
    }

    public void setNomAV2(String nomAV2) {
        this.nomAV2 = nomAV2;
    }

    public String getInfosAdditionnelles() {
        return infosAdditionnelles;
    }

    public void setInfosAdditionnelles(String infosAdditionnelles) {
        this.infosAdditionnelles = infosAdditionnelles;
    }

    public String getqRCode() {
        return qRCode;
    }

    public void setqRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void addParametersValue(String key, String value) {
        parameters.put(key, value);
    }

    public String getQrType() {
        return qrType;
    }

    public void setQrType(String qrType) {
        this.qrType = qrType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCodingType() {
        return codingType;
    }

    public void setCodingType(String codingType) {
        this.codingType = codingType;
    }

    public String getCreNom() {
        if (creNom.length() > 70) {
            return creNom.substring(0, 70);
        } else return creNom;
    }

    public void setCreNom(String creNom) {
        // Limitation du nombre de caract�re � 70 ( doc officielle)
        if (creNom.length() > 70) {
            this.creNom = creNom.substring(0, 70);
        } else this.creNom = creNom;
    }

    public String getCreAdressTyp() {
        return creAdressTyp;
    }

    public void setCreAdressTyp(String creAdressTyp) {
        this.creAdressTyp = creAdressTyp;
    }

    public String getCreRueOuLigneAdresse1() {
        // Limitation du nombre de caract�re � 70 ( doc officielle)
        if (creRueOuLigneAdresse1.length() > 70) {
            return creRueOuLigneAdresse1.substring(0, 70);
        } else return creRueOuLigneAdresse1;
    }

    public void setCreRueOuLigneAdresse1(String creRueOuLigneAdresse1) {
        // Limitation du nombre de caract�re � 70 ( doc officielle)
        if (creRueOuLigneAdresse1.length() > 70) {
            this.creRueOuLigneAdresse1 = creRueOuLigneAdresse1.substring(0, 70);
        } else this.creRueOuLigneAdresse1 = creRueOuLigneAdresse1;
    }

    public String getCreNumMaisonOuLigneAdresse2() {
        if (getCreAdressTyp().equalsIgnoreCase(STRUCTURE)) {
            // Limitation du nombre de caract�re � 16 pour les adresses structur�es ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 16) {
                return creNumMaisonOuLigneAdresse2.substring(0, 16);
            } else return creNumMaisonOuLigneAdresse2;
        } else {
            // Limitation du nombre de caract�re � 70 ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 70) {
                return creNumMaisonOuLigneAdresse2.substring(0, 70);
            } else return creNumMaisonOuLigneAdresse2;
        }

    }

    public void setCreNumMaisonOuLigneAdresse2(String creNumMaisonOuLigneAdresse2) {
        if (getCreAdressTyp().equalsIgnoreCase(STRUCTURE)) {
            // Limitation du nombre de caract�re � 16 ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 16) {
                this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2.substring(0, 16);
            } else this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2;
        } else {
            // Limitation du nombre de caract�re � 70 ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 70) {
                this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2.substring(0, 70);
            } else this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2;
        }
    }

    public String getCreCodePostal() {
        // Limitation du nombre de caract�re � 16 ( doc officielle)
        if (creCodePostal.length() > 16) {
            return creCodePostal.substring(0, 16);
        } else return creCodePostal;
    }

    public void setCreCodePostal(String creCodePostal) {
        // Limitation du nombre de caract�re � 16 ( doc officielle)
        if (creCodePostal.length() > 16) {
            this.creCodePostal = creCodePostal.substring(0, 16);
        } else this.creCodePostal = creCodePostal;
    }

    public String getCreLieu() {
        // Limitation du nombre de caract�re � 35 ( doc officielle)
        if (creLieu.length() > 35) {
            return creLieu.substring(0, 35);
        } else return creLieu;
    }

    public void setCreLieu(String creLieu) {
        // Limitation du nombre de caract�re � 35 ( doc officielle)
        if (creLieu.length() > 35) {
            this.creLieu = creLieu.substring(0, 35);
        } else this.creLieu = creLieu;

    }

    public String getCrePays() {
        try {
            return getCodePays();
        } catch (Exception e) {
            // Le code pays n'a pas �t� trouv�
            return null;
        }
    }

    public String getCrePaysVar() {
        if (crePays.isEmpty()) {
            return CODE_PAYS_DEFAUT;
        }
        return crePays;
    }

    public void setCrePays(String crePays) {
        this.crePays = crePays;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public String getDebfNom() {
        // Limitation du nombre de caract�re � 70 ( doc officielle)
        if (debfNom.length() > 70) {
            return debfNom.substring(0, 70);
        } else return debfNom;
    }

    public void setDebfNom(String debfNom) {
        if (debfNom.length() > 70) {
            this.debfNom = debfNom.substring(0, 70);
        } else this.debfNom = debfNom;
    }

    public String getDebfAdressTyp() {
        return debfAdressTyp;
    }

    public void setDebfAdressTyp(String dbefAdressTyp) {
        this.debfAdressTyp = dbefAdressTyp;
    }

    public String getDebfRueOuLigneAdresse1() {
        // Limitation du nombre de caract�re � 70 ( doc officielle)
        if (debfRueOuLigneAdresse1.length() > 70) {
            return debfRueOuLigneAdresse1.substring(0, 70);
        } else return debfRueOuLigneAdresse1;
    }

    public void setDebfRueOuLigneAdresse1(String debfRueOuLigneAdresse1) {
        // Limitation du nombre de caract�re � 70 ( doc officielle)
        if (debfRueOuLigneAdresse1.length() > 70) {
            this.debfRueOuLigneAdresse1 = debfRueOuLigneAdresse1.substring(0, 70);
        } else this.debfRueOuLigneAdresse1 = debfRueOuLigneAdresse1;
    }

    public String getDebfNumMaisonOuLigneAdresse2() {
        if (getCreAdressTyp().equalsIgnoreCase(STRUCTURE)) {
            // Limitation du nombre de caract�re � 16 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 16) {
                return debfNumMaisonOuLigneAdresse2.substring(0, 16);
            } else return debfNumMaisonOuLigneAdresse2;
        } else {
            // Limitation du nombre de caract�re � 70 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 70) {
                return debfNumMaisonOuLigneAdresse2.substring(0, 70);
            } else return debfNumMaisonOuLigneAdresse2;
        }
    }

    public void setDebfNumMaisonOuLigneAdresse2(String debfNumMaisonOuLigneAdresse2) {
        if (getCreAdressTyp().equalsIgnoreCase(STRUCTURE)) {
            // Limitation du nombre de caract�re � 16 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 16) {
                this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2.substring(0, 16);
            } else this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2;
        } else {
            int indexDepart = 0;
            if (debfNumMaisonOuLigneAdresse2.indexOf('\n') != -1 && debfNumMaisonOuLigneAdresse2.indexOf('\n') == 0 && debfNumMaisonOuLigneAdresse2.length() > 2) {
                indexDepart = 1;
            }

            // Limitation du nombre de caract�re � 70 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 70) {
                this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2.substring(indexDepart, 70);
            } else {
                this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2.substring(indexDepart, debfNumMaisonOuLigneAdresse2.length());
            }
        }
    }

    public void insertAdresseDebFAsStringInQrFacture(String adresseDebiteurAsString) {
        this.setDebfNom(adresseDebiteurAsString.substring(0, adresseDebiteurAsString.indexOf('\n')));
        adresseDebiteurAsString = adresseDebiteurAsString.substring(adresseDebiteurAsString.indexOf('\n') + 1, adresseDebiteurAsString.length());
        this.setDebfRueOuLigneAdresse1(adresseDebiteurAsString.substring(0, adresseDebiteurAsString.indexOf('\n')));
        adresseDebiteurAsString = adresseDebiteurAsString.substring(adresseDebiteurAsString.indexOf('\n') + 1, adresseDebiteurAsString.length());
        this.setDebfNumMaisonOuLigneAdresse2(adresseDebiteurAsString);
    }

    public String getDebfCodePostal() {
        // Limitation du nombre de caract�re � 16 ( doc officielle)
        if (debfCodePostal.length() > 16) {
            return debfCodePostal.substring(0, 16);
        } else return debfCodePostal;
    }

    public void setDebfCodePostal(String debfCodePostal) {
        // Limitation du nombre de caract�re � 16 ( doc officielle)
        if (debfCodePostal.length() > 16) {
            this.debfCodePostal = debfCodePostal.substring(0, 16);
        } else this.debfCodePostal = debfCodePostal;
    }

    public String getDebfLieu() {
        // Limitation du nombre de caract�re � 35 ( doc officielle)
        if (debfLieu.length() > 35) {
            return debfLieu.substring(0, 35);
        } else return debfLieu;
    }

    public void setDebfLieu(String debfLieu) {
        // Limitation du nombre de caract�re � 16 ( doc officielle)
        if (debfLieu.length() > 16) {
            this.debfLieu = debfLieu.substring(0, 16);
        } else this.debfLieu = debfLieu;
    }

    public String getDebfPays() {
        if (debfPays.isEmpty()) {
            return CODE_PAYS_DEFAUT;
        }
        return debfPays;
    }

    public void setDebfPays(String debfPays) {
        this.debfPays = debfPays;
    }

    public String getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(String typeReference) {
        this.typeReference = typeReference;
    }

    public String getCommunicationNonStructuree() {
        // Limitation du nombre de caract�re � 140 ( doc officielle)
        if (communicationNonStructuree.length() > 140) {
            return communicationNonStructuree.substring(0, 140);
        } else return communicationNonStructuree;
    }

    public void setCommunicationNonStructuree(String communicationNonStructuree) {
        // Limitation du nombre de caract�re � 140 ( doc officielle)
        if (communicationNonStructuree.length() > 140) {
            this.communicationNonStructuree = communicationNonStructuree.substring(0, 140);
        } else this.communicationNonStructuree = communicationNonStructuree;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getInfoFacture() {
        // Limitation du nombre de caract�re � 140 ( doc officielle)
        if (infoFacture.length() > 140) {
            return infoFacture.substring(0, 140);
        } else return infoFacture;
    }

    public void setInfoFacture(String infoFacture) {
        // Limitation du nombre de caract�re � 140 ( doc officielle)
        if (infoFacture.length() > 140) {
            this.infoFacture = infoFacture.substring(0, 140);
        } else this.infoFacture = infoFacture;
    }

    public String getPa1Param() {
        // Limitation du nombre de caract�re � 100 ( doc officielle)
        if (pa1Param.length() > 100) {
            return pa1Param.substring(0, 100);
        } else return pa1Param;
    }

    public void setPa1Param(String pa1Param) {
        // Limitation du nombre de caract�re � 100 ( doc officielle)
        if (pa1Param.length() > 100) {
            this.pa1Param = pa1Param.substring(0, 100);
        } else this.pa1Param = pa1Param;
    }

    public String getPa2Param() {
        // Limitation du nombre de caract�re � 100 ( doc officielle)
        if (pa2Param.length() > 100) {
            return pa2Param.substring(0, 100);
        } else return pa2Param;
    }

    public void setPa2Param(String pa2Param) {
        // Limitation du nombre de caract�re � 100 ( doc officielle)
        if (pa2Param.length() > 100) {
            this.pa1Param = pa2Param.substring(0, 100);
        } else this.pa2Param = pa2Param;
    }

    public Boolean getQrNeutre() {
        return qrNeutre;
    }

    public void setQrNeutre(Boolean qrNeutre) {
        this.qrNeutre = qrNeutre;
    }

    public String getLangueDoc() {
        return langueDoc;
    }

    public void setLangueDoc(String langueDoc) {
        this.langueDoc = langueDoc;
    }

    public String getSubReportQR() {
        return subReportQR;
    }

    public void setSubReportQR(String subReportQR) {
        this.subReportQR = subReportQR;
    }

    public String getCrefPays() {
        if (crefPays.isEmpty()) {
            return CODE_PAYS_DEFAUT;
        }
        return crefPays;
    }

    public void setCrefPays(String crefPays) {
        this.crefPays = crefPays;
    }

    public String getDefaultModelPath() {
        try {
            return getSession().getApplication().getExternalModelPath() + "defaultModel";
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getRootApplicationPath() {
        return JadeStringUtil.change(Jade.getInstance().getHomeDir() + getSession().getApplicationId().toLowerCase() + "Root/", "\\", "/");
    }

    public String getWorkApplicationPath() {
        return getRootApplicationPath() + "work/";
    }

    public void setMontantMinimeOuMontantReporter(boolean factureAvecMontantMinime) {
        this.montantMinimeOuMontantReporter = factureAvecMontantMinime;
    }

    public void setRecouvrementDirect(boolean recouvrementDirect) {
        this.recouvrementDirect = recouvrementDirect;
    }

    /**
     * Va chercher le num�ro du compte dans Babel
     *
     * @return le N� du compte (ex: 01-12345-1)
     * @throws Exception
     * @author: sel Cr�� le : 28 nov. 06
     */
    public String getNumeroCC(String langueTiers) throws Exception {
        return getTexteBabel(2, 1, langueTiers);
    }

    /**
     * R�cup�re les textes du catalogue de texte
     *
     * @param niveau
     * @param position
     * @return texte
     * @throws Exception
     * @author: sel Cr�� le : 28 nov. 06
     */
    public String getTexteBabel(int niveau, int position, String langueTiers) throws Exception {
        StringBuilder resString = new StringBuilder();
        if (this.getCurrentDocument(getSession(), langueTiers) == null) {
            resString.append(TEXTE_INTROUVABLE);
        } else {
            ICTListeTextes listeTextes = this.getCurrentDocument(getSession(), langueTiers).getTextes(niveau);
            resString.append(listeTextes.getTexte(position));
        }
        return resString.toString();
    }
}
