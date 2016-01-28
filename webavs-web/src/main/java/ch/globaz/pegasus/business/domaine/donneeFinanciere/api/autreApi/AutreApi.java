package ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiDegre;

public class AutreApi extends DonneeFinanciere implements Revenu {
    private final Montant montant;
    private final AutreApiType type;
    private final String libelleAutreApi;
    private final ApiDegre apiDegre;

    public AutreApi(Montant montant, AutreApiType type, ApiDegre apiDegre, String libelleAutreApi,
            DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");
        Checkers.checkNotNull(type, "autreApiType");
        Checkers.checkNotNull(libelleAutreApi, "libelleAutreApi");

        this.type = type;
        this.libelleAutreApi = libelleAutreApi;
        this.apiDegre = apiDegre;
        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public ApiDegre getApiDegre() {
        return apiDegre;
    }

    public AutreApiType getType() {
        return type;
    }

    public String getLibelleAutreApi() {
        return libelleAutreApi;
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
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.AUTRE_API;
    }

    @Override
    public String toString() {
        return "AutreApi [montant=" + montant + ", type=" + type + ", libelleAutreApi=" + libelleAutreApi
                + ", apiDegre=" + apiDegre + "parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((apiDegre == null) ? 0 : apiDegre.hashCode());
        result = prime * result + ((libelleAutreApi == null) ? 0 : libelleAutreApi.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        AutreApi other = (AutreApi) obj;
        if (apiDegre != other.apiDegre) {
            return false;
        }
        if (libelleAutreApi == null) {
            if (other.libelleAutreApi != null) {
                return false;
            }
        } else if (!libelleAutreApi.equals(other.libelleAutreApi)) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

}
