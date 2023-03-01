package org.opensourceway.uvp.clients.provider.osv.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.opensourceway.uvp.enums.Ecosystem;
import org.opensourceway.uvp.utility.PurlUtil;

/**
 * Request body for querying vulns of a package.
 */
public class RequestBody {

    @JsonProperty("package")
    private Pkg pkg;

    /**
     * Package version. Optional.
     */
    private String version;

    /**
     * A token pointing the next page of vulns.
     */
    @JsonProperty("page_token")
    private String pageToken = "";

    public Pkg getPkg() {
        return pkg;
    }

    public void setPkg(Pkg pkg) {
        this.pkg = pkg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPageToken() {
        return pageToken;
    }

    public void setPageToken(String pageToken) {
        this.pageToken = pageToken;
    }

    public static class Pkg {
        /**
         * The name of a package, which may contain a namespace,
         * e.g., org.apache.logging.log4j:log4j-core for Maven ecosystem
         */
        private String name;

        /**
         * The ecosystem of a package.
         */
        private Ecosystem ecosystem;

        /**
         * Package URL of a package.
         */
        private String purl;

        public Pkg(String name, Ecosystem ecosystem, String purl) {
            this.name = name;
            this.ecosystem = ecosystem;
            this.purl = purl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Ecosystem getEcosystem() {
            return ecosystem;
        }

        public void setEcosystem(Ecosystem ecosystem) {
            this.ecosystem = ecosystem;
        }

        public String getPurl() {
            return purl;
        }

        public void setPurl(String purl) {
            this.purl = purl;
        }
    }

    public static RequestBody fromPurl(String purl) {
        var body = new RequestBody();
        body.setPkg(new RequestBody.Pkg(null, null, PurlUtil.canonicalize(purl)));
        return body;
    }
}
