package globaz.corvus.process.cron;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class REInfoLogAdaptationsRentes {

    private static int COUNT_CONTROL = 1000;
    private int count = 0;
    private String traitement;

    public REInfoLogAdaptationsRentes(String traitement) {
        this.traitement = traitement;
    }

    public void countLog() {
        count++;
        if(count % COUNT_CONTROL == 0) {
            LOG.info(traitement+" - Nombre d'enregistrement traité : "+count);
        }
    }
    public void finalLog() {
        LOG.info(traitement+" - Nombre d'enregistrement TOTAL traité : "+count);
    }
}
