package globaz.osiris.eservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeDateUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ESInfoFacturationDTO {
    private String affiliateNumber;
    private String role;
    private String langue;
    private String selectionSections;
    private String selectionTris;
    private String startPeriod; // DD.MM.YYYY
    private String endPeriod; // DD.MM.YYYY
    private List<ESInfoFacturationSectionDTO> sections = new ArrayList();

    public ESInfoFacturationDTO() {
    }

    @JsonIgnore
    public Boolean isValid() {
        return (Stream.of(affiliateNumber, role, langue, selectionSections, selectionTris, startPeriod, endPeriod).allMatch(Objects::nonNull) && Stream.of(startPeriod, endPeriod).allMatch(JadeDateUtil::isGlobazDate))
            || (Stream.of(affiliateNumber, role, langue, selectionSections, selectionTris, startPeriod).allMatch(Objects::nonNull) && Stream.of(startPeriod).allMatch(JadeDateUtil::isGlobazDate))
            || (Stream.of(affiliateNumber, role, langue, selectionSections, selectionTris, endPeriod).allMatch(Objects::nonNull) && Stream.of(endPeriod).allMatch(JadeDateUtil::isGlobazDate));
    }

    @Data
    public class ESInfoFacturationSectionDTO {
        private String date; // DD.MM.YYYY
        private String sectionNumber;
        private String description;
        private String dueDate; // DD.MM.YYYY
        private String baseAmount;
        private String pmtCmpAmount;
        private String solde;
        private List<ESInfoFacturationLigneExtraitCompteDTO> ligneExtraitComptes = new ArrayList();

        @JsonIgnore
        public Boolean isValid() {
            return Stream.of(date, sectionNumber, description, dueDate, baseAmount, pmtCmpAmount, solde).allMatch(Objects::nonNull)
                    && Stream.of(date, dueDate).allMatch(JadeDateUtil::isGlobazDate);
        }
    }

    @Data
    public class ESInfoFacturationLigneExtraitCompteDTO {
        private String dateComptable; // DD.MM.YYYY
        private String dateValeur; // DD.MM.YYYY
        private String description;
        private String doit;
        private String avoir;
        private String solde;

        @JsonIgnore
        public Boolean isValid() {
            return Stream.of(dateComptable, dateValeur, description, doit, avoir, solde).allMatch(Objects::nonNull)
                    && Stream.of(dateComptable, dateValeur).allMatch(JadeDateUtil::isGlobazDate);
        }
    }
}
