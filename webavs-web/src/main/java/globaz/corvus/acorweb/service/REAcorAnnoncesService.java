package globaz.corvus.acorweb.service;

import acor.ch.admin.zas.rc.annonces.rente.pool.PoolMeldungZurZAS;
import acor.rentes.xsd.fcalcul.FCalcul;
import ch.globaz.common.ws.configuration.JacksonJsonProvider;
import globaz.corvus.acorweb.ws.token.REAcorTokenService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

@Slf4j
public class REAcorAnnoncesService {

    private static REAcorAnnoncesService instance;

    private REAcorAnnoncesService() {
    }

    public static REAcorAnnoncesService getInstance()
    {
        if (instance == null)
            instance = new REAcorAnnoncesService();

        return instance;
    }

    /**
     * Méthode permettant de récupérer les annonces en contactant le WebService ACOR "annonces".
     *
     * @param fCalcul le json fCalcul
     * @returns les annonces
     */
    public PoolMeldungZurZAS getAnnonces(FCalcul fCalcul) {
        URL url;
        HttpURLConnection con;
        String acorBaseUrl = REAcorTokenService.getAcorBaseUrl();
        PoolMeldungZurZAS annonces = null;

        try {
            url = new URL(acorBaseUrl + "/annonces");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            JacksonJsonProvider.getInstance().writeValue(os, fCalcul);
            if (con.getResponseCode() <= 400) {
                InputStream response = con.getInputStream();
                Scanner scanner = new Scanner(response);
                String responseBody = scanner.next();
                annonces = JacksonJsonProvider.getInstance().readValue(responseBody, PoolMeldungZurZAS.class);
            }
            con.disconnect();
        } catch (MalformedURLException e) {
            LOG.error("Un problème est intervenu lors de la récupération de l'URL du webService {} ", acorBaseUrl, e);
        } catch (ProtocolException e) {
            LOG.error("Un problème de protocole est intervenu lors de la connexion au webService ", e);
        } catch (IOException e) {
            LOG.error("Un problème de connexion au webService", e);
        }
        return annonces;
    }

}
