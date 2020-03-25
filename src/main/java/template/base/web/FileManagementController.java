package template.base.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import template.base.domain.File;
import template.base.services.FileManagementService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Api(value="File Management")
public class FileManagementController {
    private final FileManagementService fileManagementService;

    public FileManagementController(FileManagementService fileManagementService) {
        this.fileManagementService = fileManagementService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/file/single-upload")
    @ApiOperation("Upload Single File")
    public ResponseEntity<String> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        String result = fileManagementService.uploadSingleFile(file);
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/file/multi-upload")
    @ApiOperation("Upload Multiple File")
    public ResponseEntity<List<Object>> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {
        List<Object> result = fileManagementService.uploadMultipleFile(files);
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/file/db-upload")
    @ApiOperation("Upload Single File To DB")
    public ResponseEntity<File> uploadSingleFileToDB(@RequestParam("file") MultipartFile file) throws Exception{
        File result = fileManagementService.uploadFileToDatabase(file);
        return ResponseEntity.ok()
                .body(result);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/file/download")
    @ApiOperation("download File")
    public void downloadFile(HttpServletResponse response,
                             @RequestParam("fileName") String fileName) throws IOException {
        fileManagementService.downloadFile(response, fileName);
    }

}
