package top.bultrail.markroad.handler;

import top.bultrail.markroad.pojo.Point;
import top.bultrail.markroad.service.TransformService;
import top.bultrail.markroad.util.ResultEntity;
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

    //保存数据 实现
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

    //清空数据库 新增
    @ResponseBody
    @RequestMapping(value = "/hhcl", method = RequestMethod.POST)
    public ResultEntity<String> clear() {
        transformService.clearDB();
        return ResultEntity.successWithoutData();

    }

    //排序 新增
    @ResponseBody
    @RequestMapping(value = "/hhst", method = RequestMethod.POST)
    public ResultEntity<String> sort() {
        transformService.sortDB();
        return ResultEntity.successWithoutData();
    }

    //备份 新增
    @ResponseBody
    @RequestMapping(value = "/hhback", method = RequestMethod.POST)
    public ResultEntity<String> backup() {
        transformService.bkDB();
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping(value = "/hh1500", method = RequestMethod.POST)
    public ResultEntity<String> t1500() {
        transformService.td1500();
        return ResultEntity.successWithoutData();
    }

    //162 新增
    @ResponseBody
    @RequestMapping(value = "/hh1056", method = RequestMethod.POST)
    public ResultEntity<String> t1056() {
        transformService.td1056();
        return ResultEntity.successWithoutData();
    }

    //321 新增
    @ResponseBody
    @RequestMapping(value = "/hh232", method = RequestMethod.POST)
    public ResultEntity<String> t232() {
        transformService.td232();
        return ResultEntity.successWithoutData();
    }

    //518 新增
    @ResponseBody
    @RequestMapping(value = "/hh466", method = RequestMethod.POST)
    public ResultEntity<String> t466() {
        transformService.td466();
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping(value = "/hh15002", method = RequestMethod.POST)
    public ResultEntity<String> t15002() {
        transformService.tdGuo(484);
        return ResultEntity.successWithoutData();
    }

    //162 新增
    @ResponseBody
    @RequestMapping(value = "/hh10562", method = RequestMethod.POST)
    public ResultEntity<String> t10562() {
        transformService.tdGuo(354);
        return ResultEntity.successWithoutData();
    }

    //321 新增
    @ResponseBody
    @RequestMapping(value = "/hh2322", method = RequestMethod.POST)
    public ResultEntity<String> t2322() {
        transformService.tdGuo(114);
        return ResultEntity.successWithoutData();
    }

    //518 新增
    @ResponseBody
    @RequestMapping(value = "/hh4662", method = RequestMethod.POST)
    public ResultEntity<String> t4662() {
        transformService.tdGuo(228);
        return ResultEntity.successWithoutData();
    }

    //还原 新增
    @ResponseBody
    @RequestMapping(value = "/hhres", method = RequestMethod.POST)
    public ResultEntity<String> restore() {
        transformService.resDB();
        return ResultEntity.successWithoutData();
    }

    //用贪心算法计算，数据保存在txt 实现
    @ResponseBody
    @RequestMapping(value = "/hhe", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByGreedy(@RequestParam(value = "crosFlag") String flag) {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByGreedy_upload(flag);
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            System.out.println("Exception");
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    //用有向贪心算法计算，数据保存在txt 实现
    @ResponseBody
    @RequestMapping(value = "/hhln", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByLinner(@RequestParam(value = "crosFlag") String flag) throws Exception {
        HashMap<String, List<List<String>>> strings = null;
//        try {
            strings = transformService.calByLinner_upload(flag);
            System.out.println("Success");
//        } catch (Exception e) {
//            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
//            System.out.println("Exception");
//            listResultEntity.setMessage(e.getMessage());
//            return listResultEntity;
//        }
        return ResultEntity.sucessWithData(strings);
    }

    @ResponseBody
    @RequestMapping(value = "/hhaco", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByAco(@RequestParam(value = "crosFlag") String flag) throws Exception {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByAco_upload(flag);
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            System.out.println("Exception");
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }


    //使用python脚本计算 正在实现
    @ResponseBody
    @RequestMapping(value = "/calByPython", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByPython(@RequestParam(value = "crosFlag") String flag) {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByPython_upload(flag);
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    @ResponseBody
    @RequestMapping(value = "/calByGA", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByGA(@RequestParam(value = "crosFlag") String flag) {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calByGA_upload(flag);
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

}
