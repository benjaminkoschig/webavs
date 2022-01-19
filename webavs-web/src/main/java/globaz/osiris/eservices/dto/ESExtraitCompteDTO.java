package globaz.osiris.eservices.dto;

import lombok.Data;

@Data
public class ESExtraitCompteDTO {
    private String dateComptable; // YYYY/MM/JJ
    private String dateValeur;  // YYYY/MM/JJ
    private String description;
    private String doit; // YYYY/MM/JJ
	private String avoir;
	private String solde;
}