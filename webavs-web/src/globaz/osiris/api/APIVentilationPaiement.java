/*
 * Créé le 15 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

import globaz.globall.db.BSession;

/**
 * @author ald
 */
public interface APIVentilationPaiement {

    public APIVPListePostes ventilerPaiement(BSession session, String idSection, String typeProcedure) throws Exception;

    public APIVPListePostes[] ventilerPaiement(BSession session, String idCompteAnnexe, String periodeDebut,
            String periodeFin, String typeProcedure) throws Exception;
}
