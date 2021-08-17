package globaz.corvus.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020Token;
import lombok.Data;

@Data
public class REAcor2020Token implements Acor2020Token {

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
