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

    @RequestMapping(value = "/do/login", method = RequestMethod.GET)
    public String doLogin(User user) {

        boolean b = baseService.doLogin(user);
        if (b) {
            return "redirect:/map";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "do/register", method = RequestMethod.GET)
    public String doregister(User user) {
        baseService.doRegister(user);
        return "redirect:/";
    }


    @RequestMapping("/uploadSensor")
    @ResponseBody
    public ResultEntity uploadSensor(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultEntity.failed("没有文件！");
        }
        String fileName = file.getOriginalFilename();
        String filePath = relatedProperties.getSensorPath();
        File dest = new File(filePath +"/"+ fileName);
        try {
            file.transferTo(dest);
            System.out.println("上传成功");
            List<String> sensorList = transformService.collectSensorFile();
            return ResultEntity.sucessWithData(sensorList);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ResultEntity.failed("上传失败");
    }

    @RequestMapping("/sup")
    @ResponseBody
    public ResultEntity<List<List<String>>> sensorUpload() {
        List<List<String>> sensorList = dbRelation.readSensor();
        return ResultEntity.sucessWithData(sensorList);
    }

    @RequestMapping("/uploadGateway")
    @ResponseBody
    public ResultEntity uploadGateway(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultEntity.failed("没有文件！");
        }
        String fileName = file.getOriginalFilename();
        String filePath = relatedProperties.getGatewayPath();
        File dest = new File(filePath +"\\"+ fileName);
        try {
            file.transferTo(dest);
            System.out.println("上传成功");
            List<String> gatewayList = transformService.collectGatewayFile();
            return ResultEntity.sucessWithData(gatewayList);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ResultEntity.failed("上传失败");
    }

    @RequestMapping("/uploadCrossing")
    @ResponseBody
    public ResultEntity uploadCrossing(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultEntity.failed("没有文件！");
        }
        String fileName = file.getOriginalFilename();
        String filePath = relatedProperties.getCrossingPath();
        File dest = new File(filePath +"\\"+ fileName);
        try {
            file.transferTo(dest);
            System.out.println("上传成功");
            List<String> crosList = transformService.collectCrossingFile();
            return ResultEntity.sucessWithData(crosList);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return ResultEntity.failed("上传失败");
    }



}
