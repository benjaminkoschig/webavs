package globaz.tucana.transfert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Repr�sentation d'une entit� avec ses enfants et son parent
 * 
 * @author fgo
 * 
 */
public class TUImportEntityCollector {
    private TUImportEntity entity = null;

    private Collection listEntities = null;

    private TUImportEntityCollector parentCollector = null;

    /**
     * Constructeur en passant l'entit�, qui repr�sente une entit� sans p�re ou donc une entit� du plus haut niveau
     * 
     * @param _entity
     */
    public TUImportEntityCollector(TUImportEntity _entity) {
        entity = _entity;
        listEntities = new ArrayList();
    }

    /**
     * Constructeur en passant en param�tre l'entit� p�re et l'entit� proprement-dite
     * 
     * @param _parentCollector
     * @param _entity
     */
    public TUImportEntityCollector(TUImportEntityCollector _parentCollector, TUImportEntity _entity) {
        entity = _entity;
        parentCollector = _parentCollector;
        listEntities = new ArrayList();
    }

    /**
     * Ajoute une entit� � la liste des entit�s filles
     * 
     * @param _entity
     */
    public void addEntity(TUImportEntityCollector _entity) {
        listEntities.add(_entity);
    }

    /**
     * R�cup�re l'entit�
     * 
     * @return
     */
    public TUImportEntity getEntity() {
        return entity;
    }

    /**
     * R�cup�re la liste des entit�s filles
     * 
     * @return
     */
    public Collection getListEntities() {
        return listEntities;
    }

    /**
     * R�cup�re le TUImportEntityCollector parent
     * 
     * @return
     */
    public TUImportEntityCollector getParentCollector() {
        return parentCollector;
    }

    /**
     * Retourne vrai s'il existe un TUImportEntityCollector parent
     * 
     * @return
     */
    public boolean hasParentCollector() {
        return parentCollector != null;
    }

}
