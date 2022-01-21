package globaz.osiris.eservices.dto.req;

import lombok.Data;

@Data
public class ESExtraitCompteREQ {
    private String affiliateNumber;
    private String role;
    private String langue;
    private String section;
    private String tri;
    private String operation;
    private String startPeriod;
    private String endPeriod;
    private String documentDate; // YYYY/MM/JJ
}