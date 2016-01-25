/*
 * Créé le 21 juin 06
 */
package globaz.apg.api.prestation;

/**
 * @author hpe
 * 
 *         Helper de liaison entre APG et APG Interface pour les prestations
 * 
 */
public interface IAPPrestationLoader {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.prestation.IAPPrestation#load(java.lang.String, java.lang.String, java.lang.String)
     */
    public IAPPrestation[] load(String idDroit, String genrePrestation, String orderBy) throws Exception;
}
