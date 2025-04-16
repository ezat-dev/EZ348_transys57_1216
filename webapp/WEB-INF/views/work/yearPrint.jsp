<%@ page session="true" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>열처리 5~7호기</title>
<jsp:include page="../include/pluginpage.jsp"/>

    <style>
        body {
            text-align: center;
        }
        hr {
            width: 95%;
            margin: 10px 2.5% 2% 2.5%;
        }
        label > select {
            width: 150px;
            height: 30px;
            font-size: 14pt;
        }
        
        label > input {
         	width: 150px;
            height: 28px;
            font-size: 14pt;
        }

        
        
        #menu_bar {
            padding-top: 3%;
            background: #123478;
        }
        .list_input {
            text-align: center;
            margin: 1% 5%;
            padding: 10px 0%;
            width: 90%;
            border: 3px solid #F4EFEA;
        }
        legend {
            border: 0px;
            width: 7%;
            font-size: 15px;
            margin-bottom: 0px;
            padding-left: 2%;
            text-align: left;
        }
        .input_d {
            font-size: 14pt;
            font-weight: 700;
            color: black;
            height: 35px;
        }
        #placename {
            font-size: 14pt;
            text-align: center;
            font-weight: 700;
            width: 150px;
        }
        div > button {
            width: 120px;
            height: 30px;
            margin-left: 5px;
            border: 1px solid black; 
            color: #123478;
            font-size: 14pt;
            font-weight: 700;
        }
        div > button:hover {     
            background: #798cb3;
            border: 1px solid #798cb3;
            color: white; 
        }
        .text-center {
            font-size: 16px;
        }
        .countDATA {
            width: 90%;
            text-align: right;
            color: black;
            margin: 0 auto;
            margin-bottom: 3px;
            font-size: 15pt;
        }
        #table_file {
            width: 90%;
            text-align: center;
            margin: auto;
            max-height: 40%;
            overflow: auto;
        }
        #qr_memo {
            width: 60%;
            text-align: center;
        }
        .del_btn {
            background: transparent;
            border: 0px;
            color: #123478;
            font-size: 20px;
        }
        .NO_list {
            text-align: center;
        }
        #back_div {
            text-align: right;
            padding-right: 2%;
            padding-top: 2%;
        }
        #back_btn {
            float: right;
            background-color: transparent;
            border: 0px;
            color: #123478;
        }
        #table_file {
            -ms-overflow-style: none;
            height: 650px;
        }
        #table_file::-webkit-scrollbar { display: none; }
        #cate_list {
            border-collapse: collapse;
        }
        #cate_list th {
            position: sticky;
            top: 0;
        }
    </style>
</head>

<body data-offset="60" data-target=".navbar" style="overflow-y: hidden">
<div id="wrap">
    <div id="body2">
		<div id="menu_bar">
			<jsp:include page="../include/headerPopup.jsp"/>
		</div>
        <div id="contents">
            <div style="color: black; font-size: 14px; padding-top: 1%; margin-left: 2.5%; text-align: left;">
                <b style="font-size: 15pt;">작업실적</b> / <label style="font-size: 14pt;">작업연보 인쇄</label>
            </div>
            <hr>
            <fieldset class="list_input">
                <legend style="font-size: 15pt;">검색조건</legend>
                <div class="input_d">
					<!--
					<label> 설비명 : 
					    <select name="placename" id="placename">
					        <option value="">전체</option>
					        <option value="1">1호기</option>
					        <option value="2">2호기</option>
					        <option value="3">3호기</option>
					        <option value="4">4호기</option>
					    </select>
					</label>
					-->


                    <label style="margin-left: 15px;"> 작업일자 : 
					    <input type="text" class="yearSet" id="to_date" 
					    name="to_date" style="font-size: 14pt; font-weight: 700; text-align: center; width: 150px;" placeholder=""/>
					    
					</label>

                    <button id="searchbtn" style="margin-left: 100px;">조회</button>
                
                    <button id="excelBtn">엑셀</button>

                </div>
            </fieldset>
            <div id="table_file">
                <div class="countDATA">조회된 데이터 수 : </div>
                <div id="tabulator-table"></div> <!-- Tabulator가 테이블을 렌더링할 div -->
            </div>
        </div>
    </div>
</div>

<script>
    // Tabulator 테이블 설정
    var tableData = []; 
    var selectedDate = "";  // selectedDate를 글로벌 변수로 선언

    var table = new Tabulator("#tabulator-table", {
        height: 650,
        data: tableData, 
        layout:"fitColumns",
        selectable:true,    //로우 선택설정
        tooltips:true,
        selectableRangeMode:"click",
        reactiveData:true,
        headerHozAlign:"center",
        columns: [
        	{ title: "일자", field: "date_feat", width: 320, hozAlign:"center"},
            { title: "품명", field: "pumname", width: 320, hozAlign:"center"},
            { title: "품명코드", field: "pumcode", width: 320, hozAlign:"center"},
            { title: "기종", field: "gijong", width: 220, hozAlign:"center"},
            { title: "Cycle", field: "cycleno", width: 140, hozAlign:"center"},
            { title: "가동시간", field: "tray_time", width: 140, hozAlign:"center"},
            { title: "Tray", field: "cnt", width: 140, hozAlign:"center"},
            { title: "생산수량", field: "total_cnt", width: 145, hozAlign:"center"},
            { title: "검사", field: "check_cnt", width: 140, hozAlign:"center"},
            { title: "합계", field: "total_cnt", width: 145, hozAlign:"center"},
        ],
        placeholder: "검색 결과가 없습니다.", 
    });

    // 검색 버튼 클릭 이벤트
    document.getElementById("searchbtn").addEventListener("click", function() {
        // 선택한 날짜와 설비명 가져오기
        selectedDate = $("#to_date").val().replace(/-/g, "");  // 하이픈 제거
        var selectedHogi = $("#placename").val() || ""; // 설비명이 비어있을 경우 빈 문자열로 설정

        // 콘솔에 출력
        console.log("선택한 날짜:", selectedDate);
     
        // Ajax 요청
        $.ajax({
            url: "/transys/work/yearPrint/list", 
            method: "POST",
            data: {
                p_DATE: selectedDate // 전달할 날짜 (하이픈이 제거된 날짜)
            },
            success: function(response) {
                // 서버 응답에서 data 배열만 추출하여 Tabulator에 전달
                table.setData(response.data); 
                document.querySelector(".countDATA").textContent = "조회된 데이터 수 : " + response.data.length;
                console.log("서버에서 받아온 데이터:", response.data);
            },
            error: function() {
                alert("데이터를 가져오는 데 실패했습니다.");
            }
        });

        // Ajax 요청에 사용될 매개변수 출력
        console.log("서버로 전송할 값:", {
            date: selectedDate
        });
    });

    $(document).ready(function() {
        var now = new Date();
        var y = now.getFullYear();  // 현재 년도만 가져오기

        // "yyyy" 형식으로 설정
        $("#to_date").val(y);  // 현재 연도로 설정

        // 페이지 로드 시 자동으로 검색 수행
        $("#searchbtn").trigger("click");
    });


 // 엑셀 다운로드 버튼 클릭 이벤트
    $("#excelBtn").on("click", function () {
        console.log("엑셀 보내지는 날:", selectedDate); // selectedDate로 수정

        $.ajax({
            url: "/transys/work/yearPrint/excelDownload",
            type: "post",
            dataType: "json",
            data: {
                p_DATE: selectedDate 
            },
            success: function (result) {
                console.log(result);

 
                alert("D:\\생산일지\\연간생산일지 저장 완료되었습니다.");
            },
            error: function (xhr, status, error) {
        
                alert("엑셀 다운로드 중 오류가 발생했습니다. 다시 시도해주세요.");
                console.error("Error:", error);
            }
        });
    });

</script>


<form name="parmForm" method="post">
    <input type="hidden" id="chk1" name="chk1"/>
    <input type="hidden" id="tdate1" name="tdate1"/>
    <input type="hidden" id="pcode1" name="pcode1"/>
</form>
</body>
</html>
