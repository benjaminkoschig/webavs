package ch.globaz.vulpecula.business.services.taxationoffice;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;

public interface TaxationOfficeService extends JadeApplicationService {
    void update(TaxationOffice taxationOffice);

    void devalider(String idTaxationOffice);

    void imprimer(String idTaxationOffice) throws Exception;

    /**
     * Génération des lignes de taxation d'office pour les décomptes passées en paramètre.
     * Cette méthode va récupérer les cotisations, taux et montant actuel pour créer la taxation d'office et ses lignes.
     * 
     * @param taxationsOffice Liste de décomptes à générer
     */
    void genererTaxationsOffice(List<Decompte> decomptes);

    /**
     * Annulation d'un décompte.
     * Si le décompte est à l'état comptabilisé, on va extourner la section en comptabilité auxiliaire.
     * 
     * @param taxationOffice Taxation d'office à extourner
     */
    void annuler(String idTaxation);

    /**
     * Retourne si l'employeur dispose d'une taxation office pour la date de début non annulé.
     * 
     * @param employeur Employeur sur lequel rechercher les taxations d'office
     * @param dateDebut Date de début sur laquelle rechercher la taxation d'office
     * @return true si possède taxation office active.
     */
    boolean hasTO(Employeur employeur, Date dateDebut);
}
