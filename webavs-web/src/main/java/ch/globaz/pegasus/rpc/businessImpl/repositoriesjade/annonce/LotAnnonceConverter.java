package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import ch.globaz.common.converter.ConvertValueEnum;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.persistence.DomaineConverterJade;
import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonceSearch;
import ch.globaz.pegasus.rpc.domaine.EtatLot;
import ch.globaz.pegasus.rpc.domaine.LotAnnonce;
import ch.globaz.pegasus.rpc.domaine.LotAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.TypeLot;

public class LotAnnonceConverter implements DomaineConverterJade<LotAnnonceRpc, SimpleLotAnnonce> {

    ConvertValueEnum<String, EtatLot> etatConverter = new ConvertValueEnum<String, EtatLot>();
    ConvertValueEnum<String, TypeLot> typConverter = new ConvertValueEnum<String, TypeLot>();

    public LotAnnonceConverter() {
        etatConverter.put("64076001", EtatLot.ERROR);
        etatConverter.put("64076002", EtatLot.EN_GENERATION);
        etatConverter.put("64077003", EtatLot.ENVOYE);
        etatConverter.put("64078004", EtatLot.GENERATION_TEMINE);

        typConverter.put("1", TypeLot.INITIAL);
    }

    @Override
    public SimpleLotAnnonce convertToPersistence(LotAnnonceRpc entity) {
        SimpleLotAnnonce model = new SimpleLotAnnonce();
        model.setCsEtat(etatConverter.convert(entity.getEtat()));
        model.setDateEnvoi(entity.getDateEnvoi().getSwissValue());
        model.setCsType(typConverter.convert(entity.getType()));
        model.setIdJob(entity.getIdJob());
        return model;
    }

    @Override
    public LotAnnonceRpc convertToDomain(SimpleLotAnnonce model) {
        Date date = null;
        if (model.getDateEnvoi() != null) {
            date = new Date(model.getDateEnvoi());
        }
        EtatLot etatLot = etatConverter.convert(model.getCsEtat());
        return new LotAnnonce(model.getId(), date, etatLot, model.getIdJob());
    }

    @Override
    public DomaineJadeAbstractSearchModel getSearchModel() {
        return new SimpleLotAnnonceSearch();
    }
}
