package ch.globaz.orion.businessimpl.services.partnerWeb;

public class AfilliationForContactEb {
    private String numeroAffilie;
    private String dateDeRadiation;
    private String codeDeclaration;
    private String etape;

    public String getEtape() {
        return etape;
    }

    public void setEtape(String etape) {
        this.etape = etape;
    }

    public String getCodeDeclaration() {
        return codeDeclaration;
    }

    public void setCodeDeclaration(String codeDeclaration) {
        this.codeDeclaration = codeDeclaration;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getDateDeRadiation() {
        return dateDeRadiation;
    }

    public void setDateDeRadiation(String dateDeRadiation) {
        this.dateDeRadiation = dateDeRadiation;
    }

    @Override
    public String toString() {
        return "AfilliationForContactEb [numeroAffilie=" + numeroAffilie + ", dateDeRadiation=" + dateDeRadiation
                + ", codeDeclaration=" + codeDeclaration + ", etape=" + etape + "]";
    }
}
