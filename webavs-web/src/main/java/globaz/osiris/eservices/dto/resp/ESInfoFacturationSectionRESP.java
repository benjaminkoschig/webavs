package globaz.osiris.eservices.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class ESInfoFacturationSectionRESP {
    private String date;
    private String sectionNumber;
    private String description;
    private String dueDate; // YYYY/MM/JJ
	private String baseAmount;
	private String payedAmount;
	private String balancedAmount;
    private List<ESInfoFacturationExtraitCompteRESP> infoFacturationExtraitCompte;
}