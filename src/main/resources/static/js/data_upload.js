//232 新增
$("#tdata232").click(function () {
    $.ajax({
        "url": "hh232",
        "type": "post",
        "success": function (response) {
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(113.80634855083106, 22.816818813420948);// lyj标注位置
            map.centerAndZoom(data_point, 19);
            sensorupload();
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
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(113.80612846598119, 22.817647453549856);
            map.centerAndZoom(data_point, 19);
            sensorupload();
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//1056 新增
$("#tdata1056").click(function () {
    $.ajax({
        "url": "hh1056",
        "type": "post",
        "success": function (response) {
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(113.8062048219495, 22.816673072167394);
            map.centerAndZoom(data_point, 19);
            sensorupload();
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
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(113.80726931397848,22.816231683408517);
            map.centerAndZoom(data_point, 19);
            sensorupload();
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//232 2 新增
$("#tdata2322").click(function () {
    $.ajax({
        "url": "hh2322",
        "type": "post",
        "success": function (response) {
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(112.8835, 22.9082);
            map.centerAndZoom(data_point, 19);
            sensorupload();
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//466 2 新增
$("#tdata4662").click(function () {
    $.ajax({
        "url": "hh4662",
        "type": "post",
        "success": function (response) {
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(112.8835, 22.9082);
            map.centerAndZoom(data_point, 19);
            sensorupload();
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//518 2 新增
$("#tdata10562").click(function () {
    $.ajax({
        "url": "hh10562",
        "type": "post",
        "success": function (response) {
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(112.8835, 22.9082);
            map.centerAndZoom(data_point, 19);
            sensorupload();
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});

//1500 2
$("#tdata15002").click(function () {
    $.ajax({
        "url": "hh15002",
        "type": "post",
        "success": function (response) {
            // 清楚地图上已经标记的点
            map.clearOverlays();
            // layer.msg("The data is loaded in database successfully!");
            var data_point = new BMap.Point(112.8835, 22.9082);
            map.centerAndZoom(data_point, 19);
            sensorupload();
        },
        "error": function (response) {
            layer.msg("Error!");
        }
    });
});