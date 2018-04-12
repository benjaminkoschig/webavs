package ch.globaz.orion.ws.affiliation;

import java.util.Date;
import ch.globaz.orion.ws.enums.ModeDeclarationSalaireWebAvs;

public class InfosAffiliation {
    private Integer idAffilie;
    private String numeroAffilie;
    private String raisonSociale;
    private ModeDeclarationSalaireWebAvs modeDeclarationSalaireWebAvs;
    private Date dateDebutAffiliation;
    private Date dateFinAffiliation;
    private AffiliationAdresse adresseCourrier;
    private AffiliationAdresse adresseDomicile;

    public InfosAffiliation() {
        // need for jaxws
    }

    public InfosAffiliation(Integer idAffilie, String numeroAffilie, String raisonSociale,
            ModeDeclarationSalaireWebAvs modeDeclarationSalaireWebAvs, Date dateDebutAffiliation,
            Date dateFinAffiliation, AffiliationAdresse adresseCourrier, AffiliationAdresse adresseDomicile) {
        super();
        this.idAffilie = idAffilie;
        this.numeroAffilie = numeroAffilie;
        this.raisonSociale = raisonSociale;
        this.modeDeclarationSalaireWebAvs = modeDeclarationSalaireWebAvs;
        this.dateDebutAffiliation = dateDebutAffiliation;
        this.dateFinAffiliation = dateFinAffiliation;
        this.adresseCourrier = adresseCourrier;
        this.adresseDomicile = adresseDomicile;
    }

    public Integer getIdAffilie() {
        return idAffilie;
    }

    public void setIdAffilie(Integer idAffilie) {
        this.idAffilie = idAffilie;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public ModeDeclarationSalaireWebAvs getModeDeclarationSalaireWebAvs() {
        return modeDeclarationSalaireWebAvs;
    }

    public void setModeDeclarationSalaireWebAvs(ModeDeclarationSalaireWebAvs modeDeclarationSalaireWebAvs) {
        this.modeDeclarationSalaireWebAvs = modeDeclarationSalaireWebAvs;
    }

    public Date getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public void setDateDebutAffiliation(Date dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public Date getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public void setDateFinAffiliation(Date dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public AffiliationAdresse getAdresseCourrier() {
        return adresseCourrier;
    }

    public void setAdresseCourrier(AffiliationAdresse adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    public AffiliationAdresse getAdresseDomicile() {
        return adresseDomicile;
    }

    public void setAdresseDomicile(AffiliationAdresse adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
    }

}
