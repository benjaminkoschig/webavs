/**
 * 
 */
package ch.globaz.al.businessimpl.services.attestationVersement;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstAttestattionVersees;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.declarationVersement.ALAttestationVersementException;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexModel;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.attestationVersement.AttestationsVersementLoadCsvDetailleService;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author pta
 * 
 */
public class AttestationsVersementLoadCsvDetailleServiceImpl implements AttestationsVersementLoadCsvDetailleService {

    private void appendEnteteLabels(StringBuffer csv) {
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.attestations.versement.entete.nss"))
                .append(";");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.attestations.versement.entete.nom"))
                .append(";");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.entete.prenom")).append(";");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.entete.periode")).append(";");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.entete.typePrestation")).append(";");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.entete.typeVersement")).append(";");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.entete.total")).append(";");
        csv.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.entete.complement")).append("\n");
    }

    /**
     * Méthode qui retourne oui ou non pour les types de prestations pris en compte
     * 
     * @param typePrestation
     * @param csvData
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private StringBuffer appendTypesPrestationsLabels(String typePrestation, StringBuffer csvData)
            throws JadeApplicationException, JadePersistenceException {

        if (typePrestation == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementLoadCsvDetailleServiceImpl#loadTexteTypePrestaion typePrestation is null");
        }
        if (csvData == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementLoadCsvDetailleServiceImpl#loadTexteTypePrestaion csvData is null");
        }

        String versementIndirect = "";
        String versementDirect = "";
        String versementTiersBeneficiaire = "";
        String oui = JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.attestations.versement.oui");
        String non = JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.attestations.versement.non");

        if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_DIR_INDIR, false)) {
            versementIndirect = oui;
            versementDirect = non;
            versementTiersBeneficiaire = non;

        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_DIR_TIERS_BEN, false)) {
            versementIndirect = non;
            versementDirect = non;
            versementTiersBeneficiaire = oui;

        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_IND_DIR_TIERS_BEN,
                false)) {
            versementIndirect = oui;
            versementDirect = oui;
            versementTiersBeneficiaire = oui;

        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_IND_TIERS_BEN, false)) {
            versementIndirect = oui;
            versementDirect = non;
            versementTiersBeneficiaire = oui;

        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_INDIRECT, false)) {
            versementIndirect = oui;
            versementDirect = non;
            versementTiersBeneficiaire = non;

        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_TIERS_BEN, false)) {
            versementIndirect = non;
            versementDirect = non;
            versementTiersBeneficiaire = oui;

        } else if (JadeStringUtil.equals(typePrestation, ALConstAttestattionVersees.ATTEST_PRESTA_DIRECT, false)) {
            versementIndirect = non;
            versementDirect = oui;
            versementTiersBeneficiaire = oui;

        } else {
            throw new ALAttestationVersementException(
                    "AttestationsVersementLoadCsvDetailleServiceImpl#loadTexteTypePrestaion: this type of prestation doesnt exist ");
        }

        csvData.append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "al.attestations.versement.typesVersements",
                new String[] { versementIndirect, versementDirect, versementTiersBeneficiaire }));

        csvData.append("\n\n\n");

        return csvData;
    }

    @Override
    public String buildCsvAttestationDetaille(
            ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listPrestAtte,
            DossierAttestationVersementComplexSearchModel dossierSearch, String periodeDe, String periodeA,
            String typePrestation) throws JadeApplicationException, JadePersistenceException {

        if (listPrestAtte == null) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationDetaille#buildCsvAttestationDetaille: listPrestAtte: is null ");
        }
        if (dossierSearch == null) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationDetaille#buildCsvAttestationDetaille: dossierSearch: is null ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException("buildCsvAttestationDetaille#buildCsvAttestationDetaille: date "
                    + periodeDe + " is not a valid date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException("buildCsvAttestationDetaille#buildCsvAttestationDetaille: date "
                    + periodeA + " is not a valid date");
        }
        if (typePrestation == null) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationDetaille#buildCsvAttestationDetaille: typePrestation  is null");
        }

        StringBuffer csv = buildCsvTexteInitial(dossierSearch, periodeDe, periodeA);
        appendTypesPrestationsLabels(typePrestation, csv);
        appendEnteteLabels(csv);

        for (int i = 0; i < listPrestAtte.size(); i++) {

            String nssAlloc = null;
            String nomAlloc = null;
            String prenomAlloc = null;

            ArrayList<DeclarationVersementDetailleComplexModel> prestDossier = listPrestAtte.get(i);

            Iterator<DeclarationVersementDetailleComplexModel> iter = prestDossier.iterator();

            boolean isFirst = true;
            DossierComplexModel dossier = null;

            while (iter.hasNext()) {

                DeclarationVersementDetailleComplexModel presta = iter.next();

                if (isFirst) {

                    dossier = ALServiceLocator.getDossierComplexModelService().read(presta.getIdDossier());
                    nssAlloc = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                            .getPersonneEtendue().getNumAvsActuel();
                    nomAlloc = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation1();
                    prenomAlloc = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation2();

                    isFirst = false;

                }

                String typeVersement = getTypeVersementLabel(this.getTypeVersement(presta, dossier));

                Set<String> statutPrestation = new HashSet<String>();
                statutPrestation.add(presta.getStatutPrestation());

                csv.append(nssAlloc).append(";").append(nomAlloc).append(";").append(prenomAlloc).append(";")
                        .append(presta.periode).append(";")
                        .append(JadeCodesSystemsUtil.getCodeLibelle(presta.getTypePrestation())).append(";")
                        .append(typeVersement).append(";")
                        .append(JANumberFormatter.fmt(presta.getMontantDetailPrestation(), true, true, false, 2))
                        .append(";").append(getStatutPrestationLabel(statutPrestation)).append("\n");
            }
        }

        return csv.toString();
    }

    @Override
    public String buildCsvAttestationNonDetaille(
            ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listPrestAtte,
            DossierAttestationVersementComplexSearchModel dossierSearch, String periodeDe, String periodeA,
            String typePrestation) throws JadeApplicationException, JadePersistenceException {

        if (listPrestAtte == null) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationNonDetaille#buildCsvAttestationDetaille: listPrestAtte: is null ");
        }
        if (dossierSearch == null) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationNonDetaille#buildCsvAttestationDetaille: dossierSearch: is null ");
        }
        if (!JadeDateUtil.isGlobazDate(periodeDe)) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationNonDetaille#buildCsvAttestationDetaille: date " + periodeDe
                            + " is not a valid date");
        }
        if (!JadeDateUtil.isGlobazDate(periodeA)) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationNonDetaille#buildCsvAttestationDetaille: date " + periodeA
                            + " is not a valid date");
        }
        if (typePrestation == null) {
            throw new ALAttestationVersementException(
                    "buildCsvAttestationDetaille#buildCsvAttestationNonDetaille: typePrestation is null");
        }

        StringBuffer csv = buildCsvTexteInitial(dossierSearch, periodeDe, periodeA);
        appendTypesPrestationsLabels(typePrestation, csv);
        appendEnteteLabels(csv);

        for (int i = 0; i < listPrestAtte.size(); i++) {

            String nssAlloc = null;
            String nomAlloc = null;
            String prenomAlloc = null;

            ArrayList<?> prestDossier = listPrestAtte.get(i);

            Iterator<?> iter = prestDossier.iterator();

            boolean isFirst = true;
            BigDecimal montantTotal = new BigDecimal("0");
            Set<TypeVersement> typesVersement = new HashSet<TypeVersement>();

            Set<String> statutPrestation = new HashSet<String>();
            DossierComplexModel dossier = null;

            while (iter.hasNext()) {

                DeclarationVersementDetailleComplexModel presta = (DeclarationVersementDetailleComplexModel) iter
                        .next();
                if (isFirst) {

                    dossier = ALServiceLocator.getDossierComplexModelService().read(presta.getIdDossier());
                    nssAlloc = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                            .getPersonneEtendue().getNumAvsActuel();
                    nomAlloc = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation1();
                    prenomAlloc = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation2();

                    isFirst = false;

                }

                montantTotal = montantTotal.add(new BigDecimal(presta.getMontantDetailPrestation()));
                typesVersement.add(this.getTypeVersement(presta, dossier));
                statutPrestation.add(presta.getStatutPrestation());
            }

            String typeVersement = getTypeVersementLabel(this.getTypeVersement(typesVersement));

            csv.append(nssAlloc).append(";").append(nomAlloc).append(";").append(prenomAlloc).append(";*")
                    .append(";*;").append(typeVersement).append(";").append(montantTotal).append(";")
                    .append(getStatutPrestationLabel(statutPrestation)).append("\n");
        }

        return csv.toString();
    }

    /**
     * Méthode qui récupére le texte des entêtes pour les documents des attestations de versments
     * 
     * @param dossierSearch
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private StringBuffer buildCsvTexteInitial(DossierAttestationVersementComplexSearchModel dossierSearch,
            String periodeDe, String periodeA) throws JadeApplicationException, JadePersistenceException {

        if (dossierSearch == null) {
            throw new ALAttestationVersementException(
                    "AttestationsVersementLoadCsvDetailleServiceImpl#buildCsvTexteInitial dossierSearch is null");
        }

        StringBuffer csvData = new StringBuffer();

        ArrayList<String> listAffilie = new ArrayList<String>();
        String numAffilie = new String();
        if (dossierSearch.getSize() > 0) {
            for (int i = 0; i < dossierSearch.getSize(); i++) {
                DossierAttestationVersementComplexModel dossier = (DossierAttestationVersementComplexModel) dossierSearch
                        .getSearchResults()[i];

                if (i == 0) {
                    listAffilie.add(dossier.getNumAffilie());
                    numAffilie = dossier.getNumAffilie();
                } else if (JadeStringUtil.equals(numAffilie, dossier.getNumAffilie(), false)) {
                    continue;
                } else {
                    listAffilie.add(dossier.getNumAffilie());
                    numAffilie = dossier.getNumAffilie();
                }
            }

        }

        AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(numAffilie);

        TiersSimpleModel tiers = TIBusinessServiceLocator.getTiersService().read(
                AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(numAffilie));

        // ajout du texte initial pour l'affilie

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.NUMERO_CAISSE, periodeDe);

        csvData.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.liste.prestations.versees")).append(
                listAffilie.size() == 1 ? numAffilie + " - " + tiers.getDesignation1() + " " + tiers.getDesignation2()
                        : "*");

        csvData.append(";;;;CAF : ").append(param.getValeurAlphaParametre()).append("\n");

        csvData.append(
                JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.attestations.versement.periode",
                        new String[] { periodeDe, periodeA })).append("\n");

        return csvData;
    }

    private TypeVersement getTypeVersement(DeclarationVersementDetailleComplexModel presta, DossierComplexModel dossier) {

        if (JadeNumericUtil.isEmptyOrZero(presta.getTiersBeneficiaire())) {
            return TypeVersement.INDIRECT;
        } else if (presta.getTiersBeneficiaire().equals(
                dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire())) {
            return TypeVersement.DIRECT;
        } else {
            return TypeVersement.TIERS_BENEFICIAIRE;
        }
    }

    private TypeVersement getTypeVersement(Set<TypeVersement> typesVersement) throws ALAttestationVersementException {

        if ((typesVersement == null) || (typesVersement.size() == 0)) {
            throw new ALAttestationVersementException("typeVersement is null or empty");
        }

        if (typesVersement.size() > 1) {
            return TypeVersement.MULTIPLE;
        } else {
            return typesVersement.iterator().next();
        }
    }

    private String getTypeVersementLabel(TypeVersement typeVersement) {

        switch (typeVersement) {
            case DIRECT:
                return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.type.versement.direct");

            case TIERS_BENEFICIAIRE:
                return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.type.versement.tiersbeneficiaire");

            case INDIRECT:
                return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.type.versement.indirect");

            case MULTIPLE:
                return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "al.attestations.versement.type.versement.multiple");

            default:
                return "?";
        }
    }

    private String getStatutPrestationLabel(Set<String> statutsPrestation) throws ALAttestationVersementException {
        if ((statutsPrestation == null) || (statutsPrestation.size() == 0)) {
            throw new ALAttestationVersementException("statutsPrestation is null or empty");
        }
        if (statutsPrestation.size() > 1) {
            return "*";
        } else {
            if (ALCSPrestation.STATUT_CH.equals(statutsPrestation.iterator().next())) {
                return "";
            }
            return JadeCodesSystemsUtil.getCodeLibelle(statutsPrestation.iterator().next());
        }
    }
}
