package globaz.ij.acor2020.service;

import acor.rentes.xsd.in.ij.BasesCalculCouranteIJ;
import acor.rentes.xsd.in.ij.BasesCalculRevenusIJ;
import acor.rentes.xsd.in.ij.BeneficiaireIJ;
import acor.rentes.xsd.in.ij.IndemniteJournaliereIJ;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.TypeDemandeEnum;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.util.Dates;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJGrandeIJ;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJRevenu;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.module.IJSalaireFilter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.acor2020.mapper.PRAcorAssureTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorDemandeTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorEnfantTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorFamilleTypeMapper;
import globaz.prestation.acor.acor2020.mapper.PRAcorMapper;
import globaz.prestation.acor.acor2020.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

import java.util.Arrays;
import java.util.List;

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
            List<ISFMembreFamilleRequerant> membres = this.loadMembres(tiersRequerant.getIdTiers(), situationFamiliale);

            PRMembresFamilleRequerant membresFamilleRequerant = new PRMembresFamilleRequerant(membres);
            List<ISFMembreFamilleRequerant> enfants = membresFamilleRequerant.filtreEnfants();
            List<ISFMembreFamilleRequerant> conjoints = membresFamilleRequerant.filtreConjoints();
            ISFMembreFamilleRequerant requerant = membresFamilleRequerant.filtreByIdTiers(tiersRequerant.getIdTiers());

            PRAcorAssureTypeMapper assureTypeAcorMapper = new PRAcorAssureTypeMapper(prAcorMapper, membresFamilleRequerant.filtreTousSaufEnfants());
            PRAcorFamilleTypeMapper familleTypeMapper = new PRAcorFamilleTypeMapper(requerant, situationFamiliale, prAcorMapper);
            PRAcorEnfantTypeMapper enfantTypeMapper = new PRAcorEnfantTypeMapper(false, tiersRequerant, situationFamiliale, enfants, session);

            InHostType inHost = new InHostType();
            inHost.getFamille().addAll(familleTypeMapper.map(conjoints));
            inHost.getEnfant().addAll(enfantTypeMapper.map());
            inHost.getAssure().addAll(toAssures(assureTypeAcorMapper, prononce, session));
            inHost.setDemande(toDemande(tiersRequerant, prononce, session));
            inHost.setVersionSchema("5.0");

            return inHost;
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }

    private List<AssureType> toAssures(final PRAcorAssureTypeMapper assureTypeAcorMapper, final IJPrononce prononce, final BSession session) {
        return assureTypeAcorMapper.map((membreFamilleRequerant, assureType) -> completeMappingAssure(session, prononce, assureType));
    }

    private AssureType completeMappingAssure(final BSession session, final IJPrononce prononce, final AssureType assureType) {
        BeneficiaireIJ beneficiaireIJ = new BeneficiaireIJ();
        //        PeriodeIJType periodeIJType = new PeriodeIJType();
//        periodeIJType.setDebut(Dates.toXMLGregorianCalendar(prononce.getDateDebutPrononce()));
//        periodeIJType.setFin(Dates.toXMLGregorianCalendar(prononce.getDateFinPrononce()));


        IndemniteJournaliereIJ indemniteJournaliereIJ = new IndemniteJournaliereIJ();
        indemniteJournaliereIJ.getBasesCalcul().add(mapToBaseCalculCourante(prononce, session));
        assureType.setIndemnitesJournalieres(indemniteJournaliereIJ);

        return assureType;
    }

    private BasesCalculCouranteIJ mapToBaseCalculCourante(final IJPrononce prononce, final BSession session) {
        BasesCalculCouranteIJ basesCalculCouranteIJ = new BasesCalculCouranteIJ();

        if (prononce.isGrandeIJ()) {
            IJGrandeIJ ijGrandeIJ = this.loadGrandeIJ(prononce.getIdPrononce());
            IJRevenu ijRevenu = loadRevenu(ijGrandeIJ);
            basesCalculCouranteIJ.setRevenuMensuelDurantRea(Double.parseDouble(ijRevenu.getRevenu()));
            List<IJSituationProfessionnelle> ijSituationProfessionnelles = this.loadSituationProfessionelle(prononce.getIdPrononce());
            for (IJSituationProfessionnelle situationProfessionnelle : ijSituationProfessionnelles) {
                basesCalculCouranteIJ.getRevenus().add(mapToBaseCalculeRevenus(situationProfessionnelle, session));
            }
        }

        //Pas utilis� donc non mapper.
        //basesCalculCouranteIJ.setTauxLibre();
        basesCalculCouranteIJ.setGenreIndemnite(prononce.isGrandeIJ() ? 1 : 2);
        basesCalculCouranteIJ.setOfficeAI(prononce.getOfficeAI());

        basesCalculCouranteIJ.setDatePrononce(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));
        basesCalculCouranteIJ.setNumeroDecision(Long.parseLong(prononce.getNoDecisionAI()));
        basesCalculCouranteIJ.setEtablissement("Agent d'ex�cution");
        basesCalculCouranteIJ.setStatutij(1);
        //On n'utilise pas cette information car on est toujours en statut 1
        basesCalculCouranteIJ.setIdIJModifiee(null);

        basesCalculCouranteIJ.setGenreReadaptation(loadCode(session, prononce.getCsGenre()));
        basesCalculCouranteIJ.setDebutBases(Dates.toXMLGregorianCalendar(prononce.getDateDebutPrononce()));
        basesCalculCouranteIJ.setFinBases(Dates.toXMLGregorianCalendar(prononce.getDateFinPrononce()));

        if (prononce.isPetiteIJ()) {
            IJPetiteIJ ijPetiteIJ = loadPetiteIJ(prononce.getIdPrononce());
            basesCalculCouranteIJ.setFormation(Integer.parseInt(session.getCode(ijPetiteIJ.getCsSituationAssure())));
            String revenu= loadRevenu(ijPetiteIJ);
            basesCalculCouranteIJ.setRevenuMensuelDurantRea(Double.parseDouble(revenu));
        }
        //TODO DMA 27.08.2021: � mapper
        //basesCalculCouranteIJ.setFormationFPI()
        basesCalculCouranteIJ.setStatut(loadCode(session, prononce.getCsStatutProfessionnel()));
        basesCalculCouranteIJ.setDemiIJAC(Double.parseDouble(prononce.getDemiIJAC()));

//        BasesCalculIJ.GarantieAA garantieAA = new BasesCalculIJ.GarantieAA();
//        basesCalculCouranteIJ.setGarantieAA(garantieAA);
        basesCalculCouranteIJ.setCantonImpots(loadCode(session, prononce.getCsCantonImpositionSource()));
        //Choix fait par d�faut � false
        basesCalculCouranteIJ.setMesureRea8A(false);
        //Choix fait par d�faut � false
        basesCalculCouranteIJ.setNouvelleMesure(false);
        return basesCalculCouranteIJ;
    }

    private String loadRevenu(final IJPetiteIJ ijPetiteIJ) {
        String revenu;
        try {
             revenu = ijPetiteIJ.loadRevenu().getRevenu();
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the revenu with this id: " + ijPetiteIJ.getId());
        }
        return revenu;
    }

    private IJRevenu loadRevenu(final IJGrandeIJ ijGrandeIJ) {
        try {
            return ijGrandeIJ.loadRevenuReadaptation(null);
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the revenu with this id: " + ijGrandeIJ.getId());
        }
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

    private DemandeType toDemande(PRTiersWrapper tiersRequerant, IJPrononce prononce, BSession session) {
        PRAcorDemandeTypeMapper demandeTypeAcorMapper = new PRAcorDemandeTypeMapper(session);
        DemandeType demandeType = demandeTypeAcorMapper.map();

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(PRConverterUtils.formatNssToLong(tiersRequerant.getNSS()));
        demandeType.setTypeDemande(TypeDemandeEnum.IJ);
        demandeType.setDateTraitement(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));
        //TODO DMA 01.09.2021 : Que mettre comme date de d�pot
        //demandeType.setDateDepot()
        // Avoir dans l'ancienne mani�reil n'y pas de date de d�pot.
        demandeType.setTypeCalcul(Integer.parseInt(PRACORConst.CA_TYPE_CALCUL_STANDARD));

        return demandeType;
    }

    private List<ISFMembreFamilleRequerant> loadMembres(String idTiers, ISFSituationFamiliale sf) throws PRACORException {
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
