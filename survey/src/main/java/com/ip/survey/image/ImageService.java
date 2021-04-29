package com.ip.survey.image;

import com.ip.survey.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class ImageService {

    private final String projectPath;
    private final String filePathFromProject;

    public ImageService(){
        this.projectPath = System.getProperty("user.dir");
        this.filePathFromProject = "\\src\\main\\resources\\static\\images";
    }

    public ResponseEntity<Object> saveImage(MultipartFile image){

        String imageName;
        do{
            imageName = RandomString.generateRandomString();
        }while (isNamePresent(imageName));

        String suffix = getFileSuffix(image);

        String absoluteFilePath = projectPath + filePathFromProject + "\\" + imageName + suffix;

        File dest = new File(absoluteFilePath);
        try {
            image.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("imageName", imageName);
        return new ResponseEntity<Object>(responseMap, HttpStatus.OK);
    }


    private boolean isNamePresent(String name){
        //Check if a file with that name is present
        File dir = new File(projectPath + filePathFromProject);
        for (File file : dir.listFiles()) {
            String currentFileName = file.getName().split("\\.")[0];
            if (name.equals(currentFileName)) {
                return true;
            }
        }
        return false;
    }

    private String getFileSuffix(MultipartFile image){
        //get file type
        return "." + image.getContentType().split("/")[1];
    }

    public String getFullImageName(String name){
        //Return name + file suffix from a name given
        File dir = new File(projectPath + filePathFromProject);
        for(File file: dir.listFiles()){
            String currentFileName = file.getName().split("\\.")[0];
            if(currentFileName.equals(name)){
                return file.getName();
            }
        }
        return "";
    }

}
