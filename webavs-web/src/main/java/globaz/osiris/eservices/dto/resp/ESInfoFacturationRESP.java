package globaz.osiris.eservices.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class ESInfoFacturationRESP {
    private String affiliateNumber;
    private String role;
    private String startPeriod;
    private String endPeriod;
    private List<ESInfoFacturationSectionRESP> infoFacturationSection;
}
