package globaz.apg.acorweb.service;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.*;
import ch.admin.zas.xmlns.in_apg._0.AllocationPerteGainAPG;
import ch.admin.zas.xmlns.in_apg._0.BasesCalculAMat;
import ch.admin.zas.xmlns.in_apg._0.BasesCalculAPG;
import ch.admin.zas.xmlns.in_apg._0.GarantieIJ;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Dates;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acorweb.mapper.*;
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
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
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

    private PRTiersWrapper tiersRequerant;

    public APExportationCalculAcor(String idDroit, String genreService) {
        this.session = BSessionUtil.getSessionFromThreadContext();;
        this.idDroit = idDroit;
        this.genreService = genreService;
        this.transaction = session.getCurrentThreadTransaction();
    }

    public InHostType createInHost() {
        LOG.info("Création du inHost.");
        InHostType inHost = new InHostType();

        try {
            droit = loadDroit();
            PRDemande demande = ApgServiceLocator.getEntityService().getDemandeDuDroit(session, transaction,
                    droit.getIdDroit());
            tiersRequerant = demande.loadTiers();

            PRAcorMapper prAcorMapper = new PRAcorMapper(IPTConstantesExternes.TIERS_ADRESSE_TYPE_DOMICILE, tiersRequerant, APGUtils.getCSDomaineFromTypeDemande(droit.getGenreService()), session);
            AssureType assureType = new APAssureMapper(prAcorMapper, droit, session).createAssureType();
            inHost.getAssure().add(completeMappingAssure(inHost, assureType));
            inHost.setDemande(toDemande());
            inHost.setVersionSchema("6.0");
        } catch (Exception e) {
            LOG.error("Erreur lors de la construction du inHost.", e);
        }
        //        inHost = new AcorService().createInHostCalcul("2406"); // TODO WS ACOR APG /!\ UTILE POUR AVOIR UN INHOSTTYPE DE TEST SUR CICICAM /!\

        return inHost;
    }

    private AssureType completeMappingAssure(final InHostType inHost,
                                             final AssureType assureType) {

            AllocationPerteGainAPG allocationPerteGainAPG = new AllocationPerteGainAPG();
            List<APSituationProfessionnelle> situationsProfessionnelles = APLoader.loadSituationsProfessionnelles(idDroit, session);
            if (droit instanceof APDroitAPG) {
                allocationPerteGainAPG.setBasesCalculAPG(new APBaseCalculAPGMapper((APDroitAPG) droit, situationsProfessionnelles).map(session));
            } else if(droit instanceof APDroitMaternite) {
                allocationPerteGainAPG.setBasesCalculAMat(new APBaseCalculAmatMapper((APDroitMaternite) droit).map(session));
                inHost.getEnfant().addAll(new APEnfantMapper(tiersRequerant, APLoader.loadSituationFamillialeMat(idDroit, session)).map());
                inHost.getAssure().add(createPereInconnu());
            }
            allocationPerteGainAPG.getRevenu().addAll(new APRevenuMapper(situationsProfessionnelles, droit).map(session));
            assureType.setAllocationPerteGain(allocationPerteGainAPG);
        return assureType;
    }

    private AssureType createPereInconnu(){
        AssureType assure = new AssureType();
        assure.setNavs(PRConverterUtils.formatNssToLong(PRNSS13ChiffresUtils.getNSSErrone(0)));
        assure.setSexe(SexType.MALE);
        return assure;
    }

    private DemandeType toDemande() {
        PRAcorDemandeTypeMapper demandeTypeAcorMapper = new PRAcorDemandeTypeMapper(session, tiersRequerant);
        DemandeType demandeType = demandeTypeAcorMapper.map();

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(PRConverterUtils.formatNssToLong(tiersRequerant.getNSS()));
        demandeType.setTypeDemande(TypeDemandeEnum.A);
        demandeType.setDateTraitement(Dates.toXMLGregorianCalendar(LocalDate.now()));
        demandeType.setDateDepot(Dates.toXMLGregorianCalendar(droit.getDateDebutDroit()));
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

}
