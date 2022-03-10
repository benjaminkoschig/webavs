package globaz.osiris.eservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ESInfoFacturationDTO {
    private String affiliateNumber;
    private String role;
    private String langue = ""; // Non mandatory Field
    private String selectionSections;
    private String selectionTris;
    private String operation = CAExtraitCompteManager.FOR_ALL_IDTYPEOPERATION;
    private String startPeriod; // Format DD.MM.YYYY
    private String endPeriod; // Format DD.MM.YYYY
    private String libre1 = ""; // Non mandatory Field
    private String libre2 = ""; // Non mandatory Field
    private String libre3 = ""; // Non mandatory Field
    private List<ESInfoFacturationSectionDTO> sections = new ArrayList();

    public ESInfoFacturationDTO() {
    }

    @JsonIgnore
    public Boolean isValid() {
        return (Stream.of(affiliateNumber, role, selectionSections, selectionTris, operation, startPeriod, endPeriod).noneMatch(JadeStringUtil::isEmpty) && Stream.of(startPeriod, endPeriod).allMatch(JadeDateUtil::isGlobazDate))
            && ESValidateDTO.isValid(role, langue, selectionSections, selectionTris, operation, startPeriod, endPeriod);
    }

    @Data
    public class ESInfoFacturationSectionDTO {
        private String date; // Format DD.MM.YYYY
        private String sectionNumber;
        private String description;
        private String dueDate; // Format DD.MM.YYYY
        private String baseAmount;
        private String pmtCmpAmount;
        private String solde;
        private List<ESInfoFacturationLigneExtraitCompteDTO> ligneExtraitComptes = new ArrayList();

        @JsonIgnore
        public Boolean isValid() {
            return Stream.of(date, sectionNumber, description, dueDate, baseAmount, pmtCmpAmount, solde).noneMatch(JadeStringUtil::isEmpty)
                    && Stream.of(date, dueDate).allMatch(JadeDateUtil::isGlobazDate);
        }
    }

    @Data
    public class ESInfoFacturationLigneExtraitCompteDTO {
        private String dateComptable; // Format DD.MM.YYYY
        private String dateValeur; // Format DD.MM.YYYY
        private String description;
        private String doit;
        private String avoir;
        private String solde;

        @JsonIgnore
        public Boolean isValid() {
            return Stream.of(dateComptable, dateValeur, description, doit, avoir, solde).noneMatch(JadeStringUtil::isEmpty)
                    && Stream.of(dateComptable, dateValeur).allMatch(JadeDateUtil::isGlobazDate);
        }
    }
}
