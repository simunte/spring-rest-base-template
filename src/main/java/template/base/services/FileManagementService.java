package template.base.services;

import org.springframework.web.multipart.MultipartFile;
import template.base.domain.File;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileManagementService {
    String uploadSingleFile(MultipartFile file);
    List<Object> uploadMultipleFile(MultipartFile[] files);
    File uploadFileToDatabase(MultipartFile file) throws Exception;
    void downloadFile(HttpServletResponse response, String fileName) throws IOException;
}
