package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.db.deblocage.ReDetteEnCompta;
import globaz.corvus.db.deblocage.ReRetour;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.vb.process.REDebloquerMontantRAViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.queryexec.bridge.jade.SCM;

class DeblocageHelper {

    private BSession session;

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

    /**
     * Affiche la page de déblocage du montant de la rente accodrée
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionDebloquerMontantRA(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {
        this.session = session;

        REDebloquerMontantRAViewBean draViewBean = new REDebloquerMontantRAViewBean();

        // Ce cas arrive lors du retour depuis pyxis !!!
        if ((viewBean instanceof REDebloquerMontantRAViewBean)
                && ((REDebloquerMontantRAViewBean) viewBean).isRetourDepuisPyxis()) {

            draViewBean = (REDebloquerMontantRAViewBean) viewBean;
        } else {

            draViewBean.setISession(session);

            if (!(viewBean instanceof REDebloquerMontantRAViewBean)) {
                RERenteAccordeeJointDemandeRenteViewBean raViewBean = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;
                draViewBean.setIdRenteAccordee(raViewBean.getIdPrestationAccordee());
                draViewBean.setIdTiersBeneficiaire(raViewBean.getIdTiersBeneficiaire());
            }

            draViewBean.setTiersBeneficiaireInfo(generateDescriptionTiers(draViewBean.getIdTiersBeneficiaire()));

            REPrestationsAccordees pracc = loadPrestation(draViewBean.getIdRenteAccordee());

            draViewBean.setPeriode(pracc.getDateDebutDroit() + " - " + pracc.getDateFinDroit());
            draViewBean.setGenre(pracc.getCodePrestation());
            draViewBean.setMontantRente(pracc.getMontantPrestation());

            // recherche du total bloque
            if (!JadeStringUtil.isBlankOrZero(pracc.getIdEnteteBlocage())) {

                REEnteteBlocage entete = new REEnteteBlocage();
                entete.setIdEnteteBlocage(pracc.getIdEnteteBlocage());

                FWCurrency montantBlk = new FWCurrency(0);
                try {
                    entete.retrieve();
                    if (!entete.isNew()) {
                        montantBlk.add(entete.getMontantBloque());
                        montantBlk.sub(entete.getMontantDebloque());
                        draViewBean.setMontantADebloquer(montantBlk.toString());
                        draViewBean.setMontantDebloque(entete.getMontantDebloque());
                        draViewBean.setMontantBloque(entete.getMontantBloque());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // On charge l'adresse de pmt...
            REInformationsComptabilite ic = pracc.loadInformationsComptabilite();

            draViewBean.setIdTiersAdrPmt(ic.getIdTiersAdressePmt());
            draViewBean.setIdDomaineAdrPmt(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            draViewBean.setIdCompteAnnexe(ic.getIdCompteAnnexe());
        }

        Set<String> idTiers = findIdsTiersFamille(draViewBean.getIdTiersBeneficiaire());
        draViewBean.setRetours(findRetours(idTiers));
        List<ReDetteEnCompta> dettes = findListDetteEnCompta(idTiers);
        draViewBean.setIdTiersFamille(idTiers);
        draViewBean.setRetourDepuisPyxis(false);

        return draViewBean;
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

    private List<ReDetteEnCompta> findListDetteEnCompta(Set<String> idTiers) throws BlocageException {
        List<ReDetteEnCompta> list = new ArrayList<ReDetteEnCompta>();
        if (!idTiers.isEmpty()) {
            try {
                CASectionJoinCompteAnnexeJoinTiersManager mgr = new CASectionJoinCompteAnnexeJoinTiersManager();
                mgr.setForIdTiersIn(idTiers);
                mgr.setForSoldePositif(true);
                mgr.find(BManager.SIZE_NOLIMIT);

                for (Iterator<BEntity> it = mgr.iterator(); it.hasNext();) {
                    CASectionJoinCompteAnnexeJoinTiers e = (CASectionJoinCompteAnnexeJoinTiers) it.next();
                    ReDetteEnCompta dette = new ReDetteEnCompta();
                    dette.setIdRoleCA(e.getIdRole());
                    dette.setDescriptionCompteAnnexe(e.getDescriptionCompteAnnexe());
                    dette.setIdSectionDetteEnCompta(e.getIdSection());
                    dette.setMontant(new Montant(e.getSolde()));
                    dette.setDescription(CABusinessServiceLocator.getSectionService().findDescription(e.getIdSection()));
                    e.getDescriptionCompteAnnexe();
                    list.add(dette);
                }
            } catch (Exception e1) {
                throw new BlocageException("Unable to search the dette en compta ", e1);
            }
        }
        return list;
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
                            + "where RETOURS.ETATRETOUR <>  251003").toSql();// AND TIERS.HTITIE ").in(idTiers).toSql();
            List<ReRetour> list = SCM.newInstance(ReRetour.class).query(sql).execute();
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

}
