package ch.globaz.orion.ws.affiliation;

import globaz.jade.client.util.JadeStringUtil;

public class AffiliationAdresse {
    String rue;
    String numero;
    String casePostale;
    String npa;
    String localite;
    String attentionDe;
    String remplacement1;
    String remplacement2;
    String complement;

    public AffiliationAdresse() {
        // need for jaxws
    }

    public AffiliationAdresse(String rue, String numero, String casePostale, String npa, String localite,
            String attentionDe, String remplacement1, String remplacement2, String complement) {
        super();
        this.rue = rue;
        this.numero = numero;
        this.casePostale = casePostale;
        this.npa = npa;
        this.localite = localite;
        this.attentionDe = attentionDe;
        this.remplacement1 = remplacement1;
        this.remplacement2 = remplacement2;
        this.complement = complement;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCasePostale() {
        return casePostale;
    }

    public void setCasePostale(String casePostale) {
        this.casePostale = casePostale;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getAdresseFormatee(String raisonSociale) {
        StringBuilder adresse = new StringBuilder();

        // pour les adresses transmises on réaffiche la raison sociale
        if (!JadeStringUtil.isEmpty(raisonSociale)) {
            adresse.append(raisonSociale);
            adresse.append("\n");
        }

        // si un complément est présent on l'affiche
        if (!JadeStringUtil.isEmpty(complement)) {
            adresse.append(complement);
            adresse.append("\n");
        } else {
            // remplacer par 1
            if (!JadeStringUtil.isEmpty(getRemplacement1())) {
                adresse.append(getRemplacement1());
                adresse.append("\n");
            }

            // remplacer par 2
            if (!JadeStringUtil.isEmpty(getRemplacement2())) {
                adresse.append(getRemplacement2());
                adresse.append("\n");
            }

            // à l'attention de
            if (!JadeStringUtil.isEmpty(getAttentionDe())) {
                adresse.append(getAttentionDe());
                adresse.append("\n");
            }
        }

        // rue et numéro
        boolean carriageReturnAfterRueNumero = false;
        if (!JadeStringUtil.isEmpty(getRue())) {
            adresse.append(getRue());
            carriageReturnAfterRueNumero = true;
        }
        if (!JadeStringUtil.isEmpty(getNumero())) {
            adresse.append(" ");
            adresse.append(getNumero());
            carriageReturnAfterRueNumero = true;
        }
        if (carriageReturnAfterRueNumero) {
            adresse.append("\n");
        }

        // case postale
        if (getCasePostale() != null && !JadeStringUtil.isEmpty(getCasePostale().trim())) {
            adresse.append(getCasePostale());
            adresse.append("\n");
        }

        // npa et localité
        if (!JadeStringUtil.isEmpty(getNpa())) {
            adresse.append(getNpa());
            adresse.append(" ");
        }
        if (!JadeStringUtil.isEmpty(getLocalite())) {
            adresse.append(getLocalite());
        }

        return adresse.toString();
    }

    public String getAttentionDe() {
        return attentionDe;
    }

    public void setAttentionDe(String attentionDe) {
        this.attentionDe = attentionDe;
    }

    public String getRemplacement1() {
        return remplacement1;
    }

    public void setRemplacement1(String remplacement1) {
        this.remplacement1 = remplacement1;
    }

    public String getRemplacement2() {
        return remplacement2;
    }

    public void setRemplacement2(String remplacement2) {
        this.remplacement2 = remplacement2;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }
}
