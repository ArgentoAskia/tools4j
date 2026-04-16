package cn.argento.askia.networks.miniFeign.supports.http;

import lombok.Getter;

@Getter
public enum HttpResponseCode {
    ;


    private static final HttpResponseCode[] VALUES;

    static {
        VALUES = values();
    }

    private final Integer code;
    private final String message;
    private final String localeMessage;
    private final Series series;

    HttpResponseCode(Integer code, String message, String localeMessage, Series series) {
        this.code = code;
        this.message = message;
        this.localeMessage = localeMessage;
        this.series = series;
    }
    /**
     * Return the {@code HttpStatus} enum constant with the specified numeric value.
     * @param statusCode the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static HttpResponseCode valueOf(int statusCode) {
        HttpResponseCode status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }

    /**
     * Resolve the given status code to an {@code HttpStatus}, if possible.
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the corresponding {@code HttpStatus}, or {@code null} if not found
     * @since 5.0
     */
    public static HttpResponseCode resolve(int statusCode) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (HttpResponseCode status : VALUES) {
            if (status.code == statusCode) {
                return status;
            }
        }
        return null;
    }


    /**
     * Enumeration of HTTP status series.
     * 元源代码复制自SpringMVC
     */
    public enum Series {

        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Series(int value) {
            this.value = value;
        }

        /**
         * Return the integer value of this status series. Ranges from 1 to 5.
         */
        public int value() {
            return this.value;
        }

        /**
         * Return the {@code Series} enum constant for the supplied status code.
         * @param statusCode the HTTP status code (potentially non-standard)
         * @return the {@code Series} enum constant for the supplied status code
         * @throws IllegalArgumentException if this enum has no corresponding constant
         */
        public static Series valueOf(int statusCode) {
            Series series = resolve(statusCode);
            if (series == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            }
            return series;
        }

        /**
         * Resolve the given status code to an {@code HttpStatus.Series}, if possible.
         * @param statusCode the HTTP status code (potentially non-standard)
         * @return the corresponding {@code Series}, or {@code null} if not found
         * @since 5.1.3
         */
        public static Series resolve(int statusCode) {
            int seriesCode = statusCode / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return series;
                }
            }
            return null;
        }
    }
}
