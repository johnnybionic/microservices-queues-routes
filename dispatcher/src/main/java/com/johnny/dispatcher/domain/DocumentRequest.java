package com.johnny.dispatcher.domain;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a request sent to the system. It includes
 * a 'claim check', which is the identifier for  
 * the request. This must be returned, along with the requested document.
 * 
 * @author johnny
 *
 */
@Data
@AllArgsConstructor
// for Jackson (doesn't care that's it private ...)
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@EqualsAndHashCode
// to allow for XML as well as JSON responses
@XmlRootElement
public class DocumentRequest {

	String identifier;
	Document document;
	Map<String, String> attributes;
}
