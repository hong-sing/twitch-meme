let index = {
    init : function () {
        let _this = this;
        let streamer;
        let meme;
        $('#searchButton').on('click', function () {
            streamer = $('#streamer').val();
            if (streamer.length < 1) {
                alert('스트리머 닉네임 또는 ID를 입력해주세요');
            } else {
                _this.searchStreamer();
            }
        });
        $("#streamer").keydown(function(event) {
            streamer = $('#streamer').val();
            if (event.which === 13) {
                if (streamer.length < 1) {
                    alert('스트리머 닉네임 또는 ID를 입력해주세요');
                } else {
                    event.preventDefault(); // 기본 이벤트 방지
                    _this.searchStreamer();
                }
            }
        });
        $('#searchMeme').on('click', function () {
            meme = $('#meme').val();
            if (meme.length < 1) {
                alert('밈을 입력해주세요');
            } else {
                _this.searchMeme();
            }
        });
        $("#meme").keydown(function(event) {
            meme = $('#meme').val();
            if (event.which === 13) {
                if (meme.length < 1) {
                    alert('밈을 입력해주세요');
                } else {
                    event.preventDefault(); // 기본 이벤트 방지
                    _this.searchMeme();
                }
            }
        });
    },
    searchStreamer : function () {
        let streamer = $('#streamer').val();

        $.ajax({
            type: 'GET',
            url: '/meme/channel-list/' + streamer,
        }).done(function () {
            window.location.href = '/meme/channel-list/' + streamer;
        }).fail(function (error) {
            alert(error);
        });
    },
    searchMeme : function () {
        let meme = $('#meme').val();

        $.ajax({
            type: 'GET',
            url: '/meme/post-meme/' + meme,
        }).done(function () {
            window.location.href = '/meme/post-meme/' + meme;
        }).fail(function (error) {
            alert(error);
        });
    }

}

index.init();