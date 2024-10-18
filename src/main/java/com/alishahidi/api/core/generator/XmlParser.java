package com.alishahidi.api.core.generator;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.alishahidi.api.core.generator.model.EntityModel;
import com.alishahidi.api.core.generator.model.RelationModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    public static List<EntityModel> readEntitiesFromXml(String folderPath) throws IOException {
        List<EntityModel> entities = new ArrayList<>();

        try {
            Resource resource = new ClassPathResource(folderPath);
            File folder = resource.getFile();

            // List XML files in the folder
            File[] xmlFiles = folder.listFiles((dir, name) -> name.endsWith(".xml"));

            if (xmlFiles != null) {
                for (File xmlFile : xmlFiles) {
                    try {
                        EntityModel entity = readEntityModelFromXml(xmlFile);
                        generateFromEntityModel(entity);
                        entities.add(entity);
                    } catch (Exception e) {
                        // Log or handle the exception as needed
                        System.err.println("Error processing file: " + xmlFile.getName());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            // Log or handle the exception as needed
            System.err.println("Error reading XML files from folder: " + folderPath);
            e.printStackTrace();
        }

        return entities;
    }

    public static EntityModel readEntityModelFromXml(File xmlFile) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        XMLStreamReader xmlStreamReader = null;
        FileInputStream fileInputStream = null;

        try {
            // Obtain the FileInputStream and XMLStreamReader
            fileInputStream = new FileInputStream(xmlFile);
            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(fileInputStream);

            // Read and return the EntityModel
            return xmlMapper.readValue(xmlStreamReader, EntityModel.class);
        } catch (XMLStreamException e) {
            throw new IOException("Error reading EntityModel from XML file: " + xmlFile.getName(), e);
        } finally {
            // Close the XMLStreamReader if it was created
            if (xmlStreamReader != null) {
                try {
                    xmlStreamReader.close();
                } catch (XMLStreamException e) {
                    System.err.println("Error closing XMLStreamReader: " + e.getMessage());
                }
            }
            // Close the FileInputStream if it was created
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    System.err.println("Error closing FileInputStream: " + e.getMessage());
                }
            }
        }
    }

    public static XMLStreamReader getXmlStreamReader(File xmlFile) throws FileNotFoundException, XMLStreamException {
        // Create an instance of XMLInputFactory
        XMLInputFactory factory = XMLInputFactory.newInstance();

        // Create FileInputStream from the given File
        FileInputStream fileInputStream = new FileInputStream(xmlFile);

        // Create and return the XMLStreamReader
        return factory.createXMLStreamReader(fileInputStream);
    }

    public static void generateFromEntityModel(EntityModel entityModel) {
        for (RelationModel relation : entityModel.getRelations()) {
            relation.setRelatedEntityPackage(RelationModel.generatePackageName(relation.getRelatedEntityName()));
        }
    }
}