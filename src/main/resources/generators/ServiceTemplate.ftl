package ${basePackage}.${entityName?lower_case};

import ${basePackage}.core.entity.BaseMapper;
import ${basePackage}.core.entity.BaseRepository;
import ${basePackage}.core.entity.BaseService;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}CreateDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}LoadDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}UpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ${entityName}Service extends BaseService<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> {
    ${entityName}Repository repository;
    ${entityName}Mapper mapper = Mappers.getMapper(${entityName}Mapper.class);

    @Override
    protected BaseRepository<${entityName}Entity> getRepository() {
        return repository;
    }

    @Override
    protected BaseMapper<${entityName}Entity, ${entityName}CreateDto, ${entityName}UpdateDto, ${entityName}LoadDto> getMapper() {
        return mapper;
    }
}
