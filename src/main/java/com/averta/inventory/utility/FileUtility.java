package com.averta.inventory.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtility {

    private static final Logger logger = LogManager.getLogger(FileUtility.class);

    @Autowired
    private Environment environment;

    public String saveEncodedDocument(String docByte, String fileName) {
        String finalName = null;
        try {
            int index = fileName.lastIndexOf('.');
            String path = environment.getRequiredProperty("file.report.path");
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            finalName = fileName.substring(0, index) + System.currentTimeMillis() + fileName.substring(index);

            StringBuffer sb = new StringBuffer();
            sb.append(path);
            sb.append(finalName);

            int ind = docByte.indexOf(",");
            byte[] data = Base64.decodeBase64(docByte.substring(ind + 1));
            try (OutputStream stream = new FileOutputStream(sb.toString())) {
                stream.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return finalName;
    }

    public String saveEncodedImage(String imageByte, String fileName) {
        String finalName = null;
        try {
            int index = fileName.lastIndexOf('.');
            String path = environment.getRequiredProperty("file.report.path");
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            finalName = fileName.substring(0, index) + System.currentTimeMillis() + fileName.substring(index);

            StringBuffer sb = new StringBuffer();
            sb.append(path);
            sb.append(finalName);

            int ind = imageByte.indexOf(",");
            byte[] data = Base64.decodeBase64(imageByte.substring(ind + 1));
            try (OutputStream stream = new FileOutputStream(sb.toString())) {
                stream.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return finalName;
    }

    public File saveEncodedImageBytesFile(String fileString, String fileName) throws Exception {
        File file = null;
        try {
            int index = fileString.indexOf(",");
            String path = environment.getRequiredProperty("file.report.path") + fileName;
            byte[] data = Base64.decodeBase64(fileString.substring(index + 1));
            try (OutputStream stream = new FileOutputStream(path)) {
                stream.write(data);
            }
            file = new File(path);
        } catch (Exception e) {
            throw e;
        }
        return file;
    }

    public String saveFile(MultipartFile files) throws Exception {
        String finalName = null;
        try {
            String fileName = files.getOriginalFilename().trim();
            if (fileName != null && fileName.length() != 0 && files.getBytes().length != 0) {
                byte[] bytes = files.getBytes();
                int index = fileName.lastIndexOf('.');
                String path = environment.getRequiredProperty("file.report.path");
                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdirs();

                finalName = fileName.substring(0, index) + System.currentTimeMillis() + fileName.substring(index);

                StringBuffer sb = new StringBuffer();
                sb.append(path);
                sb.append(finalName);
                BufferedOutputStream buffStream = new BufferedOutputStream(
                        new FileOutputStream(new File(sb.toString())));
                buffStream.write(bytes);
                buffStream.close();
            }
        } catch (Exception e) {
            logger.error("Error while saving multipart file : ", e);
            throw e;
        }
        return finalName;
    }

    public void saveEncodedImageWithSameName(String imageByte, String fileName) {
        try {
            String path = environment.getRequiredProperty("file.gallery.path");
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            StringBuffer sb = new StringBuffer();
            sb.append(path);
            sb.append(fileName);

            int ind = imageByte.indexOf(",");
            byte[] data = Base64.decodeBase64(imageByte.substring(ind + 1));
            try (OutputStream stream = new FileOutputStream(sb.toString())) {
                stream.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
