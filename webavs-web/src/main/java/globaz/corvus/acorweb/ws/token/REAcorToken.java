package globaz.corvus.acorweb.ws.token;

import globaz.prestation.acor.web.ws.AcorToken;
import lombok.Data;

@Data
public class REAcorToken implements AcorToken {

    private String idDemande;
    private String idTiers;
    private String noAVSDemande;
    private String dateDemande;
    private String timeDemande;
    private String timeStampGedo;

    private String langue;
    private String email;
    private String userId;


    public boolean demGedoExist(){
        boolean exist = dateDemande != null && !dateDemande.equals("0");
        exist = exist && timeDemande != null && !timeDemande.equals("0");
        return exist;
    }

}
