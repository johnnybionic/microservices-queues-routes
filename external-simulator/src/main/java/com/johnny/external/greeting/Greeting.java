package com.johnny.external.greeting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Greeting {

	private final long id;
	private final String content;

}
