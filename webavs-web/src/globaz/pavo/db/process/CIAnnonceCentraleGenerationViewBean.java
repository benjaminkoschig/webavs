package globaz.pavo.db.process;

import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.vb.CIAbstractPersistentViewBean;
import ch.globaz.common.business.exceptions.CommonTechnicalException;

/**
 * @author mmo
 */
public class CIAnnonceCentraleGenerationViewBean extends CIAbstractPersistentViewBean {

    private String idAnnonceCentrale = null;
    private String libelleTypeAnnonce = null;
    private String moisAnneeCreation = null;

    @Override
    public void add() throws Exception {
        throw new CommonTechnicalException("Not implemented");

    }

    @Override
    public void delete() throws Exception {
        throw new CommonTechnicalException("Not implemented");
    }

    public String getIdAnnonceCentrale() {
        return idAnnonceCentrale;
    }

    public String getLibelleTypeAnnonce() {
        return libelleTypeAnnonce;
    }

    public String getMoisAnneeCreation() {
        return moisAnneeCreation;
    }

    @Override
    public void retrieve() throws Exception {

        CIAnnonceCentrale annonceCentrale = new CIAnnonceCentrale();
        annonceCentrale.setSession(getSession());
        annonceCentrale.setId(getId());
        annonceCentrale.retrieve();

        idAnnonceCentrale = annonceCentrale.getAnnonceCentraleId();

        moisAnneeCreation = annonceCentrale.getDateCreation().substring(3);

        String idTypeAnnonce = annonceCentrale.getIdTypeAnnonce();
        libelleTypeAnnonce = CodeSystem.getLibelle(idTypeAnnonce, getSession());

    }

    public void setIdAnnonceCentrale(String idAnnonceCentrale) {
        this.idAnnonceCentrale = idAnnonceCentrale;
    }

    public void setLibelleTypeAnnonce(String libelleTypeAnnonce) {
        this.libelleTypeAnnonce = libelleTypeAnnonce;
    }

    public void setMoisAnneeCreation(String moisAnneeCreation) {
        this.moisAnneeCreation = moisAnneeCreation;
    }

    @Override
    public void update() throws Exception {
        throw new CommonTechnicalException("Not implemented");

    }

}
