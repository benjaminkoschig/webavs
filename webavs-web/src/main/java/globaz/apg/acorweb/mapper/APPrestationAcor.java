package globaz.apg.acorweb.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import globaz.framework.util.FWCurrency;
import lombok.Getter;
import lombok.Setter;

public class APPrestationAcor {
    @Getter
    @Setter
    private String idAssure;
    @Getter
    @Setter
    LocalDate moisComptable;
    @Getter
    @Setter
    private LocalDate dateDebut;
    @Getter
    @Setter
    private LocalDate dateFin;
    @Getter
    @Setter
    BigDecimal tauxJour;
    @Getter
    @Setter
    BigDecimal tauxJourBase;
    @Getter
    @Setter
    FWCurrency montantBrut;
    @Getter
    @Setter
    FWCurrency allocationExploitation;
    @Getter
    @Setter
    FWCurrency allocationJournalier;
    @Getter
    @Setter
    FWCurrency fraisGarde;
    @Getter
    @Setter
    FWCurrency fraisGardeMax;
    @Getter
    @Setter
    FWCurrency versementAssure;
    @Getter
    @Setter
    Integer nombreJoursSoldes;
    @Getter
    @Setter
    Integer nombreJoursSupplementaires;
    @Getter
    @Setter
    FWCurrency revenuDeterminantMoyen;
    @Getter
    @Setter
    String genre;
    @Getter
    @Setter
    String revision;
    @Getter
    @Setter
    boolean soumisImpotSource;
    @Getter
    @Setter
    String idTauxImposition;
    @Getter
    @Setter
    String tauxImposition;

    @Getter
    List<APRepartitionPaiementAcor> repartitionPaiements;

    public APPrestationAcor(){
        repartitionPaiements = new ArrayList<>();
    }

}
