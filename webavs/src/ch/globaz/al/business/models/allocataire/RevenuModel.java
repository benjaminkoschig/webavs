package ch.globaz.al.business.models.allocataire;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Revenu d'un allocataire ou de son conjoint à un moment donné
 * 
 * @author jts
 * 
 */
public class RevenuModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Date
     */
    private String date = null;
    /**
     * Identifiant de l'allocataire
     */
    private String idAllocataire = null;

    /**
     * Identifiant du revenu
     */
    private String idRevenu = null;

    /**
     * Montant
     */
    private String montant = null;

    /**
     * Revenu du conjoint
     */
    private Boolean revenuConjoint = null;

    /**
     * Revenu Impot fédéral direct
     */
    private Boolean revenuIFD = null;

    /**
     * Retourne la date de saisie du revenu. Dans le cas d'un revenu annuel il s'agit d'une date au 31 décembre de
     * l'année concernée
     * 
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Retourne l'id du revenu
     */
    @Override
    public String getId() {
        return idRevenu;
    }

    /**
     * Retourne l'id de l'allocataire auquel appartient le revenu
     * 
     * @return the idAllocataire
     */
    public String getIdAllocataire() {
        return idAllocataire;
    }

    /**
     * 
     * @return idRevenu
     */

    public String getIdRevenu() {
        return idRevenu;
    }

    /**
     * Retourne le montant du revenu. Il peut s'agir d'un montant mensuel ou annuel
     * 
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * Indique si le revenu est celui de l'allocataire ou celui de sont conjoint
     * <ul>
     * <li>true : revenu du conjoint</li>
     * <li>false : revenu de l'allocataire</li>
     * </ul>
     * 
     * @return the revenuConjoint
     */
    public Boolean getRevenuConjoint() {
        return revenuConjoint;
    }

    /**
     * Indique si le revenu est un revenu IFD
     * 
     * @return the revenuIFD
     */
    public Boolean getRevenuIFD() {
        return revenuIFD;
    }

    /**
     * Définit la date de saisie du revenu. Dans le cas d'un revenu annuel il s'agit d'une date au 31 décembre de
     * l'année concernée
     * 
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Définit l'id du revenu
     */
    @Override
    public void setId(String id) {
        idRevenu = id;
    }

    /**
     * Définit l'id de l'allocataire auquel le revenu appartient
     * 
     * @param idAllocataire
     *            the idAllocataire to set
     */
    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }

    /**
     * 
     * @param id
     *            : the idRevenu to set
     */

    public void setIdRevenu(String id) {
        idRevenu = id;
    }

    /**
     * Définit le montant du revenu
     * 
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * Permet d'indiquer si le revenu est celui de l'allocataire ou celui de sont conjoint
     * <ul>
     * <li>true : revenu du conjoint</li>
     * <li>false : revenu de l'allocataire</li>
     * </ul>
     * 
     * @param revenuConjoint
     *            the revenuConjoint to set
     */
    public void setRevenuConjoint(Boolean revenuConjoint) {
        this.revenuConjoint = revenuConjoint;
    }

    /**
     * Permet d'indiquer si le revenu est un revenu IFD
     * 
     * @param revenuIFD
     *            the revenuIFD to set
     */
    public void setRevenuIFD(Boolean revenuIFD) {
        this.revenuIFD = revenuIFD;
    }
}