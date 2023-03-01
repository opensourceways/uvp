package org.opensourceway.uvp.clients.provider.ossindex.request;

import java.io.Serializable;
import java.util.List;

public record RequestBody(List<String> coordinates) implements Serializable {
}
