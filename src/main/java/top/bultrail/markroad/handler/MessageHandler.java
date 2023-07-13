package top.bultrail.markroad.handler;

import top.bultrail.markroad.pojo.Point;
import top.bultrail.markroad.pojo.QuickSave;
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

    // 一键保存
    @ResponseBody
    @RequestMapping(value = "/hhquicksave", method = RequestMethod.POST)
    public ResultEntity<String> quicksave(QuickSave quick_save) {
        // 将数据保存在数据库
        try {
            transformService.quicksaveDB(quick_save);
            return ResultEntity.successWithoutData();
//            if (quick_save.getCross_points() == null) {
//                return ResultEntity.failed("You haven't set marks yet！");
//            } else {
//                transformService.quicksaveDB(quick_save);
//                return ResultEntity.successWithoutData();
//            }
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }
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

    //备份
    @ResponseBody
    @RequestMapping(value = "/hhback", method = RequestMethod.POST)
    public ResultEntity<String> backup() {
        transformService.bkDB();
        return ResultEntity.successWithoutData();
    }

    //1361
    @ResponseBody
    @RequestMapping(value = "/hh1361", method = RequestMethod.POST)
    public ResultEntity<String> t1361() {
        transformService.td1361();
        return ResultEntity.successWithoutData();
    }

    //1052
    @ResponseBody
    @RequestMapping(value = "/hh1052", method = RequestMethod.POST)
    public ResultEntity<String> t1052() {
        transformService.td1052();
        return ResultEntity.successWithoutData();
    }

    //207
    @ResponseBody
    @RequestMapping(value = "/hh207", method = RequestMethod.POST)
    public ResultEntity<String> t207() {
        transformService.td207();
        return ResultEntity.successWithoutData();
    }

    //461
    @ResponseBody
    @RequestMapping(value = "/hh461", method = RequestMethod.POST)
    public ResultEntity<String> t461() {
        transformService.td461();
        return ResultEntity.successWithoutData();
    }

    //g484
    @ResponseBody
    @RequestMapping(value = "/hhg484", method = RequestMethod.POST)
    public ResultEntity<String> tg484() {
        transformService.tdGuo(484);
        return ResultEntity.successWithoutData();
    }

    //g354
    @ResponseBody
    @RequestMapping(value = "/hhg354", method = RequestMethod.POST)
    public ResultEntity<String> tg354() {
        transformService.tdGuo(354);
        return ResultEntity.successWithoutData();
    }

    //114
    @ResponseBody
    @RequestMapping(value = "/hhg114", method = RequestMethod.POST)
    public ResultEntity<String> tg114() {
        transformService.tdGuo(114);
        return ResultEntity.successWithoutData();
    }

    //228
    @ResponseBody
    @RequestMapping(value = "/hhg228", method = RequestMethod.POST)
    public ResultEntity<String> tg228() {
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
            strings = transformService.calAlgorithm("Greedy");
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
        try {
//            strings = transformService.calByLinner_upload(flag);
            strings = transformService.calByLinner_upload(flag);
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            System.out.println("Exception");
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    //用分支限界算法计算，数据保存在txt
    @ResponseBody
    @RequestMapping(value = "/hhbb", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByBB(@RequestParam(value = "crosFlag") String flag) throws Exception {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calAlgorithm("BB");
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            System.out.println("Exception");
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    //用线性规划计算，数据保存在txt
    @ResponseBody
    @RequestMapping(value = "/hhLP", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByLP(@RequestParam(value = "crosFlag") String flag) throws Exception {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calAlgorithm("LP");
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            System.out.println("Exception");
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    // 蚁群算法计算
    @ResponseBody
    @RequestMapping(value = "/hhaco", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByAco(@RequestParam(value = "crosFlag") String flag) throws Exception {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calAlgorithm("Aco");
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            System.out.println("Exception");
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

    //使用遗传算法计算
    @ResponseBody
    @RequestMapping(value = "/calByGA", method = RequestMethod.POST)
    public ResultEntity<HashMap<String, List<List<String>>>> calByGA(@RequestParam(value = "crosFlag") String flag) {
        HashMap<String, List<List<String>>> strings = null;
        try {
            strings = transformService.calAlgorithm("GA");
            System.out.println("Success");
        } catch (Exception e) {
            ResultEntity<HashMap<String, List<List<String>>>> listResultEntity = new ResultEntity<>();
            listResultEntity.setMessage(e.getMessage());
            return listResultEntity;
        }
        return ResultEntity.sucessWithData(strings);
    }

}
