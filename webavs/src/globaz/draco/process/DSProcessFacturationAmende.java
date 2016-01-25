package globaz.draco.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.FWFindParameter;
import globaz.leo.constantes.ILEConstantes;

/**
 * 
 * @author MMO 16.06.2011
 */
public class DSProcessFacturationAmende extends DSProcessFacturationTaxeSommation2 {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ID_CS_MONTANT_AMENDE = "123006";
    public static final String ID_CS_RUBRIQUE_AMENDE = "123005";

    public static final String KEY_PARAM_MONTANT_AMENDE = "MONTAMENDE";
    public static final String KEY_PARAM_RUBRIQUE_AMENDE = "RUBAMENDE";

    public DSProcessFacturationAmende() {
        super();
    }

    public DSProcessFacturationAmende(BProcess parent) {
        super(parent);
    }

    @Override
    protected void initProcessAttributes() throws Exception {

        try {
            setEtape(ILEConstantes.CS_DEF_FORMULE_AMENDE_DS);

        } catch (Exception e) {
            throw new Exception("Error during init attributes of " + this.getClass().getName() + " due to "
                    + e.toString());
        }
    }

    @Override
    protected void initVariableTaxeSelonAnnee(String annee) throws Exception {
        // PO 9156
        String dateReference = "01.01." + annee;
        setRubrique(FWFindParameter.findParameter(getTransaction(), DSProcessFacturationAmende.ID_CS_RUBRIQUE_AMENDE,
                DSProcessFacturationAmende.KEY_PARAM_RUBRIQUE_AMENDE, dateReference, "0", 0));
        setMontant(FWFindParameter.findParameter(getTransaction(), DSProcessFacturationAmende.ID_CS_MONTANT_AMENDE,
                DSProcessFacturationAmende.KEY_PARAM_MONTANT_AMENDE, dateReference, "0", 0));
    }

}