package globaz.draco.db.declaration;

import globaz.draco.vb.DSAbstractPersistentViewBean;

public class DSImprimerDeclarationViewBean extends DSAbstractPersistentViewBean {

    private String idDeclaration = "";

    /**
     * @throws Exception
     */
    public DSImprimerDeclarationViewBean() throws Exception {
        super();
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

}
