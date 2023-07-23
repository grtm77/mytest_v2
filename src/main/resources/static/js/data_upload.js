//207
$("#tdata207").click(function () {
    $.ajax({
        "url": "hh207",
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

//461
$("#tdata461").click(function () {
    $.ajax({
        "url": "hh461",
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

//1052
$("#tdata1052").click(function () {
    $.ajax({
        "url": "hh1052",
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

//1361
$("#tdata1361").click(function () {
    $.ajax({
        "url": "hh1361",
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

//g114
$("#tdatag114").click(function () {
    $.ajax({
        "url": "hhg114",
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

//g228
$("#tdatag228").click(function () {
    $.ajax({
        "url": "hhg228",
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

//g354
$("#tdatag354").click(function () {
    $.ajax({
        "url": "hhg354",
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

//g484
$("#tdatag484").click(function () {
    $.ajax({
        "url": "hhg484",
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