$(function(){

    const appendTask = function(data){
        var taskCode = '<div class="task-card" id="task' + data.id + '">' +
            '<input type="image" class="delete-task" id="delete' + data.id + '" src="img/close-img.png" alt="Кнопка «Удалить»">' +
            '<input type="image" class="change-task" id="change' + data.id + '" src="img/settings-logo.png" alt="Кнопка «Настройки»">' +
            '<h3>' + data.name + '</h3>' + '<hr>' + '<p>' + data.description + '</p>' +
            '</div><br>';
        $('#task-list')
            .append(taskCode);
    };

    //Loading tasks on load page
    function gatAll() {
    $.get('/tasks/', function(response)
    {
        for(i in response) {
            appendTask(response[i]);
        }
    });}

    function showForm(){
            $('#add-task-form').css('display', 'flex');
        }

    //Show adding task form
    $('#show-add-task-form').click(showForm());

    //Closing adding task form
    $('#add-task-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });


    //Adding task
    $('#save-task').click(function()
    {
        var data = $('#add-task-form form').serialize();

        $.ajax({
            method: "POST",
            url: '/tasks/',
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
                $('#add-task-form form').trigger("reset");;
            }
        });
        return false;
    });
    //Deleting task
    $(document).on('click', '.delete-task', function()
    {
        var taskId = parseInt($(this).attr("id").match(/\d+/));
        var divId = "#task" + taskId;
        $.ajax({
            method: "DELETE",
            url: '/tasks/'+taskId,
            //data: taskId,
            success: function(){
                $(divId).remove();
            }
        });
        return false;
    });
    //Changing task
    $(document).on('click', '.change-task', function()
    {
        showForm();
        $(#add-task-form )
        var taskId = parseInt($(this).attr("id").match(/\d+/));
        var divId = "#task" + taskId;
        $.ajax({
            method: "POST",
            url: '/tasks/'+taskId,
            data: taskId,
            success: function(){
                $(divId).remove();
            }
        });
        return false;
    });
    //Deleting all task
        $(document).on('click', '#delete-all-tasks', function()
        {

            $.ajax({
                method: "DELETE",
                url: '/tasks/',
                success: function(){
                    $("#task-list").empty();
                }
            });
            return false;
        });


});