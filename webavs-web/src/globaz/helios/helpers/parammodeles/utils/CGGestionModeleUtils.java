package globaz.helios.helpers.parammodeles.utils;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.modeles.CGEnteteModeleEcriture;
import globaz.helios.db.modeles.CGGestionModeleViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcriture;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

public class CGGestionModeleUtils {

    /**
     * Complète et ajoute une écriture en bd.
     * 
     * @param session
     * @param transaction
     * @param modeles
     * @param entete
     * @param ligne
     * @return
     * @throws Exception
     */
    public static void createLigne(BISession session, BTransaction transaction, CGGestionModeleViewBean modeles,
            CGEnteteModeleEcriture entete, CGLigneModeleEcriture ligne) throws Exception {
        ligne.setSession((BSession) session);

        ligne.setIdEnteteModeleEcriture(entete.getIdEnteteModeleEcriture());

        ligne.add(transaction);

        if (ligne.hasErrors() || ligne.isNew()) {
            throw new Exception(((BSession) session).getLabel("GESTION_MODELES_CREATION_PROBLEME") + " (line)");
        }
    }

    /**
     * Retrouve l'entête du modèle.
     * 
     * @param session
     * @param transaction
     * @param idEntete
     * @return
     * @throws Exception
     */
    public static CGEnteteModeleEcriture getEntete(BISession session, BTransaction transaction, String idEntete)
            throws Exception {
        CGEnteteModeleEcriture entete = new CGEnteteModeleEcriture();
        entete.setSession((BSession) session);

        entete.setIdEnteteModeleEcriture(idEntete);

        entete.retrieve(transaction);

        if (entete.isNew() || entete.hasErrors()) {
            throw new Exception(((BSession) session).getLabel("GESTION_MODELES_CREATION_PROBLEME") + " (header)");
        }

        return entete;
    }

    /**
     * Une écriture collective ne peut-être passée sur un compte affilie.
     * 
     * @param session
     * @param modeles
     * @param entete
     * @throws Exception
     */
    public static void testCompteAffilie(BISession session, CGGestionModeleViewBean modeles,
            CGEnteteModeleEcriture entete) throws Exception {
        if (modeles.getLignes().size() > CGGestionEcritureUtils.NUMBER_ECRITURE_DOUBLE) {
            Iterator it = modeles.getLignes().iterator();
            while (it.hasNext()) {
                CGLigneModeleEcriture ligne = (CGLigneModeleEcriture) it.next();

                if (!JadeStringUtil.isIntegerEmpty(ligne.getIdCompte())) {
                    ligne.setSession((BSession) session);
                    ligne.setIdEnteteModeleEcriture(entete.getIdEnteteModeleEcriture());
                    if (ligne.isForCompteAffillie()) {
                        throw new Exception(((BSession) session).getLabel("ENTETE_ECRITURE_CONFLIT_ECRITURE_AFFILIE"));
                    }
                }
            }
        }
    }

}
