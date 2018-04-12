package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import ch.globaz.orion.ws.enums.SourceDecisionAcompteInd;
import ch.globaz.orion.ws.enums.StatusDecisionAcompteIndEnum;
import ch.globaz.orion.ws.enums.TypeDecisionAcompteInd;

public class DecisionAcompteInd {

    private String numAffilie;
    private Integer idDecisionWebAvs;
    private Integer annee;
    private BigDecimal resultatNet;
    private BigDecimal capitalInvestit;
    private Date dateSoumission;
    private StatusDecisionAcompteIndEnum status;
    private TypeDecisionAcompteInd type;
    private Integer typeWebAvs;
    private SourceDecisionAcompteInd source;
    private Date dateMisAJour;
    private String pspy;
    private String dateFacturationWebAvs;

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public Integer getIdDecisionWebAvs() {
        return idDecisionWebAvs;
    }

    public void setIdDecisionWebAvs(Integer idDecisionWebAvs) {
        this.idDecisionWebAvs = idDecisionWebAvs;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public BigDecimal getResultatNet() {
        return resultatNet;
    }

    public void setResultatNet(BigDecimal resultatNet) {
        this.resultatNet = resultatNet;
    }

    public BigDecimal getCapitalInvestit() {
        return capitalInvestit;
    }

    public void setCapitalInvestit(BigDecimal capitalInvestit) {
        this.capitalInvestit = capitalInvestit;
    }

    public Date getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(Date dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public StatusDecisionAcompteIndEnum getStatus() {
        return status;
    }

    public TypeDecisionAcompteInd getType() {
        return type;
    }

    public void setType(TypeDecisionAcompteInd type) {
        this.type = type;
    }

    public Integer getTypeWebAvs() {
        return typeWebAvs;
    }

    public void setTypeWebAvs(Integer typeWebAvs) {
        this.typeWebAvs = typeWebAvs;
    }

    public SourceDecisionAcompteInd getSource() {
        return source;
    }

    public void setSource(SourceDecisionAcompteInd source) {
        this.source = source;
    }

    public Date getDateMisAJour() {
        return dateMisAJour;
    }

    public void setDateMisAJour(Date dateMisAJour) {
        this.dateMisAJour = dateMisAJour;
    }

    public String getPspy() {
        return pspy;
    }

    public void setPspy(String pspy) {
        this.pspy = pspy;
    }

    public void setStatus(StatusDecisionAcompteIndEnum status) {
        this.status = status;
    }

    public String getDateFacturationWebAvs() {
        return dateFacturationWebAvs;
    }

    public void setDateFacturationWebAvs(String dateFacturationWebAvs) {
        this.dateFacturationWebAvs = dateFacturationWebAvs;
    }

    public void defineTypeDec() {
        // PROVISOIRE 605001, 605003
        // DEFINITIF 605002, 605004
        // ACCOMPTE 605007

        switch (typeWebAvs) {
            case 605001:
                type = TypeDecisionAcompteInd.PROVISOIRE;
                break;
            case 605003:
                type = TypeDecisionAcompteInd.PROVISOIRE;
                break;
            case 605002:
                type = TypeDecisionAcompteInd.DEFINITIF;
                break;
            case 605004:
                type = TypeDecisionAcompteInd.DEFINITIF;
                break;
            case 605007:
                type = TypeDecisionAcompteInd.ACCOMPTE;
                break;
            default:
                type = TypeDecisionAcompteInd.PROVISOIRE;
                break;

        }
    }

    public void formatDateFacturation() throws ParseException {
        if (!StringUtils.isEmpty(dateFacturationWebAvs)) {
            DateFormat format = new SimpleDateFormat("yyyyMMdd");
            dateSoumission = format.parse(dateFacturationWebAvs);
        }
    }

    public void formatDateMisAJour(String date) throws ParseException {
        if (!StringUtils.isEmpty(date)) {
            DateFormat format = new SimpleDateFormat("yyyyMMdd");
            dateSoumission = format.parse(date);
        }

    }
}
