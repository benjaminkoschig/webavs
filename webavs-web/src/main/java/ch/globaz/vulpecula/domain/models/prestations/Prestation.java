package ch.globaz.vulpecula.domain.models.prestations;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.registre.Convention;

/**
 * Interface d�finissant des m�thodes communes pour tous les types de prestations.
 */
public interface Prestation extends DomainEntity {
    /**
     * Retourne l'id de l'employeur auquel la prestation est associ�e.
     * 
     * @return String repr�sentant l'id de l'employeur
     */
    String getIdEmployeur();

    /**
     * Retourne l'id du travailleur auquel la prestation est associ�e.
     * 
     * @return String repr�sentant l'id du travailleur
     */
    String getIdTravailleur();

    /**
     * Retourne le num�ro d'affili� de l'employeur
     * 
     * @return String repr�sentant le nun�ro d'affii�
     */
    String getNoAffilie();

    /**
     * Retourne le nom et pr�nom du travailleur.
     * 
     * @return String repr�sentant le nom et pr�nom du travailleur
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
     * Retourne un code syst�me repr�sentant le type de prestation.
     * 
     * @return String repr�sentant un code syst�me de prestation
     */
    String getTypePrestation();

    /**
     * Retourne la p�riode au format String.
     * 
     * @return String repr�sentant la p�riode couverte par la prestation
     */
    String getPeriodeAsString();

    /**
     * Retourne la convention de l'employeur
     * 
     * @return Convention de l'employeur
     */
    Convention getConventionEmployeur();

    /**
     * Retourne le poste de travail rattach� � la prestation
     * 
     * @return
     */
    PosteTravail getPosteTravail();

    /**
     * Retourne la raison sociale de l'affili�e auquel l'absence justifi�e est rattach�e.
     * 
     * @return String repr�sentant le nom de la raison sociale
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
     * Retourne l'id du passage de facturation li� � la prestation
     * 
     * @return String repr�sentant l'id d'un passage de facturation
     */
    String getIdPassageFacturation();
}
