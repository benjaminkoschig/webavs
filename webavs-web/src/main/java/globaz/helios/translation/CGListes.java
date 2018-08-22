package globaz.helios.translation;

import java.util.Vector;
import javax.servlet.http.HttpSession;
import globaz.globall.db.BSession;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.avs.CGSecteurAVS;
import globaz.helios.db.avs.CGSecteurAVSManager;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.bouclement.CGBouclementManager;
import globaz.helios.db.classifications.CGClassification;
import globaz.helios.db.classifications.CGClassificationManager;
import globaz.helios.db.classifications.CGDefinitionListe;
import globaz.helios.db.classifications.CGLiaisonDefinitionListe_MandatManager;
import globaz.helios.db.comptes.CGCentreCharge;
import globaz.helios.db.comptes.CGCentreChargeManager;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGJournalManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * Insérez la description du type ici. Date de création : (07.11.2002 15:42:08)
 *
 * @author: Administrator
 */
public class CGListes {
    private static final int TRIM_LIBELLE_CENTRE_CHARGE = 13;

    public static Vector getBouclementListe(HttpSession session) {

        Vector vList = new Vector();
        CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

        try {
            CGBouclementManager manager = new CGBouclementManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdMandat(exerciceComptable.getIdMandat());
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                CGBouclement entity = (CGBouclement) manager.getEntity(i);
                String[] list = new String[2];
                list[0] = entity.getIdBouclement();
                list[1] = entity.getLibelle() + " - " + entity.getIdBouclement();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne list vide.

        }
        return vList;
    }

    public static Vector getCentreChargeListe(HttpSession session) {
        return getCentreChargeListe(session, null);
    }

    public static Vector getCentreChargeListe(HttpSession session, String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        if (entete != null) {
            list[0] = "0";
            list[1] = entete;
            vList.add(list);
        }

        try {

            CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

            CGCentreChargeManager manager = new CGCentreChargeManager();
            manager.setForIdMandat(exerciceComptable.getIdMandat());
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGCentreCharge entity = (CGCentreCharge) manager.getEntity(i);
                list[0] = entity.getIdCentreCharge();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne list vide.
        }
        return vList;
    }

    public static Vector getCentreChargeListe(String entete, HttpSession session, String idMandat) {

        Vector vList = new Vector();
        String[] list = new String[2];

        if (entete != null) {
            list[0] = "0";
            list[1] = entete;
            vList.add(list);
        }

        try {
            CGCentreChargeManager manager = new CGCentreChargeManager();

            if (idMandat != null && !idMandat.equals("")) {
                manager.setForIdMandat(idMandat);
            }

            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];

                CGCentreCharge entity = (CGCentreCharge) manager.getEntity(i);
                list[0] = entity.getIdCentreCharge();

                if (!JadeStringUtil.isBlank(entity.getLibelle())
                        && entity.getLibelle().length() > TRIM_LIBELLE_CENTRE_CHARGE) {
                    list[1] = entity.getLibelle().substring(0, TRIM_LIBELLE_CENTRE_CHARGE) + "...";
                } else {
                    list[1] = entity.getLibelle();
                }

                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne list vide.
        }
        return vList;
    }

    public static Vector getClassificationListe(HttpSession session) {

        Vector vList = new Vector();
        try {
            CGClassificationManager manager = new CGClassificationManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                String[] list = new String[2];
                CGClassification entity = (CGClassification) manager.getEntity(i);
                list[0] = entity.getIdClassification();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne list vide.
        }
        return vList;
    }

    public static Vector getClassificationListe(HttpSession session, String idMandat, String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        if (entete != null) {
            list[0] = "";
            list[1] = entete;
            vList.add(list);
        }

        if (idMandat != null && !idMandat.equals("")) {
            try {
                CGClassificationManager manager = new CGClassificationManager();
                manager.setSession((BSession) CodeSystem.getSession(session));
                manager.setForIdMandat(idMandat);
                manager.find();

                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];
                    CGClassification entity = (CGClassification) manager.getEntity(i);
                    list[0] = entity.getIdClassification();
                    list[1] = entity.getLibelle();
                    vList.add(list);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // si probleme, retourne list vide.
            }
        }
        return vList;
    }

    public static Vector getCompteListe(HttpSession session, String idExerciceComptable) {

        Vector vList = new Vector();

        try {
            CGPlanComptableManager manager = new CGPlanComptableManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdExerciceComptable(idExerciceComptable);
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                CGPlanComptableViewBean entity = (CGPlanComptableViewBean) manager.getEntity(i);
                String[] list = new String[2];
                list[0] = entity.getIdCompte();
                list[1] = entity.getIdCompte() + " (" + entity.getIdExterne() + ")";
                if (!entity.isEstVerrouille().booleanValue()) {
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne list vide.

        }
        return vList;
    }

    public static Vector getDefinitionListeListe(HttpSession session) {
        return getDefinitionListeListe(session, null);
    }

    public static Vector getDefinitionListeListe(HttpSession session, String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        if (entete != null) {
            list[0] = "";
            list[1] = entete;
            vList.add(list);
        }

        try {

            CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

            CGLiaisonDefinitionListe_MandatManager manager = new CGLiaisonDefinitionListe_MandatManager();
            manager.setForIdMandat(exerciceComptable.getIdMandat());
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGDefinitionListe entity = (CGDefinitionListe) manager.getEntity(i);
                list[0] = entity.getIdDefinitionListe();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne list vide.
        }
        return vList;
    }

    public static Vector getExerciceComptableListe(HttpSession session, String idMandat, String ajouteTous) {
        return getExerciceComptableListe(session, idMandat, ajouteTous, null);
    }

    public static Vector getExerciceComptableListe(HttpSession session, String idMandat, String ajouteTous,
            String exceptIdExerciceComptable) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "0";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGExerciceComptableManager manager = new CGExerciceComptableManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdMandat(idMandat);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGExerciceComptable entity = (CGExerciceComptable) manager.getEntity(i);

                if (JadeStringUtil.isBlank(exceptIdExerciceComptable)
                        || !entity.getIdExerciceComptable().equals(exceptIdExerciceComptable)) {
                    list[0] = entity.getIdExerciceComptable();
                    list[1] = entity.getFullDescription();
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getExerciceComptableNonClotureListe(HttpSession session, String idMandat, String ajouteTous) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "0";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGExerciceComptableManager manager = new CGExerciceComptableManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdMandat(idMandat);
            manager.setForExerciceOuvert(new Boolean(true));
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGExerciceComptable entity = (CGExerciceComptable) manager.getEntity(i);
                list[0] = entity.getIdExerciceComptable();
                list[1] = entity.getFullDescription();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getJournauxListe(HttpSession session, String idExerciceComptable, String idPeriodeComptable,
            String ajouteTous) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "0";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGJournalManager manager = new CGJournalManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdPeriodeComptable(idPeriodeComptable);
            manager.setForIdExerciceComptable(idExerciceComptable);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGJournal entity = (CGJournal) manager.getEntity(i);
                list = new String[2];
                list[0] = entity.getIdJournal();
                list[1] = entity.getFullDescription();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getJournauxOuvertListe(HttpSession session, String idExerciceComptable,
            String idPeriodeComptable, String ajouteTous) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "0";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGJournalManager manager = new CGJournalManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdPeriodeComptable(idPeriodeComptable);
            manager.setForIdExerciceComptable(idExerciceComptable);
            manager.setForIdEtat(ICGJournal.CS_ETAT_OUVERT);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGJournal entity = (CGJournal) manager.getEntity(i);
                list = new String[2];
                list[0] = entity.getIdJournal();
                list[1] = entity.getFullDescription();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getMandatListe(HttpSession session) {

        return getMandatListe(session, "");
    }

    public static Vector getMandatListe(HttpSession session, String ajouteTous) {

        Vector vList = new Vector();
        String[] list = new String[2];
        // pour critere de recherche (par ex: ecran exerciceComptable_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGMandatManager manager = new CGMandatManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGMandat entity = (CGMandat) manager.getEntity(i);
                list[0] = entity.getIdMandat();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getMandatListe(HttpSession session, String ajouteTous, String excludeIdMandat) {

        Vector vList = new Vector();
        String[] list = new String[2];
        // pour critere de recherche (par ex: ecran exerciceComptable_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGMandatManager manager = new CGMandatManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGMandat entity = (CGMandat) manager.getEntity(i);
                if (!excludeIdMandat.equals(entity.getIdMandat())) {
                    list[0] = entity.getIdMandat();
                    list[1] = entity.getLibelle();
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getPeriodeComptableListe(HttpSession session) {
        return getPeriodeComptableListe(session, "");
    }

    public static Vector getPeriodeComptableListe(HttpSession session, String ajouteTous) {

        Vector vList = new Vector();
        String[] list = new String[2];
        CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "0";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGPeriodeComptable entity = (CGPeriodeComptable) manager.getEntity(i);
                list[0] = entity.getIdPeriodeComptable();
                list[1] = entity.getFullDescription();
                vList.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getPeriodeComptableNonClotureListe(HttpSession session) {
        return getPeriodeComptableNonClotureListe(session, "");
    }

    public static Vector getPeriodeComptableNonClotureListe(HttpSession session, String ajouteTous) {

        Vector vList = new Vector();
        String[] list = new String[2];
        CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "0";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGPeriodeComptable entity = (CGPeriodeComptable) manager.getEntity(i);
                if (!entity.isEstCloture().booleanValue()) {
                    list = new String[2];
                    list[0] = entity.getIdPeriodeComptable();
                    list[1] = entity.getFullDescription();
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getPeriodeComptableNonClotureListe(HttpSession session, String idExerciceComptable,
            String ajouteTous) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (ajouteTous != null && !ajouteTous.equals("")) {
            list[0] = "0";
            list[1] = ajouteTous;
            vList.add(list);
        }

        try {
            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdExerciceComptable(idExerciceComptable);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGPeriodeComptable entity = (CGPeriodeComptable) manager.getEntity(i);
                if (!entity.isEstCloture().booleanValue()) {
                    list = new String[2];
                    list[0] = entity.getIdPeriodeComptable();
                    list[1] = entity.getFullDescription();
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 17:20:23)
     *
     * @return java.util.Vector
     * @param session
     *            javax.servlet.http.HttpSession
     * @param label
     *            java.lang.String
     */
    public static Vector getSecteurAVSBilanListe(HttpSession session, String idMandat, String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (entete != null && !entete.equals("")) {
            list[0] = "";
            list[1] = entete;
            vList.add(list);
        }

        try {
            CGSecteurAVSManager manager = new CGSecteurAVSManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdMandat(idMandat);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                CGSecteurAVS entity = (CGSecteurAVS) manager.getEntity(i);
                list[0] = entity.getIdSecteurAVS();
                list[1] = entity.getIdSecteurAVS() + " - " + entity.getLibelle();
                if (entity.isCompteBilan().booleanValue()) {
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getSecteurAVSBilanListe(HttpSession session, String idMandat, String idNotTypeTache,
            String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (entete != null && !entete.equals("")) {
            list[0] = "";
            list[1] = entete;
            vList.add(list);
        }

        try {
            CGSecteurAVSManager manager = new CGSecteurAVSManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdMandat(idMandat);
            manager.setForNotIdTypeTache(idNotTypeTache);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {

                CGSecteurAVS entity = (CGSecteurAVS) manager.getEntity(i);
                if (entity.isCompteBilan().booleanValue()) {
                    list = new String[2];
                    list[0] = entity.getIdSecteurAVS();
                    list[1] = entity.getIdSecteurAVS() + " - " + entity.getLibelle();
                    vList.add(list);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.04.2003 17:20:23)
     *
     * @return java.util.Vector
     * @param session
     *            javax.servlet.http.HttpSession
     * @param label
     *            java.lang.String
     */
    public static Vector getSecteurAVSListe(HttpSession session, String idMandat, String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (entete != null && !entete.equals("")) {
            list[0] = "";
            list[1] = entete;
            vList.add(list);
        }

        try {
            CGSecteurAVSManager manager = new CGSecteurAVSManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdMandat(idMandat);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {

                CGSecteurAVS entity = (CGSecteurAVS) manager.getEntity(i);
                if (entity.isCompteAdministration().booleanValue() || entity.isCompteExploitation().booleanValue()
                        || entity.isCompteInvestissement().booleanValue()) {
                    list = new String[2];
                    list[0] = entity.getIdSecteurAVS();
                    list[1] = entity.getLibelle();
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getSecteurAVSResultatListe(HttpSession session, String idMandat, String idNotTypeTache,
            String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        // pour critere de recherche (par ex: ecran soldesDesComptes_rc))
        if (entete != null && !entete.equals("")) {
            list[0] = "";
            list[1] = entete;
            vList.add(list);
        }

        try {
            CGSecteurAVSManager manager = new CGSecteurAVSManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdMandat(idMandat);
            manager.setForNotIdTypeTache(idNotTypeTache);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGSecteurAVS entity = (CGSecteurAVS) manager.getEntity(i);
                if (entity.isCompteResultat().booleanValue()) {
                    list = new String[2];
                    list[0] = entity.getIdSecteurAVS();
                    list[1] = entity.getIdSecteurAVS() + " - " + entity.getLibelle();
                    vList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // si probleme, retourne une liste vide.
        }
        return vList;
    }

    public static Vector getUpdatableClassificationListe(HttpSession session, String idMandat, String entete) {

        Vector vList = new Vector();
        String[] list = new String[2];

        if (entete != null) {
            list[0] = "";
            list[1] = entete;
            vList.add(list);
        }

        if (idMandat != null && !idMandat.equals("")) {
            try {
                CGClassificationManager manager = new CGClassificationManager();
                manager.setSession((BSession) CodeSystem.getSession(session));
                manager.setForIdMandat(idMandat);
                manager.find();

                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];
                    CGClassification entity = (CGClassification) manager.getEntity(i);

                    if (!CGClassification.CS_TYPE_MANUEL.equals(entity.getIdTypeClassification())) {
                        continue;
                    }

                    list[0] = entity.getIdClassification();
                    list[1] = entity.getLibelle();
                    vList.add(list);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // si probleme, retourne list vide.
            }
        }
        return vList;
    }

}
