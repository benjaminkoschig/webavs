package ch.globaz.naos.ree.domain.converter;

public interface Converter<Ti, To> {

    /**
     * Prend en param�tre un object de type 'business message' et le convertis en un type d'object serialisable par JaxB
     * Le type retourn� doit �tre une classe g�n�r�e par JaxB. {ch.admin.bfs.xmlns}
     * 
     * @see ch.admin.bfs.xmlns
     * @param businessMessage
     * @return
     */
    To convert(Ti businessMessage) throws REEBusinessException;
}
