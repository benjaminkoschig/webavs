package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (12.11.2002 08:43:34)
 * 
 * @author: ado
 */
public class HEParametrageannonceListViewBean extends HEParametrageannonceManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEParametrageAnnonceListViewBean.
     */
    public HEParametrageannonceListViewBean() {
        super();
    }

    /**
     * Format joliment les enregistrements<br>
     * 0 devient ""<br>
     * 1 devient 01
     * 
     * @param s
     *            la chaine � formatter
     * @return String la chaine formatt�e
     */
    public String formatEnregistrement(String s) {
        s = s.trim();
        if (s.equals("0")) {
            return "";
        } else {
            // la string s contient forc�ment un integer, mais on est
            // jamais trop prudent
            try {
                int intS = Integer.parseInt(s);
                if (intS <= 9) {
                    return "0" + intS;
                } else {
                    return s;
                }
            } catch (Exception e) { // ben voil�, c'�tait pas une valeur
                // num�rique !
                // on renvoit quand m�me quelque chose...
                e.printStackTrace();
                return s;
            }
        }
    }

    public String getCodeApplicationLibelle(int pos) throws Exception {
        HEParametrageannonce entity = (HEParametrageannonce) getEntity(pos);
        return entity.getCsCodeApplication().getCurrentCodeUtilisateur().getLibelle();
    }

    public String getCodeEnregistrementDebut(int pos) {
        HEParametrageannonce entity = (HEParametrageannonce) getEntity(pos);
        return formatEnregistrement(entity.getCodeEnregistrementDebut());
        // return "CodeEnregistrementDebut " + pos;
    }

    public String getCodeEnregistrementFin(int pos) {
        HEParametrageannonce entity = (HEParametrageannonce) getEntity(pos);
        return formatEnregistrement(entity.getCodeEnregistrementFin());
        // return "CodeEnregistrementFin " + pos;
    }

    public String getIdCodeApplication(int pos) throws Exception {
        HEParametrageannonce entity = (HEParametrageannonce) getEntity(pos);
        return entity.getCsCodeApplication().getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    public String getIdParametrageAnnonce(int pos) {
        HEParametrageannonce entity = (HEParametrageannonce) getEntity(pos);
        return entity.getIdParametrageAnnonce();
        // return pos + "";
    }
}
