$(function (){
    $("#submit").on("click" ,function(){
        var $survey = $('#survey');
        //alert($survey.attr("surveyUrl"));

        var questions = [];
        $('.question').each(function () {
            //alert($(this).attr('questionId') + " " + $(this).attr('isMandatory'));

            answers = {}
            var i = 0;
            $(this).find('input').each(function () {
                //alert($(this).val());
                //alert(this.value);
                //console.log(this.value + " " +  $(this).is(":checked")  + "\n");
                if($(this).is(":checked")){
                    i++;
                    answers[this.value] = 1;
                }
                else{
                    answers[this.value] = 0;
                }
             });
             if( $(this).attr('isMandatory') === "true" && i === 0 ){
                alert("Please answer all mandatory questions!");
                return false;
             }

             temp = {
                questionId: $(this).attr('questionId'),
                answers: answers
             }
             questions.push(temp);

        });

        var survey = {
            surveyUrl: $survey.attr("surveyUrl"),
            questions: questions
        }
        //console.log(survey);
        $.ajax({
            type: 'POST',
            url: '/postresults',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(survey),
            success: function(response){
                $('#submit').remove();
                alert("Voting successful!");
            },
            error: function(error){
                alert("Voting failed!");
            }
        });

    })
});