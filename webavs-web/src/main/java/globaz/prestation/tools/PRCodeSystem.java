/*
 * Créé le 8 juin 05
 */
package globaz.prestation.tools;

import globaz.framework.translation.FWTranslation;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersCodeManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * <H1>utilitaires pour les codes system</H1>
 * 
 * @author dvh
 */
public class PRCodeSystem {

    /**
     * index du code systeme dans les tableaux contenus dans le vecteur retourne par getLibellesPourGroupe().
     */
    public static final int ID_CODE_SYSTEME = 0;

    /**
     * index du code utilisateur dans les tableaux contenus dans le vecteur retourne par getLibellesPourGroupe().
     */
    public static final int ID_CODE_UTILISATEUR = 2;

    /**
     * index du libelle dans les tableaux contenus dans le vecteur retourne par getLibellesPourGroupe().
     */
    public static final int ID_LIBELLE = 1;

    private static final Comparator<String[]> LIBELLES_PAR_CS_COMPARATOR = new Comparator<String[]>() {
        @Override
        public int compare(String[] o1, String[] o2) {
            return o1[0].compareTo(o2[0]);
        }
    };

    private static final Comparator<String[]> LIBELLES_PAR_CU_COMPARATOR = new Comparator<String[]>() {
        @Override
        public int compare(String[] o1, String[] o2) {
            return o1[2].compareTo(o2[2]);
        }
    };

    private static final Comparator<String[]> LIBELLES_PAR_LIBELLE_COMPARATOR = new Comparator<String[]>() {
        @Override
        public int compare(String[] o1, String[] o2) {
            return o1[1].compareTo(o2[1]);
        }
    };

    /**
     * Renvoie le code utilisateur pour un code Systeme
     * 
     * @param codeSystem
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return le code utilisateur, chaine vide si erreur
     * 
     * @deprecated utiliser BSession.getCode(String idCode)
     */
    @Deprecated
    public static final String getCodeUtilisateurForCS(String codeSystem, BSession session) {
        try {
            FWParametersSystemCode cs = new FWParametersSystemCode();
            cs.setSession(session);
            cs.getCode(codeSystem);

            String libelle = cs.getCurrentCodeUtilisateur().getCodeUtilisateur();

            return libelle;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Renvoie une List avec les CU du Groupe suivi de leur CS
     * 
     * @param session
     *            une BSession
     * @param CSGroupe
     *            Le Groupe de CS
     * 
     * @return une List avec les CU du Groupe suivi de leur CS
     * @deprecated remplacé par {@link #getCUCSinMap(BSession, String)}
     */
    @Deprecated
    public static final List<String> getCUCS(BSession session, String CSGroupe) {
        List<String> list = new ArrayList<String>();

        try {
            FWParametersCodeManager parametersCodeManager = FWTranslation.getSystemCodeList(CSGroupe, session);

            for (int i = 0; i < parametersCodeManager.size(); i++) {
                String cu = ((FWParametersSystemCode) parametersCodeManager.getEntity(i)).getCurrentCodeUtilisateur()
                        .getCodeUtilisateur();
                String cs = ((FWParametersSystemCode) parametersCodeManager.getEntity(i)).getCurrentCodeUtilisateur()
                        .getIdCodeSysteme();

                list.add(cu);
                list.add(cs);
            }
        } catch (Exception e) {
            return list;
        }

        return list;
    }

    /**
     * Renvoie une Map avec les CU comme clé et leurs CS comme valeurs
     * 
     * @param session
     *            une BSession
     * @param CSGroupe
     *            Le Groupe de CS
     * 
     * @return une Map avec les CU comme clé et leurs CS comme valeurs
     */
    public static final Map<String, String> getCUCSinMap(BSession session, String CSGroupe) {
        Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return JadeStringUtil.convertSpecialChars(o1.toUpperCase()).compareTo(
                        JadeStringUtil.convertSpecialChars(o2.toUpperCase()));
            }
        });

        try {
            FWParametersCodeManager parametersCodeManager = FWTranslation.getSystemCodeList(CSGroupe, session);

            for (int i = 0; i < parametersCodeManager.size(); i++) {
                String cu = ((FWParametersSystemCode) parametersCodeManager.getEntity(i)).getCurrentCodeUtilisateur()
                        .getCodeUtilisateur();
                String cs = ((FWParametersSystemCode) parametersCodeManager.getEntity(i)).getCurrentCodeUtilisateur()
                        .getIdCodeSysteme();

                map.put(cu, cs);
            }
        } catch (Exception e) {
            return map;
        }

        return map;
    }

    /**
     * renvoie le libellé d'un code system
     * 
     * @param codeSystem
     *            le CS dont on veut le libellé
     * @param session
     *            une BSession
     * 
     * @return le libellé du code system, chaine vide si erreur
     * 
     * @deprecated utiliser BSession.getCodeLibelle(String idCode)
     */
    @Deprecated
    public static String getLibelleForCS(String codeSystem, BSession session) {
        try {
            FWParametersSystemCode cs = new FWParametersSystemCode();
            cs.setSession(session);
            cs.getCode(codeSystem);

            String libelle = cs.getCurrentCodeUtilisateur().getLibelle();

            return libelle;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * renvoie tous les libelles et code utilisateur d'un groupe de codes systemes
     * 
     * @param groupeCodes
     *            le groupe dont on veut les libelles des codes systeme
     * @param session
     *            la session
     * 
     * @return un vecteur de String[3] {codeSysteme, libelle, codeUtilisateur} vecteur vide si erreur
     * @deprecated remplacée par {@link #getLibellesPourGroupeInMap(BSession, String)}
     */
    @Deprecated
    public static final Vector getLibellesPourGroupe(String groupeCodes, BSession session) {
        try {
            // on récupére la liste de tous les codes systèmes
            FWParametersCodeManager mgr = FWTranslation.getSystemCodeList(groupeCodes, session);
            Vector retValue = new Vector(mgr.size() + 2);

            // ajout des codes systemes standards
            for (int idCode = 0; idCode < mgr.size(); ++idCode) {
                FWParametersSystemCode code = (FWParametersSystemCode) mgr.getEntity(idCode);

                retValue.add(new String[] { code.getIdCode(), code.getCurrentCodeUtilisateur().getLibelle(),
                        code.getCurrentCodeUtilisateur().getCodeUtilisateur() });
            }

            return retValue;
        } catch (Exception e) {
            return new Vector();
        }
    }

    public static final Vector getLibellesEtCodePourGroupe(String groupeCodes, BSession session) {
        try {
            // on récupére la liste de tous les codes systèmes
            FWParametersCodeManager mgr = FWTranslation.getSystemCodeList(groupeCodes, session);
            Vector retValue = new Vector(mgr.size() + 2);

            // ajout des codes systemes standards
            for (int idCode = 0; idCode < mgr.size(); ++idCode) {
                FWParametersSystemCode code = (FWParametersSystemCode) mgr.getEntity(idCode);

                retValue.add(new String[] { code.getIdCode(), code.getCurrentCodeUtilisateur().getLibelle() + " - " +
                        code.getCurrentCodeUtilisateur().getCodeUtilisateur() });
            }

            return retValue;
        } catch (Exception e) {
            return new Vector();
        }
    }

    /**
     * Renvoi une map avec comme clé les traductions (dans la langue de la session) et comme valeurs le numéros de codes
     * systèmes.<br/>
     * Les codes sont triés par ordre alphabétique de traduction
     * 
     * @param session
     * @param familleCodeSysteme
     * @return
     */
    public static final Map<String, String> getLibellesPourGroupeInMap(BSession session, String familleCodeSysteme) {
        Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return JadeStringUtil.convertSpecialChars(o1.toUpperCase()).compareTo(
                        JadeStringUtil.convertSpecialChars(o2.toUpperCase()));
            }
        });

        try {
            FWParametersCodeManager parametersCodeManager = FWTranslation
                    .getSystemCodeList(familleCodeSysteme, session);

            for (int i = 0; i < parametersCodeManager.size(); i++) {
                String libelle = ((FWParametersSystemCode) parametersCodeManager.getEntity(i))
                        .getCurrentCodeUtilisateur().getLibelle();
                String idCodeSysteme = ((FWParametersSystemCode) parametersCodeManager.getEntity(i))
                        .getCurrentCodeUtilisateur().getIdCodeSysteme();

                map.put(libelle, idCodeSysteme);
            }
        } catch (Exception e) {
            return map;
        }

        return map;
    }

    /**
     * trie un vecteur retourne par getLibellesPourGroupe() par Code systeme croissant.
     * 
     * @param libelles
     *            DOCUMENT ME!
     */
    public static final void trierLibellesParCS(Vector libelles) {
        Collections.sort(libelles, PRCodeSystem.LIBELLES_PAR_CS_COMPARATOR);
    }

    /**
     * trie un vecteur retourne par getLibellesPourGroupe() par Code utilisateur croissant.
     * 
     * @param libelles
     *            DOCUMENT ME!
     */
    public static final void trierLibellesParCU(Vector libelles) {
        Collections.sort(libelles, PRCodeSystem.LIBELLES_PAR_CU_COMPARATOR);
    }

    /**
     * trie un vecteur retourne par getLibellesPourGroupe() par libelle croissant.
     * 
     * @param libelles
     *            DOCUMENT ME!
     */
    public static final void trierLibellesParLibelle(Vector libelles) {
        Collections.sort(libelles, PRCodeSystem.LIBELLES_PAR_LIBELLE_COMPARATOR);
    }
}
