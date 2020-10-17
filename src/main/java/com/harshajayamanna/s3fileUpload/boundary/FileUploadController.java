package com.harshajayamanna.s3fileUpload.boundary;

import java.awt.Image;
import java.io.IOException;

import com.harshajayamanna.s3fileUpload.service.ImageService;
import com.harshajayamanna.s3fileUpload.validators.ValidateImgSize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {


	private final ImageService imageService;

	@Autowired
	FileUploadController(ImageService imageService) {
		this.imageService = imageService;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public ImageDto uploadAvatar(@RequestParam(value = "imageFile", required = true) @ValidateImgSize MultipartFile image) {

		String bucketName = "public-bucket-for-files";

		String imageUrl = imageService.uploadImage(image, bucketName);

		ImageDto dto = new ImageDto();
		dto.url = imageUrl;

		return dto;
	}

	private class ImageDto {
		public String url;
	}
}
