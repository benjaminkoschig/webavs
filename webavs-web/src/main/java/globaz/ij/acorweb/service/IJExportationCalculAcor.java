package globaz.ij.acorweb.service;

import acor.ch.admin.zas.rc.annonces.rente.rc.DJE10BeschreibungType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.DonneesEchelleType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.OrdinaireBase10Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.RenteOrdinaire10Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.TypeDemandeEnum;
import acor.xsd.in.ij.BasesCalculCouranteIJ;
import acor.xsd.in.ij.BasesCalculIJ;
import acor.xsd.in.ij.BasesCalculRevenusIJ;
import acor.xsd.in.ij.IndemniteJournaliereIJ;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.common.domaine.CodeSystemEnumUtils;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Dates;
import ch.globaz.envoi.business.models.parametrageEnvoi.CodeSystemSearch;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import ch.globaz.vulpecula.business.models.codesystem.CodeSystemComplexModel;
import globaz.externe.IPRConstantesExternes;
import globaz.externe.IPTConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.domaine.membrefamille.SFMembresFamilleRequerant;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.ij.acorweb.mapper.IJCalculDecompteIJMapper;
import globaz.ij.api.prononces.IIJSituationProfessionnelle;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.*;
import globaz.ij.module.IJSalaireFilter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.web.mapper.PRAcorAssureTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorDemandeTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorEnfantTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorFamilleTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorMapper;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static globaz.prestation.acor.web.mapper.PRAcorMapper.loadCodeOrNull;

@Slf4j
class IJExportationCalculAcor {
    private final BSession session;
    private final EntityService entityService;

    IJExportationCalculAcor() {
        session = BSessionUtil.getSessionFromThreadContext();
        entityService = EntityService.of(this.session);
    }

    InHostType createInHostForCalcul(final String idPrononce) {
        return this.createInHost(loadePrononce(idPrononce), null);
    }

    InHostType createInHostForDecompte(final String idIJCalculee, final String idBaseIndemnisation) {
        IJBaseIndemnisation ijBaseIndemnisation = this.loadBaseIndemnisation(idBaseIndemnisation);
        IJIJCalculee ijijCalculee = loadIjCalculee(idIJCalculee);

        IJPrononce prononce = loadePrononce(ijijCalculee.getIdPrononce());

        IJCalculDecompteIJMapper ijCalculDecompteIJMapper = new IJCalculDecompteIJMapper(prononce, ijBaseIndemnisation, ijijCalculee);

        return this.createInHost(prononce, ijCalculDecompteIJMapper);
    }

    private IJPrononce loadePrononce(final String idPrononce) {
        IJPrononce prononce;

        try {
            prononce = IJPrononce.loadPrononce(session, null, idPrononce, null);
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the pronnonce with this id: " + idPrononce, e);
        }
        return prononce;
    }

    private InHostType createInHost(final IJPrononce prononce, final IJCalculDecompteIJMapper ijCalculDecompteIJMapper) {
        try {
            PRDemande demande = prononce.loadDemande(null);
            PRTiersWrapper tiersRequerant = demande.loadTiers();

            ISFSituationFamiliale situationFamiliale = this.loadSituationFamiliale(tiersRequerant.getIdTiers(), session);
            List<ISFMembreFamilleRequerant> membres = this.loadMembres(tiersRequerant.getIdTiers(), situationFamiliale);

            SFMembresFamilleRequerant membresFamilleRequerant = new SFMembresFamilleRequerant(membres);
            List<ISFMembreFamilleRequerant> enfants = membresFamilleRequerant.filtreEnfants();
            List<ISFMembreFamilleRequerant> conjoints = membresFamilleRequerant.filtreConjoints();
            ISFMembreFamilleRequerant requerant = membresFamilleRequerant.filtreByIdTiers(tiersRequerant.getIdTiers());

            PRAcorMapper prAcorMapper = new PRAcorMapper(IPTConstantesExternes.TIERS_ADRESSE_TYPE_DOMICILE, tiersRequerant, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI, session);
            PRAcorAssureTypeMapper assureTypeAcorMapper = new PRAcorAssureTypeMapper(
                    membresFamilleRequerant.filtreTousSaufEnfants(),
                    situationFamiliale,
                    prAcorMapper);

            PRAcorFamilleTypeMapper familleTypeMapper = new PRAcorFamilleTypeMapper(requerant, situationFamiliale, conjoints, prAcorMapper);
            PRAcorEnfantTypeMapper enfantTypeMapper = new PRAcorEnfantTypeMapper(situationFamiliale, enfants, prAcorMapper);


            InHostType inHost = new InHostType();
            inHost.getFamille().addAll(familleTypeMapper.map());
            inHost.getEnfant().addAll(enfantTypeMapper.map());
            inHost.getAssure().addAll(toAssures(assureTypeAcorMapper, prononce, ijCalculDecompteIJMapper, session));

            inHost.setDemande(toDemande(tiersRequerant, prononce, session));
            inHost.setVersionSchema("5.0");

            return inHost;
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }

    private List<AssureType> toAssures(final PRAcorAssureTypeMapper assureTypeAcorMapper,
                                       final IJPrononce prononce,
                                       final IJCalculDecompteIJMapper ijCalculDecompteIJMapper,
                                       final BSession session) {
        return assureTypeAcorMapper.map((membreFamilleRequerant, assureType) -> completeMappingAssure(membreFamilleRequerant, prononce, assureType,
                                                                                                      ijCalculDecompteIJMapper, session));
    }

    private AssureType completeMappingAssure(final ISFMembreFamilleRequerant membreFamille,
                                             final IJPrononce prononce,
                                             final AssureType assureType,
                                             final IJCalculDecompteIJMapper ijCalculDecompteIJMapper, final BSession session) {

        if (Objects.equals(ISFMembreFamille.CS_TYPE_RELATION_REQUERANT, membreFamille.getRelationAuRequerant())) {

            IndemniteJournaliereIJ indemniteJournaliereIJ = new IndemniteJournaliereIJ();
            indemniteJournaliereIJ.getBasesCalcul().add(mapToBaseCalculCourante(prononce, ijCalculDecompteIJMapper, session));
            assureType.setIndemnitesJournalieres(indemniteJournaliereIJ);

            if (!JadeStringUtil.isBlankOrZero(prononce.getMontantRenteEnCours())) {
                assureType.getRenteOrdinaire10().add(mapToRenteOrdinaire10(prononce));
            }

        }
        return assureType;
    }

    private RenteOrdinaire10Type mapToRenteOrdinaire10(IJPrononce prononce) {
        RenteOrdinaire10Type rente = new RenteOrdinaire10Type();
        rente.setAnneeEtat(Dates.toXMLGregorianCalendar("01.01." + prononce.getAnneeRenteEnCours()));
        rente.setMontant(new BigDecimal(prononce.getMontantRenteEnCours()));
        rente.setFraction(1f);
        try {
            rente.setGenre(Integer.valueOf(this.session.getApplication().getProperty(IJApplication.PROPERTY_GENRE_PRESTATION_ACOR)));
        } catch (Exception e) {
            LOG.error("Impossible de trouver la property PROPERTY_GENRE_PRESTATION_ACOR", e);
            rente.setGenre(50);
        }
        rente.setDebutDroit(Dates.toXMLGregorianCalendar(prononce.getDateDebutPrononce()));
        OrdinaireBase10Type base = new OrdinaireBase10Type();
        base.setAnneeNiveau(Dates.toXMLGregorianCalendar("01.01." + Dates.toDate(prononce.getDateDebutPrononce()).getYear()));
        DonneesEchelleType echelleType = new DonneesEchelleType();
        // 10. Echelle On la force � 44.
        String echelle = prononce.getEchelle();
        // si on ne donne pas d'echelle on force a 44
        if (JadeStringUtil.isBlankOrZero(echelle)) {
            echelleType.setSkala((short) 44);
        } else {
            echelleType.setSkala(Integer.valueOf(echelle).shortValue());
        }
        echelleType.setAnrechnungAb1973Bis1978FehlenderBeitragsmonate(0);
        echelleType.setAnrechnungVor1973FehlenderBeitragsmonate(0);
        echelleType.setBeitragsdauerVor1973(new BigDecimal(0));
        echelleType.setBeitragsdauerAb1973(new BigDecimal(0));
        echelleType.setBeitragsjahreJahrgang(0);
        echelleType.setDureeEtrangereApres73(new BigDecimal(0));
        echelleType.setDureeEtrangereAvant73(new BigDecimal(0));

        DJE10BeschreibungType donneesRam = new DJE10BeschreibungType();
        String ram = prononce.getRam();
        if (!JadeStringUtil.isEmpty(ram)) {
            ram = String.valueOf(new Float(ram).intValue());
        }
        donneesRam.setDurchschnittlichesJahreseinkommen(new BigDecimal(ram));
        donneesRam.setBeitragsdauerDurchschnittlichesJahreseinkommen(new BigDecimal(0));
        donneesRam.setGesplitteteEinkommen(false);
        base.setDonneesRam(donneesRam);
        base.setDonneesEchelle(echelleType);
        rente.setBases(base);
        return rente;
    }

    private BasesCalculCouranteIJ mapToBaseCalculCourante(final IJPrononce prononce, final IJCalculDecompteIJMapper ijCalculDecompteIJMapper, final BSession session) {
        BasesCalculCouranteIJ basesCalculCouranteIJ = new BasesCalculCouranteIJ();
        if (prononce.isGrandeIJ()) {
            IJGrandeIJ ijGrandeIJ = this.loadGrandeIJ(prononce.getIdPrononce());
            IJRevenu ijRevenu = loadRevenu(ijGrandeIJ);
            if (ijRevenu != null && !ijRevenu.getRevenu().isEmpty()) {
                basesCalculCouranteIJ.setRevenuMensuelDurantRea(Double.parseDouble(ijRevenu.getRevenu()));
            }
            List<IJSituationProfessionnelle> ijSituationProfessionnelles = this.loadSituationProfessionelle(prononce.getIdPrononce());
            for (IJSituationProfessionnelle situationProfessionnelle : ijSituationProfessionnelles) {
                basesCalculCouranteIJ.getRevenus().add(mapToBaseCalculeRevenus(situationProfessionnelle, session));
            }
        }

        basesCalculCouranteIJ.setId(prononce.getId());
        if(prononce.isNouvelleGrandeIJ()) {
            basesCalculCouranteIJ.setGenreIndemnite(Integer.parseInt(PRACORConst.CA_TYPE_IJ_AVEC_REVENU));
        } else if(prononce.isGrandeIJ()) {
            basesCalculCouranteIJ.setGenreIndemnite(Integer.parseInt(PRACORConst.CA_TYPE_IJ_GRANDE));
        } else if(prononce.isFpi()) {
            basesCalculCouranteIJ.setGenreIndemnite(Integer.parseInt(PRACORConst.CA_TYPE_FPI));
        } else {
            basesCalculCouranteIJ.setGenreIndemnite(Integer.parseInt(PRACORConst.CA_TYPE_IJ_PETITE));
        }

        basesCalculCouranteIJ.setOfficeAI(prononce.getOfficeAI());

        basesCalculCouranteIJ.setDatePrononce(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));
        basesCalculCouranteIJ.setNumeroDecision(Long.parseLong(prononce.getNoDecisionAI()));
        basesCalculCouranteIJ.setEtablissement("Agent d'ex�cution");
        basesCalculCouranteIJ.setStatutij(1);
        //On n'utilise pas cette information car on est toujours en statut 1
        basesCalculCouranteIJ.setIdIJModifiee(null);
        Integer genreRea = loadCodeOrNull(session, prononce.getCsGenre());
        if (genreRea != null) {
            basesCalculCouranteIJ.setGenreReadaptation(genreRea.intValue());
        }
        basesCalculCouranteIJ.setDebutBases(Dates.toXMLGregorianCalendar(prononce.getDateDebutPrononce()));
        basesCalculCouranteIJ.setFinBases(Dates.toXMLGregorianCalendar(prononce.getDateFinPrononce()));

        if (prononce.isPetiteIJ()) {
            IJPetiteIJ ijPetiteIJ = loadPetiteIJ(prononce.getIdPrononce());
            basesCalculCouranteIJ.setFormation(Integer.parseInt(session.getCode(ijPetiteIJ.getCsSituationAssure())));
            IJRevenu ijRevenu = loadRevenuReadaptation(ijPetiteIJ);
            mapRevenuMensuelDurantRea(basesCalculCouranteIJ, ijRevenu);
        } else if (prononce.isFpi()) {
            IJFpi ijFpi = loadFpi(prononce.getIdPrononce());
            IJFpiJointRevenu ijRevenu = loadRevenu(ijFpi);
            mapRevenuMensuelFpi(basesCalculCouranteIJ, ijRevenu);
            basesCalculCouranteIJ.setDebutFormation(Dates.toXMLGregorianCalendar(ijFpi.getDateFormation()));

        }
        Integer statutProf = loadCodeOrNull(session, prononce.getCsStatutProfessionnel());
        if (statutProf != null) {
            basesCalculCouranteIJ.setStatut(statutProf.intValue());
        }
        basesCalculCouranteIJ.setDemiIJAC(Double.parseDouble(prononce.getDemiIJAC()));

        if (!JadeStringUtil.isBlankOrZero(prononce.getMontantGarantiAA())) {
            BasesCalculIJ.GarantieAA garantieAA = new BasesCalculIJ.GarantieAA();
            if (prononce.getMontantGarantiAAReduit()) {
                // R= r�duite
                garantieAA.setType("R");
            } else {
                // NR= non r�duite
                garantieAA.setType("NR");
            }
            garantieAA.setValue(Double.parseDouble(prononce.getMontantGarantiAA()));
            basesCalculCouranteIJ.setGarantieAA(garantieAA);
        }
        if (prononce.getSoumisImpotSource()) {
            basesCalculCouranteIJ.setTauxLibre(!JadeStringUtil.isBlankOrZero(prononce.getTauxImpositionSource()));
            basesCalculCouranteIJ.setCantonImpots(Integer.parseInt(PRACORConst.csCantonToAcor(prononce.getCsCantonImpositionSource())));
        }
        //Choix fait par d�faut � false
        basesCalculCouranteIJ.setMesureRea8A(prononce.getMesureReadaptation8a());
        //Choix fait par d�faut � false
        basesCalculCouranteIJ.setNouvelleMesure(false);

        if (ijCalculDecompteIJMapper != null) {
            basesCalculCouranteIJ.getBasesCalculDecomptes().add(ijCalculDecompteIJMapper.map());
        }
        return basesCalculCouranteIJ;
    }

    private void mapRevenuMensuelDurantRea(BasesCalculCouranteIJ basesCalculCouranteIJ, IJRevenu ijRevenu) {
        if (ijRevenu != null) {
            String revenu = ijRevenu.getRevenu();
            basesCalculCouranteIJ.setRevenuMensuelDurantRea(Double.parseDouble(revenu));
        }
    }

    private IJRevenu loadRevenuReadaptation(final IJPetiteIJ ijPetiteIJ) {
        IJRevenu revenu;
        try {
            revenu = ijPetiteIJ.loadRevenuReadaptation(null);
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

    private IJFpiJointRevenu loadRevenu(final IJFpi ijFpi) {
        try {
            return ijFpi.loadRevenuFpi(null);
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the revenu with this id: " + ijFpi.getId());
        }
    }

    private BasesCalculRevenusIJ mapToBaseCalculeRevenus(final IJSituationProfessionnelle situationProfessionnelle, final BSession session) {
        try {
            IJSalaireFilter salaire = new IJSalaireFilter(this.session, situationProfessionnelle);
            BasesCalculRevenusIJ basesCalculRevenus = new BasesCalculRevenusIJ();
            basesCalculRevenus.setId(situationProfessionnelle.getId());
            basesCalculRevenus.setRevenu(Double.parseDouble(salaire.getMontantSalaireArrondi()));
            basesCalculRevenus.setAnnee(Dates.toXMLGregorianCalendar("01.01." + situationProfessionnelle.getAnneeCorrespondante()));
            basesCalculRevenus.setType(Integer.parseInt(PRACORConst.csPeriodiciteSalaireIJToAcor(session, salaire.getCsPeriodiciteSalaire())));
            //Si le type est egale � horaire
            if (Objects.equals(1, basesCalculRevenus.getType())) {
                basesCalculRevenus.setHeuresHebdo((int) Double.parseDouble(salaire.getNombreHeuresSemaines()));
            }
            IJEmployeur employeur = situationProfessionnelle.loadEmployeur();
            basesCalculRevenus.setNumeroAffilie(employeur.loadNumero());
            basesCalculRevenus.setNomEmployeur(employeur.loadNom());
            return basesCalculRevenus;
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to map the BasesCalculRevenusIJ with this situationProfessionnelle:"
                    + situationProfessionnelle.getIdSituationProfessionnelle(), e);
        }
    }

    private void mapRevenuMensuelFpi(BasesCalculCouranteIJ basesCalculCouranteIJ, IJFpiJointRevenu ijRevenu) {
        if (ijRevenu != null && !JadeStringUtil.isBlankOrZero(ijRevenu.getRevenu())) {
            BasesCalculRevenusIJ basesCalculRevenusIJ = new BasesCalculRevenusIJ();
            basesCalculRevenusIJ.setId(ijRevenu.getId());
            basesCalculRevenusIJ.setRevenu(Double.parseDouble(ijRevenu.getRevenu()));
            basesCalculRevenusIJ.setAnnee(Dates.toXMLGregorianCalendar("01.01." + ijRevenu.getAnnee()));
            basesCalculRevenusIJ.setType(Integer.parseInt(PRACORConst.csPeriodiciteSalaireIJToAcor(session, ijRevenu.getCsPeriodiciteRevenu())));
            if (IIJSituationProfessionnelle.CS_HEBDOMADAIRE.equals(ijRevenu.getCsPeriodiciteRevenu())) {
                basesCalculRevenusIJ.setHeuresHebdo(Integer.parseInt(ijRevenu.getNbHeuresSemaine()));
            }
            basesCalculCouranteIJ.getRevenus().add(basesCalculRevenusIJ);
        }
    }

    private DemandeType toDemande(PRTiersWrapper tiersRequerant, IJPrononce prononce, BSession session) {
        PRAcorDemandeTypeMapper demandeTypeAcorMapper = new PRAcorDemandeTypeMapper(session, tiersRequerant);
        DemandeType demandeType = demandeTypeAcorMapper.map();

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(PRConverterUtils.formatNssToLong(tiersRequerant.getNSS()));
        demandeType.setTypeDemande(TypeDemandeEnum.IJ);
        demandeType.setDateTraitement(Dates.toXMLGregorianCalendar(LocalDate.now()));
        // Il n'y a pas de date d�pot pour les IJ
        demandeType.setDateDepot(Dates.toXMLGregorianCalendar(prononce.getDatePrononce()));
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

    private List<IJSituationProfessionnelle> loadSituationProfessionelle(String idPronnonce) {
        IJSituationProfessionnelleManager mgr = new IJSituationProfessionnelleManager();
        mgr.setISession(this.session);
        mgr.setForIdPrononce(idPronnonce);
        try {
            mgr.find(BManager.SIZE_NOLIMIT);
            return mgr.getContainerAsList();
        } catch (Exception e) {
            throw new CommonTechnicalException("Impossible to load the situationProfessionelle with this id:" + idPronnonce, e);
        }
    }

    private IJPetiteIJ loadPetiteIJ(String idPronnonce) {
        return entityService.load(IJPetiteIJ.class, idPronnonce);
    }

    private IJGrandeIJ loadGrandeIJ(String idPronnonce) {
        return entityService.load(IJGrandeIJ.class, idPronnonce);
    }

    private IJFpi loadFpi(String idPronnonce) {
        return entityService.load(IJFpi.class, idPronnonce);
    }

    private IJBaseIndemnisation loadBaseIndemnisation(String idBaseIndemnisation) {
        return entityService.load(IJBaseIndemnisation.class, idBaseIndemnisation);
    }

    private IJIJCalculee loadIjCalculee(final String idIJCalculee) {
        return entityService.load(IJIJCalculee.class, idIJCalculee);
    }
}
