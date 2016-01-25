package ch.globaz.vulpecula.domain.models.common;

/**
 * A domain entity as defined in the Domain-Driven Design literature.
 * 
 */
public interface DomainEntity {

    /**
     * Retourne l'identifiant de l'entit� de domaine.
     * 
     * @return String repr�sentant l'identifiant de l'entit� de domaine.
     */
    String getId();

    /**
     * Mise � jour de l'identifiant de l'entit� de domaine.
     * 
     * @param id
     *            String repr�sentant l'identifiant de l'entit� de domaine
     */
    void setId(String id);

    /**
     * Retourne le spy de l'entit� de domaine.
     * 
     * @return le spy de l'entit� de domaine
     */
    String getSpy();

    /**
     * Mise � jour du spy de l'entit� de domaine.
     * 
     * @param spy
     *            String repr�sentant le spy de l'entit� de domaine.
     */
    void setSpy(String spy);

}
