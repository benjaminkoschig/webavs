package ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiDegre;

@SuppressWarnings("serial")
public class ApiAvsAi extends DonneeFinanciere implements Revenu {
    private final Montant montant;
    private final ApiType typeApi;
    private final ApiDegre apiDegre;

    public ApiAvsAi(Montant montant, ApiType typeApi, ApiDegre apiDegre, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");
        Checkers.checkNotNull(typeApi, "typeApi");
        this.typeApi = typeApi;
        this.apiDegre = apiDegre;
        this.montant = montant.addMensuelPeriodicity();
    }

    public ApiDegre getApiDegre() {
        return apiDegre;
    }

    public Montant getMontant() {
        return montant;
    }

    public ApiType getTypeApi() {
        return typeApi;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.API_AVS_AI;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant.annualise();
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.annualise();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((apiDegre == null) ? 0 : apiDegre.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((typeApi == null) ? 0 : typeApi.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApiAvsAi other = (ApiAvsAi) obj;
        if (apiDegre != other.apiDegre) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (typeApi != other.typeApi) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ApiAvsAi [montant=" + montant + ", typeApi=" + typeApi + ", apiDegre=" + apiDegre + "]";
    }

}
