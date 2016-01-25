package globaz.helios.db.interfaces;

/**
 * Interface pour les objets qui on un libelle multi langue (ex : CGMandat) voir aussi :
 * globaz.helios.db.utils.CGLibelle
 * 
 * Date de création : (16.10.2002 16:52:36)
 * 
 * @author: oca
 */
public interface CGLibelleInterface {
    public String getLibelle();

    public String getLibelleDe();

    public String getLibelleFr();

    public String getLibelleIt();

    public globaz.globall.db.BSession getSession();
}
