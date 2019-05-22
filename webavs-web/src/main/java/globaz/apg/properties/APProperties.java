package globaz.apg.properties;

import globaz.apg.application.APApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum APProperties implements IProperties {
    /**
     * Utilis� pour le caisse qui sont victimes d'une reprise de donn�es APG incompl�te et qui doivent re-g�n�rer les
     * annonces
     */
    AFFICHER_BOUTON_SIMULER_PAIEMENT_AVEC_BPID(
            "simuler.paiement.bpid",
            "Active l'affichage du bouton simuler pmt avec BPID dans l'�cran APG r�capitulatif. Affichage du bouton soumis � des droits !") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AC_PAR_ID("assurance.ac.paritaire.id", "D�fini l'id de l'assurance (-> affiliation) AC paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AVS_PAR_ID("assurance.avsai.paritaire.id", "D�fini l'id de l'assurance (-> affiliation) AVS paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AC_PER_ID("assurance.ac.personnelle.id", "D�fini l'id de l'assurance (-> affiliation) AC paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_AVS_PER_ID("assurance.avsai.personnelle.id", "D�fini l'id de l'assurance (-> affiliation) AVS paritaire") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_FNE_ID("assurance.fne.id", "D�fini l'id de l'assurance (-> affiliation) FNE") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_MECP_ID("assurance.mecp.id", "D�fini l'id de l'assurance (-> affiliation) MECP") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    ASSURANCE_PP_ID("assurance.pp.id", "D�fini l'id de l'assurance (-> affiliation) PP") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    /** Retourne nss vide pour docInfo GED **/
    BLANK_INDEX_GED_NSS_A_ZERO("blank.index.ged.nss.a.zero", "Renvois une chaine vide si propri�t� � TRUE") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    NUMERO_AFFILIE_POUR_LA_GED_FORCES_A_ZERO_SI_VIDE("numero.affilie.ged.zero.si.vide",
            "D�fini si un num�ro d'affili� vide doit �tre d�crit par des z�ro (si vrai) ou vide (si faux)") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    TYPE_DE_PRESTATION_ACM("type.de.prestation.acm",
            "D�fini le type de prestation acm ger�e par la caisse (ACM_ALFA, ACM_NE,NONE)") {
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
            "D�fini le nombre de jours du ACM depuis le d�but du droit") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    PRESTATION_ACM_2_ACTIF("prestation.maternite.acm2.actif",
            "D�fini des prestions maternit�e de type ACM 2 doivent �tre g�n�r�es") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },

    PRESTATION_ACM_2_NOMBRE_JOURS(
            "prestation.maternite.acm2.nombre.jours",
            "D�fini le nombre de jours des prestations maternit� ACM2. Les prestations ACM2 d�marrent � la fin des prestations maternit�e ACM1") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    PROPERTY_AFFICHER_TRAITE_PAR("amat.isAfficherDossierTraitePar.decision",
            "Permet d'afficher la notion de Dossier trait� par dans la d�cision") {
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
     * D�terminer si la valeur renseign�e dans la property est correcte
     * 
     * @param propertyValue
     *            La valeur � �valuer
     * @return <code>true</code> si la valeur de la propri�t� est correcte
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
    // # le montant journalier minimum � payer � l'assur� si le montant � verser est
    // # plus grand que le montant vers� par l'employeur
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
    // # D�termine si le libelle confidentiel doit �tre ajout� dans l'adresse du destinataire dans l'envoi des documents
    // documents.is.confidentiel=false
    //
    // #
    // # Nature des ordres de versement selon type de prestation...
    // #
    // # Valeurs possible :
    // # - NATURE_VERSEMENT_APG
    // # - NATURE_ASSURANCE_MATERNITE
    // # ( - NATURE_ASSURANCE_MATERNITE_CANTONALE non utilis�)
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
    // # Rubrique pour les �critures comptables de compensations...
    // #
    // # Valeurs possible :
    // # - RUBRIQUE_DE_LISSAGE
    // # - COMPENSATION_ALFA
    // #
    //
    // rubrique.compensation.standard=RUBRIQUE_DE_LISSAGE
    // rubrique.compensation.acm=RUBRIQUE_DE_LISSAGE
    //
    // # D�termine si une copie de decision AMAT doit �tre effectu�e � l'assur� si destinataire = employeur
    // documents.decision.amat.copie.assure=true
    //
    // # Propri�t� interne pour le traitement des cas de reprise ancien syst�me.
    // role.prestation.globaz=rPRGlobaz
    //
    // # Indique si le NIP doit appara�tre dans les documents
    // document.display.nip=false
    //
    // # Propri�t� indiquant le nom du service GED pour le domaine
    // domaine.nomService.ged=ALLOC

}
