package leibooks.domain.metadatareader;

import java.io.FileNotFoundException;

/**
 * A generic metadata reader implementation that uses the default values
 * for number of pages and authors provided by AMetadataReader.
 * 
 * This class is used for document types that don't have a specialized
 * metadata reader implementation, using Optional.empty() for numPages
 * and "n/a" for authors.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class GenericMetadataReader extends AMetadataReader {
    
    /**
     * Creates a new generic metadata reader for the specified file.
     * 
     * @param pathToDocFile The path to the document file
     * @throws FileNotFoundException if the file doesn't exist or cannot be accessed
     */
    public GenericMetadataReader(String pathToDocFile) throws FileNotFoundException {
       super(pathToDocFile);
    }
}