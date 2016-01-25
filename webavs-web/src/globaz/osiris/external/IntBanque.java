package globaz.osiris.external;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Insérez la description du type ici. Date de création : (27.11.2001 14:53:28)
 * 
 * @author: Administrator
 */
public interface IntBanque extends BIEntity {
    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 14:55:58)
     * 
     * @return java.lang.String
     */
    String getClearing();

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 14:56:58)
     * 
     * @return java.lang.String
     */
    String getCodeSwift();

    /**
     * Retourne le code swift sans espaces.
     * 
     * @return
     */
    public String getCodeSwiftWithoutSpaces();

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 14:55:40)
     * 
     * @return java.lang.String
     */
    String getIdBanque();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.11.2001 07:40:04)
     * 
     * @return java.lang.String
     */
    String getIdTiers();

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 14:56:19)
     * 
     * @return java.lang.String
     */
    String getNumCCP();

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 16:05:23)
     * 
     * @return globaz.interfaceext.tiers.IntTiers
     */
    IntTiers getTiers();

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:08:22)
     */
    void retrieve(BITransaction transaction, String idBanque) throws java.lang.Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:08:22)
     */
    void retrieve(String idBanque) throws java.lang.Exception;
}
