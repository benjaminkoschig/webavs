package ch.globaz.vulpecula.repositoriesjade.congepaye.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.congepaye.LigneCompteurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.LigneCompteurSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.LigneCompteur;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class LigneCompteurConverter implements
        DomaineConverterJade<LigneCompteur, JadeComplexModel, LigneCompteurSimpleModel> {

    private final static LigneCompteurConverter INSTANCE = new LigneCompteurConverter();

    public static LigneCompteurConverter getInstance() {
        return INSTANCE;
    }

    public List<LigneCompteur> convertToDomain(List<LigneCompteurSimpleModel> lignesCompteursSimpleModel) {
        List<LigneCompteur> lignesCompteurs = new ArrayList<LigneCompteur>();
        for (LigneCompteurSimpleModel ligneCompteurSimpleModel : lignesCompteursSimpleModel) {
            lignesCompteurs.add(convertToDomain(ligneCompteurSimpleModel));
        }
        return lignesCompteurs;
    }

    public LigneCompteur convertToDomain(LigneCompteurSimpleModel ligneCompteurSimpleModel, Compteur compteur,
            CongePaye congePaye) {
        LigneCompteur ligneCompteur = new LigneCompteur(compteur, congePaye);
        ligneCompteur.setId(ligneCompteurSimpleModel.getId());
        ligneCompteur.setMontant(new Montant(ligneCompteurSimpleModel.getMontant()));
        ligneCompteur.setSpy(ligneCompteurSimpleModel.getSpy());
        return ligneCompteur;
    }

    @Override
    public LigneCompteurSimpleModel convertToPersistence(LigneCompteur ligneCompteur) {
        LigneCompteurSimpleModel ligneCompteurSimpleModel = new LigneCompteurSimpleModel();
        ligneCompteurSimpleModel.setId(ligneCompteur.getId());
        ligneCompteurSimpleModel.setIdCompteur(ligneCompteur.getIdCompteur());
        ligneCompteurSimpleModel.setIdCongePaye(ligneCompteur.getIdCongePaye());
        ligneCompteurSimpleModel.setMontant(ligneCompteur.getMontant().getValueNormalisee());
        ligneCompteurSimpleModel.setSpy(ligneCompteur.getSpy());
        return ligneCompteurSimpleModel;
    }

    @Override
    public LigneCompteur convertToDomain(JadeComplexModel model) {
        return null;
    }

    @Override
    public LigneCompteur convertToDomain(LigneCompteurSimpleModel simpleModel) {
        Compteur compteur = new Compteur(simpleModel.getIdCompteur());
        CongePaye congePaye = new CongePaye(simpleModel.getIdCongePaye());
        return convertToDomain(simpleModel, compteur, congePaye);
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new LigneCompteurSearchSimpleModel();
    }
}
