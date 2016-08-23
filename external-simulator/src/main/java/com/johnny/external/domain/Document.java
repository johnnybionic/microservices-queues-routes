package com.johnny.external.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
// for Jackson (doesn't care that's it private ...)
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@EqualsAndHashCode
// to allow for XML as well as JSON responses
@XmlRootElement
public class Document implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private String content;
}
