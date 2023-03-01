package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Description(String lang, String value) {
    public enum Lang {
        EN("en");

        private final String lang;

        Lang(String lang) {
            this.lang = lang;
        }

        public String getLang() {
            return lang;
        }
    }
}
