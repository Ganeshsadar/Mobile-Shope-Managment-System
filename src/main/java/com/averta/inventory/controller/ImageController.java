package com.averta.inventory.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.averta.inventory.bo.Response;
import com.averta.inventory.exception.InventoryException;
import com.averta.inventory.utility.ErrorConstants;
import com.averta.inventory.utility.FileUtility;

@RestController
@RequestMapping("/v1")
public class ImageController {

    @Autowired
    private Environment environment;

    @Autowired
    private FileUtility fileUtility;

    @CrossOrigin
    @GetMapping(value = "/get-image/{imageName}/")
    public void getImages(@PathVariable("imageName") String imageName, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            FileInputStream fileInputStream = null;
            String imagePath = environment.getRequiredProperty("file.report.path") + imageName;
            if (!imagePath.equals(null) && !imagePath.equalsIgnoreCase("null") && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    byte[] bFile = new byte[(int) file.length()];
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bFile);
                    fileInputStream.close();
                    response.setContentType("image/jpeg");
                    response.getOutputStream().write(bFile);
                    response.getOutputStream().flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/video/{fileName}/", method = RequestMethod.GET)
    public void getVideoFile(@PathVariable("fileName") String fileName, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String filePath = environment.getRequiredProperty("file.report.path") + fileName;
            if (!filePath.equals(null) && !filePath.equalsIgnoreCase("null") && !filePath.isEmpty()) {
                File file = new File(filePath);
                if (file.exists()) {

                    response.setContentType("application/*");
                    response.setContentLength((int) file.length());

                    // set headers for the response
                    response.setHeader("Content-Disposition", "inline; filename=" + file.getName() + ";");

                    // get output stream of the response
                    OutputStream outStream = response.getOutputStream();

                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    FileInputStream inputStream = new FileInputStream(file);
                    // write bytes read from the input stream into the output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outStream.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/document/{fileName}/", method = RequestMethod.GET)
    public void getPdfFile(@PathVariable("fileName") String fileName, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String filePath = environment.getRequiredProperty("file.report.path") + fileName;
            if (!filePath.equals(null) && !filePath.equalsIgnoreCase("null") && !filePath.isEmpty()) {
                File file = new File(filePath);
                if (file.exists()) {

                    response.setContentType("application/pdf");
                    response.setContentLength((int) file.length());

                    // set headers for the response
                    String headerKey = "Content-Disposition";
                    String headerValue = String.format("inline; filename=\"%s\"", file.getName());
                    response.setHeader(headerKey, headerValue);

                    // get output stream of the response
                    OutputStream outStream = response.getOutputStream();

                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    FileInputStream inputStream = new FileInputStream(file);
                    // write bytes read from the input stream into the output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outStream.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @PostMapping(value = "/upload")
    public ResponseEntity<Response> uploadVideo(@RequestParam("file") MultipartFile file) {
        Response response = new Response();
        try {
            response.setResult(fileUtility.saveFile(file));
            response.setStatus(ErrorConstants.SUCCESS);
            response.setMessage("success.");
        } catch (InventoryException e) {
            response.setStatus(e.getErrorCode());
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(ErrorConstants.INTERNAL_SERVER_ERROR);
            response.setMessage(ErrorConstants.SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/view-file/{fileName}/", method = RequestMethod.GET)
    public void viewFile(@PathVariable("fileName") String fileName, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String pdfPath = environment.getRequiredProperty("file.report.path") + URLDecoder.decode(fileName, "UTF-8");
            if (!pdfPath.equals(null) && !pdfPath.equalsIgnoreCase("null") && !pdfPath.isEmpty()) {
                File file = new File(pdfPath);
                if (file.exists()) {
                    response.setContentType("application/*");
                    response.setContentLength((int) file.length());

                    // set headers for the response
                    String headerKey = "Content-Disposition";
                    String headerValue = String.format("inline; filename=\"%s\"", file.getName());
                    response.setHeader(headerKey, headerValue);

                    // get output stream of the response
                    OutputStream outStream = response.getOutputStream();

                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    FileInputStream inputStream = new FileInputStream(file);
                    // write bytes read from the input stream into the output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/get-gallery-image/{imageName}/")
    public void getGalleryImages(@PathVariable("imageName") String imageName, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            FileInputStream fileInputStream = null;
            String imagePath = environment.getRequiredProperty("file.gallery.path") + imageName;
            if (!imagePath.equals(null) && !imagePath.equalsIgnoreCase("null") && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    byte[] bFile = new byte[(int) file.length()];
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bFile);
                    fileInputStream.close();
                    response.setContentType("image/jpeg");
                    response.getOutputStream().write(bFile);
                    response.getOutputStream().flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
