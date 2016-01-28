package globaz.tucana.model;

import globaz.itucana.model.ITUEntete;
import globaz.itucana.model.ITUModelBouclement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Classe commune des modèles
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public class TUCommonModel implements ITUModelBouclement {
    private TUEnteteBouclement entete = null;
    private Collection lignes = null;

    /**
     * Constructeur de la classe
     */
    public TUCommonModel() {
        super();
        entete = new TUEnteteBouclement();
        lignes = new ArrayList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itucana.model.ITUBouclement#addLine(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addLine(String canton, String rubrique, String montantNombre) {
        lignes.add(new TULigneBouclement(canton, rubrique, montantNombre));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itucana.model.ITUModelBouclement#getEntete()
     */
    @Override
    public ITUEntete getEntete() {
        return entete;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itucana.model.ITUModelBouclement#getLines()
     */
    @Override
    public Iterator getLines() {
        return lignes.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itucana.model.ITUBouclement#setEntete(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setEntete(String anneeComptable, String moisComptable, String numeroPassage) {
        entete = new TUEnteteBouclement(anneeComptable, moisComptable, numeroPassage);
    }
}
