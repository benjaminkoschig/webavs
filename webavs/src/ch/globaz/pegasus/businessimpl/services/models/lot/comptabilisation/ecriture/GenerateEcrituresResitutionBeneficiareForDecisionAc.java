package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIEcriture;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ComptabilisationUtil;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementPeriode;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.PrestationPeriode;

class GenerateEcrituresResitutionBeneficiareForDecisionAc extends GenerateOperationBasic {
    Map<Integer, List<Ecriture>> ecritures = new TreeMap<Integer, List<Ecriture>>();

    private void addEcriture(SectionPegasus sectionPegasus, String debitCredit, BigDecimal montants,
            String idCompteAnnexe, String csRoleFamille, Integer noGroupePeriode, OrdreVersement ov)
            throws JadeApplicationException {
        Ecriture ecriture = generateEcritureStandard(sectionPegasus, debitCredit, montants, idCompteAnnexe,
                TypeEcriture.STANDARD, ov);
        if (ecriture != null) {
            ecriture.setCsTypeRoleFamille(csRoleFamille);
            if (!ecritures.containsKey(noGroupePeriode)) {
                ecritures.put(noGroupePeriode, new ArrayList<Ecriture>());
            }
            ecritures.get(noGroupePeriode).add(ecriture);
        }
    }

    public List<Ecriture> generateEcritures(List<PrestationPeriode> prestationPeriodes) throws JadeApplicationException {
        this.generateEcrituresBeneficiaireRestiution(prestationPeriodes);
        return toList();
    }

    private List<Ecriture> generateEcrituresBeneficiaireRestiution(List<PrestationPeriode> periodes)
            throws JadeApplicationException {
        for (PrestationPeriode periode : periodes) {
            this.generateEcrituresBeneficiaireRestiution(periode.getRequerant(), IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                    periode.getNoGroupePeriode());
            this.generateEcrituresBeneficiaireRestiution(periode.getConjoint(), IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                    periode.getNoGroupePeriode());
        }

        return toList();
    }

    private void generateEcrituresBeneficiaireRestiution(OrdreVersementPeriode ovPeriode,
            String csRoleFamilleRequerant, Integer noGroupePeriode) throws JadeApplicationException {
        if (ovPeriode != null) {
            generateEcritureStandards(ovPeriode.getBeneficiaire(), SectionPegasus.DECISION_PC, APIEcriture.CREDIT,
                    ovPeriode.getIdCompteAnnexe(), csRoleFamilleRequerant, noGroupePeriode);
            generateEcritureStandards(ovPeriode.getRestitution(), SectionPegasus.RESTIUTION, APIEcriture.DEBIT,
                    ovPeriode.getIdCompteAnnexe(), csRoleFamilleRequerant, noGroupePeriode);
        }
    }

    private void generateEcritureStandards(OrdreVersement ov, SectionPegasus sectionPegasus, String debitCredit,
            String idCompteAnnexe, String csRoleFamilleRequerant, Integer noGroupePeriode)
            throws JadeApplicationException {
        if (ov != null) {
            if (ov.isDom2R()) {
                BigDecimal[] montantsBeneficiare = ComptabilisationUtil.splitMontant(ov.getMontant().setScale(0));
                addEcriture(sectionPegasus, debitCredit, montantsBeneficiare[0], idCompteAnnexe,
                        IPCDroits.CS_ROLE_FAMILLE_REQUERANT, noGroupePeriode, ov);
                addEcriture(sectionPegasus, debitCredit, montantsBeneficiare[1], idCompteAnnexe,
                        IPCDroits.CS_ROLE_FAMILLE_CONJOINT, noGroupePeriode, ov);
            } else {
                addEcriture(sectionPegasus, debitCredit, ov.getMontant(), idCompteAnnexe, csRoleFamilleRequerant,
                        noGroupePeriode, ov);
            }
        }
    }

    public Map<Integer, List<Ecriture>> getMapEcritures() {
        return ecritures;
    }

    private Map<String, CompteAnnexeSimpleModel> groupByIdRole(List<CompteAnnexeSimpleModel> comptesAnnexes) {
        Map<String, CompteAnnexeSimpleModel> res = new LinkedHashMap<String, CompteAnnexeSimpleModel>();
        for (CompteAnnexeSimpleModel compteAnnexe : comptesAnnexes) {
            res.put(compteAnnexe.getIdRole(), compteAnnexe);
        }
        return res;
    }

    private List<Ecriture> toList() {
        List<Ecriture> list = new ArrayList<Ecriture>();
        for (Entry<Integer, List<Ecriture>> e : ecritures.entrySet()) {
            list.addAll(e.getValue());
        }
        return list;
    }
}
