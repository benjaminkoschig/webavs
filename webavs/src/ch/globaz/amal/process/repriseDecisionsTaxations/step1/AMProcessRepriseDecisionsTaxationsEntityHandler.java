package ch.globaz.amal.process.repriseDecisionsTaxations.step1;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.io.StringReader;
import java.util.Map;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import ch.globaz.amal.business.constantes.IAMProcess;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;

public class AMProcessRepriseDecisionsTaxationsEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {
    private String id = null;
    private String idJob = null;
    private Unmarshaller unmarshaller = null;

    public AMProcessRepriseDecisionsTaxationsEntityHandler(Unmarshaller _unmarshaller, String _idJob) {
        unmarshaller = _unmarshaller;
        idJob = _idJob;
    }

    private void addError(Exception e) throws JadeNoBusinessLogSessionError {
        this.addError(e, null);
    }

    private void addError(Exception e, String[] param) throws JadeNoBusinessLogSessionError {

        JadeThread.logError("", (e.getMessage() != null) ? e.getMessage() : e.toString(), param);
        if (e.getCause() != null) {
            String cause = "<br />" + e.getCause().toString();
            JadeThread.logError("", cause);
        }

    }

    private String getPersonneObjectInfos(String noContribuable, ch.globaz.amal.xmlns.am_0001._1.Personne personne) {
        String info = "";
        info += "No contribuable : " + noContribuable + ";<br/>";
        info += "Nom, prénom : " + personne.getNom() + " " + personne.getPrenom() + "(" + personne.getNip() + ");<br/>";

        return info;
    }

    private String getPersonneObjectInfos(String noContribuable, ch.globaz.amal.xmlns.am_0002._1.Personne personne) {
        String info = "";
        info += "No contribuable : " + noContribuable + ";<br/>";
        info += "Nom, prénom : " + personne.getNom() + " " + personne.getPrenom() + "(" + personne.getNip() + ");<br/>";

        return info;
    }

    private String getTaxationObjectInfos(String noContribuable, ch.globaz.amal.xmlns.am_0001._1.Taxation taxation) {
        String info = "";
        info += "No contribuable : " + noContribuable + ";<br/>";
        info += "Année de taxation : " + taxation.getPeriode().toString() + ";<br/>";

        return info;
    }

    private String getTaxationObjectInfos(String noContribuable, ch.globaz.amal.xmlns.am_0002._1.Taxation taxation) {
        String info = "";
        info += "No contribuable : " + noContribuable + ";<br/>";
        info += "Année de taxation : " + taxation.getPeriode().toString() + ";<br/>";

        return info;
    }

    @Override
    public void run() {
        try {
            // -----------------------------------------------------------------------------------------
            // Lecture de l'entité
            // -----------------------------------------------------------------------------------------
            SimpleUploadFichierReprise uploadFichierReprise = new SimpleUploadFichierReprise();
            uploadFichierReprise = AmalServiceLocator.getSimpleUploadFichierService().read(id);

            SimpleUploadFichierRepriseSearch allSimpleUploadFichierRepriseSearch = new SimpleUploadFichierRepriseSearch();
            allSimpleUploadFichierRepriseSearch.setForNoContribuable(uploadFichierReprise.getNoContribuable());
            allSimpleUploadFichierRepriseSearch.setForIdJob("0");
            allSimpleUploadFichierRepriseSearch = AmalServiceLocator.getSimpleUploadFichierService().search(
                    allSimpleUploadFichierRepriseSearch);

            for (JadeAbstractModel model : allSimpleUploadFichierRepriseSearch.getSearchResults()) {
                SimpleUploadFichierReprise membre = (SimpleUploadFichierReprise) model;
                Source source = new StreamSource(new StringReader(membre.getXmlLignes()));
                if (IAMProcess.TYPE_REPRISE_CONTRIBUABLE.equals(membre.getTypeReprise())) {
                    ch.globaz.amal.xmlns.am_0001._1.Contribuables cs_contri = (ch.globaz.amal.xmlns.am_0001._1.Contribuables) unmarshaller
                            .unmarshal(source);
                    validateContribuable(cs_contri);
                } else if (IAMProcess.TYPE_REPRISE_PERSONNE_CHARGE.equals(membre.getTypeReprise())) {
                    ch.globaz.amal.xmlns.am_0002._1.Contribuables cs_persCharge = (ch.globaz.amal.xmlns.am_0002._1.Contribuables) unmarshaller
                            .unmarshal(source);
                    validatePersonneCharge(cs_persCharge);
                } else {
                    throw new Exception("Type de reprise inexistant ==> typeReprise = " + membre.getTypeReprise());
                }

                membre.setIdJob(idJob);
                membre = AmalServiceLocator.getSimpleUploadFichierService().update(membre);
            }
        } catch (Exception e) {
            this.addError(e);
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        id = entity.getIdRef();
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        // TODO Auto-generated method stub

    }

    /**
     * Méthode de validation du contribuable
     * 
     * @param contribuablesToCheck
     * @throws Exception
     */
    private void validateContribuable(ch.globaz.amal.xmlns.am_0001._1.Contribuables contribuablesToCheck)
            throws Exception {
        boolean contribuablePrincipalExist = false;
        for (ch.globaz.amal.xmlns.am_0001._1.Contribuable contribuable : contribuablesToCheck.getContribuable()) {
            if (JadeStringUtil.isBlank(contribuable.getNdc())) {
                JadeThread.logError("Validation xsd contribuable", "Le numéro de contribuable ne peut pas être vide !");
            }
            for (ch.globaz.amal.xmlns.am_0001._1.Personne personne : contribuable.getPersonne()) {
                // -----------------------------------------------------------------------------------------
                // Contrôle des champs pour le contribuable principal
                // -----------------------------------------------------------------------------------------
                if (ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL.equals(personne.getType())) {
                    contribuablePrincipalExist = true;

                    boolean nnssEmpty = false;
                    boolean nomEmpty = false;
                    boolean prenomEmpty = false;
                    boolean dateNaissanceEmpty = false;

                    if (JadeStringUtil.isBlankOrZero(personne.getNavs13().toString())) {
                        nnssEmpty = true;
                    }
                    if (JadeStringUtil.isBlank(personne.getNom())) {
                        nomEmpty = true;
                    }
                    if (JadeStringUtil.isBlank(personne.getPrenom())) {
                        prenomEmpty = true;
                    }
                    if (JadeStringUtil.isBlank(personne.getDateNaiss())) {
                        dateNaissanceEmpty = true;
                    }

                    if (nnssEmpty && (nomEmpty || prenomEmpty || dateNaissanceEmpty)) {
                        JadeThread
                                .logError(this.getPersonneObjectInfos(contribuable.getNdc(), personne),
                                        "Le contribuable principal doit posséder au moins un NNSS ou un nom, prénom et date de naissance !");
                    }
                }

                // -----------------------------------------------------------------------------------------
                // Contrôle des champs pour le conjoint
                // -----------------------------------------------------------------------------------------
                if (ch.globaz.amal.xmlns.am_0001._1.TypePersonne.CONJOINT.equals(personne.getType())) {
                    contribuablePrincipalExist = true;

                    boolean nnssEmpty = false;
                    boolean nomEmpty = false;
                    boolean prenomEmpty = false;
                    boolean dateNaissanceEmpty = false;

                    if (JadeStringUtil.isBlankOrZero(personne.getNavs13().toString())) {
                        nnssEmpty = true;
                    }
                    if (JadeStringUtil.isBlank(personne.getNom())) {
                        nomEmpty = true;
                    }
                    if (JadeStringUtil.isBlank(personne.getPrenom())) {
                        prenomEmpty = true;
                    }
                    if (JadeStringUtil.isBlank(personne.getDateNaiss())) {
                        dateNaissanceEmpty = true;
                    }

                    if (nnssEmpty && (nomEmpty || prenomEmpty || dateNaissanceEmpty)) {
                        JadeThread.logError(this.getPersonneObjectInfos(contribuable.getNdc(), personne),
                                "Le conjoint doit posséder au moins un NNSS ou un nom, prénom et date de naissance !");
                    }
                }
                // -----------------------------------------------------------------------------------------
                // Contrôle des champs généraux
                // -----------------------------------------------------------------------------------------
                if (!JadeStringUtil.isBlank(personne.getDateNaiss())
                        && !JadeDateUtil.isGlobazDate(personne.getDateNaiss())) {
                    JadeThread.logError(this.getPersonneObjectInfos(contribuable.getNdc(), personne),
                            "La date de naissance : " + personne.getDateNaiss() + " n'est pas valide !");
                }
                if (!JadeStringUtil.isBlank(personne.getDateDeces())
                        && !JadeDateUtil.isGlobazDate(personne.getDateDeces())) {
                    JadeThread.logError(this.getPersonneObjectInfos(contribuable.getNdc(), personne),
                            "La date de décès : " + personne.getDateDeces() + " n'est pas valide !");
                }
            }
            if (!contribuablePrincipalExist) {
                JadeThread.logError("Validation xsd contribuable", "Le contribuable principal est obligatoire !");
            }

            // -----------------------------------------------------------------------------------------
            // Contrôle des champs taxations
            // -----------------------------------------------------------------------------------------
            for (ch.globaz.amal.xmlns.am_0001._1.Taxation taxation : contribuable.getTaxations().getTaxation()) {
                if (!JadeStringUtil.isBlank(taxation.getDateDec()) && !JadeDateUtil.isGlobazDate(taxation.getDateDec())) {
                    JadeThread.logError(this.getTaxationObjectInfos(contribuable.getNdc(), taxation),
                            "La date de taxation : " + taxation.getDateDec() + " n'est pas valide !");
                }
                if (!JadeStringUtil.isBlank(taxation.getDepartCommuneDate())
                        && !JadeDateUtil.isGlobazDate(taxation.getDepartCommuneDate())) {
                    JadeThread.logError(this.getTaxationObjectInfos(contribuable.getNdc(), taxation),
                            "départ de la commune : " + taxation.getDepartCommuneDate() + " n'est pas valide !");
                }
            }
        }
    }

    /**
     * Méthode de validation des personnes à charge
     * 
     * @param personneToCheck
     * @throws Exception
     */
    private void validatePersonneCharge(ch.globaz.amal.xmlns.am_0002._1.Contribuables personneToCheck) throws Exception {
        // -----------------------------------------------------------------------------------------
        // Contrôle des champs <personne> et <taxation>
        // -----------------------------------------------------------------------------------------
        for (ch.globaz.amal.xmlns.am_0002._1.Contribuable personneACharge : personneToCheck.getContribuable()) {
            if (JadeStringUtil.isBlank(personneACharge.getNdc())) {
                JadeThread.logError("Validation xsd personne à charge",
                        "Le numéro de contribuable ne peut pas être vide !");
            }
            for (ch.globaz.amal.xmlns.am_0002._1.Taxation taxation : personneACharge.getTaxations().getTaxation()) {
                if (JadeStringUtil.isBlankOrZero(taxation.getPeriode().toString())) {
                    JadeThread.logError(this.getTaxationObjectInfos(personneACharge.getNdc(), taxation),
                            "Le période doit être renseignée !");
                }
                for (ch.globaz.amal.xmlns.am_0002._1.Personne personne : taxation.getPersonne()) {
                    if (!"0".equals(personne.getTauxDeductIc()) && !"50".equals(personne.getTauxDeductIc())
                            && !"100".equals(personne.getTauxDeductIc())) {
                        JadeThread.logError(this.getPersonneObjectInfos(personneACharge.getNdc(), personne),
                                "Le champs tauxDeductIc doit être parmi ces valeurs : 0, 50 ou 100%");
                    }
                    if (!"0".equals(personne.getTauxDeductIfd()) && !"50".equals(personne.getTauxDeductIfd())
                            && !"100".equals(personne.getTauxDeductIfd())) {
                        JadeThread.logError("Validation xsd personne à charge",
                                "Le champs tauxDeductIfd doit être parmi ces valeurs : 0, 50 ou 100%");
                    }
                }
            }
        }
    }
}
