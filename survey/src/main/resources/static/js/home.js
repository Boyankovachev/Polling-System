$(function (){

    $(".survey-div").on("click", ".disable" ,function(){
        var survey = {
            surveyId: $(this).attr('surveyId')
        }
        sendRequest("/disablesurvey", survey);
    })

    $(".survey-div").on("click", ".enable" ,function(){
        var survey = {
            surveyId: $(this).attr('surveyId')
        }
        sendRequest("/enablesurvey", survey);
    })

});

function sendRequest(requestUrl, survey){
    $.ajax({
        type: 'POST',
        url: requestUrl,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(survey),
        success:function(response){
            if(response.type === "disable"){
                $("#" + response.surveyId).find(".disable").remove();
                $("#" + response.surveyId).append("<button class='enable' surveyId=" + response.surveyId + ">Enable Survey</button>");
            }
            else if(response.type === "enable"){
                $("#" + response.surveyId).find(".enable").remove();
                $("#" + response.surveyId).append("<button class='disable' surveyId=" + response.surveyId + ">Disable Survey</button>");
            }
        },
        error:function(error){
            alert("Could not change survey status!");
        }
    });
}