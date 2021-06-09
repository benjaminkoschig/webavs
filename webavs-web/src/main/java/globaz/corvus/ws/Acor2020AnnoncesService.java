package globaz.corvus.ws;

import acor.ch.admin.zas.rc.annonces.rente.pool.PoolMeldungZurZAS;
import com.fasterxml.jackson.databind.ObjectMapper;
import globaz.corvus.acor2020.utils.Acor2020TokenService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class Acor2020AnnoncesService {

    private static Acor2020AnnoncesService instance;

    private Acor2020AnnoncesService() {
    }

    public static Acor2020AnnoncesService getInstance()
    {
        if (instance == null)
            instance = new Acor2020AnnoncesService();

        return instance;
    }

    /**
     * Méthode permettant de récupérer les annonces en contactant le WebService ACOR "annonces".
     *
     * @param fCalcul le json fCalcul
     * @returns les annonces
     */
    public PoolMeldungZurZAS getAnnonces(String fCalcul) {
        URL url;
        HttpURLConnection con;
        String acorBaseUrl = Acor2020TokenService.getAcorBaseUrl();
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
            byte[] input = fCalcul.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            if (con.getResponseCode() <= 400) {
                InputStream response = con.getInputStream();
                Scanner scanner = new Scanner(response);
                String responseBody = scanner.next();
                annonces =  new ObjectMapper().readValue(responseBody, PoolMeldungZurZAS.class);
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
