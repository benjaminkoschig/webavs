/*
 * Cr�� le 3 nov. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.babel.vb.copies;

import globaz.babel.db.copies.CTDocumentJointDefaultCopiesManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CTDocumentJointDefaultCopiesListViewBean extends CTDocumentJointDefaultCopiesManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDomaineDocument = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTDocumentJointDefaultCopiesViewBean();
    }

    /**
     * @return
     */
    public String getCsDomaineDocument() {
        return csDomaineDocument;
    }

    /**
     * @param string
     */
    public void setCsDomaineDocument(String string) {
        csDomaineDocument = string;
    }

}
