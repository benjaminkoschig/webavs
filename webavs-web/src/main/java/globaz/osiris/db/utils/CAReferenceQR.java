package globaz.osiris.db.utils;

import globaz.aquila.print.COParameter;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;


public class CAReferenceQR extends AbstractCAReference {

    private static final String CHAR_FIN_LIGNE = "\r\n";

    // Variables à externaliser
    private static final String QR_IBAN = "QRR";
    private static final String IBAN = "SCOR";
    private static final String SANS_REF = "NON";

    public static final String COMBINE = "K";
    public static final String STRUCTURE = "S";
    // ---------------------- //

    private String adresse;
    private String adresseCopy;
    private String adresseDebiteur;
    private String compte;
    private String reference;
    private String montantSansCentimes;
    private String montantCentimes;
    private String nomAV1;
    private String nomAV2;
    private String infosAdditionnelles;

    private String qrType;
    private String version;
    private String codingType;
    private String creNom;
    private String creAdressTyp;
    private String creRueOuLigneAdresse1;
    private String creNumMaisonOuLigneAdresse2;
    private String creCodePostal;
    private String creLieu;
    private String crePays;

    // Non utilisé pour le moment - Pour une utilisation future.
    private String crefNom;
    private String crefAdressTyp;
    private String crefRueOuLigneAdresse1;
    private String crefNumMaisonOuLigneAdresse2;
    private String crefCodePostal;
    private String crefLieu;
    private String crefPays;
    //


    private String montant;
    private String monnaie;
    private String debfNom;
    private String debfAdressTyp;
    private String debfRueOuLigneAdresse1;
    private String debfNumMaisonOuLigneAdresse2;
    private String debfCodePostal;
    private String debfLieu;
    private String debfPays;
    private String typeReference;
    private String communicationNonStructuree;
    private String trailer;
    private String infoFacture;
    private String pa1Param;
    private String pa2Param;

    private HashMap<String,String> parameters;

    // Pour le moment le QR Code est défini en String. Sera modifié par la suite
    private String qRCode;

    public CAReferenceQR () {
        super();
        adresse = "";
        adresseCopy = "";
        adresseDebiteur = "";

        compte = "";
        reference = "";
        montantSansCentimes = "";
        montantCentimes = "";
        nomAV1 = "";
        nomAV2 = "";
        infosAdditionnelles = "";
        parameters = new HashMap<>();

        // Non utilisé pour le moment, mis à vide.
        crefNom = "";
        crefAdressTyp = "";
        crefRueOuLigneAdresse1 = "";
        crefNumMaisonOuLigneAdresse2 = "";
        crefCodePostal = "";
        crefLieu = "";
        crefPays = "";

        // Adresses combinées
        creAdressTyp = COMBINE;
        debfAdressTyp = COMBINE;

        // Valeur Fixe
        qrType = "SPC";
        version = "200";
        codingType = "1";

        // Fixé pour le moment, utilisation de l'IBAN
        // QR-IBAN = GRR
        // IBAN = SCOR
        // Sans ref = NON
        typeReference = IBAN;

        crefNom = "";
        crefAdressTyp = "";
        crefRueOuLigneAdresse1 = "";
        creNumMaisonOuLigneAdresse2 = "";
        creCodePostal = "";
        creLieu = "";
        crePays = "";

        montant = "";
        monnaie = "";
        debfNom = "";
        debfAdressTyp = "";
        debfRueOuLigneAdresse1 = "";
        debfNumMaisonOuLigneAdresse2 = "";
        debfCodePostal = "";
        debfLieu = "";
        debfPays = "";
        typeReference = "";
        communicationNonStructuree = "";
        trailer = "";
        infoFacture = "";
        pa1Param = "";
        pa2Param = "";
    }

    public void initEnteteQR (FWIDocumentManager document){

        parameters.put(COParameter.P_SUBREPORT_QR_FACTURE, document.getImporter().getImportPath() + "QR_FACTURE_TEMPLATE.jasper");
        parameters.put(COParameter.P_TITRE_1, getSession().getLabel("QR_RECEPISSE"));
        parameters.put(COParameter.P_TITRE_2, getSession().getLabel("QR_SECTION_PAIEMENT"));
        parameters.put(COParameter.P_POINT_DEPOT, getSession().getLabel("QR_POINT_DEPOT"));
        parameters.put(COParameter.P_INFO_SUPP, "NOM_AV1_A_CHARGER");
        parameters.put(COParameter.P_MONNAIE_TITRE_1, getSession().getLabel("QR_MONNAIE"));
        parameters.put(COParameter.P_MONNAIE_TITRE_2, getSession().getLabel("QR_MONNAIE"));
        parameters.put(COParameter.P_MONTANT_TITRE_1, getSession().getLabel("MONTANT"));
        parameters.put(COParameter.P_MONTANT_TITRE_2, getSession().getLabel("MONTANT"));
        parameters.put(COParameter.P_SUBREPORT_ZONE_INDICATIONS,document.getImporter().getImportPath() + "QR_CODE_ZONE_INDICATIONS.jasper");
        parameters.put(COParameter.P_SUBREPORT_RECEPISE, document.getImporter().getImportPath() + "QR_CODE_RECEPISE.jasper");
        parameters.put(COParameter.P_COMPTE_TITRE, getSession().getLabel("QR_COMPTE_PAYABLE"));
        parameters.put(COParameter.P_PAR_TITRE, getSession().getLabel("QR_PAYABLE_PAR"));
        parameters.put(COParameter.P_REF_TITRE, getSession().getLabel("REFERENCE"));
        parameters.put(COParameter.P_INFO_ADD_TITRE, getSession().getLabel("QR_INFO_SUPP"));

        parameters.put(COParameter.P_QR_CODE_PATH, new GenerationQRCode().generateSwissQrCode(generationPayLoad()));
        parameters.put(COParameter.P_MONNAIE_1, monnaie);
        parameters.put(COParameter.P_MONNAIE_2, monnaie);
        parameters.put(COParameter.P_MONTANT_1, montant);
        parameters.put(COParameter.P_MONTANT_2, montant);
        parameters.put(COParameter.P_COMPTE, compte + RETOUR_LIGNE + creRueOuLigneAdresse1);
        parameters.put(FWIImportParametre.PARAM_REFERENCE, getReference());
        parameters.put(COParameter.P_PAR, debfRueOuLigneAdresse1);
        //parameters.put("Payable", debfNom+ RETOUR_LIGNE  + debfRueOuLigneAdresse1 + " " + debfNumMaisonOuLigneAdresse2 + RETOUR_LIGNE  + debfCodePostal + " " + debfLieu);
        parameters.put(COParameter.P_INFO_ADD, communicationNonStructuree + RETOUR_LIGNE  + infoFacture);

    }

    public FWIDocumentManager initQR(FWIDocumentManager document){
        initEnteteQR(document);
        Iterator it = parameters.entrySet().iterator();

        while (it.hasNext()){
            HashMap.Entry element = (HashMap.Entry) it.next();
            document.setParametres(element.getKey(), Objects.isNull(element.getValue()) ? "" : element.getValue());
        }

        return document;
    }

    public String generationPayLoad (){

        // TODO Mieux gérer le String
        String payloadFinal = qrType + CHAR_FIN_LIGNE
                + version + CHAR_FIN_LIGNE
                + codingType + CHAR_FIN_LIGNE
                + compte + CHAR_FIN_LIGNE
                + creNom + CHAR_FIN_LIGNE
                + creAdressTyp + CHAR_FIN_LIGNE
                + creRueOuLigneAdresse1 + CHAR_FIN_LIGNE
                + creNumMaisonOuLigneAdresse2 + CHAR_FIN_LIGNE
                + (Objects.equals(creAdressTyp, COMBINE)? "" : creCodePostal) + CHAR_FIN_LIGNE
                + (Objects.equals(creAdressTyp, COMBINE)? "" : creLieu) + CHAR_FIN_LIGNE
                + crePays + CHAR_FIN_LIGNE
                + crefNom + CHAR_FIN_LIGNE
                + crefAdressTyp + CHAR_FIN_LIGNE
                + crefRueOuLigneAdresse1 + CHAR_FIN_LIGNE
                + crefNumMaisonOuLigneAdresse2 + CHAR_FIN_LIGNE
                + (Objects.equals(crefAdressTyp, COMBINE)? "" : crefCodePostal) + CHAR_FIN_LIGNE
                + (Objects.equals(crefAdressTyp, COMBINE)? "" : crefLieu) + CHAR_FIN_LIGNE
                + crefPays + CHAR_FIN_LIGNE
                + montant + CHAR_FIN_LIGNE
                + monnaie + CHAR_FIN_LIGNE
                + debfNom + CHAR_FIN_LIGNE
                + debfAdressTyp + CHAR_FIN_LIGNE
                + debfRueOuLigneAdresse1 + CHAR_FIN_LIGNE
                + debfNumMaisonOuLigneAdresse2 + CHAR_FIN_LIGNE
                + (Objects.equals(debfAdressTyp, COMBINE)? "" : debfCodePostal) + CHAR_FIN_LIGNE
                + (Objects.equals(debfAdressTyp, COMBINE)? "" : debfLieu) + CHAR_FIN_LIGNE
                + debfPays + CHAR_FIN_LIGNE
                + typeReference + CHAR_FIN_LIGNE
                + reference + CHAR_FIN_LIGNE
                + communicationNonStructuree + CHAR_FIN_LIGNE
                + trailer + CHAR_FIN_LIGNE
                + infoFacture + CHAR_FIN_LIGNE
                + pa1Param + CHAR_FIN_LIGNE
                + pa2Param + CHAR_FIN_LIGNE;

        String oldPayload = "SPC\r\n" +
                "0200\r\n" +
                "1\r\n" +
                "CH4431999123000889012\r\n" +
                "S\r\n" +
                "Robert Schneider AG\r\n" +
                "Via Casa Postale\r\n" +
                "1268/2/22\r\n" +
                "2501\r\n" +
                "Biel\r\n" +
                "CH\r\n" +
                "\r\n" +
                "\r\n" +
                "\r\n" +
                "\r\n" +
                "\r\n" +
                "\r\n" +
                "\r\n" +
                "123949.75\r\n" +
                "CHF\r\n" +
                "S\r\n"+
                "Pia-Maria Rutschmann-Schnyder\r\n" +
                "Grosse Marktgasse\r\n" +
                "28/5\r\n" +
                "9400\r\n" +
                "Rorschach\r\n" +
                "CH\r\n" +
                "QRR\r\n" +
                "210000000003139471430009017\r\n" +
                "Beachten sie unsere Sonderangebotswoche bis 23.02.2017!\r\n" +
                "EPD\r\n" +
                "//S1/10/10201409/11/181105/40/0:30\r\n" +
                "eBill/B/41010560425610173";

        return payloadFinal;
    }

    /**
     * Méthode de génération de la référence QR.
     *
     * A implémenter
     * @param typeReference
     */
    public void genererReferenceQR(String typeReference){
        // Méthode à implémenter
        this.typeReference = IBAN;
        reference = "VALEUR A CHANGER";
    }

    /**
     * Méthode qui génére l'adresse du débiteur depuis la DB
     *
     * Si l'adresse est trouvé (unique) alors renvoi true et charge les paramètres
     *
     * @param idTiers
     * @return Boolean
     * @throws Exception
     */
    public Boolean genererAdresseDebiteur(String idTiers) throws Exception{

        TIAdresseDataManager adresseDataManager = new TIAdresseDataManager();
        adresseDataManager.setSession(getSession());
        adresseDataManager.setForIdTiers(idTiers);
        adresseDataManager.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        adresseDataManager.find(BManager.SIZE_NOLIMIT);


        ArrayList<TIAbstractAdresseData> listAdresses = new ArrayList<>();

        for (int i = 0; i < adresseDataManager.size() ; i++) {
            TIAbstractAdresseData adresseTemp = ((TIAbstractAdresseData) adresseDataManager.getEntity(i));
            if ( adresseTemp.getDateFinRelation().isEmpty()) {
                listAdresses.add(adresseTemp);
            }
        }

        // Chargement des données débiteurs sur le QR
        if (listAdresses.size() == 1){
            debfAdressTyp = STRUCTURE;
            debfNom= listAdresses.get(0).getNom();
            debfPays = listAdresses.get(0).getPaysIso();
            debfCodePostal = listAdresses.get(0).getNpa();
            debfLieu = listAdresses.get(0).getLocalite();
            debfRueOuLigneAdresse1 = listAdresses.get(0).getRue();
            debfNumMaisonOuLigneAdresse2 = listAdresses.get(0).getNumero();
            return true;
        } else return false;
    }


    public void setAdresse(String adresse) {
        this.adresse = adresse;
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

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public void addParametersValue(String key, String value){
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
        if (creNom.length()>70){
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
        return debfNom;
    }

    public void setDebfNom(String debfNom) {
        if (debfNom.length()>70){
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
