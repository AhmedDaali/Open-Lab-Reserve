package org.hkijena.olr.services;

import jakarta.annotation.PostConstruct;
import org.hkijena.olr.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FileStorageReaperService {

    public static final Logger LOGGER = LoggerFactory.getLogger(FileStorageReaperService.class);

    private final FileStorageService fileStorageService;

    @Autowired
    public FileStorageReaperService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostConstruct
    public void deleteUnusedFiles() {
        LOGGER.info("Looking for unused files...");
        Set<String> usedIds = new HashSet<>();
//        LOGGER.info("Looking for unused files... images");
//        for (Image image : imageRepository.findAll()) {
//            if(!StringUtils.isNullOrEmpty(image.getRawDataFileId())) {
//                usedIds.add(image.getRawDataFileId());
//            }
//            if(!StringUtils.isNullOrEmpty(image.getThumbnailDataFileId())) {
//                usedIds.add(image.getThumbnailDataFileId());
//            }
//        }
//        LOGGER.info("Looking for unused files... annotations");
//        for (MaskImageAnnotation maskImageAnnotation : maskImageAnnotationRepository.findAll()) {
//            if(!StringUtils.isNullOrEmpty(maskImageAnnotation.getRawDataFileId())) {
//                usedIds.add(maskImageAnnotation.getRawDataFileId());
//            }
//            if(!StringUtils.isNullOrEmpty(maskImageAnnotation.getThumbnailDataFileId())) {
//                usedIds.add(maskImageAnnotation.getThumbnailDataFileId());
//            }
//        }
//        LOGGER.info("Looking for unused files... results");
//        for (ResultItem resultItem : resultItemRepository.findAll()) {
//            if(!StringUtils.isNullOrEmpty(resultItem.getRawDataFileId())) {
//                usedIds.add(resultItem.getRawDataFileId());
//            }
//            if(!StringUtils.isNullOrEmpty(resultItem.getThumbnailDataFileId())) {
//                usedIds.add(resultItem.getThumbnailDataFileId());
//            }
//            if(!StringUtils.isNullOrEmpty(resultItem.getVisualizationDataFileId())) {
//                usedIds.add(resultItem.getVisualizationDataFileId());
//            }
//        }
        LOGGER.info("Looking for unused files... collecting present files");
        Set<String> allStoredFileIds = fileStorageService.findAllStoredFileIds();
        LOGGER.info("Looking for unused files... found " + allStoredFileIds.size() + " stored files");
        allStoredFileIds.removeAll(usedIds);
        LOGGER.info("Looking for unused files... found " + allStoredFileIds.size() + " stored files that are not used. Deleting now.");
        for (String fileId : allStoredFileIds) {
            fileStorageService.delete(fileId);
        }

    }
}
