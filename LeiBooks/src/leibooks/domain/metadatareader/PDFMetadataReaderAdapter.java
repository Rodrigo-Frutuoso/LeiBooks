package leibooks.domain.metadatareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import leibooks.services.reader.PDFReader;

/**
 * Adapter for reading metadata from PDF documents.
 * Uses the PDFReader service to extract information specific to PDF files.
 * 
 * @author Tiago Leite 61863
 * @author Rodrigo Frutuoso 61865
 */
public class PDFMetadataReaderAdapter extends AMetadataReader {
    
    /**
     * Creates a new PDF metadata reader for the specified PDF file.
     * 
     * @param pathToDocFile The path to the PDF file
     * @throws FileNotFoundException if the file doesn't exist or cannot be accessed
     */
    public PDFMetadataReaderAdapter(String pathToDocFile) throws FileNotFoundException {
        super(pathToDocFile);
        
        try {
            PDFReader pdfReader = new PDFReader(new File(pathToDocFile));
            this.numPages = Optional.of(pdfReader.getPages());
            this.authors = pdfReader.getAuthors();

        } catch (IOException e) {
            throw new FileNotFoundException("Error reading PDF file: " + e.getMessage());
        }
    }
}