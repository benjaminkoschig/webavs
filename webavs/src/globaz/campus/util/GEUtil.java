package globaz.campus.util;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.pavo.application.CIApplication;
import globaz.phenix.application.CPApplication;
import globaz.pyxis.application.TIApplication;

public class GEUtil {
    public final static String SESSION_NAOS_KEY = "sessionNaos";
    public final static String SESSION_PAVO_KEY = "sessionPhenix";
    public final static String SESSION_PHENIX_KEY = "sessionPhenix";
    public final static String SESSION_PYXIS_KEY = "sessionPyxis";

    public static final String convertSpecialChars(String s) {
        if (JadeStringUtil.isEmpty(s)) {
            return s;
        }
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            if (Character.isLetter(sb.charAt(i))) {
                switch (sb.charAt(i)) {
                    case 'á':
                    case 'à':
                    case 'â':
                    case 'ã':
                    case 'ä':
                        sb.setCharAt(i, 'a');
                        break;
                    case 'é':
                    case 'è':
                    case 'ê':
                    case 'ë':
                        sb.setCharAt(i, 'e');
                        break;
                    case 'í':
                    case 'ì':
                    case 'î':
                    case 'ï':
                        sb.setCharAt(i, 'i');
                        break;
                    case 'ó':
                    case 'ò':
                    case 'ô':
                    case 'ö':
                    case 'õ':
                        sb.setCharAt(i, 'o');
                        break;
                    case 'ú':
                    case 'ù':
                    case 'û':
                    case 'ü':
                        sb.setCharAt(i, 'u');
                        break;
                    case 'ç':
                        sb.setCharAt(i, 'c');
                        break;
                    case 'ñ':
                        sb.setCharAt(i, 'n');
                        break;
                    case 'æ':
                        sb.setCharAt(i, 'a');
                        break;
                    case 'Á':
                    case 'À':
                    case 'Â':
                    case 'Ä':
                    case 'Ã':
                        sb.setCharAt(i, 'A');
                        break;
                    case 'É':
                    case 'È':
                    case 'Ê':
                    case 'Ë':
                        sb.setCharAt(i, 'E');
                        break;
                    case 'Í':
                    case 'Ì':
                    case 'Î':
                    case 'Ï':
                        sb.setCharAt(i, 'I');
                        break;
                    case 'Ó':
                    case 'Ò':
                    case 'Ô':
                    case 'Ö':
                    case 'Õ':
                        sb.setCharAt(i, 'O');
                        break;
                    case 'Ú':
                    case 'Ù':
                    case 'Û':
                    case 'Ü':
                        sb.setCharAt(i, 'U');
                        break;
                    case 'Ç':
                        sb.setCharAt(i, 'C');
                        break;
                    case 'Ñ':
                        sb.setCharAt(i, 'N');
                        break;
                    case 'Æ':
                        sb.setCharAt(i, 'A');
                        break;
                }
            }
        }
        return sb.toString();
    }

    public static final String convertSpecialCharsWithE(String s) {
        if (JadeStringUtil.isEmpty(s)) {
            return s;
        }
        // StringBuffer nouvelleChaine = new StringBuffer(s);
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            if (Character.isLetter(sb.charAt(i))) {
                switch (sb.charAt(i)) {
                    case 'á':
                    case 'à':
                    case 'â':
                    case 'ã':
                        sb.setCharAt(i, 'a');
                        break;
                    case 'ä':
                        sb.setCharAt(i, 'a');
                        sb.insert(i + 1, 'e');
                        break;
                    case 'é':
                    case 'è':
                    case 'ê':
                        sb.setCharAt(i, 'e');
                        break;
                    case 'ë':
                        sb.setCharAt(i, 'e');
                        sb.insert(i + 1, 'e');
                        break;
                    case 'í':
                    case 'ì':
                    case 'î':
                        sb.setCharAt(i, 'i');
                        break;
                    case 'ï':
                        sb.setCharAt(i, 'i');
                        sb.insert(i + 1, 'e');
                        break;
                    case 'ó':
                    case 'ò':
                    case 'ô':
                    case 'õ':
                        sb.setCharAt(i, 'o');
                        break;
                    case 'ö':
                        sb.setCharAt(i, 'o');
                        sb.insert(i + 1, 'e');
                        break;
                    case 'ú':
                    case 'ù':
                    case 'û':
                        sb.setCharAt(i, 'u');
                        break;
                    case 'ü':
                        sb.setCharAt(i, 'u');
                        sb.insert(i + 1, 'e');
                        break;
                    case 'ç':
                        sb.setCharAt(i, 'c');
                        break;
                    case 'ñ':
                        sb.setCharAt(i, 'n');
                        break;
                    case 'æ':
                        sb.setCharAt(i, 'a');
                        break;
                    case 'Á':
                    case 'À':
                    case 'Â':
                    case 'Ã':
                        sb.setCharAt(i, 'A');
                        break;
                    case 'Ä':
                        sb.setCharAt(i, 'A');
                        sb.insert(i + 1, 'E');
                        break;
                    case 'É':
                    case 'È':
                    case 'Ê':
                        sb.setCharAt(i, 'E');
                        break;
                    case 'Ë':
                        sb.setCharAt(i, 'E');
                        sb.insert(i + 1, 'E');
                        break;
                    case 'Í':
                    case 'Ì':
                    case 'Î':
                        sb.setCharAt(i, 'I');
                        break;
                    case 'Ï':
                        sb.setCharAt(i, 'I');
                        sb.insert(i + 1, 'E');
                        break;
                    case 'Ó':
                    case 'Ò':
                    case 'Ô':
                    case 'Õ':
                        sb.setCharAt(i, 'O');
                        break;
                    case 'Ö':
                        sb.setCharAt(i, 'O');
                        sb.insert(i + 1, 'E');
                        break;
                    case 'Ú':
                    case 'Ù':
                    case 'Û':
                        sb.setCharAt(i, 'U');
                        break;
                    case 'Ü':
                        sb.setCharAt(i, 'U');
                        sb.insert(i + 1, 'E');
                        break;
                    case 'Ç':
                        sb.setCharAt(i, 'C');
                        break;
                    case 'Ñ':
                        sb.setCharAt(i, 'N');
                        break;
                    case 'Æ':
                        sb.setCharAt(i, 'A');
                        break;
                }
            }
        }
        return sb.toString();
    }

    public static BSession creationSessionNaos(BSession local) throws Exception {
        BSession naos = (BSession) local.getAttribute(SESSION_NAOS_KEY);

        naos = (BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(local);
        local.connectSession(naos);
        local.setAttribute(SESSION_NAOS_KEY, naos);
        return naos;
    }

    public static BSession creationSessionPavo(BSession local) throws Exception {
        BSession pavo = (BSession) local.getAttribute(SESSION_PAVO_KEY);
        pavo = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(local);
        local.connectSession(pavo);
        local.setAttribute(SESSION_PAVO_KEY, pavo);
        return pavo;
    }

    public static BSession creationSessionPhenix(BSession local) throws Exception {
        BSession phenix = (BSession) local.getAttribute(SESSION_PHENIX_KEY);
        phenix = (BSession) GlobazSystem.getApplication(CPApplication.DEFAULT_APPLICATION_PHENIX).newSession(local);
        local.connectSession(phenix);
        local.setAttribute(SESSION_PHENIX_KEY, phenix);
        return phenix;
    }

    public static BSession creationSessionPyxis(BSession local) throws Exception {
        BSession pyxis = (BSession) local.getAttribute(SESSION_PYXIS_KEY);
        pyxis = (BSession) GlobazSystem.getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS).newSession(local);
        local.connectSession(pyxis);
        local.setAttribute(SESSION_PYXIS_KEY, pyxis);
        return pyxis;
    }

    /**
     * Commentaire relatif au constructeur GEUtil.
     */
    public GEUtil() {
        super();
    }

}
