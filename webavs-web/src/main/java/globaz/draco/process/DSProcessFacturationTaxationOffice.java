package globaz.draco.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.FWFindParameter;
import globaz.leo.constantes.ILEConstantes;

/**
 * 
 * @author MMO 16.06.2011
 */
public class DSProcessFacturationTaxationOffice extends DSProcessFacturationTaxeSommation2 {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ID_CS_MONTANT_TO = "123008";
    public static final String ID_CS_RUBRIQUE_TO = "123007";

    public static final String KEY_PARAM_MONTANT_TO = "MONTTO";
    public static final String KEY_PARAM_RUBRIQUE_TO = "RUBTO";

    public DSProcessFacturationTaxationOffice() {
        super();
    }

    public DSProcessFacturationTaxationOffice(BProcess parent) {
        super(parent);
    }

    @Override
    protected void initProcessAttributes() throws Exception {
        try {
            setEtape(ILEConstantes.CS_DEF_FORMULE_TAXATION_DS);
        } catch (Exception e) {
            throw new Exception("Error during init attributes of " + this.getClass().getName() + " due to "
                    + e.toString());
        }
    }

    @Override
    protected void initVariableTaxeSelonAnnee(String annee) throws Exception {

        String dateReference = "01.01." + annee;
        setRubrique(FWFindParameter.findParameter(getTransaction(),
                DSProcessFacturationTaxationOffice.ID_CS_RUBRIQUE_TO,
                DSProcessFacturationTaxationOffice.KEY_PARAM_RUBRIQUE_TO, dateReference, "0", 0));
        setMontant(FWFindParameter.findParameter(getTransaction(), DSProcessFacturationTaxationOffice.ID_CS_MONTANT_TO,
                DSProcessFacturationTaxationOffice.KEY_PARAM_MONTANT_TO, dateReference, "0", 0));
    }
}