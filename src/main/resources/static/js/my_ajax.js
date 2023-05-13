// 生成数据，保存到数据库 新增
$("#save").click(function () {
    var flag_cros = $("#nodeType").val() == "cros" ? 0 : 1;
    for (var i = flag_cros; i < pts.length; i++) {
        var a = [];
        a.push(pts[i].lng);
        a.push(pts[i].lat);
        str.push(a);
    }

    pts = [];
    var roadName = $("#roadName").val();
    var points = str;
    var pointType = $("#nodeType").val();

    $.ajax({
        "url": "hha",
        "type": "post",
        "data": {
            "roadName": roadName,
            "points": points,
            "pointType": pointType
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
            layer.msg("请求失败")
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
            layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算 有向贪心 （计算路口）新增
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
            layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算 有向贪心 （计算路口）新增
$("#calFive").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "hhfive",
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
            layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算 蚁群算法 新增
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
            layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算新线性规划 新增
$("#calPulp").click(function () {
    // 清楚地图上已经标记的点
    map.clearOverlays();
    var loading = layer.load('Loading...', {
        shade: [0.5,'#fff'] //0.1透明度的白色背景
    });
    $.ajax({
        "url": "calByPython",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            layer.close(loading);
            layer.msg("网关绘制完成");
            drawPoints(response);
        },
        "error": function (response) {
            layer.close(loading);
            layer.msg("请求失败")
        }
    });
});

// 生成完数据后，发送请求计算遗传算法 新增
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
            layer.msg("请求失败")
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
            layer.msg("请求失败")
        }
    });
});

// 格式化 实现
$("#gshIt").click(function () {
    //清空所有存放数据的数组
    all_sensor = [];
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

//232 新增
$("#tdata232").click(function () {
    $.ajax({
        "url": "hh232",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//466 新增
$("#tdata466").click(function () {
    $.ajax({
        "url": "hh466",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//518 新增
$("#tdata1056").click(function () {
    $.ajax({
        "url": "hh1056",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//1500
$("#tdata1500").click(function () {
    $.ajax({
        "url": "hh1500",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//2322 新增
$("#tdata2322").click(function () {
    $.ajax({
        "url": "hh2322",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//4662 新增
$("#tdata4662").click(function () {
    $.ajax({
        "url": "hh4662",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//5182 新增
$("#tdata10562").click(function () {
    $.ajax({
        "url": "hh10562",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//15002
$("#tdata15002").click(function () {
    $.ajax({
        "url": "hh15002",
        "type": "post",
        "success": function (response) {
            layer.msg("The data is loaded in database successfully!");
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
                var point2 = new BMap.Point(gateways[i][2], gateways[i][3]);
                var marker2 = new BMap.Marker(point2, {icon: gIcon});
                map.addOverlay(marker2);
                marker2.setTop(true);
            }
            layer.msg("网关绘制完成");
        }
    });
});

//sensor数据库读取 新增
$("#sensorupload").click(function () {
    $.ajax({
        "url": "sup",
        "type": "post",
        "success": function (response) {
            var sensors = response.data;//拿到的sensor列表
            console.log(sensors);
            var xx1 = sensors[0][2];
            var yy1 = sensors[0][3];
            var last_str = sensors[sensors.length - 1];
            var xx2 = last_str[2];
            var yy2 = last_str[3];
            var angle = Math.atan2((yy1 - yy2), (xx2 - xx1)); //弧度  0.6435011087932844
            var theta = angle * (180 / Math.PI);  //角度  36.86989764584402
            //在地图上标记
            for (var i = 0; i < sensors.length; i++) {
                var point1 = new BMap.Point(sensors[i][2], sensors[i][3]);
                var marker1 = new BMap.Marker(point1, {icon: sIcon});
                // var marker0 = new BMap.Marker(point1, {icon: rIcon});
                // marker0.setRotation(theta);
                // map.addOverlay(marker0);
                map.addOverlay(marker1);
            }
            layer.msg("传感器节点绘制完成");
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