/**
 * 
 */
package ch.globaz.perseus.business.models.donneesfinancieres;

/**
 * Interface commune des donn�es financi�res Type (enum) FortuneType RevenuType DepenseReconnueType DetteType
 * 
 * @author DDE
 * 
 */
public interface DonneeFinanciereType {

    public Integer getId();

    @Override
    public String toString();

    public Boolean useSpecialisation();

}
