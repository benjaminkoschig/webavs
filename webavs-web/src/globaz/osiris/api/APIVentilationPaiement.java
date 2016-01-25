/*
 * Cr�� le 15 nov. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
