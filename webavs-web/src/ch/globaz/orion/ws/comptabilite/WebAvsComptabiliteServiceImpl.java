package ch.globaz.orion.ws.comptabilite;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.external.IntRole;
import globaz.osiris.print.itext.list.CAProcessImpressionExtraitCompteAnnexe;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.apache.commons.io.FileUtils;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.ws.service.UtilsService;

@WebService(endpointInterface = "ch.globaz.orion.ws.comptabilite.WebAvsComptabiliteService")
public class WebAvsComptabiliteServiceImpl implements WebAvsComptabiliteService {

    private static final String LANGUE_DEFAUT = "fr";

    @Override
    public List<ApercuCompteAnnexe> listerApercuCompteAnnexe(String numeroAffilie, String langue) {
        boolean wantExcludeCompteAnnexeAffiliePersonnel = false;
        try {
            wantExcludeCompteAnnexeAffiliePersonnel = EBProperties.ECL_EXCLURE_CA_AFFILIE_PERSONNEL.getBooleanValue();
        } catch (PropertiesException e) {
            throw new RuntimeException("The properties ECL_EXCLURE_CA_AFFILIE_PERSONNEL has not be found", e);
        }

        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            throw new IllegalArgumentException("numero affilie cannot be blank or zero");
        }

        if (JadeStringUtil.isBlankOrZero(langue)) {
            langue = LANGUE_DEFAUT;
        }

        List<ApercuCompteAnnexe> listeApercuCompteAnnexe = new ArrayList<ApercuCompteAnnexe>();

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // récupération des comptes annexes pour l'affilié
        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(session);
        compteAnnexeManager.setForIdExterneRole(numeroAffilie);
        try {
            compteAnnexeManager.find();
        } catch (Exception e) {
            JadeLogger.error("Unable to find compteAnnexe for affilie : " + numeroAffilie, e);
        }

        for (int i = 0; i < compteAnnexeManager.size(); i++) {
            CACompteAnnexe compteAnnexe = (CACompteAnnexe) compteAnnexeManager.get(i);
            String solde = compteAnnexe.getSolde();
            FWCurrency soldeCurrency = new FWCurrency(solde);
            ApercuCompteAnnexe apercuCompteAnnexe = new ApercuCompteAnnexe(compteAnnexe.getIdCompteAnnexe(),
                    compteAnnexe.getRole().getDescription(langue), soldeCurrency.toStringFormat());

            // si on veut exclure les comptes annexes avec le rôle "affilié personnel" on vérifie le type de rôle avant
            // d'ajouter
            if (wantExcludeCompteAnnexeAffiliePersonnel) {
                // si role est différent de "affilié personnel" on l'ajoute
                if (!IntRole.ROLE_AFFILIE_PERSONNEL.equals(compteAnnexe.getRole().getIdRole())) {
                    listeApercuCompteAnnexe.add(apercuCompteAnnexe);
                }
            } else {
                listeApercuCompteAnnexe.add(apercuCompteAnnexe);
            }

        }

        return listeApercuCompteAnnexe;
    }

    @Override
    public String genererExtraitCompteAnnexe(String idCompteAnnexe, String dateDebut, String dateFin, String langue) {
        CAProcessImpressionExtraitCompteAnnexe processImpressionExtraitCompteAnnexe = null;

        // si la date du jour est vide on met la date du jour
        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            Date dateDuJour = new Date();
            dateFin = dateDuJour.getSwissValue();
        }

        try {
            processImpressionExtraitCompteAnnexe = new CAProcessImpressionExtraitCompteAnnexe();
            processImpressionExtraitCompteAnnexe.setEbusinessMode(true);
            processImpressionExtraitCompteAnnexe.setForIdCompteAnnexe(idCompteAnnexe);
            processImpressionExtraitCompteAnnexe.setFromDate(dateDebut);
            processImpressionExtraitCompteAnnexe.setUntilDate(dateFin);
            processImpressionExtraitCompteAnnexe.setPrintLanguageFromScreen(langue.toUpperCase());
            processImpressionExtraitCompteAnnexe.executeProcess();
            return processImpressionExtraitCompteAnnexe.getDocLocation();
        } catch (Exception e) {
            JadeLogger.error("Unabled to generate file extraitCompte for idCompteAnnexe : " + idCompteAnnexe, e);
        }
        return processImpressionExtraitCompteAnnexe.getDocLocation();
    }

    @Override
    public byte[] downloadFile(String filepath) {
        byte[] byteFile = null;
        File docFile = new File(filepath);
        try {
            byteFile = (FileUtils.readFileToByteArray(docFile));
        } catch (IOException e) {
            JadeLogger.error("Unabled to download file : " + filepath, e);
        }

        return byteFile;
    }
}
