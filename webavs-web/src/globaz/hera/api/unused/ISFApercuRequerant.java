package globaz.hera.api.unused;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * 
 * Permet d'obtenir les infos pour un requerant On passe l'idRequerant
 * 
 * <code>
 * 
 * 		System.out.println("Test Retrieve");
 * 		ISFApercuRequerant requerant = (ISFApercuRequerant) session.getAPIFor(ISFApercuRequerant.class);
 * 		requerant.setIdRequerant("108");
 * 		requerant.retrieve(transaction);
 * 		System.out.println("Found ? :" + !requerant.isNew());
 * 		System.out.println(requerant.getNom() + "," + requerant.getPrenom());
 * 
 * </code>
 * 
 * @author ado
 * 
 *         28 sept. 05
 */
public interface ISFApercuRequerant extends BIEntity {

    public String getDateDeces();

    // public void update(BITransaction transaction) throws Exception;

    public String getDateNaissance();

    public String getIdRequerant();

    public String getNationalite();

    public String getNom();

    public String getNumeroAvs();

    public String getPrenom();

    public String getSexe();

    // public void add(BITransaction transaction) throws Exception;
    // public void delete(BITransaction transaction) throws Exception;
    public void retrieve(BITransaction transaction) throws Exception;

    /*
     * public void setDateDeces(String prop); public void setDateNaissance(String prop);
     */
    public void setIdRequerant(String idRequerant);
    /*
     * public void setNationalite(String prop); public void setNom(String prop); public void setNumeroAvs(String prop);
     * public void setPrenom(String prop); public void setSexe(String prop);
     */

}
