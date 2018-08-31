package ch.globaz.vulpecula.ws.services;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.ws.bean.QualificationEbu;

/**
 * Description de la classe
 *
 * @since eBMS 1.0
 */
@WebService
public interface QualificationEbuService {

    /**
     * Retourne les qualifications propres à la convention
     *
     * @return Liste de {@link Qualification}
     */
    @WebMethod
    @WebResult(name = "listQualification")
    List<QualificationEbu> findByConvention(@WebParam(name = "conventionCode") String conventionCode);

}
