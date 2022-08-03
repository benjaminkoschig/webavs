package globaz.apg.acorweb.service;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.*;
import ch.admin.zas.xmlns.in_apg._0.AllocationPerteGainAPG;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acorweb.mapper.*;
import globaz.apg.db.droits.*;
import globaz.apg.utils.APGUtils;
import globaz.externe.IPTConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.web.mapper.PRAcorDemandeTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorMapper;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class APExportationCalculAcor {

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
            AssureType assureType = new APAcorAssureTypeMapper(prAcorMapper, droit, session).createAssureType();
            inHost.getAssure().add(completeMappingAssure(inHost, assureType));
            inHost.setDemande(toDemande());
            inHost.setVersionSchema("7.0");
        } catch (Exception e) {
            LOG.error("Erreur lors de la construction du inHost.", e);
        }
        return inHost;
    }

    private AssureType completeMappingAssure(final InHostType inHost,
                                             final AssureType assureType) {

            AllocationPerteGainAPG allocationPerteGainAPG = new AllocationPerteGainAPG();
            List<APSituationProfessionnelle> situationsProfessionnelles = APLoader.loadSituationsProfessionnelles(idDroit, session);
            // TODO : splitter gestion APG et maternité ?
            if (droit instanceof APDroitAPG) {
                allocationPerteGainAPG.setBasesCalculAPG(new APAcorBaseCalculMapper((APDroitAPG) droit, situationsProfessionnelles).map(session));
            } else if(droit instanceof APDroitMaternite) {
                allocationPerteGainAPG.setBasesCalculAMat(new APAcorBaseCalculAmatMapper((APDroitMaternite) droit).map(session));
                inHost.getEnfant().addAll(new APAcorEnfantTypeMapper(tiersRequerant, APLoader.loadSituationFamillialeMat(idDroit, session)).map());
                inHost.getAssure().add(createPereInconnu());
            }
            allocationPerteGainAPG.getRevenu().addAll(new APAcorRevenuMapper(situationsProfessionnelles, droit).map(session));
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
