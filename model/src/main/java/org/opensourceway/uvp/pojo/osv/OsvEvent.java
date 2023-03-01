package org.opensourceway.uvp.pojo.osv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.opensourceway.uvp.enums.EventType;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsvEvent extends HashMap<EventType, String> {}
