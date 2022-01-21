package globaz.osiris.eservices.dto.resp;

import lombok.Data;

@Data
public class ESExtraitCompteRESP {
    private String affiliateNumber;
    private String role;
    private String langue;
    private String sections;
    private String tri;
    private String operations;
    private String startPeriod;
    private String endPeriod;
    private String documentDate; // YYYY/MM/JJ
    private String document;
}