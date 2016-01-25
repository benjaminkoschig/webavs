package ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire;

import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresListBase;

public class PensionsAlimentaires {

    public static DonneesFinancieresListBase<PensionAlimentaire> getPensionAlimentaireByType(
            DonneesFinancieresListBase<PensionAlimentaire> list, PensionAlimentaireType type) {
        DonneesFinancieresListBase<PensionAlimentaire> listReturn = new DonneesFinancieresListBase<PensionAlimentaire>();
        for (PensionAlimentaire pa : list.getList()) {
            if (pa.getType().equals(type)) {
                listReturn.add(pa);
            }
        }
        return listReturn;
    }
}
