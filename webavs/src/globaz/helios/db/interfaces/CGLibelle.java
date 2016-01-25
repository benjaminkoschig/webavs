package globaz.helios.db.interfaces;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Classe pour centraliser les methodes utiles à la gestion d'un libelle voir aussi CGLibelleInterface
 * 
 * Date de création : (16.10.2002 16:38:54)
 * 
 * @author: oca
 */
public abstract class CGLibelle {
    private static final String LANGUAGE_DE = "de";
    private static final String LANGUAGE_IT = "it";

    public static final String LIBELLE_ERROR = "?";

    private static String getLangueApp(CGLibelleInterface obj) {
        String langue = null;
        if ((obj != null) && (obj.getSession() != null)) {
            langue = obj.getSession().getIdLangueISO();
        }
        return langue;
    }

    /*
     * Methode permattant d'obtenir la langue d'un user depuis un objet CGLibelleInterface ATTENTION : Il faudra
     * modifier cette methode et l'interface pour le FW2 puisque la transaction ne fera plus partie de BEntity
     */

    public static String getLangueUser(CGLibelleInterface obj) {
        String langue = null;
        if ((obj != null) && (obj.getSession() != null)) {
            langue = obj.getSession().getIdLangueISO();
        }
        return langue;
    }

    /*
     * Methode permattant d'obtenir la langue d'un user depuis un objet CGLibelleInterface ATTENTION : Il faudra
     * modifier cette methode et l'interface pour le FW2 puisque la transaction ne fera plus partie de BEntity
     */

    /**
     * 
     * fonction pour avoir le libelle de langue de l'utilisateur pour un objet qui a le libelle en trois langue (ex :
     * CGMandat)
     * 
     */
    public static String getLibelleApp(CGLibelleInterface obj) {
        String langue = getLangueUser(obj);
        if (langue != null) {
            if (LANGUAGE_IT.equalsIgnoreCase(langue.toLowerCase())) {
                return obj.getLibelleIt();
            } else if (LANGUAGE_DE.equalsIgnoreCase(langue.toLowerCase())) {
                return obj.getLibelleDe();
            } else {
                return obj.getLibelleFr();
            }
        }
        return "";
    }

    /**
     * Utilisé depuis les ecrans de detail qui demande la saisie d'un libelle, permet d'afficher * à cote du libellé
     * obligatoire en fonction de la langue de l'utilisateur.
     * 
     */
    public static String getLibelleMandatory(CGLibelleInterface obj, String langueLibelle) {

        String langue = getLangueApp(obj);
        if ((langue != null) && (langue.equals(langueLibelle))) {
            return "*";
        }
        return "";
    }

    /**
     * 
     * fonction pour avoir le libelle de langue de l'utilisateur pour un objet qui a le libelle en trois langue (ex :
     * CGMandat)
     * 
     */
    public static String getLibelleUser(CGLibelleInterface obj) {
        String langue = getLangueUser(obj);
        if (langue != null) {
            if (LANGUAGE_IT.equalsIgnoreCase(langue.toLowerCase())) {
                return obj.getLibelleIt();
            } else if (LANGUAGE_DE.equalsIgnoreCase(langue.toLowerCase())) {
                return obj.getLibelleDe();
            } else {
                return obj.getLibelleFr();
            }
        }
        return "";
    }

    public static boolean isAppLanguageLibelleEmpty(CGLibelleInterface obj) {

        String langue = getLangueApp(obj);
        if (langue != null) {
            if (LANGUAGE_IT.equalsIgnoreCase(langue.toLowerCase())) {
                return JadeStringUtil.isBlank(obj.getLibelleIt());
            } else if (LANGUAGE_DE.equalsIgnoreCase(langue.toLowerCase())) {
                return JadeStringUtil.isBlank(obj.getLibelleDe());
            } else {
                return JadeStringUtil.isBlank(obj.getLibelleFr());
            }
        }
        return true;
    }

    public static boolean isUserLanguageLibelleEmpty(CGLibelleInterface obj) {

        String langue = getLangueUser(obj);
        if (langue != null) {
            if (LANGUAGE_IT.equalsIgnoreCase(langue.toLowerCase())) {
                return JadeStringUtil.isBlank(obj.getLibelleIt());
            } else if (LANGUAGE_DE.equalsIgnoreCase(langue.toLowerCase())) {
                return JadeStringUtil.isBlank(obj.getLibelleDe());
            } else {
                return JadeStringUtil.isBlank(obj.getLibelleFr());
            }
        }
        return true;
    }

    /**
     * Commentaire relatif au constructeur CGLibelleMultiLangue.
     */
    public CGLibelle() {
        super();

    }
}
