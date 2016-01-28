package ch.globaz.vulpecula.businessimpl.services.compteur;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.models.congepaye.LigneCompteurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.LigneCompteurSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.compteur.CompteurGSON;
import ch.globaz.vulpecula.business.services.compteur.CompteurService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.congepaye.Compteurs;
import ch.globaz.vulpecula.domain.models.congepaye.LigneCompteur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.congepaye.CompteurRepository;
import ch.globaz.vulpecula.repositoriesjade.congepaye.converters.LigneCompteurConverter;

public class CompteurServiceImpl implements CompteurService {
    private CompteurRepository compteurRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public CompteurServiceImpl() {
        compteurRepository = VulpeculaRepositoryLocator.getCompteurRepository();
    }

    public CompteurServiceImpl(CompteurRepository compteurRepository) {
        this.compteurRepository = compteurRepository;
    }

    @Override
    public List<CompteurGSON> findByPosteTravailAndPeriode(String idPosteTravail, String anneeDebut, String anneeFin) {
        List<Compteur> list = compteurRepository.findByPosteTravailAndPeriode(idPosteTravail, anneeDebut, anneeFin);
        List<CompteurGSON> listeString = new ArrayList<CompteurGSON>();
        for (Compteur compteur : list) {
            CompteurGSON compteurGSON = new CompteurGSON();
            compteurGSON.setMontantCompteur(Double.valueOf(compteur.getCumulCotisation().getValueNormalisee()));
            compteurGSON.setMontantRestant(Double.valueOf(compteur.getMontantRestant().getValueNormalisee()));
            compteurGSON.setAnnee(compteur.getAnneeAsValue());
            listeString.add(compteurGSON);
        }
        return listeString;
    }

    @Override
    public double getMontantTotalForPoste(String idPosteTravail, String anneeDebut, String anneeFin) {
        List<Compteur> list = compteurRepository.findByPosteTravailAndPeriode(idPosteTravail, anneeDebut, anneeFin);
        BigDecimal montantTotal = BigDecimal.ZERO;
        for (Compteur compteur : list) {
            montantTotal = montantTotal.add(compteur.getMontantRestant().getBigDecimalValue());
        }
        return montantTotal.doubleValue();
    }

    @Override
    public Compteurs findCompteursByIdAndCreateIfNecessary(String idPosteTravail, Annee anneeDebut, Annee anneeFin) {
        List<Compteur> compteurs = new ArrayList<Compteur>();

        List<Annee> annees = Annee.between(anneeDebut, anneeFin);

        for (Annee annee : annees) {
            Compteur compteur = compteurRepository.findByPosteTravailAndAnnee(idPosteTravail, annee);
            if (compteur == null) {
                compteur = createCompteur(idPosteTravail, annee);
            }
            compteurs.add(compteur);
        }

        return new Compteurs(compteurs);
    }

    @Override
    public Compteur createCompteur(String idPosteTravail, Annee annee) {
        PosteTravail posteTravail = new PosteTravail(idPosteTravail);

        Compteur compteur = new Compteur();
        compteur.setAnnee(annee);
        compteur.setPosteTravail(posteTravail);

        return compteurRepository.create(compteur);
    }

    @Override
    public void update(Compteurs compteurs) {
        for (Compteur compteur : compteurs) {
            compteurRepository.update(compteur);
        }

    }

    @Override
    public List<LigneCompteur> findByIdCompteur(String idCompteur) {
        List<LigneCompteur> lignes = new ArrayList<LigneCompteur>();
        LigneCompteurSearchSimpleModel searchModel = new LigneCompteurSearchSimpleModel();
        searchModel.setForIdCompteur(idCompteur);
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }

        for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
            LigneCompteurSimpleModel ligneCompteurSimpleModel = (LigneCompteurSimpleModel) abstractModel;
            LigneCompteur ligneCompteur = LigneCompteurConverter.getInstance()
                    .convertToDomain(ligneCompteurSimpleModel);
            lignes.add(ligneCompteur);
        }
        return lignes;
    }

    @Override
    public boolean hasCompteurs(String idPosteTravail) {
        List<Compteur> listCompteurs = compteurRepository.findByIdPosteTravail(idPosteTravail);
        Compteurs compteurs = new Compteurs(listCompteurs);
        return compteurs.hasCompteursActifs();
    }

    @Override
    public boolean hasLignePourPeriodeSaisie(String idPosteTravail, Annee anneeDebut, Annee anneeFin) {
        List<Compteur> listCompteurs = compteurRepository.findByIdPosteTravailWithDependencies(idPosteTravail);
        Compteurs compteurs = new Compteurs(listCompteurs);
        return compteurs.hasLignePourPeriodeSaisie(anneeDebut, anneeFin);
    }
}
