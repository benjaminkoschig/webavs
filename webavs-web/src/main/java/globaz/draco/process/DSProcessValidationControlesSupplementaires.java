package globaz.draco.process;

import ch.globaz.orion.ws.service.AFMassesForAffilie;
import ch.globaz.orion.ws.service.AppAffiliationService;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationListViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationViewBean;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.draco.util.DSUtil;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.inscriptions.CIJournal;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DSProcessValidationControlesSupplementaires {

    public static final String CS_CANTON_VAUD = "505022";

    BSession session = null;

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public DSProcessValidationControlesSupplementaires(BSession session) {
        this.session = session;
    }

    /**
     * Contrôle si les assurances listé dans la propriété système sont toutes présentes
     * utilisé lors du processus de validation des PUCS.
     */
    public boolean contientPasToutesLesAssurancesRequises(DSDeclarationViewBean decl, List<String> listeAssurances) throws Exception {
        // Recherche cotisations actives
        if(listeAssurances == null || listeAssurances.isEmpty()) {
            return false;
        }
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService.retrieveListCotisationForNumAffilie(getSession(),
                decl.getNumeroAffilie(), decl.getAnnee() + "1231");

        // Récupère les ids d'assurance en se basant sur les ids de cotisations présents sur la déclarations de l'affilié
        List<String> cotisationsId = listeMasseForAffilie.stream().map(AFMassesForAffilie::getIdCotisation).collect(Collectors.toList());
        Set<String> assurancesId = new HashSet<>();
        for (String cotisationId : cotisationsId) {
            AFCotisation cotisation = new AFCotisation();
            cotisation.setSession(getSession());
            cotisation.setCotisationId(cotisationId);
            cotisation.retrieve();
            if (!cotisation.isNew()) {
                assurancesId.add(cotisation.getAssuranceId());
            }
        }

        // Compare les ids d'assurances trouvés avec les ids présents dans la propriété système
        if (!assurancesId.containsAll(Arrays.asList(listeAssurances))) {
            return true;
        }

        return false;
    }

    public boolean masseAFetAVScorrespondentPas(DSDeclarationViewBean decl) {
        // Vérifier que la masse AVS corresponde au total de la masse AF de tous les cantons
        BigDecimal controle = new BigDecimal(decl.getTotalControleAf());
        BigDecimal calcule = new BigDecimal("0.00");
        try {
            CIJournal journal = new CIJournal();
            if (!JadeStringUtil.isIntegerEmpty(decl.getIdJournal())) {
                journal = new CIJournal();
                journal.setSession(getSession());
                journal.setIdJournal(decl.getIdJournal());
                journal.retrieve();
                if (!journal.isNew()) {
                    calcule = new BigDecimal(journal.getTotalInscrit());
                }
            }

        } catch (Exception e) {
            calcule = new BigDecimal("0.00");
        }
        return controle.compareTo(calcule) != 0;
    }

    public boolean massePCFamilleEtMasseAFVDNeCorrespondentPas(DSDeclarationViewBean decl) throws Exception {
        DSLigneDeclarationListViewBean ligneManager = new DSLigneDeclarationListViewBean();
        ligneManager.setSession(getSession());
        ligneManager.setForIdDeclaration(decl.getIdDeclaration());
        ligneManager.find(BManager.SIZE_NOLIMIT);
        // Si on ne trouve pas les lignes, on sort
        if (ligneManager.isEmpty() || ligneManager.hasErrors()) {
            return false;
        }
        List<DSLigneDeclarationViewBean> listLigne = ligneManager.toList();

        BigDecimal montantPcFamille = listLigne.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> CodeSystem.TYPE_ASS_PC_FAMILLE.equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal montantCotVd = listLigne.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> CS_CANTON_VAUD.equals(l.getCotisation().getAssurance().getAssuranceCanton()))
                .filter(l -> TypeAssurance.COTISATION_AF.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return montantPcFamille.compareTo(montantCotVd) != 0;
    }

    public boolean masseCantonPasDansAssurance(DSDeclarationViewBean decl) throws Exception {
        // Recherche cotisations actives
        DSLigneDeclarationListViewBean ligneManager = new DSLigneDeclarationListViewBean();
        ligneManager.setSession(getSession());
        ligneManager.setForIdDeclaration(decl.getIdDeclaration());
        ligneManager.find(BManager.SIZE_NOLIMIT);
        // Si on ne trouve pas les lignes, on sort
        if (ligneManager.isEmpty() || ligneManager.hasErrors()) {
            return false;
        }
        List<DSLigneDeclarationViewBean> listLigne = ligneManager.toList();
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService.retrieveListCotisationForNumAffilie(getSession(),
                decl.getNumeroAffilie(), decl.getAnnee() + "1231");

        List<String> listCodeCantonAffilie = listeMasseForAffilie.stream().map(m -> m.getCodeCanton()).distinct().collect(Collectors.toList());
        List<String> listCodeCantonDeclaration = listLigne.stream().map(m -> m.getAssurance().getAssuranceCanton()).distinct().collect(Collectors.toList());
        return !listCodeCantonDeclaration.containsAll(listCodeCantonAffilie);
    }

    public Optional<DSInscriptionsIndividuelles> inscriptionsMontantACetAVSneCorrespondentPas(DSDeclarationViewBean decl) throws FWIException {
        DSInscriptionsIndividuellesManager mgr = new DSInscriptionsIndividuellesManager();
        mgr.setSession(getSession());
        mgr.setForIdDeclaration(decl.getIdDeclaration());
        mgr.setDateProdNNSS(DSUtil.isNNSSActif(getSession(),
                String.valueOf((new Integer(decl.getAnnee()).intValue() + 1))));
        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new FWIException("unable to find complement inscription" + e.getMessage());
        }

        for (DSInscriptionsIndividuelles dsind : mgr.<DSInscriptionsIndividuelles>toList()) {
            if (dsind.getSoumis()
                    && new BigDecimal(dsind.getMontant())
                    .compareTo(new BigDecimal(dsind.getACI()).add(new BigDecimal(dsind.getACII()))) != 0) {
                return Optional.of(dsind);
            }
        }
        return Optional.empty();
    }
}
