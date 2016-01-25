package globaz.orion.process;

import globaz.draco.db.declaration.DSValidationViewBean;
import globaz.draco.process.DSProcessValidation;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.process.CIDeclaration;
import globaz.pavo.process.CIImportPucsFileProcess;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.document.VariablesTemplate;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;

public class ValidationAutomatique {

    private final BigDecimal pourcentage;
    private final BigDecimal limit;
    private static final MathContext mathContext = new MathContext(16, RoundingMode.HALF_UP);
    private final BigDecimal montantFacture;
    private final BigDecimal masse;
    private final boolean checkMontantFactureNegatif;

    public ValidationAutomatique(BigDecimal montantFacture, BigDecimal masse) throws PropertiesException {
        pourcentage = EBProperties.VALIDATION_MONTANT_POURCENTAGE_CONTROLE.getValueAsBigDecimal();
        limit = EBProperties.VALIDATION_MONTANT_LIMIT_CONTROLE.getValueAsBigDecimal();
        checkMontantFactureNegatif = EBProperties.VALIDATION_MONTANT_NEGATIF.getBooleanValue();
        this.montantFacture = montantFacture;
        this.masse = masse;
    }

    ValidationAutomatique(BigDecimal pourcentage, BigDecimal limit, BigDecimal montantFacture, BigDecimal masse,
            boolean checkMontantFactureNegatif) {
        this.pourcentage = pourcentage;
        this.limit = limit;
        this.montantFacture = montantFacture;
        this.checkMontantFactureNegatif = checkMontantFactureNegatif;
        this.masse = masse;
    }

    public BigDecimal getPourcentage() {
        return pourcentage;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public static MathContext getMathcontext() {
        return mathContext;
    }

    private boolean isControlePourCentageValide() {
        BigDecimal result = computePourcentage();
        if (pourcentage.compareTo(result) >= 1 || pourcentage.compareTo(result) == 0) {
            return true;
        }
        return false;
    }

    public BigDecimal computePourcentage() {
        if (montantFacture.compareTo(new BigDecimal(0)) == 0) {
            return montantFacture;
        }
        BigDecimal result = montantFacture.divide(masse, mathContext).multiply(new BigDecimal(100), mathContext).abs();
        return result.setScale(2, mathContext.getRoundingMode());
    }

    private boolean isCheckMontantFactureNegatif() {
        if (checkMontantFactureNegatif) {
            return montantFacture.signum() == -1;
        }
        return false;
    }

    public boolean isMontantValide() {
        if (isCheckMontantFactureNegatif()) {
            return false;
        }

        if (isControlePourCentageValide() && isControleLimitValide()) {
            return true;
        }
        return false;
    }

    boolean isControleLimitValide() {
        if ((limit.abs().compareTo(montantFacture.abs()) >= 1 || limit.abs().compareTo(montantFacture.abs()) == 0)) {
            return true;
        }
        return false;
    }

    public boolean isMontantSame(String totalPucsFile) {
        BigDecimal totalPucs = new BigDecimal(totalPucsFile, mathContext);
        return masse.setScale(4).equals(totalPucs.setScale(4));
    }

    public static DSProcessValidation execute(BSession session, CIDeclaration declaration, ElementsDomParser parser)
            throws Exception {
        DSProcessValidation processValidation = new DSProcessValidation();
        processValidation.setEMailAddress(session.getUserEMail());
        processValidation.setSession(session);
        processValidation.setIdDeclaration(declaration.getDeclaration().getIdDeclaration());
        ValidationAutomatique validationAutomatique = null;
        Map<String, String> values = new HashMap<String, String>();
        boolean isProcessExecuted = false;

        String totalPucsFile = parser.find("Total-AHV-AVS-Incomes").getFirstValue();

        if (!session.hasErrors() && !isImportationKo(declaration) && !declaration.hasCasRejete()) {
            DSValidationViewBean vb = new DSValidationViewBean();
            vb.setIdDeclaration(declaration.getDeclaration().getIdDeclaration());
            vb.setNumeroAffilie(declaration.getNumAffilieBase());
            vb.setAffilieNumero(declaration.getNumAffilieBase());
            vb.setSession(session);
            vb.retrieve();

            BigDecimal montantMasse = new BigDecimal(vb.getMasseSalTotalWhitoutFormat(), mathContext).setScale(2);
            BigDecimal montantFacture = vb.getMontantFacture().getBigDecimalValue().setScale(2);
            if (montantFacture == null) {
                montantFacture = BigDecimal.ZERO;
            }
            validationAutomatique = new ValidationAutomatique(montantFacture, montantMasse);
            if ((!declaration.hasDifferenceAc()) && validationAutomatique.isMontantSame(totalPucsFile)
                    && validationAutomatique.isMontantValide()) {

                processValidation.setNotImpressionDecFinalAZero(vb.getNotImpressionDecFinalAZero());
                processValidation.executeProcess();
                isProcessExecuted = true;
            }
            values.put("pourcentageCalcule", validationAutomatique.computePourcentage().toString());
            values.put("pourcentageDonne",
                    validationAutomatique.getPourcentage().setScale(2, mathContext.getRoundingMode()).toString());
            values.put("limitDonne",
                    new FWCurrency(validationAutomatique.getLimit().setScale(2, mathContext.getRoundingMode())
                            .toString()).toStringFormat());
            values.put("montantFacture", new FWCurrency(montantFacture.setScale(2, mathContext.getRoundingMode())
                    .toString()).toStringFormat());
            values.put("montantCommunique", new FWCurrency(totalPucsFile.toString()).toStringFormat());
            values.put("montantMasse", new FWCurrency(montantMasse.setScale(2, mathContext.getRoundingMode())
                    .toString()).toStringFormat());

            // values.put("checkMontantFactureNegatif", validationAutomatique.isCheckMontantFactureNegatif());

        }
        if (!isProcessExecuted && !session.hasErrors()) {
            List<String> messages = new ArrayList<String>();

            if (declaration.hasCasRejete()) {
                messages.add("PROCES_IMPORTATION_CAS_EN_ERREUR");
            }
            if (declaration.hasDifferenceAc()) {
                messages.add("PROCES_IMPORTATION_AC_NOT_MATCH");
            }
            if (validationAutomatique != null) {
                if (!validationAutomatique.isMontantSame(totalPucsFile)) {
                    messages.add("PROCES_IMPORTATION_MONTANT_MASSE_IDENTIQUE");
                }
                if (!validationAutomatique.isControleLimitValide()) {
                    messages.add("PROCES_IMPORTATION_CONTORLE_MONTANT_LIMIT");
                }
                if (!validationAutomatique.isControlePourCentageValide()) {
                    messages.add("PROCES_IMPORTATION_CONTORLE_POURCENTAGE_NOT_MATCH");
                }
                if (validationAutomatique.isCheckMontantFactureNegatif()) {
                    messages.add("PROCES_IMPORTATION_CONTORLE_MONTANT_NEGATIF");
                }
            }
            String body = "";
            for (String message : messages) {
                body = body + VariablesTemplate.render(session.getLabel(message), values) + "\n";
            }

            JadeSmtpClient.getInstance().sendMail(
                    processValidation.getEMailAddress(),
                    session.getLabel("PROCES_IMPORTATION_VALIDATION_AUTOMATIQUE_IMPOSSIBLE") + ": "
                            + declaration.getNumAffilieBase(), body, null);
        }
        return processValidation;
    }

    private static boolean isImportationKo(CIDeclaration declaration) {
        if (declaration.getLauncherImportPucsFileProcess() != null) {
            return declaration.getLauncherImportPucsFileProcess().getImportStatutAFile()
                    .contains(CIImportPucsFileProcess.IMPORT_STATUT_KO);
        } else {
            return false;
        }
    }

}
