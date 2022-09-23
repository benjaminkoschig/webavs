package ch.globaz.eform.businessimpl.services.sedex.generator;

public interface GFIdentifiantSedexGenerator {
    String generateMessageId();
    String generateReferenceMessageId();
    String generateBusinessProcessId();
    String generateBusinessReferenceId();
}
