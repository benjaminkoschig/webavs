package ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation;

import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCHomes;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;

public class VdSousCodePrestation extends DetermineSousCodePrestation {

    private String descriptionHome(CalculDonneesHome home) {
        String descriptionHome = "N°:" + home.getNumeroIdentification();
        try {

            SimpleHome simpleHome = PegasusImplServiceLocator.getSimpleHomeService().read(home.getIdHome());
            descriptionHome = descriptionHome + " - " + simpleHome.getNomBatiment();

        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeCodingUtil.catchException(this, "determineSousCode", e);
        } catch (JadePersistenceException e) {
            JadeCodingUtil.catchException(this, "determineSousCode", e);
        } catch (JadeApplicationException e) {
            JadeCodingUtil.catchException(this, "determineSousCode", e);
        }
        return descriptionHome;
    }

    @Override
    public String determineSousCode(PeriodePCAccordee periode, boolean isConjoint) throws CalculException {
        final String csRoleBeneficiaire = (isConjoint ? IPCDroits.CS_ROLE_FAMILLE_CONJOINT
                : IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

        Map<String, String> mapAI = new HashMap<String, String>();
        mapAI.put(IPCHomes.CS_SERVICE_ETAT_SPAS, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SPAS);
        mapAI.put(IPCHomes.CS_SERVICE_ETAT_SASH, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SASH);
        mapAI.put(IPCHomes.CS_SERVICE_ETAT_EPS, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_EPS);
        // TODO A traiter lorsque les références rubriques auront été définies
        mapAI.put(IPCHomes.CS_SERVICE_ETAT_SPEN, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SPEN);
        mapAI.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_SESAF, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_DGEJ_SESAF);
        mapAI.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FOYER, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_DGEJ_FOYER);
        mapAI.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FA, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_DGEJ_FA);


        Map<String, String> mapAIHorsCanton = new HashMap<String, String>();
        mapAIHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_SPAS, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SPAS_HORS_CANTON);
        mapAIHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_SASH, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SASH_HORS_CANTON);
        mapAIHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_EPS, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_EPS_HORS_CANTON);
        // TODO A traiter lorsque les références rubriques auront été définies
        mapAIHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_SPEN, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SPEN_HORS_CANTON);
        mapAIHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_SESAF, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_DGEJ_SESAF_HORS_CANTON);
        mapAIHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FOYER, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_DGEJ_FOYER_HORS_CANTON);
        mapAIHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FA, REGenresPrestations.PC_SOUS_TYPE_AI_HOME_DGEJ_FA_HORS_CANTON);

        Map<String, String> mapAVS = new HashMap<String, String>();
        mapAVS.put(IPCHomes.CS_SERVICE_ETAT_SPAS, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SPAS);
        mapAVS.put(IPCHomes.CS_SERVICE_ETAT_SASH, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SASH);
        mapAVS.put(IPCHomes.CS_SERVICE_ETAT_EPS, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_EPS);
        // TODO A traiter lorsque les références rubriques auront été définies
        mapAVS.put(IPCHomes.CS_SERVICE_ETAT_SPEN, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SPEN);
        mapAVS.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_SESAF, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_DGEJ_SESAF);
        mapAVS.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FOYER, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_DGEJ_FOYER);
        mapAVS.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FA, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_DGEJ_FA);

        Map<String, String> mapAVSHorsCanton = new HashMap<String, String>();
        mapAVSHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_SPAS, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SPAS_HORS_CANTON);
        mapAVSHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_SASH, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SASH_HORS_CANTON);
        mapAVSHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_EPS, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_EPS_HORS_CANTON);
        // TODO A traiter lorsque les références rubriques auront été définies
        mapAVSHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_SPEN, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SPEN_HORS_CANTON);
        mapAVSHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_SESAF, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_DGEJ_SESAF_HORS_CANTON);
        mapAVSHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FOYER, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_DGEJ_FOYER_HORS_CANTON);
        mapAVSHorsCanton.put(IPCHomes.CS_SERVICE_ETAT_DGEJ_FA, REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_DGEJ_FA_HORS_CANTON);

        String csServiceEtat = null;
        Boolean isHorsCanton = false;
        String sousTypeGenrePrestation = null;
        CalculDonneesHome home = periode.getPersonneByCsRole(csRoleBeneficiaire).getHome();

        if (periode.getPersonneByCsRole(csRoleBeneficiaire).getIsHome() && home == null) {
            throw new CalculException(
                    "Impossible de détérminer le code sous genre prestation, il n'est pas possible de trouvé le home liée à la PCA en home pour la periode suivante: "
                            + periode.toString()
                            + " et le role du bénéficaire"
                            + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csRoleBeneficiaire));
        }
        if ((home != null) && !JadeStringUtil.isIntegerEmpty(home.getIdHome())) {
            csServiceEtat = home.getCsServiceEtatPeriode();
            isHorsCanton = home.getIsHorsCanton();
            if (JadeStringUtil.isIntegerEmpty(csServiceEtat)) {
                String descriptionHome = descriptionHome(home);
                throw new CalculBusinessException("pegasus.home.serviceEtat.null", descriptionHome);
            }
        }

        if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(periode.getTypePc())) {
            if (!JadeStringUtil.isIntegerEmpty(csServiceEtat)) {
                sousTypeGenrePrestation = findSousCodePrestation(mapAI, mapAIHorsCanton, csServiceEtat, isHorsCanton,
                        home);
            } else {
                sousTypeGenrePrestation = REGenresPrestations.PC_SOUS_TYPE_AI_DOMICILE;
            }
        } else if (IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(periode.getTypePc())
                || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(periode.getTypePc())) {

            if (!JadeStringUtil.isIntegerEmpty(csServiceEtat)) {
                sousTypeGenrePrestation = findSousCodePrestation(mapAVS, mapAVSHorsCanton, csServiceEtat, isHorsCanton,
                        home);
            } else {
                sousTypeGenrePrestation = REGenresPrestations.PC_SOUS_TYPE_AVS_DOMICILE;
            }
        } else {
            throw new CalculException("The type of this pcs is not deal for the 'sous type genre presation'"
                    + periode.getTypePc());
        }
        return sousTypeGenrePrestation;
    }

    private String findSousCodePrestation(Map<String, String> map, Map<String, String> mapHorsCanton,
            String csServiceEtat, Boolean isHorsCanton, CalculDonneesHome home) throws CalculException {
        String sousTypeGenrePrestation;
        if (isHorsCanton) {
            sousTypeGenrePrestation = mapHorsCanton.get(csServiceEtat);
        } else {
            sousTypeGenrePrestation = map.get(csServiceEtat);
        }
        if (sousTypeGenrePrestation == null) {
            throw new CalculBusinessException("pegasus.home.serviceEtat.refRubriqueNull", descriptionHome(home),
                    BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csServiceEtat));
        }
        return sousTypeGenrePrestation;
    }

}
