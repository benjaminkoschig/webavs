package ch.globaz.al.business.constantes.enumerations.echeances;

/**
 * 
 * @author ebko
 * 
 */
public enum ALEnumDocumentMode {

    PDF("PDF"),
    EXCEL("EXCEL");
    
    private String value; 
    
    ALEnumDocumentMode(String value){
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

}
