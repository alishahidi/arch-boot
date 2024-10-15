package ${basePackage}.${entityName?lower_case};

import ${basePackage}.core.entity.BaseController;
import ${basePackage}.core.entity.BaseService;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}CreateDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}LoadDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}UpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${entityName?lower_case}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ${entityName}Controller extends BaseController<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> {
    ${entityName}Service ${entityName?uncap_first}Service;

    @Override
    protected BaseService<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> getService() {
        return ${entityName?uncap_first}Service;
    }
}
