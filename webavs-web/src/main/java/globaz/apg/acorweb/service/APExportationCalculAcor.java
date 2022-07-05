package globaz.apg.acorweb.service;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.TypeDemandeEnum;
import ch.admin.zas.xmlns.in_apg._0.AllocationPerteGainAPG;
import ch.admin.zas.xmlns.in_apg._0.BasesCalculAPG;
import ch.admin.zas.xmlns.in_apg._0.GarantieIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Dates;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acorweb.mapper.APAssureMapper;
import globaz.apg.acorweb.mapper.APPeriodeMapper;
import globaz.apg.acorweb.mapper.APRevenuMapper;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.*;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.utils.APGUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.externe.IPTConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.web.mapper.PRAcorAssureTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorDemandeTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorMapper;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class APExportationCalculAcor {

    private static final Logger LOG = LoggerFactory.getLogger(APExportationCalculAcor.class);

    private BSession session;
    private String idDroit;
    private String genreService = "";
    private BTransaction transaction;
    private APDroitLAPG droit;
    private EntityService entityService;

    public APExportationCalculAcor(String idDroit, String genreService) {
        this.session = BSessionUtil.getSessionFromThreadContext();;
        this.idDroit = idDroit;
        this.genreService = genreService;
        this.transaction = session.getCurrentThreadTransaction();
        this.entityService = EntityService.of(session);
    }

    public InHostType createInHost() {
        LOG.info("Création du inHost.");
        InHostType inHost = new InHostType();

        try {
            droit = loadDroit();
            PRDemande demande = ApgServiceLocator.getEntityService().getDemandeDuDroit(session, transaction,
                    droit.getIdDroit());
            PRTiersWrapper tiersRequerant = demande.loadTiers();

            PRAcorMapper prAcorMapper = new PRAcorMapper(IPTConstantesExternes.TIERS_ADRESSE_TYPE_DOMICILE, tiersRequerant, APGUtils.getCSDomaineFromTypeDemande(droit.getGenreService()), session);
            AssureType assureType = new APAssureMapper(prAcorMapper, session).createAssureType();
            inHost.getAssure().add(completeMappingAssure(droit, assureType, session));
            inHost.setDemande(toDemande(tiersRequerant, droit.getDateDebutDroit(), session));
            inHost.setVersionSchema("6.0");
        } catch (Exception e) {
            LOG.error("Erreur lors de la construction du inHost.", e);
        }
        //        inHost = new AcorService().createInHostCalcul("2406"); // TODO WS ACOR APG /!\ UTILE POUR AVOIR UN INHOSTTYPE DE TEST SUR CICICAM /!\

        return inHost;
    }

    private AssureType completeMappingAssure(final APDroitLAPG droit,
                                             final AssureType assureType,
                                             final BSession session) {

            AllocationPerteGainAPG allocationPerteGainAPG = new AllocationPerteGainAPG();
            if (droit instanceof APDroitAPG) {
                List<APSituationProfessionnelle> situationsProfessionnelles = loadSituationsProfessionnelles();
                allocationPerteGainAPG.setBasesCalculAPG(mapToBaseCalculAPG((APDroitAPG) droit, situationsProfessionnelles, session));
                allocationPerteGainAPG.getRevenu().addAll(new APRevenuMapper(situationsProfessionnelles, droit).map(session));
            }
            assureType.setAllocationPerteGain(allocationPerteGainAPG);
        return assureType;
    }

    private BasesCalculAPG mapToBaseCalculAPG(final APDroitAPG droit, List<APSituationProfessionnelle> situationsProfessionnelles, final BSession session) {
            BasesCalculAPG basesCalcul = new BasesCalculAPG();

        basesCalcul.setGenreCarte(1);
//        basesCalcul.setCantonImpot();
//        basesCalcul.setTauxImpot();
//        basesCalcul.setAFac();
//        basesCalcul.setExemptionCotisation();
        if(!JadeStringUtil.isBlankOrZero(droit.getDroitAcquis())) {
            GarantieIJ garantie = new GarantieIJ();
            garantie.setMontant(Double.valueOf(droit.getDroitAcquis()));
            garantie.setSource(Integer.valueOf(session.getCode(droit.getCsProvenanceDroitAcquis())));
//  TODO            garantie.setNumeroReference(); --> obligatoire !
            basesCalcul.setGarantieIJ(garantie);
        }
        try {
            basesCalcul.setLimiteTransfert(Double.valueOf(session.getApplication().getProperty(
                    APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE)));
        } catch (Exception e) {
            LOG.error("Impossible de récupérer la propriété "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE, e);
            throw new CommonTechnicalException("Impossible de récupérer la propriété "+APApplication.PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE , e);
        }

        basesCalcul.setGenreService(Integer.valueOf(session.getCode(droit.getGenreService())));

        if(!JadeStringUtil.isEmpty(droit.getNoCompte())) {
            basesCalcul.setNumeroReference(Integer.valueOf(droit.getNoCompte()));
        }
        if(!JadeStringUtil.isEmpty(droit.getNoControlePers())) {
            basesCalcul.setNumeroControle(Integer.valueOf(droit.getNoControlePers()));
        }

        try {
            APSituationFamilialeAPG situationFamilialeAPG = droit.loadSituationFamilliale();
            basesCalcul.setFraisGarde(Double.valueOf(situationFamilialeAPG.getFraisGarde()));
        } catch (Exception e) {
            LOG.error("Impossible de récupérer la situation familliale.", e);
            throw new CommonTechnicalException("Impossible de récupérer la situation familliale" , e);
        }

        ;

        List<APBaseCalcul> apBases = loadBasesCalcul();
        for(APBaseCalcul apBase : apBases) {
            basesCalcul.getPeriode().add(new APPeriodeMapper(apBase, situationsProfessionnelles).map());
        }

        return basesCalcul;
    }

    private DemandeType toDemande(PRTiersWrapper tiersRequerant, String date, BSession session) {
        PRAcorDemandeTypeMapper demandeTypeAcorMapper = new PRAcorDemandeTypeMapper(session, tiersRequerant);
        DemandeType demandeType = demandeTypeAcorMapper.map();

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(PRConverterUtils.formatNssToLong(tiersRequerant.getNSS()));
        demandeType.setTypeDemande(TypeDemandeEnum.A);
        demandeType.setDateTraitement(Dates.toXMLGregorianCalendar(LocalDate.now()));
        demandeType.setDateDepot(Dates.toXMLGregorianCalendar(date));
        demandeType.setTypeCalcul(Integer.parseInt(PRACORConst.CA_TYPE_CALCUL_STANDARD));

        return demandeType;
    }

    public APDroitLAPG loadDroit() {
        if ((droit == null) || !droit.getIdDroit().equals(idDroit)) {
            try {
                droit = APGUtils.loadDroit(session, idDroit, genreService);
            } catch (Exception e) {
                LOG.error("Impossible de récupérer le droit "+droit.getIdDroit(), e);
                throw new CommonTechnicalException("Impossible de récupérer le droit "+droit.getIdDroit(), e);
            }
        }
        return droit;
    }

    public List<APBaseCalcul> loadBasesCalcul() {
        try {
            return APBasesCalculBuilder.of(session, droit).createBasesCalcul();
        } catch (Exception e) {
            LOG.error("Impossible de récupérer les bases de Calcul.", e);
            throw new CommonTechnicalException("Impossible de récupérer les bases de Calcul.", e);
        }
    }

    public List<APSituationProfessionnelle> loadSituationsProfessionnelles()  {
        APSituationProfessionnelleManager mgr = new APSituationProfessionnelleManager();
        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());

        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            LOG.error(session.getLabel("ERREUR_CHARGEMENT_SITUATION_PROFESSIONNELLE"), e);
            throw new CommonTechnicalException(session.getLabel("ERREUR_CHARGEMENT_SITUATION_PROFESSIONNELLE"), e);
        }
        return mgr.getContainer();
    }


}
