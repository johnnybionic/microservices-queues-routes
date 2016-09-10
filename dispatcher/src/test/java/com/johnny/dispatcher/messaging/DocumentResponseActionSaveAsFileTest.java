package com.johnny.dispatcher.messaging;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This test follows a pattern I saw in an IBM sample. The test extends the unit
 * it is testing, in order to override method(s) - in this test, it's the method
 * that provides an {@link OutputStream}. 
 * 
 * This is prevent the unit from creating files, although there is probably nothing
 * wrong with that. 
 * 
 * One problem with this style of test is that autowiring does not work. 
 *  
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DocumentResponseActionSaveAsFileTest extends DocumentResponseActionSaveAsFile {
	
	private static final String IDENTIFIER = "42";
	
	@Autowired
	private ObjectMapper mapper;
	
	private OutputStream outputStream;
	private Long documentId;
	private String documentTitle;

	// there is no explicit unit under test - this is the unit
	@Before
	public void setUp() throws Exception {
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void thatCorrectFileNameIsCreatedAndWrittenTo() throws JsonProcessingException {
		Message message = new Message("Can we meet today?", "bossman", "worker");
		String content = mapper.writeValueAsString(message);
		documentId = 7L;
		documentTitle = "Message from system";
		Document document = new Document(documentId, documentTitle, content);
		DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, document , null);
		
		this.perform(documentRequest);
		
		String fileContents = outputStream.toString();
		assertEquals(content, fileContents);
				
	}

	@Test
	public void thatCorrectFileNameIsCreatedAndWrittenTo_NullDocument() throws JsonProcessingException {
		Document document = new Document(null, null, null);
		DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, document, null);
		documentId = null;
		documentTitle = null;
		
		this.perform(documentRequest);
		
		String fileContents = outputStream.toString();
		assertEquals(NO_CONTENT, fileContents);
	}

	@Test
	public void thatCorrectFileNameIsCreatedAndWrittenTo_NullIdAndTitle() throws JsonProcessingException {
		
		DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, null, null);
		documentId = null;
		documentTitle = null;
		
		this.perform(documentRequest);
		
		String fileContents = outputStream.toString();
		assertEquals(NO_CONTENT, fileContents);
	}

	
	@Override
	protected OutputStream getOutputStream(String name) {
		// ensure the name is correct
		String expectedFileName = String.format(FILENAME_TEMPLATE, IDENTIFIER, 
				documentId != null ? documentId : NONE, 
				documentTitle != null ? documentTitle : NONE);
		assertEquals(expectedFileName, name);
		
		return outputStream;
	}


	@AllArgsConstructor
	@Data
	class Message {
		String message;
		String from;
		String to;
	}
}
