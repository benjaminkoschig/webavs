package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.common.converter.ConvertValueEnum;
import ch.globaz.common.persistence.DomaineConverterJade;
import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.rpc.business.models.SimpleAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleAnnonceSearch;
import ch.globaz.pegasus.rpc.domaine.Annonce;
import ch.globaz.pegasus.rpc.domaine.AnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.LotAnnonce;
import ch.globaz.pegasus.rpc.domaine.TypeAnnonce;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AnnonceConverter implements DomaineConverterJade<AnnonceRpc, SimpleAnnonce> {

    private static final ConvertValueEnum<String, EtatAnnonce> etatConverter = new ConvertValueEnum<String, EtatAnnonce>();
    private static final ConvertValueEnum<String, CodeTraitement> codeTraitementConverter = new ConvertValueEnum<String, CodeTraitement>();
    private static final ConvertValueEnum<String, TypeAnnonce> typeConverter = new ConvertValueEnum<String, TypeAnnonce>();
    private static final BiMap<Enum<?>, String> all = HashBiMap.create();
    static {
        etatConverter.put("64074001", EtatAnnonce.ERROR);
        etatConverter.put("64074002", EtatAnnonce.OUVERT);
        etatConverter.put("64074003", EtatAnnonce.ENVOYE);
        etatConverter.put("64074004", EtatAnnonce.CORRECTION);
        etatConverter.put("64074005", EtatAnnonce.POUR_ENVOI);
        etatConverter.put("64074006", EtatAnnonce.PLAUSI_KO);

        codeTraitementConverter.put("64075001", CodeTraitement.REFUS);
        codeTraitementConverter.put("64075002", CodeTraitement.REFUS_CORRIGE);
        codeTraitementConverter.put("64075003", CodeTraitement.RETOUR_A_TRAITER);
        codeTraitementConverter.put("64075004", CodeTraitement.RETOUR_ACCEPTE);
        codeTraitementConverter.put("64075005", CodeTraitement.RETOUR_CORRIGE);
        codeTraitementConverter.put("64075006", CodeTraitement.RETOUR_AVERTISEMENT);

        typeConverter.put("64077001", TypeAnnonce.PARTIEL);
        typeConverter.put("64077002", TypeAnnonce.COMPLET);
        typeConverter.put("64077003", TypeAnnonce.ANNULATION);
        all.putAll(etatConverter.toMapEnum());
        all.putAll(typeConverter.toMapEnum());
        all.putAll(codeTraitementConverter.toMapEnum());
    }

    @Override
    public SimpleAnnonce convertToPersistence(AnnonceRpc entity) {
        SimpleAnnonce simpleModel = new SimpleAnnonce();
        simpleModel.setId(entity.getId());
        simpleModel.setIdDossier(entity.getDossier().getId());
        if (entity.getVersionDroit() != null) {
            simpleModel.setIdVersionDroit(entity.getVersionDroit().getId());
        }
        if (entity.getCodeTraitement() != null) {
            simpleModel.setCsCodeTraitement(codeTraitementConverter.convert(entity.getCodeTraitement()));
        }
        simpleModel.setIdLot(entity.getLot().getId());
        simpleModel.setIdDemande(entity.getDemande().getId());
        simpleModel.setCsEtat(etatConverter.convert(entity.getEtat()));
        simpleModel.setCsType(typeConverter.convert(entity.getType()));
        simpleModel.setSpy(entity.getSpy());
        return simpleModel;
    }

    public static String translate(Enum<?> enumm, BSession session) {
        return session.getCodeLibelle(toCsCode(enumm));
    }

    public static String toCsCode(Enum<?> enumm) {
        return all.get(enumm);
    }

    public static <T> T toEnum(String cs) {
        return (T) all.inverse().get(cs);
    }

    @Override
    public AnnonceRpc convertToDomain(SimpleAnnonce model) {
        Annonce annonce = new Annonce();
        annonce.setId(model.getId());
        annonce.setSpy(model.getSpy());
        Dossier dossier = new Dossier();
        dossier.setId(model.getIdDossier());
        Demande demande = new Demande();
        demande.setId(model.getIdDemande());
        if (!JadeStringUtil.isBlankOrZero(model.getIdVersionDroit())) {
            annonce.setVersionDroit(new VersionDroit(model.getIdVersionDroit()));
        }
        annonce.setDossier(dossier);
        annonce.setDemande(demande);
        annonce.setEtat(etatConverter.convert(model.getCsEtat()));
        annonce.setType(typeConverter.convert(model.getCsType()));
        LotAnnonce lotAnnonce = new LotAnnonce();
        lotAnnonce.setId(model.getIdLot());
        annonce.setLot(lotAnnonce);
        if (model.getCsCodeTraitement() != null && !"0".equals(model.getCsCodeTraitement())) {
            annonce.setCodeTraitement(codeTraitementConverter.convert(model.getCsCodeTraitement()));
        }
        return annonce;
    }

    @Override
    public DomaineJadeAbstractSearchModel getSearchModel() {
        return new SimpleAnnonceSearch();
    }

}
