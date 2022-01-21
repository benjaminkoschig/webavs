package globaz.osiris.eservices.dto.req;

import lombok.Data;

@Data
public class ESInfoFacturationREQ {
    private String affiliateNumber;
    private String role;
    private String startPeriod;
    private String endPeriod;
}
