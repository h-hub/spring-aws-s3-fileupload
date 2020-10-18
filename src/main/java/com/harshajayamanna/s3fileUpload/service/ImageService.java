package com.harshajayamanna.s3fileUpload.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.harshajayamanna.s3fileUpload.exception.FileUploadException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ImageService {

	public String uploadImage(MultipartFile image, String bucketName) {

		File imageFile;

		try {
			imageFile = convertMultiPartToFile(image);
		} catch (IOException e) {
			throw new FileUploadException("Unable to convert input stream to file.");
		}

		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(imageFile);
		} catch (IOException e) {
			throw new FileUploadException("Unable to read image.");
		}

		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion("ap-southeast-1").build();
		TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3client).build();
		String fileName = generateFileName(image);

		try {
			Upload xfer = xfer_mgr.upload(new PutObjectRequest(bucketName, fileName, imageFile)
					.withCannedAcl(CannedAccessControlList.PublicRead));

			xfer.waitForCompletion();

		} catch (AmazonServiceException | InterruptedException e) {
			throw new FileUploadException("AWS Error or Upload was interrupted");
		}
		xfer_mgr.shutdownNow();

		return "https://"+bucketName+".s3-ap-southeast-1.amazonaws.com/"+fileName;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

}
