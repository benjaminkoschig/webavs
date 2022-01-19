package globaz.osiris.eservices.dto;

import lombok.Data;

import java.util.List;

@Data
public class ESSectionDTO {
    private String date;
    private String sectionNumber;
    private String description;
    private String dueDate; // YYYY/MM/JJ
	private String baseAmount;
	private String payedAmount;
	private String balancedAmount;
    private List<ESExtraitCompteDTO> ESExtraitCompteDTO;
}