package globaz.corvus.vb.process;

import globaz.framework.util.FWCurrency;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.HashMap;
import java.util.Map;

public class REGenererAttestationFiscaleUniqueViewBean extends PRAbstractViewBeanSupport {

    private String adresse = "";
    private String anneeAttestations = "";
    private String assure = "";
    private String attTable_assure = "";
    private String attTable_montant = "";
    private String attTable_periode = "";
    private String chf = "";
    private String codeIsoLangue = "";
    private String concerne = "";
    private String dateImpressionAttJJMMAAA = "";
    private String displaySendToGed = "0";
    private String eMailAddress = "";
    private String idTiers = "";
    private String idTiersBaseCalcul = "";
    private Boolean isSendToGed = Boolean.FALSE;
    private Map<String, String> mapAssure = new HashMap<String, String>();
    private Map<String, String> mapLibelleRente = new HashMap<String, String>();
    private Map<String, String> mapMontant = new HashMap<String, String>();
    private Map<String, String> mapOVDesignation = new HashMap<String, String>();
    private Map<String, String> mapOVMontant = new HashMap<String, String>();
    private Map<String, String> mapOVType = new HashMap<String, String>();
    private Map<String, String> mapPeriode = new HashMap<String, String>();
    private String montant = "";
    private FWCurrency montantTotal = new FWCurrency("0.00");
    private String NSS = "";
    private String para1 = "";
    private String paraAPI = "";
    private String periode = "";
    private String salutation = "";
    private String signature = "";
    private String sousConcerne = "";
    private String table_assure = "";
    private String table_montant = "";
    private String table_periode = "";
    private String titre = "";
    private String titreAPI = "";
    private String total = "";
    private String traiterPar = "";
    private String contact = "";
    private String telephone = "";

    public String getAdresse() {
        return adresse;
    }

    public String getAnneeAttestations() {
        return anneeAttestations;
    }

    public String getAssure() {
        return assure;
    }

    public String getAttTable_assure() {
        return attTable_assure;
    }

    public String getAttTable_montant() {
        return attTable_montant;
    }

    public String getAttTable_periode() {
        return attTable_periode;
    }

    public String getChf() {
        return chf;
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getConcerne() {
        return concerne;
    }

    public String getDateImpressionAttJJMMAAA() {
        return dateImpressionAttJJMMAAA;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public String getLabel(String id) {
        return getSession().getLabel(id);
    }

    public Map<String, String> getMapassure() {
        return mapAssure;
    }

    public Map<String, String> getMaplibelleRente() {
        return mapLibelleRente;
    }

    public Map<String, String> getMapmontant() {
        return mapMontant;
    }

    public Map<String, String> getMapperiode() {
        return mapPeriode;
    }

    public String getMontant() {
        return montant;
    }

    public FWCurrency getMontantTotal() {
        return montantTotal;
    }

    public String getNSS() {
        return NSS;
    }

    public Map<String, String> getOVDesignation() {
        return mapOVDesignation;
    }

    public Map<String, String> getOVMontant() {
        return mapOVMontant;
    }

    public Map<String, String> getOVType() {
        return mapOVType;
    }

    public String getPara1() {
        return para1;
    }

    public String getParaAPI() {
        return paraAPI;
    }

    public String getPeriode() {
        return periode;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getSignature() {
        return signature;
    }

    public String getSousConcerne() {
        return sousConcerne;
    }

    public String getTable_assure() {
        return table_assure;
    }

    public String getTable_montant() {
        return table_montant;
    }

    public String getTable_periode() {
        return table_periode;
    }

    public String getTitre() {
        return titre;
    }

    public String getTitreAPI() {
        return titreAPI;
    }

    public String getTotal() {
        return total;
    }

    public String getTraiterPar() {
        return traiterPar;
    }

    public String getContact() {
        return contact;
    }

    public String getTelephone() {
        return telephone ;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAnneeAttestations(String anneeAttestations) {
        this.anneeAttestations = anneeAttestations;
    }

    public void setAssure(String assure) {
        this.assure = assure;
    }

    public void setAttTable_assure(String attTable_assure) {
        this.attTable_assure = attTable_assure;
    }

    public void setAttTable_montant(String attTable_montant) {
        this.attTable_montant = attTable_montant;
    }

    public void setAttTable_periode(String attTable_periode) {
        this.attTable_periode = attTable_periode;
    }

    public void setChf(String chf) {
        this.chf = chf;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setConcerne(String concerne) {
        this.concerne = concerne;
    }

    public void setDateImpressionAttJJMMAAA(String dateImpressionAttJJMMAAA) {
        this.dateImpressionAttJJMMAAA = dateImpressionAttJJMMAAA;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    public void setEMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersBaseCalcul(String idTiersBaseCalcul) {
        this.idTiersBaseCalcul = idTiersBaseCalcul;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setMapassure(Map<String, String> mapassure) {
        mapAssure = mapassure;
    }

    public void setMaplibelleRente(Map<String, String> maplibelleRente) {
        mapLibelleRente = maplibelleRente;
    }

    public void setMapmontant(Map<String, String> mapmontant) {
        mapMontant = mapmontant;
    }

    public void setMapperiode(Map<String, String> mapperiode) {
        mapPeriode = mapperiode;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantTotal(FWCurrency montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setNSS(String nSS) {
        NSS = nSS;
    }

    public void setOVDesignation(Map<String, String> oVDesignation) {
        mapOVDesignation = oVDesignation;
    }

    public void setOVMontant(Map<String, String> oVMontant) {
        mapOVMontant = oVMontant;
    }

    public void setOVType(Map<String, String> oVType) {
        mapOVType = oVType;
    }

    public void setPara1(String para1) {
        this.para1 = para1;
    }

    public void setParaAPI(String paraAPI) {
        this.paraAPI = paraAPI;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setSousConcerne(String sousConcerne) {
        this.sousConcerne = sousConcerne;
    }

    public void setTable_assure(String table_assure) {
        this.table_assure = table_assure;
    }

    public void setTable_montant(String table_montant) {
        this.table_montant = table_montant;
    }

    public void setTable_periode(String table_periode) {
        this.table_periode = table_periode;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTitreAPI(String titreAPI) {
        this.titreAPI = titreAPI;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setTraiterPar(String traiterPar) {
        this.traiterPar = traiterPar;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
