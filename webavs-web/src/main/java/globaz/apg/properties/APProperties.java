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

    PROPERTY_DROIT_ACM_MAT_DUREE_JOURS("droits.acm.maternite.dureejours",
            "D�fini le nombre de jours du ACM depuis le d�but du droit") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    },

    PROPERTY_DROIT_ACM2_MAT_DUREE_JOURS("droits.acm2.maternite.dureejours",
            "D�fini le nombre de jours du ACM2 commencant apr�s l'ACM normal") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidIntegerPropertyValue(propertyValue);
        }
    };

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
}
