/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jgap.xml;

import org.jgap.Gene;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.UnsupportedRepresentationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;


/**
 * The XMLManager performs marshalling of genetic entity instances
 * (such as Chromosomes and Genotypes) to XML representations of those
 * entities, as well as unmarshalling. All of the methods in this class are
 * static, so no construction is required (or allowed).
 *
 * @author Neil Rotstan
 * @since 1.0
 */
public class XMLManager
{

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.3 $";

    /**
     * Constant representing the name of the genotype XML element tag.
     */
    private static final String GENOTYPE_TAG = "genotype";

    /**
     * Constant representing the name of the chromosome XML element tag.
     */
    private static final String CHROMOSOME_TAG = "chromosome";

    /**
     * Constant representing the name of the gene XML element tag.
     */
    private static final String GENES_TAG = "genes";

    /**
     * Constant representing the name of the gene XML element tag.
     */
    private static final String GENE_TAG = "gene";

    /**
     * Constant representing the name of the size XML attribute that is
     * added to genotype and chromosome elements to describe their size.
     */
    private static final String SIZE_ATTRIBUTE = "size";

    /**
     * Constant representing the fully-qualified name of the concrete
     * Gene class that was marshalled.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * Shared DocumentBuilder, which is used to create new DOM Document
     * instances.
     */
    private static final DocumentBuilder m_documentCreator;

    /**
     * Shared lock object used for synchronization purposes.
     */
    private static final Object m_lock = new Object();


    static
    {
        try
        {
            m_documentCreator =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch ( ParserConfigurationException parserError )
        {
            throw new RuntimeException(
                "XMLManager: Unable to setup DocumentBuilder: " +
                parserError.getMessage() );
        }
    }

    /**
     * Private constructor. All methods in this class are static, so no
     * construction is allowed.
     */
    private XMLManager()
    {
    }

    /**
     * Marshall a Chromosome instance to an XML Document representation,
     * including its contained Gene instances.
     *
     * @param a_subject The chromosome to represent as an XML document.
     *
     * @return a Document object representing the given Chromosome.
     */
    public static Document representChromosomeAsDocument( Chromosome a_subject )
    {
        // DocumentBuilders do not have to be thread safe, so we have to
        // protect creation of the Document with a synchronized block.
        // -------------------------------------------------------------
        Document chromosomeDocument;

        synchronized( m_lock )
        {
            chromosomeDocument = m_documentCreator.newDocument();
        }

        Element chromosomeElement =
                representChromosomeAsElement( a_subject, chromosomeDocument );

        chromosomeDocument.appendChild( chromosomeElement );
        return chromosomeDocument;
    }

    /**
     * Marshall a Genotype to an XML Document representation, including its
     * population of Chromosome instances.
     *
     * @param a_subject The genotype to represent as an XML document.
     *
     * @return a Document object representing the given Genotype.
     */
    public static Document representGenotypeAsDocument( Genotype a_subject )
    {
        // DocumentBuilders do not have to be thread safe, so we have to
        // protect creation of the Document with a synchronized block.
        // -------------------------------------------------------------
        Document genotypeDocument;

        synchronized( m_lock )
        {
            genotypeDocument = m_documentCreator.newDocument();
        }

        Element genotypeElement =
                representGenotypeAsElement( a_subject, genotypeDocument );

        genotypeDocument.appendChild( genotypeElement );
        return genotypeDocument;
    }

    /**
     * Marshall an array of Genes to an XML Element representation.
     *
     * @param a_geneValues The genes to represent as an XML element.
     * @param a_xmlDocument A Document instance that will be used to create
     *                      the Element instance. Note that the element will
     *                      NOT be added to the document by this method.
     *
     * @return an Element object representing the given genes.
     */
    public static Element representGenesAsElement( Gene[] a_geneValues,
                                                   Document a_xmlDocument )
    {
        // Create the parent genes element.
        // --------------------------------
        Element genesElement = a_xmlDocument.createElement( GENES_TAG );

        // Now add gene sub-elements for each gene in the given array.
        // ---------------------------------------------------------------
        Element geneElement;

        for( int i = 0; i < a_geneValues.length; i++ )
        {
            // Create the allele element for this gene.
            // ----------------------------------------
            geneElement = a_xmlDocument.createElement( GENE_TAG );

            // Add the class attribute and set its value to the class
            // name of the concrete class representing the current Gene.
            // ---------------------------------------------------------
            geneElement.setAttribute( CLASS_ATTRIBUTE,
                                      a_geneValues[ i ].getClass().getName() );

            // Create a text node to contain the string representation of
            // the gene's value (allele).
            // ----------------------------------------------------------
            Text alleleRepresentation = a_xmlDocument.createTextNode(
                a_geneValues[ i ].getPersistentRepresentation() );

            // And now add the text node to the gene element, and then
            // add the gene element to the genes element.
            // ---------------------------------------------------------
            geneElement.appendChild( alleleRepresentation );
            genesElement.appendChild( geneElement );
        }

        return genesElement;
    }

    /**
     * Marshall a Chromosome instance to an XML Element representation,
     * including its contained Genes as sub-elements. This may be useful in
     * scenarios where representation as an entire Document is undesirable,
     * such as when the representation of this Chromosome is to be combined
     * with other elements in a single Document.
     *
     * @param a_subject The chromosome to represent as an XML element.
     * @param a_xmlDocument A Document instance that will be used to create
     *                      the Element instance. Note that the element will
     *                      NOT be added to the document by this method.
     *
     * @return an Element object representing the given Chromosome.
     */
    public static Element representChromosomeAsElement( Chromosome a_subject,
                                                        Document a_xmlDocument )
    {
        // Start by creating an element for the chromosome and its size
        // attribute, which represents the number of genes in the chromosome.
        // ------------------------------------------------------------------
        Element chromosomeElement =
            a_xmlDocument.createElement( CHROMOSOME_TAG );

        chromosomeElement.setAttribute( SIZE_ATTRIBUTE,
                                        Integer.toString( a_subject.size() ) );

        // Next create the genes element with its nested gene elements,
        // which will contain string representations of the alleles.
        // --------------------------------------------------------------
        Element genesElement = representGenesAsElement( a_subject.getGenes(),
                                                        a_xmlDocument );

        // Add the new genes element to the chromosome element and then
        // return the chromosome element.
        // -------------------------------------------------------------
        chromosomeElement.appendChild( genesElement );

        return chromosomeElement;
    }

    /**
     * Marshall a Genotype instance into an XML Element representation,
     * including its population of Chromosome instances as sub-elements.
     * This may be useful in scenarios where representation as an
     * entire Document is undesirable, such as when the representation
     * of this Genotype is to be combined with other elements in a
     * single Document.
     *
     * @param a_subject The genotype to represent as an XML element.
     * @param a_xmlDocument A Document instance that will be used to create
     *                      the Element instance. Note that the element will
     *                      NOT be added to the document by this method.
     *
     * @return an Element object representing the given Genotype.
     */
    public static Element representGenotypeAsElement( Genotype a_subject,
                                                      Document a_xmlDocument )
    {
        Chromosome[] population = a_subject.getChromosomes();

        // Start by creating the genotype element and its size attribute,
        // which represents the number of chromosomes present in the
        // genotype.
        // --------------------------------------------------------------
        Element genotypeTag = a_xmlDocument.createElement( GENOTYPE_TAG );
        genotypeTag.setAttribute( SIZE_ATTRIBUTE,
                Integer.toString( population.length ) );

        // Next, add nested elements for each of the chromosomes in the
        // genotype.
        // ------------------------------------------------------------
        for ( int i = 0; i < population.length; i++ )
        {
            Element chromosomeElement =
                representChromosomeAsElement( population[ i ], a_xmlDocument );

            genotypeTag.appendChild( chromosomeElement );
        }

        return genotypeTag;
    }

    /**
     * Unmarshall a Chromosome instance from a given XML Element
     * representation.
     *
     * @param a_xmlElement The XML Element representation of the Chromosome.
     *
     * @return A new Chromosome instance setup with the data from the XML
     *         Element representation.
     *
     * @throws ImproperXMLException if the given Element is improperly
     *         structured or missing data.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Gene implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws GeneCreationException if there is a problem creating or
     *                                 populating an Gene instance.
     */
    public static Gene[] getGenesFromElement(
                               Configuration a_activeConfiguration,
                               Element a_xmlElement )
                           throws ImproperXMLException,
                                  UnsupportedRepresentationException,
                                  GeneCreationException
    {
        // Do some sanity checking. Make sure the XML Element isn't null and
        // that it in fact represents a set of genes.
        // -----------------------------------------------------------------
        if ( a_xmlElement == null ||
             !( a_xmlElement.getTagName().equals( GENES_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Element: " +
                "given Element is not a 'genes' element." );
        }

        List genes = new ArrayList();

        // Extract the nested gene elements.
        // ---------------------------------------------------------
        NodeList geneElements = a_xmlElement.getElementsByTagName( GENE_TAG );

        if ( geneElements == null )
        {
            throw new ImproperXMLException(
                "Unable to build Gene instances from XML Element: " +
                "'gene' sub-elements not found." );
        }

        // For each gene, get the class attribute so we know what class
        // to instantiate to represent the gene instance, and then find
        // the child text node, which is where the string representation
        // of the allele is located, and extract the representation.
        // --------------------------------------------------------------
        int numberOfGeneNodes = geneElements.getLength();
        for( int i = 0; i < numberOfGeneNodes; i++ )
        {
            Element thisGeneElement = (Element) geneElements.item( i );
            thisGeneElement.normalize();

            // Fetch the class attribute and create an instance of that
            // class to represent the current gene.
            // --------------------------------------------------------
            String geneClassName =
                thisGeneElement.getAttribute( CLASS_ATTRIBUTE );

            Gene thisGeneObject;
            try
            {
                thisGeneObject =
                    (Gene) Class.forName( geneClassName ).newInstance();
            }
            catch( Exception e )
            {
                throw new GeneCreationException( e.getMessage() );
            }

            // Find the text node and fetch the string representation of
            // the allele.
            // ---------------------------------------------------------
            NodeList children = thisGeneElement.getChildNodes();
            int childrenSize = children.getLength();
            String alleleRepresentation = null;

            for ( int j = 0; j < childrenSize; j++ )
            {
                if ( children.item( j ).getNodeType() == Node.TEXT_NODE )
                {
                    // We found the text node. Extract the representation.
                    // ------------------------------------------------------
                    alleleRepresentation = children.item( j ).getNodeValue();
                    break;
                }
            }

            // Sanity check: Make sure the representation isn't null.
            // ------------------------------------------------------
            if( alleleRepresentation == null )
            {
                throw new ImproperXMLException(
                    "Unable to build Gene instance from XML Element: " +
                    "value (allele) is missing representation." );
            }

            // Now set the value of the gene to that reflect the
            // string representation.
            // ---------------------------------------------------
            try
            {
                thisGeneObject.setValueFromPersistentRepresentation(
                    alleleRepresentation );
            }
            catch( UnsupportedOperationException e )
            {
                throw new GeneCreationException(
                    "Unable to build Gene because it does not support the " +
                    "setValueFromPersistentRepresentation() method." );
            }

            // Finally, add the current gene object to the list of genes.
            // ------------------------------------------------------------
            genes.add( thisGeneObject );
        }

        return (Gene[]) genes.toArray( new Gene[ genes.size() ] );
    }

    /**
     * Unmarshall a Chromosome instance from a given XML Element
     * representation.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Chromosome.
     *
     * @param a_xmlElement The XML Element representation of the Chromosome.
     *
     * @return A new Chromosome instance setup with the data from the XML
     *         Element representation.
     *
     * @throws ImproperXMLException if the given Element is improperly
     *         structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *         an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Gene implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws GeneCreationException if there is a problem creating or
     *                                 populating an Gene instance.
     */
    public static Chromosome getChromosomeFromElement(
                                 Configuration a_activeConfiguration,
                                 Element a_xmlElement )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   GeneCreationException
    {
        // Do some sanity checking. Make sure the XML Element isn't null and
        // that in fact represents a chromosome.
        // -----------------------------------------------------------------
        if ( a_xmlElement == null ||
             !( a_xmlElement.getTagName().equals( CHROMOSOME_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Element: " +
                "given Element is not a 'chromosome' element." );
        }

        // Extract the nested genes element and make sure it exists.
        // ---------------------------------------------------------
        Element genesElement = (Element)
            a_xmlElement.getElementsByTagName( GENES_TAG ).item( 0 );

        if ( genesElement == null )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Element: " +
                "'genes' sub-element not found." );
        }

        // Construct the genes from their representations.
        // -----------------------------------------------
        Gene[] geneAlleles = getGenesFromElement( a_activeConfiguration,
                                                  genesElement );

        // Construct the new Chromosome with the genes and return it.
        // ----------------------------------------------------------
        return new Chromosome( a_activeConfiguration, geneAlleles );
    }


    /**
     * Unmarshall a Genotype instance from a given XML Element representation.
     * Its population of Chromosomes will be unmarshalled from the Chromosome
     * sub-elements.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Genotype and Chromosome instances.
     *
     * @param a_xmlElement The XML Element representation of the Genotype
     *
     * @return A new Genotype instance, complete with a population
     *         of Chromosomes, setup with the data from the XML
     *         Element representation.
     *
     * @throws ImproperXMLException if the given Element is improperly
     *         structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *         an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Gene implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws GeneCreationException if there is a problem creating or
     *         populating an Gene instance.
     */
    public static Genotype getGenotypeFromElement( Configuration a_activeConfiguration,
                                                   Element a_xmlElement )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   GeneCreationException
    {
        // Sanity check. Make sure the XML element isn't null and that it
        // actually represents a genotype.
        // --------------------------------------------------------------
        if ( a_xmlElement == null ||
             !( a_xmlElement.getTagName().equals( GENOTYPE_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Genotype instance from XML Element: " +
                "given Element is not a 'genotype' element." );
        }

        // Fetch all of the nested chromosome elements and convert them
        // into Chromosome instances.
        // ------------------------------------------------------------
        NodeList chromosomes =
            a_xmlElement.getElementsByTagName( CHROMOSOME_TAG );
        int numChromosomes = chromosomes.getLength();

        Chromosome[] population = new Chromosome[ numChromosomes ];

        for ( int i = 0; i < numChromosomes; i++ )
        {
            population[ i ] = getChromosomeFromElement( a_activeConfiguration,
                    (Element) chromosomes.item( i ) );
        }

        // Construct a new Genotype with the chromosomes and return it.
        // ------------------------------------------------------------
        return new Genotype( a_activeConfiguration, population );
    }


    /**
     * Unmarshall a Genotype instance from a given XML Document representation.
     * Its population of Chromosomes will be unmarshalled from the Chromosome
     * sub-elements.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Genotype and Chromosome instances.
     *
     * @param a_xmlDocument The XML Document representation of the Genotype.
     *
     * @return A new Genotype instance, complete with a population of
     *         Chromosomes, setup with the data from the XML Document
     *         representation.
     *
     * @throws ImproperXMLException if the given Document is improperly
     *         structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *         an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Gene implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws GeneCreationException if there is a problem creating or
     *         populating an Gene instance.
     */
    public static Genotype getGenotypeFromDocument(
                               Configuration a_activeConfiguration,
                               Document a_xmlDocument )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   GeneCreationException
    {
        // Extract the root element, which should be a genotype element.
        // After verifying that the root element is not null and that it
        // in fact is a genotype element, then convert it into a Genotype
        // instance.
        // --------------------------------------------------------------
        Element rootElement = a_xmlDocument.getDocumentElement();

        if ( rootElement == null ||
             !( rootElement.getTagName().equals( GENOTYPE_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Genotype from XML Document: " +
                "'genotype' element must be at root of document." );
        }

        return getGenotypeFromElement( a_activeConfiguration, rootElement );
    }


    /**
     * Unmarshall a Chromosome instance from a given XML Document
     * representation. Its genes will be unmarshalled from the gene
     * sub-elements.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Chromosome instances.
     *
     * @param a_xmlDocument The XML Document representation of the Chromosome.
     *
     * @return A new Chromosome instance setup with the data from the XML
     *         Document representation.
     *
     * @throws ImproperXMLException if the given Document is improperly
     *         structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *         an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Gene implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws GeneCreationException if there is a problem creating or
     *         populating an Gene instance.
     */
    public static Chromosome getChromosomeFromDocument( Configuration a_activeConfiguration,
                                                        Document a_xmlDocument )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   GeneCreationException
    {
        // Extract the root element, which should be a chromosome element.
        // After verifying that the root element is not null and that it
        // in fact is a chromosome element, then convert it into a Chromosome
        // instance.
        // ------------------------------------------------------------------
        Element rootElement = a_xmlDocument.getDocumentElement();
        if ( rootElement == null ||
             !( rootElement.getTagName().equals( CHROMOSOME_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Document: " +
                "'chromosome' element must be at root of Document." );
        }

        return getChromosomeFromElement( a_activeConfiguration, rootElement );
    }
}
