package ch.globaz.common.domaine.repository;

import java.io.Serializable;

public interface DomainEntity extends Serializable {
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
