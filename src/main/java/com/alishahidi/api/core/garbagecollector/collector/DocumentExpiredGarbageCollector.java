package com.alishahidi.api.core.garbagecollector.collector;

import com.alishahidi.api.core.document.Document;
import com.alishahidi.api.core.document.DocumentGarbageCollectorDto;
import com.alishahidi.api.core.document.DocumentRepository;
import com.alishahidi.api.core.garbagecollector.GarbageCollector;
import com.alishahidi.api.core.s3.Bucket;
import com.alishahidi.api.core.s3.config.S3LiaraConfig;
import com.alishahidi.api.core.s3.strategy.UniqueDateFolderBucketStrategy;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class DocumentExpiredGarbageCollector implements GarbageCollector {
    @Value("${garbage.collector.document.time}")
    Long time;

    final RMap<Long, DocumentGarbageCollectorDto> documentMap;
    final DocumentRepository documentRepository;
    final S3LiaraConfig s3LiaraConfig;

    public DocumentExpiredGarbageCollector(RedissonClient redisson, DocumentRepository documentRepository, S3LiaraConfig s3LiaraConfig) {
        this.documentRepository = documentRepository;
        this.documentMap = redisson.getMap("document:expiration");
        this.s3LiaraConfig = s3LiaraConfig;
    }

    @Override
    public void collect() {
        long currentTime = System.currentTimeMillis();

        List<DocumentGarbageCollectorDto> expiredDocuments = documentMap.values().stream()
                .filter(documentGarbageCollectorDto -> documentGarbageCollectorDto.getExpirationTime() <= currentTime)
                .toList();

        for (DocumentGarbageCollectorDto documentGarbageCollectorDto : expiredDocuments) {
            documentMap.remove(documentGarbageCollectorDto.getId());
            documentRepository.deleteById(documentGarbageCollectorDto.getId());

            Bucket bucket = Bucket.builder()
                    .name(documentGarbageCollectorDto.getBucket())
                    .strategy(new UniqueDateFolderBucketStrategy())
                    .config(s3LiaraConfig)
                    .build();

            bucket.remove(documentGarbageCollectorDto.getPath());
            System.out.println("removed " + documentGarbageCollectorDto.getId());
        }
    }

    public void link(Document document) {
        documentMap.put(document.getId(), DocumentGarbageCollectorDto.builder()
                .id(document.getId())
                .expirationTime(System.currentTimeMillis() + time)
                .path(document.getPath())
                .bucket(document.getScope().getBucket())
                .build());
    }

    public void unlink(Long documentId) {
        documentMap.remove(documentId);
    }
}
