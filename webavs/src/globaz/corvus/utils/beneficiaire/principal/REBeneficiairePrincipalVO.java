package globaz.corvus.utils.beneficiaire.principal;

import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.util.JADate;

public class REBeneficiairePrincipalVO {

    public JADate dateNaissance = null;
    public String idTiersBeneficiairePrincipal = "";
    // La rente accordée du bénéficiaire principal
    public RERenteAccordee ra = null;

    public JADate getDateNaissance() {
        return dateNaissance;
    }

    public String getIdTiersBeneficiairePrincipal() {
        return idTiersBeneficiairePrincipal;
    }

    public RERenteAccordee getRa() {
        return ra;
    }

    public void setDateNaissance(JADate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdTiersBeneficiairePrincipal(String idTiersBeneficiairePrincipal) {
        this.idTiersBeneficiairePrincipal = idTiersBeneficiairePrincipal;
    }

    public void setRa(RERenteAccordee ra) {
        this.ra = ra;
    }

}
