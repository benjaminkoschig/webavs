package globaz.caisse.report.helper;

/**
 * Classe : type_conteneur Description : Classe conteneur des différents champs de la signature d'un document Date de
 * création: 11 août 04
 * 
 * @author scr
 */
public class CaisseSignatureReportBean {

    private String service = "";
    private String service2 = "";
    private String signataire = "";
    private String signataire2 = "";
    private String signatureCaisse = "";

    /**
     * Constructor for CaisseSignatureReportBean.
     */
    public CaisseSignatureReportBean() {
        super();
    }

    /**
     * @return
     */
    public String getService() {
        return service;
    }

    /**
     * @return
     */
    public String getService2() {
        return service2;
    }

    /**
     * @return
     */
    public String getSignataire() {
        return signataire;
    }

    /**
     * @return
     */
    public String getSignataire2() {
        return signataire2;
    }

    /**
     * @return
     */
    public String getSignatureCaisse() {
        return signatureCaisse;
    }

    /**
     * @param string
     */
    public void setService(String string) {
        service = string;
    }

    /**
     * @param string
     */
    public void setService2(String string) {
        service2 = string;
    }

    /**
     * @param string
     */
    public void setSignataire(String string) {
        signataire = string;
    }

    /**
     * @param string
     */
    public void setSignataire2(String string) {
        signataire2 = string;
    }

    /**
     * @param string
     */
    public void setSignatureCaisse(String string) {
        signatureCaisse = string;
    }

}
