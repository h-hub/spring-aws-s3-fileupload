package com.harshajayamanna.s3fileUpload.boundary;

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
import org.springframework.web.multipart.MultipartFile;

public class FileUploadController {

	@Autowired
	private ImageService imageService;

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/user/avatar", method = RequestMethod.POST, produces = { "application/json",
			"application/xml" })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String uploadAvatar(@RequestParam(value = "imageFile", required = true) @ValidateImgSize MultipartFile image) throws IOException {

		String bucketName = "public-bucket-for-files";

		String imageUrl = imageService.uploadImage(image, bucketName);

		return imageUrl;
	}

}
