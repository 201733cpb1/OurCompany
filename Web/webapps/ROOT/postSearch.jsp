<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<html>

<head>
    <title>DaumPost</title>
</head>

<body leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">

    <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>

    <div id="layer" style="width:100%;height:500px;"></div>

    <script>
        // 우편번호 찾기 iframe을 넣을 element
        var element = document.getElementById('layer');

        new daum.Postcode({

            oncomplete: function(data) {
                var Addr;
              
                Addr = data.roadAddress;

                window.android.setAddress(Addr);

            },
            width: '100%',
            height: '100%'
        }).embed(element);
    </script>

</body>

</html>
