package com.alishahidi.api.core.generator;


import com.alishahidi.api.core.generator.model.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CodeGenerator {

    Configuration freemarkerConfig;
    String basePackage;

    public CodeGenerator() {
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_33);
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/generators");

        basePackage = (new BasePackage()).getBasePackage();
    }

    public static void run() throws TemplateException, IOException {
        String xmlFolderPath = "generate-xmls";

        List<EntityModel> entities = XmlParser.readEntitiesFromXml(xmlFolderPath);

        CodeGenerator generator = new CodeGenerator();

        for (EntityModel entity : entities) {
            if (entity.getGenerate()) {
                generator.generate(entity);
            }
        }

        System.out.println("Entity generation completed.");
    }


    public void generate(EntityModel entityModel) throws IOException, TemplateException {
        Map<String, Object> data = new HashMap<>();
        data.put("basePackage", basePackage);
        data.put("tableName", convertToSnakeCase(entityModel.getEntityName()));
        data.put("entityName", entityModel.getEntityName());
        data.put("fields", entityModel.getFields());
        data.put("relationships", entityModel.getRelations() != null ? entityModel.getRelations() : List.of());

        generateFile("EntityTemplate.ftl", entityModel.getEntityName(), "Entity.java", data);
//        generateFile("controller.ftl", entityName + "Controller.java", data);
//        generateFile("service.ftl", entityName + "Service.java", data);
//        generateFile("repository.ftl", entityName + "Repository.java", data);
//        generateFile("dto.ftl", entityName + "CreateDto.java", data);
//        generateFile("dto.ftl", entityName + "UpdateDto.java", data);
//        generateFile("dto.ftl", entityName + "LoadDto.java", data);
    }

    private void generateFile(String templateName, String entityName, String suffixName, Map<String, Object> data) throws IOException, TemplateException {
        String srcMainJavaPath = "src/main/java/";

        String packageDir = basePackage.replace(".", "/");

        String safeEntityName = entityName.toLowerCase();

        String entityDirectoryPath = srcMainJavaPath + packageDir + "/" + safeEntityName;

        // Create directories if they don't exist
        File directory = new File(entityDirectoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFileName = entityDirectoryPath + "/" + entityName + suffixName;

        Template template = freemarkerConfig.getTemplate(templateName);
        FileWriter writer = new FileWriter(outputFileName);
        template.process(data, writer);
        writer.close();
    }


    private String convertToSnakeCase(String entity) {
        String snakeCase = entity.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();

        if (snakeCase.endsWith("y") && !snakeCase.endsWith("ay")) {
            snakeCase = snakeCase.substring(0, snakeCase.length() - 1) + "ies";
        } else if (!snakeCase.endsWith("s")) {
            snakeCase += "s";
        }

        return snakeCase;
    }

}