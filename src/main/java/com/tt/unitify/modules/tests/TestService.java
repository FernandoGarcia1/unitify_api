package com.tt.unitify.modules.tests;

import com.google.firebase.database.utilities.Pair;
import com.tt.unitify.modules.utils.FirebaseUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Log4j2
public class TestService {



    String uploadFile(MultipartFile file, String fileName) throws IOException {
        return FirebaseUtil.uploadFile(file.getBytes(), fileName);
    }



    public Pair<String, byte[]> downloadFile(String fileName){
        byte[]  file =FirebaseUtil.downloadFile(fileName);
        return new Pair<>(fileName,file);
    }


}
