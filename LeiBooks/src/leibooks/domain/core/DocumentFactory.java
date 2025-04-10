package leibooks.domain.core;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

import leibooks.domain.facade.IDocument;
import leibooks.domain.metadatareader.IMetadataReader;
import leibooks.domain.metadatareader.MetadataReaderFactory;

/**
 * A factory for creating Document objects from files.
 *
 * This enum implements the Singleton pattern to provide a single point of access
 * for document creation throughout the application. It uses the MetadataReaderFactory
 * to obtain document metadata from files and creates appropriate Document instances.
 *
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public enum DocumentFactory {
    INSTANCE;

    /**
     * Creates a Document object from the specified file path.
     *
     * @param title the title of the document
     * @param pathToPhotoFile the file path to the document
     * @return a Document object
     * @throws FileNotFoundException if the specified file cannot be found or accessed
     */
    public IDocument createDocument(String title, String pathToPhotoFile) {
        try {
            IMetadataReader metadataReader = MetadataReaderFactory.INSTANCE.createMetadataReader(pathToPhotoFile);

            String author = metadataReader.getAuthor();
            LocalDate dateModified = metadataReader.getModifiedDate();
            String mimeType = metadataReader.getMimeType();
            Optional<Integer> numPages = metadataReader.getNumPages();

            return new Document(title, author, dateModified, mimeType, pathToPhotoFile, numPages);
        } catch (FileNotFoundException e) {
            // Se a falhar a leitura do ficheiro, cria um documento padrão
            return new Document(title, "", LocalDate.now(), "image/jpeg", pathToPhotoFile, Optional.empty());        
        }
    }
}
