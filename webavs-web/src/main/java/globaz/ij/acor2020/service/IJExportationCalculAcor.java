package globaz.ij.acor2020.service;

import acor.rentes.xsd.in.ij.BasesCalculCouranteIJ;
import acor.rentes.xsd.in.ij.BasesCalculIJ;
import acor.rentes.xsd.in.ij.BasesCalculRevenusIJ;
import acor.rentes.xsd.in.ij.BeneficiaireIJ;
import acor.rentes.xsd.in.ij.IndemniteJournaliereIJ;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeIJType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.TypeDemandeEnum;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJGrandeIJ;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.module.IJSalaireFilter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.acor2020.mapper.PRAcorAssureTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorDemandeTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorEnfantTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorMapper;
import globaz.prestation.acor.acor2020.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static globaz.cygnus.mappingXmlml.RFXmlmlMappingStatistiquesMontantsSASH.getSession;

class IJExportationCalculAcor {

    InHostType createInHost(String idPrononce) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();

            IJPrononce prononce = IJPrononce.loadPrononce(session, null, idPrononce, null);

            PRDemande demande = prononce.loadDemande(null);
            PRTiersWrapper tiersRequerant = demande.loadTiers();
            PRAcorMapper prAcorMapper = new PRAcorMapper(false, tiersRequerant, session);

            ISFSituationFamiliale situationFamiliale = this.loadSituationFamiliale(tiersRequerant.getIdTiers(), session);
            List<ISFMembreFamilleRequerant> membres = this.loadMembres(tiersRequerant.getIdTiers(), situationFamiliale, session);

            InHostType inHost = new InHostType();
            List<ISFMembreFamilleRequerant> famille = filtreFamille(tiersRequerant.getIdTiers(), membres);
//            famille.forEach(membreFamile -> {
//                FamilleType familleType = new FamilleType();
//                familleType.getNavs().add(PRConverterUtils.formatNssToLong(membreFamile.getNss()));
//                familleType.setPensionAlimentaire(false);
//                inHost.getFamille().add(familleType);
//            });

            ISFMembreFamilleRequerant requerant = filtreRequerant(tiersRequerant.getIdTiers(), membres);


            PRAcorAssureTypeMapper assureTypeAcorMapper = new PRAcorAssureTypeMapper(prAcorMapper, Arrays.asList(requerant));

//            IJACORPrononceAdapter t = null;
//            PRACORAdapter adapter = IJAdapterFactory.getInstance(session).createAdapter(session, prononce);

            IJIJCalculee ijijCalculee = null;


            inHost.getAssure()
                  .addAll(assureTypeAcorMapper.map((membreFamilleRequerant, assureType) -> completeMappingAssure(session, prononce, assureType)));
            DemandeType demandeType = createDemande(tiersRequerant, prononce, session);
            inHost.setDemande(demandeType);

            List<ISFMembreFamilleRequerant> enfants = this.filtreEnfant(membres);
            PRAcorEnfantTypeMapper prAcorEnfantTypeMapper = new PRAcorEnfantTypeMapper(false, tiersRequerant, situationFamiliale, enfants, session);

            inHost.getEnfant().addAll(prAcorEnfantTypeMapper.map());
            inHost.setVersionSchema("5.0");

            return inHost;
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }

    private AssureType completeMappingAssure(final BSession session, final IJPrononce prononce, final AssureType assureType) {
        IndemniteJournaliereIJ indemniteJournaliereIJ = new IndemniteJournaliereIJ();

        BeneficiaireIJ beneficiaireIJ = new BeneficiaireIJ();
        BasesCalculCouranteIJ basesCalculCouranteIJ = mapToBaseCalculCourante(prononce, indemniteJournaliereIJ, session);
        indemniteJournaliereIJ.getBasesCalcul().add(basesCalculCouranteIJ);

        PeriodeIJType periodeIJType = new PeriodeIJType();
        periodeIJType.setDebut(Dates.toXMLGregorianCalendar(prononce.getDateDebutPrononce()));
        periodeIJType.setFin(Dates.toXMLGregorianCalendar(prononce.getDateFinPrononce()));

        assureType.setIndemnitesJournalieres(indemniteJournaliereIJ);
        return assureType;
    }

    private BasesCalculCouranteIJ mapToBaseCalculCourante(final IJPrononce prononce, final IndemniteJournaliereIJ indemniteJournaliereI, final BSession session) {
        BasesCalculCouranteIJ basesCalculCouranteIJ = new BasesCalculCouranteIJ();

        //if (prononce.isGrandeIJ()) {
        IJGrandeIJ ijGrandeIJ = this.loadGrandeIJ(prononce.getIdPrononce());
        List<IJSituationProfessionnelle> ijSituationProfessionnelles = this.loadSituationProfessionelle(prononce.getIdPrononce());
        for (IJSituationProfessionnelle situationProfessionnelle : ijSituationProfessionnelles) {
            basesCalculCouranteIJ.getRevenus().add(mapToBaseCalculeRevenus(situationProfessionnelle, session));
        }
        //}

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

        if (prononce.isPetiteIJ()) {
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
        return basesCalculCouranteIJ;
    }

    private BasesCalculRevenusIJ mapToBaseCalculeRevenus(final IJSituationProfessionnelle situationProfessionnelle, final BSession session) {
        try {
            IJSalaireFilter salaire = new IJSalaireFilter(getSession(), situationProfessionnelle);
            BasesCalculRevenusIJ basesCalculRevenus = new BasesCalculRevenusIJ();

            basesCalculRevenus.setRevenu(Double.parseDouble(salaire.getMontantSalaireArrondi()));
            basesCalculRevenus.setAnnee(Dates.toXMLGregorianCalendar("01.01." + situationProfessionnelle.getAnneeCorrespondante()));
            basesCalculRevenus.setType(Integer.parseInt(PRACORConst.csPeriodiciteSalaireIJToAcor(session, salaire.getCsPeriodiciteSalaire())));
            basesCalculRevenus.setHeuresHebdo((int) Double.parseDouble(salaire.getNombreHeuresSemaines()));
            IJEmployeur employeur = situationProfessionnelle.loadEmployeur();
            basesCalculRevenus.setNumeroAffilie(employeur.loadNumero());
            basesCalculRevenus.setNomEmployeur(employeur.loadNom());
            return basesCalculRevenus;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to map the BasesCalculRevenusIJ with this situationProfessionnelle:"
                                                       + situationProfessionnelle.getIdSituationProfessionnelle(), e);

        }
    }

    private int loadCode(final BSession session, final String csGenre) {
        if (JadeStringUtil.isBlankOrZero(csGenre)) {
            return 0;
        }
        return Integer.parseInt(session.getCode(csGenre));
    }

    private List<ISFMembreFamilleRequerant> filtreFamille(final String idTiers, final List<ISFMembreFamilleRequerant> membres) {
        return membres.stream()
                      .filter(membre -> !membre.getIdTiers().equals(idTiers))
                      .collect(Collectors.toList());

    }

    private List<ISFMembreFamilleRequerant> filtreEnfant(final List<ISFMembreFamilleRequerant> membres) {
        return membres.stream()
                      .filter(membre -> Objects.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_ENFANT))
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

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(PRConverterUtils.formatNssToLong(tiersRequerant.getNSS()));
        demandeType.setTypeDemande(TypeDemandeEnum.IJ);
        demandeType.setDateTraitement(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));
        // Avoir dans l'ancienne manièreil n'y pas de date de dépot.
        demandeType.setTypeCalcul(Integer.parseInt(PRACORConst.CA_TYPE_CALCUL_STANDARD));

        return demandeType;
    }

    private List<ISFMembreFamilleRequerant> loadMembres(String idTiers, ISFSituationFamiliale sf, BSession session) throws PRACORException {
        try {
            ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiers, JACalendar.todayJJsMMsAAAA());

            return Arrays.asList(membresFamille);
        } catch (Exception e) {
            throw new PRACORException("impossible de charger  les membres de familiale avec cette idTiers: " + idTiers, e);
        }
    }

    private ISFSituationFamiliale loadSituationFamiliale(final String idTiers, final BSession session) throws PRACORException {
        try {
            return SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE, idTiers);
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

    private List<IJSituationProfessionnelle> loadSituationProfessionelle(String idPronnonce) {
        IJSituationProfessionnelleManager mgr = new IJSituationProfessionnelleManager();
        mgr.setISession(getSession());
        mgr.setForIdPrononce(idPronnonce);
        try {
            mgr.find(BManager.SIZE_NOLIMIT);
            return mgr.getContainerAsList();
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the situationProfessionelle with this id:" + idPronnonce, e);
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
