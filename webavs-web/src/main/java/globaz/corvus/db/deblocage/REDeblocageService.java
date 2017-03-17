package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.lignedeblocage.ReLigneDeclocageServices;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDues;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.queryexec.bridge.jade.SCM;

public class REDeblocageService {
    private BSession session;

    public REDeblocageService(BSession session) {
        this.session = session;
    }

    public REDeblocage read(String idPrestationAccordee) throws Exception {
        RERenteAccordeeJoinInfoComptaJoinPrstDues pracc = loadPrestation(idPrestationAccordee);

        REEnteteBlocage enteteBlocage = readEnteteBlocage(pracc.getIdEnteteBlocage());

        PRTiersWrapper beneficiaires = PRTiersHelper.getTiersParId(session, pracc.getIdTiersBeneficiaire());
        Set<String> idTiers = findIdsTiersFamille(pracc.getIdTiersBeneficiaire());
        String tiersDescription = generateDescriptionTiers(pracc.getIdTiersBeneficiaire());

        ReLigneDeclocageServices service = new ReLigneDeclocageServices(session);
        RELigneDeblocages lignesDeblocages = service.searchByIdRenteAndCompleteInfo(
                Integer.valueOf(idPrestationAccordee), idTiers, tiersDescription);

        Montant disponibleEnCompta = computeMontantDisponibleEnComptat(beneficiaires.getIdTiers());

        List<ReRetour> retours = findRetours(idTiers);
        return new REDeblocage(lignesDeblocages, beneficiaires, enteteBlocage, pracc, tiersDescription, retours,
                disponibleEnCompta);
    }

    private Montant computeMontantDisponibleEnComptat(String idTiers) {
        Montant montant = Montant.ZERO;
        if (!idTiers.isEmpty()) {
            try {
                CASectionJoinCompteAnnexeJoinTiersManager mgr = new CASectionJoinCompteAnnexeJoinTiersManager();
                mgr.setForIdTiersIn(idTiers);
                mgr.setSession(session);
                mgr.setForSoldeNegatif(true);
                mgr.find(BManager.SIZE_NOLIMIT);
                List<CASectionJoinCompteAnnexeJoinTiers> sections = mgr.toList();
                for (CASectionJoinCompteAnnexeJoinTiers section : sections) {
                    montant = montant.add(section.getSolde());
                }
                return montant;

            } catch (Exception e1) {
                throw new RuntimeException("Unable to search the dette en compta ", e1);
            }
        }
        return montant;
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

    private RERenteAccordeeJoinInfoComptaJoinPrstDues loadPrestation(String idRenteAccordee) throws Exception {
        RERenteAccordeeJoinInfoComptaJoinPrstDues pracc = new RERenteAccordeeJoinInfoComptaJoinPrstDues();
        // REPrestationsAccordees pracc = new REPrestationsAccordees();
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

}
