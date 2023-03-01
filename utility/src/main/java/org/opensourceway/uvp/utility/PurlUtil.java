package org.opensourceway.uvp.utility;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import org.opensourceway.uvp.entity.Package;
import org.opensourceway.uvp.enums.Ecosystem;
import org.opensourceway.uvp.exception.InvalidPurlException;
import org.opensourceway.uvp.pojo.osv.OsvPackage;
import org.springframework.util.ObjectUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class PurlUtil {
    public static PackageURL newPurl(String purl) {
        try {
            return new PackageURL(encodePurl(purl));
        } catch (MalformedPackageURLException e) {
            throw new InvalidPurlException("Invalid purl: <%s>".formatted(purl), e);
        }
    }

    private static String encodePurl(String purl) {
        return URLEncoder.encode(purl, StandardCharsets.UTF_8)
                .replace("%3A", ":")
                .replace("%2F", "/")
                .replace("%40", "@")
                .replace("%3F", "?")
                .replace("%3D", "=")
                .replace("%26", "&")
                .replace("%23", "#");
    }

    public static PackageURL newPurl(String type, String namespace, String name, String version,
                                     TreeMap<String, String> qualifiers, String subpath) {
        try {
            return new PackageURL(type, namespace, name, version, qualifiers, subpath);
        } catch (MalformedPackageURLException e) {
            throw new InvalidPurlException(e);
        }
    }

    public static String canonicalize(PackageURL purl, boolean coordinatesOnly) {
        return (coordinatesOnly ? purl.getCoordinates() : purl.canonicalize())
                .replace("%20", "+")
                .replace("%2B", "+")
                .replace("%2A", "*");
    }

    public static String canonicalize(PackageURL purl) {
        return canonicalize(purl, true);
    }

    public static String canonicalize(String purl, boolean coordinatesOnly) {
        return Objects.isNull(purl) ? null : canonicalize(newPurl(purl), coordinatesOnly);
    }

    public static String canonicalize(String purl) {
        return Objects.isNull(purl) ? null : canonicalize(purl, true);
    }

    public static Package purlToPackage(String purl) {
        var pkg = new Package();
        PackageURL packageURL = newPurl(purl);
        pkg.setName(packageURL.getName());
        pkg.setEcosystem(Ecosystem.findByPurlType(packageURL.getType()));
        pkg.setVersion(packageURL.getVersion());
        pkg.setPurl(purl);
        return pkg;
    }

    public static OsvPackage purlToOsvPackage(String purl) {
        var osvPackage = new OsvPackage();
        var packageUrl = newPurl(purl);
        osvPackage.setPurl(newPurl(packageUrl.getType(), packageUrl.getNamespace(), packageUrl.getName(),
                null, null, null).getCoordinates());
        osvPackage.setName(packageUrl.getName());
        osvPackage.setEcosystem(Ecosystem.findByPurlType(packageUrl.getType()));
        return osvPackage;
    }

    public static void validate(String purl) {
        newPurl(purl);
    }

    public static void validate(Collection<String> purls) {
        List<String> invalidPurls = new ArrayList<>();
        purls.forEach(purl -> {
            try {
                newPurl(purl);
            } catch (InvalidPurlException e) {
                invalidPurls.add(purl);
            }
        });
        if (!ObjectUtils.isEmpty(invalidPurls)) {
            throw new InvalidPurlException("Invalid purls: %s".formatted(invalidPurls));
        }
    }
}
