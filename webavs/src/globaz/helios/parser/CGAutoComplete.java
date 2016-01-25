package globaz.helios.parser;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.modeles.CGModeleEcritureListViewBean;
import globaz.helios.db.modeles.CGModeleEcritureViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * @author dda
 * 
 */
public class CGAutoComplete {

    private static final String PROPERTY_MODELES_AUTO_COMPLETE_AUTO_START = "autoCompleteAutoStart";

    /**
     * Retourne l'index de démarrage de la fonction auto-complete des modèles d'écritures. Utilisé pour la saisie
     * d'écritures.
     * 
     * @return
     */
    public static int getAutoCompleteAutoStart() {
        try {
            return Integer.parseInt(GlobazSystem.getApplication(CGApplication.DEFAULT_APPLICATION_HELIOS).getProperty(
                    PROPERTY_MODELES_AUTO_COMPLETE_AUTO_START));
        } catch (Exception e) {
            JadeLogger.warn(e, "Constante " + PROPERTY_MODELES_AUTO_COMPLETE_AUTO_START + " non résolue.");
            return -1;
        }
    }

    /**
     * Retourne la liste des comptes en fonction du masque saisie par l'utilisateur.
     * 
     * @param session
     * @param like
     * @param idExerciceComptable
     * @param isComptabiliteAVS
     * @return
     */
    public static String getComptes(BSession session, String like, String idExerciceComptable, String isComptabiliteAVS) {
        String select = "<select size=\"5\" style=\"width:16cm\">";

        try {
            CGPlanComptableManager manager = new CGPlanComptableManager();
            manager.setSession(session);

            if (like != null) {
                try {
                    Integer.parseInt(like.substring(0, 1));
                    manager.setBeginWithIdExterne(getLikeFormatted(like, Boolean.valueOf(isComptabiliteAVS)));
                } catch (Exception e) {
                    if ("IT".equalsIgnoreCase(session.getIdLangueISO())) {
                        manager.setForLibelleItLike(like);
                    } else if ("DE".equalsIgnoreCase(session.getIdLangueISO())) {
                        manager.setForLibelleDeLike(like);
                    } else {
                        manager.setForLibelleFrLike(like);
                    }
                }
            }

            manager.setForIdExerciceComptable(idExerciceComptable);
            manager.setReqCritere(CodeSystem.CS_TRI_NUMERO_COMPTE);
            manager.find();

            if (manager.size() != 0) {
                for (int i = 0; i < manager.size(); i++) {
                    CGPlanComptableViewBean entity = (CGPlanComptableViewBean) manager.getEntity(i);
                    if (!entity.isEstVerrouille().booleanValue()) {
                        String codeISOMonnaie = entity.getCodeISOMonnaie();
                        if (codeISOMonnaie == null || codeISOMonnaie.equals("")) {
                            codeISOMonnaie = "CHF";
                        }

                        if (CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getIdNature())) {
                            select += "<option idCompte=\"" + entity.getIdCompte() + "\" defaultIdCentreCharge=\""
                                    + entity.getDefaultIdCentreCharge() + "\" libelleCompte=\""
                                    + JadeStringUtil.change(entity.getLibelle(), "\"", "&quot;") + "\" value=\""
                                    + entity.getIdExterne() + "\" idNature=\"" + entity.getIdNature()
                                    + "\" codeISOMonnaie=\"" + codeISOMonnaie + "\" solde=\""
                                    + JANumberFormatter.fmt(entity.getSoldeProvisoireMonnaie(), true, true, false, 2)
                                    + "\">" + entity.getIdExterne() + " - " + entity.getLibelle() + "</option>";
                        } else {
                            select += "<option idCompte=\"" + entity.getIdCompte() + "\" defaultIdCentreCharge=\""
                                    + entity.getDefaultIdCentreCharge() + "\" libelleCompte=\""
                                    + JadeStringUtil.change(entity.getLibelle(), "\"", "&quot;") + "\" value=\""
                                    + entity.getIdExterne() + "\" idNature=\"" + entity.getIdNature()
                                    + "\" codeISOMonnaie=\"" + codeISOMonnaie + "\" solde=\""
                                    + JANumberFormatter.fmt(entity.getSoldeProvisoire(), true, true, false, 2) + "\">"
                                    + entity.getIdExterne() + " - " + entity.getLibelle() + "</option>";
                        }
                    }
                }
            }
            select += "</select>";
        } catch (Exception e) {
            // Do nothing.
        }

        return select;
    }

    /**
     * Si le numéro de compte est insérer sans point, le formatter en lui rajoutant ces derniers.
     * 
     * @param like
     * @return
     */
    private static String getLikeFormatted(String like) {
        if (like != null && like.length() > 4 && like.indexOf(".") == -1) {
            StringBuffer tmp = new StringBuffer();
            tmp.append(like.substring(0, 4));
            tmp.append(".");

            if (like.length() > 8) {
                tmp.append(like.substring(4, 8));
                tmp.append(".");
                tmp.append(like.substring(8));
            } else {
                tmp.append(like.substring(4));
            }

            like = tmp.toString();
        }
        return like;
    }

    /**
     * Return le libellé formatté uniquement dans le cas d'une comptabilité avs.
     * 
     * @param like
     * @param isComptabiliteAvs
     * @return
     */
    public static String getLikeFormatted(String like, Boolean isComptabiliteAvs) {
        if (isComptabiliteAvs.booleanValue()) {
            return getLikeFormatted(like);
        } else {
            return like;
        }
    }

    /**
     * Recherche la liste des modèles d'écritures en fonction du masque like.
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getModeles(BSession session, String idMandat, String like) {
        if (like == null) {
            like = "";
        }

        String options = "";
        try {
            CGModeleEcritureListViewBean manager = new CGModeleEcritureListViewBean();
            manager.setISession(session);
            manager.setForIdMandat(idMandat);
            manager.setForLibelle(like);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGModeleEcritureViewBean entity = (CGModeleEcritureViewBean) manager.get(i);

                options += "<OPTION value=\"" + entity.getLibelle() + "\" selectedIdModel=\"";
                options += entity.getIdModeleEcriture() + "\">";
                options += entity.getLibelle();
                options += "</OPTION>";

            }

            if (JadeStringUtil.isBlank(options)) {
                options += "<OPTION value=\"" + like + "\">" + like + "</OPTION>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CGAutoComplete.class, e);
        }

        return options;
    }
}
