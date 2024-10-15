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
                XmlMapper xmlMapper = new XmlMapper();
                for (File xmlFile : xmlFiles) {
                    try {
                        EntityModel entity = xmlMapper.readValue(getXmlStreamReader(xmlFile), EntityModel.class);
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



    public static EntityModel readEntityModelFromXml(XMLStreamReader xmlFile) throws IOException {
        XmlMapper xmlMapper = new XmlMapper(); // Create a new XmlMapper instance
        return xmlMapper.readValue(xmlFile, EntityModel.class);
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