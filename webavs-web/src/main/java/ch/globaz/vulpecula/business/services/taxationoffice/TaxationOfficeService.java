package ch.globaz.vulpecula.business.services.taxationoffice;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;

public interface TaxationOfficeService extends JadeApplicationService {
    void update(TaxationOffice taxationOffice);

    void devalider(String idTaxationOffice);

    void imprimer(String idTaxationOffice) throws Exception;

    /**
     * G�n�ration des lignes de taxation d'office pour les d�comptes pass�es en param�tre.
     * Cette m�thode va r�cup�rer les cotisations, taux et montant actuel pour cr�er la taxation d'office et ses lignes.
     * 
     * @param taxationsOffice Liste de d�comptes � g�n�rer
     */
    void genererTaxationsOffice(List<Decompte> decomptes);

    /**
     * Annulation d'une TO.
     * Si le d�compte est � l'�tat comptabilis�, on va extourner la section en comptabilit� auxiliaire.
     * 
     * @param taxationOffice Taxation d'office � extourner
     */
    void annuler(String idTaxation);

    /**
     * Annulation d'une TO.
     * Si le d�compte est � l'�tat comptabilis�, on va extourner la section en comptabilit� auxiliaire.
     * 
     * @param id d�compte
     */
    void annulerByIdDecompte(String idDecompte);

    /**
     * Annulation d'une TO.
     * Si la TO est � l'�tat comptabilis�, on va extourner la section en comptabilit� auxiliaire.
     * 
     * @param taxationOffice
     */
    void annulerTO(TaxationOffice taxationOffice);

    /**
     * Annulation d'une liste de TO contentu dans une periode.
     * Annule uniquement les TO sans salaire
     * Si la TO est � l'�tat comptabilis�, on va extourner la section en comptabilit� auxiliaire.
     * 
     * @param List listeTO
     */
    void annulerForPeriode(List<TaxationOffice> listeTO, Periode periode);

    /**
     * Retourne si l'employeur dispose d'une taxation office pour la date de d�but non annul�.
     * 
     * @param employeur Employeur sur lequel rechercher les taxations d'office
     * @param dateDebut Date de d�but sur laquelle rechercher la taxation d'office
     * @return true si poss�de taxation office active.
     */
    boolean hasTO(Employeur employeur, Date dateDebut);
}
