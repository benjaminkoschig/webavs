package ch.globaz.orion.businessimpl.services.adi;

import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.orion.utils.DateConverter;
import ch.globaz.orion.ws.enums.converters.TypeDecisionAcompteIndConverter;
import ch.globaz.xmlns.eb.adi.DecisionAcompteIndEntity;
import ch.globaz.xmlns.eb.adi.StatusDecisionAcompteIndEnum;
import ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd;
import ch.globaz.xmlns.eb.partnerweb.Partner;

public class AdiServiceImpl {

    public static List<DecisionAcompteIndEntity> searchDemandes(BSession session) {
        return ServicesProviders.adiServiceProvide(session).searchDemandesModifAcompteIndForWebavs(
                Arrays.asList(TypeDecisionAcompteInd.PROVISOIRE, TypeDecisionAcompteInd.ACCOMPTE),
                Arrays.asList(StatusDecisionAcompteIndEnum.SAISIE_EN_COURS));
    }

    public static void changeStatutDemande(BSession session, Integer idDecisionAcompteIndEntity,
            StatusDecisionAcompteIndEnum newStatut, String remarqueCaisse) {
        ServicesProviders.adiServiceProvide(session).updateStatutDemandeModifAcompteInd(idDecisionAcompteIndEntity,
                newStatut, remarqueCaisse);
    }

    public static void updateDemandeModifAcompteIndAndLinkDecisionWebAvs(BSession session,
            Integer idDecisionAcompteIndEntity, StatusDecisionAcompteIndEnum newStatut, String remarqueCaisse,
            Integer idDecisionWebAvs) {
        ServicesProviders.adiServiceProvide(session).updateDemandeModifAcompteIndAndLinkDecisionWebAvs(
                idDecisionAcompteIndEntity, newStatut, remarqueCaisse, idDecisionWebAvs);
    }

    public static void createDecisionAcompteInd(BSession session, String numeroAffilie, String annee,
            String dateSoumission, String resultatNet, String capitalInvesti, String typeDecision,
            String idDecisionWebavs) throws Exception {

        // recherche du partner en fonction du numéro d'affilié
        Partner partner = ServicesProviders.partnerWebServiceProvide(session).searchAffilie(numeroAffilie);
        if (partner == null) {
            throw new IllegalStateException("partner for numeroAffilie not found " + numeroAffilie);
        }

        TypeDecisionAcompteInd type = TypeDecisionAcompteIndConverter
                .convertTypeDecisionAcompteIndToTypeEbusiness(Integer.valueOf(typeDecision));

        // conversion de la date de soumission en xmlDate
        XMLGregorianCalendar dateSoumissionXmlCalendar = DateConverter.stringDateToXmlGregorianCalendar(dateSoumission);

        BigDecimal capitalInvestiBd = new BigDecimal(0);
        if (!JadeStringUtil.isBlankOrZero(capitalInvesti)) {
            capitalInvestiBd = new BigDecimal((JANumberFormatter.deQuote(capitalInvesti)));
        }

        BigDecimal resultatNetBd = new BigDecimal(0);
        if (!JadeStringUtil.isBlankOrZero(resultatNet)) {
            resultatNetBd = new BigDecimal((JANumberFormatter.deQuote(resultatNet)));
        }

        // création de la décision
        ServicesProviders.adiServiceProvide(session).createDecisionAcompteIndFromWebavs(partner.getPartnerId(),
                Integer.valueOf(annee), dateSoumissionXmlCalendar, resultatNetBd, capitalInvestiBd, type,
                Integer.valueOf(idDecisionWebavs));
    }
}