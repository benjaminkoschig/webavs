package ch.globaz.vulpecula.repositoriesjade.postetravail.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.vulpecula.business.models.ebusiness.TravailleurEbuSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurEbuSearchSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.ws.bean.AdresseEbu;
import ch.globaz.vulpecula.ws.bean.AssuranceMaladieEbu;
import ch.globaz.vulpecula.ws.bean.CompteBanquaireEbu;
import ch.globaz.vulpecula.ws.bean.ContratAssuranceMaladieEbu;
import ch.globaz.vulpecula.ws.bean.ConventionEbu;
import ch.globaz.vulpecula.ws.bean.DureeContratEbu;
import ch.globaz.vulpecula.ws.bean.PermisTravailEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;
import ch.globaz.vulpecula.ws.bean.StatusEbu;

/***
 * Convertisseur de {@link TravailleurComplexModel} Travailleur <-> {@link Travailleur}
 * 
 */
public final class TravailleurEbuConverter implements
        DomaineConverterJade<TravailleurEbuDomain, JadeComplexModel, TravailleurEbuSimpleModel> {

    public static final TravailleurEbuConverter INSTANCE = new TravailleurEbuConverter();

    public static TravailleurEbuConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public TravailleurEbuDomain convertToDomain(TravailleurEbuSimpleModel travailleurEbuSimpleModel) {
        TravailleurEbuDomain travailleur = new TravailleurEbuDomain();
        AdresseEbu adresseEbu = new AdresseEbu();
        adresseEbu.setDescription1(travailleurEbuSimpleModel.getAdresseDescription1());
        adresseEbu.setDescription2(travailleurEbuSimpleModel.getAdresseDescription2());
        adresseEbu.setLocalite(travailleurEbuSimpleModel.getAdresseLocalite());
        adresseEbu.setNpa(travailleurEbuSimpleModel.getAdresseNpa());
        adresseEbu.setRue(travailleurEbuSimpleModel.getAdresseRue());
        adresseEbu.setRueNumero(travailleurEbuSimpleModel.getAdresseRuenumero());
        adresseEbu.setCasePostale(travailleurEbuSimpleModel.getAdresseCasePostale());
        adresseEbu.setTitre(travailleurEbuSimpleModel.getAdresseTitre());
        adresseEbu.setPays(travailleurEbuSimpleModel.getAdressePays());
        travailleur.setAdresse(adresseEbu);
        if (!JadeStringUtil.isBlankOrZero(travailleurEbuSimpleModel.getCsCodeProfessionnel())) {
            travailleur
                    .setCodeProfessionnel(Qualification.fromValue(travailleurEbuSimpleModel.getCsCodeProfessionnel()));
        }
        CompteBanquaireEbu compteBanquaire = new CompteBanquaireEbu();
        compteBanquaire.setIban(travailleurEbuSimpleModel.getCompteBancaireIban());
        compteBanquaire.setLocalite(travailleurEbuSimpleModel.getCompteBancaireLocalite());
        compteBanquaire.setIdLocalite(travailleurEbuSimpleModel.getCompteBancaireLocaliteId());
        compteBanquaire.setNom(travailleurEbuSimpleModel.getCompteBancaireNom());
        travailleur.setCompteBancaire(compteBanquaire);

        if (travailleurEbuSimpleModel.getContratCollectifAssureur() != null
                && !"0".equals(travailleurEbuSimpleModel.getContratCollectifAssureur())) {
            ContratAssuranceMaladieEbu contratCollectif = new ContratAssuranceMaladieEbu();
            AssuranceMaladieEbu assuranceMaladieEbu = AssuranceMaladieEbu.fromValue(travailleurEbuSimpleModel
                    .getContratCollectifAssureur());
            contratCollectif.setAssureur(assuranceMaladieEbu);
            contratCollectif.setAssuranceCombinee(travailleurEbuSimpleModel.getContratCollectifAssuranceCombinee());
            travailleur.setContratCollectif(contratCollectif);
        }

        if (!JadeStringUtil.isBlankOrZero(travailleurEbuSimpleModel.getConvention())) {
            travailleur.setConvention(ConventionEbu.fromValue(travailleurEbuSimpleModel.getConvention()));
        }

        travailleur.setCorrelationId(travailleurEbuSimpleModel.getCorrelationId());
        travailleur.setPosteCorrelationId(travailleurEbuSimpleModel.getPosteCorrelationId());
        travailleur.setId(travailleurEbuSimpleModel.getId());
        if (!JadeStringUtil.isEmpty(travailleurEbuSimpleModel.getDateDebutActivite())) {
            travailleur.setDateDebutActivite(travailleurEbuSimpleModel.getDateDebutActivite());
        }
        if (!JadeStringUtil.isEmpty(travailleurEbuSimpleModel.getDateFinActivite())) {
            travailleur.setDateFinActivite(travailleurEbuSimpleModel.getDateFinActivite());
        }
        travailleur.setSexe(travailleurEbuSimpleModel.getSexe());

        travailleur.setDateInscription(travailleurEbuSimpleModel.getDateInscription());

        travailleur.setDateNaissance(travailleurEbuSimpleModel.getDateNaissance());

        DureeContratEbu duree = new DureeContratEbu();
        duree.setDateFin(travailleurEbuSimpleModel.getDureeContratDateFin());
        duree.setDeterminee(travailleurEbuSimpleModel.getDureeContratDeterminee());
        travailleur.setDureeContrat(duree);

        travailleur.setEtatCivil(EtatCivil.parse(travailleurEbuSimpleModel.getCsEtatCivil()));

        travailleur.setId(travailleurEbuSimpleModel.getId());
        travailleur.setIdTravailleur(travailleurEbuSimpleModel.getIdTravailleur());
        travailleur.setNationalite(travailleurEbuSimpleModel.getNationalite());
        travailleur.setNom(travailleurEbuSimpleModel.getNom());
        travailleur.setNombreEnfants(Integer.valueOf(travailleurEbuSimpleModel.getNombreEnfant()));
        travailleur.setNomEntreprise(travailleurEbuSimpleModel.getNomEntreprise());
        travailleur.setNss(travailleurEbuSimpleModel.getNss());
        travailleur.setNumeroEntreprise(travailleurEbuSimpleModel.getNumeroEntreprise());

        PermisTravailEbu permis = new PermisTravailEbu();
        permis.setCategoriePermis(travailleurEbuSimpleModel.getPermisSejourCategorie());
        permis.setNumeroPermis(travailleurEbuSimpleModel.getPermisSejourNumero());
        travailleur.setPermisSejour(permis);

        travailleur.setPrenom(travailleurEbuSimpleModel.getPrenom());
        travailleur.setProfession(travailleurEbuSimpleModel.getProfession());
        travailleur.setSpy(travailleurEbuSimpleModel.getSpy());
        travailleur.setTauxActivite(Double.valueOf(travailleurEbuSimpleModel.getTauxActivite()));
        travailleur.setDateTauxActivite(travailleurEbuSimpleModel.getDateTauxActivite());
        travailleur.setTelephone(travailleurEbuSimpleModel.getTelephone());

        // travailleur.setTraite(travailleurEbuSimpleModel.getTraite());
        travailleur.setStatus(StatusAnnonceTravailleurEbu.fromValue(travailleurEbuSimpleModel.getStatus()));
        travailleur.setIdEmployeur(travailleurEbuSimpleModel.getIdEmployeur());
        travailleur.setSalaire(new Montant(travailleurEbuSimpleModel.getSalaire()));
        if (!JadeNumericUtil.isEmptyOrZero(travailleurEbuSimpleModel.getTypeSalaire())) {
            travailleur.setTypeSalaire(TypeSalaire.fromValue(travailleurEbuSimpleModel.getTypeSalaire()));
        }
        travailleur.setIdDecompte(travailleurEbuSimpleModel.getIdDecompte());

        travailleur.setModification(travailleurEbuSimpleModel.getModification());

        if (travailleurEbuSimpleModel.getTiersStatus() != null && !travailleurEbuSimpleModel.getTiersStatus().isEmpty()) {
            travailleur.setTiersStatus(StatusEbu.fromValue(travailleurEbuSimpleModel.getTiersStatus()));
        }
        if (travailleurEbuSimpleModel.getTravailleurStatus() != null
                && !travailleurEbuSimpleModel.getTravailleurStatus().isEmpty()) {
            travailleur.setTravailleurStatus(StatusEbu.fromValue(travailleurEbuSimpleModel.getTravailleurStatus()));
        }
        if (travailleurEbuSimpleModel.getPosteStatus() != null && !travailleurEbuSimpleModel.getPosteStatus().isEmpty()) {
            travailleur.setPosteStatus(StatusEbu.fromValue(travailleurEbuSimpleModel.getPosteStatus()));
        }
        if (travailleurEbuSimpleModel.getBanqueStatus() != null
                && !travailleurEbuSimpleModel.getBanqueStatus().isEmpty()) {
            travailleur.setBanqueStatus(StatusEbu.fromValue(travailleurEbuSimpleModel.getBanqueStatus()));
        }
        if (travailleurEbuSimpleModel.getIdPosteTravail() != null
                && !travailleurEbuSimpleModel.getIdPosteTravail().isEmpty()) {
            travailleur.setIdPosteTravail(travailleurEbuSimpleModel.getIdPosteTravail());
        }

        travailleur.setStatus(StatusAnnonceTravailleurEbu.fromValue(travailleurEbuSimpleModel.getStatus()));
        // travailleur.setIsNewAnnonce(travailleurEbuSimpleModel.getIsNewAnnonce());

        return travailleur;
    }

    /**
     * Conversion d'un objet du domaine {@link Travailleur} en objet {@link TravailleurSimpleModel} *
     * 
     * @param travailleur
     *            {@link Travailleur} représentant un travailleur d'un employeur
     * @return {@link TravailleurSimpleModel}
     */
    @Override
    public TravailleurEbuSimpleModel convertToPersistence(final TravailleurEbuDomain travailleur) {
        TravailleurEbuSimpleModel travailleurSimpleModel = new TravailleurEbuSimpleModel();
        travailleurSimpleModel.setId(String.valueOf(travailleur.getId()));
        travailleurSimpleModel.setSpy(travailleur.getSpy());
        travailleurSimpleModel.setCorrelationId(travailleur.getCorrelationId());
        travailleurSimpleModel.setPosteCorrelationId(travailleur.getPosteCorrelationId());

        AdresseEbu adresseEbu = travailleur.getAdresse();
        if (adresseEbu != null) {
            travailleurSimpleModel.setAdresseDescription1(adresseEbu.getDescription1());
            travailleurSimpleModel.setAdresseDescription2(adresseEbu.getDescription2());
            travailleurSimpleModel.setAdresseLocalite(adresseEbu.getLocalite());
            travailleurSimpleModel.setAdresseCasePostale(adresseEbu.getCasePostale());
            travailleurSimpleModel.setAdresseNpa(adresseEbu.getNpa());
            travailleurSimpleModel.setAdresseRue(adresseEbu.getRue());
            travailleurSimpleModel.setAdresseRuenumero(adresseEbu.getRueNumero());
            travailleurSimpleModel.setAdresseTitre(adresseEbu.getTitre());
            travailleurSimpleModel.setAdressePays(adresseEbu.getPays());
        }

        CompteBanquaireEbu compte = travailleur.getCompteBancaire();
        if (compte != null) {
            travailleurSimpleModel.setCompteBancaireIban(compte.getIban());
            travailleurSimpleModel.setCompteBancaireLocalite(compte.getLocalite());
            travailleurSimpleModel.setCompteBancaireLocaliteId(compte.getIdLocalite());
            travailleurSimpleModel.setCompteBancaireNom(compte.getNom());
        }

        ContratAssuranceMaladieEbu contrat = travailleur.getContratCollectif();
        if (contrat != null) {
            travailleurSimpleModel.setContratCollectifAssuranceCombinee(contrat.isAssuranceCombinee());
            if (contrat.getAssureur() != null) {
                travailleurSimpleModel.setContratCollectifAssureur(contrat.getAssureur().getValue());
            }
        }

        if (travailleur.getConvention() != null) {
            travailleurSimpleModel.setConvention(travailleur.getConvention().getValue());
        }
        if (travailleur.getCodeProfessionnel() != null) {
            travailleurSimpleModel.setCsCodeProfessionnel(travailleur.getCodeProfessionnel().getValue());
        }

        if (travailleur.getEtatCivil() != null) {
            travailleurSimpleModel.setCsEtatCivil(String.valueOf(travailleur.getEtatCivil().getCodeSysteme()));
        }

        if (!JadeStringUtil.isEmpty(travailleur.getDateDebutActivite())) {
            Date date = new Date(travailleur.getDateDebutActivite());
            travailleurSimpleModel.setDateDebutActivite(date.getSwissValue());
        }

        if (!JadeStringUtil.isEmpty(travailleur.getDateFinActivite())) {
            Date date = new Date(travailleur.getDateFinActivite());
            travailleurSimpleModel.setDateFinActivite(date.getSwissValue());
        }

        travailleurSimpleModel.setSexe(travailleur.getSexe());

        travailleurSimpleModel.setDateInscription(travailleur.getDateInscription());
        travailleurSimpleModel.setDateNaissance(travailleur.getDateNaissance());

        DureeContratEbu dureeContrat = travailleur.getDureeContrat();
        if (dureeContrat != null && !JadeStringUtil.isEmpty(travailleur.getDureeContrat().getDateFin())) {
            Date date = new Date(dureeContrat.getDateFin());
            travailleurSimpleModel.setDureeContratDateFin(date.getSwissValue());
            travailleurSimpleModel.setDureeContratDeterminee(dureeContrat.isDeterminee());
        }

        travailleurSimpleModel.setId(travailleur.getId());
        travailleurSimpleModel.setIdTravailleur(travailleur.getIdTravailleur());
        travailleurSimpleModel.setNationalite(travailleur.getNationalite());
        travailleurSimpleModel.setNom(travailleur.getNom());
        travailleurSimpleModel.setNombreEnfant(String.valueOf(travailleur.getNombreEnfants()));
        travailleurSimpleModel.setNomEntreprise(travailleur.getNomEntreprise());
        travailleurSimpleModel.setNss(travailleur.getNss());
        travailleurSimpleModel.setNumeroEntreprise(String.valueOf(travailleur.getNumeroEntreprise()));

        PermisTravailEbu permis = travailleur.getPermisSejour();
        if (permis != null) {
            travailleurSimpleModel.setPermisSejourCategorie(permis.getCategoriePermis());
            travailleurSimpleModel.setPermisSejourNumero(permis.getNumeroPermis());
        }

        travailleurSimpleModel.setPrenom(travailleur.getPrenom());
        travailleurSimpleModel.setProfession(travailleur.getProfession());
        travailleurSimpleModel.setSpy(travailleur.getSpy());
        travailleurSimpleModel.setTauxActivite(String.valueOf(travailleur.getTauxActivite()));
        travailleurSimpleModel.setDateTauxActivite(travailleur.getDateTauxActivite());
        travailleurSimpleModel.setTelephone(travailleur.getTelephone());

        // travailleurSimpleModel.setTraite(travailleur.isTraite());
        travailleurSimpleModel.setStatus(travailleur.getStatus().getValue());
        travailleurSimpleModel.setIdEmployeur(travailleur.getIdEmployeur());
        if (travailleur.getSalaire() != null) {
            travailleurSimpleModel.setSalaire(travailleur.getSalaire().toString());
        }
        if (travailleur.getTypeSalaire() != null) {
            travailleurSimpleModel.setTypeSalaire(travailleur.getTypeSalaire().getValue());
        }

        travailleurSimpleModel.setNomUpper(travailleur.getNom().toUpperCase());
        travailleurSimpleModel.setPrenomUpper(travailleur.getPrenom().toUpperCase());

        travailleurSimpleModel.setIdDecompte(travailleur.getIdDecompte());

        travailleurSimpleModel.setModification(travailleur.isModification());

        if (travailleur.getTiersStatus() != null) {
            travailleurSimpleModel.setTiersStatus(travailleur.getTiersStatus().getValue());
        }
        if (travailleur.getTravailleurStatus() != null) {
            travailleurSimpleModel.setTravailleurStatus(travailleur.getTravailleurStatus().getValue());
        }
        if (travailleur.getPosteStatus() != null) {
            travailleurSimpleModel.setPosteStatus(travailleur.getPosteStatus().getValue());
        }
        if (travailleur.getBanqueStatus() != null) {
            travailleurSimpleModel.setBanqueStatus(travailleur.getBanqueStatus().getValue());
        }

        if (travailleur.getIdPosteTravail() != null) {
            travailleurSimpleModel.setIdPosteTravail(travailleur.getIdPosteTravail());
        }

        travailleurSimpleModel.setStatus(travailleur.getStatus().getValue());

        // travailleurSimpleModel.setIsNewAnnonce(travailleur.getIsNewAnnonce());

        return travailleurSimpleModel;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new TravailleurEbuSearchSimpleModel();
    }

    @Override
    public TravailleurEbuDomain convertToDomain(JadeComplexModel model) {
        return null;
    }
}
