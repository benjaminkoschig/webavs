package globaz.osiris.eservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import lombok.Data;

import java.util.stream.Stream;

@Data
public class ESExtraitCompteDTO  {
    private String affiliateNumber;
    private String role;
    private String langue = ""; // Non mandatory Field
    private String selectionSections;
    private String selectionTris;
    private String operation = CAExtraitCompteManager.FOR_ALL_IDTYPEOPERATION;
    private String startPeriod; // Format DD.MM.YYYY
    private String endPeriod; // Format DD.MM.YYYY
    private String documentDate = ""; // Format DD.MM.YYYY // Non mandatory Field
    private String document = ""; // Non mandatory Field
    private String libre1 = ""; // Non mandatory Field
    private String libre2 = ""; // Non mandatory Field
    private String libre3 = ""; // Non mandatory Field

    public ESExtraitCompteDTO() {
    }

    @JsonIgnore
    public Boolean isValid() {
        return (Stream.of(affiliateNumber, role, selectionSections, selectionTris, operation, startPeriod, endPeriod).noneMatch(JadeStringUtil::isEmpty) && Stream.of(startPeriod, endPeriod).allMatch(JadeDateUtil::isGlobazDate))
            && (JadeStringUtil.isEmpty(documentDate) || (!JadeStringUtil.isEmpty(documentDate) && (JadeDateUtil.isGlobazDate(documentDate))))
            && ESValidateDTO.isValid(role, langue, selectionSections, selectionTris, operation, startPeriod, endPeriod);
    }
}