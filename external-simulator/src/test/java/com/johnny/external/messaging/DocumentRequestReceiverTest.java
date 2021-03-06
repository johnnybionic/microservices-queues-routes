package com.johnny.external.messaging;

import static org.junit.Assert.assertEquals;

import com.johnny.external.DocumentFactory;
import com.johnny.external.ExternalSimulatorApplication;
import com.johnny.external.domain.DocumentRequest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("junit")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExternalSimulatorApplication.class)
public class DocumentRequestReceiverTest {

    @Autowired
    private DocumentRequestReceiver receiver;

    @Before
    public void setUp() throws Exception {
    }

    // this test is leaving something running
    @Test
    @Ignore
    public void testHappyPath() {

        final DocumentRequest document = DocumentFactory.getDocumentRequest();
        final String retVal = receiver.receiveDocumentRequest(document);
        assertEquals(DocumentRequestReceiver.ACKNOWLEDGED, retVal);
    }

}
