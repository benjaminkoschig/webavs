package globaz.osiris.db.utils;

import globaz.aquila.print.COParameter;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.util.JABVR;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import org.apache.commons.lang.StringUtils;

import java.util.*;


public class CAReferenceQR extends AbstractCAReference {

    private static final String CHAR_FIN_LIGNE = "\r\n";
    private static final String CODE_PAYS_DEFAUT = "CH";

    // Variables à externaliser
    private static final String QR_IBAN = "QRR";
    private static final String IBAN = "SCOR";
    private static final String SANS_REF = "NON";

    // Caractère de fin de paiement. Valeur fixe
    private static final String END_OF_PAYMENT = "EPD";

    private static final String QR_TYPE = "SPC";
    private static final String DEFAULT_VERSION = "0200";
    private static final String CODING_TYPE= "1";

    public static final String COMBINE = "K";
    public static final String STRUCTURE = "S";
    private static final String ESPACE = " ";
    // ---------------------- //

    private String adresseCopy = StringUtils.EMPTY;
    private String adresseDebiteur = StringUtils.EMPTY;
    private String compte = StringUtils.EMPTY;
    private String reference = StringUtils.EMPTY;
    private String montantSansCentimes = StringUtils.EMPTY;
    private String montantCentimes = StringUtils.EMPTY;
    private String nomAV1 = StringUtils.EMPTY;
    private String nomAV2 = StringUtils.EMPTY;
    private String infosAdditionnelles = StringUtils.EMPTY;

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
    //


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
    private String trailer = StringUtils.EMPTY;
    private String infoFacture = StringUtils.EMPTY;
    private String pa1Param = StringUtils.EMPTY;
    private String pa2Param = StringUtils.EMPTY;

    private Map<String, String> parameters = new HashMap<>();

    // Pour le moment le QR Code est défini en String. Sera modifié par la suite
    private String qRCode;

    public CAReferenceQR() {
        super();

        // Init Trailer. Valeur fixe = EPD
        trailer = END_OF_PAYMENT;
    }


    public FWIDocumentManager initQR(FWIDocumentManager document) throws CATechnicalException {
        // Initialisation des entêtes de la QR-Facture
        initEnteteQR(document);

        // Initialisation des params de la QR-Facture
        initParamQR();

        Iterator it = parameters.entrySet().iterator();

        // Chargement des paramètres sur le document
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            document.setParametres(element.getKey(), Objects.isNull(element.getValue()) ? "" : element.getValue());
        }

        return document;
    }

    /**
     * Méthode qui charge les entêtes du documents QR-Facture
     *
     * @param document
     */
    private void initEnteteQR(FWIDocumentManager document) {
        parameters.put(COParameter.P_SUBREPORT_QR, document.getImporter().getImportPath() + "QR_FACTURE_TEMPLATE.jasper");
        parameters.put(COParameter.P_TITRE_1, getSession().getLabel("QR_RECEPISSE"));
        parameters.put(COParameter.P_TITRE_2, getSession().getLabel("QR_SECTION_PAIEMENT"));
        parameters.put(COParameter.P_POINT_DEPOT, getSession().getLabel("QR_POINT_DEPOT"));
        parameters.put(COParameter.P_MONNAIE_TITRE, getSession().getLabel("QR_MONNAIE"));
        parameters.put(COParameter.P_MONTANT_TITRE, getSession().getLabel("MONTANT"));
        parameters.put(COParameter.P_SUBREPORT_ZONE_INDICATIONS, document.getImporter().getImportPath() + "QR_CODE_ZONE_INDICATIONS.jasper");
        parameters.put(COParameter.P_SUBREPORT_RECEPISE, document.getImporter().getImportPath() + "QR_CODE_RECEPISE.jasper");
        parameters.put(COParameter.P_COMPTE_TITRE, getSession().getLabel("QR_COMPTE_PAYABLE"));
        parameters.put(COParameter.P_PAR_TITRE, getSession().getLabel("QR_PAYABLE_PAR"));
        parameters.put(COParameter.P_REF_TITRE, getSession().getLabel("REFERENCE"));
        parameters.put(COParameter.P_INFO_ADD_TITRE, getSession().getLabel("QR_INFO_SUPP"));


    }

    /**
     * Méthode qui charge les paramètres variables de la QR-Facture
     *
     */
    public void initParamQR() throws CATechnicalException {
        parameters.put(COParameter.P_QR_CODE_PATH, new GenerationQRCode().generateSwissQrCode(generationPayLoad()));
        parameters.put(COParameter.P_MONNAIE, monnaie);
        parameters.put(COParameter.P_MONTANT, montant);
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
            parameters.put(COParameter.P_PAR, (debfRueOuLigneAdresse1 + RETOUR_LIGNE + debfNumMaisonOuLigneAdresse2).trim());
        }
        parameters.put(COParameter.P_INFO_ADD, (communicationNonStructuree + RETOUR_LIGNE + infoFacture).trim());
    }

    private String generationPayLoad() {
        StringBuilder builder = new StringBuilder();

        builder.append(qrType).append(CHAR_FIN_LIGNE);
        builder.append(version).append(CHAR_FIN_LIGNE);
        builder.append(codingType).append(CHAR_FIN_LIGNE);
        builder.append(compte).append(CHAR_FIN_LIGNE);
        builder.append(creNom).append(CHAR_FIN_LIGNE);
        builder.append(creAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(creRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
        builder.append(creNumMaisonOuLigneAdresse2).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(creAdressTyp, COMBINE) ? StringUtils.EMPTY : creCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(creAdressTyp, COMBINE) ? StringUtils.EMPTY : creLieu)).append(CHAR_FIN_LIGNE);
        builder.append(Objects.isNull(getCrePays())? CODE_PAYS_DEFAUT : crePays).append(CHAR_FIN_LIGNE);
        builder.append(crefNom).append(CHAR_FIN_LIGNE);
        builder.append(crefAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(crefRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
        builder.append(crefNumMaisonOuLigneAdresse2).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(crefAdressTyp, COMBINE) ? StringUtils.EMPTY : crefCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(crefAdressTyp, COMBINE) ? StringUtils.EMPTY : crefLieu)).append(CHAR_FIN_LIGNE);
        builder.append(crefPays).append(CHAR_FIN_LIGNE);
        builder.append(montant).append(CHAR_FIN_LIGNE);
        builder.append(monnaie).append(CHAR_FIN_LIGNE);
        builder.append(debfNom).append(CHAR_FIN_LIGNE);
        builder.append(debfAdressTyp).append(CHAR_FIN_LIGNE);
        builder.append(debfRueOuLigneAdresse1).append(CHAR_FIN_LIGNE);
        builder.append(debfNumMaisonOuLigneAdresse2).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(debfAdressTyp, COMBINE) ? StringUtils.EMPTY : debfCodePostal)).append(CHAR_FIN_LIGNE);
        builder.append((Objects.equals(debfAdressTyp, COMBINE) ? StringUtils.EMPTY : debfLieu)).append(CHAR_FIN_LIGNE);
        builder.append(debfPays).append(CHAR_FIN_LIGNE);
        builder.append(typeReference).append(CHAR_FIN_LIGNE);
        builder.append(getReferenceNonFormatte()).append(CHAR_FIN_LIGNE);
        builder.append(communicationNonStructuree).append(CHAR_FIN_LIGNE);
        builder.append(trailer).append(CHAR_FIN_LIGNE);
        builder.append(infoFacture).append(CHAR_FIN_LIGNE);
        builder.append(pa1Param).append(CHAR_FIN_LIGNE);
        builder.append(pa2Param).append(CHAR_FIN_LIGNE);

        return builder.toString();
    }

    /**
     * Méthode de génération de la référence QR.
     * <p>
     *
     *
     * @param section la section
     */
    public void genererReferenceQR(APISection section) throws Exception {
        this.reference = genererNumReference(section.getCompteAnnexe().getIdRole(), section.getCompteAnnexe()
                .getIdExterneRole(), false, section.getIdTypeSection(), section.getIdExterne(), section
                .getIdCompteAnnexe());
        if (StringUtils.isEmpty(reference)) {
            this.typeReference = SANS_REF;
        } else {
            this.typeReference = IBAN;
        }

        if (JadeStringUtil.isDecimalEmpty(montant)) {
            montant = section.getSolde();
        }

        if (new FWCurrency(montant).isPositive()) {
            JABVR bvr = new JABVR(montant, reference, getNoAdherent());

            setReference(bvr.get_ligneReference());
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
        } else return false;
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

        for(int i = 0; i < adresseSplit.length ; i++) {
            if (i == (adresseSplit.length - 1)) {
                this.creNumMaisonOuLigneAdresse2 = adresseSplit[i];
            } else {
                this.creRueOuLigneAdresse1 = this.creRueOuLigneAdresse1 + adresseSplit[i] +"\r\n";
            }
        }
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

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     *
     *
     */
    public String getReferenceNonFormatte(){
        return reference.replaceAll("\\s", "");
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
        return creNom;
    }

    public void setCreNom(String creNom) {
        // Limitation du nombre de caractère à 70 ( doc officielle)
        if (creNom.length() > 70) {
            this.creNom = creNom.substring(70, creNom.length());
        } else this.creNom = creNom;
    }

    public String getCreAdressTyp() {
        return creAdressTyp;
    }

    public void setCreAdressTyp(String creAdressTyp) {
        this.creAdressTyp = creAdressTyp;
    }

    public String getCreRueOuLigneAdresse1() {
        return creRueOuLigneAdresse1;
    }

    public void setCreRueOuLigneAdresse1(String creRueOuLigneAdresse1) {
        this.creRueOuLigneAdresse1 = creRueOuLigneAdresse1;
    }

    public String getCreNumMaisonOuLigneAdresse2() {
        return creNumMaisonOuLigneAdresse2;
    }

    public void setCreNumMaisonOuLigneAdresse2(String creNumMaisonOuLigneAdresse2) {
        this.creNumMaisonOuLigneAdresse2 = creNumMaisonOuLigneAdresse2;
    }

    public String getCreCodePostal() {
        return creCodePostal;
    }

    public void setCreCodePostal(String creCodePostal) {
        this.creCodePostal = creCodePostal;
    }

    public String getCreLieu() {
        return creLieu;
    }

    public void setCreLieu(String creLieu) {
        this.creLieu = creLieu;
    }

    public String getCrePays() {
        try{
            return getCodePays();
        } catch (Exception e){
            // Le code pays n'a pas été trouvé
            return null;
        }
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
        return debfNom;
    }

    public void setDebfNom(String debfNom) {
        if (debfNom.length() > 70) {
            this.debfNom = debfNom.substring(70, debfNom.length());
        } else this.debfNom = debfNom;
    }

    public String getDebfAdressTyp() {
        return debfAdressTyp;
    }

    public void setDebfAdressTyp(String dbefAdressTyp) {
        this.debfAdressTyp = dbefAdressTyp;
    }

    public String getDebfRueOuLigneAdresse1() {
        return debfRueOuLigneAdresse1;
    }

    public void setDebfRueOuLigneAdresse1(String debfRueOuLigneAdresse1) {
        this.debfRueOuLigneAdresse1 = debfRueOuLigneAdresse1;
    }

    public String getDebfNumMaisonOuLigneAdresse2() {
        return debfNumMaisonOuLigneAdresse2;
    }

    public void setDebfNumMaisonOuLigneAdresse2(String debfNumMaisonOuLigneAdresse2) {
        this.debfNumMaisonOuLigneAdresse2 = debfNumMaisonOuLigneAdresse2;
    }

    public String getDebfCodePostal() {
        return debfCodePostal;
    }

    public void setDebfCodePostal(String debfCodePostal) {
        this.debfCodePostal = debfCodePostal;
    }

    public String getDebfLieu() {
        return debfLieu;
    }

    public void setDebfLieu(String debfLieu) {
        this.debfLieu = debfLieu;
    }

    public String getDebfPays() {
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
        return communicationNonStructuree;
    }

    public void setCommunicationNonStructuree(String communicationNonStructuree) {
        this.communicationNonStructuree = communicationNonStructuree;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getInfoFacture() {
        return infoFacture;
    }

    public void setInfoFacture(String infoFacture) {
        this.infoFacture = infoFacture;
    }

    public String getPa1Param() {
        return pa1Param;
    }

    public void setPa1Param(String pa1Param) {
        this.pa1Param = pa1Param;
    }

    public String getPa2Param() {
        return pa2Param;
    }

    public void setPa2Param(String pa2Param) {
        this.pa2Param = pa2Param;
    }

}
