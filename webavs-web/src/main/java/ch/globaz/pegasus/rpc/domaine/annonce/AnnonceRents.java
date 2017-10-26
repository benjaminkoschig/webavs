package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;

public class AnnonceRents {

    private static final String XSD_RENTCATEGORY_ANNUAL_GROSS = "ANNUAL_GROSS";
    private static final String XSD_RENTCATEGORY_RENTAL_VALUE = "RENTAL_VALUE";

    protected Montant grossRental;
    protected String rentCategory;
    protected Montant rentGrossTotal;
    protected Montant rentGrossTotalPart;
    protected Montant maxRent;

    public AnnonceRents(RpcDecisionAnnonceComplete annonce) {
        grossRental = annonce.getRpcCalcul().getLoyerBrutEnCompte();
        rentCategory = annonce.getMembresFamilleWithDonneesFinanciere().isLoyerValeurLocative() ? XSD_RENTCATEGORY_RENTAL_VALUE
                : XSD_RENTCATEGORY_ANNUAL_GROSS;
        rentGrossTotal = annonce.resolveLoyerTotalBrut();
        rentGrossTotalPart = annonce.getRpcCalcul().getPartLoyerTotatBrut();
        maxRent = annonce.getRpcCalcul().getLoyerMaximum();
    }

    public Montant getGrossRental() {
        return grossRental;
    }

    public String getRentCategory() {
        return rentCategory;
    }

    public Montant getRentGrossTotal() {
        return rentGrossTotal;
    }

    public Montant getRentGrossTotalPart() {
        return rentGrossTotalPart;
    }

    public Montant getMaxRent() {
        return maxRent;
    }
}
