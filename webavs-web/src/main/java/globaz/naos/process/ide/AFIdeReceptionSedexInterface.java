package globaz.naos.process.ide;

import java.util.List;
import globaz.naos.util.IDEDataBean;

interface AFIdeReceptionSedexInterface<T> {
    /**
     * doit retourner le full package ou trouver l'objet MESSAGE générée depuis la xsd pour le unmarshall
     * </br>
     * ex. : ch.admin.bfs.xmlns.bfs_5050_000101._1
     * 
     * @return full package description
     */
    String getPackageClass();

    /**
     * implémentation propre de découpage d'un message en 1 ou plusieurs annonces entrante (selon la structure propre de
     * chaque message)
     * 
     * @param message
     * @return retourne la liste des ideDataBean (objet "Domaine" d'une annonces IDE)
     */
    List<IDEDataBean> convertMessageToIdeDataBean(T message);
}