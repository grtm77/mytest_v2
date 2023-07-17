// 生成数据，保存到数据库
// $("#save").click(function () {
//     var flag_cros = $("#nodeType").val() == "cros" ? 0 : 1;
//     for (var i = flag_cros; i < pts.length; i++) {
//         var a = [];
//         a.push(pts[i].lng);
//         a.push(pts[i].lat);
//         str.push(a);
//     }
//
//     pts = [];
//     var roadName = $("#roadName").val();
//     var points = str;
//     var pointType = $("#nodeType").val();
//
//     $.ajax({
//         "url": "hha",
//         "type": "post",
//         "data": {
//             "roadName": roadName,
//             "points": points,
//             "pointType": pointType
//         },
//         "dataType": "json",
//         "success": function (response) {
//             layer.msg("Success!")
//             pointArr = [];
//             str = [];
//         },
//         "error": function (response) {
//             layer.msg("Error!")
//         }
//     });
// });

// 一键保存所有节点信息，用于之后计算
$("#save").click(function () {
    // 交叉路口节点数据
    var cross_points = [];
    for (var i = 0; i < cross.length; i++) {
        var a = [];
        a.push(cross[i].lng);
        a.push(cross[i].lat);
        cross_points.push(a);
    }
    // sensor节点数据
    var sensor_array = []
    for (var i = 0; i < all_sensor.length; i++) {
        let sensor_points = [];
        for(var j=0; j< all_sensor[i].length;j++){
            let a = [];
            a.push(all_sensor[i][j].lng);
            a.push(all_sensor[i][j].lat);
            sensor_points.push(a)
        }
        sensor_array.push(sensor_points)
    }
    // 网关节点数据
    var gateway_array = []
    for (var i = 0; i < all_gateway.length; i++) {
        let gateway_points = [];
        for(var j=0; j< all_gateway[i].length;j++){
            let a = [];
            a.push(all_gateway[i][j].lng);
            a.push(all_gateway[i][j].lat);
            gateway_points.push(a)
        }
        gateway_array.push(gateway_points)
    }

    $.ajax({
        "url": "hhquicksave",
        "type": "post",
        "data": {
            "cross_points": cross_points,
            "gateway_array": gateway_array,
            "sensor_array": sensor_array,
            // "roadName": roadName,
        },
        "dataType": "json",
        "success": function (response) {
            layer.msg("Success!")
            pointArr = [];
            str = [];
        },
        "error": function (response) {
            layer.msg("Error!")
        }
    });
});


// 生成完数据后，发送请求计算 贪心算法 （计算路口）实现
$("#calGreedy").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "hhe",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
        },
        "error": function (response) {
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });

});

// 生成完数据后，发送请求计算 有向贪心 （计算路口）新增
$("#calLinner").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "hhln",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
        },
        "error": function (response) {
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算 分支限界
$("#calBB").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "hhbb",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
        },
        "error": function (response) {
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算 线性规划
$("#calLP").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "hhLP",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
        },
        "error": function (response) {
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算 蚁群算法
$("#calAco").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "hhaco",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
        },
        "error": function (response) {
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算遗传算法
$("#calGA").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "calByGA",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
        },
        "error": function (response) {
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算 贪心算法 （不计算路口）实现
$("#calGreedyWithOutCros").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "hhe",
        "type": "post",
        "data": {
            "crosFlag": "withoutCros"
        },
        "success": function (response) {
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
        },
        "error": function (response) {
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });
});

// 格式化 实现
$("#gshIt").click(function () {
    //清空所有存放数据的数组
    all_sensor = [];
    all_gateway = [];
    cross = [];
    all = [];
    pointArr = [];
    str = [];
    pts = [];
    // 清除地图上已经标记的点
    map.clearOverlays();
});

//清空数据表 新增
$("#confirm").click(function () {
    $.ajax({
        "url": "hhcl",
        "type": "post",
        "success": function (response) {
            layer.msg("Success!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//排序 新增
$("#sortconfirm").click(function () {
    $.ajax({
        "url": "hhst",
        "type": "post",
        "success": function (response) {
            layer.msg("Success!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//备份 新增
$("#backup").click(function () {
    $.ajax({
        "url": "hhback",
        "type": "post",
        "success": function (response) {
            layer.msg("Success!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//还原 新增
$("#restore").click(function () {
    $.ajax({
        "url": "hhres",
        "type": "post",
        "success": function (response) {
            layer.msg("Success!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

// 绘图时需要使用
function sensorupload() {
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "sup",
        "type": "post",
        "success": function (response) {
            var sensors = response.data;//拿到的sensor列表
            console.log(sensors);
            //在地图上标记
            for (var i = 0; i < sensors.length; i++) {
                var point1 = new BMap.Point(sensors[i][3], sensors[i][4]);
                var marker1 = new BMap.Marker(point1, {icon: sIcon});
                map.addOverlay(marker1);
            }
            layer.close(loading);
            layer.msg("传感器节点绘制完成");
        }
    });
};

//sensor数据库读取 新增
$("#sensorupload").click(function(){sensorupload()});

//gateway数据库读取 新增
$("#gatewayupload").click(function () {
    $.ajax({
        "url": "gup",
        "type": "post",
        "success": function (response) {
            var gateways = response.data;//拿到的gateway列表
            // console.log(gateways);
            //在地图上标记
            for (var i = 0; i < gateways.length; i++) {
                var point2 = new BMap.Point(gateways[i][3], gateways[i][4]);
                // var marker2 = new BMap.Marker(point2, {icon: gIcon});
                var marker2 = new BMap.Marker(point2, {icon: gIcon_rec32});
                // var marker2 = new BMap.Marker(point2, {icon: gIcon_rec16});
                map.addOverlay(marker2);
                marker2.setTop(true);
            }
            layer.msg("网关绘制完成");
        }
    });
});

//crossing数据库读取 新增
$("#crosupload").click(function () {
    $.ajax({
        "url": "cup",
        "type": "post",
        "success": function (response) {
            var crossing = response.data;//拿到的cros列表
            // console.log(crossing);
            //在地图上标记
            for (var i = 0; i < crossing.length; i++) {
                var point3 = new BMap.Point(crossing[i][2], crossing[i][3]);
                var marker3 = new BMap.Marker(point3, {icon: cIcon});
                map.addOverlay(marker3);
            }
            layer.msg("路口绘制完成");
        }
    });
});