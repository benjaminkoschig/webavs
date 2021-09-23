package globaz.ij.properties;

import globaz.ij.application.IJApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum IJProperties implements IProperties {

    /** Retourne nss vide pour docInfo GED **/
    BLANK_INDEX_GED_NSS_A_ZERO("blank.index.ged.nss.a.zero", "Renvois une chaine vide si propri�t� � TRUE"),
    NUMERO_AFFILIE_POUR_LA_GED_FORCES_A_ZERO_SI_VIDE("numero.affilie.ged.zero.si.vide",
            "D�fini si un num�ro d'affili� vide doit �tre d�crit par des z�ro (si vrai) ou vide (si faux)"),

    /**
     * Le montant du revenu d�terminant maximum pour la r�vision 5 d�s 2016 : 407.00
     */
    MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_5_DES2016("montant.revenu.determinant.maximum.rev.5.des2016",
            "Le montant du revenu d�terminant maximum pour la r�vision 5 d�s 2016"),

    /**
     * Le montant maximum d'une petite IJ rev5 d�s 2016 : 122.10
     */
    MONTANT_PETITE_IJ_MAXIMUM_REV_5_DES2016("montant.petite.ij.maximum.rev.5.des2016",
            "Le montant maximum d'une petite IJ rev5 d�s 2016"),
    
    ACTIVER_ANNONCES_XML("isAnnoncesXML", "d�termine si les annonces sont g�n�r�es au format xml"),
    RACINE_NOM_FICHIER_OUTPUT_ZAS("racine.nom.fichier.centrale",
            "donne la ra�ine nom du fichier � envoyer � la centrale"),
    FTP_CENTRALE_PATH("centrale.url", "donne l'url de la centrale"),
    CENTRALE_TEST("centrale.test",
            "d�finit si nous sommes en mode test pour mettre la balise test dans le fichier output de la centrale"),
    ACOR_UTILISER_VERSION_WEB("acor.utiliser.version.web","Boolean, si true, utilisation de la version Web d'ACOR");

    private String description;
    private String propertyName;

    IJProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return IJApplication.DEFAULT_APPLICATION_IJ;
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

    // # Properties for Framework
    // applicationClassName=globaz.ij.application.IJApplication
    // # --- Application information
    // applicationName=IJ
    //
    // groupe.ij.gestionnaire=gIjaiUser
    //
    // dateDebut4emeRevision=01.01.2004
    //
    // # le salaire mensuelle qui donne droit a l'allocation ij maximum (la valeur exacte est 8192 par mois)
    // salaire.allocationmax=10000.00
    //
    // assurance.avsai.paritaire.id=10
    // assurance.avsai.personnelle.id=110
    // assurance.ac.paritaire.id=20
    // assurance.ac.personnelle.id=120
    // assurance.lfa.paritaire.id=40
    // assurance.lfa.personnelle.id=40
    // assurance.fad.paritaire.id=
    // assurance.fad.personnelle.id=
    //
    //
    // clone.ij.definition=clonesIJ.xml
    // clone.ij.grande.correction=IJGrandeIJ-correction
    // clone.ij.petite.correction=IJPetiteIJ-correction
    // clone.ij.ait.correction=IJAitIJ-correction
    // clone.ij.aa.correction=IJAaIJ-correction
    // clone.ij.grande.copie=IJGrandeIJ-copie
    // clone.ij.petite.copie=IJPetiteIJ-copie
    // clone.ij.ait.copie=IJAitIJ-copie
    // clone.ij.aa.copie=IJAaIJ-copie
    // clone.ij.baseindemnisation.correction=IJBaseIndemnisation-correction
    // clone.ij.prestations.copie=IJPrestation-copie
    //
    // acor.ij.factory.class=globaz.ij.acor.adapter.plat.IJAdapterPlatFactory
    //
    // isRecapitulatifDecompte=false
    //
    // isSentToGED=false
    //
    // noAffilieParDefaut=999.1003
    //
    // genrePrestationRenteEnCoursAcor=50
    //
    // tailleLotPourEnvoi=15
    //
    // # D�termine si le libelle confidentiel doit �tre ajout� dans l'adresse du destinataire dans l'envoi des documents
    // documents.is.confidentiel=false
    //
    // # Valeur plafonn�e d'une indemnit� (4eme rev.)
    // montant.indemnite.plafonnee.rev.4=235.00
    //
    // # Valeur plafonn�e d'une indemnit� (4eme rev.) d�s 2008
    // montant.indemnite.plafonnee.rev.4.des2008=277.00
    //
    // # Valeur minimum garanti d'une indemnit� (4eme rev.)
    // montant.indemnite.minimum.garanti.rev.4=88.00
    //
    // # Valeur minimum garanti d'une indemnit� (4eme rev.) d�s 2008
    // montant.indemnite.minimum.garanti.rev.4.des2008=104.00
    //
    // # Valeur maximum garanti pour une petite IJ
    // montant.petite.ij.maximum=88.00
    //
    // # Valeur maximum garanti pour une petite IJ (4eme rev.) d�s 2008
    // montant.petite.ij.maximum.rev.4.des2008=104.00
    //
    // # Valeur maximum garanti pour une petite IJ (5eme rev.) d�s 2008
    // montant.petite.ij.maximum.rev.5.des2008=103.80
    //
    // # Valeur minimum du revenu d�terminant (4eme rev.)
    // montant.revenu.determinant.minimum.rev.4=103.00
    //
    // # Valeur minimum du revenu d�terminant (4eme rev.) d�s 2008
    // montant.revenu.determinant.minimum.rev.4.des2008=122.00
    //
    // # Valeur maximum du revenu d�terminant (4eme rev.)
    // montant.revenu.determinant.maximum.rev.4=293.00
    //
    // # Valeur maximum du revenu d�terminant (4eme rev.) d�s 2008
    // montant.revenu.determinant.maximum.rev.4.des2008=346.00
    //
    // # D�termine si la caisse poss�de une ent�te sp�cifique
    // hasSpecificHeader=true
    //
    // noOfficeAI=350
    //
    // # si 'true', l'option "avec d�cision" du 1er �cran de saisie des prononc�s est coch�e par d�faut
    // prononce.avec.decision=false
    //
    // # Propri�t� interne pour le traitement des cas de reprise ancien syst�me.
    // role.prestation.globaz=rPRGlobaz
    //
    // # Valeur maximum du revenu d�terminant (5eme rev.)
    // montant.revenu.determinant.maximum.rev.5=346.00
    //
    // # D�termine si la caisse est priv��
    // isCaisseCantonale=true
    //
    // # Indique si le NIP doit appara�tre dans les documents
    // document.display.nip=false
}
