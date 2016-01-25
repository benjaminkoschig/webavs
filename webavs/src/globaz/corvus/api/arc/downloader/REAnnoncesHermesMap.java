package globaz.corvus.api.arc.downloader;

import globaz.hermes.api.IHEOutputAnnonce;

public class REAnnoncesHermesMap implements Comparable<REAnnoncesHermesMap> {

    private final IHEOutputAnnonce annonce;
    private final String codeAnnonce;

    public REAnnoncesHermesMap(IHEOutputAnnonce annonce, String codeAnnonce) {
        super();
        this.annonce = annonce;
        this.codeAnnonce = codeAnnonce;
    }

    @Override
    public int compareTo(REAnnoncesHermesMap o) {
        return Integer.parseInt(codeAnnonce) - Integer.parseInt(o.codeAnnonce);
    }

    public IHEOutputAnnonce getAnnonce() {
        return annonce;
    }
}
