package ch.globaz.vulpecula.domain.models.prestations;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.registre.Convention;

/**
 * Interface définissant des méthodes communes pour tous les types de prestations.
 */
public interface Prestation extends DomainEntity {
    /**
     * Retourne l'id de l'employeur auquel la prestation est associée.
     * 
     * @return String représentant l'id de l'employeur
     */
    String getIdEmployeur();

    /**
     * Retourne l'id du travailleur auquel la prestation est associée.
     * 
     * @return String représentant l'id du travailleur
     */
    String getIdTravailleur();

    /**
     * Retourne le numéro d'affilié de l'employeur
     * 
     * @return String représentant le nunéro d'affiié
     */
    String getNoAffilie();

    /**
     * Retourne le nom et prénom du travailleur.
     * 
     * @return String représentant le nom et prénom du travailleur
     */
    String getNomPrenomTravailleur();

    /**
     * Retourne le montant de la prestation.
     * 
     * @return Montant de la prestation
     */
    Montant getMontant();

    /**
     * Retourne la qualification du poste de travail.
     * 
     * @return Qualification du poste de travail
     */
    Qualification getQualification();

    /**
     * Retourne le type de salaire du poste de travail.
     * 
     * @return Type de salaire du poste de travail
     */
    TypeSalaire getTypeSalaire();

    /**
     * Retourne un code système représentant le type de prestation.
     * 
     * @return String représentant un code système de prestation
     */
    String getTypePrestation();

    /**
     * Retourne la période au format String.
     * 
     * @return String représentant la période couverte par la prestation
     */
    String getPeriodeAsString();

    /**
     * Retourne la convention de l'employeur
     * 
     * @return Convention de l'employeur
     */
    Convention getConventionEmployeur();

    /**
     * Retourne le poste de travail rattaché à la prestation
     * 
     * @return
     */
    PosteTravail getPosteTravail();

    /**
     * Retourne la raison sociale de l'affiliée auquel l'absence justifiée est rattachée.
     * 
     * @return String représentant le nom de la raison sociale
     */
    String getRaisonSocialeEmployeur();

    Periode getPeriodeActivitePoste();

    /**
     * Retourne si la prestation est modifiable
     * 
     * @return true si modifiable
     */
    boolean isModifiable();

    /**
     * Retourne l'id du passage de facturation lié à la prestation
     * 
     * @return String représentant l'id d'un passage de facturation
     */
    String getIdPassageFacturation();
}
