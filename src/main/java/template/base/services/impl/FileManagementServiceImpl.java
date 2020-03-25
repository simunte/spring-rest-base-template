package template.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import template.base.domain.File;
import template.base.repositories.FileRepository;
import template.base.services.FileManagementService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class FileManagementServiceImpl implements FileManagementService {
    @Value("${file.path.others}")
    private String pathOthers;

    private final FileRepository fileRepository;

    public FileManagementServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public String uploadSingleFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            byte[] fileByte = file.getBytes();
            Path path = Paths.get(pathOthers+fileName);
            Files.write(path, fileByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @Override
    public List<Object> uploadMultipleFile(MultipartFile[] files) {
        List<Object> fileDownloadUrls = new ArrayList<>();
        Arrays.asList(files)
                .stream()
                .forEach(file -> fileDownloadUrls.add(uploadSingleFile(file)));
        return fileDownloadUrls;
    }

    @Override
    public File uploadFileToDatabase(MultipartFile file) throws Exception{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence ");
            }
            File dbFile = new File();
            dbFile.setFileName(fileName);
            dbFile.setFileType(file.getContentType());
            dbFile.setFileByte(file.getBytes());
            return fileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void downloadFile(HttpServletResponse response, String fileName) throws IOException{
        java.io.File myfile = new java.io.File(pathOthers +fileName);
        response.setHeader("Content-Disposition", "filename=" + myfile.getName());
        OutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(myfile);
        byte[] fileByte = new byte[(int) myfile.length()];
        int length;
        while((length = fileInputStream.read(fileByte)) > 0){
            outputStream.write(fileByte, 0, length);
        }
        fileInputStream.close();
        outputStream.flush();
    }


}
