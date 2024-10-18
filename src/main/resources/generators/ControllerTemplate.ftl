package ${basePackage}.${entityName?lower_case};

import ${basePackage}.${entityName?lower_case}.dto.${entityName}CreateDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}LoadDto;
import ${basePackage}.${entityName?lower_case}.dto.${entityName}UpdateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import com.alishahidi.api.core.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${entityName?lower_case}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ${entityName}Controller {
    ${entityName}Service ${entityName?uncap_first}Service;

    @PostMapping
    public ResponseEntity<ApiResponse<${entityName}LoadDto>> create(@RequestBody ${entityName}CreateDto createDto) {
        return ResponseEntity.ok(${entityName?uncap_first}Service.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<${entityName}LoadDto>> update(@PathVariable Long id, @RequestBody ${entityName}UpdateDto updateDto) {
        return ResponseEntity.ok(${entityName?uncap_first}Service.update(id, updateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<${entityName}LoadDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(${entityName?uncap_first}Service.findById(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<${entityName}LoadDto>>> findAll() {
        return ResponseEntity.ok(${entityName?uncap_first}Service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ${entityName?uncap_first}Service.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }
}