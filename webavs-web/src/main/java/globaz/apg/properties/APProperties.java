package globaz.apg.properties;

import globaz.apg.application.APApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum APProperties implements IProperties {
    /**
     * Utilisé pour le caisse qui sont victimes d'une reprise de données APG incomplète et qui doivent re-générer les
     * annonces
     */
    AFFICHER_BOUTON_SIMULER_PAIEMENT_AVEC_BPID(
            "simuler.paiement.bpid",
            "Active l'affichage du bouton simuler pmt avec BPID dans l'écran APG récapitulatif. Affichage du bouton soumis à des droits !") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AC_PAR_ID("assurance.ac.paritaire.id", "Défini l'id de l'assurance (-> affiliation) AC paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AVS_PAR_ID("assurance.avsai.paritaire.id", "Défini l'id de l'assurance (-> affiliation) AVS paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AC_PER_ID("assurance.ac.personnelle.id", "Défini l'id de l'assurance (-> affiliation) AC paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AVS_PER_ID("assurance.avsai.personnelle.id", "Défini l'id de l'assurance (-> affiliation) AVS paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_FNE_ID("assurance.fne.id", "Défini l'id de l'assurance (-> affiliation) FNE") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_MECP_ID("assurance.mecp.id", "Défini l'id de l'assurance (-> affiliation) MECP") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_PP_ID("assurance.pp.id", "Défini l'id de l'assurance (-> affiliation) PP") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    /** Retourne nss vide pour docInfo GED **/
    BLANK_INDEX_GED_NSS_A_ZERO("blank.index.ged.nss.a.zero", "Renvois une chaine vide si propriété à TRUE") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    NUMERO_AFFILIE_POUR_LA_GED_FORCES_A_ZERO_SI_VIDE("numero.affilie.ged.zero.si.vide",
            "Défini si un numéro d'affilié vide doit être décrit par des zéro (si vrai) ou vide (si faux)") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    TYPE_DE_PRESTATION_ACM("type.de.prestation.acm",
            "Défini le type de prestation acm gerée par la caisse (ACM_ALFA, ACM_NE,NONE)") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            for (final APPropertyTypeDePrestationAcmValues e : APPropertyTypeDePrestationAcmValues.values()) {
                if (e.equals(propertyValue)) {
                    return true;
                }
            }
            return false;
        }
    },

    DROIT_ACM_MAT_DUREE_JOURS("droits.acm.maternite.dureejours",
            "Défini le nombre de jours du ACM depuis le début du droit") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    PRESTATION_ACM_2_ACTIF("prestation.maternite.acm2.actif",
            "Défini des prestions maternitée de type ACM 2 doivent être générées") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    PRESTATION_ACM_2_NOMBRE_JOURS(
            "prestation.maternite.acm2.nombre.jours",
            "Défini le nombre de jours des prestations maternité ACM2. Les prestations ACM2 démarrent à la fin des prestations maternitée ACM1") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    PROPERTY_AFFICHER_TRAITE_PAR("amat.isAfficherDossierTraitePar.decision",
            "Permet d'afficher la notion de Dossier traité par dans la décision") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }

    }

    ;

    private String description;
    private String propertyName;

    APProperties(final String propertyName, final String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return APApplication.DEFAULT_APPLICATION_APG;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

    public boolean isEqualToValue(final String value) throws PropertiesException {
        return getValue().equals(value);
    }

    /**
     * Déterminer si la valeur renseignée dans la property est correcte
     * 
     * @param propertyValue
     *            La valeur à évaluer
     * @return <code>true</code> si la valeur de la propriété est correcte
     */
    public abstract boolean isValidValue(final String propertyValue);

    // # Properties for Framework
    // applicationClassName=globaz.apg.application.APApplication
    // # --- Application information
    // applicationName=APG
    //
    // groupe.apg.gestionnaire=gApgUser
    // groupe.maternite.gestionnaire=gMaterniteUser
    //
    // droits.situation.professionnelle.departement.enabled=true
    // droits.maternite.dureejours=98
    // montant.minimum.prestation.maternite=200
    //
    // montant.minimum.paye.employeur.apg=0.00
    // montant.minimum.paye.employeur.mat=0.00
    //
    // # le montant journalier minimum à payer à l'assuré si le montant à verser est
    // # plus grand que le montant versé par l'employeur
    // montant.minimum.paye.assure=2.00
    //
    // clone.structure.definition.filename=clones.xml
    // clone.copie.droit.apg.id=prestation-APG-copie-droit-APG
    // clone.correction.droit.apg.id=prestation-APG-correction-droit-APG
    // clone.copie.droit.maternite.id=prestation-APG-copie-droit-MATERNITE
    //
    //
    //
    // isDroitMaterniteCantonale=false
    // droits.maternite.cantonale.dureejours=112
    // droits.acm.maternite.dureejours=112
    //
    // assurance.avsai.paritaire.id=10
    // assurance.avsai.personnelle.id=110
    // assurance.ac.paritaire.id=20
    // assurance.ac.personnelle.id=120
    // assurance.lfa.paritaire.id=40
    // assurance.lfa.personnelle.id=40
    // assurance.fad.paritaire.id=50
    // assurance.fad.personnelle.id=50
    //
    // # la factory a utiliser pour la generation des fichiers d'execution de ACOR
    // acor.apg.factory.class=globaz.apg.acor.adapter.plat.APAdapterPlatFactory
    //
    // isRecapitulatifDecompte=false
    //
    // isSentToGED=true
    // service.ged=AF
    //
    // tailleLotPourEnvoi=15
    //
    // # Détermine si le libelle confidentiel doit être ajouté dans l'adresse du destinataire dans l'envoi des documents
    // documents.is.confidentiel=false
    //
    // #
    // # Nature des ordres de versement selon type de prestation...
    // #
    // # Valeurs possible :
    // # - NATURE_VERSEMENT_APG
    // # - NATURE_ASSURANCE_MATERNITE
    // # ( - NATURE_ASSURANCE_MATERNITE_CANTONALE non utilisé)
    // # - NATURE_ALFA_ACM
    // #
    //
    // nature.versement.apg=NATURE_VERSEMENT_APG
    // nature.versement.acm.apg=NATURE_VERSEMENT_APG
    //
    // nature.versement.maternite=NATURE_ASSURANCE_MATERNITE
    // nature.versement.acm.maternite=NATURE_ASSURANCE_MATERNITE
    // nature.versement.lamat=NATURE_ASSURANCE_MATERNITE
    // #
    // # Rubrique pour les écritures comptables de compensations...
    // #
    // # Valeurs possible :
    // # - RUBRIQUE_DE_LISSAGE
    // # - COMPENSATION_ALFA
    // #
    //
    // rubrique.compensation.standard=RUBRIQUE_DE_LISSAGE
    // rubrique.compensation.acm=RUBRIQUE_DE_LISSAGE
    //
    // # Détermine si une copie de decision AMAT doit être effectuée à l'assuré si destinataire = employeur
    // documents.decision.amat.copie.assure=true
    //
    // # Propriété interne pour le traitement des cas de reprise ancien système.
    // role.prestation.globaz=rPRGlobaz
    //
    // # Indique si le NIP doit apparaître dans les documents
    // document.display.nip=false
    //
    // # Propriété indiquant le nom du service GED pour le domaine
    // domaine.nomService.ged=ALLOC

}
