/*
 * Créé le 27 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFAdresseDestination {
    private BSession _session;
    private TITiers _tiers;
    private String nomDest = "";
    private String titreDest = "";

    public AFAdresseDestination(BSession session) {
        super();
        _session = session;
    }

    public String getAdresseDestinataire(String id) throws Exception {
        return getTiers(id).getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION);
    }

    public String getAdresseDestinataire(String id, String numAff) throws Exception {
        return getTiers(id).getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), numAff);
    }

    public String getAdresseDestinataire(String id, String numAff, String domaineAdresse) throws Exception {

        if (JadeStringUtil.isIntegerEmpty(domaineAdresse)) {
            return getTiers(id).getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), numAff);
        } else {
            return getTiers(id).getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER, domaineAdresse,
                    JACalendar.todayJJsMMsAAAA(), numAff);
        }
    }

    public String getAdresseDestinatairePlanFactu(String id, String numAff) throws Exception {
        return getTiers(id).getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                IConstantes.CS_APPLICATION_FACTURATION, JACalendar.todayJJsMMsAAAA(), numAff);
    }

    public String getFormulePolitesse(String idTiers) throws Exception {
        return getTiers(idTiers).getFormulePolitesse(null);
    }

    public String getLangueDestinataire(String id) throws Exception {
        return AFApplication.getISOLangueTiers(getTiers(id));
    }

    public String getLangueTiers(String idTiers) throws Exception {
        return getTiers(idTiers).getLangue();
    }

    public String getNomDestinataire(String id) throws Exception {
        if (JadeStringUtil.isEmpty(nomDest)) {
            nomDest = getTiers(id).getNom();
        }
        return nomDest;
    }

    private TITiers getTiers(String idTiers) throws Exception {
        // if(_tiers == null){
        _tiers = new TITiers();
        _tiers.setSession(_session);
        _tiers.setIdTiers(idTiers);
        _tiers.retrieve();
        if (_tiers.isNew()) {
            throw new Exception("Erreur, impossible de trouver le tiers pour l'id =" + idTiers);
        }
        // }
        return _tiers;
    }

    public String getTitreDestinataire(String id) throws Exception {
        if (JadeStringUtil.isEmpty(titreDest)) {
            titreDest = getTiers(id).getTitreTiers();
        }
        return titreDest;
    }
}
