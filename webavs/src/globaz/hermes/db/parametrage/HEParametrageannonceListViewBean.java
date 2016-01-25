package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (12.11.2002 08:43:34)
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
     *            la chaine à formatter
     * @return String la chaine formattée
     */
    public String formatEnregistrement(String s) {
        s = s.trim();
        if (s.equals("0")) {
            return "";
        } else {
            // la string s contient forcément un integer, mais on est
            // jamais trop prudent
            try {
                int intS = Integer.parseInt(s);
                if (intS <= 9) {
                    return "0" + intS;
                } else {
                    return s;
                }
            } catch (Exception e) { // ben voilà, c'était pas une valeur
                // numérique !
                // on renvoit quand même quelque chose...
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
