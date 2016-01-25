package ch.globaz.pegasus.businessimpl.services.process.statistiquesofas;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.exceptions.EntiteException;
import ch.globaz.jade.process.business.exceptions.JadeProcessException;
import ch.globaz.jade.process.business.models.logInfo.EntityLogsProperties;
import ch.globaz.pegasus.business.exceptions.models.process.StatistiquesOFASException;
import ch.globaz.pegasus.business.models.process.statistiquesofas.PlanCalculeDemandeDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.process.statistiquesofas.RenteApiIjSearch;
import ch.globaz.pegasus.business.services.process.statistiquesofas.StatistiquesOFASService;
import ch.globaz.pegasus.process.statistiquesOFAS.PCProcessStatistiquesOFASEnum;
import ch.globaz.pegasus.process.statistiquesOFAS.StatistiquesOFAData;
import com.google.gson.Gson;

public class StatistiquesOFASServiceImpl implements StatistiquesOFASService {

    private String createContent(String idExecutionProcess) {
        List<EntityLogsProperties<PCProcessStatistiquesOFASEnum>> list = searchEntity(idExecutionProcess);
        Gson gson = new Gson();
        StringBuffer content = new StringBuffer();

        for (EntityLogsProperties<PCProcessStatistiquesOFASEnum> entity : list) {

            StatistiquesOFAData data = gson.fromJson(
                    entity.getProperties().get(PCProcessStatistiquesOFASEnum.OBJET_JSON_STATISTIQUESOFAS),
                    StatistiquesOFAData.class);
            if (data != null) {
                createLigne(data, content);
                content.append("\r\n");
            }
        }
        // content.lastIndexOf(str)
        return content.toString();
    }

    private String createFile(String content, String name) throws StatistiquesOFASException {

        String paht = Jade.getInstance().getPersistenceDir() + "_" + name + "_" + JadeUUIDGenerator.createStringUUID()
                + ".txt";
        java.io.File f = new java.io.File(paht);
        FileWriter fstream;
        try {
            fstream = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(content);
            out.close();
        } catch (IOException e) {
            throw new StatistiquesOFASException("Unable to create the file", e);
        }

        return paht;
    }

    private void createLigne(StatistiquesOFAData data, StringBuffer content) {
        // Identification
        content.append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getNumAvs().replaceAll("\\.", ""), 13))
                // NAP1
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getNumAvsConj().replaceAll("\\.", ""), 13))
                // NAP2
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getNumAvsDansHome().replaceAll("\\.", ""),
                        13));// NAP3

        // montant PC
        content.append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getMontantPc()), 6));// MBEL_X

        // Revenus
        content.append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getRentesAvs()), 6))
                // MERE
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getAllocationImpotant()), 6))// MEH1
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getIj()), 6))// METG
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getPrestaionsLAMAL()), 6))// MEK1
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getActLucBrut()), 6))// MEER_X
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getActLucPrisEnCompt()), 6))// MEER
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getAutreRentes()), 6))// MEUR
                .append(JadeStringUtil.fillWithZeroes(
                        String.valueOf((int) data.getRevenu().getRevenuFortuneMobilier()), 6))// MEVE
                .append(JadeStringUtil.fillWithZeroes(
                        String.valueOf((int) data.getRevenu().getProduitFortuneImmobilier()), 6))// MELE
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getValeurLocative()), 6))// MEEM
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getUsufrutit()), 6))// MEWO
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getRevenu().getAutresRevenues()), 6))// MEUE
                .append(JadeStringUtil.fillWithZeroes(
                        String.valueOf((int) data.getRevenu().getForturnePriseEnCompte()), 6))// MEVV
                .append(JadeStringUtil.fillWithZeroes(
                        String.valueOf((int) data.getRevenu().getForturnePriseEnCompteTaux()), 6));// PEVV_X

        // Fortune/Dettes
        content.append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getFortuneDettes().getImmobiliere()), 7))// MVVE
                .append(JadeStringUtil.fillWithZeroes(
                        String.valueOf((int) data.getFortuneDettes().getHabitationPrincipal()), 7))
                // MVVL
                .append(JadeStringUtil.fillWithZeroes(
                        String.valueOf((int) data.getFortuneDettes().getAutresFortunes()), 7))// MVVA
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getFortuneDettes().getDettesHyp()), 7))// MVSH
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getFortuneDettes().getAutresDettes()),
                        7))// MVSA
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getFortuneDettes().getFranchise()), 7))// MVFB
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getFortuneDettes().getPrisCompte()), 7));// MVAN_X

        // Depenses
        content.append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getLoyer()), 6))// MAMI_X
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getLoyerCompte()), 6))// MAMI
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getTaxeHome()), 6))// MAT1_X
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getTaxeHomeCompte()), 6))// MAT1
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getPersonnelles()), 6))// MAP1
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getPrimeMaladie()), 6))
                // MAK1
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getPrimeMaladieConjEnf()),
                        6))// MAK2
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getInteretHypothecaire()),
                        6))// MAHY_X
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getEntretienImmeubles()),
                        6))// MAUN_X
                .append(JadeStringUtil.fillWithZeroes(
                        String.valueOf((int) data.getDepense().getInteretFraisDeterminant()), 6))// MAHY
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getBesoinsVitaux()), 6))// MALE
                .append(JadeStringUtil.fillWithZeroes(String.valueOf((int) data.getDepense().getAutresDepenses()), 6));// MAUE

        // Frais maladie
        content.append(JadeStringUtil.fillWithZeroes(String.valueOf("0"), 6))// MADI
                .append(JadeStringUtil.fillWithZeroes(String.valueOf("0"), 6));// MAUK

        // Situation bénéficiaire
        String partEnfants = data.getBeneficiaire().getPartEnfants().toString();
        if (data.getBeneficiaire().getPartEnfants() > 9) {
            partEnfants = "9";
        }
        String date = PRDateFormater.convertDate_MMxAAAA_to_MMAA(data.getBeneficiaire().getDate());

        String debutDroit = PRDateFormater.convertDate_MMxAAAA_to_MMAA(data.getBeneficiaire().getDebutDroit());
        if (JadeStringUtil.isEmpty(debutDroit)) {
            debutDroit = "9999";
        }

        String anneeNaiss = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(data.getBeneficiaire().getAnneeNaiss());

        String anneeNaissConj = "0000";
        if (!JadeStringUtil.isBlankOrZero(data.getBeneficiaire().getNumAvsConj())) {
            anneeNaissConj = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(data.getBeneficiaire().getAnneeNaissConj());
            if (JadeStringUtil.isEmpty(anneeNaissConj)) {
                anneeNaissConj = "9999";
            }
        }

        content.append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getOrgane(), 2))// CSAK
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getCantonDomicile(), 2))// CSKT1
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getLieuDomicile(), 4))// CSOR1
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getGenreHabitation(), 1))// CSWO
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getCatRente(), 1))// CSRE1
                .append(JadeStringUtil.fillWithZeroes(partEnfants, 1))// CSKI
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getCatBeneficiaire(), 1))// CSBE
                .append(JadeStringUtil.fillWithZeroes(data.getBeneficiaire().getEtatCivil(), 2))// CSZI1
                .append(JadeStringUtil.fillWithZeroes(debutDroit, 4))// DSAN
                .append(JadeStringUtil.fillWithZeroes(date, 4))// DSER
                .append(JadeStringUtil.fillWithZeroes(anneeNaiss, 4))// DSJ1
                .append(data.getBeneficiaire().getSexe())// CSG1
                .append(data.getBeneficiaire().getOrigine())// CSH1
                .append(JadeStringUtil.fillWithZeroes(anneeNaissConj, 4))// DSJ2
                .append(data.getBeneficiaire().getSexeConj())// CSG2
                .append(data.getBeneficiaire().getOrigineConj());// CSH2
    }

    @Override
    public String genereateFileStatOFAS(String idExecutionProcess) throws StatistiquesOFASException {
        String content = createContent(idExecutionProcess);
        String paht = createFile(content, "statistiquesOFAS");
        return paht;
    }

    @Override
    public PlanCalculeDemandeDroitMembreFamilleSearch search(PlanCalculeDemandeDroitMembreFamilleSearch search)
            throws JadePersistenceException, StatistiquesOFASException {
        if (search == null) {
            throw new StatistiquesOFASException(
                    "Unable to search planCalculeDemandeDroitMembreFamille, the model passed is null!");
        }
        return (PlanCalculeDemandeDroitMembreFamilleSearch) JadePersistenceManager.search(search);
    }

    @Override
    public RenteApiIjSearch search(RenteApiIjSearch search) throws JadePersistenceException, StatistiquesOFASException {
        if (search == null) {
            throw new StatistiquesOFASException("Unable to search search, the model passed is null!");
        }
        return (RenteApiIjSearch) JadePersistenceManager.search(search);
    }

    private List<EntityLogsProperties<PCProcessStatistiquesOFASEnum>> searchEntity(String idExecutionProcess) {
        List<EntityLogsProperties<PCProcessStatistiquesOFASEnum>> list = null;
        try {
            list = JadeProcessServiceLocator.getJadeProcessEntityService().searchEntityLogsAndProperties(
                    idExecutionProcess);
        } catch (EntiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadeApplicationServiceNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadeProcessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }

}
