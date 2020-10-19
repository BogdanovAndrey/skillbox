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

//    //Loading tasks on load page
//    function getAll() {
//    $.get('/tasks/', function(response)
//    {
//        for(i in response) {
//            appendTask(response[i]);
//        }
//    });}


    //Show adding task form
    $('#show-add-task-form').click(function(){
        $('#add-task-form').css('display', 'flex');
        return false;
    });

    //Closing adding task form
    $('.close-form').click(function(event){

        $(this).parent().parent().css('display', 'none');
        return false;
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
                $('#add-task-form form').trigger("reset");
            }
        });
        return false;
    });
    //Deleting task
    $(document).on('click', '.delete-task', function()
    {
        var taskId = parseInt($(this).parent().attr("id").match(/\d+/));
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
        var taskId = parseInt($(this).parent().attr("id").match(/\d+/));
        var shortName = $(this).siblings("h3").text();
        var description = $(this).siblings("p").text();

        $('#change-task-form').css('display', 'flex');
        $("#change-task-form").find("input").val(shortName);
        $('#change-task-form').find('textarea').text(description);
        $('#change-task-form').find('p').text(taskId);
        return false;
});
    //Save changes
        $(document).on('click', '#change-task', function()
        {

        var data = $('#change-task-form form').serialize();
        var taskId = $('#change-task-form').find('p').text();
        $.ajax({
            method: "PUT",
            url: '/tasks/'+taskId,
            data: data,
            success: function(response){

                var taskId = response;

                $('#change-task-form').css('display', 'none');
                var data = $('#change-task-form form').serializeArray();
                var shortName = data[0].value;
                var description = data[1].value;
                $("#task"+taskId).find("h3").text("" + shortName);
                $("#task"+taskId).find("p").text(description.toString());
                $('#change-task-form form').trigger("reset");
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