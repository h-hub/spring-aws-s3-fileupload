package com.harshajayamanna.s3fileUpload.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.harshajayamanna.s3fileUpload.exception.FileUploadException;

public class ImgValidator implements ConstraintValidator<ValidateImg, Object> {
   public void initialize(ValidateImg constraint) {
   }

   public boolean isValid(Object obj, ConstraintValidatorContext context) {

      File image;
      try {
         image = convertMultiPartToFile((MultipartFile)obj);
      } catch (IOException e) {
         throw new FileUploadException("ImgValidator:Unable to convert input stream to file.");
      }

      BufferedImage bimg = null;

      try {
         bimg = ImageIO.read(image);
      } catch (IOException e) {
         throw new FileUploadException("ImgValidator: Unable to read image.");
      }

      int width          = bimg.getWidth();
      int height         = bimg.getHeight();

      if(width==1024 && height==1024){
         return true;
      }

      return false;
   }

   private File convertMultiPartToFile(MultipartFile file) throws IOException {
      File convFile = new File(file.getOriginalFilename());
      FileOutputStream fos = new FileOutputStream(convFile);
      fos.write(file.getBytes());
      fos.close();
      return convFile;
   }
}
