package ch.globaz.common.document.reference;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.GenerationQRCode;
import globaz.aquila.print.COParameter;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.util.JABVR;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.i18n.JadeI18n;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.api.APISection;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ReferenceQR extends AbstractReference {

    public static final String DEVISE_DEFAUT = "CHF";
    private static final String CHAR_FIN_LIGNE = "\r\n";
    private static final String CHAR_FIN_LIGNE_DEB = "\n";
    private static final String CODE_PAYS_DEFAUT = "CH";
    private static final String ESPACE = " ";
    private static final String LANGUE_PAR_DEFAUT = "FR";

    private String subReportQR = "QR_FACTURE_TEMPLATE.jasper";
    private String subReportQRCurrentPage = "QR_FACTURE_TEMPLATE_CURRENT_PAGE.jasper";

    private static final String QR_IBAN = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"type.reference.qr.iban");
    private static final String IBAN = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"type.reference.iban");
    private static final String SANS_REF = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"type.sans.reference");
    private static final String END_OF_PAYMENT = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"end.of.payment");
    private static final String QR_TYPE = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"type.qr");
    private static final String DEFAULT_VERSION = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"default.version");
    private static final String CODING_TYPE= JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"coding.type");
    public static final String COMBINE = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"adresse.combine");
    public static final String STRUCTURE = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT,"adresse.structure");

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
    private String creAdressTyp  = StringUtils.EMPTY;
    private String creRueOuLigneAdresse1  = StringUtils.EMPTY;
    private String creNumMaisonOuLigneAdresse2  = StringUtils.EMPTY;
    private String creCodePostal = StringUtils.EMPTY;
    private String creLieu = StringUtils.EMPTY;
    private String crePays = StringUtils.EMPTY;

    // Non utilisé pour le moment - Pour une utilisation future.
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

    // Boolean qui permet d'activer un QR Neutre
    private Boolean qrNeutre = false;

    private Map<String, String> parameters = new HashMap<>();

    // Pour le moment le QR Code est défini en String. Sera modifié par la suite
    private String qRCode;

    // Param de langue du document
    private String langueDoc = LANGUE_PAR_DEFAUT;

    public ReferenceQR() {
        super();
    }

    public FWIDocumentManager initQR(FWIDocumentManager document) {

        // Initialisation des entêtes de la QR-Facture
        initEnteteQR(document);

        // Initialisation des params de la QR-Facture
         initParamQR();

         for (Map.Entry element : parameters.entrySet()){
             document.setParametres(element.getKey(), Objects.isNull(element.getValue()) ? "" : element.getValue());
         }

        return document;
    }

    /**
     * Méthode qui charge les entêtes du documents QR-Facture
     *
     * @param document
     */
    private void initEnteteQR(FWIDocumentManager document)  {

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
            throw new CommonTechnicalException("Problème à l'initialisation des entêtes QR : ", e);
        }
    }

    /**
     * Méthode qui charge les paramètres variables de la QR-Facture
     *
     */
    public void initParamQR()  {
        parameters.put(COParameter.P_QR_CODE_PATH, GenerationQRCode.generateSwissQrCode(generationPayLoad()));
        parameters.put(COParameter.P_MONNAIE, monnaie);

        // Si l'on est sur un QR Neutre, dans ce cas, il doit être sans montant ni adresse Debiteur.
        if (!qrNeutre) {
            if (new Montant(montant).isNegative()) {
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
            parameters.put(COParameter.P_COMPTE, (compte + RETOUR_LIGNE + creRueOuLigneAdresse1 + creNumMaisonOuLigneAdresse2).trim());
        }
        if (!COMBINE.equals(debfAdressTyp)){
            parameters.put(COParameter.P_PAR, (debfNom + RETOUR_LIGNE + debfRueOuLigneAdresse1 + ESPACE + debfNumMaisonOuLigneAdresse2 + RETOUR_LIGNE + debfCodePostal + ESPACE + debfLieu).trim());
        } else {
            parameters.put(COParameter.P_PAR, (debfNom + RETOUR_LIGNE +debfRueOuLigneAdresse1 + RETOUR_LIGNE + debfNumMaisonOuLigneAdresse2).trim());
        }


    }

    private String generationPayLoad() {
        StringBuilder builder = new StringBuilder();

        builder.append(qrType).append(CHAR_FIN_LIGNE);
        builder.append(version).append(CHAR_FIN_LIGNE);
        builder.append(codingType).append(CHAR_FIN_LIGNE);
        builder.append(getCompteWithoutSpace()).append(CHAR_FIN_LIGNE);

        builder.append(creAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(creNom.isEmpty()?creRueOuLigneAdresse1.replace(CHAR_FIN_LIGNE, " ") : creNom ).append(CHAR_FIN_LIGNE);

        // replace des CHAR_FIN_LIGNE compris dans le string.
        builder.append(creNom.isEmpty()? "" : creRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
        builder.append(creNumMaisonOuLigneAdresse2.replace(CHAR_FIN_LIGNE, " ")).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(creAdressTyp, COMBINE) ? StringUtils.EMPTY : creCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(creAdressTyp, COMBINE) ? StringUtils.EMPTY : creLieu)).append(CHAR_FIN_LIGNE);
        builder.append(Objects.isNull(getCrePays())? CODE_PAYS_DEFAUT : getCrePaysVar()).append(CHAR_FIN_LIGNE);

        builder.append(crefAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(crefNom.isEmpty()? crefRueOuLigneAdresse1.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ") : crefNom ).append(CHAR_FIN_LIGNE);
        builder.append(crefNom.isEmpty() ? "" : crefRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
        builder.append(crefNumMaisonOuLigneAdresse2.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ")).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(crefAdressTyp, COMBINE) ? StringUtils.EMPTY : crefCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(crefAdressTyp, COMBINE) ? StringUtils.EMPTY : crefLieu)).append(CHAR_FIN_LIGNE);
        if (crefNom.isEmpty()) {
            builder.append(CHAR_FIN_LIGNE);
        } else {
            builder.append(Objects.isNull(getCrefPays())? CODE_PAYS_DEFAUT : getCrefPays()).append(CHAR_FIN_LIGNE);
        }

        // Débiteur final
        if (qrNeutre) {
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
            builder.append(monnaie).append(CHAR_FIN_LIGNE);
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
            builder.append(StringUtils.EMPTY).append(CHAR_FIN_LIGNE);
        } else {
            if (new Montant(montant).isNegative()) {
                builder.append("0.00").append(CHAR_FIN_LIGNE);
            } else {
                builder.append(montant).append(CHAR_FIN_LIGNE);
            }
            builder.append(monnaie).append(CHAR_FIN_LIGNE);
            builder.append(debfAdressTyp).append(CHAR_FIN_LIGNE);
            builder.append(debfNom.isEmpty()? debfRueOuLigneAdresse1.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ") : debfNom).append(CHAR_FIN_LIGNE);
            builder.append(debfNom.isEmpty()? "" : debfRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
            builder.append(debfNumMaisonOuLigneAdresse2.replace(CHAR_FIN_LIGNE, " ").replace(CHAR_FIN_LIGNE_DEB, " ")).append(CHAR_FIN_LIGNE);
            builder.append((Objects.equals(debfAdressTyp, COMBINE) ? StringUtils.EMPTY : debfCodePostal)).append(CHAR_FIN_LIGNE);
            builder.append((Objects.equals(debfAdressTyp, COMBINE) ? StringUtils.EMPTY : debfLieu)).append(CHAR_FIN_LIGNE);
        }
        builder.append(Objects.isNull(getDebfPays())? CODE_PAYS_DEFAUT : getDebfPays()).append(CHAR_FIN_LIGNE);

        // Référence paiement
        builder.append(typeReference).append(CHAR_FIN_LIGNE);
        builder.append(getReferenceWithoutSpace()).append(CHAR_FIN_LIGNE);

        // Info Supp
        if (qrNeutre && new Montant(montant).isNegative()) {
            builder.append(pInfoAddErreur + " " +communicationNonStructuree).append(CHAR_FIN_LIGNE);
        } else {
            builder.append(communicationNonStructuree).append(CHAR_FIN_LIGNE);
        }
        builder.append(trailer).append(CHAR_FIN_LIGNE);
        builder.append(infoFacture).append(CHAR_FIN_LIGNE);
        // Procédure alternative
        builder.append(pa1Param).append(CHAR_FIN_LIGNE);
        builder.append(pa2Param);

        return builder.toString();
    }

    /**
     * Méthode de génération de la référence QR pour Contentieux depuis la section
     * <p>
     *
     *
     * @param section la section
     */
    public void genererReferenceQR(APISection section) throws CATechnicalException {
        genererReferenceQR(section.getCompteAnnexe().getIdRole(), section.getCompteAnnexe().getIdExterneRole(), false, section.getIdTypeSection()
                , section.getIdExterne(), section.getIdCompteAnnexe(), section.getSolde());

    }

    /**
     *
     * Méthode de génération de la référence QR
     *
     * @param idRole
     * @param idExterneRole
     * @param isPlanPaiement
     * @param idTypeSection
     * @param idExterne
     * @param idCompteAnnexe
     */
    public void genererReferenceQR(String idRole, String idExterneRole, Boolean isPlanPaiement, String idTypeSection, String idExterne, String idCompteAnnexe, String solde) throws CATechnicalException {

        try{
            this.reference = genererNumReference(idRole, idExterneRole , isPlanPaiement, idTypeSection, idExterne, idCompteAnnexe);
        } catch (Exception e) {
            throw new CATechnicalException("Erreur à la génération de la référence QR :", e);
        }


        if (StringUtils.isEmpty(reference)) {
            this.typeReference = SANS_REF;
        } else {
            this.typeReference = QR_IBAN;
        }

        if (JadeStringUtil.isDecimalEmpty(montant)) {
            montant = solde;
        }

        genererReference(reference, montant);

    }

    /**
     * Génération de la référence
     *
     * @param reference
     * @param montant
     * @throws CATechnicalException
     */

    public void genererReference(String reference, String montant) throws CATechnicalException {
        if (new FWCurrency(montant).isPositive()) {
            try{
                JABVR bvr = new JABVR(montant, reference, getNoAdherent());
                setReference(bvr.get_ligneReference());
            } catch (Exception e) {
                throw new CATechnicalException("Erreur lors de la récupération du N° Adhérent :", e);
            }
        }
    }

    /**
     * Méthode de génération de la référence QR pour Facturation
     * <p>
     *
     *
     * @param enteteFacture l'entête facture
     */
    public void genererReferenceQRFact(FAEnteteFacture enteteFacture, boolean isFactureAvecMontantMinime, boolean isFactureMontantReport) throws Exception {
        this.reference = genererNumReference(enteteFacture.getIdRole(), enteteFacture.getIdExterneRole(), false,
                JadeStringUtil.rightJustifyInteger(enteteFacture.getIdTypeFacture(), 2), enteteFacture.getIdExterneFacture(), null);
        if (StringUtils.isEmpty(reference)) {
            this.typeReference = SANS_REF;
        } else {
            this.typeReference = QR_IBAN;
        }

        JABVR bvr = null;

        if (new FWCurrency(enteteFacture.getTotalFacture()).isPositive()
                && !enteteFacture.getIdModeRecouvrement().equals(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT)) {
            bvr = new JABVR(JANumberFormatter.deQuote(enteteFacture.getTotalFacture()), reference, getNoAdherent());
        }

        if (!(new FWCurrency(enteteFacture.getTotalFacture()).isZero() || isFactureAvecMontantMinime || isFactureMontantReport)
                && (bvr != null)) {
            if (!enteteFacture.getIdModeRecouvrement().equals(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT)) {
                setReference(bvr.get_ligneReference());
            } else {
                setReference(REFERENCE_NON_FACTURABLE);
            }
        } else {
            setReference(REFERENCE_NON_FACTURABLE);
        }
    }

    /**
     * Méthode qui génére l'adresse du débiteur depuis la DB
     * <p>
     * Si l'adresse est trouvé (unique) alors renvoi true et charge les paramètres
     *
     * @param idTiers
     * @return Boolean
     * @throws Exception
     */
    public Boolean genererAdresseDebiteur(String idTiers) throws Exception {

        TIAdresseDataManager adresseDataManager = new TIAdresseDataManager();
        adresseDataManager.setSession(getSession());
        adresseDataManager.setForIdTiers(idTiers);
        adresseDataManager.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        adresseDataManager.find(BManager.SIZE_NOLIMIT);


        ArrayList<TIAbstractAdresseData> listAdresses = new ArrayList<>();

        for (int i = 0; i < adresseDataManager.size(); i++) {
            TIAbstractAdresseData adresseTemp = ((TIAbstractAdresseData) adresseDataManager.getEntity(i));
            if (adresseTemp.getDateFinRelation().isEmpty()) {
                listAdresses.add(adresseTemp);
            }
        }

        // Chargement des données débiteurs sur le QR
        if (listAdresses.size() == 1) {
            debfAdressTyp = STRUCTURE;
            debfNom = listAdresses.get(0).getNom();
            debfPays = listAdresses.get(0).getPaysIso();
            debfCodePostal = listAdresses.get(0).getNpa();
            debfLieu = listAdresses.get(0).getLocalite();
            debfRueOuLigneAdresse1 = listAdresses.get(0).getRue();
            debfNumMaisonOuLigneAdresse2 = listAdresses.get(0).getNumero();
            return true;
        } else {
            return false;
        }
    }

    public void recupererIban() throws Exception {
        this.compte = this.getNumeroCC();
    }


    /**
     * Méthode qui va setter l'adresse créditeur
     * depuis une adresse combinée
     *
     */
    public void genererCreAdresse() {

        String[] adresseSplit =  getAdresse().split("\r\n");

        this.creAdressTyp = COMBINE;

        StringBuilder creRueOuLigneAdresse1SB = new StringBuilder(creRueOuLigneAdresse1);

        for(int i = 0; i < adresseSplit.length ; i++) {
            if (i == (adresseSplit.length - 1)) {
                this.creNumMaisonOuLigneAdresse2 = adresseSplit[i];
            } else {
                creRueOuLigneAdresse1SB.append(adresseSplit[i] +"\r\n");
            }
        }
        creRueOuLigneAdresse1 = creRueOuLigneAdresse1SB.toString();
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
        // Limitation du nombre de caractère à 70 ( doc officielle)
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
        // Limitation du nombre de caractère à 70 ( doc officielle)
        if (creRueOuLigneAdresse1.length() > 70) {
            return creRueOuLigneAdresse1.substring(0, 70);
        } else return creRueOuLigneAdresse1;
    }

    public void setCreRueOuLigneAdresse1(String creRueOuLigneAdresse1) {
        // Limitation du nombre de caractère à 70 ( doc officielle)
        if (creRueOuLigneAdresse1.length() > 70) {
            this.creRueOuLigneAdresse1 = creRueOuLigneAdresse1.substring(0, 70);
        } else this.creRueOuLigneAdresse1 = creRueOuLigneAdresse1;
    }

    public String getCreNumMaisonOuLigneAdresse2() {
        if(getCreAdressTyp().equalsIgnoreCase(STRUCTURE)){
            // Limitation du nombre de caractère à 16 pour les adresses structurées ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 16) {
                return creNumMaisonOuLigneAdresse2.substring(0, 16);
            } else return creNumMaisonOuLigneAdresse2;
        } else {
            // Limitation du nombre de caractère à 70 ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 70) {
                return creNumMaisonOuLigneAdresse2.substring(0, 70);
            } else return creNumMaisonOuLigneAdresse2;
        }

    }

    public void setCreNumMaisonOuLigneAdresse2(String creNumMaisonOuLigneAdresse2) {
        if(getCreAdressTyp().equalsIgnoreCase(STRUCTURE)) {
            // Limitation du nombre de caractère à 16 ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 16) {
                this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2.substring(0, 16);
            } else this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2;
        } else {
            // Limitation du nombre de caractère à 70 ( doc officielle)
            if (creNumMaisonOuLigneAdresse2.length() > 70) {
                this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2.substring(0, 70);
            } else this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2;
        }
    }

    public String getCreCodePostal() {
        // Limitation du nombre de caractère à 16 ( doc officielle)
        if (creCodePostal.length() > 16) {
            return creCodePostal.substring(0, 16);
        } else return creCodePostal;
    }

    public void setCreCodePostal(String creCodePostal) {
        // Limitation du nombre de caractère à 16 ( doc officielle)
        if (creCodePostal.length() > 16) {
            this.creCodePostal = creCodePostal.substring(0, 16);
        } else this.creCodePostal = creCodePostal;
    }

    public String getCreLieu() {
        // Limitation du nombre de caractère à 35 ( doc officielle)
        if (creLieu.length() > 35) {
            return creLieu.substring(0, 35);
        } else return creLieu;
    }

    public void setCreLieu(String creLieu) {
        // Limitation du nombre de caractère à 35 ( doc officielle)
        if (creLieu.length() > 35) {
            this.creLieu = creLieu.substring(0, 35);
        } else this.creLieu = creLieu;

    }

    public String getCrePays() {
        try{
            return getCodePays();
        } catch (Exception e){
            // Le code pays n'a pas été trouvé
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
        // Limitation du nombre de caractère à 70 ( doc officielle)
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
        // Limitation du nombre de caractère à 70 ( doc officielle)
        if (debfRueOuLigneAdresse1.length() > 70) {
            return debfRueOuLigneAdresse1.substring(0, 70);
        } else return debfRueOuLigneAdresse1;
    }

    public void setDebfRueOuLigneAdresse1(String debfRueOuLigneAdresse1) {
        // Limitation du nombre de caractère à 70 ( doc officielle)
        if (debfRueOuLigneAdresse1.length() > 70) {
//            this.debfRueOuLigneAdresse1 = debfRueOuLigneAdresse1.substring(70, debfRueOuLigneAdresse1.length());
            this.debfRueOuLigneAdresse1 = debfRueOuLigneAdresse1.substring(0, 70);
        } else this.debfRueOuLigneAdresse1 = debfRueOuLigneAdresse1;
    }

    public String getDebfNumMaisonOuLigneAdresse2() {
        if(getCreAdressTyp().equalsIgnoreCase(STRUCTURE)) {
            // Limitation du nombre de caractère à 16 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 16) {
                return debfNumMaisonOuLigneAdresse2.substring(0, 16);
            } else return debfNumMaisonOuLigneAdresse2;
        } else {
            // Limitation du nombre de caractère à 70 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 70) {
                return debfNumMaisonOuLigneAdresse2.substring(0, 70);
            } else return debfNumMaisonOuLigneAdresse2;
        }
    }

    public void setDebfNumMaisonOuLigneAdresse2(String debfNumMaisonOuLigneAdresse2) {
        if(getCreAdressTyp().equalsIgnoreCase(STRUCTURE)) {
            // Limitation du nombre de caractère à 16 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 16) {
                this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2.substring(0, 16);
            } else this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2;
        } else {
            int indexDepart = 0;
            if (debfNumMaisonOuLigneAdresse2.indexOf('\n') != -1 && debfNumMaisonOuLigneAdresse2.indexOf('\n') == 0 && debfNumMaisonOuLigneAdresse2.length()>2) {
                indexDepart = 1;
            }

            // Limitation du nombre de caractère à 70 ( doc officielle)
            if (debfNumMaisonOuLigneAdresse2.length() > 70) {
                this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2.substring(indexDepart, 70);
            } else {
                this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2.substring(indexDepart, debfNumMaisonOuLigneAdresse2.length());
            }
        }
    }

    public void insertAdresseDebFAsStringInQrFacture(String adresseDebiteurAsString){
        this.setDebfNom(adresseDebiteurAsString.substring(0, adresseDebiteurAsString.indexOf('\n')));
        adresseDebiteurAsString = adresseDebiteurAsString.substring(adresseDebiteurAsString.indexOf('\n')+1, adresseDebiteurAsString.length());
        this.setDebfRueOuLigneAdresse1(adresseDebiteurAsString.substring(0, adresseDebiteurAsString.indexOf('\n')));
        adresseDebiteurAsString = adresseDebiteurAsString.substring(adresseDebiteurAsString.indexOf('\n')+1, adresseDebiteurAsString.length());
        this.setDebfNumMaisonOuLigneAdresse2(adresseDebiteurAsString);
    }

    public String getDebfCodePostal() {
        // Limitation du nombre de caractère à 16 ( doc officielle)
        if (debfCodePostal.length() > 16) {
            return debfCodePostal.substring(0, 16);
        } else return debfCodePostal;
    }

    public void setDebfCodePostal(String debfCodePostal) {
        // Limitation du nombre de caractère à 16 ( doc officielle)
        if (debfCodePostal.length() > 16) {
            this.debfCodePostal = debfCodePostal.substring(0, 16);
        } else this.debfCodePostal = debfCodePostal;
    }

    public String getDebfLieu() {
        // Limitation du nombre de caractère à 35 ( doc officielle)
        if (debfLieu.length() > 35) {
            return debfLieu.substring(0, 35);
        } else return debfLieu;
    }

    public void setDebfLieu(String debfLieu) {
        // Limitation du nombre de caractère à 16 ( doc officielle)
        if (debfLieu.length() > 16) {
            this.debfLieu = debfLieu.substring(0, 16);
        } else this.debfLieu = debfLieu;
    }

    public String getDebfPays() {
        if (debfPays.isEmpty()){
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
        // Limitation du nombre de caractère à 140 ( doc officielle)
        if (communicationNonStructuree.length() > 140) {
            return communicationNonStructuree.substring(0, 140);
        } else return communicationNonStructuree;
    }

    public void setCommunicationNonStructuree(String communicationNonStructuree) {
        // Limitation du nombre de caractère à 140 ( doc officielle)
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
        // Limitation du nombre de caractère à 140 ( doc officielle)
        if (infoFacture.length() > 140) {
            return infoFacture.substring(0, 140);
        } else return infoFacture;
    }

    public void setInfoFacture(String infoFacture) {
        // Limitation du nombre de caractère à 140 ( doc officielle)
        if (infoFacture.length() > 140) {
            this.infoFacture = infoFacture.substring(0, 140);
        } else this.infoFacture = infoFacture;
    }

    public String getPa1Param() {
        // Limitation du nombre de caractère à 100 ( doc officielle)
        if (pa1Param.length() > 100) {
            return pa1Param.substring(0, 100);
        } else return pa1Param;
    }

    public void setPa1Param(String pa1Param) {
        // Limitation du nombre de caractère à 100 ( doc officielle)
        if (pa1Param.length() > 100) {
            this.pa1Param = pa1Param.substring(0, 100);
        } else this.pa1Param = pa1Param;
    }

    public String getPa2Param() {
        // Limitation du nombre de caractère à 100 ( doc officielle)
        if (pa2Param.length() > 100) {
            return pa2Param.substring(0, 100);
        } else return pa2Param;
    }

    public void setPa2Param(String pa2Param) {
        // Limitation du nombre de caractère à 100 ( doc officielle)
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
}
