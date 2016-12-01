/*
 * Créé le 10 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
        // génère le document de l'étape suivante
        // charge le générateur de document
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
        // Génère l'étape suivante
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
