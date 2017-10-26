package ch.globaz.pegasus.business.domaine.donneeFinanciere;

public class DonneesFinancieresListBase<T extends DonneeFinanciere> extends
        DonneesFinancieresList<T, DonneesFinancieresListBase<T>> {

    public DonneesFinancieresListBase() {
        super(DonneesFinancieresListBase.class);
    }

}
