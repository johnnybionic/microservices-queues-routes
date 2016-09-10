package com.johnny.external.domain;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a request from another system. It includes a 'claim check', which
 * is the other's service identifier for the request. This must be returned,
 * along with the requested document.
 * 
 * @author johnny
 *
 */
@Data
@AllArgsConstructor
// for Jackson (doesn't care that's it private ...)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
// to allow for XML as well as JSON responses
@XmlRootElement
public class DocumentRequest {

    private String identifier;
    private Document document;
    private Map<String, String> attributes;
}
