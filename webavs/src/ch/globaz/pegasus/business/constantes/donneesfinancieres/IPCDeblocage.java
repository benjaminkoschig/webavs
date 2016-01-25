package ch.globaz.pegasus.business.constantes.donneesfinancieres;

public interface IPCDeblocage {
    // Etat deblocage
    public final static String CS_ETAT_COMPTABILISE = "64069003";
    public final static String CS_ETAT_ENREGISTRE = "64069001";
    public final static String CS_ETAT_VALIDE = "64069002";

    public final static String CS_TYPE_DEBLOCAGE_CREANCIER = "64070001";
    public final static String CS_TYPE_DEBLOCAGE_DETTE_EN_COMPTA = "64070002";
    public final static String CS_TYPE_DEBLOCAGE_VERSEMENT_BENEFICIAIRE = "64070003";
}
