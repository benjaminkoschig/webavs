package ch.globaz.vulpecula.domain.models.common;

/**
 * A domain entity as defined in the Domain-Driven Design literature.
 * 
 */
public interface DomainEntity {

    /**
     * Retourne l'identifiant de l'entité de domaine.
     * 
     * @return String représentant l'identifiant de l'entité de domaine.
     */
    String getId();

    /**
     * Mise à jour de l'identifiant de l'entité de domaine.
     * 
     * @param id
     *            String représentant l'identifiant de l'entité de domaine
     */
    void setId(String id);

    /**
     * Retourne le spy de l'entité de domaine.
     * 
     * @return le spy de l'entité de domaine
     */
    String getSpy();

    /**
     * Mise à jour du spy de l'entité de domaine.
     * 
     * @param spy
     *            String représentant le spy de l'entité de domaine.
     */
    void setSpy(String spy);

}
