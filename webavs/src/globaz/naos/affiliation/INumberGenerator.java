package globaz.naos.affiliation;

import globaz.naos.db.affiliation.AFAffiliation;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (14.02.2003 14:14:06)
 * 
 * @author: Administrator
 */
public interface INumberGenerator {
    public String generateBeforeAdd(AFAffiliation affiliation) throws Exception;

    public String generateBeforeDisplay(AFAffiliation affiliation) throws Exception;

    public boolean isEditable(AFAffiliation affiliation) throws Exception;

}