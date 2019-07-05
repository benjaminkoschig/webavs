package ch.globaz.orion.business.models.af;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import ch.globaz.xmlns.eb.recapaf.Partner;

public class RecapAf {

    private Integer idRecap;
    private Partner partner;
    private Date anneeMoisRecap;
    private String anneeMoisRecapStr;
    private Date dateMiseADisposition;
    private String dateMiseADispositionStr;
    private Date lastModificationDate;
    private String lastModificationDateStr;
    private StatutRecapAfWebAvsEnum statut;
    private Boolean aucunChangement;
    private Boolean controleManuelle;

    private static final DateFormat moisAnneeFormat = new SimpleDateFormat("MM.yyyy");
    private static final DateFormat jourMoisAnneeFormat = new SimpleDateFormat("dd.MM.yyyy");

    public RecapAf() {

    }

    public RecapAf(Integer idRecap, Partner partner, Date anneeMoisRecap, Date dateMiseADisposition,
            Date lastModificationDate, StatutRecapAfWebAvsEnum statut, Boolean aucunChangement, Boolean controleManuelle) {
        this.idRecap = idRecap;
        this.partner = partner;
        this.anneeMoisRecap = anneeMoisRecap;
        anneeMoisRecapStr = moisAnneeFormat.format(anneeMoisRecap);
        this.dateMiseADisposition = dateMiseADisposition;
        dateMiseADispositionStr = jourMoisAnneeFormat.format(dateMiseADisposition);
        this.lastModificationDate = lastModificationDate;
        lastModificationDateStr = jourMoisAnneeFormat.format(lastModificationDate);
        this.statut = statut;
        this.aucunChangement = aucunChangement;
        this.controleManuelle = controleManuelle;
    }

    public Integer getIdRecap() {
        return idRecap;
    }

    public void setIdRecap(Integer idRecap) {
        this.idRecap = idRecap;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Date getAnneeMoisRecap() {
        return anneeMoisRecap;
    }

    public void setAnneeMoisRecap(Date anneeMoisRecap) {
        this.anneeMoisRecap = anneeMoisRecap;
    }

    public Date getDateMiseADisposition() {
        return dateMiseADisposition;
    }

    public void setDateMiseADisposition(Date dateMiseADisposition) {
        this.dateMiseADisposition = dateMiseADisposition;
    }

    public StatutRecapAfWebAvsEnum getStatut() {
        return statut;
    }

    public void setStatut(StatutRecapAfWebAvsEnum statut) {
        this.statut = statut;
    }

    public Boolean getAucunChangement() {
        return aucunChangement;
    }

    public void setAucunChangement(Boolean aucunChangement) {
        this.aucunChangement = aucunChangement;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getAnneeMoisRecapStr() {
        return anneeMoisRecapStr;
    }

    public void setAnneeMoisRecapStr(String anneeMoisRecapStr) {
        this.anneeMoisRecapStr = anneeMoisRecapStr;
    }

    public String getDateMiseADispositionStr() {
        return dateMiseADispositionStr;
    }

    public void setDateMiseADispositionStr(String dateMiseADispositionStr) {
        this.dateMiseADispositionStr = dateMiseADispositionStr;
    }

    public String getLastModificationDateStr() {
        return lastModificationDateStr;
    }

    public void setLastModificationDateStr(String lastModificationDateStr) {
        this.lastModificationDateStr = lastModificationDateStr;
    }

    public Boolean getControleManuelle() {
        return controleManuelle;
    }

    public void setControleManuelle(Boolean controleManuelle) {
        this.controleManuelle = controleManuelle;
    }

}
