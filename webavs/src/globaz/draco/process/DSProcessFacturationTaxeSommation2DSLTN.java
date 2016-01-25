package globaz.draco.process;

import globaz.globall.db.BProcess;
import globaz.leo.constantes.ILEConstantes;
import globaz.osiris.api.APISection;

/**
 * 
 * @author MMO 16.06.2011
 */
public class DSProcessFacturationTaxeSommation2DSLTN extends DSProcessFacturationTaxeSommation2 {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DSProcessFacturationTaxeSommation2DSLTN() {
        super();
    }

    public DSProcessFacturationTaxeSommation2DSLTN(BProcess parent) {
        super(parent);
    }

    @Override
    protected String giveInIdSousType() {
        return APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL + "," + APISection.ID_CATEGORIE_SECTION_LTN;
    }

    @Override
    protected void initProcessAttributes() throws Exception {

        try {
            setEtape(ILEConstantes.CS_DEF_FORMULE_SOMMATION_DS_LTN);
        } catch (Exception e) {
            throw new Exception("Error during init attributes of " + this.getClass().getName() + " due to "
                    + e.toString());
        }
    }
}