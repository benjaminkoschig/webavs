package globaz.aquila.print;

import globaz.osiris.external.IntTiers;

/**
 * Inteface Description : DocumentOnCopy Interface, définitions des méthodes pour les documents qui sont envoyés en
 * copie. Date de création: 17 décembre 04
 * 
 * @author kurkus
 */
public interface COIDocumentOnCopy {

    /**
     * Method setCopieAu. Ajoute au document le nom du tiers qui est en copie
     * 
     * @param property
     * @param additionalValue
     */
    public void setCopieAu(String property, String additionalValue);

    /**
     * Method setCorpsTexte. Ajoute au document le corps du texte
     * 
     * @param property
     * @param additionalValue
     */
    public void setCorpsTexte(String property, String additionalValue);

    /**
     * Method setDestinataire. Ajoute au document le destinataire
     * 
     * @param destinataire
     */
    public void setDestinataire(IntTiers destinataire);

    /**
     * Method setFormuleDestinataire. Ajoute au document le titre du destinataire
     * 
     * @param property
     * @param additionalValue
     */
    public void setFormuleDestinataire(String property, String additionalValue);

}
