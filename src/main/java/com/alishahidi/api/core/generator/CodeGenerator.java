package com.alishahidi.api.core.generator;


import com.alishahidi.api.core.generator.model.*;
import com.alishahidi.api.core.util.StringUtils;
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
        data.put("tableName", convertToSnakeCase(entityModel.getName()));
        data.put("entityName", entityModel.getName());
        data.put("fields", entityModel.getFields() != null ? entityModel.getFields() : List.of());
        data.put("relationships", entityModel.getRelations() != null ? entityModel.getRelations() : List.of());

        generateFile("EntityTemplate.ftl", entityModel.getName(), "Entity.java", data);
        generateFile("ControllerTemplate.ftl", entityModel.getName(), "Controller.java", data);
        generateFile("ServiceTemplate.ftl", entityModel.getName(), "Service.java", data);
        generateFile("RepositoryTemplate.ftl", entityModel.getName(), "Repository.java", data);
        data.put("dtoType", "Load");
        generateFile("DtoTemplate.ftl", entityModel.getName(), "LoadDto.java", data, "dto");
        data.put("dtoType", "Update");
        generateFile("DtoTemplate.ftl", entityModel.getName(), "UpdateDto.java", data, "dto");
        data.put("dtoType", "Create");
        generateFile("DtoTemplate.ftl", entityModel.getName(), "CreateDto.java", data, "dto");
        generateFile("MapperTemplate.ftl", entityModel.getName(), "Mapper.java", data);

        if (!entityModel.getFields().isEmpty())
            for (FieldModel fieldModel : entityModel.getFields()) {
                if(!fieldModel.getEnums().isEmpty()){
                    data.put("enumName", fieldModel.getName());
                    data.put("enums", fieldModel.getEnums());
                    generateFile("EnumTemplate.ftl", entityModel.getName(), StringUtils.toUpperCamelCase(fieldModel.getName())  + "Enum.java", data, "constant");
                }
            }
    }

    private void generateFile(String templateName, String entityName, String suffixName, Map<String, Object> data) throws IOException, TemplateException {
        generateFile(templateName, entityName, suffixName, data, null);
    }

    private void generateFile(String templateName, String entityName, String suffixName, Map<String, Object> data, String folder) throws IOException, TemplateException {
        String srcMainJavaPath = "src/main/java/";

        String packageDir = basePackage.replace(".", "/");

        String safeEntityName = entityName.toLowerCase();

        String entityDirectoryPath = srcMainJavaPath + packageDir + "/" + safeEntityName;

        if (folder != null) {
            entityDirectoryPath = entityDirectoryPath + "/" + folder;
        }

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