package ch.globaz.vulpecula.ws.client;

import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import ch.globaz.vulpecula.ws.bean.DecompteEbu;
import ch.globaz.vulpecula.ws.bean.DecomptePeriodiqueEbu;
import ch.globaz.vulpecula.ws.services.DecompteEbuService;

/**
 * Description de la classe
 * 
 * @since eBMS 1.0
 */
public class TestClient {
    private static final String STATUS_COMPTABILISE = "68012007";

    public static void main(String[] args) throws Exception {

        // Paramétrer l'URL
        URL url = new URL("http://localhost:9030/webavs/ws/decompteService?wsdl");
        URL url2 = new URL("file://d:/Temp/decompteService.wsdl");
        QName qnameDecompte = new QName("http://services.ws.vulpecula.globaz.ch/", "DecompteEbuServiceImplService");
        // QName qnameEmployeur = new QName("http://ws.vulpecula.globaz.ch/", "EmployeurServiceImplService");

        Service service = Service.create(url, qnameDecompte);

        // EmployeurService employeurService = service.getPort(EmployeurService.class);
        // System.out.println(employeurService.getIdEmployeur("000015.01-01"));

        DecompteEbuService decompteService = service.getPort(DecompteEbuService.class);

        List<DecomptePeriodiqueEbu> listDecompte = decompteService.listDecomptes("1", "201501", "201501",
                TestClient.STATUS_COMPTABILISE, false);

        for (DecompteEbu decompteEbu : listDecompte) {
            System.out.println(decompteEbu);
        }

    }
}
