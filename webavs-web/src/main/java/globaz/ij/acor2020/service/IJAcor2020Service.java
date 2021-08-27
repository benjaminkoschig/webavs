package globaz.ij.acor2020.service;

import acor.rentes.xsd.in.ij.BasesCalculCouranteIJ;
import acor.rentes.xsd.in.ij.BasesCalculIJ;
import acor.rentes.xsd.in.ij.BeneficiaireIJ;
import acor.rentes.xsd.in.ij.IndemniteJournaliereIJ;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.TypeDemandeEnum;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJGrandeIJ;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.acor2020.mapper.PRAcorAssureTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorDemandeTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorMapper;
import globaz.prestation.acor.acor2020.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static globaz.cygnus.mappingXmlml.RFXmlmlMappingStatistiquesMontantsSASH.getSession;

@Slf4j
public class IJAcor2020Service {

    public InHostType createInHost(String idPrononce) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();

            IJPrononce prononce = IJPrononce.loadPrononce(session, null, idPrononce, null);

            PRDemande demande = prononce.loadDemande(null);
            PRTiersWrapper tiers = demande.loadTiers();
            PRAcorMapper prAcorMapper = new PRAcorMapper(false, tiers, session);


            List<ISFMembreFamilleRequerant> membres = this.loadMembres(tiers.getIdTiers(), session);
            ISFMembreFamilleRequerant requerant = filtreRequerant(tiers.getIdTiers(), membres);

            List<ISFMembreFamilleRequerant> famille = filtreFamille(tiers.getIdTiers(), membres);

            PRAcorAssureTypeMapper assureTypeAcorMapper = new PRAcorAssureTypeMapper(prAcorMapper, Arrays.asList(requerant));

//            IJACORPrononceAdapter t = null;
//            PRACORAdapter adapter = IJAdapterFactory.getInstance(session).createAdapter(session, prononce);

            IJIJCalculee ijijCalculee = null;

            InHostType inHost = new InHostType();
            inHost.getAssure()
                  .addAll(assureTypeAcorMapper.map((membreFamilleRequerant, assureType) -> {
                      IndemniteJournaliereIJ indemniteJournaliereIJ = new IndemniteJournaliereIJ();
                      BeneficiaireIJ beneficiaireIJ = new BeneficiaireIJ();
                      BasesCalculCouranteIJ basesCalculCouranteIJ = new BasesCalculCouranteIJ();

                      //TODO DMA 27.08.2021:  à mapper ou pas ????
                      //basesCalculCouranteIJ.setTauxLibre();
                      basesCalculCouranteIJ.setGenreIndemnite(prononce.isGrandeIJ() ? 1 : 2);
                      basesCalculCouranteIJ.setOfficeAI(prononce.getOfficeAI());

                      basesCalculCouranteIJ.setDatePrononce(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));
                      basesCalculCouranteIJ.setNumeroDecision(Long.parseLong(prononce.getNoDecisionAI()));
                      basesCalculCouranteIJ.setEtablissement("Agent d'exécution");
                      basesCalculCouranteIJ.setStatutij(1);
                      //TODO DMA 27.08.2021:   chois fait par défaut à null ????
                      basesCalculCouranteIJ.setIdIJModifiee(null);
                      basesCalculCouranteIJ.setGenreReadaptation(loadCode(session, prononce.getCsGenre()));
                      basesCalculCouranteIJ.setDebutBases(Dates.toXMLGregorianCalendar(prononce.getDateDebutPrononce()));
                      basesCalculCouranteIJ.setFinBases(Dates.toXMLGregorianCalendar(prononce.getDateFinPrononce()));
                      //TODO DMA 27.08.2021:   à mapper ou pas ????
                      //basesCalculCouranteIJ.setRevenuMensuelDurantRea();

                      if(prononce.isPetiteIJ()) {
                          IJPetiteIJ ijPetiteIJ = loadPetiteIJ(prononce.getIdPrononce());
                          basesCalculCouranteIJ.setFormation(Integer.parseInt(session.getCode(ijPetiteIJ.getCsSituationAssure())));
                      }
                      //TODO DMA 27.08.2021: à mapper ou pas ????
                      //basesCalculCouranteIJ.setFormationFPI()
                      basesCalculCouranteIJ.setStatut(loadCode(session, prononce.getCsStatutProfessionnel()));
                      basesCalculCouranteIJ.setDemiIJAC(Double.parseDouble(prononce.getDemiIJAC()));
                      //TODO DMA 27.08.2021:  définir la baseGarantie ????
                      BasesCalculIJ.GarantieAA garantieAA = new BasesCalculIJ.GarantieAA();
                      basesCalculCouranteIJ.setGarantieAA(garantieAA);
                      basesCalculCouranteIJ.setCantonImpots(loadCode(session, prononce.getCsCantonImpositionSource()));
                      //TODO DMA 27.08.2021: chois fait par défaut à false ????
                      basesCalculCouranteIJ.setMesureRea8A(false);
                      //TODO DMA 27.08.2021: chois fait par défaut à false ????
                      basesCalculCouranteIJ.setNouvelleMesure(false);
                      indemniteJournaliereIJ.getBasesCalcul().add(basesCalculCouranteIJ);
                      assureType.setIndemnitesJournalieres(indemniteJournaliereIJ);
                      return assureType;
                  }));
            DemandeType demandeType = createDemande(tiers, prononce, session);
            inHost.setDemande(demandeType);

            //inHost.getEnfant()
            //inHost.getFamille().addAll(famille)

            inHost.setVersionSchema("5.0");

            return inHost;
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }

    private Integer loadCode(final BSession session, final String csGenre) {
        if(JadeStringUtil.isBlankOrZero(csGenre)) {
            return null;
        }
        return Integer.parseInt(session.getCode(csGenre));
    }

    private List<ISFMembreFamilleRequerant> filtreFamille(final String idTiers, final List<ISFMembreFamilleRequerant> membres) {
        return membres.stream()
                      .filter(membre -> !membre.getIdTiers().equals(idTiers))
                      .collect(Collectors.toList());

    }

    private ISFMembreFamilleRequerant filtreRequerant(final String idTiers, final List<ISFMembreFamilleRequerant> membres) {
        return membres.stream()
                      .filter(membre -> membre.getIdTiers().equals(idTiers))
                      .findFirst()
                      .orElseThrow(() -> new CommonTechnicalException("Membre famille requerant not found with this id " + idTiers));
    }


    private DemandeType createDemande(PRTiersWrapper tiersRequerant, IJPrononce prononce, BSession session) {
        PRAcorDemandeTypeMapper demandeTypeAcorMapper = new PRAcorDemandeTypeMapper(session);
        DemandeType demandeType = demandeTypeAcorMapper.map();

        //) IIJPrononce.CS_ALLOC_ASSIST =      prononce.getCsTypeIJ();

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(PRConverterUtils.formatNssToLong(tiersRequerant.getNSS()));
        demandeType.setTypeDemande(TypeDemandeEnum.IJ);
        demandeType.setDateTraitement(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));
        demandeType.setDateDepot(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));

        //demandeType.setTypeCalcul(getTypeCalcul(demandeRente.getCsTypeCalcul()));

        return demandeType;
    }

    private List<ISFMembreFamilleRequerant> loadMembres(String idTiers, BSession session) throws PRACORException {
        try {
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE, idTiers);
            ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiers, JACalendar.todayJJsMMsAAAA());

            return Arrays.asList(membresFamille);
        } catch (Exception e) {
            throw new PRACORException("impossible de charger la situation familiale avec cette idTiers: " + idTiers, e);
        }
    }

    private IJPetiteIJ loadPetiteIJ(String idPronnonce) {
        IJPetiteIJ ijPetiteIJ = new IJPetiteIJ();
        ijPetiteIJ.setSession(getSession());
        ijPetiteIJ.setIdPrononce(idPronnonce);
        try {
            ijPetiteIJ.retrieve();
            return ijPetiteIJ;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the ijPetiteIJ with this id:" + idPronnonce, e);
        }
    }

    private IJGrandeIJ loadGrandeIJ(String idPronnonce) {
        IJGrandeIJ grandeIJ = new IJGrandeIJ();
        grandeIJ.setSession(getSession());
        grandeIJ.setIdPrononce(idPronnonce);
        try {
            grandeIJ.retrieve();
            return grandeIJ;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the grandeIJ with this id:" + idPronnonce, e);
        }
    }
}
