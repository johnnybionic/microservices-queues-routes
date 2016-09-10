package com.johnny.dispatcher.messaging;

import static com.johnny.dispatcher.messaging.DocumentResponseActionSaveAsFile.FILENAME_TEMPLATE;
import static com.johnny.dispatcher.messaging.DocumentResponseActionSaveAsFile.NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This version writes to the file system (the other version writes to a byte array).
 * Let's face it, at some point the actual write will need to be tested. :)
 * 
 * @author johnny
 *
 */
@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DocumentResponseActionSaveAsFileTest_2 {

	private static final String IDENTIFIER = "42";

	@Autowired
	private DocumentResponseActionSaveAsFile action;

	@Autowired
	private ObjectMapper mapper;
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private Long documentId;
	private String documentTitle;

	@Before
	public void setUp() throws Exception {
		// looks like this is need to create the actual folder
		folder.newFile();
		action.setOutputFolder(folder.getRoot().getAbsolutePath());
	}

	@Test
	public void thatCorrectFileNameIsCreatedAndWrittenTo() throws IOException {
		Message message = new Message("Can we meet today?", "bossman", "worker");
		String content = mapper.writeValueAsString(message);
		documentId = 7L;
		documentTitle = "Message from system";
		Document document = new Document(documentId, documentTitle, content);
		DocumentRequest documentRequest = new DocumentRequest(IDENTIFIER, document , null);
		
		action.perform(documentRequest);
		
		String expectedFileName = String.format(FILENAME_TEMPLATE, IDENTIFIER, 
				documentId != null ? documentId : NONE, 
				documentTitle != null ? documentTitle : NONE);

		Path path = Paths.get(folder.getRoot().getAbsolutePath(), expectedFileName);
		assertTrue(Files.exists(path));
		
		List<String> readAllLines = Files.readAllLines(path);
		assertNotNull(readAllLines);
		assertTrue(readAllLines.size() == 1);
		assertEquals(content, readAllLines.get(0));
	}

	@AllArgsConstructor
	@Data
	class Message {
		String message;
		String from;
		String to;
	}

}
