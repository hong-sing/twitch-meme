let post = {
    init : function () {
        let _this = this;
        $('#saveMeme').on('click', function () {
            _this.goToMemeSavePage();
        });
        $('#save').on('click', function () {
            if (_this.isContent()) {
                _this.save();
            }
        });
        $('#updateForm').on('click', function () {
            _this.updateForm();
        });
        $('#update').on('click', function () {
            _this.update();
        });
        $('#delete').on('click', function () {
            if (confirm('정말 삭제하시겠습니까?')) {
                _this.delete();
            }
        });
        $('#addGood').on('click', function () {
            _this.addGood();
        });
        $('#cancelGood').on('click', function () {
            _this.cancelGood();
        });
    },
    goToMemeSavePage : function () {
        let broadcastId = $('#streamerLogin').val();
        let isLogin = $('#isLogin').val();
        if (isLogin != "" && isLogin != null) {
            window.location.href = '/meme/post-save/' + broadcastId;
        } else {
            alert("로그인 후 이용해주세요");
        }
    },
    updateForm : function () {
        let postId = $('#postId').val();
        window.location.href = '/meme/post-update/' + postId;
    },
    save : function () {
        let description = $('#summernote').summernote('code');
        let youtubeIframe = $(description).find('iframe[src*=youtube]');
        let reference = null;
        let broadcastId = $('#broadcastId').val();
        let array = new Array();
        if (youtubeIframe.length > 0) {
            $.each(youtubeIframe, function (idx, value) {
                let iframe = $(value);
                reference = iframe.attr('src').replace('//www.youtube.com/embed/', 'https://www.youtube.com/watch?v=');
                array.push(reference);
            });
        }

        let data = {
            memberId: $('#member_id').val(),
            title: $('#meme').val(),
            summary: $('#summary').val(),
            broadcastId: broadcastId,
            content: description,
            reference: array
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/post',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            window.location.href = '/meme/post/' + broadcastId;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    },
    update : function () {
        let description = $('#summernote').summernote('code');
        let youtubeIframe = $(description).find('iframe[src*=youtube]');
        let reference = null;
        let broadcastId = $('#broadcastId').val();
        let postId = $('#postId').val();
        let array = new Array();
        if (youtubeIframe.length > 0) {
            $.each(youtubeIframe, function (idx, value) {
                let iframe = $(value);
                reference = iframe.attr('src').replace('//www.youtube.com/embed/', 'https://www.youtube.com/watch?v=');
                array.push(reference);
            });
        }

        let data = {
            title: $('#meme').val(),
            summary: $('#summary').val(),
            content: description,
            reference: array
        };

        $.ajax({
            type: 'PUT',
            url: '/api/v1/post/' + postId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('성공');
            window.location.href = '/meme/post/' + broadcastId;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        })
    },
    delete : function () {
        let postId = $('#postId').val();
        let broadcastId = $('#broadcastId').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/post/' + postId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert('밈이 삭제되었습니다.');
            window.location.href = '/meme/post/' + broadcastId;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    addGood : function () {
        $('#addGood').removeClass('btn-bd-outline-purple').addClass('btn-bd-purple');
        let postId = $('#postId').val();
        let memberId = $('#memberId').val();

        let data = {
            postId: postId,
            memberId: memberId
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/good',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    cancelGood : function () {
        $('#cancelGood').removeClass('btn-bd-purple').addClass('btn-bd-outline-purple');
        let postId = $('#postId').val();
        let memberId = $('#memberId').val();

        let data = {
            postId: postId,
            memberId: memberId
        };

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/good',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    isContent : function () {
        let meme = $('#meme').val();
        let summary = $('#summary').val();

        if (meme == null || meme == '') {
            alert('밈을 입력해주세요');
            return false;
        }
        if (summary == null || summary == '') {
            alert('간단한 설명을 입력해주세요');
            return false;
        }
        return true;
    }
}

$(document).ready(function () {
    $('#summernote').summernote({
        // 에디터 높이
        height: 500,
        // 에디터 한글 설정
        lang: "ko-KR",
        // 에디터에 커서 이동 (input창의 autofocus라고 생각)
        focus : true,
        toolbar: [
            // 글꼴 설정
            ['fontname', ['fontname']],
            // 글자 크기 설정
            ['fontsize', ['fontsize']],
            // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
            ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
            // 글자색
            ['color', ['forecolor','color']],
            // 표만들기
            ['table', ['table']],
            // 글머리 기호, 번호매기기, 문단정렬
            ['para', ['ul', 'ol', 'paragraph']],
            // 줄간격
            ['height', ['height']],
            // 첨부
            ['insert', ['link', 'picture', 'video']],
            // 코드보기, 확대해서보기, 도움말
            ['view', ['codeview','fullscreen', 'help']]
        ],
        // 추가한 글꼴
        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
        // 추가한 폰트사이즈
        fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
    });
});

post.init();

