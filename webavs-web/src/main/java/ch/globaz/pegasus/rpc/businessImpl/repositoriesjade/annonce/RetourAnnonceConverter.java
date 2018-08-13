package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import ch.globaz.common.converter.ConvertValueEnum;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.persistence.DomaineConverterJade;
import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;
import ch.globaz.common.persistence.RepositoryJade;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.DecisionSuppressionSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleLienAnnonceDecision;
import ch.globaz.pegasus.rpc.business.models.SimpleLienAnnonceDecisionSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleRetourAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleRetourAnnonceSearch;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.StatusRetourAnnonce;
import ch.globaz.pegasus.rpc.domaine.TypeViolationPlausi;
import ch.globaz.pegasus.rpc.domaine.plausi.PlausiRetour;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class RetourAnnonceConverter implements DomaineConverterJade<RetourAnnonceRpc, SimpleRetourAnnonce> {

    ConvertValueEnum<String, StatusRetourAnnonce> statusConverter = new ConvertValueEnum<String, StatusRetourAnnonce>();
    ConvertValueEnum<String, RpcPlausiType> typeConverter = new ConvertValueEnum<String, RpcPlausiType>();
    ConvertValueEnum<String, TypeViolationPlausi> typeViolationConverter = new ConvertValueEnum<String, TypeViolationPlausi>();
    ConvertValueEnum<String, RpcPlausiCategory> categoriePlausiConverter = new ConvertValueEnum<String, RpcPlausiCategory>();
    private static final BiMap<Enum<?>, String> all = HashBiMap.create();

    public RetourAnnonceConverter() {
        statusConverter.put("64081001", StatusRetourAnnonce.A_TRAITER);
        statusConverter.put("64081002", StatusRetourAnnonce.ACCEPTE);
        statusConverter.put("64081003", StatusRetourAnnonce.TRAITE);
        statusConverter.put("64081004", StatusRetourAnnonce.MODIFICATION_DROIT);
        typeViolationConverter.put("64078001", TypeViolationPlausi.GENERAL);
        typeViolationConverter.put("64078002", TypeViolationPlausi.PERSON);
        typeViolationConverter.put("64078003", TypeViolationPlausi.OVERLAP);
        typeConverter.put("64079001", RpcPlausiType.INTRA);
        typeConverter.put("64079002", RpcPlausiType.CALCUL);
        typeConverter.put("64079003", RpcPlausiType.INFO);
        typeConverter.put("64079004", RpcPlausiType.INTER);
        typeConverter.put("64079005", RpcPlausiType.SIMPLE);
        typeConverter.put("64079006", RpcPlausiType.INTERNAL);
        categoriePlausiConverter.put("64080001", RpcPlausiCategory.BLOCKING);
        categoriePlausiConverter.put("64080002", RpcPlausiCategory.AUTO);
        categoriePlausiConverter.put("64080003", RpcPlausiCategory.DATA_INTEGRITY);
        categoriePlausiConverter.put("64080004", RpcPlausiCategory.ERROR);
        categoriePlausiConverter.put("64080005", RpcPlausiCategory.INACTIVE);
        categoriePlausiConverter.put("64080006", RpcPlausiCategory.INFO);
        categoriePlausiConverter.put("64080007", RpcPlausiCategory.MANUAL);
        categoriePlausiConverter.put("64080008", RpcPlausiCategory.NONE);
        categoriePlausiConverter.put("64080009", RpcPlausiCategory.WARNING);
        all.putAll(statusConverter.toMapEnum());
        all.putAll(typeViolationConverter.toMapEnum());
        all.putAll(typeConverter.toMapEnum());
        all.putAll(categoriePlausiConverter.toMapEnum());
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

    public List<SimpleRetourAnnonce> convertToPersistence(PlausiRetour plausiRetour) {
        List<SimpleRetourAnnonce> retoursAnnonce = new ArrayList<SimpleRetourAnnonce>();
        for (RetourAnnonce retour : plausiRetour.getRetours()) {
            retour.setNssAnnonce(plausiRetour.getNss());
            retoursAnnonce.add(convertToPersistence(retour));
        }
        return retoursAnnonce;
    }

    @Override
    public SimpleRetourAnnonce convertToPersistence(RetourAnnonceRpc entity) {
        SimpleRetourAnnonce model = new SimpleRetourAnnonce();
        model.setId(entity.getId());
        model.setSpy(entity.getSpy());
        model.setIdLien(entity.getIdLien());
        model.setIdLienAnnonceDecision(entity.getIdLien());
        model.setCsCategoriePlausi(categoriePlausiConverter.convert(entity.getCategorie()));
        model.setCsTypeViolationPlausi(typeViolationConverter.convert(entity.getTypeViolationPlausi()));
        model.setCsTypePlausi(typeConverter.convert(entity.getType()));
        model.setCodePlausi(entity.getCodePlausi());
        model.setOfficePC(entity.getOfficePC());
        model.setOfficePCConflit(entity.getOfficePCConflit());
        model.setNssAnnonce(entity.getNssAnnonce());
        model.setNssPersonne(entity.getNssPersonne());
        model.setCaseIdConflit(entity.getCaseIdConflit());
        model.setDecisionIdConflit(entity.getDecisionIdConflit());
        if (entity.getValidFromConflit() != null) {
            model.setValidFromConflit(entity.getValidFromConflit().getSwissValue());
        }
        if (entity.getValidToConflit() != null) {
            model.setValidToConflit(entity.getValidToConflit().getSwissValue());
        }
        model.setCsStatusRetour(statusConverter.convert(entity.getStatus()));
        model.setRemarqueRetour(entity.getRemarque());
        return model;
    }

    @Override
    public RetourAnnonceRpc convertToDomain(SimpleRetourAnnonce model) {

        StatusRetourAnnonce status = StatusRetourAnnonce.A_TRAITER;
        if (!model.getCsStatusRetour().equals("0")) {
            status = statusConverter.convert(model.getCsStatusRetour());
        }

        RpcPlausiCategory categorie = categoriePlausiConverter.convert(model.getCsCategoriePlausi());
        RpcPlausiType type = typeConverter.convert(model.getCsTypePlausi());
        TypeViolationPlausi typeVio = resolveTypeViolation(model);

        RetourAnnonce retourAnnonce = new RetourAnnonce(model.getCodePlausi(), categorie, type, typeVio);

        if (Date.isValid(model.getValidFromConflit())) {
            retourAnnonce.setValidFromConflit(new Date(model.getValidFromConflit()));
        }
        if (Date.isValid(model.getValidToConflit())) {
            retourAnnonce.setValidToConflit(new Date(model.getValidToConflit()));
        }
        retourAnnonce.setStatus(status);
        retourAnnonce.setCaseIdConflit(model.getCaseIdConflit());
        retourAnnonce.setDecisionIdConflit(model.getDecisionIdConflit());
        retourAnnonce.setId(model.getId());
        retourAnnonce.setIdLien(model.getIdLien());
        retourAnnonce.setIdLienAnnonceDecision(resolveSimpleLienAnnonce(model.getIdLien()));
        retourAnnonce.setNssAnnonce(model.getNssAnnonce());
        retourAnnonce.setNssPersonne(model.getNssPersonne());
        retourAnnonce.setOfficePC(model.getOfficePC());
        retourAnnonce.setOfficePCConflit(model.getOfficePCConflit());
        retourAnnonce.setRemarque(model.getRemarqueRetour());
        retourAnnonce.setSpy(model.getSpy());
        
        if(!JadeStringUtil.isBlank(model.getIdDecision())) {
            resolveDecision(retourAnnonce, model.getIdDecision());
        }

        return retourAnnonce;
    }

    private SimpleLienAnnonceDecision resolveSimpleLienAnnonce(String idLien) {
        // Load SimpleModel: SimpleLienAnnonceDecision
        SimpleLienAnnonceDecisionSearch search = new SimpleLienAnnonceDecisionSearch();
        search.setForId(idLien);
        List<SimpleLienAnnonceDecision> liensAnnonce = RepositoryJade.searchForAndFetch(search);
        if (!liensAnnonce.isEmpty()) {
            return liensAnnonce.get(0);
        }
        return null;
    }
    
    private void resolveDecision(RetourAnnonce retourAnnonce, String idDecision) {

        DecisionApresCalculSearch search2 = new DecisionApresCalculSearch();
        search2.setForIdDecisionHeader(idDecision);
        List<DecisionApresCalcul> decisionAPC = RepositoryJade.searchForAndFetch(search2);
        if (!decisionAPC.isEmpty()) {
            DecisionApresCalcul decision = decisionAPC.get(0);
            retourAnnonce.setIdDemande(decision.getVersionDroit().getDemande().getId());
            retourAnnonce.setIdDroit(decision.getVersionDroit().getId());
            return ;
        }
        
        DecisionSuppressionSearch search3 = new DecisionSuppressionSearch();
        search3.setForIdDecisionHeader(idDecision);
        List<DecisionSuppression> decisionSup = RepositoryJade.searchForAndFetch(search3);
        if (!decisionSup.isEmpty()) {
            DecisionSuppression decision = decisionSup.get(0);
            retourAnnonce.setIdDemande(decision.getVersionDroit().getDemande().getId());
            retourAnnonce.setIdDroit(decision.getVersionDroit().getId());
            return ;
        }
        
    }

    private TypeViolationPlausi resolveTypeViolation(SimpleRetourAnnonce model) {
        return typeViolationConverter.convert(model.getCsTypeViolationPlausi());
    }

    @Override
    public DomaineJadeAbstractSearchModel getSearchModel() {
        return new SimpleRetourAnnonceSearch();
    }
}
