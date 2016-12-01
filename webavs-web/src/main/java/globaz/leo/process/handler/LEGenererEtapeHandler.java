/*
 * Cr�� le 10 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.handler;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LEGenererEtapeHandler {
    LEEnvoiHandler envoi = new LEEnvoiHandler();

    public String genererEtape(LEEnvoiDataSource envoiDS, BSession session, BProcess parent) throws Exception {
        return this.genererEtape(envoiDS, session, parent, null);
    }

    public String genererEtape(LEEnvoiDataSource envoiDS, BSession session, BProcess parent, String idDestinataire)
            throws Exception {
        return this.genererEtape(envoiDS, session, parent, idDestinataire, null);
    }

    public String genererEtape(LEEnvoiDataSource envoiDS, BSession session, BProcess parent, String idDestinataire,
            String dateCreation) throws Exception {
        // g�n�re le document de l'�tape suivante
        // charge le g�n�rateur de document
        LEDocumentHandler docHandler = new LEDocumentHandler();
        ILEGeneration generateur = docHandler.chargeGenerateurDocument(envoiDS, session);

        if (generateur != null) {
            // generateur.setDateImpression(JACalendar.todayJJsMMsAAAA());
            if (parent != null && !JadeStringUtil.isEmpty(parent.getEMailAddress())) {
                generateur.setEMailAddress(parent.getEMailAddress());
            } else {
                generateur.setEMailAddress(session.getUserEMail());
            }
            generateur.start();
        }
        // G�n�re l'�tape suivante
        if (JadeStringUtil.isEmpty(dateCreation)) {
            return envoi.genererEnvoi(envoiDS, session, null, idDestinataire);
        } else {
            return envoi.genererEnvoi("", dateCreation, envoiDS, session, null, idDestinataire);
        }
    }

    public String genererEtapeSuivante(String idJournal, BSession session) throws Exception {
        return this.genererEtape(envoi.chargerDonnees(idJournal, session, null), session, null);
    }

    public String genererEtapeSuivante(String idJournal, BSession session, BTransaction transaction) throws Exception {
        return this.genererEtape(envoi.chargerDonnees(idJournal, session, transaction), session, null);
    }

    public String genererEtapeSuivante(String idJournal, BSession session, BTransaction transaction, BProcess parent)
            throws Exception {
        return this.genererEtape(envoi.chargerDonnees(idJournal, session, transaction), session, parent);
    }

    public String genererEtapeSuivante(String idJournal, BSession session, BTransaction transaction, BProcess parent,
            String dateRappel, String dateCreation) throws Exception {
        return this.genererEtape(envoi.chargerDonnees(idJournal, session, transaction, dateRappel, dateCreation),
                session, parent, "", dateCreation);
    }

    /**
     * @param idJournal
     * @param sessionUt
     * @param string
     * @return
     */
    public String genererEtapeSuivante(String idJournal, BSession session, String idDestinataire) throws Exception {
        return this.genererEtape(envoi.chargerDonnees(idJournal, session, null), session, null, idDestinataire);
    }
}
