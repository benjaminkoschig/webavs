/*
 * Créé le 3 mars 06
 */
package globaz.aquila.util;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;
import globaz.osiris.translation.CACodeSystem;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dvh
 */
public class COAdministrateurUtil {
    /**
     * Permet d'obtenir toutes les sociétés pour lequel ce tiers est administrateur
     * 
     * @return une Map (idCompteAnnexe, nomSociete)
     */
    public static Map<String, String> getSocietes(String idCompteAnnexeAdministrateur, BISession session)
            throws Exception {
        String erreur = "";

        Map<String, String> retValue = new HashMap<String, String>();
        BSession sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                .newSession();
        session.connectSession(sessionOsiris);

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(sessionOsiris);
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexeAdministrateur);
        compteAnnexe.retrieve();
        String idTiers = compteAnnexe.getIdTiers();

        CACompteAnnexeManager compteAnnexeAdministrateurManager = new CACompteAnnexeManager();
        compteAnnexeAdministrateurManager.setSession(sessionOsiris);
        compteAnnexeAdministrateurManager.setForIdTiers(idTiers);
        compteAnnexeAdministrateurManager.setForSelectionRole(IntRole.ROLE_ADMINISTRATEUR);
        compteAnnexeAdministrateurManager.setForIdGenreCompte(CACodeSystem.COMPTE_AUXILIAIRE);

        compteAnnexeAdministrateurManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < compteAnnexeAdministrateurManager.size(); i++) {
            CACompteAnnexe compteAnnexeAdministrateur = (CACompteAnnexe) compteAnnexeAdministrateurManager.getEntity(i);

            // on recherche le nom de la société
            CACompteAnnexeManager compteAnnexeSocieteManager = new CACompteAnnexeManager();
            compteAnnexeSocieteManager.setSession(sessionOsiris);
            String idExterne = compteAnnexeAdministrateur.getIdExterneRole();
            COApplication application = (COApplication) GlobazSystem
                    .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA);
            if (application.numeroAdministrateurReplaceTiret()) {
                idExterne = JadeStringUtil.split(idExterne, '-', Integer.MAX_VALUE)[0];
            } else {
                idExterne.substring(0, idExterne.lastIndexOf('-'));
            }

            compteAnnexeSocieteManager.setLikeIdExterneRole(idExterne);
            compteAnnexeSocieteManager.find();
            CACompteAnnexe compteAnnexeSociete;
            if (compteAnnexeSocieteManager.size() != 1) {
                for (int j = 0; j < compteAnnexeSocieteManager.size(); j++) {
                    compteAnnexeSociete = (CACompteAnnexe) compteAnnexeSocieteManager.getEntity(j);
                    if (!compteAnnexeSociete.getIdRole().equals(IntRole.ROLE_AFFILIE_PARITAIRE)
                            && !compteAnnexeSociete.getIdRole().equals(IntRole.ROLE_AFFILIE)) {
                        continue;
                    } else {
                        compteAnnexeSociete = (CACompteAnnexe) compteAnnexeSocieteManager.getEntity(j);
                        retValue.put(compteAnnexeAdministrateur.getIdCompteAnnexe(), compteAnnexeSociete.getTiers()
                                .getNom());
                        return retValue;
                    }
                }

                erreur += sessionOsiris.getLabel("AQUILA_ADMIN_ERREUR_SOCIETE");

                throw new Exception(erreur);

            } else {
                compteAnnexeSociete = (CACompteAnnexe) compteAnnexeSocieteManager.getEntity(0);
                retValue.put(compteAnnexeAdministrateur.getIdCompteAnnexe(), compteAnnexeSociete.getTiers().getNom());
            }
        }

        return retValue;
    }

    /**
     * Check if a string is like a pattern
     * 
     * @param s
     *            original string
     * @param pattern
     *            for the comparison
     * @return true or false
     */
    public static boolean isFormatNumAffilie(String s, String pattern) {
        int point = 0;
        int tiret = 0;

        point = JadeStringUtil.indexOf(pattern, ".");
        tiret = JadeStringUtil.indexOf(pattern, "-");

        if ((point == JadeStringUtil.indexOf(s, ".")) && (tiret == JadeStringUtil.indexOf(s, "-"))) {
            return true;
        } else {
            return false;
        }
    }

}
