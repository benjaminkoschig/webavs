package globaz.vulpecula.vb.ctrlemployeur;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.jade.noteIt.NoteException;
import ch.globaz.jade.noteIt.SimpleNoteSearch;
import ch.globaz.jade.noteIt.business.service.JadeNoteService;
import ch.globaz.jade.noteIt.businessimpl.service.JadeNoteServiceImpl;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTControlesEmployeursAjaxViewBean extends JadeAbstractAjaxFindForDomain<ControleEmployeur> {
    private static final long serialVersionUID = -2919232113135309440L;

    private String idEmployeur;

    private List<ControleEmployeur> controlesEmployeurs;

    private JadeNoteService noteService = new JadeNoteServiceImpl();

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public List<ControleEmployeur> getControlesEmployeurs() {
        return controlesEmployeurs;
    }

    public void setControlesEmployeurs(List<ControleEmployeur> controlesEmployeurs) {
        this.controlesEmployeurs = controlesEmployeurs;
    }

    @Override
    public ControleEmployeur getEntity() {
        return new ControleEmployeur();
    }

    @Override
    public Repository<ControleEmployeur> getRepository() {
        return VulpeculaRepositoryLocator.getControleEmployeurRepository();
    }

    @Override
    public List<ControleEmployeur> findByRepository() {
        return VulpeculaRepositoryLocator.getControleEmployeurRepository().findByIdEmployeur(idEmployeur);
    }

    // Ne pas changer l'argument, WebSphere cherche une méthode avec comme signature Object, ce qui Tomcat ne fait pas
    public boolean hasNote(Object object) throws NoteException, JadePersistenceException {
        ControleEmployeur currentEntity = (ControleEmployeur) object;
        SimpleNoteSearch search = new SimpleNoteSearch();
        search.setForTableSource("PT_CONTROLES_EMPLOYEURS");
        search.setForSourceId(currentEntity.getId());
        return noteService.search(search).size() > 0;
    }
}
