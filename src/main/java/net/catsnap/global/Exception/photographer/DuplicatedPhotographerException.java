package net.catsnap.global.Exception.photographer;

import net.catsnap.global.Exception.BusinessException;

public class DuplicatedPhotographerException extends BusinessException {

    public DuplicatedPhotographerException(String message) {
        super(message);
    }

    public DuplicatedPhotographerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedPhotographerException(Throwable cause) {
        super(cause);
    }
}
