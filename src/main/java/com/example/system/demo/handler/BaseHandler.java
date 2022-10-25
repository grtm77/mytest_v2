package com.example.system.demo.handler;

import com.example.system.demo.calculate.DBRelation;
import com.example.system.demo.config.RelatedProperties;
import com.example.system.demo.pojo.User;
import com.example.system.demo.service.BaseService;
import com.example.system.demo.service.TransformService;
import com.example.system.demo.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class BaseHandler {
    @Autowired
    TransformService transformService;

    @Autowired
    DBRelation dbRelation;

    @Autowired
    RelatedProperties relatedProperties;
    @Autowired
    BaseService baseService;

    @RequestMapping("/sup")
    @ResponseBody
    public ResultEntity<List<List<String>>> sensorUpload() {
        List<List<String>> sensorList = dbRelation.readSensor();
        return ResultEntity.sucessWithData(sensorList);
    }

    @RequestMapping("/gup")
    @ResponseBody
    public ResultEntity<List<List<String>>> gatewayUpload() {
        List<List<String>> gatewayList = dbRelation.readGateway();
        return ResultEntity.sucessWithData(gatewayList);
    }

    @RequestMapping("/cup")
    @ResponseBody
    public ResultEntity<List<List<String>>> crossingUpload() {
        List<List<String>> crossingList = dbRelation.readCrossing();
        return ResultEntity.sucessWithData(crossingList);
    }

}
