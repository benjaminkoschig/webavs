/**
 * 
 */
package ch.globaz.perseus.business.models.qd;

import globaz.jade.client.util.JadeNumericUtil;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class QD extends ComplexQD {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String montantLimite;
    private String montantMaximalRemboursable;
    private String montantUtilise;
    private String montantUtiliseQdParente;
    private String montantLimiteQdParente;
    private ComplexQD qdParente = null;

    public QD() {
        super();
        qdParente = new ComplexQD();
    }

    /**
     * Retourne le montant limite de la qd ou celui de la qd supérieure si il n'y en a pas dans celle-là
     * 
     * @return the montantLimite
     */
    public String getMontantLimite() {
        if (JadeNumericUtil.isEmptyOrZero(getSimpleQD().getMontantLimite())) {
            return qdParente.getSimpleQD().getMontantLimite();
        } else {
            return getSimpleQD().getMontantLimite();
        }
    }

    public String getMontantUtiliseQdParente() {
        return montantUtiliseQdParente;
    }

    public void setMontantUtiliseQdParente(String montantUtiliseQdParente) {
        this.montantUtiliseQdParente = montantUtiliseQdParente;
    }

    public String getMontantLimiteQdParente() {
        return montantLimiteQdParente;
    }

    public void setMontantLimiteQdParente(String montantLimiteQdParente) {
        this.montantLimiteQdParente = montantLimiteQdParente;
    }

    /**
     * HACK: Appelle d'un service dans un get pour pouvoir utiliser le widget de completion de liste dans facture_de
     * 
     * @return le montant maximal remboursable
     */
    public String getMontantMaximalRemboursable() {
        try {
            return PerseusServiceLocator.getQDService().getMontantMaximalRemboursable(this).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Retourne le montant utilisé de la qd ou celui de la qd supérieure si il n'y en a pas dans celle-là
     * 
     * @return the montantUtilise
     */
    public String getMontantUtilise() {
        return getSimpleQD().getMontantUtilise();
    }

    /**
     * public void setMontantLimite(String montantLimite) { this.montantLimite = montantLimite; } pubpublic void
     * setMontantMaximalRemboursable(String montantMaximalRemboursable) { this.montantMaximalRemboursable =
     * montantMaximalRemboursable; } pubpublic void setMontantUtilise(String montantUtilise) { this.montantUtilise =
     * montantUtilise; } pub/**
     * 
     * @return the qdParente
     */
    public ComplexQD getQdParente() {
        return qdParente;
    }

    public void setMontantLimite(String montantLimite) {
        this.montantLimite = montantLimite;
    }

    public void setMontantMaximalRemboursable(String montantMaximalRemboursable) {
        this.montantMaximalRemboursable = montantMaximalRemboursable;
    }

    public void setMontantUtilise(String montantUtilise) {
        this.montantUtilise = montantUtilise;
    }

    /**
     * @param qdParente
     *            the qdParente to set
     */
    public void setQdParente(ComplexQD qdParente) {
        this.qdParente = qdParente;
    }

}
