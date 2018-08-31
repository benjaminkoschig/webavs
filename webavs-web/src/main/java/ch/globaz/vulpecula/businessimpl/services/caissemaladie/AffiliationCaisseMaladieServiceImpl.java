package ch.globaz.vulpecula.businessimpl.services.caissemaladie;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.business.exception.SaisiePeriodeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.caissemaladie.AffiliationCaisseMaladieService;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.process.caissemaladie.PosteTravailCaisseMaladieNonAnnonceeDTO;

public class AffiliationCaisseMaladieServiceImpl implements AffiliationCaisseMaladieService {

    private AffiliationCaisseMaladieRepository repository;

    public AffiliationCaisseMaladieServiceImpl(AffiliationCaisseMaladieRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException {
        List<AffiliationCaisseMaladie> listCaisseMaladie = VulpeculaRepositoryLocator
                .getAffiliationCaisseMaladieRepository().findByIdTravailleur(
                        affiliationCaisseMaladie.getIdTravailleur());
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdPosteTravailWithDependencies(affiliationCaisseMaladie.getIdPosteTravail());
        affiliationCaisseMaladie.setPosteTravail(poste);
        affiliationCaisseMaladie.validate(listCaisseMaladie);
        repository.create(affiliationCaisseMaladie);
        for (SuiviCaisseMaladie suiviCaisseMaladie : affiliationCaisseMaladie.getSuiviDocument()) {
            VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().create(suiviCaisseMaladie);
        }
    }

    private List<SuiviCaisseMaladie> retrieveListeSuiviDocumentExistant(
            AffiliationCaisseMaladie affiliationCaisseMaladie) {
        return VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().findByIdTravailleurAndCaisseMaladie(
                affiliationCaisseMaladie.getIdTravailleur(), affiliationCaisseMaladie.getIdCaisseMaladie());
    }

    @Override
    public void update(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException {
        List<AffiliationCaisseMaladie> listCaisseMaladie = VulpeculaRepositoryLocator
                .getAffiliationCaisseMaladieRepository().findByIdTravailleur(
                        affiliationCaisseMaladie.getIdTravailleur());
        PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdPosteTravailWithDependencies(affiliationCaisseMaladie.getIdPosteTravail());
        affiliationCaisseMaladie.setPosteTravail(poste);
        affiliationCaisseMaladie.validate(listCaisseMaladie);
        if (affiliationCaisseMaladie.isModifiable()) {
            repository.update(affiliationCaisseMaladie);
            gestionSuivi(affiliationCaisseMaladie);
        }
    }

    private void gestionSuivi(AffiliationCaisseMaladie affiliationCaisseMaladie) {
        List<SuiviCaisseMaladie> suiviDocumentExistant = retrieveListeSuiviDocumentExistant(affiliationCaisseMaladie);
        for (SuiviCaisseMaladie suiviExistant : suiviDocumentExistant) {
            VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().deleteById(suiviExistant.getId());
        }
        for (SuiviCaisseMaladie suiviCaisseMaladie : affiliationCaisseMaladie.getSuiviDocument()) {
            VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().create(suiviCaisseMaladie);
        }
    }

    @Override
    public void delete(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException {
        if (affiliationCaisseMaladie.isSupprimable()) {
            repository.delete(affiliationCaisseMaladie);
            // Si il n'existe plus d'affiliationCaisseMaladie pour la même caisse et le même travailleur on supprimme le
            // suivi des documents.
            List<AffiliationCaisseMaladie> listeAffiliation = VulpeculaRepositoryLocator
                    .getAffiliationCaisseMaladieRepository().findByIdTravailleur(
                            affiliationCaisseMaladie.getIdTravailleur());
            boolean existeEncoreLaMemeCaisseMaladie = false;
            for (AffiliationCaisseMaladie affiliation : listeAffiliation) {
                if (affiliation.getIdCaisseMaladie().equals(affiliationCaisseMaladie.getIdCaisseMaladie())) {
                    existeEncoreLaMemeCaisseMaladie = true;
                }
            }
            if (!existeEncoreLaMemeCaisseMaladie) {
                List<SuiviCaisseMaladie> listeSuivi = VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository()
                        .findByIdTravailleurAndCaisseMaladie(affiliationCaisseMaladie.getIdTravailleur(),
                                affiliationCaisseMaladie.getIdCaisseMaladie());
                for (SuiviCaisseMaladie suiviCaisseMaladie : listeSuivi) {
                    VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().delete(suiviCaisseMaladie);
                }
            }
        }
    }

    @Override
    public void createForPosteTravail(PosteTravail poste) {
        if (poste.getIdTiersCM() != null && !poste.getIdTiersCM().equals("-1")) {
            AffiliationCaisseMaladie caisseMaladie = new AffiliationCaisseMaladie();
            caisseMaladie.setMoisDebut(poste.getDebutActivite());
            caisseMaladie.setMoisFin(poste.getFinActivite());
            caisseMaladie.setTravailleur(poste.getTravailleur());
            caisseMaladie.setIdPosteTravail(poste.getId());
            // Si aucune caisse idTiersCM = -1, aucune caisse n'a été sélectionné, donc pas d'entrée
            Administration adminCaisse = VulpeculaRepositoryLocator.getAdministrationRepository().findById(
                    poste.getIdTiersCM());
            caisseMaladie.setCaisseMaladie(adminCaisse);
            VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository().create(caisseMaladie);
        }
    }

    @Override
    public void deleteForPosteTravail(PosteTravail poste) {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(poste.getId());

        List<AffiliationCaisseMaladie> affiliations = VulpeculaRepositoryLocator
                .getAffiliationCaisseMaladieRepository().findByIdPosteTravail(posteTravail.getId());
        for (AffiliationCaisseMaladie affiliation : affiliations) {
            VulpeculaRepositoryLocator.getAffiliationCaisseMaladieRepository().delete(affiliation);
        }
    }

    @Override
    public String translateEbuCodeToWMCode(int codeSysteme) {
        HashMap<Integer, String> mapIdEbuIdTiersCM = new HashMap<Integer, String>();
        mapIdEbuIdTiersCM.put(1, "1000000");
        mapIdEbuIdTiersCM.put(2, "1000001");
        mapIdEbuIdTiersCM.put(3, "1000002");
        mapIdEbuIdTiersCM.put(4, "1000043");
        return mapIdEbuIdTiersCM.get(codeSysteme);
    }

    private List<PosteTravailCaisseMaladieNonAnnonceeDTO> getListePosteTravailCaisseMaladieNonAnnonceeDTO(
            String dateAnnonceFrom, String dateAnnonceTo) {
        StringBuilder query = new StringBuilder();
        query.append(
                "SELECT tra.ID AS ID_TRAVAILLEUR, avs.HXNAVS AS NSS, tie.HTLDE1 AS NOM, tie.HTLDE2 AS PRENOM, po.DATE_DEBUT_ACTIVITE as DATE_DEBUT, po.DATE_FIN_ACTIVITE as DATE_FIN ")
                .append("FROM SCHEMA.PT_POSTES_TRAVAILS po ")
                .append("INNER JOIN SCHEMA.PT_TRAVAILLEURS tra on tra.ID=po.ID_PT_TRAVAILLEURS ")
                .append("INNER JOIN SCHEMA.TITIERP tie on tie.HTITIE=tra.ID_TITIERP ")
                .append("INNER JOIN SCHEMA.TIPAVSP avs on tie.HTITIE=avs.HTITIE ")
                .append("LEFT OUTER JOIN SCHEMA.PT_CAISSES_MALADIES cm on po.ID=cm.ID_PT_POSTES_TRAVAILS ")
                .append("where cm.ID IS NULL ").append("AND po.DATE_DEBUT_ACTIVITE>=").append(dateAnnonceFrom);
        if (!"0".equals(dateAnnonceTo)) {
            query.append(" AND po.DATE_FIN_ACTIVITE <= ").append(dateAnnonceTo);
        }

        return SCM.newInstance(PosteTravailCaisseMaladieNonAnnonceeDTO.class).query(query.toString()).execute();
    }

    private Map<String, String> getIdPosteTravailAMCABMap(String dateAnnonceFrom, String dateAnnonceTo) {

        StringBuilder queryIsAMCAB = new StringBuilder();
        queryIsAMCAB
                .append("SELECT tra.ID AS ID_TRAVAILLEUR ")
                .append("FROM SCHEMA.PT_POSTES_TRAVAILS poste ")
                .append("inner join SCHEMA.PT_TRAVAILLEURS tra on tra.id=poste.ID_PT_TRAVAILLEURS ")
                .append("inner join SCHEMA.PT_ADHESIONS_COTIS_POSTES adhcoti on adhcoti.ID_PT_POSTES_TRAVAILS=poste.id ")
                .append("inner join SCHEMA.afcotip coti on adhcoti.ID_AFCOTIP=coti.MEICOT ")
                .append("inner join SCHEMA.afassup assu on coti.MBIASS=assu.mbiass ")
                .append("where assu.MBTTYP=68904007 and adhcoti.DATE_DEBUT >= ").append(dateAnnonceFrom);
        if (!"0".equals(dateAnnonceTo)) {
            queryIsAMCAB.append(" and adhcoti.DATE_FIN <= ").append(dateAnnonceTo);
        }

        List<PosteTravailCaisseMaladieNonAnnonceeDTO> postesNonAnnoncesAMCAB = SCM
                .newInstance(PosteTravailCaisseMaladieNonAnnonceeDTO.class).query(queryIsAMCAB.toString()).execute();

        Map<String, String> mapIDAMCAB = new HashMap<String, String>();
        for (PosteTravailCaisseMaladieNonAnnonceeDTO poste : postesNonAnnoncesAMCAB) {
            mapIDAMCAB.put(poste.getIdTravailleur(), poste.getIdTravailleur());
        }
        return mapIDAMCAB;
    }

    @Override
    public Map<Administration, Collection<AffiliationCaisseMaladie>> prepareMapCasNonAnnonces(String dateAnnonceFrom,
            String dateAnnonceTo) {
        List<AffiliationCaisseMaladie> affiliationsCaissesMaladies = new ArrayList<AffiliationCaisseMaladie>();
        // Récupérer la liste des postes non annoncés
        List<PosteTravailCaisseMaladieNonAnnonceeDTO> postesNonAnnonces = getListePosteTravailCaisseMaladieNonAnnonceeDTO(
                dateAnnonceFrom, dateAnnonceTo);
        // récupérer la liste des cas AMCAB
        Map<String, String> mapIDAMCAB = getIdPosteTravailAMCABMap(dateAnnonceFrom, dateAnnonceTo);
        for (PosteTravailCaisseMaladieNonAnnonceeDTO poste : postesNonAnnonces) {
            Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(
                    poste.getIdTravailleur());
            AffiliationCaisseMaladie affCM = new AffiliationCaisseMaladie();
            affCM.setTravailleur(travailleur);
            if (!JadeStringUtil.isBlankOrZero(poste.getDateDebut())) {
                affCM.setMoisDebut(new Date(poste.getDateDebut()));
            }
            if (!JadeStringUtil.isBlankOrZero(poste.getDateFin())) {
                affCM.setMoisFin(new Date(poste.getDateFin()));
            }
            if (mapIDAMCAB.containsKey(poste.getIdTravailleur())) {
                affCM.setHasCotisationAMCAB("X");
            }
            affiliationsCaissesMaladies.add(affCM);
        }
        Administration fakeAdmin = new Administration();
        fakeAdmin.setDesignation1("Non annoncées");
        fakeAdmin.setId("0");
        Map<Administration, Collection<AffiliationCaisseMaladie>> affiliationsGroupByCaisseMaladie = new HashMap<Administration, Collection<AffiliationCaisseMaladie>>();
        affiliationsGroupByCaisseMaladie.put(fakeAdmin, affiliationsCaissesMaladies);
        return affiliationsGroupByCaisseMaladie;
    }

    @Override
    public void checkPeriodValidty(String dateFrom, String dateTo, BSession session) throws SaisiePeriodeException {

        if (JadeStringUtil.isBlankOrZero(dateFrom)) {

            throw new SaisiePeriodeException(session.getLabel("LISTE_ANNONCE_ERREUR_PERIODE_SAISIE_SANS_DD"));
        }
        // Vérifier si la période donnée est une periode valable
        if (!JadeStringUtil.isBlankOrZero(dateTo) && !JadeDateUtil.isDateBefore(dateFrom, dateTo)) {

            throw new SaisiePeriodeException(session.getLabel("LISTE_ANNONCE_ERREUR_PERIODE_SAISIE"));
        }
    }

}
