package com.tt.unitify.modules.tests;

import com.tt.unitify.modules.utils.TransformUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/test")
@Log4j2
public class TestController {
    @Autowired
    TestService testService;


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("filename") String fileName) throws IOException{
        return testService.uploadFile(file, fileName);
    }

    @GetMapping("/download")
    public ResponseEntity downloadFile(@RequestParam("filename") String fileName){
        try {
            return TransformUtil.okFile(testService.downloadFile(fileName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/pdf")
    public String uploadFile() throws Exception {
        testService.creeatePdf();
        return "OK";
    }
}
