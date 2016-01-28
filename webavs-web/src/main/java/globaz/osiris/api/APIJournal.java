package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Insérez la description du type ici. Date de création : (15.01.2002 16:28:26)
 * 
 * @author: Administrator
 */
public interface APIJournal extends BIEntity {
    /**
     * Retourne le total des écritures contenues dans le journal. <br/>
     * Utiliser dans l'écran de détail et par AF.
     * 
     * @return
     */
    public String _getTotalEcritures();

    public String getDate();

    public String getDateValeurCG();

    public Boolean getEstPublic();

    public Boolean getEstVisibleImmediatement();

    public String getEtat();

    public String getIdJournal();

    public String getIdLog();

    public String getLibelle();

    public String getProprietaire();

    public String getTypeJournal();

    void retrieve() throws Exception;

    void retrieve(BITransaction transaction) throws Exception;

    void setIdJournal(String newIdJournal);
}
