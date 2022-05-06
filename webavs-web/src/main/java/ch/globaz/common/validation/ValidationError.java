package ch.globaz.common.validation;

import globaz.globall.db.BSession;

public enum ValidationError {
    MANDATORY,
    EMPTY,
    MISSING,
    MALFORMED,
    ILLEGAL_VALUE,
    INTERNAL_ERROR,
    ALREADY_EXIST
}
