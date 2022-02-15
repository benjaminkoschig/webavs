package globaz.osiris.eservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeDateUtil;
import lombok.Data;

import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ESExtraitCompteDTO  {
    public static final int MAX_PERIOD = 60; // max 5 ans de données autorisées;
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
    private String libre1;
    private String libre2;
    private String libre3;

    public ESExtraitCompteDTO() {
    }

    @JsonIgnore
    public Boolean isValid() {
        return ((Stream.of(affiliateNumber, role, langue, selectionSections, selectionTris, operation, startPeriod, endPeriod, documentDate).allMatch(Objects::nonNull) && Stream.of(startPeriod, endPeriod, documentDate).allMatch(JadeDateUtil::isGlobazDate))
            || (Stream.of(affiliateNumber, role, langue, selectionSections, selectionTris, operation, startPeriod, endPeriod).allMatch(Objects::nonNull) && Stream.of(startPeriod, endPeriod).allMatch(JadeDateUtil::isGlobazDate)))
            && (JadeDateUtil.getNbMonthsBetween(startPeriod, endPeriod) <= MAX_PERIOD);
    }
}