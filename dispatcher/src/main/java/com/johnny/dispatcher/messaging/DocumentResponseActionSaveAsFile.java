package com.johnny.dispatcher.messaging;

import com.johnny.dispatcher.domain.Document;
import com.johnny.dispatcher.domain.DocumentRequest;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Saves the document to a file. Uses the identifier as the filename.
 * 
 * @author johnny
 *
 */
@Slf4j
@Component
public class DocumentResponseActionSaveAsFile implements DocumentResponseAction {

    static final String FILENAME_TEMPLATE = "COR[%s] ID[%s] '%s'.txt";
    static final String NO_CONTENT = "(no content received)";
    static final String NONE = "none";

    @Setter
    @Getter
    @Value("${document.file.output.folder}")
    private String outputFolder;

    @Override
    public void perform(final DocumentRequest documentRequest) {

        final String name = getOutputName(documentRequest);
        final OutputStream outputStream = getOutputStream(name);
        if (outputStream != null) {
            final Document document = documentRequest.getDocument();

            try {
                if (document != null && document.getContent() != null) {
                    outputStream.write(document.getContent().getBytes());
                }
                else {
                    outputStream.write(NO_CONTENT.getBytes());
                }

                outputStream.flush();
                outputStream.close();

            }
            catch (final IOException exception) {
                log.error(exception.getMessage());
            }
        }
    }

    /**
     * Generate the output filename.
     * 
     * @param documentRequest the response.
     * @return the filename
     */
    private String getOutputName(final DocumentRequest documentRequest) {
        String title = NONE;
        String id = NONE;

        final Document document = documentRequest.getDocument();
        if (document != null) {
            if (document.getTitle() != null) {
                title = document.getTitle();
            }
            if (document.getId() != null) {
                id = document.getId().toString();
            }
        }

        final String fileName = String.format(FILENAME_TEMPLATE, documentRequest.getIdentifier(), id, title);
        log.info("Generated filename [{}] for document with identifier [{}]", fileName,
                documentRequest.getIdentifier());
        return fileName;
    }

    /**
     * Get the output stream (file) for the given name. It can be overridden by
     * tests to prevent writing to the file system.
     * 
     * @param name the name of the file to create
     * @return the stream.
     */
    protected OutputStream getOutputStream(final String name) {
        final Path path = Paths.get(outputFolder, name);
        try {
            final Path createFile = Files.createFile(path);
            final OutputStream out = new BufferedOutputStream(Files.newOutputStream(createFile));
            return out;
        }
        catch (final IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

}
