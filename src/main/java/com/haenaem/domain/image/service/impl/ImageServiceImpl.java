package com.haenaem.domain.image.service.impl;

import com.haenaem.domain.image.entity.Image;
import com.haenaem.domain.image.repository.ImageRepository;
import com.haenaem.domain.image.service.ImageService;
import com.haenaem.domain.image.service.S3Service;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {

  private final S3Service s3Service;
  private final ImageRepository imageRepository;

  @Override
  public Image upload(MultipartFile image) {

    if (image == null) {
      return null;
    }

    Image saveImage = s3Service.save(image);
    imageRepository.save(saveImage);

    return saveImage;
  }

  @Override
  @Transactional(readOnly = true)
  public String read(Long id) {

    Image image = imageRepository.findById(id)
        .orElseThrow(() -> new DomainException(ErrorCode.IMAGE_NOT_FOUND));

    return image.getUrl();
  }

  @Override
  public void delete(Long id) {

    Image image = imageRepository.findById(id)
        .orElseThrow(() -> new DomainException(ErrorCode.IMAGE_NOT_FOUND));
    s3Service.delete(image.getFileName());
    imageRepository.delete(image);
  }
}