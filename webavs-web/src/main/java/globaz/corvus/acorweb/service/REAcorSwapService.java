package globaz.corvus.acorweb.service;

import acor.ch.eahv_iv.xmlns.eahv_iv_2401_000501._1.Message;
import ch.globaz.common.ws.configuration.JacksonJsonProvider;
import globaz.corvus.acorweb.ws.token.REAcorTokenServiceImpl;
import globaz.prestation.acor.PRACORException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class REAcorSwapService {
    public REAcorSwapService() {
    }

    private static REAcorSwapService instance;

    public static REAcorSwapService getInstance() {
        if (instance == null)
            instance = new REAcorSwapService();

        return instance;
    }

    public String getSwap(Message message) throws PRACORException {
        URL url;
        HttpURLConnection con;
        String acorBaseUrl = REAcorTokenServiceImpl.loadAcorBaseUrl();
        String response = null;

        try {
            url = new URL(acorBaseUrl + "/xmlswap");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("Accept", "application/xml");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();

            JacksonJsonProvider.getInstance().writeValue(os, message);

            if (con.getInputStream() != null) {
                final InputStream inputStream = con.getInputStream();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                response=sb.toString();

            }
            con.disconnect();
        } catch (MalformedURLException e) {
            throw new PRACORException("Un problème est intervenu lors de la récupération de l'URL du webService " + acorBaseUrl, e);
        } catch (ProtocolException e) {
            throw new PRACORException("Un problème de protocole est intervenu lors de la connexion au webService ", e);
        } catch (IOException e) {
            throw new PRACORException("Un problème de connexion au webService ", e);
        }
        return response;
    }
}
