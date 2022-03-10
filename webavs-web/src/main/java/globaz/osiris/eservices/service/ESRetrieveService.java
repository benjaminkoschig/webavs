package globaz.osiris.eservices.service;

import ch.globaz.orion.ws.comptabilite.WebAvsComptabiliteServiceImpl;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.contentieux.CAExtraitCompteListViewBean;
import globaz.osiris.db.contentieux.CALigneExtraitCompte;
import globaz.osiris.eservices.dto.ESExtraitCompteDTO;
import globaz.osiris.eservices.dto.ESInfoFacturationDTO;
import globaz.osiris.eservices.exceptions.ESBadRequestException;
import globaz.osiris.eservices.exceptions.ESInternalException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Locale;

@Slf4j
public class ESRetrieveService {

    public ESRetrieveService() {
    }

    private WebAvsComptabiliteServiceImpl webAvsComptabiliteService = new WebAvsComptabiliteServiceImpl();
    private ESSearchService esSearchService = new ESSearchService();
    private ESCreationService esCreationService = new ESCreationService();

    /**
     * Récupération du document et encode en base64.
     *
     * @return le document encodé en base 64 ou une chaîne vide
     */
    private String encodeBase64(String pathFile) throws IOException {
        if (!JadeStringUtil.isEmpty(pathFile)) {
            byte[] inFileBytes = Files.readAllBytes(Paths.get(pathFile));
            return Base64.getEncoder().encodeToString(inFileBytes);
        } else {
            return "";
        }
    }

    public ESExtraitCompteDTO getExtraitCompte(ESExtraitCompteDTO dto, String token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();

            if (JadeStringUtil.isEmpty(dto.getLangue())) {
                dto.setLangue(JadeStringUtil.toUpperCase(session.getIdLangueISO().toUpperCase(Locale.ROOT)));
            }

            // Récupération du compte annexe
            CACompteAnnexeManager compteAnnexeManager = esSearchService.searchComptesAnnexes(dto.getAffiliateNumber(), dto.getRole(), session);

            // Génération de l'extrait de compte et récupération de son emplacement
            String docLocation = webAvsComptabiliteService.genererExtraitCompteAnnexe(dto.getOperation(), dto.getSelectionTris(), dto.getSelectionSections(), ((CACompteAnnexe) compteAnnexeManager.getFirstEntity()).getIdCompteAnnexe(), dto.getAffiliateNumber(), dto.getStartPeriod(), dto.getEndPeriod(), dto.getDocumentDate(), dto.getLangue());

            // Ajoute le document dans le dto de réponse
            dto.setDocument(encodeBase64(docLocation));

            return dto;

        } catch (ESInternalException e) {
            LOG.error("Une erreur interne s'est produite lors de la récupération de l'extrait de compte : ", e);
            throw e;
        } catch (ESBadRequestException e) {
            LOG.error("Une erreur de paramètre s'est produite lors de la récupération de l'extrait de compte : ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération de l'extrait de compte : ", e);
            throw new ESInternalException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    public ESInfoFacturationDTO getInfoFacturation(ESInfoFacturationDTO dto, String token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();

            if (JadeStringUtil.isEmpty(dto.getLangue())) {
                dto.setLangue(JadeStringUtil.toUpperCase(session.getIdLangueISO().toUpperCase(Locale.ROOT)));
            }

            // Récupération des compte annexes
            CACompteAnnexeManager compteAnnexeManager = esSearchService.searchComptesAnnexes(dto.getAffiliateNumber(), dto.getRole(), session);
            for (int i = 0; i < compteAnnexeManager.size(); i++) {
                CACompteAnnexe caCompteAnnexe = (CACompteAnnexe) compteAnnexeManager.get(i);

                // Récupération des sections
                CASectionManager sectionManager = esSearchService.searchSections(caCompteAnnexe, dto.getSelectionSections(), dto.getStartPeriod(), dto.getEndPeriod(), session);
                for (int j = 0; j < sectionManager.size(); j++) {
                    CASection caSection = (CASection) sectionManager.get(j);
                    ESInfoFacturationDTO.ESInfoFacturationSectionDTO section = esCreationService.createSection(caSection, dto.getLangue());

                    // Récupération des lignes d'extraits de comptes
                    CAExtraitCompteListViewBean ligneExtraitCompteManager = esSearchService.searchLignesExtraitComptes(caSection, dto.getSelectionTris(), dto.getSelectionSections(), dto.getOperation(), dto.getLangue(), session);
                    FWCurrency soldeCumule = new FWCurrency();
                    for (int k = 0; k < ligneExtraitCompteManager.size(); k++) {
                        CALigneExtraitCompte caLigneExtraitCompte = (CALigneExtraitCompte) ligneExtraitCompteManager.getLigneExtraitCompte().get(k);
                        ESInfoFacturationDTO.ESInfoFacturationLigneExtraitCompteDTO ligneExtraitCompte = esCreationService.createLigneExtraitCompte(caLigneExtraitCompte, soldeCumule);

                        // Ajoute la ligne d'extrait dans la section dans le dto de réponse
                        section.getLigneExtraitComptes().add(ligneExtraitCompte);
                    }

                    // Ajout la section dans le dto de réponse
                    dto.getSections().add(section);
                }
            }

            return dto;

        } catch (ESInternalException e) {
            LOG.error("Une erreur interne s'est produite lors de la récupération des informations de facturation : ", e);
            throw e;
        } catch (ESBadRequestException e) {
            LOG.error("Une erreur de paramètre s'est produite lors de la récupération des informations de facturation : ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération des informations de facturation : ", e);
            throw new ESInternalException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }
}