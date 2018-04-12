package ch.globaz.orion.businessimpl.services.af;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.orion.business.models.af.RecapAfAndLignes;
import ch.globaz.orion.business.models.af.StatutRecapAfWebAvsEnum;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.xmlns.eb.recapaf.EBRecapAfException_Exception;
import ch.globaz.xmlns.eb.recapaf.FindResultBean;
import ch.globaz.xmlns.eb.recapaf.MotifChangementAfEnum;
import ch.globaz.xmlns.eb.recapaf.NouvelleLigneRecapAf;
import ch.globaz.xmlns.eb.recapaf.OrderByDirEnum;
import ch.globaz.xmlns.eb.recapaf.RecapAfOrderByEnum;
import ch.globaz.xmlns.eb.recapaf.StatutLigneRecapEnum;
import ch.globaz.xmlns.eb.recapaf.StatutRecapEnum;
import ch.globaz.xmlns.eb.recapaf.UniteTempsEnum;

public class AfServiceImpl {
    public static void createRecapAf(BSession session, String numAffilie, XMLGregorianCalendar anneeMoisRecap,
            XMLGregorianCalendar misADispo, List<NouvelleLigneRecapAf> lignesRecap) throws EBRecapAfException_Exception {
        ServicesProviders.recapAfServiceProvide(session).createRecapAf(numAffilie, anneeMoisRecap, misADispo,
                lignesRecap);
    }

    public static FindResultBean listRecapAf(BSession session, String likeNumeroAffilie, String likeRaisonSociale,
            Integer forAnnee, Integer forMois, StatutRecapAfWebAvsEnum forRecapStatut,
            XMLGregorianCalendar lastModificationDate, Integer fromRecord, Integer nbRecords,
            RecapAfOrderByEnum orderBy, OrderByDirEnum orderByDir) throws EBRecapAfException_Exception {
        Boolean isAucunChangement = null;
        StatutRecapEnum statutEbu = null;
        List<StatutRecapEnum> statutsEbu = null;

        if (forRecapStatut != null) {
            if (forRecapStatut.equals(StatutRecapAfWebAvsEnum.GENEREE)) {
                statutEbu = StatutRecapEnum.A_TRAITER;
            } else if (forRecapStatut.equals(StatutRecapAfWebAvsEnum.TRAITEE)) {
                statutEbu = StatutRecapEnum.TRAITEE_CAISSE;
            } else if (forRecapStatut.equals(StatutRecapAfWebAvsEnum.A_TRAITER)) {
                isAucunChangement = false;
                statutEbu = StatutRecapEnum.TRAITEE;
            } else if (forRecapStatut.equals(StatutRecapAfWebAvsEnum.AUCUN_CHANGEMENT)) {
                isAucunChangement = true;
                statutEbu = StatutRecapEnum.TRAITEE;
            } else if (forRecapStatut.equals(StatutRecapAfWebAvsEnum.CLOTUREE)) {
                isAucunChangement = null;
                statutEbu = StatutRecapEnum.CLOTUREE;
            }

            statutsEbu = Arrays.asList(statutEbu);
        } else {
            isAucunChangement = null;
        }

        return ServicesProviders.recapAfServiceProvide(session).searchRecapAfForCaisse(likeNumeroAffilie,
                likeRaisonSociale, forAnnee, forMois, statutsEbu, isAucunChangement, lastModificationDate, fromRecord,
                nbRecords, orderBy, orderByDir);
    }

    public static RecapAfAndLignes readRecapAfAndLignes(BSession session, Integer idRecapAf) throws Exception {
        ch.globaz.xmlns.eb.recapaf.RecapAfAndLignes recapAfAndLignesEbu = ServicesProviders.recapAfServiceProvide(
                session).readRecapAfAndLignes(idRecapAf);

        return new RecapAfAndLignes(recapAfAndLignesEbu);
    }

    public static void updateStatutLigneRecapToTreatedByCaisse(BSession session, String idLigneRecap,
            Integer nbUniteTravailInt, String uniteTravail, String motif, XMLGregorianCalendar dateDebutXMLGregCal,
            XMLGregorianCalendar dateFinXMLGregCal, String remarque) {

        MotifChangementAfEnum motifEnum;
        if (JadeStringUtil.isEmpty(motif)) {
            motifEnum = null;
        } else {
            motifEnum = MotifChangementAfEnum.valueOf(motif);
        }

        ServicesProviders.recapAfServiceProvide(session).updateLigneRecapAfFromWebAvs(Integer.valueOf(idLigneRecap),
                nbUniteTravailInt, UniteTempsEnum.valueOf(uniteTravail), motifEnum, dateDebutXMLGregCal,
                dateFinXMLGregCal, remarque, StatutLigneRecapEnum.TRAITEE_CAISSE);
    }

    public static void cloturerRecapAf(BSession session, XMLGregorianCalendar periode) {
        ServicesProviders.recapAfServiceProvide(session).cloturerRecapAf(periode);
    }
}
