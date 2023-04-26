let comment = {
    init : function () {
        let _this = this;
        $('#commentSave').on('click', function () {
            _this.save();
        });
    },
    save : function () {
        let comment = $('#comment').val();
        let postId = $('#postId').val();
        let memberId = $('#memberId').val();

        let data = {
            comment: comment,
            postId: postId,
            memberId: memberId
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/reply',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('댓글이 등록되었습니다.');
            window.location.href = '/meme/post-detail/' + postId;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}

function showComment(replyId) {
    $('#comment' + replyId).toggle();
}

function showCommentUpdate(replyId) {
    $('#commentUpdateForm' + replyId).toggle();
}

function commentSave2(replyId) {
    let comment = $('#comment' + replyId + 'Save').val();
    let postId = $('#postId').val();
    let memberId = $('#memberId').val();

    let data = {
        comment: comment,
        postId: postId,
        memberId: memberId,
        parentId: replyId
    };

    $.ajax({
        type: 'POST',
        url: '/api/v1/reply',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function () {
        alert('댓글이 등록되었습니다.');
        window.location.href = '/meme/post-detail/' + postId;
    }).fail(function (error) {
        alert(JSON.stringify(error));
    });
}

function deleteReply(replyId) {
    let postId = $('#postId').val();

    if (confirm('댓글을 삭제하시겠습니까?')) {
        $.ajax({
            type: 'PUT',
            url: '/api/v1/reply/' + replyId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            window.location.href = '/meme/post-detail/' + postId;
        }).fade(function (error) {
            alert(JSON.stringify(error));
        });
    }
}

comment.init();