package ch.globaz.vulpecula.web.views.decompte;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.decompte.ImprimerDecomptesProcess;
import ch.globaz.vulpecula.documents.decompte.DocumentDecompteSpecialPrinter;
import ch.globaz.vulpecula.documents.rappels.DocumentTaxationOfficePrinter;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteReceptionEtat;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.externalservices.RequestFactory;
import ch.globaz.vulpecula.process.taxationoffice.ListeTOPrevisionnelleProcess;
import ch.globaz.vulpecula.services.PTAFServices;
import ch.globaz.vulpecula.web.gson.DecompteGSON;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

public class DecompteViewService {
    public DecompteGSON getDecompteById(final String idDecompte) {
        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(idDecompte);
        DecompteGSON decompteGSON = new DecompteGSON();
        if (decompte != null) {
            decompteGSON.idDecompte = decompte.getId();
            decompteGSON.employeur = decompte.getEmployeur().getDesignation1();
            decompteGSON.noDecompte = decompte.getNumeroDecompte().getValue();
            decompteGSON.descriptionDecompte = "Décompte de cotisation du " + decompte.getPeriode().getMoisDebut()
                    + " " + decompte.getPeriode().getAnneeDebut();
            decompteGSON.typeDecompte = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                    decompte.getType().getValue());
        }
        return decompteGSON;
    }

    public void callTGedmyProdis(String id) {
        // Pouvoir logger en cas de problème d'appel
        FWMemoryLog log = new FWMemoryLog();
        log.setSession(BSessionUtil.getSessionFromThreadContext());

        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(id);
        JsonObject json = new JsonObject();
        json.addProperty(PTConstants.GED_LOGIN_EXTERNE, BSessionUtil.getSessionFromThreadContext().getUserId());
        json.addProperty(PTConstants.GED_DOC_LABEL_SEARCH, id);
        json.addProperty(PTConstants.GED_TYPE, "MB");
        json.addProperty(PTConstants.GED_PARTNER_EXTERNE_ID, decompte.getEmployeur().getIdTiers());
        
        try {
            HostnameVerifier hostname = new HostnameVerifier() {
                
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    System.out.println("Warning: URL Host: "+RequestFactory.URL_GED_CONSULTATION+" vs. "
                            +session.getPeerHost());
                          return true;
                }
            };
            
            System.setProperty("javax.net.ssfl.trustStore", "jssecacerts.cert");
            
            HttpsURLConnection.setDefaultHostnameVerifier(hostname);
            URL url = new URL(RequestFactory.URL_GED_CONSULTATION);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "Application/json");
            
            OutputStream os = urlConn.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            
            if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JadeLogger.info(null, "L'envoie de la requête a réussi");
            }
            
            urlConn.disconnect();
        } catch (Exception e) {
            log.logMessage("Error while accessing webService for GED : " + e.getMessage(), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
        }

    }
    
    public DecompteReceptionEtat setDateReception(final String idDecompte, final String date) {
        return VulpeculaServiceLocator.getDecompteService().receptionner(idDecompte, date);
    }

    public void devalider(String idDecompte) {
        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(idDecompte);
        VulpeculaServiceLocator.getDecompteService().devalider(idDecompte);
        PTAFServices.supprimerPrestationsAF(decompte);
    }

    public void rectifier(String objetRectifier) {
        Gson gson = new Gson();
        RectifierGSON rectifierGSON = gson.fromJson(objetRectifier, RectifierGSON.class);

        Preconditions.checkArgument(!JadeNumericUtil.isEmptyOrZero(rectifierGSON.idDecompte));

        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(rectifierGSON.idDecompte);
        VulpeculaServiceLocator.getDecompteService().rectifierControleErrone(rectifierGSON.idDecompte,
                rectifierGSON.remarque);
        PTAFServices.genererPrestationsAFPeriodique(decompte.getEmployeur(), decompte.getPeriode(), decompte.getType());
    }

    public void imprimerTO(String toPrints) throws Exception {
        String[] elements = toPrints.split(PTConstants.JSON_STRING_REGEX_SPLIT);
        List<String> listeElementsWithRights = new ArrayList<String>();
        if (elements.length == 0) {
            throw new IllegalStateException("Aucun document sélectionné");
        }
        for (int i = 0; i < elements.length; i++) {
            TaxationOffice taxation = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findById(elements[i]);
            if (VulpeculaServiceLocator.getUsersService().hasRightAccesSecurity(
                    taxation.getEmployeur().getAccesSecurite())) {
                listeElementsWithRights.add(elements[i]);
            } else {
                throw new IllegalStateException("Vous n'avez pas le droit d'imprimer ce document");
            }
        }
        DocumentTaxationOfficePrinter to = new DocumentTaxationOfficePrinter(listeElementsWithRights);
        to.start();
    }

    public void imprimerDecompte(String toPrints) throws Exception {
        String[] elements = toPrints.split(PTConstants.JSON_STRING_REGEX_SPLIT);
        List<String> listeElementsWithRights = new ArrayList<String>();
        if (elements.length == 0) {
            throw new IllegalStateException("Aucun document sélectionné");
        }
        // On regarde que l'utilisateur possède les droits d'impression
        for (int i = 0; i < elements.length; i++) {
            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(elements[i]);
            if (VulpeculaServiceLocator.getUsersService().hasRightAccesSecurity(
                    decompte.getEmployeur().getAccesSecurite())) {
                listeElementsWithRights.add(elements[i]);
            } else {
                throw new IllegalStateException("Vous n'avez pas le droit d'imprimer ce document");
            }
        }
        ImprimerDecomptesProcess process = ImprimerDecomptesProcess.createWithIds(listeElementsWithRights);
        process.start();
    }

    public void imprimerDecompteSpecial(String idDecompte) throws Exception {
        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(idDecompte);
        if (!VulpeculaServiceLocator.getUsersService()
                .hasRightAccesSecurity(decompte.getEmployeur().getAccesSecurite())) {
            throw new IllegalStateException("Vous n'avez pas le droit d'imprimer ce document");
        }
        DocumentDecompteSpecialPrinter printer = new DocumentDecompteSpecialPrinter();
        printer.setIds(Arrays.asList(idDecompte));
        printer.start();
    }

    public void controler(String objetControle) {
        Gson gson = new Gson();
        ControleGSON controleGSON = gson.fromJson(objetControle, ControleGSON.class);
        VulpeculaServiceLocator.getDecompteService().controler(controleGSON.idDecompte, controleGSON.pasDeControle);
    }

    public List<String> findTOs(String imprimerSearchGSON) {
        Gson gson = new Gson();
        ImprimerSearchGSON search = gson.fromJson(imprimerSearchGSON, ImprimerSearchGSON.class);

        List<String> idsTaxation = new ArrayList<String>();
        EtatTaxation etatTaxation = null;
        if (!JadeStringUtil.isEmpty(search.etat)) {
            etatTaxation = EtatTaxation.fromValue(search.etat);
        }
        List<TaxationOffice> taxations = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findBy(
                search.idPassage, search.noAffilie, etatTaxation);
        for (TaxationOffice to : taxations) {
            idsTaxation.add(to.getId());
        }
        return idsTaxation;
    }

    public List<String> findDecomptes(String imprimerDecompteSearchGSON) {
        Gson gson = new Gson();
        ImprimerDecompteSearchGSON search = gson.fromJson(imprimerDecompteSearchGSON, ImprimerDecompteSearchGSON.class);

        List<String> idsDecompte = new ArrayList<String>();
        EtatDecompte etatDecompte = null;
        if (!JadeStringUtil.isEmpty(search.etat)) {
            etatDecompte = EtatDecompte.fromValue(search.etat);
        }
        List<Decompte> decomptes = VulpeculaRepositoryLocator.getDecompteRepository().findBy(search.idDecompte,
                search.noDecompte, search.noAffilie, search.idPassage, etatDecompte);
        for (Decompte decompte : decomptes) {
            idsDecompte.add(decompte.getId());
        }
        return idsDecompte;
    }

    public void genererListeTOPrevisionnelle(String nothing) throws Exception {
        ListeTOPrevisionnelleProcess process = new ListeTOPrevisionnelleProcess();
        process.setEmail(BSessionUtil.getSessionFromThreadContext().getUserEMail());
        BProcessLauncher.start(process);
    }

    private static final class ImprimerSearchGSON {
        String idPassage;
        String noAffilie;
        String etat;
    }

    private static final class ImprimerDecompteSearchGSON {
        String idDecompte;
        String noAffilie;
        String noDecompte;
        String idPassage;
        String etat;
    }

    private static final class ControleGSON {
        String idDecompte;
        boolean pasDeControle;
    }

    private static final class RectifierGSON {
        String idDecompte;
        String remarque;
    }
}
