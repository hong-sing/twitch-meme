let index = {
    init : function () {
        let _this = this;
        let streamer;
        let meme;
        let streamerFollow;
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
        $('#searchStreamerFollow').on('click', function () {
            streamerFollow = $('#streamerFollow').val();
            if (streamerFollow.length < 1) {
                alert('스트리머 닉네임 또는 ID를 입력해주세요');
            } else {
                _this.searchStreamerFollow();
            }
        });
        $("#streamerFollow").keydown(function(event) {
            streamerFollow = $('#streamerFollow').val();
            if (event.which === 13) {
                if (streamerFollow.length < 1) {
                    alert('스트리머 닉네임 또는 ID를 입력해주세요');
                } else {
                    event.preventDefault(); // 기본 이벤트 방지
                    _this.searchStreamerFollow();
                }
            }
        });
        $('#withdrawal').on('click', function () {
            if (confirm('회원 탈퇴 하시겠습니까?')) {
                _this.withdrawal();
            }
        })
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
    },
    searchStreamerFollow : function () {
        let streamerFollow = $('#streamerFollow').val();

        $.ajax({
            type: 'GET',
            url: '/streamer/channel-list/' + streamerFollow,
        }).done(function () {
            window.location.href = '/streamer/channel-list/' + streamerFollow;
        }).fail(function (error) {
            alert(error);
        });
    },
    withdrawal : function () {
        let memberId = $('#memberId').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/member/' + memberId,
        }).done(function () {
            alert('회원 탈퇴 되었습니다. 계정 연결은 트위치 설정에서 직접 해제하셔야 합니다. \n 다시 로그인 시 재가입 됩니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(error);
        });
    }

}

index.init();