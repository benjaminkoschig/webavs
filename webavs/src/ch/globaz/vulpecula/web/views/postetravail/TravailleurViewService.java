package ch.globaz.vulpecula.web.views.postetravail;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.jade.noteIt.NoteException;
import ch.globaz.jade.noteIt.SimpleNote;
import ch.globaz.jade.noteIt.SimpleNoteSearch;
import ch.globaz.jade.noteIt.business.service.JadeNoteService;
import ch.globaz.jade.noteIt.businessimpl.service.JadeNoteServiceImpl;
import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.vulpecula.business.models.notification.NotificationSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class TravailleurViewService {

    @SuppressWarnings("unused")
    private static final class InformationsTravailleurs {
        public String dateNaissance;
        public String genreSalaire;
        public String qualification;
        public String note;
        public String dateFinPoste;
    }

    /**
     * Retourne les informations spécifiques au travailleur au format HTML
     * 
     * @param idTravailleur String représentant un id travailleur
     * @return Données HTML
     * @throws JadePersistenceException
     * @throws NoteException
     */
    public InformationsTravailleurs getInformationsTravailleur(String idPosteTravail) throws NoteException,
            JadePersistenceException {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);

        InformationsTravailleurs infos = new InformationsTravailleurs();
        infos.dateNaissance = posteTravail.getDateNaissanceTravailleur();
        infos.genreSalaire = session.getCodeLibelle(posteTravail.getTypeSalaireAsValue());
        infos.qualification = session.getCodeLibelle(posteTravail.getQualificationAsValue());
        infos.note = retrieveNotes(posteTravail.getIdTravailleur());
        infos.dateFinPoste = getDateFinPoste(posteTravail.getFinActivite());
        return infos;
    }

    private String getDateFinPoste(Date finActivite) {
        if (finActivite == null || Date.isNull(finActivite.getSwissValue())) {
            return "";
        } else {
            return finActivite.getSwissValue();
        }
    }

    private String retrieveNotes(String idTravailleur) throws NoteException, JadePersistenceException {
        StringBuilder noteHtml = new StringBuilder("<b>");
        SimpleNoteSearch search = new SimpleNoteSearch();
        search.setForTableSource("PT_TRAVAILLEURS");
        search.setForSourceId(idTravailleur);
        JadeNoteService noteService = new JadeNoteServiceImpl();
        List<SimpleNote> notes = noteService.search(search);
        for (SimpleNote note : notes) {
            if (!noteHtml.toString().equals("<b>")) {
                noteHtml.append("<br/>");
            }
            noteHtml.append(note.memo);
        }
        noteHtml.append("</b>");
        return noteHtml.toString();
    }

    public void annoncerEnfants(String idTravailleur) throws JadePersistenceException {
        NotificationSimpleModel notificationSimpleModel = new NotificationSimpleModel();
        notificationSimpleModel.setInfoType(InfoType.ANNONCE_ENFANTS_TRAVAILLEUR.toString());
        notificationSimpleModel.setIdCible(idTravailleur);
        JadePersistenceManager.add(notificationSimpleModel);
    }
}
