package leibooks.domain.metadatareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Abstract class that provides a skeleton implementation of IMetadataReader.
 * Handles basic file metadata like modification date and MIME type.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public abstract class AMetadataReader implements IMetadataReader {
    protected String pathToDocFile;
    protected Optional<Integer> numPages = Optional.empty();
    protected String authors = "n/a"; 
    protected LocalDate modifiedDate;
    protected String mimeType;
    
    /**
     * Creates a new metadata reader for the specified file.
     * Reads basic metadata (modification date and MIME type) from the file.
     * 
     * @param pathToDocFile The path to the document file
     * @throws FileNotFoundException if the file doesn't exist or cannot be accessed
     */
    public AMetadataReader(String pathToDocFile) throws FileNotFoundException {
        File file = new File(pathToDocFile);
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException("File does not exist or cannot be accessed: " + pathToDocFile);
        }
        
        this.pathToDocFile = pathToDocFile;
        
        try {
            Path path = Paths.get(pathToDocFile);

            this.modifiedDate = Files.getLastModifiedTime(path)
                                     .toInstant()
                                     .atZone(ZoneId.systemDefault())
                                     .toLocalDate();

            this.mimeType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new FileNotFoundException("Error accessing file metadata: " + e.getMessage());
        }  
    }
    
    @Override
    public String getAuthor() {
        return authors;
    }
    
    @Override
    public LocalDate getModifiedDate() {
        return modifiedDate;
    }
    
    @Override
    public String getMimeType() {
        return mimeType;
    }
    
    @Override
    public Optional<Integer> getNumPages() {
        return numPages;
    }
}

