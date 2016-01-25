package globaz.hercule.utils;

import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.pavo.application.CIApplication;

/**
 * Classe d'utilitaire pour gestion des codes barres
 * 
 * @author MMO
 * @since 19 août 2010
 */
public class CECodeBarreUtils {

    public static String formatNumeroAffilie(String numeroAffilie) throws Exception {
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        IFormatData affilieFormater = application.getAffileFormater();
        if (affilieFormater == null) {
            throw new Exception(CECodeBarreUtils.class.getName() + "\n" + "L' Affilie Formater retourné est null");
        }
        return affilieFormater.format(numeroAffilie);
    }

    public static String giveAnnee(String codeBarre) {
        return codeBarre.substring(3, 7);
    }

    public static String giveIdDocument(String codeBarre) {
        return codeBarre.substring(0, 3);
    }

    public static String giveNumeroAffilie(String codeBarre) {
        return codeBarre.substring(7);
    }

    public static String giveNumeroAffilieFormate(String codeBarre) throws Exception {
        return formatNumeroAffilie(giveNumeroAffilie(codeBarre));
    }

}
