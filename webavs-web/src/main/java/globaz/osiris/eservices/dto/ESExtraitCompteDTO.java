package globaz.osiris.eservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeDateUtil;
import lombok.Data;

import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ESExtraitCompteDTO  {
    private String affiliateNumber;
    private String role;
    private String langue;
    private String selectionSections;
    private String selectionTris;
    private String operation;
    private String startPeriod; // DD.MM.YYYY
    private String endPeriod; // DD.MM.YYYY
    private String documentDate; // DD.MM.YYYY
    private String document;

    public ESExtraitCompteDTO() {
    }

    @JsonIgnore
    public Boolean isValid() {
        return (Stream.of(affiliateNumber, role, langue, selectionSections, selectionTris, operation, startPeriod, endPeriod, documentDate).allMatch(Objects::nonNull) && Stream.of(startPeriod, endPeriod, documentDate).allMatch(JadeDateUtil::isGlobazDate))
            || (Stream.of(affiliateNumber, role, langue, selectionSections, selectionTris, operation, startPeriod, endPeriod).allMatch(Objects::nonNull) && Stream.of(startPeriod, endPeriod).allMatch(JadeDateUtil::isGlobazDate));
    }
}