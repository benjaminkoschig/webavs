package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.lignedeblocage.ReLigneDeclocageServices;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BSession;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SoldeCompteCourant;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.queryexec.bridge.jade.SCM;

public class REDeblocageService {
    private BSession session;

    public REDeblocageService(BSession session) {
        this.session = session;
    }

    public REDeblocage read(String idPrestationAccordee) throws Exception {
        REPrestationsAccordees pracc = loadPrestation(idPrestationAccordee);

        REEnteteBlocage enteteBlocage = readEnteteBlocage(pracc.getIdEnteteBlocage());

        PRTiersWrapper beneficiaires = PRTiersHelper.getTiersParId(session, pracc.getIdTiersBeneficiaire());
        Set<String> idTiers = findIdsTiersFamille(pracc.getIdTiersBeneficiaire());
        String tiersDescription = generateDescriptionTiers(pracc.getIdTiersBeneficiaire());

        ReLigneDeclocageServices service = new ReLigneDeclocageServices(session);
        RELigneDeblocages lignesDeblocages = service.searchByIdRenteAndCompleteInfo(
                Integer.valueOf(idPrestationAccordee), idTiers, tiersDescription);
        List<ReRetour> retours = findRetours(idTiers);

        List<SoldeCompteCourant> compteCourants = findSoldeCompteCourant(pracc.getIdTiersBeneficiaire(),
                beneficiaires.getNSS(), pracc.getDateDebutDroit());

        return new REDeblocage(lignesDeblocages, beneficiaires, enteteBlocage, pracc, tiersDescription, retours,
                compteCourants);
    }

    private REEnteteBlocage readEnteteBlocage(String idEnteteBlocage) {
        if (!JadeStringUtil.isBlankOrZero(idEnteteBlocage)) {
            REEnteteBlocage entete = new REEnteteBlocage();
            entete.setIdEnteteBlocage(idEnteteBlocage);
            entete.setSession(session);
            try {
                entete.retrieve();
                if (!entete.isNew()) {
                    return entete;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Any one enteteBlocage found with this id: " + idEnteteBlocage);
    }

    private String generateDescriptionTiers(String idTiers) {
        PRTiersWrapper beneficiaire;
        try {
            beneficiaire = PRTiersHelper.getTiersParId(session, idTiers);
            return PRNSSUtil.formatDetailRequerantDetail(
                    beneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    beneficiaire.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + beneficiaire.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    beneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    session.getCodeLibelle(beneficiaire.getProperty(PRTiersWrapper.PROPERTY_SEXE)),
                    getLibellePays(beneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), session));
        } catch (Exception e) {
            throw new RuntimeException("Unable to load the tiers with this id:" + idTiers, e);
        }

    }

    private List<ReRetour> findRetours(Set<String> idTiers) {
        if (!idTiers.isEmpty()) {
            String sql = SQLWriter
                    .write()
                    .append("select TIERS.HTITIE as ID_TIERS, RETOURS.IDRETOUR as ID_RETOUR, "
                            + "RETOURS.LIBELLERETOUR as LIBELLE, RETOURS.MONTANTRETOUR as MONTANT, RETOURS.DATERETOUR "
                            + "from schema.CARETOP as RETOURS "
                            + "inner join schema.CACPTAP COMPTE_ANNEXE on COMPTE_ANNEXE.IDCOMPTEANNEXE = RETOURS.IDCOMPTEANNEXE "
                            + "inner join schema.TITIERP TIERS on TIERS.HTITIE = COMPTE_ANNEXE.IDTIERS "
                            + "inner join schema.TIPAVSP NSS on NSS.HTITIE = TIERS.HTITIE  "
                            + "where RETOURS.ETATRETOUR <>  251003  AND TIERS.HTITIE ").in(idTiers).toSql();
            List<ReRetour> list = SCM.newInstance(ReRetour.class).query(sql).session(session).execute();
            for (ReRetour reRetour : list) {
                reRetour.setDescriptionTiers(generateDescriptionTiers(reRetour.getIdTiers()));
            }
            return list;
        }
        return new ArrayList<ReRetour>();
    }

    private REPrestationsAccordees loadPrestation(String idRenteAccordee) throws Exception {
        REPrestationsAccordees pracc = new REPrestationsAccordees();
        pracc.setSession(session);
        pracc.setIdPrestationAccordee(idRenteAccordee);
        pracc.retrieve();
        return pracc;
    }

    private Set<String> findIdsTiersFamille(final String idTiersBeneficiaire) throws Exception {

        // récupération des ID Tiers des membres de la famille (étendue)
        Set<String> idMembreFamille = new HashSet<String>();

        Set<PRTiersWrapper> famille = SFFamilleUtils.getTiersFamilleProche(session, idTiersBeneficiaire);

        for (PRTiersWrapper unMembre : famille) {
            if (!JadeStringUtil.isBlank(unMembre.getIdTiers())) {
                idMembreFamille.add(unMembre.getIdTiers());
            }
        }
        return idMembreFamille;
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    private String getLibellePays(final String idPays, final BSession session) {

        if ("999".equals(session.getCode(session.getSystemCode("CIPAYORI", idPays)))) {
            return "";
        } else {
            return session.getCodeLibelle(session.getSystemCode("CIPAYORI", idPays));
        }
    }

    private List<SoldeCompteCourant> findSoldeCompteCourant(String idTier, String nss, String dateDebut) {

        CompteAnnexeSimpleModel compteAnnexe;
        try {
            compteAnnexe = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null, idTier,
                    IntRole.ROLE_RENTIER, nss, false);
            APIRubrique rubrique = findRubrique();

            String dateValeurSection = dateDebut.substring(3);

            List<SoldeCompteCourant> list = CABusinessServiceLocator.getCompteCourantService()
                    .searchSoldeCompteCourant(dateValeurSection, compteAnnexe.getIdCompteAnnexe(),
                            rubrique.getIdRubrique());
            return list;
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        } catch (JadeApplicationException e) {
            throw new RuntimeException(e);
        }

    }

    private APIRubrique findRubrique() {
        APIReferenceRubrique referenceRubrique;
        try {
            referenceRubrique = (APIReferenceRubrique) PRSession.connectSession(session, "OSIRIS").getAPIFor(
                    APIReferenceRubrique.class);
            return referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.PC_COMPTE_COURANT_BLOCAGE);

        } catch (Exception e) {
            throw new RuntimeException("Technical exception, error to retrieve the reference rubrique", e);
        }
    }
}
