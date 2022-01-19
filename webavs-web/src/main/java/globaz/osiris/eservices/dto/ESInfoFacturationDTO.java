package globaz.osiris.eservices.dto;

import lombok.Data;

import java.util.List;
@Data
public class ESInfoFacturationDTO {
    private String affiliateNumber;
    private String role;
    private String startPeriod;
    private String endPeriod;
    private List<ESSectionDTO> ESSectionDTO;
    private String document;
}
