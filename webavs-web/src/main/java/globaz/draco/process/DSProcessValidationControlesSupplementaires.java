package globaz.draco.process;

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
import globaz.naos.translation.CodeSystem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DSProcessValidationControlesSupplementaires {

    public static final String CS_CANTON_VAUD = "505022";

    private BSession session = null;

    private DSDeclarationViewBean decl;

    private List<DSLigneDeclarationViewBean> lignes = new ArrayList<>();

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public DSProcessValidationControlesSupplementaires(BSession session, DSDeclarationViewBean decl) throws Exception {
        this.session = session;
        this.decl = decl;
        chargeListDeclaration() ;
    }

    private void chargeListDeclaration() throws Exception {
        DSLigneDeclarationListViewBean ligneManager = new DSLigneDeclarationListViewBean();
        ligneManager.setSession(getSession());
        ligneManager.setForIdDeclaration(decl.getIdDeclaration());
        ligneManager.find(BManager.SIZE_NOLIMIT);
        if (!ligneManager.isEmpty() && !ligneManager.hasErrors()) {
            lignes = ligneManager.toList();
        }
    }

    /**
     * Contrôle si les assurances listé dans la propriété système sont toutes présentes
     * utilisé lors du processus de validation des PUCS.
     */
    public boolean contientPasToutesLesAssurancesRequises(List<String> listeAssurances) throws Exception {
        // Recherche cotisations actives
        if(listeAssurances == null || listeAssurances.isEmpty()) {
            return false;
        }

        List<String> assurancesId = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .map(l -> l.getCotisation().getAssurance().getAssuranceId())
                .collect(Collectors.toList());

        // Compare les ids d'assurances trouvés avec les ids présents dans la propriété système
        return !assurancesId.containsAll(listeAssurances);
    }

    public boolean masseAFetAVScorrespondentPas() {
        // Vérifier que la masse AVS corresponde au total de la masse AF de tous les cantons
        BigDecimal montantAVS = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> TypeAssurance.COTISATION_AVS_AI.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal montantAF = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> TypeAssurance.COTISATION_AF.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return montantAF.compareTo(montantAVS) != 0;
    }

    public boolean massePCFamilleEtMasseAFVDNeCorrespondentPas() throws Exception {
        BigDecimal montantPcFamille = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> CodeSystem.TYPE_ASS_PC_FAMILLE.equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal montantCotVd = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> CS_CANTON_VAUD.equals(l.getCotisation().getAssurance().getAssuranceCanton()))
                .filter(l -> TypeAssurance.COTISATION_AF.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return montantPcFamille.compareTo(montantCotVd) != 0;
    }

    public boolean masseCantonPasDansAssurance() throws Exception {
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

        List<DSInscriptionsIndividuelles> list = mgr.<DSInscriptionsIndividuelles>toList();

        Map<String, Double> mapDetailCanton = list.stream()
                .collect(Collectors.groupingBy(DSInscriptionsIndividuelles::getCodeCanton,
                                Collectors.summingDouble(d -> JadeStringUtil.isBlank(d.getMontant()) ? 0D: Double.parseDouble(d.getMontant()))));

        Map<String, Double> mapApercuCanton = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> TypeAssurance.COTISATION_AF.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .filter(l -> !JadeStringUtil.isBlankOrZero(l.getCotisation().getAssurance().getAssuranceCanton()))
                .collect(Collectors.groupingBy(l -> l.getCotisation().getAssurance().getAssuranceCanton(),
                        Collectors.summingDouble(l -> Double.parseDouble(JANumberFormatter.deQuote(l.getMontantDeclaration())))));

        for (Map.Entry<String, Double> entry : mapDetailCanton.entrySet()) {
            // canton manquant dans l apercu
            if(mapApercuCanton.get(entry.getKey()) == null) {
                return true;
            }
            // les montants entre la somme
            if(entry.getValue().compareTo(mapApercuCanton.get(entry.getKey())) != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean inscriptionsMontantACetAVSneCorrespondentPas() throws FWIException {
        BigDecimal montantAVS = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> TypeAssurance.COTISATION_AVS_AI.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal montantAC = lignes.stream()
                .filter(l ->l.getCotisation() != null)
                .filter(l -> TypeAssurance.ASSURANCE_CHOMAGE.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance())
                        || TypeAssurance.COTISATION_AC2.getValue().equals(l.getCotisation().getAssurance().getTypeAssurance()))
                .map(l -> new BigDecimal(JANumberFormatter.deQuote(l.getMontantDeclaration())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return montantAC.compareTo(montantAVS) != 0;
    }
}
