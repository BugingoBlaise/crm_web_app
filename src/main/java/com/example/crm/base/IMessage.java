package com.example.crm.base;

/**
 * The Interface IMessage.
 *
 * @author Blaise Mugisha
 * @version 1.0
 * @version 2.0
 */

public interface IMessage {
    /** The constant INFORMATION_NOT_FOUND.*/
    int INFORMATION_NOT_FOUND = 1000;

    /** The constant INFORMATION_FOUND. */
    int INFORMATION_FOUND = 1001;

    /** The constant INFORMATION_NOT_SAVED. */
    int INFORMATION_NOT_SAVED = 1002;

    /** The constant INFORMATION_SAVED. */
    int INFORMATION_SAVED = 1003;

    /** The constant INFORMATION_UPDATED. */
    int INFORMATION_UPDATED = 1004;

    /** The constant INFORMATION_NOT_UPDATED. */
    int INFORMATION_NOT_UPDATED = 1005;
    int INVALID_INPUT=1006;
    int DUPLICATE_PRODUCT_TYPES=1007;
}
