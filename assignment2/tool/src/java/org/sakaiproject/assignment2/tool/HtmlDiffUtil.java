package org.sakaiproject.assignment2.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Simple utility to diff HTML fragments and produce a marked up output. This 
 * is primarily for diffing and showing changes between original student 
 * submissions and the annotated feedback from an instructor.
 * 
 * We're using the Daisy Diff library for this.
 *
 * @author sgithens
 *
 */
public class HtmlDiffUtil {

    public HtmlDiffUtil() {};
    
    /**
     * Takes 2 input strings, assumed to be HTML fragments, and returns a 
     * String of the diffed structure.
     * 
     * This is largely based on code from org.outerj.daisy.diff.Main with all
     * the extra and unused options removed, as well as operating on Strings
     * inside of file system streams.
     * 
     * @param source
     * @param annotated
     * @return
     */
    public String diffHtml(String source, String annotated) {
        // change nulls to the empty string to avoid NPEs down the road
        if (source == null) {
            source = "";
        }
        if (annotated == null) {
            annotated = "";
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
        .newInstance();
        
        TransformerHandler result = null;
        try {
            result = tf.newTransformerHandler();
            result.getTransformer().setOutputProperty("omit-xml-declaration", "yes");
        } catch (TransformerConfigurationException e) {
            throw UniversalRuntimeException.accumulate(e, 
            "Error creating the SAX Transformer to be used for Daisy Diffing.");
        }
        result.setResult(new StreamResult(baos));
        
        ContentHandler postProcess = result;
        
        Locale locale = Locale.getDefault();
        String prefix = "diff";
        
        HtmlCleaner cleaner = new HtmlCleaner();
        
        InputSource oldSource = new InputSource(new ByteArrayInputStream(source.getBytes()));
        InputSource newSource = new InputSource(new ByteArrayInputStream(annotated.getBytes()));
        
        DomTreeBuilder oldHandler = new DomTreeBuilder();
        DomTreeBuilder newHandler = new DomTreeBuilder();
        try {
            cleaner.cleanAndParse(oldSource, oldHandler);
            cleaner.cleanAndParse(newSource, newHandler);
        } catch (Exception e) {
            throw UniversalRuntimeException.accumulate(e, 
                    "Error cleaning/parsing HTML before DaisyDiffing: source:\n" 
                    + source + "\n\nannotated:\n: " + annotated);
        }
        
        TextNodeComparator leftComparator = new TextNodeComparator(
                oldHandler, locale);
        
        TextNodeComparator rightComparator = new TextNodeComparator(
                newHandler, locale);
        
        try {
            postProcess.startDocument();
            HtmlSaxDiffOutput output = new HtmlSaxDiffOutput(postProcess,
                    prefix);
            HTMLDiffer differ = new HTMLDiffer(output);
            differ.diff(leftComparator, rightComparator);
            postProcess.endDocument();
        } catch (SAXException e) {
            throw UniversalRuntimeException.accumulate(e, 
                    "Error processing DaisyDiffing: source:\n" 
                    + source + "\n\nannotated:\n: " + annotated);
        }
        
        return baos.toString();
    }

}