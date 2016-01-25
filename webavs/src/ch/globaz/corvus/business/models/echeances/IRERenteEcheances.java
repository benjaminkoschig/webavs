package ch.globaz.corvus.business.models.echeances;

import java.util.Set;

public interface IRERenteEcheances {

    public String getAnneeAnticipation();

    public String getCodePrestation();

    public Set<String> getCodesCasSpeciaux();

    public String getCsEtat();

    public String getCsEtatDemandeRente();

    public String getCsGenreDroitApi();

    public String getCsTypeInfoComplementaire();

    public String getDateDebutDroit();

    public String getDateEcheance();

    public String getDateFinDroit();

    public String getDateRevocationAjournement();

    public String getIdPrestationAccordee();

    public String getIdTiersBeneficiaire();

    public String getMontant();

    public boolean isPrestationBloquee();
}