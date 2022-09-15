package com.example.system.demo.handler;

import com.example.system.demo.pojo.Point;
import com.example.system.demo.service.TransformService;
import com.example.system.demo.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class MessageHandler {
    @Autowired
    TransformService transformService;

    //保存数据
    @ResponseBody
    @RequestMapping(value = "/hha", method = RequestMethod.POST)
    public ResultEntity<String> save02(Point point) {
        // 将数据保存在数据库
        try {
            if (point.getPoints() == null) {
                return ResultEntity.failed("You haven't set marks yet！");
            } else {
                transformService.saveDB(point);
                return ResultEntity.successWithoutData();
            }
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    //清空数据库
    @ResponseBody
    @RequestMapping(value = "/hhcl", method = RequestMethod.POST)
    public ResultEntity<String> clear() {
        transformService.clearDB();
        return ResultEntity.successWithoutData();

    }

    //用贪心算法计算，数据保存在txt，计算
    @ResponseBody
    @RequestMapping(value = "/hhe", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByGreedy(@RequestParam(value = "crosFlag") String flag) {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByGreedy_upload(flag);
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }


    //将txt文件,与redis的数据删除掉
    @ResponseBody
    @RequestMapping(value = "/hho", method = RequestMethod.POST)
    public ResultEntity<List<String>> tets04() {
        transformService.deleteFile();
        return ResultEntity.successWithoutData();
    }

    //使用python脚本计算
    @ResponseBody
    @RequestMapping(value = "/calByPython", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByPython(@RequestParam(value = "crosFlag") String flag) {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByPython_upload(flag);
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    //用贪心算法计算，数据保存在redis 摄像头
    @ResponseBody
    @RequestMapping(value = "/calByGreedyCm", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByGreedyCm() {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByGreedyCm();
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    //使用python脚本计算 摄像头
    @ResponseBody
    @RequestMapping(value = "/calByPythonCm", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByPythonCm() {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByPythonCm();
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }
}
