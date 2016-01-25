package globaz.hermes.api.helper;

import globaz.globall.db.BTransaction;
import globaz.globall.shared.GlobazValueObject;
import globaz.hermes.api.IHEAnnonceCentrale;
import globaz.hermes.api.IHEOutputAnnonce;

public class IHEAnnoncesCentraleHelper extends IHEOutputAnnonceHelper implements IHEAnnonceCentrale {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IHEAnnoncesCentraleHelper() {
        super("globaz.hermes.service.HEAnnoncesCentrale");
    }

    /**
     * Constructeur du type IHEOutputAnnonceHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IHEAnnoncesCentraleHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    @Override
    public IHEOutputAnnonce[] getAnnoncesAdaptationRentes(BTransaction transaction, String idLot, Integer limiteDemandes)
            throws Exception {
        Object res = _getObject("getAnnoncesAdaptationRentes", new Object[] { transaction, idLot, limiteDemandes });
        if (res == null) {
            return null;
        } else {
            return (IHEOutputAnnonce[]) res;
        }
    }

    @Override
    public IHEOutputAnnonce[] getAnnoncesRentes(BTransaction transaction) throws Exception {
        Object res = _getObject("getAnnoncesRentes", new Object[] { transaction });
        if (res == null) {
            return null;
        } else {
            return (IHEOutputAnnonce[]) res;
        }
    }

    @Override
    public IHEOutputAnnonce[] getAnnoncesTerminees(BTransaction transaction, Integer limiteNbDemandes) throws Exception {
        Object res = _getObject("getAnnoncesTerminees", new Object[] { transaction, limiteNbDemandes });
        if (res == null) {
            return null;
        } else {
            return (IHEOutputAnnonce[]) res;
        }
    }
}
