let report = {
    init : function () {
        let _this = this;
        $('#report').on('click', function () {
            _this.save();
        })
    },
    save : function () {
        let reportReason = $('#reportReason').val();
        let postId = $('#postId').val();
        let memberId = $('#memberId').val();

        let data = {
            postId: postId,
            memberId: memberId,
            reportReason: reportReason
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/report',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("해당 게시글이 신고되었습니다.");
            window.location.href = '/meme/post-detail/' + postId;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}

report.init();