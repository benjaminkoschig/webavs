package globaz.vulpecula.vb.decompte;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.jade.noteIt.NoteException;
import ch.globaz.jade.noteIt.SimpleNoteSearch;
import ch.globaz.jade.noteIt.business.service.JadeNoteService;
import ch.globaz.jade.noteIt.businessimpl.service.JadeNoteServiceImpl;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModelAJAX;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSearchComplexModelAJAX;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;

public class PTDecompteAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<DecompteComplexModelAJAX, DecompteSearchComplexModelAJAX> {
    private static final long serialVersionUID = 303802298398045415L;

    private JadeNoteService noteService = new JadeNoteServiceImpl();

    private DecompteSearchComplexModelAJAX searchModel;
    private DecompteComplexModelAJAX currentEntity;

    public PTDecompteAjaxViewBean() {
        searchModel = new DecompteSearchComplexModelAJAX();
        currentEntity = new DecompteComplexModelAJAX();
    }

    @Override
    public void initList() {
    }

    @Override
    public DecompteComplexModelAJAX getCurrentEntity() {
        return currentEntity;
    }

    // Ne pas changer l'argument, WebSphere cherche une méthode avec comme signature Object, ce qui Tomcat ne fait pas
    public boolean hasNote(Object object) throws NoteException, JadePersistenceException {
        DecompteComplexModelAJAX currentEntity = (DecompteComplexModelAJAX) object;
        SimpleNoteSearch search = new SimpleNoteSearch();
        search.setForTableSource("PT_DECOMPTES");
        search.setForSourceId(currentEntity.getId());
        return noteService.search(search).size() > 0;
    }

    @Override
    public DecompteSearchComplexModelAJAX getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<DecompteComplexModelAJAX, DecompteSearchComplexModelAJAX> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getDecompteServiceCRUD();
    }

    @Override
    public void setCurrentEntity(DecompteComplexModelAJAX entite) {
        currentEntity = entite;
    }

    @Override
    public void setSearchModel(DecompteSearchComplexModelAJAX jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }

}
