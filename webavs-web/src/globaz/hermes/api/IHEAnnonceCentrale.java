package globaz.hermes.api;

import globaz.globall.db.BTransaction;

public interface IHEAnnonceCentrale {
    public IHEOutputAnnonce[] getAnnoncesAdaptationRentes(BTransaction transaction, String idLot, Integer limiteDemandes)
            throws Exception;

    public IHEOutputAnnonce[] getAnnoncesRentes(globaz.globall.db.BTransaction transaction) throws Exception;

    public IHEOutputAnnonce[] getAnnoncesTerminees(globaz.globall.db.BTransaction transaction, Integer limiteNbDemandes)
            throws Exception;
}
