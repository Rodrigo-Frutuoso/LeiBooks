package leibooks.domain.metadatareader;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This interface defines methods to access common document metadata such as
 * author information, modification date, MIME type, and page count.
 * Different implementations handle specific document formats.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public interface IMetadataReader {
    /**
     * Gets the author of the document.
     * 
     * @return the author's name as a String
     * @ensures \result != null
     */
    String getAuthor();
    
    /**
     * Gets the last modification date of the document.
     * 
     * @return the date when the document was last modified
     * @ensures \result != null
     */
    LocalDate getModifiedDate();
    
    /**
     * Gets the MIME type of the document.
     * 
     * @return the MIME type as a String
     * @ensures \result != null
     */
    String getMimeType();
    
    /**
     * Gets the number of pages in the document.
     * 
     * @return an Optional containing the number of pages, or empty if not applicable
     * @ensures \result != null
     */
    Optional<Integer> getNumPages();
}