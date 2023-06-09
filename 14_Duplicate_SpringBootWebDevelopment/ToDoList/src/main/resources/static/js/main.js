$(function(){

    const appendCase = function(data){
        var caseCode = '<a href = "#" class = "case-link" data-id="' +
        data.id + '">' + data.description + '</a><br>';
        $('#case-list')
            .append('<div>' + caseCode + '</div>');
    };


    //Show adding case form
    $('#show-add-case-form').click(function(){
        $('#case-form').css('display', 'flex');
    });

    //Closing adding case form
    $('#case-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Getting case
    $(document).on('click', '.case-link', function(){
        var link = $(this);
        var caseId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/cases/' + caseId,
            success: function(response)
            {
                var code = '<span>Номер дела:' + response.number + '</span>';
                link.parent().append(code);
            },
            error: function(response)
            {
                if(response.status == 404) {
                    alert('Дело не найдено!');
                }
            }
        });
        return false;
    });

    //Adding case
    $('#save-case').click(function()
    {
        var data = $('#case-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/cases/',
            data: data,
            success: function(response)
            {
                $('#case-form').css('display', 'none');
                var caseVariable = {};
                caseVariable.id = response;
                var dataArray = $('#case-form form').serializeArray();
                for(i in dataArray) {
                    caseVariable[dataArray[i]['description']] = dataArray[i]['value'];
                }
                appendCase(caseVariable);
            }
        });
        return false;
    });

});