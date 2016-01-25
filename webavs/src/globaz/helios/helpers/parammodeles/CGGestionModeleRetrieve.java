package globaz.helios.helpers.parammodeles;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.helios.db.modeles.CGEnteteModeleEcriture;
import globaz.helios.db.modeles.CGGestionModeleViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcritureManager;
import globaz.helios.db.modeles.CGModeleEcriture;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

public class CGGestionModeleRetrieve {

    /**
     * Charge un modèle avec l'entête et les écritures .
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrieveModele(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionModeleViewBean modeles = (CGGestionModeleViewBean) viewBean;

        validate(session, modeles);

        CGModeleEcriture modele = new CGModeleEcriture();
        modele.setSession((BSession) session);
        modele.setIdModeleEcriture(modeles.getIdModeleEcriture());
        modele.retrieve();

        if (modele.hasErrors() || modele.isNew()) {
            throw new Exception(((BSession) session).getLabel("GESTION_MODELES_CREATION_PROBLEME") + " (modele)");
        }

        modeles.setLibelleModele(modele.getLibelle());
        // modeles.setPieceModele(modele.getPiece());

        CGEnteteModeleEcriture entete = new CGEnteteModeleEcriture();
        entete.setSession((BSession) session);

        entete.setIdEnteteModeleEcriture(modeles.getIdEnteteModeleEcriture());

        entete.retrieve();

        if (entete.hasErrors()) {
            throw new Exception(((BSession) session).getLabel("GESTION_MODELES_CREATION_PROBLEME") + " (entete)");
        }

        if (entete.isNew()) {
            if (!JadeStringUtil.isIntegerEmpty(modele.getIdMandat())) {
                modeles.setIdMandat(modele.getIdMandat());
            } else if (JadeStringUtil.isIntegerEmpty(modeles.getIdMandat())) {
                modeles.setDefaultIdMandat();
            }
        } else {
            modeles.setIdMandat(entete.getIdMandat());
        }

        if (!JadeStringUtil.isIntegerEmpty(modeles.getIdEnteteModeleEcriture())) {
            CGLigneModeleEcritureManager manager = new CGLigneModeleEcritureManager();
            manager.setSession((BSession) session);
            manager.setForIdModeleEcriture(modeles.getIdModeleEcriture());
            manager.setForIdEnteteModeleEcriture(modeles.getIdEnteteModeleEcriture());
            manager.find();

            if (manager.hasErrors()) {
                throw new Exception(((BSession) session).getLabel("GESTION_MODELES_CREATION_PROBLEME")
                        + " (line not found)");
            }

            ArrayList lignes = new ArrayList();
            for (int i = 0; i < manager.size(); i++) {
                lignes.add(manager.get(i));
            }
            modeles.setLignes(lignes);

            modeles.setShowRows(manager.size());
        }
    }

    /**
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param session
     * @param modeles
     * @throws Exception
     */
    private static void validate(BISession session, CGGestionModeleViewBean modeles) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(modeles.getIdModeleEcriture())) {
            throw new Exception(((BSession) session).getLabel("GESTION_MODELES_CREATION_PROBLEME") + " (id entete)");
        }

    }
}
