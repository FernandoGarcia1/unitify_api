package com.tt.unitify.modules.departments;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
@Log4j2
@RestController
@RequestMapping("/department")
public class DepartmetController {



    @Autowired
    DepartmentService departmentService;
    @PostMapping
    public String createGuard(@RequestBody DepartmentDto department) throws ExecutionException, InterruptedException {
        log.info("Department: {}", department);
        log.info("name: {}", department.getName());
        log.info("idBuilding: {}", department.getIdBuilding());
        return departmentService.create(department);
    }
}
