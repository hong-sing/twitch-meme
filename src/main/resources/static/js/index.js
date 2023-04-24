let index = {
    init : function () {
        // console.log("a");
        let _this = this;
        let streamer;
        $('#searchButton').on('click', function () {
            streamer = $('#streamer').val();
            if (streamer.length < 1) {
                alert('스트리머 닉네임 또는 ID를 입력해주세요');
            } else {
                _this.search();
            }
            // console.log("z");
        });
        $("#streamer").keydown(function(event) {
            streamer = $('#streamer').val();
            if (event.which === 13) {
                if (streamer.length < 1) {
                    alert('스트리머 ID를 입력해주세요');
                } else {
                    event.preventDefault(); // 기본 이벤트 방지
                    _this.search();
                }
            }
        });
    },
    search : function () {
        let streamer = $('#streamer').val();

        // window.location.href = '/search/' + streamer;

        $.ajax({
            type: 'GET',
            url: '/meme/streamer/' + streamer,
        }).done(function () {
            window.location.href = '/meme/streamer/' + streamer;
        }).fail(function (error) {
            alert(error);
            // console.log(error);
        });
    }

}

index.init();