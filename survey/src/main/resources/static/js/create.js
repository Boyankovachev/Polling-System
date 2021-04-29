var numQuestions = 1;

$(function (){
    $("#question-1").append($("#question-template").html());
    numQuestions = 1;

    $("#add-question").on("click", function(){
        numQuestions = numQuestions + 1;
        appendQuestion(numQuestions);
    })

    $("#questions").on("click", ".add-answer" ,function(){
        //Add answer
        appendAnswer($(this).attr('question-num'));
    })

    $("#create").on("click", function(){

        var questions = [];
        for(i=1; i<numQuestions+1; i++){

            answers = {}
            j = 1
            $('#answer-' + i).children('input').each(function () {
                answers["answer"+j] = this.value;
                //console.log(this.value);
                j++;
            });
            //console.log(answers);

            var imageName = saveImage($("#image-" + i).prop('files')[0]);

            temp = {
                questionText: $("#question-text-" + i).val(),
                isQuestionMandatory: $('option:selected', $("#is-mandatory-" + i)).val(),
                areMultipleAnswersPossible: $('option:selected', $("#multiple-answer-" + i)).val(),
                imageName: imageName,
                answers: answers
            }

            questions.push(temp);
        }

        var survey = {
            surveyName: $("#survey-name").val(),
            questions: questions
        }

        $.ajax({
            type: 'POST',
            url: '/create',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(survey),
            success:function(response){
                $("#survey").remove();
                $("#create").remove();
                $("#title").remove();
                appendResponse(response);
            }
        });

        //console.log(survey);
    })

});

function appendQuestion(num){
    $("#questions").append("<div id='question-" + num + "' class='question'></div>");
    var $div = $("#question-" + num);
    $div.append("<p>Question " + num + " text");
    $div.append("<input type='text' id='question-text-" + num + "'>");
    $div.append("<p>Is question mandatory?</p>");
    $div.append("<select id='is-mandatory-" + num + "'>"
     + "<option value='yes'>Yes</option>"
     + "<option value='no'>No</option>"
     + "</select>");
    $div.append("<p>Are multiple answers possible?</p>");
    $div.append("<select id='multiple-answer-" + num + "'>"
     + "<option value='no'>No</option>"
     + "<option value='yes'>Yes</option>"
     + "</select>");
    $div.append("<p>Select image (optional)</p>");
    $div.append("<input type='file' id='image-" + num + "' name='image'>");
    $div.append("<div id='answer-" + num + "'></div>");
    $div.append("<button class='add-answer' question-num='" + num + "'>Add answer</button>");
}

function appendAnswer(num){
    var $div = $("#answer-" + num);
    $div.append("<input type='text' placeholder='Answer'><br>")
}

function saveImage(file){
        if(typeof file === "undefined"){
            return "empty";
        }
        else{
            var result;
            var formData = new FormData();
            formData.append("image", file);
            $.ajax({
                async: false, // LOSHO!
                type: 'POST',
                url: '/uploadimage',
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                data: formData,
                success: function(response){
                    result = response.imageName;
                },
                error: function(e){
                    result = "empty";
                }
            });
            return result;
        }
}

function appendResponse(response){
    $message = $("#message");

    $message.append("<p>Survey created successfully!</p>");
    $message.append("<p>Your survey url is: survey/vote?surveyUrl=" + response.surveyUrl + "</p>");
    $message.append("<p>Your survey results url us: survey/results?resultsUrl=" + response.resultsUrl + "</p>");
    $message.append("<form action='/' method='get'>"
                    +    "<button type='submit'>Back</button>"
                    +"</form>");
}
