package globaz.osiris.eservices.dto.resp;

import lombok.Data;

@Data
public class ESInfoFacturationExtraitCompteRESP {
    private String dateComptable; // YYYY/MM/JJ
    private String dateValeur; // YYYY/MM/JJ
    private String description;
    private String doit;
	private String avoir;
	private String solde;
}