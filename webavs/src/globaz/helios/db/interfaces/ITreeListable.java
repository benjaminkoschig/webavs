package globaz.helios.db.interfaces;

import globaz.globall.db.BManager;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (04.12.2002 16:46:22)
 * 
 * @author: Administrator
 */
public interface ITreeListable {
    public BManager[] getChilds() throws Exception;

    public String getId();

    public String getLibelle();
}
