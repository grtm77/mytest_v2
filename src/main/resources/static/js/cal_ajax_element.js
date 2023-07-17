function calLinner() {
    // 在这里调用你的函数
    console.log('按钮被点击了');
    // // 或者调用一个自定义的函数
    // calLinner_raw();
    //
    console.log('自定义函数被调用了');
    // 清楚地图上已经标记的点
    map.clearOverlays();

    var loading = layer.load('Loading...', {
        shade: [0.5, '#fff'] //0.1透明度的白色背景
    });
    console.log('自定义函数被调用了');
    var self = this; // 存储 this 的引用
    $.ajax({
        "url": "hhln",
        "type": "post",
        "data": {
            "crosFlag": "withCros"
        },
        "success": function (response) {
            console.log('自定义函数被调用了');
            drawPoints(response);
            layer.close(loading);
            layer.msg("网关绘制完成");
            self.dialogVisible = false;
        },
        "error": function (response) {
            console.log('自定义函数被调用了');
            layer.close(loading);
            var mess = response.responseJSON.message;
            if (mess != null)
                layer.msg(mess)
            else layer.msg("请求失败")
        }
    });
}

