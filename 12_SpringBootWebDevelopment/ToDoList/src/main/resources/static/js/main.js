$(function(){

    const appendTask = function(data){
        var taskCode = '<h3 class="task-name" data-name="' +
            data.name + '">' + '<br>' + '<p class="task-description">' + data.description + '</p>';
        $('#task-list')
            .append('<div>' + bookCode + '</div>');
    };

    //Loading books on load page
//    $.get('/books/', function(response)
//    {
//        for(i in response) {
//            appendBook(response[i]);
//        }
//    });

    //Show adding book form
    $('#show-add-task-form').click(function(){
        $('#add-task-form').css('display', 'flex');
    });

    //Closing adding book form
    $('#add-task-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Getting book
    $(document).on('click', '.book-link', function(){
        var link = $(this);
        var bookId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/books/' + bookId,
            success: function(response)
            {
                var code = '<span>Год выпуска:' + response.year + '</span>';
                link.parent().append(code);
            },
            error: function(response)
            {
                if(response.status == 404) {
                    alert('Книга не найдена!');
                }
            }
        });
        return false;
    });

    //Adding book
    $('#save-task').click(function()
    {
        var data = $('add-task-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/books/',
            data: data,
            success: function(response)
            {
                $('#add-task-form').css('display', 'none');
                var task = {};
                task.id = response;
                var dataArray = $('#add-task-form form').serializeArray();
                for(i in dataArray) {
                    task[dataArray[i]['name']] = dataArray[i]['value'];
                }
                appendTask(task);
            }
        });
        return false;
    });

});